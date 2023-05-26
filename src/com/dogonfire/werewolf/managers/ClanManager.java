package com.dogonfire.werewolf.managers;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.dogonfire.werewolf.Werewolf;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;


public class ClanManager
{
	private Werewolf plugin;

	public enum ClanType
	{
		WildBite, WerewolfBite, Potion;
	}

	private long						lastSaveTime		= 0L;
	
	private HashMap<ClanType, String>	werewolfAccount		= new HashMap<ClanType, String>();
	private HashMap<ClanType, UUID>		werewolfAccountId	= new HashMap<ClanType, UUID>();

	private HashMap<ClanType, String>	werewolfTextures	= new HashMap<ClanType, String>();
	private HashMap<ClanType, String>	werewolfTextureSignatures	= new HashMap<ClanType, String>();

	private FileConfiguration			clansConfig			= null;
	private File						clansConfigFile		= null;
	private HashMap<ClanType, Double>	totalClanPoints		= new HashMap<ClanType, Double>();
	private HashMap<String, Double>		playerClanPoints	= new HashMap<String, Double>();
	private HashMap<ClanType, String>	clanNames			= new HashMap<ClanType, String>();
	private String						datePattern			= "HH:mm:ss dd-MM-yyyy";
	
	private UUID 						alphaAccountId		= null;
	private String 						alphaAccount		= "";

	public ClanManager(Werewolf plugin)
	{
		this.plugin = plugin;
	}

	//Bloodmoon da508eccdbd946c5809547b91aa4ff5f
	//Silvermane b68a8f007d244c52b6ad1423bfbe26ee
	//Witherfang 68c5d9f216384a0f8f56d43fb368ad17
	//https://sessionserver.mojang.com/session/minecraft/profile/039c53aa-5873-4420-a095-9af971321408?unsigned=false
	//https://sessionserver.mojang.com/session/minecraft/profile/b68a8f00-7d24-4c52-b6ad-1423bfbe26ee?unsigned=false
	//https://sessionserver.mojang.com/session/minecraft/profile/da508ecc-dbd9-46c5-8095-47b91aa4ff5f?unsigned=false
	//UUID of player DogOnFire is 54f089fc-19e7-4a3e-9902-b90c5eb1cbed
	public void load()
	{
		if (this.clansConfigFile == null)
		{
			this.clansConfigFile = new File(this.plugin.getDataFolder(), "clans.yml");
		}
		this.clansConfig = YamlConfiguration.loadConfiguration(this.clansConfigFile);

		this.plugin.log("Loaded " + this.clansConfig.getKeys(false).size() + " clans.");
		
		this.alphaAccount = plugin.alphaAccountName;
		this.alphaAccountId = UUID.fromString(plugin.alphaAccountUUID);
		
		this.clanNames.put(ClanType.Potion, plugin.potionName);
		this.clanNames.put(ClanType.WerewolfBite, plugin.werewolfBiteName);
		this.clanNames.put(ClanType.WildBite, plugin.wildBiteName);

		this.werewolfAccount.put(ClanType.Potion, plugin.potionAccountName);
		this.werewolfAccount.put(ClanType.WerewolfBite, plugin.werewolfBiteAccountName);
		this.werewolfAccount.put(ClanType.WildBite, plugin.wildBiteAccountName);

		this.werewolfAccountId.put(ClanType.Potion,       UUID.fromString(plugin.potionAccountUUID));
		this.werewolfAccountId.put(ClanType.WerewolfBite, UUID.fromString(plugin.werewolfBiteAccountUUID));
		this.werewolfAccountId.put(ClanType.WildBite,     UUID.fromString(plugin.wildBiteAccountUUID));
					
		this.werewolfTextures.put(ClanType.Potion,       plugin.potionSkinValue);
		this.werewolfTextures.put(ClanType.WerewolfBite, plugin.werewolfBiteSkinValue);
		this.werewolfTextures.put(ClanType.WildBite,     plugin.wildBiteSkinValue);
						
		this.werewolfTextureSignatures.put(ClanType.Potion,       plugin.potionSkinSignature);
		this.werewolfTextureSignatures.put(ClanType.WerewolfBite, plugin.werewolfBiteSkinSignature);
		this.werewolfTextureSignatures.put(ClanType.WildBite,     plugin.wildBiteSkinSignature);
		
		for (ClanType clan : ClanType.values())
		{
			this.totalClanPoints.put(clan, Double.valueOf(0.0D));
		}
		
		resetClanScores();
	}

	public void saveTimed()
	{
		if (System.currentTimeMillis() - this.lastSaveTime < 180000L)
		{
			return;
		}
		
		save();
	}

	public void save()
	{
		this.lastSaveTime = System.currentTimeMillis();
		if ((this.clansConfig == null) || (this.clansConfigFile == null))
		{
			return;
		}
		
		try
		{
			this.clansConfig.save(this.clansConfigFile);
		}
		catch (Exception ex)
		{
			this.plugin.log("Could not save config to " + this.clansConfigFile + ": " + ex.getMessage());
		}
	}

	public String getClanName(UUID playerId)
	{
		return this.clanNames.get(Werewolf.getWerewolfManager().getWerewolfClan(playerId));
	}

	public String getWerewolfAccountForClan(ClanType type)
	{
		return this.werewolfAccount.get(type);
	}

	public UUID getWerewolfAccountIdForClan(ClanType type)
	{
		return this.werewolfAccountId.get(type);
	}

	
	public String getWerewolfTextureForClan(ClanType type)
	{
		return this.werewolfTextures.get(type);
	}
	
	public String getWerewolfTextureSignatureForClan(ClanType type)
	{
		return this.werewolfTextureSignatures.get(type);
	}

	public String getWerewolfAccountForAlpha(ClanType type)
	{
		return this.alphaAccount;
	}

	public UUID getWerewolfAccountIdForAlpha(ClanType type)
	{
		return this.alphaAccountId;
	}

	public String getWerewolfTextureForAlpha(ClanType type)
	{
		return plugin.alphaSkinValue;
	}

	public String getWerewolfTextureSignatureForAlpha(ClanType type)
	{
		return plugin.alphaSkinSignature;
	}
	
	public void handleMobKill(Player player, ClanType clanType, EntityType mobType)
	{
		double points = 0.0D;
		if (this.playerClanPoints.containsKey(player.getName()))
		{
			points = this.playerClanPoints.get(player.getName()).doubleValue();
		}
		
		switch (mobType)
		{
			case WARDEN:
			case ENDER_DRAGON:
			case WITHER:
				points = 50.0D;
				break;
			case PLAYER:
			case VILLAGER:
			case WANDERING_TRADER:
			case PILLAGER:
			case VINDICATOR:
			case EVOKER:
			case ILLUSIONER:
			case RAVAGER:
			case ZOGLIN:
			case PIGLIN_BRUTE:
			case ELDER_GUARDIAN:
			case ENDERMAN:
				points = 5.0D;
				break;
			case POLAR_BEAR:
			case HORSE:
			case DONKEY:
			case MULE:
			case COW:
			case MUSHROOM_COW:
			case ZOMBIE:
			case ZOMBIE_VILLAGER:
			case LLAMA:
			case TRADER_LLAMA:
			case PANDA:
			case DROWNED:
			case HUSK:
			case DOLPHIN:
			case PIGLIN:
			case STRIDER:
			case ZOMBIFIED_PIGLIN:
			case WITCH:
			case GUARDIAN:
			case SKELETON:
			case WITHER_SKELETON:
			case CAMEL:
			case SNIFFER:
				points = 3.0D;
				break;
			case SHEEP:
			case GOAT:
			case WOLF:
			case CAT:
			case OCELOT:
			case RABBIT:
			case PIG:
			case TURTLE:
			case FOX:
			case SPIDER:
			case GLOW_SQUID:
			case PHANTOM:
				points = 2.0D;
				break;
			case BLAZE:
			case STRAY:
			case CHICKEN:
			case PARROT:
			case AXOLOTL:
			case SQUID:
			case BEE:
			case COD:
			case SALMON:
			case PUFFERFISH:
			case TROPICAL_FISH:
			case FROG:
			case TADPOLE:
			case BAT:
			case VEX:
				points = 1.0D;
				break;
			case ALLAY:
			case ENDERMITE:
			case SNOWMAN:
			case MAGMA_CUBE:
			case SILVERFISH:
			default:
				points = 0.5D;
				break;
		}
				
		if (points > 0)
		{
			this.playerClanPoints.put(player.getName(), Double.valueOf(points));

			double totalPoints = ((Double) this.totalClanPoints.get(clanType)).doubleValue();

			totalPoints += points / Werewolf.getWerewolfManager().getWerewolfClanMembers(clanType).size();

			this.totalClanPoints.put(clanType, Double.valueOf(totalPoints));

			//Werewolf.getLanguageManager().setAmount("" + points);
			//this.plugin.sendInfo(player, Werewolf.getLanguageManager().getLanguageString(LanguageManager.LANGUAGESTRING.KilledMobPoints, ChatColor.AQUA));
		}
	}

	public List<ClanType> getClansRanked()
	{
		List<ClanType> clanList = new ArrayList<ClanType>();
		for (ClanType clan : ClanType.values())
		{
			clanList.add(clan);
		}
		Collections.sort(clanList, new ClanComparator());

		return clanList;
	}

	public double getClanPoint(ClanType clan)
	{
		return ((Double) this.totalClanPoints.get(clan)).doubleValue();
	}

	public String getClanName(ClanType clan)
	{
		return (String) this.clanNames.get(clan);
	}

	// TODO Bloodrage clan?
	public void updateClans()
	{
		if (!this.plugin.useClans)
		{
			return;
		}
		List<ClanType> clanList = getClansRanked();
		if ((((Double) this.totalClanPoints.get(ClanType.Potion)).doubleValue() > 0.0D) || (((Double) this.totalClanPoints.get(ClanType.WildBite)).doubleValue() > 0.0D) || (((Double) this.totalClanPoints.get(ClanType.WerewolfBite)).doubleValue() > 0.0D))
		{
//			this.plugin.getServer().broadcastMessage(ChatColor.GOLD + "------------ Werewolf Clan Summary ------------");
//			this.plugin.getServer().broadcastMessage(ChatColor.GOLD + "1) " + ChatColor.WHITE + (String) this.clanNames.get(clanList.get(0)) + ChatColor.AQUA + " - " + String.format("%1$,.2f", new Object[] { this.totalClanPoints.get(clanList.get(0)) }) + " points");
//			this.plugin.getServer().broadcastMessage(ChatColor.GOLD + "2) " + ChatColor.WHITE + (String) this.clanNames.get(clanList.get(1)) + ChatColor.AQUA + " - " + String.format("%1$,.2f", new Object[] { this.totalClanPoints.get(clanList.get(1)) }) + " points");
//			this.plugin.getServer().broadcastMessage(ChatColor.GOLD + "3) " + ChatColor.WHITE + (String) this.clanNames.get(clanList.get(2)) + ChatColor.AQUA + " - " + String.format("%1$,.2f", new Object[] { this.totalClanPoints.get(clanList.get(2)) }) + " points");
//			this.plugin.getServer().broadcastMessage("" + ChatColor.GOLD);
//			this.plugin.getServer().broadcastMessage(ChatColor.GOLD + (String) this.clanNames.get(clanList.get(0)) + " now has the " + ChatColor.DARK_RED + " Blood Rage " + ChatColor.DARK_RED + " power!");
//
			setBloodrageClan((ClanType) clanList.get(0));
		}
	}

	private void setBloodrageClan(ClanType clan)
	{
	}

	/*
	public void updateClan(ClanType clanType)
	{
		Set<String> werewolves = Werewolf.getWerewolfManager().getWerewolvesInClan(clanType);

		List<String> werewolfList = new ArrayList();
		for (String werewolf : werewolves)
		{
			werewolfList.add(werewolf);
		}
		
		Collections.sort(werewolfList, new ClanMemberComparator());

		resetClanScores();
	}
*/
	private void resetClanScores()
	{
		this.playerClanPoints.clear();
		for (ClanType clan : ClanType.values())
		{
			double points = ((Double) this.totalClanPoints.get(clan)).doubleValue();

			this.totalClanPoints.put(clan, Double.valueOf(points / 2.0D));
		}
	}

	public void setHomeForClan(ClanType clan, Location location)
	{
		this.clansConfig.set(clan.name() + ".Home.X", Double.valueOf(location.getX()));
		this.clansConfig.set(clan.name() + ".Home.Y", Double.valueOf(location.getY()));
		this.clansConfig.set(clan.name() + ".Home.Z", Double.valueOf(location.getZ()));
		this.clansConfig.set(clan.name() + ".Home.World", location.getWorld().getName());

		saveTimed();
	}

	public Location getHomeForClan(ClanType clan)
	{
		Location location = new Location(null, 0.0D, 0.0D, 0.0D);

		String worldName = this.clansConfig.getString(clan.name() + ".Home.World");
		if (worldName == null)
		{
			return null;
		}
		location.setWorld(this.plugin.getServer().getWorld(worldName));

		location.setX(this.clansConfig.getDouble(clan.name() + ".Home.X"));
		location.setY(this.clansConfig.getDouble(clan.name() + ".Home.Y"));
		location.setZ(this.clansConfig.getDouble(clan.name() + ".Home.Z"));

		return location;
	}
	
	
	public UUID getAlpha(ClanType clan)
	{
		String alphaId = this.clansConfig.getString(clan.name() + ".Alpha");

		try
		{
			return UUID.fromString(alphaId);
		}
		catch(Exception ex)
		{
			UUID alphaPlayerId = Werewolf.getWerewolfManager().getAlphaCandidate(clan);
			
			if (alphaPlayerId != null)
			{
				assignAlphaInClan(clan, alphaPlayerId);
			}
		}

		return null;
	}

	public boolean isAlpha(UUID playerId)
	{
		for (ClanType clan : ClanType.values())
		{
			String alphaId = this.clansConfig.getString(clan.name() + ".Alpha");

			try
			{
				if(UUID.fromString(alphaId).equals(playerId))
				{
					return true;
				}
			}
			catch(Exception ex)
			{
				
				
			}
		}
		
		return false;
	}

	public boolean isAlpha(ClanType clan, UUID playerId)
	{
		String alphaId = this.clansConfig.getString(clan.name() + ".Alpha");

		try
		{
			return UUID.fromString(alphaId).equals(playerId);			
		}
		catch(Exception ex)
		{
			
			
		}
				
		return false;
	}

	public void assignAlphaInClan(ClanType clan, UUID playerId)
	{
		if(playerId==null)
		{	
			this.clansConfig.set(clan.name() + ".Alpha", null);			
		}
		else
		{
			this.clansConfig.set(clan.name() + ".Alpha", playerId.toString());			
		}		

		saveTimed();
	}

	public boolean hasRecentCall(ClanType clan)
	{
		DateFormat formatter = new SimpleDateFormat(this.datePattern);
		Date thisDate = new Date();
		Date transformDate = null;

		String transformDateString = this.clansConfig.getString(clan.name() + ".LastCallTime");
		try
		{
			transformDate = formatter.parse(transformDateString);
		}
		catch (Exception ex)
		{
			this.plugin.log(clan.name() + " has invalid LastCallTime date. Resetting.");
			transformDate = new Date();
			transformDate.setTime(0L);
		}
		long diff = thisDate.getTime() - transformDate.getTime();
		long diffSeconds = diff / 1000L;

		return diffSeconds < 30L;
	}

	public void setLastCall(ClanType clan)
	{
		Date thisDate = new Date();
		DateFormat formatter = new SimpleDateFormat(this.datePattern);

		this.clansConfig.set(clan.name() + ".LastCallTime", formatter.format(thisDate));

		saveTimed();
	}

	public class ClanMemberComparator implements Comparator<String>
	{
		public ClanMemberComparator()
		{
		}

		public int compare(String member1, String member2)
		{
			return (int) (((Double) ClanManager.this.playerClanPoints.get(member2)).doubleValue() - ((Double) ClanManager.this.playerClanPoints.get(member1)).doubleValue());
		}
	}

	public class ClanComparator implements Comparator<ClanManager.ClanType>
	{
		public int compare(ClanManager.ClanType clan1, ClanManager.ClanType clan2)
		{
			return (int) (((Double) ClanManager.this.totalClanPoints.get(clan2)).doubleValue() - ((Double) ClanManager.this.totalClanPoints.get(clan1)).doubleValue());
		}
	}
}