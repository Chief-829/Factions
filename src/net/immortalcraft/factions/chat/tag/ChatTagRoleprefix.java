package net.immortalcraft.factions.chat.tag;

import org.bukkit.command.CommandSender;

import net.immortalcraft.factions.chat.ChatTagAbstract;
import net.immortalcraft.factions.entity.Faction;
import net.immortalcraft.factions.entity.UConf;
import net.immortalcraft.factions.entity.UPlayer;

public class ChatTagRoleprefix extends ChatTagAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private ChatTagRoleprefix() { super("factions_roleprefix"); }
	private static ChatTagRoleprefix i = new ChatTagRoleprefix();
	public static ChatTagRoleprefix get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public String getReplacement(CommandSender sender, CommandSender recipient)
	{
		// Check disabled
		if (UConf.isDisabled(sender)) return "";
		
		// Get entities
		UPlayer usender = UPlayer.get(sender);
                
                // No "force"
                Faction faction = usender.getFaction();
                if (faction.isNone()) return "";
		
		return usender.getRole().getPrefix();
	}

}
