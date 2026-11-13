# Minecraft NeoForge Quality-of-Life Mod

OptiMek is a dependency-free Minecraft NeoForge quality-of-life and diagnostics mod for automation-focused servers. It provides server commands for checking loaded dimensions, counting dropped items near a player, and removing dropped item entities in a controlled radius. It can complement Create, Applied Energistics 2, and Mekanism setups without requiring those mods or changing their machines and recipes.

## Install

1. Install Minecraft 1.21.11.
2. Install NeoForge 21.11.48 or a compatible later release.
3. Place the built `factoryflow-1.0.0.jar` file in the instance's `mods` folder.
4. Launch the NeoForge profile.
5. Use an account with permission level 2 or equivalent game-master permissions to run the commands.

## Use

Run these commands as a player with the required permissions:

```text
/factoryflow status
```

Reports the number of loaded dimensions and the total number of loaded dropped-item entities across those dimensions.

```text
/factoryflow items
```

Counts dropped-item entities and individual items within 32 blocks of the executing player.

```text
/factoryflow items <radius>
```

Counts dropped-item entities and individual items within the specified radius. The radius must be between 1 and 128 blocks.

```text
/factoryflow clearitems
```

Removes dropped-item entities within 32 blocks of the executing player.

```text
/factoryflow clearitems <radius>
```

Removes dropped-item entities within the specified radius. The radius must be between 1 and 128 blocks.

The mod sends a readiness message to permitted players when they join. The commands operate on the server and do not require Create, Applied Energistics 2, or Mekanism to be installed.

---

More Minecraft mod projects by EnderDevelopment: https://github.com/EnderDevelopment

Project page: https://dash.enderdevelopment.com/community/bb984b97-51a9-48ba-abfd-36b4a42db910?utm_source=github&utm_medium=readme&utm_campaign=auto_publish&utm_content=bb984b97-51a9-48ba-abfd-36b4a42db910
