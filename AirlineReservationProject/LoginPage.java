import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.HashMap;
import java.util.Map;

/**
 * PAGE DE CONNEXION - C'est la première fenêtre que l'utilisateur voit au démarrage.
 * Cette classe crée la fenêtre de connexion où l'utilisateur peut entrer son nom d'utilisateur et son mot de passe.
 * Elle gère également la redirection vers les différentes interfaces selon le type d'utilisateur (admin ou normal).
 */
public class LoginPage extends JFrame {
    // Les panneaux principaux qui vont contenir les éléments de l'interface
    private JPanel mainPanel;      // Panneau principal qui contient tout
    private JPanel loginPanel;     // Panneau qui contient le formulaire de connexion
    
    // Les composants du formulaire de connexion
    private JTextField usernameField;      // Champ pour entrer le nom d'utilisateur
    private JPasswordField passwordField;  // Champ pour entrer le mot de passe (les caractères sont masqués)
    private JButton loginButton;           // Bouton pour se connecter
    private JButton logoutButton;          // Bouton pour se déconnecter
    private JLabel statusLabel;            // Étiquette pour afficher les messages (succès ou erreur)
    private JButton domesticFlightButton;  // Bouton pour accéder à la réservation de vol
    private JButton signupButton;          // Bouton pour s'inscrire
    
    // Variable qui garde en mémoire l'utilisateur actuellement connecté
    private User currentUser;

    /**
     * Constructeur - C'est ici qu'on crée la fenêtre principale de l'application
     * Cette méthode est appelée quand on écrit "new LoginPage()" dans le code
     */
    public LoginPage() {
        // On définit le titre de la fenêtre qui apparaîtra en haut
        super("Tunisia Airways");
        
        // On crée le panneau principal avec une disposition de type BorderLayout
        // BorderLayout divise l'écran en 5 zones: North, South, East, West et Center
        mainPanel = new JPanel(new BorderLayout(20, 20)); // Les chiffres représentent l'espace horizontal et vertical entre les éléments
        mainPanel.setBackground(AppStyle.BACKGROUND);      // On définit la couleur de fond
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40)); // On ajoute une marge autour du panneau
        
        // On crée le panneau de connexion avec une disposition de type GridBagLayout
        // GridBagLayout permet de placer les éléments de manière très précise
        loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(AppStyle.WHITE);          // Fond blanc pour le formulaire
        loginPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1), // Une bordure gris clair de 1 pixel
            BorderFactory.createEmptyBorder(30, 30, 30, 30)              // Et une marge intérieure
        ));
        
        // On appelle la méthode qui va créer le formulaire de connexion
        createLoginForm();
        
        // On ajoute le panneau de connexion au centre du panneau principal
        mainPanel.add(loginPanel, BorderLayout.CENTER);
        
        // On définit le panneau principal comme contenu de la fenêtre
        setContentPane(mainPanel);
        
        // Configuration de la fenêtre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // La fenêtre se ferme complètement quand on clique sur la croix
        AppStyle.setFullScreen(this);                    // On met la fenêtre en plein écran
        setVisible(true);                                // On rend la fenêtre visible
    }
    
    /**
     * Cette méthode crée tous les éléments du formulaire de connexion
     * et les place correctement dans le panneau
     */
    private void createLoginForm() {
        // GridBagConstraints permet de définir comment les éléments sont placés dans un GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);           // Espace autour de chaque élément
        gbc.anchor = GridBagConstraints.WEST;          // Alignement à gauche
        gbc.fill = GridBagConstraints.HORIZONTAL;      // Les éléments s'étendent horizontalement
        
        // Titre "Welcome Back"
        JLabel titleLabel = AppStyle.createTitleLabel("Welcome Back");
        gbc.gridx = 0;                                // Position en X dans la grille
        gbc.gridy = 0;                                // Position en Y dans la grille
        gbc.gridwidth = 2;                            // L'élément prend 2 cellules en largeur
        gbc.insets = new Insets(0, 0, 20, 0);         // Marge spéciale pour le titre (plus d'espace en bas)
        loginPanel.add(titleLabel, gbc);              // On ajoute l'élément au panneau
        
        // Étiquette "Username"
        JLabel usernameLabel = AppStyle.createLabel("Username");
        gbc.gridx = 0;
        gbc.gridy = 1;                                // On descend d'une ligne
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 5, 0);          // Petite marge en bas
        loginPanel.add(usernameLabel, gbc);
        
        // Champ de texte pour entrer le nom d'utilisateur
        usernameField = new JTextField(20);           // 20 = largeur approximative en caractères
        AppStyle.styleTextField(usernameField);       // On applique un style uniforme
        gbc.gridy = 2;                                // On descend encore d'une ligne
        gbc.insets = new Insets(0, 0, 15, 0);         // Plus grande marge en bas
        loginPanel.add(usernameField, gbc);
        
        // Étiquette "Password"
        JLabel passwordLabel = AppStyle.createLabel("Password");
        gbc.gridy = 3;                                // On continue de descendre
        gbc.insets = new Insets(0, 0, 5, 0);
        loginPanel.add(passwordLabel, gbc);
        
        // Champ de mot de passe (les caractères sont remplacés par des points)
        passwordField = new JPasswordField(20);
        AppStyle.styleTextField(passwordField);
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 20, 0);
        loginPanel.add(passwordField, gbc);
        
        // Bouton de connexion
        loginButton = new JButton("Sign In");
        AppStyle.stylePrimaryButton(loginButton);          // Style de bouton principal
        loginButton.addActionListener(e -> login());       // Quand on clique, ça appelle la méthode login()
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 10, 0);
        loginPanel.add(loginButton, gbc);
        
        // Bouton de déconnexion (invisible au début)
        logoutButton = new JButton("Logout");
        AppStyle.stylePrimaryButton(logoutButton);
        logoutButton.addActionListener(e -> logout());     // Quand on clique, ça appelle la méthode logout()
        logoutButton.setVisible(false);                    // Invisible par défaut
        gbc.gridy = 6;
        loginPanel.add(logoutButton, gbc);
        
        // Étiquette pour les messages de statut (succès ou erreur)
        statusLabel = new JLabel("");
        statusLabel.setFont(AppStyle.FONT_REGULAR);
        gbc.gridy = 7;
        gbc.insets = new Insets(10, 0, 0, 0);
        loginPanel.add(statusLabel, gbc);
        
        // Bouton pour réserver un vol (invisible au début)
        domesticFlightButton = new JButton("Book Flight");
        AppStyle.stylePrimaryButton(domesticFlightButton);
        domesticFlightButton.addActionListener(e -> openDomesticFlight());  // Ouvre la page de réservation
        domesticFlightButton.setVisible(false);                             // Invisible par défaut
        gbc.gridy = 8;
        gbc.insets = new Insets(20, 0, 0, 0);
        loginPanel.add(domesticFlightButton, gbc);
        
        // Panneau pour le lien d'inscription
        JPanel signupPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        signupPanel.setBackground(AppStyle.WHITE);
        
        // Message "Don't have an account?"
        JLabel signupLabel = AppStyle.createLabel("Don't have an account? ");
        
        // Bouton d'inscription stylisé comme un lien
        signupButton = new JButton("Sign Up");
        signupButton.setFont(AppStyle.FONT_REGULAR);
        signupButton.setBorderPainted(false);                  // Pas de bordure
        signupButton.setContentAreaFilled(false);              // Pas de fond
        signupButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Curseur main au survol
        signupButton.setForeground(AppStyle.TEAL);             // Texte en couleur teal
        signupButton.addActionListener(e -> openSignupPage()); // Ouvre la page d'inscription
        
        // On ajoute les deux éléments au panneau d'inscription
        signupPanel.add(signupLabel);
        signupPanel.add(signupButton);
        
        // On ajoute le panneau d'inscription au formulaire
        gbc.gridy = 9;
        gbc.insets = new Insets(20, 0, 0, 0);
        loginPanel.add(signupPanel, gbc);
    }
    
    /**
     * Cette méthode est appelée quand l'utilisateur clique sur le bouton de connexion
     * Elle vérifie si les identifiants sont corrects et redirige l'utilisateur
     */
    private void login() {
        // On récupère le nom d'utilisateur et le mot de passe entrés
        String username = usernameField.getText();
        char[] passwordChars = passwordField.getPassword();  // Le mot de passe est récupéré comme un tableau de caractères pour des raisons de sécurité
        String password = new String(passwordChars);         // On le convertit en chaîne de caractères
        
        // On vérifie les identifiants dans la base de données
        currentUser = UserDAO.authenticateUser(username, password);
        
        if (currentUser != null) {
            // Si l'authentification a réussi (l'utilisateur existe et le mot de passe est correct)
            statusLabel.setText("Login successful!");
            statusLabel.setForeground(AppStyle.SUCCESS);     // Message en vert
            
            // On affiche les boutons qui étaient cachés
            domesticFlightButton.setVisible(true);
            logoutButton.setVisible(true);
            
            // On désactive les champs de connexion
            usernameField.setEnabled(false);
            passwordField.setEnabled(false);
            loginButton.setEnabled(false);
            signupButton.setEnabled(false);
            
            // On redirige vers l'interface appropriée selon le rôle de l'utilisateur
            if (currentUser.isAdmin()) {
                // Si c'est un administrateur
                openAdminDashboard();
            } else {
                // Si c'est un utilisateur normal
                openMainMenu();
            }
        } else {
            // Si l'authentification a échoué
            statusLabel.setText("Invalid username or password");
            statusLabel.setForeground(AppStyle.ERROR);      // Message en rouge
        }
    }
    
    /**
     * Logout action
     */
    public void logout() {
        // Reset current user
        currentUser = null;
        
        // Reset form
        usernameField.setText("");
        passwordField.setText("");
        statusLabel.setText("");
        
        // Enable login fields
        usernameField.setEnabled(true);
        passwordField.setEnabled(true);
        loginButton.setEnabled(true);
        signupButton.setEnabled(true);
        
        // Hide buttons
        domesticFlightButton.setVisible(false);
        logoutButton.setVisible(false);
    }
    
    /**
     * Opens the main menu for regular users
     */
    private void openMainMenu() {
        new MainMenuPage(this, currentUser);
        setVisible(false); // Hide login window
    }
    
    /**
     * Opens the domestic flight booking window
     */
    private void openDomesticFlight() {
        new DomesticFlight(this, currentUser);
        setVisible(false); // Hide login window
    }
    
    /**
     * Opens the signup page
     */
    private void openSignupPage() {
        new SignupPage(this);
        setVisible(false); // Hide login window
    }
    
    /**
     * Opens the admin dashboard
     */
    private void openAdminDashboard() {
        new AdminDashboard(this, currentUser);
        setVisible(false); // Hide login window
    }
    
    /**
     * Gets the current user
     */
    public User getCurrentUser() {
        return currentUser;
    }
} 