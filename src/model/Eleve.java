package model;

public class Eleve {
    private int idPatient; // FK vers patient
    private String matricule;
    private String classe;

    public Eleve() {}

    public Eleve(int idPatient, String matricule, String classe) {
        this.idPatient = idPatient;
        this.matricule = matricule;
        this.classe = classe;
    }

    public int getIdPatient() { return idPatient; }
    public void setIdPatient(int idPatient) { this.idPatient = idPatient; }

    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }

    public String getClasse() { return classe; }
    public void setClasse(String classe) { this.classe = classe; }
}


