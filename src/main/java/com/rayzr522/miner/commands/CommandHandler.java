
package com.rayzr522.miner.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;

import com.rayzr522.miner.Miner;
import com.rayzr522.miner.arena.ArenaControl;
import com.rayzr522.miner.commands.admin.ConfigCommand;
import com.rayzr522.miner.commands.admin.CreateCommand;
import com.rayzr522.miner.commands.admin.DeleteCommand;
import com.rayzr522.miner.commands.admin.DisableCommand;
import com.rayzr522.miner.commands.admin.EnableCommand;
import com.rayzr522.miner.commands.admin.LoadCommand;
import com.rayzr522.miner.commands.admin.SaveCommand;
import com.rayzr522.miner.commands.admin.SetupCommand;
import com.rayzr522.miner.commands.user.JoinCommand;
import com.rayzr522.miner.commands.user.LeaveCommand;
import com.rayzr522.miner.commands.user.ListCommand;
import com.rayzr522.miner.commands.user.ResetCommand;
import com.rayzr522.miner.commands.user.RewardsCommand;
import com.rayzr522.miner.commands.user.ShowCommand;
import com.rayzr522.miner.util.Messenger;
import com.rayzr522.miner.util.Msg;

public class CommandHandler implements CommandExecutor {
	
	private Miner plugin;
	private ArenaControl arenaControl;
	private HashMap<String, ArenaCommand> commands;
	
	public CommandHandler(Miner plugin, ArenaControl arenaControl) {
		
		this.plugin = plugin;
		this.arenaControl = arenaControl;
		
		this.commands = new HashMap<String, ArenaCommand>();
		
		registerCommands();
		
	}
	
	public boolean onCommand(CommandSender sender, Command bcmd, String commandLabel, String[] args) {
		
		String cmd = (args.length > 0) ? args[0].toLowerCase() : "";
		String last = (args.length > 0) ? args[(args.length - 1)] : "";
		
		if (((sender instanceof Conversable)) && (((Conversable) sender).isConversing())) {
			return true;
		}
		
		if (cmd.equals("")) {
			
			showHelp(sender);
			return true;
			
		}
		
		if (cmd.equals("?") || cmd.equals("help")) {
			
			showHelp(sender);
			return true;
			
		}
		
		List<ArenaCommand> matches = getMatchingCommands(cmd);
		
		if (matches.size() == 0) {
			
			Messenger.playerMsg(sender, Msg.NO_MATCHES);
			return true;
			
		}
		
		if (matches.size() > 1) {
			
			Messenger.playerMsg(sender, Msg.MULTIPLE_MATCHES);
			
			for (ArenaCommand arenaCommand : matches) {
				
				showUsage(sender, arenaCommand);
				
			}
			
			return true;
			
		}
		
		ArenaCommand command = matches.get(0);
		CommandInfo info = (CommandInfo) command.getClass().getAnnotation(CommandInfo.class);
		
		if (!this.plugin.has(sender, info.perm())) {
			
			Messenger.playerWarning(sender, Msg.NO_PERMISSION.format(info.perm()));
			return true;
			
		}
		
		if (last.equals("?") || last.equals("help")) {
			
			showUsage(sender, command);
			return true;
			
		}
		
		String[] params = trimFirstArg(args);
		
		if (!command.execute(arenaControl, sender, params)) {
			
			showUsage(sender, command);
			
		}
		
		return true;
		
	}
	
	private List<ArenaCommand> getMatchingCommands(String cmd) {
		
		List<ArenaCommand> matches = new ArrayList<ArenaCommand>();
		
		for (Entry<String, ArenaCommand> entry : this.commands.entrySet()) {
			
			if (cmd.matches(entry.getKey())) {
				
				matches.add(entry.getValue());
				
			}
			
		}
		
		return matches;
		
	}
	
	// Show help messages
	// Format: 'usage : description'
	// Example: 'miner join <arena> : Join an arena'
	private void showHelp(CommandSender sender) {
		
		Messenger.playerTitle(sender, " ----- COMMANDS ----- ");
		
		for (ArenaCommand command : this.commands.values()) {
			
			CommandInfo info = (CommandInfo) command.getClass().getAnnotation(CommandInfo.class);
			
			StringBuilder message = new StringBuilder();
			
			message.append(ChatColor.WHITE + info.usage());
			message.append(" | ");
			message.append(ChatColor.YELLOW + info.desc());
			
			if (!this.plugin.has(sender, info.perm())) {
				
				message.append(ChatColor.WHITE + " | ");
				message.append(ChatColor.RED + "Needs '" + info.perm() + "'");
				
			}
			
			Messenger.playerRaw(sender, message.toString());
			
		}
		
	}
	
	// Show usage for a command
	private void showUsage(CommandSender sender, ArenaCommand command) {
		
		Messenger.playerTitle(sender, "Usage:");
		
		CommandInfo info = (CommandInfo) command.getClass().getAnnotation(CommandInfo.class);
		
		StringBuilder message = new StringBuilder();
		
		message.append(ChatColor.WHITE + info.usage());
		message.append(" | ");
		message.append(ChatColor.YELLOW + info.desc());
		
		Messenger.playerRaw(sender, message.toString());
		
	}
	
	public boolean isRegistered(String command) {
		
		return this.commands.containsKey(command);
		
	}
	
	public void registerCommands() {
		
		// User commands
		register(JoinCommand.class);
		register(LeaveCommand.class);
		register(ShowCommand.class);
		register(ListCommand.class);
		register(RewardsCommand.class);
		register(ResetCommand.class);
		
		// TODO: Leave
		
		// Admin commands
		register(CreateCommand.class);
		register(DeleteCommand.class);
		register(SaveCommand.class);
		register(LoadCommand.class);
		register(SetupCommand.class);
		register(DisableCommand.class);
		register(EnableCommand.class);
		register(ConfigCommand.class);
		// TODO: Arena Create, Arena Delete, Arena Edit, Arena
		
	}
	
	public void register(Class<? extends ArenaCommand> c) {
		
		CommandInfo info = (CommandInfo) c.getAnnotation(CommandInfo.class);
		
		if (info == null) {
			
			plugin.getLogger().info("The class '" + c.getName() + "' has no command info. Skipping...");
			return;
			
		}
		
		try {
			
			this.commands.put(info.pattern(), c.newInstance());
			
		} catch (Exception e) {
			
			plugin.getLogger().info("Registration derped while registering the command '" + c.getName() + "'!");
			
		}
		
	}
	
	private String[] trimFirstArg(String[] args) {
		
		return (String[]) Arrays.copyOfRange(args, 1, args.length);
		
	}
	
}
