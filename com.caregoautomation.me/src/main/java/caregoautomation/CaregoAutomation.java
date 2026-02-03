package caregoautomation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.message.HttpRequestResponse;
import caregoautomation.handler.CaregoHTTPHandlerTemplate;
import caregoautomation.menu_items.CaregoContextMenuItems;
import caregoautomation.ui.CareGoAutomationTab;

public class CaregoAutomation implements BurpExtension
{
    public final ArrayList<String> sessions;
    public final Map<String, HttpRequestResponse> chosenRequestMap;

    public CaregoAutomation() {
        this.sessions = new ArrayList<>();
        this.chosenRequestMap = new HashMap<>();
    }

    @Override
    public void initialize(MontoyaApi api) {
        api.extension().setName("CaregoAutomation Extension");
        api.userInterface().registerContextMenuItemsProvider(
            new CaregoContextMenuItems(
                api, this.sessions, this.chosenRequestMap
            )
        );

        api.userInterface().registerSuiteTab("CareGo Automation", new CareGoAutomationTab(api));
        api.http().registerHttpHandler(new CaregoHTTPHandlerTemplate(api, this.sessions));
    }
}
