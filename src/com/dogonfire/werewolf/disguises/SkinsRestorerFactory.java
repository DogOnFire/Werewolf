package com.dogonfire.werewolf.disguises;

import com.dogonfire.werewolf.managers.ClanManager;
import com.dogonfire.werewolf.Werewolf;
import net.skinsrestorer.api.PlayerWrapper;
import net.skinsrestorer.api.SkinsRestorerAPI;
import net.skinsrestorer.api.exception.SkinRequestException;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SkinsRestorerFactory implements IWerewolfDisguiseFactory
{
    private SkinsRestorerAPI skinsRestorerAPI;

    public class SkinsRestorerWerewolfDisguise extends WerewolfDisguise
    {
        public SkinsRestorerWerewolfDisguise(UUID accountId, String accountName) {
            super(accountId, accountName);
        }

        @Override
        public boolean disguise(Player player, String werewolfName) {
            Werewolf.instance().logDebug("Disguising player " + player.getName() + " using SkinsRestorer...");

            if(!Werewolf.instance().getServer().getPluginManager().getPlugin("SkinsRestorer").isEnabled())
            {
                Werewolf.instance().logDebug("Didn't disguise player... SkinsRestorer is not enabled!");
                return false;
            }

            Werewolf.instance().logDebug("[SkinsRestorer] Started Disguising of player: " + player.getName());

            try
            {
                // Cache the old, current skin
                skinsRestorerAPI.setSkinData(player.getName() + "_cache", skinsRestorerAPI.getSkinData(player.getName()));

                // #setSkin() for player skin
                if (Werewolf.getClanManager().isAlpha(player.getUniqueId())) {
                    Werewolf.instance().logDebug("[SkinsRestorer] Disguising " + player.getName() + " using the Alpha skin.");
                    skinsRestorerAPI.setSkin(player.getName(), "Alpha");
                }
                else {
                    String clanName = Werewolf.getWerewolfManager().getWerewolfClan(player.getUniqueId()).name();
                    Werewolf.instance().logDebug("[SkinsRestorer] Disguising " + player.getName() + " using the " + clanName + " skin.");
                    skinsRestorerAPI.setSkin(player.getName(), clanName);
                }

                // Force skin refresh for player
                skinsRestorerAPI.applySkin(new PlayerWrapper(player));
                return true;
            }
            catch (SkinRequestException e)
            {
                Werewolf.instance().log("[SkinsRestorer] Failed to apply skin for player");
                e.printStackTrace();
            }
            return false;
        }

        @Override
        public boolean undisguise(Player player) {
            Werewolf.instance().logDebug("Undisguising player " + player.getName() + " using SkinsRestorer...");

            if(!Werewolf.instance().getServer().getPluginManager().getPlugin("SkinsRestorer").isEnabled())
            {
                Werewolf.instance().logDebug("Didn't undisguise player... SkinsRestorer is not enabled!");
                return false;
            }

            Werewolf.instance().logDebug("[SkinsRestorer] Undisguise - Start undisguise!");

            try
            {
                skinsRestorerAPI.setSkin(player.getName(), player.getName() + "_cache");

                // Force skin refresh for player
                skinsRestorerAPI.applySkin(new PlayerWrapper(player));
                return true;
            }
            catch (SkinRequestException e)
            {
                Werewolf.instance().log("[SkinsRestorer] Failed to apply skin for player");
                e.printStackTrace();
            }
            return false;
        }
    }

    public SkinsRestorerFactory()
    {
        this.skinsRestorerAPI = SkinsRestorerAPI.getApi();

        try
        {
            skinsRestorerAPI.setSkinData("Alpha", skinsRestorerAPI.createPlatformProperty("textures", Werewolf.instance().alphaSkinValue, Werewolf.instance().alphaSkinSignature));
            skinsRestorerAPI.setSkinData(ClanManager.ClanType.Potion.name(), skinsRestorerAPI.createPlatformProperty("textures", Werewolf.instance().potionSkinValue, Werewolf.instance().potionSkinSignature));
            skinsRestorerAPI.setSkinData(ClanManager.ClanType.WerewolfBite.name(), skinsRestorerAPI.createPlatformProperty("textures", Werewolf.instance().werewolfBiteSkinValue, Werewolf.instance().werewolfBiteSkinSignature));
            skinsRestorerAPI.setSkinData(ClanManager.ClanType.WildBite.name(), skinsRestorerAPI.createPlatformProperty("textures", Werewolf.instance().wildBiteSkinValue, Werewolf.instance().wildBiteSkinSignature));
        }
        catch (NullPointerException e)
        {
            Werewolf.instance().log("[ERROR] Couldn't save Werewolf skin data to SkinsRestorer's cache... Werewolves are disabled!");
            Werewolf.instance().onDisable();
        }
    }

    @Override
    public WerewolfDisguise newDisguise(UUID disguiseAccountId, String disguiseAccountName)
    {
        return new SkinsRestorerWerewolfDisguise(disguiseAccountId, disguiseAccountName);
    }
}
