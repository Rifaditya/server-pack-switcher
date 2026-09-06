# Panduan Langkah demi Langkah

Selamat datang! Panduan ini ditulis agar **sangat mudah** diikuti, bahkan jika Anda belum pernah mengelola server Minecraft atau memasang mod sebelumnya!

---

## 🎮 Apa fungsi mod ini?
Biasanya, server Minecraft hanya dapat memiliki satu tampilan (resource pack) yang wajib diunduh oleh semua pemain.

Dengan mod ini, **pemain dapat memilih tampilan mereka sendiri menggunakan perintah!**
Misalnya, pemain dapat mengetik `/pack plastic` untuk membuat balok terlihat seperti plastik, atau `/pack pop` untuk kembali ke tampilan normal.

Berikut cara mengaturnya dalam **5 langkah mudah**!

---

## 📅 Langkah 1: Dapatkan URL Resource Pack Anda
Untuk mengirimkan resource pack ke pemain, server perlu mengetahui dari mana file `.zip` dapat diunduh.

### Pilihan A: Menggunakan Modrinth (Paling Mudah & Disarankan!)
Anda tidak memerlukan tautan file langsung atau CDN jika menggunakan Modrinth! Cukup gunakan URL proyek Modrinth, slug proyek, atau URL versi:
- **URL Proyek**: `https://modrinth.com/resourcepack/glass-doors`
- **Slug**: `glass-doors`
- **URL Versi**: `https://modrinth.com/resourcepack/glass-doors/version/1.2.3`

### Pilihan B: Menggunakan Discord
1. Unggah file `.zip` resource pack Anda ke ruang obrolan Discord mana saja.
2. Setelah terunggah, **Klik kanan** pada panah/ikon unduhan file tersebut.
3. Klik **Copy Link**.
4. Tautan yang disalin akan tampak seperti ini: `https://cdn.discordapp.com/attachments/.../pack.zip` (Ini adalah URL langsung Anda!).

### Pilihan C: Menggunakan Dropbox
1. Unggah file `.zip` Anda ke Dropbox.
2. Klik tombol **Share** dan salin tautannya.
3. Tautan akan tampak seperti: `https://www.dropbox.com/s/.../pack.zip?dl=0`.
4. Ubah bagian paling akhir tautan dari `?dl=0` menjadi `?raw=1`.
5. Tautan baru Anda: `https://www.dropbox.com/s/.../pack.zip?raw=1` (Ini adalah URL langsung Anda!).

---

## 🔑 Langkah 2: Dapatkan "Sidik Jari" Pack Anda (Hash SHA-1)
Server membutuhkan kode 40 digit (**Hash SHA-1**) untuk memberi tahu game pemain: *«Hei, kamu sudah pernah mengunduh pack ini, tidak perlu mengunduhnya lagi!»*

### Pilihan A: Menggunakan Modrinth (Tidak Perlu Hash!)
Jika Anda memilih **Pilihan A (Modrinth)** pada Langkah 1, **Anda tidak perlu melakukan langkah ini!** Server akan otomatis menanyakan kode hash ke Modrinth. Langsung lanjutkan ke Langkah 3.

### Pilihan B: Menggunakan Situs Web
1. Buka [https://emn178.github.io/online-tools/sha1_checksum.html](https://emn178.github.io/online-tools/sha1_checksum.html).
2. Seret dan lepas file `.zip` resource pack Anda ke dalam kotak di situs web tersebut.
3. Situs akan langsung menampilkan kode 40 karakter (contoh: `da39a3ee5e6b4b0d3255bfef95601890afd80709`).
4. **Salin** kode tersebut!

### Pilihan C: Menggunakan Windows PowerShell (Tanpa Internet)
1. Tekan tombol **Windows** pada keyboard, ketik **PowerShell**, dan tekan **Enter**.
2. Ketik `Get-FileHash -Algorithm SHA1 ` (pastikan ada spasi di ujungnya).
3. Seret file `.zip` resource pack Anda dari folder dan lepaskan ke dalam jendela PowerShell.
4. Tekan **Enter**.
5. Salin kode yang tercantum di bawah kolom **Hash**.

---

## 📝 Langkah 3: Masukkan URL dan Hash ke File Konfigurasi
Setelah Anda memulai server Minecraft dengan mod ini terpasang, file konfigurasi akan dibuat:

1. Buka folder server Minecraft Anda.
2. Buka folder **`config`**.
3. Temukan file bernama **`serverpackswitcher.json`**.
4. Klik kanan file tersebut, pilih **Open with**, lalu pilih **Notepad**.
5. Anda dapat mengonfigurasinya menggunakan **tautan Modrinth** atau **URL Langsung**:

### Jika menggunakan Modrinth:
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
*(Tidak perlu mencantumkan `url` atau `hash`! Server akan mengunduh versi terbaru secara otomatis).*

### Jika menggunakan URL Langsung (Discord/Dropbox):
```json
{
  "packs": {
    "plastic": {
      "url": "TEMPEL_LINK_DISCORD_ATAU_DROPBOX_ANDA_DI_SINI",
      "hash": "TEMPEL_KODE_40_KARAKTER_SHA1_ANDA_DI_SINI",
      "required": false,
      "prompt": "Would you like to use the Plastic look?"
    }
  }
}
```

6. Simpan file (**Ctrl + S**) dan tutup Notepad.

---

## 🔄 Langkah 4: Muat Ulang Konfigurasi
Jika server Anda sudah berjalan, Anda tidak perlu mematikannya:
1. Masuk ke server Minecraft Anda.
2. Pastikan Anda adalah admin (memiliki izin `/op`).
3. Tekan **T** atau **/** untuk membuka obrolan.
4. Ketik **`/pack reload`** dan tekan **Enter**.
5. Anda akan melihat pesan yang menyatakan bahwa pack berhasil dimuat!

---

## 🧱 Langkah 5: Cara Menggunakan Perintah di Minecraft

Semua pemain di server dapat menggunakan perintah berikut di obrolan game:

### 1. Melihat semua pack yang tersedia
Ketik: **`/pack list`**  
*Ini akan mencantumkan semua nama pack yang Anda konfigurasikan di Langkah 3.*

### 2. Memasang pack untuk diri sendiri
Ketik: **`/pack glassdoors`** (atau `/pack apply glassdoors`)  
*Layar konfirmasi akan muncul menanyakan apakah Anda ingin mengunduh dan memasang pack. Klik **Yes**!*

### 3. Menghapus pack yang aktif
Ketik: **`/pack pop`** (atau `/pack clear`)  
*Tampilan game Anda akan langsung kembali ke tampilan default normal.*

### 4. Perintah Admin: Memberikan pack ke pemain lain
Jika Anda seorang admin, Anda dapat mengirimkan pack ke pemain lain:  
Ketik: **`/pack apply glassdoors Bob`** (ganti Bob dengan nama pemain, atau `@a` untuk semua orang).

---

Selesai! Sekarang para pemain Anda dapat menyesuaikan tampilan visual game mereka kapan pun mereka mau!
