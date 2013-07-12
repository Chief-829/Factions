package net.immortalcraft.factions.cmd;

import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;
import com.massivecraft.mcore.util.Txt;

import net.immortalcraft.factions.cmd.req.ReqFactionsEnabled;
import net.immortalcraft.factions.entity.Faction;
import net.immortalcraft.factions.Lang;
import net.immortalcraft.factions.Perm;

public class CmdFactionsAnnounce extends FCommand 
{	
    public CmdFactionsAnnounce() 
    {
		this.addAliases("announce");
		
		this.addRequiredArg("message");
		this.errorOnToManyArgs = false;
		
		this.addRequirements(ReqFactionsEnabled.get());
                this.addRequirements(ReqHasPerm.get(Perm.ANNOUNCE.node));
		this.addRequirements(ReqIsPlayer.get());
		
		this.setDesc("sends an announcment to your Faction");
    }
	
	@Override
	public void perform() 
        {
		String message = Txt.implode(args, " ").replaceAll("(&([a-f0-9]))", "& $2");
		
                // Data
		Faction faction = usender.getFaction();
                
                // TODO: Add a feature on the FPerm board to only allow certain ranks.
                
                // Command
		faction.msg(Lang.ANNOUNCE_FORMAT, message);
	}
}
