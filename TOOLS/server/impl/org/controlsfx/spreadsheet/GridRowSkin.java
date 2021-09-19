/*     */ package impl.org.controlsfx.spreadsheet;
/*     */ 
/*     */ import com.sun.javafx.scene.control.behavior.BehaviorBase;
/*     */ import com.sun.javafx.scene.control.behavior.CellBehaviorBase;
/*     */ import com.sun.javafx.scene.control.behavior.TableRowBehavior;
/*     */ import com.sun.javafx.scene.control.skin.CellSkinBase;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Cell;
/*     */ import javafx.scene.control.TableColumn;
/*     */ import javafx.scene.control.TableColumnBase;
/*     */ import javafx.scene.control.TableRow;
/*     */ import javafx.scene.control.TableView;
/*     */ import javafx.scene.layout.BorderStroke;
/*     */ import org.controlsfx.control.spreadsheet.Grid;
/*     */ import org.controlsfx.control.spreadsheet.SpreadsheetCell;
/*     */ import org.controlsfx.control.spreadsheet.SpreadsheetColumn;
/*     */ import org.controlsfx.control.spreadsheet.SpreadsheetView;
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
/*     */ public class GridRowSkin
/*     */   extends CellSkinBase<TableRow<ObservableList<SpreadsheetCell>>, CellBehaviorBase<TableRow<ObservableList<SpreadsheetCell>>>>
/*     */ {
/*     */   private final SpreadsheetHandle handle;
/*     */   private final SpreadsheetView spreadsheetView;
/*     */   private Reference<HashMap<TableColumnBase, CellView>> cellsMap;
/*  57 */   private final List<CellView> cells = new ArrayList<>();
/*     */   
/*     */   public GridRowSkin(SpreadsheetHandle handle, TableRow<ObservableList<SpreadsheetCell>> gridRow) {
/*  60 */     super((Cell)gridRow, (BehaviorBase)new TableRowBehavior(gridRow));
/*  61 */     this.handle = handle;
/*  62 */     this.spreadsheetView = handle.getView();
/*     */     
/*  64 */     ((TableRow)getSkinnable()).setPickOnBounds(false);
/*     */     
/*  66 */     registerChangeListener((ObservableValue)gridRow.itemProperty(), "ITEM");
/*  67 */     registerChangeListener((ObservableValue)gridRow.indexProperty(), "INDEX");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void handleControlPropertyChanged(String p) {
/*  72 */     super.handleControlPropertyChanged(p);
/*     */     
/*  74 */     if ("INDEX".equals(p)) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  79 */       if (((TableRow)getSkinnable()).isEmpty()) {
/*  80 */         requestCellUpdate();
/*     */       }
/*  82 */     } else if ("ITEM".equals(p)) {
/*  83 */       requestCellUpdate();
/*  84 */     } else if ("FIXED_CELL_SIZE".equals(p)) {
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void requestCellUpdate() {
/*  91 */     ((TableRow)getSkinnable()).requestLayout();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  99 */     int newIndex = ((TableRow)getSkinnable()).getIndex();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 104 */     getChildren().clear();
/* 105 */     for (int i = 0, max = this.cells.size(); i < max; i++) {
/* 106 */       ((CellView)this.cells.get(i)).updateIndex(newIndex);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void layoutChildren(double x, double y, double w, double h) {
/* 113 */     ObservableList<? extends TableColumnBase<?, ?>> visibleLeafColumns = this.handle.getGridView().getVisibleLeafColumns();
/* 114 */     if (visibleLeafColumns.isEmpty()) {
/* 115 */       super.layoutChildren(x, y, w, h);
/*     */       
/*     */       return;
/*     */     } 
/* 119 */     GridRow control = (GridRow)getSkinnable();
/* 120 */     SpreadsheetGridView gridView = this.handle.getGridView();
/* 121 */     Grid grid = this.spreadsheetView.getGrid();
/* 122 */     int index = control.getIndex();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 130 */     if (index < 0 || index >= gridView.getItems().size()) {
/* 131 */       getChildren().clear();
/* 132 */       putCellsInCache();
/*     */       
/*     */       return;
/*     */     } 
/* 136 */     List<SpreadsheetCell> row = (List<SpreadsheetCell>)((TableRow)getSkinnable()).getItem();
/* 137 */     ObservableList<SpreadsheetColumn> observableList = this.spreadsheetView.getColumns();
/* 138 */     ObservableList<TableColumn<ObservableList<SpreadsheetCell>, ?>> tableViewColumns = gridView.getColumns();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 145 */     if (observableList.size() != tableViewColumns.size()) {
/*     */       return;
/*     */     }
/*     */     
/* 149 */     ((TableRow)getSkinnable()).setVisible(true);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 154 */     double verticalPadding = snappedTopInset() + snappedBottomInset();
/*     */     
/* 156 */     double horizontalPadding = snappedLeftInset() + snappedRightInset();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 161 */     double controlHeight = getTableRowHeight(index);
/* 162 */     double customHeight = (controlHeight == -1.0D) ? GridViewSkin.DEFAULT_CELL_HEIGHT : controlHeight;
/*     */     
/* 164 */     GridViewSkin skin = this.handle.getCellsViewSkin();
/* 165 */     skin.hBarValue.set(index, true);
/*     */ 
/*     */     
/* 168 */     double headerWidth = gridView.getWidth();
/* 169 */     double hbarValue = skin.getHBar().getValue();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 174 */     ((GridRow)getSkinnable()).verticalShift.setValue(Double.valueOf(getFixedRowShift(index)));
/*     */     
/* 176 */     double fixedColumnWidth = 0.0D;
/* 177 */     List<CellView> fixedCells = new ArrayList<>();
/*     */ 
/*     */     
/* 180 */     putCellsInCache();
/*     */     
/* 182 */     boolean firstVisibleCell = false;
/* 183 */     CellView lastCell = null;
/*     */     
/* 185 */     boolean rowHeightChange = false;
/* 186 */     for (int indexColumn = 0; indexColumn < observableList.size(); indexColumn++) {
/*     */       
/* 188 */       if (((TableColumn)((TableView)skin.getSkinnable()).getColumns().get(indexColumn)).isVisible()) {
/*     */ 
/*     */         
/* 191 */         double width = snapSize(((SpreadsheetColumn)observableList.get(indexColumn)).getWidth()) - snapSize(horizontalPadding);
/*     */         
/* 193 */         if (row.size() <= indexColumn) {
/*     */           break;
/*     */         }
/* 196 */         SpreadsheetCell spreadsheetCell = row.get(indexColumn);
/* 197 */         int columnSpan = this.spreadsheetView.getColumnSpan(spreadsheetCell);
/* 198 */         boolean isVisible = !isInvisible(x, width, hbarValue, headerWidth, columnSpan);
/*     */         
/* 200 */         if (((SpreadsheetColumn)observableList.get(indexColumn)).isFixed()) {
/* 201 */           isVisible = true;
/*     */         }
/*     */         
/* 204 */         if (!isVisible)
/* 205 */         { if (firstVisibleCell) {
/*     */             break;
/*     */           }
/* 208 */           x += width; }
/*     */         else
/*     */         
/* 211 */         { CellView tableCell = getCell((TableColumnBase)gridView.getColumns().get(indexColumn));
/*     */           
/* 213 */           this.cells.add(0, tableCell);
/*     */ 
/*     */           
/* 216 */           tableCell.setManaged(true);
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 221 */           double tableCellX = 0.0D;
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
/* 232 */           boolean increaseFixedWidth = false;
/* 233 */           int viewColumn = this.spreadsheetView.getViewColumn(spreadsheetCell.getColumn());
/*     */ 
/*     */           
/* 236 */           if (((SpreadsheetColumn)observableList.get(indexColumn)).isFixed())
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 243 */             if (hbarValue + fixedColumnWidth > x && spreadsheetCell.getColumn() == indexColumn) {
/* 244 */               increaseFixedWidth = true;
/* 245 */               tableCellX = Math.abs(hbarValue - x + fixedColumnWidth);
/*     */               
/* 247 */               fixedColumnWidth += width;
/*     */               
/* 249 */               fixedCells.add(tableCell);
/*     */             } 
/*     */           }
/*     */           
/* 253 */           if (isVisible)
/* 254 */           { double height; boolean needToBeShifted; int rowSpan; double spaceBetweenTopAndMe; SpreadsheetView.SpanType spanType = this.spreadsheetView.getSpanType(index, indexColumn);
/*     */             
/* 256 */             switch (spanType)
/*     */             { case ROW_SPAN_INVISIBLE:
/*     */               case BOTH_INVISIBLE:
/* 259 */                 fixedCells.remove(tableCell);
/* 260 */                 getChildren().remove(tableCell);
/*     */                 
/* 262 */                 x += width;
/*     */                 break;
/*     */               case COLUMN_SPAN_INVISIBLE:
/* 265 */                 fixedCells.remove(tableCell);
/* 266 */                 getChildren().remove(tableCell);
/*     */                 break;
/*     */               
/*     */               case ROW_VISIBLE:
/*     */               case NORMAL_CELL:
/* 271 */                 if (tableCell.getIndex() != index) {
/* 272 */                   tableCell.updateIndex(index);
/*     */                 } else {
/* 274 */                   tableCell.updateItem(spreadsheetCell, false);
/*     */                 } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 284 */                 if (!tableCell.isEditing() && tableCell.getParent() != getSkinnable()) {
/* 285 */                   getChildren().add(0, tableCell);
/*     */                 }
/*     */               
/*     */               default:
/* 289 */                 if (columnSpan > 1) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/* 295 */                   int max = ((TableView)skin.getSkinnable()).getVisibleLeafColumns().size() - viewColumn;
/* 296 */                   for (int i = 1, colSpan = columnSpan; i < colSpan && i < max; i++) {
/* 297 */                     double tempWidth = snapSize(((TableView)skin.getSkinnable()).getVisibleLeafColumn(viewColumn + i).getWidth());
/* 298 */                     width += tempWidth;
/* 299 */                     if (increaseFixedWidth) {
/* 300 */                       fixedColumnWidth += tempWidth;
/*     */                     }
/*     */                   } 
/*     */                 } 
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
/* 314 */                 if (controlHeight == -1.0D && !tableCell.isEditing()) {
/*     */                   
/* 316 */                   double tempHeight = tableCell.prefHeight(width) + tableCell.snappedTopInset() + tableCell.snappedBottomInset();
/* 317 */                   if (tempHeight > customHeight) {
/* 318 */                     rowHeightChange = true;
/* 319 */                     skin.rowHeightMap.put(Integer.valueOf(spreadsheetCell.getRow()), Double.valueOf(tempHeight));
/* 320 */                     for (CellView cell : this.cells)
/*     */                     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                       
/* 328 */                       cell.resize(cell.getWidth(), cell.getHeight() + tempHeight - customHeight);
/*     */                     }
/* 330 */                     customHeight = tempHeight;
/* 331 */                     skin.getFlow().layoutChildren();
/*     */                   } 
/*     */                 } 
/*     */                 
/* 335 */                 height = customHeight;
/* 336 */                 height = snapSize(height) - snapSize(verticalPadding);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 342 */                 rowSpan = this.spreadsheetView.getRowSpan(spreadsheetCell, index);
/* 343 */                 if (rowSpan > 1) {
/* 344 */                   height = 0.0D;
/* 345 */                   int maxRow = index + rowSpan;
/* 346 */                   for (int i = index; i < maxRow; i++) {
/* 347 */                     height += snapSize(skin.getRowHeight(i));
/*     */                   }
/*     */                 } 
/*     */ 
/*     */                 
/* 352 */                 needToBeShifted = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 362 */                 if (lastCell != null && 
/*     */                   
/* 364 */                   !hasRightBorder(lastCell) && 
/* 365 */                   !hasLeftBorder(tableCell)) {
/* 366 */                   tableCell.resize(width + 1.0D, height);
/* 367 */                   needToBeShifted = true;
/*     */                 } else {
/* 369 */                   tableCell.resize(width, height);
/*     */                 } 
/* 371 */                 lastCell = tableCell;
/*     */                 
/* 373 */                 spaceBetweenTopAndMe = 0.0D;
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 378 */                 tableCell.relocate(x + tableCellX + (needToBeShifted ? -1 : false), snappedTopInset() - spaceBetweenTopAndMe + ((GridRow)
/* 379 */                     getSkinnable()).verticalShift.get());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 386 */                 x += width; }  } else { getChildren().remove(tableCell); x += width; }  } 
/*     */       } 
/* 388 */     }  skin.fixedColumnWidth = fixedColumnWidth;
/* 389 */     handleFixedCell(fixedCells, index);
/* 390 */     removeUselessCell(index);
/* 391 */     if ((this.handle.getCellsViewSkin()).lastRowLayout.get() == true) {
/* 392 */       (this.handle.getCellsViewSkin()).lastRowLayout.setValue(Boolean.valueOf(false));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 399 */     if (rowHeightChange && this.spreadsheetView.getFixedRows().contains(Integer.valueOf(this.spreadsheetView.getModelRow(index)))) {
/* 400 */       skin.computeFixedRowHeight();
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean hasRightBorder(CellView tableCell) {
/* 405 */     return (tableCell.getBorder() != null && 
/* 406 */       !tableCell.getBorder().isEmpty() && ((BorderStroke)tableCell
/* 407 */       .getBorder().getStrokes().get(0)).getWidths().getRight() > 0.0D);
/*     */   }
/*     */   
/*     */   private boolean hasLeftBorder(CellView tableCell) {
/* 411 */     return (tableCell.getBorder() != null && 
/* 412 */       !tableCell.getBorder().isEmpty() && ((BorderStroke)tableCell
/* 413 */       .getBorder().getStrokes().get(0)).getWidths().getLeft() > 0.0D);
/*     */   }
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
/*     */   private void removeUselessCell(int index) {
/* 427 */     getChildren().removeIf(t -> (t instanceof CellView) ? (
/*     */         
/* 429 */         (!this.cells.contains(t) && ((CellView)t).getIndex() == index)) : false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void removeDeportedCells() {
/* 436 */     GridViewSkin skin = this.handle.getCellsViewSkin();
/* 437 */     for (Map.Entry<GridRow, Set<CellView>> entry : skin.deportedCells.entrySet()) {
/* 438 */       ArrayList<CellView> toRemove = new ArrayList<>();
/* 439 */       for (CellView cell : entry.getValue()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 446 */         if (!cell.isEditing() && cell.getTableRow() == getSkinnable() && entry.getKey() != getSkinnable()) {
/* 447 */           ((GridRow)entry.getKey()).removeCell(cell);
/* 448 */           toRemove.add(cell);
/*     */         } 
/*     */       } 
/* 451 */       ((Set)entry.getValue()).removeAll(toRemove);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleFixedCell(List<CellView> fixedCells, int index) {
/* 462 */     removeDeportedCells();
/* 463 */     if (fixedCells.isEmpty()) {
/*     */       return;
/*     */     }
/* 466 */     GridViewSkin skin = this.handle.getCellsViewSkin();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 472 */     if (skin.rowToLayout.get(index)) {
/* 473 */       GridRow gridRow = skin.getFlow().getTopRow();
/* 474 */       if (gridRow != null) {
/* 475 */         for (CellView cell : fixedCells) {
/* 476 */           if (!cell.isEditing()) {
/* 477 */             gridRow.removeCell(cell);
/* 478 */             gridRow.addCell(cell);
/*     */           } 
/* 480 */           double originalLayoutY = ((TableRow)getSkinnable()).getLayoutY() + cell.getLayoutY();
/*     */           
/* 482 */           if (skin.deportedCells.containsKey(gridRow)) {
/* 483 */             ((Set<CellView>)skin.deportedCells.get(gridRow)).add(cell);
/*     */           } else {
/* 485 */             Set<CellView> temp = new HashSet<>();
/* 486 */             temp.add(cell);
/* 487 */             skin.deportedCells.put(gridRow, temp);
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 496 */           cell.relocate(cell.getLayoutX(), originalLayoutY - gridRow.getLayoutY());
/*     */         } 
/*     */       }
/*     */     } else {
/* 500 */       for (CellView cell : fixedCells) {
/* 501 */         cell.toFront();
/*     */       }
/*     */     } 
/*     */   }
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
/*     */   private HashMap<TableColumnBase, CellView> getCellsMap() {
/* 517 */     if (this.cellsMap == null || this.cellsMap.get() == null) {
/* 518 */       HashMap<TableColumnBase, CellView> map = new HashMap<>();
/* 519 */       this.cellsMap = new WeakReference<>(map);
/* 520 */       return map;
/*     */     } 
/* 522 */     return this.cellsMap.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void putCellsInCache() {
/* 529 */     for (CellView cell : this.cells) {
/* 530 */       getCellsMap().put(cell.getTableColumn(), cell);
/*     */     }
/* 532 */     this.cells.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CellView getCell(TableColumnBase tcb) {
/* 543 */     TableColumn tableColumn = (TableColumn)tcb;
/*     */     
/* 545 */     if (getCellsMap().containsKey(tableColumn)) {
/* 546 */       return getCellsMap().remove(tableColumn);
/*     */     }
/* 548 */     CellView cell = (CellView)tableColumn.getCellFactory().call(tableColumn);
/* 549 */     cell.updateTableColumn(tableColumn);
/* 550 */     cell.updateTableView(tableColumn.getTableView());
/* 551 */     cell.updateTableRow((TableRow)getSkinnable());
/*     */     
/* 553 */     return cell;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private double getFixedRowShift(int index) {
/* 564 */     double tableCellY = 0.0D;
/* 565 */     int positionY = this.spreadsheetView.getFixedRows().indexOf(Integer.valueOf(this.spreadsheetView.getFilteredSourceIndex(index)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 570 */     double space = 0.0D;
/* 571 */     for (int o = 0; o < positionY; o++) {
/* 572 */       if (!this.spreadsheetView.isRowHidden(o)) {
/* 573 */         space += this.handle.getCellsViewSkin().getRowHeight(((Integer)this.spreadsheetView.getFixedRows().get(o)).intValue());
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 578 */     if (positionY != -1 && ((TableRow)getSkinnable()).getLocalToParentTransform().getTy() <= space) {
/*     */       
/* 580 */       tableCellY = space - ((TableRow)getSkinnable()).getLocalToParentTransform().getTy();
/* 581 */       this.handle.getCellsViewSkin().getCurrentlyFixedRow().add(Integer.valueOf(index));
/*     */     } else {
/* 583 */       this.handle.getCellsViewSkin().getCurrentlyFixedRow().remove(Integer.valueOf(index));
/*     */     } 
/* 585 */     return tableCellY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private double getTableRowHeight(int row) {
/* 595 */     Double rowHeightCache = (Double)(this.handle.getCellsViewSkin()).rowHeightMap.get(Integer.valueOf(this.spreadsheetView.getModelRow(row)));
/* 596 */     return (rowHeightCache == null) ? this.handle.getView().getGrid().getRowHeight(this.spreadsheetView.getModelRow(row)) : rowHeightCache.doubleValue();
/*     */   }
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
/*     */   private boolean isInvisible(double x, double width, double hbarValue, double headerWidth, int columnSpan) {
/* 611 */     return ((x + width < hbarValue && columnSpan == 1) || x > hbarValue + headerWidth);
/*     */   }
/*     */ 
/*     */   
/*     */   protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 616 */     double prefWidth = 0.0D;
/*     */     
/* 618 */     ObservableList<TableColumnBase> observableList = this.handle.getGridView().getVisibleLeafColumns();
/* 619 */     for (int i = 0, max = observableList.size(); i < max; i++) {
/* 620 */       prefWidth += ((TableColumnBase)observableList.get(i)).getWidth();
/*     */     }
/*     */     
/* 623 */     return prefWidth;
/*     */   }
/*     */ 
/*     */   
/*     */   protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 628 */     return ((TableRow)getSkinnable()).getPrefHeight();
/*     */   }
/*     */ 
/*     */   
/*     */   protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 633 */     return ((TableRow)getSkinnable()).getPrefHeight();
/*     */   }
/*     */ 
/*     */   
/*     */   protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 638 */     return super.computeMaxHeight(width, topInset, rightInset, bottomInset, leftInset);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\spreadsheet\GridRowSkin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */