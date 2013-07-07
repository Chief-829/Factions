package net.immortalcraft.factions.cmd;

import net.immortalcraft.factions.Perm;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;


public class CmdFactionsAccessView extends CmdFactionsAccessAbstract
{
	public CmdFactionsAccessView()
	{
		this.addAliases("v", "view");
		
		this.addRequirements(ReqHasPerm.get(Perm.ACCESS_VIEW.node));
	}

	@Override
	public void innerPerform()
	{
		this.sendAccessInfo();
	}
}
