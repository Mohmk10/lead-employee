# 🏢 Lead Employee

Application **Java 21 / Maven** permettant la **gestion d’entreprise** avec plusieurs rôles : **Admin**, **Manager** et **Développeur**, bâtie selon les bonnes pratiques (JPA, Repository, Service, View, Factory Pattern).

---

## 🚀 Fonctionnalités principales

- 👨‍💼 **Admin** :
  - Crée des services  
  - Crée des employés (Managers / Développeurs)  
  - Liste les employés (tous / par service)  
  - Consulte les services (avec manager et nombre d’employés)

- 🧑‍💻 **Manager** :
  - Liste les employés de son service  
  - Filtre les employés par spécialité  

- 🧩 **Super Admin (console principale)** :
  - Crée des Admins  
  - Permet la connexion comme Admin ou Manager (avec validation des IDs)

---

## 🧠 Architecture du projet

ead-employee/
├── pom.xml
├── src/
│ ├── main/
│ │ ├── java/com/entreprise/
│ │ │ ├── config/ → Configuration JPA
│ │ │ ├── entity/ → Entités JPA (Admin, Manager, Developer, Service…)
│ │ │ ├── repository/ → Repositories (implémentation JPA)
│ │ │ ├── services/ → Couche métier (avec validations)
│ │ │ ├── views/ → Interface console (MVC)
│ │ │ └── container/ → Factory (instanciation des couches)
│ │ └── resources/
│ │ ├── META-INF/persistence.xml
│ │ ├── app.properties
│ │ ├── app-example.properties
│ │ └── db/migration/ → Scripts SQL initiaux
└── target/ → fichiers compilés (non versionnés)


---

## ⚙️ Technologies utilisées

- **Java 21 (LTS)**  
- **Maven 3.9+**  
- **Hibernate ORM 6.5**  
- **JPA / HikariCP**  
- **PostgreSQL (Neon)**  
- **Design Pattern : Factory**  
- **Console IO (pure Java)**  

---

## 🧰 Installation & exécution

### 1️⃣ Cloner le projet
```bash
git clone https://github.com/<ton-utilisateur>/lead-employee.git
cd lead-employee

2️⃣ Configurer la base de données

Créer un fichier :

src/main/resources/app.properties


Et y copier le contenu de app-example.properties, puis remplacer :

hibernate.connection.url=jdbc:postgresql://<host>/<database>?sslmode=require&channelBinding=require
hibernate.connection.username=<username>
hibernate.connection.password=<password>

3️⃣ Compiler le projet
mvn clean compile

4️⃣ Exécuter l’application
mvn exec:java -Dexec.mainClass="com.entreprise.views.Main"

🧱 Exemples de menus
Super Admin
===== MENU SUPER ADMIN =====
1 - Créer un Admin
2 - Se connecter
3 - Quitter

Admin
===== MENU ADMIN =====
1 - Créer un service
2 - Lister les services (manager + nb employés)
3 - Créer un employé (Manager / Développeur)
4 - Lister employés (tous | par service)
5 - Quitter (retour)

Manager
===== MENU MANAGER =====
1 - Lister employés de mon service
2 - Lister employés par spécialité
3 - Quitter (retour)

🧩 Design Pattern appliqué
🏭 Factory Pattern

Tous les services, repositories et vues sont instanciés via la classe Factory, permettant :

une centralisation des dépendances ;

une meilleure lisibilité du Main ;

une isolation claire entre les couches.

🔐 Sécurité & Validation

Validation stricte des IDs (Admin & Manager).

Contrôle des doublons de téléphone.

Vérification des salaires / primes non négatifs.

Un seul Manager par Service.

🧪 Migration SQL
src/main/resources/db/migration/


V1__init.sql → création des tables.

V2__seed_services.sql → insertion des services par défaut.

📦 Structure Maven

GroupId : com.entreprise

ArtifactId : lead-employee

Version : 1.0.0

🧑‍💻 Auteur

Mohamed M. Kouyaté
Développeur Fullstack Java / TypeScript
📍 Dakar, Sénégal
🔗 GitHub – Mohmk10

📝 Licence

Distribué sous licence MIT.