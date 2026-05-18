package presentation;

import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Utility class that uses Java Reflection to dynamically construct
 * JTable models from a list of generic objects.
 * * @author Yasmina
 * @version 1.0
 */
public class TableReflection {

    /**
     * Extracts field names to create column headers and retrieves field
     * values to populate rows dynamically for a swing table.
     *
     * @param <T>     The type of objects stored in the list.
     * @param objects The list containing data models to be displayed.
     * @return A ready-to-use DefaultTableModel matching the object structure.
     */
    public static <T> DefaultTableModel buildTableModel(List<T> objects) {
        if (objects == null || objects.isEmpty()) {
            return new DefaultTableModel();
        }
        //get the blueprint of class
        Class<?> type = objects.get(0).getClass();
        Field[] fields = type.getDeclaredFields();

        //generate column headers
        String[] columnNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            columnNames[i] = fields[i].getName();
        }

        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        //put in rows actual data
        for (T obj : objects) {
            Object[] rowData = new Object[fields.length];
            for (int i = 0; i < fields.length; i++) {
                try {
                    fields[i].setAccessible(true);
                    rowData[i] = fields[i].get(obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            tableModel.addRow(rowData);
        }

        return tableModel;
    }
}
