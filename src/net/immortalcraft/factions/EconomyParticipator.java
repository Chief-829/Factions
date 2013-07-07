package net.immortalcraft.factions;


public interface EconomyParticipator extends RelationParticipator
{
	public boolean msg(String msg, Object... args);
}