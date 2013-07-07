package net.immortalcraft.factions.cmd;

import net.immortalcraft.factions.FPerm;
import net.immortalcraft.factions.Perm;
import net.immortalcraft.factions.cmd.arg.ARFaction;
import net.immortalcraft.factions.entity.BoardColls;
import net.immortalcraft.factions.entity.Faction;
import com.massivecraft.mcore.cmd.arg.ARBoolean;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdFactionsAccessFaction extends CmdFactionsAccessAbstract
{
	public CmdFactionsAccessFaction()
	{
		this.addAliases("f", "faction");
		
		this.addRequiredArg("faction");
		this.addOptionalArg("yes/no", "toggle");
		
		this.addRequirements(ReqHasPerm.get(Perm.ACCESS_FACTION.node));
	}

	@Override
	public void innerPerform()
	{
		// Args
		Faction faction = this.arg(0, ARFaction.get(usender));
		if (faction == null) return;
		
		Boolean newValue = this.arg(1, ARBoolean.get(), !ta.isFactionIdGranted(faction.getId()));
		if (newValue == null) return;
		
		// FPerm
		if (!FPerm.ACCESS.has(usender, hostFaction, true)) return;
		
		// Apply
		ta = ta.withFactionId(faction.getId(), newValue);
		BoardColls.get().setTerritoryAccessAt(chunk, ta);
		
		// Inform
		this.sendAccessInfo();
	}
}
