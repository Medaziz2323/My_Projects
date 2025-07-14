#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <ctype.h>

#define MAX_PRODUITS 100
#define MAX_CLIENTS 50
#define MAX_COMMANDES 200

typedef struct {
    int idProduit;
    char nom[50];
    float prix;
    int stock;
} Produit;

typedef struct {
    int idClient;
    char nom[50];
    char telephone[9];
} Client;

typedef struct {
    int idCommande;
    int idClient;
    int idProduit;
    int quantite;
    float total;
} Commande;

Produit produits[MAX_PRODUITS];
Client clients[MAX_CLIENTS];
Commande commandes[MAX_COMMANDES];
int nbProduits = 0, nbClients = 0, nbCommandes = 0;
FILE *fichierUnique;

bool verifierTelephone(const char *telephone) {
    if (strlen(telephone) != 8) return false;
    for (int i = 0; i < 8; i++) {
        if (!isdigit(telephone[i])) return false;
    }
    return true;
}

bool verifierNomClient(const char *nom) {
    for (int i = 0; nom[i] != '\0'; i++) {
        if (!isalpha(nom[i]) && nom[i] != ' ') {
            return false;
        }
    }
    return true;
}

bool idProduitExistant(int id) {
    for (int i = 0; i < nbProduits; i++) {
        if (produits[i].idProduit == id) {
            return true;
        }
    }
    return false;
}

bool idClientExistant(int id) {
    for (int i = 0; i < nbClients; i++) {
        if (clients[i].idClient == id) {
            return true;
        }
    }
    return false;
}

bool verifierNomProduit(const char *nom) {
    return (strcmp(nom, "gateau") == 0 || strcmp(nom, "tarte") == 0 || strcmp(nom, "croissant") == 0);
}

bool verifierPrix(float prix) {
    return prix > 0;
}

void sauvegarderDonnees() {
    fichierUnique = fopen("patisserie.txt", "w");
    if (!fichierUnique) {
        printf("Erreur d'ouverture du fichier patisserie.txt\n");
        return;
    }

    fprintf(fichierUnique, "=== Produits ===\n");
    for (int i = 0; i < nbProduits; i++) {
        fprintf(fichierUnique, "%d,%s,%.2f,%d\n", produits[i].idProduit, produits[i].nom, produits[i].prix, produits[i].stock);
    }

    fprintf(fichierUnique, "\n=== Clients ===\n");
    for (int i = 0; i < nbClients; i++) {
        fprintf(fichierUnique, "%d,%s,%s\n", clients[i].idClient, clients[i].nom, clients[i].telephone);
    }

    fprintf(fichierUnique, "\n=== Commandes ===\n");
    for (int i = 0; i < nbCommandes; i++) {
        fprintf(fichierUnique, "%d,%d,%d,%d,%.2f\n", commandes[i].idCommande, commandes[i].idClient, commandes[i].idProduit, commandes[i].quantite, commandes[i].total);
    }

    fclose(fichierUnique);
}

void chargerProduits() {
    fichierUnique = fopen("patisserie.txt", "r");
    if (!fichierUnique) {
        printf("Aucun fichier trouve.\n");
        return;
    }

    char ligne[200];
    while (fgets(ligne, sizeof(ligne), fichierUnique)) {
        if (strstr(ligne, "=== Produits ===")) {
            while (fgets(ligne, sizeof(ligne), fichierUnique) && ligne[0] != '\n') {
                Produit p;
                sscanf(ligne, "%d,%49[^,],%f,%d", &p.idProduit, p.nom, &p.prix, &p.stock);
                produits[nbProduits++] = p;
            }
        }
    }
    fclose(fichierUnique);
}

void chargerClients() {
    fichierUnique = fopen("patisserie.txt", "r");
    if (!fichierUnique) {
        printf("Aucun fichier trouve.\n");
        return;
    }

    char ligne[200];
    while (fgets(ligne, sizeof(ligne), fichierUnique)) {
        if (strstr(ligne, "=== Clients ===")) {
            while (fgets(ligne, sizeof(ligne), fichierUnique) && ligne[0] != '\n') {
                Client c;
                sscanf(ligne, "%d,%49[^,],%s", &c.idClient, c.nom, c.telephone);
                clients[nbClients++] = c;
            }
        }
    }
    fclose(fichierUnique);
}

void chargerCommandes() {
    fichierUnique = fopen("patisserie.txt", "r");
    if (!fichierUnique) {
        printf("Aucun fichier trouve.\n");
        return;
    }

    char ligne[200];
    while (fgets(ligne, sizeof(ligne), fichierUnique)) {
        if (strstr(ligne, "=== Commandes ===")) {
            while (fgets(ligne, sizeof(ligne), fichierUnique) && ligne[0] != '\n') {
                Commande cmd;
                sscanf(ligne, "%d,%d,%d,%d,%f", &cmd.idCommande, &cmd.idClient, &cmd.idProduit, &cmd.quantite, &cmd.total);
                commandes[nbCommandes++] = cmd;
            }
        }
    }
    fclose(fichierUnique);
}

void afficherProduits() {
    if (nbProduits == 0) {
        printf("Aucun produit disponible.\n");
    } else {
        printf("\n=== Liste des Produits ===\n");
        for (int i = 0; i < nbProduits; i++) {
            printf("ID: %d, Nom: %s, Prix: %.2f, Stock: %d\n",
                   produits[i].idProduit, produits[i].nom, produits[i].prix, produits[i].stock);
        }
    }
}

void afficherClients() {
        if (nbClients == 0) {
        printf("Aucun Client disponible.\n");
    }else{
    printf("\n=== Liste des Clients ===\n");
    for (int i = 0; i < nbClients; i++) {
        printf("ID: %d, Nom: %s, Telephone: %s\n",
               clients[i].idClient, clients[i].nom, clients[i].telephone);
    }
}
}
void afficherCommandes() {
            if (nbCommandes == 0) {
        printf("Aucune Commande disponible.\n");
    }else{
    printf("\n=== Liste des Commandes ===\n");
    for (int i = 0; i < nbCommandes; i++) {
        printf("ID Commande: %d, ID Client: %d, ID Produit: %d, Quantite: %d, Total: %.2f DT\n",
               commandes[i].idCommande, commandes[i].idClient, commandes[i].idProduit,
               commandes[i].quantite, commandes[i].total);
    }
}}

void viderFichier() {
    fichierUnique = fopen("patisserie.txt", "w");
    if (!fichierUnique) {
        printf("Erreur lors de l'ouverture du fichier pour le vider.\n");
        return;
    }

    nbProduits = 0;
    nbClients = 0;
    nbCommandes = 0;

    fclose(fichierUnique);

    printf("L'historique a ete vide avec succes.\n");
}

void ajouterProduit() {
    if (nbProduits >= MAX_PRODUITS) {
        printf("Stock de produits plein !\n");
        return;
    }
    Produit *p = &produits[nbProduits];
    printf("ID produit : ");
    scanf("%d", &p->idProduit);
    if (idProduitExistant(p->idProduit)) {
        printf("Cet ID de produit existe deja.\n");
        return;
    }

    printf("Nom du produit (gateau, tarte, croissant) : ");
    scanf(" %[^\n]", p->nom);
    if (!verifierNomProduit(p->nom)) {
        printf("Nom du produit invalide, Choisissez parmi 'gateau', 'tarte', 'croissant'.\n");
        return;
    }

    printf("Prix du produit (en DT) : ");
    scanf("%f", &p->prix);
    if (!verifierPrix(p->prix)) {
        printf("Prix invalide, il doit etre positif.\n");
        return;
    }

    printf("Stock du produit : ");
    scanf("%d", &p->stock);

    nbProduits++;
    sauvegarderDonnees();
    printf("Produit ajoute avec succes !\n");
}

void ajouterClient() {
    if (nbClients >= MAX_CLIENTS) {
        printf("Capacite maximale des clients atteinte !\n");
        return;
    }
    Client *c = &clients[nbClients];
    printf("ID client : ");
    scanf("%d", &c->idClient);
    if (idClientExistant(c->idClient)) {
        printf("Cet ID de client existe deja.\n");
        return;
    }

    printf("Nom du client : ");
    scanf(" %[^\n]", c->nom);

    if (!verifierNomClient(c->nom)) {
        printf("Le nom du client ne doit contenir que des lettres et des espaces.\n");
        return;
    }

    printf("Telephone (8 chiffres) : ");
    scanf(" %[^\n]", c->telephone);
    if (!verifierTelephone(c->telephone)) {
        printf("Numero de telephone invalide.\n");
        return;
    }

    nbClients++;
    sauvegarderDonnees();
    printf("Client ajoute avec succes !\n");
}

void passerCommande() {
    if (nbCommandes >= MAX_COMMANDES) {
        printf("Capacite maximale des commandes atteinte !\n");
        return;
    }

    int idClient, idProduit, quantite;
    printf("ID du client : ");
    scanf("%d", &idClient);

    int clientIndex = -1;
    for (int i = 0; i < nbClients; i++) {
        if (clients[i].idClient == idClient) {
            clientIndex = i;
            break;
        }
    }
    if (clientIndex == -1) {
        printf("Client introuvable !\n");
        return;
    }

    printf("ID du produit : ");
    scanf("%d", &idProduit);

    int produitIndex = -1;
    for (int i = 0; i < nbProduits; i++) {
        if (produits[i].idProduit == idProduit) {
            produitIndex = i;
            break;
        }
    }
    if (produitIndex == -1) {
        printf("Produit introuvable !\n");
        return;
    }

    printf("Quantite : ");
    scanf("%d", &quantite);

    if (produits[produitIndex].stock < quantite) {
        printf("Stock insuffisant ! il reste seulement %d en stock.\nVoulez-vous quand meme passer une commande avec cette quantite ? (o/n)\n", produits[produitIndex].stock);
        char choix[4];
        scanf("%s", choix);
        if (strcmp(choix, "o") == 0) {
            Commande *cmd = &commandes[nbCommandes];
            cmd->idCommande = nbCommandes + 1;
            cmd->idClient = idClient;
            cmd->idProduit = idProduit;
            cmd->quantite = produits[produitIndex].stock;
            cmd->total = produits[produitIndex].stock * produits[produitIndex].prix;

            produits[produitIndex].stock = 0;
            nbCommandes++;
            sauvegarderDonnees();
            printf("Commande passee avec succes ! Total : %.2f\n", cmd->total);
        } else {
            printf("Commande annulee.\n");
        }
    } else {
        Commande *cmd = &commandes[nbCommandes];
        cmd->idCommande = nbCommandes + 1;
        cmd->idClient = idClient;
        cmd->idProduit = idProduit;
        cmd->quantite = quantite;
        cmd->total = quantite * produits[produitIndex].prix;

        produits[produitIndex].stock -= quantite;
        nbCommandes++;
        sauvegarderDonnees();
        printf("Commande passee avec succes ! Total : %.2f\n", cmd->total);
    }
}

void modifierProduit() {
    int idProduit;
    printf("Entrez id du produit a modifier : ");
    scanf("%d", &idProduit);

    int indexProduit = -1;
    for (int i = 0; i < nbProduits; i++) {
        if (produits[i].idProduit == idProduit) {
            indexProduit = i;
            break;
        }
    }

    if (indexProduit == -1) {
        printf("Produit non trouve !\n");
        return;
    }

    printf("Modifier le nom du produit (actuel : %s) : ", produits[indexProduit].nom);
    scanf(" %[^\n]", produits[indexProduit].nom);

    printf("Modifier le prix du produit (actuel : %.2f) : ", produits[indexProduit].prix);
    scanf("%f", &produits[indexProduit].prix);

    printf("Modifier le stock du produit (actuel : %d) : ", produits[indexProduit].stock);
    scanf("%d", &produits[indexProduit].stock);

    sauvegarderDonnees();
    printf("Produit modifie avec succes !\n");
}

void modifierClient() {
    int idClient;
    printf("Entrez l'id du client a modifier : ");
    scanf("%d", &idClient);

    int indexClient = -1;
    for (int i = 0; i < nbClients; i++) {
        if (clients[i].idClient == idClient) {
            indexClient = i;
            break;
        }
    }

    if (indexClient == -1) {
        printf("Client non trouve !\n");
        return;
    }

    printf("Modifier le nom du client (actuel : %s) : ", clients[indexClient].nom);
    scanf(" %[^\n]", clients[indexClient].nom);

    printf("Modifier le telephone du client (actuel : %s) : ", clients[indexClient].telephone);
    scanf(" %s", clients[indexClient].telephone);
    if (!verifierTelephone(clients[indexClient].telephone)) {
        printf("Numero de telephone invalide.\n");
        return;
    }

    sauvegarderDonnees();
    printf("Client modifie avec succes !\n");
}

void menuPrincipal() {
    int choix;
    do {
        printf("\n=== Menu Patisserie ===\n");
        printf("1. Ajouter un produit\n");
        printf("2. Afficher les produits\n");
        printf("3. Modifier un produit\n");
        printf("4. Ajouter un client\n");
        printf("5. Afficher les clients\n");
        printf("6. Modifier un client\n");
        printf("7. Passer une commande\n");
        printf("8. Afficher les commandes\n");
        printf("9. Vider l'historique\n");
        printf("10. Quitter\n");
        printf("Choix : ");
        scanf("%d", &choix);

        switch (choix) {
            case 1: ajouterProduit(); break;
            case 2: afficherProduits(); break;
            case 3: modifierProduit(); break;
            case 4: ajouterClient(); break;
            case 5: afficherClients(); break;
            case 6: modifierClient(); break;
            case 7: passerCommande(); break;
            case 8: afficherCommandes(); break;
            case 9: viderFichier(); break;
            case 10: printf("Au revoir !\n"); break;
            default: printf("Choix invalide !\n");
        }
    } while (choix != 10);
}

int main() {
    chargerProduits();
    chargerClients();
    chargerCommandes();
    menuPrincipal();
    return 0;
}
