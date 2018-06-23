package com.pg85.otg.bukkit.generator;

import com.pg85.otg.bukkit.BukkitWorld;
import com.pg85.otg.bukkit.OTGPlugin;
import com.pg85.otg.configuration.WorldConfig;
import com.pg85.otg.generator.ChunkProviderOTG;
import com.pg85.otg.util.ChunkCoordinate;
import com.pg85.otg.configuration.WorldConfig.TerrainMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OTGChunkGenerator extends ChunkGenerator
{
    private ChunkProviderOTG chunkProviderOTG;
    private ArrayList<BlockPopulator> BlockPopulator = new ArrayList<BlockPopulator>();
    private boolean NotGenerate = false;
    private OTGPlugin plugin;

    public OTGChunkGenerator(OTGPlugin _plugin)
    {
        this.plugin = _plugin;
    }

    /**
     * Initializes the world if it hasn't already been initialized.
     * 
     * @param world
     *            The world of this generator.
     */
    private void makeSureWorldIsInitialized(World world)
    {
        if (this.chunkProviderOTG == null)
        {
            // Not yet initialized, do it now
            this.plugin.onWorldInit(world);
        }
    }

    /**
     * Called whenever a BukkitWorld instance becomes available.
     * 
     * @param _world
     *            The BukkitWorld instance.
     */
    public void onInitialize(BukkitWorld _world)
    {
        this.chunkProviderOTG = new ChunkProviderOTG(_world.getConfigs(), _world);

        TerrainMode mode = _world.getConfigs().getWorldConfig().ModeTerrain;

        if (mode == WorldConfig.TerrainMode.Normal || mode == WorldConfig.TerrainMode.OldGenerator)
            this.BlockPopulator.add(new OTGBlockPopulator(_world));

        if (mode == WorldConfig.TerrainMode.NotGenerate)
            this.NotGenerate = true;
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world)
    {
        makeSureWorldIsInitialized(world);

        return this.BlockPopulator;
    }

    @Override
    public boolean canSpawn(World world, int x, int z)
    {
        makeSureWorldIsInitialized(world);

        Material material = world.getHighestBlockAt(x, z).getType();
        return material.isSolid();
    }

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome)
    {
        makeSureWorldIsInitialized(world);

        ChunkData chunkData = createChunkData(world);

        if (this.NotGenerate)
            return chunkData;

        ChunkCoordinate chunkCoord = ChunkCoordinate.fromChunkCoords(chunkX, chunkZ);
        BukkitChunkBuffer chunkBuffer = new BukkitChunkBuffer(chunkCoord, chunkData);
        this.chunkProviderOTG.generate(chunkBuffer);

        return chunkData;
    }

}