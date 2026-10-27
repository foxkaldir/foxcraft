# Sacks

Eight leather surrounding a normal chest in a crafting table produce one
brown shulker box named **Sack**, with a non-italic custom name.

```text
Leather  Leather  Leather
Leather  Chest    Leather
Leather  Leather  Leather
```

Sacks intentionally use vanilla shulker boxes. They store and retain items,
can be placed and broken, and can be re-dyed with all 16 vanilla dyes in a
crafting grid. Dyeing preserves the Sack name and stored contents. In the
supported version, 1.21.1, the current color can be applied again.

The shared recipe is
`common/content/sack/recipes/sack.json`, registered as
`foxcraft:sack`. Its advancement in `common/content/sack/advancements/sack.json`
unlocks on obtaining leather or unlocking the recipe. The result is
`minecraft:brown_shulker_box` with a `minecraft:custom_name` component.

There is no custom Sack block, renderer, container, or dyeing code. Changes to
vanilla shulker behavior in a future Minecraft version must be checked during
that port.

For a manual check, craft a Sack, fill it, place and break it, and cycle through
all 16 dyes. Verify its name, tooltip, contents, and shulker-box appearance.
See [Editing content](editing-content.md)
for source locations and build commands.
