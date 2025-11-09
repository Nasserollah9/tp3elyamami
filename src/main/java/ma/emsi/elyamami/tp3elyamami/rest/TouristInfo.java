package ma.emsi.elyamami.tp3elyamami.rest;

import java.util.List;

/**
 * POJO (Plain Old Java Object) pour représenter la structure JSON
 * attendue par l'application cliente et générée par le LLM.
 * JAX-RS utilise cette classe pour la sérialisation/désérialisation JSON.
 */
public class TouristInfo {

    // Noms des champs DOIVENT correspondre aux clés JSON
    private String ville_ou_pays;
    private List<String> endroits_a_visiter;
    private String prix_moyen_repas;

    // Constructeur sans arguments (requis pour la sérialisation JSON)
    public TouristInfo() {}

    // Getters et Setters (requis pour la sérialisation JSON)

    public String getVille_ou_pays() {
        return ville_ou_pays;
    }

    public void setVille_ou_pays(String ville_ou_pays) {
        this.ville_ou_pays = ville_ou_pays;
    }

    public List<String> getEndroits_a_visiter() {
        return endroits_a_visiter;
    }

    public void setEndroits_a_visiter(List<String> endroits_a_visiter) {
        this.endroits_a_visiter = endroits_a_visiter;
    }

    public String getPrix_moyen_repas() {
        return prix_moyen_repas;
    }

    public void setPrix_moyen_repas(String prix_moyen_repas) {
        this.prix_moyen_repas = prix_moyen_repas;
    }
}