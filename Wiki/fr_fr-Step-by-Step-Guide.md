# Guide étape par étape

Bienvenue ! Ce guide a été conçu pour être **très accessible**, même si vous n'avez jamais géré de serveur Minecraft ni configuré de mod auparavant.

---

## 🎮 Que fait ce mod ?
Par défaut, un serveur Minecraft ne peut proposer qu'un seul pack de ressources que tous les joueurs sont obligés de télécharger.

Grâce à ce mod, **les joueurs peuvent choisir leur propre apparence avec des commandes !**
Par exemple, un joueur peut taper `/pack plastic` pour obtenir un style plastique, ou `/pack pop` pour revenir au style vanilla.

Voici comment le configurer en **5 étapes faciles** !

---

## 📅 Étape 1 : Obtenir le lien du pack de ressources
Le serveur doit savoir où télécharger le fichier `.zip`.

### Option A : Utiliser Modrinth (Le plus simple et recommandé !)
Avec Modrinth, aucun besoin de CDN direct ! Indiquez simplement l'URL du projet, son identifiant (slug) ou l'URL d'une version :
- **URL du projet** : `https://modrinth.com/resourcepack/glass-doors`
- **Slug** : `glass-doors`
- **URL de version** : `https://modrinth.com/resourcepack/glass-doors/version/1.2.3`

### Option B : Utiliser Discord
1. Envoyez votre fichier `.zip` dans un salon Discord.
2. Faites un **clic droit** sur l'icône de téléchargement du fichier.
3. Cliquez sur **Copier le lien**.
4. Le lien aura cette forme : `https://cdn.discordapp.com/attachments/.../pack.zip` (votre lien direct !).

### Option C : Utiliser Dropbox
1. Envoyez votre fichier `.zip` sur Dropbox.
2. Cliquez sur **Partager** et copiez le lien.
3. Le lien ressemble à : `https://www.dropbox.com/s/.../pack.zip?dl=0`.
4. Remplacez `?dl=0` à la toute fin par `?raw=1`.
5. Votre lien direct est prêt : `https://www.dropbox.com/s/.../pack.zip?raw=1`.

---

## 🔑 Étape 2 : Obtenir le hash SHA-1 du pack
Le serveur a besoin d'un code de 40 caractères (**hash SHA-1**) pour avertir le jeu : *« Tu possèdes déjà ce pack, inutile de le télécharger à nouveau ! »*

### Option A : Avec Modrinth (Aucun hash nécessaire !)
Si vous avez choisi l'**Option A (Modrinth)** à l'Étape 1, **passez cette étape !** Le serveur récupère le hash automatiquement depuis l'API Modrinth. Rendez-vous directement à l'Étape 3.

### Option B : Via un site internet
1. Allez sur [https://emn178.github.io/online-tools/sha1_checksum.html](https://emn178.github.io/online-tools/sha1_checksum.html).
2. Déposez votre fichier `.zip` dans la boîte sur le site.
3. Une suite de 40 caractères apparaît immédiatement (ex. : `da39a3ee5e6b4b0d3255bfef95601890afd80709`).
4. **Copiez** ce code !

### Option C : Avec Windows PowerShell (hors-ligne)
1. Appuyez sur la **touche Windows**, tapez **PowerShell** et appuyez sur **Entrée**.
2. Tapez `Get-FileHash -Algorithm SHA1 ` (avec un espace à la fin).
3. Glissez-déposez votre fichier `.zip` dans la fenêtre PowerShell.
4. Appuyez sur **Entrée**.
5. Copiez la valeur inscrite dans la colonne **Hash**.

---

## 📝 Étape 3 : Renseigner les liens et hashs dans la configuration
Après avoir lancé votre serveur avec le mod installé, le fichier de configuration est créé :

1. Ouvrez le dossier de votre serveur Minecraft.
2. Ouvrez le sous-dossier **`config`**.
3. Trouvez le fichier **`serverpackswitcher.json`**.
4. Clic droit sur le fichier, sélectionnez **Ouvrir avec**, puis choisissez le **Bloc-notes**.
5. Configurez selon votre méthode :

### Si vous utilisez Modrinth :
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
*(Inutile de spécifier `url` ou `hash` ! Le serveur récupère la dernière version au démarrage).*

### Si vous utilisez des liens directs (Discord/Dropbox) :
```json
{
  "packs": {
    "plastic": {
      "url": "COLLEZ_VOTRE_LIEN_DISCORD_OU_DROPBOX_ICI",
      "hash": "COLLEZ_LE_CODE_SHA1_DE_40_CARACTERES_ICI",
      "required": false,
      "prompt": "Would you like to use the Plastic look?"
    }
  }
}
```

6. Sauvegardez le fichier (**Ctrl + S**) et fermez l'éditeur.

---

## 🔄 Étape 4 : Recharger la configuration
Si votre serveur tourne déjà, vous n'avez pas besoin de le relancer :
1. Connectez-vous à votre serveur Minecraft.
2. Assurez-vous d'être administrateur (`/op`).
3. Ouvrez le chat avec **T** ou **/**.
4. Tapez **`/pack reload`** et appuyez sur **Entrée**.
5. Un message confirme que les packs ont été rechargés avec succès !

---

## 🧱 Étape 5 : Utiliser les commandes dans le jeu

Tout joueur peut utiliser ces commandes dans le chat :

### 1. Voir tous les packs disponibles
Tapez : **`/pack list`**  
*Affiche tous les noms de packs configurés à l'Étape 3.*

### 2. Appliquer un pack sur soi-même
Tapez : **`/pack glassdoors`** (ou `/pack apply glassdoors`)  
*Une fenêtre s'affiche pour confirmer le téléchargement du pack. Cliquez sur **Oui** !*

### 3. Retirer son pack actif
Tapez : **`/pack pop`** (ou `/pack clear`)  
*Votre jeu redevient instantanément normal.*

### 4. Commande d'admin : Donner un pack à un autre joueur
Les administrateurs peuvent forcer l'application d'un pack :  
Tapez : **`/pack apply glassdoors Bob`** (remplacez Bob par le pseudo du joueur, ou `@a` pour tous les joueurs).

---

Et voilà ! Vos joueurs peuvent désormais changer d'apparence à tout moment et selon leurs envies !
