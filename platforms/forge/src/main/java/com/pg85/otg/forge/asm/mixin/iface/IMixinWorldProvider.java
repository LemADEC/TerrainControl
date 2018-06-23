package com.pg85.otg.forge.asm.mixin.iface;

import net.minecraft.world.biome.BiomeProvider;

public interface IMixinWorldProvider {

    void setBiomeProvider(BiomeProvider provider);
}
