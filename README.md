# Frog Lib
Lets you add custom frogs, easily!

To register a custom frog, it's as simple as:

```java
// Registers a frog variant, called modid:blue
FrogVariant BLUE = FrogLib.registerFrog(new Identifier("modid:blue"), new Identifier("modid:textures/entity/frog/blue_frog.png"));

// Makes it so the blue frog will only be created while in the end.
FrogLib.registerVariantSpawnLocation(BLUE, BiomeTags.IS_END);

// Makes it so the frog will drop an ender pearl when it eats a magma cube
FrogLib.registerFroglightLikeItem(BLUE, Items.ENDER_PEARL);
```
