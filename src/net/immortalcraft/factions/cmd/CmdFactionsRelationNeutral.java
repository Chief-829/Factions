package net.immortalcraft.factions.cmd;

import net.immortalcraft.factions.Rel;

public class CmdFactionsRelationNeutral extends CmdFactionsRelationAbstract
{
	public CmdFactionsRelationNeutral()
	{
		this.addAliases("neutral");
		
		this.targetRelation = Rel.NEUTRAL;
	}
}
