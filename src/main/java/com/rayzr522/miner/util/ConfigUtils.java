
package com.rayzr522.miner.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import com.rayzr522.miner.Miner;
import com.rayzr522.miner.arena.Arena;
import com.rayzr522.miner.arena.ArenaControl;
import com.rayzr522.miner.arena.Region;

public class ConfigUtils {
	
	public static Miner plugin;
	
	public static void loadArena(ArenaControl arenaControl, String name) {
		
		ConfigurationSection arenas = getSection("arenas");
		
		if (!arenas.contains(name)) {
			
			return;
			
		}
		
		Arena arena;
		
		if (!arenaControl.arenaExists(name)) {
			
			arena = arenaControl.createArena(null, name);
			
		} else {
			
			arena = arenaControl.getArena(name);
			
		}
		
		ConfigurationSection arenaSection = getSection(arenas, name);
		
		Region arenaRegion = parseRegion(arenaSection, "Arena");
		Region oreRegion = parseRegion(arenaSection, "Ore");
		
		Location spawn = parseLocation(arenaSection, "Spawn");
		Location exit = parseLocation(arenaSection, "Exit");
		
		World world = parseWorld(arenaSection, "Main");
		
		boolean enabled = parseBoolean(arenaSection, "Enabled");
		boolean editable = parseBoolean(arenaSection, "Editable");
		
		arenaControl.setUpArena(arena, arenaRegion, oreRegion, spawn, exit);
		
		arena.setWorld(world);
		
		arena.setEnabled(enabled);
		arena.setEditable(editable);
		
	}
	
	public static void saveArena(Arena arena) {
		
		String name = arena.getName();
		
		Region arenaRegion = arena.getArenaRegion();
		Region oreRegion = arena.getOreRegion();
		
		Location spawn = arena.getSpawn();
		Location exit = arena.getExit();
		
		World world = arena.getWorld();
		
		boolean enabled = arena.isEnabled();
		boolean editable = arena.isEditable();
		
		ConfigurationSection arenaSection = getSection(getSection("arenas"), name);
		
		saveRegion(arenaSection, arenaRegion, "Arena");
		saveRegion(arenaSection, oreRegion, "Ore");
		
		saveLocation(arenaSection, spawn, "Spawn");
		saveLocation(arenaSection, exit, "Exit");
		
		saveWorld(arenaSection, world, "Main");
		
		saveBoolean(arenaSection, enabled, "Enabled");
		saveBoolean(arenaSection, editable, "Editable");
		
		saveConfig();
		
	}
	
	public static void removeArena(Arena arena) {
		
		getSection("arenas").set(arena.getName(), null);
		saveConfig();
		
	}
	
	public static void saveRegion(ConfigurationSection section, Region region, String type) {
		
		if (region == null) {
			
			return;
			
		}
		
		int minX = region.getMinX();
		int minY = region.getMinY();
		int minZ = region.getMinZ();
		
		int maxX = region.getMaxX();
		int maxY = region.getMaxY();
		int maxZ = region.getMaxZ();
		
		String formatted = minX + "," + minY + "," + minZ + "," + maxX + "," + maxY + "," + maxZ;
		
		section.set(type + "Region", formatted);
		
	}
	
	public static Region parseRegion(ConfigurationSection section, String type) {
		
		if (!section.contains(type + "Region")) {
			
			return null;
			
		}
		
		String configString = section.getString(type + "Region");
		
		String[] split = configString.trim().split(",");
		
		if (split.length < 6) {
			
			return null;
			
		}
		
		int x1 = Integer.parseInt(split[0]);
		int y1 = Integer.parseInt(split[1]);
		int z1 = Integer.parseInt(split[2]);
		
		int x2 = Integer.parseInt(split[3]);
		int y2 = Integer.parseInt(split[4]);
		int z2 = Integer.parseInt(split[5]);
		
		return new Region(x1, y1, z1, x2, y2, z2);
		
	}
	
	public static void saveLocation(ConfigurationSection section, Location location, String type) {
		
		if (location == null) {
			
			return;
			
		}
		
		double x = location.getX();
		double y = location.getY();
		double z = location.getZ();
		World world = location.getWorld();
		
		String formatted = x + "," + y + "," + z + "," + world.getUID();
		
		section.set(type + "Location", formatted);
		
	}
	
	public static Location parseLocation(ConfigurationSection section, String type) {
		
		if (!section.contains(type + "Location")) {
			
			return null;
			
		}
		
		String configString = section.getString(type + "Location");
		
		String[] split = configString.trim().split(",");
		
		if (split.length < 4) {
			
			return null;
			
		}
		
		double x = Double.parseDouble(split[0]);
		double y = Double.parseDouble(split[1]);
		double z = Double.parseDouble(split[2]);
		World world = Bukkit.getWorld(UUID.fromString(split[3]));
		
		return new Location(world, x, y, z);
		
	}
	
	public static void saveBoolean(ConfigurationSection section, boolean bool, String name) {
		
		section.set(name, bool);
		
	}
	
	public static boolean parseBoolean(ConfigurationSection section, String name) {
		
		if (section.contains(name)) {
			
			return section.getBoolean(name);
			
		}
		
		return false;
		
	}
	
	public static void saveWorld(ConfigurationSection section, World world, String name) {
		
		section.set(name + "World", world.getUID().toString());
		
	}
	
	public static World parseWorld(ConfigurationSection section, String name) {
		
		if (!section.contains(name + "World")) {
			
			return null;
			
		}
		
		return Bukkit.getWorld(UUID.fromString(section.getString(name + "World")));
		
	}
	
	public static ConfigurationSection getSection(String path) {
		
		if (!plugin.getConfig().contains(path)) {
			
			return plugin.getConfig().createSection(path);
			
		}
		
		return plugin.getConfig().getConfigurationSection(path);
		
	}
	
	public static ConfigurationSection getSection(ConfigurationSection section, String path) {
		
		if (!section.contains(path)) {
			
			return section.createSection(path);
			
		}
		
		return section.getConfigurationSection(path);
		
	}
	
	public static void saveConfig() {
		
		plugin.saveConfig();
		
	}
	
	public static void reloadConfig() {
		
		plugin.reloadConfig();
		
	}
	
	public static boolean isCommandEnabled(String command) {
		
		List<String> allowedCommands = plugin.getConfig().getStringList("enabledCommands");
		
		String cmd = command.substring(1);
		
		if (cmd.startsWith("miner")) {
			
			return true;
			
		}
		
		if (cmd.contains(" ")) {
			
			if (allowedCommands.contains(cmd.split(" ")[0])) {
				
				return true;
				
			}
			
			return false;
			
		}
		
		return allowedCommands.contains(cmd);
		
	}
	
	public static HashMap<Material, Integer> loadRewards() {
		
		HashMap<Material, Integer> rewards = new HashMap<Material, Integer>();
		
		if (!plugin.getConfig().contains("rewards")) {
			
			return rewards;
			
		}
		
		List<String> rewardsList = plugin.getConfig().getStringList("rewards");
		
		for (String string : rewardsList) {
			
			String[] split = string.split(" ");
			
			if (split.length < 2) {
				
				continue;
				
			}
			
			if (Material.valueOf(split[0]) == null) {
				
				plugin.getLogger().info("Material." + split[0] + " is not a valid material!");
				continue;
				
			}
			
			Material material = Material.valueOf(split[0]);
			int reward = Integer.parseInt(split[1]);
			
			rewards.put(material, reward);
			
		}
		
		return rewards;
		
	}
	
	public static List<BlockWeight> loadBlockWeights() {
		
		List<BlockWeight> blockWeights = new ArrayList<BlockWeight>();
		
		if (!plugin.getConfig().contains("blockWeights")) {
			
			return blockWeights;
			
		}
		
		List<String> blockWeightsList = plugin.getConfig().getStringList("blockWeights");
		
		for (String string : blockWeightsList) {
			
			String[] split = string.split(" ");
			
			if (split.length < 2) {
				
				continue;
				
			}
			
			if (Material.valueOf(split[0]) == null) {
				
				plugin.getLogger().info("Material." + split[0] + " is not a valid material!");
				continue;
				
			}
			
			Material block = Material.valueOf(split[0]);
			int weight = Integer.parseInt(split[1]);
			
			blockWeights.add(new BlockWeight(block, weight));
			
		}
		
		return blockWeights;
		
	}
	
	public static List<BlockWeight> loadBlockWeights(String name) {
		
		List<BlockWeight> blockWeights = new ArrayList<BlockWeight>();
		
		if (!plugin.getConfig().contains("arenas." + name + ".blockWeights")) {
			
			return blockWeights;
			
		}
		
		List<String> blockWeightsList = plugin.getConfig().getStringList("blockWeights");
		
		for (String string : blockWeightsList) {
			
			String[] split = string.split(" ");
			
			if (split.length < 2) {
				
				continue;
				
			}
			
			if (Material.valueOf(split[0]) == null) {
				
				plugin.getLogger().info("Material." + split[0] + " is not a valid material!");
				continue;
				
			}
			
			Material block = Material.valueOf(split[0]);
			int weight = Integer.parseInt(split[1]);
			
			blockWeights.add(new BlockWeight(block, weight));
			
		}
		
		return blockWeights;
		
	}
	
}
