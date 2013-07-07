package net.immortalcraft.factions.cmd;

import net.immortalcraft.factions.cmd.arg.ARFaction;
import net.immortalcraft.factions.cmd.req.ReqBankCommandsEnabled;
import net.immortalcraft.factions.cmd.req.ReqFactionsEnabled;
import net.immortalcraft.factions.entity.Faction;
import net.immortalcraft.factions.integration.Econ;
import net.immortalcraft.factions.Perm;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdFactionsMoneyBalance extends FCommand
{
	public CmdFactionsMoneyBalance()
	{
		this.addAliases("b", "balance");
		
		this.addOptionalArg("faction", "you");
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqHasPerm.get(Perm.MONEY_BALANCE.node));
		this.addRequirements(ReqBankCommandsEnabled.get());
	}
	
	@Override
	public void perform()
	{
		Faction faction = this.arg(0, ARFaction.get(sender), usenderFaction);
		if (faction == null) return;
			
		if (faction != usenderFaction && ! Perm.MONEY_BALANCE_ANY.has(sender, true)) return;
		
		Econ.sendBalanceInfo(usender, faction);
	}
	
}
