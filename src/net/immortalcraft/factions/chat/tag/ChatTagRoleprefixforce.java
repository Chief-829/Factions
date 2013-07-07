package net.immortalcraft.factions.chat.tag;

import org.bukkit.command.CommandSender;

import net.immortalcraft.factions.chat.ChatTagAbstract;
import net.immortalcraft.factions.entity.UConf;
import net.immortalcraft.factions.entity.UPlayer;

public class ChatTagRoleprefixforce extends ChatTagAbstract
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //
  
    private ChatTagRoleprefixforce() { super("factions_roleprefix"); }
    private static ChatTagRoleprefixforce i = new ChatTagRoleprefixforce();
    public static ChatTagRoleprefixforce get() { return i; }
  
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
    
        return usender.getRole().getPrefix();
    }

}