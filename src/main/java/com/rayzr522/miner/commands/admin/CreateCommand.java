
package com.rayzr522.miner.commands.admin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.rayzr522.miner.arena.ArenaControl;
import com.rayzr522.miner.commands.ArenaCommand;
import com.rayzr522.miner.commands.CommandInfo;
import com.rayzr522.miner.util.Messenger;
import com.rayzr522.miner.util.Msg;

@CommandInfo(desc = "Create an arena", name = "create", pattern = "create", perm = "miner.admin.create", usage = "/miner create <name>")
public class CreateCommand implements ArenaCommand {
	
	public boolean execute(ArenaControl arenaControl, CommandSender sender, String[] args) {
		
		Player player = (Player) sender;
		
		if (args.length < 1) {
			
			Messenger.playerMsg(sender, Msg.NO_ARG, "name");
			return false;
			
		}
		
		if (arenaControl.arenaExists(args[0])) {
			
			Messenger.playerMsg(sender, Msg.ARENA_EXISTS, args[0]);
			return true;
			
		}
		
		Messenger.playerMsg(sender, Msg.ARENA_CREATED, args[0]);
		arenaControl.createArena(player.getWorld(), args[0]);
		
		return true;
		
	}
	
}
