package com.dogonfire.werewolf.api;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.dogonfire.werewolf.Werewolf;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

// File is not currently used, but can be used in future versions, as it works fine.
public class MojangAPI
{
	public Map<UUID, MojangSkin> mojangSkinCollection = new HashMap<UUID, MojangSkin>();

	public MojangAPI()
	{}

	public class MojangSkin
	{
		String value;
		String signature;
		
		public MojangSkin(String value, String signature)
		{
			this.value = value;
			this.signature = signature;
		}
	}

	public Map<UUID, MojangSkin> getSkinDetails(Player player)
	{
		MojangSkin mojangSkin = new MojangSkin("eyJ0aW1lc3RhbXAiOjE1NjUxMjgzMjI3MDIsInByb2ZpbGVJZCI6Ijg2NjdiYTcxYjg1YTQwMDRhZjU0NDU3YTk3MzRlZWQ3IiwicHJvZmlsZU5hbWUiOiJTdGV2ZSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGMxYzc3Y2U4ZTU0OTI1YWI1ODEyNTQ0NmVjNTNiMGNkZDNkMGNhM2RiMjczZWI5MDhkNTQ4Mjc4N2VmNDAxNiJ9LCJDQVBFIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTUzY2FjOGI3NzlmZTQxMzgzZTY3NWVlMmI4NjA3MWE3MTY1OGYyMTgwZjU2ZmJjZThhYTMxNWVhNzBlMmVkNiJ9fX0=", "XmomE7hQjmADK1AJJajzFDuK2QCRyvkIdSaa/JSHJYQaqQWasxvR5h0bmxI3L2v+wYeRbso0+5b5xNGo5ATfuTilPD1vWmlplo0OmnTGYnW7SrmUf+QcC9J+CQusfCbQDOOMPbSlsZPMBQwRlCGPG8GzWDRXgNerw0MJoVYGi9pgI6ThGuoNDtz4wnDsnVFi35/j+SlttWJrDfIP7WlMQyVZjCZ1gcXFE6idNzf0kfH+CeTxd65ltINWQGDi7aIq51zKCSfuceQkQq0gK5//Smm5mC+OoiDGSpnArc+/1SeMWPBWB4dwPbm4YHrLiWGFk+gVu2sBopz9+8FsCo2nuOhjoWbaMCWTKCmSYnTcp3fJR5zlXAu2I2jOK+g36wOL1DLZcWkJkZbIjqhMJL8h7i+teeUbUMDL1lZUhb2QezDJjgYl8RzXavsSwf3gw2/fEejsSFsZ7mzgzEhmY4NXFQYrLOzu0RdajJ/W3ZrnzvpeXA431QqQ4ayUaLEWoEawgutPT9gkWPt42a90M35zAI2cdVuYxglrMLnCqlIanwTxYTpXn1e0eGJ4nbYFQttpCgoT2eRoCWmDeSFn0sN1pX1lvHKAS9aS7XamutGA+S1lYkjmb5CwpMPWBgrfXYw8DkzBQwMFPuwHY2gjFd288ScAVRYbU2HruQOYxiVcXts=");
		String uuid = player.getUniqueId().toString().replace("-", "");

		Map<UUID, MojangSkin> fullMap = new HashMap<UUID, MojangSkin>();

		try
		{
			// TODO Check and make sure this doesn't return being rate-limited!
			URL mojang = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
			InputStreamReader reader = new InputStreamReader(mojang.openStream());
			JsonObject textureProperty = new JsonParser().parse(reader).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();

			if (textureProperty.get("value").getAsString() != null && textureProperty.get("signature").getAsString() != null)
			{
				mojangSkin = new MojangSkin(textureProperty.get("value").getAsString(), textureProperty.get("signature").getAsString());
			} 
			else
			{
				Werewolf.instance().log("[ERROR] Could not fetch user's texture details! unDisguising user to default!");
			}
			reader.close();

			/*
			URLConnection uc = mojang.openConnection();
			uc.setUseCaches(false);
			uc.setDefaultUseCaches(false);
			uc.addRequestProperty("User-Agent", "Mozilla/5.0");
			uc.addRequestProperty("Cache-Control", "no-cache, no-store, must-revalidate");
			uc.addRequestProperty("Pragma", "no-cache");*/

			/*InputStream in = new BufferedInputStream(uc.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			JsonObject textureProperty = new JsonParser().parse(reader).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();

			if (textureProperty.get("value").getAsString() != null && textureProperty.get("signature").getAsString() != null)
			{
				mojangSkin = new MojangSkin(textureProperty.get("value").getAsString(), textureProperty.get("signature").getAsString());
			} 
			else
			{
				plugin.log("[ERROR] Could not fetch user's texture details! Cannot unDisguise user!");
			}*/

			/*@SuppressWarnings("resource")
			String json = new Scanner(uc.getInputStream(), "UTF-8").useDelimiter("\\A").next();
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(json);
			JSONArray properties = (JSONArray) ((JSONObject) obj).get("properties");
			for (int i = 0; i < properties.size(); i++) {
				try {
					JSONObject property = (JSONObject) properties.get(i);
					String name = (String) property.get("name");
					String value = (String) property.get("value");
					String signature = property.containsKey("signature") ? (String) property.get("signature") : null;
					mojangSkin = new MojangSkin(value, signature);
				} catch (Exception e) {
					e.printStackTrace();
					plugin.log("[ERROR] Could not fetch user's texture details! Cannot unDisguise user!");
				}
			}*/
		}
		catch (IOException e)
		{
			e.printStackTrace();
			Werewolf.instance().log("[ERROR] Could not fetch user's texture details! Cannot unDisguise user!");
		}

		mojangSkinCollection.put(player.getUniqueId(), mojangSkin);

        return fullMap;
	}
}