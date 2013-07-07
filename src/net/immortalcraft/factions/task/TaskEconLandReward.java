package net.immortalcraft.factions.task;

import net.immortalcraft.factions.entity.FactionColl;
import net.immortalcraft.factions.entity.FactionColls;
import net.immortalcraft.factions.entity.MConf;
import net.immortalcraft.factions.entity.UConf;
import com.massivecraft.mcore.ModuloRepeatTask;
import com.massivecraft.mcore.util.TimeUnit;

public class TaskEconLandReward extends ModuloRepeatTask
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TaskEconLandReward i = new TaskEconLandReward();
	public static TaskEconLandReward get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE: MODULO REPEAT TASK
	// -------------------------------------------- //
	
	@Override
	public long getDelayMillis()
	{
		return (long) (MConf.get().taskEconLandRewardMinutes * TimeUnit.MILLIS_PER_MINUTE);
	}
	
	@Override
	public void setDelayMillis(long delayMillis)
	{
		MConf.get().taskEconLandRewardMinutes = delayMillis / (double) TimeUnit.MILLIS_PER_MINUTE;
	}
	
	@Override
	public void invoke(long now)
	{
		for (FactionColl coll : FactionColls.get().getColls())
		{
			// Check disabled
			if (UConf.isDisabled(coll)) continue;
						
			coll.econLandRewardRoutine();
		}
	}
	
}
