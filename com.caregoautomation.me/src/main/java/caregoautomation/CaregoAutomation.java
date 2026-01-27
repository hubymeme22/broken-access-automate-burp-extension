package caregoautomation;

import java.util.ArrayList;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import caregoautomation.handler.CaregoHTTPHandlerTemplate;

public class CaregoAutomation implements BurpExtension
{
    public final ArrayList<String> sessions;

    public CaregoAutomation() {
        this.sessions = new ArrayList<>();
    }

    @Override
    public void initialize(MontoyaApi api) {
        api.extension().setName("CaregoAutomation Extension");
        api.userInterface().registerContextMenuItemsProvider(new CaregoContextMenuItems(api, this.sessions));

        api.userInterface().registerSuiteTab("CareGo Automation", new CareGoAutomationTab(api));
        api.http().registerHttpHandler(new CaregoHTTPHandlerTemplate(api, this.sessions));
    }
}
