package com.Khorn.PTMBukkit;

import java.io.*;
import java.util.HashMap;

import com.Khorn.PTMBukkit.Commands.PTMCommandExecutor;
import com.Khorn.PTMBukkit.Generator.BiomeManagerOld;
import com.Khorn.PTMBukkit.Generator.ChunkProviderPTM;
import com.Khorn.PTMBukkit.Listeners.PTMBlockListener;
import com.Khorn.PTMBukkit.Listeners.PTMPlayerListener;
import com.Khorn.PTMBukkit.Listeners.PTMWorldListener;
import com.Khorn.PTMBukkit.Util.FileSystemManager;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class PTMPlugin extends JavaPlugin
{

    public final HashMap<String, WorldConfig> worldsSettings = new HashMap<String, WorldConfig>();
    private final PTMBlockListener blockListener = new PTMBlockListener(this);
    private final PTMWorldListener worldListener = new PTMWorldListener(this);
    private final PTMPlayerListener playerListener = new PTMPlayerListener(this);
    private final HashMap<String,PTMPlayer> sessions = new HashMap<String, PTMPlayer>();


    public void onDisable()
    {
        System.out.println(getDescription().getFullName() + " is now disabled");
    }

    public void onEnable()
    {
        BiomeManagerOld.GenBiomeDiagram();

        this.getCommand("ptm").setExecutor(new PTMCommandExecutor(this));
        this.RegisterEvents();
        CheckDefaultSettingsFolder();

        System.out.println(getDescription().getFullName() + " is now enabled");

    }

    public PTMPlayer GetPlayer(Player bukkitPlayer)
    {
        PTMPlayer player;
        synchronized (this.sessions)
        {
            if(this.sessions.containsKey(bukkitPlayer.getName()))
                return  this.sessions.get(bukkitPlayer.getName());
            player = new PTMPlayer(bukkitPlayer);
            this.sessions.put(bukkitPlayer.getName(),player);
        }
        return  player;

    }


    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id)
    {
        if (worldsSettings.containsKey(worldName))
        {
            System.out.println("PhoenixTerrainMod: enabled for '" + worldName + "'");
            return worldsSettings.get(worldName).ChunkProvider;
        }


        ChunkProviderPTM prov = new ChunkProviderPTM(this.GetSettings(worldName));


        System.out.println("PhoenixTerrainMod: enabled for '" + worldName + "'");
        return prov;


    }

    public WorldConfig GetSettings(String worldName)
    {
        File baseFolder = new File(this.getDataFolder(), "worlds/" + worldName);

        File oldFolder = new File(this.getDataFolder(), worldName);
        if (oldFolder.exists() && oldFolder.isDirectory())
        {
            try
            {
                FileSystemManager.CopyFileOrDirectory(oldFolder, baseFolder);
                System.out.println("PhoenixTerrainMod: config files copied to new folder");
                FileSystemManager.DeleteFileOrDirectory(oldFolder);
            } catch (IOException e)
            {
                System.out.println("PhoenixTerrainMod: error copying old directory, working with defaults");
            }


        }

        if (!baseFolder.exists())
        {
            try
            {
                File BOBDirectory = new File(baseFolder, PTMDefaultValues.WorldBOBDirectoryName.stringValue());
                File defaultBOBDirectory = new File(this.getDataFolder(), PTMDefaultValues.DefaultBOBDirectoryName.stringValue());
                FileSystemManager.CopyFileOrDirectory(defaultBOBDirectory, BOBDirectory);

                File settingsFile = new File(baseFolder, PTMDefaultValues.WorldSettingsName.stringValue());
                File defaultSettingsFile = new File(this.getDataFolder(), PTMDefaultValues.DefaultSettingsName.stringValue());
                FileSystemManager.CopyFileOrDirectory(defaultSettingsFile, settingsFile);

                System.out.println("PhoenixTerrainMod: config files copied from defaults");

            } catch (IOException e)
            {
                System.out.println("PhoenixTerrainMod: error copying old directory, working with defaults");
            }


        }


        WorldConfig worker = new WorldConfig(baseFolder, this,worldName);

        worldsSettings.put(worldName, worker);

        System.out.println("PhoenixTerrainMod: settings for '" + worldName + "' loaded");
        return worker;
    }


    public void WorldInit(World world)
    {
        if (this.worldsSettings.containsKey(world.getName()))
        {
            WorldConfig worldSetting = this.worldsSettings.get(world.getName());
            if (worldSetting.isInit)
                return;

            net.minecraft.server.World workWorld = ((CraftWorld) world).getHandle();

            workWorld.worldProvider.b = new BiomeManagerOld(workWorld, worldSetting);

            worldSetting.objectSpawner.Init(workWorld);
            worldSetting.ChunkProvider.Init(world);
            worldSetting.isInit = true;

            System.out.println("PhoenixTerrainMod: world seed is " + workWorld.getSeed());

        }
    }

    private void RegisterEvents()
        {
            PluginManager pm = this.getServer().getPluginManager();
            pm.registerEvent(Event.Type.BLOCK_PLACE, blockListener, Event.Priority.Monitor, this);

            pm.registerEvent(Event.Type.WORLD_INIT, worldListener, Event.Priority.High, this);

            pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Event.Priority.Normal, this);


        }

    private void CheckDefaultSettingsFolder()
    {
        /*
           /worlds
           /DefaultBOBPlugins
           /DefaultSettings.ini
         */
        if (!this.getDataFolder().exists())
            if (this.getDataFolder().mkdir())
                System.out.println("PhoenixTerrainMod: error create plugin directory");

        File temp = new File(this.getDataFolder(), "worlds");
        if (!temp.exists())
            if (temp.mkdir())
                System.out.println("PhoenixTerrainMod: error create worlds directory");

        temp = new File(this.getDataFolder(), PTMDefaultValues.DefaultBOBDirectoryName.stringValue());
        if (!temp.exists())
            if (temp.mkdir())
                System.out.println("PhoenixTerrainMod: error create DefaultBOBPlugins directory");
        temp = new File(this.getDataFolder(), PTMDefaultValues.DefaultSettingsName.stringValue());
        if (!temp.exists())
            (new WorldConfig()).CreateDefaultSettings(temp);

    }

}
//TODO Update chunk provider to new version
//TODO Split old object spawner to ObjectGens
//TODO Add new object gens
//TODO Add new biome generator
//TODO Add new general settings file
//TODO Add new biome settings file
//TODO Set block replace per biome
//
