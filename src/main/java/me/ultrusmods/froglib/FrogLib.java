package me.ultrusmods.froglib;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.FrogVariant;
import net.minecraft.item.ItemConvertible;
import net.minecraft.loot.condition.DamageSourcePropertiesLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.TypeSpecificPredicate;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class FrogLib implements ModInitializer {
    public static final String MOD_ID = "froglib";
    public static final Logger LOGGER = LoggerFactory.getLogger("froglib");

    private static final Identifier MAGMA_CUBE_TABLE = new Identifier("entities/magma_cube");

    public static final HashMap<FrogVariant, TagKey<Biome>> CUSTOM_FROG_SPAWN_TAGS = new HashMap<>();
    public static final HashMap<FrogVariant, ItemConvertible> CUSTOM_FROGLIGHT_ITEMS = new HashMap<>();


    @Override
    public void onInitialize() {
		LOGGER.info("The Frogs are Awakening!");
		LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, table, setter) -> {
			if (MAGMA_CUBE_TABLE.equals(id)) {
                FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
                        .rolls(ConstantLootNumberProvider.create(1));
                CUSTOM_FROGLIGHT_ITEMS.forEach((frogVariant, itemConvertible) -> {
                    poolBuilder.with(ItemEntry.builder(itemConvertible).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0f))).conditionally(this.killedByFrog(frogVariant)));
                });
				table.pool(poolBuilder);
			}
		});
    }

    /**
     * Registers and returns a new frog variant.
     *
     * @param id The id of the frog.
     * @param texture The texture location of the frog
     * @return Returns the new FrogVariant
     */
    public static FrogVariant registerFrog(Identifier id, Identifier texture) {
        return register(id, texture);
    }

    /**
     * Registers a tag that will control where the specified frog variant will be born.
     *
     * @param frogVariant The Frog Variant you wish to give a custom spawn tag.
     * @param spawnTag The tag that dictates where the frog will spawn.
     */
    public static void registerVariantSpawnLocation(FrogVariant frogVariant, TagKey<Biome> spawnTag) {
        CUSTOM_FROG_SPAWN_TAGS.put(frogVariant, spawnTag);
    }

    /**
     * Registers an item that will drop when magma cubes eat the specified frog variant.
     *
     * @param frogVariant The Frog Variant you wish to give a custom spawn tag.
     * @param itemConvertible Item Convertible of item you wish magma cubes to drop when eaten by frog.
     */
    public static void registerFroglightLikeItem(FrogVariant frogVariant, ItemConvertible itemConvertible) {
        CUSTOM_FROGLIGHT_ITEMS.put(frogVariant, itemConvertible);
    }


    private LootCondition.Builder killedByFrog(FrogVariant variant) {
        return DamageSourcePropertiesLootCondition.builder(DamageSourcePredicate.Builder.create().sourceEntity(EntityPredicate.Builder.create().type(EntityType.FROG).typeSpecific(TypeSpecificPredicate.frog(variant))));
    }

    private static FrogVariant register(Identifier id, Identifier texture) {
        return Registry.register(Registry.FROG_VARIANT, id, new FrogVariant(texture));
    }
}
