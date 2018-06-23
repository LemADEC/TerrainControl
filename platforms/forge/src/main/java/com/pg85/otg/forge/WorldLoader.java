package com.pg85.otg.forge;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.pg85.otg.OTG;
import com.pg85.otg.configuration.ServerConfigProvider;
import com.pg85.otg.forge.asm.mixin.iface.IMixinWorld;
import com.pg85.otg.forge.asm.mixin.iface.IMixinWorldProvider;
import com.pg85.otg.forge.util.WorldHelper;
import com.pg85.otg.logging.LogMarker;

import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


/**
 * Responsible for loading and unloading the world.
 */
public final class WorldLoader
{
    private final File configsDir;
    private final Map<String, ServerConfigProvider> configMap = Maps.newHashMap();
    private final HashMap<String, ForgeWorld> worlds = new HashMap<String, ForgeWorld>();

    WorldLoader(File configsDir)
    {
        this.configsDir = Preconditions.checkNotNull(configsDir, "configsDir");
    }

    public ForgeWorld getWorld(String name)
    {
        return this.worlds.get(name);
    }

    public File getConfigsFolder()
    {
        return this.configsDir;
    }

    private File getWorldDir(String worldName)
    {
        return new File(this.configsDir, "worlds/" + worldName);
    }

    public void initializeTCWorld(World world) {
        final String worldName = WorldHelper.getWorldName(world);

        OTG.log(LogMarker.INFO, "Checking if we have configs for \"{}\"..", worldName);
        final File worldConfigsFolder = this.getWorldDir(worldName);
        if (!worldConfigsFolder.exists()) {
            OTG.log(LogMarker.INFO, "No configs found for \"{}\".", worldName);
            return;
        }

        final ForgeWorld tcWorld = new ForgeWorld(worldName);
        ServerConfigProvider config = this.configMap.get(worldName);
        if (config == null) {
            OTG.log(LogMarker.INFO, "Loading configs for world \"{}\"..", tcWorld.getName());
        }

        this.worlds.put(worldName, tcWorld);
        config = new ServerConfigProvider(worldConfigsFolder, tcWorld);
        tcWorld.provideConfigs(config);
        this.configMap.put(worldName, config);

        ((IMixinWorld) world).setTCWorld(tcWorld);

        tcWorld.provideWorldInstance((WorldServer) world);

        ((IMixinWorldProvider) world.provider).setBiomeProvider(OTGPlugin.instance.worldType.getBiomeProvider(world));
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if (FMLCommonHandler.instance().getSide().isClient()) {
            return;
        }

        final String worldName = WorldHelper.getWorldName(event.getWorld());
        this.configMap.remove(worldName);
        this.worlds.remove(worldName);
    }
}
