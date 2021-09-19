package org.controlsfx.control.spreadsheet;

import java.util.Collection;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;

public interface Grid {
  public static final double AUTOFIT = -1.0D;
  
  int getRowCount();
  
  int getColumnCount();
  
  ObservableList<ObservableList<SpreadsheetCell>> getRows();
  
  void setCellValue(int paramInt1, int paramInt2, Object paramObject);
  
  double getRowHeight(int paramInt);
  
  boolean isRowResizable(int paramInt);
  
  ObservableList<String> getRowHeaders();
  
  ObservableList<String> getColumnHeaders();
  
  void spanRow(int paramInt1, int paramInt2, int paramInt3);
  
  void spanColumn(int paramInt1, int paramInt2, int paramInt3);
  
  void setRows(Collection<ObservableList<SpreadsheetCell>> paramCollection);
  
  boolean isDisplaySelection();
  
  void setDisplaySelection(boolean paramBoolean);
  
  BooleanProperty displaySelectionProperty();
  
  void setCellDisplaySelection(int paramInt1, int paramInt2, boolean paramBoolean);
  
  boolean isCellDisplaySelection(int paramInt1, int paramInt2);
  
  <E extends GridChange> void addEventHandler(EventType<E> paramEventType, EventHandler<E> paramEventHandler);
  
  <E extends GridChange> void removeEventHandler(EventType<E> paramEventType, EventHandler<E> paramEventHandler);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\spreadsheet\Grid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */