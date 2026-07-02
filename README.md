# AplhaTracer

**AlphaTracer** is een krachtige Android-applicatie ontworpen voor het realtime volgen van de aandelenmarkt, uitgebreid portfoliobeheer en geautomatiseerde prijsalerts. De applicatie is ontwikkeld met een moderne tech-stack om een naadloze en veilige gebruikerservaring te garanderen.

---

## 🚀 Kernfunctionaliteiten

* **Realtime Marktmonitoring:** Zoek naar aandelen en bekijk gedetailleerde marktanalyses.
* **Portfoliobeheer:** Volg je beleggingen, bekijk prestaties en beheer transacties (kopen/verkopen).
* **Intelligente Alerts:** Stel aangepaste prijsalerts in. Dankzij de `AlertWorker` draaien deze taken efficiënt op de achtergrond.
* **Veilige Toegang:** Geïntegreerde biometrische authenticatie voor optimale beveiliging van je financiële gegevens.
* **Responsive UI:** Volledig opgebouwd met **Jetpack Compose** voor een moderne en vloeiende interface.

---

## 🛠 Technische Architectuur

Het project hanteert een modulaire architectuur volgens het **MVVM-patroon (Model-View-ViewModel)** om de scheiding van verantwoordelijkheden te waarborgen:

* **Data Laag:** Bevat de repositories (`PortfolioRepository`, `StockRepository`) en netwerklogica (`RetrofitClient`) voor data-abstractie.
* **Domein Laag:** Beheert de datamodellen en bedrijfslogica.
* **UI Laag:** Gebaseerd op Jetpack Compose, onderverdeeld in functionele modules:
* `Auth`: Biometrische beveiliging en Token-beheer.
* `Portfolio`: Overzicht en bulk-alert beheer.
* `StockUi`: Gedetailleerde aandelendetails en analyse-schakelaars.
* `Alert`: Achtergrondservices voor prijs-notificaties.



---

## 📁 Projectstructuur

```text
app/src/main/java/com/main/alphatracer/
├── Auth/           # Biometrische authenticatie & Token-beheer
├── data/           # Repository-laag voor data-ophaling
├── model/          # Dataklassen (AlertRule, Transaction, etc.)
├── network/        # Retrofit API-configuratie
├── ui/             # Jetpack Compose schermen & ViewModels
│   ├── Alert/      # Logica voor Alert-overzicht en WorkManager
│   ├── market/     # Zoekfunctionaliteit & marktdata
│   ├── Portfolio/  # Portfolio-tracking en bulk-alerts
│   ├── StockUi/    # Analyse en detailweergaven
│   └── theme/      # Material Design theming & typografie
└── MainActivity.kt # Entry point van de applicatie

```

---

## 🛠 Aan de slag

### Vereisten

* **Android Studio** (Koala of nieuwer aanbevolen).
* **JDK 17** of hoger.
* Een Android-apparaat of emulator met **API 26+ (Android 8.0)**.

### Installatie

1. Kloon de repository naar je lokale machine:
`git clone [https://github.com/sebian-lab/AlphaTracer.git](https://github.com/sebian-lab/AlphaTracer.git)`
2. Open het project in Android Studio.
3. Wacht tot de Gradle-sync is voltooid.
4. Configureer eventuele API-keys in je `local.properties` (indien vereist door de API-services).
5. Bouw en run de app op een emulator of verbonden Android-toestel.

---

## 📅 Recente Ontwikkelingen

### DevOps & Cybersecurity Toevoegingen

#### 🛠 Automated Build Pipeline (CI/CD)
Er is een GitHub Actions-workflow geconfigureerd onder [build.yml](file:///c:/Users/xemon/Downloads/fd/intern/AlphaTracer/.github/workflows/build.yml). Bij elke push of pull-request naar de `main` branch:
1. Wordt de code uitgecheckt en wordt JDK 17 opgezet.
2. Worden er automatische kwaliteitscontroles uitgevoerd via `./gradlew lintDebug`.
3. Wordt de debug APK gecompileerd via `./gradlew assembleDebug`.
4. Worden de resulterende APK- en lint-rapport-bestanden opgeslagen als GitHub artifacts.

#### 🔐 Secure Secrets Management
Om te voorkomen dat gevoelige netwerkkonfiguraties of API base URLs hardcoded in de codebase worden opgeslagen:
1. Wordt de `API_BASE_URL` gedefinieerd in een lokaal bestand genaamd `local.properties` (dit bestand is uitgesloten van Git via `.gitignore`).
2. Gradle injecteert deze waarde tijdens het compileerproces via `buildConfigField` in de automatisch gegenereerde `BuildConfig`-klasse.
3. In [RetrofitClient.kt](file:///c:/Users/xemon/Downloads/fd/intern/AlphaTracer/app/src/main/java/com/main/alphatracer/network/RetrofitClient.kt) wordt verwezen naar `BuildConfig.API_BASE_URL` in plaats van een hardcoded string.
4. Mocht het project op CI (GitHub Actions) gebouwd worden waar `local.properties` niet aanwezig is, dan valt de build-script automatisch terug naar de standaard Cloudflare Tunnel URL om build-falen te voorkomen.

#### 🌐 Network Security Config
Om de netwerkcommunicatie te beveiligen:
1. Is er een [network_security_config.xml](file:///c:/Users/xemon/Downloads/fd/intern/AlphaTracer/app/src/main/res/xml/network_security_config.xml) toegevoegd die expliciet **cleartext traffic (HTTP) verbiedt** en HTTPS dwingt voor alle verbindingen.
2. Bevat de configuratie een implementatie-voorbeeld van **Certificate Pinning** voor de API-backend (`sculpture-marker-adequate-respective.trycloudflare.com`) om Man-in-the-Middle (MitM) aanvallen te voorkomen.
3. De configuratie is correct gekoppeld in het [AndroidManifest.xml](file:///c:/Users/xemon/Downloads/fd/intern/AlphaTracer/app/src/main/AndroidManifest.xml).

#### 🛡 Code Obfuscation (ProGuard/R8)
Om reverse-engineering door aanvallers te bemoeilijken, is code-obfuscatie ingeschakeld via [proguard-rules.pro](file:///c:/Users/xemon/Downloads/fd/intern/AlphaTracer/app/proguard-rules.pro):
- **Klasse- en Ledenaanpassing:** R8/ProGuard hernoemt klassen, variabelen en methoden naar onbetekenende letters (bijv. `a`, `b`, `c`), tenzij ze expliciet behouden moeten blijven.
- **Serialization & Data Models:** De regels behouden `@SerializedName` annotaties van Gson en de data model-klassen in `com.main.alphatracer.model` om te voorkomen dat JSON-deserialisatie faalt als gevolg van hernoemde velden.
- **Retrofit HTTP-methoden:** Specifieke regels behouden de methodesignaturen van interfaces met `@retrofit2.http.*` annotaties.

---

*Dit project wordt actief ontwikkeld en onderhouden door het **sebian-lab** .*
