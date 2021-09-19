package impl.org.controlsfx.spreadsheet;

import org.controlsfx.control.spreadsheet.SpreadsheetView;

public abstract class SpreadsheetHandle {
  protected abstract SpreadsheetView getView();
  
  protected abstract SpreadsheetGridView getGridView();
  
  protected abstract GridViewSkin getCellsViewSkin();
  
  protected abstract boolean isColumnWidthSet(int paramInt);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\spreadsheet\SpreadsheetHandle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */