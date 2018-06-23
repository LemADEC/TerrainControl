package com.pg85.otg.forge;

import com.pg85.otg.OTG;
import com.pg85.otg.events.EventPriority;
import com.pg85.otg.forge.events.*;
import com.pg85.otg.forge.generator.ForgeVanillaBiomeGenerator;
import com.pg85.otg.forge.generator.structure.OTGRareBuildingStart;
import com.pg85.otg.forge.generator.structure.OTGVillageStart;
import com.pg85.otg.generator.biome.VanillaBiomeGenerator;
import com.pg85.otg.util.minecraftTypes.StructureNames;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.io.File;

@Mod(modid = "terraincontrol", name = "OTG", acceptableRemoteVersions = "*")
public class OTGPlugin
{
    @Mod.Instance
    public static OTGPlugin instance;

    public OTGWorldType worldType;
    public WorldLoader worldLoader;

    @EventHandler
    public void load(FMLInitializationEvent event)
    {
        // This is the place where the mod starts loading
        File configsDir = new File(Loader.instance().getConfigDir(), "OTG");
        this.worldLoader = new WorldLoader(configsDir);

        // Create the world type. WorldType registers itself in the constructor
        // - that is Mojang code, so don't blame me
        this.worldType = new OTGWorldType(this.worldLoader);

        // Start OTG engine
        final ForgeEngine engine = new ForgeEngine(this.worldLoader);
        OTG.setEngine(engine);

        // Register Default biome generator to OTG
        engine.getBiomeModeManager().register(VanillaBiomeGenerator.GENERATOR_NAME, ForgeVanillaBiomeGenerator.class);

        // Register village and rare building starts
        MapGenStructureIO.registerStructure(OTGRareBuildingStart.class, StructureNames.RARE_BUILDING);
        MapGenStructureIO.registerStructure(OTGVillageStart.class, StructureNames.VILLAGE);

        // Register sapling tracker, for custom tree growth.
        SaplingListener saplingListener = new SaplingListener(this.worldLoader);
        MinecraftForge.TERRAIN_GEN_BUS.register(saplingListener);
        MinecraftForge.EVENT_BUS.register(this.worldLoader);
        MinecraftForge.EVENT_BUS.register(saplingListener);

        // Register to our own events, so that they can be fired again as Forge events.
        engine.registerEventHandler(new OTGToForgeEventConverter(), EventPriority.CANCELABLE);
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new OTGCommandHandler());
    }
}