package dev.foxcraft.data;

import dev.foxcraft.Foxcraft;
import dev.foxcraft.content.ContentCatalog;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

/** Build-time resource generation only; this class is never shipped in the mod. */
public final class NeoForgeFuelData {
    public static void main(String[] arguments) throws IOException {
        Path output = Path.of(arguments[0]).resolve("data/neoforge/data_maps/item/furnace_fuels.json");
        // IDs are validated by BlockDefinition, and fuel ticks are integers.
        String entries = ContentCatalog.BLOCKS.stream()
                .filter(block -> block.fuelTicks() > 0)
                .map(block -> "    \"" + Foxcraft.MOD_ID + ":" + block.id()
                        + "\": { \"burn_time\": " + block.fuelTicks() + " }")
                .collect(Collectors.joining(",\n"));
        Files.createDirectories(output.getParent());
        Files.writeString(output, "{\n  \"values\": {\n" + entries + "\n  }\n}\n");
    }

    private NeoForgeFuelData() {}
}
