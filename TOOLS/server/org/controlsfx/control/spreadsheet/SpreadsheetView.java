/*      */ package org.controlsfx.control.spreadsheet;
/*      */ 
/*      */ import impl.org.controlsfx.i18n.Localization;
/*      */ import impl.org.controlsfx.spreadsheet.CellView;
/*      */ import impl.org.controlsfx.spreadsheet.FocusModelListener;
/*      */ import impl.org.controlsfx.spreadsheet.GridViewBehavior;
/*      */ import impl.org.controlsfx.spreadsheet.GridViewSkin;
/*      */ import impl.org.controlsfx.spreadsheet.RectangleSelection;
/*      */ import impl.org.controlsfx.spreadsheet.SpreadsheetGridView;
/*      */ import impl.org.controlsfx.spreadsheet.SpreadsheetHandle;
/*      */ import impl.org.controlsfx.spreadsheet.TableViewSpanSelectionModel;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.BitSet;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.IdentityHashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Optional;
/*      */ import java.util.concurrent.FutureTask;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javafx.application.Platform;
/*      */ import javafx.beans.InvalidationListener;
/*      */ import javafx.beans.Observable;
/*      */ import javafx.beans.property.BooleanProperty;
/*      */ import javafx.beans.property.DoubleProperty;
/*      */ import javafx.beans.property.ObjectProperty;
/*      */ import javafx.beans.property.ReadOnlyBooleanProperty;
/*      */ import javafx.beans.property.ReadOnlyObjectProperty;
/*      */ import javafx.beans.property.ReadOnlyObjectWrapper;
/*      */ import javafx.beans.property.SimpleBooleanProperty;
/*      */ import javafx.beans.property.SimpleDoubleProperty;
/*      */ import javafx.beans.property.SimpleObjectProperty;
/*      */ import javafx.beans.value.ChangeListener;
/*      */ import javafx.beans.value.ObservableValue;
/*      */ import javafx.beans.value.WeakChangeListener;
/*      */ import javafx.collections.FXCollections;
/*      */ import javafx.collections.ListChangeListener;
/*      */ import javafx.collections.ObservableList;
/*      */ import javafx.collections.ObservableMap;
/*      */ import javafx.collections.transformation.FilteredList;
/*      */ import javafx.collections.transformation.SortedList;
/*      */ import javafx.event.ActionEvent;
/*      */ import javafx.event.Event;
/*      */ import javafx.event.EventHandler;
/*      */ import javafx.event.EventType;
/*      */ import javafx.event.WeakEventHandler;
/*      */ import javafx.geometry.Pos;
/*      */ import javafx.scene.Node;
/*      */ import javafx.scene.control.ContextMenu;
/*      */ import javafx.scene.control.Control;
/*      */ import javafx.scene.control.Menu;
/*      */ import javafx.scene.control.MenuItem;
/*      */ import javafx.scene.control.SelectionMode;
/*      */ import javafx.scene.control.Skin;
/*      */ import javafx.scene.control.Skinnable;
/*      */ import javafx.scene.control.TableCell;
/*      */ import javafx.scene.control.TableColumn;
/*      */ import javafx.scene.control.TablePosition;
/*      */ import javafx.scene.control.TableView;
/*      */ import javafx.scene.image.Image;
/*      */ import javafx.scene.image.ImageView;
/*      */ import javafx.scene.input.Clipboard;
/*      */ import javafx.scene.input.ClipboardContent;
/*      */ import javafx.scene.input.DataFormat;
/*      */ import javafx.scene.input.KeyCode;
/*      */ import javafx.scene.input.KeyCodeCombination;
/*      */ import javafx.scene.input.KeyCombination;
/*      */ import javafx.scene.input.KeyEvent;
/*      */ import javafx.scene.input.ScrollEvent;
/*      */ import javafx.scene.transform.Scale;
/*      */ import javafx.stage.WindowEvent;
/*      */ import javafx.util.Pair;
/*      */ import org.controlsfx.tools.Utils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class SpreadsheetView
/*      */   extends Control
/*      */ {
/*      */   private static final double DEFAULT_ROW_HEADER_WIDTH = 30.0D;
/*      */   private final SpreadsheetGridView cellsView;
/*      */   
/*      */   public enum SpanType
/*      */   {
/*  334 */     NORMAL_CELL,
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  340 */     COLUMN_SPAN_INVISIBLE,
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  346 */     ROW_SPAN_INVISIBLE,
/*      */ 
/*      */     
/*  349 */     ROW_VISIBLE,
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  354 */     BOTH_INVISIBLE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  366 */   private SimpleObjectProperty<Grid> gridProperty = new SimpleObjectProperty();
/*      */   
/*      */   private DataFormat fmt;
/*  369 */   private final ObservableList<Integer> fixedRows = FXCollections.observableArrayList();
/*  370 */   private final ObservableList<SpreadsheetColumn> fixedColumns = FXCollections.observableArrayList();
/*      */   
/*  372 */   private final BooleanProperty fixingRowsAllowedProperty = (BooleanProperty)new SimpleBooleanProperty(true);
/*  373 */   private final BooleanProperty fixingColumnsAllowedProperty = (BooleanProperty)new SimpleBooleanProperty(true);
/*      */   
/*  375 */   private final BooleanProperty showColumnHeader = (BooleanProperty)new SimpleBooleanProperty(Boolean.valueOf(true), "showColumnHeader", true);
/*  376 */   private final BooleanProperty showRowHeader = (BooleanProperty)new SimpleBooleanProperty(Boolean.valueOf(true), "showRowHeader", true);
/*      */   
/*      */   private BitSet rowFix;
/*      */   
/*  380 */   private final ObservableMap<Integer, Picker> rowPickers = FXCollections.observableHashMap();
/*      */   
/*  382 */   private final ObservableMap<Integer, Picker> columnPickers = FXCollections.observableHashMap();
/*      */ 
/*      */ 
/*      */   
/*  386 */   private ObservableList<SpreadsheetColumn> columns = FXCollections.observableArrayList();
/*  387 */   private Map<SpreadsheetCellType<?>, SpreadsheetCellEditor> editors = new IdentityHashMap<>();
/*      */ 
/*      */   
/*      */   private final SpreadsheetViewSelectionModel selectionModel;
/*      */ 
/*      */   
/*  393 */   private final DoubleProperty rowHeaderWidth = (DoubleProperty)new SimpleDoubleProperty(30.0D);
/*      */ 
/*      */   
/*  396 */   private DoubleProperty zoomFactor = (DoubleProperty)new SimpleDoubleProperty(1.0D);
/*      */   
/*      */   private static final double MIN_ZOOM = 0.1D;
/*      */   private static final double MAX_ZOOM = 2.0D;
/*      */   private static final double STEP_ZOOM = 0.1D;
/*  401 */   private final ObjectProperty<BitSet> hiddenRowsProperty = (ObjectProperty<BitSet>)new SimpleObjectProperty();
/*  402 */   private final ObjectProperty<BitSet> hiddenColumnsProperty = (ObjectProperty<BitSet>)new SimpleObjectProperty();
/*      */   private HashMap<Integer, Integer> rowMap;
/*  404 */   private HashMap<Integer, Integer> columnMap = new HashMap<>();
/*      */ 
/*      */ 
/*      */   
/*      */   private Integer filteredRow;
/*      */ 
/*      */   
/*      */   private FilteredList<ObservableList<SpreadsheetCell>> filteredList;
/*      */ 
/*      */   
/*      */   private SortedList<ObservableList<SpreadsheetCell>> sortedList;
/*      */ 
/*      */   
/*  417 */   private final BitSet columnWidthSet = new BitSet();
/*      */   
/*  419 */   final SpreadsheetHandle handle = new SpreadsheetHandle()
/*      */     {
/*      */       protected SpreadsheetView getView()
/*      */       {
/*  423 */         return SpreadsheetView.this;
/*      */       }
/*      */ 
/*      */       
/*      */       protected GridViewSkin getCellsViewSkin() {
/*  428 */         return SpreadsheetView.this.getCellsViewSkin();
/*      */       }
/*      */ 
/*      */       
/*      */       protected SpreadsheetGridView getGridView() {
/*  433 */         return SpreadsheetView.this.getCellsView();
/*      */       }
/*      */ 
/*      */       
/*      */       protected boolean isColumnWidthSet(int indexColumn) {
/*  438 */         return SpreadsheetView.this.columnWidthSet.get(indexColumn);
/*      */       }
/*      */     };
/*      */   private final ListChangeListener<Integer> fixedRowsListener; private final ListChangeListener<SpreadsheetColumn> fixedColumnsListener; private final ChangeListener<ContextMenu> contextMenuChangeListener;
/*      */   private final EventHandler<WindowEvent> hideContextMenuEventHandler;
/*      */   private final EventHandler<KeyEvent> keyPressedHandler;
/*      */   
/*      */   final GridViewSkin getCellsViewSkin() {
/*  446 */     return (GridViewSkin)this.cellsView.getSkin();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final SpreadsheetGridView getCellsView() {
/*  453 */     return this.cellsView;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void columnWidthSet(int indexColumn) {
/*  463 */     this.columnWidthSet.set(indexColumn);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SpreadsheetView() {
/*  475 */     this(getSampleGrid());
/*  476 */     for (SpreadsheetColumn column : getColumns()) {
/*  477 */       column.setPrefWidth(100.0D);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void layoutChildren() {
/*  596 */     super.layoutChildren();
/*  597 */     Pos pos = Pos.TOP_LEFT;
/*  598 */     double width = getWidth();
/*  599 */     double height = getHeight();
/*  600 */     double top = getInsets().getTop();
/*  601 */     double right = getInsets().getRight();
/*  602 */     double left = getInsets().getLeft();
/*  603 */     double bottom = getInsets().getBottom();
/*  604 */     double contentWidth = (width - left - right) / this.zoomFactor.get();
/*  605 */     double contentHeight = (height - top - bottom) / this.zoomFactor.get();
/*  606 */     layoutInArea((Node)getChildren().get(0), left, top, contentWidth, contentHeight, 0.0D, null, pos
/*      */ 
/*      */         
/*  609 */         .getHpos(), pos
/*  610 */         .getVpos());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isRowHidden(int row) {
/*  620 */     return ((BitSet)this.hiddenRowsProperty.get()).get(row);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BitSet getHiddenRows() {
/*  629 */     return (BitSet)this.hiddenRowsProperty.get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ObjectProperty<BitSet> hiddenRowsProperty() {
/*  638 */     return this.hiddenRowsProperty;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHiddenRows(BitSet hiddenRows) {
/*  648 */     BitSet bitSet = new BitSet(hiddenRows.size());
/*  649 */     bitSet.or(hiddenRows);
/*      */     
/*  651 */     this.hiddenRowsProperty.setValue(bitSet);
/*      */     
/*  653 */     requestLayout();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHiddenColumns(BitSet hiddenColumns) {
/*  663 */     BitSet bitSet = new BitSet(hiddenColumns.size());
/*  664 */     bitSet.or(hiddenColumns);
/*      */     
/*  666 */     this.hiddenColumnsProperty.setValue(bitSet);
/*      */     
/*  668 */     requestLayout();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isColumnHidden(int column) {
/*  680 */     return ((BitSet)this.hiddenColumnsProperty.get()).get(column);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BitSet getHiddenColumns() {
/*  691 */     return (BitSet)this.hiddenColumnsProperty.get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ObjectProperty<BitSet> hiddenColumnsProperty() {
/*  700 */     return this.hiddenColumnsProperty;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getFilteredRow() {
/*  711 */     return (this.filteredRow == null) ? -1 : this.filteredRow.intValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFilteredRow(Integer row) {
/*  721 */     if (row == null || row.intValue() > getGrid().getRowCount()) {
/*  722 */       this.filteredRow = null;
/*      */     } else {
/*  724 */       this.filteredRow = row;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void hideRow(int row) {
/*  734 */     if (getHiddenRows().get(row)) {
/*      */       return;
/*      */     }
/*  737 */     getHiddenRows().set(row, true);
/*  738 */     BitSet bitSet = new BitSet(getHiddenRows().size());
/*  739 */     bitSet.or(getHiddenRows());
/*  740 */     setHiddenRows(bitSet);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void hideColumn(SpreadsheetColumn column) {
/*  749 */     int indexColumn = getColumns().indexOf(column);
/*  750 */     if (getHiddenColumns().get(indexColumn)) {
/*      */       return;
/*      */     }
/*  753 */     getHiddenColumns().set(indexColumn, true);
/*  754 */     BitSet bitSet = new BitSet(getHiddenColumns().size());
/*  755 */     bitSet.or(getHiddenColumns());
/*  756 */     setHiddenColumns(bitSet);
/*      */   }
/*      */   
/*      */   private void computeRowMap() {
/*  760 */     if (getHiddenRows().isEmpty()) {
/*  761 */       this.filteredList.setPredicate(null);
/*      */     } else {
/*  763 */       this.filteredList.setPredicate(new Predicate<ObservableList<SpreadsheetCell>>()
/*      */           {
/*      */             public boolean test(ObservableList<SpreadsheetCell> t) {
/*  766 */               int index = SpreadsheetView.this.getGrid().getRows().indexOf(t);
/*  767 */               return (!SpreadsheetView.this.getHiddenRows().get(index) || index == SpreadsheetView.this.getFilteredRow());
/*      */             }
/*      */           });
/*      */     } 
/*  771 */     int rowCount = getGrid().getRowCount();
/*  772 */     this.rowMap = new HashMap<>(rowCount);
/*  773 */     int visibleRow = 0;
/*  774 */     for (int i = 0; i < rowCount; i++) {
/*  775 */       if (!getHiddenRows().get(i)) {
/*  776 */         this.rowMap.put(Integer.valueOf(i), Integer.valueOf(visibleRow++));
/*      */       } else {
/*  778 */         this.rowMap.put(Integer.valueOf(i), Integer.valueOf(visibleRow));
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void computeColumnMap() {
/*  784 */     int columnCount = getGrid().getColumnCount();
/*  785 */     this.columnMap = new HashMap<>(columnCount);
/*      */ 
/*      */     
/*  788 */     CellView.getValue(() -> {
/*      */           int columnSize = getColumns().size();
/*      */           int totalColumn = getGrid().getColumnCount();
/*      */           int visibleColumn = 0;
/*      */           for (int i = 0; i < totalColumn; i++) {
/*      */             if (!getHiddenColumns().get(i)) {
/*      */               if (i < columnSize) {
/*      */                 ((SpreadsheetColumn)getColumns().get(i)).column.setVisible(true);
/*      */               }
/*      */               this.columnMap.put(Integer.valueOf(i), Integer.valueOf(visibleColumn++));
/*      */             } else {
/*      */               if (i < columnSize) {
/*      */                 ((SpreadsheetColumn)getColumns().get(i)).column.setVisible(false);
/*      */               }
/*      */               this.columnMap.put(Integer.valueOf(i), Integer.valueOf(visibleColumn));
/*      */             } 
/*      */           } 
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void showRow(int row) {
/*  815 */     if (!getHiddenRows().get(row)) {
/*      */       return;
/*      */     }
/*  818 */     getHiddenRows().set(row, false);
/*  819 */     BitSet bitSet = new BitSet(getHiddenRows().size());
/*  820 */     bitSet.or(getHiddenRows());
/*  821 */     setHiddenRows(bitSet);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void showColumn(SpreadsheetColumn column) {
/*  830 */     int indexColumn = getColumns().indexOf(column);
/*  831 */     if (!getHiddenColumns().get(indexColumn)) {
/*      */       return;
/*      */     }
/*  834 */     getHiddenColumns().set(indexColumn, false);
/*  835 */     BitSet bitSet = new BitSet(getHiddenColumns().size());
/*  836 */     bitSet.or(getHiddenColumns());
/*  837 */     setHiddenColumns(bitSet);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getFilteredRow(int modelRow) {
/*      */     try {
/*  850 */       return ((Integer)this.rowMap.get(Integer.valueOf(modelRow))).intValue();
/*  851 */     } catch (NullPointerException ex) {
/*  852 */       return modelRow;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getViewColumn(int modelColumn) {
/*      */     try {
/*  865 */       return ((Integer)this.columnMap.get(Integer.valueOf(modelColumn))).intValue();
/*  866 */     } catch (NullPointerException ex) {
/*  867 */       return modelColumn;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getModelColumn(int viewColumn) {
/*      */     try {
/*  882 */       return this.cellsView.getColumns().indexOf(this.cellsView.getVisibleLeafColumn(viewColumn));
/*  883 */     } catch (NullPointerException ex) {
/*  884 */       return viewColumn;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getModelRow(int viewRow) {
/*  897 */     if (viewRow < 0 || viewRow >= this.sortedList.size()) {
/*  898 */       return viewRow;
/*      */     }
/*      */     try {
/*  901 */       return getFilteredSourceIndex(this.sortedList.getSourceIndex(viewRow));
/*  902 */     } catch (NullPointerException|IndexOutOfBoundsException ex) {
/*  903 */       return viewRow;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getFilteredSourceIndex(int viewRow) {
/*  919 */     if (viewRow < 0 || viewRow >= this.filteredList.size()) {
/*  920 */       return viewRow;
/*      */     }
/*      */     try {
/*  923 */       return this.filteredList.getSourceIndex(viewRow);
/*  924 */     } catch (NullPointerException|IndexOutOfBoundsException ex) {
/*  925 */       return viewRow;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getRowSpan(SpreadsheetCell cell, int index) {
/*  946 */     int rowSpan = 0;
/*      */     do {
/*  948 */       rowSpan++;
/*  949 */     } while (++index < this.sortedList.size() && cell.getColumn() < getGrid().getColumnCount() && ((ObservableList)this.sortedList
/*  950 */       .get(index)).get(cell.getColumn()) == cell);
/*      */     
/*  952 */     return rowSpan;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getRowSpanFilter(SpreadsheetCell cell) {
/*  963 */     int rowSpan = cell.getRowSpan();
/*      */     
/*  965 */     for (int i = cell.getRow(); i < cell.getRow() + cell.getRowSpan(); i++) {
/*  966 */       rowSpan -= getHiddenRows().get(i) ? 1 : 0;
/*      */     }
/*  968 */     return rowSpan;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObservableList<ObservableList<SpreadsheetCell>> getItems() {
/*  978 */     return this.cellsView.getItems();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getColumnSpan(SpreadsheetCell cell) {
/*  988 */     int colSpan = cell.getColumnSpan();
/*  989 */     for (int i = cell.getColumn(); i < cell.getColumn() + cell.getColumnSpan(); i++) {
/*  990 */       colSpan -= getHiddenColumns().get(i) ? 1 : 0;
/*      */     }
/*  992 */     return colSpan;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Double getZoomFactor() {
/* 1001 */     return Double.valueOf(this.zoomFactor.get());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setZoomFactor(Double zoomFactor) {
/* 1011 */     this.zoomFactor.set(zoomFactor.doubleValue());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final DoubleProperty zoomFactorProperty() {
/* 1020 */     return this.zoomFactor;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void incrementZoom() {
/* 1029 */     double newZoom = getZoomFactor().doubleValue();
/* 1030 */     int prevValue = (int)((newZoom - 0.1D) / 0.1D);
/* 1031 */     newZoom = (prevValue + 1) * 0.1D + 0.1D;
/* 1032 */     setZoomFactor(Double.valueOf((newZoom > 2.0D) ? 2.0D : newZoom));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void decrementZoom() {
/* 1040 */     double newZoom = getZoomFactor().doubleValue() - 0.01D;
/* 1041 */     int prevValue = (int)((newZoom - 0.1D) / 0.1D);
/* 1042 */     newZoom = prevValue * 0.1D + 0.1D;
/* 1043 */     setZoomFactor(Double.valueOf((newZoom < 0.1D) ? 0.1D : newZoom));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void edit(int row, SpreadsheetColumn column) {
/* 1057 */     this.cellsView.edit(row, column.column);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Comparator getComparator() {
/* 1068 */     return (this.sortedList == null) ? null : this.sortedList.getComparator();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectProperty<Comparator<? super ObservableList<SpreadsheetCell>>> comparatorProperty() {
/* 1079 */     return this.sortedList.comparatorProperty();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setComparator(Comparator<ObservableList<SpreadsheetCell>> comparator) {
/* 1088 */     this.sortedList.setComparator(comparator);
/* 1089 */     computeRowMap();
/* 1090 */     requestLayout();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setGrid(Grid grid) {
/* 1101 */     if (grid == null) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/* 1106 */     this.filteredList = new FilteredList(grid.getRows());
/* 1107 */     this.sortedList = new SortedList((ObservableList)this.filteredList);
/* 1108 */     this.gridProperty.set(grid);
/* 1109 */     setHiddenRows(new BitSet(this.filteredList.getSource().size()));
/* 1110 */     setHiddenColumns(new BitSet(grid.getColumnCount()));
/* 1111 */     initRowFix(grid);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1118 */     List<Integer> newFixedRows = new ArrayList<>();
/* 1119 */     for (Integer rowFixed : getFixedRows()) {
/* 1120 */       if (isRowFixable(rowFixed.intValue())) {
/* 1121 */         newFixedRows.add(rowFixed);
/*      */       }
/*      */     } 
/* 1124 */     getFixedRows().setAll(newFixedRows);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1130 */     List<Integer> columnsFixed = new ArrayList<>();
/* 1131 */     for (SpreadsheetColumn column : getFixedColumns()) {
/* 1132 */       columnsFixed.add(Integer.valueOf(getColumns().indexOf(column)));
/*      */     }
/* 1134 */     getFixedColumns().clear();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1139 */     List<Double> widthColumns = new ArrayList<>();
/* 1140 */     for (SpreadsheetColumn column : this.columns) {
/* 1141 */       widthColumns.add(Double.valueOf(column.getWidth()));
/*      */     }
/*      */     
/* 1144 */     Pair<Integer, Integer> focusedPair = null;
/* 1145 */     TablePosition focusedCell = this.cellsView.getFocusModel().getFocusedCell();
/* 1146 */     if (focusedCell != null && focusedCell.getRow() != -1 && focusedCell.getColumn() != -1) {
/* 1147 */       focusedPair = new Pair(Integer.valueOf(focusedCell.getRow()), Integer.valueOf(focusedCell.getColumn()));
/*      */     }
/*      */     
/* 1150 */     Pair<Integer, Integer> finalPair = focusedPair;
/*      */     
/* 1152 */     if (grid.getRows() != null) {
/*      */ 
/*      */ 
/*      */       
/* 1156 */       this.cellsView.setItems((ObservableList)this.sortedList);
/* 1157 */       computeRowMap();
/*      */       
/* 1159 */       int columnCount = grid.getColumnCount();
/* 1160 */       this.columns.clear();
/* 1161 */       for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
/* 1162 */         SpreadsheetColumn spreadsheetColumn = new SpreadsheetColumn(getTableColumn(grid, columnIndex), this, Integer.valueOf(columnIndex), grid);
/* 1163 */         if (widthColumns.size() > columnIndex) {
/* 1164 */           spreadsheetColumn.setPrefWidth(((Double)widthColumns.get(columnIndex)).doubleValue());
/*      */         }
/* 1166 */         this.columns.add(spreadsheetColumn);
/*      */ 
/*      */         
/* 1169 */         if (columnsFixed.contains(Integer.valueOf(columnIndex)) && spreadsheetColumn.isColumnFixable()) {
/* 1170 */           spreadsheetColumn.setFixed(true);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1175 */     List<Pair<Integer, Integer>> selectedCells = new ArrayList<>();
/* 1176 */     for (TablePosition position : getSelectionModel().getSelectedCells()) {
/* 1177 */       selectedCells.add(new Pair(Integer.valueOf(position.getRow()), Integer.valueOf(position.getColumn())));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1192 */     Runnable runnable = () -> {
/*      */         if (this.cellsView.getColumns().size() > grid.getColumnCount()) {
/*      */           this.cellsView.getColumns().remove(grid.getColumnCount(), this.cellsView.getColumns().size());
/*      */         } else if (this.cellsView.getColumns().size() < grid.getColumnCount()) {
/*      */           for (int i = this.cellsView.getColumns().size(); i < grid.getColumnCount(); i++) {
/*      */             this.cellsView.getColumns().add(((SpreadsheetColumn)this.columns.get(i)).column);
/*      */           }
/*      */         } 
/*      */         
/*      */         ((TableViewSpanSelectionModel)this.cellsView.getSelectionModel()).verifySelectedCells(selectedCells);
/*      */         
/*      */         if (finalPair != null && ((Integer)finalPair.getKey()).intValue() < getGrid().getRowCount() && ((Integer)finalPair.getValue()).intValue() < getGrid().getColumnCount()) {
/*      */           this.cellsView.getFocusModel().focus(((Integer)finalPair.getKey()).intValue(), (TableColumn)this.cellsView.getColumns().get(((Integer)finalPair.getValue()).intValue()));
/*      */         }
/*      */       };
/* 1207 */     if (Platform.isFxApplicationThread()) {
/* 1208 */       runnable.run();
/*      */     } else {
/*      */       try {
/* 1211 */         FutureTask future = new FutureTask(runnable, null);
/* 1212 */         Platform.runLater(future);
/* 1213 */         future.get();
/* 1214 */       } catch (InterruptedException|java.util.concurrent.ExecutionException ex) {
/* 1215 */         Logger.getLogger(SpreadsheetView.class.getName()).log(Level.SEVERE, (String)null, ex);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TablePosition<ObservableList<SpreadsheetCell>, ?> getEditingCell() {
/* 1226 */     return this.cellsView.getEditingCell();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ReadOnlyObjectProperty<TablePosition<ObservableList<SpreadsheetCell>, ?>> editingCellProperty() {
/* 1237 */     return this.cellsView.editingCellProperty();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ObservableList<SpreadsheetColumn> getColumns() {
/* 1248 */     return this.columns;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Grid getGrid() {
/* 1257 */     return (Grid)this.gridProperty.get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ReadOnlyObjectProperty<Grid> gridProperty() {
/* 1266 */     return (ReadOnlyObjectProperty<Grid>)this.gridProperty;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObservableList<Integer> getFixedRows() {
/* 1277 */     return this.fixedRows;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isRowFixable(int row) {
/* 1291 */     return (row >= 0 && row < this.rowFix.size() && isFixingRowsAllowed()) ? this.rowFix.get(row) : false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean areRowsFixable(List<? extends Integer> list) {
/* 1304 */     if (list == null || list.isEmpty() || !isFixingRowsAllowed()) {
/* 1305 */       return false;
/*      */     }
/* 1307 */     Grid grid = getGrid();
/* 1308 */     int rowCount = grid.getRowCount();
/* 1309 */     ObservableList<ObservableList<SpreadsheetCell>> rows = grid.getRows();
/* 1310 */     for (Integer row : list) {
/* 1311 */       if (row == null || row.intValue() < 0 || row.intValue() >= rowCount) {
/* 1312 */         return false;
/*      */       }
/*      */       
/* 1315 */       if (!isRowFixable(row.intValue())) {
/* 1316 */         int maxSpan = 1;
/* 1317 */         List<SpreadsheetCell> gridRow = (List<SpreadsheetCell>)rows.get(row.intValue());
/* 1318 */         for (SpreadsheetCell cell : gridRow) {
/*      */           
/* 1320 */           if (!list.contains(Integer.valueOf(cell.getRow()))) {
/* 1321 */             return false;
/*      */           }
/*      */           
/* 1324 */           if (getRowSpan(cell, row.intValue()) > maxSpan && cell.getRow() == row.intValue()) {
/* 1325 */             maxSpan = cell.getRowSpan();
/*      */           }
/*      */         } 
/*      */         
/* 1329 */         int count = row.intValue() + maxSpan - 1;
/* 1330 */         for (int index = row.intValue() + 1; index <= count; index++) {
/* 1331 */           if (!list.contains(Integer.valueOf(index))) {
/* 1332 */             return false;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/* 1337 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFixingRowsAllowed() {
/* 1346 */     return this.fixingRowsAllowedProperty.get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFixingRowsAllowed(boolean b) {
/* 1355 */     this.fixingRowsAllowedProperty.set(b);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ReadOnlyBooleanProperty fixingRowsAllowedProperty() {
/* 1366 */     return (ReadOnlyBooleanProperty)this.fixingRowsAllowedProperty;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObservableList<SpreadsheetColumn> getFixedColumns() {
/* 1377 */     return this.fixedColumns;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isColumnFixable(int columnIndex) {
/* 1390 */     return (columnIndex >= 0 && columnIndex < getColumns().size() && isFixingColumnsAllowed()) ? ((SpreadsheetColumn)
/* 1391 */       getColumns().get(columnIndex)).isColumnFixable() : false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean areSpreadsheetColumnsFixable(List<? extends SpreadsheetColumn> list) {
/* 1405 */     List<Integer> newList = new ArrayList<>();
/* 1406 */     for (SpreadsheetColumn column : list) {
/* 1407 */       if (column != null) {
/* 1408 */         newList.add(Integer.valueOf(this.columns.indexOf(column)));
/*      */       }
/*      */     } 
/* 1411 */     return areColumnsFixable(newList);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean areColumnsFixable(List<? extends Integer> list) {
/* 1425 */     if (list == null || list.isEmpty() || !isFixingRowsAllowed()) {
/* 1426 */       return false;
/*      */     }
/* 1428 */     Grid grid = getGrid();
/* 1429 */     int columnCount = grid.getColumnCount();
/* 1430 */     ObservableList<ObservableList<SpreadsheetCell>> rows = grid.getRows();
/* 1431 */     for (Integer columnIndex : list) {
/* 1432 */       if (columnIndex == null || columnIndex.intValue() < 0 || columnIndex.intValue() >= columnCount) {
/* 1433 */         return false;
/*      */       }
/*      */       
/* 1436 */       if (!isColumnFixable(columnIndex.intValue())) {
/* 1437 */         int maxSpan = 1;
/*      */         
/* 1439 */         for (List<SpreadsheetCell> row : rows) {
/* 1440 */           SpreadsheetCell cell = row.get(columnIndex.intValue());
/*      */           
/* 1442 */           if (!list.contains(Integer.valueOf(cell.getColumn()))) {
/* 1443 */             return false;
/*      */           }
/*      */           
/* 1446 */           if (cell.getColumnSpan() > maxSpan && cell.getColumn() == columnIndex.intValue()) {
/* 1447 */             maxSpan = cell.getColumnSpan();
/*      */           }
/*      */         } 
/*      */         
/* 1451 */         int count = columnIndex.intValue() + maxSpan - 1;
/* 1452 */         for (int index = columnIndex.intValue() + 1; index <= count; index++) {
/* 1453 */           if (!list.contains(Integer.valueOf(index))) {
/* 1454 */             return false;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/* 1459 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFixingColumnsAllowed() {
/* 1468 */     return this.fixingColumnsAllowedProperty.get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFixingColumnsAllowed(boolean b) {
/* 1477 */     this.fixingColumnsAllowedProperty.set(b);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ReadOnlyBooleanProperty fixingColumnsAllowedProperty() {
/* 1488 */     return (ReadOnlyBooleanProperty)this.fixingColumnsAllowedProperty;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setShowColumnHeader(boolean b) {
/* 1497 */     this.showColumnHeader.setValue(Boolean.valueOf(b));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isShowColumnHeader() {
/* 1506 */     return this.showColumnHeader.get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final BooleanProperty showColumnHeaderProperty() {
/* 1515 */     return this.showColumnHeader;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setShowRowHeader(boolean b) {
/* 1524 */     this.showRowHeader.setValue(Boolean.valueOf(b));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isShowRowHeader() {
/* 1533 */     return this.showRowHeader.get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final BooleanProperty showRowHeaderProperty() {
/* 1542 */     return this.showRowHeader;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final DoubleProperty rowHeaderWidthProperty() {
/* 1552 */     return this.rowHeaderWidth;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setRowHeaderWidth(double value) {
/* 1561 */     this.rowHeaderWidth.setValue(Double.valueOf(value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final double getRowHeaderWidth() {
/* 1569 */     return this.rowHeaderWidth.get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObservableMap<Integer, Picker> getRowPickers() {
/* 1577 */     return this.rowPickers;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObservableMap<Integer, Picker> getColumnPickers() {
/* 1585 */     return this.columnPickers;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void resizeRowsToFitContent() {
/* 1594 */     if (getCellsViewSkin() != null) {
/* 1595 */       getCellsViewSkin().resizeRowsToFitContent();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void resizeRowsToMaximum() {
/* 1606 */     if (getCellsViewSkin() != null) {
/* 1607 */       getCellsViewSkin().resizeRowsToMaximum();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void resizeRowsToDefault() {
/* 1616 */     if (getCellsViewSkin() != null) {
/* 1617 */       getCellsViewSkin().resizeRowsToDefault();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getRowHeight(int row) {
/* 1627 */     if (getCellsViewSkin() == null) {
/* 1628 */       return getGrid().getRowHeight(row);
/*      */     }
/* 1630 */     return getCellsViewSkin().getRowHeight(row);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SpreadsheetViewSelectionModel getSelectionModel() {
/* 1640 */     return this.selectionModel;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void scrollToRow(int modelRow) {
/* 1648 */     this.cellsView.scrollTo(getFilteredRow(modelRow));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setVBarValue(double value) {
/* 1657 */     if (getCellsViewSkin() == null) {
/* 1658 */       Platform.runLater(() -> setVBarValue(value));
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 1663 */     getCellsViewSkin().getVBar().setValue(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHBarValue(double value) {
/* 1672 */     setHBarValue(value, 0);
/*      */   }
/*      */   
/*      */   private void setHBarValue(double value, int attempt) {
/* 1676 */     if (attempt > 10) {
/*      */       return;
/*      */     }
/* 1679 */     if (getCellsViewSkin() == null) {
/* 1680 */       int newAttempt = ++attempt;
/* 1681 */       Platform.runLater(() -> setHBarValue(value, newAttempt));
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 1686 */     getCellsViewSkin().setHbarValue(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getVBarValue() {
/* 1696 */     if (getCellsViewSkin() != null && getCellsViewSkin().getVBar() != null) {
/* 1697 */       return getCellsViewSkin().getVBar().getValue();
/*      */     }
/* 1699 */     return 0.0D;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getHBarValue() {
/* 1709 */     if (getCellsViewSkin() != null && getCellsViewSkin().getHBar() != null) {
/* 1710 */       return getCellsViewSkin().getHBar().getValue();
/*      */     }
/* 1712 */     return 0.0D;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void scrollToColumn(SpreadsheetColumn column) {
/* 1720 */     this.cellsView.scrollToColumn(column.column);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void scrollToColumnIndex(int modelColumn) {
/* 1731 */     this.cellsView.scrollToColumnIndex(modelColumn);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Optional<SpreadsheetCellEditor> getEditor(SpreadsheetCellType<?> cellType) {
/* 1743 */     if (cellType == null) {
/* 1744 */       return Optional.empty();
/*      */     }
/* 1746 */     SpreadsheetCellEditor cellEditor = this.editors.get(cellType);
/* 1747 */     if (cellEditor == null) {
/* 1748 */       cellEditor = cellType.createEditor(this);
/* 1749 */       if (cellEditor == null) {
/* 1750 */         return Optional.empty();
/*      */       }
/* 1752 */       this.editors.put(cellType, cellEditor);
/*      */     } 
/* 1754 */     return Optional.of(cellEditor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setEditable(boolean b) {
/* 1763 */     this.cellsView.setEditable(b);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isEditable() {
/* 1772 */     return this.cellsView.isEditable();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final BooleanProperty editableProperty() {
/* 1784 */     return this.cellsView.editableProperty();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ObjectProperty<Node> placeholderProperty() {
/* 1791 */     return this.cellsView.placeholderProperty();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setPlaceholder(Node placeholder) {
/* 1800 */     this.cellsView.setPlaceholder(placeholder);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Node getPlaceholder() {
/* 1809 */     return this.cellsView.getPlaceholder();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void copyClipboard() {
/* 1822 */     checkFormat();
/*      */     
/* 1824 */     ArrayList<GridChange> list = new ArrayList<>();
/* 1825 */     ObservableList<TablePosition> posList = getSelectionModel().getSelectedCells();
/*      */     
/* 1827 */     for (TablePosition<?, ?> p : posList) {
/* 1828 */       SpreadsheetCell cell = (SpreadsheetCell)((ObservableList)getGrid().getRows().get(getModelRow(p.getRow()))).get(getModelColumn(p.getColumn()));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1835 */       for (int row = 0; row < getRowSpan(cell, p.getRow()); row++) {
/* 1836 */         for (int col = 0; col < getColumnSpan(cell); col++) {
/*      */           try {
/* 1838 */             (new ObjectOutputStream(new ByteArrayOutputStream())).writeObject(cell.getItem());
/* 1839 */             list.add(new GridChange(p.getRow() + row, p.getColumn() + col, null, (cell.getItem() == null) ? null : cell.getItem()));
/* 1840 */           } catch (IOException exception) {
/* 1841 */             list.add(new GridChange(p.getRow() + row, p.getColumn() + col, null, (cell.getItem() == null) ? null : cell.getItem().toString()));
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 1846 */     ClipboardContent content = new ClipboardContent();
/* 1847 */     content.put(this.fmt, list);
/* 1848 */     Clipboard.getSystemClipboard().setContent((Map)content);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void pasteOneValue(GridChange change) {
/* 1856 */     for (TablePosition position : getSelectionModel().getSelectedCells()) {
/* 1857 */       tryPasteCell(getModelRow(position.getRow()), getModelColumn(position.getColumn()), change.getNewValue());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void tryPasteCell(int row, int column, Object value) {
/* 1868 */     SpanType type = getSpanType(row, column);
/* 1869 */     if (type == SpanType.NORMAL_CELL || type == SpanType.ROW_VISIBLE) {
/* 1870 */       SpreadsheetCell cell = (SpreadsheetCell)((ObservableList)getGrid().getRows().get(row)).get(column);
/* 1871 */       boolean succeed = cell.getCellType().match(value);
/* 1872 */       if (succeed) {
/* 1873 */         getGrid().setCellValue(cell.getRow(), cell.getColumn(), cell
/* 1874 */             .getCellType().convertValue(value));
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void pasteMixedValues(ArrayList<GridChange> list) {
/* 1889 */     RectangleSelection.SelectionRange sourceSelectionRange = new RectangleSelection.SelectionRange();
/* 1890 */     sourceSelectionRange.fillGridRange(list);
/*      */ 
/*      */     
/* 1893 */     if (sourceSelectionRange.getRange() != null) {
/* 1894 */       RectangleSelection.SelectionRange targetSelectionRange = new RectangleSelection.SelectionRange();
/* 1895 */       targetSelectionRange.fill((List)this.cellsView.getSelectionModel().getSelectedCells());
/* 1896 */       if (targetSelectionRange.getRange() != null) {
/*      */         
/* 1898 */         RectangleSelection.GridRange sourceRange = sourceSelectionRange.getRange();
/* 1899 */         RectangleSelection.GridRange targetRange = targetSelectionRange.getRange();
/* 1900 */         int sourceRowGap = sourceRange.getBottom() - sourceRange.getTop() + 1;
/* 1901 */         int targetRowGap = targetRange.getBottom() - targetRange.getTop() + 1;
/*      */         
/* 1903 */         int sourceColumnGap = sourceRange.getRight() - sourceRange.getLeft() + 1;
/* 1904 */         int targetColumnGap = targetRange.getRight() - targetRange.getLeft() + 1;
/*      */         
/* 1906 */         int offsetRow = targetRange.getTop() - sourceRange.getTop();
/* 1907 */         int offsetCol = targetRange.getLeft() - sourceRange.getLeft();
/*      */ 
/*      */         
/* 1910 */         if ((sourceRowGap == targetRowGap || targetRowGap == 1) && targetColumnGap % sourceColumnGap == 0) {
/* 1911 */           label42: for (GridChange change : list) {
/* 1912 */             int row = getModelRow(change.getRow() + offsetRow);
/* 1913 */             int column = change.getColumn() + offsetCol;
/*      */             while (true)
/* 1915 */             { int modelColumn = getModelColumn(column);
/* 1916 */               if (row < getGrid().getRowCount() && modelColumn < getGrid().getColumnCount() && row >= 0 && column >= 0)
/*      */               {
/* 1918 */                 tryPasteCell(row, modelColumn, change.getNewValue());
/*      */               }
/* 1920 */               if ((column += sourceColumnGap) > targetRange.getRight())
/*      */                 continue label42;  } 
/*      */           } 
/* 1923 */         } else if ((sourceColumnGap == targetColumnGap || targetColumnGap == 1) && targetRowGap % sourceRowGap == 0) {
/* 1924 */           label43: for (GridChange change : list) {
/*      */             
/* 1926 */             int row = change.getRow() + offsetRow;
/* 1927 */             int column = getModelColumn(change.getColumn() + offsetCol);
/*      */             while (true) {
/* 1929 */               int modelRow = getModelRow(row);
/* 1930 */               if (modelRow < getGrid().getRowCount() && column < getGrid().getColumnCount() && row >= 0 && column >= 0)
/*      */               {
/* 1932 */                 tryPasteCell(modelRow, column, change.getNewValue());
/*      */               }
/* 1934 */               if ((row += sourceRowGap) > targetRange.getBottom()) {
/*      */                 continue label43;
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void pasteSeveralValues(ArrayList<GridChange> list) {
/* 1948 */     int minRow = getGrid().getRowCount();
/* 1949 */     int minCol = getGrid().getColumnCount();
/* 1950 */     int maxRow = 0;
/* 1951 */     int maxCol = 0;
/* 1952 */     for (GridChange gridChange : list) {
/* 1953 */       int tempcol = gridChange.getColumn();
/* 1954 */       int temprow = gridChange.getRow();
/* 1955 */       if (tempcol < minCol) {
/* 1956 */         minCol = tempcol;
/*      */       }
/* 1958 */       if (tempcol > maxCol) {
/* 1959 */         maxCol = tempcol;
/*      */       }
/* 1961 */       if (temprow < minRow) {
/* 1962 */         minRow = temprow;
/*      */       }
/* 1964 */       if (temprow > maxRow) {
/* 1965 */         maxRow = temprow;
/*      */       }
/*      */     } 
/*      */     
/* 1969 */     TablePosition<?, ?> p = this.cellsView.getFocusModel().getFocusedCell();
/*      */     
/* 1971 */     int offsetRow = p.getRow() - minRow;
/* 1972 */     int offsetCol = p.getColumn() - minCol;
/* 1973 */     int rowCount = getGrid().getRowCount();
/* 1974 */     int columnCount = getGrid().getColumnCount();
/*      */ 
/*      */ 
/*      */     
/* 1978 */     for (GridChange change : list) {
/* 1979 */       int row = getModelRow(change.getRow() + offsetRow);
/* 1980 */       int column = getModelColumn(change.getColumn() + offsetCol);
/* 1981 */       if (row < rowCount && column < columnCount && row >= 0 && column >= 0)
/*      */       {
/* 1983 */         tryPasteCell(row, column, change.getNewValue());
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void pasteClipboard() {
/* 1996 */     ObservableList observableList = this.cellsView.getSelectionModel().getSelectedCells();
/* 1997 */     if (!isEditable() || observableList.isEmpty()) {
/*      */       return;
/*      */     }
/*      */     
/* 2001 */     checkFormat();
/* 2002 */     Clipboard clipboard = Clipboard.getSystemClipboard();
/* 2003 */     if (clipboard.getContent(this.fmt) != null) {
/*      */ 
/*      */       
/* 2006 */       ArrayList<GridChange> list = (ArrayList<GridChange>)clipboard.getContent(this.fmt);
/* 2007 */       if (list.size() == 1) {
/* 2008 */         pasteOneValue(list.get(0));
/* 2009 */       } else if (observableList.size() > 1) {
/* 2010 */         pasteMixedValues(list);
/*      */       } else {
/* 2012 */         pasteSeveralValues(list);
/*      */       }
/*      */     
/* 2015 */     } else if (clipboard.hasString()) {
/*      */     
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ContextMenu getSpreadsheetViewContextMenu() {
/* 2034 */     ContextMenu contextMenu = new ContextMenu();
/*      */     
/* 2036 */     MenuItem copyItem = new MenuItem(Localization.localize(Localization.asKey("spreadsheet.view.menu.copy")));
/* 2037 */     copyItem.setGraphic((Node)new ImageView(new Image(SpreadsheetView.class
/* 2038 */             .getResourceAsStream("copySpreadsheetView.png"))));
/* 2039 */     copyItem.setAccelerator((KeyCombination)new KeyCodeCombination(KeyCode.C, new KeyCombination.Modifier[] { KeyCombination.SHORTCUT_DOWN }));
/* 2040 */     copyItem.setOnAction(new EventHandler<ActionEvent>()
/*      */         {
/*      */           public void handle(ActionEvent e) {
/* 2043 */             SpreadsheetView.this.copyClipboard();
/*      */           }
/*      */         });
/*      */     
/* 2047 */     MenuItem pasteItem = new MenuItem(Localization.localize(Localization.asKey("spreadsheet.view.menu.paste")));
/* 2048 */     pasteItem.setGraphic((Node)new ImageView(new Image(SpreadsheetView.class
/* 2049 */             .getResourceAsStream("pasteSpreadsheetView.png"))));
/* 2050 */     pasteItem.setAccelerator((KeyCombination)new KeyCodeCombination(KeyCode.V, new KeyCombination.Modifier[] { KeyCombination.SHORTCUT_DOWN }));
/* 2051 */     pasteItem.setOnAction(new EventHandler<ActionEvent>()
/*      */         {
/*      */           public void handle(ActionEvent e) {
/* 2054 */             SpreadsheetView.this.pasteClipboard();
/*      */           }
/*      */         });
/*      */     
/* 2058 */     Menu cornerMenu = new Menu(Localization.localize(Localization.asKey("spreadsheet.view.menu.comment")));
/* 2059 */     cornerMenu.setGraphic((Node)new ImageView(new Image(SpreadsheetView.class
/* 2060 */             .getResourceAsStream("comment.png"))));
/*      */     
/* 2062 */     MenuItem topLeftItem = new MenuItem(Localization.localize(Localization.asKey("spreadsheet.view.menu.comment.top-left")));
/* 2063 */     topLeftItem.setOnAction(new EventHandler<ActionEvent>()
/*      */         {
/*      */           public void handle(ActionEvent t)
/*      */           {
/* 2067 */             TablePosition<ObservableList<SpreadsheetCell>, ?> pos = SpreadsheetView.this.cellsView.getFocusModel().getFocusedCell();
/* 2068 */             SpreadsheetCell cell = (SpreadsheetCell)((ObservableList)SpreadsheetView.this.getGrid().getRows().get(SpreadsheetView.this.getModelRow(pos.getRow()))).get(SpreadsheetView.this.getModelColumn(pos.getColumn()));
/* 2069 */             cell.activateCorner(SpreadsheetCell.CornerPosition.TOP_LEFT);
/*      */           }
/*      */         });
/* 2072 */     MenuItem topRightItem = new MenuItem(Localization.localize(Localization.asKey("spreadsheet.view.menu.comment.top-right")));
/* 2073 */     topRightItem.setOnAction(new EventHandler<ActionEvent>()
/*      */         {
/*      */           public void handle(ActionEvent t)
/*      */           {
/* 2077 */             TablePosition<ObservableList<SpreadsheetCell>, ?> pos = SpreadsheetView.this.cellsView.getFocusModel().getFocusedCell();
/* 2078 */             SpreadsheetCell cell = (SpreadsheetCell)((ObservableList)SpreadsheetView.this.getGrid().getRows().get(SpreadsheetView.this.getModelRow(pos.getRow()))).get(SpreadsheetView.this.getModelColumn(pos.getColumn()));
/* 2079 */             cell.activateCorner(SpreadsheetCell.CornerPosition.TOP_RIGHT);
/*      */           }
/*      */         });
/* 2082 */     MenuItem bottomRightItem = new MenuItem(Localization.localize(Localization.asKey("spreadsheet.view.menu.comment.bottom-right")));
/* 2083 */     bottomRightItem.setOnAction(new EventHandler<ActionEvent>()
/*      */         {
/*      */           public void handle(ActionEvent t)
/*      */           {
/* 2087 */             TablePosition<ObservableList<SpreadsheetCell>, ?> pos = SpreadsheetView.this.cellsView.getFocusModel().getFocusedCell();
/* 2088 */             SpreadsheetCell cell = (SpreadsheetCell)((ObservableList)SpreadsheetView.this.getGrid().getRows().get(SpreadsheetView.this.getModelRow(pos.getRow()))).get(SpreadsheetView.this.getModelColumn(pos.getColumn()));
/* 2089 */             cell.activateCorner(SpreadsheetCell.CornerPosition.BOTTOM_RIGHT);
/*      */           }
/*      */         });
/* 2092 */     MenuItem bottomLeftItem = new MenuItem(Localization.localize(Localization.asKey("spreadsheet.view.menu.comment.bottom-left")));
/* 2093 */     bottomLeftItem.setOnAction(new EventHandler<ActionEvent>()
/*      */         {
/*      */           public void handle(ActionEvent t)
/*      */           {
/* 2097 */             TablePosition<ObservableList<SpreadsheetCell>, ?> pos = SpreadsheetView.this.cellsView.getFocusModel().getFocusedCell();
/* 2098 */             SpreadsheetCell cell = (SpreadsheetCell)((ObservableList)SpreadsheetView.this.getGrid().getRows().get(SpreadsheetView.this.getModelRow(pos.getRow()))).get(SpreadsheetView.this.getModelColumn(pos.getColumn()));
/* 2099 */             cell.activateCorner(SpreadsheetCell.CornerPosition.BOTTOM_LEFT);
/*      */           }
/*      */         });
/*      */     
/* 2103 */     cornerMenu.getItems().addAll((Object[])new MenuItem[] { topLeftItem, topRightItem, bottomRightItem, bottomLeftItem });
/*      */     
/* 2105 */     contextMenu.getItems().addAll((Object[])new MenuItem[] { copyItem, pasteItem, (MenuItem)cornerMenu });
/* 2106 */     return contextMenu;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void deleteSelectedCells() {
/* 2115 */     for (TablePosition<ObservableList<SpreadsheetCell>, ?> position : getSelectionModel().getSelectedCells()) {
/* 2116 */       getGrid().setCellValue(getModelRow(position.getRow()), getModelColumn(position.getColumn()), null);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SpanType getSpanType(int rowIndex, int modelColumn) {
/* 2129 */     if (getGrid() == null) {
/* 2130 */       return SpanType.NORMAL_CELL;
/*      */     }
/*      */     
/* 2133 */     if (rowIndex < 0 || modelColumn < 0 || rowIndex >= getItems().size() || modelColumn >= getGrid().getColumnCount()) {
/* 2134 */       return SpanType.NORMAL_CELL;
/*      */     }
/*      */     
/* 2137 */     SpreadsheetCell cell = (SpreadsheetCell)((ObservableList)getCellsView().getItems().get(rowIndex)).get(modelColumn);
/*      */     
/* 2139 */     int cellColumn = getHiddenColumns().nextClearBit(cell.getColumn());
/*      */     
/* 2141 */     int cellRowSpan = getRowSpanFilter(cell);
/*      */     
/* 2143 */     if (cellColumn == modelColumn && cellRowSpan == 1) {
/* 2144 */       return SpanType.NORMAL_CELL;
/*      */     }
/*      */     
/* 2147 */     int cellColumnSpan = getColumnSpan(cell);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2152 */     GridViewSkin skin = getCellsViewSkin();
/* 2153 */     boolean containsRowMinusOne = (skin == null) ? true : skin.containsRow(rowIndex - 1);
/*      */ 
/*      */     
/* 2156 */     boolean containsSameCellMinusOne = (rowIndex > 0) ? ((((ObservableList)getCellsView().getItems().get(rowIndex - 1)).get(modelColumn) == cell)) : false;
/*      */     
/* 2158 */     if (containsRowMinusOne && cellColumnSpan > 1 && cellColumn != modelColumn && cellRowSpan > 1 && containsSameCellMinusOne)
/*      */     {
/* 2160 */       return SpanType.BOTH_INVISIBLE; } 
/* 2161 */     if (cellRowSpan > 1 && cellColumn == modelColumn) {
/* 2162 */       if (!containsSameCellMinusOne || !containsRowMinusOne) {
/* 2163 */         return SpanType.ROW_VISIBLE;
/*      */       }
/* 2165 */       return SpanType.ROW_SPAN_INVISIBLE;
/*      */     } 
/* 2167 */     if (cellColumnSpan > 1 && (!containsSameCellMinusOne || !containsRowMinusOne)) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2174 */       if (cellColumn == modelColumn) {
/* 2175 */         return SpanType.NORMAL_CELL;
/*      */       }
/* 2177 */       return SpanType.COLUMN_SPAN_INVISIBLE;
/*      */     } 
/*      */     
/* 2180 */     return SpanType.NORMAL_CELL;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private TableColumn<ObservableList<SpreadsheetCell>, SpreadsheetCell> getTableColumn(Grid grid, int columnIndex) {
/*      */     TableColumn<ObservableList<SpreadsheetCell>, SpreadsheetCell> column;
/* 2204 */     String columnHeader = (grid.getColumnHeaders().size() > columnIndex) ? (String)grid.getColumnHeaders().get(columnIndex) : Utils.getExcelLetterFromNumber(columnIndex);
/*      */     
/* 2206 */     if (columnIndex < this.cellsView.getColumns().size()) {
/* 2207 */       column = (TableColumn<ObservableList<SpreadsheetCell>, SpreadsheetCell>)this.cellsView.getColumns().get(columnIndex);
/* 2208 */       column.setText(columnHeader);
/*      */     } else {
/* 2210 */       column = new TableColumn(columnHeader);
/*      */       
/* 2212 */       column.setEditable(true);
/*      */       
/* 2214 */       column.setSortable(false);
/*      */       
/* 2216 */       column.impl_setReorderable(false);
/*      */ 
/*      */       
/* 2219 */       column.setCellValueFactory(p -> (columnIndex >= ((ObservableList)p.getValue()).size()) ? null : new ReadOnlyObjectWrapper(((ObservableList)p.getValue()).get(columnIndex)));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2227 */       column.setCellFactory(p -> new CellView(this.handle));
/*      */     } 
/* 2229 */     return column;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Grid getSampleGrid() {
/* 2240 */     GridBase gridBase = new GridBase(100, 15);
/* 2241 */     ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
/*      */     
/* 2243 */     for (int row = 0; row < gridBase.getRowCount(); row++) {
/* 2244 */       ObservableList<SpreadsheetCell> currentRow = FXCollections.observableArrayList();
/* 2245 */       for (int column = 0; column < gridBase.getColumnCount(); column++) {
/* 2246 */         currentRow.add(SpreadsheetCellType.STRING.createCell(row, column, 1, 1, "toto"));
/*      */       }
/* 2248 */       rows.add(currentRow);
/*      */     } 
/* 2250 */     gridBase.setRows((Collection<ObservableList<SpreadsheetCell>>)rows);
/* 2251 */     return gridBase;
/*      */   }
/*      */   
/*      */   private void initRowFix(Grid grid) {
/* 2255 */     ObservableList<ObservableList<SpreadsheetCell>> rows = grid.getRows();
/* 2256 */     this.rowFix = new BitSet(rows.size());
/*      */     
/* 2258 */     for (int r = 0; r < rows.size(); r++) {
/* 2259 */       ObservableList<SpreadsheetCell> row = (ObservableList<SpreadsheetCell>)rows.get(r);
/* 2260 */       Iterator<SpreadsheetCell> iterator = row.iterator(); while (true) { if (iterator.hasNext()) { SpreadsheetCell cell = iterator.next();
/* 2261 */           if (getRowSpanFilter(cell) > 1)
/*      */             break; 
/*      */           continue; }
/*      */         
/* 2265 */         this.rowFix.set(r);
/*      */         break; }
/*      */     
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void verifyGrid(Grid grid) {
/* 2276 */     verifyColumnSpan(grid);
/*      */   }
/*      */   
/*      */   private void verifyColumnSpan(Grid grid) {
/* 2280 */     for (int i = 0; i < grid.getRows().size(); i++) {
/* 2281 */       ObservableList<SpreadsheetCell> row = (ObservableList<SpreadsheetCell>)grid.getRows().get(i);
/* 2282 */       int count = 0;
/* 2283 */       for (int j = 0; j < row.size(); j++) {
/* 2284 */         if (((SpreadsheetCell)row.get(j)).getColumnSpan() == 1) {
/* 2285 */           count++;
/* 2286 */         } else if (((SpreadsheetCell)row.get(j)).getColumnSpan() > 1) {
/* 2287 */           count++;
/* 2288 */           SpreadsheetCell currentCell = (SpreadsheetCell)row.get(j);
/* 2289 */           for (int k = j + 1; k < currentCell.getColumn() + currentCell.getColumnSpan(); k++) {
/* 2290 */             if (!((SpreadsheetCell)row.get(k)).equals(currentCell)) {
/* 2291 */               throw new IllegalStateException("\n At row " + i + " and column " + j + ": this cell is in the range of a columnSpan but is different. \nEvery cell in a range of a ColumnSpan must be of the same instance.");
/*      */             }
/*      */ 
/*      */             
/* 2295 */             count++;
/* 2296 */             j++;
/*      */           } 
/*      */         } else {
/* 2299 */           throw new IllegalStateException("\n At row " + i + " and column " + j + ": this cell has a negative columnSpan");
/*      */         } 
/*      */       } 
/*      */       
/* 2303 */       if (count != grid.getColumnCount()) {
/* 2304 */         throw new IllegalStateException("The row" + i + " has a number of cells different of the columnCount declared in the grid.");
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void checkFormat() {
/* 2311 */     if ((this.fmt = DataFormat.lookupMimeType("SpreadsheetView")) == null) {
/* 2312 */       this.fmt = new DataFormat(new String[] { "SpreadsheetView" });
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SpreadsheetView(final Grid grid) {
/* 2322 */     this.fixedRowsListener = new ListChangeListener<Integer>()
/*      */       {
/*      */         public void onChanged(ListChangeListener.Change<? extends Integer> c) {
/* 2325 */           while (c.next()) {
/* 2326 */             if (c.wasAdded()) {
/* 2327 */               List<? extends Integer> newRows = c.getAddedSubList();
/* 2328 */               if (!SpreadsheetView.this.areRowsFixable(newRows)) {
/* 2329 */                 throw new IllegalArgumentException(SpreadsheetView.this.computeReason(newRows));
/*      */               }
/* 2331 */               FXCollections.sort(SpreadsheetView.this.fixedRows);
/*      */             } 
/*      */             
/* 2334 */             if (c.wasRemoved());
/*      */           } 
/*      */         }
/*      */       };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2372 */     this.fixedColumnsListener = new ListChangeListener<SpreadsheetColumn>()
/*      */       {
/*      */         public void onChanged(ListChangeListener.Change<? extends SpreadsheetColumn> c) {
/* 2375 */           while (c.next()) {
/* 2376 */             if (c.wasAdded()) {
/* 2377 */               List<? extends SpreadsheetColumn> newColumns = c.getAddedSubList();
/* 2378 */               if (!SpreadsheetView.this.areSpreadsheetColumnsFixable(newColumns)) {
/* 2379 */                 List<Integer> newList = new ArrayList<>();
/* 2380 */                 for (SpreadsheetColumn column : newColumns) {
/* 2381 */                   if (column != null) {
/* 2382 */                     newList.add(Integer.valueOf(SpreadsheetView.this.columns.indexOf(column)));
/*      */                   }
/*      */                 } 
/* 2385 */                 throw new IllegalArgumentException(computeReason(newList));
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         }
/*      */ 
/*      */         
/*      */         private String computeReason(List<Integer> list) {
/* 2393 */           String reason = "\n This column cannot be fixed.";
/* 2394 */           ObservableList<ObservableList<SpreadsheetCell>> rows = SpreadsheetView.this.getGrid().getRows();
/* 2395 */           for (Integer columnIndex : list) {
/*      */             
/* 2397 */             if (!SpreadsheetView.this.isColumnFixable(columnIndex.intValue())) {
/* 2398 */               int maxSpan = 1;
/*      */               
/* 2400 */               for (List<SpreadsheetCell> row : rows) {
/* 2401 */                 SpreadsheetCell cell = row.get(columnIndex.intValue());
/*      */                 
/* 2403 */                 if (!list.contains(Integer.valueOf(cell.getColumn()))) {
/* 2404 */                   reason = reason + "The column " + columnIndex + " is inside a column span and the starting column " + cell.getColumn() + " is not fixed.\n";
/*      */                 }
/*      */                 
/* 2407 */                 if (cell.getColumnSpan() > maxSpan && cell.getColumn() == columnIndex.intValue()) {
/* 2408 */                   maxSpan = cell.getColumnSpan();
/*      */                 }
/*      */               } 
/*      */               
/* 2412 */               int count = columnIndex.intValue() + maxSpan - 1;
/* 2413 */               for (int index = columnIndex.intValue() + 1; index < count; index++) {
/* 2414 */                 if (!list.contains(Integer.valueOf(index))) {
/* 2415 */                   reason = reason + "One cell on the column " + columnIndex + " has a column span of " + maxSpan + ". But the column " + index + " contained within that span is not fixed.\n";
/*      */                 }
/*      */               } 
/*      */             } 
/*      */           } 
/*      */           
/* 2421 */           return reason;
/*      */         }
/*      */       };
/*      */     
/* 2425 */     this.contextMenuChangeListener = new ChangeListener<ContextMenu>()
/*      */       {
/*      */         public void changed(ObservableValue<? extends ContextMenu> arg0, ContextMenu oldContextMenu, ContextMenu newContextMenu)
/*      */         {
/* 2429 */           if (oldContextMenu != null) {
/* 2430 */             oldContextMenu.setOnShowing(null);
/*      */           }
/* 2432 */           if (newContextMenu != null) {
/* 2433 */             newContextMenu.setOnShowing((EventHandler)new WeakEventHandler(SpreadsheetView.this.hideContextMenuEventHandler));
/*      */           }
/*      */         }
/*      */       };
/*      */     
/* 2438 */     this.hideContextMenuEventHandler = new EventHandler<WindowEvent>()
/*      */       {
/*      */ 
/*      */         
/*      */         public void handle(WindowEvent arg0)
/*      */         {
/* 2444 */           if (SpreadsheetView.this.getEditingCell() != null)
/*      */           {
/*      */             
/* 2447 */             Platform.runLater(() -> SpreadsheetView.this.getContextMenu().hide());
/*      */           }
/*      */         }
/*      */       };
/*      */ 
/*      */ 
/*      */     
/* 2454 */     this.keyPressedHandler = (keyEvent -> {
/*      */         TablePosition<ObservableList<SpreadsheetCell>, ?> position = getSelectionModel().getFocusedCell(); if (getEditingCell() == null && KeyCode.ENTER.equals(keyEvent.getCode())) {
/*      */           if (position != null) {
/*      */             if (keyEvent.isShiftDown()) {
/*      */               ((GridViewBehavior)getCellsViewSkin().getBehavior()).selectCell(-1, 0);
/*      */             } else {
/*      */               ((GridViewBehavior)getCellsViewSkin().getBehavior()).selectCell(1, 0);
/*      */             }  keyEvent.consume();
/*      */           }  getCellsViewSkin().scrollHorizontally();
/*      */         } else if (getEditingCell() == null && KeyCode.TAB.equals(keyEvent.getCode()) && !keyEvent.isShortcutDown()) {
/*      */           if (position != null)
/*      */             if (keyEvent.isShiftDown()) {
/*      */               getSelectionModel().clearAndSelectLeftCell();
/*      */             } else {
/*      */               getSelectionModel().clearAndSelectRightCell();
/*      */             }   keyEvent.consume(); getCellsViewSkin().scrollHorizontally();
/*      */         } else if (KeyCode.DELETE.equals(keyEvent.getCode())) {
/*      */           deleteSelectedCells();
/*      */         } else if (isEditionKey(keyEvent)) {
/*      */           getCellsView().edit(position.getRow(), position.getTableColumn());
/*      */         } else if (keyEvent.isShortcutDown() && (KeyCode.NUMPAD0.equals(keyEvent.getCode()) || KeyCode.DIGIT0.equals(keyEvent.getCode()))) {
/*      */           setZoomFactor(Double.valueOf(1.0D));
/*      */         } else if (keyEvent.isShortcutDown() && KeyCode.ADD.equals(keyEvent.getCode())) {
/*      */           incrementZoom();
/*      */         } else if (keyEvent.isShortcutDown() && KeyCode.SUBTRACT.equals(keyEvent.getCode())) {
/*      */           decrementZoom();
/*      */         } 
/*      */       }); addEventHandler(RowHeightEvent.ROW_HEIGHT_CHANGE, event -> {
/*      */           if (getFixedRows().contains(Integer.valueOf(getModelRow(event.getRow()))) && getCellsViewSkin() != null)
/*      */             getCellsViewSkin().computeFixedRowHeight(); 
/*      */         }); this.hiddenRowsProperty.addListener(new InvalidationListener() { public void invalidated(Observable observable) { SpreadsheetView.this.computeRowMap(); SpreadsheetView.this.initRowFix(grid); } }
/*      */       ); this.hiddenColumnsProperty.addListener(new InvalidationListener() { public void invalidated(Observable observable) { SpreadsheetView.this.computeColumnMap(); SpreadsheetView.this.initRowFix(grid); } }
/*      */       ); getStyleClass().add("SpreadsheetView"); setSkin(new Skin<SpreadsheetView>() { public Node getNode() { return (Node)SpreadsheetView.this.getCellsView(); } public SpreadsheetView getSkinnable() { return SpreadsheetView.this; } public void dispose() {} }
/*      */       ); this.cellsView = new SpreadsheetGridView(this.handle); getChildren().add(this.cellsView); TableViewSpanSelectionModel tableViewSpanSelectionModel = new TableViewSpanSelectionModel(this, this.cellsView); this.cellsView.setSelectionModel((TableView.TableViewSelectionModel)tableViewSpanSelectionModel); tableViewSpanSelectionModel.setCellSelectionEnabled(true); tableViewSpanSelectionModel.setSelectionMode(SelectionMode.MULTIPLE);
/*      */     this.selectionModel = new SpreadsheetViewSelectionModel(this, tableViewSpanSelectionModel);
/*      */     this.cellsView.getFocusModel().focusedCellProperty().addListener((ChangeListener)new FocusModelListener(this, this.cellsView));
/*      */     this.cellsView.setOnKeyPressed(this.keyPressedHandler);
/*      */     contextMenuProperty().addListener((ChangeListener)new WeakChangeListener(this.contextMenuChangeListener));
/*      */     CellView.getValue(() -> setContextMenu(getSpreadsheetViewContextMenu()));
/*      */     setGrid(grid);
/*      */     setEditable(true);
/*      */     this.fixedRows.addListener(this.fixedRowsListener);
/*      */     this.fixedColumns.addListener(this.fixedColumnsListener);
/*      */     final Scale scale = new Scale(1.0D, 1.0D);
/*      */     getTransforms().add(scale);
/*      */     this.zoomFactor.addListener(new ChangeListener<Number>() { public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) { scale.setX(newValue.doubleValue());
/*      */             scale.setY(newValue.doubleValue());
/*      */             SpreadsheetView.this.requestLayout(); } }
/*      */       );
/*      */     addEventFilter(ScrollEvent.ANY, event -> {
/*      */           if (event.isShortcutDown()) {
/*      */             if (event.getTextDeltaY() > 0.0D) {
/*      */               incrementZoom();
/*      */             } else {
/*      */               decrementZoom();
/*      */             } 
/*      */             event.consume();
/*      */           } 
/*      */         });
/* 2513 */   } private boolean isEditionKey(KeyEvent keyEvent) { return (!keyEvent.isShortcutDown() && 
/* 2514 */       !keyEvent.getCode().isNavigationKey() && 
/* 2515 */       !keyEvent.getCode().isFunctionKey() && 
/* 2516 */       !keyEvent.getCode().isModifierKey() && 
/* 2517 */       !keyEvent.getCode().isMediaKey() && keyEvent
/* 2518 */       .getCode() != KeyCode.ESCAPE); } private String computeReason(List<? extends Integer> list) { String reason = "\n A row cannot be fixed. \n"; for (Integer row : list) {
/*      */       if (!isRowFixable(row.intValue())) {
/*      */         int maxSpan = 1; List<SpreadsheetCell> gridRow = (List<SpreadsheetCell>)getGrid().getRows().get(row.intValue()); for (SpreadsheetCell cell : gridRow) {
/*      */           if (!list.contains(Integer.valueOf(cell.getRow())))
/*      */             reason = reason + "The row " + row + " is inside a row span and the starting row " + cell.getRow() + " is not fixed.\n";  if (cell.getRowSpan() > maxSpan && cell.getRow() == row.intValue())
/*      */             maxSpan = cell.getRowSpan(); 
/*      */         }  int count = row.intValue() + maxSpan - 1; for (int index = row.intValue() + 1; index < count; index++) {
/*      */           if (!list.contains(Integer.valueOf(index)))
/*      */             reason = reason + "One cell on the row " + row + " has a row span of " + maxSpan + ". But the row " + index + " contained within that span is not fixed.\n"; 
/*      */         } 
/*      */       } 
/*      */     }  return reason; } public static class RowHeightEvent extends Event {
/* 2530 */     public static final EventType<RowHeightEvent> ROW_HEIGHT_CHANGE = new EventType(Event.ANY, "RowHeightChange");
/*      */     
/*      */     private final int modelRow;
/*      */     private final double height;
/*      */     
/*      */     public RowHeightEvent(int row, double height) {
/* 2536 */       super(ROW_HEIGHT_CHANGE);
/* 2537 */       this.modelRow = row;
/* 2538 */       this.height = height;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getRow() {
/* 2546 */       return this.modelRow;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public double getHeight() {
/* 2554 */       return this.height;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class ColumnWidthEvent
/*      */     extends Event
/*      */   {
/* 2567 */     public static final EventType<ColumnWidthEvent> COLUMN_WIDTH_CHANGE = new EventType(Event.ANY, "ColumnWidthChange");
/*      */     
/*      */     private final int column;
/*      */     private final double width;
/*      */     
/*      */     public ColumnWidthEvent(int column, double width) {
/* 2573 */       super(COLUMN_WIDTH_CHANGE);
/* 2574 */       this.column = column;
/* 2575 */       this.width = width;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getColumn() {
/* 2583 */       return this.column;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public double getWidth() {
/* 2591 */       return this.width;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\spreadsheet\SpreadsheetView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */