/*     */ package impl.org.controlsfx.skin;
/*     */ 
/*     */ import com.sun.javafx.scene.control.behavior.BehaviorBase;
/*     */ import com.sun.javafx.scene.control.skin.CellSkinBase;
/*     */ import java.util.Collections;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Cell;
/*     */ import javafx.scene.control.Control;
/*     */ import org.controlsfx.control.GridCell;
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
/*     */ public class GridRowSkin<T>
/*     */   extends CellSkinBase<GridRow<T>, BehaviorBase<GridRow<T>>>
/*     */ {
/*     */   public GridRowSkin(GridRow<T> control) {
/*  43 */     super((Cell)control, new BehaviorBase((Control)control, Collections.emptyList()));
/*     */ 
/*     */     
/*  46 */     getChildren().clear();
/*  47 */     updateCells();
/*     */     
/*  49 */     registerChangeListener((ObservableValue)((GridRow)getSkinnable()).indexProperty(), "INDEX");
/*  50 */     registerChangeListener((ObservableValue)((GridRow)getSkinnable()).widthProperty(), "WIDTH");
/*  51 */     registerChangeListener((ObservableValue)((GridRow)getSkinnable()).heightProperty(), "HEIGHT");
/*     */   }
/*     */   
/*     */   protected void handleControlPropertyChanged(String p) {
/*  55 */     super.handleControlPropertyChanged(p);
/*     */     
/*  57 */     if ("INDEX".equals(p)) {
/*  58 */       updateCells();
/*  59 */     } else if ("WIDTH".equals(p)) {
/*  60 */       updateCells();
/*  61 */     } else if ("HEIGHT".equals(p)) {
/*  62 */       updateCells();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GridCell<T> getCellAtIndex(int index) {
/*  73 */     if (index < getChildren().size()) {
/*  74 */       return (GridCell<T>)getChildren().get(index);
/*     */     }
/*  76 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateCells() {
/*  84 */     int rowIndex = ((GridRow)getSkinnable()).getIndex();
/*  85 */     if (rowIndex >= 0) {
/*  86 */       GridView<T> gridView = ((GridRow<T>)getSkinnable()).getGridView();
/*  87 */       int maxCellsInRow = ((GridViewSkin)gridView.getSkin()).computeMaxCellsInRow();
/*  88 */       int totalCellsInGrid = gridView.getItems().size();
/*  89 */       int startCellIndex = rowIndex * maxCellsInRow;
/*  90 */       int endCellIndex = startCellIndex + maxCellsInRow - 1;
/*  91 */       int cacheIndex = 0;
/*     */       
/*  93 */       for (int cellIndex = startCellIndex; cellIndex <= endCellIndex && 
/*  94 */         cellIndex < totalCellsInGrid; cellIndex++, cacheIndex++) {
/*     */         
/*  96 */         GridCell<T> cell = getCellAtIndex(cacheIndex);
/*  97 */         if (cell == null) {
/*  98 */           cell = createCell();
/*  99 */           getChildren().add(cell);
/*     */         } 
/* 101 */         cell.updateIndex(-1);
/* 102 */         cell.updateIndex(cellIndex);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 110 */       getChildren().remove(cacheIndex, getChildren().size());
/*     */     } 
/*     */   }
/*     */   private GridCell<T> createCell() {
/*     */     GridCell<T> cell;
/* 115 */     GridView<T> gridView = (GridView<T>)((GridRow<T>)getSkinnable()).gridViewProperty().get();
/*     */     
/* 117 */     if (gridView.getCellFactory() != null) {
/* 118 */       cell = (GridCell<T>)gridView.getCellFactory().call(gridView);
/*     */     } else {
/* 120 */       cell = createDefaultCellImpl();
/*     */     } 
/* 122 */     cell.updateGridView(gridView);
/* 123 */     return cell;
/*     */   }
/*     */   
/*     */   private GridCell<T> createDefaultCellImpl() {
/* 127 */     return new GridCell<T>() {
/*     */         protected void updateItem(T item, boolean empty) {
/* 129 */           super.updateItem(item, empty);
/* 130 */           if (empty) {
/* 131 */             setText("");
/*     */           } else {
/* 133 */             setText(item.toString());
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 140 */     return super.computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
/*     */   }
/*     */   
/*     */   protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 144 */     return Double.MAX_VALUE;
/*     */   }
/*     */   
/*     */   protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 148 */     GridView<T> gv = (GridView<T>)((GridRow<T>)getSkinnable()).gridViewProperty().get();
/* 149 */     return gv.getCellHeight() + gv.getVerticalCellSpacing() * 2.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void layoutChildren(double x, double y, double w, double h) {
/* 154 */     double cellWidth = ((GridView)((GridRow<T>)getSkinnable()).gridViewProperty().get()).getCellWidth();
/* 155 */     double cellHeight = ((GridView)((GridRow<T>)getSkinnable()).gridViewProperty().get()).getCellHeight();
/* 156 */     double horizontalCellSpacing = ((GridView)((GridRow<T>)getSkinnable()).gridViewProperty().get()).getHorizontalCellSpacing();
/* 157 */     double verticalCellSpacing = ((GridView)((GridRow<T>)getSkinnable()).gridViewProperty().get()).getVerticalCellSpacing();
/*     */     
/* 159 */     double xPos = 0.0D;
/* 160 */     double yPos = 0.0D;
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
/* 173 */     for (Node child : getChildren()) {
/* 174 */       child.relocate(xPos + horizontalCellSpacing, yPos + verticalCellSpacing);
/* 175 */       child.resize(cellWidth, cellHeight);
/* 176 */       xPos = xPos + horizontalCellSpacing + cellWidth + horizontalCellSpacing;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\skin\GridRowSkin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */