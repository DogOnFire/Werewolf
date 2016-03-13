package com.dogonfire.werewolf;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

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
	public static ShapedRecipe SilverArmorRecipe()
	{
		ItemStack silverArmor = Werewolf.getItemManager().newSilverArmor(1);

		ShapedRecipe silverArmorRecipe = new ShapedRecipe(silverArmor);
		silverArmorRecipe.shape("BXB", "LSL", "BXB");
		silverArmorRecipe.setIngredient('X', Material.QUARTZ);
		silverArmorRecipe.setIngredient('S', Material.IRON_CHESTPLATE);
		silverArmorRecipe.setIngredient('B', Material.BLAZE_POWDER);
		silverArmorRecipe.setIngredient('L', Material.LEATHER);
				
		return silverArmorRecipe;
	}

	public static ShapedRecipe SilverSwordRecipe()
	{
		ItemStack silverSword = Werewolf.getItemManager().newSilverSword(1);

		ShapedRecipe silverSwordRecipe = new ShapedRecipe(silverSword);
		silverSwordRecipe.shape("OBX", "OXB", "SOO");
		silverSwordRecipe.setIngredient('X', Material.QUARTZ);
		silverSwordRecipe.setIngredient('S', Material.IRON_SWORD);
		silverSwordRecipe.setIngredient('B', Material.BLAZE_POWDER);
				
		return silverSwordRecipe;
	}

	public static ShapelessRecipe WolfbanePotion()
	{
		ItemStack potion = Werewolf.getItemManager().newWolfbanePotion();

		ShapelessRecipe wolfbaneRecipe = new ShapelessRecipe(potion);
		wolfbaneRecipe.addIngredient(Material.GLASS_BOTTLE);
		wolfbaneRecipe.addIngredient(Material.MILK_BUCKET);
		wolfbaneRecipe.addIngredient(Material.CARROT_ITEM);
		wolfbaneRecipe.addIngredient(Material.SULPHUR);
		wolfbaneRecipe.addIngredient(Material.CACTUS);

		return wolfbaneRecipe;
	}

	public static ShapelessRecipe InfectionPotion()
	{
		ItemStack potion = Werewolf.getItemManager().newInfectionPotion();

		ShapelessRecipe infectionRecipe = new ShapelessRecipe(potion);
		infectionRecipe.addIngredient(Material.SLIME_BALL);
		infectionRecipe.addIngredient(Material.LEATHER);
		infectionRecipe.addIngredient(Material.BLAZE_POWDER);
		infectionRecipe.addIngredient(Material.GHAST_TEAR);
		infectionRecipe.addIngredient(Material.GLASS_BOTTLE);

		return infectionRecipe;
	}

	public static ShapelessRecipe CurePotion()
	{
		ItemStack potion = Werewolf.getItemManager().newCurePotion();

		ShapelessRecipe cureRecipe = new ShapelessRecipe(potion);
		cureRecipe.addIngredient(Material.RED_ROSE);
		cureRecipe.addIngredient(Material.GLASS_BOTTLE);
		cureRecipe.addIngredient(Material.MILK_BUCKET);

		return cureRecipe;
	}

	public static ShapedRecipe LoreBook()
	{
		ShapedRecipe loreBookRecipe = new ShapedRecipe(Werewolf.getItemManager().newLoreBook());

		loreBookRecipe.shape("BPB", "PXP", "BPB");
		loreBookRecipe.setIngredient('X', Material.BOOK);
		loreBookRecipe.setIngredient('P', Material.PAPER);
		loreBookRecipe.setIngredient('B', Material.BLAZE_POWDER);

		return loreBookRecipe;
	}
}
