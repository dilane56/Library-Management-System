package library;

import library.interfaces.Afficher;


import java.time.LocalDate;

public class Emprunt implements Afficher {
    private int idEmprunt;
    private int membreId;
    private int livreId;
    private LocalDate dateEmprunt;
    private String dateRetourPrevue;
    private LocalDate dateRetourEffective;
    private  Boolean statut;

    private int penalite;
    private String titre_livre;
    private String nom_membre;


    // Constructeur, getters et setters

    public Emprunt(int idEmprunt, int membreId, int livreId, LocalDate dateEmprunt, String dateRetourPrevue, LocalDate dateRetourEffective, Boolean statut ,int penalite) {
        this.idEmprunt = idEmprunt;
        this.membreId = membreId;
        this.livreId = livreId;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = dateRetourPrevue;
        this.dateRetourEffective = dateRetourEffective;
        this.statut = statut;
        this.penalite = penalite;
    }

    public Emprunt( int membreId, int livreId, LocalDate dateEmprunt, String dateRetourPrevue) {

        this.membreId = membreId;
        this.livreId = livreId;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = dateRetourPrevue;


    }

    public Emprunt(String nom_membre , String titre_livre ,LocalDate dateEmprunt, String dateRetourPrevue, Boolean statut, LocalDate dateRetourEffective, int penalite) {
         this.nom_membre = nom_membre;
         this.titre_livre = titre_livre;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = dateRetourPrevue;
        this.dateRetourEffective = dateRetourEffective;
        this.statut = statut;
        this.penalite = penalite;
    }

    public Emprunt() {

    }

    public int getIdEmprunt() {
        return idEmprunt;
    }

    public void setIdEmprunt(int idEmprunt) {
        this.idEmprunt = idEmprunt;
    }

    public int getMembreId() {
        return membreId;
    }

    public void setMembreId(int membreId) {
        this.membreId = membreId;
    }

    public int getLivreId() {
        return livreId;
    }

    public void setLivreId(int livreId) {
        this.livreId = livreId;
    }

    public LocalDate getDateEmprunt() {
        return dateEmprunt;
    }

    public void setDateEmprunt(LocalDate dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
    }

    public String getDateRetourPrevue() {
        return dateRetourPrevue;
    }

    public void setDateRetourPrevue(String dateRetourPrevue) {
        this.dateRetourPrevue = dateRetourPrevue;
    }

    public Boolean getStatut() {
        return statut;
    }

    public void setStatut(Boolean statut) {
        this.statut = statut;
    }

    public LocalDate getDateRetourEffective() {
        return dateRetourEffective;
    }

    public int getPenalite() {
        return penalite;
    }

    public void setPenalite(int penalite) {
        this.penalite = penalite;
    }

    public void setDateRetourEffective(LocalDate dateRetourEffective) {
        this.dateRetourEffective = dateRetourEffective;
    }

    @Override
    public String toString() {
        return "Emprunt{" +
                "idEmprunt=" + idEmprunt +
                ", livreId=" + livreId +
                ", membreId="+ membreId +
                ", dateEmprunt=" + dateEmprunt +
                ", dateRetourPrevue='" + dateRetourPrevue + '\'' +
                ", dateRetourEffective=" + dateRetourEffective +
                ", statut=" +(statut ? "EN COURS" : "TERMINER") +
                ", penalite=" + penalite +
                '}';
    }

    @Override
    public void afficherDetails() {
        System.out.println("- Nom Membre: " + nom_membre + "\n- Titre Livre: " + titre_livre+ "\n- Date Emprunt: "+ dateEmprunt + " \n- Date Retour Prévue: "
                + dateRetourPrevue +"\n- Date Retour Effective : "+ dateRetourEffective + "\n- Statut: "+ (statut? "EN COURS":"TERMINER" )+ "\n- Pénalités: "+ penalite);
    }
}
