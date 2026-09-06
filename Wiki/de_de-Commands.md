# Befehlsübersicht

Alle Befehle beginnen mit dem Präfix `/pack`.

## Befehlsliste

| Befehl | Berechtigung | Beschreibung |
| :--- | :--- | :--- |
| `/pack list` | Jeder | Zeigt eine Liste aller konfigurierten Server-Ressourcenpakete an. |
| `/pack <pack_name>` | Jeder | Lädt das angegebene Ressourcenpaket herunter und wendet es auf den Spieler an. |
| `/pack apply <pack_name>` | Jeder | Alias für `/pack <pack_name>`. |
| `/pack pop` | Jeder | Entfernt das aktive Serverpaket vom Client des Spielers. |
| `/pack clear` | Jeder | Alias für `/pack pop`. |
| `/pack apply <pack_name> <targets>` | Spielleiter (Stufe 2+) | Weist das angegebene Paket den Zielspielern verbindlich zu. |
| `/pack pop <targets>` | Spielleiter (Stufe 2+) | Entfernt das aktive Serverpaket verbindlich von den Zielspielern. |
| `/pack reload` | Spielleiter (Stufe 2+) | Lädt die Konfigurationsdatei neu von der Festplatte. |

*Hinweis: Befehlsargumente unterstützen Tab-Vervollständigung (TAB) für alle eingerichteten Paketnamen.*

---

## Befehlsdetails

### `/pack list`
Zeigt die Namen aller eingerichteten Ressourcenpakete, die Spielern zur Verfügung stehen.

### `/pack <pack_name>`
Lädt das mit `<pack_name>` übereinstimmende Paket herunter und wendet es an. Falls bereits ein Paket über diese Mod aktiv war, wird es zuvor sauber entfernt.

### `/pack pop` / `/pack clear`
Entfernt das aktive Server-Ressourcenpaket und stellt das Standardaussehen des Spiels wieder her.
