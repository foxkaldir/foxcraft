package dev.foxcraft.content;

import java.util.List;

/** Edit block behavior here; edit recipes and textures in common/content/<feature>/. */
public final class ContentCatalog {
    public static final BlockDefinition CHARCOAL_BLOCK = BlockDefinition
            .copyOf("charcoal_block", "minecraft:coal_block")
            .fuelTicks(16_000) // 80 furnace items, or ten individual charcoal.
            .creativeTab("minecraft:building_blocks");

    // Add another definition to this list to register it on BOTH loaders.
    public static final List<BlockDefinition> BLOCKS = List.of(CHARCOAL_BLOCK);

    private ContentCatalog() {}
}
