/*     */ package impl.org.controlsfx.spreadsheet;
/*     */ 
/*     */ import com.sun.javafx.scene.control.skin.NestedTableColumnHeader;
/*     */ import com.sun.javafx.scene.control.skin.TableColumnHeader;
/*     */ import com.sun.javafx.scene.control.skin.TableViewSkinBase;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.event.Event;
/*     */ import javafx.event.EventHandler;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.TableColumn;
/*     */ import javafx.scene.control.TableColumnBase;
/*     */ import javafx.scene.input.MouseEvent;
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
/*     */ public class HorizontalHeaderColumn
/*     */   extends NestedTableColumnHeader
/*     */ {
/*  46 */   int lastColumnResized = -1;
/*     */ 
/*     */   
/*     */   public HorizontalHeaderColumn(TableViewSkinBase<?, ?, ?, ?, ?, ?> skin, TableColumnBase<?, ?> tc) {
/*  50 */     super(skin, tc);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  55 */     widthProperty().addListener(observable -> {
/*     */           ((GridViewSkin)skin).hBarValue.clear();
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           ((GridViewSkin)skin).rectangleSelection.updateRectangle();
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  67 */     this.columnReorderLine.layoutXProperty().addListener((observable, oldValue, newValue) -> {
/*     */           HorizontalHeader headerRow = (HorizontalHeader)((GridViewSkin)skin).getTableHeaderRow();
/*     */           GridViewSkin mySkin = (GridViewSkin)skin;
/*     */           if (newValue.intValue() == 0 && this.lastColumnResized >= 0 && headerRow.selectedColumns.get(this.lastColumnResized)) {
/*     */             double width1 = ((TableColumn)mySkin.getColumns().get(this.lastColumnResized)).getWidth();
/*     */             int i;
/*     */             for (i = headerRow.selectedColumns.nextSetBit(0); i >= 0; i = headerRow.selectedColumns.nextSetBit(i + 1)) {
/*     */               ((TableColumn)mySkin.getColumns().get(i)).setPrefWidth(width1);
/*     */             }
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected TableColumnHeader createTableColumnHeader(final TableColumnBase<?, ?> col) {
/*  83 */     final TableViewSkinBase<?, ?, ?, ?, ?, TableColumnBase<?, ?>> tableViewSkin = getTableViewSkin();
/*  84 */     if (col.getColumns().isEmpty()) {
/*  85 */       TableColumnHeader columnHeader = new TableColumnHeader(tableViewSkin, col);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  90 */       columnHeader.setOnMousePressed(new EventHandler<MouseEvent>()
/*     */           {
/*     */             public void handle(MouseEvent mouseEvent) {
/*  93 */               if (mouseEvent.getClickCount() == 2 && mouseEvent.isPrimaryButtonDown()) {
/*  94 */                 ((GridViewSkin)tableViewSkin).resize(col, -1);
/*     */               }
/*     */             }
/*     */           });
/*  98 */       return columnHeader;
/*     */     } 
/* 100 */     return (TableColumnHeader)new HorizontalHeaderColumn(getTableViewSkin(), col);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void layoutChildren() {
/* 106 */     super.layoutChildren();
/* 107 */     layoutFixedColumns();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void layoutFixedColumns() {
/* 114 */     SpreadsheetHandle handle = ((GridViewSkin)getTableViewSkin()).handle;
/* 115 */     SpreadsheetView spreadsheetView = handle.getView();
/* 116 */     if (handle.getCellsViewSkin() == null || getChildren().isEmpty()) {
/*     */       return;
/*     */     }
/* 119 */     double hbarValue = handle.getCellsViewSkin().getHBar().getValue();
/*     */     
/* 121 */     int labelHeight = (int)((Node)getChildren().get(0)).prefHeight(-1.0D);
/* 122 */     double fixedColumnWidth = 0.0D;
/* 123 */     double x = snappedLeftInset();
/* 124 */     int max = getColumnHeaders().size();
/* 125 */     max = (max > handle.getGridView().getVisibleLeafColumns().size()) ? handle.getGridView().getVisibleLeafColumns().size() : max;
/* 126 */     max = (max > spreadsheetView.getColumns().size()) ? spreadsheetView.getColumns().size() : max;
/* 127 */     for (int j = 0; j < max; j++) {
/* 128 */       TableColumnHeader n = (TableColumnHeader)getColumnHeaders().get(j);
/* 129 */       double prefWidth = snapSize(n.prefWidth(-1.0D));
/* 130 */       n.setPrefHeight(24.0D);
/*     */       
/* 132 */       if (((SpreadsheetColumn)spreadsheetView.getColumns().get(spreadsheetView.getModelColumn(j))).isFixed()) {
/* 133 */         double tableCellX = 0.0D;
/*     */         
/* 135 */         if (hbarValue + fixedColumnWidth > x) {
/*     */           
/* 137 */           tableCellX = Math.abs(hbarValue - x + fixedColumnWidth);
/*     */           
/* 139 */           n.toFront();
/* 140 */           fixedColumnWidth += prefWidth;
/*     */         } 
/* 142 */         n.relocate(x + tableCellX, labelHeight + snappedTopInset());
/*     */       } 
/*     */       
/* 145 */       x += prefWidth;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\spreadsheet\HorizontalHeaderColumn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */