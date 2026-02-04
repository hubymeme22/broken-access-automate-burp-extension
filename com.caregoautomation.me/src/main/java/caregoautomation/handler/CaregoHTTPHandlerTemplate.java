package caregoautomation.handler;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.handler.HttpHandler;
import burp.api.montoya.http.handler.HttpRequestToBeSent;
import burp.api.montoya.http.handler.HttpResponseReceived;
import burp.api.montoya.http.handler.RequestToBeSentAction;
import burp.api.montoya.http.handler.ResponseReceivedAction;
import caregoautomation.CaregoAutomation;
import caregoautomation.automate.CaregoAutomateSessionLogger;

/**
 * This http handler acts as a kind of "middleware" template
 * for every actions that are happening from the proxy
 * 
 * You can add logic in between `handleHttpRequestToBeSent` and `handleHttpResponseReceived`
 */
public class CaregoHTTPHandlerTemplate implements HttpHandler {
    private final MontoyaApi api;
    private final CaregoAutomation controller;

    public CaregoHTTPHandlerTemplate(MontoyaApi api, CaregoAutomation controller) {
        this.api = api;
        this.controller = controller;
    }

    @Override
    public RequestToBeSentAction handleHttpRequestToBeSent(HttpRequestToBeSent requestToBeSent) {
        CaregoAutomateSessionLogger.generalRequestSessionLogger(this.api, requestToBeSent, this.controller);
        return RequestToBeSentAction.continueWith(requestToBeSent);
    }

    @Override
    public ResponseReceivedAction handleHttpResponseReceived(HttpResponseReceived responseReceived) {
        CaregoAutomateSessionLogger.sessionLoginLogger(this.api, responseReceived, this.controller);
        return ResponseReceivedAction.continueWith(responseReceived);
    }
}

