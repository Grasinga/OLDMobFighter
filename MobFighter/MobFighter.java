package network.ethereal.MobFighter;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class MobFighter extends JavaPlugin {
	
	PluginManager pluginmanager;
	Logger log = Logger.getLogger("Minecraft");
	private int defaultSpawnNight = getConfig().getInt("EventNight");
	private int Night = getConfig().getInt("CurrentNight");
	private String worldName = getConfig().getString("WorldName");
	public static String staticWorld = "";
	public static int theNight = 0;
	public static boolean allOut = false;
	private boolean startDay = true;
	private boolean isNight = false;
	private boolean firstGiant = true;
	private boolean firstBabyZombie = true;
	private boolean firstWolfPack = true;

	public void onEnable(){
		log.info("MobFighter enabled!");
		
		pluginmanager = this.getServer().getPluginManager();
		Listener listener = new MobFighterListener(this);
		pluginmanager.registerEvents(listener, this);
		staticWorld = worldName;
		theNight = Night;
		
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
			@SuppressWarnings("deprecation")
			public void run() {
            	if(Bukkit.getServer().getWorld(worldName).getTime() > 13700 && Bukkit.getServer().getWorld(worldName).getTime() < 22700)
            	{
            		MobFighterListener.isDay = false;          		
    				Collection<? extends Player> list = Bukkit.getOnlinePlayers();
    				for(Player a : list)
    				{
    					if(a.getGameMode().equals(GameMode.CREATIVE))
    						for(int i=0;i<getConfig().getList("Creative Immunity").size();i++)
    							if(!(a.getDisplayName().equalsIgnoreCase(getConfig().getList("Creative Immunity").get(i).toString())))
    								a.setGameMode(GameMode.SURVIVAL);
    				}
            		if(!Bukkit.getServer().getWorld(worldName).getPlayers().isEmpty())
            			if(!isNight)
            			{
                			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"scoreboard objectives setDisplay list Player-Kills");  
            				for(Player a : list)
            				{
            					a.teleport(Bukkit.getServer().getWorld(worldName).getSpawnLocation());
            				}
            				startDay = true;
            				isNight = true;
            				Night++;
            				theNight++;
            				Bukkit.broadcastMessage(ChatColor.BLUE + "It's night time! Go out and earn your keep!");
            				Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Night: " + Night);
            				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"region flag spawnShack" + " -w "+ worldName + " mob-damage deny");
            				Bukkit.broadcastMessage(ChatColor.GREEN + (ChatColor.ITALIC + "Spawn protection from mobs will be disabled in 30 seconds!"));
            				spawnProtection();

            				if(Night%defaultSpawnNight == 0)
            				{
            					int r = (int)(Math.random()*100+1);
            					System.out.print("Event Chance = " + r);
            					if(Night%100 == 0)
            					{
            						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"region flag __global__" + " -w " + MobFighter.getTheWorld() + " deny-spawn Slime");
            						allOut = true;
            						r = 10;
            					}
            					else if(r>0 && r<20) // Enderdragon with 20% Chance
            					{
            						Location spawnLoc = new Location(Bukkit.getServer().getWorld(worldName),Bukkit.getServer().getWorld(worldName).getSpawnLocation().getX(),(Bukkit.getServer().getWorld(worldName).getSpawnLocation().getY()+100),Bukkit.getServer().getWorld(worldName).getSpawnLocation().getZ());
            						Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + (ChatColor.BOLD + "The Ender Dragon has Appeared!"));
            						Bukkit.getServer().getWorld(worldName).spawnEntity(spawnLoc, EntityType.ENDER_DRAGON);
            						if(allOut)
            							r = 25;
            					}
            					else if(r>20 && r<30) // Wither with 10% Chance
            					{
            						if(!allOut)
            						{
            							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"region flag __global__" + " -w " + MobFighter.getTheWorld() + " deny-spawn Creeper, Witch, Skeleton, Spider, Zombie, Slime, Enderman");
            							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"butcher");
            						}
            						Location spawnLoc = new Location(Bukkit.getServer().getWorld(worldName),Bukkit.getServer().getWorld(worldName).getSpawnLocation().getX()-20,(Bukkit.getServer().getWorld(worldName).getSpawnLocation().getY()+10),Bukkit.getServer().getWorld(worldName).getSpawnLocation().getZ());
            						Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + (ChatColor.BOLD + "The Wither has Appeared!"));
            						Bukkit.getServer().getWorld(worldName).spawnEntity(spawnLoc, EntityType.WITHER);
            						if(allOut)
            							r = 35;
            					}
            					else if(r>=30 && r<40) // Field of Flowers with 10% Chance
            					{
            						Bukkit.broadcastMessage(ChatColor.GREEN + "A field of flowers appears!");
            						MobFighterListener.flowers = true;
            						if(allOut)
            							r = 45;
            					}
            					else if(r>=40 && r<50) // Lightning Storm 10% Chance
            					{
            						Bukkit.broadcastMessage(ChatColor.GOLD + "A Storm is brewing!");
            						Bukkit.getServer().getWorld(worldName).setStorm(true);
            	            		MobFighterListener.lightning = true;
            						if(allOut)
            							r = 55;
            					}
            					else if(r>=50 && r<60) // Explosive Drops 10% Chance
            					{
            						Bukkit.broadcastMessage(ChatColor.GOLD + "There is a \"Primed Feeling\" in the air...");
            	            		MobFighterListener.tnt = true;
            						if(allOut)
            							r = 63;
            					}
            					else if(r>=60 && r<65) // Hot Potato 5% Chance
            					{
            						Bukkit.broadcastMessage(ChatColor.DARK_RED + "The God of Death has handed down his potato!");
            						int size = (Bukkit.getServer().getWorld(worldName).getPlayers().size());
            						int random = (int)(Math.random()*size);
            						Bukkit.getServer().getWorld(worldName).getPlayers().get(random).getInventory().addItem(new ItemStack(Material.POISONOUS_POTATO));
            						MobFighterListener.reaper = true;
            						if(allOut)
            							r = 70;
            					}
            					else if(r>=65 && r<85) // PvP On 20% Chance
            					{
            						Bukkit.broadcastMessage(ChatColor.DARK_RED + "PvP Enabled!");
            						Bukkit.getServer().getWorld(worldName).setPVP(true);
            						if(allOut)
            							r = 88;
            					}
            					else if(r>=85 && r<90) // Giants 5% Chance
            					{
            						Bukkit.broadcastMessage(ChatColor.DARK_RED + "The four Giants have risen!");
            						MobFighterListener.giants = true;
            						int x = 15;
            						int z = 15;
            						if(!allOut)
            						{
            							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"butcher");
            							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"region flag __global__" + " -w " + MobFighter.getTheWorld() + " deny-spawn Creeper, Witch, Skeleton, Spider, Zombie, Slime, Enderman");
            						}
            						// First
            						Location loc1 = new Location(Bukkit.getServer().getWorld(worldName),(Bukkit.getServer().getWorld(worldName).getSpawnLocation().getX()-x),(Bukkit.getServer().getWorld(worldName).getSpawnLocation().getY()),(Bukkit.getServer().getWorld(worldName).getSpawnLocation().getZ()-z));
            						Bukkit.getServer().getWorld(worldName).spawnEntity(loc1, EntityType.GIANT);
            						MobFighterListener.numOfGiants++;
            						// Second
            						Location loc2 = new Location(Bukkit.getServer().getWorld(worldName),(Bukkit.getServer().getWorld(worldName).getSpawnLocation().getX()+x),(Bukkit.getServer().getWorld(worldName).getSpawnLocation().getY()),(Bukkit.getServer().getWorld(worldName).getSpawnLocation().getZ()-z));
            						Bukkit.getServer().getWorld(worldName).spawnEntity(loc2, EntityType.GIANT);
            						MobFighterListener.numOfGiants++;
            						// Third
            						Location loc3 = new Location(Bukkit.getServer().getWorld(worldName),(Bukkit.getServer().getWorld(worldName).getSpawnLocation().getX()+x),(Bukkit.getServer().getWorld(worldName).getSpawnLocation().getY()),(Bukkit.getServer().getWorld(worldName).getSpawnLocation().getZ()+z));
            						Bukkit.getServer().getWorld(worldName).spawnEntity(loc3, EntityType.GIANT);
            						MobFighterListener.numOfGiants++;
            						// Fourth
            						Location loc4 = new Location(Bukkit.getServer().getWorld(worldName),(Bukkit.getServer().getWorld(worldName).getSpawnLocation().getX()-x),(Bukkit.getServer().getWorld(worldName).getSpawnLocation().getY()),(Bukkit.getServer().getWorld(worldName).getSpawnLocation().getZ()+z));
            						Bukkit.getServer().getWorld(worldName).spawnEntity(loc4, EntityType.GIANT);
            						MobFighterListener.numOfGiants++;
            						firstGiant = true;
            						if(allOut)
            							r = 92;
            					}
            					else if(r>=90 && r<95) // Baby Zombie Swarms 5% Chance
            					{
            						Bukkit.broadcastMessage(ChatColor.DARK_RED + "The Undead Horde has come!");
            						if(!allOut)
            						{
            							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"region flag __global__" + " -w " + MobFighter.getTheWorld() + " deny-spawn Creeper, Witch, Skeleton, Spider, Slime, Enderman, Giant");
            							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"butcher");
            						}
        							Location a = new Location(Bukkit.getServer().getWorld(worldName),(Bukkit.getServer().getWorld(worldName).getSpawnLocation().getBlockX()-20),(Bukkit.getServer().getWorld(worldName).getSpawnLocation().getY()),(Bukkit.getServer().getWorld(worldName).getSpawnLocation().getZ()-20));
        							Location b = new Location(Bukkit.getServer().getWorld(worldName),(Bukkit.getServer().getWorld(worldName).getSpawnLocation().getBlockX()+20),(Bukkit.getServer().getWorld(worldName).getSpawnLocation().getY()),(Bukkit.getServer().getWorld(worldName).getSpawnLocation().getZ()-15));
        							int xMin = Math.min(a.getBlockX(),b.getBlockX());
        							int yMin = Math.min(a.getBlockY(),b.getBlockY());
        							int zMin = Math.min(a.getBlockZ(),b.getBlockZ());
        							int xMax = Math.max(a.getBlockX(),b.getBlockX());
        							int yMax = Math.max(a.getBlockY(),b.getBlockY());
        							int zMax = Math.max(a.getBlockZ(),b.getBlockZ());
        							for (int x = xMin; x<=xMax; x++)
        								for (int y = yMin; y<=yMax; y++)
        									for (int z = zMin; z<=zMax; z++)
            								{
            									Location spawnLoc = new Location(Bukkit.getServer().getWorld(worldName),Bukkit.getServer().getWorld(worldName).getBlockAt(x,y,z).getX(),Bukkit.getServer().getWorld(worldName).getBlockAt(x,y,z).getY(),Bukkit.getServer().getWorld(worldName).getBlockAt(x,y,z).getZ());
                								Zombie zombie = (Zombie) Bukkit.getServer().getWorld(worldName).spawnEntity(spawnLoc, EntityType.ZOMBIE);
                								zombie.setBaby(true);
            								}
            						firstBabyZombie = true;
            	            		MobFighterListener.babyZombies = true;
            						if(allOut)
            							r = 98;
            					}
            					else if(r>=95 && r<=100) // Corrupted Wolf Pack 5% Chance
            					{
            						Bukkit.broadcastMessage(ChatColor.WHITE + "The Corrupted Wolves have come!");
            						if(!allOut)
            						{
            							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"region flag __global__" + " -w " + MobFighter.getTheWorld() + " deny-spawn Creeper, Witch, Skeleton, Zombie, Spider, Slime, Enderman, Giant");
            							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"butcher");
            						}
        							Location a = new Location(Bukkit.getServer().getWorld(worldName),(Bukkit.getServer().getWorld(worldName).getSpawnLocation().getBlockX()-20),(Bukkit.getServer().getWorld(worldName).getSpawnLocation().getY()),(Bukkit.getServer().getWorld(worldName).getSpawnLocation().getZ()-20));
        							Location b = new Location(Bukkit.getServer().getWorld(worldName),(Bukkit.getServer().getWorld(worldName).getSpawnLocation().getBlockX()+20),(Bukkit.getServer().getWorld(worldName).getSpawnLocation().getY()),(Bukkit.getServer().getWorld(worldName).getSpawnLocation().getZ()-10));
        							int xMin = Math.min(a.getBlockX(),b.getBlockX());
        							int yMin = Math.min(a.getBlockY(),b.getBlockY());
        							int zMin = Math.min(a.getBlockZ(),b.getBlockZ());
        							int xMax = Math.max(a.getBlockX(),b.getBlockX());
        							int yMax = Math.max(a.getBlockY(),b.getBlockY());
        							int zMax = Math.max(a.getBlockZ(),b.getBlockZ());
        							for (int x = xMin; x<=xMax; x++)
        								for (int y = yMin; y<=yMax; y++)
        									for (int z = zMin; z<=zMax; z++)
            								{
            									Location spawnLoc = new Location(Bukkit.getServer().getWorld(worldName),Bukkit.getServer().getWorld(worldName).getBlockAt(x,y,z).getX(),Bukkit.getServer().getWorld(worldName).getBlockAt(x,y,z).getY(),Bukkit.getServer().getWorld(worldName).getBlockAt(x,y,z).getZ());
                								Wolf w = (Wolf) Bukkit.getServer().getWorld(worldName).spawnEntity(spawnLoc, EntityType.WOLF);
                								w.setAngry(true);
                								List<Entity> entities = w.getNearbyEntities(10, 10, 10);
                								for(Entity e : entities)
                									if(e.getType().equals(EntityType.PLAYER))
                										w.setTarget((LivingEntity) e);
            								}
            						firstWolfPack = true;
            						MobFighterListener.wolfPack = true;
            						if(allOut)
            							allOut = false;
            					}
            				}
            			}
            	}
            	else if(Bukkit.getServer().getWorld(worldName).getTime() >= 0 && Bukkit.getServer().getWorld(worldName).getTime() < 13700)
            	{
            		if(startDay)
            		{
        				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"scoreboard objectives setDisplay list $Money$Top$");
            			Collection<? extends Player> list = Bukkit.getOnlinePlayers();
            			for(Player a : list)
            			{
            				a.teleport(Bukkit.getServer().getWorld(worldName).getSpawnLocation());
        					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"scoreboard players set " + a.getDisplayName() + " $Money$Top$ " + (int) VaultEco.getEconomy().getBalance(a.getDisplayName()));
            			}
        				Bukkit.broadcastMessage(ChatColor.GOLD + "It's day time! Get prepared for the next night!");
        				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"region flag __global__" + " -w "+ worldName + " deny-spawn Slime");
        				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"butcher -a");
        				startDay = false;
            		}
    				MobFighterListener.isDay = true;
    				if(Bukkit.getServer().getWorld(worldName).getTime() == 12700)
    					Bukkit.broadcastMessage(ChatColor.GOLD + "It will be night in 50 seconds!");
            		Bukkit.getServer().getWorld(worldName).setPVP(false);
            		if(Night > 1000000)
            		{
            			Night = 0;
            			theNight = 0;
            		}
            		if(isNight)
            			isNight = false;
            		if(MobFighterListener.lightning)
            		{
                		Bukkit.broadcastMessage(ChatColor.GOLD + "The Storm seems to have subsided.");
                		Bukkit.getServer().getWorld(worldName).setStorm(false);
            			MobFighterListener.lightning = false;
            		}
            		if(MobFighterListener.tnt)
            		{
            			Bukkit.broadcastMessage(ChatColor.GOLD + "The air feels normal again.");
            			MobFighterListener.tnt = false;
            		}
            		if(MobFighterListener.reaper)
            		{
            			for(int i=0;i<Bukkit.getServer().getWorld(worldName).getPlayers().size();i++)
            			{
            				if(Bukkit.getServer().getWorld(worldName).getPlayers().get(i).getInventory().contains(Material.POISONOUS_POTATO))
            				{
            					Bukkit.getServer().getWorld(worldName).getPlayers().get(i).getInventory().remove(Material.POISONOUS_POTATO);
								Bukkit.getServer().getWorld(worldName).getPlayers().get(i).damage(20);
            				}
            			}
					MobFighterListener.reaper = false;
					MobFighterListener.hasPotato = "";
            		}
            		if(firstGiant)
            		{
            			firstGiant = false;
            		}
            		MobFighterListener.giants = false;
            		if(firstBabyZombie)
            		{
            			firstBabyZombie = false;
            		}
            		MobFighterListener.babyZombies = false;
            		if(firstWolfPack)
            		{
            			firstWolfPack = false;
            		}
					MobFighterListener.wolfPack = false;
            	}
            }
        }, 0L, 1L);
        
		// Commands:
        getCommand("mfreload").setExecutor(new MobFighterCommands());
		getCommand("ready").setExecutor(new MobFighterCommands());
		getCommand("night").setExecutor(new MobFighterCommands());
		getCommand("craft").setExecutor(new MobFighterCommands());
		getCommand("getbook").setExecutor(new MobFighterCommands());
		getCommand("exchange").setExecutor(new MobFighterCommands());
		getCommand("setBoards").setExecutor(new MobFighterCommands());
		getCommand("getshop").setExecutor(new MobFighterCommands());
		
		// Configuration file:
		getConfig().options().copyDefaults(true);
		saveConfig();
	}    
    
	public void onDisable(){
		getConfig().set("CurrentNight", getNight());
		getConfig().setDefaults(getConfig());
		saveConfig();
		log.info("MobFighter disabled.");
	}
	
	public static int getNight()
	{
		return theNight;
	}
	
	public static String getTheWorld()
	{
		return staticWorld;
	}
	
	public static boolean isAllOut()
	{
		return allOut;
	}
	
	public void spawnProtection()
	{
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() 
		{
			public void run() 
			{
				if(isNight)
				{
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"region flag spawnShack" + " -w "+ worldName + " mob-damage");
					Bukkit.broadcastMessage(ChatColor.RED + (ChatColor.ITALIC + "Spawn protection from mobs is now disabled!"));
				}
				else
					Bukkit.broadcastMessage(ChatColor.RED + (ChatColor.ITALIC + "Spawn protection was not disabled because it's daytime."));
			}
		}, 20*30);
	}
}

