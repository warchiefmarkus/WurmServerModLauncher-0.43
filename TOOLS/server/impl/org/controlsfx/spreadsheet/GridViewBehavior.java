/*     */ package impl.org.controlsfx.spreadsheet;
/*     */ 
/*     */ import com.sun.javafx.scene.control.behavior.TableViewBehavior;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.scene.control.SelectionMode;
/*     */ import javafx.scene.control.TableColumnBase;
/*     */ import javafx.scene.control.TableFocusModel;
/*     */ import javafx.scene.control.TablePositionBase;
/*     */ import javafx.scene.control.TableSelectionModel;
/*     */ import javafx.scene.control.TableView;
/*     */ import javafx.util.Pair;
/*     */ import org.controlsfx.control.spreadsheet.SpreadsheetCell;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GridViewBehavior
/*     */   extends TableViewBehavior<ObservableList<SpreadsheetCell>>
/*     */ {
/*     */   private GridViewSkin skin;
/*     */   
/*     */   public GridViewBehavior(TableView<ObservableList<SpreadsheetCell>> control) {
/*  67 */     super(control);
/*     */   }
/*     */   
/*     */   void setGridViewSkin(GridViewSkin skin) {
/*  71 */     this.skin = skin;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateCellVerticalSelection(int delta, Runnable defaultAction) {
/*  76 */     TableViewSpanSelectionModel sm = (TableViewSpanSelectionModel)getSelectionModel();
/*  77 */     if (sm == null || sm.getSelectionMode() == SelectionMode.SINGLE) {
/*     */       return;
/*     */     }
/*     */     
/*  81 */     TableFocusModel fm = getFocusModel();
/*  82 */     if (fm == null) {
/*     */       return;
/*     */     }
/*     */     
/*  86 */     TablePositionBase focusedCell = getFocusedCell();
/*     */     
/*  88 */     if (this.isShiftDown && getAnchor() != null) {
/*     */       
/*  90 */       SpreadsheetCell cell = (SpreadsheetCell)focusedCell.getTableColumn().getCellData(focusedCell.getRow());
/*  91 */       sm.direction = new Pair(Integer.valueOf(delta), Integer.valueOf(0));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  99 */       if (delta < 0) {
/* 100 */         newRow = this.skin.getFirstRow(cell, focusedCell.getRow()) + delta;
/*     */       } else {
/* 102 */         newRow = focusedCell.getRow() + this.skin.spreadsheetView.getRowSpan(cell, focusedCell.getRow()) - 1 + delta;
/*     */       } 
/*     */ 
/*     */       
/* 106 */       int newRow = Math.max(Math.min(getItemCount() - 1, newRow), 0);
/*     */       
/* 108 */       TablePositionBase<?> anchor = getAnchor();
/* 109 */       int minRow = Math.min(anchor.getRow(), newRow);
/* 110 */       int maxRow = Math.max(anchor.getRow(), newRow);
/* 111 */       int minColumn = Math.min(anchor.getColumn(), focusedCell.getColumn());
/* 112 */       int maxColumn = Math.max(anchor.getColumn(), focusedCell.getColumn());
/*     */       
/* 114 */       sm.clearSelection();
/* 115 */       if (minColumn != -1 && maxColumn != -1) {
/* 116 */         sm.selectRange(minRow, (TableColumnBase<ObservableList<SpreadsheetCell>, ?>)((TableView)getControl()).getVisibleLeafColumn(minColumn), maxRow, (TableColumnBase<ObservableList<SpreadsheetCell>, ?>)((TableView)
/* 117 */             getControl()).getVisibleLeafColumn(maxColumn));
/*     */       }
/* 119 */       fm.focus(newRow, focusedCell.getTableColumn());
/*     */     } else {
/* 121 */       int focusIndex = fm.getFocusedIndex();
/* 122 */       if (!sm.isSelected(focusIndex, focusedCell.getTableColumn())) {
/* 123 */         sm.select(focusIndex, focusedCell.getTableColumn());
/*     */       }
/* 125 */       defaultAction.run();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateCellHorizontalSelection(int delta, Runnable defaultAction) {
/* 131 */     TableViewSpanSelectionModel sm = (TableViewSpanSelectionModel)getSelectionModel();
/* 132 */     if (sm == null || sm.getSelectionMode() == SelectionMode.SINGLE) {
/*     */       return;
/*     */     }
/*     */     
/* 136 */     TableFocusModel fm = getFocusModel();
/* 137 */     if (fm == null) {
/*     */       return;
/*     */     }
/*     */     
/* 141 */     TablePositionBase focusedCell = getFocusedCell();
/* 142 */     if (focusedCell == null || focusedCell.getTableColumn() == null) {
/*     */       return;
/*     */     }
/*     */     
/* 146 */     TableColumnBase adjacentColumn = getColumn(focusedCell.getTableColumn(), delta);
/* 147 */     if (adjacentColumn == null) {
/*     */       return;
/*     */     }
/*     */     
/* 151 */     int focusedCellRow = focusedCell.getRow();
/*     */     
/* 153 */     if (this.isShiftDown && getAnchor() != null) {
/*     */       int newColumn;
/* 155 */       SpreadsheetCell cell = (SpreadsheetCell)focusedCell.getTableColumn().getCellData(focusedCell.getRow());
/*     */       
/* 157 */       sm.direction = new Pair(Integer.valueOf(0), Integer.valueOf(delta));
/*     */       
/* 159 */       if (delta < 0) {
/* 160 */         newColumn = this.skin.spreadsheetView.getViewColumn(cell.getColumn()) + delta;
/*     */       } else {
/* 162 */         newColumn = this.skin.spreadsheetView.getViewColumn(cell.getColumn()) + this.skin.spreadsheetView.getColumnSpan(cell) - 1 + delta;
/*     */       } 
/* 164 */       TablePositionBase<?> anchor = getAnchor();
/* 165 */       int minRow = Math.min(anchor.getRow(), focusedCellRow);
/* 166 */       int maxRow = Math.max(anchor.getRow(), focusedCellRow);
/* 167 */       int minColumn = Math.min(anchor.getColumn(), newColumn);
/* 168 */       int maxColumn = Math.max(anchor.getColumn(), newColumn);
/*     */       
/* 170 */       sm.clearSelection();
/* 171 */       if (minColumn != -1 && maxColumn != -1) {
/* 172 */         sm.selectRange(minRow, (TableColumnBase<ObservableList<SpreadsheetCell>, ?>)((TableView)getControl()).getVisibleLeafColumn(minColumn), maxRow, (TableColumnBase<ObservableList<SpreadsheetCell>, ?>)((TableView)
/* 173 */             getControl()).getVisibleLeafColumn(maxColumn));
/*     */       }
/* 175 */       fm.focus(focusedCell.getRow(), getColumn(newColumn));
/*     */     } else {
/* 177 */       defaultAction.run();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void focusPreviousRow() {
/* 184 */     focusVertical(true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void focusNextRow() {
/* 189 */     focusVertical(false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void focusLeftCell() {
/* 194 */     focusHorizontal(true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void focusRightCell() {
/* 199 */     focusHorizontal(false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void discontinuousSelectPreviousRow() {
/* 204 */     discontinuousSelectVertical(true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void discontinuousSelectNextRow() {
/* 209 */     discontinuousSelectVertical(false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void discontinuousSelectPreviousColumn() {
/* 214 */     discontinuousSelectHorizontal(true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void discontinuousSelectNextColumn() {
/* 219 */     discontinuousSelectHorizontal(false);
/*     */   }
/*     */   
/*     */   private void focusVertical(boolean previous) {
/* 223 */     TableSelectionModel sm = getSelectionModel();
/* 224 */     if (sm == null || sm.getSelectionMode() == SelectionMode.SINGLE) {
/*     */       return;
/*     */     }
/*     */     
/* 228 */     TableFocusModel fm = getFocusModel();
/* 229 */     if (fm == null) {
/*     */       return;
/*     */     }
/*     */     
/* 233 */     TablePositionBase focusedCell = getFocusedCell();
/* 234 */     if (focusedCell == null || focusedCell.getTableColumn() == null) {
/*     */       return;
/*     */     }
/*     */     
/* 238 */     SpreadsheetCell cell = (SpreadsheetCell)focusedCell.getTableColumn().getCellData(focusedCell.getRow());
/* 239 */     sm.clearAndSelect(previous ? findPreviousRow(focusedCell, cell) : findNextRow(focusedCell, cell), focusedCell.getTableColumn());
/* 240 */     this.skin.focusScroll();
/*     */   }
/*     */   
/*     */   private void focusHorizontal(boolean previous) {
/* 244 */     TableSelectionModel sm = getSelectionModel();
/* 245 */     if (sm == null) {
/*     */       return;
/*     */     }
/*     */     
/* 249 */     TableFocusModel fm = getFocusModel();
/* 250 */     if (fm == null) {
/*     */       return;
/*     */     }
/* 253 */     TablePositionBase focusedCell = getFocusedCell();
/* 254 */     if (focusedCell == null || focusedCell.getTableColumn() == null) {
/*     */       return;
/*     */     }
/*     */     
/* 258 */     SpreadsheetCell cell = (SpreadsheetCell)focusedCell.getTableColumn().getCellData(focusedCell.getRow());
/*     */     
/* 260 */     sm.clearAndSelect(focusedCell.getRow(), (TableColumnBase)((TableView)getControl()).getVisibleLeafColumn(previous ? findPreviousColumn(focusedCell, cell) : findNextColumn(focusedCell, cell)));
/* 261 */     this.skin.focusScroll();
/*     */   }
/*     */   
/*     */   private int findPreviousRow(TablePositionBase focusedCell, SpreadsheetCell cell) {
/* 265 */     ObservableList<ObservableList<SpreadsheetCell>> items = ((TableView)getControl()).getItems();
/*     */ 
/*     */     
/* 268 */     if (isEmpty(cell)) {
/* 269 */       for (int row = focusedCell.getRow() - 1; row >= 0; row--) {
/* 270 */         SpreadsheetCell temp = (SpreadsheetCell)((ObservableList)items.get(row)).get(focusedCell.getColumn());
/* 271 */         if (!isEmpty(temp)) {
/* 272 */           return row;
/*     */         }
/*     */       } 
/* 275 */     } else if (focusedCell.getRow() - 1 >= 0 && !isEmpty((SpreadsheetCell)((ObservableList)items.get(focusedCell.getRow() - 1)).get(focusedCell.getColumn()))) {
/* 276 */       for (int row = focusedCell.getRow() - 2; row >= 0; row--) {
/* 277 */         SpreadsheetCell temp = (SpreadsheetCell)((ObservableList)items.get(row)).get(focusedCell.getColumn());
/* 278 */         if (isEmpty(temp)) {
/* 279 */           return row + 1;
/*     */         }
/*     */       } 
/*     */     } else {
/*     */       
/* 284 */       for (int row = focusedCell.getRow() - 2; row >= 0; row--) {
/* 285 */         SpreadsheetCell temp = (SpreadsheetCell)((ObservableList)items.get(row)).get(focusedCell.getColumn());
/* 286 */         if (!isEmpty(temp)) {
/* 287 */           return row;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 293 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void selectCell(int rowDiff, int columnDiff) {
/* 299 */     TableViewSpanSelectionModel sm = (TableViewSpanSelectionModel)getSelectionModel();
/* 300 */     if (sm == null) {
/*     */       return;
/*     */     }
/* 303 */     sm.direction = new Pair(Integer.valueOf(rowDiff), Integer.valueOf(columnDiff));
/*     */     
/* 305 */     TableFocusModel fm = getFocusModel();
/* 306 */     if (fm == null) {
/*     */       return;
/*     */     }
/*     */     
/* 310 */     TablePositionBase focusedCell = getFocusedCell();
/* 311 */     int currentRow = focusedCell.getRow();
/* 312 */     int currentColumn = getVisibleLeafIndex(focusedCell.getTableColumn());
/*     */     
/* 314 */     if (rowDiff < 0 && currentRow <= 0)
/* 315 */       return;  if (rowDiff > 0 && currentRow >= getItemCount() - 1)
/* 316 */       return;  if (columnDiff < 0 && currentColumn <= 0)
/* 317 */       return;  if (columnDiff > 0 && currentColumn >= getVisibleLeafColumns().size() - 1)
/* 318 */       return;  if (columnDiff > 0 && currentColumn == -1)
/*     */       return; 
/* 320 */     TableColumnBase tc = focusedCell.getTableColumn();
/* 321 */     tc = getColumn(tc, columnDiff);
/*     */     
/* 323 */     int row = focusedCell.getRow() + rowDiff;
/*     */     
/* 325 */     sm.clearAndSelect(row, tc);
/* 326 */     setAnchor(row, tc);
/*     */   }
/*     */   
/*     */   private int findNextRow(TablePositionBase focusedCell, SpreadsheetCell cell) {
/* 330 */     ObservableList<ObservableList<SpreadsheetCell>> items = ((TableView)getControl()).getItems();
/* 331 */     int itemCount = getItemCount();
/*     */ 
/*     */     
/* 334 */     if (isEmpty(cell)) {
/* 335 */       for (int row = focusedCell.getRow() + 1; row < itemCount; row++) {
/* 336 */         SpreadsheetCell temp = (SpreadsheetCell)((ObservableList)items.get(row)).get(focusedCell.getColumn());
/* 337 */         if (!isEmpty(temp)) {
/* 338 */           return row;
/*     */         }
/*     */       } 
/* 341 */     } else if (focusedCell.getRow() + 1 < itemCount && !isEmpty((SpreadsheetCell)((ObservableList)items.get(focusedCell.getRow() + 1)).get(focusedCell.getColumn()))) {
/* 342 */       for (int row = focusedCell.getRow() + 2; row < getItemCount(); row++) {
/* 343 */         SpreadsheetCell temp = (SpreadsheetCell)((ObservableList)items.get(row)).get(focusedCell.getColumn());
/* 344 */         if (isEmpty(temp)) {
/* 345 */           return row - 1;
/*     */         }
/*     */       } 
/*     */     } else {
/* 349 */       for (int row = focusedCell.getRow() + 2; row < itemCount; row++) {
/* 350 */         SpreadsheetCell temp = (SpreadsheetCell)((ObservableList)items.get(row)).get(focusedCell.getColumn());
/* 351 */         if (!isEmpty(temp)) {
/* 352 */           return row;
/*     */         }
/*     */       } 
/*     */     } 
/* 356 */     return itemCount - 1;
/*     */   }
/*     */   
/*     */   private void discontinuousSelectVertical(boolean previous) {
/* 360 */     TableSelectionModel sm = getSelectionModel();
/* 361 */     if (sm == null) {
/*     */       return;
/*     */     }
/*     */     
/* 365 */     TableFocusModel fm = getFocusModel();
/* 366 */     if (fm == null) {
/*     */       return;
/*     */     }
/* 369 */     TablePositionBase focusedCell = getFocusedCell();
/* 370 */     if (focusedCell == null || focusedCell.getTableColumn() == null) {
/*     */       return;
/*     */     }
/*     */     
/* 374 */     SpreadsheetCell cell = (SpreadsheetCell)focusedCell.getTableColumn().getCellData(focusedCell.getRow());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 382 */     int newRow = previous ? findPreviousRow(focusedCell, cell) : findNextRow(focusedCell, cell);
/*     */ 
/*     */     
/* 385 */     newRow = Math.max(Math.min(getItemCount() - 1, newRow), 0);
/*     */     
/* 387 */     TablePositionBase<?> anchor = getAnchor();
/* 388 */     int minRow = Math.min(anchor.getRow(), newRow);
/* 389 */     int maxRow = Math.max(anchor.getRow(), newRow);
/* 390 */     int minColumn = Math.min(anchor.getColumn(), focusedCell.getColumn());
/* 391 */     int maxColumn = Math.max(anchor.getColumn(), focusedCell.getColumn());
/*     */     
/* 393 */     sm.clearSelection();
/* 394 */     if (minColumn != -1 && maxColumn != -1) {
/* 395 */       sm.selectRange(minRow, (TableColumnBase)((TableView)getControl()).getVisibleLeafColumn(minColumn), maxRow, (TableColumnBase)((TableView)
/* 396 */           getControl()).getVisibleLeafColumn(maxColumn));
/*     */     }
/* 398 */     fm.focus(newRow, focusedCell.getTableColumn());
/* 399 */     this.skin.focusScroll();
/*     */   }
/*     */   
/*     */   private void discontinuousSelectHorizontal(boolean previous) {
/* 403 */     TableSelectionModel sm = getSelectionModel();
/* 404 */     if (sm == null) {
/*     */       return;
/*     */     }
/*     */     
/* 408 */     TableFocusModel fm = getFocusModel();
/* 409 */     if (fm == null) {
/*     */       return;
/*     */     }
/* 412 */     TablePositionBase focusedCell = getFocusedCell();
/* 413 */     if (focusedCell == null || focusedCell.getTableColumn() == null) {
/*     */       return;
/*     */     }
/*     */     
/* 417 */     int columnPos = getVisibleLeafIndex(focusedCell.getTableColumn());
/* 418 */     int focusedCellRow = focusedCell.getRow();
/* 419 */     SpreadsheetCell cell = (SpreadsheetCell)focusedCell.getTableColumn().getCellData(focusedCell.getRow());
/*     */     
/* 421 */     int newColumn = previous ? findPreviousColumn(focusedCell, cell) : findNextColumn(focusedCell, cell);
/*     */     
/* 423 */     TablePositionBase<?> anchor = getAnchor();
/* 424 */     int minRow = Math.min(anchor.getRow(), focusedCellRow);
/* 425 */     int maxRow = Math.max(anchor.getRow(), focusedCellRow);
/* 426 */     int minColumn = Math.min(anchor.getColumn(), newColumn);
/* 427 */     int maxColumn = Math.max(anchor.getColumn(), newColumn);
/*     */     
/* 429 */     sm.clearSelection();
/* 430 */     if (minColumn != -1 && maxColumn != -1) {
/* 431 */       sm.selectRange(minRow, (TableColumnBase)((TableView)getControl()).getVisibleLeafColumn(minColumn), maxRow, (TableColumnBase)((TableView)
/* 432 */           getControl()).getVisibleLeafColumn(maxColumn));
/*     */     }
/* 434 */     fm.focus(focusedCell.getRow(), getColumn(newColumn));
/* 435 */     this.skin.focusScroll();
/*     */   }
/*     */   
/*     */   private int findNextColumn(TablePositionBase focusedCell, SpreadsheetCell cell) {
/* 439 */     ObservableList<ObservableList<SpreadsheetCell>> items = ((TableView)getControl()).getItems();
/* 440 */     int itemCount = ((TableView)getControl()).getColumns().size();
/*     */ 
/*     */     
/* 443 */     if (isEmpty(cell)) {
/* 444 */       for (int column = focusedCell.getColumn() + 1; column < itemCount; column++) {
/* 445 */         SpreadsheetCell temp = (SpreadsheetCell)((ObservableList)items.get(focusedCell.getRow())).get(column);
/* 446 */         if (!isEmpty(temp)) {
/* 447 */           return column;
/*     */         }
/*     */       } 
/* 450 */     } else if (focusedCell.getColumn() + 1 < itemCount && !isEmpty((SpreadsheetCell)((ObservableList)items.get(focusedCell.getRow())).get(focusedCell.getColumn() + 1))) {
/* 451 */       for (int column = focusedCell.getColumn() + 2; column < itemCount; column++) {
/* 452 */         SpreadsheetCell temp = (SpreadsheetCell)((ObservableList)items.get(focusedCell.getRow())).get(column);
/* 453 */         if (isEmpty(temp)) {
/* 454 */           return column - 1;
/*     */         }
/*     */       } 
/*     */     } else {
/* 458 */       for (int column = focusedCell.getColumn() + 2; column < itemCount; column++) {
/* 459 */         SpreadsheetCell temp = (SpreadsheetCell)((ObservableList)items.get(focusedCell.getRow())).get(column);
/* 460 */         if (!isEmpty(temp)) {
/* 461 */           return column;
/*     */         }
/*     */       } 
/*     */     } 
/* 465 */     return itemCount - 1;
/*     */   }
/*     */   
/*     */   private int findPreviousColumn(TablePositionBase focusedCell, SpreadsheetCell cell) {
/* 469 */     ObservableList<ObservableList<SpreadsheetCell>> items = ((TableView)getControl()).getItems();
/*     */ 
/*     */     
/* 472 */     if (isEmpty(cell)) {
/* 473 */       for (int column = focusedCell.getColumn() - 1; column >= 0; column--) {
/* 474 */         SpreadsheetCell temp = (SpreadsheetCell)((ObservableList)items.get(focusedCell.getRow())).get(column);
/* 475 */         if (!isEmpty(temp)) {
/* 476 */           return column;
/*     */         }
/*     */       } 
/* 479 */     } else if (focusedCell.getColumn() - 1 >= 0 && !isEmpty((SpreadsheetCell)((ObservableList)items.get(focusedCell.getRow())).get(focusedCell.getColumn() - 1))) {
/* 480 */       for (int column = focusedCell.getColumn() - 2; column >= 0; column--) {
/* 481 */         SpreadsheetCell temp = (SpreadsheetCell)((ObservableList)items.get(focusedCell.getRow())).get(column);
/* 482 */         if (isEmpty(temp)) {
/* 483 */           return column + 1;
/*     */         }
/*     */       } 
/*     */     } else {
/* 487 */       for (int column = focusedCell.getColumn() - 2; column >= 0; column--) {
/* 488 */         SpreadsheetCell temp = (SpreadsheetCell)((ObservableList)items.get(focusedCell.getRow())).get(column);
/* 489 */         if (!isEmpty(temp)) {
/* 490 */           return column;
/*     */         }
/*     */       } 
/*     */     } 
/* 494 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isEmpty(SpreadsheetCell cell) {
/* 505 */     return (cell.getGraphic() == null && (cell.getItem() == null || (cell
/* 506 */       .getItem() instanceof Double && ((Double)cell.getItem()).isNaN())));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\spreadsheet\GridViewBehavior.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */