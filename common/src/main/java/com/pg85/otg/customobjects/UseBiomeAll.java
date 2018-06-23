package com.pg85.otg.customobjects;

import com.pg85.otg.LocalWorld;
import com.pg85.otg.util.ChunkCoordinate;

import java.util.Random;

public class UseBiomeAll extends UseBiome
{
    @Override
    public String getName()
    {
        return "UseBiomeAll";
    }

    @Override
    public boolean process(LocalWorld world, Random random, ChunkCoordinate chunkCoord)
    {
        boolean spawnedAtLeastOneObject = false;

        for (CustomObject object : getPossibleObjectsAt(world, chunkCoord.getBlockXCenter(), chunkCoord.getBlockZCenter()))
        {
            if (object.process(world, random, chunkCoord))
            {
                spawnedAtLeastOneObject = true;
            }
        }

        return spawnedAtLeastOneObject;
    }
}
