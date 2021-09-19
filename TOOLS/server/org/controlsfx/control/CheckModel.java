package org.controlsfx.control;

import javafx.collections.ObservableList;

public interface CheckModel<T> {
  int getItemCount();
  
  ObservableList<T> getCheckedItems();
  
  void checkAll();
  
  void clearCheck(T paramT);
  
  void clearChecks();
  
  boolean isEmpty();
  
  boolean isChecked(T paramT);
  
  void check(T paramT);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\CheckModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */