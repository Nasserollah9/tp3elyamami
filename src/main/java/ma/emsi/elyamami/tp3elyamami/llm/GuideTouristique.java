package ma.emsi.elyamami.tp3elyamami.llm;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import ma.emsi.elyamami.tp3elyamami.rest.TouristInfo;

/**
 * Interface de service LLM pour le guide touristique.
 * LangChain4j implémentera cette interface pour appeler Gemini.
 */
public interface GuideTouristique {

    /**
     * Génère des informations touristiques pour un lieu donné.
     *
     * @param location Le nom de la ville ou du pays
     * @param numberOfPlaces Le nombre d'endroits à visiter
     * @return Un objet TouristInfo avec les données
     */
    @SystemMessage("""
        Tu es un guide touristique expert. 
        Pour chaque ville ou pays demandé, tu dois fournir :
        1. Les principaux endroits à visiter (selon le nombre demandé)
        2. Le prix moyen d'un repas dans la devise du pays
        
        N'utilise pas Markdown.
        
        Ta réponse DOIT être strictement au format JSON suivant :
        {
          "ville_ou_pays": "nom de la ville ou du pays",
          "endroits_a_visiter": ["endroit 1", "endroit 2", ...],
          "prix_moyen_repas": "<prix> <devise du pays>"
        }
        
        Ne retourne RIEN d'autre que ce JSON pur, sans backticks ni formatting.
        """)
    @UserMessage("Indique les {{numberOfPlaces}} principaux endroits à visiter à/en {{location}} et le prix moyen d'un repas.")
    TouristInfo generateInfo(@V("location") String location, @V("numberOfPlaces") int numberOfPlaces);
}