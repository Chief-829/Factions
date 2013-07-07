package net.immortalcraft.factions.cmd;

import org.bukkit.ChatColor;

import net.immortalcraft.factions.Perm;
import net.immortalcraft.factions.Rel;
import net.immortalcraft.factions.cmd.arg.ARUPlayer;
import net.immortalcraft.factions.cmd.req.ReqFactionsEnabled;
import net.immortalcraft.factions.cmd.req.ReqRoleIsAtLeast;
import net.immortalcraft.factions.entity.UPlayer;
import net.immortalcraft.factions.event.FactionsEventTitleChange;
import com.massivecraft.mcore.cmd.arg.ARString;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.util.Txt;

public class CmdFactionsTitle extends FCommand
{
	public CmdFactionsTitle()
	{
		this.addAliases("title");
		
		this.addRequiredArg("player");
		this.addOptionalArg("title", "");
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqHasPerm.get(Perm.TITLE.node));
		this.addRequirements(ReqRoleIsAtLeast.get(Rel.OFFICER));
	}
	
	@Override
	public void perform()
	{
		// Args
		UPlayer you = this.arg(0, ARUPlayer.getStartAny(sender));
		if (you == null) return;
		
		String newTitle = this.argConcatFrom(1, ARString.get(), "");
		if (newTitle == null) return;
		
		newTitle = Txt.parse(newTitle);
		if (!Perm.TITLE_COLOR.has(sender, false))
		{
			newTitle = ChatColor.stripColor(newTitle);
		}
		
		// Verify
		if ( ! canIAdministerYou(usender, you)) return;

		// Event
		FactionsEventTitleChange event = new FactionsEventTitleChange(sender, you, newTitle);
		event.run();
		if (event.isCancelled()) return;
		newTitle = event.getNewTitle();

		// Apply
		you.setTitle(newTitle);
		
		// Inform
		usenderFaction.msg("%s<i> changed a title: %s", usender.describeTo(usenderFaction, true), you.describeTo(usenderFaction, true));
	}
	
}
