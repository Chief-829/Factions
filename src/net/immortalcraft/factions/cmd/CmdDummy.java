package net.immortalcraft.factions.cmd

import net.immortalcraft.factions.Factions
import net.immortalcraft.factions.cmd.FCommand

// This is a test/template for commands
public class CmdDummy extends FCommand
{
  public CmdDummy()
  {
    
      // Aliases
      this.addAliases("dummy");
  
      // Requirements
      this.addRequirements("dummy");
  
    
  }
  
    @Override
    public void perform()
    {
      
      String message = Txt.implode(args, " ").replaceAll("(&([a-f0-9]))", "& $2");

      usender.msg("Hello Dummy!");
    }
}
