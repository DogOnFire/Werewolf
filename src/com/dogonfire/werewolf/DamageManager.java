package com.dogonfire.werewolf;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class DamageManager
{
	public static int 		SilverSwordMultiplier = 3;
	public static double	werewolfHandDamage			= 8.0D;
	public static double	werewolfItemDamage			= 3.0D;
	public static double	SilverArmorMultiplier		= 0.25D;
		
	public static double getDamageFromItemInHand(Player player)
	{
		ItemStack is = player.getInventory().getItemInMainHand();
		if (is == null)
		{
			return 1;
		}
		
		double damage = 1;//is.getDurability();
		Material mat = is.getType();		
					
		switch (mat)
		{
			case WOODEN_PICKAXE: damage = 3; break;
			case GOLDEN_PICKAXE: damage = 3; break;
			case STONE_PICKAXE: damage = 4; break;
			case IRON_PICKAXE: damage = 5; break;
			case DIAMOND_PICKAXE: damage = 6; break;

			case WOODEN_SHOVEL: damage = 2; break;
			case GOLDEN_SHOVEL: damage = 2; break;
			case STONE_SHOVEL: damage = 3; break;
			case IRON_SHOVEL: damage = 4; break;
			case DIAMOND_SHOVEL: damage = 5; break;

			case WOODEN_AXE: damage = 4; break;
			case GOLDEN_AXE: damage = 4; break;
			case STONE_AXE: damage = 5; break;
			case IRON_AXE: damage = 6; break;
			case DIAMOND_AXE: damage = 7; break;

			case WOODEN_SWORD: damage = 5; break;
			case GOLDEN_SWORD: damage = 5; break;
			case STONE_SWORD: damage = 6; break;
			case IRON_SWORD: damage = 7; break;
			case DIAMOND_SWORD: damage = 8; break;
			
			default: damage = 1; break;
		}

		if(Werewolf.getItemManager().isSilverSword(is))
		{
			damage *= SilverSwordMultiplier; 
		}
		/*
		switch (mat)
		{
			case GOLD_SWORD:
				damage *= SilverSwordMultiplier; 
				break;
			default: 
				break;
		}
		*/
				
		return damage;
	}

	public static boolean canPlayerHit(Player player, Location targetPoint)
	{
		Vector diff = targetPoint.toVector().subtract(player.getLocation().toVector());
		double length = diff.length();
		
		diff = diff.normalize();
		if (length > 4.0D)
		{			
			return false;
		}

		double dotProduct = player.getLocation().getDirection().dot(diff);

		return dotProduct > 0.95D;
	}

	public static boolean isPlayerInCone(Player player, Location startpoint, int radius, int degrees, int direction)
	{
		int[] startPos = { (int) startpoint.getX(), (int) startpoint.getZ() };

		int[] endA = { (int) (radius * Math.cos(direction - degrees / 2)), (int) (radius * Math.sin(direction - degrees / 2)) };

		Location l = player.getLocation();
		if (!isPointInCircle(startPos[0], startPos[1], radius, l.getBlockX(), l.getBlockY()))
		{
			return false;
		}
		int[] playerVector = getVectorForPoints(startPos[0], startPos[1], l.getBlockX(), l.getBlockY());

		double angle = getAngleBetweenVectors(endA, playerVector);
		return ((Math.toDegrees(angle) < degrees) && (Math.toDegrees(angle) > 0.0D));
	}

	public static int[] getVectorForPoints(int x1, int y1, int x2, int y2)
	{
		return new int[] { x2 - x1, y2 - y1 };
	}

	public static boolean isPointInCircle(int cx, int cy, int radius, int px, int py)
	{
		double dist = px - cx ^ 2 + (py - cy) ^ 0x2;
		return dist < (radius ^ 0x2);
	}

	public static double getAngleBetweenVectors(int[] vector1, int[] vector2)
	{
		return Math.atan2(vector2[1], vector2[0]) - Math.atan2(vector1[1], vector1[0]);
	}
}