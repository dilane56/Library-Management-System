package library.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseInitializer {
    private Connection connection;

    public DatabaseInitializer(Connection connection) {
        this.connection = connection;
    }

    public void initializeDatabase() {
        createLivresTable();
        createMembresTable();
        createEmpruntsTable();
        createFunctionsAndTriggers();  // Ajout de la création des fonctions et triggers
    }

    private void createLivresTable() {
        String sql = "CREATE TABLE IF NOT EXISTS livres (" +
                "id SERIAL PRIMARY KEY," +
                "titre VARCHAR(255) NOT NULL," +
                "auteur VARCHAR(255) NOT NULL," +
                "categorie VARCHAR(255)," +
                "nombre_exemplaires INT DEFAULT 1" +
                ")";

        executeUpdate(sql, "Table 'livres' créée ou déjà existante.");
    }

    private void createMembresTable() {
        String sql = "CREATE TABLE IF NOT EXISTS membres (" +
                "id SERIAL PRIMARY KEY," +
                "nom VARCHAR(255) NOT NULL," +
                "prenom VARCHAR(255) NOT NULL," +
                "email VARCHAR(255) UNIQUE NOT NULL," +
                "adhesion_date DATE DEFAULT CURRENT_DATE" +
                ")";

        executeUpdate(sql, "Table 'membres' créée ou déjà existante.");
    }

    private void createEmpruntsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS emprunts (" +
                "id_emprunt SERIAL PRIMARY KEY," +
                "membre_id INT REFERENCES membres(id) ON DELETE RESTRICT," +
                "livre_id INT REFERENCES livres(id) ON DELETE RESTRICT," +
                "date_emprunt DATE NOT NULL," +
                "date_retour_prevue DATE NOT NULL," +
                "date_retour_effective DATE," +
                "statut BOOLEAN DEFAULT TRUE," +
                "penalite INT DEFAULT 0" +
                ")";

        executeUpdate(sql, "Table 'emprunts' créée ou déjà existante.");
    }

    private void createFunctionsAndTriggers() {
        createMiseAJourExemplairesEmpruntFunction();
        createTriggerMiseAJourEmprunt();
        createMiseAJourExemplairesStatutFunction();
        createTriggerMiseAJourStatut();
        createMiseAJourExemplairesIdLivreFunction();
        createTriggerMiseAJourIdLivre();
        createMiseAJourExemplairesDeleteFunction();
        createTriggerMiseAJourDelete();
    }

    private void createMiseAJourExemplairesEmpruntFunction() {
        String sql = "CREATE OR REPLACE FUNCTION mise_a_jour_exemplaires_emprunt() " +
                "RETURNS TRIGGER AS $$ " +
                "BEGIN " +
                "    UPDATE livres " +
                "    SET nombre_exemplaires = nombre_exemplaires - 1 " +
                "    WHERE id = NEW.livre_id; " +
                "    RETURN NEW; " +
                "END; " +
                "$$ LANGUAGE plpgsql;";

        executeUpdate(sql, "Fonction 'mise_a_jour_exemplaires_emprunt' créée.");
    }

    private void createTriggerMiseAJourEmprunt() {
        String sql = "CREATE OR REPLACE TRIGGER trigger_mise_a_jour_emprunt " +
                "AFTER INSERT ON emprunts " +
                "FOR EACH ROW " +
                "EXECUTE FUNCTION mise_a_jour_exemplaires_emprunt();";

        executeUpdate(sql, "Trigger 'trigger_mise_a_jour_emprunt' créé.");
    }

    private void createMiseAJourExemplairesStatutFunction() {
        String sql = "CREATE OR REPLACE FUNCTION mise_a_jour_exemplaires_statut() " +
                "RETURNS TRIGGER AS $$ " +
                "BEGIN " +
                "    IF OLD.statut = TRUE AND NEW.statut = FALSE THEN " +
                "        UPDATE livres " +
                "        SET nombre_exemplaires = nombre_exemplaires + 1 " +
                "        WHERE id = NEW.livre_id; " +
                "    ELSIF OLD.statut = FALSE AND NEW.statut = TRUE THEN " +
                "        UPDATE livres " +
                "        SET nombre_exemplaires = nombre_exemplaires - 1 " +
                "        WHERE id = NEW.livre_id; " +
                "    END IF; " +
                "    RETURN NEW; " +
                "END; " +
                "$$ LANGUAGE plpgsql;";

        executeUpdate(sql, "Fonction 'mise_a_jour_exemplaires_statut' créée.");
    }

    private void createTriggerMiseAJourStatut() {
        String sql = "CREATE OR REPLACE TRIGGER trigger_mise_a_jour_statut " +
                "AFTER UPDATE OF statut ON emprunts " +
                "FOR EACH ROW " +
                "EXECUTE FUNCTION mise_a_jour_exemplaires_statut();";

        executeUpdate(sql, "Trigger 'trigger_mise_a_jour_statut' créé.");
    }

    private void createMiseAJourExemplairesIdLivreFunction() {
        String sql = "CREATE OR REPLACE FUNCTION mise_a_jour_exemplaires_idLivre() " +
                "RETURNS TRIGGER AS $$ " +
                "BEGIN " +
                "    IF OLD.livre_id != NEW.livre_id THEN " +
                "        UPDATE livres SET nombre_exemplaires = nombre_exemplaires - 1 WHERE id = NEW.livre_id; " +
                "        UPDATE livres SET nombre_exemplaires = nombre_exemplaires + 1 WHERE id = OLD.livre_id; " +
                "    END IF; " +
                "    RETURN NEW; " +
                "END; " +
                "$$ LANGUAGE plpgsql;";

        executeUpdate(sql, "Fonction 'mise_a_jour_exemplaires_idLivre' créée.");
    }

    private void createTriggerMiseAJourIdLivre() {
        String sql = "CREATE OR REPLACE TRIGGER trigger_mise_a_jour_idLivre " +
                "AFTER UPDATE OF livre_id ON emprunts " +
                "FOR EACH ROW " +
                "EXECUTE FUNCTION mise_a_jour_exemplaires_idLivre();";

        executeUpdate(sql, "Trigger 'trigger_mise_a_jour_idLivre' créé.");
    }

    private void createMiseAJourExemplairesDeleteFunction() {
        String sql = "CREATE OR REPLACE FUNCTION mise_a_jour_exemplaires_delete() " +
                "RETURNS TRIGGER AS $$ " +
                "BEGIN " +
                "    IF OLD.statut = TRUE THEN " +
                "        UPDATE livres " +
                "        SET nombre_exemplaires = nombre_exemplaires + 1 " +
                "        WHERE id = OLD.livre_id; " +
                "    END IF; " +
                "    RETURN OLD; " +
                "END; " +
                "$$ LANGUAGE plpgsql;";

        executeUpdate(sql, "Fonction 'mise_a_jour_exemplaires_delete' créée.");
    }

    private void createTriggerMiseAJourDelete() {
        String sql = "CREATE OR REPLACE TRIGGER trigger_mise_a_jour_delete " +
                "AFTER DELETE ON emprunts " +
                "FOR EACH ROW " +
                "EXECUTE FUNCTION mise_a_jour_exemplaires_delete();";

        executeUpdate(sql, "Trigger 'trigger_mise_a_jour_delete' créé.");
    }

    private void executeUpdate(String sql, String successMessage) {
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.executeUpdate();
            System.out.println(successMessage);
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'exécution de la requête : " + e.getMessage());
        }
    }
}
