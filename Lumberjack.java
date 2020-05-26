package lumberjack;

import java.util.List;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class Lumberjack extends JavaPlugin implements Listener {

	public HashMap<Player, Integer> toggleMap = new HashMap<>();

	@Override
	public void onEnable() {
		//Implements event listener
		getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent joinEvent) {
		//Adds players to the hashmap as they join, with a value of 0
		toggleMap.put(joinEvent.getPlayer(), 0);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if (sender instanceof Player) {

			Player player = (Player) sender;

			if ((commandLabel.equalsIgnoreCase("lumberjack")) && (toggleMap.get(player.getPlayer()) == 0)) {
				player.sendMessage(ChatColor.GREEN + "Lumberjack has been enabled!");
				toggleMap.replace(player.getPlayer(), 1);

			}

			else if ((commandLabel.equalsIgnoreCase("lumberjack")) && (toggleMap.get(player.getPlayer()) == 1)) {
				player.sendMessage(ChatColor.RED + "Lumberjack has been disabled");
				toggleMap.replace(player.getPlayer(), 0);

			}

		}

		else {

			getLogger().info("Command can't be sent from the console!");
			
		}

		return false;

	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {

		if (toggleMap.get(e.getPlayer()) == 1) {
		
			// Defines list of valid tools for players to use to chop down an entire tree
			List<String> vt = new ArrayList<String>();
			vt.add("WOODEN_AXE");
			vt.add("STONE_AXE");
			vt.add("IRON_AXE");
			vt.add("GOLD_AXE");
			vt.add("DIAMOND_AXE");

			Player p = e.getPlayer();
			String ih = p.getInventory().getItemInMainHand().getType().name();
			String tbn = e.getBlock().getType().name();

			World w = p.getWorld();
			int x = e.getBlock().getX();
			int y = e.getBlock().getY();
			int z = e.getBlock().getZ();
			String utbn = w.getBlockAt(x, y - 1, z).getType().name();
			String atbn;

			if (vt.contains(ih) && (utbn.contains("DIRT") || utbn.contains("GRASS")) && (tbn.contains("LOG") || tbn.contains("LOG_2"))) {

				Boolean logIsAbove = true;
				while (logIsAbove == true) {

					atbn = w.getBlockAt(x, y + 1, z).getType().name();

					if (atbn.contains("LOG") || atbn.contains("LOG_2")) {
						
						w.getBlockAt(x, y + 1, z).breakNaturally();
						y = y + 1;

					} 

					else {
						
						logIsAbove = false;

					}
				}
			}	
		} 
	}
}
