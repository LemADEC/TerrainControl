package com.pg85.otg.generator;

import com.pg85.otg.LocalBiome;
import com.pg85.otg.LocalWorld;
import com.pg85.otg.OTG;
import com.pg85.otg.configuration.BiomeConfig;
import com.pg85.otg.configuration.ConfigFunction;
import com.pg85.otg.configuration.ConfigProvider;
import com.pg85.otg.configuration.WorldConfig;
import com.pg85.otg.generator.noise.NoiseGeneratorNewOctaves;
import com.pg85.otg.generator.resource.Resource;
import com.pg85.otg.logging.LogMarker;
import com.pg85.otg.util.ChunkCoordinate;

import java.util.Random;

public class ObjectSpawner
{

    private final ConfigProvider configProvider;
    private final Random rand;
    private final LocalWorld world;

    public ObjectSpawner(ConfigProvider configProvider, LocalWorld localWorld)
    {
        this.configProvider = configProvider;
        this.rand = new Random();
        this.world = localWorld;
        new NoiseGeneratorNewOctaves(new Random(world.getSeed()), 4);
    }

    public void populate(ChunkCoordinate chunkCoord)
    {
        // Get the corner block coords
        int x = chunkCoord.getChunkX() * 16;
        int z = chunkCoord.getChunkZ() * 16;

        // Get the biome of the other corner
        LocalBiome biome = world.getBiome(x + 15, z + 15);

        // Null check
        if (biome == null)
        {
            OTG.log(LogMarker.DEBUG, "Unknown biome at {},{}  (chunk {}). Population failed.", x + 15, z + 15, chunkCoord);
            return;
        }

        BiomeConfig biomeConfig = biome.getBiomeConfig();

        // Get the random generator
        WorldConfig worldConfig = configProvider.getWorldConfig();
        long resourcesSeed = worldConfig.resourcesSeed != 0L ? worldConfig.resourcesSeed : world.getSeed();
        this.rand.setSeed(resourcesSeed);
        long l1 = this.rand.nextLong() / 2L * 2L + 1L;
        long l2 = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed(chunkCoord.getChunkX() * l1 + chunkCoord.getChunkZ() * l2 ^ resourcesSeed);

        // Generate structures
        boolean hasVillage = world.placeDefaultStructures(rand, chunkCoord);

        // Mark population started
        world.startPopulation(chunkCoord);
        OTG.firePopulationStartEvent(world, rand, hasVillage,
                                     chunkCoord);

        // Resource sequence
        for (ConfigFunction<BiomeConfig> res : biomeConfig.resourceSequence)
        {
            if (res instanceof Resource)
                ((Resource) res).process(world, rand, hasVillage, chunkCoord);
        }

        // Animals
        world.placePopulationMobs(biome, rand, chunkCoord);

        // Snow and ice
        new FrozenSurfaceHelper(world).freezeChunk(chunkCoord);

        // Replace blocks
        world.replaceBlocks(chunkCoord);

        // Mark population ended
        OTG.firePopulationEndEvent(world, rand, hasVillage, chunkCoord);
        world.endPopulation();
    }

}