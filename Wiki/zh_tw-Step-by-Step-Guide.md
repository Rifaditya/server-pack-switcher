# 逐步指南

歡迎！本指南專為新手編寫，即使您從未管理過伺服器或設定過模組，也能輕鬆搞定！

---

## 🎮 這個模組有什麼作用？
一般情況下，Minecraft 伺服器只能設定一個強制所有人下載的材質包。

透過本模組，**玩家可以透過指令自由選擇喜歡的外觀！**
例如，玩家可以輸入 `/pack plastic` 讓方塊呈現塑膠質感，或輸入 `/pack pop` 恢復原版預設外觀。

以下是只需 **5 個簡易步驟** 的設定流程！

---

## 📅 第 1 步：取得資源包下載連結
為了向玩家發送資源包，伺服器需要知道從何處下載 `.zip` 檔案。

### 方案 A：使用 Modrinth（最簡單且推薦！）
使用 Modrinth 不需要尋找直接下載 CDN！您只需複製 Modrinth 專案網址、slug 或版本連結即可：
- **專案 URL**：`https://modrinth.com/resourcepack/glass-doors`
- **Slug**：`glass-doors`
- **版本 URL**：`https://modrinth.com/resourcepack/glass-doors/version/1.2.3`

### 方案 B：使用 Discord
1. 將資源包 `.zip` 檔案上傳到任意 Discord 頻道。
2. 上傳完成後，**右鍵點擊** 檔案的下載箭頭/圖示。
3. 點選 **複製連結**。
4. 複製的連結格式如下：`https://cdn.discordapp.com/attachments/.../pack.zip`（這就是您的直接下載 URL！）。

### 方案 C：使用 Dropbox
1. 將 `.zip` 檔案上傳到 Dropbox。
2. 點擊 **分享** 按鈕並複製連結。
3. 連結格式如下：`https://www.dropbox.com/s/.../pack.zip?dl=0`。
4. 將末尾的 `?dl=0` 改為 `?raw=1`。
5. 修改後的連結格式為：`https://www.dropbox.com/s/.../pack.zip?raw=1`（這就是您的直接下載 URL！）。

---

## 🔑 第 2 步：取得材質包「數位指紋」（SHA-1 雜湊）
伺服器需要一段 40 位字元代碼（稱為 **SHA-1 雜湊**）來告知玩家客戶端：*「你已經下載過這個材質包了，不需要重新下載！」*

### 方案 A：使用 Modrinth（免手動計算雜湊！）
若您在第 1 步中選擇了 **方案 A（Modrinth）**，**您可以跳過此步驟！** 伺服器會在啟動時自動向 Modrinth API 查詢正確的雜湊值。直接前往第 3 步即可。

### 方案 B：使用線上工具
1. 開啟 [https://emn178.github.io/online-tools/sha1_checksum.html](https://emn178.github.io/online-tools/sha1_checksum.html) 或搜尋「online SHA1 hash calculator」。
2. 將材質包 `.zip` 檔案拖入網頁中的方框。
3. 網頁會立即顯示一串 40 位的字元（例如：`da39a3ee5e6b4b0d3255bfef95601890afd80709`）。
4. **複製**該代碼！

### 方案 C：使用 Windows PowerShell（無需連網）
1. 按鍵盤上的 **Windows 鍵**，輸入 **PowerShell** 並按 **Enter**。
2. 輸入 `Get-FileHash -Algorithm SHA1 `（請注意末尾有一個空格）。
3. 將材質包 `.zip` 檔案直接拖曳到 PowerShell 視窗中，路徑會自動填入。
4. 按 **Enter** 執行。
5. 複製 **Hash** 欄位下顯示的 40 位字串。

---

## 📝 第 3 步：將 URL/Modrinth 與雜湊填入設定檔案
在安裝了本模組的 Minecraft 伺服器啟動後，會自動產生設定檔案。

1. 進入伺服器根目錄。
2. 開啟 **`config`** 資料夾。
3. 找到名為 **`serverpackswitcher.json`** 的檔案。
4. 右鍵點擊該檔案，選擇 **開啟檔案**，然後選擇 **記事本**。
5. 您可以使用 **Modrinth 連結** 或 **直接 URL** 進行設定：

### 若使用 Modrinth：
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
*(無需填寫 `url` 或 `hash`！伺服器啟動時會自動自 Modrinth 取得)*

### 若使用直連 URL (Discord/Dropbox)：
```json
{
  "packs": {
    "plastic": {
      "url": "在此貼上您的DISCORD或DROPBOX直連",
      "hash": "在此貼上40位SHA1雜湊代碼",
      "required": false,
      "prompt": "Would you like to use the Plastic look?"
    }
  }
}
```

6. 儲存檔案（快捷鍵 **Ctrl + S**）並關閉記事本。

---

## 🔄 第 4 步：熱重載設定
若您的伺服器已在運行中，您無需重新啟動伺服器！
1. 加入您的 Minecraft 伺服器。
2. 確保您具有管理員權限（擁有 `/op` 權限）。
3. 按 **T** 或 **/** 開啟聊天欄。
4. 輸入 **`/pack reload`** 並按 **Enter**。
5. 聊天欄會顯示材質包載入成功的提示訊息！

---

## 🧱 第 5 步：在 Minecraft 中使用指令

伺服器上的所有玩家都可以使用下列指令：

### 1. 檢視所有可用材質包
輸入：**`/pack list`**  
*將列出在第 3 步中設定的所有材質包名稱（例如 "glassdoors" 或 "plastic"）。*

### 2. 為自己套用材質包
輸入：**`/pack glassdoors`**（或 `/pack apply glassdoors`）  
*客戶端將跳出確認視窗詢問是否下載並安裝材質包，點擊 **是** 即可！*

### 3. 移除目前材質包
輸入：**`/pack pop`**（或 `/pack clear`）  
*遊戲將立即恢復為預設外觀。*

### 4. 管理員指令：為其他玩家推送材質包
若您是管理員，可以將材質包推送給其他玩家：  
輸入：**`/pack apply glassdoors Bob`**（將 Bob 替換為目標玩家名稱，或使用 `@a` 代表所有線上玩家）。

---

設定大功告成！現在您的玩家可以隨時隨地自由打造專屬的遊戲視覺體驗了！
