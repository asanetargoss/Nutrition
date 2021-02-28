# Nutrition: Hardcore Alchemy Edition

Nutrition: Hardcore Alchemy Edition is a fork of the 1.10 version of WesCook's Nutrition mod. It was created for the [Hardcore Alchemy Modpack](https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/mod-packs/2900247-hardcore-alchemy-0-4-1-fixing-the-4th-dimension).

By default, Nutrition: HcA edition uses Nutrition's five-food group system: grain, vegetable, fruit, protein, and dairy. Nutrients are highly configurable; the mod allows for the creation of arbitrary player stats which are affected by food items.

There is no dedicated wiki for Nutrition: HcA Edition, however [the upstream wiki](https://github.com/WesCook/Nutrition/wiki) is a mostly accurate resource for how to configure the mod.

![Nutrition GUI](screenshots/gui.png "Nutrition GUI")

## Food Compatibility

By default, Nutrition: HcA edition includes values for foods from the following mods using the five-food group system:

* Vanilla Minecraft
* [Aether Legacy](https://minecraft.curseforge.com/projects/aether-legacy)
* [Animalium](https://minecraft.curseforge.com/projects/animalium)
* [Animania](https://minecraft.curseforge.com/projects/animania)
* [Aquaculture](https://minecraft.curseforge.com/projects/aquaculture)
* [Better with Mods](https://minecraft.curseforge.com/projects/better-with-mods)
* [Biomes O' Plenty](https://minecraft.curseforge.com/projects/biomes-o-plenty)
* [Edible Bugs](https://minecraft.curseforge.com/projects/edible-bugs)
* [Food Expansion](https://minecraft.curseforge.com/projects/food-expansion)
* [Forestry](https://minecraft.curseforge.com/projects/forestry)
* [Grim Pack](https://minecraft.curseforge.com/projects/grim-pack)
* [Ice and Fire](https://minecraft.curseforge.com/projects/ice-and-fire-dragons-in-a-whole-new-light)
* [Jurassicraft](https://minecraft.curseforge.com/projects/jurassicraft2)
* [Natura](https://minecraft.curseforge.com/projects/natura)
* [Pam's HarvestCraft](https://minecraft.curseforge.com/projects/pams-harvestcraft)
* [Plants](https://minecraft.curseforge.com/projects/plants)
* [Roots](https://minecraft.curseforge.com/projects/roots)
* [Simple Corn](https://minecraft.curseforge.com/projects/simple-corn)
* [Tinkers Construct](https://minecraft.curseforge.com/projects/tinkers-construct)

## Changelog

[v1.6.0.hca.3](https://github.com/asanetargoss/Nutrition/releases/tag/v1.6.0.hca.3) - 2021-02-27
* Fixed the numeric value display in the nutrient overlay not matching the nutrient gui

[v1.6.0.hca.2](https://github.com/asanetargoss/Nutrition/releases/tag/v1.6.0.hca.2) - 2021-02-14
* Added Nutrition HUD overlay which fades away if nutrition is nearly full
* Fixed crash which would sometimes occur when opening the Nutrition GUI

[v1.6.0_HcA_v1](https://github.com/asanetargoss/Nutrition/releases/tag/v1.6.0_HcA_v1) - 2018-01-12
* Added ability to disable nutrients and adjust decay rate in the player nutrition capability

[v1.6.0](https://github.com/WesCook/Nutrition/releases/tag/v1.6.0) - 2017-08-01
* Added config to prevent nutrition from being reset back up to the minimum upon death
* Amplifier is no longer a required field
* Added support for VanillaFoodPantry (contribution from darloth)
* Updated support for Animania (contribution from Sunconure11)

[v1.5.0](https://github.com/WesCook/Nutrition/releases/tag/v1.5.0) - 2017-06-28
* Added nourished and malnourished effects.  Each adds or subtracts one heart per amplifier level.
* Added `cumulative_modifier` property to effects.  In cumulative detection mode, this will increase the amplifier by the provided value for each cumulative level.  

[v1.4.0](https://github.com/WesCook/Nutrition/releases/tag/v1.4.0) - 2017-06-22
* Added support for Aether Legacy, Animalium, Aquaculture, Ice and Fire, Jurassicraft, Plants, and Rustic (contribution from Sunconure11)

[v1.3.1](https://github.com/WesCook/Nutrition/releases/tag/v1.3.1) - 2017-06-13
* Enabled Nutrition key to both open and close GUI
* Fixed severe nutrition decay bug in multiplayer

[v1.3.0](https://github.com/WesCook/Nutrition/releases/tag/v1.3.0) - 2017-06-10
* Corrected packet sync error which resulted in clients showing incorrect data under some conditions 
* Added Dutch localizations (contribution from Arthur Dent)
* Added Spanish localizations (contribution from Rougito)

[v1.2.0](https://github.com/WesCook/Nutrition/releases/tag/v1.2.0) - 2017-06-03
* Updated support for HarvestCraft on 1.10
* Added German localizations (contribution from ACGaming)
* Added Swedish localizations (contribution from John "Rufus" Lundström)
* Added Norwegian localizations (contribution from Marcus "Rex" Holm)

[v1.1.0](https://github.com/WesCook/Nutrition/releases/tag/v1.1.0) - 2017-05-31
* Added support for Biomes O' Plenty, Forestry, Natura, Roots, and Tinkers Construct (contribution from KnightMiner)
* Nutrient field can now be negated from other detection modes (contribution from KnightMiner)

[v1.0.1](https://github.com/WesCook/Nutrition/releases/tag/v1.0.1) - 2017-05-28
* Improved detection when attaching capability

[v1.0.0](https://github.com/WesCook/Nutrition/releases/tag/v1.0.0) - 2017-05-28
* Initial release
