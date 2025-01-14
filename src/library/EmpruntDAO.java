package library;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;



public class EmpruntDAO {
    private final Connection connection ;


    public EmpruntDAO(Connection connection) {
        this.connection = connection;
    }

    // Méthode pour enregistrer un emprunt
    public void enregistrerEmprunt(Emprunt emprunt) {
        String sql = "INSERT INTO emprunts (membre_id, livre_id, date_emprunt, date_retour_prevue) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, emprunt.getMembreId());
            pstmt.setInt(2, emprunt.getLivreId());
            pstmt.setDate(3, java.sql.Date.valueOf(emprunt.getDateEmprunt()));
            pstmt.setDate(4, java.sql.Date.valueOf(String.valueOf(emprunt.getDateRetourPrevue())));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Emprunt enregistré avec succès.");
            } else {
                System.out.println("Erreur lors de l'enregistrement de l'emprunt.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void supprimerEmprunt(int idEmprunt){
        String sql="DELETE FROM emprunts WHERE id_emprunt=? ";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idEmprunt);
            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected >0){
                System.out.println("Emprunt Supprimer Avec Succès");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Méthode pour afficher tous les emprunts
    public List<Emprunt> afficherTousLesEmprunts() {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM emprunts";

        return getEmprunts(emprunts, sql);
    }
    // Fonction permettant d'afficher les emprunt en retard
    public List<Emprunt>afficherTousLesEmpruntsEnRetard(){
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM emprunts WHERE date_retour_prevue < CURRENT_DATE AND statut = TRUE ";
        return getEmprunts(emprunts, sql);
    }

    // Fonction permettant d’afficher tous les emprunts ayant été pénalisés
    public  List<Emprunt> AfficherEmpruntsPenalise(){
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM emprunts WHERE penalite > 0 ";
        return getEmprunts(emprunts, sql);
    }

    // Méthode d'aide pour mapper le ResultSet à un objet Emprunt
    private Emprunt mapResultSetToEmprunt(ResultSet rs) throws SQLException {
        Emprunt emprunt = new Emprunt();
        emprunt.setIdEmprunt(rs.getInt("id_emprunt")); // Assurez-vous que l'ID est bien récupéré
        emprunt.setMembreId(rs.getInt("membre_id"));
        emprunt.setLivreId(rs.getInt("livre_id"));
        emprunt.setDateEmprunt(rs.getDate("date_emprunt").toLocalDate());
        emprunt.setDateRetourPrevue(rs.getString("date_retour_prevue"));
        emprunt.setStatut(rs.getBoolean("statut"));
        emprunt.setPenalite(rs.getInt("penalite"));
        // Vérification si la date de retour est null
        java.sql.Date dateRetourPrevue = rs.getDate("date_retour_effective");
        if (dateRetourPrevue != null) {
            emprunt.setDateRetourEffective(dateRetourPrevue.toLocalDate());
        } else {
            emprunt.setDateRetourEffective(null); // Ou vous pouvez choisir de ne pas définir cette valeur
        }

        return emprunt;
    }
    public List<Emprunt> afficherTousLesEmpruntsEncour() {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM emprunts WHERE statut ='TRUE'";

        return getEmprunts(emprunts, sql);
    }

    private List<Emprunt> getEmprunts(List<Emprunt> emprunts, String sql) {
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Emprunt emprunt = mapResultSetToEmprunt(rs);
                emprunts.add(emprunt);
            }

            if (emprunts.isEmpty()) {
                System.out.println("Aucun emprunt trouvé.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return emprunts;
    }
    // Fonction permettant de d'indiquer le retour d'un livre
    public void retournerLivre(LocalDate dateRetourEffective, int idEmprunt, Boolean statut, int penalite) {
        String sql = "UPDATE emprunts SET date_retour_effective = ?, statut = ?, penalite = ? WHERE id_emprunt = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(dateRetourEffective)); // Date de retour actuelle
            pstmt.setBoolean(2, statut);
            pstmt.setInt(3, penalite);
            pstmt.setInt(4, idEmprunt);


            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Livre retourné avec succès.");
            } else {
                System.out.println("Aucun emprunt trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //Fonction permettant de modifier les informations d'un emprunt
    public void modifierEmprunt(int idEmprunt, int newIdMembre, int newIdLivre, String dateRetourPrevue) {
        String sql = "UPDATE emprunts SET membre_id = ?, livre_id = ?, date_retour_prevue = ? WHERE id_emprunt = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, newIdMembre);
            pstmt.setInt(2, newIdLivre);
            pstmt.setDate(3, java.sql.Date.valueOf(String.valueOf(dateRetourPrevue)));
            pstmt.setInt(4, idEmprunt);

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Emprunt modifié avec succès.");
            } else {
                System.out.println("Aucun emprunt trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode privée pour calculer les pénalités
    public int calculerPenalite(LocalDate dateRetourPrevue, LocalDate dateRetourEffective) {
        final int PENALITE_PAR_JOUR = 100; // Montant de la pénalité par jour
        if (dateRetourEffective.isAfter(dateRetourPrevue)) {
            long joursDeRetard = dateRetourEffective.toEpochDay() - dateRetourPrevue.toEpochDay();
            return (int) (joursDeRetard * PENALITE_PAR_JOUR);
        }
        return 0; // Pas de pénalité si le livre est retourné à temps
    }



    public  void afficherListeEmprunt(List<Emprunt>emprunts) {

        int index = 1;
        for (Emprunt emprunt : emprunts) {
            System.out.println(index + ". " + emprunt);
            index++;
        }

    }


    public Emprunt recupererDetailsEmprunts(int idMembre, int idLivre) {
        Emprunt emprunt = null;
        String sql = "SELECT l.titre as titre_livre, m.nom as nom_membre, date_emprunt, date_retour_prevue,date_retour_effective, statut, penalite FROM emprunts INNER JOIN livres l on l.id = emprunts.livre_id INNER JOIN membres m on m.id = emprunts.membre_id where livre_id = ? AND membre_id= ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)){
             pstmt.setInt(1,idLivre);
             pstmt.setInt(2, idMembre);
             ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {

                String titreLivre =  rs.getString("titre_livre");
                String nomMembre =  rs.getString("nom_membre");
                LocalDate dateEmprunt =  rs.getDate("date_emprunt").toLocalDate();
                String dateRetourPrevue =  rs.getString("date_retour_prevue");
                LocalDate dateRetourEffective = (rs.getDate("date_retour_effective")!= null ? rs.getDate("date_retour_effective").toLocalDate():null);
                Boolean statut = rs.getBoolean("statut");
                int penalite = rs. getInt("penalite");
                emprunt = new Emprunt(nomMembre, titreLivre, dateEmprunt, dateRetourPrevue, statut, dateRetourEffective, penalite);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return emprunt;
    }

    public Emprunt recupererEmprunt (int idEmprunt){
        Emprunt emprunt = null;
        String sql = "SELECT * FROM emprunts WHERE id_emprunt= ?";
        try(PreparedStatement pstm = connection.prepareStatement(sql)){
            pstm.setInt(1,idEmprunt);
            ResultSet rs = pstm.executeQuery();
            if(rs.next()){
                int idLivre = rs.getInt("livre_id");
                int idmembre = rs.getInt("membre_id");
                Boolean statut = rs.getBoolean("statut");
                LocalDate dateEmprunt = rs.getDate("date_emprunt").toLocalDate();
                String dateRetourPrevue = rs.getString("date_retour_prevue");
                LocalDate dateRetourEffective = (rs.getDate("date_retour_effective")!= null ? rs.getDate("date_retour_effective").toLocalDate():null);
                int penalite = rs.getInt("penalite");
                emprunt = new Emprunt(idEmprunt, idmembre,idLivre, dateEmprunt, dateRetourPrevue,dateRetourEffective, statut, penalite );

            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return emprunt;
    }
}
