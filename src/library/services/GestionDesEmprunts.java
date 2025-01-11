package library.services;

import library.DateValidator;
import library.Emprunt;
import library.EmpruntDAO;
import library.Menu;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class GestionDesEmprunts {
    Connection conn = library.connection.DatabaseConnection.connect();
    Scanner scanner = new Scanner(System.in);
    DateValidator dateValidator = new DateValidator("yyyy-MM-dd");
    EmpruntDAO empruntDAO =new EmpruntDAO(conn);
    String completeBorrowmanagement;

    public void gestionDesEmprunt(){

        do{
            Menu.afficherMenuDeGestionDesEmprunt();
            completeBorrowmanagement = scanner.nextLine();
            switch (completeBorrowmanagement) {
                case "1" -> {
                    System.out.print("Entrer L id Du Membre Qui Emprunte Le Livre : ");
                    int membreId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Entrer L'id Du Livre A Emprunter : ");
                    int livreId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter la Date De Retour Prévue Du Livre (YYYY-MM-DD): ");
                    String dateRetourPrevue = scanner.nextLine();
                    if (dateValidator.isValid((dateRetourPrevue))) {
                        LocalDate dateEmprunt = LocalDate.now();
                        Emprunt emprunt = new Emprunt(livreId, membreId, dateEmprunt, dateRetourPrevue);
                        empruntDAO.enregistrerEmprunt(emprunt);

                    } else {
                        System.out.println("le format de la date entrée est incorrect");
                    }
                }
                case "2" -> {
                    System.out.print("Entrer l'Id De L' Emprunt a Retourner: ");
                    int idEmprunt = scanner.nextInt();
                    scanner.nextLine();
                    Emprunt emprunt = empruntDAO.recupererEmprunt(idEmprunt);
                    if (emprunt !=null){
                        if(emprunt.getDateRetourEffective() ==null){
                            LocalDate dateRetourPrevue = LocalDate.parse(emprunt.getDateRetourPrevue());
                            Boolean statut = false;
                            LocalDate dateRetourEffective = LocalDate.now();
                            int penalite = empruntDAO.calculerPenalite(dateRetourPrevue,dateRetourEffective);
                            System.out.println("Les penalite de L'Emprunt sont de : "+penalite+ "FCFA");
                            empruntDAO.retournerLivre(idEmprunt, statut, penalite);
                        }else{
                            System.out.println("L' Emprunt que vous avez indiquer est déja TERMINER et Le Livre A deja été Retourné ");
                        }


                    }else{
                        System.out.println("Aucun emprunt trouvé avec ce ID: "+idEmprunt);
                    }

                }
                case "3" -> {
                    System.out.print("Entrer L'Id De L'Emprunt a Modifier : ");
                    int idEmprunt = scanner.nextInt();
                    scanner.nextLine();
                    Emprunt emprunt =  empruntDAO.recupererEmprunt(idEmprunt);
                    if(emprunt != null){
                        if(emprunt.getDateRetourEffective()==null || emprunt.getStatut()){
                            System.out.print("Entrer Le Nouvel Id du Livre A Emprunter : ");
                            int idLivre = scanner.nextInt();
                            scanner.nextLine();
                            System.out.print("Entrer Le Nouvel Id Du Membre Qui Emprunt Le Livre : ");
                            int idMembre = scanner.nextInt();
                            scanner.nextLine();
                            System.out.print("Entrer La Nouvelle Date De Retour Prévue (YYYY-MM-DD) : ");
                            String dateRetourPrevue = scanner.nextLine();
                            if (dateValidator.isValid(dateRetourPrevue)) {
                                empruntDAO.modifierEmprunt(idEmprunt, idMembre, idLivre, dateRetourPrevue);
                            } else {
                                System.out.println("Format De Date Incorrect");
                            }
                        }else{
                            System.out.println("Impossible De Modifier Les Informations De L'Emprunt Car il est deja Terminer");
                        }

                    }else {
                        System.out.println("Aucun Emprunt Trouvé avec l'id: "+idEmprunt);
                    }


                }
                case "4" -> {
                    System.out.println("Entrer L'id de L' Emprunt Que Vous Voulez Supprimer");
                    int idEmprunt = scanner.nextInt();
                    scanner.nextLine();
                    empruntDAO.supprimerEmprunt(idEmprunt);
                }
                case "5" -> {
                    List<Emprunt> emprunts = empruntDAO.afficherTousLesEmprunts();
                    empruntDAO.afficherListeEmprunt(emprunts);

                }
                case "7" -> {
                    List<Emprunt> empruntEncours = empruntDAO.afficherTousLesEmpruntsEncour();
                    empruntDAO.afficherListeEmprunt(empruntEncours);
                }
                case "6" ->{
                    System.out.print("Entrer l'Id de L'Emprunt Dont Vous Voulez Les Details: ");
                    int idEmprunt= scanner.nextInt();
                    scanner.nextLine();
                    Emprunt emprunt = empruntDAO.recupererEmprunt(idEmprunt);
                    if(emprunt!=null){
                        int idLivre = emprunt.getLivreId();
                        int membreId = emprunt.getMembreId();
                        Emprunt empruntDetails = empruntDAO.recupererDetailsEmprunts(membreId,idLivre);
                        empruntDetails.afficherDetails();//Affiche-les details de l’Emprunt
                    }else {
                        System.out.println("Aucun Emprunt trouvé avec cet Id: "+ idEmprunt);
                    }

                }
                case "8" ->{
                    System.out.println("******* Liste des Emprunts En Retard De Retour *******");
                    List<Emprunt> empruntsEnRetard = empruntDAO.afficherTousLesEmpruntsEnRetard();
                    empruntDAO.afficherListeEmprunt(empruntsEnRetard);
                }
                case "9" ->{
                    System.out.println("******* Liste Des Emprunts Pénalisés");
                    List<Emprunt> empruntsPenalise = empruntDAO.AfficherEmpruntsPenalise();
                    empruntDAO.afficherListeEmprunt(empruntsPenalise);
                }
                default -> System.out.println("Choix incorrect");
            }
        }while (!completeBorrowmanagement.equals("0"));
    }
}
