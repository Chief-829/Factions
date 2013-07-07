package net.immortalcraft.factions.chat.tag;

import org.bukkit.command.CommandSender;

import net.immortalcraft.factions.chat.ChatTagAbstract;
import net.immortalcraft.factions.entity.Faction;
import net.immortalcraft.factions.entity.UConf;
import net.immortalcraft.factions.entity.UPlayer;

public class ChatTagName extends ChatTagAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private ChatTagName() { super("factions_name"); }
	private static ChatTagName i = new ChatTagName();
	public static ChatTagName get() { return i; }
	
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
		if (faction.isNone()) return "";
		return faction.getName();
	}

}
