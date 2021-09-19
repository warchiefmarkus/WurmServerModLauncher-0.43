/*     */ package impl.org.controlsfx.spreadsheet;
/*     */ 
/*     */ import com.sun.javafx.scene.control.skin.NestedTableColumnHeader;
/*     */ import com.sun.javafx.scene.control.skin.TableColumnHeader;
/*     */ import com.sun.javafx.scene.control.skin.TableHeaderRow;
/*     */ import java.util.BitSet;
/*     */ import java.util.Iterator;
/*     */ import javafx.application.Platform;
/*     */ import javafx.beans.InvalidationListener;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.beans.value.ChangeListener;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.collections.ListChangeListener;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.event.EventHandler;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.TableColumn;
/*     */ import javafx.scene.control.TableColumnBase;
/*     */ import javafx.scene.control.TablePosition;
/*     */ import javafx.scene.control.TableView;
/*     */ import javafx.scene.input.MouseEvent;
/*     */ import javafx.scene.shape.Rectangle;
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
/*     */ public class HorizontalHeader
/*     */   extends TableHeaderRow
/*     */ {
/*     */   final GridViewSkin gridViewSkin;
/*     */   private boolean working = true;
/*  65 */   protected BitSet selectedColumns = new BitSet();
/*     */   private final InvalidationListener verticalHeaderListener;
/*     */   private final ChangeListener<Boolean> horizontalHeaderVisibilityListener;
/*     */   private final ListChangeListener<SpreadsheetColumn> fixedColumnsListener;
/*     */   private final InvalidationListener selectionListener;
/*     */   
/*     */   public HorizontalHeader(GridViewSkin skin)
/*     */   {
/*  73 */     super(skin);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 229 */     this.verticalHeaderListener = new InvalidationListener()
/*     */       {
/*     */         public void invalidated(Observable observable)
/*     */         {
/* 233 */           HorizontalHeader.this.updateTableWidth();
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 241 */     this.horizontalHeaderVisibilityListener = new ChangeListener<Boolean>()
/*     */       {
/*     */         public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
/* 244 */           HorizontalHeader.this.updateHorizontalHeaderVisibility(arg2.booleanValue());
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 252 */     this.fixedColumnsListener = new ListChangeListener<SpreadsheetColumn>()
/*     */       {
/*     */         public void onChanged(ListChangeListener.Change<? extends SpreadsheetColumn> change)
/*     */         {
/* 256 */           while (change.next()) {
/*     */             
/* 258 */             for (SpreadsheetColumn remitem : change.getRemoved()) {
/* 259 */               HorizontalHeader.this.unfixColumn(remitem);
/*     */             }
/*     */             
/* 262 */             for (SpreadsheetColumn additem : change.getAddedSubList()) {
/* 263 */               HorizontalHeader.this.fixColumn(additem);
/*     */             }
/*     */           } 
/* 266 */           HorizontalHeader.this.updateHighlightSelection();
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 313 */     this.selectionListener = new InvalidationListener()
/*     */       {
/*     */         public void invalidated(Observable valueModel) {
/* 316 */           HorizontalHeader.this.updateHighlightSelection();
/*     */         }
/*     */       }; this.gridViewSkin = skin; }
/*     */   public void init() { SpreadsheetView spv = this.gridViewSkin.handle.getView(); updateHorizontalHeaderVisibility(spv.isShowColumnHeader()); spv.showRowHeaderProperty().addListener(this.verticalHeaderListener); this.gridViewSkin.verticalHeader.verticalHeaderWidthProperty().addListener(this.verticalHeaderListener); spv.showColumnHeaderProperty().addListener(this.horizontalHeaderVisibilityListener); this.gridViewSkin.getSelectedColumns().addListener(this.selectionListener); spv.getFixedColumns().addListener(this.fixedColumnsListener); Platform.runLater(() -> { for (SpreadsheetColumn column : spv.getFixedColumns())
/*     */             fixColumn(column);  requestLayout(); installHeaderMouseEvent();
/*     */         }); getRootHeader().getColumnHeaders().addListener(o -> {
/*     */           for (SpreadsheetColumn fixItem : spv.getFixedColumns())
/*     */             fixColumn(fixItem);  updateHighlightSelection(); installHeaderMouseEvent();
/* 324 */         }); } public HorizontalHeaderColumn getRootHeader() { return (HorizontalHeaderColumn)super.getRootHeader(); } void clearSelectedColumns() { this.selectedColumns.clear(); } private void updateHighlightSelection() { for (TableColumnHeader i : getRootHeader().getColumnHeaders()) {
/* 325 */       i.getStyleClass().removeAll((Object[])new String[] { "selected" });
/*     */     } 
/*     */     
/* 328 */     ObservableList<Integer> observableList = this.gridViewSkin.getSelectedColumns();
/* 329 */     for (Integer i : observableList)
/* 330 */     { if (getRootHeader().getColumnHeaders().size() > i.intValue())
/* 331 */         ((TableColumnHeader)getRootHeader().getColumnHeaders().get(i.intValue())).getStyleClass()
/* 332 */           .addAll((Object[])new String[] { "selected" });  }  }
/*     */   protected void updateTableWidth() { super.updateTableWidth(); double padding = 0.0D; if (this.working && this.gridViewSkin != null && this.gridViewSkin.spreadsheetView != null && this.gridViewSkin.spreadsheetView.showRowHeaderProperty().get() && this.gridViewSkin.verticalHeader != null) padding += this.gridViewSkin.verticalHeader.getVerticalHeaderWidth();  Rectangle clip = (Rectangle)getClip(); clip.setWidth((clip.getWidth() == 0.0D) ? 0.0D : (clip.getWidth() - padding)); }
/*     */   protected void updateScrollX() { super.updateScrollX(); this.gridViewSkin.horizontalPickers.updateScrollX(); if (this.working) { requestLayout(); getRootHeader().layoutFixedColumns(); }  }
/*     */   protected NestedTableColumnHeader createRootHeader() { return new HorizontalHeaderColumn(getTableSkin(), null); }
/*     */   private void installHeaderMouseEvent() { for (Iterator<TableColumnHeader> iterator = getRootHeader().getColumnHeaders().iterator(); iterator.hasNext(); ) { TableColumnHeader columnHeader = iterator.next(); EventHandler<MouseEvent> mouseEventHandler = mouseEvent -> { if (mouseEvent.isPrimaryButtonDown())
/*     */             headerClicked((TableColumn)columnHeader.getTableColumn(), mouseEvent);  }; ((Node)columnHeader.getChildrenUnmodifiable().get(0)).setOnMousePressed(mouseEventHandler); }  }
/*     */   private void headerClicked(TableColumn column, MouseEvent event) { TableView.TableViewSelectionModel<ObservableList<SpreadsheetCell>> sm = this.gridViewSkin.handle.getGridView().getSelectionModel(); int lastRow = this.gridViewSkin.getItemCount() - 1; int indexColumn = column.getTableView().getColumns().indexOf(column); TablePosition focusedPosition = sm.getTableView().getFocusModel().getFocusedCell(); if (event.isShortcutDown()) { BitSet tempSet = (BitSet)this.selectedColumns.clone(); sm.selectRange(0, (TableColumnBase)column, lastRow, (TableColumnBase)column); this.selectedColumns.or(tempSet); this.selectedColumns.set(indexColumn); } else if (event.isShiftDown() && focusedPosition != null && focusedPosition.getTableColumn() != null) { sm.clearSelection(); sm.selectRange(0, (TableColumnBase)column, lastRow, (TableColumnBase)focusedPosition.getTableColumn()); sm.getTableView().getFocusModel().focus(0, focusedPosition.getTableColumn()); int min = Math.min(indexColumn, focusedPosition.getColumn()); int max = Math.max(indexColumn, focusedPosition.getColumn()); this.selectedColumns.set(min, max + 1); } else { sm.clearSelection(); sm.selectRange(0, (TableColumnBase)column, lastRow, (TableColumnBase)column); sm.getTableView().getFocusModel().focus(0, column); this.selectedColumns.set(indexColumn); }  }
/* 339 */   private void fixColumn(SpreadsheetColumn column) { addStyleHeader(Integer.valueOf(this.gridViewSkin.spreadsheetView.getViewColumn(this.gridViewSkin.spreadsheetView.getColumns().indexOf(column)))); } private void updateHorizontalHeaderVisibility(boolean visible) { this.working = visible;
/* 340 */     setManaged(this.working);
/* 341 */     if (!visible) {
/* 342 */       getStyleClass().add("invisible");
/*     */     } else {
/* 344 */       getStyleClass().remove("invisible");
/* 345 */       requestLayout();
/* 346 */       getRootHeader().layoutFixedColumns();
/* 347 */       updateHighlightSelection();
/*     */     }  }
/*     */    private void unfixColumn(SpreadsheetColumn column) {
/*     */     removeStyleHeader(Integer.valueOf(this.gridViewSkin.spreadsheetView.getViewColumn(this.gridViewSkin.spreadsheetView.getColumns().indexOf(column))));
/*     */   }
/*     */   private void removeStyleHeader(Integer i) {
/*     */     if (getRootHeader().getColumnHeaders().size() > i.intValue())
/*     */       ((TableColumnHeader)getRootHeader().getColumnHeaders().get(i.intValue())).getStyleClass().removeAll((Object[])new String[] { "fixed" }); 
/*     */   }
/*     */   private void addStyleHeader(Integer i) {
/*     */     if (getRootHeader().getColumnHeaders().size() > i.intValue())
/*     */       ((TableColumnHeader)getRootHeader().getColumnHeaders().get(i.intValue())).getStyleClass().addAll((Object[])new String[] { "fixed" }); 
/*     */   }
/*     */   protected double computePrefHeight(double width) {
/* 361 */     if (!this.gridViewSkin.handle.getView().isShowColumnHeader()) {
/* 362 */       return 0.0D;
/*     */     }
/*     */ 
/*     */     
/* 366 */     double headerPrefHeight = getRootHeader().prefHeight(width);
/* 367 */     headerPrefHeight = (headerPrefHeight == 0.0D) ? 24.0D : headerPrefHeight;
/* 368 */     double height = snappedTopInset() + headerPrefHeight + snappedBottomInset();
/* 369 */     height = (height < GridViewSkin.DEFAULT_CELL_HEIGHT) ? GridViewSkin.DEFAULT_CELL_HEIGHT : height;
/* 370 */     return height;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\spreadsheet\HorizontalHeader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */