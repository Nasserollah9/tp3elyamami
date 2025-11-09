package ma.emsi.elyamami.tp3elyamami.rest;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ma.emsi.elyamami.tp3elyamami.llm.GuideTouristique;


/**
 * Ressource JAX-RS pour l'API du guide touristique.
 * Chemin de base: /api/guide
 */
@Path("/guide")
@RequestScoped
public class GuideTouristiqueResource {

    // Injection du service LLM configuré par la classe LlmService
    @Inject
    private GuideTouristique llmGuide;

    /**
     * Endpoint pour obtenir des informations touristiques sur une ville ou un pays.
     * URL: /api/guide/lieu/{ville_ou_pays}/{nb}
     * * @param location Le nom de la ville ou du pays (e.g., "Paris", "Maroc").
     * @param nb Le nombre d'endroits à visiter à retourner (bonus implémenté).
     * @return Une réponse HTTP contenant un objet TouristInfo sérialisé en JSON.
     */
    @GET
    @Path("lieu/{ville_ou_pays}/{nb}")
    // L'annotation @Produces indique que la méthode retourne du JSON
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTouristInfo(
            @PathParam("ville_ou_pays") String location,
            @PathParam("nb") int nb) {

        if (location == null || location.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Le paramètre ville_ou_pays est requis.")
                    .build();
        }

        try {
            // Appel au LLM en utilisant l'interface implémentée par LangChain4j
            TouristInfo touristInfo = llmGuide.generateInfo(location, nb);

            // Créer la réponse HTTP OK (200) avec l'objet JSON
            Response.ResponseBuilder responseBuilder = Response.ok(touristInfo);

            // Pour permettre le test depuis une page HTML locale (CORS) et désactiver le cache
            responseBuilder.header("Access-Control-Allow-Origin", "*");
            responseBuilder.header("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
            responseBuilder.header("Pragma", "no-cache");

            return responseBuilder.build();

        } catch (Exception e) {
            // Gestion des erreurs de l'API ou de la connexion
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"erreur\": \"Échec de l'appel au LLM: " + e.getMessage() + "\", \"detail\": \"Vérifiez la clé Gemini et la connexion.\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }
}