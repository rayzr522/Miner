
package com.rayzr522.miner.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.rayzr522.miner.Miner;
import com.rayzr522.miner.arena.Arena;
import com.rayzr522.miner.arena.ArenaControl;
import com.rayzr522.miner.arena.ArenaPlayer;
import com.rayzr522.miner.util.ConfigUtils;
import com.rayzr522.miner.util.Messenger;
import com.rayzr522.miner.util.Msg;

public class MinerEventListener implements Listener {
	
	private Miner plugin;
	private ArenaControl arenaControl;
	
	public MinerEventListener(Miner plugin, ArenaControl arenaControl) {
		
		this.plugin = plugin;
		this.arenaControl = arenaControl;
		
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockBreak(BlockBreakEvent e) {
		
		if (arenaControl.getArenaPlayer(e.getPlayer()) == null) {
			
			return;
			
		}
		
		ArenaPlayer arenaPlayer = arenaControl.getArenaPlayer(e.getPlayer());
		Arena arena = arenaPlayer.getArena();
		
		if (!arena.getOreRegion().inside(e.getBlock())) {
			
			e.setCancelled(true);
			return;
			
		}
		
		ItemStack handItem = arenaPlayer.getPlayer().getItemInHand();
		
		if (handItem != null && handItem.getType() != Material.AIR) {
			
			if (handItem.getType().toString().endsWith("_PICKAXE")) {
				
				handItem.setDurability((short) 0);
				
			}
			
		}
		
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockPlace(BlockPlaceEvent e) {
		
		if (arenaControl.getArenaPlayer(e.getPlayer()) == null) {
			
			return;
			
		}
		
		ArenaPlayer arenaPlayer = arenaControl.getArenaPlayer(e.getPlayer());
		Arena arena = arenaPlayer.getArena();
		
		if (!arena.getOreRegion().inside(e.getBlock())) {
			
			e.setCancelled(true);
			return;
			
		}
		
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onMove(PlayerMoveEvent e) {
		
		if (arenaControl.getArenaPlayer(e.getPlayer()) == null) {
			
			return;
			
		}
		
		if (e.getPlayer().getFoodLevel() < 20) {
			
			e.getPlayer().setFoodLevel(20);
			
		}
		
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onDamage(EntityDamageEvent e) {
		
		if (e.getEntity() instanceof Player) {
			
			Player player = (Player) e.getEntity();
			
			if (arenaControl.getArenaPlayer(player) != null) {
				
				e.setCancelled(true);
				
			}
			
		}
		
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityExplode(EntityExplodeEvent e) {
		
		for (Block block : e.blockList()) {
			
			for (Arena arena : arenaControl.getArenas()) {
				
				if (arena.getArenaRegion().inside(block)) {
					
					e.setCancelled(true);
					
				}
				
			}
			
		}
		
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
		
		if (arenaControl.getArenaPlayer(e.getPlayer()) != null) {
			
			if (!ConfigUtils.isCommandEnabled(e.getMessage())) {
				
				e.setCancelled(true);
				Messenger.playerMsg(e.getPlayer(), Msg.COMMAND_NOT_ALLOWED,
				e.getMessage().contains(" ") ? e.getMessage().split(" ")[0] : e.getMessage());
				
			}
			
		}
		
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerQuit(PlayerQuitEvent e) {
		
		if (arenaControl.getArenaPlayer(e.getPlayer()) != null) {
			
			arenaControl.leaveArena(e.getPlayer());
			
		}
		
	}
	
}
