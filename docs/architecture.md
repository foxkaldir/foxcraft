# Architecture

## Requirements

- Support Minecraft Java 1.21.1 on both Fabric and NeoForge.
- Maintain one shared implementation of the charcoal block, flesh-to-leather
  smoking recipe, and Sack recipe.
- Keep only active mod sources, resources, build tooling, and documentation
  in the repository. Remove superseded pack implementations and unused files
  instead of retaining archives or duplicate assets.
- Do not add automated tests or test infrastructure. Verify builds through
  compilation and packaging, and verify gameplay manually on both loaders.
- Keep the code modular, easily editable by the owner, and readable like
  configuration. Prefer a small declarative catalog and ordinary resource JSON
  over custom serializers, configuration frameworks, or a large content DSL.
- Group authored resources by feature in `common/content/`, with shared tags
  and translations centralized. Assemble Minecraft's required paths at build time.
- Make future Minecraft upgrades straightforward by isolating version-sensitive
  APIs and documenting the necessary format changes. Do not promise that a
  version-number edit alone ports a mod to a different release.

## Module boundaries

| Area | Responsibility |
| --- | --- |
| `common/src/main/java/dev/foxcraft/content` | Loader- and Minecraft-independent block definitions; the editable catalog. |
| `common/src/main/java/dev/foxcraft/platform` | Small bridge to version-sensitive vanilla APIs, such as block settings and registry keys. |
| `common/content/<feature>` | One copy of each feature's vanilla recipes, advancements, models, texture, and drops. |
| `common/content/shared` | Centralized translations, tag contributions, and pack metadata. |
| `common/build.gradle` | Assemble feature resources into Minecraft's packaged layout for all three modules. |
| `fabric/src/main` | Fabric registration, creative-tab event, fuel registry, and mod metadata. |
| `neoforge/src/main` | Deferred registration, creative-tab event, generated fuel data map, and mod metadata. |
| `common/src/datagen` | Build-time fuel-map generation from the same shared catalog; no generator code ships in the mod. |

Both loader modules compile the shared source directly. Fabric Loom remaps its
classes from official Mojang names for distribution; NeoForge uses Mojang names.
The common module separately compiles against unmodified Minecraft via
ModDevGradle's NeoForm mode, so accidentally adding loader imports fails that
build. No Architectury runtime, cross-loader abstraction library, or common JAR
is required by players.

This arrangement follows the loader tools' intended split:
[ModDevGradle vanilla mode](https://github.com/neoforged/ModDevGradle#vanilla-mode)
and [Fabric block registration](https://docs.fabricmc.net/1.21.1/develop/blocks/first-block).

## Content lifecycle

Authored resources live together under `common/content/<feature>/`, using
ordinary Minecraft JSON. `:common:assembleContentResources` uses a Gradle `Sync`
task to copy them into `common/build/generated/contentResources/`, mapping
filenames to the [1.21.1 resource layout](https://docs.neoforged.net/docs/1.21.1/resources/).
All three modules consume that task's output as a resource source directory,
so builds, source JARs, and development runs use the same assembled resources.
The task removes stale output, rejects duplicate destinations and unknown source
paths, and copies file contents unchanged. Shared tags and translations each
have one editable file, so no JSON merging is needed. See
[Editing content](editing-content.md) for the filename mappings and
[Gradle resource copying](https://docs.gradle.org/current/userguide/working_with_files.html)
for the build mechanism. Generated resources are never edited directly.

`ContentCatalog.BLOCKS` contains immutable `BlockDefinition` records. Each loader
iterates the same list. `MinecraftContent` validates the source block ID and
creates a block by copying its settings. Each loader registers a matching block
item and adds it to the chosen creative tab. Settings are compiled source
configuration, not a per-world runtime config.

Fuel time has one source: `ContentCatalog`. Fabric registers it through
`FuelRegistry`. The build generates NeoForge's standard
`data/neoforge/data_maps/item/furnace_fuels.json` from the same catalog. It is
packaged only in the NeoForge JAR, with no separately maintained numeric value.
This follows [NeoForge's recommendation to use data maps for fixed fuel times](https://docs.neoforged.net/docs/1.21.1/resources/server/datamaps/builtin/#neoforgefurnace_fuels)
and allows a higher-priority datapack to override the fuel value. A custom item
fuel override would take precedence over those overrides and is unnecessary here.

Charcoal belongs to additive block and item tags `c:storage_blocks` and
`c:storage_blocks/charcoal`, following the [shared tag namespace convention](https://docs.neoforged.net/docs/1.21.1/resources/server/tags/).
It does not join the coal-specific material tag. The custom tag has an English
translation for recipe viewers.

Recipes are vanilla data inside the JAR. The Sack intentionally remains a named
brown vanilla shulker box, so vanilla storage, dyeing, placement, and breaking
behavior apply. The charcoal block is a real registered block, so unpacking
previews and automated Crafters work without advancements or tick conversion.

## Builds and checks

`gradle.properties` pins Minecraft, Java, mappings, loader APIs, and build plugins.
`gradle/wrapper/gradle-wrapper.properties` separately pins the Gradle distribution
and its SHA-256 checksum. There are no floating dependency versions or snapshots.
Archive entries use a fixed timestamp and stable file order so rebuilding with
the same source, toolchain, and dependencies produces reproducible JARs. Development clients and
servers use separate, ignored `runs/client` and `runs/server` worlds per loader.
`.gitattributes` and `.editorconfig` keep source/resources in UTF-8 with LF line
endings across platforms; Windows batch launchers retain CRLF.

`gradlew build` compiles all three modules and packages both loaders.
`gradlew bundleMods` builds and checks all three modules, then copies just the
two installable JARs into the generated `output/` directory. Do not install a
common, sources, or development JAR. The `Sync` task removes stale generated
release files from `output/`.

Launch each loader's development client to verify gameplay and appearance
manually after changing content.
