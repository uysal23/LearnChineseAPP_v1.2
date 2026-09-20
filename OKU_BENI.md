# 柳溪镇 · Çince — APK oluşturma

Bu klasör hazır bir Android Studio / Gradle projesidir (WebView + yerel Android TTS).
Uygulamanın tamamı `app/src/main/assets/index.html` içindedir (internet gerekmez).

## Yol 1 — GitHub ile (bilgisayar/Android Studio gerekmez, ~5 dk)
1. github.com'da yeni bir depo aç, bu klasörün *içindekileri* depoya yükle (`.github` klasörü dahil).
2. Depoda **Actions → Build APK → Run workflow**'a bas.
3. Bitince sayfanın altındaki **Artifacts → liuxi-cince-apk** dosyasını indir, zip'ten `app-debug.apk` çıkar.
4. APK'yı telefona at, "bilinmeyen kaynaklardan yüklemeye" izin verip kur.

## Yol 2 — Android Studio
File → Open → bu klasör → Build → Build APK(s). Çıktı: `app/build/outputs/apk/debug/app-debug.apk`

## Ses (TTS)
Telefonda Ayarlar → Genel yönetim → Metin okuma → Google TTS → Çince (zh-CN) ses verisini indir.
