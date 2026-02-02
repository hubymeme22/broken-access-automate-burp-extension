package caregoautomation.menu_items;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.core.ToolType;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.ui.contextmenu.ContextMenuEvent;
import burp.api.montoya.ui.contextmenu.ContextMenuItemsProvider;
import caregoautomation.automate.CaregoAutomateBrokenAccess;

public class CaregoContextMenuItems implements ContextMenuItemsProvider {
    private final MontoyaApi api;
    private final ArrayList<String> sessionStorage;

    public CaregoContextMenuItems(MontoyaApi api, ArrayList<String> sessionStorage) {
        this.api = api;
        this.sessionStorage = sessionStorage;
    }

    @Override
    public List<Component> provideMenuItems(ContextMenuEvent event)
    {
        // check if tool is pointing to repeater or proxy
        if (event.isFromTool(ToolType.REPEATER, ToolType.PROXY)) {
            List<Component> menuItemList = new ArrayList<>();

            // retrieves the currently selected request
            HttpRequestResponse requestResponse = this.retrieveHttpRequest(event);

            // menu item for sending to test
            JMenuItem sendToCareGoAutomation = new JMenuItem("Send to CareGo Automation");
            JMenuItem sendCareGoTesting = new JMenuItem("Quick Test");

            // event listeners for menu items
            sendCareGoTesting.addActionListener(l -> this.careGoTerminalTest(requestResponse));

            menuItemList.add(sendToCareGoAutomation);
            menuItemList.add(sendCareGoTesting);

            return menuItemList;
        }
        return null;
    }

    /**
     * Retrieves the request selected from the menu so it
     * can be passed to the CareGo Automation
     */
    private HttpRequestResponse retrieveHttpRequest(ContextMenuEvent event) {
        HttpRequestResponse requestResponse;
        if (event.messageEditorRequestResponse().isPresent()) {
            requestResponse = event.messageEditorRequestResponse().get().requestResponse();
        } else {
            requestResponse = event.selectedRequestResponses().get(0);
        }

        return requestResponse;
    }

    /**
     * Checks and sends the request for automated testing
     */
    private void careGoTerminalTest(HttpRequestResponse requestResponse) {
        // check if the request can be tested with the automation
        HttpRequest request = requestResponse.request();
        if (!requestResponse.contains("bearer", false)) {
            this.api.logging().logToError(
                String.format(
                    "The request %s - %s cannot be tested due to lack of authorization",
                    request.method(),
                    request.path()
                )
            );

            return;
        }

        // broken access checks
        CaregoAutomateBrokenAccess brokenAccessCheck = new CaregoAutomateBrokenAccess(this.api, requestResponse);
        brokenAccessCheck.proofOfConcept(this.sessionStorage);
    }
}