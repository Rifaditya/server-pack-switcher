# Konfigurasi Server

Mod ini membuat file konfigurasinya di `config/serverpackswitcher.json` di dalam folder server Anda. Anda dapat mengonfigurasi pack menggunakan **URL Langsung** atau **tautan Modrinth**.

---

## 🔧 Templat JSON
Berikut adalah contoh templat konfigurasi yang menunjukkan penggunaan Modrinth dan URL langsung:

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

## 📋 Kolom Konfigurasi
Untuk setiap entri di bawah `"packs"`, Anda dapat menentukan:

* **`modrinth`** (String Opsional): URL Modrinth (misal `https://modrinth.com/resourcepack/glass-doors`), slug proyek (misal `glass-doors`), atau URL versi tertentu. Jika ditentukan, server akan secara otomatis dan asinkron mengambil tautan unduhan langsung serta hash SHA-1 dari API Modrinth saat startup.
* **`url`** (Wajib jika `modrinth` dihilangkan): Tautan unduhan langsung ke file `.zip` resource pack.
* **`hash`** (String Opsional): Hash SHA-1 dari file zip (40 karakter heksadesimal). Sangat disarankan jika tidak menggunakan Modrinth agar klien dapat menyimpan cache pack.
* **`required`** (Boolean Opsional, Default: `false`): Jika `true`, klien akan diberi dialog wajib untuk menerima pack.
* **`prompt`** (String Opsional): Pesan kustom yang ditampilkan di layar klien saat meminta untuk mengunduh pack.
* **`id`** (String UUID Opsional): Pengidentifikasi unik untuk pack. Jika dihilangkan, akan dibuat secara otomatis.

---

## 🔍 Cara Mendapatkan URL & Menghasilkan Hash (Untuk Tautan Langsung)

Jika Anda tidak menggunakan Modrinth dan ingin mengonfigurasi tautan langsung:

### 1. Mendapatkan URL Unduhan Langsung
URL harus langsung mengunduh file `.zip` saat diklik, tanpa memuat halaman web perantara.
- **Discord**: Unggah file `.zip` ke saluran Discord, klik kanan pada ikon panah unduhan, lalu klik **Copy Link**.
- **Dropbox**: Salin tautan bagikan dan ubah bagian akhir dari `?dl=0` menjadi `?raw=1`.
- **GitHub**: Gunakan tautan unduhan langsung aset rilis (Release Asset).

### 2. Menghasilkan Hash SHA-1
Hash adalah sidik jari unik 40 karakter yang memungkinkan Minecraft menyimpan pack dalam cache agar tidak perlu mengunduh ulang:
- **Windows (PowerShell)**:
  ```powershell
  Get-FileHash -Algorithm SHA1 .\my_resource_pack.zip
  ```
- **macOS / Linux (Terminal)**:
  ```bash
  sha1sum my_resource_pack.zip
  ```
- **Alat Online**: Seret dan lepas file `.zip` Anda ke kalkulator checksum SHA-1 online.
