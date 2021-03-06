package net.immortalcraft.factions.cmd;

import net.immortalcraft.factions.FPerm;
import net.immortalcraft.factions.Perm;
import net.immortalcraft.factions.cmd.arg.ARFaction;
import net.immortalcraft.factions.cmd.req.ReqFactionsEnabled;
import net.immortalcraft.factions.entity.Faction;
import net.immortalcraft.factions.entity.UConf;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;
import com.massivecraft.mcore.ps.PS;

public class CmdFactionsAutoClaim extends FCommand
{
	public CmdFactionsAutoClaim()
	{
		this.addAliases("autoclaim");
		
		this.addOptionalArg("faction", "you");
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqHasPerm.get(Perm.AUTOCLAIM.node));
		this.addRequirements(ReqIsPlayer.get());
	}

	@Override
	public void perform()
	{
		// Check disabled
		if (UConf.isDisabled(sender, sender)) return;
		
		// Args
		Faction forFaction = this.arg(0, ARFaction.get(usenderFaction), usenderFaction);
		
		if (forFaction == null || forFaction == usender.getAutoClaimFaction())
		{
			usender.setAutoClaimFaction(null);
			msg("<i>Auto-claiming of land disabled.");
			return;
		}
		
		// FPerm
		if (forFaction.isNormal() && !FPerm.TERRITORY.has(usender, forFaction, true)) return;
		
		usender.setAutoClaimFaction(forFaction);
		
		msg("<i>Now auto-claiming land for <h>%s<i>.", forFaction.describeTo(usender));
		usender.tryClaim(forFaction, PS.valueOf(me), true, true);
	}
	
}