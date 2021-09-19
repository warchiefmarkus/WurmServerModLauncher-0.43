/*     */ package impl.org.controlsfx.spreadsheet;
/*     */ 
/*     */ import javafx.beans.InvalidationListener;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.beans.WeakInvalidationListener;
/*     */ import javafx.beans.property.DoubleProperty;
/*     */ import javafx.beans.property.SimpleDoubleProperty;
/*     */ import javafx.collections.MapChangeListener;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.event.Event;
/*     */ import javafx.event.EventHandler;
/*     */ import javafx.event.EventTarget;
/*     */ import javafx.event.WeakEventHandler;
/*     */ import javafx.scene.control.Skin;
/*     */ import javafx.scene.control.TableRow;
/*     */ import javafx.scene.input.MouseEvent;
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
/*     */ 
/*     */ 
/*     */ public class GridRow
/*     */   extends TableRow<ObservableList<SpreadsheetCell>>
/*     */ {
/*     */   private final SpreadsheetHandle handle;
/*  61 */   DoubleProperty verticalShift = (DoubleProperty)new SimpleDoubleProperty();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final InvalidationListener setPrefHeightListener;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final WeakInvalidationListener weakPrefHeightListener;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final EventHandler<MouseEvent> dragDetectedEventHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final WeakEventHandler<MouseEvent> weakDragHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final InvalidationListener gridListener;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final WeakInvalidationListener weakGridListener;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final InvalidationListener comparatorListener;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final WeakInvalidationListener weakComparatorListener;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void addCell(CellView cell) {
/* 109 */     getChildren().add(cell);
/*     */   }
/*     */   
/*     */   void removeCell(CellView gc) {
/* 113 */     getChildren().remove(gc);
/*     */   }
/*     */   
/*     */   SpreadsheetView getSpreadsheetView() {
/* 117 */     return this.handle.getView();
/*     */   }
/*     */ 
/*     */   
/*     */   protected double computePrefHeight(double width) {
/* 122 */     return this.handle.getCellsViewSkin().getRowHeight(getIndex());
/*     */   }
/*     */ 
/*     */   
/*     */   protected double computeMinHeight(double width) {
/* 127 */     return this.handle.getCellsViewSkin().getRowHeight(getIndex());
/*     */   }
/*     */ 
/*     */   
/*     */   protected Skin<?> createDefaultSkin() {
/* 132 */     return (Skin<?>)new GridRowSkin(this.handle, this);
/*     */   }
/*     */   public GridRow(final SpreadsheetHandle handle) {
/* 135 */     this.setPrefHeightListener = new InvalidationListener()
/*     */       {
/*     */         public void invalidated(Observable o)
/*     */         {
/* 139 */           GridRow.this.setRowHeight(GridRow.this.computePrefHeight(-1.0D));
/*     */         }
/*     */       };
/*     */     
/* 143 */     this.weakPrefHeightListener = new WeakInvalidationListener(this.setPrefHeightListener);
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
/* 154 */     this.dragDetectedEventHandler = new EventHandler<MouseEvent>()
/*     */       {
/*     */         public void handle(MouseEvent event) {
/* 157 */           if (event.getTarget().getClass().equals(GridRow.class) && event.getPickResult().getIntersectedNode() != null && event
/* 158 */             .getPickResult().getIntersectedNode().getClass().equals(CellView.class)) {
/* 159 */             Event.fireEvent((EventTarget)event.getPickResult().getIntersectedNode(), (Event)event);
/*     */           }
/*     */         }
/*     */       };
/*     */     
/* 164 */     this.weakDragHandler = new WeakEventHandler(this.dragDetectedEventHandler);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 170 */     this.gridListener = new InvalidationListener()
/*     */       {
/*     */         public void invalidated(Observable o)
/*     */         {
/* 174 */           GridRow.this.setRowHeight(GridRow.this.computePrefHeight(-1.0D));
/* 175 */           GridRow.this.handle.getView().comparatorProperty().addListener((InvalidationListener)GridRow.this.weakComparatorListener);
/*     */         }
/*     */       };
/*     */     
/* 179 */     this.weakGridListener = new WeakInvalidationListener(this.gridListener);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 184 */     this.comparatorListener = new InvalidationListener()
/*     */       {
/*     */         public void invalidated(Observable o) {
/* 187 */           GridRow.this.updateIndex(GridRow.this.getIndex());
/* 188 */           GridRow.this.setRowHeight(GridRow.this.computePrefHeight(-1.0D));
/*     */         }
/*     */       };
/*     */     
/* 192 */     this.weakComparatorListener = new WeakInvalidationListener(this.comparatorListener);
/*     */     this.handle = handle;
/*     */     indexProperty().addListener((InvalidationListener)this.weakPrefHeightListener);
/*     */     visibleProperty().addListener((InvalidationListener)this.weakPrefHeightListener);
/*     */     handle.getView().gridProperty().addListener((InvalidationListener)this.weakGridListener);
/*     */     handle.getView().hiddenRowsProperty().addListener((InvalidationListener)this.weakPrefHeightListener);
/*     */     handle.getView().hiddenColumnsProperty().addListener((InvalidationListener)this.weakPrefHeightListener);
/*     */     handle.getView().comparatorProperty().addListener((InvalidationListener)this.weakComparatorListener);
/*     */     (handle.getCellsViewSkin()).rowHeightMap.addListener(new MapChangeListener<Integer, Double>() {
/*     */           public void onChanged(MapChangeListener.Change<? extends Integer, ? extends Double> change) {
/*     */             if (change.wasAdded() && ((Integer)change.getKey()).intValue() == handle.getView().getModelRow(GridRow.this.getIndex())) {
/*     */               GridRow.this.setRowHeight(((Double)change.getValueAdded()).doubleValue());
/*     */             } else if (change.wasRemoved() && ((Integer)change.getKey()).intValue() == handle.getView().getModelRow(GridRow.this.getIndex())) {
/*     */               GridRow.this.setRowHeight(GridRow.this.computePrefHeight(-1.0D));
/*     */             } 
/*     */           }
/*     */         });
/*     */     addEventHandler(MouseEvent.DRAG_DETECTED, (EventHandler)this.weakDragHandler);
/*     */   }
/*     */   
/*     */   public void setRowHeight(double height) {
/*     */     CellView.getValue(() -> setHeight(height));
/*     */     setPrefHeight(height);
/*     */     (this.handle.getCellsViewSkin()).rectangleSelection.updateRectangle();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\spreadsheet\GridRow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */