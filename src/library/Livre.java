package library;

import library.interfaces.Afficher;

public class Livre implements Afficher {
    private int id;
    private String titre;
    private String auteur;
    private String categorie;
    private int nombreExemplaires;

    public Livre( String titre, String auteur, String categorie, int nombreExemplaires) {

        this.titre = titre;
        this.auteur = auteur;
        this.categorie = categorie;
        this.nombreExemplaires = nombreExemplaires;
    }
    public Livre(int id, String titre, String auteur, String categorie, int nombreExemplaires) {
        this.id = id;
        this.titre = titre;
        this.auteur = auteur;
        this.categorie = categorie;
        this.nombreExemplaires = nombreExemplaires;
    }

    public Livre() {

    }

    public int getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public String getCategorie() {
        return categorie;
    }

    public int getNombreExemplaires() {
        return nombreExemplaires;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public void setNombreExemplaires(int nombreExemplaires) {
        this.nombreExemplaires = nombreExemplaires;
    }

    @Override
    public String toString() {
        return "Livre{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", auteur='" + auteur + '\'' +
                ", categorie='" + categorie + '\'' +
                ", nombreExemplaires=" + nombreExemplaires +
                '}';
    }


    @Override
    public void afficherDetails() {
        System.out.println("//////////// Informations Sur Le Livre ////////////");
        System.out.println(" - Titre Du Livre: " + titre + "\n - Auteur: " + auteur+ "\n - categorie: "+ categorie + " \n - nbrExemplaire: "+ nombreExemplaires);
        System.out.println("////////////////////////////////////////////////////");

    }
}
