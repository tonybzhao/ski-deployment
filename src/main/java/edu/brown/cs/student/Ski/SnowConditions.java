package edu.brown.cs.student.Ski;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.Ski.Records.SnowForecast;
import okio.Buffer;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class SnowConditions {

    public SnowConditions(){

    }

    public SnowForecast getForecast(String resort) throws IOException, InterruptedException {
        resort = this.regexInput(resort);
        try{
            String apiKey = "8e5a8dc010mshe2e53dfed6089f7p1eff16jsnde54793f6715";
            String apiHost = "ski-resort-forecast.p.rapidapi.com";

            String apiUrl = "https://ski-resort-forecast.p.rapidapi.com/" + resort + "/snowConditions?units=i";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to GET
            connection.setRequestMethod("GET");

            // Set request headers
            connection.setRequestProperty("X-RapidAPI-Key", apiKey);
            connection.setRequestProperty("X-RapidAPI-Host", apiHost);

            //   HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<SnowForecast> jsonAdapter = moshi.adapter(SnowForecast.class);
            // System.out.println(connection.getInputStream());
            SnowForecast body =
                    jsonAdapter.fromJson(new Buffer().readFrom(connection.getInputStream()));
            connection.disconnect();
          //  System.out.println(body);
            return body;
        }  catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    public String regexInput(String input){

        String[] splitArray = input.split(" ");
        //System.out.println(splitArray[0]);

        String returnString = "";
        if(splitArray.length > 1) {
            returnString = splitArray[0];
            for (int i = 1; i < splitArray.length; i++) {
                if(splitArray[i].contains("/")){
                    splitArray = splitArray[i].split("/");
                    returnString = returnString + "%20"  + splitArray[0];
                    break;
                }
                returnString = returnString + "%20" + splitArray[i];
            }
        } else {
            if(splitArray[0].contains("/")){
                splitArray = splitArray[0].split("/");
            }
            returnString = splitArray[0];
        }
        return returnString;
    }
}
