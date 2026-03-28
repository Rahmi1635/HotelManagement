# OTEL REZERVASYON VE MÜŞTERİ TAKİP SİSTEMİ

Bu proje, Kırklareli Üniversitesi Mühendislik Fakültesi  
Yazılım Mühendisliği Bölümü  
**YAZ16303 – Yazılım Mimarisi ve Tasarımı** dersi kapsamında geliştirilmiştir.

---

## 📌 Proje Tanımı

Bu proje, Java ve JavaFX kullanılarak geliştirilmiş masaüstü tabanlı bir
**Otel Rezervasyon ve Müşteri Takip Sistemi**dir.

Sistem sayesinde;
- Müşteriler uygun odaları arayabilir, rezervasyon yapabilir ve profil bilgilerini yönetebilir,
- Otel personeli müşteri, oda ve rezervasyon işlemlerini yönetebilir,
- Check-in ve Check-out süreçleri sistem üzerinden kontrol edilebilir.

---

## 🛠️ Kullanılan Teknolojiler

- Java
- JavaFX
- Maven
- FXML
- CSS

---

## 👥 Kullanıcı Rolleri

### 👤 Müşteri
- Sisteme kayıt olabilir ve giriş yapabilir
- Uygun odaları arayabilir
- Rezervasyon oluşturabilir ve iptal edebilir
- Geçmiş konaklamalarını görüntüleyebilir
- Profil bilgilerini güncelleyebilir

### 🧑‍💼 Personel
- Müşteri yönetimi yapabilir
- Oda ekleme, güncelleme ve listeleme işlemlerini gerçekleştirebilir
- Rezervasyonları yönetebilir
- Check-in ve Check-out işlemlerini gerçekleştirebilir

---

## 🏗️ Proje Mimarisi

Proje, katmanlı mimari yaklaşımı kullanılarak geliştirilmiştir.

- **domain**: Varlık (Entity) ve iş modeli sınıfları
- **repository**: Veri erişim katmanı
- **service**: İş mantığı katmanı
- **ui**: JavaFX arayüz ve controller sınıfları
- **security**: Kimlik doğrulama ve yetkilendirme işlemleri
- **config / context**: Uygulama ve oturum yapılandırmaları

---

## 🎨 Kullanılan Tasarım Desenleri

Projede aşağıdaki tasarım desenleri uygulanmıştır:

- **Singleton**  
  Veritabanı bağlantısının tek bir örnek üzerinden yönetilmesi için kullanılmıştır.

- **Factory / Abstract Factory**  
  Rezervasyon servisinde strategy oluşturmak için kullanılmıştır.

- **State**  
  Rezervasyon durumlarının (Pending, Confirmed, CheckedIn, CheckedOut, Cancelled)
  yönetilmesi için kullanılmıştır.

- **Observer**  
  Rezervasyon veya oda durumları değiştiğinde sistemin bilgilendirilmesi amacıyla kullanılmıştır.

- **Decorator**  
  Odalara dinamik özellikler eklemek için kullanılmıştır.

- **Strategy**  
  Rezervasyon filtreleme işlemlerinde farklı algoritmaların kullanılabilmesi için uygulanmıştır.

---

## 🧩 Abstract Sınıflar

Projede aşağıdaki abstract sınıflar kullanılmıştır:

- **Person**  
  Sistem kullanıcıları (Customer, Staff) için temel abstract sınıftır.

- **RoomDecorator**  
  Decorator tasarım deseninde kullanılan abstract sınıftır.

---

## ▶️ Uygulamanın Çalıştırılması

1. Proje GitHub üzerinden klonlanır.
2. Proje bir IDE (tercihen IntelliJ IDEA) ile açılır.
3. JavaFX yapılandırmasının doğru olduğundan emin olunur.
4. Aşağıdaki komut ile uygulama çalıştırılır:

mvn javafx:run


## Project Team

- Yavuz Selim Erhan – https://github.com/YavuzSelimErhan
- Rahmi Göktaş – https://github.com/Rahmi1635

## ER Diagram

![ER Diagram](docs/ER%20Diagram/ERDiagram.png)

## Use-case Diagram

![Use-case Diagram](docs/Use-Case%20Diagram/out/use-case/use-case.png)

## Sequence Diagram

![Sequence Diagram](docs/Sequence%20Diagram/out/Sequence%20Diagram/sequence/sequence.png)

