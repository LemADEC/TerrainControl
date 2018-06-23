package com.pg85.otg.forge;

import com.pg85.otg.BiomeIds;
import com.pg85.otg.LocalBiome;
import com.pg85.otg.configuration.BiomeConfig;
import com.pg85.otg.forge.generator.OTGBiome;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

public class ForgeBiome implements LocalBiome
{
    private final Biome wrapped;
    private final boolean isCustom;

    private final BiomeIds biomeIds;
    private final BiomeConfig biomeConfig;

    ForgeBiome(Biome biome, BiomeConfig biomeConfig, BiomeIds biomeIds)
    {
        this.wrapped = biome;
        this.biomeIds = biomeIds;
        this.biomeConfig = biomeConfig;
        this.isCustom = biome instanceof OTGBiome;
    }

    @Override
    public boolean isCustom()
    {
        return this.isCustom;
    }

    @Override
    public String getName()
    {
        return this.wrapped.biomeName;
    }

    public Biome getHandle()
    {
        return this.wrapped;
    }

    @Override
    public BiomeIds getIds()
    {
        return this.biomeIds;
    }

    @Override
    public float getTemperatureAt(int x, int y, int z)
    {
        return this.wrapped.getTemperature(new BlockPos(x, y, z));
    }

    @Override
    public BiomeConfig getBiomeConfig()
    {
        return this.biomeConfig;
    }

    @Override
    public String toString()
    {
        return getName() + "[" + getIds() + "]";
    }
}
