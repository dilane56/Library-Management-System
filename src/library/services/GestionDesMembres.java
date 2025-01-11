package library.services;

import library.*;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class GestionDesMembres {



    Connection conn = library.connection.DatabaseConnection.connect();
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
                    LocalDate dateAdhesion = LocalDate.now();
                    Membre membre = new Membre(nom, prenom, email, dateAdhesion);
                    membreDAO.ajouterMembre(membre);
                }
                case "2" -> {
                    System.out.print("Entrer l'id du membre a supprimer : ");
                    int id = scanner.nextInt();
                    membreDAO.supprimerMembre(id);
                }
                case "3" -> {
                    System.out.print("Entrer lID du Membre A Modifier: ");
                    int idMembre = scanner.nextInt();
                    scanner.nextLine();
                    Membre membre = membreDAO.recupererUnMembre(idMembre);
                    System.out.println(membre);
                    if(membre!= null){
                        String choix;

                        do {
                            System.out.println("Quelle l'information du Membre que Voulez Vous Modifier");
                            System.out.println("1 - Modifier le NOM du Membre");
                            System.out.println("2 - Modifer le PRENOM du membre ");
                            System.out.println("3 - Modifier L'EMAIL Du Membre");
                            System.out.println("0 - Quitter");
                            System.out.print("Entrer Votre Choix : ");
                            choix = scanner.nextLine();
                            switch (choix) {
                                case "1" -> {
                                    System.out.print("Entrer le nouveau  NOM du Membre: ");
                                    String newName = scanner.nextLine();
                                    membre.setNom(newName);
                                    System.out.println(membre);
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
                                    membre.setEmail(newEmail);
                                    membreDAO.modifierMembre(membre, idMembre);
                                }
                                default -> System.out.println("Choix incorrect");
                            }



                        } while (!choix.equals("0"));

                    }

                }
                case "4" -> {
                    System.out.println("Entrer le nom du Membre que vous chercher");
                    String nom = scanner.nextLine();
                    List<Membre> membreList = membreDAO.rechercherMembreParNom(nom);
                    membreDAO.afficherListeDeMembre(membreList);

                }
                case "5" -> {
                    System.out.print("Entrer l'id du Membre Dont Vous Voulez les Details: ");
                    int idMembre = scanner.nextInt();
                    scanner.nextLine();
                    Membre membre = membreDAO.recupererUnMembre(idMembre);
                    if(membre!= null){
                        membre.afficherDetails();//Afficher les details du membre
                    }else{
                        System.out.println("Aucun membre trouver avec l'id : "+ idMembre);
                    }
                }
                case "6" -> {
                    List<Membre> membresList = membreDAO.listerTousLesMembres();
                    membreDAO.afficherListeDeMembre(membresList);
                }
                default -> System.out.println("Choix Incorrect");
            }


        }while(!completeMemberManagement.equals("0"));
    }
}
