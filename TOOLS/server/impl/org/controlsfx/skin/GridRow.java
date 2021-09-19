/*     */ package impl.org.controlsfx.skin;
/*     */ 
/*     */ import javafx.beans.InvalidationListener;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.beans.property.SimpleObjectProperty;
/*     */ import javafx.scene.control.IndexedCell;
/*     */ import javafx.scene.control.Skin;
/*     */ import org.controlsfx.control.GridView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class GridRow<T>
/*     */   extends IndexedCell<T>
/*     */ {
/*     */   private final SimpleObjectProperty<GridView<T>> gridView;
/*     */   
/*     */   protected Skin<?> createDefaultSkin() {
/*     */     return (Skin<?>)new GridRowSkin<>(this);
/*     */   }
/*     */   
/*     */   public GridRow() {
/*  88 */     this.gridView = new SimpleObjectProperty(this, "gridView");
/*     */     getStyleClass().add("grid-row");
/*     */     indexProperty().addListener(new InvalidationListener() { public void invalidated(Observable observable) {
/*     */             GridRow.this.updateItem(null, (GridRow.this.getIndex() == -1));
/*     */           } }
/*     */       );
/*     */   } public final void updateGridView(GridView<T> gridView) {
/*  95 */     this.gridView.set(gridView);
/*     */   }
/*     */   public SimpleObjectProperty<GridView<T>> gridViewProperty() {
/*     */     return this.gridView;
/*     */   }
/*     */   
/*     */   public GridView<T> getGridView() {
/* 102 */     return (GridView<T>)this.gridView.get();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\skin\GridRow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */