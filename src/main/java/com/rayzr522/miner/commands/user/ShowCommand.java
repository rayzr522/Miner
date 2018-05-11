
package com.rayzr522.miner.commands.user;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.rayzr522.miner.arena.ArenaControl;
import com.rayzr522.miner.commands.ArenaCommand;
import com.rayzr522.miner.commands.CommandInfo;

@CommandInfo(desc = "Show test", pattern="show", name = "show", perm = "miner.user.show", usage = "/miner show")
public class ShowCommand implements ArenaCommand {
	
	public boolean execute(ArenaControl arenaControl, CommandSender sender, String[] args) {
		
		if (args.length > 0) {
			
			
			
			return false;
			
		}
		
//		arenaControl.showArenaRegion((Player) sender);
		
		return false;
		
	}
	
}
