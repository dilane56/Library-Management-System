package library;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

public class LivreDAO {
    private final Connection connection;

    public LivreDAO(Connection connection) {
        this.connection = connection;
    }

    public void ajouterLivre(Livre livre) {
        String sql = "INSERT INTO livres ( titre, auteur, categorie, nombre_exemplaires) VALUES ( ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, livre.getTitre());
            statement.setString(2, livre.getAuteur());
            statement.setString(3, livre.getCategorie());
            statement.setInt(4, livre.getNombreExemplaires());
            statement.executeUpdate();
            System.out.println("Livre ajouté avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Livre> chercherParCategorie(String categorie) {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres WHERE categorie = ?";

        return getLivres(categorie, livres, sql);
    }

    // Méthode pour chercher des livres par auteur
    public List<Livre> chercherParAuteur(String auteur) {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres WHERE auteur = ?";

        return getLivres(auteur, livres, sql);
    }

    private List<Livre> getLivres(String auteur, List<Livre> livres, String sql) {
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, auteur);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                livres.add(mapResultSetToLivre(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livres;
    }

    // Méthode pour chercher des livres par titre
    public List<Livre> chercherParTitre(String titre) {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres WHERE titre ILIKE ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, "%" + titre + "%"); // Utilise le wildcard pour une recherche partielle
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                livres.add(mapResultSetToLivre(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livres;
    }

    // Méthode pour mapper le ResultSet à un objet Livre
    private Livre mapResultSetToLivre(ResultSet rs) throws SQLException {
        Livre livre = new Livre();
        livre.setId(rs.getInt("id"));
        livre.setTitre(rs.getString("titre"));
        livre.setAuteur(rs.getString("auteur"));
        livre.setCategorie(rs.getString("categorie"));
        livre.setNombreExemplaires(rs.getInt("nombre_exemplaires"));
        return livre;
    }

    // Méthode pour afficher tous les livres
    public List<Livre> afficherTousLesLivres() {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres";

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                livres.add(mapResultSetToLivre(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livres;
    }

    // Méthode pour supprimer un livre par ID
    public void supprimerLivre(int id) {
        String sql = "DELETE FROM livres WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Livre supprimé avec succès.");
            } else {
                System.out.println("Aucun livre trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Méthode pour modifier un livre
    public void modifierLivre(Livre livre) {
        String sql = "UPDATE livres SET titre = ?, auteur = ?, categorie = ?, nombre_exemplaires = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, livre.getTitre());
            pstmt.setString(2, livre.getAuteur());
            pstmt.setString(3, livre.getCategorie());
            pstmt.setInt(4, livre.getNombreExemplaires());
            pstmt.setInt(5, livre.getId());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Livre modifié avec succès.");
            } else {
                System.out.println("Aucun livre trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void afficherListeDeLivre(List<Livre> livres){
        if(livres.size()==0){
            System.out.println("Aucun livre trouver");
        }else{
            int index =1;
            for(Livre livre :livres){
                System.out.println(index+". "+livre);
                index++;
            }
        }

    }
    // Méthode pour récupérer un livre par ID
    public Livre recupererUnLivre(int id) {
        Livre livre = null;
        String sql = "SELECT titre, auteur, categorie, nombre_exemplaires FROM livres WHERE id = ?";

        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String titre = rs.getString("titre");
                String auteur = rs.getString("auteur");
                String categorie = rs.getString("categorie");
                int nbrExemplaire = rs.getInt("nombre_exemplaires");
                livre = new Livre(titre, auteur,categorie,nbrExemplaire);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return livre; // Retourne null si le livre n'est pas trouvé
    }


}
