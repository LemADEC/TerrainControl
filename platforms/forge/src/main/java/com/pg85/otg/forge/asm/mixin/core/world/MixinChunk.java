package com.pg85.otg.forge.asm.mixin.core.world;

import com.pg85.otg.LocalWorld;
import com.pg85.otg.forge.ForgeBiome;
import com.pg85.otg.forge.asm.mixin.iface.IMixinWorld;
import com.pg85.otg.forge.generator.OTGBiome;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.Chunk;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Chunk.class)
public abstract class MixinChunk {

    @Shadow @Final private World world;

    @Inject(
            method = "getBiome",
            at = @At(
                    value = "RETURN"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true
    )
    private void onGetBiome(final BlockPos pos, final BiomeProvider provider, final CallbackInfoReturnable<Biome> cir, final int x, final int y,
            final int z, final Biome biome) {
        if (biome instanceof OTGBiome) {
            return;
        }

        final LocalWorld tcWorld = ((IMixinWorld) this.world).getTCWorld();
        if (tcWorld == null) {
            return;
        }

        final ForgeBiome tcBiome = (ForgeBiome) tcWorld.getBiome(pos.getX(), pos.getZ());
        cir.setReturnValue(tcBiome.getHandle());
    }
}
