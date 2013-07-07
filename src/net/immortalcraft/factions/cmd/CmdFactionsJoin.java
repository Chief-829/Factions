package net.immortalcraft.factions.cmd;

import net.immortalcraft.factions.Factions;
import net.immortalcraft.factions.Perm;
import net.immortalcraft.factions.cmd.arg.ARUPlayer;
import net.immortalcraft.factions.cmd.arg.ARFaction;
import net.immortalcraft.factions.cmd.req.ReqFactionsEnabled;
import net.immortalcraft.factions.entity.UPlayer;
import net.immortalcraft.factions.entity.Faction;
import net.immortalcraft.factions.entity.MConf;
import net.immortalcraft.factions.entity.UConf;
import net.immortalcraft.factions.event.FactionsEventMembershipChange;
import net.immortalcraft.factions.event.FactionsEventMembershipChange.MembershipChangeReason;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdFactionsJoin extends FCommand
{
	public CmdFactionsJoin()
	{
		this.addAliases("join");
		
		this.addRequiredArg("faction");
		this.addOptionalArg("player", "you");
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqHasPerm.get(Perm.JOIN.node));
	}
	
	@Override
	public void perform()
	{
		// Args
		Faction faction = this.arg(0, ARFaction.get(sender));
		if (faction == null) return;

		UPlayer uplayer = this.arg(1, ARUPlayer.getStartAny(sender), usender);
		if (uplayer == null) return;
		Faction uplayerFaction = uplayer.getFaction();
		
		boolean samePlayer = uplayer == usender;
		
		// Validate
		if (!samePlayer  && ! Perm.JOIN_OTHERS.has(sender, false))
		{
			msg("<b>You do not have permission to move other players into a faction.");
			return;
		}

		if (faction == uplayerFaction)
		{
			msg("<i>%s <i>%s already a member of %s<i>.", uplayer.describeTo(usender, true), (samePlayer ? "are" : "is"), faction.getName(usender));
			return;
		}

		if (UConf.get(faction).factionMemberLimit > 0 && faction.getUPlayers().size() >= UConf.get(faction).factionMemberLimit)
		{
			msg(" <b>!<white> The faction %s is at the limit of %d members, so %s cannot currently join.", faction.getName(usender), UConf.get(faction).factionMemberLimit, uplayer.describeTo(usender, false));
			return;
		}

		if (uplayerFaction.isNormal())
		{
			msg("<b>%s must leave %s current faction first.", uplayer.describeTo(usender, true), (samePlayer ? "your" : "their"));
			return;
		}

		if (!UConf.get(faction).canLeaveWithNegativePower && uplayer.getPower() < 0)
		{
			msg("<b>%s cannot join a faction with a negative power level.", uplayer.describeTo(usender, true));
			return;
		}

		if( ! (faction.isOpen() || faction.isInvited(uplayer) || usender.isUsingAdminMode() || Perm.JOIN_ANY.has(sender, false)))
		{
			msg("<i>This faction requires invitation.");
			if (samePlayer)
			{
				faction.msg("%s<i> tried to join your faction.", uplayer.describeTo(faction, true));
			}
			return;
		}

		// Event
		FactionsEventMembershipChange membershipChangeEvent = new FactionsEventMembershipChange(sender, usender, faction, MembershipChangeReason.JOIN);
		membershipChangeEvent.run();
		if (membershipChangeEvent.isCancelled()) return;
		
		// Inform
		if (!samePlayer)
		{
			uplayer.msg("<i>%s <i>moved you into the faction %s<i>.", usender.describeTo(uplayer, true), faction.getName(uplayer));
		}
		faction.msg("<i>%s <i>joined <lime>your faction<i>.", uplayer.describeTo(faction, true));
		usender.msg("<i>%s <i>successfully joined %s<i>.", uplayer.describeTo(usender, true), faction.getName(usender));
		
		// Apply
		uplayer.resetFactionData();
		uplayer.setFaction(faction);
	    
		faction.setInvited(uplayer, false);

		// Derplog
		if (MConf.get().logFactionJoin)
		{
			if (samePlayer)
				Factions.get().log("%s joined the faction %s.", uplayer.getName(), faction.getName());
			else
				Factions.get().log("%s moved the player %s into the faction %s.", usender.getName(), uplayer.getName(), faction.getName());
		}
	}
}
