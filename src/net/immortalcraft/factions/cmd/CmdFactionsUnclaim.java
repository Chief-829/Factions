package net.immortalcraft.factions.cmd;

import net.immortalcraft.factions.cmd.req.ReqFactionsEnabled;
import net.immortalcraft.factions.cmd.req.ReqHasFaction;
import net.immortalcraft.factions.entity.Faction;
import net.immortalcraft.factions.entity.FactionColls;
import net.immortalcraft.factions.Perm;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;
import com.massivecraft.mcore.ps.PS;

public class CmdFactionsUnclaim extends FCommand
{
	public CmdFactionsUnclaim()
	{
		this.addAliases("unclaim");
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqHasPerm.get(Perm.UNCLAIM.node));
		this.addRequirements(ReqHasFaction.get());
		this.addRequirements(ReqIsPlayer.get());
	}
	
	@Override
	public void perform()
	{
		// Args
		PS chunk = PS.valueOf(me).getChunk(true);
		Faction newFaction = FactionColls.get().get(me).getNone();

		// Apply
		if (usender.tryClaim(newFaction, chunk, true, true)) return;
	}
	
}
