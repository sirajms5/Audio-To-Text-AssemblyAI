import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

import com.google.gson.Gson;

// Connector class will use .net library to POST and GET requests and responses to the assemblyAI
public class AssemblyAiConnector {

    private HttpRequest postRequest;
    private HttpRequest getRequest;
    private HttpClient httpClient = HttpClient.newHttpClient();

    // building a POST request
    public void createPostRequest(String key, String jsonRequest) {
        try {
            postRequest = HttpRequest.newBuilder()
                    .uri(new URI("https://api.assemblyai.com/v2/transcript"))
                    .header("Authorization", key)
                    .POST(BodyPublishers.ofString(jsonRequest))
                    .build();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    // sending and restoring POST request
    public Transcript postResponse(Transcript transcript, Gson gson) {
        try {
            HttpResponse<String> postResponse = httpClient.send(postRequest, BodyHandlers.ofString());
            System.out.println(postResponse.body());

            // converting the response from json to object
            transcript = gson.fromJson(postResponse.body(), Transcript.class);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        return transcript;
    }

    // building a GET request
    public void createGetRequest(String key, Transcript transcript) {
        try {
            getRequest = HttpRequest.newBuilder()
                    .uri(new URI("https://api.assemblyai.com/v2/transcript/" + transcript.getId()))
                    .header("Authorization", key)
                    .build();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    // sending and restoring GET request
    public Transcript getResponse(Transcript transcript, Gson gson) {
        try {
            HttpResponse<String> getResponse = httpClient.send(getRequest, BodyHandlers.ofString());
            transcript = gson.fromJson(getResponse.body(), Transcript.class);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        return transcript;
    }
}