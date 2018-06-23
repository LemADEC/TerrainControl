package com.pg85.otg.forge.asm.mixin.core.world;

import com.pg85.otg.LocalWorld;
import com.pg85.otg.forge.asm.mixin.iface.IMixinWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(World.class)
public abstract class MixinWorld implements IMixinWorld {

    private LocalWorld tcWorld;

    @Override
    public void setTCWorld(LocalWorld world) {
        this.tcWorld = world;
    }

    @Override
    public LocalWorld getTCWorld() {
        return this.tcWorld;
    }
}
