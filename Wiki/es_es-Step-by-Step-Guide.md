# Guía paso a paso

¡Bienvenido! Esta guía está redactada para ser **muy fácil de seguir**, incluso si nunca antes ha administrado un servidor de Minecraft o configurado un mod.

---

## 🎮 ¿Qué hace este mod?
Por lo general, un servidor de Minecraft solo puede tener un único paquete de recursos que todos los jugadores se ven obligados a descargar.

Con este mod, **¡los jugadores pueden elegir su propia apariencia mediante comandos!**
Por ejemplo, un jugador puede escribir `/pack plastic` para activar un paquete de textura estilo plástico, o `/pack pop` para volver al aspecto original.

¡A continuación se explica cómo configurarlo en **5 sencillos pasos**!

---

## 📅 Paso 1: Obtener el enlace del paquete de recursos
El servidor necesita saber de dónde descargar el archivo `.zip`.

### Opción A: Usar Modrinth (¡La más sencilla y recomendada!)
¡No necesita enlaces directos ni CDNs! Puede utilizar el enlace del proyecto, el slug o la versión de Modrinth:
- **URL del proyecto**: `https://modrinth.com/resourcepack/glass-doors`
- **Slug**: `glass-doors`
- **URL de versión**: `https://modrinth.com/resourcepack/glass-doors/version/1.2.3`

### Opción B: Usar Discord
1. Suba el archivo `.zip` del paquete a cualquier canal de Discord.
2. Haga **clic derecho** sobre la flecha/icono de descarga del archivo.
3. Seleccione **Copiar enlace**.
4. El enlace tendrá el formato: `https://cdn.discordapp.com/attachments/.../pack.zip` (¡este es su enlace directo!).

### Opción C: Usar Dropbox
1. Suba el archivo `.zip` a Dropbox.
2. Haga clic en **Compartir** y copie el enlace.
3. El enlace tendrá este formato: `https://www.dropbox.com/s/.../pack.zip?dl=0`.
4. Modifique el final de la URL cambiando `?dl=0` por `?raw=1`.
5. El enlace final será: `https://www.dropbox.com/s/.../pack.zip?raw=1` (¡enlace directo listo!).

---

## 🔑 Paso 2: Obtener el hash SHA-1 del paquete
El servidor necesita un código de 40 caracteres (llamado **hash SHA-1**) para indicar al juego: *«Ya descargaste este paquete antes, ¡no necesitas volver a descargarlo!»*

### Opción A: Usando Modrinth (¡No se requiere hash!)
Si eligió la **Opción A (Modrinth)** en el Paso 1, **¡puede saltarse este paso!** El servidor consultará el hash automáticamente mediante la API de Modrinth. Vaya directamente al Paso 3.

### Opción B: Mediante un sitio web
1. Entre en [https://emn178.github.io/online-tools/sha1_checksum.html](https://emn178.github.io/online-tools/sha1_checksum.html).
2. Arrastre y suelte el archivo `.zip` en la casilla de la página web.
3. Se generará un código alfanumérico largo (ejemplo: `da39a3ee5e6b4b0d3255bfef95601890afd80709`).
4. ¡**Copie** ese código!

### Opción C: Con Windows PowerShell (sin conexión a internet)
1. Presione la **tecla Windows**, escriba **PowerShell** y pulse **Enter**.
2. Escriba `Get-FileHash -Algorithm SHA1 ` (asegúrese de dejar un espacio al final).
3. Arrastre su archivo `.zip` desde el explorador de archivos a la ventana de PowerShell.
4. Presione **Enter**.
5. Copie la secuencia que aparece bajo la columna **Hash**.

---

## 📝 Paso 3: Introducir los datos en el archivo de configuración
Una vez iniciado el servidor con el mod instalado, se genera el archivo de configuración:

1. Diríjase a la carpeta raíz de su servidor.
2. Abra la carpeta **`config`**.
3. Localice el archivo **`serverpackswitcher.json`**.
4. Haga clic derecho, seleccione **Abrir con** y elija el **Bloc de notas**.
5. Puede configurar sus paquetes con **Modrinth** o **enlaces directos**:

### Si usa Modrinth:
```json
{
  "packs": {
    "freshanimations": {
      "modrinth": "https://modrinth.com/resourcepack/fresh-animations",
      "required": false,
      "prompt": "Would you like to use the Fresh Animations look?"
    }
  }
}
```
*(¡No hace falta especificar `url` ni `hash`! El servidor los resolverá automáticamente al iniciar).*

### Si usa enlaces directos (Discord/Dropbox):
```json
{
  "packs": {
    "plastic": {
      "url": "PEGUE_AQUI_SU_ENLACE_DE_DISCORD_O_DROPBOX",
      "hash": "PEGUE_AQUI_EL_CODIGO_SHA1_DE_40_CARACTERES",
      "required": false,
      "prompt": "Would you like to use the Plastic look?"
    }
  }
}
```

6. Guarde el archivo (**Ctrl + S**) y cierre el editor.

---

## 🔄 Paso 4: Recargar la configuración
Si el servidor ya está funcionando, no es necesario reiniciarlo:
1. Conéctese a su servidor de Minecraft.
2. Asegúrese de tener permisos de operador (`/op`).
3. Abra el chat pulsando **T** o **/**.
4. Escriba **`/pack reload`** y presione **Enter**.
5. ¡Aparecerá un mensaje confirmando que los paquetes se han cargado exitosamente!

---

## 🧱 Paso 5: Cómo usar los comandos en el juego

Cualquier jugador en el servidor puede utilizar los siguientes comandos:

### 1. Ver todos los paquetes disponibles
Escriba: **`/pack list`**  
*Mostrará los nombres de los paquetes registrados en el Paso 3.*

### 2. Aplicarse un paquete
Escriba: **`/pack glassdoors`** (o `/pack apply glassdoors`)  
*Aparecerá una ventana en pantalla preguntando si desea descargar e instalar el paquete. Haga clic en **Sí**.*

### 3. Quitar el paquete actual
Escriba: **`/pack pop`** (o `/pack clear`)  
*El juego volverá al instante a su apariencia predeterminada.*

### 4. Comando de administración: Enviar un paquete a otro jugador
Los administradores pueden asignar un paquete a otros usuarios:  
Escriba: **`/pack apply glassdoors Bob`** (sustituyendo Bob por el nombre del jugador, o `@a` para todos).

---

¡Eso es todo! Ahora los jugadores de su servidor pueden personalizar su experiencia visual cuando lo deseen.
