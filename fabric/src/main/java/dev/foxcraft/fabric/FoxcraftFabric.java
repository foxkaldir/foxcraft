package dev.foxcraft.fabric;

import dev.foxcraft.content.BlockDefinition;
import dev.foxcraft.content.ContentCatalog;
import dev.foxcraft.platform.MinecraftContent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/** Fabric registration only. Gameplay definitions belong in common/. */
public final class FoxcraftFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ContentCatalog.BLOCKS.forEach(FoxcraftFabric::register);
    }

    private static void register(BlockDefinition definition) {
        Block block = Registry.register(BuiltInRegistries.BLOCK,
                MinecraftContent.id(definition.id()), MinecraftContent.createBlock(definition));
        Item item = Registry.register(BuiltInRegistries.ITEM,
                MinecraftContent.id(definition.id()), new BlockItem(block, new Item.Properties()));

        if (definition.fuelTicks() > 0) {
            FuelRegistry.INSTANCE.add(item, definition.fuelTicks());
        }
        ItemGroupEvents.modifyEntriesEvent(MinecraftContent.creativeTab(definition))
                .register(entries -> entries.accept(item));
    }
}
