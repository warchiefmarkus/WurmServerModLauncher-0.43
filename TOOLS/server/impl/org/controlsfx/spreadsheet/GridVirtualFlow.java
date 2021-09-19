/*     */ package impl.org.controlsfx.spreadsheet;
/*     */ 
/*     */ import com.sun.javafx.scene.control.skin.VirtualFlow;
/*     */ import com.sun.javafx.scene.control.skin.VirtualScrollBar;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.beans.binding.When;
/*     */ import javafx.beans.value.ChangeListener;
/*     */ import javafx.beans.value.ObservableBooleanValue;
/*     */ import javafx.beans.value.ObservableNumberValue;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.geometry.Pos;
/*     */ import javafx.scene.Group;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.IndexedCell;
/*     */ import javafx.scene.control.ScrollBar;
/*     */ import javafx.scene.control.TableRow;
/*     */ import javafx.scene.input.MouseEvent;
/*     */ import javafx.scene.layout.Region;
/*     */ import javafx.scene.layout.StackPane;
/*     */ import javafx.scene.shape.Rectangle;
/*     */ import javafx.scene.transform.Scale;
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
/*     */ final class GridVirtualFlow<T extends IndexedCell<?>>
/*     */   extends VirtualFlow<T>
/*     */ {
/*  71 */   private static final Comparator<GridRow> ROWCMP = new Comparator<GridRow>()
/*     */     {
/*     */       public int compare(GridRow firstRow, GridRow secondRow)
/*     */       {
/*  75 */         return secondRow.getIndex() - firstRow.getIndex();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SpreadsheetView spreadSheetView;
/*     */ 
/*     */   
/*     */   private final GridViewSkin gridViewSkin;
/*     */ 
/*     */   
/*  88 */   private final ArrayList<T> myFixedCells = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final List<Node> sheetChildren;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private StackPane corner;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Scale scale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ChangeListener<Number> hBarValueChangeListener;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void init(SpreadsheetView spv) {
/* 129 */     getHbar().maxProperty().addListener(new ChangeListener<Number>()
/*     */         {
/*     */           
/*     */           public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
/*     */           {
/* 134 */             GridVirtualFlow.this.getHbar().setBlockIncrement(GridVirtualFlow.this.getWidth());
/* 135 */             GridVirtualFlow.this.getHbar().setUnitIncrement(newValue.doubleValue() / 20.0D);
/*     */           }
/*     */         });
/* 138 */     this.scale = new Scale(1.0D / spv.getZoomFactor().doubleValue(), 1.0D / spv.getZoomFactor().doubleValue());
/* 139 */     this.scale.setPivotX(getHbar().getWidth() / 2.0D);
/* 140 */     getHbar().getTransforms().add(this.scale);
/* 141 */     getVbar().getTransforms().add(this.scale);
/* 142 */     this.corner.getTransforms().add(this.scale);
/*     */     
/* 144 */     this.spreadSheetView = spv;
/*     */     
/* 146 */     this.spreadSheetView.zoomFactorProperty().addListener((observable, oldValue, newValue) -> {
/*     */           this.scale.setX(1.0D / newValue.doubleValue());
/*     */ 
/*     */           
/*     */           this.scale.setY(1.0D / newValue.doubleValue());
/*     */         });
/*     */     
/* 153 */     Rectangle rec = new Rectangle();
/* 154 */     rec.widthProperty().bind((ObservableValue)widthProperty().subtract((ObservableNumberValue)(new When((ObservableBooleanValue)getVbar().visibleProperty())).then((ObservableNumberValue)getVbar().widthProperty()).otherwise(0)));
/* 155 */     rec.heightProperty().bind((ObservableValue)heightProperty().subtract((ObservableNumberValue)(new When((ObservableBooleanValue)getHbar().visibleProperty())).then((ObservableNumberValue)getHbar().heightProperty()).otherwise(0)));
/* 156 */     this.gridViewSkin.rectangleSelection.setClip((Node)rec);
/*     */     
/* 158 */     getChildren().add(this.gridViewSkin.rectangleSelection);
/*     */     
/* 160 */     spv.getFixedRows().addListener(observable -> {
/*     */           List<T> toRemove = new ArrayList<>();
/*     */           for (IndexedCell indexedCell : this.myFixedCells) {
/*     */             if (!spv.getFixedRows().contains(Integer.valueOf(this.spreadSheetView.getFilteredSourceIndex(indexedCell.getIndex())))) {
/*     */               indexedCell.setManaged(false);
/*     */               indexedCell.setVisible(false);
/*     */               toRemove.add((T)indexedCell);
/*     */             } 
/*     */           } 
/*     */           this.myFixedCells.removeAll(toRemove);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void show(int index) {
/* 175 */     super.show(index);
/* 176 */     layoutTotal();
/* 177 */     layoutFixedRows();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void scrollTo(int index) {
/* 183 */     if (!getCells().isEmpty() && !VerticalHeader.isFixedRowEmpty(this.spreadSheetView)) {
/* 184 */       double offset = this.gridViewSkin.getFixedRowHeight();
/*     */       
/* 186 */       while (offset >= 0.0D && index > 0) {
/* 187 */         index--;
/* 188 */         offset -= this.gridViewSkin.getRowHeight(index);
/*     */       } 
/*     */     } 
/* 191 */     super.scrollTo(index);
/*     */     
/* 193 */     layoutTotal();
/* 194 */     layoutFixedRows();
/*     */   }
/*     */ 
/*     */   
/*     */   public double adjustPixels(double delta) {
/* 199 */     double returnValue = super.adjustPixels(delta);
/*     */     
/* 201 */     layoutTotal();
/* 202 */     layoutFixedRows();
/*     */     
/* 204 */     return returnValue;
/*     */   }
/*     */   
/*     */   List<T> getFixedCells() {
/* 208 */     return this.myFixedCells;
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
/*     */   GridRow getTopRow() {
/* 222 */     if (!this.sheetChildren.isEmpty()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 228 */       int i = this.sheetChildren.size() - 1;
/* 229 */       while (((GridRow)this.sheetChildren.get(i)).getChildrenUnmodifiable().isEmpty() && i > 0) {
/* 230 */         i--;
/*     */       }
/* 232 */       return (GridRow)this.sheetChildren.get(i);
/*     */     } 
/* 234 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void layoutChildren() {
/* 244 */     if (this.spreadSheetView != null) {
/*     */ 
/*     */       
/* 247 */       sortRows();
/* 248 */       super.layoutChildren();
/* 249 */       layoutTotal();
/* 250 */       layoutFixedRows();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 257 */       if (getVbar().getVisibleAmount() == 0.0D && 
/* 258 */         getVbar().isVisible() && 
/* 259 */         getCells().size() != getCellCount()) {
/* 260 */         getVbar().setMax(1.0D);
/* 261 */         getVbar().setVisibleAmount((getCells().size() / getCellCount()));
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 270 */     Pos pos = Pos.TOP_LEFT;
/* 271 */     double width = getWidth();
/* 272 */     double height = getHeight();
/* 273 */     double top = getInsets().getTop();
/* 274 */     double right = getInsets().getRight();
/* 275 */     double left = getInsets().getLeft();
/* 276 */     double bottom = getInsets().getBottom();
/* 277 */     double scaleX = this.scale.getX();
/* 278 */     double shift = 1.0D - scaleX;
/* 279 */     double contentWidth = width / scaleX - left - right - getVbar().getWidth();
/* 280 */     double contentHeight = height / scaleX - top - bottom - getHbar().getHeight();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 287 */     layoutInArea((Node)getHbar(), 0.0D - shift * 10.0D, height - 
/* 288 */         getHbar().getHeight() * scaleX, contentWidth, contentHeight, 0.0D, null, pos
/*     */ 
/*     */         
/* 291 */         .getHpos(), pos
/* 292 */         .getVpos());
/*     */     
/* 294 */     layoutInArea((Node)getVbar(), width - getVbar().getWidth() + shift, 0.0D, contentWidth, contentHeight, 0.0D, null, pos
/*     */ 
/*     */ 
/*     */         
/* 298 */         .getHpos(), pos
/* 299 */         .getVpos());
/*     */ 
/*     */     
/* 302 */     if (this.corner != null) {
/* 303 */       layoutInArea((Node)this.corner, width - getVbar().getWidth() + shift, 
/* 304 */           getHeight() - getHbar().getHeight() * scaleX, this.corner
/* 305 */           .getWidth(), this.corner.getHeight(), 0.0D, null, pos
/*     */           
/* 307 */           .getHpos(), pos
/* 308 */           .getVpos());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void layoutTotal() {
/* 316 */     sortRows();
/* 317 */     removeDeportedCells();
/*     */ 
/*     */     
/* 320 */     if (getCells().isEmpty()) {
/* 321 */       reconfigureCells();
/*     */     }
/*     */     
/* 324 */     for (GridRow cell : getCells()) {
/* 325 */       if (cell != null && cell.getIndex() >= 0 && (!this.gridViewSkin.hBarValue.get(cell.getIndex()) || this.gridViewSkin.rowToLayout.get(cell.getIndex()))) {
/* 326 */         cell.requestLayout();
/*     */       }
/*     */     } 
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
/*     */   private void removeDeportedCells() {
/* 341 */     ArrayList<GridRow> rowToRemove = new ArrayList<>();
/* 342 */     for (Map.Entry<GridRow, Set<CellView>> entry : this.gridViewSkin.deportedCells.entrySet()) {
/* 343 */       ArrayList<CellView> toRemove = new ArrayList<>();
/* 344 */       for (CellView cell : entry.getValue()) {
/*     */         
/* 346 */         if (!cell.isEditing() && !getCells().contains(cell.getTableRow())) {
/* 347 */           ((GridRow)entry.getKey()).removeCell(cell);
/* 348 */           toRemove.add(cell);
/*     */         } 
/*     */       } 
/* 351 */       ((Set)entry.getValue()).removeAll(toRemove);
/* 352 */       if (((Set)entry.getValue()).isEmpty()) {
/* 353 */         rowToRemove.add(entry.getKey());
/*     */       }
/*     */     } 
/* 356 */     for (GridRow row : rowToRemove) {
/* 357 */       this.gridViewSkin.deportedCells.remove(row);
/*     */     }
/*     */   }
/*     */   
/*     */   protected ScrollBar getVerticalBar() {
/* 362 */     return (ScrollBar)getVbar();
/*     */   }
/*     */   protected ScrollBar getHorizontalBar() {
/* 365 */     return (ScrollBar)getHbar();
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<T> getCells() {
/* 370 */     return super.getCells();
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
/*     */   
/*     */   private List<Node> findSheetChildren() {
/* 385 */     if (!getChildren().isEmpty() && 
/* 386 */       getChildren().get(0) instanceof Region) {
/* 387 */       Region region = (Region)getChildren().get(0);
/* 388 */       if (!region.getChildrenUnmodifiable().isEmpty() && 
/* 389 */         region.getChildrenUnmodifiable().get(0) instanceof Group) {
/* 390 */         return (List<Node>)((Group)region.getChildrenUnmodifiable().get(0)).getChildren();
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 395 */     return new ArrayList<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void findCorner() {
/* 403 */     if (!getChildren().isEmpty()) {
/* 404 */       for (Node node : getChildren()) {
/* 405 */         if (node instanceof StackPane) {
/* 406 */           this.corner = (StackPane)node;
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void layoutFixedRows() {
/* 419 */     if (!VerticalHeader.isFixedRowEmpty(this.spreadSheetView) && getFirstVisibleCellWithinViewPort() != null) {
/* 420 */       sortRows();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 429 */       T row = null;
/*     */       
/*     */       int i;
/*     */       
/* 433 */       label37: for (i = this.spreadSheetView.getFixedRows().size() - 1; i >= 0; i--) {
/* 434 */         Integer fixedRowIndex = (Integer)this.spreadSheetView.getFixedRows().get(i);
/* 435 */         if (!this.spreadSheetView.isRowHidden(i)) {
/*     */ 
/*     */ 
/*     */           
/* 439 */           fixedRowIndex = Integer.valueOf(this.spreadSheetView.getFilteredRow(fixedRowIndex.intValue()));
/* 440 */           IndexedCell indexedCell = getLastVisibleCellWithinViewPort();
/*     */           
/* 442 */           if (indexedCell != null && fixedRowIndex.intValue() > indexedCell.getIndex()) {
/* 443 */             if (row != null) {
/* 444 */               row.setVisible(false);
/* 445 */               row.setManaged(false);
/* 446 */               this.sheetChildren.remove(row);
/*     */             } 
/*     */           } else {
/*     */             IndexedCell indexedCell1;
/*     */ 
/*     */             
/* 452 */             for (IndexedCell indexedCell2 : getCells()) {
/* 453 */               if (indexedCell2.getIndex() > fixedRowIndex.intValue())
/*     */                 break; 
/* 455 */               if (indexedCell2.getIndex() == fixedRowIndex.intValue()) {
/* 456 */                 row = containsRows(fixedRowIndex.intValue());
/* 457 */                 if (row != null) {
/* 458 */                   row.setVisible(false);
/* 459 */                   row.setManaged(false);
/* 460 */                   this.sheetChildren.remove(row);
/*     */                 } 
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
/* 472 */                 indexedCell2.toFront();
/*     */                 
/*     */                 continue label37;
/*     */               } 
/*     */             } 
/* 477 */             row = containsRows(fixedRowIndex.intValue());
/* 478 */             if (row == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 483 */               indexedCell1 = (IndexedCell)getCreateCell().call(this);
/* 484 */               indexedCell1.getProperties().put("newcell", null);
/*     */               
/* 486 */               setCellIndex(indexedCell1, fixedRowIndex.intValue());
/* 487 */               resizeCellSize(indexedCell1);
/* 488 */               this.myFixedCells.add((T)indexedCell1);
/*     */             } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 496 */             if (!this.sheetChildren.contains(indexedCell1)) {
/* 497 */               this.sheetChildren.add(indexedCell1);
/*     */             }
/*     */             
/* 500 */             indexedCell1.setManaged(true);
/* 501 */             indexedCell1.setVisible(true);
/* 502 */             indexedCell1.toFront();
/* 503 */             indexedCell1.requestLayout();
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private T containsRows(int i) {
/* 515 */     for (IndexedCell indexedCell : this.myFixedCells) {
/* 516 */       if (indexedCell.getIndex() == i) {
/* 517 */         return (T)indexedCell;
/*     */       }
/*     */     } 
/* 520 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void sortRows() {
/* 526 */     List<GridRow> temp = (List)getCells();
/* 527 */     List<GridRow> tset = new ArrayList<>(temp);
/* 528 */     Collections.sort(tset, ROWCMP);
/* 529 */     for (TableRow<ObservableList<SpreadsheetCell>> r : tset)
/* 530 */       r.toFront(); 
/*     */   }
/*     */   
/*     */   public GridVirtualFlow(GridViewSkin gridViewSkin) {
/* 534 */     this.hBarValueChangeListener = new ChangeListener<Number>()
/*     */       {
/*     */         public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
/* 537 */           GridVirtualFlow.this.gridViewSkin.hBarValue.clear();
/*     */         }
/*     */       };
/*     */     this.gridViewSkin = gridViewSkin;
/*     */     ChangeListener<Number> listenerY = new ChangeListener<Number>() {
/*     */         public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
/*     */           GridVirtualFlow.this.layoutTotal();
/*     */         }
/*     */       };
/*     */     getVbar().valueProperty().addListener(listenerY);
/*     */     getHbar().valueProperty().addListener(this.hBarValueChangeListener);
/*     */     widthProperty().addListener(this.hBarValueChangeListener);
/*     */     this.sheetChildren = findSheetChildren();
/*     */     findCorner();
/*     */     addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
/*     */           if (event.getTarget().getClass() == GridRow.class)
/*     */             this.spreadSheetView.getSelectionModel().clearSelection(); 
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\spreadsheet\GridVirtualFlow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */