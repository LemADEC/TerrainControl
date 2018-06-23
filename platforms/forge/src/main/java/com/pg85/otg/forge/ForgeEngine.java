package com.pg85.otg.forge;

import com.pg85.otg.LocalMaterialData;
import com.pg85.otg.LocalWorld;
import com.pg85.otg.OTGEngine;
import com.pg85.otg.configuration.standard.PluginStandardValues;
import com.pg85.otg.exception.InvalidConfigException;
import com.pg85.otg.forge.asm.mixin.iface.IMixinForgeRegistry;
import com.pg85.otg.forge.asm.mixin.iface.IMixinNamespacedWrapper;
import com.pg85.otg.forge.util.NBTHelper;
import com.pg85.otg.util.NamedBinaryTag;
import com.pg85.otg.util.minecraftTypes.DefaultMaterial;

import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

import java.io.File;
import java.util.Map;

public class ForgeEngine extends OTGEngine
{

    private WorldLoader worldLoader;

    @SuppressWarnings("unchecked")
    ForgeEngine(WorldLoader worldLoader)
    {
        super(new ForgeLogger());
        this.worldLoader = worldLoader;
    }

    // Used to bypass Forge's API in order to properly register a virtual biome
    // that would otherwise be blocked by Forge due to virtual biome ID's
    // surpassing 255.
    public void registerForgeBiome(int id, ResourceLocation resourceLocation, Biome biome)
    {
        final IMixinForgeRegistry<Biome> registry = ((IMixinNamespacedWrapper) Biome.REGISTRY).getDelegate();
        final Map<Integer, Biome> ids = registry.getIds();
        final Map<ResourceLocation, Biome> names = registry.getNames();

        ids.put(id, biome);
        names.put(resourceLocation, biome);
        registry.getAvailabilityMap().set(id);
    }

    @Override
    public LocalWorld getWorld(String name)
    {
        return this.worldLoader.getWorld(name);
    }

    @Override
    public File getTCDataFolder()
    {
        return this.worldLoader.getConfigsFolder();
    }

    @Override
    public File getGlobalObjectsDirectory()
    {
        return new File(this.getTCDataFolder(), PluginStandardValues.BO_DirectoryName);
    }

    @Override
    public LocalMaterialData readMaterial(String input) throws InvalidConfigException
    {
        return ForgeMaterialData.ofString(input);
    }

    @Override
    public LocalMaterialData toLocalMaterialData(DefaultMaterial defaultMaterial, int blockData)
    {
        return ForgeMaterialData.ofDefaultMaterial(defaultMaterial, blockData);
    }

    @Override
    public NamedBinaryTag toNamedBinaryTag(String mojangson) throws InvalidConfigException
    {
        try
        {
            return NBTHelper.getNBTFromNMSTagCompound("", JsonToNBT.getTagFromJson(mojangson));
        } catch (NBTException e)
        {
            throw new InvalidConfigException("Error parsing NBT data \n" + mojangson + "\n" + e.getLocalizedMessage());
        }
    }
}
