package com.pg85.otg.customobjects;

import com.pg85.otg.LocalWorld;
import com.pg85.otg.util.ChunkCoordinate;

import java.util.Random;

public class UseWorldAll extends UseWorld
{
    @Override
    public String getName()
    {
        return "UseWorldAll";
    }

    @Override
    public boolean process(LocalWorld world, Random rand, ChunkCoordinate chunkCoord)
    {
        boolean spawnedAtLeastOneObject = false;

        for (CustomObject selectedObject : world.getConfigs().getCustomObjects())
        {
            if (!selectedObject.hasPreferenceToSpawnIn(world.getBiome(chunkCoord.getBlockXCenter(), chunkCoord.getBlockZCenter())))
                continue;

            // Process the object
            if (selectedObject.process(world, rand, chunkCoord))
            {
                spawnedAtLeastOneObject = true;
            }
        }
        return spawnedAtLeastOneObject;
    }
}
