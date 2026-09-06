# Commands Reference

All commands start with the `/pack` base prefix.

## Command List

| Command | Permission | Description |
| :--- | :--- | :--- |
| `/pack list` | Everyone | Displays a list of all configured server resource packs. |
| `/pack <pack_name>` | Everyone | Applies the specified resource pack to the caller. |
| `/pack apply <pack_name>` | Everyone | Alias for `/pack <pack_name>`. |
| `/pack pop` | Everyone | Removes the active server pack from the caller's client. |
| `/pack clear` | Everyone | Alias for `/pack pop`. |
| `/pack apply <pack_name> <targets>` | Gamemaster (Level 2+) | Forces the specified resource pack onto the target player(s). |
| `/pack pop <targets>` | Gamemaster (Level 2+) | Forces removal of the active server pack from the target player(s). |
| `/pack reload` | Gamemaster (Level 2+) | Reloads the configuration file from disk. |

*Note: Command arguments support autocomplete (TAB suggestions) for all configured pack names.*

---

## Command Details

### `/pack list`
Displays the names of all configured resource packs that are available for players to apply.

### `/pack <pack_name>`
Downloads and applies the pack matching `<pack_name>` to your game. If you currently have another pack applied from this mod, the mod will automatically remove it first to ensure a clean swap.

### `/pack pop` / `/pack clear`
Clears the active server resource pack from your client, returning your game to its default look.
