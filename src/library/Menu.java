package library;



public class Menu {
    public static void afficherMenuPrincipal(){
        System.out.println("************************************************************");
        System.out.println("1. Gestion des livres");
        System.out.println("2. Gestion des Membres");
        System.out.println("3. Gestion des Emprunts ");
        System.out.println("0. Quitter");
        System.out.println("************************************************************");
        System.out.print("Entrez votre choix : ");

    }
    public static void afficherMenuDeGestionDesLivres(){
        System.out.println("====== Bienvenue sur le menu de Gestion des Livres ======");
        System.out.println("1. Ajouter un livre");
        System.out.println("2. Supprimer un Livre");
        System.out.println("3. Modifier les Informations d'un Livre");
        System.out.println("4. Chercher des Livre a l'aide du Titre");
        System.out.println("5. Chercher des Livre a l'aide du nom de l'Auteur");
        System.out.println("6. Chercher des Livre à l'aide de leurs Catégorie");
        System.out.println("7. Afficher tous les Livres disponibles");
        System.out.println("8. Afficher les Details d'un Livre ");
        System.out.println("0. Precedent");
        System.out.println("============================================================");

        System.out.print("Entrer votre choix : ");
    }
     public static void afficherMenuDeGestionDesMembres(){
         System.out.println("####### Bienvenue sur le Menu de Gestion des Membres #######");
         System.out.println("1. Inscrire Un Nouveau Membre");
         System.out.println("2. Supprimer un Membre");
         System.out.println("3. Modifier Les Informations D'un Membre");
         System.out.println("4. Rechercher Un Membre à L'aide De Son Nom");
         System.out.println("5. Afficher les Details d'un Membre");
         System.out.println("6. Afficher Tous les Membres");
         System.out.println("0. Precedent");
         System.out.println("#############################################################");
         System.out.print("Entrer votre choix : ");

     }

     public static void afficherMenuDeGestionDesEmprunt(){
         System.out.println("+++++++ Bienvenue sur le Menu de Gestion des Emprunts ++++++++");
         System.out.println("1. Enregistrer Un Emprunt De Livre");
         System.out.println("2. Indiquer Le Retours D'un Livre");
         System.out.println("3. Modifier Informations Emprunt");
         System.out.println("4. Supprimer Emprunt");
         System.out.println("5. Lister tous les Emprunts");
         System.out.println("6. Afficher Les Details D'un Emprunts");
         System.out.println("7. Afficher Tous Les Emprunts Qui Ne Sont Pas Encore Terminer");
         System.out.println("8. Afficher Tous Les Emprunts En Retard");
         System.out.println("9. Afficher Tous les Emprunts Terminés Ayant des Pénalités");
         System.out.println("0. Precedent");
         System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
         System.out.print("Entrer Votre Choix : ");
     }

    public  static  void modifierLivreMenu(){

        System.out.println("<><><><><><> Quel Information Voulez Vous Modifer <><><><><><><><>");
        System.out.println("1- Le Titre");
        System.out.println("2- L'Auteur");
        System.out.println("3- La Categorie");
        System.out.println("4- Le Nombre D'exemplaire");
        System.out.println("0- Retour Au Menu Precedent");
        System.out.println("<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>");
        System.out.print("Choisisiez Une  Option: ");
    }

}
