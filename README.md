# Uçak Rezervasyon Sistemi

Java ile yazılmış basit bir konsol tabanlı uçak rezervasyon sistemi.

## Özellikler

- Uçuş listesi görüntüleme
- Rezervasyon yapma
- Rezervasyon listesi görüntüleme
- Verileri CSV dosyalarına kaydetme
- CSV dosyalarından veri okuma
- Program yeniden başlatıldığında verilerin korunması

### Menü Seçenekleri

1. **Uçuşları Listele** - Mevcut tüm uçuşları gösterir
2. **Rezervasyon Yap** - Yeni rezervasyon oluşturur
3. **Rezervasyonları Görüntüle** - Yapılmış rezervasyonları listeler
4. **Dosyaya Kaydet** - Tüm verileri CSV dosyalarına kaydeder
5. **Çıkış** - Programı sonlandırır

## Veri Yapısı

### Sınıflar

- **`Ucak`** - Uçak bilgilerini tutar (marka, model, seri no, kapasite)
- **`Lokasyon`** - Havaalanı bilgilerini tutar (ülke, şehir, havaalanı)
- **`Ucus`** - Uçuş bilgilerini tutar (kod, nereden, nereye, tarih, saat)
- **`Rezervasyon`** - Rezervasyon bilgilerini tutar (no, yolcu bilgileri, uçuş)

### CSV Dosyaları

Program aşağıdaki CSV dosyalarını oluşturur ve okur:

- `ucaklar.csv` - Uçak bilgileri
- `lokasyonlar.csv` - Havaalanı lokasyonları
- `ucuslar.csv` - Uçuş bilgileri
- `rezervasyonlar.csv` - Rezervasyon kayıtları

## Örnek Veriler

Program ilk çalıştırıldığında (CSV dosyaları yoksa) şu örnek verilerle başlar:

### Uçaklar
- Turkish Airlines A320 (TC-JKL) - 180 koltuk
- Pegasus B737 (TC-MNO) - 189 koltuk
- Turkish Airlines A330 (TC-PQR) - 300 koltuk

### Lokasyonlar
- İstanbul Havalimanı
- Esenboğa Havalimanı (Ankara)
- Adnan Menderes Havalimanı (İzmir)
- Berlin Havalimanı

### Örnek Uçuşlar
- TK101: İstanbul → Ankara
- PC205: İstanbul → İzmir
- TK1847: İstanbul → Berlin
- PC311: Ankara → İzmir
