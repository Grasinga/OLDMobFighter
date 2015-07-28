package network.ethereal.MobFighter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Giant;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Wither;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MobFighterListener implements Listener{
	
	MobFighter main;
	private static Economy econ = VaultEco.getEconomy();
	public static ArrayList<String> mobDrops = new ArrayList<String>();
    public static ArrayList<String> mobDropPrices = new ArrayList<String>();
	public static boolean flowers = false;
	public static boolean isDay = true;
	public static boolean lightning = false;
	public static boolean tnt = false;
	public static boolean reaper = false;
	public static boolean giants = false;
	public static boolean babyZombies = false;
	public static boolean wolfPack = false;
	public static ItemStack taintedSoul = new ItemStack(Material.EMERALD);
	public static ItemStack undeadHeart = new ItemStack(Material.COAL);
	public static String hasPotato = "";
	private boolean flowersOver = false;
	public static int numOfGiants = 0;
	private int giantKills = 0;
	private ArrayList<BlockState> blockStates = new ArrayList<BlockState>();
	private int fixBlockDelay = 0;
	private Location nuke = new Location(Bukkit.getWorld(MobFighter.getTheWorld()), 0, 0, 0);
	
	public MobFighterListener(MobFighter main)
	{
		this.main = main;
		ItemMeta meta = taintedSoul.getItemMeta();
		meta.setDisplayName(ChatColor.DARK_GREEN + "Tainted Soul");
		taintedSoul.setItemMeta(meta);
		ItemMeta meta2 = undeadHeart.getItemMeta();
		meta2.setDisplayName(ChatColor.LIGHT_PURPLE + "Undead Heart");
		undeadHeart.setItemMeta(meta2);
		fillDrops();
		fillDropPrices();
	}
	
	private void fillDrops() {
		List<String> list = main.getConfig().getStringList("MobDrops");
		String[] a = list.toArray(new String[0]);
		for(int i=0;i<a.length;i++)
		{
			mobDrops.add(a[i]);
		}
	}

	private void fillDropPrices() {
		List<String> list = main.getConfig().getStringList("MobDropPrice");
		String[] a = list.toArray(new String[0]);
		for(int i=0;i<a.length;i++)
		{
			mobDropPrices.add(a[i]);
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.NORMAL)
	public void logIn(PlayerLoginEvent e)
	{
		Player p = e.getPlayer();
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"scoreboard players set " + p.getDisplayName() + " $Money$Top$ " + (int) econ.getBalance(p.getDisplayName()));
		try
		{
			p.setHealthScale(main.getConfig().getDouble("HealthScale."+p.getDisplayName()));
		}
		catch(Exception ex){}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void playerJoin(PlayerJoinEvent e)
	{	
		Player p = e.getPlayer();
		if(p.hasPlayedBefore()){ return; }
		ItemStack paper = new ItemStack(Material.PAPER);
		ItemMeta paperMeta = paper.getItemMeta();
		paperMeta.setDisplayName(ChatColor.BLUE + "Shop");
		paper.setItemMeta(paperMeta);
		e.getPlayer().getInventory().addItem(paper);
		e.getPlayer().sendMessage(ChatColor.BLUE + "Right-click the piece of paper to open the shop!");
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void logOff(PlayerQuitEvent e)
	{
		Player p = e.getPlayer();
		if(p.getHealthScale() > 20)
		{
			main.getConfig().set("HealthScale."+p.getDisplayName(), p.getHealthScale());
			main.saveConfig();
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.NORMAL)
	public void playerClick(PlayerInteractEvent event)
	{
		final Player player = event.getPlayer();
		
		//Sell Drops:
		if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
		{
			for(int a=0;a<mobDrops.size();a++)
			{
				if(player.getItemInHand().getType().toString().equals(mobDrops.get(a).toString()))
				{
					double dropPrice = Double.parseDouble(mobDropPrices.get(a));
					dropPrice *= player.getItemInHand().getAmount();
					player.setItemInHand(new ItemStack(Material.AIR));
					econ.depositPlayer(player.getDisplayName(),dropPrice);
					player.sendMessage(ChatColor.GREEN + "Sold " + mobDrops.get(a).toString() + " for: $" + dropPrice);
				}
			}
		}
		
		try
		{
			if(event.getAction() == Action.RIGHT_CLICK_AIR)
				if(player.getItemInHand().getType().equals(Material.BLAZE_ROD))
				{
					if(player.getItemInHand().getType().equals(Material.BLAZE_ROD))
						player.setItemInHand(new ItemStack(Material.AIR));	
					Bukkit.broadcastMessage(ChatColor.RED + "/!\\WARNING NUKE INCOMING IN 30 SECONDS/!\\");
					nuke = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(2)).toLocation(player.getWorld(), player.getLocation().getYaw(), player.getLocation().getPitch());
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.main, new Runnable() 
					{
						public void run() 
						{
							Fireball fireball = player.getWorld().spawn(nuke, Fireball.class);
							fireball.setYield(500);
							fireball.setIsIncendiary(false);
							fireball.getLastDamageCause().setDamage(1000);
						}
					}, 20*30);
				}
		}
		catch(Exception e){}
		if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if(player.getItemInHand().getType().equals(Material.BRICK))
			{
				for(int i=0;i<this.main.getConfig().getList("Creative Immunity").size();i++)
					if(player.getDisplayName().equalsIgnoreCase(this.main.getConfig().getList("Creative Immunity").get(i).toString()))
						return;
				if(player.getLevel() >= 60)
				{
					player.setItemInHand(new ItemStack(Material.AIR));
					player.setGameMode(GameMode.CREATIVE);
				}
				else
					player.sendMessage(ChatColor.RED + "You need to be at least level 60 to get creative!");
			}
		}
		
		if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
			if(player.getItemInHand().getType().equals(Material.RED_MUSHROOM))
			{
				int amount = player.getItemInHand().getAmount();
				player.setItemInHand(new ItemStack(Material.AIR));
				player.setHealthScale(player.getHealthScale() + 4*amount);
				if(player.getHealthScale() > 40)
				{
					player.setHealthScale(40);
					player.sendMessage(ChatColor.RED + "Your health is already boosted to the max!");
				}
			}
			else if(player.getItemInHand().getType().equals(Material.STONE_BUTTON))
			{
				player.setItemInHand(new ItemStack(Material.AIR));
				player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
				player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0));
				player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
				player.sendMessage(ChatColor.GREEN + "Stat Boost Activated!");
			}
			else if(player.getItemInHand().getType().equals(Material.WORKBENCH))
			{
				for(int i=0;i<this.main.getConfig().getList("Creative Immunity").size();i++)
					if(player.getDisplayName().equalsIgnoreCase(this.main.getConfig().getList("Creative Immunity").get(i).toString()))
						return;
				player.sendMessage(ChatColor.GRAY + "Type: /craft");
			}
		
		if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if(player.getItemInHand().getType().equals(Material.SPONGE))
			{
				player.getInventory().clear();
				ItemStack coal = new ItemStack(Material.COAL, 64);
				ItemMeta meta = coal.getItemMeta();
				meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Undead Heart");
				coal.setItemMeta(meta);
				int i = 0;
				while(i<36)
				{
					player.getInventory().addItem(coal);
					i++;
				}
				player.updateInventory();
			}
		}
		else if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
		{
			if(player.getItemInHand().getType().equals(Material.SPONGE))
			{
				player.getInventory().clear();
				ItemStack emerald = new ItemStack(Material.EMERALD, 64);
				ItemMeta meta = emerald.getItemMeta();
				meta.setDisplayName(ChatColor.DARK_GREEN + "Tainted Soul");
				emerald.setItemMeta(meta);
				int n = 0;
				while(n<36)
				{
					player.getInventory().addItem(emerald);
					n++;
				}
				player.updateInventory();
			}
		}
		
		if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if(player.getGameMode().equals(GameMode.CREATIVE))
			{
				for(int i=0;i<this.main.getConfig().getList("Creative Immunity").size();i++)
					if(!(player.getDisplayName().equalsIgnoreCase(this.main.getConfig().getList("Creative Immunity").get(i).toString())))
						if(!(player.getItemInHand().getType().equals(Material.AIR)))
							event.setCancelled(true);
			}
		}
		
		if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
			if(player.getItemInHand().getType().equals(Material.PAPER))
				player.openInventory(ShopMenu.getShop());
		
		if(flowersOver)
			if(event.getAction() == Action.LEFT_CLICK_BLOCK)
				if(player.getItemInHand().getType().equals(Material.DIAMOND_SPADE))
				{
					player.getInventory().remove(Material.DIAMOND_SPADE);
					Block block = event.getClickedBlock();
					if(block.getType().equals(Material.RED_ROSE))
						block.breakNaturally();
					ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
					BookMeta bm = (BookMeta) book.getItemMeta();
					bm.setPages(Arrays.asList("The Lords and other Dwellers of the Overworld have recognized you. They will be willing to give you more power if you can gather enough Tainted Souls."));
					bm.setAuthor("Mob Fighter");
					bm.setTitle("Sage Path");
					book.setItemMeta(bm);
					player.getInventory().addItem(book);
					player.updateInventory();
				}		
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.NORMAL)
	public void onDeath(EntityDeathEvent event)
	{
		if(event.getEntity() instanceof EnderDragon)
		{
			Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + (ChatColor.BOLD + "The Ender Dragon has been defeated!"));
			ItemStack dEgg = new ItemStack(Material.DRAGON_EGG);
			event.getDrops().clear();
			event.getDrops().add(dEgg);
		}
		
		if(event.getEntity() instanceof Wither)
		{
			Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + (ChatColor.BOLD + "The Wither has been defeated!"));
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"region flag __global__" + " -w "+ event.getEntity().getWorld().getName() + " deny-spawn Slime");
			ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 1);
			ItemStack item2 = new ItemStack(Material.SKULL_ITEM, 1, (byte) 1);
			ItemStack item3 = new ItemStack(Material.SKULL_ITEM, 1, (byte) 1);
			ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
			BookMeta bm = (BookMeta) book.getItemMeta();
			bm.setPages(Arrays.asList("The Lords and Other powerful Dwellers of the Underworld are up to giving you more power if you can collect enough Undead Hearts."));
			bm.setAuthor("Mob Fighter");
			bm.setTitle("Demon Path");
			book.setItemMeta(bm);
			event.getDrops().clear();
			event.getDrops().add(item);
			event.getDrops().add(item2);
			event.getDrops().add(item3);
			event.getDrops().add(book);
			event.setDroppedExp(200000);
		}
		
		if(event.getEntity() instanceof Creeper)
		{
			if(((Creeper) event.getEntity()).isPowered())
			{
				ItemStack item = new ItemStack(Material.DIAMOND);
				event.getDrops().add(item);
			}
		}
		
		if(event.getEntity() instanceof Giant)
		{
			Giant g = (Giant) event.getEntity();
			ItemStack item = new ItemStack(Material.DIAMOND_BLOCK);
			event.getDrops().add(item);
			event.setDroppedExp(10000);
			giantKills++;
			Bukkit.broadcastMessage(ChatColor.YELLOW + "Giants defeated: " + giantKills);
			List<Entity> entities = g.getNearbyEntities(100, 100, 100);
			int num = 0;
			for(Entity e : entities)
			{
				if(e instanceof Giant)
					num++;
			}
			if(num == 0){
				if(!MobFighter.isAllOut())
					Bukkit.broadcastMessage("Now that all of the Giants have been defeated, it will become day in 10 seconds!");
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.main, new Runnable() 
				{
					public void run() 
					{
						if(!MobFighter.isAllOut())
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"time "+ MobFighter.getTheWorld() +" day");
					}
				}, 20*10);	
			}
		}
		
		if(event.getEntity() instanceof Zombie)
		{
			Zombie z = (Zombie)event.getEntity();
			if(babyZombies)
			{
				if(z.isBaby())
				{
					event.getDrops().clear();
					ItemStack coal1 = new ItemStack(Material.COAL);
					ItemStack coal2 = new ItemStack(Material.COAL);
					ItemMeta meta = coal1.getItemMeta();
					meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Undead Heart");
					coal1.setItemMeta(meta);
					coal2.setItemMeta(meta);
					event.getDrops().add(coal1);
					event.getDrops().add(coal2);
				}
			}
		}
		
		if(event.getEntity() instanceof Wolf)
		{
			Wolf w = (Wolf)event.getEntity();
			if(wolfPack)
			{
				w.setAngry(true);
				List<Entity> entities = w.getNearbyEntities(10, 10, 10);
				for(int i=0;i<w.getNearbyEntities(10,10,10).size();i++)
				{
					if(entities.get(i).getType().equals(EntityType.WOLF))
					{
						Wolf other = (Wolf) entities.get(i);
						for(Entity e : entities)
						{
							if(e.getType().equals(EntityType.PLAYER))
								other.setTarget((LivingEntity) e);
							break;
						}
					}
				}
				ItemStack emerald = new ItemStack(Material.EMERALD);
				ItemMeta meta = emerald.getItemMeta();
				meta.setDisplayName(ChatColor.DARK_GREEN + "Tainted Soul");
				emerald.setItemMeta(meta);
				event.getDrops().clear();
				event.getDrops().add(emerald);
			}
		}
		
		if(event.getEntity() instanceof Player)
		{
			Player player = (Player)event.getEntity();
			Double loss = 0.0;
			Double amount = econ.getBalance(player.getDisplayName());
			loss = amount * 0.1;
			econ.withdrawPlayer(player.getDisplayName(),loss);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityCreatePortalEvent(EntityCreatePortalEvent event) 
	{
		if (event.getEntity() instanceof EnderDragon)
		{
			event.setCancelled(true);
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void chunkLoad(ChunkLoadEvent event)
	{
		if(flowers)
		{
			World world = Bukkit.getServer().getWorld(MobFighter.getTheWorld());
			Location a = new Location(world,(world.getSpawnLocation().getBlockX()-110),(world.getSpawnLocation().getY()),(world.getSpawnLocation().getZ()-110));
			Location b = new Location(world,(world.getSpawnLocation().getBlockX()+110),(world.getSpawnLocation().getY()),(world.getSpawnLocation().getZ()+110));
			int xMin = Math.min(a.getBlockX(),b.getBlockX());
			int yMin = Math.min(a.getBlockY(),b.getBlockY());
			int zMin = Math.min(a.getBlockZ(),b.getBlockZ());
			int xMax = Math.max(a.getBlockX(),b.getBlockX());
			int yMax = Math.max(a.getBlockY(),b.getBlockY());
			int zMax = Math.max(a.getBlockZ(),b.getBlockZ());
			int r = (int)(Math.random()*48841+1);
			int sFlower = 0;
			for (int x = xMin; x<=xMax; x++)
			{
				for (int y = yMin; y<=yMax; y++)
				{
					for (int z = zMin; z<=zMax; z++)
				    {
						Location blockLoc = new Location(world,(world.getBlockAt(x,y,z).getX()), (world.getBlockAt(x,y,z).getY()), (world.getBlockAt(x,y,z).getZ()));
						if(blockLoc.getBlock().getType().equals(Material.AIR))
							blockLoc.getBlock().setTypeIdAndData(38, (byte) 0x4, false);
						sFlower++;
						if(sFlower==r)
						{
							if(blockLoc.getBlock().getType().equals(Material.RED_ROSE))
								blockLoc.getBlock().setTypeIdAndData(38, (byte) 0x0, false);
						}
				    }
				}
			}
			Location c = new Location(world,(world.getSpawnLocation().getBlockX()-7),(world.getSpawnLocation().getY()),(world.getSpawnLocation().getZ()-8));
			Location d = new Location(world,(world.getSpawnLocation().getBlockX()+7),(world.getSpawnLocation().getY()),(world.getSpawnLocation().getZ()+6));
			int xMin2 = Math.min(c.getBlockX(),d.getBlockX());
			int yMin2 = Math.min(c.getBlockY(),d.getBlockY());
			int zMin2 = Math.min(c.getBlockZ(),d.getBlockZ());
			int xMax2 = Math.max(c.getBlockX(),d.getBlockX());
			int yMax2 = Math.max(c.getBlockY(),d.getBlockY());
			int zMax2 = Math.max(c.getBlockZ(),d.getBlockZ());
			for (int x = xMin2; x<=xMax2; x++)
			{
				for (int y = yMin2; y<=yMax2; y++)
				{
					for (int z = zMin2; z<=zMax2; z++)
				    {
						Location blockLoc = new Location(world,(world.getBlockAt(x,y,z).getX()), (world.getBlockAt(x,y,z).getY()), (world.getBlockAt(x,y,z).getZ()));
						if(blockLoc.getBlock().getType().equals(Material.RED_ROSE))
							blockLoc.getBlock().setType(Material.AIR);
				    }
				}
			}
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"stoplag");
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"stoplag -c");
			flowers = false;
			flowersOver = true;
		}
		if(isDay && flowersOver)
		{
			World world = Bukkit.getServer().getWorld(MobFighter.getTheWorld());
			Location a = new Location(world,(world.getSpawnLocation().getBlockX()-110),(world.getSpawnLocation().getY()),(world.getSpawnLocation().getZ()-110));
			Location b = new Location(world,(world.getSpawnLocation().getBlockX()+110),(world.getSpawnLocation().getY()),(world.getSpawnLocation().getZ()+110));
			int xMin = Math.min(a.getBlockX(),b.getBlockX());
			int yMin = Math.min(a.getBlockY(),b.getBlockY());
			int zMin = Math.min(a.getBlockZ(),b.getBlockZ());
			int xMax = Math.max(a.getBlockX(),b.getBlockX());
			int yMax = Math.max(a.getBlockY(),b.getBlockY());
			int zMax = Math.max(a.getBlockZ(),b.getBlockZ());
			for (int x = xMin; x<=xMax; x++)
			{
				for (int y = yMin; y<=yMax; y++)
				{
					for (int z = zMin; z<=zMax; z++)
				    {
						Location blockLoc = new Location(world,(world.getBlockAt(x,y,z).getX()), (world.getBlockAt(x,y,z).getY()), (world.getBlockAt(x,y,z).getZ()));
						if(blockLoc.getBlock().getType().equals(Material.RED_ROSE))
							blockLoc.getBlock().setType(Material.AIR);
				    }
				}
			}
			Bukkit.broadcastMessage(ChatColor.GREEN + "The flowers seem to have disappeared!");
			flowersOver = false;
		}
		
		if(isDay)
		{
				for(int i=0;i<blockStates.size();i++)
				{
					final BlockState state = blockStates.get(i);
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable()
					{
						public void run()
						{
							state.update(true, false);
						}
					}, fixBlockDelay);
					blockStates.remove(i);
				}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void getFlower(PlayerPickupItemEvent event)
	{
		final Player player = event.getPlayer();
		World world = player.getWorld();
		if(flowersOver)
		{
			ItemStack theFlower = new ItemStack(Material.RED_ROSE);
			if(event.getItem().getItemStack().getData().equals(theFlower.getData()))
			{
				ItemStack flower = event.getItem().getItemStack();
				ItemMeta itemMeta = flower.getItemMeta();
				itemMeta.setDisplayName("Power Flower");
				flower.setItemMeta(itemMeta);
				flower.addUnsafeEnchantment(new EnchantmentWrapper(16), 5);
				flower.addUnsafeEnchantment(new EnchantmentWrapper(21), 5);
				flower.addUnsafeEnchantment(new EnchantmentWrapper(19), 1);
			}
		}
		
		if(lightning)
		{
			int x = (player.getLocation().getBlockX() + ((int)(Math.random()*11)));
			int y = player.getLocation().getBlockY();
			int z = (player.getLocation().getBlockZ() + ((int)(Math.random()*11)));
			Location strike = new Location(world, x, y, z);
			world.strikeLightning(strike);
		}
		
		if(tnt)
		{
			int x = (player.getLocation().getBlockX() + ((int)(Math.random()*11)));
			int y = player.getLocation().getBlockY();
			int z = (player.getLocation().getBlockZ() + ((int)(Math.random()*11)));
			Location strike = new Location(world, x, y, z);
			Entity tnt = world.spawnEntity(strike, EntityType.PRIMED_TNT);
			((TNTPrimed)tnt).setFuseTicks(0);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onInventoryClick(InventoryClickEvent event)
	{
		Player player = (Player) event.getWhoClicked();
		ItemStack clicked = event.getCurrentItem();
		Inventory inventory = event.getInventory();
		if(inventory.getName().equals(ShopMenu.shopName()))
		{
			event.setCancelled(true);
			if(clicked.getType() == Material.WOOD_SWORD)
			{
				if(VaultEco.getEconomy().getBalance(player) < 5)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
					return;
				}
				VaultEco.getEconomy().withdrawPlayer(player, 5);
				player.getInventory().addItem(new ItemStack(Material.WOOD_SWORD));
			}
			else if(clicked.getType() == Material.LEATHER_HELMET)
			{
				if(VaultEco.getEconomy().getBalance(player) < 5)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
					return;
				}
				VaultEco.getEconomy().withdrawPlayer(player, 5);
				player.getInventory().addItem(new ItemStack(Material.LEATHER_HELMET));
			}
			else if(clicked.getType() == Material.LEATHER_CHESTPLATE)
			{
				if(VaultEco.getEconomy().getBalance(player) < 10)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
					return;
				}
				VaultEco.getEconomy().withdrawPlayer(player, 10);
				player.getInventory().addItem(new ItemStack(Material.LEATHER_CHESTPLATE));
			}
			else if(clicked.getType() == Material.LEATHER_LEGGINGS)
			{
				if(VaultEco.getEconomy().getBalance(player) < 10)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
					return;
				}
				VaultEco.getEconomy().withdrawPlayer(player, 10);
				player.getInventory().addItem(new ItemStack(Material.LEATHER_LEGGINGS));
			}
			else if(clicked.getType() == Material.LEATHER_BOOTS)
			{
				if(VaultEco.getEconomy().getBalance(player) < 5)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
					return;
				}
				VaultEco.getEconomy().withdrawPlayer(player, 5);
				player.getInventory().addItem(new ItemStack(Material.LEATHER_BOOTS));
			}
			else if(clicked.getType() == Material.BOW)
			{
				if(VaultEco.getEconomy().getBalance(player) < 10)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
					return;
				}
				VaultEco.getEconomy().withdrawPlayer(player, 10);
				player.getInventory().addItem(new ItemStack(Material.BOW));
			}
			else if(clicked.getType() == Material.GOLD_SWORD)
			{
				if(VaultEco.getEconomy().getBalance(player) < 10)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
					return;
				}
				VaultEco.getEconomy().withdrawPlayer(player, 10);
				player.getInventory().addItem(new ItemStack(Material.GOLD_SWORD));
			}
			else if(clicked.getType() == Material.GOLD_HELMET)
			{
				if(VaultEco.getEconomy().getBalance(player) < 10)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
					return;
				}
				VaultEco.getEconomy().withdrawPlayer(player, 10);
				player.getInventory().addItem(new ItemStack(Material.GOLD_HELMET));
			}
			else if(clicked.getType() == Material.GOLD_CHESTPLATE)
			{
				if(VaultEco.getEconomy().getBalance(player) < 15)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
					return;
				}
				VaultEco.getEconomy().withdrawPlayer(player, 15);
				player.getInventory().addItem(new ItemStack(Material.GOLD_CHESTPLATE));
			}
			else if(clicked.getType() == Material.GOLD_LEGGINGS)
			{
				if(VaultEco.getEconomy().getBalance(player) < 15)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
					return;
				}
				VaultEco.getEconomy().withdrawPlayer(player, 15);
				player.getInventory().addItem(new ItemStack(Material.GOLD_LEGGINGS));
			}
			else if(clicked.getType() == Material.GOLD_BOOTS)
			{
				if(VaultEco.getEconomy().getBalance(player) < 10)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
					return;
				}
				VaultEco.getEconomy().withdrawPlayer(player, 10);
				player.getInventory().addItem(new ItemStack(Material.GOLD_BOOTS));
			}
			else if(clicked.getType() == Material.ARROW)
			{
				if(VaultEco.getEconomy().getBalance(player) < 10)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
					return;
				}
				VaultEco.getEconomy().withdrawPlayer(player, 10);
				player.getInventory().addItem(new ItemStack(Material.ARROW,16));
			}
			else if(clicked.getType() == Material.IRON_SWORD)
			{
				if(VaultEco.getEconomy().getBalance(player) < 50)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
					return;
				}
				VaultEco.getEconomy().withdrawPlayer(player, 50);
				player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
			}
			else if(clicked.getType() == Material.IRON_HELMET)
			{
				if(VaultEco.getEconomy().getBalance(player) < 100)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
					return;
				}
				VaultEco.getEconomy().withdrawPlayer(player, 100);
				player.getInventory().addItem(new ItemStack(Material.GOLD_HELMET));
			}
			else if(clicked.getType() == Material.IRON_CHESTPLATE)
			{
				if(VaultEco.getEconomy().getBalance(player) < 400)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
					return;
				}
				VaultEco.getEconomy().withdrawPlayer(player, 400);
				player.getInventory().addItem(new ItemStack(Material.GOLD_CHESTPLATE));
			}
			else if(clicked.getType() == Material.IRON_LEGGINGS)
			{
				if(VaultEco.getEconomy().getBalance(player) < 300)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
					return;
				}
				VaultEco.getEconomy().withdrawPlayer(player, 300);
				player.getInventory().addItem(new ItemStack(Material.GOLD_LEGGINGS));
			}
			else if(clicked.getType() == Material.IRON_BOOTS)
			{
				if(VaultEco.getEconomy().getBalance(player) < 100)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
					return;
				}
				VaultEco.getEconomy().withdrawPlayer(player, 100);
				player.getInventory().addItem(new ItemStack(Material.GOLD_BOOTS));
			}
			else if(clicked.getType() == Material.COOKED_BEEF)
			{
				if(VaultEco.getEconomy().getBalance(player) < 10)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
					return;
				}
				VaultEco.getEconomy().withdrawPlayer(player, 10);
				player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF,8));
			}
			else if(clicked.getType() == Material.DIAMOND_SWORD)
			{
				if(VaultEco.getEconomy().getBalance(player) < 100)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
					return;
				}
				VaultEco.getEconomy().withdrawPlayer(player, 100);
				player.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));
			}
			else if(clicked.getType() == Material.DIAMOND_HELMET)
			{
				if(VaultEco.getEconomy().getBalance(player) < 300)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
					return;
				}
				VaultEco.getEconomy().withdrawPlayer(player, 300);
				player.getInventory().addItem(new ItemStack(Material.DIAMOND_HELMET));
			}
			else if(clicked.getType() == Material.DIAMOND_CHESTPLATE)
			{
				if(VaultEco.getEconomy().getBalance(player) < 2600)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
					return;
				}
				VaultEco.getEconomy().withdrawPlayer(player, 2600);
				player.getInventory().addItem(new ItemStack(Material.DIAMOND_CHESTPLATE));
			}
			else if(clicked.getType() == Material.DIAMOND_LEGGINGS)
			{
				if(VaultEco.getEconomy().getBalance(player) < 1000)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
					return;
				}
				VaultEco.getEconomy().withdrawPlayer(player, 1000);
				player.getInventory().addItem(new ItemStack(Material.DIAMOND_LEGGINGS));
			}
			else if(clicked.getType() == Material.DIAMOND_BOOTS)
			{
				if(VaultEco.getEconomy().getBalance(player) < 300)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
					return;
				}
				VaultEco.getEconomy().withdrawPlayer(player, 300);
				player.getInventory().addItem(new ItemStack(Material.DIAMOND_BOOTS));
			}
			else if(clicked.getType() == Material.APPLE)
			{
				if(VaultEco.getEconomy().getBalance(player) < 20)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
					return;
				}
				VaultEco.getEconomy().withdrawPlayer(player, 20);
				player.getInventory().addItem(new ItemStack(Material.APPLE,10));
			}
			else if(clicked.getType() == Material.BOOK)
			{
				if(VaultEco.getEconomy().getBalance(player) < 100)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
					return;
				}
				VaultEco.getEconomy().withdrawPlayer(player, 100);
				player.getInventory().addItem(new ItemStack(Material.BOOK));
			}
			else if(clicked.getType() == Material.EXP_BOTTLE)
			{
				if(VaultEco.getEconomy().getBalance(player) < 100)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
					return;
				}
				VaultEco.getEconomy().withdrawPlayer(player, 100);
				player.getInventory().addItem(new ItemStack(Material.EXP_BOTTLE,10));
			}
			else if(clicked.getType() == Material.RED_MUSHROOM)
			{
				if(VaultEco.getEconomy().getBalance(player) < 2000)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
					return;
				}
				VaultEco.getEconomy().withdrawPlayer(player, 2000);
				player.getInventory().addItem(new ItemStack(Material.RED_MUSHROOM));
			}
			else if(clicked.getType() == Material.GOLD_INGOT)
			{
				if(VaultEco.getEconomy().getBalance(player) < 100)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
					return;
				}
				VaultEco.getEconomy().withdrawPlayer(player, 100);
				player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT,10));
			}
			else if(clicked.getType() == Material.IRON_INGOT)
			{
				if(VaultEco.getEconomy().getBalance(player) < 1000)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
					return;
				}
				VaultEco.getEconomy().withdrawPlayer(player, 1000);
				player.getInventory().addItem(new ItemStack(Material.IRON_INGOT,10));
			}
			else if(clicked.getType() == Material.DIAMOND)
			{
				if(VaultEco.getEconomy().getBalance(player) < 2000)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
					return;
				}
				VaultEco.getEconomy().withdrawPlayer(player, 2000);
				player.getInventory().addItem(new ItemStack(Material.DIAMOND,10));
			}
			else if(clicked.getType() == Material.BRICK)
			{
				if(VaultEco.getEconomy().getBalance(player) < 200)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
					return;
				}
				VaultEco.getEconomy().withdrawPlayer(player, 200);
				player.getInventory().addItem(new ItemStack(Material.BRICK));
			}
			else if(clicked.getType() == Material.WORKBENCH)
			{
				if(VaultEco.getEconomy().getBalance(player) < 1000)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
					return;
				}
				VaultEco.getEconomy().withdrawPlayer(player, 1000);
				player.getInventory().addItem(new ItemStack(Material.WORKBENCH));
			}
			else if(clicked.getType() == Material.STONE_BUTTON)
			{
				if(VaultEco.getEconomy().getBalance(player) < 10000)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
					return;
				}
				VaultEco.getEconomy().withdrawPlayer(player, 10000);
				player.getInventory().addItem(new ItemStack(Material.STONE_BUTTON));
			}
			else if(clicked.getType() == Material.BLAZE_ROD)
			{
				if(VaultEco.getEconomy().getBalance(player) < 10000)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
					return;
				}
				VaultEco.getEconomy().withdrawPlayer(player, 10000);
				player.getInventory().addItem(new ItemStack(Material.BLAZE_ROD));
			}
			else if(clicked.getType() == Material.POTION)
			{
				if(VaultEco.getEconomy().getBalance(player) < 20)
				{
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
					return;
				}
				VaultEco.getEconomy().withdrawPlayer(player, 20);
				player.getInventory().addItem(clicked);
			}
			else if(clicked.getType() == Material.WRITTEN_BOOK)
			{
				player.getInventory().addItem(clicked);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.NORMAL)
	public void anvilFix(final InventoryClickEvent e)
	{
		if(!e.isCancelled())
		{
			HumanEntity ent = e.getWhoClicked();
			if(ent instanceof Player)
			{
				final Player player = (Player)ent;
				Inventory inv = e.getInventory();
				if(player.getGameMode() == GameMode.CREATIVE)
				{
					if(inv instanceof AnvilInventory)
					{
						AnvilInventory anvil = (AnvilInventory)inv;
						InventoryView view = e.getView();
						int rawSlot = e.getRawSlot();
						if(rawSlot == view.convertSlot(rawSlot))
						{
							if(rawSlot == 2)
							{
								ItemStack[] items = anvil.getContents();
								ItemStack item1 = items[0];
								ItemStack item2 = items[1];
								if(item1 != null && item2 != null)
								{
									int id1 = item1.getTypeId();
									int id2 = item2.getTypeId();
									if(id1 != 0 && id2 != 0)
									{
										ItemStack item3 = e.getCurrentItem();
										if(item3 != null)
										{
											ItemMeta meta = item3.getItemMeta();
											if(meta != null)
											{
												if(player.getLevel() >= 60)
												{
													player.setLevel(player.getLevel()-60);
													player.sendMessage(ChatColor.GREEN + "Repair/Combine Successful!");
													Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.main, new Runnable() 
													{
														public void run() 
														{
															for(int i=0;i<main.getConfig().getList("Creative Immunity").size();i++)
																if(player.getDisplayName().equalsIgnoreCase(main.getConfig().getList("Creative Immunity").get(i).toString()))
																	return;
															e.getWhoClicked().getOpenInventory().close();
															player.setGameMode(GameMode.SURVIVAL);
														}
													}, 20*2);
												}					
											}
										}
									}
								}
							}
						}
					}
					else
					{
						for(int i=0;i<this.main.getConfig().getList("Creative Immunity").size();i++)
							if(player.getDisplayName().equalsIgnoreCase(this.main.getConfig().getList("Creative Immunity").get(i).toString()))
								e.setCancelled(false);
							else
							{
								e.setCancelled(true);
								player.closeInventory();
							}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onCraft(final CraftItemEvent event)
	{
		event.getWhoClicked().getInventory().remove(Material.WORKBENCH);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.main, new Runnable() {
			 
		    public void run() 
		    {
				event.getWhoClicked().getOpenInventory().close();
		    }
		 
		}, 20*1);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteractWithEntity(PlayerInteractEntityEvent event)
	{
		if(reaper)
		{
			final Player player = event.getPlayer();
			final Player targetPlayer = (Player) event.getRightClicked();
			if(targetPlayer instanceof Player)
				if(player.getItemInHand().getType().equals(Material.POISONOUS_POTATO))
				{
					ItemStack item = player.getItemInHand();
					targetPlayer.getInventory().addItem(item);
					player.getInventory().remove(item);
				}
			hasPotato = targetPlayer.getDisplayName();
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void spawningMobs(CreatureSpawnEvent event)
	{		
		if(event.getEntity() instanceof Creeper)
			if(flowersOver)
				event.setCancelled(true);
		
		if(event.getEntity() instanceof Wolf)
		{
			Wolf w = (Wolf)event.getEntity();
			if(wolfPack)
			{
				w.setAngry(true);
				List<Entity> entities = w.getNearbyEntities(10, 10, 10);
				for(int i=0;i<w.getNearbyEntities(10,10,10).size();i++)
				{
					if(entities.get(i).getType().equals(EntityType.WOLF))
					{
						Wolf other = (Wolf) entities.get(i);
						for(Entity e : entities)
							if(e.getType().equals(EntityType.PLAYER))
								other.setTarget((LivingEntity) e);
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void playerItemDrop(PlayerDropItemEvent event)
	{
		ItemStack potato = new ItemStack(Material.POISONOUS_POTATO);
		if(event.getItemDrop().getItemStack().equals(potato))
		{
			final Player player = event.getPlayer();
			Location loc = player.getLocation();
			player.getWorld().strikeLightning(loc);
			event.setCancelled(true);
			Bukkit.broadcastMessage(player.getDisplayName() + " you shall not throw the potato.");
			player.setHealth(0);
		}
	}
	
	@EventHandler (priority = EventPriority.HIGH)
	public void onExplode(final EntityExplodeEvent e)
	{
		if(e.getEntity() instanceof Fireball)
		{
			e.blockList().clear();
		}
        for(Block b : e.blockList())
        {
        	final BlockState state = b.getState();
        	b.setType(Material.AIR);
        	blockStates.add(state);

			int delay = 20;
	        if((b.getType() == Material.SAND || b.getType() == Material.GRAVEL))
	        	delay++;
	        
	        fixBlockDelay = delay;
        }	
	}
	
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event)
	{
		if(babyZombies)
			if(event.getEntity() instanceof Zombie)
			{
				Zombie z = (Zombie)event.getEntity();
				if(!(z.isBaby()))
					((LivingEntity)z).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
			}
	}
}
