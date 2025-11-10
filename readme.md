# 💊 Gestion de Pharmacie Scolaire

Application Java Swing pour la gestion des médicaments, des patients (élèves et enseignants), et des délivrances au sein d'une pharmacie d'établissement scolaire.

## Fonctionnalités

- 🔑 Connexion administrateur / infirmière
- 💊 Gestion des médicaments (ajout, modification, suppression, liste)
- 👥 Gestion des patients (élèves et enseignants)
- 🧾 Gestion des délivrances de médicaments
- ⚠️ Système d’alertes (périmés, bientôt expirés, stock faible)
- 📊 Tableau de bord avec statistiques et rapports

## Structure du Projet

src/
├── model/ → Classes POJO (entités)
├── controller/ → Logique métier et accès à la base de données
├── view/ → Interfaces graphiques Swing
└── main/ → Point d'entrée principal (App.java)


## Prérequis

- Java JDK 8 ou supérieur  
- PostgreSQL  
- Driver PostgreSQL JDBC (inclus dans `lib/postgresql-42.7.7.jar`)

## Pour importer la base
```bash
psql -U postgres -d pharmacie_scolaire -f pharmacie.sql
***Si vous n'avez pas encore créé la base :***
```bash
- createdb -U postgres pharmacie_scolaire
- psql -U postgres -d pharmacie_scolaire -f pharmacie.sql
```

## Configuration de la Base de Données
```sql
CREATE DATABASE pharmacie_scolaire;
\c pharmacie_scolaire;

-- Table des utilisateurs (admin, infirmière)
CREATE TABLE utilisateur (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(30) NOT NULL
);

-- Table des médicaments
CREATE TABLE medicament (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    categorie VARCHAR(50),
    quantite_stock INT DEFAULT 0,
    date_peremption DATE NOT NULL
);

-- Table des patients
CREATE TABLE patient (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100),
    type_patient VARCHAR(50) CHECK (type_patient IN ('eleve', 'enseignant'))
);

-- Table des délivrances
CREATE TABLE delivrance (
    id SERIAL PRIMARY KEY,
    medicament_id INT REFERENCES medicament(id) ON DELETE CASCADE,
    patient_id INT REFERENCES patient(id) ON DELETE CASCADE,
    quantite INT NOT NULL,
    date_delivrance DATE DEFAULT CURRENT_DATE,
    utilisateur_id INT REFERENCES utilisateur(id) ON DELETE SET NULL
);
```
*** Créez un utilisateur administrateur et une infirmière :***
```sql
INSERT INTO utilisateur (username, password, role) VALUES 
('admin', 'admin123', 'admin'),
('infirmiere', 'inf123', 'infirmiere');
```

***Configurez les paramètres de connexion dans src/controller/DBConnection.java :***
```
URL: jdbc:postgresql://localhost:5432/pharmacie_scolaire
User: postgres
Password: 1234 (à modifier selon votre configuration)
```

## Compilation **Linux/Mac:**
bash
javac -cp "lib/postgresql-42.7.7.jar" -d out $(find src -name "*.java")
**Windows:**
cmd
javac -cp "lib/postgresql-42.7.7.jar" -d out -sourcepath src src\**\*.java
## Exécution **Linux/Mac (utilisez : comme séparateur):**
bash
java -cp "out:lib/postgresql-42.7.7.jar" . main.App
**Windows (utilisez ; comme séparateur):**
cmd
java -cp "out;lib/postgresql-42.7.7.jar" main.App