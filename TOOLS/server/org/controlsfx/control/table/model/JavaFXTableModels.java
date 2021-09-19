/*    */ package org.controlsfx.control.table.model;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javafx.scene.control.TableColumn;
/*    */ import javafx.scene.control.TableView;
/*    */ import javax.swing.RowSorter;
/*    */ import javax.swing.SortOrder;
/*    */ import javax.swing.table.TableModel;
/*    */ import javax.swing.table.TableRowSorter;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class JavaFXTableModels
/*    */ {
/*    */   public static <S> JavaFXTableModel<S> wrap(final TableModel tableModel) {
/* 52 */     return new JavaFXTableModel<S>()
/*    */       {
/*    */ 
/*    */         
/* 56 */         final TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
/*    */ 
/*    */ 
/*    */         
/*    */         public S getValueAt(int rowIndex, int columnIndex) {
/* 61 */           return (S)tableModel.getValueAt(this.sorter.convertRowIndexToView(rowIndex), columnIndex);
/*    */         }
/*    */         
/*    */         public void setValueAt(S value, int rowIndex, int columnIndex) {
/* 65 */           tableModel.setValueAt(value, rowIndex, columnIndex);
/*    */         }
/*    */         
/*    */         public int getRowCount() {
/* 69 */           return tableModel.getRowCount();
/*    */         }
/*    */         
/*    */         public int getColumnCount() {
/* 73 */           return tableModel.getColumnCount();
/*    */         }
/*    */         
/*    */         public String getColumnName(int columnIndex) {
/* 77 */           return tableModel.getColumnName(columnIndex);
/*    */         }
/*    */         
/*    */         public void sort(TableView<TableModelRow<S>> table) {
/* 81 */           List<RowSorter.SortKey> sortKeys = new ArrayList<>();
/*    */           
/* 83 */           for (TableColumn<TableModelRow<S>, ?> column : (Iterable<TableColumn<TableModelRow<S>, ?>>)table.getSortOrder()) {
/* 84 */             int columnIndex = table.getVisibleLeafIndex(column);
/* 85 */             TableColumn.SortType sortType = column.getSortType();
/* 86 */             SortOrder sortOrder = (sortType == TableColumn.SortType.ASCENDING) ? SortOrder.ASCENDING : ((sortType == TableColumn.SortType.DESCENDING) ? SortOrder.DESCENDING : SortOrder.UNSORTED);
/*    */ 
/*    */             
/* 89 */             RowSorter.SortKey sortKey = new RowSorter.SortKey(columnIndex, sortOrder);
/* 90 */             sortKeys.add(sortKey);
/*    */             
/* 92 */             this.sorter.setComparator(columnIndex, column.getComparator());
/*    */           } 
/*    */           
/* 95 */           this.sorter.setSortKeys(sortKeys);
/* 96 */           this.sorter.sort();
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\table\model\JavaFXTableModels.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */