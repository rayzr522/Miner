
package com.rayzr522.miner.util;

import java.util.Collection;

import org.bukkit.ChatColor;

import com.rayzr522.miner.arena.Arena;
import com.rayzr522.miner.arena.ArenaControl;

public class TextUtils {
	
	public static String listToString(Collection<? extends Object> list) {
		
		StringBuilder output = new StringBuilder("");
		
		for (Object o : list) {
			
			output.append(", " + o.toString());
			
		}
		
		return output.substring(2);
		
	}
	
	public static String arenaListToString(ArenaControl arenaControl) {
		
		StringBuilder output = new StringBuilder("");
		
		for (Arena arena : arenaControl.getArenas()) {
			
			String name = arena.getName();
			
			output.append(", ");
			output.append(ChatColor.GREEN);
			
			if (!arena.isPlayable()) {
				
				output.append(ChatColor.STRIKETHROUGH);
				
			}
			
			output.append(name);
			
			output.append(ChatColor.RESET);
			
			if (arena.isOccupied()) {
				
				output.append(ChatColor.YELLOW);
				output.append(" (Occupied)");
				output.append(ChatColor.RESET);
				
			}
			
		}
		
		return output.toString().equals("") ? "" : output.substring(2);
		
	}
	
}
