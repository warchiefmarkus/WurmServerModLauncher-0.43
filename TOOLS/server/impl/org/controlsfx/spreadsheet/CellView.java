/*     */ package impl.org.controlsfx.spreadsheet;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.logging.Logger;
/*     */ import javafx.animation.FadeTransition;
/*     */ import javafx.application.Platform;
/*     */ import javafx.beans.binding.When;
/*     */ import javafx.beans.value.ChangeListener;
/*     */ import javafx.beans.value.ObservableBooleanValue;
/*     */ import javafx.beans.value.ObservableNumberValue;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.beans.value.WeakChangeListener;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.collections.SetChangeListener;
/*     */ import javafx.collections.WeakSetChangeListener;
/*     */ import javafx.event.Event;
/*     */ import javafx.event.EventHandler;
/*     */ import javafx.event.WeakEventHandler;
/*     */ import javafx.geometry.Side;
/*     */ import javafx.scene.Cursor;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.ContentDisplay;
/*     */ import javafx.scene.control.ContextMenu;
/*     */ import javafx.scene.control.Control;
/*     */ import javafx.scene.control.SelectionMode;
/*     */ import javafx.scene.control.Skin;
/*     */ import javafx.scene.control.TableCell;
/*     */ import javafx.scene.control.TableColumnBase;
/*     */ import javafx.scene.control.TablePosition;
/*     */ import javafx.scene.control.TablePositionBase;
/*     */ import javafx.scene.control.TableView;
/*     */ import javafx.scene.control.Tooltip;
/*     */ import javafx.scene.image.ImageView;
/*     */ import javafx.scene.input.DragEvent;
/*     */ import javafx.scene.input.Dragboard;
/*     */ import javafx.scene.input.MouseButton;
/*     */ import javafx.scene.input.MouseEvent;
/*     */ import javafx.scene.input.TransferMode;
/*     */ import javafx.scene.layout.Region;
/*     */ import javafx.util.Duration;
/*     */ import org.controlsfx.control.spreadsheet.Filter;
/*     */ import org.controlsfx.control.spreadsheet.SpreadsheetCell;
/*     */ import org.controlsfx.control.spreadsheet.SpreadsheetCellEditor;
/*     */ import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
/*     */ import org.controlsfx.control.spreadsheet.SpreadsheetColumn;
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
/*     */ public class CellView
/*     */   extends TableCell<ObservableList<SpreadsheetCell>, SpreadsheetCell>
/*     */ {
/*     */   final SpreadsheetHandle handle;
/*     */   private Tooltip tooltip;
/*     */   private EventHandler<DragEvent> dragOverHandler;
/*     */   private EventHandler<DragEvent> dragDropHandler;
/*     */   private static final String ANCHOR_PROPERTY_KEY = "table.anchor";
/*     */   private static final int TOOLTIP_MAX_WIDTH = 400;
/*  92 */   private static final Duration FADE_DURATION = Duration.millis(200.0D); private final ChangeListener<Node> graphicListener; private final WeakChangeListener<Node> weakGraphicListener; private final SetChangeListener<String> styleClassListener; private final WeakSetChangeListener<String> weakStyleClassListener; private ChangeListener<String> styleListener;
/*     */   
/*     */   static TablePositionBase<?> getAnchor(Control table, TablePositionBase<?> focusedCell) {
/*  95 */     return hasAnchor(table) ? (TablePositionBase)table.getProperties().get("table.anchor") : focusedCell;
/*     */   }
/*     */   private WeakChangeListener<String> weakStyleListener; private final EventHandler<MouseEvent> startFullDragEventHandler; private final EventHandler<MouseEvent> dragMouseEventHandler; private final ChangeListener<SpreadsheetCell> itemChangeListener; private final EventHandler<MouseEvent> actionEventHandler; private final WeakEventHandler weakActionhandler;
/*     */   static boolean hasAnchor(Control table) {
/*  99 */     return (table.getProperties().get("table.anchor") != null);
/*     */   }
/*     */   
/*     */   static void setAnchor(Control table, TablePositionBase anchor) {
/* 103 */     if (table != null && anchor == null) {
/* 104 */       removeAnchor(table);
/*     */     } else {
/* 106 */       table.getProperties().put("table.anchor", anchor);
/*     */     } 
/*     */   }
/*     */   
/*     */   static void removeAnchor(Control table) {
/* 111 */     table.getProperties().remove("table.anchor");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CellView(SpreadsheetHandle handle)
/*     */   {
/* 499 */     this.graphicListener = new ChangeListener<Node>()
/*     */       {
/*     */         public void changed(ObservableValue<? extends Node> arg0, Node arg1, Node newGraphic) {
/* 502 */           CellView.this.setCellGraphic((SpreadsheetCell)CellView.this.getItem());
/*     */         }
/*     */       };
/*     */     
/* 506 */     this.weakGraphicListener = new WeakChangeListener(this.graphicListener);
/*     */     
/* 508 */     this.styleClassListener = new SetChangeListener<String>()
/*     */       {
/*     */         public void onChanged(SetChangeListener.Change<? extends String> arg0) {
/* 511 */           if (arg0.wasAdded()) {
/* 512 */             CellView.this.getStyleClass().add(arg0.getElementAdded());
/* 513 */           } else if (arg0.wasRemoved()) {
/* 514 */             CellView.this.getStyleClass().remove(arg0.getElementRemoved());
/*     */           } 
/*     */         }
/*     */       };
/*     */     
/* 519 */     this.weakStyleClassListener = new WeakSetChangeListener(this.styleClassListener);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 618 */     this.startFullDragEventHandler = new EventHandler<MouseEvent>()
/*     */       {
/*     */         public void handle(MouseEvent arg0) {
/* 621 */           if (CellView.this.handle.getGridView().getSelectionModel().getSelectionMode().equals(SelectionMode.MULTIPLE)) {
/* 622 */             CellView.setAnchor((Control)CellView.this.getTableView(), (TablePositionBase)CellView.this.getTableView().getFocusModel().getFocusedCell());
/* 623 */             CellView.this.startFullDrag();
/*     */           } 
/*     */         }
/*     */       };
/*     */     
/* 628 */     this.dragMouseEventHandler = new EventHandler<MouseEvent>()
/*     */       {
/*     */         public void handle(MouseEvent arg0) {
/* 631 */           CellView.this.dragSelect(arg0);
/*     */         }
/*     */       };
/*     */     
/* 635 */     this.itemChangeListener = new ChangeListener<SpreadsheetCell>()
/*     */       {
/*     */         
/*     */         public void changed(ObservableValue<? extends SpreadsheetCell> arg0, SpreadsheetCell oldItem, SpreadsheetCell newItem)
/*     */         {
/* 640 */           if (oldItem != null) {
/* 641 */             oldItem.getStyleClass().removeListener((SetChangeListener)CellView.this.weakStyleClassListener);
/* 642 */             oldItem.graphicProperty().removeListener((ChangeListener)CellView.this.weakGraphicListener);
/*     */             
/* 644 */             if (oldItem.styleProperty() != null) {
/* 645 */               oldItem.styleProperty().removeListener((ChangeListener)CellView.this.weakStyleListener);
/*     */             }
/*     */           } 
/* 648 */           if (newItem != null) {
/* 649 */             CellView.this.getStyleClass().clear();
/* 650 */             CellView.this.getStyleClass().setAll((Collection)newItem.getStyleClass());
/*     */             
/* 652 */             newItem.getStyleClass().addListener((SetChangeListener)CellView.this.weakStyleClassListener);
/* 653 */             CellView.this.setCellGraphic(newItem);
/* 654 */             newItem.graphicProperty().addListener((ChangeListener)CellView.this.weakGraphicListener);
/*     */             
/* 656 */             if (newItem.styleProperty() != null) {
/* 657 */               CellView.this.initStyleListener();
/* 658 */               newItem.styleProperty().addListener((ChangeListener)CellView.this.weakStyleListener);
/* 659 */               CellView.this.setStyle(newItem.getStyle());
/*     */             } else {
/*     */               
/* 662 */               CellView.this.setStyle(null);
/*     */             } 
/*     */           } 
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 672 */     this.actionEventHandler = new EventHandler<MouseEvent>()
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void handle(MouseEvent event)
/*     */         {
/* 680 */           if (CellView.this.getItem() != null && ((SpreadsheetCell)CellView.this.getItem()).hasPopup() && MouseButton.PRIMARY.equals(event.getButton()) && (CellView.this
/* 681 */             .getFilter() == null || !CellView.this.getFilter().getMenuButton().isShowing())) {
/* 682 */             ContextMenu menu = new ContextMenu();
/* 683 */             menu.getScene().getStylesheets().add(SpreadsheetView.class.getResource("spreadsheet.css").toExternalForm());
/* 684 */             menu.getStyleClass().add("popup-button");
/* 685 */             menu.getItems().setAll(((SpreadsheetCell)CellView.this.getItem()).getPopupItems());
/* 686 */             menu.show((Node)CellView.this, Side.BOTTOM, 0.0D, 0.0D);
/*     */           } 
/*     */         }
/*     */       };
/* 690 */     this.weakActionhandler = new WeakEventHandler(this.actionEventHandler); this.handle = handle;
/*     */     addEventHandler(MouseEvent.DRAG_DETECTED, (EventHandler)new WeakEventHandler(this.startFullDragEventHandler));
/*     */     setOnMouseDragEntered((EventHandler)new WeakEventHandler(this.dragMouseEventHandler));
/* 693 */     itemProperty().addListener(this.itemChangeListener); } private void initStyleListener() { if (this.styleListener == null) {
/* 694 */       this.styleListener = ((observable, oldValue, newValue) -> styleProperty().set(newValue));
/*     */     }
/*     */ 
/*     */     
/* 698 */     this.weakStyleListener = new WeakChangeListener(this.styleListener); }
/*     */ 
/*     */   
/*     */   public void startEdit() {
/*     */     if (getParent() == null) {
/*     */       updateTableView(null);
/*     */       updateTableRow(null);
/*     */       updateTableColumn(null);
/*     */       return;
/*     */     } 
/*     */     if (!isEditable()) {
/*     */       getTableView().edit(-1, null);
/*     */       return;
/*     */     } 
/*     */     int column = getTableView().getColumns().indexOf(getTableColumn());
/*     */     int row = getIndex();
/*     */     SpreadsheetView spv = this.handle.getView();
/*     */     SpreadsheetView.SpanType type = spv.getSpanType(row, column);
/*     */     if (type == SpreadsheetView.SpanType.NORMAL_CELL || type == SpreadsheetView.SpanType.ROW_VISIBLE) {
/*     */       if (!getTableRow().isManaged())
/*     */         return; 
/*     */       GridCellEditor editor = getEditor((SpreadsheetCell)getItem(), spv);
/*     */       if (editor != null) {
/*     */         super.startEdit();
/*     */         setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
/*     */         editor.startEdit();
/*     */       } else {
/*     */         getTableView().edit(-1, null);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   Filter getFilter() {
/*     */     Filter filter = (getItem() != null) ? ((SpreadsheetColumn)this.handle.getView().getColumns().get(((SpreadsheetCell)getItem()).getColumn())).getFilter() : null;
/*     */     if (filter != null && ((SpreadsheetCell)getItem()).getRowSpan() > 1) {
/*     */       int rowSpan = this.handle.getView().getRowSpan((SpreadsheetCell)getItem(), getIndex());
/*     */       int row = ((SpreadsheetCell)getItem()).getRow();
/*     */       for (int i = row; i < row + rowSpan; i++) {
/*     */         if (this.handle.getView().getFilteredRow() == this.handle.getView().getModelRow(i))
/*     */           return filter; 
/*     */       } 
/*     */       return null;
/*     */     } 
/*     */     return (filter != null && this.handle.getView().getFilteredRow() == this.handle.getView().getModelRow(getIndex())) ? filter : null;
/*     */   }
/*     */   
/*     */   public void commitEdit(SpreadsheetCell newValue) {
/*     */     FadeTransition fadeTransition = new FadeTransition(FADE_DURATION, (Node)this);
/*     */     fadeTransition.setFromValue(0.0D);
/*     */     fadeTransition.setToValue(1.0D);
/*     */     fadeTransition.play();
/*     */     if (!isEditing())
/*     */       return; 
/*     */     super.commitEdit(newValue);
/*     */     setContentDisplay(ContentDisplay.LEFT);
/*     */     updateItem(newValue, false);
/*     */     if (getTableView() != null)
/*     */       getTableView().requestFocus(); 
/*     */   }
/*     */   
/*     */   public void cancelEdit() {
/*     */     if (!isEditing())
/*     */       return; 
/*     */     super.cancelEdit();
/*     */     setContentDisplay(ContentDisplay.LEFT);
/*     */     updateItem((SpreadsheetCell)getItem(), false);
/*     */     GridCellEditor editor = this.handle.getCellsViewSkin().getSpreadsheetCellEditorImpl();
/*     */     if (editor.isEditing())
/*     */       editor.endEdit(false); 
/*     */     if (getTableView() != null)
/*     */       getTableView().requestFocus(); 
/*     */   }
/*     */   
/*     */   public void updateItem(SpreadsheetCell item, boolean empty) {
/*     */     boolean emptyRow = (getTableView().getItems().size() < getIndex() + 1);
/*     */     if (!isEditing())
/*     */       super.updateItem(item, (empty && emptyRow)); 
/*     */     if (empty && isSelected())
/*     */       updateSelected(false); 
/*     */     if (empty && emptyRow) {
/*     */       textProperty().unbind();
/*     */       setText(null);
/*     */       setContentDisplay(null);
/*     */     } else if (!isEditing() && item != null) {
/*     */       show(item);
/*     */       if (item.getGraphic() == null)
/*     */         setGraphic(null); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void show(SpreadsheetCell cell) {
/*     */     textProperty().bind((ObservableValue)cell.textProperty());
/*     */     setCellGraphic(cell);
/*     */     Optional<String> tooltipText = cell.getTooltip();
/*     */     String trimTooltip = tooltipText.isPresent() ? ((String)tooltipText.get()).trim() : null;
/*     */     if (trimTooltip != null && !trimTooltip.isEmpty()) {
/*     */       Tooltip localTooltip = getAvailableTooltip();
/*     */       if (localTooltip != null) {
/*     */         if (!Objects.equals(localTooltip.getText(), trimTooltip))
/*     */           getTooltip().setText(trimTooltip); 
/*     */       } else {
/*     */         getValue(() -> {
/*     */               Tooltip newTooltip = new Tooltip(tooltipText.get());
/*     */               newTooltip.setWrapText(true);
/*     */               newTooltip.setMaxWidth(400.0D);
/*     */               setTooltip(newTooltip);
/*     */             });
/*     */       } 
/*     */     } else {
/*     */       if (getTooltip() != null)
/*     */         this.tooltip = getTooltip(); 
/*     */       setTooltip(null);
/*     */     } 
/*     */     setWrapText(cell.isWrapText());
/*     */     setEditable(cell.hasPopup() ? false : cell.isEditable());
/*     */     if (cell.hasPopup()) {
/*     */       setOnMouseClicked((EventHandler)this.weakActionhandler);
/*     */       setCursor(Cursor.HAND);
/*     */     } else {
/*     */       setOnMouseClicked(null);
/*     */       setCursor(Cursor.DEFAULT);
/*     */     } 
/*     */     if (cell.getCellType().acceptDrop()) {
/*     */       setOnDragOver(getDragOverHandler());
/*     */       setOnDragDropped(getDragDropHandler());
/*     */     } else {
/*     */       setOnDragOver(null);
/*     */       setOnDragDropped(null);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Tooltip getAvailableTooltip() {
/*     */     if (getTooltip() != null)
/*     */       return getTooltip(); 
/*     */     if (this.tooltip != null) {
/*     */       setTooltip(this.tooltip);
/*     */       return this.tooltip;
/*     */     } 
/*     */     return null;
/*     */   }
/*     */   
/*     */   private void setCellGraphic(SpreadsheetCell item) {
/*     */     if (isEditing())
/*     */       return; 
/*     */     Node graphic = item.getGraphic();
/*     */     if (graphic != null) {
/*     */       if (graphic instanceof ImageView) {
/*     */         ImageView image = (ImageView)graphic;
/*     */         image.setCache(true);
/*     */         image.setPreserveRatio(true);
/*     */         image.setSmooth(true);
/*     */         if (image.getImage() != null) {
/*     */           image.fitHeightProperty().bind((ObservableValue)(new When((ObservableBooleanValue)heightProperty().greaterThan(image.getImage().getHeight()))).then(image.getImage().getHeight()).otherwise((ObservableNumberValue)heightProperty()));
/*     */           image.fitWidthProperty().bind((ObservableValue)(new When((ObservableBooleanValue)widthProperty().greaterThan(image.getImage().getWidth()))).then(image.getImage().getWidth()).otherwise((ObservableNumberValue)widthProperty()));
/*     */         } 
/*     */       } else if (graphic instanceof Region && item.getItem() == null) {
/*     */         Region region = (Region)graphic;
/*     */         region.minHeightProperty().bind((ObservableValue)heightProperty());
/*     */         region.minWidthProperty().bind((ObservableValue)widthProperty());
/*     */       } 
/*     */       setGraphic(graphic);
/*     */       if (!getChildren().contains(graphic))
/*     */         getChildren().add(graphic); 
/*     */     } else {
/*     */       setGraphic(null);
/*     */     } 
/*     */   }
/*     */   
/*     */   private GridCellEditor getEditor(SpreadsheetCell cell, SpreadsheetView spv) {
/*     */     SpreadsheetCellType<?> cellType = cell.getCellType();
/*     */     Optional<SpreadsheetCellEditor> cellEditor = spv.getEditor(cellType);
/*     */     if (cellEditor.isPresent()) {
/*     */       GridCellEditor editor = this.handle.getCellsViewSkin().getSpreadsheetCellEditorImpl();
/*     */       if (editor.isEditing()) {
/*     */         if (editor.getModelCell() != null) {
/*     */           StringBuilder builder = new StringBuilder();
/*     */           builder.append("The cell at row ").append(editor.getModelCell().getRow()).append(" and column ").append(editor.getModelCell().getColumn()).append(" was in edition and cell at row ").append(cell.getRow()).append(" and column ").append(cell.getColumn()).append(" requested edition. This situation should not happen as the previous cell should not be in edition.");
/*     */           Logger.getLogger("root").warning(builder.toString());
/*     */         } 
/*     */         editor.endEdit(false);
/*     */       } 
/*     */       editor.updateSpreadsheetCell(this);
/*     */       editor.updateDataCell(cell);
/*     */       editor.updateSpreadsheetCellEditor(cellEditor.get());
/*     */       return editor;
/*     */     } 
/*     */     return null;
/*     */   }
/*     */   
/*     */   private EventHandler<DragEvent> getDragOverHandler() {
/*     */     if (this.dragOverHandler == null)
/*     */       this.dragOverHandler = new EventHandler<DragEvent>() {
/*     */           public void handle(DragEvent event) {
/*     */             Dragboard db = event.getDragboard();
/*     */             if (db.hasFiles()) {
/*     */               event.acceptTransferModes(TransferMode.ANY);
/*     */             } else {
/*     */               event.consume();
/*     */             } 
/*     */           }
/*     */         }; 
/*     */     return this.dragOverHandler;
/*     */   }
/*     */   
/*     */   private EventHandler<DragEvent> getDragDropHandler() {
/*     */     if (this.dragDropHandler == null)
/*     */       this.dragDropHandler = new EventHandler<DragEvent>() {
/*     */           public void handle(DragEvent event) {
/*     */             Dragboard db = event.getDragboard();
/*     */             boolean success = false;
/*     */             if (db.hasFiles() && db.getFiles().size() == 1 && ((SpreadsheetCell)CellView.this.getItem()).getCellType().match(db.getFiles().get(0))) {
/*     */               CellView.this.handle.getView().getGrid().setCellValue(((SpreadsheetCell)CellView.this.getItem()).getRow(), ((SpreadsheetCell)CellView.this.getItem()).getColumn(), ((SpreadsheetCell)CellView.this.getItem()).getCellType().convertValue(db.getFiles().get(0)));
/*     */               success = true;
/*     */             } 
/*     */             event.setDropCompleted(success);
/*     */             event.consume();
/*     */           }
/*     */         }; 
/*     */     return this.dragDropHandler;
/*     */   }
/*     */   
/*     */   private void dragSelect(MouseEvent e) {
/*     */     if (!contains(e.getX(), e.getY()))
/*     */       return; 
/*     */     TableView<ObservableList<SpreadsheetCell>> tableView = getTableView();
/*     */     if (tableView == null)
/*     */       return; 
/*     */     int count = tableView.getItems().size();
/*     */     if (getIndex() >= count)
/*     */       return; 
/*     */     TableView.TableViewSelectionModel<ObservableList<SpreadsheetCell>> sm = tableView.getSelectionModel();
/*     */     if (sm == null)
/*     */       return; 
/*     */     int row = getIndex();
/*     */     int column = tableView.getVisibleLeafIndex(getTableColumn());
/*     */     SpreadsheetCell cell = (SpreadsheetCell)getItem();
/*     */     int rowCell = getIndex() + this.handle.getView().getRowSpan(cell, getIndex()) - 1;
/*     */     int columnCell = this.handle.getView().getViewColumn(cell.getColumn()) + this.handle.getView().getColumnSpan(cell) - 1;
/*     */     TableView.TableViewFocusModel<?> fm = tableView.getFocusModel();
/*     */     if (fm == null)
/*     */       return; 
/*     */     TablePosition tablePosition = fm.getFocusedCell();
/*     */     MouseButton button = e.getButton();
/*     */     if (button == MouseButton.PRIMARY) {
/*     */       TablePositionBase<?> anchor = getAnchor((Control)tableView, (TablePositionBase<?>)tablePosition);
/*     */       int minRow = Math.min(anchor.getRow(), row);
/*     */       minRow = Math.min(minRow, rowCell);
/*     */       int maxRow = Math.max(anchor.getRow(), row);
/*     */       maxRow = Math.max(maxRow, rowCell);
/*     */       int minColumn = Math.min(anchor.getColumn(), column);
/*     */       minColumn = Math.min(minColumn, columnCell);
/*     */       int maxColumn = Math.max(anchor.getColumn(), column);
/*     */       maxColumn = Math.max(maxColumn, columnCell);
/*     */       if (!e.isShortcutDown())
/*     */         sm.clearSelection(); 
/*     */       if (minColumn != -1 && maxColumn != -1)
/*     */         sm.selectRange(minRow, (TableColumnBase)tableView.getVisibleLeafColumn(minColumn), maxRow, (TableColumnBase)tableView.getVisibleLeafColumn(maxColumn)); 
/*     */       setAnchor((Control)tableView, anchor);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void getValue(Runnable runnable) {
/*     */     if (Platform.isFxApplicationThread()) {
/*     */       runnable.run();
/*     */     } else {
/*     */       Platform.runLater(runnable);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Skin<?> createDefaultSkin() {
/*     */     return (Skin<?>)new CellViewSkin(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\spreadsheet\CellView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */