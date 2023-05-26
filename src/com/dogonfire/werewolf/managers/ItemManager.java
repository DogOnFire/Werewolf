package com.dogonfire.werewolf.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.dogonfire.werewolf.items.Recipes;
import com.dogonfire.werewolf.Werewolf;
import org.bukkit.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class ItemManager
{
	private Werewolf plugin;
	private NamespacedKey namespacedKey;

	public ItemManager(Werewolf plugin)
	{
		this.plugin = plugin;
		this.namespacedKey = new NamespacedKey(plugin, "ww-item");
	}

	private boolean isWerewolfItem(ItemStack item, String itemName)
	{
		ItemMeta itemMeta = item.getItemMeta();
		if (itemMeta == null)
		{
			return false;
		}
		PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
		return dataContainer.has(namespacedKey, PersistentDataType.STRING) && Objects.equals(dataContainer.get(namespacedKey, PersistentDataType.STRING), itemName);
	}
	
	public void setupRecipes()
	{
		if(plugin.craftableInfectionPotionEnabled)
		{			
			Bukkit.addRecipe(Recipes.infectionPotion(plugin));
			plugin.logDebug("Craftable infection potions are enabled");
		}
		
		if(plugin.craftableCurePotionEnabled)
		{
			Bukkit.addRecipe(Recipes.curePotion(plugin));
			plugin.logDebug("Craftable cure potions are enabled");
		}

		if(plugin.craftableWolfbanePotionEnabled)
		{
			Bukkit.addRecipe(Recipes.wolfbanePotion(plugin));
			plugin.logDebug("Craftable wolfbane potions are enabled");
		}

		if(plugin.craftableSilverSwordEnabled)
		{
			Bukkit.addRecipe(Recipes.silverSwordRecipe(plugin));
			plugin.logDebug("Craftable silver swords are enabled");
		}

		if(plugin.craftableSilverArmorEnabled)
		{
			Bukkit.addRecipe(Recipes.silverArmorRecipe(plugin));
			plugin.logDebug("Craftable silver armors are enabled");
		}

		if(plugin.craftableLoreBookEnabled)
		{
			Bukkit.addRecipe(Recipes.loreBook(plugin));
			plugin.logDebug("Craftable lore books are enabled");
		}
	}

	public ItemStack newInfectionPotion()
	{
		ItemStack potion = new ItemStack(Material.POTION);

		PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();

		potionMeta.setDisplayName(ChatColor.GOLD + Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.InfectionPotionTitle, ChatColor.GOLD));

	    List<String> pages = new ArrayList<String>();
	    
	    pages.add(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.InfectionPotionDescription1, ChatColor.GRAY));
	    pages.add(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.InfectionPotionDescription2, ChatColor.GRAY));
	    
	    potionMeta.setLore(pages);
	    //potionMeta.setMainEffect(PotionEffectType.CONFUSION);
	    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.CONFUSION, 300, 0), true);

		// Add NBT data
		potionMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, "InfectionPotion");

		potion.setItemMeta(potionMeta);

		return potion;
	}

	public ItemStack newCurePotion()
	{
		ItemStack potion = new ItemStack(Material.POTION);

		PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();

		potionMeta.setDisplayName(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.CurePotionTitle, ChatColor.GOLD));

	    List<String> pages = new ArrayList<String>();
	    
	    pages.add(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.CurePotionDescription1, ChatColor.GRAY));
	    pages.add(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.CurePotionDescription2, ChatColor.GRAY));
	    
	    potionMeta.setLore(pages);
	    //potionMeta.setMainEffect(PotionEffectType.CONFUSION);
	    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.CONFUSION, 300, 0), true);

		// Add NBT data
		potionMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, "CurePotion");

		potion.setItemMeta(potionMeta);

		return potion;
	}
	
	public ItemStack newWolfbanePotion()
	{
		ItemStack potion = new ItemStack(Material.SPLASH_POTION);
		//Potion potion = new Potion(PotionType.POISON, 2);
		//potion.setSplash(true);
		
		//ItemStack itemStack = potion.toItemStack(1);

		//PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
		PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();

		potionMeta.setDisplayName(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.WolfbanePotionTitle, ChatColor.GOLD));

	    List<String> pages = new ArrayList<String>();

	    Werewolf.getLanguageManager().setAmount(String.valueOf(plugin.wolfbaneUntransformChance));
	    pages.add(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.WolfbanePotionDescription1, ChatColor.LIGHT_PURPLE));
	    pages.add(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.WolfbanePotionDescription2, ChatColor.GRAY));
	    
	    potionMeta.setLore(pages);
	    PotionData pd = new PotionData(PotionType.POISON);
	    potionMeta.setBasePotionData(pd);
	    //potionMeta.setMainEffect(PotionEffectType.CONFUSION);
	    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.CONFUSION, 300, 0), true);

	    //itemStack.setItemMeta(potionMeta);
		
		//return itemStack;

		// Add NBT data
		potionMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, "WolfbanePotion");
	    
	    potion.setItemMeta(potionMeta);
		
		return potion;
	}

	public ItemStack newSilverSword(int level)
	{
		ItemStack weapon = new ItemStack(Material.IRON_SWORD);

		ItemMeta itemMeta = weapon.getItemMeta();

		itemMeta.setDisplayName(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.SilverSwordTitle, ChatColor.GOLD));

		List<String> pages = new ArrayList<String>();

	    Werewolf.getLanguageManager().setAmount(String.valueOf(DamageManager.SilverSwordMultiplier));
	    pages.add(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.SilverSwordDescription1, ChatColor.LIGHT_PURPLE));
	    pages.add(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.SilverSwordDescription2, ChatColor.GRAY));

		itemMeta.setLore(pages);

		// Add NBT data
		itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, "SilverSword");

		weapon.setItemMeta(itemMeta);

		return weapon;
	}

	public ItemStack newSilverArmor(int level)
	{
		ItemStack weapon = new ItemStack(Material.IRON_CHESTPLATE);

		ItemMeta itemMeta = weapon.getItemMeta();

		itemMeta.setDisplayName(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.SilverArmorTitle, ChatColor.GOLD));

		List<String> pages = new ArrayList<String>();

	    Werewolf.getLanguageManager().setAmount(String.valueOf(DamageManager.SilverArmorMultiplier));
	    pages.add(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.SilverArmorDescription1, ChatColor.LIGHT_PURPLE));
	    pages.add(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.SilverArmorDescription2, ChatColor.GRAY));

		itemMeta.setLore(pages);

		// Add NBT data
		itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, "SilverArmor");

		weapon.setItemMeta(itemMeta);

		return weapon;
	}

	public ItemStack newLoreBook()
	{
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK);

		// Add pages
		List<String> bookPages = new ArrayList<String>();

		bookPages.add("§lThe Werewolf infection\n§r\nThe werewolf curse is a ancient curse carried by some wolves who only come out at night.\n§r\nAn human infected with the werewolf curse will turn into his werewolf form every fullmoon.");
		
		if(plugin.useClans)
		{
			bookPages.add("§lWerewolves in combat\n§r\nThe werewolf is a ferocious creature. A werewolf does " + DamageManager.werewolfHandDamage + " damage with his hands and " + DamageManager.werewolfItemDamage + " with an item in his hands. Because of his unique physique, a werewolf takes " + 100*DamageManager.SilverArmorMultiplier + " %  damage\n§r\n");		
		}
		else
		{
			bookPages.add("§lWerewolves in combat\n§r\nThe werewolf is a ferocious creature. A werewolf does " + DamageManager.werewolfHandDamage + " damage with his hands and " + DamageManager.werewolfItemDamage + " with an item in his hands. Because of his unique physique, a werewolf takes " + 100*DamageManager.SilverArmorMultiplier + " %  damage\n§r\nThe leader of a clan, an alpha werewolf, takes " + 100*DamageManager.SilverArmorMultiplier/2 + " %  damage and does " + 2*DamageManager.werewolfHandDamage + "  damage!");					
		}		
		
		bookPages.add("§lTransformation\n§r\nDuring transformation, a werewolf will drop his clothes to the ground. But for each fullmoon, a werewolf will gain more control over his condition.\nAfter having experienced enough fullmoons, the werewolf may be able");
		bookPages.add("§rto not drop his clothes or even transform at will!");

		bookPages.add("§lGetting infected\n§r\nYou can be infected in 3 ways:\n§r\n 1) Getting bitten by a wolf at night\n§r\n 2) Drinking a werewolf potion\n§r\n 3) Being bitten by a werewolf.");
		bookPages.add("§lWerewolf language\n§r\nWhen in werewolf form, werewolves speak in a language only understood by themselves.\n§r\nA non-infected player will only hear wolf noises when they chat.");
		
		if(plugin.useClans)
		{
			bookPages.add("§lClans\n§r\nWerewolves are flock animals and they live in groups. There are three werewolf clans fighting for power.\n§r\nWerewolf players can check their clan info with the /ww clan command.");
			bookPages.add("§lThe Witherfangs\n§r\nWhen a human drinks a werewolf potion, he will turn into a Witherfang werewolf.\n§r\nWitherfang werewolves has yellow eyes, grey fur are fast and can jump higher than other werewolves.");
			bookPages.add("§lThe Silvermanes\n§r\nWhen a human is bitten by a wild wolf, he will turn into a Silvermane werewolf.\n§r\nSilvermane werewolves has blue eyes, grey fur and can take more damage than other werewolves.");
			bookPages.add("§lThe Bloodmoons\n§r\nWhen a human is bitten by a werewolf, he will turn into a Bloodmoon werewolf.\n§r\nThe Bloodmoon werewolves has red eyes, black fur and do more damage than other werewolves.");
		}
		
		if(plugin.useTrophies)
		{
			bookPages.add("§lTrophies\n§r\nIf you able to slay a werewolf using a sword, you may be able to cut the of the werewolf and keep it as a trophy!");
		}

		bookPages.add("§lCuring the infection\n§r\nA werewolf can drink the werewolf cure potion to cure himself.");		
		if(plugin.craftableCurePotionEnabled)
		{
			bookPages.add("§lCrafting the Cure\n§r\nIn a workbench, place the following ingredients:");			
			
			int n = 1;
			for(ItemStack ingredient : Recipes.curePotion(plugin).getIngredientList())
			{
				bookPages.add("\n§r\n " + n + ") " + ingredient.getType().name()); 
				n++;
			}			
		}		

		if(plugin.craftableInfectionPotionEnabled)
		{
			bookPages.add("§lThe infection potion\n§r\nA potion can be made that infects the drinker with the werewolf infection. This special potion must be used during a fullmoon.");		
			bookPages.add("§lCrafting the Infection\n§r\nIn a workbench, place the following ingredients:");			
			
			int n = 1;
			for(ItemStack ingredient : Recipes.infectionPotion(plugin).getIngredientList())
			{
				bookPages.add("\n§r\n " + n + ") " + ingredient.getType().name()); 
				n++;
			}			
		}		

		if(plugin.craftableWolfbanePotionEnabled)
		{
			bookPages.add("§lThe wolfbane potion\n§r\nA potion can be made that severely hurts the werewolf and has a chance to untransform him into human form. This special potion must be used during a fullmoon.");		
			bookPages.add("§lCrafting the Wolfbane\n§r\nIn a workbench, place the following ingredients:");			
			
			int n = 1;
			for(ItemStack ingredient : Recipes.wolfbanePotion(plugin).getIngredientList())
			{
				bookPages.add("\n§r\n " + n + ") " + ingredient.getType().name()); 
				n++;
			}			
		}		
		
		bookPages.add("§lHunting werewolves\n§r\nWerewolves are vulnerable to silver. Hitting a werewolf with a silver sword will do x" + DamageManager.SilverSwordMultiplier + " damage compared to a normal sword.\n§r\nOnly a werewolf hunter using the /ww hunt command can use this item.");
		if(plugin.craftableSilverSwordEnabled)
		{
			bookPages.add("§lThe Silver Sword\n§r\nBecause a werewolf is especially vulnerable to silver, a sword can be made that severely hurts the werewolf.");		
			bookPages.add("§lCrafting the Silver Sword\n§r\nIn a workbench, place the following ingredients:");			
			
			int n = 1;
			for(ItemStack ingredient : Recipes.silverSwordRecipe(plugin).getIngredientMap().values())
			{
				if(ingredient!=null)
				{
					bookPages.add("\n§r\n " + n + ") " + ingredient.getType().name()); 
					n++;
				}
			}						
		}
								
		bookPages.add("§lHunting werewolves\n§r\nWerewolves are vulnerable to silver. When a werewolf hits a silver armor, it will take x" + DamageManager.SilverArmorMultiplier + " damage.\n§r\nOnly a werewolf hunter using the /ww hunt command can use this item.");
		if(plugin.craftableSilverArmorEnabled)
		{
			bookPages.add("§lThe Silver Armor\n§r\nBecause a werewolf is especially vulnerable to silver, an armor can be made that hurts the werewolf.");		
			bookPages.add("§lCrafting the Silver Armor\n§r\nIn a workbench, place the following ingredients:");			
			
			int n = 1;
			for(ItemStack ingredient : Recipes.silverArmorRecipe(plugin).getIngredientMap().values())
			{
				if(ingredient!=null)
				{
					bookPages.add("\n§r\n " + n + ") " + ingredient.getType().name()); 
					n++;
				}
			}						
		}

		BookMeta bookMeta = (BookMeta) book.getItemMeta();
		bookMeta.setTitle(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.LoreBookTitle, ChatColor.GOLD));//ChatColor.GOLD + "The Big Book of Werewolves");
		bookMeta.setAuthor("DogOnFire");
		bookMeta.setPages(bookPages);
		book.setItemMeta(bookMeta);

		// Add title
		ItemMeta itemMeta = book.getItemMeta();

		itemMeta.setDisplayName(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.LoreBookTitle, ChatColor.GOLD));

		// Add description
		List<String> pages = new ArrayList<String>();

		pages.add(ChatColor.GRAY + "All you ever wanted to know about werewolves");
		pages.add(ChatColor.GRAY + "but were afraid to ask");

		itemMeta.setLore(pages);

		// Add NBT data
		itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, "LoreBook");

		book.setItemMeta(itemMeta);

		return book;
	}

	public boolean isSilverSword(ItemStack IS)
	{
		return isWerewolfItem(IS, "SilverSword");
	}

	public boolean isCurePotion(ItemStack IS)
	{
		return isWerewolfItem(IS, "CurePotion");
	}

	public boolean isInfectionPotion(ItemStack IS)
	{
		return isWerewolfItem(IS, "InfectionPotion");
	}
	
	public boolean isWolfbanePotion(ItemStack IS)
	{
		return isWerewolfItem(IS, "WolfbanePotion");
	}

	public boolean isLoreBook(ItemStack IS)
	{
		return isWerewolfItem(IS, "LoreBook");
	}
}
