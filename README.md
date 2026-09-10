# NoCheating

A client-side Fabric mod that keeps Survival worlds from turning cheats on after you create them.

If you created a world **without commands** and **not in Creative**, vanilla still lets you open World Options or Open to LAN and flip Allow Cheats or Game Mode. NoCheating disables those controls on that world.

## What it locks

On a locked world:

- **World Options** in the Options screen
- **Game Mode** and **Allow Cheats** on Open to LAN

Disabled buttons stay visible and show a tooltip explaining they are locked.

![The Options screen with World Options greyed out and a tooltip that says World Options is disabled.](https://raw.githubusercontent.com/0LostConnection/NoCheating/master/docs/world-options-disabled.png)

## What it does not lock

Nothing is disabled if the world was created in **Creative**, or with **commands / Allow Cheats** already on. Those saves can still use the usual vanilla options.

NoCheating only disables the vanilla buttons. It does not change the world after the fact, and it is not a server anti-cheat.

## Requirements

- Minecraft **26.2**
- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 or later
- [Fabric API](https://modrinth.com/mod/fabric-api)

The mod is **client-only**. Install it on the client that hosts the world.

## Installation

1. Install Fabric Loader for Minecraft 26.2.
2. Put Fabric API and NoCheating in your `mods` folder.
3. Create or load a Survival world with commands off — World Options and the Open to LAN cheat controls should be disabled.

## Translations

English and Brazilian Portuguese (`pt_br`).

## License

[MIT](LICENSE). Copyright © 2026 Geovane Saraiva da Silva.

- [Source](https://github.com/0LostConnection/NoCheating)
- [Issues](https://github.com/0LostConnection/NoCheating/issues)
