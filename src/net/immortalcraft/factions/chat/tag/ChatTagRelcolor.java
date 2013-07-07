package net.immortalcraft.factions.chat.tag;

import org.bukkit.command.CommandSender;

import net.immortalcraft.factions.chat.ChatTagAbstract;
import net.immortalcraft.factions.entity.UConf;
import net.immortalcraft.factions.entity.UPlayer;

public class ChatTagRelcolor extends ChatTagAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private ChatTagRelcolor() { super("factions_relcolor"); }
	private static ChatTagRelcolor i = new ChatTagRelcolor();
	public static ChatTagRelcolor get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public String getReplacement(CommandSender sender, CommandSender recipient)
	{
		if (recipient == null) return "";
		
		// Check disabled
		if (UConf.isDisabled(sender)) return "";

		// Get entities
		UPlayer usender = UPlayer.get(sender);
		UPlayer urecipient = UPlayer.get(recipient);
		
		return urecipient.getRelationTo(usender).getColor().toString();
	}

}
