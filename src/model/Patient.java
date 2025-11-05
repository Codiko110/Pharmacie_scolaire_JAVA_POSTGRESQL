package model;

import java.sql.Date;

public class Patient {
    private int id;
    private String nom;
    private char sexe; // 'M' ou 'F'
    private Date dateNaissance;
    private String typePatient; // Eleve ou Enseignant

    public Patient() {}

    public Patient(int id, String nom, char sexe, Date dateNaissance, String typePatient) {
        this.id = id;
        this.nom = nom;
        this.sexe = sexe;
        this.dateNaissance = dateNaissance;
        this.typePatient = typePatient;
    }

    public Patient(String nom, char sexe, Date dateNaissance, String typePatient) {
        this.nom = nom;
        this.sexe = sexe;
        this.dateNaissance = dateNaissance;
        this.typePatient = typePatient;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public char getSexe() { return sexe; }
    public void setSexe(char sexe) { this.sexe = sexe; }

    public Date getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(Date dateNaissance) { this.dateNaissance = dateNaissance; }

    public String getTypePatient() { return typePatient; }
    public void setTypePatient(String typePatient) { this.typePatient = typePatient; }
}


