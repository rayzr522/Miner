
package com.rayzr522.miner.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Messenger {
	
	public static final ChatColor INFO = ChatColor.WHITE;
	public static final ChatColor NOTE = ChatColor.GRAY;
	public static final ChatColor WARNING = ChatColor.YELLOW;
	public static final ChatColor ERROR = ChatColor.RED;
	public static final ChatColor TITLE = ChatColor.GREEN;
	
	public static void playerInfo(Player player, String message) {
		
		player.sendMessage(INFO + message);
		
	}
	
	public static void playerNote(Player player, String message) {
		
		player.sendMessage(NOTE + message);
		
	}
	
	public static void playerWarning(Player player, String message) {
		
		player.sendMessage(WARNING + message);
		
	}
	
	public static void playerError(Player player, String message) {
		
		player.sendMessage(ERROR + message);
		
	}
	
	public static void playerRaw(Player player, String message) {
		
		player.sendMessage(message);
		
	}
	
	public static void playerInfo(CommandSender sender, String message) {
		
		if (isConsole(sender)) {
			return;
		}
		
		playerInfo((Player) sender, message);
		
	}
	
	public static void playerNote(CommandSender sender, String message) {
		
		if (isConsole(sender)) {
			return;
		}
		
		playerNote((Player) sender, message);
		
	}
	
	public static void playerWarning(CommandSender sender, String message) {
		
		if (isConsole(sender)) {
			return;
		}
		
		playerWarning((Player) sender, message);
		
	}
	
	public static void playerError(CommandSender sender, String message) {
		
		if (isConsole(sender)) {
			return;
		}
		
		playerError((Player) sender, message);
		
	}
	
	public static void playerRaw(CommandSender sender, String message) {
		
		if (isConsole(sender)) {
			return;
		}
		
		playerRaw((Player) sender, message);
		
	}
	
	public static void playerMsg(Player player, Msg message, String arg) {
		
		playerRaw(player, message.type + message.format(arg));
		
	}
	
	public static void playerMsg(CommandSender sender, Msg message, String arg) {
		
		if (isConsole(sender)) {
			return;
		}
		
		playerMsg((Player) sender, message, arg);
		
	}
	
	public static void playerMsg(Player player, Msg message) {
		
		playerRaw(player, message.type + message.text);
		
	}
	
	public static void playerMsg(CommandSender sender, Msg message) {
		
		if (isConsole(sender)) {
			return;
		}
		
		playerMsg((Player) sender, message);
		
	}
	
	public static void playerTitle(Player player, String message) {
		
		player.sendMessage(TITLE + message);
		
	}
	
	public static void playerTitle(CommandSender sender, String message) {
		
		if (isConsole(sender)) {
			return;
		}
		
		playerTitle((Player) sender, message);
		
	}
	
	public static boolean isConsole(CommandSender sender) {
		
		return (sender instanceof ConsoleCommandSender);
		
	}
	
}
