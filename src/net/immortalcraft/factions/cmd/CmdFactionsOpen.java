package net.immortalcraft.factions.cmd;

import net.immortalcraft.factions.Perm;
import net.immortalcraft.factions.Rel;
import net.immortalcraft.factions.cmd.req.ReqFactionsEnabled;
import net.immortalcraft.factions.cmd.req.ReqHasFaction;
import net.immortalcraft.factions.cmd.req.ReqRoleIsAtLeast;
import net.immortalcraft.factions.event.FactionsEventOpenChange;
import com.massivecraft.mcore.cmd.arg.ARBoolean;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdFactionsOpen extends FCommand
{
	public CmdFactionsOpen()
	{
		this.addAliases("open");
		
		this.addOptionalArg("yes/no", "toggle");
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqHasPerm.get(Perm.OPEN.node));
		this.addRequirements(ReqHasFaction.get());
		this.addRequirements(ReqRoleIsAtLeast.get(Rel.OFFICER));
	}
	
	@Override
	public void perform()
	{
		// Args
		Boolean newOpen = this.arg(0, ARBoolean.get(), !usenderFaction.isOpen());
		if (newOpen == null) return;

		// Event
		FactionsEventOpenChange event = new FactionsEventOpenChange(sender, usenderFaction, newOpen);
		event.run();
		if (event.isCancelled()) return;
		newOpen = event.isNewOpen();
		
		// Apply
		usenderFaction.setOpen(newOpen);
		
		// Inform
		String descTarget = usenderFaction.isOpen() ? "open" : "closed";
		usenderFaction.msg("%s<i> changed the faction to <h>%s<i>.", usender.describeTo(usenderFaction, true), descTarget);
	}
	
}
