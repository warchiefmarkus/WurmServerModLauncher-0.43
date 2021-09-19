/*    */ package org.controlsfx.control.table.model;
/*    */ 
/*    */ import javafx.beans.property.ReadOnlyObjectWrapper;
/*    */ import javafx.beans.value.ObservableValue;
/*    */ import javafx.scene.control.TableColumn;
/*    */ import javafx.util.Callback;
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
/*    */ class TableModelValueFactory<S, T>
/*    */   implements Callback<TableColumn.CellDataFeatures<TableModelRow<S>, T>, ObservableValue<T>>
/*    */ {
/*    */   private final JavaFXTableModel<S> _tableModel;
/*    */   private final int _columnIndex;
/*    */   
/*    */   public TableModelValueFactory(JavaFXTableModel<S> tableModel, int columnIndex) {
/* 45 */     this._tableModel = tableModel;
/* 46 */     this._columnIndex = columnIndex;
/*    */   }
/*    */ 
/*    */   
/*    */   public ObservableValue<T> call(TableColumn.CellDataFeatures<TableModelRow<S>, T> cdf) {
/* 51 */     TableModelRow<S> row = (TableModelRow<S>)cdf.getValue();
/* 52 */     T valueAt = (T)row.get(this._columnIndex);
/* 53 */     return (valueAt instanceof ObservableValue) ? (ObservableValue<T>)valueAt : (ObservableValue<T>)new ReadOnlyObjectWrapper(valueAt);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\table\model\TableModelValueFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */