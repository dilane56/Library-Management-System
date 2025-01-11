package library;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MembreDAO {
    private final Connection connection;

    public MembreDAO(Connection connection) {
        this.connection = connection;
    }
    public void ajouterMembre(Membre membre){
        String sql = "INSERT INTO membres (nom, prenom, email, adhesion_date) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement =connection.prepareStatement(sql)){
            preparedStatement.setString(1,membre.getNom());
            preparedStatement.setString(2, membre.getPrenom());
            preparedStatement.setString(3, membre.getEmail());
            preparedStatement.setDate(4, java.sql.Date.valueOf(membre.getAdhesionDate()));
            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected>0){
                System.out.println("Membre ajouter avec succès");
            }else{
                System.out.println("Erreur lors de l'ajout du membre");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Méthode pour supprimer un membre par ID
    public void supprimerMembre(int id) {
        String sql = "DELETE FROM membres WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Membre supprimé avec succès.");
            } else {
                System.out.println("Aucun membre trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour lister tous les membres
    public List<Membre> listerTousLesMembres() {
        List<Membre> membres = new ArrayList<>();
        String sql = "SELECT * FROM membres";

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Membre membre = mapResultSetToMembre(rs);
                membres.add(membre);
            }

            if (membres.isEmpty()) {
                System.out.println("Aucun membre trouvé.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return membres;
    }

    // Méthode pour modifier un membre
    public void modifierMembre(Membre membre, int idMembre) {
        String sql = "UPDATE membres SET nom = ?, prenom = ?, email = ?, adhesion_date = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, membre.getNom());
            pstmt.setString(2, membre.getPrenom());
            pstmt.setString(3, membre.getEmail());
            pstmt.setDate(4, java.sql.Date.valueOf(membre.getAdhesionDate()));
            pstmt.setInt(5, idMembre);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Membre modifié avec succès.");
            } else {
                System.out.println("Aucun membre trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Méthode pour rechercher un membre par nom
    public List<Membre> rechercherMembreParNom(String nom) {
        List<Membre> membres = new ArrayList<>();
        String sql = "SELECT * FROM membres WHERE nom LIKE ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, "%" + nom + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                membres.add(mapResultSetToMembre(rs));
            }

            if (membres.isEmpty()) {
                System.out.println("Aucun membre trouvé avec ce nom.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return membres;
    }
    private Membre mapResultSetToMembre(ResultSet rs) throws SQLException {
        Membre membre = new Membre();
        membre.setId(rs.getInt("id"));
        membre.setNom(rs.getString("nom"));
        membre.setPrenom(rs.getString("prenom"));
        membre.setEmail(rs.getString("email"));
        membre.setAdhesionDate(rs.getDate("adhesion_date").toLocalDate());
        return membre;
    }

    public  void afficherListeDeMembre(List<Membre>membres){
        if(membres.size()==0){
            System.out.println("Aucun Membres Trouvé");
        }else{
            int index =1;
            for(Membre membre : membres){
                System.out.println(index+". "+membre);
                index++;
            }
        }
    }

        // Fonction permettant de recuperer les informations d’un membre à l’aide de son ID
    public Membre recupererUnMembre(int idMembre) {
       Membre membre =null;
        String sql = "SELECT nom, prenom, email, adhesion_date FROM membres WHERE  id = ?";

        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, idMembre);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String email = rs.getString("email");
                LocalDate dateAdhesion = rs.getDate("adhesion_date").toLocalDate();
                membre =new Membre(nom, prenom, email, dateAdhesion);


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return membre;
    }
}
