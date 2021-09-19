/*     */ package impl.org.controlsfx.skin;
/*     */ 
/*     */ import com.sun.javafx.scene.control.behavior.BehaviorBase;
/*     */ import com.sun.javafx.scene.control.skin.VirtualContainerBase;
/*     */ import com.sun.javafx.scene.control.skin.VirtualFlow;
/*     */ import java.util.Collections;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.collections.ListChangeListener;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.collections.WeakListChangeListener;
/*     */ import javafx.scene.control.Control;
/*     */ import javafx.scene.control.IndexedCell;
/*     */ import javafx.util.Callback;
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
/*     */ public class GridViewSkin<T>
/*     */   extends VirtualContainerBase<GridView<T>, BehaviorBase<GridView<T>>, GridRow<T>>
/*     */ {
/*  45 */   private final ListChangeListener<T> gridViewItemsListener = new ListChangeListener<T>() {
/*     */       public void onChanged(ListChangeListener.Change<? extends T> change) {
/*  47 */         GridViewSkin.this.updateRowCount();
/*  48 */         ((GridView)GridViewSkin.this.getSkinnable()).requestLayout();
/*     */       }
/*     */     };
/*     */   
/*  52 */   private final WeakListChangeListener<T> weakGridViewItemsListener = new WeakListChangeListener(this.gridViewItemsListener);
/*     */ 
/*     */   
/*     */   public GridViewSkin(GridView<T> control) {
/*  56 */     super((Control)control, new BehaviorBase((Control)control, Collections.emptyList()));
/*     */     
/*  58 */     updateGridViewItems();
/*     */     
/*  60 */     this.flow.setId("virtual-flow");
/*  61 */     this.flow.setPannable(false);
/*  62 */     this.flow.setVertical(true);
/*  63 */     this.flow.setFocusTraversable(((GridView)getSkinnable()).isFocusTraversable());
/*  64 */     this.flow.setCreateCell(new Callback<VirtualFlow, GridRow<T>>() {
/*     */           public GridRow<T> call(VirtualFlow flow) {
/*  66 */             return GridViewSkin.this.createCell();
/*     */           }
/*     */         });
/*  69 */     getChildren().add(this.flow);
/*     */     
/*  71 */     updateRowCount();
/*     */ 
/*     */     
/*  74 */     registerChangeListener((ObservableValue)control.itemsProperty(), "ITEMS");
/*  75 */     registerChangeListener((ObservableValue)control.cellFactoryProperty(), "CELL_FACTORY");
/*  76 */     registerChangeListener((ObservableValue)control.parentProperty(), "PARENT");
/*  77 */     registerChangeListener((ObservableValue)control.cellHeightProperty(), "CELL_HEIGHT");
/*  78 */     registerChangeListener((ObservableValue)control.cellWidthProperty(), "CELL_WIDTH");
/*  79 */     registerChangeListener((ObservableValue)control.horizontalCellSpacingProperty(), "HORIZONZAL_CELL_SPACING");
/*  80 */     registerChangeListener((ObservableValue)control.verticalCellSpacingProperty(), "VERTICAL_CELL_SPACING");
/*  81 */     registerChangeListener((ObservableValue)control.widthProperty(), "WIDTH_PROPERTY");
/*  82 */     registerChangeListener((ObservableValue)control.heightProperty(), "HEIGHT_PROPERTY");
/*     */   }
/*     */   
/*     */   protected void handleControlPropertyChanged(String p) {
/*  86 */     super.handleControlPropertyChanged(p);
/*  87 */     if (p == "ITEMS") {
/*  88 */       updateGridViewItems();
/*  89 */     } else if (p == "CELL_FACTORY") {
/*  90 */       this.flow.recreateCells();
/*  91 */     } else if (p == "CELL_HEIGHT") {
/*  92 */       this.flow.recreateCells();
/*  93 */     } else if (p == "CELL_WIDTH") {
/*  94 */       updateRowCount();
/*  95 */       this.flow.recreateCells();
/*  96 */     } else if (p == "HORIZONZAL_CELL_SPACING") {
/*  97 */       updateRowCount();
/*  98 */       this.flow.recreateCells();
/*  99 */     } else if (p == "VERTICAL_CELL_SPACING") {
/* 100 */       this.flow.recreateCells();
/* 101 */     } else if (p == "PARENT") {
/* 102 */       if (((GridView)getSkinnable()).getParent() != null && ((GridView)getSkinnable()).isVisible()) {
/* 103 */         ((GridView)getSkinnable()).requestLayout();
/*     */       }
/* 105 */     } else if (p == "WIDTH_PROPERTY" || p == "HEIGHT_PROPERTY") {
/* 106 */       updateRowCount();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void updateGridViewItems() {
/* 111 */     if (((GridView)getSkinnable()).getItems() != null) {
/* 112 */       ((GridView)getSkinnable()).getItems().removeListener((ListChangeListener)this.weakGridViewItemsListener);
/*     */     }
/*     */     
/* 115 */     if (((GridView)getSkinnable()).getItems() != null) {
/* 116 */       ((GridView)getSkinnable()).getItems().addListener((ListChangeListener)this.weakGridViewItemsListener);
/*     */     }
/*     */     
/* 119 */     updateRowCount();
/* 120 */     this.flow.recreateCells();
/* 121 */     ((GridView)getSkinnable()).requestLayout();
/*     */   }
/*     */   
/*     */   protected void updateRowCount() {
/* 125 */     if (this.flow == null) {
/*     */       return;
/*     */     }
/* 128 */     int oldCount = this.flow.getCellCount();
/* 129 */     int newCount = getItemCount();
/*     */     
/* 131 */     if (newCount != oldCount) {
/* 132 */       this.flow.setCellCount(newCount);
/* 133 */       this.flow.rebuildCells();
/*     */     } else {
/* 135 */       this.flow.reconfigureCells();
/*     */     } 
/* 137 */     updateRows(newCount);
/*     */   }
/*     */   
/*     */   protected void layoutChildren(double x, double y, double w, double h) {
/* 141 */     double x1 = ((GridView)getSkinnable()).getInsets().getLeft();
/* 142 */     double y1 = ((GridView)getSkinnable()).getInsets().getTop();
/* 143 */     double w1 = ((GridView)getSkinnable()).getWidth() - ((GridView)getSkinnable()).getInsets().getLeft() + ((GridView)getSkinnable()).getInsets().getRight();
/* 144 */     double h1 = ((GridView)getSkinnable()).getHeight() - ((GridView)getSkinnable()).getInsets().getTop() + ((GridView)getSkinnable()).getInsets().getBottom();
/*     */     
/* 146 */     this.flow.resizeRelocate(x1, y1, w1, h1);
/*     */   }
/*     */   
/*     */   public GridRow<T> createCell() {
/* 150 */     GridRow<T> row = new GridRow<>();
/* 151 */     row.updateGridView((GridView<T>)getSkinnable());
/* 152 */     return row;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getItemCount() {
/* 160 */     ObservableList<?> items = ((GridView)getSkinnable()).getItems();
/*     */ 
/*     */ 
/*     */     
/* 164 */     return (items == null) ? 0 : (int)Math.ceil(items.size() / computeMaxCellsInRow());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int computeMaxCellsInRow() {
/* 172 */     return Math.max((int)Math.floor(computeRowWidth() / computeCellWidth()), 1);
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
/*     */   protected double computeRowWidth() {
/* 186 */     return ((GridView)getSkinnable()).getWidth() - 18.0D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected double computeCellWidth() {
/* 194 */     return ((GridView)getSkinnable()).cellWidthProperty().doubleValue() + ((GridView)getSkinnable()).horizontalCellSpacingProperty().doubleValue() * 2.0D;
/*     */   }
/*     */   
/*     */   protected void updateRows(int rowCount) {
/* 198 */     for (int i = 0; i < rowCount; i++) {
/* 199 */       GridRow<T> row = (GridRow<T>)this.flow.getVisibleCell(i);
/* 200 */       if (row != null) {
/*     */         
/* 202 */         row.updateIndex(-1);
/* 203 */         row.updateIndex(i);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean areRowsVisible() {
/* 209 */     if (this.flow == null) {
/* 210 */       return false;
/*     */     }
/* 212 */     if (this.flow.getFirstVisibleCell() == null) {
/* 213 */       return false;
/*     */     }
/* 215 */     if (this.flow.getLastVisibleCell() == null) {
/* 216 */       return false;
/*     */     }
/* 218 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected double computeMinHeight(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 223 */     return 0.0D;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\skin\GridViewSkin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */