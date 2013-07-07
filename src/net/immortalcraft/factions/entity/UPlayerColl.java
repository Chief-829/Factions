package net.immortalcraft.factions.entity;

import net.immortalcraft.factions.ConfServer;
import net.immortalcraft.factions.Factions;
import net.immortalcraft.factions.Rel;
import com.massivecraft.mcore.mixin.Mixin;
import com.massivecraft.mcore.store.MStore;
import com.massivecraft.mcore.store.SenderColl;
import com.massivecraft.mcore.util.TimeUnit;
import com.massivecraft.mcore.util.Txt;

public class UPlayerColl extends SenderColl<UPlayer>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public UPlayerColl(String name)
	{
		super(name, UPlayer.class, MStore.getDb(ConfServer.dburi), Factions.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE: COLL
	// -------------------------------------------- //

	@Override
	protected synchronized String attach(UPlayer entity, Object oid, boolean noteChange)
	{
		String ret = super.attach(entity, oid, noteChange);
		
		// If inited ...
		if (!this.inited()) return ret;
		if (!Factions.get().isDatabaseInitialized()) return ret;
		
		// ... update the index.
		Faction faction = entity.getFaction();
		faction.uplayers.add(entity);
		
		return ret;
	}
	
	@Override
	public UPlayer detachId(Object oid)
	{
		UPlayer ret = this.get(oid);
		if (ret == null) return null;
		
		// If inited ...
		if (this.inited())
		{
			// ... update the index.
			Faction faction = ret.getFaction();
			faction.uplayers.remove(ret);
		}
		
		super.detachId(oid);
	
		return ret;
	}
	
	// -------------------------------------------- //
	// EXTRAS
	// -------------------------------------------- //
	
	public void clean()
	{
		FactionColl factionColl = FactionColls.get().get(this);
		String universe = this.getUniverse();
		for (UPlayer uplayer : this.getAll())
		{
			String factionId = uplayer.getFactionId();
			if (factionColl.containsId(factionId)) continue;
			
			uplayer.resetFactionData();
			
			String message = Txt.parse("<i>Reset data for <h>%s <i>in <h>%s <i>universe. Unknown factionId <h>%s", uplayer.getDisplayName(), universe, factionId);
			Factions.get().log(message);
		}
	}
	
	public void removePlayerDataAfterInactiveDaysRoutine()
	{
		if (MConf.get().removePlayerDataAfterInactiveDays <= 0.0) return;
		
		long now = System.currentTimeMillis();
		double toleranceMillis = MConf.get().removePlayerDataAfterInactiveDays * TimeUnit.MILLIS_PER_DAY;
		
		for (UPlayer uplayer : this.getAll())
		{
			Long lastPlayed = Mixin.getLastPlayed(uplayer.getId());
			if (lastPlayed == null) continue;
			
			if (uplayer.isOnline()) continue;
			if (now - lastPlayed <= toleranceMillis) continue;
			
			if (MConf.get().logFactionLeave || MConf.get().logFactionKick)
			{
				Factions.get().log("Player "+uplayer.getName()+" was auto-removed due to inactivity.");
			}

			// if player is faction leader, sort out the faction since he's going away
			if (uplayer.getRole() == Rel.LEADER)
			{
				Faction faction = uplayer.getFaction();
				if (faction != null)
				{
					uplayer.getFaction().promoteNewLeader();
				}
			}

			uplayer.leave();
			uplayer.detach();
		}
	}
}
