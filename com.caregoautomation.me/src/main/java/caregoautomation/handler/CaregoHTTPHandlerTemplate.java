package caregoautomation.handler;

import java.util.ArrayList;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.handler.HttpHandler;
import burp.api.montoya.http.handler.HttpRequestToBeSent;
import burp.api.montoya.http.handler.HttpResponseReceived;
import burp.api.montoya.http.handler.RequestToBeSentAction;
import burp.api.montoya.http.handler.ResponseReceivedAction;
import caregoautomation.automate.CaregoAutomateSessionLogger;

/**
 * This http handler acts as a kind of "middleware" template
 * for every actions that are happening from the proxy
 * 
 * You can add logic in between `handleHttpRequestToBeSent` and `handleHttpResponseReceived`
 */
public class CaregoHTTPHandlerTemplate implements HttpHandler {
    private final MontoyaApi api;
    private final ArrayList<String> sessionStorage;

    public CaregoHTTPHandlerTemplate(MontoyaApi api, ArrayList<String> sessionStorage) {
        this.api = api;
        this.sessionStorage = sessionStorage;
    }

    @Override
    public RequestToBeSentAction handleHttpRequestToBeSent(HttpRequestToBeSent requestToBeSent) {
        return RequestToBeSentAction.continueWith(requestToBeSent);
    }

    @Override
    public ResponseReceivedAction handleHttpResponseReceived(HttpResponseReceived responseReceived) {
        CaregoAutomateSessionLogger.sessionLoginLogger(this.api, responseReceived, this.sessionStorage);
        return ResponseReceivedAction.continueWith(responseReceived);
    }
}

