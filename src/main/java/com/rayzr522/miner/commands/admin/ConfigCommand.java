
package com.rayzr522.miner.commands.admin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.rayzr522.miner.arena.ArenaControl;
import com.rayzr522.miner.commands.ArenaCommand;
import com.rayzr522.miner.commands.CommandInfo;
import com.rayzr522.miner.util.ConfigUtils;
import com.rayzr522.miner.util.Messenger;
import com.rayzr522.miner.util.Msg;

@CommandInfo(desc = "Manage the config", name = "config", pattern = "config", perm = "miner.admin.config", usage = "/miner config <save:reload>")
public class ConfigCommand implements ArenaCommand {
	
	public boolean execute(ArenaControl arenaControl, CommandSender sender, String[] args) {
		
		Player player = (Player) sender;
		
		if (args.length < 1) {
			
			Messenger.playerMsg(sender, Msg.NO_ARG, "action");
			return false;
			
		}
		
		if (!args[0].equals("save") && !args[0].equals("reload")) {
			
			return false;
			
		}
		
		if (args[0].equals("save")) {
			
			arenaControl.saveArenas();
			ConfigUtils.saveConfig();
			
			Messenger.playerInfo(sender, "Config saved!");
			
		}
		
		if (args[0].equals("reload")) {
			
			ConfigUtils.reloadConfig();
			arenaControl.loadData();
			
			Messenger.playerInfo(sender, "Config reloaded!");
			
		}
		
		return true;
		
	}
	
}
