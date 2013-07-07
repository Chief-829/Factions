package net.immortalcraft.factions.cmd;

import net.immortalcraft.factions.Perm;
import net.immortalcraft.factions.cmd.req.ReqFactionsEnabled;
import net.immortalcraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdFactionsLeave extends FCommand {
	
	public CmdFactionsLeave()
	{
		this.addAliases("leave");
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqHasPerm.get(Perm.LEAVE.node));
		this.addRequirements(ReqHasFaction.get());
	}
	
	@Override
	public void perform()
	{
		usender.leave();
	}
	
}
