package net.immortalcraft.factions.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import net.immortalcraft.factions.Factions;
import net.immortalcraft.factions.entity.Faction;
import net.immortalcraft.factions.entity.UConf;
import net.immortalcraft.factions.entity.UPlayer;
import net.immortalcraft.factions.event.FactionsEventAbstractSender;
import net.immortalcraft.factions.event.FactionsEventChunkChange;
import net.immortalcraft.factions.event.FactionsEventChunkChangeType;
import net.immortalcraft.factions.event.FactionsEventCreate;
import net.immortalcraft.factions.event.FactionsEventDescriptionChange;
import net.immortalcraft.factions.event.FactionsEventDisband;
import net.immortalcraft.factions.event.FactionsEventHomeChange;
import net.immortalcraft.factions.event.FactionsEventHomeTeleport;
import net.immortalcraft.factions.event.FactionsEventInvitedChange;
import net.immortalcraft.factions.event.FactionsEventMembershipChange;
import net.immortalcraft.factions.event.FactionsEventMembershipChange.MembershipChangeReason;
import net.immortalcraft.factions.event.FactionsEventOpenChange;
import net.immortalcraft.factions.event.FactionsEventRelationChange;
import net.immortalcraft.factions.event.FactionsEventNameChange;
import net.immortalcraft.factions.event.FactionsEventTitleChange;
import net.immortalcraft.factions.integration.Econ;
import com.massivecraft.mcore.money.Money;

public class FactionsListenerEcon implements Listener
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static FactionsListenerEcon i = new FactionsListenerEcon();
	public static FactionsListenerEcon get() { return i; }
	public FactionsListenerEcon() {}
	
	// -------------------------------------------- //
	// SETUP
	// -------------------------------------------- //
	
	public void setup()
	{
		Bukkit.getPluginManager().registerEvents(this, Factions.get());
	}

	// -------------------------------------------- //
	// TAKE ON LEAVE
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void takeOnLeave(FactionsEventMembershipChange event)
	{
		// If a player is leaving the faction ...
		if (event.getReason() != MembershipChangeReason.LEAVE) return;
		
		// ... and that player was the last one in the faction ...
		UPlayer uplayer = event.getUPlayer();
		Faction oldFaction = uplayer.getFaction();
		if (oldFaction.getUPlayers().size() > 1) return;
		
		// ... then transfer all money to the player. 
		Econ.transferMoney(uplayer, oldFaction, uplayer, Money.get(oldFaction));
	}
	
	// -------------------------------------------- //
	// TAKE ON DISBAND
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void takeOnDisband(FactionsEventDisband event)
	{
		// If there is a usender ...
		UPlayer usender = event.getUSender();
		if (usender == null) return;
		
		// ... and economy is enabled ...
		if (!Econ.isEnabled(usender)) return;
		
		// ... then transfer all the faction money to the sender.
		Faction faction = event.getFaction();
	
		double amount = Money.get(faction);
		String amountString = Money.format(faction, amount);
		
		Econ.transferMoney(usender, faction, usender, amount, true);
		
		usender.msg("<i>You have been given the disbanded faction's bank, totaling %s.", amountString);
		Factions.get().log(usender.getName() + " has been given bank holdings of "+amountString+" from disbanding "+faction.getName()+".");
	}
	
	// -------------------------------------------- //
	// PAY FOR ACTION
	// -------------------------------------------- //
	
	public static void payForAction(FactionsEventAbstractSender event, Double cost, String desc)
	{
		// If there is a sender ...
		UPlayer usender = event.getUSender();
		if (usender == null) return;
		
		// ... and there is a cost ...
		if (cost == null) return;
		if (cost == 0) return;
		
		// ... that the sender can't afford ...
		if (Econ.payForAction(cost, usender, desc)) return;
		
		// ... then cancel.
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void payForAction(FactionsEventChunkChange event)
	{
		Faction newFaction = event.getNewFaction();
		UConf uconf = UConf.get(newFaction);
		FactionsEventChunkChangeType type = event.getType();
		Double cost = uconf.econChunkCost.get(type);
		
		String desc = type.toString().toLowerCase() + " this land";
		
		payForAction(event, cost, desc);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void payForAction(FactionsEventMembershipChange event)
	{
		Double cost = null;		
		String desc = null;
		
		UConf uconf = UConf.get(event.getSender());
		if (uconf == null) return;
		
		if (event.getReason() == MembershipChangeReason.JOIN)
		{
			cost = uconf.econCostJoin;
			desc = "join a faction";
		}
		else if (event.getReason() == MembershipChangeReason.LEAVE)
		{
			cost = uconf.econCostLeave;
			desc = "leave a faction";
		}
		else if (event.getReason() == MembershipChangeReason.KICK)
		{
			cost = uconf.econCostKick;
			desc = "kick someone from a faction";
		}
		else
		{
			return;
		}
		
		payForAction(event, cost, desc);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void payForCommand(FactionsEventRelationChange event)
	{
		Double cost = UConf.get(event.getSender()).econRelCost.get(event.getNewRelation());
		String desc = Factions.get().getOuterCmdFactions().cmdFactionsRelationNeutral.getDesc();
		
		payForAction(event, cost, desc);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void payForCommand(FactionsEventHomeChange event)
	{
		Double cost = UConf.get(event.getSender()).econCostSethome;
		String desc = Factions.get().getOuterCmdFactions().cmdFactionsSethome.getDesc();
		
		payForAction(event, cost, desc);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void payForCommand(FactionsEventCreate event)
	{
		Double cost = UConf.get(event.getSender()).econCostCreate;
		String desc = Factions.get().getOuterCmdFactions().cmdFactionsCreate.getDesc();
		
		payForAction(event, cost, desc);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void payForCommand(FactionsEventDescriptionChange event)
	{
		Double cost = UConf.get(event.getSender()).econCostDescription;
		String desc = Factions.get().getOuterCmdFactions().cmdFactionsDescription.getDesc();
		
		payForAction(event, cost, desc);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void payForCommand(FactionsEventNameChange event)
	{
		Double cost = UConf.get(event.getSender()).econCostName;
		String desc = Factions.get().getOuterCmdFactions().cmdFactionsName.getDesc();
		
		payForAction(event, cost, desc);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void payForCommand(FactionsEventTitleChange event)
	{
		Double cost = UConf.get(event.getSender()).econCostTitle;
		String desc = Factions.get().getOuterCmdFactions().cmdFactionsTitle.getDesc();
		
		payForAction(event, cost, desc);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void payForCommand(FactionsEventOpenChange event)
	{
		Double cost = UConf.get(event.getSender()).econCostOpen;
		String desc = Factions.get().getOuterCmdFactions().cmdFactionsOpen.getDesc();
		
		payForAction(event, cost, desc);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void payForCommand(FactionsEventInvitedChange event)
	{
		Double cost = event.isNewInvited() ? UConf.get(event.getSender()).econCostInvite : UConf.get(event.getSender()).econCostDeinvite;
		String desc = Factions.get().getOuterCmdFactions().cmdFactionsInvite.getDesc();
		
		payForAction(event, cost, desc);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void payForCommand(FactionsEventHomeTeleport event)
	{
		Double cost = UConf.get(event.getSender()).econCostHome;
		String desc = Factions.get().getOuterCmdFactions().cmdFactionsHome.getDesc();
		
		payForAction(event, cost, desc);
	}
	

}
