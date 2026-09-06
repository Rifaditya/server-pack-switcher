# Configuration du serveur

Le mod génère son fichier de configuration à l'emplacement `config/serverpackswitcher.json` dans le dossier de votre serveur. Vous pouvez configurer vos packs avec des **liens directs** ou des **liens Modrinth**.

---

## 🔧 Modèle JSON
Exemple de configuration combinant Modrinth et des liens de téléchargement directs :

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

## 📋 Champs de configuration
Pour chaque entrée sous `"packs"`, vous pouvez renseigner :

* **`modrinth`** (Chaîne optionnelle) : URL de projet Modrinth (ex. `https://modrinth.com/resourcepack/glass-doors`), slug de projet (ex. `glass-doors`) ou URL de version spécifique. Si renseigné, le serveur récupère automatiquement le lien direct et le hash SHA-1 de façon asynchrone via l'API Modrinth au démarrage.
* **`url`** (Requis si `modrinth` est omis) : Lien de téléchargement direct vers le fichier `.zip` du pack.
* **`hash`** (Chaîne optionnelle) : Hash SHA-1 du fichier zip (40 caractères hexadécimaux). Recommandé avec les liens directs pour permettre la mise en cache côté client.
* **`required`** (Booléen optionnel, par défaut : `false`) : Si `true`, le client reçoit une boîte de dialogue imposant l'acceptation du pack.
* **`prompt`** (Chaîne optionnelle) : Message affiché à l'écran du joueur lors de la demande de téléchargement.
* **`id`** (UUID optionnel) : Identifiant unique du pack. Généré automatiquement s'il est omis.

---

## 🔍 Trouver les URLs et générer les hashs (liens directs)

Si vous n'utilisez pas Modrinth et souhaitez configurer des liens directs :

### 1. Obtenir une URL de téléchargement direct
L'URL doit lancer le téléchargement direct du fichier `.zip` dès qu'on clique dessus, sans ouvrir de page intermédiaire.
- **Discord** : Déposez le fichier `.zip` dans un salon, faites un clic droit sur la flèche de téléchargement et cliquez sur **Copier le lien**.
- **Dropbox** : Copiez le lien de partage et remplacez `?dl=0` à la fin par `?raw=1`.
- **GitHub** : Utilisez le lien de téléchargement direct d'une Release.

### 2. Calculer le hash SHA-1
Le hash est une empreinte unique de 40 caractères permettant au jeu de garder le pack en cache :
- **Windows (PowerShell)** :
  ```powershell
  Get-FileHash -Algorithm SHA1 .\my_resource_pack.zip
  ```
- **macOS / Linux (Terminal)** :
  ```bash
  sha1sum my_resource_pack.zip
  ```
- **Outils en ligne** : Glissez-déposez le fichier `.zip` dans un calculateur de somme SHA-1 en ligne.
