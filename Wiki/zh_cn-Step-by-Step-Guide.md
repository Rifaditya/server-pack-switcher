# 分步指南

欢迎！本指南专为新手设计，即使您从未管理过服务器或配置过模组，也可以轻松完成！

---

## 🎮 这个模组有什么作用？
通常情况下，Minecraft 服务器只能设置一个强制所有人下载的材质包。

借助本模组，**玩家可以通过指令自由选择喜欢的外观！**
例如，玩家可以输入 `/pack plastic` 让方块呈现塑料质感，或者输入 `/pack pop` 恢复原版默认外观。

以下是只需 **5 个简单步骤** 的配置流程！

---

## 📅 第 1 步：获取资源包下载链接
为了向玩家发送资源包，服务器需要知道从哪里下载 `.zip` 文件。

### 方案 A：使用 Modrinth（最简单且推荐！）
使用 Modrinth 不需要寻找直链 CDN！您只需复制 Modrinth 项目 URL、slug 或版本链接即可：
- **项目 URL**：`https://modrinth.com/resourcepack/glass-doors`
- **Slug**：`glass-doors`
- **版本 URL**：`https://modrinth.com/resourcepack/glass-doors/version/1.2.3`

### 方案 B：使用 Discord
1. 将资源包 `.zip` 文件上传到任意 Discord 频道。
2. 上传完成后，**右键点击** 文件的下载箭头/图标。
3. 点击 **复制链接**。
4. 复制的链接格式如下：`https://cdn.discordapp.com/attachments/.../pack.zip`（这就是您的直接下载 URL！）。

### 方案 C：使用 Dropbox
1. 将 `.zip` 文件上传到 Dropbox。
2. 点击 **分享** 按钮并复制链接。
3. 链接格式如下：`https://www.dropbox.com/s/.../pack.zip?dl=0`。
4. 将末尾的 `?dl=0` 改为 `?raw=1`。
5. 修改后的链接格式为：`https://www.dropbox.com/s/.../pack.zip?raw=1`（这就是您的直接下载 URL！）。

---

## 🔑 第 2 步：获取材质包“数字指纹”（SHA-1 哈希）
服务器需要一段 40 位代码（称为 **SHA-1 哈希**）来告知玩家客户端：*“你已经下载过这个材质包了，无需重新下载！”*

### 方案 A：使用 Modrinth（无需手动计算哈希！）
如果您在第 1 步中选择了 **方案 A（Modrinth）**，**您可以跳过这一步！** 服务器会在启动时自动查询 Modrinth API 获取正确的哈希值。直接进入第 3 步即可。

### 方案 B：使用在线工具
1. 打开 [https://emn178.github.io/online-tools/sha1_checksum.html](https://emn178.github.io/online-tools/sha1_checksum.html) 或搜索“online SHA1 hash calculator”。
2. 将材质包 `.zip` 文件拖入网页中的虚线框。
3. 网页会立即显示一串 40 位的字符（例如：`da39a3ee5e6b4b0d3255bfef95601890afd80709`）。
4. **复制**该代码！

### 方案 C：使用 Windows PowerShell（无需联网）
1. 按键盘上的 **Windows 键**，输入 **PowerShell** 并按 **回车**。
2. 输入 `Get-FileHash -Algorithm SHA1 `（请注意末尾有一个空格）。
3. 将材质包 `.zip` 文件直接拖拽到 PowerShell 窗口中，路径会自动填入。
4. 按 **回车** 执行。
5. 复制 **Hash** 列下显示的 40 位字符串。

---

## 📝 第 3 步：将 URL/Modrinth 与哈希填入配置文件
在安装了本模组的 Minecraft 服务器启动后，会自动生成配置文件。

1. 进入服务器根目录。
2. 打开 **`config`** 文件夹。
3. 找到名为 **`serverpackswitcher.json`** 的文件。
4. 右键点击该文件，选择 **打开方式**，然后选择 **记事本**。
5. 您可以使用 **Modrinth 链接** 或 **直接 URL** 进行配置：

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
*(无需填写 `url` 或 `hash`！服务器启动时会自动从 Modrinth 获取)*

### 若使用直链 URL (Discord/Dropbox)：
```json
{
  "packs": {
    "plastic": {
      "url": "在此粘贴您的DISCORD或DROPBOX直链",
      "hash": "在此粘贴40位SHA1哈希代码",
      "required": false,
      "prompt": "Would you like to use the Plastic look?"
    }
  }
}
```

6. 保存文件（快捷键 **Ctrl + S**）并关闭记事本。

---

## 🔄 第 4 步：热重载配置
如果您的服务器已经在运行，您无需重启服务器！
1. 加入您的 Minecraft 服务器。
2. 确保您具有管理员权限（拥有 `/op` 权限）。
3. 按 **T** 或 **/** 打开聊天栏。
4. 输入 **`/pack reload`** 并按 **回车**。
5. 聊天栏会显示材质包加载成功的提示信息！

---

## 🧱 第 5 步：在 Minecraft 中使用指令

服务器上的所有玩家都可以使用以下指令：

### 1. 查看所有可用材质包
输入：**`/pack list`**  
*将列出在第 3 步中配置的所有材质包名称（例如 "glassdoors" 或 "plastic"）。*

### 2. 为自己应用材质包
输入：**`/pack glassdoors`**（或 `/pack apply glassdoors`）  
*客户端将弹出确认窗口询问是否下载并安装材质包，点击 **是** 即可！*

### 3. 移除当前材质包
输入：**`/pack pop`**（或 `/pack clear`）  
*游戏将立即恢复为默认外观。*

### 4. 管理员指令：为其他玩家推送材质包
如果您是管理员，可以将材质包推送给其他玩家：  
输入：**`/pack apply glassdoors Bob`**（将 Bob 替换为目标玩家名，或使用 `@a` 代表所有在线玩家）。

---

配置大功告成！现在您的玩家可以随时随地自由定制他们的游戏视觉体验了！
