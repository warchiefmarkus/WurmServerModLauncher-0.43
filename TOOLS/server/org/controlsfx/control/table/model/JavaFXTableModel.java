package org.controlsfx.control.table.model;

import javafx.scene.control.TableView;

interface JavaFXTableModel<T> {
  T getValueAt(int paramInt1, int paramInt2);
  
  void setValueAt(T paramT, int paramInt1, int paramInt2);
  
  int getRowCount();
  
  int getColumnCount();
  
  String getColumnName(int paramInt);
  
  void sort(TableView<TableModelRow<T>> paramTableView);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\table\model\JavaFXTableModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */