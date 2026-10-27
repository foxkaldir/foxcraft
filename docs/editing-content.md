# Editing content

Start with [ContentCatalog.java](../common/src/main/java/dev/foxcraft/content/ContentCatalog.java)
and `common/content/`. These are shared by both loaders. Change one
source, rebuild, and both release JARs receive the change.

## Feature folders

Keep each feature's resources together:

```text
common/content/
  charcoal_block/
    blockstate.json
    block_model.json
    item_model.json
    texture.png
    loot.json
    recipes/
    advancements/
  leather/
    recipes/
    advancements/
  sack/
    recipes/
    advancements/
  shared/
    lang/en_us.json
    tags/c/
    tags/minecraft/
    pack.mcmeta
```

All JSON is still ordinary Minecraft 1.21.1 JSON. The build copies it unchanged
into the required pack paths; it does not generate JSON or merge contributions.
The feature folder names a block's registered ID when it contains block files.
Recipe and advancement filenames retain their own IDs independently of the
feature folder.

| Source relative to `common/content/` | Packaged path |
| --- | --- |
| `<feature>/blockstate.json` | `assets/foxcraft/blockstates/<feature>.json` |
| `<feature>/block_model.json` | `assets/foxcraft/models/block/<feature>.json` |
| `<feature>/item_model.json` | `assets/foxcraft/models/item/<feature>.json` |
| `<feature>/texture.png` | `assets/foxcraft/textures/block/<feature>.png` |
| `<feature>/loot.json` | `data/foxcraft/loot_table/blocks/<feature>.json` |
| `<feature>/recipes/<id>.json` | `data/foxcraft/recipe/<id>.json` |
| `<feature>/advancements/<id>.json` | `data/foxcraft/advancement/recipes/<id>.json` |
| `shared/lang/<locale>.json` | `assets/foxcraft/lang/<locale>.json` |
| `shared/tags/<namespace>/<type>/<path>.json` | `data/<namespace>/tags/<type>/<path>.json` |
| `shared/pack.mcmeta` | `pack.mcmeta` |

Nested recipe, advancement, and tag paths are preserved. JSON references still
use Minecraft resource IDs, such as `foxcraft:block/charcoal_block`, rather than
authoring filenames. Keep tags and translations centralized in `shared/`.
The mapping lives in `common/build.gradle`; extend it deliberately for new
resource types. Unknown source paths and duplicate packaged paths fail the build.

`gradlew :common:assembleContentResources` assembles just these resources under
`common/build/generated/contentResources/`. Normal builds and development runs
also run it automatically. Never edit the generated copies or add a second copy
under `common/src/main/resources/`.

## Recipes

Recipes live in their feature's `recipes/` directory under `common/content/`:

| File | What to edit |
| --- | --- |
| `charcoal_block/recipes/charcoal_block.json` | The 3x3 charcoal packing pattern and block output count. |
| `charcoal_block/recipes/charcoal_from_block.json` | The single-block unpacking input and charcoal count. |
| `leather/recipes/leather_from_smoking.json` | Input, output, cooking time, and XP. Keep type `minecraft:smoking` for smoker-only behavior. |
| `sack/recipes/sack.json` | Leather/chest pattern, brown shulker result, and the Sack custom name. |

Keep result counts and reverse recipes balanced. When changing a recipe's main
ingredient, also edit its matching unlock advancement in
the same feature's `advancements/` directory. Existing IDs should remain stable once
released. Vanilla datapacks may override these recipe IDs intentionally.

## Block settings

```java
public static final BlockDefinition CHARCOAL_BLOCK = BlockDefinition
        .copyOf("charcoal_block", "minecraft:coal_block")
        .fuelTicks(16_000)
        .creativeTab("minecraft:building_blocks");
```

`copyOf` selects the vanilla physical behavior to copy: hardness, resistance,
sound, collision, and other block settings. `fuelTicks(0)` means the block item
has no default fuel value. A normal furnace needs 200 fuel ticks per item.
Fabric reads that value at registration; the build generates NeoForge's fuel
data map from it. Do not edit generated JSON in `common/build/` or add another
loader-specific fuel constant. NeoForge datapacks can override the generated
default through the normal fuel data map.

## Adding another plain block

1. Add a `BlockDefinition` and include it in `ContentCatalog.BLOCKS`. Both
   loaders register its block and block item automatically.
2. Create `common/content/<id>/` with `blockstate.json`, `block_model.json`,
   and `item_model.json`. The charcoal block is a compact example to copy;
   update its JSON references to the new block ID.
3. Add `texture.png` in that folder and a translated name in
   `common/content/shared/lang/en_us.json`.
4. Add `loot.json` in that folder. Add the block to the appropriate
   `common/content/shared/tags/minecraft/block/mineable/` tool tag. Higher-tier tool
   requirements need the corresponding vanilla tool-level tag too.
   Add applicable shared `c:` tags for interoperability, with translated item
   tag names for recipe viewers; keep tag contributions additive.
5. Add recipe JSON under `recipes/` and an unlock advancement under
   `advancements/` in the feature folder, then verify any new behavior
   manually on both loaders.

No loader Java should change for another ordinary block. Blocks with inventories,
custom interactions, or rendering need a deliberately scoped extension to the
shared model and adapters; avoid turning the catalog into a general-purpose
scripting engine.

The canonical charcoal texture is
`common/content/charcoal_block/texture.png`.

## Verify a change

Run `gradlew.bat bundleMods` on Windows or `./gradlew bundleMods` elsewhere.
Launch `:fabric:runClient` and `:neoforge:runClient` to verify changed gameplay,
models, textures, names, and creative-tab UI manually on both loaders.
