# Configuración del servidor

El mod genera su archivo de configuración en `config/serverpackswitcher.json` dentro de la carpeta del servidor. Puede configurar paquetes usando **enlaces directos** o **enlaces de Modrinth**.

---

## 🔧 Plantilla JSON
Ejemplo de configuración que ilustra la integración con Modrinth y enlaces directos:

```json
{
  "packs": {
    "glassdoors": {
      "modrinth": "https://modrinth.com/resourcepack/glass-doors",
      "required": false,
      "prompt": "Would you like to apply the Glass Doors resource pack?"
    },
    "freshanimations": {
      "modrinth": "https://modrinth.com/resourcepack/fresh-animations",
      "required": false,
      "prompt": "Would you like to apply the Fresh Animations resource pack?"
    },
    "plastic": {
      "url": "https://example.com/plastic_pack.zip",
      "hash": "da39a3ee5e6b4b0d3255bfef95601890afd80709",
      "required": false,
      "prompt": "Would you like to use the Plastic look?"
    }
  }
}
```

---

## 📋 Parámetros de configuración
Para cada elemento registrado en `"packs"` es posible definir:

* **`modrinth`** (Cadena opcional): URL de Modrinth (ej. `https://modrinth.com/resourcepack/glass-doors`), slug del proyecto (ej. `glass-doors`) o enlace de versión específica. Si se especifica, el servidor resolverá el enlace de descarga directa y el hash SHA-1 de forma asíncrona mediante la API de Modrinth al iniciar.
* **`url`** (Obligatorio si se omite `modrinth`): Enlace de descarga directa del archivo `.zip` del paquete de recursos.
* **`hash`** (Cadena opcional): Hash SHA-1 del archivo zip (40 caracteres hexadecimales). Recomendado si no se utiliza Modrinth para permitir el almacenamiento en caché del cliente.
* **`required`** (Booleano opcional, por defecto: `false`): Si es `true`, el cliente recibe un cuadro de diálogo obligatorio para aceptar el paquete.
* **`prompt`** (Cadena opcional): Mensaje mostrado en pantalla al solicitar la descarga del paquete.
* **`id`** (UUID opcional): Identificador único del paquete. Si se omite, se genera automáticamente.

---

## 🔍 Obtención de enlaces directos y hashes SHA-1

Si prefiere no utilizar Modrinth y configurar enlaces directos:

### 1. Obtener una URL de descarga directa
La URL debe descargar el archivo `.zip` inmediatamente al hacer clic, sin cargar páginas web intermedias.
- **Discord**: Suba el archivo a un canal, haga clic derecho en la flecha de descarga y pulse **Copiar enlace**.
- **Dropbox**: Copie el enlace para compartir y cambie el final de `?dl=0` a `?raw=1`.
- **GitHub**: Utilice el enlace de descarga directa de un asset en los Releases.

### 2. Generar el hash SHA-1
El hash es una huella única de 40 caracteres que permite a Minecraft guardar el paquete en caché:
- **Windows (PowerShell)**:
  ```powershell
  Get-FileHash -Algorithm SHA1 .\my_resource_pack.zip
  ```
- **macOS / Linux (Terminal)**:
  ```bash
  sha1sum my_resource_pack.zip
  ```
- **Herramientas en línea**: Arrastre el archivo `.zip` a un generador de sumas de verificación SHA-1 web.
