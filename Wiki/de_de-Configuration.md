# Server-Konfiguration

Die Mod generiert ihre Konfigurationsdatei unter `config/serverpackswitcher.json` im Serververzeichnis. Pakete können über **Direkt-URLs** oder **Modrinth-Links** eingerichtet werden.

---

## 🔧 JSON-Vorlage
Hier ist ein Konfigurationsbeispiel, das sowohl Modrinth- als auch Direkt-URL-Einträge zeigt:

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

## 📋 Konfigurationsfelder
Für jeden Paketeintrag unter `"packs"` kann definiert werden:

* **`modrinth`** (Optionaler String): Modrinth-URL (z. B. `https://modrinth.com/resourcepack/glass-doors`), Projekt-Slug (z. B. `glass-doors`) oder Versions-URL. Wenn angegeben, fragt der Server beim Start im Hintergrund automatisch den Download-Link und den SHA-1-Hash über die Modrinth-API ab.
* **`url`** (Erforderlich, falls `modrinth` weggelassen wird): Direkter Download-Link zur `.zip`-Datei des Pakets.
* **`hash`** (Optionaler String): SHA-1-Hash der ZIP-Datei (40 Hexadezimalzeichen). Ohne Modrinth dringend empfohlen, um clientseitiges Caching zu aktivieren.
* **`required`** (Optionaler Boolean, Standard: `false`): Wenn `true`, wird dem Client ein verpflichtender Dialog zum Akzeptieren angezeigt.
* **`prompt`** (Optionaler String): Ein individueller Hinweistext, der beim Download auf dem Bildschirm des Spielers erscheint.
* **`id`** (Optionale UUID): Eine eindeutige Kennung des Pakets. Wird bei Auslassung automatisch generiert.

---

## 🔍 URLs finden & Hashes generieren (für Direkt-Links)

Falls Sie Modrinth nicht nutzen und direkte Links einrichten möchten:

### 1. Einen direkten Download-Link erhalten
Die URL muss beim Aufrufen sofort den Download der `.zip`-Datei starten, ohne eine Webseite zu öffnen.
- **Discord**: Laden Sie das Paket in einen Kanal hoch, machen Sie einen Rechtsklick auf das Download-Symbol und wählen Sie **Link kopieren**.
- **Dropbox**: Kopieren Sie den Freigabelink und ändern Sie das Ende von `?dl=0` in `?raw=1` um.
- **GitHub**: Nutzen Sie den direkten Asset-Download-Link eines Releases.

### 2. Den SHA-1-Hash berechnen
Der Hash ist ein 40-stelliger Fingerabdruck, der Minecraft mitteilt, dass das Paket im lokalen Cache gespeichert werden kann:
- **Windows (PowerShell)**:
  ```powershell
  Get-FileHash -Algorithm SHA1 .\my_resource_pack.zip
  ```
- **macOS / Linux (Terminal)**:
  ```bash
  sha1sum my_resource_pack.zip
  ```
- **Online-Tools**: Ziehen Sie Ihre `.zip`-Datei per Drag & Drop in einen Online-SHA-1-Rechner.
