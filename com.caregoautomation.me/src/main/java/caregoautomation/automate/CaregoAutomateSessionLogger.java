package caregoautomation.automate;


import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.handler.HttpResponseReceived;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.http.message.responses.HttpResponse;
import caregoautomation.models.AuthModel;


public class CaregoAutomateSessionLogger {
    /**
     * A simple functionality for logging the sessions
     * detected for each of the http responses retrieved
     */
    public static void sessionLoginLogger(
        MontoyaApi api,
        HttpResponseReceived responseReceived,
        ArrayList<String> sessionStorage
    ) {
        HttpRequest request = responseReceived.initiatingRequest();

        String path = request.path().toLowerCase();
        if (path.contains("login/company") && request.method().toLowerCase().equals("post")) {

            HttpResponse response = responseReceived.withStatusCode((short)200);
            String responseBody = response.bodyToString().trim();

            try {
                Gson jsonParser = new Gson();
                AuthModel authResponse = jsonParser.fromJson(responseBody, AuthModel.class);

                // ensure that the token is properly set
                if (authResponse.token != null) {
                    api.logging().logToOutput("[+] Auth Token Detected: " + authResponse.token);
                    sessionStorage.add(authResponse.token);
                }
            } catch (JsonSyntaxException e) {
                api.logging().logToOutput(e);
            }
        }
    }
}