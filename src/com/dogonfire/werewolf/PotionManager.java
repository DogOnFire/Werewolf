package com.dogonfire.werewolf;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;

public class PotionManager
{
  private Werewolf plugin;
  private Random random = new Random();
  
  PotionManager(Werewolf plugin)
  {
    this.plugin = plugin;
  }
  
  public ItemStack createWerewolfInfectionPotion()
  {
    ItemStack potion = new ItemStack(Material.POTION);
    
    PotionMeta potionMeta = (PotionMeta)potion.getItemMeta();
    
    potionMeta.setDisplayName(ChatColor.GOLD + "Werewolf infection potion");
    
    List<String> pages = new ArrayList<String>();
    
    pages.add(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.InfectionPotionDescription1, ChatColor.GRAY));
    pages.add(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.InfectionPotionDescription2, ChatColor.GRAY));
    
    potionMeta.setLore(pages);
    potionMeta.setMainEffect(PotionEffectType.CONFUSION);
    
    potion.setItemMeta(potionMeta);
    
    return potion;
  }
  
  public ItemStack createWerewolfCurePotion()
  {
    ItemStack potion = new ItemStack(Material.POTION);
    
    PotionMeta potionMeta = (PotionMeta)potion.getItemMeta();
    
    potionMeta.setDisplayName(ChatColor.GOLD + "Werewolf cure potion");
    
    List<String> pages = new ArrayList();
    
    pages.add(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.CurePotionDescription1, ChatColor.GRAY));
    pages.add(Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.CurePotionDescription2, ChatColor.GRAY));
    
    potionMeta.setLore(pages);
    potionMeta.setMainEffect(PotionEffectType.CONFUSION);
    
    potion.setItemMeta(potionMeta);
    
    return potion;
  }
}
