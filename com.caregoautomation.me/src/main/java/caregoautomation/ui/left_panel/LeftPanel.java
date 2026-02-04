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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import burp.api.montoya.MontoyaApi;
import caregoautomation.CaregoAutomation;

public class LeftPanel extends JPanel {
    private final MontoyaApi api;
    private final CaregoAutomation controller;

    public LeftPanel(
        MontoyaApi api,
        CaregoAutomation controller
    ) {
        this.api = api;
        this.controller = controller;
        this.build();
    }

    private void build() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.add(this.tableContents(), BorderLayout.NORTH);
    }

    private JComponent tableContents() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        JButton basicTestBtn = new JButton("Basic Test");
        JButton advancedTestBtn = new JButton("Advanced Test");
        JButton removeBtn = new JButton("Remove");

        Dimension btnSize = new Dimension(150, 25);
        for (JButton btn : new JButton[]{ basicTestBtn, advancedTestBtn, removeBtn }) {
            btn.setPreferredSize(btnSize);
            btn.setMaximumSize(btnSize);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setForeground(Color.WHITE);
            btn.setOpaque(true);
            btn.setBorderPainted(false);
        }

        basicTestBtn.setBackground(new Color(54, 207, 72));
        advancedTestBtn.setBackground(new Color(54, 118, 207));
        removeBtn.setBackground(new Color(207, 54, 69));

        buttonPanel.add(basicTestBtn);
        buttonPanel.add(Box.createVerticalStrut(8));
        buttonPanel.add(advancedTestBtn);
        buttonPanel.add(Box.createVerticalStrut(8));
        buttonPanel.add(removeBtn);

        String[] columns = { "Host", "Path", "Date" };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);

        // 👇 Selection config (this is what you want)
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        table.setCellSelectionEnabled(false);

        table.setFillsViewportHeight(true);
        table.setShowGrid(false);

        JScrollPane scrollPane = new JScrollPane(table);

        /* ---------- assemble ---------- */
        panel.add(buttonPanel, BorderLayout.WEST);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;

    }
}