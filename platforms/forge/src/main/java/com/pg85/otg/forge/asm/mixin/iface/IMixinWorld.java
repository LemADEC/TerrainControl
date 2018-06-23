package com.pg85.otg.forge.asm.mixin.iface;

import com.pg85.otg.LocalWorld;

public interface IMixinWorld {

    void setTCWorld(LocalWorld world);

    LocalWorld getTCWorld();
}
