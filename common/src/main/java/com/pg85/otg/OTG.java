package com.pg85.otg;

import com.pg85.otg.configuration.ConfigFunctionsManager;
import com.pg85.otg.configuration.PluginConfig;
import com.pg85.otg.customobjects.CustomObject;
import com.pg85.otg.customobjects.CustomObjectManager;
import com.pg85.otg.events.EventHandler;
import com.pg85.otg.events.EventPriority;
import com.pg85.otg.exception.InvalidConfigException;
import com.pg85.otg.generator.biome.BiomeModeManager;
import com.pg85.otg.generator.resource.Resource;
import com.pg85.otg.logging.LogMarker;
import com.pg85.otg.util.ChunkCoordinate;
import com.pg85.otg.util.minecraftTypes.DefaultMaterial;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Random;

public class OTG
{

    /**
     * The engine that powers Open Terrain Generator.
     */
    private static OTGEngine engine;

    /**
     * The amount of different block ids that are supported. 4096 on Minecraft.
     */
    public static final int SUPPORTED_BLOCK_IDS = 4096;

    /**
     * The world depth that the engine supports. Not the actual depth the
     * world is capped at. 0 in Minecraft.
     */
    public static final int WORLD_DEPTH = 0;

    /**
     * The world height that the engine supports. Not the actual height the
     * world is capped at. 256 in Minecraft.
     */
    public static final int WORLD_HEIGHT = 256;

    public static final int MAX_BIOME_ID = 16384;
    /**
     * @see OTGEngine#fireCanCustomObjectSpawnEvent(CustomObject,
     * LocalWorld, int, int, int)
     */
    public static boolean fireCanCustomObjectSpawnEvent(CustomObject object, LocalWorld world, int x, int y, int z)
    {
        return engine.fireCanCustomObjectSpawnEvent(object, world, x, y, z);
    }

    /**
     * @see OTGEngine#firePopulationEndEvent(LocalWorld, Random,
     * boolean, ChunkCoordinate)
     */
    public static void firePopulationEndEvent(LocalWorld world, Random random, boolean villageInChunk, ChunkCoordinate chunkCoord)
    {
        engine.firePopulationEndEvent(world, random, villageInChunk, chunkCoord);
    }

    /**
     * @see OTGEngine#firePopulationStartEvent(LocalWorld, Random,
     * boolean, ChunkCoordinate)
     */
    public static void firePopulationStartEvent(LocalWorld world, Random random, boolean villageInChunk, ChunkCoordinate chunkCoord)
    {
        engine.firePopulationStartEvent(world, random, villageInChunk, chunkCoord);
    }

    /**
     * @see OTGEngine#fireResourceProcessEvent(Resource,
     * LocalWorld, Random, boolean, int, int)
     */
    public static boolean fireResourceProcessEvent(Resource resource, LocalWorld world, Random random, boolean villageInChunk, int chunkX,
                                                   int chunkZ)
    {
        return engine.fireResourceProcessEvent(resource, world, random, villageInChunk, chunkX, chunkZ);
    }

    /**
     * Returns the biome managers. Register your own biome manager here.
     * <p>
     * @return The biome managers.
     */
    public static BiomeModeManager getBiomeModeManager()
    {
        return engine.getBiomeModeManager();
    }

    /**
     * Convienence method to quickly get the biome name at the given
     * coordinates. Will return null if the world isn't loaded by Terrain
     * Control.
     * <p>
     * @param worldName The world name.
     * @param x         The block x in the world.
     * @param z         The block z in the world.
     * @return The biome name, or null if the world isn't managed by Terrain
     *         Control.
     */
    public static String getBiomeName(String worldName, int x, int z)
    {
        LocalWorld world = getWorld(worldName);
        if (world == null)
        {
            // World isn't loaded by Open Terrain Generator
            return null;
        }
        return world.getSavedBiome(x, z).getName();
    }

    /**
     * Returns the Resource manager.
     * <p>
     * @return The Resource manager.
     */
    public static ConfigFunctionsManager getConfigFunctionsManager()
    {
        return engine.getConfigFunctionsManager();
    }

    /**
     * Returns the CustomObject manager, with hooks to spawn CustomObjects.
     * <p>
     * @return The CustomObject manager.
     */
    public static CustomObjectManager getCustomObjectManager()
    {
        return engine.getCustomObjectManager();
    }

    /**
     * Returns the engine, containing the API methods.
     * <p>
     * @return The engine
     */
    public static OTGEngine getEngine()
    {
        return engine;
    }

    /**
     * @see OTGEngine#readMaterial(String)
     */
    public static LocalMaterialData readMaterial(String name) throws InvalidConfigException
    {
        return engine.readMaterial(name);
    }

    /**
     * @see OTGEngine#toLocalMaterialData(DefaultMaterial, int)
     */
    public static LocalMaterialData toLocalMaterialData(DefaultMaterial defaultMaterial, int blockData)
    {
        return engine.toLocalMaterialData(defaultMaterial, blockData);
    }

    /**
     * Returns the global config file.
     * <p>
     * @return The global config file.
     */
    public static PluginConfig getPluginConfig()
    {
        return engine.getPluginConfig();
    }

    /**
     * Returns the world object with the given name.
     * <p>
     * @param name The name of the world.
     * @return The world object.
     */
    public static LocalWorld getWorld(String name)
    {
        return engine.getWorld(name);
    }

    /**
     * Logs the messages with the given importance. Message will be
     * prefixed with [OpenTerrainGenerator], so don't do that yourself.
     * <p>
     * @param messages The messages to log.
     * @param level    The severity of the message
     */
    public static void log(LogMarker level, List<String> messages)
    {
        engine.getLogger().log(level, messages);
    }

    /**
     * Logs a format string message with the given importance. Message will
     * be prefixed with [OpenTerrainGenerator], so don't do that yourself.
     * <p>
     * @param message The messages to log formatted similar to Logger.log()
     *                with the same args.
     * @param level   The severity of the message
     * @param params  The parameters belonging to {0...} in the message
     *                string
     */
    public static void log(LogMarker level, String message, Object... params)
    {
        engine.getLogger().log(level, message, params);
    }

    /**
     * Logs the message(s) with the given importance <b>ONLY IF</b> logger
     * level matches the level provided. Message will be prefixed with
     * [OpenTerrainGenerator], so don't do that yourself.
     * <p>
     * @param ifLevel  the Log level to test for
     * @param messages The messages to log.
     */
    public static void logIfLevel(LogMarker ifLevel, List<String> messages)
    {
        engine.getLogger().logIfLevel(ifLevel, messages);
    }

    /**
     * Logs the message(s) with the given importance <b>ONLY IF</b> logger
     * level matches the level provided. Message will be prefixed with
     * [OpenTerrainGenerator], so don't do that yourself.
     * <p>
     * @param ifLevel the Log level to test for
     * @param message The messages to log formatted similar to
     *                Logger.log() with the same args.
     * @param params  The parameters belonging to {0...} in the message
     *                string
     */
    public static void logIfLevel(LogMarker ifLevel, String message, Object... params)
    {
        engine.getLogger().logIfLevel(ifLevel, message, params);
    }

    /**
     * Logs the message(s) with the given importance <b>ONLY IF</b> logger
     * level is between the min/max provided. Message will be prefixed with
     * [OpenTerrainGenerator], so don't do that yourself.
     * <p>
     * @param min      The minimum Log level to test for
     * @param max      The maximum Log level to test for
     * @param messages The messages to log.
     */
    public static void logIfLevel(LogMarker min, LogMarker max, List<String> messages)
    {
        engine.getLogger().logIfLevel(min, max, messages);
    }

    /**
     * Logs the message(s) with the given importance <b>ONLY IF</b> logger
     * level is between the min/max provided. Message will be prefixed with
     * [OpenTerrainGenerator], so don't do that yourself.
     * <p>
     * @param min     The minimum Log level to test for
     * @param max     The maximum Log level to test for
     * @param message The messages to log formatted similar to
     *                Logger.log() with the same args.
     * @param params  The parameters belonging to {0...} in the message
     *                string
     */
    public static void logIfLevel(LogMarker min, LogMarker max, String message, Object... params)
    {
        engine.getLogger().logIfLevel(min, max, message, params);
    }

    /**
     * Prints the stackTrace of the provided Throwable object
     * <p>
     * @param level The log level to log this stack trace at
     * @param e     The Throwable object to obtain stack trace information from
     */
    public static void printStackTrace(LogMarker level, Throwable e)
    {
        printStackTrace(level, e, Integer.MAX_VALUE);
    }

    /**
     * Prints the stackTrace of the provided Throwable object to a certain
     * depth
     * <p>
     * @param level    The log level to log this stack trace at
     * @param e        The Throwable object to obtain stack trace information
     *                 from
     * @param maxDepth The max number of trace elements to print
     */
    public static void printStackTrace(LogMarker level, Throwable e, int maxDepth)
    {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        engine.getLogger().log(level, stringWriter.toString());
    }

    /**
     * @see OTGEngine#registerEventHandler(EventHandler)
     */
    public static void registerEventHandler(EventHandler handler)
    {
        engine.registerEventHandler(handler);
    }

    /**
     * @see OTGEngine#registerEventHandler(EventHandler,
     * EventPriority)
     */
    public static void registerEventHandler(EventHandler handler, EventPriority priority)
    {
        engine.registerEventHandler(handler, priority);
    }

    /**
     * Sets the engine and calls its {@link OTGEngine#onStart()
     * onStart()} method.
     * <p>
     * @param engine The engine.
     */
    public static void setEngine(OTGEngine engine)
    {
        if (OTG.engine != null)
        {
            throw new IllegalStateException("Engine is already set.");
        }
    
        OTG.engine = engine;
        engine.onStart();
    }

    /**
     * Nulls out static references to free up memory. Should be called on
     * shutdown. Engine can be restarted after this.
     */
    public static void stopEngine()
    {
        engine.onShutdown();
        engine = null;
    }

    private OTG()
    {
        // Forbidden to instantiate.
    }

}
