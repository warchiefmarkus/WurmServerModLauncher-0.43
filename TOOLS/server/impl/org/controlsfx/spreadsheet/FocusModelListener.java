/*     */ package impl.org.controlsfx.spreadsheet;
/*     */ 
/*     */ import javafx.application.Platform;
/*     */ import javafx.beans.value.ChangeListener;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.scene.control.TableColumn;
/*     */ import javafx.scene.control.TablePosition;
/*     */ import javafx.scene.control.TableView;
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
/*     */ public class FocusModelListener
/*     */   implements ChangeListener<TablePosition<ObservableList<SpreadsheetCell>, ?>>
/*     */ {
/*     */   private final TableView.TableViewFocusModel<ObservableList<SpreadsheetCell>> tfm;
/*     */   private final SpreadsheetGridView cellsView;
/*     */   private final SpreadsheetView spreadsheetView;
/*     */   
/*     */   public FocusModelListener(SpreadsheetView spreadsheetView, SpreadsheetGridView cellsView) {
/*  56 */     this.tfm = cellsView.getFocusModel();
/*  57 */     this.spreadsheetView = spreadsheetView;
/*  58 */     this.cellsView = cellsView;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void changed(ObservableValue<? extends TablePosition<ObservableList<SpreadsheetCell>, ?>> ov, TablePosition<ObservableList<SpreadsheetCell>, ?> oldPosition, TablePosition<ObservableList<SpreadsheetCell>, ?> newPosition) {
/*  65 */     int columnIndex = -1;
/*  66 */     if (newPosition != null && newPosition.getTableColumn() != null) {
/*  67 */       columnIndex = this.cellsView.getColumns().indexOf(newPosition.getTableColumn());
/*     */     }
/*  69 */     SpreadsheetView.SpanType spanType = this.spreadsheetView.getSpanType(newPosition.getRow(), columnIndex);
/*  70 */     switch (spanType) {
/*     */ 
/*     */ 
/*     */       
/*     */       case ROW_SPAN_INVISIBLE:
/*  75 */         if (!this.spreadsheetView.isPressed() && oldPosition.getColumn() == newPosition.getColumn() && oldPosition.getRow() == newPosition.getRow() - 1) {
/*  76 */           Platform.runLater(() -> this.tfm.focus(getNextRowNumber(oldPosition, this.cellsView, this.spreadsheetView), oldPosition.getTableColumn()));
/*     */ 
/*     */           
/*     */           break;
/*     */         } 
/*     */ 
/*     */         
/*  83 */         Platform.runLater(() -> this.tfm.focus(newPosition.getRow() - 1, newPosition.getTableColumn()));
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case BOTH_INVISIBLE:
/*  92 */         Platform.runLater(() -> this.tfm.focus(newPosition.getRow() - 1, (TableColumn)this.cellsView.getColumns().get(newPosition.getColumn() - 1)));
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case COLUMN_SPAN_INVISIBLE:
/* 100 */         if (!this.spreadsheetView.isPressed() && oldPosition.getColumn() == newPosition.getColumn() - 1 && oldPosition.getRow() == newPosition.getRow()) {
/*     */           
/* 102 */           Platform.runLater(() -> this.tfm.focus(oldPosition.getRow(), getTableColumnSpan(oldPosition, this.cellsView, this.spreadsheetView)));
/*     */ 
/*     */           
/*     */           break;
/*     */         } 
/*     */ 
/*     */         
/* 109 */         Platform.runLater(() -> this.tfm.focus(newPosition.getRow(), this.cellsView.getVisibleLeafColumn(newPosition.getColumn() - 1)));
/*     */         break;
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
/*     */   static TableColumn<ObservableList<SpreadsheetCell>, ?> getTableColumnSpan(TablePosition<?, ?> pos, SpreadsheetGridView cellsView, SpreadsheetView spv) {
/* 126 */     return cellsView.getVisibleLeafColumn(pos.getColumn() + spv
/* 127 */         .getColumnSpan((SpreadsheetCell)((ObservableList)cellsView.getItems().get(pos.getRow())).get(cellsView.getColumns().indexOf(pos.getTableColumn()))));
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
/*     */   public static int getNextRowNumber(TablePosition<?, ?> pos, TableView<ObservableList<SpreadsheetCell>> cellsView, SpreadsheetView spv) {
/* 140 */     return spv.getRowSpan((SpreadsheetCell)((ObservableList)cellsView.getItems().get(pos.getRow())).get(cellsView.getColumns().indexOf(pos.getTableColumn())), pos.getRow()) + pos
/* 141 */       .getRow();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\spreadsheet\FocusModelListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */