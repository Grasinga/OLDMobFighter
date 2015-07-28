package network.ethereal.MobFighter;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class MobFighterCommands implements CommandExecutor{
	private int countDay = 0;
	private int countNight = 0;
	private boolean day = false;
	private boolean night = false;
	World w = Bukkit.getServer().getWorld(MobFighter.getTheWorld());
	private List<Player> playersOnServer = w.getPlayers();
	private int numberOfPlayers = 0;
	private PlayerNames playerNames = new PlayerNames();
	
	public MobFighterCommands()
	{
		numberOfPlayers = getNumberOfPlayers();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if(commandLabel.equalsIgnoreCase("night"))
		{
			sender.sendMessage(ChatColor.DARK_AQUA + ("Night: " + MobFighter.getNight()));
			return true;
		}
		else if(commandLabel.equalsIgnoreCase("setBoards"))
		{			
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"scoreboard objectives add Player-Kills playerKillCount");
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"scoreboard objectives add /Total\\/Kills\\ totalKillCount");
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"scoreboard objectives add Health health");
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"scoreboard objectives add $Money$Top$ dummy");
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"scoreboard objectives setDisplay list Player-Kills");
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"scoreboard objectives setDisplay sidebar /Total\\/Kills\\");
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"scoreboard objectives setDisplay belowName Health");
		}		
		else if(commandLabel.equalsIgnoreCase("mfreload")){
			try{
				 Bukkit.getServer().getPluginManager().disablePlugin(Bukkit.getServer().getPluginManager().getPlugin("MobFighter"));
				 Bukkit.getServer().getPluginManager().enablePlugin(Bukkit.getServer().getPluginManager().getPlugin("MobFighter"));
				 sender.sendMessage(ChatColor.GREEN + "MobFighter has been reloaded!");
				}catch(Exception e){sender.sendMessage("Error: " + e); return false;} // Fails to reload.
				return true;
		}
		else if (sender instanceof Player) 
		{
			Player player = (Player) sender;
			
			if(commandLabel.equalsIgnoreCase("ready"))
			{
				if(w.getTime() > 0 && w.getTime() < 13700)
				{
					day = true;
					night = false;
				}
				else if(w.getTime() > 13700 && w.getTime() < 24000)
				{
					day = false;
					night = true;
				}
				playersOnServer = w.getPlayers();
				numberOfPlayers = getNumberOfPlayers();
				
				if(!(args.length<=0) && args[0].equalsIgnoreCase("clear"))
				{
					playerNames.removeName(player.getDisplayName());
					if(countDay > 0)
						countDay--;
					else if(countNight > 0)
						countNight--;
					player.chat(ChatColor.RED + "Unreadied");
					return true;
				}
				else if(!(args.length<=0) && args[0].equalsIgnoreCase("list"))
				{
					
					player.sendMessage("Players Readied: " + ChatColor.GREEN + playerNames.getAllNames());
				}
				else if((getHasReadied(player.getDisplayName())))
				{
					player.sendMessage("You have already readied! Type: /ready clear to ready up again.");
					return true;
				}
				else if(!(getHasReadied(player.getDisplayName())))
				{
					if(args.length <= 0)
					{
						player.sendMessage("Please type: /ready night | /ready day | /ready clear | /ready list");
						return true;
					}
					else if(args.length == 1)
					{
						if(args[0].equalsIgnoreCase("day")&&night)
						{
							countDay++;
							player.chat(ChatColor.GREEN + "Readied for Day!");
							playerNames.addName(player.getDisplayName());
							if(countDay >= numberOfPlayers)
							{
								countDay = 0;
								countNight = 0;
								w.setTime(0);
								playerNames.removeAll();
								return true;
							}
							return true;
						}
						else if(args[0].equalsIgnoreCase("night")&&day)
						{
							countNight++;
							player.chat(ChatColor.GREEN + "Readied for Night!");
							playerNames.addName(player.getDisplayName());
							if(countNight >= numberOfPlayers)
							{
								countDay = 0;
								countNight = 0;
								w.setTime(13700);
								playerNames.removeAll();
								return true;
							}
							return true;
						}
						else
						{
							player.sendMessage("Please type: /ready night | /ready day | /ready clear | /ready list (You can't ready for day if it is already daytime!)");
							return false;
						}
					}
					else
					{
						player.sendMessage("Please type: /ready night | /ready day | /ready clear | /ready list");
						return true;
					}
				}
			}
			else if(commandLabel.equalsIgnoreCase("craft"))
			{
				if(player.getItemInHand().getType().equals(Material.WORKBENCH))
				{
					Inventory inv = Bukkit.getServer().createInventory(null, InventoryType.WORKBENCH);
					player.openInventory(inv);
					player.openWorkbench(null, true);
					return true;
				}
				else
					player.sendMessage("You need a Workbench in your hand in order to craft!");
				return false;
			}
			else if(commandLabel.equalsIgnoreCase("getbook"))
			{
				if(!(args.length<=0)&&(!(args.length>=2))&&args[0].equalsIgnoreCase("starter"))
				{
					ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
					BookMeta bm = (BookMeta) book.getItemMeta();
					// Book Page Per Line
					bm.setPages(Arrays.asList(
							"Welcome to:\nMobFighter!\n\nThis server is a\nmob fighting game!\nYou kill mobs and\npick up their loot\nat night so you\ncan sell it\nduring the day to\nget better gear!\nAs you play you\nmay notice a\n",
							"message saying \n\"Night: #\" this\nlets you know the\ncurrent night and\nhelps you prepare\nfor the events!\nEvents happen every\nfive waves varying\nfrom fighting the\nEnderdragon, to\nstrolling in a\nmeadow of flowers.\n",
							"Commands:\n/getshop\n/ready day\n/ready night\n/ready clear\n/ready list\n/night\n/craft\n/call <username>\n/bring <username>\n/exchange\n/getbook <name>",
							"Emojies Spice up\nchat!\n\nType:\n/help mmemoji\n/help mmemoji 2\n/help mmemoji 3\n\nSee what emojies\nyou can use!\n\nThanks for reading!"));
					bm.setAuthor("Mob Fighter");
					bm.setTitle("Mob Fighter Info");
					book.setItemMeta(bm);
					player.getInventory().addItem(book);
				}
				else if(!(args.length<=0)&&(!(args.length>=2))&&args[0].equalsIgnoreCase("anvil"))
				{
					ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
					BookMeta bm = (BookMeta) book.getItemMeta();
					// Book Page Per Line
					bm.setPages(Arrays.asList(
							"The anvil has a restriction set on any enchament combines that are over level 40.\n\nMinecraft Wiki:\n\nIf the job would cost 40 or more levels, it will be rejected as \"Too Expensive!\". (This does not apply in creative mode.)",
							"So in order to combine items that are \"Too Expensive!\" a player can buy creative in order to combine the two items. The player also has to have at least 60 levels!"));
					bm.setAuthor("Mob Fighter");
					bm.setTitle("Anvil - Creative");
					book.setItemMeta(bm);
					player.getInventory().addItem(book);
				}
				else
					player.sendMessage("Current Books: starter and anvil.");
			}
			else if(commandLabel.equalsIgnoreCase("exchange"))
			{
				if(!(args.length<=0)&&(!(args.length>=2))&&args[0].equalsIgnoreCase("demon"))
				{
					ItemStack coal = new ItemStack(Material.COAL);
					coal.setItemMeta(MobFighterListener.undeadHeart.getItemMeta());
					ItemStack fCharge = new ItemStack(Material.FIREWORK_CHARGE);
					ItemMeta meta = fCharge.getItemMeta();
					meta.setDisplayName(ChatColor.DARK_PURPLE + "Festering Darkness");
					fCharge.setItemMeta(meta);
					if(player.getInventory().containsAtLeast(coal, 2304))
					{
						player.getInventory().clear();
						player.getInventory().addItem(fCharge);
					}
					else if(player.getInventory().containsAtLeast(fCharge, 5))
					{
						player.getInventory().clear();
						ItemStack button = new ItemStack(Material.STONE_BUTTON);
						ItemStack blazerod = new ItemStack(Material.BLAZE_ROD);
						ItemStack workbench = new ItemStack(Material.WORKBENCH);
						ItemStack brick = new ItemStack(Material.BRICK);
						ItemStack dh = new ItemStack(Material.DIAMOND_HELMET);
						ItemMeta meta1 = dh.getItemMeta();
						meta1.setDisplayName(ChatColor.DARK_RED + "Minotuar's Horns");
						dh.setItemMeta(meta1);
						dh.addUnsafeEnchantment(new EnchantmentWrapper(0), 10);
						dh.addUnsafeEnchantment(new EnchantmentWrapper(7), 20);
						dh.addUnsafeEnchantment(new EnchantmentWrapper(34), 20);
						ItemStack dc = new ItemStack(Material.DIAMOND_CHESTPLATE);
						ItemMeta meta2 = dc.getItemMeta();
						meta2.setDisplayName(ChatColor.DARK_RED + "Cerberus' Breastplate");
						dc.setItemMeta(meta2);
						dc.addUnsafeEnchantment(new EnchantmentWrapper(0), 10);
						dc.addUnsafeEnchantment(new EnchantmentWrapper(3), 20);
						dc.addUnsafeEnchantment(new EnchantmentWrapper(34), 20);
						ItemStack dl = new ItemStack(Material.DIAMOND_LEGGINGS);
						ItemMeta meta3 = dc.getItemMeta();
						meta3.setDisplayName(ChatColor.DARK_RED + "Hades' Guard");
						dl.setItemMeta(meta3);
						dl.addUnsafeEnchantment(new EnchantmentWrapper(0), 10);
						dl.addUnsafeEnchantment(new EnchantmentWrapper(1), 20);
						dl.addUnsafeEnchantment(new EnchantmentWrapper(34), 20);
						ItemStack db = new ItemStack(Material.DIAMOND_BOOTS);
						ItemMeta meta4 = dc.getItemMeta();
						meta4.setDisplayName(ChatColor.DARK_RED + "Titan's Fortitude");
						db.setItemMeta(meta4);
						db.addUnsafeEnchantment(new EnchantmentWrapper(0), 10);
						db.addUnsafeEnchantment(new EnchantmentWrapper(4), 20);
						db.addUnsafeEnchantment(new EnchantmentWrapper(34), 20);
						ItemStack ds = new ItemStack(Material.DIAMOND_SWORD);
						ItemMeta meta5 = ds.getItemMeta();
						meta5.setDisplayName(ChatColor.DARK_RED + "Pandemonium");
						ds.setItemMeta(meta5);
						ds.addUnsafeEnchantment(new EnchantmentWrapper(16), 20);
						ds.addUnsafeEnchantment(new EnchantmentWrapper(20), 20);
						ds.addUnsafeEnchantment(new EnchantmentWrapper(34), 20);
						player.getInventory().addItem(dh);
						player.getInventory().addItem(dc);
						player.getInventory().addItem(dl);
						player.getInventory().addItem(db);
						player.getInventory().addItem(ds);
						player.getInventory().addItem(button);
						player.getInventory().addItem(blazerod);
						player.getInventory().addItem(workbench);
						player.getInventory().addItem(brick);
					}
					else
						player.sendMessage(ChatColor.GRAY + "You seem to be lacking substance.");
				}
				else if(!(args.length<=0)&&(!(args.length>=2))&&args[0].equalsIgnoreCase("sage"))
				{
					ItemStack emerald = new ItemStack(Material.EMERALD);
					emerald.setItemMeta(MobFighterListener.taintedSoul.getItemMeta());
					ItemStack star = new ItemStack(Material.NETHER_STAR);
					ItemMeta meta = star.getItemMeta();
					meta.setDisplayName(ChatColor.GREEN + "Swirling Souls");
					star.setItemMeta(meta);
					if(player.getInventory().containsAtLeast(emerald, 2304))
					{
						player.getInventory().clear();
						player.getInventory().addItem(star);
					}
					else if(player.getInventory().containsAtLeast(star, 5))
					{
						player.getInventory().clear();
						ItemStack button = new ItemStack(Material.STONE_BUTTON);
						ItemStack blazerod = new ItemStack(Material.BLAZE_ROD);
						ItemStack workbench = new ItemStack(Material.WORKBENCH);
						ItemStack brick = new ItemStack(Material.BRICK);
						ItemStack dh = new ItemStack(Material.DIAMOND_HELMET);
						ItemMeta meta1 = dh.getItemMeta();
						meta1.setDisplayName(ChatColor.AQUA + "Posiden's Helm");
						dh.setItemMeta(meta1);
						dh.addUnsafeEnchantment(new EnchantmentWrapper(0), 10);
						dh.addUnsafeEnchantment(new EnchantmentWrapper(5), 20);
						dh.addUnsafeEnchantment(new EnchantmentWrapper(34), 20);
						ItemStack dc = new ItemStack(Material.DIAMOND_CHESTPLATE);
						ItemMeta meta2 = dc.getItemMeta();
						meta2.setDisplayName(ChatColor.AQUA + "Zeus' Might");
						dc.setItemMeta(meta2);
						dc.addUnsafeEnchantment(new EnchantmentWrapper(0), 10);
						dc.addUnsafeEnchantment(new EnchantmentWrapper(3), 20);
						dc.addUnsafeEnchantment(new EnchantmentWrapper(34), 20);
						ItemStack dl = new ItemStack(Material.DIAMOND_LEGGINGS);
						ItemMeta meta3 = dc.getItemMeta();
						meta3.setDisplayName(ChatColor.AQUA + "Anthena's Wrath");
						dl.setItemMeta(meta3);
						dl.addUnsafeEnchantment(new EnchantmentWrapper(0), 10);
						dl.addUnsafeEnchantment(new EnchantmentWrapper(1), 20);
						dl.addUnsafeEnchantment(new EnchantmentWrapper(34), 20);
						ItemStack db = new ItemStack(Material.DIAMOND_BOOTS);
						ItemMeta meta4 = dc.getItemMeta();
						meta4.setDisplayName(ChatColor.AQUA + "Hermes' Boots");
						db.setItemMeta(meta4);
						db.addUnsafeEnchantment(new EnchantmentWrapper(0), 10);
						db.addUnsafeEnchantment(new EnchantmentWrapper(4), 20);
						db.addUnsafeEnchantment(new EnchantmentWrapper(34), 20);
						ItemStack ds = new ItemStack(Material.DIAMOND_SWORD);
						ItemMeta meta5 = ds.getItemMeta();
						meta5.setDisplayName(ChatColor.AQUA + "Nirvana");
						ds.setItemMeta(meta5);
						ds.addUnsafeEnchantment(new EnchantmentWrapper(16), 20);
						ds.addUnsafeEnchantment(new EnchantmentWrapper(19), 20);					
						ds.addUnsafeEnchantment(new EnchantmentWrapper(34), 20);
						player.getInventory().addItem(dh);
						player.getInventory().addItem(dc);
						player.getInventory().addItem(dl);
						player.getInventory().addItem(db);
						player.getInventory().addItem(ds);
						player.getInventory().addItem(button);
						player.getInventory().addItem(blazerod);
						player.getInventory().addItem(workbench);
						player.getInventory().addItem(brick);
				}
					else
						player.sendMessage(ChatColor.GRAY + "You seem to be lacking substance.");
			}
				else
					player.sendMessage(ChatColor.GRAY + "You seem to be lacking a path.");
				return true;
		}
		else if(commandLabel.equalsIgnoreCase("getshop"))
		{
			ItemStack paper = new ItemStack(Material.PAPER);
			ItemMeta paperMeta = paper.getItemMeta();
			paperMeta.setDisplayName(ChatColor.BLUE + "Shop");
			paper.setItemMeta(paperMeta);
			player.getInventory().addItem(paper);
			player.sendMessage(ChatColor.BLUE + "Right-click the piece of paper to open the shop!");
		}
		else  if(sender instanceof ConsoleCommandSender)
		{
			System.out.print("You must be a player to use that command!");
			return false;
		}
		return false;
	}
		
		return false;
}
	
	public int getNumberOfPlayers()
	{
		int players = 0;
		for(int i=0; i<playersOnServer.size();i++)
		{
			players++;
		}
		return players;
	}
	
	public boolean getHasReadied(String name)
	{
		for(int i=0;i<playerNames.getSize();i++)
		{
			if(playerNames.getName(i).equalsIgnoreCase(name))
				return true;
		}
		return false;
	}
}