package com.dogonfire.werewolf.versioning;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class UpdateChecker
{
	private final int			projectID				= 39095;
	private final String		apiKey					= null;
	private static final String	API_QUERY				= "/servermods/files?projectIds=";
	private static final String	API_HOST				= "https://api.curseforge.com";
	private static String		versionGameVersion;

	public String getLatestVersionName()
	{
		URL url = null;

		String versionName = null;
		try
		{
			url = new URL(API_HOST + API_QUERY + projectID);
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();

			return versionName;
		}
		try
		{
			URLConnection conn = url.openConnection();
			if (this.apiKey != null)
			{
				conn.addRequestProperty("X-API-Key", this.apiKey);
			}
			conn.addRequestProperty("User-Agent", "Werewolf (by DoggyOnFire)");

			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String response = reader.readLine();

			JSONArray array = (JSONArray) JSONValue.parse(response);
			if (array.size() > 0)
			{
				JSONObject latest = (JSONObject) array.get(array.size() - 1);

				versionName = (String) latest.get("name");

				versionGameVersion = (String) latest.get("gameVersion");
			}
			else
			{
				System.out.println("There are no files for this project");
			}
		}
		catch (IOException e)
		{
			return versionName;
		}
		return versionName;
	}

	public String getLatestVersionGameVersion()
	{
		return versionGameVersion;
	}

	public String getLatestVersionLink()
	{
		return "http://dev.bukkit.org/bukkit-plugins/werewolf/";
		//return versionLink;
	}
}
