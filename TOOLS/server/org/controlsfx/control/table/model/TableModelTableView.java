/*    */ package org.controlsfx.control.table.model;
/*    */ 
/*    */ import com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList;
/*    */ import javafx.collections.ObservableList;
/*    */ import javafx.scene.control.TableColumn;
/*    */ import javafx.scene.control.TableView;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class TableModelTableView<S>
/*    */   extends TableView<TableModelRow<S>>
/*    */ {
/*    */   public TableModelTableView(final JavaFXTableModel<S> tableModel) {
/* 42 */     setItems((ObservableList)new ReadOnlyUnbackedObservableList<TableModelRow<S>>() {
/*    */           public TableModelRow<S> get(int row) {
/* 44 */             if (row < 0 || row >= tableModel.getRowCount()) return null; 
/* 45 */             TableModelRow<S> backingRow = new TableModelRow<>(tableModel, row);
/* 46 */             return backingRow;
/*    */           }
/*    */           
/*    */           public int size() {
/* 50 */             return tableModel.getRowCount();
/*    */           }
/*    */         });
/*    */     
/* 54 */     setSortPolicy(table -> {
/*    */           tableModel.sort(table);
/*    */           
/*    */           return Boolean.valueOf(true);
/*    */         });
/*    */     
/* 60 */     for (int i = 0; i < tableModel.getColumnCount(); i++) {
/* 61 */       TableColumn<TableModelRow<S>, ?> column = new TableColumn(tableModel.getColumnName(i));
/* 62 */       column.setCellValueFactory(new TableModelValueFactory<>(tableModel, i));
/* 63 */       getColumns().add(column);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\table\model\TableModelTableView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */