package caregoautomation.automate;

import java.util.ArrayList;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.requests.HttpRequest;


/**
 * A simple class designed for automating broken access
 * request and response checking. The original flow for this
 * is that we can provide two (or more) different tokens
 * and we can check 
 */
public class CaregoAutomateBrokenAccess {
    private final HttpRequestResponse requestResponse;
    private final MontoyaApi api;

    public CaregoAutomateBrokenAccess(MontoyaApi api, HttpRequestResponse requestResponse) {
        this.requestResponse = requestResponse;
        this.api = api;
    }

    public void proofOfConcept(ArrayList<String> sessionStorage) {
        this.api.logging().logToOutput("[+] Testing request idea...");

        // retrieve the request ascii
        String contents = this.requestResponse.request().toString();

        // original request
        this.api.logging().logToOutput("[*] Request Before:");
        this.api.logging().logToOutput("\n\n" + contents);

        // new requests based on the derived requests
        ArrayList<HttpRequest> modifiedRequests = this.getAllBearerConsidered(sessionStorage);
        if (modifiedRequests.size() <= 0) {
            this.api.logging().logToOutput("[*] No bearer received... cannot automate test");
            return;
        }

        this.api.logging().logToOutput("[*] Request After:");
        for (HttpRequest requestMod: modifiedRequests) {
            String requestModString = requestMod.toString();
            this.api.logging().logToOutput("\n\n" + requestModString);
        }
    }

    /**
     * Generates an array list of http requests based on requestResponse
     * passed from the constructor.
     * 
     * Each of the http-request contains modified authorization value
     * based on the session token retrieved from the current session
     */
    public ArrayList<HttpRequest> getAllBearerConsidered(ArrayList<String> sessionStorage) {
        ArrayList<HttpRequest> retrievedRequests = new ArrayList<>();

        for (String sessionToken : sessionStorage) {
            retrievedRequests.add(
                this.requestResponse.request().withUpdatedHeader(
                    "authorization",
                    "Bearer " + sessionToken
                )
            );
        }

        return retrievedRequests;
    }
}