package dev.foxcraft.content;

/** Content settings only. This class intentionally has no Minecraft or loader imports. */
public record BlockDefinition(String id, String copyOf, int fuelTicks, String creativeTab) {
    public BlockDefinition {
        if (id == null || !id.matches("[a-z0-9_]+")) {
            throw new IllegalArgumentException("Block ID must be a simple lowercase name: " + id);
        }
        requireResourceId(copyOf);
        requireResourceId(creativeTab);
        if (fuelTicks < 0) {
            throw new IllegalArgumentException("Fuel ticks cannot be negative: " + id);
        }
    }

    public static BlockDefinition copyOf(String id, String vanillaBlock) {
        return new BlockDefinition(id, vanillaBlock, 0, "minecraft:building_blocks");
    }

    public BlockDefinition fuelTicks(int ticks) {
        return new BlockDefinition(id, copyOf, ticks, creativeTab);
    }

    public BlockDefinition creativeTab(String tab) {
        return new BlockDefinition(id, copyOf, fuelTicks, tab);
    }

    private static void requireResourceId(String value) {
        if (value == null || !value.matches("[a-z0-9_.-]+:[a-z0-9_./-]+")) {
            throw new IllegalArgumentException("Expected a namespaced ID, such as minecraft:coal_block: " + value);
        }
    }
}
