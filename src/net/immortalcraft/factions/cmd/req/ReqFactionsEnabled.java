package net.immortalcraft.factions.cmd.req;

import org.bukkit.command.CommandSender;

import net.immortalcraft.factions.entity.UConf;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.req.ReqAbstract;

public class ReqFactionsEnabled extends ReqAbstract
{
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ReqFactionsEnabled i = new ReqFactionsEnabled();
	public static ReqFactionsEnabled get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean apply(CommandSender sender, MCommand command)
	{
		return !UConf.isDisabled(sender);
	}
	
	@Override
	public String createErrorMessage(CommandSender sender, MCommand command)
	{
		return UConf.getDisabledMessage(sender);
	}
	
}
