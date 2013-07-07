package net.immortalcraft.factions.cmd;

import java.util.Set;

import net.immortalcraft.factions.FPerm;
import net.immortalcraft.factions.Factions;
import net.immortalcraft.factions.Perm;
import net.immortalcraft.factions.Rel;
import net.immortalcraft.factions.cmd.req.ReqFactionsEnabled;
import net.immortalcraft.factions.cmd.req.ReqHasFaction;
import net.immortalcraft.factions.cmd.req.ReqRoleIsAtLeast;
import net.immortalcraft.factions.entity.BoardColl;
import net.immortalcraft.factions.entity.BoardColls;
import net.immortalcraft.factions.entity.Faction;
import net.immortalcraft.factions.entity.FactionColls;
import net.immortalcraft.factions.entity.MConf;
import net.immortalcraft.factions.event.FactionsEventChunkChange;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.ps.PS;

public class CmdFactionsUnclaimall extends FCommand
{	
	public CmdFactionsUnclaimall()
	{
		this.addAliases("unclaimall");
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqHasPerm.get(Perm.UNCLAIM_ALL.node));
		this.addRequirements(ReqHasFaction.get());
		this.addRequirements(ReqRoleIsAtLeast.get(Rel.OFFICER));
	}
	
	@Override
	public void perform()
	{
		// Args
		Faction faction = usenderFaction;
		Faction newFaction = FactionColls.get().get(faction).getNone();
		
		// FPerm
		if (!FPerm.TERRITORY.has(usender, faction, true)) return;

		// Apply
		BoardColl boardColl = BoardColls.get().get(faction);
		Set<PS> chunks = boardColl.getChunks(faction);
		int countTotal = chunks.size();
		int countSuccess = 0;
		int countFail = 0;
		for (PS chunk : chunks)
		{
			FactionsEventChunkChange event = new FactionsEventChunkChange(sender, chunk, newFaction);
			event.run();
			if (event.isCancelled())
			{
				countFail++;
			}
			else
			{
				countSuccess++;
				boardColl.setFactionAt(chunk, newFaction);
			}
		}
		
		// Inform
		usenderFaction.msg("%s<i> unclaimed <h>%d <i>of your <h>%d <i>faction land. You now have <h>%d <i>land claimed.", usender.describeTo(usenderFaction, true), countSuccess, countTotal, countFail);

		// Log
		if (MConf.get().logLandUnclaims)
		{
			Factions.get().log(usender.getName()+" unclaimed everything for the faction: "+usenderFaction.getName());
		}
	}
	
}
