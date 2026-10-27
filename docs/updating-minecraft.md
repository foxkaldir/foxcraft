# Updating Minecraft

## Porting contract

The released target is **1.21.1**. Dependency versions and loader metadata are
intentionally bounded to that target. A new Minecraft release can change Java,
build tools, registries, recipes, text components, and item rendering. The goal
is to keep those changes localized and testable, not to claim automatic binary
compatibility.

The catalog is plain Java records and strings. Recipes and assets are shared
once, and no mixins or internal field access need to be maintained. Normal
content edits should not require loader changes.

## Upgrade sequence

1. Choose the target Minecraft release and compatible Fabric / NeoForge builds.
   Use a development branch; retain the working 1.21.1 release.
2. Update version pins in `gradle.properties`: Minecraft, Java, NeoForm, loader,
   Fabric API, NeoForge, its accepted version range, Loom, and ModDevGradle.
   Upgrade the Gradle wrapper and checksum when those build plugins require it.
3. Check `common/src/main/java/dev/foxcraft/platform/MinecraftContent.java` for
   changed registry lookup, block settings, or required registration IDs. Keep
   those adaptations out of `ContentCatalog.java`.
4. Update Fabric's small registration/fuel adapter, NeoForge's deferred
   registration, and the generated NeoForge fuel-map format if their APIs
   changed. Retain a single shared fuel value in the catalog. Newer loader fuel
   registries can replace either integration without changing the catalog.
5. Convert shared resource JSON for the target: recipe ingredient format, text
   components, item-model definitions, loot tables, tags, and pack metadata.
   Update the feature-to-pack path mappings in `common/build.gradle` when the
   target changes resource directories; authored files stay in `common/content/`.
   Update both mod metadata templates if their loader schema changed. Their
   Minecraft target and NeoForge version range already expand from properties.
6. Run `gradlew clean build bundleMods` to compile and package both loaders.
   Do not widen a Minecraft version range to bypass an API or resource-format
   incompatibility.
7. Launch each loader's client and inspect inventory/world textures, creative
   tabs, recipe previews, Sack storage/dyeing, and placement/mining. Test a copy
   of a saved world before publishing the port.

## Known boundaries after 1.21.1

| Release | Changes to account for |
| --- | --- |
| 1.21.2 | Recipe ingredients switch from item objects to item ID strings; registration settings APIs and item-model selection also change. |
| 1.21.4 | Custom items need the newer `assets/<namespace>/items/` model definitions; the old item model alone is insufficient. |
| 1.21.5 | Recipe-result names become inline text components instead of JSON inside strings. |
| 1.21.9 | Pack metadata gains major/minor version ranges. |
| 26.1 | Java 25 and unobfuscated game executables affect toolchains, mappings, and the Fabric build-plugin setup. |

Read the exact target's official notes rather than assuming later versions keep
an earlier format:
[1.21.2](https://www.minecraft.net/en-us/article/minecraft-java-edition-1-21-2),
[1.21.4](https://www.minecraft.net/en-us/article/minecraft-java-edition-1-21-4),
[1.21.5](https://www.minecraft.net/en-us/article/minecraft-java-edition-1-21-5),
[1.21.9](https://www.minecraft.net/en-us/article/minecraft-java-edition-1-21-9),
and [26.1](https://www.minecraft.net/en-us/article/minecraft-java-edition-26-1).

Build-tool references:
[Fabric Loom](https://docs.fabricmc.net/develop/loom/),
[NeoForge getting started](https://docs.neoforged.net/docs/gettingstarted/),
[ModDevGradle](https://github.com/neoforged/ModDevGradle),
[NeoForm releases](https://projects.neoforged.net/neoforged/neoform),
and [Gradle's Java compatibility matrix](https://docs.gradle.org/current/userguide/compatibility.html).

## Keep saved identities stable

Keep the `foxcraft` mod ID and `charcoal_block` registry name stable. Recipes,
models, drops, and translations refer to the same ID. Renaming a Java class is
safe when its registry ID remains unchanged; changing a registry ID needs an
explicit saved-world migration.
