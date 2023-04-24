import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

import com.google.gson.*;

/*
* This is the class with method main, it will ask the user
* for an audio link and send it to assemblyai to convert it into text and output the text
*/
public class App {
    public static void main(String[] args) throws Exception {
        
        ApiKey apiKey = new ApiKey();
        final String KEY = apiKey.getKey(); // this variable will have the API key, make sure to use your own key.

        User user = new User();
        String userInput = user.inputString("Enter URL for the audio to be converted: ");
        Transcript transcript = new Transcript();
        transcript.setAudio_url(userInput);
        Gson gson = new Gson();
        String jsonRequest = gson.toJson(transcript); // will convert the provided link into json format
        System.out.println(jsonRequest);

        // building a POST request
        HttpRequest postRequest = HttpRequest.newBuilder()
        .uri(new URI("https://api.assemblyai.com/v2/transcript"))
        .header("Authorization", KEY)
        .POST(BodyPublishers.ofString(jsonRequest))
        .build();

        // sending and restoring POST request
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> postResponse = httpClient.send(postRequest, BodyHandlers.ofString());
        System.out.println(postResponse.body());
        
        // converting the response from json to object
        transcript = gson.fromJson(postResponse.body(), Transcript.class);

        // building a GET request
        HttpRequest getRequest = HttpRequest.newBuilder()
        .uri(new URI("https://api.assemblyai.com/v2/transcript/" + transcript.getId()))
        .header("Authorization", KEY)
        .build();

        do {
        // sending and restoring GET request
        HttpResponse<String> getResponse = httpClient.send(getRequest, BodyHandlers.ofString());
        transcript = gson.fromJson(getResponse.body(), Transcript.class);
        System.out.println(transcript.getStatus());
        Thread.sleep(1000);
        } while (!(transcript.getStatus().equals("completed") || transcript.getStatus().equals("error")));

        if(transcript.getStatus().equals("completed")){
            System.out.println("Transcription completed!");
            System.out.println(transcript.getText()); // output the text that was converted from the audio
        } else {
            System.out.println("*** Make sure that the link is for an audio/video file and not a website such as YouTube. ***");
        }        
    }
}
