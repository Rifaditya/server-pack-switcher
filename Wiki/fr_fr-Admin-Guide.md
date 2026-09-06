# Guide administrateur

Ce guide détaille les fonctionnalités d'administration, le rechargement à chaud et la persistance des préférences des joueurs.

---

## 💾 Persistance des préférences

Lorsqu'un joueur sélectionne un pack avec la commande `/pack <name>`, le serveur enregistre son choix dans `config/serverpackswitcher_preferences.json`.
- **Réapplication automatique** : Lors de sa prochaine connexion, le serveur relit automatiquement sa préférence et lui envoie son pack.
- **Effacement des préférences** : Si un joueur tape `/pack pop` ou `/pack clear`, sa préférence est supprimée et aucun pack ne lui sera envoyé lors de sa prochaine connexion.

---

## 🔄 Rechargement de la configuration à chaud

Si vous modifiez le fichier `config/serverpackswitcher.json` pendant que le serveur tourne, nul besoin de redémarrer !
1. Assurez-vous d'avoir les permissions d'administrateur (opérateur niveau 2+).
2. Tapez **`/pack reload`** dans le chat ou dans la console du serveur.
3. Le serveur recharge instantanément la configuration. Les packs liés à Modrinth sont résolus de façon asynchrone en arrière-plan.

---

## 🛡️ Commandes administratives ciblées

Les administrateurs (opérateurs de niveau 2+) peuvent appliquer ou retirer un pack à d'autres joueurs.

### Forcer l'application d'un pack
- `/pack apply plastic Bob` - Force l'envoi du pack `plastic` au joueur `Bob`.
- `/pack apply plastic @a` - Force l'envoi du pack `plastic` à tous les joueurs en ligne.

### Forcer le retrait d'un pack
- `/pack pop Bob` - Retire le pack actif du joueur `Bob`.
- `/pack pop @a` - Retire le pack actif de tous les joueurs en ligne.
