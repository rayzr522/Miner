
package com.rayzr522.miner.commands.user;

import org.bukkit.command.CommandSender;

import com.rayzr522.miner.arena.Arena;
import com.rayzr522.miner.arena.ArenaControl;
import com.rayzr522.miner.commands.ArenaCommand;
import com.rayzr522.miner.commands.CommandInfo;
import com.rayzr522.miner.util.Messenger;
import com.rayzr522.miner.util.Msg;
import com.rayzr522.miner.util.TextUtils;

@CommandInfo(desc = "List arenas", name = "list", pattern = "list|arenas|ls", perm = "miner.user.list", usage = "/miner list")
public class ListCommand implements ArenaCommand {
	
	public boolean execute(ArenaControl arenaControl, CommandSender sender, String[] args) {
		
		Messenger.playerInfo(sender, Msg.ARENA_LIST.format(TextUtils.arenaListToString(arenaControl)));
		
		return true;
		
	}
	
}
