
package com.rayzr522.miner.commands.admin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.rayzr522.miner.arena.ArenaControl;
import com.rayzr522.miner.commands.ArenaCommand;
import com.rayzr522.miner.commands.CommandInfo;
import com.rayzr522.miner.util.Messenger;
import com.rayzr522.miner.util.Msg;

@CommandInfo(desc = "Load an arena", name = "load", pattern = "load", perm = "miner.admin.load", usage = "/miner load <name>")
public class LoadCommand implements ArenaCommand {
	
	public boolean execute(ArenaControl arenaControl, CommandSender sender, String[] args) {
		
		Player player = (Player) sender;
		
		if (args.length < 1) {
			
			Messenger.playerMsg(sender, Msg.NO_ARG, "name");
			return false;
			
		}
		
		Messenger.playerInfo(sender, "Arena loaded!");
		arenaControl.loadArena(args[0]);
		
		return true;
		
	}
	
}
