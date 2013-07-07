package net.immortalcraft.factions.cmd;

import net.immortalcraft.factions.Perm;
import net.immortalcraft.factions.Rel;
import net.immortalcraft.factions.cmd.arg.ARUPlayer;
import net.immortalcraft.factions.cmd.req.ReqFactionsEnabled;
import net.immortalcraft.factions.entity.UPlayer;
import net.immortalcraft.factions.entity.Faction;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;


public class CmdFactionsBishop extends FCommand
{
  public CmdFactionsBishop()
	{
		this.addAliases("bishop");


		this.addRequiredArg("player");


		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqHasPerm.get(Perm.BISHOP.node));
	}


	@Override
	public void perform()
	{
		UPlayer you = this.arg(0, ARUPlayer.getStartAny(sender));
		if (you == null) return;


		boolean permAny = Perm.BISHOP_ANY.has(sender, false);
		Faction targetFaction = you.getFaction();


		if (targetFaction != usenderFaction && !permAny)
		{
			msg("%s<b> is not a member in your faction.", you.describeTo(usender, true));
			return;
		}


		if (usender != null && usender.getRole() != Rel.LEADER && !permAny)
		{
			msg("<b>You are not the faction leader.");
			return;
		}


		if (you == usender && !permAny)
		{
			msg("<b>The target player musn't be yourself.");
			return;
		}


		if (you.getRole() == Rel.LEADER)
		{
			msg("<b>The target player is a faction leader. Demote them first.");
			return;
		}


		if (you.getRole() == Rel.BISHOP)
		{
			// Revoke
			you.setRole(Rel.MEMBER);
			targetFaction.msg("%s<i> is no longer bishop in your faction.", you.describeTo(targetFaction, true));
			msg("<i>You have removed bishop status from %s<i>.", you.describeTo(usender, true));
		}
		else
		{
			// Give
			you.setRole(Rel.BISHOP);
			targetFaction.msg("%s<i> was promoted to bishop in your faction.", you.describeTo(targetFaction, true));
			msg("<i>You have promoted %s<i> to bishop.", you.describeTo(usender, true));
		}
	}


}
