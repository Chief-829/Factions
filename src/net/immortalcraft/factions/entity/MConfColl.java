package net.immortalcraft.factions.entity;

import net.immortalcraft.factions.ConfServer;
import net.immortalcraft.factions.Const;
import net.immortalcraft.factions.Factions;
import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.store.Coll;
import com.massivecraft.mcore.store.MStore;

public class MConfColl extends Coll<MConf>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MConfColl i = new MConfColl();
	public static MConfColl get() { return i; }
	private MConfColl()
	{
		super(Const.COLLECTION_BASENAME_MCONF, MConf.class, MStore.getDb(ConfServer.dburi), Factions.get(), true, false);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void init()
	{
		super.init();
		
		MConf.i = this.get(MCore.INSTANCE);
	}
	
}
