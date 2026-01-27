package caregoautomation;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import burp.api.montoya.MontoyaApi;

public class CareGoAutomationTab extends JPanel {
    public CareGoAutomationTab(MontoyaApi api) {
        this.setLayout(new BorderLayout());

        JLabel label = new JLabel("CareGo Automation Tab");
        label.setHorizontalAlignment(SwingConstants.CENTER);

        this.add(label, BorderLayout.CENTER);
    }
}