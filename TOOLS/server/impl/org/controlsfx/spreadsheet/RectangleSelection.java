/*     */ package impl.org.controlsfx.spreadsheet;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.TreeSet;
/*     */ import javafx.beans.InvalidationListener;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.beans.value.ObservableNumberValue;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.event.Event;
/*     */ import javafx.event.EventHandler;
/*     */ import javafx.scene.control.IndexedCell;
/*     */ import javafx.scene.control.TableColumn;
/*     */ import javafx.scene.control.TablePosition;
/*     */ import javafx.scene.control.TableView;
/*     */ import javafx.scene.input.MouseEvent;
/*     */ import javafx.scene.shape.Rectangle;
/*     */ import org.controlsfx.control.spreadsheet.GridChange;
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
/*     */ public class RectangleSelection
/*     */   extends Rectangle
/*     */ {
/*     */   private final GridViewSkin skin;
/*     */   private final TableViewSpanSelectionModel sm;
/*     */   private final SelectionRange selectionRange;
/*     */   private final InvalidationListener selectedCellListener;
/*     */   private final InvalidationListener layoutListener;
/*     */   private final EventHandler<MouseEvent> mouseDraggedListener;
/*     */   
/*     */   public RectangleSelection(GridViewSkin skin, TableViewSpanSelectionModel sm) {
/*  82 */     this.layoutListener = (observable -> updateRectangle());
/*     */ 
/*     */ 
/*     */     
/*  86 */     this.mouseDraggedListener = new EventHandler<MouseEvent>()
/*     */       {
/*     */         public void handle(MouseEvent event)
/*     */         {
/*  90 */           RectangleSelection.this.skin.getVBar().valueProperty().removeListener(RectangleSelection.this.layoutListener);
/*  91 */           RectangleSelection.this.setVisible(false);
/*  92 */           RectangleSelection.this.skin.getVBar().addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>()
/*     */               {
/*     */                 public void handle(MouseEvent event)
/*     */                 {
/*  96 */                   RectangleSelection.this.skin.getVBar().removeEventFilter(MouseEvent.MOUSE_RELEASED, this);
/*  97 */                   RectangleSelection.this.skin.getVBar().valueProperty().addListener(RectangleSelection.this.layoutListener);
/*  98 */                   RectangleSelection.this.updateRectangle(); } }); } }; this.skin = skin; this.sm = sm; getStyleClass().add("selection-rectangle"); setMouseTransparent(true); this.selectionRange = new SelectionRange(); this.selectedCellListener = (observable -> {
/*     */         skin.getHorizontalHeader().clearSelectedColumns(); skin.verticalHeader.clearSelectedRows(); this.selectionRange.fill((List<TablePosition>)sm.getSelectedCells(), skin.spreadsheetView); updateRectangle();
/*     */       }); skin.getVBar().valueProperty().addListener(this.layoutListener);
/*     */     skin.getVBar().addEventFilter(MouseEvent.MOUSE_DRAGGED, this.mouseDraggedListener);
/*     */     skin.spreadsheetView.hiddenRowsProperty().addListener(this.selectedCellListener);
/*     */     skin.spreadsheetView.hiddenColumnsProperty().addListener(this.selectedCellListener);
/*     */     skin.getHBar().valueProperty().addListener(this.layoutListener);
/* 105 */     sm.getSelectedCells().addListener(this.selectedCellListener); } public final void updateRectangle() { if (this.sm.getSelectedCells().isEmpty() || this.skin
/* 106 */       .getSelectedRows().isEmpty() || this.skin
/* 107 */       .getSelectedColumns().isEmpty() || this.selectionRange
/* 108 */       .range == null) {
/* 109 */       setVisible(false);
/*     */       
/*     */       return;
/*     */     } 
/* 113 */     GridRow gridRow1 = this.skin.getFlow().getTopRow();
/* 114 */     if (gridRow1 == null) {
/*     */       return;
/*     */     }
/*     */     
/* 118 */     int topRow = gridRow1.getIndex();
/* 119 */     IndexedCell bottomRowCell = this.skin.getFlow().getCells().get(this.skin.getFlow().getCells().size() - 1);
/* 120 */     if (bottomRowCell == null) {
/*     */       return;
/*     */     }
/* 123 */     int bottomRow = bottomRowCell.getIndex();
/*     */     
/* 125 */     int minRow = this.selectionRange.range.getTop();
/* 126 */     if (minRow > bottomRow) {
/* 127 */       setVisible(false);
/*     */       return;
/*     */     } 
/* 130 */     minRow = Math.max(minRow, topRow);
/*     */     
/* 132 */     int maxRow = this.selectionRange.range.getBottom();
/* 133 */     if (maxRow < topRow) {
/* 134 */       setVisible(false);
/*     */       
/*     */       return;
/*     */     } 
/* 138 */     maxRow = Math.min(maxRow, bottomRow);
/* 139 */     int minColumn = this.selectionRange.range.getLeft();
/* 140 */     int maxColumn = this.selectionRange.range.getRight();
/*     */     
/* 142 */     GridRow gridMinRow = this.skin.getRowIndexed(minRow);
/* 143 */     if (gridMinRow == null) {
/* 144 */       setVisible(false);
/*     */       
/*     */       return;
/*     */     } 
/* 148 */     if (maxRow >= this.skin.getItemCount() || maxColumn >= ((TableView)this.skin.getSkinnable()).getVisibleLeafColumns().size() || minColumn < 0) {
/*     */       
/* 150 */       setVisible(false);
/*     */       return;
/*     */     } 
/* 153 */     SpreadsheetCell cell = (SpreadsheetCell)((ObservableList)((TableView)this.skin.getSkinnable()).getItems().get(maxRow)).get(this.skin.spreadsheetView.getModelColumn(maxColumn));
/* 154 */     handleHorizontalPositioning(minColumn, this.skin.spreadsheetView.getViewColumn(cell.getColumn()) + this.skin.spreadsheetView.getColumnSpan(cell) - 1);
/*     */ 
/*     */     
/* 157 */     if (getX() + getWidth() < 0.0D) {
/* 158 */       setVisible(false);
/*     */       
/*     */       return;
/*     */     } 
/* 162 */     GridRow gridMaxRow = this.skin.getRowIndexed(maxRow);
/* 163 */     if (gridMaxRow == null) {
/* 164 */       setVisible(false);
/*     */       return;
/*     */     } 
/* 167 */     setVisible(true);
/*     */     
/* 169 */     handleVerticalPositioning(minRow, maxRow, gridMinRow, gridMaxRow); }
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
/*     */   private void handleVerticalPositioning(int minRow, int maxRow, GridRow gridMinRow, GridRow gridMaxRow) {
/* 181 */     double height = 0.0D;
/* 182 */     for (int i = maxRow; i <= maxRow; i++) {
/* 183 */       height += this.skin.getRowHeight(i);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 191 */     if (!this.skin.getCurrentlyFixedRow().contains(Integer.valueOf(minRow))) {
/* 192 */       yProperty().unbind();
/*     */       
/* 194 */       if (gridMinRow.getLayoutY() < this.skin.getFixedRowHeight()) {
/* 195 */         setY(this.skin.getFixedRowHeight());
/*     */       } else {
/* 197 */         yProperty().bind((ObservableValue)gridMinRow.layoutYProperty());
/*     */       
/*     */       }
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 206 */       yProperty().bind((ObservableValue)gridMinRow.layoutYProperty().add((ObservableNumberValue)gridMinRow.verticalShift));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 213 */     heightProperty().bind((ObservableValue)gridMaxRow.layoutYProperty().add((ObservableNumberValue)gridMaxRow.verticalShift).subtract((ObservableNumberValue)yProperty()).add(height));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleHorizontalPositioning(int minColumn, int maxColumn) {
/* 223 */     double x = 0.0D;
/*     */     
/* 225 */     ObservableList<TableColumn> observableList = this.skin.handle.getGridView().getVisibleLeafColumns();
/* 226 */     ObservableList observableList1 = this.skin.handle.getGridView().getColumns();
/* 227 */     ObservableList observableList2 = this.skin.spreadsheetView.getColumns();
/* 228 */     if (observableList.size() <= minColumn || observableList.size() <= maxColumn) {
/*     */       return;
/*     */     }
/*     */     
/* 232 */     for (int i = 0; i < minColumn; i++)
/*     */     {
/* 234 */       x += snapSize(((TableColumn)observableList.get(i)).getWidth());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 242 */     x -= this.skin.getHBar().getValue();
/*     */ 
/*     */     
/* 245 */     double width = 0.0D;
/* 246 */     for (int j = minColumn; j <= maxColumn; j++) {
/* 247 */       width += snapSize(((TableColumn)observableList.get(j)).getWidth());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 257 */     if (!this.skin.spreadsheetView.getFixedColumns().contains(observableList2.get(this.skin.spreadsheetView.getModelColumn(minColumn)))) {
/* 258 */       if (x < this.skin.fixedColumnWidth)
/*     */       {
/* 260 */         width -= this.skin.fixedColumnWidth - x;
/* 261 */         x = this.skin.fixedColumnWidth;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 276 */     else if (x + width < this.skin.fixedColumnWidth) {
/* 277 */       x = 0.0D;
/* 278 */       width = 0.0D;
/* 279 */       for (SpreadsheetColumn column : this.skin.spreadsheetView.getFixedColumns()) {
/* 280 */         int indexColumn = this.skin.spreadsheetView.getViewColumn(observableList2.indexOf(column));
/* 281 */         if (indexColumn < minColumn && indexColumn != minColumn) {
/* 282 */           x += snapSize(column.getWidth());
/*     */         }
/* 284 */         if (indexColumn >= minColumn && indexColumn <= maxColumn) {
/* 285 */           width += snapSize(column.getWidth());
/*     */         
/*     */         }
/*     */       
/*     */       }
/*     */ 
/*     */     
/*     */     }
/* 293 */     else if (x < this.skin.fixedColumnWidth) {
/* 294 */       double tempX = 0.0D;
/* 295 */       for (SpreadsheetColumn column : this.skin.spreadsheetView.getFixedColumns()) {
/* 296 */         int indexColumn = this.skin.spreadsheetView.getViewColumn(observableList2.indexOf(column));
/* 297 */         if (indexColumn < minColumn && indexColumn != minColumn) {
/* 298 */           tempX += snapSize(column.getWidth());
/*     */         }
/*     */       } 
/* 301 */       width -= tempX - x;
/* 302 */       x = tempX;
/*     */     } 
/*     */     
/* 305 */     setX(x);
/* 306 */     setWidth(width);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private double snapSize(double value) {
/* 316 */     return Math.ceil(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class SelectionRange
/*     */   {
/* 325 */     private final TreeSet<Long> set = new TreeSet<>();
/*     */ 
/*     */     
/*     */     private RectangleSelection.GridRange range;
/*     */ 
/*     */     
/*     */     public void fill(List<TablePosition> list) {
/* 332 */       this.set.clear();
/* 333 */       for (TablePosition pos : list) {
/* 334 */         long key = key(pos.getRow(), pos.getColumn()).longValue();
/* 335 */         this.set.add(Long.valueOf(key));
/*     */       } 
/* 337 */       computeRange();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void fill(List<TablePosition> list, SpreadsheetView spv) {
/* 348 */       this.set.clear();
/* 349 */       this.range = null;
/* 350 */       for (TablePosition pos : list) {
/* 351 */         long key = key(pos.getRow(), pos.getColumn()).longValue();
/* 352 */         this.set.add(Long.valueOf(key));
/*     */         
/* 354 */         if (!spv.getGrid().isCellDisplaySelection(spv.getModelRow(pos.getRow()), spv.getModelColumn(pos.getColumn()))) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */       
/* 359 */       computeRange();
/*     */     }
/*     */     
/*     */     public void fillGridRange(List<GridChange> list) {
/* 363 */       this.set.clear();
/* 364 */       this.range = null;
/* 365 */       for (GridChange pos : list) {
/* 366 */         this.set.add(key(pos.getRow(), pos.getColumn()));
/*     */       }
/* 368 */       computeRange();
/*     */     }
/*     */     
/*     */     public RectangleSelection.GridRange getRange() {
/* 372 */       return this.range;
/*     */     }
/*     */     
/*     */     public static Long key(int row, int column) {
/* 376 */       return Long.valueOf(row << 32L | column);
/*     */     }
/*     */     
/*     */     private int getRow(Long l) {
/* 380 */       return (int)(l.longValue() >> 32L);
/*     */     }
/*     */     
/*     */     private int getColumn(Long l) {
/* 384 */       return (int)(l.longValue() & 0xFFFFFFFFFFFFFFFFL);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void computeRange() {
/* 393 */       this.range = null;
/* 394 */       while (!this.set.isEmpty()) {
/* 395 */         if (this.range != null) {
/* 396 */           this.range = null;
/*     */           
/*     */           return;
/*     */         } 
/* 400 */         long first = ((Long)this.set.first()).longValue();
/* 401 */         this.set.remove(Long.valueOf(first));
/*     */         
/* 403 */         int row = getRow(Long.valueOf(first));
/* 404 */         int column = getColumn(Long.valueOf(first));
/*     */ 
/*     */         
/* 407 */         while (this.set.contains(key(row, column + 1))) {
/* 408 */           column++;
/* 409 */           this.set.remove(key(row, column));
/*     */         } 
/*     */ 
/*     */         
/* 413 */         boolean flag = true;
/* 414 */         while (flag) {
/* 415 */           row++; int col;
/* 416 */           for (col = getColumn(Long.valueOf(first)); col <= column; col++) {
/* 417 */             if (!this.set.contains(key(row, col))) {
/* 418 */               flag = false;
/*     */               break;
/*     */             } 
/*     */           } 
/* 422 */           if (flag) {
/* 423 */             for (col = getColumn(Long.valueOf(first)); col <= column; col++)
/* 424 */               this.set.remove(key(row, col)); 
/*     */             continue;
/*     */           } 
/* 427 */           row--;
/*     */         } 
/*     */         
/* 430 */         this.range = new RectangleSelection.GridRange(getRow(Long.valueOf(first)), row, getColumn(Long.valueOf(first)), column);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static class GridRange
/*     */   {
/*     */     private final int top;
/*     */     private final int bottom;
/*     */     private final int left;
/*     */     private final int right;
/*     */     
/*     */     public GridRange(int top, int bottom, int left, int right) {
/* 443 */       this.top = top;
/* 444 */       this.bottom = bottom;
/* 445 */       this.left = left;
/* 446 */       this.right = right;
/*     */     }
/*     */     
/*     */     public int getTop() {
/* 450 */       return this.top;
/*     */     }
/*     */     
/*     */     public int getBottom() {
/* 454 */       return this.bottom;
/*     */     }
/*     */     
/*     */     public int getLeft() {
/* 458 */       return this.left;
/*     */     }
/*     */     
/*     */     public int getRight() {
/* 462 */       return this.right;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\spreadsheet\RectangleSelection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */