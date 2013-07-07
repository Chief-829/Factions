package net.immortalcraft.factions.chat.tag;

import org.bukkit.command.CommandSender;

import net.immortalcraft.factions.chat.ChatTagAbstract;
import net.immortalcraft.factions.entity.Faction;
import net.immortalcraft.factions.entity.UConf;
import net.immortalcraft.factions.entity.UPlayer;

public class ChatTagNameforce extends ChatTagAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private ChatTagNameforce() { super("factions_nameforce"); }
	private static ChatTagNameforce i = new ChatTagNameforce();
	public static ChatTagNameforce get() { return i; }
	
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
		
		Faction faction = usender.getFaction();
		return faction.getName();
	}

}
