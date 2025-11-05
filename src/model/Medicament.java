package model;

import java.sql.Date;

public class Medicament {
    private int id;
    private String nom;
    private String categorie;
    private int quantiteStock;
    private Date datePeremption;

    public Medicament() {}

    public Medicament(int id, String nom, String categorie, int quantiteStock, Date datePeremption) {
        this.id = id;
        this.nom = nom;
        this.categorie = categorie;
        this.quantiteStock = quantiteStock;
        this.datePeremption = datePeremption;
    }

    public Medicament(String nom, String categorie, int quantiteStock, Date datePeremption) {
        this.nom = nom;
        this.categorie = categorie;
        this.quantiteStock = quantiteStock;
        this.datePeremption = datePeremption;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }

    public int getQuantiteStock() { return quantiteStock; }
    public void setQuantiteStock(int quantiteStock) { this.quantiteStock = quantiteStock; }

    public Date getDatePeremption() { return datePeremption; }
    public void setDatePeremption(Date datePeremption) { this.datePeremption = datePeremption; }

    public int getIdMedicament() {
        return id;
    }

    public int getQuantite() {
        return quantiteStock;
    }

    public Date getDateExpiration() {
        return datePeremption;
    }
}
