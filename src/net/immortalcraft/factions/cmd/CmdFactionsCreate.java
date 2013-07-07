package net.immortalcraft.factions.cmd;

import java.util.ArrayList;

import net.immortalcraft.factions.Factions;
import net.immortalcraft.factions.Perm;
import net.immortalcraft.factions.Rel;
import net.immortalcraft.factions.cmd.req.ReqFactionsEnabled;
import net.immortalcraft.factions.cmd.req.ReqHasntFaction;
import net.immortalcraft.factions.entity.UPlayer;
import net.immortalcraft.factions.entity.UPlayerColls;
import net.immortalcraft.factions.entity.Faction;
import net.immortalcraft.factions.entity.FactionColl;
import net.immortalcraft.factions.entity.FactionColls;
import net.immortalcraft.factions.entity.MConf;
import net.immortalcraft.factions.event.FactionsEventCreate;
import net.immortalcraft.factions.event.FactionsEventMembershipChange;
import net.immortalcraft.factions.event.FactionsEventMembershipChange.MembershipChangeReason;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.store.MStore;

public class CmdFactionsCreate extends FCommand
{
	public CmdFactionsCreate()
	{
		this.addAliases("create");
		
		this.addRequiredArg("name");
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqHasntFaction.get());
		this.addRequirements(ReqHasPerm.get(Perm.CREATE.node));
	}
	
	@Override
	public void perform()
	{	
		// Args
		String newName = this.arg(0);
		
		// Verify
		FactionColl coll = FactionColls.get().get(usender);
		
		if (coll.isNameTaken(newName))
		{
			msg("<b>That name is already in use.");
			return;
		}
		
		ArrayList<String> nameValidationErrors = coll.validateName(newName);
		if (nameValidationErrors.size() > 0)
		{
			sendMessage(nameValidationErrors);
			return;
		}

		// Pre-Generate Id
		String factionId = MStore.createId();
		
		// Event
		FactionsEventCreate createEvent = new FactionsEventCreate(sender, coll.getUniverse(), factionId, newName);
		createEvent.run();
		if (createEvent.isCancelled()) return;
		
		// Apply
		Faction faction = coll.create(factionId);
		faction.setName(newName);
		
		usender.setRole(Rel.LEADER);
		usender.setFaction(faction);
		
		FactionsEventMembershipChange joinEvent = new FactionsEventMembershipChange(sender, usender, faction, MembershipChangeReason.CREATE);
		joinEvent.run();
		// NOTE: join event cannot be cancelled or you'll have an empty faction
		
		// Inform
		for (UPlayer follower : UPlayerColls.get().get(usender).getAllOnline())
		{
			follower.msg("%s<i> created a new faction %s", usender.describeTo(follower, true), faction.getName(follower));
		}
		
		msg("<i>You should now: %s", Factions.get().getOuterCmdFactions().cmdFactionsDescription.getUseageTemplate());

		if (MConf.get().logFactionCreate)
		{
			Factions.get().log(usender.getName()+" created a new faction: "+newName);
		}
	}
	
}
