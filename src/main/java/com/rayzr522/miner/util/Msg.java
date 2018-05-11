
package com.rayzr522.miner.util;

import org.bukkit.ChatColor;

public enum Msg {
	
	CMD_DERP("Are you sure you know what you're doing?", Messenger.NOTE),
	COMMAND_NOT_ALLOWED("The command '%' is not allowed!", Messenger.WARNING),
	NO_PERMISSION("You don't have the permission '%'.\nPlease contact an admin for assistance.", Messenger.ERROR), 
	MULTIPLE_MATCHES("There are multiple matches for your command.", Messenger.WARNING), 
	NO_MATCHES("There were no matches found for your command.", Messenger.WARNING), 
	ARENA_LIST("Available arenas: %", Messenger.INFO), 
	NO_ARG("The argument '%' was not provided.", Messenger.WARNING),
	NO_SUCH_ARENA("The arena '%' does not exist.", Messenger.WARNING),
	ARENA_NOT_PLAYABLE("The arena '%' is not currently playable.", Messenger.WARNING),
	ARENA_OCCUPIED("The arena '%' is currently occupied.", Messenger.WARNING),
	ARENA_CREATED("The arena '%' has been created.", Messenger.INFO),
	ARENA_JOINED("You have joined '%'!", Messenger.INFO),
	ARENA_LEFT("You have left '%'!", Messenger.INFO),
	ARENA_EXISTS("The arena '%' already exists. Please choose a different name.", Messenger.WARNING), 
	ARENA_NOT_SET_UP("The arena '%' has not been set up yet.", Messenger.WARNING),
	ARENA_ENABLED("'%' has been enabled!", Messenger.INFO),
	ARENA_DISABLED("'%' has been disabled!", Messenger.INFO),
	ARENA_DELETED("'%' has been deleted!", Messenger.INFO), 
	ALREADY_IN_ARENA("You're already in the arena '%'. Leave your current arena first.", Messenger.WARNING),
	NOT_IN_ARENA("You're not in an arena!", Messenger.WARNING),
	ARENA_RESET("The arena '%' has been reset!", Messenger.INFO);
	
	public final String text;
	public final ChatColor type;
	
	Msg(String text, ChatColor type) {
		this.text = text;
		this.type = type;
	}
	
	public String format(String s) {
		
		return s == null ? "" : this.text.replace("%", s);
		
	}
	
}
