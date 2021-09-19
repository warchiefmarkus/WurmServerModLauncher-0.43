/*     */ package org.controlsfx.control.spreadsheet;
/*     */ 
/*     */ import impl.org.controlsfx.i18n.Localization;
/*     */ import impl.org.controlsfx.spreadsheet.CellView;
/*     */ import impl.org.controlsfx.spreadsheet.CellViewSkin;
/*     */ import javafx.beans.InvalidationListener;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.beans.property.DoubleProperty;
/*     */ import javafx.beans.property.ObjectProperty;
/*     */ import javafx.beans.property.ReadOnlyDoubleProperty;
/*     */ import javafx.beans.property.SimpleObjectProperty;
/*     */ import javafx.beans.value.ChangeListener;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.event.ActionEvent;
/*     */ import javafx.event.Event;
/*     */ import javafx.event.EventHandler;
/*     */ import javafx.event.EventTarget;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.ContextMenu;
/*     */ import javafx.scene.control.MenuItem;
/*     */ import javafx.scene.control.TableColumn;
/*     */ import javafx.scene.control.TableColumnBase;
/*     */ import javafx.scene.image.Image;
/*     */ import javafx.scene.image.ImageView;
/*     */ import javafx.stage.WindowEvent;
/*     */ import org.controlsfx.tools.Utils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SpreadsheetColumn
/*     */ {
/*     */   private final SpreadsheetView spreadsheetView;
/*     */   final TableColumn<ObservableList<SpreadsheetCell>, SpreadsheetCell> column;
/*     */   private final boolean canFix;
/*     */   private final Integer indexColumn;
/*     */   private MenuItem fixItem;
/* 101 */   private final ObjectProperty<Filter> filterProperty = (ObjectProperty<Filter>)new SimpleObjectProperty();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   SpreadsheetColumn(final TableColumn<ObservableList<SpreadsheetCell>, SpreadsheetCell> column, final SpreadsheetView spreadsheetView, final Integer indexColumn, final Grid grid) {
/* 115 */     this.spreadsheetView = spreadsheetView;
/* 116 */     this.column = column;
/* 117 */     column.setMinWidth(0.0D);
/* 118 */     this.indexColumn = indexColumn;
/* 119 */     this.canFix = initCanFix(grid);
/*     */ 
/*     */     
/* 122 */     CellView.getValue(() -> column.setContextMenu(getColumnContextMenu()));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 127 */     spreadsheetView.fixingColumnsAllowedProperty().addListener(new ChangeListener<Boolean>()
/*     */         {
/*     */           public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
/*     */           {
/* 131 */             CellView.getValue(() -> column.setContextMenu(SpreadsheetColumn.this.getColumnContextMenu()));
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 138 */     grid.getColumnHeaders().addListener(new InvalidationListener()
/*     */         {
/*     */           public void invalidated(Observable arg0) {
/* 141 */             ObservableList<String> observableList = spreadsheetView.getGrid().getColumnHeaders();
/* 142 */             if (observableList.size() <= indexColumn.intValue()) {
/* 143 */               SpreadsheetColumn.this.setText(Utils.getExcelLetterFromNumber(indexColumn.intValue()));
/* 144 */             } else if (!((String)observableList.get(indexColumn.intValue())).equals(SpreadsheetColumn.this.getText())) {
/* 145 */               SpreadsheetColumn.this.setText(observableList.get(indexColumn.intValue()));
/*     */             } 
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 151 */     grid.getRows().addListener(new InvalidationListener()
/*     */         {
/*     */           public void invalidated(Observable arg0) {
/* 154 */             SpreadsheetColumn.this.initCanFix(grid);
/*     */           }
/*     */         });
/*     */     
/* 158 */     this.filterProperty.addListener(new ChangeListener<Filter>()
/*     */         {
/*     */           public void changed(ObservableValue<? extends Filter> observable, Filter oldFilter, Filter newFilter) {
/* 161 */             if (newFilter != null) {
/*     */ 
/*     */               
/* 164 */               if (spreadsheetView.getFilteredRow() == -1) {
/* 165 */                 SpreadsheetColumn.this.setFilter(null);
/*     */                 return;
/*     */               } 
/* 168 */               SpreadsheetCell cell = (SpreadsheetCell)((ObservableList)spreadsheetView.getGrid().getRows().get(spreadsheetView.getFilteredRow())).get(indexColumn.intValue());
/* 169 */               if (cell.getColumnSpan() > 1) {
/* 170 */                 SpreadsheetColumn.this.setFilter(null);
/*     */                 return;
/*     */               } 
/*     */             } 
/* 174 */             Event.fireEvent((EventTarget)column, new Event(CellViewSkin.FILTER_EVENT_TYPE));
/*     */           }
/*     */         });
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
/*     */   public boolean isFixed() {
/* 189 */     return this.spreadsheetView.getFixedColumns().contains(this);
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
/*     */   public void setFixed(boolean fixed) {
/* 204 */     if (fixed) {
/* 205 */       this.spreadsheetView.getFixedColumns().add(this);
/*     */     } else {
/* 207 */       this.spreadsheetView.getFixedColumns().removeAll((Object[])new SpreadsheetColumn[] { this });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrefWidth(double width) {
/* 217 */     width = Math.ceil(width);
/* 218 */     if (this.column.getPrefWidth() == width && this.column.getWidth() != width) {
/* 219 */       this.column.impl_setWidth(width);
/*     */     } else {
/* 221 */       this.column.setPrefWidth(width);
/*     */     } 
/* 223 */     this.spreadsheetView.columnWidthSet(this.indexColumn.intValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getWidth() {
/* 232 */     return this.column.getWidth();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ReadOnlyDoubleProperty widthProperty() {
/* 241 */     return this.column.widthProperty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setMinWidth(double value) {
/* 250 */     this.column.setMinWidth(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double getMinWidth() {
/* 259 */     return this.column.getMinWidth();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final DoubleProperty minWidthProperty() {
/* 270 */     return this.column.minWidthProperty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final DoubleProperty maxWidthProperty() {
/* 281 */     return this.column.maxWidthProperty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setMaxWidth(double value) {
/* 290 */     this.column.setMaxWidth(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double getMaxWidth() {
/* 299 */     return this.column.getMaxWidth();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setResizable(boolean b) {
/* 307 */     this.column.setResizable(b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fitColumn() {
/* 315 */     if (this.column.isResizable() && this.spreadsheetView.getCellsViewSkin() != null) {
/* 316 */       this.spreadsheetView.getCellsViewSkin().resize((TableColumnBase)this.column, 100);
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
/*     */   public boolean isColumnFixable() {
/* 331 */     return (this.canFix && this.spreadsheetView.isFixingColumnsAllowed());
/*     */   }
/*     */   
/*     */   public void setFilter(Filter filter) {
/* 335 */     this.filterProperty.setValue(filter);
/*     */   }
/*     */   
/*     */   public Filter getFilter() {
/* 339 */     return (Filter)this.filterProperty.get();
/*     */   }
/*     */   public ObjectProperty filterProperty() {
/* 342 */     return this.filterProperty;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setText(String text) {
/* 349 */     this.column.setText(text);
/*     */   }
/*     */   
/*     */   private String getText() {
/* 353 */     return this.column.getText();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ContextMenu getColumnContextMenu() {
/* 363 */     if (isColumnFixable()) {
/* 364 */       ContextMenu contextMenu = new ContextMenu();
/*     */       
/* 366 */       this.fixItem = new MenuItem(Localization.localize(Localization.asKey("spreadsheet.column.menu.fix")));
/* 367 */       contextMenu.setOnShowing(new EventHandler<WindowEvent>()
/*     */           {
/*     */             public void handle(WindowEvent event)
/*     */             {
/* 371 */               if (!SpreadsheetColumn.this.isFixed()) {
/* 372 */                 SpreadsheetColumn.this.fixItem.setText(Localization.localize(Localization.asKey("spreadsheet.column.menu.fix")));
/*     */               } else {
/* 374 */                 SpreadsheetColumn.this.fixItem.setText(Localization.localize(Localization.asKey("spreadsheet.column.menu.unfix")));
/*     */               } 
/*     */             }
/*     */           });
/* 378 */       this.fixItem.setGraphic((Node)new ImageView(new Image(getClass().getResourceAsStream("pinSpreadsheetView.png"))));
/* 379 */       this.fixItem.setOnAction(new EventHandler<ActionEvent>()
/*     */           {
/*     */             public void handle(ActionEvent arg0) {
/* 382 */               if (!SpreadsheetColumn.this.isFixed()) {
/* 383 */                 SpreadsheetColumn.this.setFixed(true);
/*     */               } else {
/* 385 */                 SpreadsheetColumn.this.setFixed(false);
/*     */               } 
/*     */             }
/*     */           });
/* 389 */       contextMenu.getItems().addAll((Object[])new MenuItem[] { this.fixItem });
/*     */       
/* 391 */       return contextMenu;
/*     */     } 
/* 393 */     return new ContextMenu();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean initCanFix(Grid grid) {
/* 403 */     for (ObservableList<SpreadsheetCell> row : grid.getRows()) {
/* 404 */       int columnSpan = ((SpreadsheetCell)row.get(this.indexColumn.intValue())).getColumnSpan();
/* 405 */       if (columnSpan > 1) {
/* 406 */         return false;
/*     */       }
/*     */     } 
/* 409 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\spreadsheet\SpreadsheetColumn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */