/*     */ package org.controlsfx.control.spreadsheet;
/*     */ 
/*     */ import com.sun.javafx.event.EventHandlerManager;
/*     */ import impl.org.controlsfx.spreadsheet.RectangleSelection;
/*     */ import java.util.BitSet;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.TreeSet;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.beans.property.BooleanProperty;
/*     */ import javafx.beans.property.SimpleBooleanProperty;
/*     */ import javafx.collections.FXCollections;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.event.Event;
/*     */ import javafx.event.EventDispatchChain;
/*     */ import javafx.event.EventDispatcher;
/*     */ import javafx.event.EventHandler;
/*     */ import javafx.event.EventTarget;
/*     */ import javafx.event.EventType;
/*     */ import javafx.util.Callback;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GridBase
/*     */   implements Grid, EventTarget
/*     */ {
/*     */   private ObservableList<ObservableList<SpreadsheetCell>> rows;
/*     */   private int rowCount;
/*     */   private int columnCount;
/*     */   private Callback<Integer, Double> rowHeightFactory;
/*     */   private final BooleanProperty locked;
/* 183 */   private final EventHandlerManager eventHandlerManager = new EventHandlerManager(this);
/*     */   private final ObservableList<String> rowsHeader;
/*     */   private final ObservableList<String> columnsHeader;
/*     */   private BitSet resizableRow;
/* 187 */   private final TreeSet<Long> displaySelectionCells = new TreeSet<>();
/* 188 */   private final TreeSet<Long> noDisplaySelectionCells = new TreeSet<>();
/* 189 */   private final BooleanProperty displaySelection = (BooleanProperty)new SimpleBooleanProperty(true);
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
/*     */   public GridBase(int rowCount, int columnCount) {
/* 204 */     this.rowCount = rowCount;
/* 205 */     this.columnCount = columnCount;
/* 206 */     this.rowsHeader = FXCollections.observableArrayList();
/* 207 */     this.columnsHeader = FXCollections.observableArrayList();
/* 208 */     this.locked = (BooleanProperty)new SimpleBooleanProperty(false);
/* 209 */     this.rowHeightFactory = new MapBasedRowHeightFactory(new HashMap<>());
/* 210 */     this.rows = FXCollections.observableArrayList();
/* 211 */     this.rows.addListener(observable -> setRowCount(this.rows.size()));
/*     */ 
/*     */     
/* 214 */     this.resizableRow = new BitSet(rowCount);
/* 215 */     this.resizableRow.set(0, rowCount, true);
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
/*     */   public ObservableList<ObservableList<SpreadsheetCell>> getRows() {
/* 227 */     return this.rows;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCellValue(int modelRow, int column, Object value) {
/* 233 */     if (modelRow < getRowCount() && column < this.columnCount && !isLocked()) {
/* 234 */       SpreadsheetCell cell = (SpreadsheetCell)((ObservableList)getRows().get(modelRow)).get(column);
/* 235 */       Object previousItem = cell.getItem();
/* 236 */       Object convertedValue = cell.getCellType().convertValue(value);
/* 237 */       cell.setItem(convertedValue);
/* 238 */       if (!Objects.equals(previousItem, cell.getItem())) {
/* 239 */         GridChange cellChange = new GridChange(cell.getRow(), cell.getColumn(), previousItem, convertedValue);
/* 240 */         Event.fireEvent(this, cellChange);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRowCount() {
/* 248 */     return this.rowCount;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColumnCount() {
/* 254 */     return this.columnCount;
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
/*     */   public double getRowHeight(int row) {
/* 266 */     return ((Double)this.rowHeightFactory.call(Integer.valueOf(row))).doubleValue();
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
/*     */   
/*     */   public void setRowHeightCallback(Callback<Integer, Double> rowHeight) {
/* 282 */     this.rowHeightFactory = rowHeight;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ObservableList<String> getRowHeaders() {
/* 288 */     return this.rowsHeader;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ObservableList<String> getColumnHeaders() {
/* 294 */     return this.columnsHeader;
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
/*     */   public BooleanProperty lockedProperty() {
/* 306 */     return this.locked;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLocked() {
/* 315 */     return this.locked.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocked(Boolean lock) {
/* 324 */     this.locked.setValue(lock);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void spanRow(int count, int rowIndex, int colIndex) {
/* 330 */     if (count <= 0 || count > getRowCount() || rowIndex >= getRowCount() || colIndex >= this.columnCount) {
/*     */       return;
/*     */     }
/* 333 */     SpreadsheetCell cell = (SpreadsheetCell)((ObservableList)this.rows.get(rowIndex)).get(colIndex);
/* 334 */     int colSpan = cell.getColumnSpan();
/* 335 */     int rowSpan = count;
/* 336 */     cell.setRowSpan(rowSpan);
/* 337 */     for (int row = rowIndex; row < rowIndex + rowSpan && row < getRowCount(); row++) {
/* 338 */       for (int col = colIndex; col < colIndex + colSpan && col < this.columnCount; col++) {
/* 339 */         if (row != rowIndex || col != colIndex) {
/* 340 */           ((ObservableList)this.rows.get(row)).set(col, cell);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void spanColumn(int count, int rowIndex, int colIndex) {
/* 349 */     if (count <= 0 || count > this.columnCount || rowIndex >= getRowCount() || colIndex >= this.columnCount) {
/*     */       return;
/*     */     }
/* 352 */     SpreadsheetCell cell = (SpreadsheetCell)((ObservableList)this.rows.get(rowIndex)).get(colIndex);
/* 353 */     int colSpan = count;
/* 354 */     int rowSpan = cell.getRowSpan();
/* 355 */     cell.setColumnSpan(colSpan);
/* 356 */     for (int row = rowIndex; row < rowIndex + rowSpan && row < getRowCount(); row++) {
/* 357 */       for (int col = colIndex; col < colIndex + colSpan && col < this.columnCount; col++) {
/* 358 */         if (row != rowIndex || col != colIndex) {
/* 359 */           ((ObservableList)this.rows.get(row)).set(col, cell);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRows(Collection<ObservableList<SpreadsheetCell>> rows) {
/* 368 */     this.rows.clear();
/* 369 */     this.rows.addAll(rows);
/*     */     
/* 371 */     setRowCount(rows.size());
/* 372 */     setColumnCount((this.rowCount == 0) ? 0 : ((ObservableList)this.rows.get(0)).size());
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
/*     */   public void setResizableRows(BitSet resizableRow) {
/* 384 */     this.resizableRow = resizableRow;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRowResizable(int row) {
/* 390 */     return this.resizableRow.get(row);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDisplaySelection() {
/* 398 */     return this.displaySelection.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDisplaySelection(boolean value) {
/* 406 */     this.displaySelection.setValue(Boolean.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BooleanProperty displaySelectionProperty() {
/* 414 */     return this.displaySelection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCellDisplaySelection(int row, int column, boolean displaySelection) {
/* 422 */     Long key = RectangleSelection.SelectionRange.key(row, column);
/* 423 */     if (displaySelection) {
/* 424 */       this.displaySelectionCells.add(key);
/* 425 */       this.noDisplaySelectionCells.remove(key);
/*     */     } else {
/* 427 */       this.displaySelectionCells.remove(key);
/* 428 */       this.noDisplaySelectionCells.add(key);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCellDisplaySelection(int row, int column) {
/* 437 */     Long key = RectangleSelection.SelectionRange.key(row, column);
/* 438 */     if (this.displaySelectionCells.contains(key))
/* 439 */       return true; 
/* 440 */     if (this.noDisplaySelectionCells.contains(key)) {
/* 441 */       return false;
/*     */     }
/* 443 */     return isDisplaySelection();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <E extends GridChange> void addEventHandler(EventType<E> eventType, EventHandler<E> eventHandler) {
/* 449 */     this.eventHandlerManager.addEventHandler(eventType, eventHandler);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <E extends GridChange> void removeEventHandler(EventType<E> eventType, EventHandler<E> eventHandler) {
/* 455 */     this.eventHandlerManager.removeEventHandler(eventType, eventHandler);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
/* 461 */     return tail.append((EventDispatcher)this.eventHandlerManager);
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
/*     */   private void setRowCount(int rowCount) {
/* 476 */     this.rowCount = rowCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setColumnCount(int columnCount) {
/* 485 */     this.columnCount = columnCount;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class MapBasedRowHeightFactory
/*     */     implements Callback<Integer, Double>
/*     */   {
/*     */     private final Map<Integer, Double> rowHeightMap;
/*     */ 
/*     */     
/*     */     public MapBasedRowHeightFactory(Map<Integer, Double> rowHeightMap) {
/* 497 */       this.rowHeightMap = rowHeightMap;
/*     */     }
/*     */ 
/*     */     
/*     */     public Double call(Integer index) {
/* 502 */       Double value = this.rowHeightMap.get(index);
/* 503 */       return Double.valueOf((value == null) ? -1.0D : value.doubleValue());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\spreadsheet\GridBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */