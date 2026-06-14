# Step-by-Step Guide

Welcome! This guide is written to be **super easy** to follow. Even if you have never run a server or set up a mod before, you can do this!

---

## 🎮 What does this mod do?
Imagine you are playing on a server. Normally, a server can only have one look (resource pack) that everyone is forced to download. 

With this mod, **players can choose their own looks using commands!** 
For example, a player can type `/pack plastic` to make their blocks look like plastic, or `/pack pop` to go back to normal.

Here is how to set it up in **5 easy steps**!

---

## 📅 Step 1: Get your Resource Pack URL (Direct Link)
To send a resource pack to a player, the server needs to know where to download the `.zip` file from.

### Option A: Using Modrinth (Easiest & Recommended!)
You don't need direct file links or CDN URLs for Modrinth! You can simply use a Modrinth project URL, a project slug, or a version URL:
- **Project URL**: `https://modrinth.com/resourcepack/glass-doors`
- **Slug**: `glass-doors`
- **Version URL**: `https://modrinth.com/resourcepack/glass-doors/version/1.2.3`

### Option B: Using Discord
1. Upload your resource pack `.zip` file to any Discord chat.
2. Once uploaded, **Right-click** on the download arrow/icon of the file.
3. Click **Copy Link**.
4. The copied link will look like this: `https://cdn.discordapp.com/attachments/.../pack.zip` (This is your direct URL!).

### Option C: Using Dropbox
1. Upload your `.zip` file to Dropbox.
2. Click the **Share** button and copy the link.
3. The link will look like this: `https://www.dropbox.com/s/.../pack.zip?dl=0`
4. Change the very end of the link from `?dl=0` to `?raw=1`. 
5. Your new link is: `https://www.dropbox.com/s/.../pack.zip?raw=1` (This is your direct URL!).

---

## 🔑 Step 2: Get the "Fingerprint" of your Pack (SHA-1 Hash)
The server needs a code (called a **SHA-1 Hash**) to tell the player's computer: *"Hey, you already downloaded this pack, you don't need to download it again!"* This code is 40 letters/numbers long.

### Option A: Using Modrinth (No Hash Needed!)
If you chose **Option A** (Modrinth) in Step 1, **you do not need to do this!** The server will automatically query Modrinth for the correct hash code for you. Go straight to Step 3.

### Option B: Using a Website
1. Go to [https://emn178.github.io/online-tools/sha1_checksum.html](https://emn178.github.io/online-tools/sha1_checksum.html) or search online for "online SHA1 hash calculator".
2. Drag and drop your resource pack `.zip` file into the box on the website.
3. It will instantly show you a long line of letters and numbers (for example: `da39a3ee5e6b4b0d3255bfef95601890afd80709`).
4. **Copy** that code!

### Option C: Using Windows PowerShell (No Internet Needed)
1. Press the **Windows Key** on your keyboard, type **PowerShell**, and press **Enter**.
2. Type `Get-FileHash -Algorithm SHA1 ` (make sure there is a space at the end).
3. Drag your resource pack `.zip` file from your folder and drop it into the PowerShell window. It will automatically type the path for you.
4. Press **Enter**.
5. Copy the long code listed under the **Hash** column.

---

## 📝 Step 3: Put the URL/Modrinth and Hash in the Config File
Once you start your Minecraft server with this mod installed, a configuration file is created.

1. Go to your Minecraft server folder.
2. Open the **`config`** folder.
3. Find a file named **`serverpackswitcher.json`**.
4. Right-click the file, select **Open with**, and choose **Notepad**.
5. You can configure it using either **Direct URLs** or **Modrinth links**:

### If using Modrinth:
```json
{
  "packs": {
    "glassdoors": {
      "modrinth": "https://modrinth.com/resourcepack/glass-doors",
      "required": false,
      "prompt": "Would you like to use the Glass Doors look?"
    }
  }
}
```
*(No need to specify `url` or `hash`! The server will download the latest version automatically at startup)*

### If using Direct URLs (Discord/Dropbox):
```json
{
  "packs": {
    "plastic": {
      "url": "INSERT_YOUR_DISCORD_OR_DROPBOX_LINK_HERE",
      "hash": "INSERT_YOUR_40_CHARACTER_CODE_HERE",
      "required": false,
      "prompt": "Would you like to use the Plastic look?"
    }
  }
}
```

6. Save the file (Press **Ctrl + S**) and close Notepad.

---

## 🔄 Step 4: Reload the Config
If your server is already running, you don't need to restart it!
1. Join your Minecraft server.
2. Make sure you are an administrator (have operator `/op` permission).
3. Press **T** or **/** to open the chat.
4. Type **`/pack reload`** and press **Enter**.
5. You should see a message saying the packs loaded successfully!

---

## 🧱 Step 5: How to Use the Commands in Minecraft

Anyone on the server can use these commands in the game chat:

### 1. View all available packs
Type: **`/pack list`**
*This will list all the names of packs you configured in Step 3 (like "glassdoors" or "plastic").*

### 2. Apply a pack to yourself
Type: **`/pack glassdoors`** (or `/pack apply glassdoors`)
*A screen will pop up asking if you want to download and install the pack. Click **Yes**!*

### 3. Remove your active pack
Type: **`/pack pop`** (or `/pack clear`)
*Your game will instantly go back to looking normal.*

### 4. Admin Command: Give a pack to another player
If you are an administrator, you can send a pack to other players:
Type: **`/pack apply glassdoors Bob`** (replaces Bob with the player's name, or `@a` for everyone).

---

And that's it! Your players can now customize how their game looks whenever they want!
