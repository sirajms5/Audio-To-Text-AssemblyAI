import com.google.gson.*;

/*
* This is the class with method main, it will ask the user
* for an audio link and send it to assemblyai to convert it into text and output the text
*/
public class App {
    public static void main(String[] args) throws Exception {

        String option;
        ApiKey apiKey = new ApiKey();
        final String KEY = apiKey.getKey(); // this variable will have the API key, make sure to use your own key.
        User user = new User();

        do {
            String userInput = user.inputString("Enter URL for the audio to be converted: ");

            Transcript transcript = new Transcript();
            transcript.setAudio_url(userInput);

            Gson gson = new Gson();
            String jsonRequest = gson.toJson(transcript); // will convert the provided link into json format
            System.out.println(jsonRequest);

            AssemblyAiConnector connector = new AssemblyAiConnector();
            connector.createPostRequest(KEY, jsonRequest); // building a POST request
            transcript = connector.postResponse(transcript, gson); // sending and restoring POST request
            connector.createGetRequest(KEY, transcript); // building a GET request

            do {
                transcript = connector.getResponse(transcript, gson); // sending and restoring GET request
                System.out.println(transcript.getStatus());
                Thread.sleep(1000);
            } while (transcript.getStatus() != null
                    && !(transcript.getStatus().equals("completed") || transcript.getStatus().equals("error")));

            if (transcript.getStatus() != null
                    && transcript.getStatus().equals("completed")) {
                System.out.println("Transcription completed!");
                String text = transcript.getText();
                text = text.replaceAll("([?.])(\\s)", "$1\n"); // rearrange the converted audio into seperated lines
                System.out.println(text); // output the text that was converted from the audio
            } else {
                System.out.println(
                        "*** Make sure that the link is for an audio/video file and not a website such as YouTube. ***");
            }
            option = user.inputString("Would you like to enter another url? (Y/N): ");
        } while (option.equalsIgnoreCase("Y"));
    }
}
