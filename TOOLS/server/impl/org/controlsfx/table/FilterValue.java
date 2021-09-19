/*    */ package impl.org.controlsfx.table;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import javafx.beans.InvalidationListener;
/*    */ import javafx.beans.Observable;
/*    */ import javafx.beans.WeakInvalidationListener;
/*    */ import javafx.beans.property.BooleanProperty;
/*    */ import javafx.beans.property.Property;
/*    */ import javafx.beans.property.SimpleBooleanProperty;
/*    */ import javafx.scene.Node;
/*    */ import javafx.scene.control.CheckBox;
/*    */ import javafx.scene.control.Label;
/*    */ import javafx.scene.layout.HBox;
/*    */ import javafx.scene.paint.Color;
/*    */ 
/*    */ final class FilterValue<T, R> extends HBox implements Comparable<FilterValue<T, R>> {
/*    */   private final R value;
/* 18 */   private final BooleanProperty isSelected = (BooleanProperty)new SimpleBooleanProperty(true);
/* 19 */   private final BooleanProperty inScope = (BooleanProperty)new SimpleBooleanProperty(true);
/*    */   
/*    */   private final ColumnFilter<T, R> columnFilter;
/*    */   private final InvalidationListener scopeListener;
/*    */   
/*    */   FilterValue(R value, ColumnFilter<T, R> columnFilter) {
/* 25 */     this.value = value;
/* 26 */     this.columnFilter = columnFilter;
/*    */     
/* 28 */     CheckBox checkBox = new CheckBox();
/* 29 */     Label label = new Label();
/* 30 */     label.setText(Optional.<R>ofNullable(value).map(Object::toString).orElse(null));
/* 31 */     this.scopeListener = (v -> label.textFillProperty().set(getInScopeProperty().get() ? Color.BLACK : Color.LIGHTGRAY));
/* 32 */     this.inScope.addListener((InvalidationListener)new WeakInvalidationListener(this.scopeListener));
/* 33 */     checkBox.selectedProperty().bindBidirectional((Property)selectedProperty());
/* 34 */     getChildren().addAll((Object[])new Node[] { (Node)checkBox, (Node)label });
/*    */   }
/*    */   
/*    */   public R getValue() {
/* 38 */     return this.value;
/*    */   }
/*    */   
/*    */   public BooleanProperty selectedProperty() {
/* 42 */     return this.isSelected;
/*    */   }
/*    */   public BooleanProperty getInScopeProperty() {
/* 45 */     return this.inScope;
/*    */   }
/*    */   
/*    */   public void refreshScope() {
/* 49 */     this.inScope.setValue(Boolean.valueOf((this.columnFilter.wasLastFiltered() || this.columnFilter.valueIsVisible(this.value))));
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 54 */     return Optional.<R>ofNullable(this.value).map(Object::toString).orElse("");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int compareTo(FilterValue<T, R> other) {
/* 60 */     if (this.value != null && other.value != null && 
/* 61 */       this.value instanceof Comparable && other.value instanceof Comparable) {
/* 62 */       return ((Comparable<Comparable>)this.value).compareTo((Comparable)other.value);
/*    */     }
/*    */     
/* 65 */     return ((String)Optional.<R>ofNullable(this.value).map(Object::toString).orElse(""))
/* 66 */       .compareTo(Optional.<FilterValue<T, R>>ofNullable(other).map(Object::toString).orElse(""));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\table\FilterValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */