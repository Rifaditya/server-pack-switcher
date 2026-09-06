# Referensi Perintah

Semua perintah diawali dengan prefiks dasar `/pack`.

## Daftar Perintah

| Perintah | Izin | Deskripsi |
| :--- | :--- | :--- |
| `/pack list` | Semua Orang | Menampilkan daftar semua server resource pack yang telah dikonfigurasi. |
| `/pack <pack_name>` | Semua Orang | Menerapkan resource pack yang ditentukan ke pemain yang menjalankan perintah. |
| `/pack apply <pack_name>` | Semua Orang | Alias untuk `/pack <pack_name>`. |
| `/pack pop` | Semua Orang | Menghapus server pack yang aktif dari klien pemain. |
| `/pack clear` | Semua Orang | Alias untuk `/pack pop`. |
| `/pack apply <pack_name> <targets>` | Admin (Level 2+) | Memaksa penerapan resource pack tertentu ke pemain target. |
| `/pack pop <targets>` | Admin (Level 2+) | Memaksa penghapusan server pack yang aktif dari pemain target. |
| `/pack reload` | Admin (Level 2+) | Memuat ulang file konfigurasi dari disk secara langsung. |

*Catatan: Argumen perintah mendukung pelengkapan otomatis (tombol TAB) untuk semua nama pack yang terdaftar.*

---

## Detail Perintah

### `/pack list`
Menampilkan nama semua resource pack yang dikonfigurasi di server dan tersedia untuk dipasang oleh pemain.

### `/pack <pack_name>`
Mengunduh dan menerapkan pack yang cocok dengan `<pack_name>` ke game Anda. Jika Anda saat ini sedang menggunakan pack lain dari mod ini, mod akan otomatis mencopotnya terlebih dahulu untuk memastikan pergantian yang bersih.

### `/pack pop` / `/pack clear`
Menghapus server resource pack yang aktif dari klien Anda, mengembalikan tampilan game ke visual vanilla default.
