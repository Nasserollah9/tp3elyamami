package ma.emsi.elyamami.tp3elyamami.llm;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import ma.emsi.elyamami.tp3elyamami.rest.TouristInfo;
// L'import problématique a été retiré.


/**
 * Interface de service LLM pour le guide touristique.
 * LangChain4j implémentera cette interface pour appeler Gemini.
 */
public interface GuideTouristique {

    // IMPORTANT: Le rôle système force le LLM à répondre dans le format JSON précis.
    @SystemMessage("""
        Vous êtes un guide touristique expert. Votre tâche est de fournir des informations 
        précises sur une ville ou un pays.
        N'utilise pas Markdown. Répondez STRICTEMENT au format JSON suivant :
        {
          "ville_ou_pays": "<nom du lieu>",
          "endroits_a_visiter": ["<endroit 1>", "<endroit 2>", ...],
          "prix_moyen_repas": "<prix> <devise du pays>"
        }
        """)
    @UserMessage("Indique les {numberOfPlaces} principaux endroits à visiter à/en {location} et le prix moyen d'un repas.")
    // L'annotation @ResponseFormat.Json (ou @Json) a été retirée pour résoudre la compilation.
    // L'annotation @ResponseFormat.Json est maintenant gérée dans LlmService.java.
    TouristInfo generateInfo(
            // FIX: Suppression des annotations @UserMessage sur les paramètres.
            // LangChain4j mappe automatiquement par nom de paramètre (location, numberOfPlaces)
            String location,
            int numberOfPlaces
    );
}