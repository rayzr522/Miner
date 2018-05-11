
package com.rayzr522.miner.commands.user;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.rayzr522.miner.arena.Arena;
import com.rayzr522.miner.arena.ArenaControl;
import com.rayzr522.miner.commands.ArenaCommand;
import com.rayzr522.miner.commands.CommandInfo;
import com.rayzr522.miner.util.LocUtils;
import com.rayzr522.miner.util.Messenger;
import com.rayzr522.miner.util.Msg;
import com.rayzr522.miner.util.TextUtils;

@CommandInfo(desc = "Join an arena", name = "join", pattern = "j(oin)?|p(lay)?", perm = "miner.user.join", usage = "/miner join <arena>")
public class JoinCommand implements ArenaCommand {
	
	public boolean execute(ArenaControl arenaControl, CommandSender sender, String[] args) {
		
		// if player is not in another arena
		
		Player player = (Player) sender;
		
		if (args.length < 1) {
			
			Messenger.playerMsg(player, Msg.NO_ARG, "name");
			return false;
			
		}
		
		if (!arenaControl.arenaExists(args[0])) {
			
			Messenger.playerMsg(player, Msg.NO_SUCH_ARENA, args[0]);
			Messenger.playerMsg(player, Msg.ARENA_LIST, TextUtils.arenaListToString(arenaControl));
			return true;
			
		}
		
		Arena arena = arenaControl.getArena(args[0]);
		
		if (!arena.isPlayable()) {
			
			Messenger.playerMsg(player, Msg.ARENA_NOT_PLAYABLE, args[0]);
			return true;
			
		}
		
		if (arenaControl.getArenaPlayer(player) != null) {
			
			Messenger.playerMsg(player, Msg.ALREADY_IN_ARENA, arenaControl.getArenaPlayer(player).getArena().getName());
			return true;
			
		}
		
		Messenger.playerMsg(player, Msg.ARENA_JOINED, args[0]);
		
		arenaControl.joinArena(player, args[0]);
		
		return true;
		
	}
	
}
