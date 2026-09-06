# Panduan Admin

Panduan ini merinci fitur administrasi, pemuatan ulang konfigurasi secara langsung (live reload), dan persistensi preferensi pemain.

---

## 💾 Preferensi Persisten

Saat pemain memilih pack melalui `/pack <name>`, server secara otomatis mencatat pilihan tersebut di `config/serverpackswitcher_preferences.json`.
- **Penerapan Ulang Otomatis**: Saat pemain keluar dan bergabung kembali nanti, server secara otomatis membaca preferensi mereka dan mengirimkan pack yang sesuai.
- **Menghapus Preferensi**: Jika pemain menjalankan `/pack pop` atau `/pack clear`, preferensi mereka akan dihapus, dan mereka tidak akan menerima pack apa pun saat bergabung berikutnya.

---

## 🔄 Memuat Ulang Konfigurasi Secara Langsung (Live Reload)

Jika Anda mengedit file konfigurasi `config/serverpackswitcher.json` saat server sedang berjalan, Anda tidak perlu memulai ulang server!
1. Pastikan Anda adalah administrator (memiliki izin operator level 2+).
2. Ketik **`/pack reload`** di obrolan game atau konsol server.
3. Server akan memuat ulang konfigurasi secara instan. Pack yang menggunakan tautan Modrinth akan diselesaikan kembali secara asinkron di latar belakang.

---

## 🛡️ Perintah Admin Bertarget

Administrator (operator dengan izin level 2+) dapat memaksa atau menghapus resource pack untuk pemain lain.

### Menerapkan Pack Secara Paksa
- `/pack apply plastic Bob` - Menerapkan pack `plastic` ke pemain `Bob`.
- `/pack apply plastic @a` - Menerapkan pack `plastic` ke semua pemain yang sedang online.

### Menghapus Pack Secara Paksa
- `/pack pop Bob` - Menghapus pack aktif dari pemain `Bob`.
- `/pack pop @a` - Menghapus pack aktif dari semua pemain yang sedang online.
