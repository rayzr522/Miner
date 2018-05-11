
package com.rayzr522.miner.commands.user;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.rayzr522.miner.arena.Arena;
import com.rayzr522.miner.arena.ArenaControl;
import com.rayzr522.miner.arena.ArenaPlayer;
import com.rayzr522.miner.commands.ArenaCommand;
import com.rayzr522.miner.commands.CommandInfo;
import com.rayzr522.miner.util.Messenger;
import com.rayzr522.miner.util.Msg;

@CommandInfo(desc = "Reset the ores in your arena", name = "reset", pattern = "rs|reset", perm = "miner.user.reset", usage = "/miner reset")
public class ResetCommand implements ArenaCommand {
	
	public boolean execute(ArenaControl arenaControl, CommandSender sender, String[] args) {
		
		// if player is not in another arena
		
		Player player = (Player) sender;
		
		if (arenaControl.getArenaPlayer(player) == null) {
			
			Messenger.playerMsg(player, Msg.NOT_IN_ARENA);
			return true;
			
		}
		
		ArenaPlayer arenaPlayer = arenaControl.getArenaPlayer(player);
		Arena arena = arenaPlayer.getArena();
		
		for (ArenaPlayer ap : arenaControl.getArenaPlayers(arena)) {
			
			arena.teleportToSpawn(ap.getPlayer());
			
		}
		
		arenaControl.fillWithOres(arena);
		
		Messenger.playerMsg(player, Msg.ARENA_RESET, arena.getName());
		
		return true;
		
	}
	
}
