package net.immortalcraft.factions.cmd;

import net.immortalcraft.factions.Factions;
import net.immortalcraft.factions.Perm;
import net.immortalcraft.factions.cmd.arg.ARFaction;
import net.immortalcraft.factions.cmd.req.ReqBankCommandsEnabled;
import net.immortalcraft.factions.cmd.req.ReqFactionsEnabled;
import net.immortalcraft.factions.entity.Faction;
import net.immortalcraft.factions.entity.MConf;
import net.immortalcraft.factions.integration.Econ;
import com.massivecraft.mcore.cmd.arg.ARDouble;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.money.Money;
import com.massivecraft.mcore.util.Txt;

import org.bukkit.ChatColor;


public class CmdFactionsMoneyDeposit extends FCommand
{
	public CmdFactionsMoneyDeposit()
	{
		this.addAliases("d", "deposit");
		
		this.addRequiredArg("amount");
		this.addOptionalArg("faction", "you");
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqHasPerm.get(Perm.MONEY_DEPOSIT.node));
		this.addRequirements(ReqBankCommandsEnabled.get());
	}
	
	@Override
	public void perform()
	{
		Double amount = this.arg(0, ARDouble.get());
		if (amount == null) return;
		
		Faction faction = this.arg(1, ARFaction.get(sender), usenderFaction);
		if (faction == null) return;
		
		boolean success = Econ.transferMoney(usender, usender, faction, amount);
		
		if (success && MConf.get().logMoneyTransactions)
		{
			Factions.get().log(ChatColor.stripColor(Txt.parse("%s deposited %s in the faction bank: %s", usender.getName(), Money.format(usender, amount), faction.describeTo(null))));
		}
	}
	
}
