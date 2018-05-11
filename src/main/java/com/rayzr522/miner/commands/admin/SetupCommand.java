
package com.rayzr522.miner.commands.admin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.rayzr522.miner.arena.Arena;
import com.rayzr522.miner.arena.ArenaControl;
import com.rayzr522.miner.arena.Region;
import com.rayzr522.miner.commands.ArenaCommand;
import com.rayzr522.miner.commands.CommandInfo;
import com.rayzr522.miner.util.Messenger;
import com.rayzr522.miner.util.Msg;
import com.rayzr522.miner.util.PlayerData;
import com.rayzr522.miner.util.TextUtils;

@CommandInfo(desc = "Setup an arena", name = "setup", pattern = "set(up)?", perm = "miner.admin.setup", usage = "/miner setup <arena>")
public class SetupCommand implements ArenaCommand {
	
	public boolean execute(ArenaControl arenaControl, CommandSender sender, String[] args) {
		
		if (args.length < 1) {
			
			Messenger.playerMsg(sender, Msg.NO_ARG, "name");
			return false;
			
		}
		
		if (!arenaControl.arenaExists(args[0])) {
			
			Messenger.playerMsg(sender, Msg.NO_SUCH_ARENA, args[0]);
			Messenger.playerMsg(sender, Msg.ARENA_LIST, TextUtils.arenaListToString(arenaControl));
			return true;
			
		}
		
		Arena arena = arenaControl.getArena(args[0]);
		
		Player player = (Player) sender;
		
		Setup setup = new Setup(player, arena);
		
		Conversation convo = new Conversation(arenaControl.getPlugin(), player, setup);
		
		arenaControl.getPlugin().registerEvents(setup);
		
		convo.addConversationAbandonedListener(setup);
		convo.setLocalEchoEnabled(false);
		convo.begin();
		
		return true;
		
	}
	
	private class Setup implements Prompt, ConversationAbandonedListener, Listener {
		
		private Player player;
		private Arena arena;
		
		private PlayerData data;
		private String next = ChatColor.GRAY + "Type 'help' or '?' for help";
		private List<String> missing;
		
		private Vector a1;
		private Vector a2;
		
		private Vector o1;
		private Vector o2;
		
		private Location spawn = null;
		private Location exit = null;
		
		public static final String HELP = "[?]|h(elp)?";
		public static final String DONE = "done|ex(it)?|leave";
		public static final String MISS = "m(iss(ing)?)?";
		public static final String SHOW = "sh(ow)?";
		
		public static final String ARENA_REGION_TOOL_NAME = "Arena Region";
		public static final String ORE_REGION_TOOL_NAME = "Ore Region";
		public static final String SPAWN_TOOL_NAME = "Spawn";
		public static final String EXIT_TOOL_NAME = "Exit";
		
		public static final int ARENA_REGION_TOOL = 0;
		public static final int ORE_REGION_TOOL = 1;
		public static final int SPAWN_TOOL = 2;
		public static final int EXIT_TOOL = 3;
		public static final int OTHER = 4;
		
		public Setup(Player player, Arena arena) {
			
			this.player = player;
			this.arena = arena;
			
			this.data = new PlayerData(player);
			data.store(true);
			
			player.getInventory().setContents(
			new ItemStack[] {
					null, null, createTool(Material.IRON_AXE, ARENA_REGION_TOOL_NAME, "Set A1", "Set A2"),
					createTool(Material.IRON_AXE, ORE_REGION_TOOL_NAME, "Set O1", "Set O2"), null,
					createTool(Material.IRON_HOE, SPAWN_TOOL_NAME, "Set spawn", null),
					createTool(Material.IRON_HOE, EXIT_TOOL_NAME, "Set exit", null), null, null
			});
			
			missing = new ArrayList<String>();
			
			if (arena.getArenaRegion() == null)
				missing.add("arena region");
			else {
				a1 = arena.getArenaRegion().getMin();
				a2 = arena.getArenaRegion().getMax();
			}
			
			if (arena.getOreRegion() == null)
				missing.add("ore region");
			else {
				o1 = arena.getOreRegion().getMin();
				o2 = arena.getOreRegion().getMax();
			}
			
			if (arena.getSpawn() == null)
				missing.add("spawn");
			else
				spawn = arena.getSpawn();
			
			if (arena.getExit() == null)
				missing.add("exit");
			else
				exit = arena.getExit();
			
		}
		
		public Prompt acceptInput(ConversationContext context, String s) {
			
			if (s.matches(HELP)) {
				
				return help();
				
			} else if (s.matches(DONE)) {
				
				return done();
				
			} else if (s.matches(MISS)) {
				
				return miss();
				
			} else if (s.matches(SHOW)) {
				
				return show();
				
			} else {
				
				return help();
				
			}
			
		}
		
		public boolean blocksForInput(ConversationContext context) {
			return true;
		}
		
		public String getPromptText(ConversationContext context) {
			return this.next;
		}
		
		public void conversationAbandoned(ConversationAbandonedEvent e) {
			
			HandlerList.unregisterAll(this);
			
			finish();
			
		}
		
		public void finish() {
			
			if (missing.isEmpty()) {
				arena.getArenaControl().setUpArena(arena, new Region(a1, a2), new Region(o1, o2), spawn, exit);
			} else {
				setupArenaPartial();
			}
			
		}
		
		public Prompt done() {
			
			data.restore();
			
			updateMissing();
			
			player.sendRawMessage(stillMissing());
			
			return Prompt.END_OF_CONVERSATION;
			
		}
		
		public Prompt help() {
			
			StringBuilder builder = new StringBuilder();
			
			builder.append("\n");
			builder.append(info("Setup Help"));
			builder.append("\n");
			builder.append(infoDesc("help, ?", "Show this help screen."));
			builder.append("\n");
			builder.append(infoDesc("done, exit, leave", "Leave the setup mode."));
			builder.append("\n");
			builder.append(infoDesc("miss", "Tells you what you still haven't set up."));
			builder.append("\n");
			builder.append(infoDesc("show", "Shows you various information\nArena region: green\nOre region: red\nSpawn: blue\nExit: orange"));
			
			next = builder.toString();
			
			return this;
			
		}
		
		public Prompt miss() {
			
			info(stillMissing());
			return this;
			
		}
		
		public Prompt show() {
			
			info("Displaying spawnpoints and regions");
			
			updateMissing();
			setupArenaPartial();
			
			arena.getArenaControl().showArena(player, arena);
			
			return this;
			
		}
		
		public String stillMissing() {
			
			updateMissing();
			
			if (missing.isEmpty()) {
				
				return (Messenger.INFO + "Nothing is missing. The arena is set up!");
				
			}
			
			return (Messenger.WARNING + "The following things are missing:\n" + TextUtils.listToString(missing));
			
		}
		
		public void updateMissing() {
			
			if (missing.contains("arena region")) {
				
				if (a1 != null && a2 != null) {
					
					missing.remove("arena region");
					
				}
				
			}
			
			if (missing.contains("ore region")) {
				
				if (o1 != null && o2 != null) {
					
					missing.remove("ore region");
					
				}
				
			}
			
			if (missing.contains("spawn")) {
				
				if (spawn != null) {
					
					missing.remove("spawn");
					
				}
				
			}
			
			if (missing.contains("exit")) {
				
				if (exit != null) {
					
					missing.remove("exit");
					
				}
				
			}
			
		}
		
		public void setupArenaPartial() {
			
			if (!missing.contains("arena region")) {
				
				arena.setArenaRegion(new Region(a1, a2));
				
			}
			
			if (!missing.contains("ore region")) {
				
				arena.setOreRegion(new Region(o1, o2));
				
			}
			
			if (!missing.contains("spawn")) {
				
				arena.setSpawn(spawn);
				
			}
			
			if (!missing.contains("exit")) {
				
				arena.setExit(exit);
				
			}
			
			updateMissing();
			
		}
		
		public String infoDesc(String info, String desc) {
			
			next = ChatColor.GRAY + info + ChatColor.WHITE + " - " + ChatColor.YELLOW + desc;
			
			return next;
			
		}
		
		public String info(String text) {
			
			next = ChatColor.GRAY + text;
			
			return next;
			
		}
		
		public ItemStack createTool(Material type, String name, String left, String right) {
			
			ItemStack tool = new ItemStack(type);
			
			ItemMeta meta = tool.getItemMeta();
			
			meta.setDisplayName(name);
			
			List<String> lore = new ArrayList<String>();
			
			if (left != null)
				lore.add(ChatColor.RED + "Left click: " + ChatColor.WHITE + left);
			
			if (right != null)
				lore.add(ChatColor.BLUE + "Right click: " + ChatColor.WHITE + right);
			
			meta.setLore(lore);
			
			tool.setItemMeta(meta);
			
			return tool;
			
		}
		
		public int getTool(ItemStack item) {
			
			if (item == null) {
				
				return OTHER;
				
			}
			
			if (item.getType() == Material.AIR) {
				
				return OTHER;
				
			}
			
			String name = item.getItemMeta().getDisplayName();
			
			if (name.equals(ARENA_REGION_TOOL_NAME)) {
				
				return ARENA_REGION_TOOL;
				
			}
			
			if (name.equals(ORE_REGION_TOOL_NAME)) {
				
				return ORE_REGION_TOOL;
				
			}
			
			if (name.equals(SPAWN_TOOL_NAME)) {
				
				return SPAWN_TOOL;
				
			}
			
			if (name.equals(EXIT_TOOL_NAME)) {
				
				return EXIT_TOOL;
				
			}
			
			return OTHER;
			
		}
		
		@EventHandler
		public void onPlayerInteract(PlayerInteractEvent e) {
			
			Player p = e.getPlayer();
			if (!p.equals(player))
				return;
			
			ItemStack item = e.getItem();
			
			if (getTool(item) == ARENA_REGION_TOOL) {
				
				if (!setUpArenaRegion(e))
					return;
				
			} else if (getTool(item) == ORE_REGION_TOOL) {
				
				if (!setUpOreRegion(e))
					return;
				
			} else if (getTool(item) == SPAWN_TOOL) {
				
				if (!setUpSpawn(e))
					return;
				
			} else if (getTool(item) == EXIT_TOOL) {
				
				if (!setUpExit(e))
					return;
				
			}
			
			if (getTool(item) != OTHER) {
				
				e.setUseItemInHand(Event.Result.DENY);
				e.setCancelled(true);
				
				player.sendRawMessage(getPromptText(null));
				
			}
			
		}
		
		@EventHandler
		public void onPlayerQuit(PlayerQuitEvent e) {
			
			if (!e.getPlayer().equals(player)) {
				
				return;
				
			}
			
			done();
			
		}
		
		private boolean setUpArenaRegion(PlayerInteractEvent e) {
			
			if (!e.hasBlock())
				return false;
			
			if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
				
				a1 = e.getClickedBlock().getLocation().toVector();
				info("Set a1");
				return true;
				
			}
			
			if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				
				a2 = e.getClickedBlock().getLocation().toVector();
				info("Set a2");
				return true;
				
			}
			
			return false;
			
		}
		
		private boolean setUpOreRegion(PlayerInteractEvent e) {
			
			if (!e.hasBlock())
				return false;
			
			if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
				
				o1 = e.getClickedBlock().getLocation().toVector();
				info("Set o1");
				return true;
				
			} else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				
				o2 = e.getClickedBlock().getLocation().toVector();
				info("Set o2");
				return true;
				
			}
			
			return false;
			
		}
		
		private boolean setUpSpawn(PlayerInteractEvent e) {
			
			if (!e.hasBlock())
				return false;
			
			if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
				
				spawn = e.getClickedBlock().getLocation();
				spawn.setY(spawn.getY() + 1);
				info("Set spawn");
				return true;
				
			} else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				
				return true;
				
			}
			
			return false;
			
		}
		
		private boolean setUpExit(PlayerInteractEvent e) {
			
			if (!e.hasBlock())
				return false;
			
			if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
				
				exit = e.getClickedBlock().getLocation();
				exit.setY(exit.getY() + 1);
				info("Set exit");
				return true;
				
			} else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				
				return true;
				
			}
			
			return false;
			
		}
		
	}
	
}
