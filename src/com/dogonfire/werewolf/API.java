package com.dogonfire.werewolf;

import java.util.UUID;

import org.bukkit.inventory.ItemStack;

public class API
{
	public static void setFullWerewolf(UUID playerId)
	{
		// Werewolf.getWerewolfManager().makeWerewolf(player, turnNow, clan);
	}

	public static ItemStack newWerewolfSilverSword()
	{
		return Werewolf.getItemManager().newSilverSword(1);
	}

	public static ItemStack newWerewolfLorebook()
	{
		return Werewolf.getItemManager().newLoreBook();
	}

	public static ItemStack newWerewolfCurePotion()
	{
		return Werewolf.getItemManager().newCurePotion();
	}

	public static ItemStack newWerewolfInfectionPotion()
	{
		return Werewolf.getItemManager().newInfectionPotion();
	}

	public static ItemStack newWerewolfWolfbanePotion()
	{
		return Werewolf.getItemManager().newWolfbanePotion();
	}

	public static boolean isWerewolf(UUID playerId)
	{
		return Werewolf.getWerewolfManager().isWerewolf(playerId);
	}

	public static int getNumberOfWerewolves()
	{
		return Werewolf.getWerewolfManager().getNumberOfWerewolves();
	}
}
