
package com.rayzr522.miner.arena;

import java.io.Serializable;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.rayzr522.miner.Miner;
import com.rayzr522.miner.util.BlockWeight;
import com.rayzr522.miner.util.ConfigUtils;
import com.rayzr522.miner.util.LocUtils;

public class Arena implements Serializable {
	
	private static final long serialVersionUID = -8679580053907884953L;
	
	private Miner plugin;
	private ArenaControl arenaControl;
	private FileConfiguration config;
	
	private String name;
	private World world;
	
	private Region arenaRegion = null;
	private Region oreRegion = null;
	
	private Location spawn = null;
	private Location exit = null;
	
	private boolean setUp = false;
	private boolean enabled = false;
	private boolean occupied = false;
	private boolean editable = false;
	
	private List<BlockWeight> blockWeights;
	
	public Arena(Miner plugin, ArenaControl arenaControl, String name, World world) {
		
		this.plugin = plugin;
		this.arenaControl = arenaControl;
		
		this.name = name;
		
		this.world = world;
		
		this.config = plugin.getConfig();
		
		this.blockWeights = ConfigUtils.loadBlockWeights(name);
		
	}
	
	public void setup(Region arenaRegion, Region oreRegion, Location spawn, Location exit) {
		
		setUp = true;
		
		this.arenaRegion = arenaRegion;
		this.oreRegion = oreRegion;
		
		this.spawn = spawn;
		this.exit = exit;
		
	}
	
	public Miner getPlugin() {
		return plugin;
	}
	
	public void setPlugin(Miner plugin) {
		this.plugin = plugin;
	}
	
	public ArenaControl getArenaControl() {
		return arenaControl;
	}
	
	public void setArenaControl(ArenaControl arenaControl) {
		this.arenaControl = arenaControl;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public World getWorld() {
		return world;
	}
	
	public void setWorld(World world) {
		this.world = world;
	}
	
	public Region getArenaRegion() {
		return arenaRegion;
	}
	
	public void setArenaRegion(Region arenaRegion) {
		this.arenaRegion = arenaRegion;
	}
	
	public Region getOreRegion() {
		return oreRegion;
	}
	
	public void setOreRegion(Region oreRegion) {
		this.oreRegion = oreRegion;
	}
	
	public Location getSpawn() {
		return spawn;
	}
	
	public void setSpawn(Location spawn) {
		this.spawn = spawn;
	}
	
	public Location getExit() {
		return exit;
	}
	
	public void setExit(Location exit) {
		this.exit = exit;
	}
	
	public boolean isSetUp() {
		return setUp;
	}
	
	public void setSetUp(boolean setUp) {
		this.setUp = setUp;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean isOccupied() {
		return occupied;
	}
	
	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}
	
	public boolean isEditable() {
		return editable;
	}
	
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	public boolean isPlayable() {
		
		return !editable && setUp && enabled;
		
	}
	
	public void teleportToSpawn(Player player) {
		
		player.teleport(LocUtils.centerify(spawn));
		
	}
	
	public void teleportToExit(Player player) {
		
		player.teleport(LocUtils.centerify(exit));
		
	}
	
	public List<BlockWeight> getBlockWeights() {
		
		return blockWeights;
		
	}
	
}
