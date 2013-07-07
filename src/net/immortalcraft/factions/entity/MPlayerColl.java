package net.immortalcraft.factions.entity;

import net.immortalcraft.factions.ConfServer;
import net.immortalcraft.factions.Const;
import net.immortalcraft.factions.Factions;
import com.massivecraft.mcore.store.MStore;
import com.massivecraft.mcore.store.SenderColl;

public class MPlayerColl extends SenderColl<MPlayer>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MPlayerColl i = new MPlayerColl();
	public static MPlayerColl get() { return i; }
	private MPlayerColl()
	{
		super(Const.COLLECTION_BASENAME_MPLAYER, MPlayer.class, MStore.getDb(ConfServer.dburi), Factions.get());
	}
	
}
