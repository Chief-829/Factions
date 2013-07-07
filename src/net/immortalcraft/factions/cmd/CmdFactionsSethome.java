package net.immortalcraft.factions.cmd;

import net.immortalcraft.factions.FPerm;
import net.immortalcraft.factions.Factions;
import net.immortalcraft.factions.Perm;
import net.immortalcraft.factions.cmd.arg.ARFaction;
import net.immortalcraft.factions.cmd.req.ReqFactionsEnabled;
import net.immortalcraft.factions.entity.Faction;
import net.immortalcraft.factions.entity.UConf;
import net.immortalcraft.factions.event.FactionsEventHomeChange;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;
import com.massivecraft.mcore.ps.PS;

public class CmdFactionsSethome extends FCommand
{
	public CmdFactionsSethome()
	{
		this.addAliases("sethome");
		
		this.addOptionalArg("faction", "you");
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqIsPlayer.get());
		this.addRequirements(ReqHasPerm.get(Perm.SETHOME.node));
	}
	
	@Override
	public void perform()
	{
		// Args
		Faction faction = this.arg(0, ARFaction.get(usenderFaction), usenderFaction);
		if (faction == null) return;
		
		PS newHome = PS.valueOf(me.getLocation());
		
		// Validate
		if ( ! UConf.get(faction).homesEnabled)
		{
			usender.msg("<b>Sorry, Faction homes are disabled on this server.");
			return;
		}
		
		// FPerm
		if ( ! FPerm.SETHOME.has(usender, faction, true)) return;
		
		// Verify
		if (!usender.isUsingAdminMode() && !faction.isValidHome(newHome))
		{
			usender.msg("<b>Sorry, your faction home can only be set inside your own claimed territory.");
			return;
		}
		
		// Event
		FactionsEventHomeChange event = new FactionsEventHomeChange(sender, faction, newHome);
		event.run();
		if (event.isCancelled()) return;
		newHome = event.getNewHome();

		// Apply
		faction.setHome(newHome);
		
		// Inform
		faction.msg("%s<i> set the home for your faction. You can now use:", usender.describeTo(usenderFaction, true));
		faction.sendMessage(Factions.get().getOuterCmdFactions().cmdFactionsHome.getUseageTemplate());
		if (faction != usenderFaction)
		{
			usender.msg("<b>You have set the home for the "+faction.getName(usender)+"<i> faction.");
		}
	}
	
}
