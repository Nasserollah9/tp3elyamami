package ma.emsi.elyamami.tp3elyamami.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Configure la racine de l'application JAX-RS.
 * L'annotation @ApplicationPath définit la base de l'API REST.
 * * Tous les endpoints seront accessibles via: [CONTEXT_ROOT]/api/...
 */
@ApplicationPath("api")
public class RestApplication extends Application {
    // Cette classe est vide car Payara et CDI détectent automatiquement
    // les ressources (@Path) et les producteurs (@Produces) dans les packages.
}