package caregoautomation.ui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import burp.api.montoya.MontoyaApi;
import caregoautomation.CaregoAutomation;
import caregoautomation.ui.left_panel.LeftPanel;

public class CareGoAutomationUITab extends JPanel {
    private final MontoyaApi api;
    private final CaregoAutomation controller;

    public CareGoAutomationUITab(
        MontoyaApi api,
        CaregoAutomation controller
    ) {
        this.controller = controller;
        this.api = api;
        buildUI();
    }

    private void buildUI() {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(this::buildUI);
            return;
        }

        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JSplitPane centerSplitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            (new LeftPanel(this.api, this.controller)),
            this.rightPanel()
        );

        centerSplitPane.setResizeWeight(0.6);
        centerSplitPane.setDividerLocation(0.6);
        centerSplitPane.setOneTouchExpandable(false);

        this.add(this.headerPanel(), BorderLayout.NORTH);
        this.add(centerSplitPane, BorderLayout.CENTER);
    }

    /**
     * Header panel containing the message title
     * and subtitle above the interface
     */
    private JComponent headerPanel() {
        JPanel headerPanel = new JPanel();
        JLabel titleText = new JLabel("CareGo Automation");
        JLabel subTitleText = new JLabel("This extension provides you with automation techniques specifically designed for CareGo Health Suite application");

        // stack components vertically & add border
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(5,5,10,5));

        // font adjustments
        titleText.setFont(titleText.getFont().deriveFont(Font.BOLD, 16f));
        subTitleText.setFont(subTitleText.getFont().deriveFont(Font.PLAIN, 14f));

        headerPanel.add(titleText);
        headerPanel.add(subTitleText);

        return headerPanel;
    }


    /**
     * Right-side panel of the UI, this part contains
     * the UI: test results and tokens
     */
    private JComponent rightPanel() {
        JPanel rightPanel = new JPanel();

        return rightPanel;
    }
}