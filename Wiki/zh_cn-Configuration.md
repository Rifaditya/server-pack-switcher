# 服务端配置

模组会在服务器目录下的 `config/serverpackswitcher.json` 生成配置文件。您可以使用**直链 URL**或 **Modrinth 链接**配置资源包。

---

## 🔧 JSON 模板
以下是同时展示 Modrinth 与直链 URL 配置的示例模板：

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

## 📋 配置字段说明
对于 `"packs"` 下的每个资源包条目，您可以定义：

* **`modrinth`** (可选字符串)：Modrinth URL（例如 `https://modrinth.com/resourcepack/glass-doors`）、项目标识符 slug（例如 `glass-doors`）或特定版本 URL。如果指定，服务器将在启动时在后台线程异步通过 Modrinth API 自动检索直接下载链接和 SHA-1 哈希值。
* **`url`** (如果省略 `modrinth` 则为必填)：资源包 `.zip` 文件的直接下载链接。
* **`hash`** (可选字符串)：zip 文件的 SHA-1 哈希值（40 位十六进制字符）。若不使用 Modrinth，推荐填写此项以启用客户端缓存。
* **`required`** (可选布尔值，默认值：`false`)：若为 `true`，客户端将弹出强制接受资源包的确认窗口。
* **`prompt`** (可选字符串)：在提示下载资源包时显示在客户端屏幕上的自定义消息。
* **`id`** (可选 UUID 字符串)：资源包的唯一标识符。如果省略，将自动生成。

---

## 🔍 如何获取直链与生成哈希（直链方式）

如果您不使用 Modrinth 而是希望配置直接下载链接：

### 1. 获取直接下载 URL
URL 必须在点击后立即开始下载 `.zip` 文件，不能加载中间网页。
- **Discord**：上传材质包 `.zip` 文件到频道，右键点击下载箭头图标，然后选择**复制链接**。
- **Dropbox**：复制分享链接，并将链接末尾的 `?dl=0` 修改为 `?raw=1`。
- **GitHub**：使用 Release Assets 的直接文件下载链接。

### 2. 生成 SHA-1 哈希
哈希是 40 个字符的唯一指纹，可让 Minecraft 客户端在本地缓存材质包，避免重复下载。
- **Windows (PowerShell)**：
  ```powershell
  Get-FileHash -Algorithm SHA1 .\my_resource_pack.zip
  ```
- **macOS / Linux (终端)**：
  ```bash
  sha1sum my_resource_pack.zip
  ```
- **在线工具**：将您的 `.zip` 文件拖放到在线 SHA-1 校验和计算器中。
