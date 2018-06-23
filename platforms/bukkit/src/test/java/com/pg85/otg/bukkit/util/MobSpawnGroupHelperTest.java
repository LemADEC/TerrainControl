package com.pg85.otg.bukkit.util;

import static com.pg85.otg.bukkit.util.MobSpawnGroupHelper.fromMinecraftList;
import static com.pg85.otg.bukkit.util.MobSpawnGroupHelper.toMinecraftClass;
import static com.pg85.otg.bukkit.util.MobSpawnGroupHelper.toMinecraftlist;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.pg85.otg.util.minecraftTypes.MobNames;
import net.minecraft.server.v1_12_R1.DispenserRegistry;

import com.pg85.otg.configuration.WeightedMobSpawnGroup;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class MobSpawnGroupHelperTest
{

    @BeforeClass
    public static void initMinecraft()
    {
        DispenserRegistry.c(); // initializes Minecraft. In Forge, this method is called Bootstrap.init()
    }

    /**
     * Tests whether Minecraft has a class for all our MobNames.
     */
    @Test
    public void testMobNamesExistInMinecraft()
    {
        for (MobNames name : MobNames.values())
        {
            assertTrue("Expected Minecraft class for " + name, MobSpawnGroupHelper.toMinecraftClass(name.getInternalName()) != null);
        }
    }

    /**
     * Verifies that the converter methods are working fine.
     */
    @Test
    public void testRoundtrip()
    {
        List<WeightedMobSpawnGroup> groups = Arrays.asList(
                new WeightedMobSpawnGroup("minecraft:cow", 10, 2, 5),
                new WeightedMobSpawnGroup("minecraft:chicken", 8, 3, 6));
        Assert.assertEquals(groups, MobSpawnGroupHelper.fromMinecraftList(MobSpawnGroupHelper.toMinecraftlist(groups)));

    }
}
