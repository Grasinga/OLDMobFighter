package network.ethereal.MobFighter;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class ShopMenu {
	public static Inventory shopInv = Bukkit.createInventory(null, 54, ChatColor.BLUE + "MobFighter - Shop");
	static
	{
		ItemStack woodSword = new ItemStack(Material.WOOD_SWORD);
		
		ItemMeta five = woodSword.getItemMeta();
		ArrayList<String> fiveLore = new ArrayList<String>();
		fiveLore.add(ChatColor.WHITE + "Price: $5.00");
		five.setLore(fiveLore);
		
		ItemMeta ten = woodSword.getItemMeta();
		ArrayList<String> tenLore = new ArrayList<String>();
		tenLore.add(ChatColor.WHITE + "Price: $10.00");
		ten.setLore(tenLore);
		
		ItemMeta fifteen = woodSword.getItemMeta();
		ArrayList<String> fifteenLore = new ArrayList<String>();
		fifteenLore.add(ChatColor.WHITE + "Price: $15.00");
		fifteen.setLore(fifteenLore);
		
		ItemMeta twenty = woodSword.getItemMeta();
		ArrayList<String> twentyLore = new ArrayList<String>();
		twentyLore.add(ChatColor.WHITE + "Price: $20.00");
		twenty.setLore(twentyLore);
	    
		ItemMeta fifty = woodSword.getItemMeta();
		ArrayList<String> fiftyLore = new ArrayList<String>();
		fiftyLore.add(ChatColor.WHITE + "Price: $50.00");
		fifty.setLore(fiftyLore);
		
		ItemMeta hundred = woodSword.getItemMeta();
		ArrayList<String> hundredLore = new ArrayList<String>();
		hundredLore.add(ChatColor.WHITE + "Price: $100.00");
		hundred.setLore(hundredLore);
		
		ItemMeta hundredFifty = woodSword.getItemMeta();
		ArrayList<String> hundredFiftyLore = new ArrayList<String>();
		hundredFiftyLore.add(ChatColor.WHITE + "Price: $150.00");
		hundredFifty.setLore(hundredFiftyLore);
		
		ItemMeta twoHundred = woodSword.getItemMeta();
		ArrayList<String> twoHundredLore = new ArrayList<String>();
		twoHundredLore.add(ChatColor.WHITE + "Price: $200.00");
		twoHundred.setLore(twoHundredLore);
		
		ItemMeta threeHundred = woodSword.getItemMeta();
		ArrayList<String> threeHundredLore = new ArrayList<String>();
		threeHundredLore.add(ChatColor.WHITE + "Price: $300.00");
		threeHundred.setLore(threeHundredLore);
		
		ItemMeta fourHundred = woodSword.getItemMeta();
		ArrayList<String> fourHundredLore = new ArrayList<String>();
		fourHundredLore.add(ChatColor.WHITE + "Price: $400.00");
		fourHundred.setLore(fourHundredLore);
		
		ItemMeta eightHundred = woodSword.getItemMeta();
		ArrayList<String> eightHundredLore = new ArrayList<String>();
		eightHundredLore.add(ChatColor.WHITE + "Price: $800.00");
		eightHundred.setLore(eightHundredLore);
		
		ItemMeta oneThousand = woodSword.getItemMeta();
		ArrayList<String> oneThousandLore = new ArrayList<String>();
		oneThousandLore.add(ChatColor.WHITE + "Price: $1,000.00");
		oneThousand.setLore(oneThousandLore);
		
		ItemMeta twoThousand = woodSword.getItemMeta();
		ArrayList<String> twoThousandLore = new ArrayList<String>();
		twoThousandLore.add(ChatColor.WHITE + "Price: $2,000.00");
		twoThousand.setLore(twoThousandLore);
		
		ItemMeta twentySixHundred = woodSword.getItemMeta();
		ArrayList<String> twentySixHundredLore = new ArrayList<String>();
		twentySixHundredLore.add(ChatColor.WHITE + "Price: $2,600.00");
		twentySixHundred.setLore(twentySixHundredLore);
		
		ItemMeta tenThousand = woodSword.getItemMeta();
		ArrayList<String> tenThousandLore = new ArrayList<String>();
		tenThousandLore.add(ChatColor.WHITE + "Price: $10,000.00");
		tenThousand.setLore(tenThousandLore);
		
		woodSword.setItemMeta(five);
		shopInv.setItem(0, woodSword);
		
		ItemStack leatherHat = new ItemStack(Material.LEATHER_HELMET);
		leatherHat.setItemMeta(five);
		shopInv.setItem(9, leatherHat);
		
		ItemStack leatherChest = new ItemStack(Material.LEATHER_CHESTPLATE);		
		leatherChest.setItemMeta(ten);
		shopInv.setItem(18, leatherChest);
		
		ItemStack leatherLegs = new ItemStack(Material.LEATHER_LEGGINGS);
		leatherLegs.setItemMeta(ten);
		shopInv.setItem(27, leatherLegs);
		
		ItemStack leatherBoots = new ItemStack(Material.LEATHER_BOOTS);
		leatherBoots.setItemMeta(five);
		shopInv.setItem(36, leatherBoots);
		
		ItemStack bow = new ItemStack(Material.BOW);
		bow.setItemMeta(ten);
		shopInv.setItem(45, bow);
		
		ItemStack goldSword = new ItemStack(Material.GOLD_SWORD);
		goldSword.setItemMeta(ten);
		shopInv.setItem(1, goldSword);
		
		ItemStack goldHat = new ItemStack(Material.GOLD_HELMET);
		goldHat.setItemMeta(ten);
		shopInv.setItem(10, goldHat);
		
		ItemStack goldChest = new ItemStack(Material.GOLD_CHESTPLATE);
		goldChest.setItemMeta(fifteen);
		shopInv.setItem(19, goldChest);
		
		ItemStack goldLegs = new ItemStack(Material.GOLD_LEGGINGS);
		goldLegs.setItemMeta(fifteen);
		shopInv.setItem(28, goldLegs);
		
		ItemStack goldBoots = new ItemStack(Material.GOLD_BOOTS);
		goldBoots.setItemMeta(ten);
		shopInv.setItem(37, goldBoots);
		
		ItemStack arrow = new ItemStack(Material.ARROW,16);
		arrow.setItemMeta(ten);
		shopInv.setItem(46, arrow);		
		
		ItemStack ironSword = new ItemStack(Material.IRON_SWORD);
		ironSword.setItemMeta(fifty);
		shopInv.setItem(2, ironSword);
		
		ItemStack ironHat = new ItemStack(Material.IRON_HELMET);
		ironHat.setItemMeta(hundred);
		shopInv.setItem(11, ironHat);
		
		ItemStack ironChest = new ItemStack(Material.IRON_CHESTPLATE);
		ironChest.setItemMeta(fourHundred);
		shopInv.setItem(20, ironChest);
		
		ItemStack ironLegs = new ItemStack(Material.IRON_LEGGINGS);
		ironLegs.setItemMeta(threeHundred);
		shopInv.setItem(29, ironLegs);
		
		ItemStack ironBoots = new ItemStack(Material.IRON_BOOTS);
		ironBoots.setItemMeta(hundred);
		shopInv.setItem(38, ironBoots);
		
		ItemStack beef = new ItemStack(Material.COOKED_BEEF,8);
		beef.setItemMeta(ten);
		shopInv.setItem(47, beef);
		
		ItemStack diamondSword = new ItemStack(Material.DIAMOND_SWORD);
		diamondSword.setItemMeta(hundred);
		shopInv.setItem(3, diamondSword);
		
		ItemStack diamondHat = new ItemStack(Material.DIAMOND_HELMET);
		diamondHat.setItemMeta(threeHundred);
		shopInv.setItem(12, diamondHat);
		
		ItemStack diamondChest = new ItemStack(Material.DIAMOND_CHESTPLATE);
		diamondChest.setItemMeta(twentySixHundred);
		shopInv.setItem(21, diamondChest);
		
		ItemStack diamondLegs = new ItemStack(Material.DIAMOND_LEGGINGS);
		diamondLegs.setItemMeta(oneThousand);
		shopInv.setItem(30, diamondLegs);
		
		ItemStack diamondBoots = new ItemStack(Material.DIAMOND_BOOTS);
		diamondBoots.setItemMeta(threeHundred);
		shopInv.setItem(39, diamondBoots);
		
		ItemStack apple = new ItemStack(Material.APPLE,10);
		apple.setItemMeta(twenty);
		shopInv.setItem(48, apple);
		
		ItemStack starterBook = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta bm = (BookMeta) starterBook.getItemMeta();
		// Book Page Per Line
		bm.setPages(Arrays.asList(
				"Welcome to:\nMobFighter!\n\nThis server is a\nmob fighting game!\nYou kill mobs and\npick up their loot\nat night so you\ncan sell it\nduring the day to\nget better gear!\nAs you play you\nmay notice a\n",
				"message saying \n\"Night: #\" this\nlets you know the\ncurrent night and\nhelps you prepare\nfor the events!\nEvents happen every\nfive waves varying\nfrom fighting the\nEnderdragon, to\nstrolling in a\nmeadow of flowers.\n",
				"Commands:\n/getshop\n/ready day\n/ready night\n/ready clear\n/ready list\n/night\n/craft\n/call <username>\n/bring <username>\n/exchange\n/getbook <name>",
				"Emojies Spice up\nchat!\n\nType:\n/help mmemoji\n/help mmemoji 2\n/help mmemoji 3\n\nSee what emojies\nyou can use!\n\nThanks for reading!"));
		bm.setAuthor("Mob Fighter");
		bm.setTitle("Mob Fighter Info");
		ArrayList<String> bookLore = new ArrayList<String>();
		bookLore.add(ChatColor.WHITE + "Price: FREE");
		bm.setLore(bookLore);
		starterBook.setItemMeta(bm);
		shopInv.setItem(6, starterBook);
		
		ItemStack anvilBook = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta bm2 = (BookMeta) anvilBook.getItemMeta();
		// Book Page Per Line
		bm2.setPages(Arrays.asList(
				"The anvil has a restriction set on any enchament combines that are over level 40.\n\nMinecraft Wiki:\n\nIf the job would cost 40 or more levels, it will be rejected as \"Too Expensive!\". (This does not apply in creative mode.)",
				"So in order to combine items that are \"Too Expensive!\" a player can buy creative in order to combine the two items. The player also has to have at least 60 levels!"));
		bm2.setAuthor("Mob Fighter");
		bm2.setTitle("Anvil - Creative");
		ArrayList<String> bookLore2 = new ArrayList<String>();
		bookLore2.add(ChatColor.WHITE + "Price: FREE");
		bm2.setLore(bookLore2);
		anvilBook.setItemMeta(bm2);
		shopInv.setItem(7, anvilBook);
		
		ItemStack book = new ItemStack(Material.BOOK);
		book.setItemMeta(hundred);
		shopInv.setItem(24, book);
		
		ItemStack expBottle = new ItemStack(Material.EXP_BOTTLE,10);
		expBottle.setItemMeta(hundred);
		shopInv.setItem(25, expBottle);
		
		Potion regen = new Potion(PotionType.REGEN);
		Potion swift = new Potion(PotionType.SPEED);
		Potion night = new Potion(PotionType.NIGHT_VISION);
		Potion strength = new Potion(PotionType.STRENGTH);

		ItemStack pot1 = regen.toItemStack(1);
		pot1.setItemMeta(twenty);
		shopInv.setItem(32, pot1);

		ItemStack pot2 = swift.toItemStack(1);
		pot2.setItemMeta(twenty);
		shopInv.setItem(33, pot2);

		ItemStack pot3 = night.toItemStack(1);
		pot3.setItemMeta(twenty);		
		shopInv.setItem(34, pot3);		

		ItemStack pot4 = strength.toItemStack(1);
		pot4.setItemMeta(twenty);
		shopInv.setItem(35, pot4);
		
		ItemStack health = new ItemStack(Material.RED_MUSHROOM);
		ItemMeta healthMeta = health.getItemMeta();
		ArrayList<String> healthLore = new ArrayList<String>();
		healthLore.add(ChatColor.WHITE + "Price: $2,000.00");
		healthMeta.setDisplayName("Health Boost");
		healthMeta.setLore(healthLore);
		health.setItemMeta(healthMeta);
		shopInv.setItem(41, health);
		
		ItemStack gold = new ItemStack(Material.GOLD_INGOT,10);
		gold.setItemMeta(hundred);
		shopInv.setItem(42, gold);
		
		ItemStack iron = new ItemStack(Material.IRON_INGOT,10);
		iron.setItemMeta(oneThousand);
		shopInv.setItem(43, iron);
		
		ItemStack diamond = new ItemStack(Material.DIAMOND,10);
		diamond.setItemMeta(twoThousand);
		shopInv.setItem(44, diamond);
		
		ItemStack brick = new ItemStack(Material.BRICK);
		brick.setItemMeta(twoHundred);
		shopInv.setItem(50, brick);
		
		ItemStack bench = new ItemStack(Material.WORKBENCH);
		bench.setItemMeta(oneThousand);
		shopInv.setItem(51, bench);
		
		ItemStack button = new ItemStack(Material.STONE_BUTTON);
		ItemMeta buttonMeta = button.getItemMeta();
		ArrayList<String> buttonLore = new ArrayList<String>();
		buttonLore.add(ChatColor.WHITE + "Price: $10,000.00");
		buttonMeta.setDisplayName("Stat Boost");
		buttonMeta.setLore(buttonLore);
		button.setItemMeta(buttonMeta);
		shopInv.setItem(52, button);
		
		ItemStack blazeRod = new ItemStack(Material.BLAZE_ROD);
		blazeRod.setItemMeta(tenThousand);
		shopInv.setItem(53, blazeRod);
	} 
	
	public static String shopName()
	{
		return shopInv.getName();
	}
	
	public static Inventory getShop()
	{
		return shopInv;
	}
}
