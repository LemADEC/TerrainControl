package com.khorn.terraincontrol.forge.asm.mixin.core.world;

import com.khorn.terraincontrol.LocalWorld;
import com.khorn.terraincontrol.TerrainControl;
import com.khorn.terraincontrol.forge.util.WorldHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Chunk.class)
public abstract class MixinChunk {

    @Shadow private World world;

    private LocalWorld tcWorld;

    @Redirect(method = "getBiome", at = @At(target = "Lnet/minecraft/world/biome/Biome;getBiome(I)Lnet/minecraft/world/biome/Biome;", value =
            "INVOKE"))
    public Biome onGetBiome(int biomeId, BlockPos pos, BiomeProvider provider) {
        // TODO Need to cache this wrap
        if (this.tcWorld == null) {
            this.tcWorld = TerrainControl.getEngine().getWorld(this.world.getWorldInfo().getWorldName());
            //this.hasLookedUpTCWorld = true;
        }

        if (this.tcWorld != null) {
            return WorldHelper.getBiome(tcWorld, pos);
        }

        return Biome.getBiome(biomeId);
    }
}