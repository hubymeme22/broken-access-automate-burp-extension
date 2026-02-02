package caregoautomation.automate;


import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.handler.HttpRequestToBeSent;
import burp.api.montoya.http.handler.HttpResponseReceived;
import burp.api.montoya.http.message.HttpHeader;
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

        // unique token upon login
        if (path.contains("login/company") && request.method().toLowerCase().equals("post")) {
            HttpResponse response = responseReceived.withStatusCode((short)200);
            String responseBody = response.bodyToString().trim();

            try {
                Gson jsonParser = new Gson();
                AuthModel authResponse = jsonParser.fromJson(responseBody, AuthModel.class);

                // ensure that the token is properly set
                if (authResponse.token != null) {
                    // ensure that token is unique everytime
                    if (!sessionStorage.contains(authResponse.token)) {
                        api.logging().logToOutput("[+] Login Session Found!: " + authResponse.token);
                        sessionStorage.add(authResponse.token);
                    }
                }
            } catch (JsonSyntaxException e) {
                api.logging().logToOutput(e);
            }
        }
    }

    /**
     * A simple request session logger for checking specific
     * general requests
     */
    public static void generalRequestSessionLogger(
        MontoyaApi api,
        HttpRequestToBeSent responseReceived,
        ArrayList<String> sessionStorage
    ) {
        HttpRequest request = responseReceived.copyToTempFile();
        String path = request.path().toLowerCase();

        // unique token upon login
        if (path.contains("user-details") && request.method().toLowerCase().equals("get")) {
            if (request.hasHeader("Authorization")) {
                HttpHeader authHeader = request.header("Authorization");
                String bearerToken = authHeader.value().replace("Bearer ", "");

                // unique session token found
                if (bearerToken != null && !bearerToken.equals("") && !sessionStorage.contains(bearerToken)) {
                    api.logging().logToOutput("[+] Unique Request Session Found!: " + bearerToken);
                    sessionStorage.add(bearerToken);
                }
            }            
        }

        // removal of tokens
    }
}