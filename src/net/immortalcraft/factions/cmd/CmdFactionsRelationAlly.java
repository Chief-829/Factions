package net.immortalcraft.factions.cmd;

import net.immortalcraft.factions.Rel;

public class CmdFactionsRelationAlly extends CmdFactionsRelationAbstract
{
	public CmdFactionsRelationAlly()
	{
		this.addAliases("ally");
		
		this.targetRelation = Rel.ALLY;
	}
}
