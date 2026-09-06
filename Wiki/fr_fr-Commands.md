# Référence des commandes

Toutes les commandes commencent par le préfixe `/pack`.

## Liste des commandes

| Commande | Permissions requises | Description |
| :--- | :--- | :--- |
| `/pack list` | Tout le monde | Affiche la liste de tous les packs de ressources configurés. |
| `/pack <pack_name>` | Tout le monde | Télécharge et applique le pack de ressources spécifié au joueur. |
| `/pack apply <pack_name>` | Tout le monde | Alias de `/pack <pack_name>`. |
| `/pack pop` | Tout le monde | Retire le pack serveur actif sur le client du joueur. |
| `/pack clear` | Tout le monde | Alias de `/pack pop`. |
| `/pack apply <pack_name> <targets>` | Opérateur (Niveau 2+) | Force l'application du pack spécifié aux joueurs ciblés. |
| `/pack pop <targets>` | Opérateur (Niveau 2+) | Force le retrait du pack serveur actif sur les joueurs ciblés. |
| `/pack reload` | Opérateur (Niveau 2+) | Recharge le fichier de configuration depuis le disque. |

*Remarque : Les arguments de commande prennent en charge l'autocomplétion (touche TAB) pour tous les noms de packs configurés.*

---

## Détails des commandes

### `/pack list`
Affiche les noms de tous les packs configurés sur le serveur et disponibles pour les joueurs.

### `/pack <pack_name>`
Télécharge et applique le pack correspondant à `<pack_name>`. Si un autre pack de ce mod était déjà actif, il est automatiquement retiré pour assurer une transition propre.

### `/pack pop` / `/pack clear`
Retire le pack de ressources du serveur actif et rétablit l'apparence vanilla par défaut.
