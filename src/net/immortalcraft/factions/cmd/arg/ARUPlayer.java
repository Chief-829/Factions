package net.immortalcraft.factions.cmd.arg;

import net.immortalcraft.factions.entity.UPlayer;
import net.immortalcraft.factions.entity.UPlayerColls;
import com.massivecraft.mcore.cmd.arg.ARSenderEntity;
import com.massivecraft.mcore.cmd.arg.ArgReader;

public class ARUPlayer
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	public static ArgReader<UPlayer> getFullAny(Object o) { return ARSenderEntity.getFullAny(UPlayerColls.get().get(o)); }
	
	public static ArgReader<UPlayer> getStartAny(Object o) { return ARSenderEntity.getStartAny(UPlayerColls.get().get(o)); }
	
	public static ArgReader<UPlayer> getFullOnline(Object o) { return ARSenderEntity.getFullOnline(UPlayerColls.get().get(o)); }
	
	public static ArgReader<UPlayer> getStartOnline(Object o) { return ARSenderEntity.getStartOnline(UPlayerColls.get().get(o)); }
	
}
