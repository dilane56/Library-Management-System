package library.services;

import library.*;
import library.connection.DatabaseConnection;
import library.validator.EmailValidator;
import library.validator.IntInputValidator;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class GestionDesMembres {



    Connection conn = DatabaseConnection.connect();
    Scanner scanner = new Scanner(System.in);
    MembreDAO membreDAO = new MembreDAO(conn);

    String completeMemberManagement;
    public void gestionDesMembres(){

        do

        {
            Menu.afficherMenuDeGestionDesMembres();
            completeMemberManagement = scanner.nextLine();
            switch (completeMemberManagement) {
                case "1" -> {
                    System.out.print("Entrer le nom du membre : ");
                    String nom = scanner.nextLine();
                    System.out.print("Entrer le Prénom du Membre : ");
                    String prenom = scanner.nextLine();
                    System.out.print("Entrer l'Email du Membre : ");
                    String email = scanner.nextLine();
                    while(!EmailValidator.isValidEmail(email)){
                        System.out.print("Le format de l'email est invalide veillez réessayer: ");
                        email = scanner.nextLine();
                    }
                    LocalDate dateAdhesion = LocalDate.now();
                    Membre membre = new Membre(nom, prenom, email, dateAdhesion);
                    membreDAO.ajouterMembre(membre);
                }
                case "2" -> {

                    int id = IntInputValidator.getIntFromUser(scanner, "Entrer l'id du membre a supprimer : " );
                    membreDAO.supprimerMembre(id);

                }
                case "3" -> {

                    int idMembre = IntInputValidator.getIntFromUser(scanner, "Entrer lID du Membre A Modifier: ");
                    Membre membre = membreDAO.recupererUnMembre(idMembre);

                    if (membre !=null){
                        membre.setId(idMembre);
                        System.out.println(membre);
                        String choix1 = "";
                        do {
                            System.out.println("Quelle Information du Membre que Voulez Vous Modifier");
                            System.out.println("1 - Modifier le NOM du Membre");
                            System.out.println("2 - Modifer le PRENOM du membre ");
                            System.out.println("3 - Modifier L'EMAIL Du Membre");
                            System.out.println("0 - Precedent");
                            System.out.print("Entrer Votre Choix : ");
                            String choix = scanner.nextLine();
                            switch (choix) {
                                case "1" -> {
                                    System.out.print("Entrer le nouveau  NOM du Membre: ");
                                    String newName = scanner.nextLine();
                                    membre.setNom(newName);
                                    membreDAO.modifierMembre(membre, idMembre);
                                }
                                case "2" -> {
                                    System.out.print("Enter Le Nouveau Prénom du Membre: ");
                                    String nouveauPrenom = scanner.nextLine();
                                    membre.setPrenom(nouveauPrenom);
                                    membreDAO.modifierMembre(membre, idMembre);
                                }
                                case "3" -> {
                                    System.out.print("Enter Le Nouvel Email Du Membre: ");
                                    String newEmail = scanner.nextLine();
                                    while(!EmailValidator.isValidEmail(newEmail)){
                                        System.out.print("Le format de l'email est invalide veillez réessayer: ");
                                        newEmail = scanner.nextLine();
                                    }
                                    membre.setEmail(newEmail);
                                    membreDAO.modifierMembre(membre, idMembre);

                                }
                                case "0" ->{
                                    choix1 ="0";
                                }
                                default -> System.out.println("Choix incorrect");
                            }
                        } while (!choix1.equals("0"));
                        }else{
                        System.out.println("Aucun Membre trouvé avec cet Id");
                    }



                }
                case "4" -> {
                    System.out.print("Entrer le nom du Membre que vous chercher: ");
                    String nom = scanner.nextLine();
                    List<Membre> membreList = membreDAO.rechercherMembreParNom(nom);
                    if (!membreList.isEmpty()){
                        System.out.println("################## Listes de Membre Trouvés ##################");
                        membreDAO.afficherListeDeMembre(membreList);
                    }
                }
                case "5" -> {
                    int idMembre = IntInputValidator.getIntFromUser(scanner, "Entrer l'id du Membre Dont Vous Voulez les Details: ");
                    Membre membre = membreDAO.recupererUnMembre(idMembre);
                    if(membre!= null){
                        System.out.println("################ Informations Sur Le Membre ##############");
                        membre.afficherDetails();//Afficher les details du membre
                    }else{
                        System.out.println("Aucun membre trouver avec l'id : "+ idMembre);
                    }
                }
                case "6" -> {
                    List<Membre> membresList = membreDAO.listerTousLesMembres();
                    if (!membresList.isEmpty()){
                        System.out.println("################# Liste De Tous Les Membres ################ ");
                        membreDAO.afficherListeDeMembre(membresList);
                    }

                }
                default -> System.out.println("Choix Incorrect");
            }


        }while(!completeMemberManagement.equals("0"));
    }
}
