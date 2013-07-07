package net.immortalcraft.factions.entity;

import net.immortalcraft.factions.ConfServer;
import net.immortalcraft.factions.Factions;
import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.store.Coll;
import com.massivecraft.mcore.store.MStore;

public class UConfColl extends Coll<UConf>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public UConfColl(String name)
	{
		super(name, UConf.class, MStore.getDb(ConfServer.dburi), Factions.get(), true, false);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void init()
	{
		super.init();
		
		this.get(MCore.INSTANCE);
	}
	
}
