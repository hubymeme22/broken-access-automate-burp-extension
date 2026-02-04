package caregoautomation.ui.left_panel.design;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


public class TableHighlightRenderer extends DefaultTableCellRenderer {
    private int hoveredRow = -1;

    public void setHoveredRow(int row) {
        this.hoveredRow = row;
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (isSelected) {
            setBackground(new Color(30, 60, 120)); // Dark blue for selection
            setForeground(Color.WHITE);
        } else if (row == this.hoveredRow) {
            setBackground(new Color(173, 216, 230)); // Light blue for hover
            setForeground(Color.BLACK);
        } else {
            setBackground(Color.WHITE); // default
            setForeground(Color.BLACK);
        }

        return this;
    }
}