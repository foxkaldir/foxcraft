# Charcoal blocks

Nine charcoal in a full 3x3 grid produce one **Charcoal Block**. One block in
any crafting-grid slot returns nine charcoal. Both recipes work in automated
Crafters with correct output previews. The registered ID is
`foxcraft:charcoal_block` for both the block and its inventory item.

One block provides **16,000 fuel ticks**, ten individual charcoal worth of fuel,
or **80 items in a normal furnace**. Smokers and blast furnaces consume this fuel
at their normal faster rate. Fuel is declared once in `ContentCatalog.java`;
both loader adapters use it.

The block copies a coal block's physical settings, including hardness 5,
resistance 6, sounds, and tool requirements. Its mineable/pickaxe tag allows any
pickaxe. Mining drops one charcoal block, including with Silk Touch; Fortune
does not multiply it. Normal piston movement preserves the actual block state.

The supplied texture is bundled at
`common/content/charcoal_block/texture.png`,
with the same texture on every face. The blockstate, block model, item model,
and English name are also shared resources. No separate resource pack is needed.
Additive `c:storage_blocks` and `c:storage_blocks/charcoal` item and block tags
provide interoperability without treating charcoal as ordinary coal.

Packing and unpacking JSON live in `common/content/charcoal_block/recipes/`.
Their matching files in `advancements/` unlock on obtaining charcoal or a
charcoal block. Drops live in the feature's `loot.json`; the pickaxe tag is in
`common/content/shared/tags/minecraft/block/mineable/pickaxe.json`. The build
assembles these into the standard Minecraft resource paths without changing IDs.

Verify manually on each loader: round-trip crafting,
ordinary coal unpacking, automated charcoal unpacking, real placement/mining,
piston movement, and a real furnace smelting exactly 80 items from one block
while leaving an 81st input uncooked. Also check shared material tags and the
loader's creative-tab insertion, and visually inspect rendering when changing
assets.
