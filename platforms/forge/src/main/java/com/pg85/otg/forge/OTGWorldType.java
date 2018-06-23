package com.pg85.otg.forge;

import com.google.common.base.Preconditions;
import com.pg85.otg.LocalWorld;
import com.pg85.otg.OTG;
import com.pg85.otg.configuration.WorldConfig;
import com.pg85.otg.configuration.standard.PluginStandardValues;
import com.pg85.otg.forge.generator.ForgeVanillaBiomeGenerator;
import com.pg85.otg.forge.generator.OTGBiomeProvider;
import com.pg85.otg.forge.util.WorldHelper;
import com.pg85.otg.generator.biome.BiomeGenerator;
import com.pg85.otg.util.helpers.ReflectionHelper;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OTGWorldType extends WorldType
{

    private final WorldLoader worldLoader;

    public OTGWorldType(WorldLoader worldLoader)
    {
        super(PluginStandardValues.PLUGIN_NAME);
        this.worldLoader = Preconditions.checkNotNull(worldLoader, "worldLoader");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasInfoNotice()
    {
        return true;
    }

    @Override
    public BiomeProvider getBiomeProvider(World world)
    {
        // Ignore client worlds
        if (world.isRemote)
        {
            return super.getBiomeProvider(world);
        }

        final ForgeWorld tcWorld = (ForgeWorld) WorldHelper.toLocalWorld(world);
        if (tcWorld == null)
        {
            // Not a OTG world
            return super.getBiomeProvider(world);
        }

        Class<? extends BiomeGenerator> biomeGenClass = tcWorld.getConfigs().getWorldConfig().biomeMode;
        BiomeGenerator biomeGenerator = OTG.getBiomeModeManager().createCached(biomeGenClass, tcWorld);
        BiomeProvider biomeProvider = this.createBiomeProvider(tcWorld, biomeGenerator);
        tcWorld.setBiomeGenerator(biomeGenerator);
        return biomeProvider;
    }

    /**
     * Gets the appropriate BiomeGenerator. For the vanilla biome generator we
     * have to use BiomeGenerator, for other biome modes TCBiomeProvider is
     * the right option.
     * 
     * @param tcWorld ForgeWorld instance, needed to instantiate the
     *            BiomeGenerator.
     * @param biomeGenerator Biome generator.
     * @return The most appropriate BiomeGenerator.
     */
    private BiomeProvider createBiomeProvider(ForgeWorld tcWorld, BiomeGenerator biomeGenerator)
    {
        final World world = tcWorld.getWorld();
        BiomeProvider biomeProvider;
        if (biomeGenerator instanceof ForgeVanillaBiomeGenerator)
        {
            biomeProvider = world.provider.getBiomeProvider();
            // Let our biome generator depend on Minecraft's
            ((ForgeVanillaBiomeGenerator) biomeGenerator).setBiomeProvider(biomeProvider);
        } else
        {
            biomeProvider = new OTGBiomeProvider(tcWorld, biomeGenerator);
            // Let Minecraft's biome generator depend on ours
            ReflectionHelper.setValueInFieldOfType(world.provider, BiomeProvider.class, biomeProvider);
        }

        return biomeProvider;
    }

    @Override
    public IChunkGenerator getChunkGenerator(World world, String generatorOptions)
    {
        final ForgeWorld tcWorld = (ForgeWorld) WorldHelper.toLocalWorld(world);
        if (tcWorld != null && tcWorld.getConfigs().getWorldConfig().ModeTerrain != WorldConfig.TerrainMode.Default)
        {
            return tcWorld.getChunkGenerator();
        } else
            return super.getChunkGenerator(world, generatorOptions);
    }

    @Override
    public int getMinimumSpawnHeight(World world)
    {
        final LocalWorld tcWorld = WorldHelper.toLocalWorld(world);
        if (tcWorld == null)
        {
            // MCPC+ has an interesting load order sometimes
            return 64;
        }
        return tcWorld.getConfigs().getWorldConfig().waterLevelMax;
    }
}
