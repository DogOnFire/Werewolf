package com.dogonfire.werewolf;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.ChatColor;

public class ItemManager
{
	private Werewolf	plugin;

	public ItemManager(Werewolf plugin)
	{
		this.plugin = plugin;
	}
	
	public void setupRecipes()
	{
		if(plugin.craftableInfectionPotionEnabled)
		{			
			Bukkit.addRecipe(Recipes.InfectionPotion());
			plugin.logDebug("Cratable infection potions are enabled");
		}
		
		if(plugin.craftableCurePotionEnabled)
		{
			Bukkit.addRecipe(Recipes.CurePotion());
			plugin.logDebug("Craftable cure potions are enabled");
		}

		if(plugin.craftableWolfbanePotionEnabled)
		{
			Bukkit.addRecipe(Recipes.WolfbanePotion());
			plugin.logDebug("Craftable wolfbane potions are enabled");
		}

		if(plugin.craftableSilverSwordEnabled)
		{
			Bukkit.addRecipe(Recipes.SilverSwordRecipe());
			plugin.logDebug("Craftable silver swords are enabled");
		}

		if(plugin.craftableSilverArmorEnabled)
		{
			Bukkit.addRecipe(Recipes.SilverArmorRecipe());
			plugin.logDebug("Craftable silver armors are enabled");
		}

		if(plugin.craftableLoreBookEnabled)
		{
			Bukkit.addRecipe(Recipes.LoreBook());
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
	    potionMeta.setMainEffect(PotionEffectType.CONFUSION);

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
	    potionMeta.setMainEffect(PotionEffectType.CONFUSION);

		potion.setItemMeta(potionMeta);

		return potion;
	}
	
	public ItemStack newWolfbanePotion()
	{
		Potion potion = new Potion(PotionType.POISON, 2);
		potion.setSplash(true);
		
		ItemStack itemStack = potion.toItemStack(1);

		PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();

		potionMeta.setDisplayName(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.WolfbanePotionTitle, ChatColor.GOLD));

	    List<String> pages = new ArrayList<String>();

	    Werewolf.getLanguageManager().setAmount(String.valueOf(plugin.wolfbaneUntransformChance));
	    pages.add(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.WolfbanePotionDescription1, ChatColor.LIGHT_PURPLE));
	    pages.add(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.WolfbanePotionDescription2, ChatColor.GRAY));
	    
	    potionMeta.setLore(pages);
	    potionMeta.setMainEffect(PotionEffectType.CONFUSION);

	    itemStack.setItemMeta(potionMeta);
		
		return itemStack;
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
		
		bookPages.add("§lTransformation\n§r\nDuring transformation, a werewolf will drop his clothes to the ground. But for each fullmoon, a werewolf will gain more control over his condition.\nAfter having experienced enough fullmoons, the werewolf may be able to not drop his clothes or even transform at will!");

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
			for(ItemStack ingredient : Recipes.CurePotion().getIngredientList())
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
			for(ItemStack ingredient : Recipes.InfectionPotion().getIngredientList())
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
			for(ItemStack ingredient : Recipes.WolfbanePotion().getIngredientList())
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
			for(ItemStack ingredient : Recipes.SilverSwordRecipe().getIngredientMap().values())
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
			for(ItemStack ingredient : Recipes.SilverArmorRecipe().getIngredientMap().values())
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

		book.setItemMeta(itemMeta);

		return book;
	}

	
	public boolean isSilverSword(ItemStack IS)
	{
		ItemMeta itemMeta = IS.getItemMeta();
		if (itemMeta == null)
		{
			return false;
		}
		if (itemMeta.getDisplayName() == null)
		{
			return false;
		}
		if (itemMeta.getDisplayName().contains(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.SilverSwordTitle, ChatColor.GOLD)))
		{
			return true;
		}
		return false;
	}

	public boolean isCurePotion(ItemStack IS)
	{
		ItemMeta itemMeta = IS.getItemMeta();
		if (itemMeta == null)
		{
			return false;
		}
		if (itemMeta.getDisplayName() == null)
		{
			return false;
		}
		if (itemMeta.getDisplayName().contains(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.CurePotionTitle, ChatColor.GOLD)))
		{
			return true;
		}
		return false;
	}

	public boolean isInfectionPotion(ItemStack IS)
	{
		ItemMeta itemMeta = IS.getItemMeta();
		if (itemMeta == null)
		{
			return false;
		}
		if (itemMeta.getDisplayName() == null)
		{
			return false;
		}
		if (itemMeta.getDisplayName().contains(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.InfectionPotionTitle, ChatColor.GOLD)))
		{
			return true;
		}
		return false;
	}
	
	public boolean isWolfbanePotion(ItemStack IS)
	{
		ItemMeta itemMeta = IS.getItemMeta();
		if (itemMeta == null)
		{
			return false;
		}
		if (itemMeta.getDisplayName() == null)
		{
			return false;
		}
		if (itemMeta.getDisplayName().contains(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.WolfbanePotionTitle, ChatColor.GOLD)))
		{
			return true;
		}
		return false;
	}

	public boolean isLoreBook(ItemStack IS)
	{
		ItemMeta itemMeta = IS.getItemMeta();
		if (itemMeta == null)
		{
			return false;
		}
		if (itemMeta.getDisplayName() == null)
		{
			return false;
		}
		if (itemMeta.getDisplayName().contains(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.LoreBookTitle, ChatColor.GOLD)))
		{
			return true;
		}
		return false;
	}
}
