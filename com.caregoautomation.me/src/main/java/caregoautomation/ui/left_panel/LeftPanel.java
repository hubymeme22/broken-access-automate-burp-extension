package caregoautomation.ui.left_panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseMotionAdapter;

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
import javax.swing.table.DefaultTableModel;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.requests.HttpRequest;
import caregoautomation.CaregoAutomation;
import caregoautomation.ui.left_panel.design.TableHighlightRenderer;

public class LeftPanel extends JPanel {

    private final MontoyaApi api;
    private final CaregoAutomation controller;
    private final String[] columns = { "Method", "IP Address", "Host", "Path" };

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
        this.add(this.tableContents(), BorderLayout.CENTER);
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
        TableHighlightRenderer rowRenderer = new TableHighlightRenderer();
        JScrollPane scrollPane = new JScrollPane(this.requestTable);

        // render the labels to center
        rowRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < this.columns.length; i++) {
            this.requestTable.getColumnModel().getColumn(i).setCellRenderer(rowRenderer);
        }

        // table configuration (single selection & show grid)
        this.requestTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.requestTable.setRowSelectionAllowed(true);
        this.requestTable.setColumnSelectionAllowed(false);
        this.requestTable.setCellSelectionEnabled(false);

        this.requestTable.setFillsViewportHeight(true);
        this.requestTable.setShowGrid(false);

        this.requestTable.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                int row = requestTable.rowAtPoint(e.getPoint());
                rowRenderer.setHoveredRow(row);
                requestTable.repaint();
            }
        });

        // scroll pane configuration
        scrollPane.setPreferredSize(new Dimension(600, 200));

        panel.add(buttonPanel, BorderLayout.WEST);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Adds a request to the UI and maps the path to the
     * request response
     */
    public void addRequest(HttpRequestResponse requestResponse) {
        SwingUtilities.invokeLater(() -> {
            HttpRequest request = requestResponse.request();

            // check if the request is already in the map
            if (this.controller.chosenRequestMap.containsKey(request.path())) {
                return;
            }

            this.controller.chosenRequestMap.put(
                request.path(), requestResponse
            );

            String method = request.method();
            String ipAddress = requestResponse.httpService().ipAddress();
            String host = requestResponse.httpService().host();
            String path = request.path();

            // add contents to the table
            this.requestTableModel.addRow(new Object[] {
                method, ipAddress, host, path
            });

        });
    }
}