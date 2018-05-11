
package com.rayzr522.miner.arena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.rayzr522.miner.Miner;
import com.rayzr522.miner.util.BlockWeight;
import com.rayzr522.miner.util.ConfigUtils;
import com.rayzr522.miner.util.Messenger;
import com.rayzr522.miner.util.NumberUtils;

public class ArenaControl {
	
	private Miner plugin;
	private List<Arena> arenas;
	private HashMap<Material, Integer> rewards;
	private List<BlockWeight> blockWeights;
	
	private HashMap<ArenaPlayer, Arena> players;
	
	private Economy economy;
	
	public ArenaControl(Miner plugin, Economy economy) {
		
		this.plugin = plugin;
		this.arenas = new ArrayList<Arena>();
		this.players = new HashMap<ArenaPlayer, Arena>();
		
		this.economy = economy;
		
		loadData();
		
	}
	
	public void loadData() {
		
		kickAll();
		loadArenas();
		this.rewards = ConfigUtils.loadRewards();
		this.blockWeights = ConfigUtils.loadBlockWeights();
		
	}
	
	public void loadArenas() {
		
		for (String name : ConfigUtils.getSection("arenas").getKeys(false)) {
			
			ConfigUtils.loadArena(this, name);
			
		}
		
	}
	
	public void saveArenas() {
		
		for (Arena arena : arenas) {
			
			ConfigUtils.saveArena(arena);
			
		}
		
	}
	
	public Arena createArena(World world, String name) {
		
		Arena arena = new Arena(plugin, this, name, world);
		
		arenas.add(arena);
		
		return arena;
		
	}
	
	public void removeArena(String name) {
		
		Arena arena = getArena(name);
		
		arenas.remove(arena);
		
		ConfigUtils.removeArena(arena);
		
	}
	
	public void saveArena(String name) {
		
		Arena arena = getArena(name);
		
		ConfigUtils.saveArena(arena);
		
	}
	
	public void loadArena(String name) {
		
		ConfigUtils.loadArena(this, name);
		
	}
	
	public Arena getArena(String name) {
		
		for (Arena arena : getArenas()) {
			
			if (arena.getName().equals(name)) {
				
				return arena;
				
			}
			
		}
		
		return null;
		
	}
	
	public void fillWithOres(Arena arena) {
		
		Random random = new Random();
		
		for (Vector vec : arena.getOreRegion().getBlocks()) {
			
			Block block = arena.getWorld().getBlockAt(vec.getBlockX(), vec.getBlockY(), vec.getBlockZ());
			
			int randInt = random.nextInt(100) + 1;
			
			int startInt = 0;
			
			for (BlockWeight blockWeight : blockWeights) {
				
				startInt += blockWeight.getWeight();
				
				if (randInt <= startInt) {
					
					block.setType(blockWeight.getBlock());
					break;
					
				}
				
			}
			
		}
		
	}
	
	public void joinArena(Player player, String name) {
		
		if (!arenaExists(name)) {
			
			return;
			
		}
		
		Arena arena = getArena(name);
		
		ArenaPlayer arenaPlayer = new ArenaPlayer(player, arena, this);
		
		if (!arena.isOccupied()) {
			
			fillWithOres(arena);
			
		}
		
		arena.setOccupied(true);
		
		arena.teleportToSpawn(player);
		
		arenaPlayer.storeData();
		
		players.put(arenaPlayer, arena);
		
		ItemStack pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
		
		ItemMeta im = pickaxe.getItemMeta();
		im.setDisplayName(ChatColor.AQUA + "Diamond " + ChatColor.RED + ChatColor.BOLD + "Pickaxe");
		pickaxe.setItemMeta(im);
		
		pickaxe.addEnchantment(Enchantment.DIG_SPEED, 5);
		pickaxe.addEnchantment(Enchantment.SILK_TOUCH, 1);
		
		player.getInventory().addItem(pickaxe);
		player.setGameMode(GameMode.SURVIVAL);
		
	}
	
	public void leaveArena(Player player) {
		
		if (getArenaPlayer(player) == null) {
			
			return;
			
		}
		
		leaveArena(getArenaPlayer(player));
		
	}
	
	public void leaveArena(ArenaPlayer arenaPlayer) {
		
		if (arenaPlayer == null) {
			
			return;
			
		}
		
		Player player = arenaPlayer.getPlayer();
		
		rewardPlayer(player);
		
		arenaPlayer.getArena().teleportToExit(player);
		
		arenaPlayer.restoreData();
		
		players.remove(arenaPlayer);
		
		if (getArenaPlayers(arenaPlayer.getArena()).size() == 0) {
			
			arenaPlayer.getArena().setOccupied(false);
			
		}
		
	}
	
	public void rewardPlayer(Player player) {
		
		if (getArenaPlayer(player) == null) {
			
			return;
			
		}
		
		int reward = 0;
		
		for (ItemStack itemStack : player.getInventory().getContents()) {
			
			if (itemStack == null || itemStack.getType() == Material.AIR) {
				
				continue;
				
			}
			
			if (!rewards.containsKey(itemStack.getType())) {
				
				continue;
				
			}
			
			int money = rewards.get(itemStack.getType()) * itemStack.getAmount();
			
			reward += money;
			
		}
		
		economy.depositPlayer(player, reward);
		
		Messenger.playerInfo(player, "You earned " + NumberUtils.COLOR + "$" + NumberUtils.formatNumber(reward) + "!");
		
	}
	
	public void kickAll() {
		
		for (ArenaPlayer arenaPlayer : players.keySet()) {
			
			leaveArena(arenaPlayer);
			
		}
		
	}
	
	public ArenaPlayer getArenaPlayer(Player player) {
		
		for (ArenaPlayer arenaPlayer : players.keySet()) {
			
			if (arenaPlayer.getPlayer() == player) {
				
				return arenaPlayer;
				
			}
			
		}
		
		return null;
		
	}
	
	public List<ArenaPlayer> getArenaPlayers(Arena arena) {
		
		List<ArenaPlayer> arenaPlayers = new ArrayList<ArenaPlayer>();
		
		for (ArenaPlayer arenaPlayer : players.keySet()) {
			
			if (arenaPlayer.getArena() == arena) {
				
				arenaPlayers.add(arenaPlayer);
				
			}
			
		}
		
		return arenaPlayers;
		
	}
	
	public boolean arenaExists(String name) {
		
		return getArena(name) != null;
		
	}
	
	public void showArenaRegion(final Player player, final Arena arena) {
		
		final Region arenaRegion = arena.getArenaRegion();
		
		for (int z = arenaRegion.getMinZ(); z <= arenaRegion.getMaxZ(); z++) {
			
			for (int y = arenaRegion.getMinY(); y <= arenaRegion.getMaxY(); y++) {
				
				for (int x = arenaRegion.getMinX(); x <= arenaRegion.getMaxX(); x++) {
					
					player.sendBlockChange(new Location(arena.getWorld(), x, y, z), Material.GLASS, (byte) 0);
					
				}
				
			}
			
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				
				for (int z = arenaRegion.getMinZ(); z <= arenaRegion.getMaxZ(); z++) {
					
					for (int y = arenaRegion.getMinY(); y <= arenaRegion.getMaxY(); y++) {
						
						for (int x = arenaRegion.getMinX(); x <= arenaRegion.getMaxX(); x++) {
							
							player.sendBlockChange(new Location(arena.getWorld(), x, y, z), arena.getWorld().getBlockAt(x, y, z).getType(), arena
							.getWorld().getBlockAt(x, y, z).getData());
							
						}
						
					}
					
				}
				
			}
		}, 60);
		
	}
	
	public List<Arena> getArenas() {
		
		return arenas;
		
	}
	
	public Miner getPlugin() {
		
		return plugin;
		
	}
	
	public void setUpArena(Arena arena, Region arenaRegion, Region oreRegion, Location spawn, Location exit) {
		
		boolean setUp = (arenaRegion != null) && (oreRegion != null) && (spawn != null) && (exit != null);
		
		if (arenaRegion != null)
			arena.setArenaRegion(arenaRegion);
		
		if (oreRegion != null)
			arena.setOreRegion(oreRegion);
		
		if (spawn != null)
			arena.setSpawn(spawn);
		
		if (exit != null)
			arena.setExit(exit);
		
		arena.setSetUp(setUp);
		arena.setEnabled(setUp);
		
	}
	
	public void showArena(Player player, Arena arena) {
		
		Region arenaRegion = arena.getArenaRegion();
		Region oreRegion = arena.getOreRegion();
		
		Location spawn = arena.getSpawn();
		Location exit = arena.getExit();
		
		if (arenaRegion != null) {
			
			showBlocks(player, 5, arenaRegion.getFramePoints(player.getWorld()));
			
		}
		
		if (oreRegion != null) {
			
			showBlocks(player, 14, oreRegion.getFramePoints(player.getWorld()));
			
		}
		
		if (spawn != null) {
			
			showBlock(player, 11, spawn);
			
		}
		
		if (exit != null) {
			
			showBlock(player, 1, exit);
			
		}
		
	}
	
	public void showBlocks(final Player p, final int color, final List<Location> points) {
		
		final HashMap<Location, BlockState> blocks = new HashMap<Location, BlockState>();
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				
				for (Location l : points) {
					Block b = l.getBlock();
					blocks.put(l, b.getState());
					p.sendBlockChange(l, Material.WOOL, (byte) color);
				}
				
			}
			
		}, 0L);
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				if (!p.isOnline()) {
					return;
				}
				
				for (Entry<Location, BlockState> entry : blocks.entrySet()) {
					Location l = (Location) entry.getKey();
					BlockState b = (BlockState) entry.getValue();
					Material type = b.getType();
					byte data = b.getRawData();
					
					p.sendBlockChange(l, type, data);
				}
			}
			
		}, 100L);
	}
	
	public void showBlock(final Player p, final int color, final Location loc) {
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				
				p.sendBlockChange(loc, Material.WOOL, (byte) color);
				
			}
		}, 0L);
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				if (!p.isOnline()) {
					return;
				}
				
				Block block = loc.getWorld().getBlockAt(loc);
				Material type = block.getType();
				byte data = block.getData();
				
				p.sendBlockChange(loc, type, data);
				
			}
		}, 100L);
	}
	
	public HashMap<Material, Integer> getRewards() {
		
		return rewards;
		
	}
	
	public List<BlockWeight> getBlockWeights() {
		
		return blockWeights;
		
	}
	
}
