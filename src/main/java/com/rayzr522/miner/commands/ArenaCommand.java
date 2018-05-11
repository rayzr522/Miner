
package com.rayzr522.miner.commands;

import org.bukkit.command.CommandSender;

import com.rayzr522.miner.arena.ArenaControl;

public abstract interface ArenaCommand {
	
	public abstract boolean execute(ArenaControl paramArenaControl, CommandSender paramCommandSender, String[] paramArrayOfString);
	
}
