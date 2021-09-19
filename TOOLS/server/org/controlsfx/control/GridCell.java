/*     */ package org.controlsfx.control;
/*     */ 
/*     */ import impl.org.controlsfx.skin.GridCellSkin;
/*     */ import javafx.beans.InvalidationListener;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.beans.property.SimpleObjectProperty;
/*     */ import javafx.scene.control.IndexedCell;
/*     */ import javafx.scene.control.Skin;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GridCell<T>
/*     */   extends IndexedCell<T>
/*     */ {
/*     */   private final SimpleObjectProperty<GridView<T>> gridView;
/*     */   
/*     */   protected Skin<?> createDefaultSkin() {
/*     */     return (Skin<?>)new GridCellSkin(this);
/*     */   }
/*     */   
/*     */   public GridCell() {
/* 109 */     this.gridView = new SimpleObjectProperty(this, "gridView"); getStyleClass().add("grid-cell"); indexProperty().addListener(new InvalidationListener() { public void invalidated(Observable observable) { GridView<T> gridView = GridCell.this.getGridView(); if (gridView == null)
/*     */               return;  if (GridCell.this.getIndex() < 0) {
/*     */               GridCell.this.updateItem(null, true);
/*     */               return;
/*     */             } 
/*     */             T item = (T)gridView.getItems().get(GridCell.this.getIndex());
/*     */             GridCell.this.updateItem(item, (item == null)); } });
/* 116 */   } public final void updateGridView(GridView<T> gridView) { this.gridView.set(gridView); }
/*     */   
/*     */   public SimpleObjectProperty<GridView<T>> gridViewProperty() {
/*     */     return this.gridView;
/*     */   }
/*     */   
/*     */   public GridView<T> getGridView() {
/* 123 */     return (GridView<T>)this.gridView.get();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\GridCell.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */