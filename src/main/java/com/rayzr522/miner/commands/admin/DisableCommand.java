
package com.rayzr522.miner.commands.admin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.rayzr522.miner.arena.Arena;
import com.rayzr522.miner.arena.ArenaControl;
import com.rayzr522.miner.commands.ArenaCommand;
import com.rayzr522.miner.commands.CommandInfo;
import com.rayzr522.miner.util.Messenger;
import com.rayzr522.miner.util.Msg;
import com.rayzr522.miner.util.TextUtils;

@CommandInfo(desc = "Disables an arena", name = "disable", pattern = "disable", perm = "miner.admin.disable", usage = "/miner disable <name>")
public class DisableCommand implements ArenaCommand {
	
	public boolean execute(ArenaControl arenaControl, CommandSender sender, String[] args) {
		
		Player player = (Player) sender;
		
		if (args.length < 1) {
			
			Messenger.playerMsg(sender, Msg.NO_ARG, "name");
			return false;
			
		}
		
		if (!arenaControl.arenaExists(args[0])) {
			
			Messenger.playerMsg(sender, Msg.NO_SUCH_ARENA, args[0]);
			Messenger.playerMsg(sender, Msg.ARENA_LIST, TextUtils.arenaListToString(arenaControl));
			return true;
			
		}
		
		Arena arena = arenaControl.getArena(args[0]);
		
		if (!arena.isSetUp()) {
			
			Messenger.playerMsg(sender, Msg.ARENA_NOT_SET_UP, args[0]);
			return true;
			
		}
		
		Messenger.playerMsg(sender, Msg.ARENA_DISABLED, args[0]);
		arena.setEnabled(false);
		
		return true;
		
	}
	
}
