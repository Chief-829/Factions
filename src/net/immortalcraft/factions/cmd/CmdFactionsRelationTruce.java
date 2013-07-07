package net.immortalcraft.factions.cmd;

import net.immortalcraft.factions.Rel;

public class CmdFactionsRelationTruce extends CmdFactionsRelationAbstract
{
	public CmdFactionsRelationTruce()
	{
		this.addAliases("truce");
		
		this.targetRelation = Rel.TRUCE;
	}
}
