# Leather smoking

One rotten flesh produces one leather in a **smoker only**. Furnaces, blast
furnaces, and campfires cannot perform this conversion. The recipe takes
**100 cooking ticks** and awards **0.1 experience** per item.

The shared recipe is
`common/content/leather/recipes/leather_from_smoking.json`,
registered as `foxcraft:leather_from_smoking`. Keep `type` set to
`minecraft:smoking`. Its advancement at
`common/content/leather/advancements/leather_from_smoking.json` unlocks the recipe
when the player obtains rotten flesh or already unlocks the recipe.

Both mod JARs contain the same JSON. No Java recipe handler is necessary.
Verify manually on each loader that flesh cooks in a smoker and that the
other three cooking recipe types have no matching conversion.

See [Editing content](editing-content.md) for changing input, output, time, XP,
and the unlock ingredient, then rebuild with `gradlew bundleMods`.
