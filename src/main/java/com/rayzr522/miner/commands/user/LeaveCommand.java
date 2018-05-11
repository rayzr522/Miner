
package com.rayzr522.miner.commands.user;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.rayzr522.miner.arena.ArenaControl;
import com.rayzr522.miner.arena.ArenaPlayer;
import com.rayzr522.miner.commands.ArenaCommand;
import com.rayzr522.miner.commands.CommandInfo;
import com.rayzr522.miner.util.LocUtils;
import com.rayzr522.miner.util.Messenger;
import com.rayzr522.miner.util.Msg;

@CommandInfo(desc = "Leave your current arena", name = "leave", pattern = "l(eave)?", perm = "miner.user.leave", usage = "/miner leave")
public class LeaveCommand implements ArenaCommand {
	
	public boolean execute(ArenaControl arenaControl, CommandSender sender, String[] args) {
		
		// if player is not in another arena
		
		Player player = (Player) sender;
		
		ArenaPlayer arenaPlayer = arenaControl.getArenaPlayer(player);
		
		if (arenaPlayer == null) {
			
			Messenger.playerMsg(player, Msg.NOT_IN_ARENA);
			return true;
			
		}
		
		Messenger.playerMsg(player, Msg.ARENA_LEFT, arenaPlayer.getArena().getName());
		
		arenaControl.leaveArena(player);
		
		return true;
		
	}
	
}
