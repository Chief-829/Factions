package net.immortalcraft.factions.cmd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.immortalcraft.factions.RelationParticipator;
import net.immortalcraft.factions.TerritoryAccess;
import net.immortalcraft.factions.cmd.req.ReqFactionsEnabled;
import net.immortalcraft.factions.entity.BoardColls;
import net.immortalcraft.factions.entity.Faction;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;
import com.massivecraft.mcore.ps.PS;
import com.massivecraft.mcore.ps.PSFormatHumanSpace;
import com.massivecraft.mcore.util.Txt;


public abstract class CmdFactionsAccessAbstract extends FCommand
{
	public PS chunk;
	public TerritoryAccess ta;
	public Faction hostFaction;
	
	public CmdFactionsAccessAbstract()
	{
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqIsPlayer.get());
	}
	
	@Override
	public void perform()
	{
		chunk = PS.valueOf(me).getChunk(true);
		ta = BoardColls.get().getTerritoryAccessAt(chunk);
		hostFaction = ta.getHostFaction(usender);
		
		this.innerPerform();
	}
	
	public abstract void innerPerform();

	public void sendAccessInfo()
	{
		sendMessage(Txt.titleize("Access at " + chunk.toString(PSFormatHumanSpace.get())));
		msg("<k>Host Faction: %s", hostFaction.describeTo(usender, true));
		msg("<k>Host Faction Allowed: %s", ta.isHostFactionAllowed() ? Txt.parse("<lime>TRUE") : Txt.parse("<rose>FALSE"));
		msg("<k>Granted Players: %s", describeRelationParticipators(ta.getGrantedUPlayers(usender), usender));
		msg("<k>Granted Factions: %s", describeRelationParticipators(ta.getGrantedFactions(usender), usender));
	}
	
	public static String describeRelationParticipators(Collection<? extends RelationParticipator> relationParticipators, RelationParticipator observer)
	{
		if (relationParticipators.size() == 0) return Txt.parse("<silver><em>none");
		List<String> descriptions = new ArrayList<String>();
		for (RelationParticipator relationParticipator : relationParticipators)
		{
			descriptions.add(relationParticipator.describeTo(observer));
		}
		return Txt.implodeCommaAnd(descriptions, Txt.parse("<i>, "), Txt.parse(" <i>and "));
	}
}
