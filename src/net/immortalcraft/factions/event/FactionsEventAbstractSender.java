package net.immortalcraft.factions.event;

import org.bukkit.command.CommandSender;

import net.immortalcraft.factions.entity.UPlayer;
import com.massivecraft.mcore.event.MCoreEvent;

public abstract class FactionsEventAbstractSender extends MCoreEvent
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final CommandSender sender;
	public CommandSender getSender() { return this.sender; }
	public UPlayer getUSender() { return this.sender == null ? null : UPlayer.get(this.sender); }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public FactionsEventAbstractSender(CommandSender sender)
	{
		this.sender = sender;
	}
}
