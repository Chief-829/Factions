package net.immortalcraft.factions.event;

import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

import net.immortalcraft.factions.entity.BoardColls;
import net.immortalcraft.factions.entity.Faction;
import net.immortalcraft.factions.entity.UPlayer;
import com.massivecraft.mcore.ps.PS;

public class FactionsEventChunkChange extends FactionsEventAbstractSender
{	
	// -------------------------------------------- //
	// REQUIRED EVENT CODE
	// -------------------------------------------- //
	
	private static final HandlerList handlers = new HandlerList();
	@Override public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerList() { return handlers; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final PS chunk;
	public PS getChunk() { return this.chunk; }
	
	private final Faction newFaction;
	public Faction getNewFaction() { return this.newFaction; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public FactionsEventChunkChange(CommandSender sender, PS chunk, Faction newFaction)
	{
		super(sender);
		this.chunk = chunk.getChunk(true);
		this.newFaction = newFaction;
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public FactionsEventChunkChangeType getType()
	{
		Faction currentFaction = BoardColls.get().getFactionAt(chunk);
		
		if (currentFaction.isNone()) return FactionsEventChunkChangeType.BUY;
		if (newFaction.isNormal()) return FactionsEventChunkChangeType.CONQUER;
		
		UPlayer usender = this.getUSender();
		if (usender != null && usender.getFaction() == currentFaction) return FactionsEventChunkChangeType.SELL;
		
		return FactionsEventChunkChangeType.PILLAGE;
	}
	
}
