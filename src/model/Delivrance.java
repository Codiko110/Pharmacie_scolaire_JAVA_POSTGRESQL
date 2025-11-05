package model;

import java.sql.Date;

public class Delivrance {
    private int id;
    private Date dateSortie;
    private int idPatient;
    private int idMedicament;
    private int quantite;
    private int idUtilisateur;
    private String motif;

    public Delivrance() {}

    public Delivrance(int id, Date dateSortie, int idPatient, int idMedicament, int quantite, int idUtilisateur, String motif) {
        this.id = id;
        this.dateSortie = dateSortie;
        this.idPatient = idPatient;
        this.idMedicament = idMedicament;
        this.quantite = quantite;
        this.idUtilisateur = idUtilisateur;
        this.motif = motif;
    }

    public Delivrance(Date dateSortie, int idPatient, int idMedicament, int quantite, int idUtilisateur, String motif) {
        this.dateSortie = dateSortie;
        this.idPatient = idPatient;
        this.idMedicament = idMedicament;
        this.quantite = quantite;
        this.idUtilisateur = idUtilisateur;
        this.motif = motif;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Date getDateSortie() { return dateSortie; }
    public void setDateSortie(Date dateSortie) { this.dateSortie = dateSortie; }
    public int getIdPatient() { return idPatient; }
    public void setIdPatient(int idPatient) { this.idPatient = idPatient; }
    public int getIdMedicament() { return idMedicament; }
    public void setIdMedicament(int idMedicament) { this.idMedicament = idMedicament; }
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    public int getIdUtilisateur() { return idUtilisateur; }
    public void setIdUtilisateur(int idUtilisateur) { this.idUtilisateur = idUtilisateur; }
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
}


