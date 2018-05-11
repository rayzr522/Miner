
package com.rayzr522.miner.util;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerData {
	
	private Player player;
	
	private boolean canFly;
	private boolean isFlying;
	
	private double health;
	private float exp;
	private int food;
	
	private ItemStack[] inv;
	private ItemStack[] armor;
	
	private GameMode gameMode;
	
	private boolean stored = false;
	
	public PlayerData(Player player, boolean store, boolean clear) {
		
		this.player = player;
		
		if (store) {
			store(clear);
		}
		
	}
	
	public PlayerData(Player player) {
		
		this(player, false, false);
		
	}
	
	public void clearPlayer() {
		
		player.getInventory().clear();
		player.setHealth(20);
		player.setFoodLevel(20);
		
	}
	
	public PlayerData store(boolean clear) {
		
		canFly = player.getAllowFlight();
		isFlying = player.isFlying();
		
		health = player.getHealth();
		exp = player.getExp();
		food = player.getFoodLevel();
		
		inv = player.getInventory().getContents();
		armor = player.getInventory().getArmorContents();
		
		gameMode = player.getGameMode();
		
		stored = true;
		
		if (clear) {
			
			clearPlayer();
			
		}
		
		return this;
		
	}
	
	public PlayerData restore() {
		
		if (!stored) {
			return null;
		}
		
		player.setAllowFlight(canFly);
		player.setFlying(isFlying);
		
		player.setHealth(health);
		player.setExp(exp);
		player.setFoodLevel(food);
		
		player.getInventory().setContents(inv);
		player.getInventory().setArmorContents(armor);
		
		player.setGameMode(gameMode);
		
		return this;
		
	}
	
}
