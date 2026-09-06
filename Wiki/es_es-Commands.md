# Referencia de comandos

Todos los comandos del mod comienzan con el prefijo `/pack`.

## Lista de comandos

| Comando | Permiso | Descripción |
| :--- | :--- | :--- |
| `/pack list` | Todos | Muestra la lista de todos los paquetes de recursos configurados. |
| `/pack <pack_name>` | Todos | Descarga y aplica el paquete de recursos especificado al jugador. |
| `/pack apply <pack_name>` | Todos | Alias de `/pack <pack_name>`. |
| `/pack pop` | Todos | Elimina el paquete del servidor activo en el cliente del jugador. |
| `/pack clear` | Todos | Alias de `/pack pop`. |
| `/pack apply <pack_name> <targets>` | Operador (Nivel 2+) | Aplica forzosamente el paquete de recursos a los jugadores indicados. |
| `/pack pop <targets>` | Operador (Nivel 2+) | Elimina forzosamente el paquete del servidor de los jugadores indicados. |
| `/pack reload` | Operador (Nivel 2+) | Recarga el archivo de configuración desde el disco. |

*Nota: Los argumentos de los comandos son compatibles con autocompletado (tecla TAB) para todos los nombres de paquetes configurados.*

---

## Detalles de los comandos

### `/pack list`
Muestra los nombres de todos los paquetes de recursos configurados en el servidor disponibles para los jugadores.

### `/pack <pack_name>`
Descarga y aplica el paquete coincidente con `<pack_name>`. Si ya tenía aplicado otro paquete proporcionado por este mod, se retirará automáticamente para garantizar un cambio limpio.

### `/pack pop` / `/pack clear`
Elimina el paquete de recursos del servidor activo de su cliente, restableciendo la apariencia visual vanilla por defecto.
