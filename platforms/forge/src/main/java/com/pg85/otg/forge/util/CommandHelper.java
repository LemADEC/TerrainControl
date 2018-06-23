package com.pg85.otg.forge.util;

import com.pg85.otg.LocalWorld;
import com.pg85.otg.OTG;

import net.minecraft.command.ICommandSender;
import net.minecraft.world.World;

public class CommandHelper
{

    public static LocalWorld getWorld(ICommandSender sender, String worldName)
    {
        if (worldName.isEmpty())
        {
            World mcWorld = ((ICommandSender) sender).getEntityWorld();
            if (mcWorld != null)
            {
                LocalWorld world = WorldHelper.toLocalWorld(mcWorld);
                if (world != null)
                {
                    return world;
                }
            }
        }

        return OTG.getWorld(worldName);
    }

    public static boolean containsArgument(String[] args, String arg)
    {
        for (String str : args)
        {
            if (str.equalsIgnoreCase(arg))
            {
                return true;
            }
        }

        return false;
    }
}
