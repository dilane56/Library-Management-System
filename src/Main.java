import library.*;

import library.connection.DatabaseInitializer;
import library.services.GestionDesLivres;
import library.services.GestionDesEmprunts;
import library.services.GestionDesMembres;
import library.validator.IntInputValidator;

//import java.sql.Connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;



public class Main {
    public static void main(String[] args) {

     /*   Connection conn = library.connection.DatabaseConnection.connect();


        if (conn != null) {
            // Initialiser la base de données
            DatabaseInitializer dbInitializer = new DatabaseInitializer(conn);
            dbInitializer.initializeDatabase();

            // Création des DAO
            LivreDAO livreDAO = new LivreDAO(conn);
            MembreDAO membreDAO = new MembreDAO(conn);
            EmpruntDAO empruntDAO = new EmpruntDAO(conn);


            // Fermer la connexion après utilisation
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("La connexion à la base de données a échoué, le programme ne peut pas continuer.");
        }
*/



        // Création des DAO



        Scanner scanner = new Scanner(System.in);

        GestionDesMembres gestionDesMembres = new GestionDesMembres();
        GestionDesEmprunts gestionDesEmprunt = new GestionDesEmprunts();
        while (true) {

            Menu.afficherMenuPrincipal();


            int choix = IntInputValidator.getIntFromUser(scanner, "Entrez votre choix : ");
            switch (choix) {
                case 1 -> // Gestion des livres
                        GestionDesLivres.gestionDesLivres();
                case 2 -> // Gestion des Membres
                        gestionDesMembres.gestionDesMembres();
                case 3 ->// Gestion des Emprunts Retard et Pénalités
                        gestionDesEmprunt.gestionDesEmprunt();
                case 0 -> { // Quitter
                    System.out.println("Au revoir !");
                    System.exit(0);
                }
                default -> System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }

    }
}