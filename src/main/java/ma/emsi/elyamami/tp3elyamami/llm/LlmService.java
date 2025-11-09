package ma.emsi.elyamami.tp3elyamami.llm;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import java.io.Serializable;

/**
 * Service CDI pour configurer et produire l'instance du service GuideTouristique.
 * Utilisé pour l'injection dans la ressource JAX-RS.
 *
 * Cette classe est SIMPLE et STATELESS (sans mémoire de conversation)
 * car les APIs REST doivent être sans état.
 *
 * Pattern Producer de CDI :
 * - @ApplicationScoped : Une seule instance pour toute l'application
 * - @Produces : Permet l'injection de GuideTouristique dans d'autres beans
 */
@ApplicationScoped
public class LlmService implements Serializable {

    /**
     * Méthode producer qui crée et configure l'instance de GuideTouristique.
     * Cette méthode est appelée par CDI lors de l'injection.
     *
     * Chaque appel à l'interface GuideTouristique sera INDÉPENDANT
     * (pas de mémoire entre les requêtes = stateless).
     *
     * @return Une instance configurée de GuideTouristique
     * @throws RuntimeException si la clé API n'est pas définie
     */
    @Produces
    public GuideTouristique createGuideTouristique() {
        // Récupération de la clé API depuis les variables d'environnement
        String geminiKey = System.getenv("GEMINI_KEY");

        if (geminiKey == null || geminiKey.isEmpty()) {
            throw new RuntimeException(
                    "ERREUR : La variable d'environnement GEMINI_KEY n'est pas définie. " +
                            "Définissez-la dans votre serveur d'applications ou IDE."
            );
        }

        System.out.println("✓ Initialisation du service LLM avec Gemini...");

        // Créer le modèle de chat Gemini
        ChatModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(geminiKey)
                .modelName("gemini-2.5-flash") // Rapide et économique
                // Alternatives possibles :
                // - "gemini-1.5-pro-latest" : plus puissant
                // - "gemini-pro" : version stable
                .build();

        // Créer l'implémentation de l'interface GuideTouristique
        // AiServices implémente automatiquement l'interface avec @SystemMessage

        GuideTouristique service = AiServices.builder(GuideTouristique.class)
                .chatModel(model)
                // PAS de .chatMemory() = stateless (parfait pour REST)
                .build();

        System.out.println("✓ Service GuideTouristique prêt !");

        return service;
    }
}
