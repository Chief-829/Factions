package net.immortalcraft.factions.cmd;

import net.immortalcraft.factions.Perm;
import net.immortalcraft.factions.Rel;
import net.immortalcraft.factions.cmd.req.ReqFactionsEnabled;
import net.immortalcraft.factions.cmd.req.ReqHasFaction;
import net.immortalcraft.factions.cmd.req.ReqRoleIsAtLeast;
import net.immortalcraft.factions.event.FactionsEventDescriptionChange;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.mixin.Mixin;

public class CmdFactionsDescription extends FCommand
{
	public CmdFactionsDescription()
	{
		this.addAliases("desc");
		
		this.addRequiredArg("desc");
		this.setErrorOnToManyArgs(false);
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqHasPerm.get(Perm.DESCRIPTION.node));
		this.addRequirements(ReqHasFaction.get());
		this.addRequirements(ReqRoleIsAtLeast.get(Rel.OFFICER));
	}
	
	@Override
	public void perform()
	{	
		// Args
		String newDescription = this.argConcatFrom(0);
		
		// Event
		FactionsEventDescriptionChange event = new FactionsEventDescriptionChange(sender, usenderFaction, newDescription);
		event.run();
		if (event.isCancelled()) return;
		newDescription = event.getNewDescription();

		// Apply
		usenderFaction.setDescription(newDescription);
		
		// Inform
		usenderFaction.msg("<i>%s <i>set your faction description to:\n%s", Mixin.getDisplayName(sender), usenderFaction.getDescription());
	}
	
}
