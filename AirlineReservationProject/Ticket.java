import java.io.Serializable;
import java.util.Date;

/**
 * La classe Ticket - Représente un billet d'avion dans notre système
 * 
 * Cette classe est comme une "carte d'identité" pour chaque billet d'avion.
 * Elle contient toutes les informations importantes comme:
 * - D'où part l'avion et où il va
 * - Quand il part (date et heure)
 * - La classe de voyage (économique, business)
 * - Le prix du billet
 * 
 * Chaque billet créé dans l'application sera une "instance" de cette classe.
 * 
 * Note: Serializable permet de sauvegarder l'objet (par exemple pour l'impression)
 */
public class Ticket implements Serializable {
    // Les caractéristiques (ou attributs) de chaque billet
    private int ticketId;         // Identifiant unique du billet dans la base de données
    private String ticketNumber;  // Numéro du billet (comme "TK493")
    private int flightId;         // Identifiant du vol dans la base de données
    private String flightNumber;  // Numéro du vol (comme "TNTP404")
    private String fromCity;      // Ville de départ (exemple: "Tunis")
    private String toCity;        // Ville d'arrivée (exemple: "Paris")
    private Date departureDate;   // Date de départ (jour/mois/année)
    private String departureTime; // Heure de départ (format "hh:mm")
    private String travelClass;   // Classe de voyage (exemple: "Economic", "Business")
    private int price;            // Prix du billet en dinars tunisiens
    private boolean isActive;     // Indique si le billet est toujours disponible
    
    /**
     * Constructeur complet - Permet de créer un billet avec tous ses détails
     * 
     * Un constructeur est une méthode spéciale qui est appelée quand on crée un nouvel objet.
     * Il initialise toutes les données du billet en une seule fois.
     */
    public Ticket(int ticketId, String ticketNumber, int flightId, String flightNumber, 
                 String fromCity, String toCity, Date departureDate, String departureTime,
                 String travelClass, int price, boolean isActive) {
        // "this" fait référence à l'objet en cours de création
        this.ticketId = ticketId;             // On assigne l'ID passé en paramètre à l'attribut de l'objet
        this.ticketNumber = ticketNumber;     // Pareil pour le numéro de billet
        this.flightId = flightId;             // Et ainsi de suite pour chaque attribut...
        this.flightNumber = flightNumber;
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.travelClass = travelClass;
        this.price = price;
        this.isActive = isActive;
    }
    
    /**
     * Constructeur simplifié - Pour créer un nouveau billet pas encore enregistré dans la base de données
     * 
     * Ce constructeur est utilisé quand on crée un nouveau billet qui n'a pas encore d'ID assigné.
     * On utilise -1 comme ID temporaire, et true pour indiquer qu'il est actif par défaut.
     * L'ID réel sera attribué quand le billet sera enregistré dans la base de données.
     */
    public Ticket(String ticketNumber, int flightId, String flightNumber, 
                 String fromCity, String toCity, Date departureDate, String departureTime,
                 String travelClass, int price) {
        // On appelle le premier constructeur avec des valeurs par défaut pour certains paramètres
        this(-1, ticketNumber, flightId, flightNumber, fromCity, toCity, 
             departureDate, departureTime, travelClass, price, true);
    }
    
    // Méthodes Getter et Setter
    // Les getters permettent de lire les valeurs des attributs (qui sont privés)
    // Les setters permettent de modifier ces valeurs
    // C'est un principe de la Programmation Orientée Objet appelé "encapsulation"
    
    /**
     * Renvoie l'identifiant unique du billet
     */
    public int getTicketId() {
        return ticketId;  // On retourne la valeur de l'attribut ticketId
    }
    
    /**
     * Définit l'identifiant unique du billet
     */
    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;  // On modifie la valeur de l'attribut avec celle reçue en paramètre
    }
    
    /**
     * Renvoie le numéro du billet (chaîne de caractères comme "TK493")
     */
    public String getTicketNumber() {
        return ticketNumber;
    }
    
    /**
     * Définit le numéro du billet
     */
    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }
    
    /**
     * Renvoie l'identifiant du vol
     */
    public int getFlightId() {
        return flightId;
    }
    
    /**
     * Définit l'identifiant du vol
     */
    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }
    
    /**
     * Renvoie le numéro du vol (chaîne de caractères comme "TNTP404")
     */
    public String getFlightNumber() {
        return flightNumber;
    }
    
    /**
     * Définit le numéro du vol
     */
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }
    
    /**
     * Renvoie la ville de départ
     */
    public String getFromCity() {
        return fromCity;
    }
    
    /**
     * Définit la ville de départ
     */
    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }
    
    /**
     * Renvoie la ville d'arrivée
     */
    public String getToCity() {
        return toCity;
    }
    
    /**
     * Définit la ville d'arrivée
     */
    public void setToCity(String toCity) {
        this.toCity = toCity;
    }
    
    /**
     * Renvoie la date de départ
     */
    public Date getDepartureDate() {
        return departureDate;
    }
    
    /**
     * Définit la date de départ
     */
    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }
    
    /**
     * Renvoie l'heure de départ
     */
    public String getDepartureTime() {
        return departureTime;
    }
    
    /**
     * Définit l'heure de départ
     */
    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }
    
    /**
     * Renvoie la classe de voyage (ex: "Economic", "Business")
     */
    public String getTravelClass() {
        return travelClass;
    }
    
    /**
     * Définit la classe de voyage
     */
    public void setTravelClass(String travelClass) {
        this.travelClass = travelClass;
    }
    
    /**
     * Renvoie le prix du billet en dinars tunisiens
     */
    public int getPrice() {
        return price;
    }
    
    /**
     * Définit le prix du billet
     */
    public void setPrice(int price) {
        this.price = price;
    }
    
    /**
     * Vérifie si le billet est actif/disponible
     * @return true si le billet est actif, false sinon
     */
    public boolean isActive() {
        return isActive;
    }
    
    /**
     * Définit si le billet est actif ou non
     */
    public void setActive(boolean active) {
        isActive = active;
    }
    
    /**
     * Méthode toString - Convertit les informations du billet en une chaîne de caractères
     * 
     * Cette méthode est utile pour afficher les informations du billet lors du débogage.
     * Elle est automatiquement appelée quand on essaie d'afficher un objet Ticket.
     */
    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId=" + ticketId +
                ", ticketNumber='" + ticketNumber + '\'' +
                ", flightNumber='" + flightNumber + '\'' +
                ", fromCity='" + fromCity + '\'' +
                ", toCity='" + toCity + '\'' +
                ", departureDate=" + departureDate +
                ", departureTime='" + departureTime + '\'' +
                ", travelClass='" + travelClass + '\'' +
                ", price=" + price +
                ", isActive=" + isActive +
                '}';
    }
} 