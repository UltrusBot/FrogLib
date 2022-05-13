package me.ultrusmods.froglib.mixin;

import me.ultrusmods.froglib.FrogLib;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.entity.passive.FrogVariant;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(FrogEntity.class)
public abstract class FrogEntityMixin extends AnimalEntity {
    @Shadow public abstract void setVariant(FrogVariant variant);

    protected FrogEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * @author UltrusBot
     * @reason Makes it so custom frog variants can be applied on frog initialization.
     */
    @Inject(method = "initialize", at = @At("RETURN"))
    public void addNewTypesInit(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cir) {
        RegistryEntry<Biome> registryEntry = world.getBiome(this.getBlockPos());
        FrogLib.CUSTOM_FROG_SPAWN_TAGS.forEach((frogVariant, biomeTagKey) -> {
            if (registryEntry.isIn(biomeTagKey)) {
                this.setVariant(frogVariant);
            }
        });

    }
}
