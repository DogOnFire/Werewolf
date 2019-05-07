package com.dogonfire.werewolf;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.Plugin;

public class Recipes
{
	/*
	if(plugin.craftableSilverSwordEnabled)
	{
		ItemStack silverSword = newSilverSword(1);

		ShapedRecipe silverSwordRecipe = new ShapedRecipe(silverSword);
		silverSwordRecipe.shape("OBX", "OXB", "SOO");
		silverSwordRecipe.setIngredient('X', Material.IRON_INGOT);
		silverSwordRecipe.setIngredient('S', Material.STICK);
		silverSwordRecipe.setIngredient('B', Material.BLAZE_POWDER);
		Bukkit.addRecipe(silverSwordRecipe);
	}

	if(plugin.craftableLoreBookEnabled)
	{
		ItemStack loreBook = newLoreBook();

		ShapedRecipe loreBookRecipe = new ShapedRecipe(loreBook);
		loreBookRecipe.shape("BPB", "PXP", "BPB");
		loreBookRecipe.setIngredient('X', Material.BOOK);
		loreBookRecipe.setIngredient('P', Material.PAPER);
		loreBookRecipe.setIngredient('B', Material.BLAZE_POWDER);
		Bukkit.addRecipe(loreBookRecipe);
	}

	if(plugin.craftableInfectionPotionEnabled)
	{
		ItemStack infectionPotion = newInfectionPotion();

		ShapelessRecipe infectionRecipe = new ShapelessRecipe(infectionPotion);
		infectionRecipe.addIngredient(Material.GLASS_BOTTLE);
		infectionRecipe.addIngredient(Material.BLAZE_POWDER);
		infectionRecipe.addIngredient(Material.DEAD_BUSH);
		infectionRecipe.addIngredient(Material.SLIME_BALL);
		Bukkit.addRecipe(infectionRecipe);
	}
	
	if(plugin.craftableCurePotionEnabled)
	{
		ItemStack curePotion = newCurePotion();

		ShapelessRecipe cureRecipe = new ShapelessRecipe(curePotion);
		cureRecipe.addIngredient(Material.GLASS_BOTTLE);
		cureRecipe.addIngredient(Material.MILK_BUCKET);
		Bukkit.addRecipe(cureRecipe);
	}

	if(plugin.craftableWolfbanePotionEnabled)
	{
		ItemStack wolfbanePotion = newWolfbanePotion();

		ShapelessRecipe wolfbaneRecipe = new ShapelessRecipe(wolfbanePotion);
		wolfbaneRecipe.addIngredient(Material.GLASS_BOTTLE);
		wolfbaneRecipe.addIngredient(Material.MILK_BUCKET);
		wolfbaneRecipe.addIngredient(Material.WATER_BUCKET);
		wolfbaneRecipe.addIngredient(Material.DEAD_BUSH);
		Bukkit.addRecipe(wolfbaneRecipe);
	}	
	
	public static ShapedRecipe SilverSword()
	{
		ShapedRecipe apple = new ShapedRecipe(new ItemStack(Material.GOLDEN_APPLE, 1));
		apple.shape("GGG", "GAG", "GGG");

		apple.setIngredient('G', Material.GOLD_INGOT);
		apple.setIngredient('A', Material.APPLE);

		return apple;
	}
*/
	public static ShapedRecipe silverArmorRecipe(Plugin plugin)
	{
		ItemStack silverArmor = Werewolf.getItemManager().newSilverArmor(1);
		NamespacedKey key = new NamespacedKey(plugin, "SilverArmor");

		ShapedRecipe silverArmorRecipe = new ShapedRecipe(key, silverArmor);
		silverArmorRecipe.shape("BXB", "LSL", "BXB");
		silverArmorRecipe.setIngredient('X', Material.QUARTZ);
		silverArmorRecipe.setIngredient('S', Material.IRON_CHESTPLATE);
		silverArmorRecipe.setIngredient('B', Material.BLAZE_POWDER);
		silverArmorRecipe.setIngredient('L', Material.LEATHER);
				
		return silverArmorRecipe;
	}

	public static ShapedRecipe silverSwordRecipe(Plugin plugin)
	{
		ItemStack silverSword = Werewolf.getItemManager().newSilverSword(1);
		NamespacedKey key = new NamespacedKey(plugin, "SilverSword");

		ShapedRecipe silverSwordRecipe = new ShapedRecipe(key, silverSword);
		silverSwordRecipe.shape("OBX", "OXB", "SOO");
		silverSwordRecipe.setIngredient('X', Material.QUARTZ);
		silverSwordRecipe.setIngredient('S', Material.IRON_SWORD);
		silverSwordRecipe.setIngredient('B', Material.BLAZE_POWDER);
				
		return silverSwordRecipe;
	}

	public static ShapelessRecipe wolfbanePotion(Plugin plugin)
	{
		ItemStack potion = Werewolf.getItemManager().newWolfbanePotion();
		NamespacedKey key = new NamespacedKey(plugin, "WolfbanePotion");

		ShapelessRecipe wolfbaneRecipe = new ShapelessRecipe(key, potion);
		wolfbaneRecipe.addIngredient(Material.GLASS_BOTTLE);
		wolfbaneRecipe.addIngredient(Material.MILK_BUCKET);
		wolfbaneRecipe.addIngredient(Material.CARROT);
		wolfbaneRecipe.addIngredient(Material.GLOWSTONE);
		wolfbaneRecipe.addIngredient(Material.CACTUS);

		return wolfbaneRecipe;
	}

	public static ShapelessRecipe infectionPotion(Plugin plugin)
	{
		ItemStack potion = Werewolf.getItemManager().newInfectionPotion();
		NamespacedKey key = new NamespacedKey(plugin, "InfectionPotion");
		
		ShapelessRecipe infectionRecipe = new ShapelessRecipe(key, potion);
		infectionRecipe.addIngredient(Material.SLIME_BALL);
		infectionRecipe.addIngredient(Material.LEATHER);
		infectionRecipe.addIngredient(Material.BLAZE_POWDER);
		infectionRecipe.addIngredient(Material.GHAST_TEAR);
		infectionRecipe.addIngredient(Material.GLASS_BOTTLE);

		return infectionRecipe;
	}

	public static ShapelessRecipe curePotion(Plugin plugin)
	{
		ItemStack potion = Werewolf.getItemManager().newCurePotion();
		NamespacedKey key = new NamespacedKey(plugin, "CurePotion");

		ShapelessRecipe cureRecipe = new ShapelessRecipe(key, potion);
		cureRecipe.addIngredient(Material.POPPY);
		cureRecipe.addIngredient(Material.GLASS_BOTTLE);
		cureRecipe.addIngredient(Material.MILK_BUCKET);

		return cureRecipe;
	}

	public static ShapedRecipe loreBook(Plugin plugin)
	{
		NamespacedKey key = new NamespacedKey(plugin, "LoreBook");
		ShapedRecipe loreBookRecipe = new ShapedRecipe(key, Werewolf.getItemManager().newLoreBook());

		loreBookRecipe.shape("BPB", "PXP", "BPB");
		loreBookRecipe.setIngredient('X', Material.BOOK);
		loreBookRecipe.setIngredient('P', Material.PAPER);
		loreBookRecipe.setIngredient('B', Material.BLAZE_POWDER);

		return loreBookRecipe;
	}
}
