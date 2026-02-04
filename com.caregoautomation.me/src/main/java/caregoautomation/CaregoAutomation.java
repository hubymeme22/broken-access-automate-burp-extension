package caregoautomation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.message.HttpRequestResponse;
import caregoautomation.handler.CaregoHTTPHandlerTemplate;
import caregoautomation.menu_items.CaregoContextMenuItems;
import caregoautomation.ui.CareGoAutomationUITab;

public class CaregoAutomation implements BurpExtension
{
    public final ArrayList<String> sessions;
    public final Map<String, HttpRequestResponse> chosenRequestMap;
    public CareGoAutomationUITab uiTab;

    public CaregoAutomation() {
        this.sessions = new ArrayList<>();
        this.chosenRequestMap = new HashMap<>();
    }

    @Override
    public void initialize(MontoyaApi api) {
        api.extension().setName("CaregoAutomation Extension");

        // automation tab user interface
        this.uiTab = new CareGoAutomationUITab(api, this);
        api.userInterface().registerSuiteTab("CareGo Automation", this.uiTab);

        // context menu for repeater and proxy
        api.userInterface().registerContextMenuItemsProvider(
            new CaregoContextMenuItems(api, this)
        );

        // http reqest and response middleware
        api.http().registerHttpHandler(new CaregoHTTPHandlerTemplate(api, this.sessions));
    }
}
