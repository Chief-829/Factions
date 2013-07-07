package net.immortalcraft.factions.cmd;

import net.immortalcraft.factions.cmd.arg.ARFaction;
import net.immortalcraft.factions.cmd.req.ReqFactionsEnabled;
import net.immortalcraft.factions.entity.UPlayer;
import net.immortalcraft.factions.entity.UPlayerColls;
import net.immortalcraft.factions.entity.Faction;
import net.immortalcraft.factions.entity.FactionColls;
import net.immortalcraft.factions.entity.MConf;
import net.immortalcraft.factions.event.FactionsEventDisband;
import net.immortalcraft.factions.event.FactionsEventMembershipChange;
import net.immortalcraft.factions.event.FactionsEventMembershipChange.MembershipChangeReason;
import net.immortalcraft.factions.FFlag;
import net.immortalcraft.factions.FPerm;
import net.immortalcraft.factions.Factions;
import net.immortalcraft.factions.Perm;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdFactionsDisband extends FCommand
{
	public CmdFactionsDisband()
	{
		this.addAliases("disband");
		
		this.addOptionalArg("faction", "you");

		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqHasPerm.get(Perm.DISBAND.node));
	}
	
	@Override
	public void perform()
	{	
		// Args
		Faction faction = this.arg(0, ARFaction.get(usender), usenderFaction);
		if (faction == null) return;
		
		// FPerm
		if ( ! FPerm.DISBAND.has(usender, faction, true)) return;

		// Verify
		if (faction.getFlag(FFlag.PERMANENT))
		{
			msg("<i>This faction is designated as permanent, so you cannot disband it.");
			return;
		}

		// Event
		FactionsEventDisband event = new FactionsEventDisband(me, faction);
		event.run();
		if (event.isCancelled()) return;

		// Merged Apply and Inform
		
		// Run event for each player in the faction
		for (UPlayer uplayer : faction.getUPlayers())
		{
			FactionsEventMembershipChange membershipChangeEvent = new FactionsEventMembershipChange(sender, uplayer, FactionColls.get().get(faction).getNone(), MembershipChangeReason.DISBAND);
			membershipChangeEvent.run();
		}

		// Inform all players
		for (UPlayer uplayer : UPlayerColls.get().get(usender).getAllOnline())
		{
			String who = usender.describeTo(uplayer);
			if (uplayer.getFaction() == faction)
			{
				uplayer.msg("<h>%s<i> disbanded your faction.", who);
			}
			else
			{
				uplayer.msg("<h>%s<i> disbanded the faction %s.", who, faction.getName(uplayer));
			}
		}
		
		if (MConf.get().logFactionDisband)
		{
			Factions.get().log("The faction "+faction.getName()+" ("+faction.getId()+") was disbanded by "+(senderIsConsole ? "console command" : usender.getName())+".");
		}

		
		
		faction.detach();
	}
}
