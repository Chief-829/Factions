package net.immortalcraft.factions.cmd;

import net.immortalcraft.factions.Rel;

public class CmdFactionsRelationEnemy extends CmdFactionsRelationAbstract
{
	public CmdFactionsRelationEnemy()
	{
		this.addAliases("enemy");
		
		this.targetRelation = Rel.ENEMY;
	}
}
