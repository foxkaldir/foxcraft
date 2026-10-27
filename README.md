# Foxcraft

A Java 21 mod for Minecraft **1.21.1**, built separately for **Fabric** and
**NeoForge** from the same content definitions and resources.

- **Charcoal Block:** nine charcoal make one block; one block returns nine
  charcoal. Burns for 16,000 ticks, enough for 80 furnace items. Uses the supplied
  texture and the same physical properties as a coal block. Automated Crafters
  work in both directions.
- **Leather smoking:** one rotten flesh becomes one leather in a smoker only,
  taking 100 ticks and awarding 0.1 XP.
- **Sack:** eight leather around a chest make a brown shulker box named Sack.
  All 16 dyes preserve its name and contents through vanilla shulker-box dyeing.

## Build and install

Install a **Java 21 JDK** and set `JAVA_HOME` to it. The Gradle wrapper downloads
Gradle and the Minecraft dependencies automatically; the first build takes longer.

Windows PowerShell:

```powershell
.\gradlew.bat bundleMods
```

Linux / macOS:

```sh
bash ./gradlew bundleMods
```

This builds both loaders and writes:

```text
output/foxcraft-fabric-1.21.1-0.1.0.jar
output/foxcraft-neoforge-1.21.1-0.1.0.jar
```

Install **one** JAR matching the loader into `mods/` on both the server and each
client. Fabric also requires **Fabric API for 1.21.1**. NeoForge needs no extra
runtime library. Models, textures, and recipes are included in the JAR; no
separate resource pack or datapack is needed.

## Editing the mod

Edit resources in their feature folder under `common/content/`; the build
assembles Minecraft's required resource paths for both loaders.

| Change | Edit |
| --- | --- |
| Block properties and fuel | [ContentCatalog.java](common/src/main/java/dev/foxcraft/content/ContentCatalog.java) |
| Charcoal models, texture, drops, recipes, and unlocks | [Charcoal block](common/content/charcoal_block/) |
| Leather smoking recipe and unlock | [Leather](common/content/leather/) |
| Sack recipe and unlock | [Sack](common/content/sack/) |
| Names, tool tags, and material tags | [Shared resources](common/content/shared/) |
| Minecraft / loader versions | [gradle.properties](gradle.properties) |

For example, the charcoal definition reads:

```java
BlockDefinition.copyOf("charcoal_block", "minecraft:coal_block")
        .fuelTicks(16_000)
        .creativeTab("minecraft:building_blocks");
```

The shared catalog has no Minecraft or loader imports. Loader-specific code is
limited to registration, fuel integration, and creative-tab insertion. No mixins,
placement raycasts, or tick functions are used by the mod.

See [the editing guide](docs/editing-content.md),
[architecture](docs/architecture.md), and
[Minecraft upgrade guide](docs/updating-minecraft.md).
Upgrading to another Minecraft release can still require API and JSON changes;
changing the version number alone does not establish compatibility.

## Development and verification

```powershell
.\gradlew.bat build
.\gradlew.bat :fabric:runClient
.\gradlew.bat :neoforge:runClient
```

`build` compiles all three modules and packages both loaders. Verify gameplay
and appearance manually in each loader's development client.

The [documentation index](docs/README.md) routes maintenance tasks.
