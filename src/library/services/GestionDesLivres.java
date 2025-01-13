package library.services;

import library.Livre;
import library.LivreDAO;
import library.Menu;
import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class GestionDesLivres {
    static Connection conn = library.connection.DatabaseConnection.connect();
    static Scanner scanner = new Scanner(System.in);
    static String completeBookManagement;
    static LivreDAO livreDAO = new LivreDAO(conn);


    public static void gestionDesLivres(){

        do{
            Menu.afficherMenuDeGestionDesLivres();
            completeBookManagement =scanner.nextLine();
            switch (completeBookManagement){
                case "1":
                {
                    System.out.print("Entrez le titre du livre : ");
                    String titre = scanner.nextLine();
                    System.out.print("Entrez l'auteur du livre : ");
                    String auteur = scanner.nextLine();
                    System.out.print("Entrez la categorie du livre : ");
                    String categorie = scanner.nextLine();
                    System.out.print("Entrez le nombre d'exemplaire du livre: ");
                    int nbrExemplaires = scanner.nextInt();
                    scanner.nextLine(); // Consommer la nouvelle ligne
                    Livre livre = new Livre( titre, auteur,categorie, nbrExemplaires );
                    livreDAO.ajouterLivre(livre);
                    break;
                }
                case "2":
                {
                    System.out.print("Entrer l'id du livre a supprimer: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    livreDAO.supprimerLivre(id);
                    break;
                }
                case "4":
                {
                    System.out.print("Entrer le titre du livre : ");
                    String title = scanner.nextLine();
                    List<Livre> livres = livreDAO.chercherParTitre(title);
                    livreDAO.afficherListeDeLivre(livres);
                    break;
                }
                case "5":
                {
                    System.out.println("Entrer l' auteur du Livre: ");
                    String auteur =scanner.nextLine();
                    List<Livre> livres = livreDAO.chercherParAuteur(auteur);
                    livreDAO.afficherListeDeLivre(livres);
                    break;
                }
                case "6":
                {
                    System.out.println("Entrer la Categorie du Livre : ");
                    String categorie = scanner.nextLine();
                    List<Livre> livres = livreDAO.chercherParCategorie(categorie);
                    livreDAO.afficherListeDeLivre(livres);
                    break;
                }
                case "7":
                {
                    System.out.println("Liste des Livres");
                    List<Livre>livres;
                    livres = livreDAO.afficherTousLesLivres();
                    livreDAO.afficherListeDeLivre(livres);
                    break;
                }
                case "8":
                {
                    System.out.print("Entrer l'ID du Livre d'ont vous voulez afficher les Details : ");
                    int idLivre = scanner.nextInt();
                    scanner.nextLine();
                    Livre livre = livreDAO.recupererUnLivre(idLivre);
                    if(livre != null){
                        livre.afficherDetails();//Afficher les details du livre
                    }else {
                        System.out.println("Le livre avec l'id "+ idLivre +" n'existe pas");
                    }
                    break;
                }
                case"3":
                {
                    System.out.print("Entrer l'id du livre a modifier: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    if(livreDAO.recupererUnLivre(id)!= null){
                        Livre livre = livreDAO.recupererUnLivre(id);
                        livre.setId(id);
                        livre.afficherDetails();
                        boolean updateComplete= false;
                        while(!updateComplete){
                            Menu.modifierLivreMenu();
                            String update =scanner.nextLine();
                            switch (update){
                                case "1" :
                                {
                                    System.out.print("Entrer le nouveau titre du livre: ");
                                    String titre = scanner.nextLine();
                                    livre.setTitre(titre);
                                    livreDAO.modifierLivre(livre);
                                    break;
                                }
                                case "2":
                                {
                                    System.out.print("Entrer le Nouveau non de l'Auteur: ");
                                    String auteur = scanner.nextLine();
                                    livre.setAuteur(auteur);
                                    livreDAO.modifierLivre(livre);
                                    break;
                                }
                                case "3":
                                {
                                    System.out.print("Enter la Nouvelle Catégorie: ");
                                    String categorie =scanner.nextLine();
                                    livre.setCategorie(categorie);
                                    livreDAO.modifierLivre(livre);
                                    break;
                                }
                                case "4":
                                {
                                    System.out.print("Entrer le  nouveau nombre d'exemplaire: ");
                                    int nbrExemplaire= scanner.nextInt();
                                    scanner.nextLine();
                                    livre.setNombreExemplaires(nbrExemplaire);
                                    livreDAO.modifierLivre(livre);
                                    break;
                                }
                                case "0":{
                                    updateComplete= true;
                                    break;
                                }
                                default:
                                {
                                    System.out.println("Choix Incorrect");
                                    break;
                                }

                            }

                        }





                    }else {
                        System.out.println("Aucun livre trouver avec cet Id: "+id);
                    }
                    break;
                }
                default:
                    System.out.println("Choix incorrect");
                    break;


            }

        }while (!completeBookManagement.equals("0"));
    }
}