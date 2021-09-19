/*    */ package org.controlsfx.control.table.model;
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
/*    */ class TableModelRow<S>
/*    */ {
/*    */   private final int columnCount;
/*    */   private final JavaFXTableModel<S> tableModel;
/*    */   private final int row;
/*    */   
/*    */   TableModelRow(JavaFXTableModel<S> tableModel, int row) {
/* 37 */     this.row = row;
/* 38 */     this.tableModel = tableModel;
/* 39 */     this.columnCount = tableModel.getColumnCount();
/*    */   }
/*    */   
/*    */   public Object get(int column) {
/* 43 */     return (column < 0 || column >= this.columnCount) ? null : this.tableModel.getValueAt(this.row, column);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 47 */     String text = "Row " + this.row + ": [ ";
/*    */     
/* 49 */     for (int col = 0; col < this.columnCount; col++) {
/* 50 */       text = text + get(col);
/*    */       
/* 52 */       if (col < this.columnCount - 1) {
/* 53 */         text = text + ", ";
/*    */       }
/*    */     } 
/*    */     
/* 57 */     text = text + " ]";
/* 58 */     return text;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\table\model\TableModelRow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */