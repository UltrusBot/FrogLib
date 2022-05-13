package me.ultrusmods.froglib.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;

public interface FrogInitializeCallback {
    Event<FrogInitializeCallback> EVENT = EventFactory.createArrayBacked(FrogInitializeCallback.class,
            (listeners) -> (world, difficulty, spawnReason, entityData, entityNbt, frog) -> {
                for (FrogInitializeCallback callback : listeners) {
                    callback.onFrogInit(world, difficulty, spawnReason, entityData, entityNbt, frog);
                }
            }
    );
    void onFrogInit(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, NbtCompound entityNbt, FrogEntity frog);

}
