📱 WeTrust – Aplikasi Berita Android

WeTrust adalah aplikasi mobile berbasis Android yang menyediakan berita terkini secara real-time melalui integrasi dengan [NewsAPI.org](https://newsapi.org/). Aplikasi ini dirancang untuk memberikan pengalaman membaca berita yang cepat, ringan, dan dapat diandalkan.

---
🎯 Fitur Utama

- 🔥 Menampilkan berita **terkini** secara real-time.
- 🗂️ Kategori berita: Business, Entertainment, Health, Science, Sports, Technology.
- ⭐ Simpan berita favorit untuk dibaca nanti.
- 🌙 Dukungan **mode gelap** (dark mode).
- 🔄 Swipe to refresh dan navigasi yang sederhana.
- 
---
📸 Cuplikan Layar

![Home Screen]
![TNAhome](https://github.com/user-attachments/assets/63f9576e-9a32-422c-9080-cc4db88f51fa)

![Detail Screen]
![TNAdetails](https://github.com/user-attachments/assets/36f96a1c-199e-4133-8785-dfe15b464506)

![Favourites Screen]
![TNAfav](https://github.com/user-attachments/assets/5d229833-29e0-4668-b17d-f3dbbf206574)

---

🛠️ Teknologi yang Digunakan

- Bahasa: Kotlin / Java
- IDE: Android Studio
- API: [NewsAPI](https://newsapi.org)
- Library:
  - [Retrofit](https://square.github.io/retrofit/) – untuk HTTP client
  - [Glide](https://bumptech.github.io/glide/) – untuk memuat gambar
  - RecyclerView, ViewBinding, dll

---

⚙️ Cara Instalasi

1. **Clone repository** ini ke lokal:
   ```bash
   git clone https://github.com/namakamu/WeTrust.git
````

2. **Buka di Android Studio**.
3. Tambahkan API Key dari [NewsAPI.org](https://newsapi.org) ke `constants.kt` atau `BuildConfig`:

   ```kotlin
   const val API_KEY = "29b46a5ce5474d81b0635b804d998293"
   ```
4. Jalankan aplikasi di emulator atau perangkat fisik.

---

🔍 Struktur Proyek

```
📁 WeTrust/
 ┣ 📂 app/
 ┃ ┣ 📂 src/
 ┃ ┃ ┣ 📂 main/
 ┃ ┃ ┃ ┣ 📁 java/com/wetrust/
 ┃ ┃ ┃ ┣ 📁 res/
 ┃ ┃ ┃ ┗ AndroidManifest.xml
 ┣ build.gradle
 ┗ README.md
```

---

❓ FAQ

Q: Apakah aplikasi ini bisa digunakan tanpa internet?
A: Saat ini belum. Semua data berasal dari API online.

Q: Apa sumber beritanya?
A: Menggunakan endpoint dari [NewsAPI.org](https://newsapi.org), mengumpulkan berita dari berbagai media terpercaya.

---

📌 Catatan Tambahan

* Proyek ini dibuat untuk memenuhi tugas akhir praktikum **Pemrograman Mobile**.
* Hanya digunakan untuk pembelajaran, bukan untuk produksi komersial.

---

🙌 Kontribusi

Pull request terbuka untuk pengembangan lebih lanjut seperti:

* Fitur pencarian berita
* Push notification
* Penyimpanan offline

---

👤 Pengembang

* Nama: Fathan Wibowo
* Universitas: Universitas Hasanuddin
* Mata Kuliah: Pemrograman Mobile – Final Lab 2025
* References: Android Knowledge - https://youtu.be/UvaVJ0EseP0?si=5zkWKDx3wcc2SKG2

```

