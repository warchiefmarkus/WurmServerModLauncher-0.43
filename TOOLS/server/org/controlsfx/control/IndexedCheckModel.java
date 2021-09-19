package org.controlsfx.control;

import javafx.collections.ObservableList;

public interface IndexedCheckModel<T> extends CheckModel<T> {
  T getItem(int paramInt);
  
  int getItemIndex(T paramT);
  
  ObservableList<Integer> getCheckedIndices();
  
  void checkIndices(int... paramVarArgs);
  
  void clearCheck(int paramInt);
  
  boolean isChecked(int paramInt);
  
  void check(int paramInt);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\IndexedCheckModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */