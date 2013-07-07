package net.immortalcraft.factions.chat.modifier;

import org.bukkit.command.CommandSender;

import net.immortalcraft.factions.chat.ChatModifierAbstract;

public class ChatModifierUc extends ChatModifierAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private ChatModifierUc() { super("uc"); }
	private static ChatModifierUc i = new ChatModifierUc();
	public static ChatModifierUc get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public String getModified(String subject, CommandSender sender, CommandSender recipient)
	{
		return subject.toUpperCase();
	}

}
