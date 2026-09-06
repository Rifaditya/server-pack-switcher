# Schritt-für-Schritt-Anleitung

Herzlich willkommen! Diese Anleitung ist besonders verständlich gestaltet, sodass auch Einsteiger die Mod mühelos einrichten können.

---

## 🎮 Was macht diese Mod?
Standardmäßig kann ein Minecraft-Server nur ein einziges Ressourcenpaket bereitstellen, das alle Spieler zwingend herunterladen müssen.

Mit dieser Mod **können Spieler ihr Aussehen eigenständig per Chat-Befehl wählen!**
Beispielsweise kann ein Spieler `/pack plastic` eingeben, um Texturen im Plastik-Look zu sehen, oder mit `/pack pop` zum Standard-Look zurückkehren.

So gelingt die Einrichtung in **5 einfachen Schritten**!

---

## 📅 Schritt 1: Download-Link für das Ressourcenpaket beschaffen
Der Server muss wissen, von welcher Webadresse die `.zip`-Datei heruntergeladen werden kann.

### Option A: Modrinth nutzen (am einfachsten & empfohlen!)
Für Modrinth benötigen Sie keine direkten CDN-Links! Kopieren Sie einfach die Projekt-URL, den Slug oder den Versions-Link:
- **Projekt-URL**: `https://modrinth.com/resourcepack/glass-doors`
- **Slug**: `glass-doors`
- **Versions-URL**: `https://modrinth.com/resourcepack/glass-doors/version/1.2.3`

### Option B: Discord nutzen
1. Laden Sie Ihre `.zip`-Datei in einen beliebigen Discord-Kanal hoch.
2. Klicken Sie mit der **rechten Maustaste** auf das Download-Symbol der Datei.
3. Wählen Sie **Link kopieren**.
4. Der kopierte Link sieht so aus: `https://cdn.discordapp.com/attachments/.../pack.zip` (Ihr Direkt-Link!).

### Option C: Dropbox nutzen
1. Laden Sie Ihre `.zip`-Datei auf Dropbox hoch.
2. Klicken Sie auf **Freigeben** und kopieren Sie den Link.
3. Der Link hat das Format: `https://www.dropbox.com/s/.../pack.zip?dl=0`.
4. Ändern Sie am Ende des Links `?dl=0` in `?raw=1` um.
5. Der fertige Direkt-Link lautet: `https://www.dropbox.com/s/.../pack.zip?raw=1`.

---

## 🔑 Schritt 2: Den SHA-1-Hash ermitteln
Der Server benötigt einen 40-stelligen Code (**SHA-1-Hash**), damit das Spiel des Spielers weiß: *„Dieses Paket ist bereits heruntergeladen und muss nicht erneut geladen werden!“*

### Option A: Modrinth (kein Hash nötig!)
Wenn Sie in Schritt 1 die **Option A (Modrinth)** gewählt haben, **können Sie diesen Schritt überspringen!** Der Server fragt den Hash automatisch per Modrinth-API ab. Gehen Sie direkt zu Schritt 3 über.

### Option B: Über eine Webseite
1. Öffnen Sie [https://emn178.github.io/online-tools/sha1_checksum.html](https://emn178.github.io/online-tools/sha1_checksum.html).
2. Ziehen Sie Ihre `.zip`-Datei per Drag & Drop in das Feld auf der Seite.
3. Die Seite generiert sofort den 40-stelligen Code (z. B. `da39a3ee5e6b4b0d3255bfef95601890afd80709`).
4. **Kopieren** Sie diesen Code!

### Option C: Mit Windows PowerShell (offline)
1. Drücken Sie die **Windows-Taste**, tippen Sie **PowerShell** ein und drücken Sie **Enter**.
2. Tippen Sie `Get-FileHash -Algorithm SHA1 ` ein (Leerstelle am Ende beachten).
3. Ziehen Sie die `.zip`-Datei per Drag & Drop in das PowerShell-Fenster.
4. Drücken Sie **Enter**.
5. Kopieren Sie den Code unter der Spalte **Hash**.

---

## 📝 Schritt 3: Einstellungen in die Konfigurationsdatei eintragen
Nach dem ersten Serverstart mit installierter Mod wird die Konfigurationsdatei angelegt:

1. Öffnen Sie Ihr Minecraft-Serververzeichnis.
2. Öffnen Sie den Ordner **`config`**.
3. Suchen Sie die Datei **`serverpackswitcher.json`**.
4. Öffnen Sie die Datei mit dem **Editor / Notepad**.
5. Tragen Sie Ihre Pakete ein:

### Mit Modrinth:
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
*(Die Felder `url` und `hash` sind nicht nötig! Der Server lädt die Daten automatisch).*

### Mit Direkt-Links (Discord/Dropbox):
```json
{
  "packs": {
    "plastic": {
      "url": "HIER_IHREN_DISCORD_ODER_DROPBOX_LINK_EINFUEGEN",
      "hash": "HIER_40_STELLIGEN_HASH_CODE_EINFUEGEN",
      "required": false,
      "prompt": "Would you like to use the Plastic look?"
    }
  }
}
```

6. Speichern Sie die Datei (**Strg + S**) und schließen Sie den Editor.

---

## 🔄 Schritt 4: Konfiguration im laufenden Betrieb neu laden
Läuft Ihr Server bereits, müssen Sie ihn nicht neu starten:
1. Betreten Sie Ihren Minecraft-Server.
2. Stellen Sie sicher, dass Sie Operator-Rechte haben (`/op`).
3. Öffnen Sie den Chat mit **T** oder **/**.
4. Geben Sie **`/pack reload`** ein und drücken Sie **Enter**.
5. Im Chat erscheint die Bestätigung, dass die Pakete erfolgreich neu geladen wurden!

---

## 🧱 Schritt 5: Befehle im Spiel verwenden

Jeder Spieler auf dem Server kann folgende Befehle nutzen:

### 1. Alle verfügbaren Pakete ansehen
Eingabe: **`/pack list`**  
*Listet alle konfigurierten Paketnamen auf.*

### 2. Ein Paket für sich selbst aktivieren
Eingabe: **`/pack glassdoors`** (oder `/pack apply glassdoors`)  
*Ein Bestätigungsfenster erscheint im Spiel. Klicken Sie auf **Ja**!*

### 3. Aktives Paket entfernen
Eingabe: **`/pack pop`** (oder `/pack clear`)  
*Das Spiel wechselt sofort zum Standardaussehen zurück.*

### 4. Admin-Befehl: Paket an andere Spieler senden
Administratoren können Pakete an andere Spieler senden:  
Eingabe: **`/pack apply glassdoors Bob`** (Bob durch den Spielernamen ersetzen, oder `@a` für alle).

---

Geschafft! Nun können Ihre Spieler ihr Aussehen jederzeit ganz nach Wunsch anpassen!
