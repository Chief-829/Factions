package net.immortalcraft.factions.cmd;

import net.immortalcraft.factions.Perm;
import net.immortalcraft.factions.cmd.arg.ARUPlayer;
import net.immortalcraft.factions.cmd.arg.ARFaction;
import net.immortalcraft.factions.cmd.req.ReqBankCommandsEnabled;
import net.immortalcraft.factions.cmd.req.ReqFactionsEnabled;
import net.immortalcraft.factions.entity.UPlayer;
import net.immortalcraft.factions.entity.Faction;
import net.immortalcraft.factions.entity.MConf;
import net.immortalcraft.factions.Factions;
import net.immortalcraft.factions.integration.Econ;
import com.massivecraft.mcore.cmd.arg.ARDouble;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.money.Money;
import com.massivecraft.mcore.util.Txt;

import org.bukkit.ChatColor;


public class CmdFactionsMoneyTransferPf extends FCommand
{
	public CmdFactionsMoneyTransferPf()
	{
		this.addAliases("pf");
		
		this.addRequiredArg("amount");
		this.addRequiredArg("player");
		this.addRequiredArg("faction");
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqHasPerm.get(Perm.MONEY_P2F.node));
		this.addRequirements(ReqBankCommandsEnabled.get());
	}
	
	@Override
	public void perform()
	{
		Double amount = this.arg(0, ARDouble.get());
		if (amount == null) return;
		
		UPlayer from = this.arg(1, ARUPlayer.getStartAny(sender));
		if (from == null) return;
		
		Faction to = this.arg(2, ARFaction.get(sender));
		if (to == null) return;
		
		boolean success = Econ.transferMoney(usender, from, to, amount);

		if (success && MConf.get().logMoneyTransactions)
		{
			Factions.get().log(ChatColor.stripColor(Txt.parse("%s transferred %s from the player \"%s\" to the faction \"%s\"", usender.getName(), Money.format(from, amount), from.describeTo(null), to.describeTo(null))));
		}
	}
}
