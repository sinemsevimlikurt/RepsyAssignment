# Repsy Assignment Project

A minimal software package repository system for the fictional Repsy programming language. Built with Java 17, Spring Boot, PostgreSQL, and supports both file-system and MinIO object storage backends.

## Features
- Upload (`deployment`) and download endpoints for `.rep` and `meta.json` files
- File-system and MinIO (object storage) strategies, switchable via configuration
- Package metadata and version tracking in PostgreSQL
- Modular Maven structure for storage strategies
- Robust validation and error handling
- Dockerized for easy deployment

## Technology Stack
- Java 17
- Spring Boot 3.x
- Hibernate/JPA
- PostgreSQL
- MinIO (object storage)
- Maven (multi-module)
- Docker

## Project Structure
- `storage-common` — Shared storage interface
- `storage-filesystem` — File-system storage implementation
- `storage-minio` — MinIO storage implementation
- `src/main/java/com.repsy.assignment` — Main Spring Boot app, REST endpoints, business logic, models

## Quick Start

### 1. PostgreSQL ve MinIO Kurulumu

#### PostgreSQL (örnek Docker komutu):
```sh
docker run --name repsy-postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=repsy -p 5432:5432 -d postgres:15
```

#### MinIO (örnek Docker komutu):
```sh
docker run -p 9000:9000 -p 9001:9001 \
  -e MINIO_ROOT_USER=minioadmin \
  -e MINIO_ROOT_PASSWORD=minioadmin \
  -v ~/minio/data:/data \
  minio/minio server /data --console-address ":9001"
```

MinIO web arayüzünden bir bucket oluşturun (ör: `repsy-bucket`).

### 2. Uygulama Konfigürasyonu
`src/main/resources/application.properties` dosyasındaki ayarları kendi ortamınıza göre düzenleyin:
```properties
# storageStrategy=file-system
# storage.filesystem.root=/tmp/repsy-storage

storageStrategy=minio
minio.url=http://localhost:9000
minio.access-key=minioadmin
minio.secret-key=minioadmin
minio.bucket=repsy-bucket

spring.datasource.url=jdbc:postgresql://localhost:5432/repsy
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### 3. Projeyi Derleme ve Çalıştırma
```sh
mvn clean install
mvn spring-boot:run
```
veya Docker ile build:
```sh
docker build -t repsy-assignment .
docker run -p 8080:8080 --env-file .env repsy-assignment
```

## API Endpoints

### Deployment (Yükleme)
```http
POST /api/v1/{packageName}/{version}
Content-Type: multipart/form-data
file=[package.rep | meta.json]
```
- `package.rep`: Derlenmiş kaynak kodları içeren .zip dosyası (uzantısı .rep)
- `meta.json`: Paket meta verisi (örnek aşağıda)

#### Örnek meta.json
```json
{
  "name": "mypackage",
  "version": "1.0.0",
  "author": "John Doe",
  "dependencies": [
    { "package": "even", "version": "3.4.7" },
    { "package": "math", "version": "4.2.8" }
  ]
}
```

### Download (İndirme)
```http
GET /api/v1/{packageName}/{version}/{fileName}
```
- `fileName` olarak `package.rep` veya `meta.json` kullanılabilir.

## Storage Strategy Seçimi
- `application.properties` veya ortam değişkeni ile `storageStrategy` değerini `file-system` veya `minio` olarak ayarlayın.
- MinIO kullanımı için ilgili bağlantı bilgilerini ayarlayın.

## Çoklu Modül Yapısı
- `storage-filesystem` ve `storage-minio` modülleri bağımsız olarak build edilip Maven repository'ye deploy edilebilir.

## Geliştirici Notları
- Tüm bağımlılıklar Maven ile otomatik kurulur.
- Uygulama Docker ile kolayca taşınabilir.
- Geçersiz isteklerde anlamlı hata mesajları ve uygun HTTP kodları döner.

## Katkı ve Lisans
MIT Lisansı. Katkılarınızı bekliyoruz!
