package net.immortalcraft.factions.mixin;

import net.immortalcraft.factions.entity.UPlayer;

public interface PowerMixin
{
	public double getMaxUniversal(UPlayer uplayer);
	public double getMax(UPlayer uplayer);
	public double getMin(UPlayer uplayer);
	public double getPerHour(UPlayer uplayer);
	public double getPerDeath(UPlayer uplayer);
}
