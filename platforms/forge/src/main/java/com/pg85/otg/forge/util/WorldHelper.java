package com.pg85.otg.forge.util;

import com.pg85.otg.LocalWorld;
import com.pg85.otg.forge.asm.mixin.iface.IMixinWorld;

import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public abstract class WorldHelper
{
    /**
     * Returns the LocalWorld of the Minecraft world. Returns null if there is
     * no world.
     *
     * @param world The Minecraft world.
     * @return The LocalWorld, or null if there is none.
     */
    public static LocalWorld toLocalWorld(World world)
    {
        return ((IMixinWorld) world).getTCWorld();
    }

    public static String getWorldName(World world) {
        final WorldServer defaultWorld = DimensionManager.getWorld(0);
        final int dimension = world.provider.getDimension();
        if (dimension == -1) {
            return "DIM-1";
        } else if (dimension == 1) {
            return "DIM1";
        // Support mods who use have worlds use unique save handlers (ex. SpongeForge)
        } else if (dimension == 0 || (defaultWorld != null && defaultWorld.getSaveHandler() != world.getSaveHandler())) {
            return world.getWorldInfo().getWorldName();
        } else {
            return world.provider.getSaveFolder();
        }
    }

    private WorldHelper()
    {
    }

}
