
package com.rayzr522.miner.arena;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.rayzr522.miner.util.PlayerData;

public class ArenaPlayer {
	
	private Player player;
	private PlayerData playerData;
	private Arena arena;
	private ArenaControl arenaControl;
	
	public ArenaPlayer(Player player, Arena arena, ArenaControl arenaControl) {
		
		this.player = player;
		this.playerData = new PlayerData(player);
		this.arena = arena;
		this.arenaControl = arenaControl;
		
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public Arena getArena() {
		return arena;
	}
	
	public void setArena(Arena arena) {
		this.arena = arena;
	}
	
	public ArenaControl getArenaControl() {
		return arenaControl;
	}
	
	public void setArenaControl(ArenaControl arenaControl) {
		this.arenaControl = arenaControl;
	}

	public void storeData() {
		
		playerData.store(true);
		
		player.setGameMode(GameMode.SURVIVAL);
		
	}
	
	public void restoreData() {
		
		playerData.restore();
		
	}
	
}
