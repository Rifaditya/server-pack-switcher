# Administrator-Handbuch

Dieses Handbuch beschreibt Verwaltungsfunktionen, das Neuladen im laufenden Betrieb und die dauerhafte Speicherung von Spielereinstellungen.

---

## 💾 Dauerhafte Einstellungen

Wählt ein Spieler ein Paket über `/pack <name>` aus, speichert der Server diese Auswahl automatisch in `config/serverpackswitcher_preferences.json`.
- **Automatisches Wiederanwenden**: Betritt der Spieler den Server später erneut, liest der Server die Einstellung aus und sendet das Paket automatisch.
- **Präferenz löschen**: Führt ein Spieler `/pack pop` oder `/pack clear` aus, wird seine Auswahl gelöscht und er erhält beim nächsten Login kein Paket.

---

## 🔄 Konfiguration im laufenden Betrieb neu laden

Wenn Sie die Datei `config/serverpackswitcher.json` bearbeiten, müssen Sie den Server nicht neu starten!
1. Stellen Sie sicher, dass Sie über Operator-Rechte verfügen (Berechtigungsstufe 2+).
2. Geben Sie im Spiel-Chat oder in der Serverkonsole **`/pack reload`** ein.
3. Der Server aktualisiert die Einstellungen sofort. Mit Modrinth verknüpfte Pakete werden asynchron im Hintergrund neu aufgelöst.

---

## 🛡️ Gezielte Administrator-Befehle

Administratoren (Operatoren mit Berechtigungsstufe 2+) können Pakete auf andere Spieler erzwingen oder entfernen.

### Zuweisung eines Pakets erzwingen
- `/pack apply plastic Bob` - Weist Spieler `Bob` das Paket `plastic` zu.
- `/pack apply plastic @a` - Weist allen anwesenden Spielern das Paket `plastic` zu.

### Entfernung eines Pakets erzwingen
- `/pack pop Bob` - Entfernt das aktive Serverpaket von Spieler `Bob`.
- `/pack pop @a` - Entfernt die aktiven Serverpakete von allen Online-Spielern.
