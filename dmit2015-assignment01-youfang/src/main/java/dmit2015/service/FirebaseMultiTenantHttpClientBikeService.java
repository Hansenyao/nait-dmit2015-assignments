package dmit2015.service;

import dmit2015.faces.FirebaseAuthSignInSession;
import dmit2015.model.Bike;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.json.JsonObject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

/**
 * This class implements the StudentService using the HttpClient library
 * to send Http Request to the Firebase Realtime Database REST API.
 * <p>
 * Verify that you have set up the following Rules in your Firebase Realtime Database
 * where the multi-tenant data is in a root path name "multi_tenant_data".
 * <p>
 * {
 * "rules": {
 * "multi_tenant_data": {
 * "Bike": {
 * "$uid": {
 * // Allow only authenticated content owners access to their data
 * ".read": "auth !== null && auth.uid === $uid",
 * ".write": "auth !== null && auth.uid === $uid"
 * }
 * }
 * }
 * }
 * }
 */

@Named("firebaseMultiTenantHttpClientBikeService")
@ApplicationScoped
public class FirebaseMultiTenantHttpClientBikeService implements BikeService {
    /**
     * The base URL to the Firebase Realtime Database that is defined in
     * `src/main/resources/META-INF/microprofile-config.properties` file.
     *
     */
    @Inject
    @ConfigProperty(name = "firebase.rtdb.Bike.base.url")
    private String firebaseRtdbBaseUrl;

    /**
     * The URL to the Firebase Realtime Database to access all data.
     */
    private String jsonAllDataPath;

    /**
     * HttpClient is native Java library for sending Http Request to a web server
     */
    private HttpClient httpClient;

    /**
     * Jsonb is used for converting Java objects to a JSON string or visa-versa
     */
    private Jsonb jsonb;

    @Inject
    private FirebaseAuthSignInSession firebaseAuthSignInSession;

    private static final String MULTI_TENANT_PATH_PREFIX = "multi_tenant_data";

    @PostConstruct
    private void init() {
        httpClient = HttpClient.newHttpClient();
        jsonb = JsonbBuilder.create();
    }

    /**
     * Pushing currentBike data to Firebase Realtime Database using the REST API
     *
     * @link <a href="https://firebase.google.com/docs/reference/rest/database">Firebase Realtime Database REST API</a>
     */
    @Override
    public Bike createBike(Bike bike) {

        // Get the Firebase Authenticated userId and token.
        String firebaseUserId = firebaseAuthSignInSession.getFirebaseAuthSignInResponsePayload().getLocalId();
        String firebaseToken = firebaseAuthSignInSession.getFirebaseAuthSignInResponsePayload().getIdToken();
        // Set the path in the database for content-owner access only data
        jsonAllDataPath = String.format("%s/%s/%s/%s.json?auth=%s",
                firebaseRtdbBaseUrl,
                MULTI_TENANT_PATH_PREFIX,
                Bike.class.getSimpleName(),
                firebaseUserId,
                firebaseToken);

        // Convert the Java object to a JSON string using JSONB
        String requestBodyJson = jsonb.toJson(bike);

        // Create a Http Request for sending a Http POST request to push new data
        var httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(jsonAllDataPath))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson, StandardCharsets.UTF_8))
                .build();
        try {
            // Send the Http Request
            var httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            // Check if the Http Request response is successful
            if (httpResponse.statusCode() == 200) {
                // Get the body of the Http Response
                var responseBodyJson = httpResponse.body();
                // Convert the JSON String to a JsonObject
                JsonObject responseJsonObject = jsonb.fromJson(responseBodyJson, JsonObject.class);
                // Set the unique key name for this object
                bike.setId(responseJsonObject.getString("name"));

            } else {
                String errorMessage = String.format("Create was not successful with status code: %s", httpResponse.statusCode());
                throw new RuntimeException(errorMessage);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return bike;
    }

    @Override
    public Optional<Bike> getBikeById(String id) {
        // Build the url path to object to access
        String firebaseUserId = firebaseAuthSignInSession.getFirebaseAuthSignInResponsePayload().getLocalId();
        String firebaseToken = firebaseAuthSignInSession.getFirebaseAuthSignInResponsePayload().getIdToken();
        String jsonSingleDataPath = String.format("%s/%s/%s/%s/%s.json?auth=%s",
                firebaseRtdbBaseUrl,
                MULTI_TENANT_PATH_PREFIX,
                Bike.class.getSimpleName(),
                firebaseUserId,
                id,
                firebaseToken);

        try {
            // Create an GET Http Request to fetch all data
            var httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(jsonSingleDataPath))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();
            // Send the GET Http Request
            var httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            // Check if the Http Request was successful
            if (httpResponse.statusCode() == 200) {
                // Get the body of the Http Response
                var responseBodyJson = httpResponse.body();
                if (!responseBodyJson.equals("null")) {
                    // Convert the responseBodyJson to an LinkedHashMap<String, Bike>
                    Bike responseData = jsonb.fromJson(responseBodyJson, Bike.class);
                    responseData.setId(id);
                    return Optional.of(responseData);
                } else {
                    throw new RuntimeException(String.format("Bike with id of %s not found", id));
                }

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Bike> getAllBikes() {
        // Get the Firebase Authenticated userId and token.
        String firebaseUserId = firebaseAuthSignInSession.getFirebaseAuthSignInResponsePayload().getLocalId();
        String firebaseToken = firebaseAuthSignInSession.getFirebaseAuthSignInResponsePayload().getIdToken();
        // Set the path in the database for content-owner access only data
        jsonAllDataPath = String.format("%s/%s/%s/%s.json?auth=%s",
                firebaseRtdbBaseUrl,
                MULTI_TENANT_PATH_PREFIX,
                Bike.class.getSimpleName(),
                firebaseUserId,
                firebaseToken);

        // Create an GET Http Request to fetch all data
        var httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(jsonAllDataPath))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        try {
            // Send the GET Http Request
            var httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            // Check if the Http Request was successful
            if (httpResponse.statusCode() == 200) {
                // Get the body of the Http Response
                var responseBodyJson = httpResponse.body();
                // Convert the responseBodyJson to an LinkedHashMap<String, Bike>
                LinkedHashMap<String, Bike> responseData = jsonb.fromJson(responseBodyJson, new LinkedHashMap<String, Bike>() {
                }.getClass().getGenericSuperclass());
                if (responseData != null) {
                    // Convert the LinkedHashMap<String, Bike> to List<Bike>
                    return responseData.entrySet()
                            .stream()
                            .map(item -> {
                                var currentBike = new Bike();
                                currentBike.setId(item.getKey());
                                // TODO: Set each property of the Java data object
                                // currentBike.setProperty1(item.getValue().getProperty1());
                                // currentBike.setProperty2(item.getValue().getProperty2());
                                // currentBike.setProperty3(item.getValue().getProperty3());

                                return currentBike;
                            })
                            .toList();
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return List.of();
    }

    /**
     * Writing currentBike data to Firebase Realtime Database using the REST API
     *
     * @link <a href="https://firebase.google.com/docs/reference/rest/database">Firebase Realtime Database REST API</a>
     */
    @Override
    public Bike updateBike(Bike bike) {
        // Build the url path to object to access
        String firebaseUserId = firebaseAuthSignInSession.getFirebaseAuthSignInResponsePayload().getLocalId();
        String firebaseToken = firebaseAuthSignInSession.getFirebaseAuthSignInResponsePayload().getIdToken();
        String jsonSingleDataPath = String.format("%s/%s/%s/%s/%s.json?auth=%s",
                firebaseRtdbBaseUrl,
                MULTI_TENANT_PATH_PREFIX,
                Bike.class.getSimpleName(),
                firebaseUserId,
                bike.getId(),
                firebaseToken);

        // Convert the Java object to a JSON string using JSONB
        String requestBodyJson = jsonb.toJson(bike);

        // Create and Http Request to send an HTTP PUT request to write over existing data
        var httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(jsonSingleDataPath))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBodyJson, StandardCharsets.UTF_8))
                .build();
        try {
            // Send the Http Request
            var httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            // Check if the Http Response was successful
            if (httpResponse.statusCode() != 200) {
                String errorMessage = String.format("Update was not successful with status code: %s", httpResponse.statusCode());
                throw new RuntimeException(errorMessage);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return bike;
    }

    /**
     * Remove data from Firebase Realtime Database using the REST API
     *
     * @link <a href="https://firebase.google.com/docs/reference/rest/database">Firebase Realtime Database REST API</a>
     */
    @Override
    public void deleteBikeById(String id) {
        // Build the url path to object to access
        String firebaseUserId = firebaseAuthSignInSession.getFirebaseAuthSignInResponsePayload().getLocalId();
        String firebaseToken = firebaseAuthSignInSession.getFirebaseAuthSignInResponsePayload().getIdToken();
        String jsonSingleDataPath = String.format("%s/%s/%s/%s/%s.json?auth=%s",
                firebaseRtdbBaseUrl,
                MULTI_TENANT_PATH_PREFIX,
                Bike.class.getSimpleName(),
                firebaseUserId,
                id,
                firebaseToken);

        // Create an DELETE Http Request
        var httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(jsonSingleDataPath))
                .DELETE()
                .build();
        try {
            // Send the DELETE Http Request
            var httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            // Check if the Http Response was successful
            if (httpResponse.statusCode() != 200) {
                String errorMessage = String.format("Delete was not successful with status code: %s", httpResponse.statusCode());
                throw new RuntimeException(errorMessage);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}