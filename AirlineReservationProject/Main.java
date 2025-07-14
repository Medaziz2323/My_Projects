import javax.swing.*;

/**
 * Fichier PRINCIPAL du programme Tunisia Airways.
 * C'est ici que tout commence! Ce fichier est le point de démarrage de l'application.
 * Il prépare la base de données et lance l'interface graphique.
 */
public class Main {
    
    /**
     * Méthode main : c'est la première méthode exécutée quand on lance le programme.
     * Toutes les applications Java commencent par cette méthode.
     */
    public static void main(String[] args) {
        System.out.println("Démarrage de l'application Tunisia Airways...");
        
        // On définit l'apparence de l'interface pour qu'elle ressemble à celle du système d'exploitation
        // Ça permettra à l'application d'avoir un look familier selon que l'on soit sur Windows, Mac ou Linux
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Si ça ne fonctionne pas, ce n'est pas grave, l'application fonctionnera quand même
            System.err.println("Attention: Impossible de définir l'apparence du système: " + e.getMessage());
        }
        
        // Chargement du "pilote" MySQL pour pouvoir communiquer avec la base de données
        // C'est comme si on installait un traducteur entre Java et MySQL
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Pilote MySQL chargé avec succès");
        } catch (ClassNotFoundException e) {
            // Si le pilote n'est pas trouvé, on ne peut pas continuer car on a besoin de la base de données
            System.err.println("ERREUR: Pilote MySQL introuvable!");
            System.err.println("Assurez-vous que MySQL Connector/J est dans votre classpath.");
            System.err.println("Si vous utilisez Maven, exécutez: mvn clean package");
            System.err.println("Puis exécutez: java -jar target/tunisia-airways-1.0-SNAPSHOT-jar-with-dependencies.jar");
            
            // Affichage d'une fenêtre d'erreur pour informer l'utilisateur du problème
            JOptionPane.showMessageDialog(null, 
                "Pilote MySQL introuvable.\n\n" +
                "Assurez-vous que MySQL Connector/J est dans votre classpath.\n\n" +
                "Si vous utilisez Maven, exécutez: mvn clean package\n" +
                "Puis exécutez: java -jar target/tunisia-airways-1.0-SNAPSHOT-jar-with-dependencies.jar", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            
            // Affiche les détails techniques de l'erreur (pour les développeurs)
            e.printStackTrace();
            return; // On arrête le programme ici car on ne peut pas continuer sans base de données
        }
        
        // Maintenant on initialise la connexion à la base de données
        // C'est comme ouvrir la porte vers notre stock de données
        try {
            DatabaseConnection.initializeDatabase();
            System.out.println("Base de données initialisée avec succès");
        } catch (Exception e) {
            // Si on ne peut pas se connecter à la base de données, on informe l'utilisateur
            System.err.println("Erreur d'initialisation de la base de données: " + e.getMessage());
            System.err.println("Assurez-vous que XAMPP est démarré et que la base de données tunisia_airways existe!");
            
            // Affichage d'une fenêtre d'erreur
            JOptionPane.showMessageDialog(null, 
                "Erreur de connexion à la base de données: " + e.getMessage() + "\n\n" +
                "Assurez-vous que XAMPP est démarré et que la base de données tunisia_airways existe.", 
                "Erreur de Base de Données", 
                JOptionPane.ERROR_MESSAGE);
            
            e.printStackTrace();
            return; // On arrête le programme car on ne peut pas fonctionner sans base de données
        }
        
        // On lance l'interface graphique dans un thread spécial (EDT)
        // C'est comme si on confiait l'affichage de l'interface à un assistant spécialisé
        SwingUtilities.invokeLater(() -> {
            System.out.println("Démarrage de Tunisia Airways avec redirection basée sur le rôle");
            System.out.println("- Les administrateurs seront redirigés vers le Tableau de Bord Admin");
            System.out.println("- Les utilisateurs normaux seront redirigés vers le Menu Principal");
            
            // Création de la page de connexion - c'est la première fenêtre que l'utilisateur verra
            new LoginPage();
            System.out.println("Interface utilisateur démarrée");
        });
    }
} 