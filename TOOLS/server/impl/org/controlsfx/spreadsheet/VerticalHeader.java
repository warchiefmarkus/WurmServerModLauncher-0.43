/*     */ package impl.org.controlsfx.spreadsheet;
/*     */ 
/*     */ import impl.org.controlsfx.i18n.Localization;
/*     */ import java.util.ArrayList;
/*     */ import java.util.BitSet;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Stack;
/*     */ import javafx.beans.InvalidationListener;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.beans.property.DoubleProperty;
/*     */ import javafx.beans.property.ReadOnlyDoubleProperty;
/*     */ import javafx.beans.property.SimpleDoubleProperty;
/*     */ import javafx.beans.value.ChangeListener;
/*     */ import javafx.beans.value.ObservableNumberValue;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.collections.ObservableSet;
/*     */ import javafx.event.ActionEvent;
/*     */ import javafx.event.Event;
/*     */ import javafx.event.EventHandler;
/*     */ import javafx.event.EventTarget;
/*     */ import javafx.geometry.NodeOrientation;
/*     */ import javafx.scene.Cursor;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.ContextMenu;
/*     */ import javafx.scene.control.Label;
/*     */ import javafx.scene.control.MenuItem;
/*     */ import javafx.scene.control.ScrollBar;
/*     */ import javafx.scene.control.TableColumn;
/*     */ import javafx.scene.control.TableColumnBase;
/*     */ import javafx.scene.control.TableView;
/*     */ import javafx.scene.image.Image;
/*     */ import javafx.scene.image.ImageView;
/*     */ import javafx.scene.input.MouseEvent;
/*     */ import javafx.scene.layout.StackPane;
/*     */ import javafx.scene.paint.Color;
/*     */ import javafx.scene.paint.Paint;
/*     */ import javafx.scene.shape.Rectangle;
/*     */ import javafx.stage.WindowEvent;
/*     */ import org.controlsfx.control.spreadsheet.Picker;
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
/*     */ public class VerticalHeader
/*     */   extends StackPane
/*     */ {
/*     */   public static final int PICKER_SIZE = 16;
/*     */   private static final int DRAG_RECT_HEIGHT = 5;
/*     */   private static final String TABLE_ROW_KEY = "TableRow";
/*     */   private static final String PICKER_INDEX = "PickerIndex";
/*     */   private static final String TABLE_LABEL_KEY = "Label";
/*  78 */   private static final Image pinImage = new Image(SpreadsheetView.class.getResource("pinSpreadsheetView.png").toExternalForm());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final SpreadsheetHandle handle;
/*     */ 
/*     */ 
/*     */   
/*     */   private final SpreadsheetView spreadsheetView;
/*     */ 
/*     */ 
/*     */   
/*     */   private double horizontalHeaderHeight;
/*     */ 
/*     */ 
/*     */   
/*  95 */   private final DoubleProperty innerVerticalHeaderWidth = (DoubleProperty)new SimpleDoubleProperty();
/*     */   
/*     */   private Rectangle clip;
/*     */   
/*     */   private ContextMenu blankContextMenu;
/* 100 */   private double lastY = 0.0D;
/* 101 */   private static double dragAnchorY = 0.0D;
/*     */ 
/*     */   
/* 104 */   private final List<Rectangle> dragRects = new ArrayList<>();
/* 105 */   private int dragRectCount = 0;
/* 106 */   private final List<Label> labelList = new ArrayList<>();
/* 107 */   private int labelCount = 0;
/*     */ 
/*     */   
/*     */   private GridViewSkin skin;
/*     */   
/*     */   private boolean resizing = false;
/*     */   
/*     */   private final Stack<Label> pickerPile;
/*     */   
/*     */   private final Stack<Label> pickerUsed;
/*     */   
/* 118 */   private final BitSet selectedRows = new BitSet();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final EventHandler<MouseEvent> rectMousePressed;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final EventHandler<MouseEvent> rectMouseDragged;
/*     */ 
/*     */ 
/*     */   
/*     */   private final EventHandler<MouseEvent> rectMouseReleased;
/*     */ 
/*     */ 
/*     */   
/*     */   private final EventHandler<MouseEvent> pickerMouseEvent;
/*     */ 
/*     */ 
/*     */   
/*     */   private final InvalidationListener layout;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void init(GridViewSkin skin, HorizontalHeader horizontalHeader) {
/* 146 */     this.skin = skin;
/*     */     
/* 148 */     horizontalHeader.heightProperty().addListener(new ChangeListener<Number>()
/*     */         {
/*     */           public void changed(ObservableValue<? extends Number> arg0, Number oldHeight, Number newHeight) {
/* 151 */             VerticalHeader.this.horizontalHeaderHeight = newHeight.doubleValue();
/* 152 */             VerticalHeader.this.requestLayout();
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 157 */     this.handle.getView().gridProperty().addListener(this.layout);
/* 158 */     this.handle.getView().hiddenRowsProperty().addListener(this.layout);
/* 159 */     this.handle.getView().hiddenColumnsProperty().addListener(this.layout);
/*     */ 
/*     */     
/* 162 */     this.clip = new Rectangle(getVerticalHeaderWidth(), snapSize(((TableView)skin.getSkinnable()).getHeight()));
/* 163 */     this.clip.relocate(snappedTopInset(), snappedLeftInset());
/* 164 */     this.clip.setSmooth(false);
/* 165 */     this.clip.heightProperty().bind((ObservableValue)((TableView)skin.getSkinnable()).heightProperty());
/* 166 */     this.clip.widthProperty().bind((ObservableValue)this.innerVerticalHeaderWidth);
/* 167 */     setClip((Node)this.clip);
/*     */ 
/*     */     
/* 170 */     this.spreadsheetView.showRowHeaderProperty().addListener(this.layout);
/*     */ 
/*     */ 
/*     */     
/* 174 */     this.spreadsheetView.showColumnHeaderProperty().addListener(this.layout);
/* 175 */     this.spreadsheetView.getFixedRows().addListener(this.layout);
/* 176 */     this.spreadsheetView.fixingRowsAllowedProperty().addListener(this.layout);
/* 177 */     this.spreadsheetView.rowHeaderWidthProperty().addListener(this.layout);
/*     */ 
/*     */     
/* 180 */     this.spreadsheetView.heightProperty().addListener(this.layout);
/*     */ 
/*     */     
/* 183 */     this.spreadsheetView.getRowPickers().addListener(this.layout);
/*     */ 
/*     */ 
/*     */     
/* 187 */     skin.getSelectedRows().addListener(this.layout);
/*     */     
/* 189 */     this.blankContextMenu = new ContextMenu();
/*     */   }
/*     */   
/*     */   public double getVerticalHeaderWidth() {
/* 193 */     return this.innerVerticalHeaderWidth.get();
/*     */   }
/*     */   
/*     */   public ReadOnlyDoubleProperty verticalHeaderWidthProperty() {
/* 197 */     return (ReadOnlyDoubleProperty)this.innerVerticalHeaderWidth;
/*     */   }
/*     */   
/*     */   public double computeHeaderWidth() {
/* 201 */     double width = 0.0D;
/* 202 */     if (!this.spreadsheetView.getRowPickers().isEmpty()) {
/* 203 */       width += 16.0D;
/*     */     }
/* 205 */     if (this.spreadsheetView.isShowRowHeader()) {
/* 206 */       width += this.spreadsheetView.getRowHeaderWidth();
/*     */     }
/* 208 */     return width;
/*     */   }
/*     */   
/*     */   void clearSelectedRows() {
/* 212 */     this.selectedRows.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void layoutChildren() {
/* 217 */     if (this.resizing) {
/*     */       return;
/*     */     }
/* 220 */     if ((this.spreadsheetView.isShowRowHeader() || !this.spreadsheetView.getRowPickers().isEmpty()) && this.skin.getCellsSize() > 0) {
/*     */       
/* 222 */       double x = snappedLeftInset();
/*     */ 
/*     */ 
/*     */       
/* 226 */       this.pickerPile.addAll(this.pickerUsed.subList(0, this.pickerUsed.size()));
/* 227 */       this.pickerUsed.clear();
/*     */       
/* 229 */       this.labelCount = 0;
/* 230 */       this.dragRectCount = 0;
/* 231 */       if (!this.spreadsheetView.getRowPickers().isEmpty()) {
/* 232 */         this.innerVerticalHeaderWidth.setValue(Integer.valueOf(16));
/* 233 */         x += 16.0D;
/*     */       } else {
/* 235 */         this.innerVerticalHeaderWidth.setValue(Integer.valueOf(0));
/*     */       } 
/* 237 */       if (this.spreadsheetView.isShowRowHeader()) {
/* 238 */         this.innerVerticalHeaderWidth.setValue(Double.valueOf(getVerticalHeaderWidth() + this.spreadsheetView.getRowHeaderWidth()));
/*     */       }
/*     */       
/* 241 */       getChildren().clear();
/*     */       
/* 243 */       int cellSize = this.skin.getCellsSize();
/*     */ 
/*     */ 
/*     */       
/* 247 */       addVisibleRows(x, cellSize);
/*     */ 
/*     */       
/* 250 */       addFixedRows(x, cellSize);
/*     */ 
/*     */       
/* 253 */       if (this.spreadsheetView.showColumnHeaderProperty().get()) {
/* 254 */         Label label = getLabel((Integer)null);
/* 255 */         label.setOnMousePressed(event -> this.spreadsheetView.getSelectionModel().selectAll());
/*     */ 
/*     */         
/* 258 */         label.setText("");
/* 259 */         label.resize(this.spreadsheetView.getRowHeaderWidth(), this.horizontalHeaderHeight);
/* 260 */         label.layoutYProperty().unbind();
/* 261 */         label.setLayoutY(0.0D);
/* 262 */         label.setLayoutX(x);
/* 263 */         label.getStyleClass().clear();
/* 264 */         label.setContextMenu(this.blankContextMenu);
/* 265 */         getChildren().add(label);
/*     */       } 
/*     */       
/* 268 */       ScrollBar hbar = this.handle.getCellsViewSkin().getHBar();
/*     */       
/* 270 */       if (hbar.isVisible()) {
/*     */         
/* 272 */         Label label = getLabel((Integer)null);
/* 273 */         label.getProperties().put("TableRow", null);
/* 274 */         label.setText("");
/* 275 */         label.resize(getVerticalHeaderWidth(), hbar.getHeight());
/* 276 */         label.layoutYProperty().unbind();
/* 277 */         label.relocate(snappedLeftInset(), getHeight() - hbar.getHeight());
/* 278 */         label.getStyleClass().clear();
/* 279 */         label.setContextMenu(this.blankContextMenu);
/* 280 */         getChildren().add(label);
/*     */       } 
/*     */     } else {
/* 283 */       getChildren().clear();
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
/*     */   public static boolean isFixedRowEmpty(SpreadsheetView spreadsheetView) {
/* 295 */     for (Integer fixedRow : spreadsheetView.getFixedRows()) {
/* 296 */       if (!spreadsheetView.getHiddenRows().get(fixedRow.intValue())) {
/* 297 */         return false;
/*     */       }
/*     */     } 
/* 300 */     return true;
/*     */   }
/*     */   
/*     */   private void addFixedRows(double x, int cellSize) {
/* 304 */     double spaceUsedByFixedRows = 0.0D;
/*     */ 
/*     */ 
/*     */     
/* 308 */     ObservableSet<Integer> observableSet = this.handle.getCellsViewSkin().getCurrentlyFixedRow();
/*     */     
/* 310 */     if (!isFixedRowEmpty(this.spreadsheetView) && cellSize != 0) {
/* 311 */       for (int j = 0; j < this.spreadsheetView.getFixedRows().size(); j++) {
/*     */         
/* 313 */         int modelRow = ((Integer)this.spreadsheetView.getFixedRows().get(j)).intValue();
/* 314 */         if (!this.spreadsheetView.getHiddenRows().get(modelRow)) {
/*     */ 
/*     */ 
/*     */           
/* 318 */           int viewRow = this.spreadsheetView.getFilteredRow(modelRow);
/* 319 */           if (!observableSet.contains(Integer.valueOf(viewRow))) {
/*     */             break;
/*     */           }
/*     */           
/* 323 */           double rowHeight = this.skin.getRowHeight(viewRow);
/*     */           
/* 325 */           double y = this.spreadsheetView.showColumnHeaderProperty().get() ? (snappedTopInset() + this.horizontalHeaderHeight + spaceUsedByFixedRows) : (snappedTopInset() + spaceUsedByFixedRows);
/*     */           
/* 327 */           if (this.spreadsheetView.getRowPickers().containsKey(Integer.valueOf(modelRow))) {
/* 328 */             Label picker = getPicker((Picker)this.spreadsheetView.getRowPickers().get(Integer.valueOf(modelRow)));
/* 329 */             picker.resize(16.0D, rowHeight);
/* 330 */             picker.layoutYProperty().unbind();
/* 331 */             picker.setLayoutY(y);
/* 332 */             getChildren().add(picker);
/*     */           } 
/* 334 */           if (this.spreadsheetView.isShowRowHeader()) {
/* 335 */             Label label = getLabel(Integer.valueOf(viewRow));
/* 336 */             GridRow row = this.skin.getRowIndexed(viewRow);
/* 337 */             label.getProperties().put("TableRow", row);
/* 338 */             label.setText(getRowHeader(viewRow));
/* 339 */             label.resize(this.spreadsheetView.getRowHeaderWidth(), rowHeight);
/* 340 */             label.setContextMenu(getRowContextMenu(Integer.valueOf(viewRow)));
/* 341 */             if (row != null) {
/* 342 */               label.layoutYProperty().bind((ObservableValue)row.layoutYProperty().add(this.horizontalHeaderHeight).add((ObservableNumberValue)row.verticalShift));
/*     */             }
/* 344 */             label.setLayoutX(x);
/* 345 */             ObservableList<String> css = label.getStyleClass();
/* 346 */             if (this.skin.getSelectedRows().contains(Integer.valueOf(viewRow))) {
/* 347 */               css.addAll((Object[])new String[] { "selected" });
/*     */             } else {
/* 349 */               css.removeAll((Object[])new String[] { "selected" });
/*     */             } 
/* 351 */             css.addAll((Object[])new String[] { "fixed" });
/* 352 */             getChildren().add(label);
/*     */             
/* 354 */             if (this.spreadsheetView.getGrid().isRowResizable(viewRow)) {
/* 355 */               Rectangle dragRect = getDragRect();
/* 356 */               dragRect.getProperties().put("TableRow", row);
/* 357 */               dragRect.getProperties().put("Label", label);
/* 358 */               dragRect.setWidth(label.getWidth());
/* 359 */               dragRect.relocate(snappedLeftInset() + x, y + rowHeight - 5.0D);
/* 360 */               getChildren().add(dragRect);
/*     */             } 
/*     */           } 
/* 363 */           spaceUsedByFixedRows += this.skin.getRowHeight(viewRow);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void addVisibleRows(double x, int cellSize) {
/* 372 */     double y = snappedTopInset();
/*     */     
/* 374 */     if (this.spreadsheetView.showColumnHeaderProperty().get()) {
/* 375 */       y += this.horizontalHeaderHeight;
/*     */     }
/*     */ 
/*     */     
/* 379 */     if (cellSize != 0) {
/* 380 */       y += this.skin.getRow(0).getLocalToParentTransform().getTy();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 385 */     int viewRowCount = this.skin.getItemCount();
/*     */     
/* 387 */     int i = 0;
/*     */     
/* 389 */     GridRow row = this.skin.getRow(i);
/*     */     
/* 391 */     double fixedRowHeight = this.skin.getFixedRowHeight();
/* 392 */     double rowHeaderWidth = this.spreadsheetView.getRowHeaderWidth();
/*     */ 
/*     */ 
/*     */     
/* 396 */     while (cellSize != 0 && row != null && row.getIndex() < viewRowCount) {
/* 397 */       int rowIndex = row.getIndex();
/* 398 */       double height = row.getHeight();
/*     */ 
/*     */ 
/*     */       
/* 402 */       int modelRow = this.spreadsheetView.getFilteredSourceIndex(rowIndex);
/* 403 */       if (row.getLayoutY() >= fixedRowHeight && this.spreadsheetView.getRowPickers().containsKey(Integer.valueOf(modelRow))) {
/* 404 */         Label picker = getPicker((Picker)this.spreadsheetView.getRowPickers().get(Integer.valueOf(modelRow)));
/* 405 */         picker.resize(16.0D, height);
/* 406 */         picker.layoutYProperty().bind((ObservableValue)row.layoutYProperty().add(this.horizontalHeaderHeight));
/* 407 */         getChildren().add(picker);
/*     */       } 
/*     */       
/* 410 */       if (this.spreadsheetView.isShowRowHeader()) {
/* 411 */         Label label = getLabel(Integer.valueOf(rowIndex));
/* 412 */         label.getProperties().put("TableRow", row);
/* 413 */         label.setText(getRowHeader(rowIndex));
/* 414 */         label.resize(rowHeaderWidth, height);
/* 415 */         label.setLayoutX(x);
/* 416 */         label.layoutYProperty().bind((ObservableValue)row.layoutYProperty().add(this.horizontalHeaderHeight));
/* 417 */         label.setContextMenu(getRowContextMenu(Integer.valueOf(rowIndex)));
/*     */         
/* 419 */         getChildren().add(label);
/*     */         
/* 421 */         ObservableList<String> css = label.getStyleClass();
/* 422 */         if (this.skin.getSelectedRows().contains(Integer.valueOf(rowIndex))) {
/* 423 */           css.addAll((Object[])new String[] { "selected" });
/*     */         } else {
/* 425 */           css.removeAll((Object[])new String[] { "selected" });
/*     */         } 
/* 427 */         if (this.spreadsheetView.getFixedRows().contains(Integer.valueOf(modelRow))) {
/* 428 */           css.addAll((Object[])new String[] { "fixed" });
/*     */         } else {
/* 430 */           css.removeAll((Object[])new String[] { "fixed" });
/*     */         } 
/*     */         
/* 433 */         y += height;
/*     */ 
/*     */         
/* 436 */         if (this.spreadsheetView.getGrid().isRowResizable(modelRow)) {
/* 437 */           Rectangle dragRect = getDragRect();
/* 438 */           dragRect.getProperties().put("TableRow", row);
/* 439 */           dragRect.getProperties().put("Label", label);
/* 440 */           dragRect.setWidth(label.getWidth());
/* 441 */           dragRect.relocate(snappedLeftInset() + x, y - 5.0D);
/* 442 */           getChildren().add(dragRect);
/*     */         } 
/*     */       } 
/* 445 */       row = this.skin.getRow(++i);
/*     */     } 
/*     */   }
/*     */   public VerticalHeader(SpreadsheetHandle handle) {
/* 449 */     this.rectMousePressed = new EventHandler<MouseEvent>()
/*     */       {
/*     */         public void handle(MouseEvent me)
/*     */         {
/* 453 */           if (me.getClickCount() == 2 && me.isPrimaryButtonDown()) {
/* 454 */             Rectangle rect = (Rectangle)me.getSource();
/* 455 */             GridRow row = (GridRow)rect.getProperties().get("TableRow");
/* 456 */             VerticalHeader.this.skin.resizeRowToFitContent(VerticalHeader.this.spreadsheetView.getModelRow(row.getIndex()));
/* 457 */             VerticalHeader.this.requestLayout();
/*     */           }
/*     */           else {
/*     */             
/* 461 */             VerticalHeader.dragAnchorY = me.getSceneY();
/* 462 */             VerticalHeader.this.resizing = true;
/*     */           } 
/* 464 */           me.consume();
/*     */         }
/*     */       };
/*     */     
/* 468 */     this.rectMouseDragged = new EventHandler<MouseEvent>()
/*     */       {
/*     */         public void handle(MouseEvent me) {
/* 471 */           Rectangle rect = (Rectangle)me.getSource();
/* 472 */           GridRow row = (GridRow)rect.getProperties().get("TableRow");
/* 473 */           Label label = (Label)rect.getProperties().get("Label");
/* 474 */           if (row != null) {
/* 475 */             VerticalHeader.this.rowResizing(row, label, me);
/*     */           }
/* 477 */           me.consume();
/*     */         }
/*     */       };
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
/* 502 */     this.rectMouseReleased = new EventHandler<MouseEvent>()
/*     */       {
/*     */         public void handle(MouseEvent me) {
/* 505 */           VerticalHeader.this.lastY = 0.0D;
/* 506 */           VerticalHeader.this.resizing = false;
/* 507 */           VerticalHeader.this.requestLayout();
/* 508 */           me.consume();
/*     */           
/* 510 */           Rectangle rect = (Rectangle)me.getSource();
/* 511 */           GridRow row = (GridRow)rect.getProperties().get("TableRow");
/* 512 */           if (VerticalHeader.this.selectedRows.get(row.getIndex())) {
/* 513 */             double height = row.getHeight(); int i;
/* 514 */             for (i = VerticalHeader.this.selectedRows.nextSetBit(0); i >= 0; i = VerticalHeader.this.selectedRows.nextSetBit(i + 1)) {
/* 515 */               VerticalHeader.this.skin.rowHeightMap.put(Integer.valueOf(VerticalHeader.this.spreadsheetView.getModelRow(i)), Double.valueOf(height));
/* 516 */               Event.fireEvent((EventTarget)VerticalHeader.this.spreadsheetView, (Event)new SpreadsheetView.RowHeightEvent(VerticalHeader.this.spreadsheetView.getModelRow(i), height));
/*     */             } 
/*     */           } 
/*     */         }
/*     */       };
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
/* 604 */     this.pickerMouseEvent = new EventHandler<MouseEvent>()
/*     */       {
/*     */         public void handle(MouseEvent mouseEvent)
/*     */         {
/* 608 */           Label picker = (Label)mouseEvent.getSource();
/*     */           
/* 610 */           ((Picker)picker.getProperties().get("PickerIndex")).onClick();
/*     */         }
/*     */       };
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
/* 699 */     this.layout = (arg0 -> requestLayout());
/*     */     this.handle = handle;
/*     */     this.spreadsheetView = handle.getView();
/*     */     this.pickerPile = new Stack<>();
/*     */     this.pickerUsed = new Stack<>();
/*     */   }
/*     */   
/*     */   private void rowResizing(GridRow gridRow, Label label, MouseEvent me) {
/*     */     double draggedY = me.getSceneY() - dragAnchorY;
/*     */     if (gridRow.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT)
/*     */       draggedY = -draggedY; 
/*     */     double delta = draggedY - this.lastY;
/*     */     Double newHeight = Double.valueOf(gridRow.getHeight() + delta);
/*     */     if (newHeight.doubleValue() < 0.0D)
/*     */       return; 
/*     */     (this.handle.getCellsViewSkin()).rowHeightMap.put(Integer.valueOf(this.spreadsheetView.getModelRow(gridRow.getIndex())), newHeight);
/*     */     Event.fireEvent((EventTarget)this.spreadsheetView, (Event)new SpreadsheetView.RowHeightEvent(this.spreadsheetView.getModelRow(gridRow.getIndex()), newHeight.doubleValue()));
/*     */     label.resize(this.spreadsheetView.getRowHeaderWidth(), newHeight.doubleValue());
/*     */     gridRow.setPrefHeight(newHeight.doubleValue());
/*     */     gridRow.requestLayout();
/*     */     this.lastY = draggedY;
/*     */   }
/*     */   
/*     */   private Label getLabel(Integer row) {
/*     */     Label label;
/*     */     if (this.labelList.isEmpty() || this.labelList.size() <= this.labelCount) {
/*     */       label = new Label();
/*     */       this.labelList.add(label);
/*     */     } else {
/*     */       label = this.labelList.get(this.labelCount);
/*     */     } 
/*     */     this.labelCount++;
/*     */     label.setOnMousePressed((row == null) ? null : (event -> {
/*     */           if (event.isPrimaryButtonDown())
/*     */             if (event.getClickCount() == 2) {
/*     */               this.skin.resizeRowToFitContent(this.spreadsheetView.getModelRow(row.intValue()));
/*     */               requestLayout();
/*     */             } else {
/*     */               headerClicked(row.intValue(), event);
/*     */             }  
/*     */         }));
/*     */     return label;
/*     */   }
/*     */   
/*     */   private void headerClicked(int row, MouseEvent event) {
/*     */     TableView.TableViewSelectionModel<ObservableList<SpreadsheetCell>> sm = this.handle.getGridView().getSelectionModel();
/*     */     int focusedRow = sm.getFocusedIndex();
/*     */     int rowCount = this.handle.getCellsViewSkin().getItemCount();
/*     */     ObservableList<TableColumn<ObservableList<SpreadsheetCell>, ?>> columns = sm.getTableView().getColumns();
/*     */     TableColumn<ObservableList<SpreadsheetCell>, ?> firstColumn = (TableColumn<ObservableList<SpreadsheetCell>, ?>)columns.get(0);
/*     */     TableColumn<ObservableList<SpreadsheetCell>, ?> lastColumn = (TableColumn<ObservableList<SpreadsheetCell>, ?>)columns.get(columns.size() - 1);
/*     */     if (event.isShortcutDown()) {
/*     */       BitSet tempSet = (BitSet)this.selectedRows.clone();
/*     */       sm.selectRange(row, (TableColumnBase)firstColumn, row, (TableColumnBase)lastColumn);
/*     */       this.selectedRows.or(tempSet);
/*     */       this.selectedRows.set(row);
/*     */     } else if (event.isShiftDown() && focusedRow >= 0 && focusedRow < rowCount) {
/*     */       sm.clearSelection();
/*     */       sm.selectRange(focusedRow, (TableColumnBase)firstColumn, row, (TableColumnBase)lastColumn);
/*     */       sm.getTableView().getFocusModel().focus(focusedRow, firstColumn);
/*     */       int min = Math.min(row, focusedRow);
/*     */       int max = Math.max(row, focusedRow);
/*     */       this.selectedRows.set(min, max + 1);
/*     */     } else {
/*     */       sm.clearSelection();
/*     */       sm.selectRange(row, (TableColumnBase)firstColumn, row, (TableColumnBase)lastColumn);
/*     */       sm.getTableView().getFocusModel().focus(row, firstColumn);
/*     */       this.selectedRows.set(row);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Label getPicker(Picker picker) {
/*     */     Label pickerLabel;
/*     */     if (this.pickerPile.isEmpty()) {
/*     */       pickerLabel = new Label();
/*     */       picker.getStyleClass().addListener(this.layout);
/*     */       pickerLabel.setOnMouseClicked(this.pickerMouseEvent);
/*     */     } else {
/*     */       pickerLabel = this.pickerPile.pop();
/*     */     } 
/*     */     this.pickerUsed.push(pickerLabel);
/*     */     pickerLabel.getStyleClass().setAll((Collection)picker.getStyleClass());
/*     */     pickerLabel.getProperties().put("PickerIndex", picker);
/*     */     return pickerLabel;
/*     */   }
/*     */   
/*     */   private Rectangle getDragRect() {
/*     */     if (this.dragRects.isEmpty() || this.dragRects.size() <= this.dragRectCount) {
/*     */       Rectangle rect = new Rectangle();
/*     */       rect.setWidth(getVerticalHeaderWidth());
/*     */       rect.setHeight(5.0D);
/*     */       rect.setFill((Paint)Color.TRANSPARENT);
/*     */       rect.setSmooth(false);
/*     */       rect.setOnMousePressed(this.rectMousePressed);
/*     */       rect.setOnMouseDragged(this.rectMouseDragged);
/*     */       rect.setOnMouseReleased(this.rectMouseReleased);
/*     */       rect.setCursor(Cursor.V_RESIZE);
/*     */       this.dragRects.add(rect);
/*     */       this.dragRectCount++;
/*     */       return rect;
/*     */     } 
/*     */     return this.dragRects.get(this.dragRectCount++);
/*     */   }
/*     */   
/*     */   private ContextMenu getRowContextMenu(final Integer row) {
/*     */     if (this.spreadsheetView.isRowFixable(row.intValue())) {
/*     */       ContextMenu contextMenu = new ContextMenu();
/*     */       final MenuItem fixItem = new MenuItem(Localization.localize(Localization.asKey("spreadsheet.verticalheader.menu.fix")));
/*     */       contextMenu.setOnShowing(new EventHandler<WindowEvent>() {
/*     */             public void handle(WindowEvent event) {
/*     */               if (VerticalHeader.this.spreadsheetView.getFixedRows().contains(Integer.valueOf(VerticalHeader.this.spreadsheetView.getFilteredSourceIndex(row.intValue())))) {
/*     */                 fixItem.setText(Localization.localize(Localization.asKey("spreadsheet.verticalheader.menu.unfix")));
/*     */               } else {
/*     */                 fixItem.setText(Localization.localize(Localization.asKey("spreadsheet.verticalheader.menu.fix")));
/*     */               } 
/*     */             }
/*     */           });
/*     */       fixItem.setGraphic((Node)new ImageView(pinImage));
/*     */       fixItem.setOnAction(new EventHandler<ActionEvent>() {
/*     */             public void handle(ActionEvent arg0) {
/*     */               Integer modelRow = Integer.valueOf(VerticalHeader.this.spreadsheetView.getFilteredSourceIndex(row.intValue()));
/*     */               if (VerticalHeader.this.spreadsheetView.getFixedRows().contains(modelRow)) {
/*     */                 VerticalHeader.this.spreadsheetView.getFixedRows().remove(modelRow);
/*     */               } else {
/*     */                 VerticalHeader.this.spreadsheetView.getFixedRows().add(modelRow);
/*     */               } 
/*     */             }
/*     */           });
/*     */       contextMenu.getItems().add(fixItem);
/*     */       return contextMenu;
/*     */     } 
/*     */     return this.blankContextMenu;
/*     */   }
/*     */   
/*     */   private String getRowHeader(int index) {
/*     */     int newIndex = this.spreadsheetView.getFilteredSourceIndex(index);
/*     */     return (this.spreadsheetView.getGrid().getRowHeaders().size() > newIndex) ? (String)this.spreadsheetView.getGrid().getRowHeaders().get(newIndex) : String.valueOf(newIndex + 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\spreadsheet\VerticalHeader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */