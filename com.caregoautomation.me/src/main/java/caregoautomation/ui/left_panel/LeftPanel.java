package caregoautomation.ui.left_panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.requests.HttpRequest;
import caregoautomation.CaregoAutomation;

public class LeftPanel extends JPanel {

    private final MontoyaApi api;
    private final CaregoAutomation controller;
    private final String[] columns = { "Method", "IP Address", "Host", "Path", "Date" };

    private JTable requestTable;

    private final DefaultTableModel requestTableModel = new DefaultTableModel(this.columns, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public LeftPanel(MontoyaApi api, CaregoAutomation controller) {
        this.api = api;
        this.controller = controller;
        this.build();
    }

    private void build() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.add(this.tableContents(), BorderLayout.NORTH);
    }

    /**
     * Upper left table that holds requests chosen
     * and shows the current selected requests for testing
     */
    private JComponent tableContents() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel buttonPanel = new JPanel();

        JButton basicTestBtn = new JButton("Basic Test");
        JButton advancedTestBtn = new JButton("Advanced Test");
        JButton removeBtn = new JButton("Remove");

        panel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        Dimension btnSize = new Dimension(150, 25);
        for (JButton btn : new JButton[]{ basicTestBtn, advancedTestBtn, removeBtn }) {
            btn.setPreferredSize(btnSize);
            btn.setMaximumSize(btnSize);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setForeground(Color.WHITE);
            btn.setOpaque(true);
            btn.setBorderPainted(false);
        }

        // background color for buttons
        basicTestBtn.setBackground(new Color(54, 207, 72));
        advancedTestBtn.setBackground(new Color(54, 118, 207));
        removeBtn.setBackground(new Color(207, 54, 69));

        buttonPanel.add(basicTestBtn);
        buttonPanel.add(Box.createVerticalStrut(8));
        buttonPanel.add(advancedTestBtn);
        buttonPanel.add(Box.createVerticalStrut(8));
        buttonPanel.add(removeBtn);

        // table and scroll pane
        this.requestTable = new JTable(this.requestTableModel);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        JScrollPane scrollPane = new JScrollPane(this.requestTable);

        // render the labels to center
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < this.columns.length; i++) {
            this.requestTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // table configuration (single selection & show grid)
        this.requestTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.requestTable.setRowSelectionAllowed(true);
        this.requestTable.setColumnSelectionAllowed(false);
        this.requestTable.setCellSelectionEnabled(false);

        this.requestTable.setFillsViewportHeight(true);
        this.requestTable.setShowGrid(false);

        panel.add(buttonPanel, BorderLayout.WEST);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public void addRequest(HttpRequestResponse requestResponse) {
        SwingUtilities.invokeLater(() -> {
            HttpRequest request = requestResponse.request();

            this.controller.chosenRequestMap.put(
                request.path(), requestResponse
            );

            String method = request.method();
            String ipAddress = requestResponse.httpService().ipAddress();
            String host = requestResponse.httpService().host();
            String path = request.path();
            String date = requestResponse.timingData().toString();

            // add contents to the table
            this.requestTableModel.addRow(new Object[] {
                method, ipAddress, host, path, date
            });
        });
    }
}