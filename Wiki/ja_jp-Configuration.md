# サーバー設定

Modはサーバーディレクトリ内の `config/serverpackswitcher.json` に設定ファイルを生成します。パックの設定には **直リンクURL** または **Modrinthリンク** のいずれかを使用できます。

---

## 🔧 JSONテンプレート
Modrinthと直リンクURLの両方の設定例を示すテンプレートです：

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

## 📋 設定項目一覧
`"packs"` 配下の各パック設定項目：

* **`modrinth`** (任意の文字列): ModrinthのURL（例: `https://modrinth.com/resourcepack/glass-doors`）、プロジェクトスラッグ（例: `glass-doors`）、または特定バージョンのURL。指定すると、サーバー起動時にバックグラウンドスレッドでModrinth APIから直リンクとSHA-1ハッシュを非同期で自動取得します。
* **`url`** (`modrinth` を省略する場合は必須): リソースパック `.zip` ファイルへの直接ダウンロードリンク。
* **`hash`** (任意の文字列): zipファイルのSHA-1ハッシュ値（40桁の16進数）。Modrinthを使用しない場合、クライアント側でのキャッシュを有効にするために推奨されます。
* **`required`** (任意の真偽値、デフォルト: `false`): `true` の場合、クライアントにパックの承諾を強制するダイアログを表示します。
* **`prompt`** (任意の文字列): パックのダウンロード確認時に画面上に表示するカスタムメッセージ。
* **`id`** (任意のUUID文字列): パックの一意識別子。省略した場合は自動生成されます。

---

## 🔍 直リンクURLの取得とハッシュ生成方法

Modrinthを使用せず、直接URLを設定する場合の手順：

### 1. 直ダウンロードURLの取得
URLはクリックした瞬間にWebページを挟まず直接 `.zip` ファイルのダウンロードが開始される必要があります。
- **Discord**: チャンネルに `.zip` ファイルをアップロードし、ダウンロード矢印アイコンを右クリックして **「リンクをコピー」** を選択。
- **Dropbox**: 共有リンクをコピーし、末尾の `?dl=0` を `?raw=1` に書き換え。
- **GitHub**: リリースのアセットの直接ダウンロードリンクを使用。

### 2. SHA-1 ハッシュ値の生成
ハッシュ値は40文字の固有指紋コードで、Minecraftがパックをローカルにキャッシュして再ダウンロードを防ぐために使用されます：
- **Windows (PowerShell)**:
  ```powershell
  Get-FileHash -Algorithm SHA1 .\my_resource_pack.zip
  ```
- **macOS / Linux (ターミナル)**:
  ```bash
  sha1sum my_resource_pack.zip
  ```
- **オンラインツール**: Web上のオンラインSHA-1ハッシュ計算ツールに `.zip` ファイルをドラッグ＆ドロップ。
