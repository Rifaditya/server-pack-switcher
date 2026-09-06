# Guía de administración

Esta guía detalla las características para administradores, la recarga en vivo de la configuración y la persistencia de preferencias de los jugadores.

---

## 💾 Persistencia de preferencias

Cuando un jugador selecciona un paquete mediante `/pack <name>`, el servidor guarda esta elección en `config/serverpackswitcher_preferences.json`.
- **Reaplicación automática**: Cuando el jugador vuelve a conectarse más tarde, el servidor lee automáticamente sus preferencias y le envía su paquete configurado.
- **Limpieza de preferencias**: Si un jugador usa `/pack pop` o `/pack clear`, su registro se borra y no recibirá ningún paquete al volver a conectarse.

---

## 🔄 Recarga de configuración en vivo

Si edita el archivo `config/serverpackswitcher.json` con el servidor en marcha, ¡no necesita reiniciar el servidor!
1. Asegúrese de contar con permisos de operador (nivel 2+).
2. Escriba **`/pack reload`** en el chat o en la consola del servidor.
3. El servidor recargará la configuración al instante. Los paquetes configurados con Modrinth se actualizarán de forma asíncrona en un hilo secundario.

---

## 🛡️ Comandos administrativos dirigidos

Los administradores (operadores con nivel de permiso 2+) pueden forzar la aplicación o eliminación de paquetes a otros jugadores.

### Forzar la aplicación de un paquete
- `/pack apply plastic Bob` - Aplica el paquete `plastic` al jugador `Bob`.
- `/pack apply plastic @a` - Aplica el paquete `plastic` a todos los jugadores conectados.

### Forzar la eliminación de un paquete
- `/pack pop Bob` - Elimina el paquete activo del jugador `Bob`.
- `/pack pop @a` - Elimina los paquetes activos de todos los jugadores conectados.
