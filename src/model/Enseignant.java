package model;

public class Enseignant {
    private int idPatient; // FK vers patient
    private String departement;
    private String fonction;

    public Enseignant() {}

    public Enseignant(int idPatient, String departement, String fonction) {
        this.idPatient = idPatient;
        this.departement = departement;
        this.fonction = fonction;
    }

    public int getIdPatient() { return idPatient; }
    public void setIdPatient(int idPatient) { this.idPatient = idPatient; }

    public String getDepartement() { return departement; }
    public void setDepartement(String departement) { this.departement = departement; }

    public String getFonction() { return fonction; }
    public void setFonction(String fonction) { this.fonction = fonction; }
}


