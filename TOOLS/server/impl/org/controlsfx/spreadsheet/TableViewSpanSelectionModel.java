/*     */ package impl.org.controlsfx.spreadsheet;
/*     */ 
/*     */ import com.sun.javafx.collections.MappingChange;
/*     */ import com.sun.javafx.collections.NonIterableChange;
/*     */ import com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import javafx.animation.KeyFrame;
/*     */ import javafx.animation.Timeline;
/*     */ import javafx.beans.InvalidationListener;
/*     */ import javafx.beans.NamedArg;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.collections.ListChangeListener;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.collections.WeakListChangeListener;
/*     */ import javafx.event.ActionEvent;
/*     */ import javafx.event.Event;
/*     */ import javafx.event.EventHandler;
/*     */ import javafx.event.WeakEventHandler;
/*     */ import javafx.scene.control.SelectionMode;
/*     */ import javafx.scene.control.TableColumn;
/*     */ import javafx.scene.control.TableColumnBase;
/*     */ import javafx.scene.control.TablePosition;
/*     */ import javafx.scene.control.TableView;
/*     */ import javafx.scene.input.KeyEvent;
/*     */ import javafx.scene.input.MouseEvent;
/*     */ import javafx.util.Duration;
/*     */ import javafx.util.Pair;
/*     */ import org.controlsfx.control.spreadsheet.SpreadsheetCell;
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
/*     */ public class TableViewSpanSelectionModel
/*     */   extends TableView.TableViewSelectionModel<ObservableList<SpreadsheetCell>>
/*     */ {
/*     */   private boolean shift = false;
/*     */   private boolean key = false;
/*     */   private boolean drag = false;
/*     */   private MouseEvent mouseEvent;
/*     */   private boolean makeAtomic;
/*     */   private SpreadsheetGridView cellsView;
/*     */   private SpreadsheetView spreadsheetView;
/*     */   private final SelectedCellsMapTemp<TablePosition<ObservableList<SpreadsheetCell>, ?>> selectedCellsMap;
/*     */   private final ReadOnlyUnbackedObservableList<TablePosition<ObservableList<SpreadsheetCell>, ?>> selectedCellsSeq;
/*  92 */   private TableColumn oldTableColumn = null;
/*  93 */   private int oldRow = -1;
/*     */   public Pair<Integer, Integer> direction;
/*  95 */   private int oldColSpan = -1;
/*  96 */   private int oldRowSpan = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Timeline timer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final EventHandler<ActionEvent> timerEventHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final EventHandler<MouseEvent> dragDoneHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final EventHandler<KeyEvent> keyPressedEventHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final EventHandler<MouseEvent> mousePressedEventHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final EventHandler<MouseEvent> onDragDetectedEventHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final EventHandler<MouseEvent> onMouseDragEventHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ListChangeListener<TablePosition<ObservableList<SpreadsheetCell>, ?>> listChangeListener;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TablePosition<ObservableList<SpreadsheetCell>, ?> old;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TableViewSpanSelectionModel(@NamedArg("spreadsheetView") SpreadsheetView spreadsheetView, @NamedArg("cellsView") SpreadsheetGridView cellsView) {
/* 183 */     super(cellsView);
/*     */     this.timerEventHandler = (event -> {
/*     */         GridViewSkin skin = getCellsViewSkin();
/*     */         if (this.mouseEvent != null && !this.cellsView.contains(this.mouseEvent.getX(), this.mouseEvent.getY())) {
/*     */           double sceneX = this.mouseEvent.getSceneX();
/*     */           double sceneY = this.mouseEvent.getSceneY();
/*     */           double layoutX = this.cellsView.getLocalToSceneTransform().getTx();
/*     */           double layoutY = this.cellsView.getLocalToSceneTransform().getTy();
/*     */           double layoutXMax = layoutX + this.cellsView.getWidth();
/*     */           double layoutYMax = layoutY + this.cellsView.getHeight();
/*     */           if (sceneX > layoutXMax) {
/*     */             skin.getHBar().increment();
/*     */           } else if (sceneX < layoutX) {
/*     */             skin.getHBar().decrement();
/*     */           } 
/*     */           if (sceneY > layoutYMax) {
/*     */             skin.getVBar().increment();
/*     */           } else if (sceneY < layoutY) {
/*     */             skin.getVBar().decrement();
/*     */           } 
/*     */         } 
/*     */       });
/*     */     this.dragDoneHandler = new EventHandler<MouseEvent>() { public void handle(MouseEvent mouseEvent) { TableViewSpanSelectionModel.this.drag = false;
/*     */           TableViewSpanSelectionModel.this.timer.stop();
/*     */           TableViewSpanSelectionModel.this.spreadsheetView.removeEventHandler(MouseEvent.MOUSE_RELEASED, this); } }
/*     */       ;
/*     */     this.keyPressedEventHandler = (keyEvent -> {
/*     */         this.key = true;
/*     */         this.shift = keyEvent.isShiftDown();
/*     */       });
/*     */     this.mousePressedEventHandler = (mouseEvent1 -> {
/*     */         this.key = false;
/*     */         this.shift = mouseEvent1.isShiftDown();
/*     */       });
/*     */     this.onDragDetectedEventHandler = new EventHandler<MouseEvent>() {
/*     */         public void handle(MouseEvent mouseEvent) { TableViewSpanSelectionModel.this.cellsView.addEventHandler(MouseEvent.MOUSE_RELEASED, TableViewSpanSelectionModel.this.dragDoneHandler);
/*     */           TableViewSpanSelectionModel.this.drag = true;
/*     */           TableViewSpanSelectionModel.this.timer.setCycleCount(-1);
/*     */           TableViewSpanSelectionModel.this.timer.play(); }
/*     */       };
/*     */     this.onMouseDragEventHandler = (e -> this.mouseEvent = e);
/*     */     this.listChangeListener = this::handleSelectedCellsListChangeEvent;
/* 225 */     this.old = null; this.cellsView = cellsView; this.spreadsheetView = spreadsheetView; this.timer = new Timeline(new KeyFrame[] { new KeyFrame(Duration.millis(100.0D), (EventHandler)new WeakEventHandler(this.timerEventHandler), new javafx.animation.KeyValue[0]) }); cellsView.addEventHandler(KeyEvent.KEY_PRESSED, (EventHandler)new WeakEventHandler(this.keyPressedEventHandler)); cellsView.addEventFilter(MouseEvent.MOUSE_PRESSED, (EventHandler)new WeakEventHandler(this.mousePressedEventHandler)); cellsView.setOnDragDetected((EventHandler)new WeakEventHandler(this.onDragDetectedEventHandler)); cellsView.setOnMouseDragged((EventHandler)new WeakEventHandler(this.onMouseDragEventHandler));
/*     */     this.selectedCellsMap = new SelectedCellsMapTemp<>((ListChangeListener<TablePosition<ObservableList<SpreadsheetCell>, ?>>)new WeakListChangeListener(this.listChangeListener));
/*     */     this.selectedCellsSeq = new ReadOnlyUnbackedObservableList<TablePosition<ObservableList<SpreadsheetCell>, ?>>() { public TablePosition<ObservableList<SpreadsheetCell>, ?> get(int i) { return TableViewSpanSelectionModel.this.selectedCellsMap.get(i); } public int size() { return TableViewSpanSelectionModel.this.selectedCellsMap.size(); } }
/*     */       ;
/* 229 */   } public void select(int row, TableColumn<ObservableList<SpreadsheetCell>, ?> column) { if (row < 0 || row >= getItemCount()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 236 */     if (isCellSelectionEnabled() && column == null) {
/*     */       return;
/*     */     }
/*     */     
/* 240 */     TablePosition<ObservableList<SpreadsheetCell>, ?> posFinal = new TablePosition(getTableView(), row, column);
/*     */ 
/*     */     
/* 243 */     int columnIndex = this.cellsView.getColumns().indexOf(posFinal.getTableColumn());
/* 244 */     SpreadsheetView.SpanType spanType = this.spreadsheetView.getSpanType(row, columnIndex);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 251 */     switch (spanType) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case ROW_SPAN_INVISIBLE:
/* 260 */         if (this.old != null && !this.shift && this.old.getColumn() == posFinal.getColumn() && this.old
/* 261 */           .getRow() == posFinal.getRow() - 1) {
/* 262 */           int visibleRow = FocusModelListener.getNextRowNumber(this.old, this.cellsView, this.spreadsheetView);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 269 */           if (visibleRow < getItemCount()) {
/* 270 */             posFinal = getVisibleCell(visibleRow, (this.oldColSpan > 1) ? this.oldTableColumn : this.old.getTableColumn());
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */         
/* 276 */         posFinal = getVisibleCell(row, (this.oldColSpan > 1) ? this.oldTableColumn : column);
/*     */         break;
/*     */ 
/*     */       
/*     */       case BOTH_INVISIBLE:
/* 281 */         posFinal = getVisibleCell(row, column);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case COLUMN_SPAN_INVISIBLE:
/* 287 */         if (this.old != null && !this.shift && this.old.getColumn() == posFinal.getColumn() - 1 && this.old
/* 288 */           .getRow() == posFinal.getRow()) {
/* 289 */           posFinal = getVisibleCell((this.oldRowSpan > 1) ? this.oldRow : this.old.getRow(), FocusModelListener.getTableColumnSpan(this.old, this.cellsView, this.spreadsheetView));
/*     */         }
/*     */         else {
/*     */           
/* 293 */           posFinal = getVisibleCell(row, column);
/*     */         } 
/*     */       default:
/* 296 */         if (this.direction != null && this.key) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 302 */           if (((Integer)this.direction.getKey()).intValue() != 0 && this.oldColSpan > 1) {
/*     */             
/* 304 */             posFinal = getVisibleCell(posFinal.getRow(), this.oldTableColumn); break;
/* 305 */           }  if (((Integer)this.direction.getValue()).intValue() != 0 && this.oldRowSpan > 1) {
/* 306 */             posFinal = getVisibleCell(this.oldRow, posFinal.getTableColumn());
/*     */           }
/*     */         } 
/*     */         break;
/*     */     } 
/*     */     
/* 312 */     this.old = posFinal;
/*     */ 
/*     */     
/* 315 */     if (!this.key) {
/* 316 */       this.oldRow = this.old.getRow();
/*     */       
/* 318 */       this.oldTableColumn = this.old.getTableColumn();
/*     */     }
/* 320 */     else if (this.direction != null && ((Integer)this.direction.getKey()).intValue() != 0) {
/* 321 */       this.oldRow = this.old.getRow();
/* 322 */     } else if (this.direction != null && ((Integer)this.direction.getValue()).intValue() != 0) {
/*     */       
/* 324 */       this.oldTableColumn = this.old.getTableColumn();
/*     */     } 
/* 326 */     if (getSelectionMode() == SelectionMode.SINGLE) {
/* 327 */       quietClearSelection();
/*     */     }
/* 329 */     SpreadsheetCell cell = (SpreadsheetCell)this.old.getTableColumn().getCellData(this.old.getRow());
/* 330 */     this.oldRowSpan = this.spreadsheetView.getRowSpan(cell, this.old.getRow());
/* 331 */     this.oldColSpan = this.spreadsheetView.getColumnSpan(cell);
/* 332 */     for (int i = this.old.getRow(); i < this.oldRowSpan + this.old.getRow(); i++) {
/* 333 */       for (int j = this.spreadsheetView.getViewColumn(cell.getColumn()); j < this.oldColSpan + this.spreadsheetView.getViewColumn(cell.getColumn()); j++) {
/* 334 */         posFinal = new TablePosition(getTableView(), i, getTableView().getVisibleLeafColumn(j));
/* 335 */         this.selectedCellsMap.add(posFinal);
/*     */       } 
/*     */     } 
/*     */     
/* 339 */     updateScroll(this.old);
/* 340 */     addSelectedRowsAndColumns(this.old);
/*     */     
/* 342 */     setSelectedIndex(this.old.getRow());
/* 343 */     setSelectedItem(getModelItem(this.old.getRow()));
/* 344 */     if (getTableView().getFocusModel() == null) {
/*     */       return;
/*     */     }
/*     */     
/* 348 */     getTableView().getFocusModel().focus(this.old.getRow(), this.old.getTableColumn()); }
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleSelectedCellsListChangeEvent(ListChangeListener.Change<? extends TablePosition<ObservableList<SpreadsheetCell>, ?>> c) {
/*     */     if (this.makeAtomic) {
/*     */       return;
/*     */     }
/*     */     this.selectedCellsSeq.callObservers((ListChangeListener.Change)new MappingChange(c, MappingChange.NOOP_MAP, (ObservableList)this.selectedCellsSeq));
/*     */     c.reset();
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateScroll(TablePosition<ObservableList<SpreadsheetCell>, ?> posFinal) {
/* 362 */     if (!this.drag && this.key && getCellsViewSkin().getCellsSize() != 0 && !VerticalHeader.isFixedRowEmpty(this.spreadsheetView)) {
/*     */       
/* 364 */       int start = getCellsViewSkin().getRow(0).getIndex();
/* 365 */       double posFinalOffset = 0.0D;
/* 366 */       for (int j = start; j < posFinal.getRow(); j++) {
/* 367 */         posFinalOffset += getSpreadsheetViewSkin().getRowHeight(j);
/*     */       }
/*     */       
/* 370 */       if (getCellsViewSkin().getFixedRowHeight() > posFinalOffset) {
/* 371 */         this.cellsView.scrollTo(posFinal.getRow());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearSelection(int row, TableColumn<ObservableList<SpreadsheetCell>, ?> column) {
/* 380 */     TablePosition<ObservableList<SpreadsheetCell>, ?> tp = new TablePosition(getTableView(), row, column);
/*     */     
/* 382 */     if (tp.getRow() < 0 || tp.getColumn() < 0) {
/*     */       return;
/*     */     }
/*     */     List<TablePosition<ObservableList<SpreadsheetCell>, ?>> selectedCells;
/* 386 */     if ((selectedCells = isSelectedRange(row, column, tp.getColumn())) != null) {
/* 387 */       for (TablePosition<ObservableList<SpreadsheetCell>, ?> cell : selectedCells) {
/* 388 */         this.selectedCellsMap.remove(cell);
/* 389 */         removeSelectedRowsAndColumns(cell);
/* 390 */         focus(cell.getRow());
/*     */       } 
/*     */     } else {
/* 393 */       for (TablePosition<ObservableList<SpreadsheetCell>, ?> pos : getSelectedCells()) {
/* 394 */         if (pos.equals(tp)) {
/* 395 */           this.selectedCellsMap.remove(pos);
/* 396 */           removeSelectedRowsAndColumns(pos);
/*     */           
/* 398 */           focus(row);
/*     */           return;
/*     */         } 
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
/*     */   public void verifySelectedCells(List<Pair<Integer, Integer>> selectedCells) {
/* 414 */     List<TablePosition<ObservableList<SpreadsheetCell>, ?>> newList = new ArrayList<>();
/* 415 */     clearSelection();
/*     */     
/* 417 */     int itemCount = getItemCount();
/* 418 */     int columnSize = getTableView().getVisibleLeafColumns().size();
/* 419 */     final HashSet<Integer> selectedRows = new HashSet<>();
/* 420 */     final HashSet<Integer> selectedColumns = new HashSet<>();
/* 421 */     TablePosition<ObservableList<SpreadsheetCell>, ?> pos = null;
/* 422 */     for (Pair<Integer, Integer> position : selectedCells) {
/* 423 */       if (((Integer)position.getKey()).intValue() < 0 || ((Integer)position
/* 424 */         .getKey()).intValue() >= itemCount || ((Integer)position
/* 425 */         .getValue()).intValue() < 0 || ((Integer)position
/* 426 */         .getValue()).intValue() >= columnSize) {
/*     */         continue;
/*     */       }
/*     */       
/* 430 */       TableColumn<ObservableList<SpreadsheetCell>, ?> column = getTableView().getVisibleLeafColumn(((Integer)position.getValue()).intValue());
/*     */       
/* 432 */       pos = getVisibleCell(((Integer)position.getKey()).intValue(), column);
/*     */ 
/*     */       
/* 435 */       SpreadsheetCell cell = (SpreadsheetCell)column.getCellData(pos.getRow());
/* 436 */       if (cell != null) {
/* 437 */         int rowSpan = this.spreadsheetView.getRowSpan(cell, pos.getRow());
/* 438 */         int currentRow = pos.getRow();
/* 439 */         for (int i = pos.getRow(); i < rowSpan + currentRow; i++) {
/* 440 */           selectedColumns.add(Integer.valueOf(i));
/* 441 */           for (int j = this.spreadsheetView.getViewColumn(cell.getColumn()); j < this.spreadsheetView.getColumnSpan(cell) + this.spreadsheetView.getViewColumn(cell.getColumn()); j++) {
/* 442 */             selectedRows.add(Integer.valueOf(j));
/* 443 */             pos = new TablePosition(getTableView(), i, getTableView().getVisibleLeafColumn(j));
/* 444 */             newList.add(pos);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 449 */     this.selectedCellsMap.setAll(newList);
/*     */     
/* 451 */     final TablePosition<ObservableList<SpreadsheetCell>, ?> finalPos = pos;
/*     */     
/* 453 */     GridViewSkin skin = getSpreadsheetViewSkin();
/*     */     
/* 455 */     if (skin == null) {
/* 456 */       this.cellsView.skinProperty().addListener(new InvalidationListener()
/*     */           {
/*     */             public void invalidated(Observable observable)
/*     */             {
/* 460 */               TableViewSpanSelectionModel.this.cellsView.skinProperty().removeListener(this);
/* 461 */               GridViewSkin skin = TableViewSpanSelectionModel.this.getSpreadsheetViewSkin();
/* 462 */               if (skin != null) {
/* 463 */                 TableViewSpanSelectionModel.this.updateSelectedVisuals(skin, finalPos, selectedRows, selectedColumns);
/*     */               }
/*     */             }
/*     */           });
/*     */     } else {
/* 468 */       updateSelectedVisuals(skin, pos, selectedRows, selectedColumns);
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
/*     */   private void updateSelectedVisuals(GridViewSkin skin, TablePosition pos, HashSet<Integer> selectedRows, HashSet<Integer> selectedColumns) {
/* 482 */     if (skin != null) {
/* 483 */       skin.getSelectedRows().addAll(selectedColumns);
/* 484 */       skin.getSelectedColumns().addAll(selectedRows);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 492 */     if (pos != null) {
/* 493 */       (getCellsViewSkin()).lastRowLayout.set(true);
/* 494 */       (getCellsViewSkin()).lastRowLayout.addListener(new InvalidationListener()
/*     */           {
/*     */             public void invalidated(Observable observable)
/*     */             {
/* 498 */               TableViewSpanSelectionModel.this.handleSelectedCellsListChangeEvent((ListChangeListener.Change<? extends TablePosition<ObservableList<SpreadsheetCell>, ?>>)new NonIterableChange.SimpleAddChange(0, TableViewSpanSelectionModel.this
/* 499 */                     .selectedCellsMap.size(), (ObservableList)TableViewSpanSelectionModel.this.selectedCellsSeq));
/* 500 */               (TableViewSpanSelectionModel.this.getCellsViewSkin()).lastRowLayout.removeListener(this);
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void selectRange(int minRow, TableColumnBase<ObservableList<SpreadsheetCell>, ?> minColumn, int maxRow, TableColumnBase<ObservableList<SpreadsheetCell>, ?> maxColumn) {
/* 510 */     if (getSelectionMode() == SelectionMode.SINGLE) {
/* 511 */       quietClearSelection();
/* 512 */       select(maxRow, maxColumn);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 517 */     this.makeAtomic = true;
/*     */     
/* 519 */     int itemCount = getItemCount();
/*     */     
/* 521 */     int minColumnIndex = getTableView().getVisibleLeafIndex((TableColumn)minColumn);
/*     */     
/* 523 */     int maxColumnIndex = getTableView().getVisibleLeafIndex((TableColumn)maxColumn);
/*     */     
/* 525 */     int _minColumnIndex = Math.min(minColumnIndex, maxColumnIndex);
/* 526 */     int _maxColumnIndex = Math.max(minColumnIndex, maxColumnIndex);
/*     */     
/* 528 */     int _minRow = Math.min(minRow, maxRow);
/* 529 */     int _maxRow = Math.max(minRow, maxRow);
/*     */     
/* 531 */     HashSet<Integer> selectedRows = new HashSet<>();
/* 532 */     HashSet<Integer> selectedColumns = new HashSet<>();
/*     */     
/* 534 */     for (int _row = _minRow; _row <= _maxRow; _row++) {
/* 535 */       for (int _col = _minColumnIndex; _col <= _maxColumnIndex; _col++) {
/*     */ 
/*     */         
/* 538 */         if (_row >= 0 && _row < itemCount) {
/*     */ 
/*     */ 
/*     */           
/* 542 */           TableColumn<ObservableList<SpreadsheetCell>, ?> column = getTableView().getVisibleLeafColumn(_col);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 548 */           if (column != null) {
/*     */ 
/*     */ 
/*     */             
/* 552 */             TablePosition<ObservableList<SpreadsheetCell>, ?> pos = getVisibleCell(_row, column);
/*     */ 
/*     */ 
/*     */             
/* 556 */             SpreadsheetCell cell = (SpreadsheetCell)column.getCellData(pos.getRow());
/* 557 */             int rowSpan = this.spreadsheetView.getRowSpan(cell, pos.getRow());
/* 558 */             int currentRow = pos.getRow();
/* 559 */             for (int i = currentRow; i < rowSpan + currentRow; i++) {
/* 560 */               selectedRows.add(Integer.valueOf(i));
/* 561 */               for (int j = this.spreadsheetView.getViewColumn(cell.getColumn()); j < this.spreadsheetView.getColumnSpan(cell) + this.spreadsheetView.getViewColumn(cell.getColumn()); j++) {
/* 562 */                 selectedColumns.add(Integer.valueOf(j));
/* 563 */                 pos = new TablePosition(getTableView(), i, getTableView().getVisibleLeafColumn(j));
/* 564 */                 this.selectedCellsMap.add(pos);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 572 */     this.makeAtomic = false;
/*     */ 
/*     */     
/* 575 */     getSpreadsheetViewSkin().getSelectedRows().addAll(selectedRows);
/* 576 */     getSpreadsheetViewSkin().getSelectedColumns().addAll(selectedColumns);
/*     */ 
/*     */     
/* 579 */     setSelectedIndex(maxRow);
/* 580 */     setSelectedItem(getModelItem(maxRow));
/* 581 */     if (getTableView().getFocusModel() == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 586 */     getTableView().getFocusModel().focus(maxRow, (TableColumn)maxColumn);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 594 */     int startChangeIndex = this.selectedCellsMap.indexOf(new TablePosition(getTableView(), minRow, (TableColumn)minColumn));
/*     */     
/* 596 */     int endChangeIndex = this.selectedCellsMap.getSelectedCells().size() - 1;
/*     */ 
/*     */     
/* 599 */     if (startChangeIndex > -1 && endChangeIndex > -1) {
/* 600 */       int startIndex = Math.min(startChangeIndex, endChangeIndex);
/* 601 */       int endIndex = Math.max(startChangeIndex, endChangeIndex);
/* 602 */       handleSelectedCellsListChangeEvent((ListChangeListener.Change<? extends TablePosition<ObservableList<SpreadsheetCell>, ?>>)new NonIterableChange.SimpleAddChange(startIndex, endIndex + 1, (ObservableList)this.selectedCellsSeq));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void selectAll() {
/* 609 */     if (getSelectionMode() == SelectionMode.SINGLE) {
/*     */       return;
/*     */     }
/*     */     
/* 613 */     quietClearSelection();
/*     */     
/* 615 */     List<TablePosition<ObservableList<SpreadsheetCell>, ?>> indices = new ArrayList<>();
/*     */     
/* 617 */     TablePosition<ObservableList<SpreadsheetCell>, ?> tp = null;
/*     */     
/* 619 */     for (int col = 0; col < getTableView().getVisibleLeafColumns().size(); col++) {
/* 620 */       TableColumn<ObservableList<SpreadsheetCell>, ?> column = (TableColumn<ObservableList<SpreadsheetCell>, ?>)getTableView().getVisibleLeafColumns().get(col);
/* 621 */       for (int j = 0; j < getItemCount(); j++) {
/* 622 */         tp = new TablePosition(getTableView(), j, column);
/* 623 */         indices.add(tp);
/*     */       } 
/*     */     } 
/* 626 */     this.selectedCellsMap.setAll(indices);
/*     */ 
/*     */     
/* 629 */     ArrayList<Integer> selectedColumns = new ArrayList<>();
/* 630 */     for (int i = 0; i < this.spreadsheetView.getGrid().getColumnCount(); i++) {
/* 631 */       selectedColumns.add(Integer.valueOf(i));
/*     */     }
/*     */     
/* 634 */     ArrayList<Integer> selectedRows = new ArrayList<>();
/* 635 */     for (int row = 0; row < getItemCount(); row++) {
/* 636 */       selectedRows.add(Integer.valueOf(row));
/*     */     }
/* 638 */     getSpreadsheetViewSkin().getSelectedRows().addAll(selectedRows);
/* 639 */     getSpreadsheetViewSkin().getSelectedColumns().addAll(selectedColumns);
/*     */     
/* 641 */     if (tp != null) {
/* 642 */       select(tp.getRow(), tp.getTableColumn());
/*     */ 
/*     */       
/* 645 */       getTableView().getFocusModel().focus(0, (TableColumn)getTableView().getColumns().get(0));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSelected(int row, TableColumn<ObservableList<SpreadsheetCell>, ?> column) {
/* 655 */     if (column == null || row < 0) {
/* 656 */       return false;
/*     */     }
/*     */     
/* 659 */     int columnIndex = getTableView().getVisibleLeafIndex(column);
/*     */     
/* 661 */     if (getCellsViewSkin().getCellsSize() != 0) {
/* 662 */       TablePosition<ObservableList<SpreadsheetCell>, ?> posFinal = getVisibleCell(row, column);
/* 663 */       return this.selectedCellsMap.isSelected(posFinal.getRow(), posFinal.getColumn());
/*     */     } 
/* 665 */     return this.selectedCellsMap.isSelected(row, columnIndex);
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
/*     */   public List<TablePosition<ObservableList<SpreadsheetCell>, ?>> isSelectedRange(int row, TableColumn<ObservableList<SpreadsheetCell>, ?> column, int col) {
/* 680 */     if (col < 0 || row < 0) {
/* 681 */       return null;
/*     */     }
/*     */     
/* 684 */     SpreadsheetCell cell = (SpreadsheetCell)column.getCellData(row);
/*     */     
/* 686 */     int infRow = row;
/* 687 */     int supRow = infRow + this.spreadsheetView.getRowSpan(cell, row);
/*     */     
/* 689 */     int infCol = this.spreadsheetView.getViewColumn(cell.getColumn());
/* 690 */     int supCol = infCol + this.spreadsheetView.getColumnSpan(cell);
/* 691 */     List<TablePosition<ObservableList<SpreadsheetCell>, ?>> selectedCells = new ArrayList<>();
/* 692 */     for (TablePosition<ObservableList<SpreadsheetCell>, ?> tp : getSelectedCells()) {
/* 693 */       if (tp.getRow() >= infRow && tp.getRow() < supRow && tp.getColumn() >= infCol && tp
/* 694 */         .getColumn() < supCol) {
/* 695 */         selectedCells.add(tp);
/*     */       }
/*     */     } 
/* 698 */     return selectedCells.isEmpty() ? null : selectedCells;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addSelectedRowsAndColumns(TablePosition<?, ?> pos) {
/* 707 */     GridViewSkin skin = getSpreadsheetViewSkin();
/* 708 */     if (skin == null) {
/*     */       return;
/*     */     }
/* 711 */     SpreadsheetCell cell = (SpreadsheetCell)pos.getTableColumn().getCellData(pos.getRow());
/* 712 */     int rowSpan = this.spreadsheetView.getRowSpan(cell, pos.getRow());
/*     */     
/* 714 */     for (int i = pos.getRow(); i < rowSpan + pos.getRow(); i++) {
/* 715 */       skin.getSelectedRows().add(Integer.valueOf(i));
/* 716 */       for (int j = this.spreadsheetView.getViewColumn(cell.getColumn()); j < this.spreadsheetView.getColumnSpan(cell) + this.spreadsheetView.getViewColumn(cell.getColumn()); j++) {
/* 717 */         skin.getSelectedColumns().add(Integer.valueOf(j));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void removeSelectedRowsAndColumns(TablePosition<?, ?> pos) {
/* 723 */     SpreadsheetCell cell = (SpreadsheetCell)pos.getTableColumn().getCellData(pos.getRow());
/* 724 */     int rowSpan = this.spreadsheetView.getRowSpan(cell, pos.getRow());
/*     */     
/* 726 */     for (int i = pos.getRow(); i < rowSpan + pos.getRow(); i++) {
/* 727 */       getSpreadsheetViewSkin().getSelectedRows().remove(Integer.valueOf(i));
/* 728 */       for (int j = this.spreadsheetView.getViewColumn(cell.getColumn()); j < this.spreadsheetView.getColumnSpan(cell) + this.spreadsheetView.getViewColumn(cell.getColumn()); j++) {
/* 729 */         getSpreadsheetViewSkin().getSelectedColumns().remove(Integer.valueOf(j));
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
/*     */   
/*     */   public void clearAndSelect(int row, TableColumn<ObservableList<SpreadsheetCell>, ?> column) {
/* 746 */     this.makeAtomic = true;
/*     */ 
/*     */ 
/*     */     
/* 750 */     List<TablePosition<ObservableList<SpreadsheetCell>, ?>> previousSelection = new ArrayList<>((Collection<? extends TablePosition<ObservableList<SpreadsheetCell>, ?>>)this.selectedCellsMap.getSelectedCells());
/*     */ 
/*     */     
/* 753 */     clearSelection();
/*     */ 
/*     */     
/* 756 */     select(row, column);
/*     */     
/* 758 */     this.makeAtomic = false;
/*     */ 
/*     */ 
/*     */     
/* 762 */     if (this.old != null && this.old.getColumn() >= 0) {
/* 763 */       TableColumn<ObservableList<SpreadsheetCell>, ?> columnFinal = getTableView().getVisibleLeafColumn(this.old
/* 764 */           .getColumn());
/* 765 */       int changeIndex = this.selectedCellsSeq.indexOf(new TablePosition(getTableView(), this.old.getRow(), columnFinal));
/*     */       
/* 767 */       NonIterableChange.GenericAddRemoveChange<TablePosition<ObservableList<SpreadsheetCell>, ?>> change = new NonIterableChange.GenericAddRemoveChange(changeIndex, changeIndex + 1, previousSelection, (ObservableList)this.selectedCellsSeq);
/*     */       
/* 769 */       handleSelectedCellsListChangeEvent((ListChangeListener.Change<? extends TablePosition<ObservableList<SpreadsheetCell>, ?>>)change);
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
/*     */   public ObservableList<TablePosition> getSelectedCells() {
/* 781 */     return (ObservableList)this.selectedCellsSeq;
/*     */   }
/*     */ 
/*     */   
/*     */   public void selectAboveCell() {
/* 786 */     TablePosition<ObservableList<SpreadsheetCell>, ?> pos = getFocusedCell();
/* 787 */     if (pos.getRow() == -1) {
/* 788 */       select(getItemCount() - 1);
/* 789 */     } else if (pos.getRow() > 0) {
/* 790 */       select(pos.getRow() - 1, pos.getTableColumn());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void selectBelowCell() {
/* 797 */     TablePosition<ObservableList<SpreadsheetCell>, ?> pos = getFocusedCell();
/*     */     
/* 799 */     if (pos.getRow() == -1) {
/* 800 */       select(0);
/* 801 */     } else if (pos.getRow() < getItemCount() - 1) {
/* 802 */       select(pos.getRow() + 1, pos.getTableColumn());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void selectLeftCell() {
/* 809 */     if (!isCellSelectionEnabled()) {
/*     */       return;
/*     */     }
/*     */     
/* 813 */     TablePosition<ObservableList<SpreadsheetCell>, ?> pos = getFocusedCell();
/* 814 */     if (pos.getColumn() - 1 >= 0) {
/* 815 */       select(pos.getRow(), getTableColumn(pos.getTableColumn(), -1));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void selectRightCell() {
/* 822 */     if (!isCellSelectionEnabled()) {
/*     */       return;
/*     */     }
/*     */     
/* 826 */     TablePosition<ObservableList<SpreadsheetCell>, ?> pos = getFocusedCell();
/* 827 */     if (pos.getColumn() + 1 < getTableView().getVisibleLeafColumns().size()) {
/* 828 */       select(pos.getRow(), getTableColumn(pos.getTableColumn(), 1));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearSelection() {
/* 835 */     if (!this.makeAtomic) {
/* 836 */       setSelectedIndex(-1);
/* 837 */       setSelectedItem(getModelItem(-1));
/* 838 */       focus(-1);
/*     */     } 
/* 840 */     quietClearSelection();
/*     */   }
/*     */   
/*     */   private void quietClearSelection() {
/* 844 */     this.selectedCellsMap.clear();
/* 845 */     GridViewSkin skin = getSpreadsheetViewSkin();
/* 846 */     if (skin != null) {
/* 847 */       skin.getSelectedRows().clear();
/* 848 */       skin.getSelectedColumns().clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private TablePosition<ObservableList<SpreadsheetCell>, ?> getFocusedCell() {
/* 854 */     if (getTableView().getFocusModel() == null) {
/* 855 */       return new TablePosition(getTableView(), -1, null);
/*     */     }
/* 857 */     return this.cellsView.getFocusModel().getFocusedCell();
/*     */   }
/*     */ 
/*     */   
/*     */   private TableColumn<ObservableList<SpreadsheetCell>, ?> getTableColumn(TableColumn<ObservableList<SpreadsheetCell>, ?> column, int offset) {
/* 862 */     int columnIndex = getTableView().getVisibleLeafIndex(column);
/* 863 */     int newColumnIndex = columnIndex + offset;
/* 864 */     return getTableView().getVisibleLeafColumn(newColumnIndex);
/*     */   }
/*     */   
/*     */   private GridViewSkin getSpreadsheetViewSkin() {
/* 868 */     return getCellsViewSkin();
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
/*     */   private TablePosition<ObservableList<SpreadsheetCell>, ?> getVisibleCell(int row, TableColumn<ObservableList<SpreadsheetCell>, ?> column) {
/* 883 */     int modelColumn = this.cellsView.getColumns().indexOf(column);
/* 884 */     SpreadsheetView.SpanType spanType = this.spreadsheetView.getSpanType(row, modelColumn);
/* 885 */     switch (spanType) {
/*     */       case NORMAL_CELL:
/*     */       case ROW_VISIBLE:
/* 888 */         return new TablePosition(this.cellsView, row, column);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 893 */     SpreadsheetCell cell = (SpreadsheetCell)((ObservableList)this.cellsView.getItems().get(row)).get(modelColumn);
/* 894 */     int firstRow = (getCellsViewSkin() == null) ? -1 : getCellsViewSkin().getFirstRow(cell, row);
/* 895 */     if (getCellsViewSkin() == null || (getCellsViewSkin().getCellsSize() != 0 && getNonFixedRow(0).getIndex() <= firstRow)) {
/* 896 */       return new TablePosition(this.cellsView, firstRow, this.cellsView
/* 897 */           .getVisibleLeafColumn(this.spreadsheetView.getViewColumn(cell.getColumn())));
/*     */     }
/* 899 */     GridRow gridRow = getNonFixedRow(0);
/* 900 */     return new TablePosition(this.cellsView, (gridRow == null) ? row : gridRow.getIndex(), this.cellsView
/* 901 */         .getVisibleLeafColumn(this.spreadsheetView.getViewColumn(cell.getColumn())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final GridViewSkin getCellsViewSkin() {
/* 910 */     return (GridViewSkin)this.cellsView.getSkin();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private GridRow getNonFixedRow(int index) {
/* 920 */     return getCellsViewSkin().getRow(index);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\spreadsheet\TableViewSpanSelectionModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */