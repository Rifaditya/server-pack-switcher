# 伺服器設定

模組會在伺服器目錄下的 `config/serverpackswitcher.json` 產生設定檔案。您可以使用**直連 URL**或 **Modrinth 連結**設定資源包。

---

## 🔧 JSON 範本
以下是同時展示 Modrinth 與直連 URL 設定的範例範本：

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

## 📋 設定欄位說明
對於 `"packs"` 下的每個資源包項目，您可以定義：

* **`modrinth`** (選填字串)：Modrinth URL（例如 `https://modrinth.com/resourcepack/glass-doors`）、專案識別碼 slug（例如 `glass-doors`）或特定版本 URL。若指定，伺服器將在啟動時於背景執行緒非同步透過 Modrinth API 自動取得直接下載連結與 SHA-1 雜湊值。
* **`url`** (若省略 `modrinth` 則為必填)：資源包 `.zip` 檔案的直接下載連結。
* **`hash`** (選填字串)：zip 檔案的 SHA-1 雜湊值（40 位十六進位字元）。若不使用 Modrinth，建議填寫此項以啟用客戶端快取。
* **`required`** (選填布林值，預設值：`false`)：若為 `true`，客戶端將彈出強制接受資源包的確認對話框。
* **`prompt`** (選填字串)：在提示下載資源包時顯示於客戶端畫面上的自訂訊息。
* **`id`** (選填 UUID 字串)：資源包的唯一識別碼。若省略，將自動產生。

---

## 🔍 如何取得直連與產生雜湊（直連方式）

若您不使用 Modrinth 而希望設定直接下載連結：

### 1. 取得直接下載 URL
URL 必須在點擊後立即開始下載 `.zip` 檔案，不能載入中間網頁。
- **Discord**：上傳材質包 `.zip` 檔案至頻道，右鍵點擊下載箭頭圖示，然後選擇**複製連結**。
- **Dropbox**：複製分享連結，並將連結末尾的 `?dl=0` 修改為 `?raw=1`。
- **GitHub**：使用 Release Assets 的直接檔案下載連結。

### 2. 產生 SHA-1 雜湊
雜湊是 40 個字元的唯一指紋，可讓 Minecraft 客戶端在本地快取材質包，避免重複下載。
- **Windows (PowerShell)**：
  ```powershell
  Get-FileHash -Algorithm SHA1 .\my_resource_pack.zip
  ```
- **macOS / Linux (終端機)**：
  ```bash
  sha1sum my_resource_pack.zip
  ```
- **線上工具**：將您的 `.zip` 檔案拖放至線上 SHA-1 總和檢查碼計算器中。
