package net.immortalcraft.factions.cmd;

import net.immortalcraft.factions.FPerm;
import net.immortalcraft.factions.Perm;
import net.immortalcraft.factions.cmd.arg.ARUPlayer;
import net.immortalcraft.factions.entity.BoardColls;
import net.immortalcraft.factions.entity.UPlayer;
import com.massivecraft.mcore.cmd.arg.ARBoolean;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdFactionsAccessPlayer extends CmdFactionsAccessAbstract
{
	public CmdFactionsAccessPlayer()
	{
		this.addAliases("p", "player");
		
		this.addRequiredArg("player");
		this.addOptionalArg("yes/no", "toggle");
		
		this.addRequirements(ReqHasPerm.get(Perm.ACCESS_PLAYER.node));
	}

	@Override
	public void innerPerform()
	{
		// Args
		UPlayer uplayer = this.arg(0, ARUPlayer.getStartAny(usender));
		if (uplayer == null) return;
		
		Boolean newValue = this.arg(1, ARBoolean.get(), !ta.isPlayerIdGranted(uplayer.getId()));
		if (newValue == null) return;
		
		// FPerm
		if (!FPerm.ACCESS.has(usender, hostFaction, true)) return;
		
		// Apply
		ta = ta.withPlayerId(uplayer.getId(), newValue);
		BoardColls.get().setTerritoryAccessAt(chunk, ta);
		
		// Inform
		this.sendAccessInfo();
	}
}
