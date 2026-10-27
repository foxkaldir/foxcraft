package dev.foxcraft.platform;

import dev.foxcraft.Foxcraft;
import dev.foxcraft.content.BlockDefinition;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

/** Version-sensitive vanilla API calls stay here, away from the content catalog. */
public final class MinecraftContent {
    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(Foxcraft.MOD_ID, path);
    }

    public static Block createBlock(BlockDefinition definition) {
        ResourceLocation sourceId = ResourceLocation.parse(definition.copyOf());
        if (!BuiltInRegistries.BLOCK.containsKey(sourceId)) {
            throw new IllegalArgumentException("Unknown block to copy: " + sourceId);
        }
        Block source = BuiltInRegistries.BLOCK.get(sourceId);
        return new Block(BlockBehaviour.Properties.ofFullCopy(source));
    }

    public static ResourceKey<CreativeModeTab> creativeTab(BlockDefinition definition) {
        return ResourceKey.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.parse(definition.creativeTab()));
    }

    private MinecraftContent() {}
}
