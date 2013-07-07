package net.immortalcraft.factions.cmd;

import net.immortalcraft.factions.Perm;
import net.immortalcraft.factions.Rel;
import net.immortalcraft.factions.cmd.arg.ARUPlayer;
import net.immortalcraft.factions.cmd.req.ReqFactionsEnabled;
import net.immortalcraft.factions.entity.UPlayer;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdFactionsDemote extends FCommand
{
	
	public CmdFactionsDemote()
	{
		this.addAliases("demote");
		
		this.addRequiredArg("player");
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqHasPerm.get(Perm.DEMOTE.node));
		
		//To demote someone from member -> recruit you must be an officer.
		//To demote someone from officer -> member you must be a leader.
		//We'll handle this internally
	}
	
	@Override
	public void perform()
	{	
		UPlayer you = this.arg(0, ARUPlayer.getStartAny(usender));
		if (you == null) return;
		
		if (you.getFaction() != usenderFaction)
		{
			msg("%s<b> is not a member in your faction.", you.describeTo(usender, true));
			return;
		}
		
		if (you == usender)
		{
			msg("<b>The target player mustn't be yourself.");
			return;
		}

		if (you.getRole() == Rel.MEMBER)
		{
			if (!usender.getRole().isAtLeast(Rel.OFFICER))
			{
				msg("<b>You must be an officer to demote a member to recruit.");
				return;
			}
			you.setRole(Rel.RECRUIT);
			usenderFaction.msg("%s<i> was demoted to being a recruit in your faction.", you.describeTo(usenderFaction, true));
		}
		else if (you.getRole() == Rel.OFFICER)
		{
			if (!usender.getRole().isAtLeast(Rel.BISHOP))
			{
				msg("<b>You must be a bishop to demote an officer to member.");
				return;
			}
			you.setRole(Rel.MEMBER);
			usenderFaction.msg("%s<i> was demoted to being a member in your faction.", you.describeTo(usenderFaction, true));
		}
		else if (you.getRole() == Rel.BISHOP)
		{
			if (!usender.getRole().isAtLeast(Rel.LEADER))
			{
				msg("<b>You must be the leader to demote a bishop to member.");
				return;
			}
			you.setRole(Rel.MEMBER);
			usenderFaction.msg("%s<i> was demoted to being a member in your faction.", you.describeTo(usenderFaction, true));
		}
	}
	
}
