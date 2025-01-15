package library.services;

import library.*;
import library.connection.DatabaseConnection;
import library.validator.DateValidator;
import library.validator.IntInputValidator;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class GestionDesEmprunts {
    Connection conn = DatabaseConnection.connect();
    Scanner scanner = new Scanner(System.in);
    DateValidator dateValidator = new DateValidator("yyyy-MM-dd");
    EmpruntDAO empruntDAO = new EmpruntDAO(conn);
    String completeBorrowmanagement;
    MembreDAO membreDAO = new MembreDAO(conn);
    LivreDAO livreDAO = new LivreDAO(conn);

    public void gestionDesEmprunt() {

        do {
            Menu.afficherMenuDeGestionDesEmprunt();
            completeBorrowmanagement = scanner.nextLine();
            switch (completeBorrowmanagement) {
                case "1" -> {
                    int membreId;
                    int livreId;
                    LocalDate dateRetourPrevue;

                    while (true) {
                        membreId = IntInputValidator.getIntFromUser(scanner, "Entrer L id Du Membre Qui Emprunte Le Livre : ");
                        Membre membre = membreDAO.recupererUnMembre(membreId);
                        if (membre != null) {
                            break;
                        } else {
                            System.out.println("Aucun membre trouver avec cet ID: " + membreId);
                        }

                    }
                    while (true) {
                        livreId = IntInputValidator.getIntFromUser(scanner, "Entrer L'id Du Livre A Emprunter : ");
                        Livre livre = livreDAO.recupererUnLivre(livreId);
                        if (livre != null) {
                            int nbrExemplaire = livre.getNombreExemplaires();
                            if (nbrExemplaire > 0) {
                                break;
                            } else {
                                System.out.println("On ne dispose plus d'exemplaire de ce livre veillez essayer un autre");
                            }

                        } else {
                            System.out.println("Aucun Livre Trouver avec cet ID: " + livreId);
                        }
                    }
                    System.out.print("Enter la Date De Retour Prévue Du Livre (YYYY-MM-DD): ");
                    String dateInput = scanner.nextLine();
                    while (!dateValidator.isValid(dateInput)) {
                        System.out.print("le format de la date entrée est incorrect veillez réessayer: ");
                        dateInput = scanner.nextLine();
                    }
                    dateRetourPrevue = LocalDate.parse(dateInput);
                    while (!dateRetourPrevue.isAfter(LocalDate.now())) {
                        System.out.print("La date de retour prévue doit être supérieure à la date du jour. Veuillez réessayer: ");
                        dateRetourPrevue = LocalDate.parse(scanner.nextLine());
                    }
                    LocalDate dateEmprunt = LocalDate.now();
                    String dateRetour = dateRetourPrevue.toString();
                    Emprunt emprunt = new Emprunt(membreId, livreId, dateEmprunt, dateRetour);
                    empruntDAO.enregistrerEmprunt(emprunt);


                }
                case "2" -> {

                    int idEmprunt = IntInputValidator.getIntFromUser(scanner, "Entrer l'Id De L' Emprunt a Retourner: ");
                    Emprunt emprunt = empruntDAO.recupererEmprunt(idEmprunt);
                    if (emprunt != null) {
                        LocalDate dateRetourEffective;

                        if (emprunt.getDateRetourEffective() == null) {

                            System.out.print("Entrer la date de retour effective du livre (YYYY-MM-DD) ou appuyer sur entrer pour utiliser la date d'Aujourd'hui: ");
                            String dateInput = scanner.nextLine();
                            if (dateInput.isEmpty()) {
                                dateRetourEffective = LocalDate.now();
                            } else {
                                while (!dateValidator.isValid(dateInput)) {
                                    System.out.print("le format de la date entrée est incorrect veillez réessayer: ");
                                    dateInput = scanner.nextLine();
                                }
                                LocalDate dateEmprunt = emprunt.getDateEmprunt();
                                dateRetourEffective = LocalDate.parse(dateInput);
                                while (!dateRetourEffective.isAfter(dateEmprunt)) {
                                    System.out.println("La date de retour doit être supérieure a la date d'emprunt: " + dateEmprunt);
                                    dateRetourEffective = LocalDate.parse(scanner.nextLine());
                                }

                            }
                            Boolean statut = false;
                            LocalDate dateRetourPrevue = LocalDate.parse(emprunt.getDateRetourPrevue());
                            int penalite = empruntDAO.calculerPenalite(dateRetourPrevue, dateRetourEffective);
                            System.out.println("Les penalite de L'Emprunt sont de : " + penalite + "FCFA");
                            empruntDAO.retournerLivre(dateRetourEffective, idEmprunt, statut, penalite);
                        } else {
                            System.out.println("L' Emprunt que vous avez indiquer est déja TERMINER et Le Livre A deja été Retourné ");
                        }


                    } else {
                        System.out.println("Aucun emprunt trouvé avec ce ID: " + idEmprunt);
                    }

                }
                case "3" -> {

                    int idEmprunt = IntInputValidator.getIntFromUser(scanner, "Entrer L'Id De L'Emprunt a Modifier : ");
                    Emprunt emprunt = empruntDAO.recupererEmprunt(idEmprunt);
                    if (emprunt != null) {
                        if (emprunt.getDateRetourEffective() == null || emprunt.getStatut()) {
                            System.out.println(emprunt);
                            boolean completeUpdate = false ;
                            do {

                                System.out.println("+++++ Quels Informations De L'Emprunt Voulez Vous Modifier ++++++");
                                System.out.println("1- L'Id du Membre Qui Emprunte Le Livre");
                                System.out.println("2- L'Id du Livre Qui est Emprunte");
                                System.out.println("3- La Date De Retour Prevue");
                                System.out.println("0- Precedent");
                                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                                System.out.print("Entrer Votre choix: ");
                               String choix = scanner.nextLine();
                                switch (choix) {
                                    case "1": {

                                            int idMembre = IntInputValidator.getIntFromUser(scanner, "Entrer Le Nouvel Id Du Membre Qui Emprunt Le Livre : ");
                                            Membre membre = membreDAO.recupererUnMembre(idMembre);
                                            if (membre != null) {
                                                int idLivre = emprunt.getLivreId();
                                                String dateRetourPrevue = emprunt.getDateRetourPrevue();
                                                empruntDAO.modifierEmprunt(idEmprunt, idMembre, idLivre, dateRetourPrevue);
                                                break;
                                            } else {
                                                System.out.println("Aucun membre trouver avec cet ID: " + idMembre);
                                            }


                                        break;
                                    }
                                    case "2": {

                                            int idLivre = IntInputValidator.getIntFromUser(scanner, "Entrer Le Nouvel Id du Livre A Emprunter : ");
                                            Livre livre = livreDAO.recupererUnLivre(idLivre);
                                            if (livre != null) {
                                                int nbrExemplaire = livre.getNombreExemplaires();
                                                if (nbrExemplaire > 0) {
                                                    int idMembre = emprunt.getMembreId();
                                                    String dateRetourPrevue = emprunt.getDateRetourPrevue();
                                                    empruntDAO.modifierEmprunt(idEmprunt, idMembre, idLivre, dateRetourPrevue);
                                                    break;

                                                } else {
                                                    System.out.println("On ne dispose plus d'exemplaire de ce livre veillez essayer un autre");
                                                }

                                            } else {
                                                System.out.println("Aucun Livre Trouver avec cet ID: " + idLivre);
                                            }

                                        break;
                                    }
                                    case "3": {
                                        System.out.print("Entrer La Nouvelle Date De Retour Prévue (YYYY-MM-DD) : ");
                                        String dateInput = scanner.nextLine();
                                        if (dateValidator.isValid(dateInput)) {
                                            LocalDate dateEmprunt =emprunt.getDateEmprunt();
                                            LocalDate dateRetourPrevue = LocalDate.parse(dateInput);
                                             int idMembre = emprunt.getMembreId();
                                          int idLivre = emprunt.getLivreId();
                                            if(dateRetourPrevue.isAfter(dateEmprunt)) {
                                                empruntDAO.modifierEmprunt(idEmprunt, idMembre, idLivre, String.valueOf(dateRetourPrevue));
                                                break;
                                            }

                                        } else {
                                            System.out.println("Format De Date Incorrect");
                                        }
                                        break;
                                    }
                                    case "0": {
                                        completeUpdate = true ;
                                        break;
                                    }
                                    default:{
                                        System.out.println("choix incorrecte");
                                    }
                                }


                            } while (!completeUpdate);
                        } else {
                            System.out.println("Impossible De Modifier Les Informations De L'Emprunt Car il est deja Terminer");
                        }

                    } else {
                        System.out.println("Aucun Emprunt Trouvé avec l'id: " + idEmprunt);
                    }


                }
                case "4" -> {

                    int idEmprunt = IntInputValidator.getIntFromUser(scanner, "Entrer L'id de L' Emprunt Que Vous Voulez Supprimer: ");
                    Emprunt emprunt = empruntDAO.recupererEmprunt(idEmprunt);
                    if (emprunt != null) {
                        String statut;
                        if(emprunt.getStatut()){
                            boolean completDelete = false;
                            while (!completDelete) {
                                System.out.println("Etes vous sur de vouloir Supprimer cet Emprunt Car Il est Encore En Cours");
                                System.out.println("1 - OUi");
                                System.out.println("2 - Non");
                                System.out.print("Entrer votre Decision : ");
                                String decision = scanner.nextLine();
                                switch (decision) {
                                    case "1": {
                                        empruntDAO.supprimerEmprunt(idEmprunt);
                                        completDelete = true;
                                        break;
                                    }
                                    case "2": {
                                        completDelete = true ;
                                        break;
                                    }
                                    default:{
                                        System.out.println("choix incorrect");
                                        break;
                                    }
                                }
                            }
                        }


                    }else{
                        System.out.println("Aucun Emprunt Trouver Avec cet ID: " + idEmprunt);
                    }


                }
                case "5" -> {
                    List<Emprunt> emprunts = empruntDAO.afficherTousLesEmprunts();
                    if (!emprunts.isEmpty()) {
                        System.out.println("++++++++++++ Liste de Tous Les Emprunts Enregistrer ++++++++++++");
                        empruntDAO.afficherListeEmprunt(emprunts);
                    } else {
                        System.out.println("Vous N'avez Pas encore enregistrer d'emprunt");
                    }


                }
                case "7" -> {
                    List<Emprunt> empruntEncours = empruntDAO.afficherTousLesEmpruntsEncour();
                    if (!empruntEncours.isEmpty()) {
                        System.out.println("++++++++++++++++ Liste Des Emprunts En Cours ++++++++++++++++");
                        empruntDAO.afficherListeEmprunt(empruntEncours);
                        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    }else{
                        System.out.println("Aucun Emprunt Trouvé");
                    }

                }
                case "6" -> {
                    int idEmprunt = IntInputValidator.getIntFromUser(scanner, "Entrer l'Id de L'Emprunt Dont Vous Voulez Les Details: ");
                    Emprunt emprunt = empruntDAO.recupererEmprunt(idEmprunt);
                    if (emprunt != null) {
                        int idLivre = emprunt.getLivreId();
                        int membreId = emprunt.getMembreId();
                        Emprunt empruntDetails = empruntDAO.recupererDetailsEmprunts(membreId, idLivre);
                        System.out.println("++++++++++++++++ Informations Sur L'Emprunt ++++++++++++++++");
                        empruntDetails.afficherDetails();//Affiche-les details de l’Emprunt
                        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    } else {
                        System.out.println("Aucun Emprunt trouvé avec cet Id: " + idEmprunt);
                    }

                }
                case "8" -> {
                    System.out.println("++++++++++ Liste des Emprunts En Retard De Retour ++++++++++");
                    List<Emprunt> empruntsEnRetard = empruntDAO.afficherTousLesEmpruntsEnRetard();
                    empruntDAO.afficherListeEmprunt(empruntsEnRetard);
                    System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                }
                case "9" -> {
                    System.out.println("++++++++++++ Liste Des Emprunts Pénalisés ++++++++++++++++++");
                    List<Emprunt> empruntsPenalise = empruntDAO.AfficherEmpruntsPenalise();
                    empruntDAO.afficherListeEmprunt(empruntsPenalise);
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                }
                case "0" -> {

                }
                default -> System.out.println("Choix incorrect");
            }
        } while (!completeBorrowmanagement.equals("0"));
    }
}
