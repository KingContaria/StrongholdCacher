package com.kingcontaria.strongholdcacher.mixin;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ChunkGenerator.class)
public class ChunkGeneratorMixin {
    private static List<ChunkPos> strongholdCache;
    private static long cachedSeed;

    @Shadow @Final private long worldSeed;
    @Mutable
    @Shadow @Final private List<ChunkPos> strongholds;

    @Inject(method = "generateStrongholdPositions", at = @At("HEAD"), cancellable = true)
    private void applyCachedStrongholds(CallbackInfo ci) {
        if (strongholdCache != null && this.worldSeed == cachedSeed && this.strongholds.isEmpty()) {
            this.strongholds = strongholdCache;
            ci.cancel();
        }
    }

    // this is just a proof of concept and obviously still needs more checks not just the seed
    @Inject(method = "generateStrongholdPositions", at = @At("TAIL"))
    private void cacheStrongholds(CallbackInfo ci) {
        if (cachedSeed != this.worldSeed) {
            strongholdCache = this.strongholds;
            cachedSeed = this.worldSeed;
        }
    }

}
