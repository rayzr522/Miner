
package com.rayzr522.miner.commands.user;

import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.rayzr522.miner.arena.ArenaControl;
import com.rayzr522.miner.commands.ArenaCommand;
import com.rayzr522.miner.commands.CommandInfo;
import com.rayzr522.miner.util.Messenger;

@CommandInfo(desc = "List the rewards", name = "rewards", pattern = "rewards|val(ue(s)?)?", perm = "miner.user.rewards", usage = "/miner rewards")
public class RewardsCommand implements ArenaCommand {
	
	public boolean execute(ArenaControl arenaControl, CommandSender sender, String[] args) {
		
		Messenger.playerTitle(sender, " --- REWARDS --- ");
		
		for (Entry<Material, Integer> entry : arenaControl.getRewards().entrySet()) {
			
			Messenger.playerInfo(sender, entry.getKey().toString().replace("_", " ").toLowerCase() + ": $" + entry.getValue());
			
		}
		
		return true;
		
	}
	
}
