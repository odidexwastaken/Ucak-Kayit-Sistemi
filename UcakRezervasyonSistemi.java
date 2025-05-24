import java.util.*;
import java.io.*;



// Ana program sınıfı
public class UcakRezervasyonSistemi {

    
    // Tüm verileri tutacak listeler
    static ArrayList<Ucak> ucaklar = new ArrayList<Ucak>();
    static ArrayList<Lokasyon> lokasyonlar = new ArrayList<Lokasyon>();
    static ArrayList<Ucus> ucuslar = new ArrayList<Ucus>();
    static ArrayList<Rezervasyon> rezervasyonlar = new ArrayList<Rezervasyon>();
    
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        // Önce dosyalardan verileri oku
        dosyaVerileriniOku();

        // Eğer dosyalardan veri okunamadıysa örnek verileri ekle
        if (ucaklar.size() == 0) {
            ornekVerileriEkle();
        }

        System.out.println("=== UCAK REZERVASYON SISTEMI ===");

        // Ana menü döngüsü
        while (true) {
            menuGoster();
            int secim = input.nextInt();

            if (secim == 1) {
                ucuslariListele();
            } 
            else if (secim == 2) {
                rezervasyonYap();
            } 
            else if (secim == 3) {
                rezervasyonlariGoster();
            } 
            else if (secim == 4) {
                dosyayaKaydet();
            } 
            else if (secim == 5) {
                System.out.println("Program Sonlandiriliyor...");
                break;
            } 
            else {
                System.out.println("Hatali secim! Tekrar deneyin.");
            }
        }
    }

    // Dosyalardan verileri oku
    static void dosyaVerileriniOku() {
        System.out.println("Dosyalardan veriler okunuyor...");

        // Önce temel verileri oku (uçaklar ve lokasyonlar)
        ucaklariOku();
        lokasyonlariOku();

        // Sonra bağımlı verileri oku (uçuşlar ve rezervasyonlar)
        ucuslariOku();
        rezervasyonlariOku();

        System.out.println("Veri okuma islemi tamamlandi.");
    }

    // Uçakları dosyadan oku
    static void ucaklariOku() {
        try {
            File dosya = new File("ucaklar.csv");
            if (!dosya.exists()) {
                System.out.println("ucaklar.csv dosyasi bulunamadi.");
                return;
            }

            Scanner dosyaOkuyucu = new Scanner(dosya);

            // İlk satırı atla (başlık satırı)
            if (dosyaOkuyucu.hasNextLine()) {
                dosyaOkuyucu.nextLine();
            }

            while (dosyaOkuyucu.hasNextLine()) {
                String satir = dosyaOkuyucu.nextLine();
                String[] bilgiler = satir.split(",");

                if (bilgiler.length == 4) {
                    String marka = bilgiler[0];
                    String model = bilgiler[1];
                    String seriNo = bilgiler[2];
                    int kapasite = Integer.parseInt(bilgiler[3]);

                    ucaklar.add(new Ucak(marka, model, seriNo, kapasite));
                }
            }
            dosyaOkuyucu.close();
            System.out.println(ucaklar.size() + " ucak bilgisi okundu.");

        } 
        catch (Exception e) {
            System.out.println("Ucak dosyasi okuma hatasi: " + e.getMessage());
        }
    }

    // Lokasyonları dosyadan oku
    static void lokasyonlariOku() {
        try {
            File dosya = new File("lokasyonlar.csv");
            
            if (!dosya.exists()) {
                System.out.println("lokasyonlar.csv dosyasi bulunamadi.");
                return;
            }

            Scanner dosyaOkuyucu = new Scanner(dosya);

            // İlk satırı atla (başlık satırı)
            if (dosyaOkuyucu.hasNextLine()) {
                dosyaOkuyucu.nextLine();
            }

            while (dosyaOkuyucu.hasNextLine()) {
                
                String satir = dosyaOkuyucu.nextLine();
                String[] bilgiler = satir.split(",");

                if (bilgiler.length == 3) {
                    
                    String ulke = bilgiler[0];
                    String sehir = bilgiler[1];
                    String havaalani = bilgiler[2];

                    lokasyonlar.add(new Lokasyon(ulke, sehir, havaalani));
                }
            }
            
            dosyaOkuyucu.close();
            System.out.println(lokasyonlar.size() + " lokasyon bilgisi okundu.");

        } 
        catch (Exception e) {
            System.out.println("Lokasyon dosyasi okuma hatasi: " + e.getMessage());
        }
    }



    
    // Uçuşları dosyadan oku
    static void ucuslariOku() {
        try {
            File dosya = new File("ucuslar.csv");
            if (!dosya.exists()) {
                System.out.println("ucuslar.csv dosyasi bulunamadi, ornek ucuslar ekleniyor...");

                // Eğer uçuş dosyası yoksa ve uçaklar/lokasyonlar varsa örnek uçuşlar ekle
                if (ucaklar.size() > 0 && lokasyonlar.size() > 0) {
                    ornekUcuslariEkle();
                }
                return;
            }

            Scanner dosyaOkuyucu = new Scanner(dosya);

            // İlk satırı atla (başlık satırı)
            if (dosyaOkuyucu.hasNextLine()) {
                dosyaOkuyucu.nextLine();
            }

            while (dosyaOkuyucu.hasNextLine()) {
                
                String satir = dosyaOkuyucu.nextLine();
                String[] bilgiler = satir.split(",");

                if (bilgiler.length == 7) {
                    String ucusKodu = bilgiler[0];
                    String neredenSehir = bilgiler[1];
                    String nereyeSehir = bilgiler[2];
                    String tarih = bilgiler[3];
                    String saat = bilgiler[4];
                    String seriNo = bilgiler[5];
                    int doluKoltuk = Integer.parseInt(bilgiler[6]);

                    // Lokasyonları bul
                    Lokasyon nereden = lokasyonBul(neredenSehir);
                    Lokasyon nereye = lokasyonBul(nereyeSehir);
                    Ucak ucak = ucakBul(seriNo);

                    if (nereden != null && nereye != null && ucak != null) {
                        Ucus ucus = new Ucus(ucusKodu, nereden, nereye, tarih, saat, ucak);
                        ucus.doluKoltukSayisi = doluKoltuk;
                        ucuslar.add(ucus);
                    }
                }
            }
            dosyaOkuyucu.close();
            System.out.println(ucuslar.size() + " ucus bilgisi okundu.");

        } catch (Exception e) {
            System.out.println("Ucus dosyasi okuma hatasi: " + e.getMessage());
        }
    }

    // Rezervasyonları dosyadan oku
    static void rezervasyonlariOku() {
        
        try {
            File dosya = new File("rezervasyonlar.csv");
            
            if (!dosya.exists()) {
                System.out.println("rezervasyonlar.csv dosyasi bulunamadi.");
                return;
            }

            Scanner dosyaOkuyucu = new Scanner(dosya);

            // İlk satırı atla (başlık satırı)
            if (dosyaOkuyucu.hasNextLine()) {
                dosyaOkuyucu.nextLine();
            }

            while (dosyaOkuyucu.hasNextLine()) {
                
                String satir = dosyaOkuyucu.nextLine();
                String[] bilgiler = satir.split(",");

                if (bilgiler.length == 8) {
                    
                    String rezervasyonNo = bilgiler[0];
                    String ad = bilgiler[1];
                    String soyad = bilgiler[2];
                    int yas = Integer.parseInt(bilgiler[3]);
                    String ucusKodu = bilgiler[4];
                    String tarih = bilgiler[7];

                    // Uçuşu bul
                    Ucus ucus = ucusBul(ucusKodu);

                    if (ucus != null) {
                        Rezervasyon rezervasyon = new Rezervasyon(rezervasyonNo, ucus, ad, soyad, yas);
                        rezervasyon.rezervasyonTarihi = tarih;
                        rezervasyonlar.add(rezervasyon);
                    }
                }
            }
            
            dosyaOkuyucu.close();
            System.out.println(rezervasyonlar.size() + " rezervasyon bilgisi okundu.");

        } 
        catch (Exception e) {
            System.out.println("Rezervasyon dosyasi okuma hatasi: " + e.getMessage());
        }
    }

    // Şehir adına göre lokasyon bul
    static Lokasyon lokasyonBul(String sehir) {
        
        for (int i = 0; i < lokasyonlar.size(); i++) {
            
            if (lokasyonlar.get(i).sehir.equals(sehir)) {
                return lokasyonlar.get(i);
            }
        }
        return null;
    }

    // Seri numarasına göre uçak bul
    static Ucak ucakBul(String seriNo) {
        
        for (int i = 0; i < ucaklar.size(); i++) {
            
            if (ucaklar.get(i).seriNo.equals(seriNo)) {
                return ucaklar.get(i);
            }
        }
        return null;
    }

    // Uçuş koduna göre uçuş bul
    static Ucus ucusBul(String ucusKodu) {
        
        for (int i = 0; i < ucuslar.size(); i++) {
            
            if (ucuslar.get(i).ucusKodu.equals(ucusKodu)) {
                return ucuslar.get(i);
            }
        }
        return null;
    }

    // Menüyü ekrana yazdır
    static void menuGoster() {
        
        System.out.println("\n--- MENÜ ---");
        System.out.println("1. Ucuslari Listele");
        System.out.println("2. Rezervasyon Yap");
        System.out.println("3. Rezervasyonlari Goruntule");
        System.out.println("4. Dosyaya Kaydet");
        System.out.println("5. Cikis");
        System.out.print("Seciminiz: ");
    }

    // Örnek verileri sisteme ekle
    static void ornekVerileriEkle() {
        
        // Uçakları ekle
        ucaklar.add(new Ucak("Turkish Airlines", "A320", "TC-JKL", 180));
        ucaklar.add(new Ucak("Pegasus", "B737", "TC-MNO", 189));
        ucaklar.add(new Ucak("Turkish Airlines", "A330", "TC-PQR", 300));

        // Lokasyonları ekle
        lokasyonlar.add(new Lokasyon("Turkiye", "Istanbul", "Istanbul Havalimani"));
        lokasyonlar.add(new Lokasyon("Turkiye", "Ankara", "Esenboga Havalimani"));
        lokasyonlar.add(new Lokasyon("Turkiye", "Izmir", "Adnan Menderes Havalimani"));
        lokasyonlar.add(new Lokasyon("Almanya", "Berlin", "Berlin Havalimani"));

        // Uçuşları ekle
        ornekUcuslariEkle();
    }

    // Örnek uçuşları ekle
    static void ornekUcuslariEkle() {
        
        if (ucaklar.size() > 0 && lokasyonlar.size() > 0) {
            ucuslar.add(new Ucus("TK101", lokasyonlar.get(0), lokasyonlar.get(1), "15/06/2024", "10:30", ucaklar.get(0)));
            ucuslar.add(new Ucus("PC205", lokasyonlar.get(0), lokasyonlar.get(2), "16/06/2024", "14:20", ucaklar.get(1)));
            ucuslar.add(new Ucus("TK1847", lokasyonlar.get(0), lokasyonlar.get(3), "20/06/2024", "08:15", ucaklar.get(2)));
            ucuslar.add(new Ucus("PC311", lokasyonlar.get(1), lokasyonlar.get(2), "18/06/2024", "16:45", ucaklar.get(0)));
        }
    }

    // Tüm uçuşları listele
    static void ucuslariListele() {
        
        System.out.println("\n=== MEVCUT UCUSLAR ===");
        for (int i = 0; i < ucuslar.size(); i++) {
            System.out.print((i + 1) + ". ");
            ucuslar.get(i).bilgileriYazdir();
        }
    }

    // Yeni rezervasyon yap
    static void rezervasyonYap() {
        
        System.out.println("\n=== REZERVASYON YAPMA ===");

        // Uçuşları göster
        ucuslariListele();

        System.out.print("\nHangi ucusu seçmek istiyorsunuz? (Numara girin): ");
        int secim = input.nextInt();

        // Geçerli seçim kontrolü
        if (secim < 1 || secim > ucuslar.size()) {
            System.out.println("Hatalı ucus secimi!");
            return;
        }

        Ucus secilenUcus = ucuslar.get(secim - 1);

        // Yer kontrolü
        if (!secilenUcus.yerVarMi()) {
            System.out.println("Bu ucusta boş yer kalmadi!");
            return;
        }

        // Yolcu bilgilerini al
        System.out.print("Adiniz: ");
        String ad = input.next();

        System.out.print("Soyadiniz: ");
        String soyad = input.next();

        System.out.print("Yasiniz: ");
        int yas = input.nextInt();

        // Rezervasyon numarası oluştur
        String rezervasyonNo = "R" + (rezervasyonlar.size() + 1) + "001";

        // Yeni rezervasyon oluştur
        Rezervasyon yeniRezervasyon = new Rezervasyon(rezervasyonNo, secilenUcus, ad, soyad, yas);

        // Rezervasyonu kaydet ve uçuşta yer ayır
        rezervasyonlar.add(yeniRezervasyon);
        secilenUcus.yerRezerveEt();

        System.out.println("\nRezervasyon basariyla olusturuldu!");
        System.out.println("Rezervasyon Numaraniz: " + rezervasyonNo);
    }

    // Tüm rezervasyonları göster
    static void rezervasyonlariGoster() {
        
        System.out.println("\n=== TUM REZERVASYONLAR ===");

        if (rezervasyonlar.size() == 0) {
            System.out.println("Henuz hic rezervasyon yapilmamis.");
            return;
        }

        for (int i = 0; i < rezervasyonlar.size(); i++) {
            System.out.print((i + 1) + ". ");
            rezervasyonlar.get(i).bilgileriYazdir();
        }
    }

    // Verileri CSV dosyasına kaydet
    static void dosyayaKaydet() {
        
        try {
            // Rezervasyonları kaydet
            PrintWriter dosya = new PrintWriter(new FileWriter("rezervasyonlar.csv"));
            dosya.println("RezervasyonNo,Ad,Soyad,Yas,UcusKodu,Nereden,Nereye,Tarih");

            for (int i = 0; i < rezervasyonlar.size(); i++) {
                Rezervasyon r = rezervasyonlar.get(i);
                dosya.println(r.rezervasyonNo + "," + r.ad + "," + r.soyad + "," + r.yas + "," +
                        r.ucus.ucusKodu + "," + r.ucus.nereden.sehir + "," +
                        r.ucus.nereye.sehir + "," + r.rezervasyonTarihi);
            }
            dosya.close();

            // Uçakları kaydet
            PrintWriter ucakDosya = new PrintWriter(new FileWriter("ucaklar.csv"));
            ucakDosya.println("Marka,Model,SeriNo,Kapasite");

            for (int i = 0; i < ucaklar.size(); i++) {
                Ucak u = ucaklar.get(i);
                ucakDosya.println(u.marka + "," + u.model + "," + u.seriNo + "," + u.koltukKapasitesi);
            }
            ucakDosya.close();

            // Lokasyonları kaydet
            PrintWriter lokasyonDosya = new PrintWriter(new FileWriter("lokasyonlar.csv"));
            lokasyonDosya.println("Ulke,Sehir,Havaalani");

            for (int i = 0; i < lokasyonlar.size(); i++) {
                Lokasyon l = lokasyonlar.get(i);
                lokasyonDosya.println(l.ulke + "," + l.sehir + "," + l.havaalani);
            }
            lokasyonDosya.close();

            // Uçuşları kaydet
            PrintWriter ucusDosya = new PrintWriter(new FileWriter("ucuslar.csv"));
            ucusDosya.println("UcusKodu,Nereden,Nereye,Tarih,Saat,SeriNo,DoluKoltuk");

            for (int i = 0; i < ucuslar.size(); i++) {
                Ucus u = ucuslar.get(i);
                ucusDosya.println(u.ucusKodu + "," + u.nereden.sehir + "," + u.nereye.sehir + "," +
                        u.tarih + "," + u.saat + "," + u.ucak.seriNo + "," + u.doluKoltukSayisi);
            }
            ucusDosya.close();

            System.out.println("Veriler basariyla dosyalara kaydedildi!");
            System.out.println("- rezervasyonlar.csv");
            System.out.println("- ucaklar.csv");
            System.out.println("- lokasyonlar.csv");
            System.out.println("- ucuslar.csv");

        } catch (Exception e) {
            System.out.println("Dosya kaydetme hatasi: " + e.getMessage());
        }
    }
}

// Uçak bilgilerini tutan sınıf
class Ucak {
    
    String model;
    String marka;
    String seriNo;
    int koltukKapasitesi;
    boolean aktif;

    public Ucak(String marka, String model, String seriNo, int koltukKapasitesi) {
        
        this.marka = marka;
        this.model = model;
        this.seriNo = seriNo;
        this.koltukKapasitesi = koltukKapasitesi;
        this.aktif = true;
    }

    public void bilgileriYazdir() {
        System.out.println(marka + " " + model + " - Seri: " + seriNo + " - Kapasite: " + koltukKapasitesi);
    }
}


// Lokasyon bilgilerini tutan sınıf
class Lokasyon {
    
    String ulke;
    String sehir;
    String havaalani;
    boolean aktif;

    public Lokasyon(String ulke, String sehir, String havaalani) {
        this.ulke = ulke;
        this.sehir = sehir;
        this.havaalani = havaalani;
        this.aktif = true;
    }

    public void bilgileriYazdir() {
        System.out.println(sehir + " (" + ulke + ") - " + havaalani);
    }
}



// Uçuş bilgilerini tutan sınıf
class Ucus {
    
    String ucusKodu;
    Lokasyon nereden;
    Lokasyon nereye;
    String tarih;
    String saat;
    Ucak ucak;
    int doluKoltukSayisi;

    public Ucus(String ucusKodu, Lokasyon nereden, Lokasyon nereye, String tarih, String saat, Ucak ucak) {
        this.ucusKodu = ucusKodu;
        this.nereden = nereden;
        this.nereye = nereye;
        this.tarih = tarih;
        this.saat = saat;
        this.ucak = ucak;
        this.doluKoltukSayisi = 0;
    }

    public boolean yerVarMi() {
        return doluKoltukSayisi < ucak.koltukKapasitesi;
    }

    public void yerRezerveEt() {
        if (yerVarMi()) {
            doluKoltukSayisi++;
        }
    }

    public int bosKoltukSayisi() {
        return ucak.koltukKapasitesi - doluKoltukSayisi;
    }

    public void bilgileriYazdir() {
        System.out.println(ucusKodu + " - " + nereden.sehir + " -> " + nereye.sehir +
                " | " + tarih + " " + saat + " | Bos Koltuk: " + bosKoltukSayisi());
    }
}




// Rezervasyon bilgilerini tutan sınıf
class Rezervasyon {
    
    String rezervasyonNo;
    Ucus ucus;
    String ad;
    String soyad;
    int yas;
    String rezervasyonTarihi;

    public Rezervasyon(String rezervasyonNo, Ucus ucus, String ad, String soyad, int yas) {
        this.rezervasyonNo = rezervasyonNo;
        this.ucus = ucus;
        this.ad = ad;
        this.soyad = soyad;
        this.yas = yas;
        this.rezervasyonTarihi = "25/05/2024";
    }

    public void bilgileriYazdir() {
        System.out.println("Rezervasyon No: " + rezervasyonNo + " | " + ad + " " + soyad +
                " | Ucus: " + ucus.ucusKodu + " | Tarih: " + rezervasyonTarihi);
    }
}
