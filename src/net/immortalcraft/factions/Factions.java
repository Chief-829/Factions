package net.immortalcraft.factions;

import net.immortalcraft.factions.cmd.CmdFactions;
import net.immortalcraft.factions.adapter.BoardAdapter;
import net.immortalcraft.factions.adapter.BoardMapAdapter;
import net.immortalcraft.factions.adapter.FFlagAdapter;
import net.immortalcraft.factions.adapter.FPermAdapter;
import net.immortalcraft.factions.adapter.FactionPreprocessAdapter;
import net.immortalcraft.factions.adapter.RelAdapter;
import net.immortalcraft.factions.adapter.TerritoryAccessAdapter;
import net.immortalcraft.factions.chat.modifier.ChatModifierLc;
import net.immortalcraft.factions.chat.modifier.ChatModifierLp;
import net.immortalcraft.factions.chat.modifier.ChatModifierParse;
import net.immortalcraft.factions.chat.modifier.ChatModifierRp;
import net.immortalcraft.factions.chat.modifier.ChatModifierUc;
import net.immortalcraft.factions.chat.modifier.ChatModifierUcf;
import net.immortalcraft.factions.chat.tag.ChatTagRelcolor;
import net.immortalcraft.factions.chat.tag.ChatTagRole;
import net.immortalcraft.factions.chat.tag.ChatTagRoleprefix;
import net.immortalcraft.factions.chat.tag.ChatTagRoleprefixforce;
import net.immortalcraft.factions.chat.tag.ChatTagName;
import net.immortalcraft.factions.chat.tag.ChatTagNameforce;
import net.immortalcraft.factions.chat.tag.ChatTagTitle;
import net.immortalcraft.factions.entity.Board;
import net.immortalcraft.factions.entity.BoardColls;
import net.immortalcraft.factions.entity.Faction;
import net.immortalcraft.factions.entity.MPlayerColl;
import net.immortalcraft.factions.entity.UConfColls;
import net.immortalcraft.factions.entity.UPlayerColls;
import net.immortalcraft.factions.entity.FactionColls;
import net.immortalcraft.factions.entity.MConfColl;
import net.immortalcraft.factions.integration.herochat.HerochatFeatures;
import net.immortalcraft.factions.listeners.FactionsListenerChat;
import net.immortalcraft.factions.listeners.FactionsListenerEcon;
import net.immortalcraft.factions.listeners.FactionsListenerExploit;
import net.immortalcraft.factions.listeners.FactionsListenerMain;
import net.immortalcraft.factions.mixin.PowerMixin;
import net.immortalcraft.factions.mixin.PowerMixinDefault;
import net.immortalcraft.factions.task.TaskPlayerDataRemove;
import net.immortalcraft.factions.task.TaskEconLandReward;
import net.immortalcraft.factions.task.TaskPlayerPowerUpdate;

import com.massivecraft.mcore.Aspect;
import com.massivecraft.mcore.AspectColl;
import com.massivecraft.mcore.MPlugin;
import com.massivecraft.mcore.Multiverse;
import com.massivecraft.mcore.util.MUtil;
import com.massivecraft.mcore.xlib.gson.Gson;
import com.massivecraft.mcore.xlib.gson.GsonBuilder;


public class Factions extends MPlugin
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static Factions i;
	public static Factions get() { return i; }
	public Factions() { Factions.i = this; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// Commands
	private CmdFactions outerCmdFactions;
	public CmdFactions getOuterCmdFactions() { return this.outerCmdFactions; }
	
	// Aspects
	private Aspect aspect;
	public Aspect getAspect() { return this.aspect; }
	public Multiverse getMultiverse() { return this.getAspect().getMultiverse(); }
	
	// Database Initialized
	private boolean databaseInitialized;
	public boolean isDatabaseInitialized() { return this.databaseInitialized; }
	
	// Mixins
	private PowerMixin powerMixin = null;
	public PowerMixin getPowerMixin() { return this.powerMixin == null ? PowerMixinDefault.get() : this.powerMixin; }
	public void setPowerMixin(PowerMixin powerMixin) { this.powerMixin = powerMixin; }
	
	// Gson without preprocessors
	public final Gson gsonWithoutPreprocessors = this.getGsonBuilderWithotPreprocessors().create();

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void onEnable()
	{
		if ( ! preEnable()) return;
		
		// Initialize Aspects
		this.aspect = AspectColl.get().get(Const.ASPECT, true);
		this.aspect.register();
		this.aspect.setDesc(
			"<i>If the factions system even is enabled and how it's configured.",
			"<i>What factions exists and what players belong to them."
		);
		
		// Register Faction accountId Extractor
		// TODO: Perhaps this should be placed in the econ integration somewhere?
		MUtil.registerExtractor(String.class, "accountId", ExtractorFactionAccountId.get());

		// Initialize Database
		this.databaseInitialized = false;
		MConfColl.get().init();
		MPlayerColl.get().init();
		UConfColls.get().init();
		UPlayerColls.get().init();
		FactionColls.get().init();
		BoardColls.get().init();
		FactionColls.get().reindexUPlayers();
		this.databaseInitialized = true;
		
		// Commands
		this.outerCmdFactions = new CmdFactions();
		this.outerCmdFactions.register();

		// Setup Listeners
		FactionsListenerMain.get().setup();
		FactionsListenerChat.get().setup();
		FactionsListenerExploit.get().setup();
		
		// TODO: This listener is a work in progress.
		// The goal is that the Econ integration should be completely based on listening to our own events.
		// Right now only a few situations are handled through this listener.
		FactionsListenerEcon.get().setup();
		
		// Integrate
		this.integrate(
			HerochatFeatures.get()
		);
		
		// Schedule recurring non-tps-dependent tasks
		TaskPlayerPowerUpdate.get().schedule(this);
		TaskPlayerDataRemove.get().schedule(this);
		TaskEconLandReward.get().schedule(this);
		
		// Register built in chat modifiers
		ChatModifierLc.get().register();
		ChatModifierLp.get().register();
		ChatModifierParse.get().register();
		ChatModifierRp.get().register();
		ChatModifierUc.get().register();
		ChatModifierUcf.get().register();
		
		// Register built in chat tags
		ChatTagRelcolor.get().register();
		ChatTagRole.get().register();
		ChatTagRoleprefix.get().register();
                ChatTagRoleprefixforce.get().register();
		ChatTagName.get().register();
		ChatTagNameforce.get().register();
		ChatTagTitle.get().register();
		
		postEnable();
	}
	
	public GsonBuilder getGsonBuilderWithotPreprocessors()
	{
		return super.getGsonBuilder()
		.registerTypeAdapter(TerritoryAccess.class, TerritoryAccessAdapter.get())
		.registerTypeAdapter(Board.class, BoardAdapter.get())
		.registerTypeAdapter(Board.MAP_TYPE, BoardMapAdapter.get())
		.registerTypeAdapter(Rel.class, RelAdapter.get())
		.registerTypeAdapter(FPerm.class, FPermAdapter.get())
		.registerTypeAdapter(FFlag.class, FFlagAdapter.get())
		;
	}
	
	@Override
	public GsonBuilder getGsonBuilder()
	{
		return this.getGsonBuilderWithotPreprocessors()
		.registerTypeAdapter(Faction.class, FactionPreprocessAdapter.get())
		;
	}
	
}
