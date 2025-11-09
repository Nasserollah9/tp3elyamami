package ma.emsi.elyamami.tp3elyamami.rest;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
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

    // Injection du service LLM configuré par LlmService
    @Inject
    private GuideTouristique llmGuide;

    /**
     * Endpoint de test pour vérifier que l'API fonctionne.
     * URL: /api/guide/test
     *
     * @return Un message de confirmation
     */
    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_PLAIN)
    public String testEndpoint() {
        return "API Guide Touristique fonctionne ! ✓";
    }

    // ========================================================================
    // BONUS 1 : Nombre d'endroits dans le CHEMIN de l'URL
    // ========================================================================

    /**
     * Endpoint avec nombre d'endroits personnalisé dans le chemin.
     * URL: /api/guide/lieu/{ville_ou_pays}/{nb}
     * Exemple: /api/guide/lieu/Paris/4
     *
     * @param location Le nom de la ville ou du pays
     * @param nb Le nombre d'endroits à visiter
     * @return Une réponse HTTP avec TouristInfo en JSON
     */
    @GET
    @Path("/lieu/{ville_ou_pays}/{nb}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTouristInfoWithNumber(
            @PathParam("ville_ou_pays") String location,
            @PathParam("nb") int nb) {

        System.out.println("=== BONUS 1 : Nombre dans le chemin ===");
        System.out.println("Lieu: " + location);
        System.out.println("Nombre d'endroits: " + nb);

        return processRequest(location, nb);
    }

    // ========================================================================
    // BONUS 2 : Nombre d'endroits comme PARAMÈTRE de requête (?nb=X)
    // ========================================================================

    /**
     * Endpoint avec nombre d'endroits comme paramètre de requête.
     * URL: /api/guide/lieu/{ville_ou_pays}?nb=4
     * Exemple: /api/guide/lieu/Paris?nb=4
     * Si nb n'est pas fourni, la valeur par défaut est 2.
     *
     * @param location Le nom de la ville ou du pays
     * @param nb Le nombre d'endroits à visiter (défaut: 2)
     * @return Une réponse HTTP avec TouristInfo en JSON
     */
    @GET
    @Path("/lieu/{ville_ou_pays}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTouristInfoWithQueryParam(
            @PathParam("ville_ou_pays") String location,
            @QueryParam("nb") @DefaultValue("2") int nb) {

        System.out.println("=== BONUS 2 : Nombre comme paramètre ?nb= ===");
        System.out.println("Lieu: " + location);
        System.out.println("Nombre d'endroits: " + nb);

        return processRequest(location, nb);
    }

    /**
     * Traite la requête et appelle le LLM.
     */
    private Response processRequest(String location, int nb) {
        // Validation des paramètres
        if (location == null || location.trim().isEmpty()) {
            System.err.println("ERREUR: Paramètre location vide");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"erreur\": \"Le paramètre ville_ou_pays est requis.\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        if (nb <= 0 || nb > 10) {
            System.err.println("ERREUR: Nombre d'endroits invalide: " + nb);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"erreur\": \"Le nombre d'endroits doit être entre 1 et 10.\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        try {
            System.out.println("Appel au LLM...");

            // Vérifier que l'injection a fonctionné
            if (llmGuide == null) {
                System.err.println("ERREUR CRITIQUE: llmGuide est null (injection CDI a échoué)");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"erreur\": \"Service LLM non initialisé. Vérifiez beans.xml et LlmService.\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            // Appel au LLM
            TouristInfo touristInfo = llmGuide.generateInfo(location, nb);

            System.out.println("Réponse reçue du LLM: " + touristInfo);

            // Vérification de la réponse
            if (touristInfo == null || touristInfo.getVille_ou_pays() == null) {
                System.err.println("ERREUR: Réponse du LLM invalide");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"erreur\": \"Le LLM n'a pas retourné de données valides.\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            System.out.println("✓ Succès ! Envoi de la réponse...");

            // Créer la réponse HTTP OK (200)
            return Response.ok(touristInfo)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0")
                    .header("Pragma", "no-cache")
                    .build();

        } catch (Exception e) {
            System.err.println("EXCEPTION lors de l'appel au LLM:");
            e.printStackTrace();

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"erreur\": \"Échec de l'appel au LLM\", " +
                            "\"message\": \"" + e.getMessage() + "\", " +
                            "\"detail\": \"Vérifiez la clé GEMINI_KEY et les logs du serveur.\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }
}