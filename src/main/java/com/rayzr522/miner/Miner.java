
package com.rayzr522.miner;

import java.io.File;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.rayzr522.miner.arena.ArenaControl;
import com.rayzr522.miner.commands.CommandHandler;
import com.rayzr522.miner.events.MinerEventListener;
import com.rayzr522.miner.util.ConfigUtils;

public class Miner extends JavaPlugin implements Listener {
	
	private Plugin plugin = this;
	
	private Economy economy;
	
	private ArenaControl arenaControl;
	private CommandHandler commandHandler;
	
	public void onEnable() {
		
		ConfigUtils.plugin = this;
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		setupVault();
		
		arenaControl = new ArenaControl(this, economy);
		
		arenaControl.loadArenas();
		
		registerListeners();
		
		registerEvents(new MinerEventListener(this, arenaControl));
		
		getLogger().info("TheMiner has been loaded.");
		
	}
	
	public void onDisable() {
		
		arenaControl.kickAll();
		arenaControl.saveArenas();
		
		getLogger().info("TheMiner has been unloaded.");
		
	}
	
	public void registerListeners() {
		
		commandHandler = new CommandHandler(this, arenaControl);
		
		getCommand("miner").setExecutor(commandHandler);
		
	}
	
	public void registerEvents(Listener listener) {
		
		this.getServer().getPluginManager().registerEvents(listener, this);
		
	}
	
	public void setupVault() {
		
		RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
		economy = ((Economy) economyProvider.getProvider());
		
	}
	
	public boolean has(Player p, String s) {
		
		return p.hasPermission(s);
		
	}
	
	public boolean has(CommandSender sender, String s) {
		
		if (sender instanceof ConsoleCommandSender) {
			
			return true;
			
		}
		
		return has((Player) sender, s);
		
	}
	
	public ArenaControl getArenaControl() {
		
		return arenaControl;
		
	}
}
