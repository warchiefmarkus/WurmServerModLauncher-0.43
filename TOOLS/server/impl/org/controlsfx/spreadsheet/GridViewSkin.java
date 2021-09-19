/*      */ package impl.org.controlsfx.spreadsheet;
/*      */ 
/*      */ import com.sun.javafx.scene.control.behavior.BehaviorBase;
/*      */ import com.sun.javafx.scene.control.behavior.TableViewBehavior;
/*      */ import com.sun.javafx.scene.control.skin.CellSkinBase;
/*      */ import com.sun.javafx.scene.control.skin.TableHeaderRow;
/*      */ import com.sun.javafx.scene.control.skin.TableViewSkinBase;
/*      */ import com.sun.javafx.scene.control.skin.VirtualFlow;
/*      */ import java.lang.reflect.Field;
/*      */ import java.util.BitSet;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import javafx.application.Platform;
/*      */ import javafx.beans.InvalidationListener;
/*      */ import javafx.beans.Observable;
/*      */ import javafx.beans.property.BooleanProperty;
/*      */ import javafx.beans.property.ObjectProperty;
/*      */ import javafx.beans.property.SimpleBooleanProperty;
/*      */ import javafx.beans.value.ObservableValue;
/*      */ import javafx.collections.FXCollections;
/*      */ import javafx.collections.ListChangeListener;
/*      */ import javafx.collections.ObservableList;
/*      */ import javafx.collections.ObservableMap;
/*      */ import javafx.collections.ObservableSet;
/*      */ import javafx.collections.SetChangeListener;
/*      */ import javafx.event.Event;
/*      */ import javafx.event.EventHandler;
/*      */ import javafx.event.EventTarget;
/*      */ import javafx.geometry.HPos;
/*      */ import javafx.geometry.VPos;
/*      */ import javafx.scene.Node;
/*      */ import javafx.scene.control.Control;
/*      */ import javafx.scene.control.IndexedCell;
/*      */ import javafx.scene.control.ResizeFeaturesBase;
/*      */ import javafx.scene.control.ScrollBar;
/*      */ import javafx.scene.control.Skin;
/*      */ import javafx.scene.control.TableCell;
/*      */ import javafx.scene.control.TableColumn;
/*      */ import javafx.scene.control.TableColumnBase;
/*      */ import javafx.scene.control.TableFocusModel;
/*      */ import javafx.scene.control.TablePositionBase;
/*      */ import javafx.scene.control.TableRow;
/*      */ import javafx.scene.control.TableSelectionModel;
/*      */ import javafx.scene.control.TableView;
/*      */ import javafx.scene.input.MouseEvent;
/*      */ import javafx.scene.layout.Region;
/*      */ import javafx.stage.Screen;
/*      */ import javafx.util.Callback;
/*      */ import org.controlsfx.control.spreadsheet.Grid;
/*      */ import org.controlsfx.control.spreadsheet.SpreadsheetCell;
/*      */ import org.controlsfx.control.spreadsheet.SpreadsheetColumn;
/*      */ import org.controlsfx.control.spreadsheet.SpreadsheetView;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class GridViewSkin
/*      */   extends TableViewSkinBase<ObservableList<SpreadsheetCell>, ObservableList<SpreadsheetCell>, TableView<ObservableList<SpreadsheetCell>>, TableViewBehavior<ObservableList<SpreadsheetCell>>, TableRow<ObservableList<SpreadsheetCell>>, TableColumn<ObservableList<SpreadsheetCell>, ?>>
/*      */ {
/*      */   public static final double DEFAULT_CELL_HEIGHT;
/*  101 */   private static final double DATE_CELL_MIN_WIDTH = 200.0D - Screen.getPrimary().getDpi();
/*      */   
/*      */   static {
/*  104 */     double cell_size = 24.0D;
/*      */     try {
/*  106 */       Class<?> clazz = CellSkinBase.class;
/*  107 */       Field f = clazz.getDeclaredField("DEFAULT_CELL_SIZE");
/*  108 */       f.setAccessible(true);
/*  109 */       cell_size = f.getDouble(null);
/*  110 */     } catch (NoSuchFieldException e) {
/*      */       
/*  112 */       e.printStackTrace();
/*  113 */     } catch (SecurityException e) {
/*      */       
/*  115 */       e.printStackTrace();
/*  116 */     } catch (IllegalArgumentException e) {
/*      */       
/*  118 */       e.printStackTrace();
/*  119 */     } catch (IllegalAccessException e) {
/*      */       
/*  121 */       e.printStackTrace();
/*      */     } 
/*  123 */     DEFAULT_CELL_HEIGHT = cell_size;
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
/*  136 */   final Map<GridRow, Set<CellView>> deportedCells = new HashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  144 */   ObservableMap<Integer, Double> rowHeightMap = FXCollections.observableHashMap();
/*      */ 
/*      */   
/*      */   private GridCellEditor gridCellEditor;
/*      */ 
/*      */   
/*      */   protected final SpreadsheetHandle handle;
/*      */   
/*      */   protected SpreadsheetView spreadsheetView;
/*      */   
/*      */   protected VerticalHeader verticalHeader;
/*      */   
/*      */   protected HorizontalPicker horizontalPickers;
/*      */   
/*  158 */   private ObservableSet<Integer> currentlyFixedRow = FXCollections.observableSet(new HashSet());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  165 */   private final ObservableList<Integer> selectedRows = FXCollections.observableArrayList();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  172 */   private final ObservableList<Integer> selectedColumns = FXCollections.observableArrayList();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  177 */   private double fixedRowHeight = 0.0D;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   BitSet hBarValue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   BitSet rowToLayout;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   RectangleSelection rectangleSelection;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   double fixedColumnWidth;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  210 */   BooleanProperty lastRowLayout = (BooleanProperty)new SimpleBooleanProperty(true); private InvalidationListener rowToLayoutListener;
/*      */   private final InvalidationListener vbarValueListener;
/*      */   private final ListChangeListener<Integer> fixedRowsListener;
/*      */   private final SetChangeListener<? super Integer> currentlyFixedRowListener;
/*      */   private final ListChangeListener<SpreadsheetColumn> fixedColumnsListener;
/*      */   
/*  216 */   public GridViewSkin(final SpreadsheetHandle handle) { super((Control)handle.getGridView(), (BehaviorBase)new GridViewBehavior(handle.getGridView()));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  308 */     this.rowToLayoutListener = new InvalidationListener()
/*      */       {
/*      */         public void invalidated(Observable observable) {
/*  311 */           GridViewSkin.this.rowToLayout = GridViewSkin.this.initRowToLayoutBitSet();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1014 */     this.vbarValueListener = new InvalidationListener()
/*      */       {
/*      */         public void invalidated(Observable valueModel) {
/* 1017 */           GridViewSkin.this.verticalScroll();
/*      */         }
/*      */       };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1025 */     this.fixedRowsListener = new ListChangeListener<Integer>()
/*      */       {
/*      */         public void onChanged(ListChangeListener.Change<? extends Integer> c) {
/* 1028 */           GridViewSkin.this.hBarValue.clear();
/* 1029 */           while (c.next()) {
/* 1030 */             if (c.wasPermutated()) {
/* 1031 */               for (Integer fixedRow : c.getList())
/* 1032 */                 GridViewSkin.this.rowToLayout.set(GridViewSkin.this.spreadsheetView.getFilteredRow(fixedRow.intValue()), true); 
/*      */               continue;
/*      */             } 
/* 1035 */             for (Integer unfixedRow : c.getRemoved()) {
/* 1036 */               GridViewSkin.this.rowToLayout.set(GridViewSkin.this.spreadsheetView.getFilteredRow(unfixedRow.intValue()), false);
/*      */ 
/*      */               
/* 1039 */               if (GridViewSkin.this.spreadsheetView.getGrid().getRows().size() > unfixedRow.intValue()) {
/* 1040 */                 List<SpreadsheetCell> myRow = (List<SpreadsheetCell>)GridViewSkin.this.spreadsheetView.getGrid().getRows().get(unfixedRow.intValue());
/* 1041 */                 for (SpreadsheetCell cell : myRow) {
/* 1042 */                   if (GridViewSkin.this.spreadsheetView.getRowSpanFilter(cell) > 1) {
/* 1043 */                     GridViewSkin.this.rowToLayout.set(GridViewSkin.this.spreadsheetView.getFilteredRow(unfixedRow.intValue()), true);
/*      */                   }
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */ 
/*      */ 
/*      */             
/* 1051 */             for (Integer fixedRow : c.getAddedSubList()) {
/* 1052 */               GridViewSkin.this.rowToLayout.set(GridViewSkin.this.spreadsheetView.getFilteredRow(fixedRow.intValue()), true);
/*      */             }
/*      */           } 
/*      */ 
/*      */           
/* 1057 */           GridViewSkin.this.getFlow().requestLayout();
/*      */         }
/*      */       };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1065 */     this.currentlyFixedRowListener = new SetChangeListener<Integer>()
/*      */       {
/*      */         public void onChanged(SetChangeListener.Change<? extends Integer> arg0) {
/* 1068 */           GridViewSkin.this.computeFixedRowHeight();
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
/* 1087 */     this.fixedColumnsListener = new ListChangeListener<SpreadsheetColumn>() { public TableRow<ObservableList<SpreadsheetCell>> call(TableView<ObservableList<SpreadsheetCell>> p) { return new GridRow(handle); } }); tableView.getStyleClass().add("cell-spreadsheet"); getCurrentlyFixedRow().addListener(this.currentlyFixedRowListener); this.spreadsheetView.getFixedRows().addListener(this.fixedRowsListener); this.spreadsheetView.getFixedColumns().addListener(this.fixedColumnsListener); init(); handle.getView().gridProperty().addListener(this.rowToLayoutListener); handle.getView().hiddenRowsProperty().addListener(this.rowToLayoutListener); handle.getView().hiddenColumnsProperty().addListener(this.rowToLayoutListener); this.hBarValue = new BitSet(getItemCount()); this.rowToLayout = initRowToLayoutBitSet(); computeFixedRowHeight(); EventHandler<MouseEvent> ml = event -> { if (tableView.getEditingCell() != null) tableView.edit(-1, null);  tableView.requestFocus(); }; getFlow().getVerticalBar().addEventFilter(MouseEvent.MOUSE_PRESSED, ml); getFlow().getHorizontalBar().addEventFilter(MouseEvent.MOUSE_PRESSED, ml); TableViewBehavior<ObservableList<SpreadsheetCell>> behavior = (TableViewBehavior<ObservableList<SpreadsheetCell>>)getBehavior(); behavior.setOnFocusPreviousRow(new Runnable() { public void run() { GridViewSkin.this.onFocusPreviousCell(); } }
/*      */     ); behavior.setOnFocusNextRow(new Runnable() { public void run() { GridViewSkin.this.onFocusNextCell(); } }
/*      */     ); behavior.setOnMoveToFirstCell(new Runnable() { public void run() { GridViewSkin.this.onMoveToFirstCell(); } }); behavior.setOnMoveToLastCell(new Runnable() { public void run() { GridViewSkin.this.onMoveToLastCell(); } }); behavior.setOnScrollPageDown(new Callback<Boolean, Integer>() { public Integer call(Boolean isFocusDriven) { return Integer.valueOf(GridViewSkin.this.onScrollPageDown(isFocusDriven.booleanValue())); } }
/* 1090 */       ; init((Control)handle.getGridView()); this.handle = handle; this.spreadsheetView = handle.getView(); this.gridCellEditor = new GridCellEditor(handle); TableView<ObservableList<SpreadsheetCell>> tableView = handle.getGridView(); tableView.setRowFactory(new Callback<TableView<ObservableList<SpreadsheetCell>>, TableRow<ObservableList<SpreadsheetCell>>>() { public Integer call(Boolean isFocusDriven) { return Integer.valueOf(GridViewSkin.this.onScrollPageUp(isFocusDriven.booleanValue())); } }); behavior.setOnSelectPreviousRow(new Runnable() { public void run() { GridViewSkin.this.onSelectPreviousCell(); } }); behavior.setOnSelectNextRow(new Runnable() { public void run() { GridViewSkin.this.onSelectNextCell(); } }); behavior.setOnScrollPageUp(new Callback<Boolean, Integer>() { public void onChanged(ListChangeListener.Change<? extends SpreadsheetColumn> c) { GridViewSkin.this.hBarValue.clear();
/* 1091 */             GridViewSkin.this.getFlow().requestLayout(); } }); behavior.setOnSelectLeftCell(new Runnable() {
/*      */           public void run() { GridViewSkin.this.onSelectLeftCell(); }
/*      */         }); behavior.setOnSelectRightCell(new Runnable() {
/*      */           public void run() { GridViewSkin.this.onSelectRightCell(); }
/*      */         }); registerChangeListener((ObservableValue)tableView.fixedCellSizeProperty(), "FIXED_CELL_SIZE"); }
/*      */   public double getRowHeight(int row) { if (row == -1) return DEFAULT_CELL_HEIGHT;  Double rowHeightCache = (Double)this.rowHeightMap.get(Integer.valueOf(this.spreadsheetView.getModelRow(row))); if (rowHeightCache == null) { double rowHeight = this.handle.getView().getGrid().getRowHeight(this.spreadsheetView.getModelRow(row)); return (rowHeight == -1.0D) ? DEFAULT_CELL_HEIGHT : rowHeight; }  return rowHeightCache.doubleValue(); }
/*      */   public double getFixedRowHeight() { return this.fixedRowHeight; }
/*      */   public ObservableList<Integer> getSelectedRows() { return this.selectedRows; }
/* 1099 */   public ObservableList<Integer> getSelectedColumns() { return this.selectedColumns; } public GridCellEditor getSpreadsheetCellEditorImpl() { return this.gridCellEditor; } public GridRow getRowIndexed(int index) { List<? extends IndexedCell> cells = (List)getFlow().getCells(); if (!cells.isEmpty()) { IndexedCell cell = cells.get(0); if (index >= cell.getIndex() && index - cell.getIndex() < cells.size()) return (GridRow)cells.get(index - cell.getIndex());  }  for (IndexedCell cell : getFlow().getFixedCells()) { if (cell.getIndex() == index) return (GridRow)cell;  }  return null; } public int getFirstRow(SpreadsheetCell cell, int index) { do { index--; } while (index >= 0 && ((ObservableList)this.spreadsheetView.getItems().get(index)).get(cell.getColumn()) == cell); return index + 1; } public GridRow getRow(int index) { if (index < getFlow().getCells().size()) return getFlow().getCells().get(index);  return null; } public final boolean containsRow(int index) { for (Object obj : getFlow().getCells()) { if (((GridRow)obj).getIndex() == index && !((GridRow)obj).getChildrenUnmodifiable().isEmpty()) return true;  }  return false; } public int getCellsSize() { return getFlow().getCells().size(); } public ScrollBar getHBar() { if (getFlow() != null) return getFlow().getHorizontalBar();  return null; } public ScrollBar getVBar() { return getFlow().getVerticalBar(); } public void resizeRowsToFitContent() { Grid grid = this.spreadsheetView.getGrid(); int maxRows = this.handle.getView().getGrid().getRowCount(); for (int row = 0; row < maxRows; row++) { if (grid.isRowResizable(row)) resizeRowToFitContent(row);  }  } public void resizeRowToFitContent(int modelRow) { if (((TableView)getSkinnable()).getColumns().isEmpty()) return;  TableColumn<ObservableList<SpreadsheetCell>, ?> col = (TableColumn<ObservableList<SpreadsheetCell>, ?>)((TableView)getSkinnable()).getColumns().get(0); List<?> items = (List)itemsProperty().get(); if (items == null || items.isEmpty()) return;  if (!this.spreadsheetView.getGrid().isRowResizable(modelRow)) return;  Callback cellFactory = col.getCellFactory(); if (cellFactory == null) return;  CellView cell = (CellView)cellFactory.call(col); if (cell == null) return;  cell.getProperties().put("deferToParentPrefWidth", Boolean.TRUE); double padding = 5.0D; Node n = (cell.getSkin() == null) ? null : cell.getSkin().getNode(); if (n instanceof Region) { Region r = (Region)n; padding = r.snappedTopInset() + r.snappedBottomInset(); }  double maxHeight = 0.0D; getChildren().add(cell); int columnSize = ((TableView)getSkinnable()).getColumns().size(); for (int viewColumn = 0; viewColumn < columnSize; viewColumn++) { TableColumn column = (TableColumn)((TableView)getSkinnable()).getColumns().get(viewColumn); cell.updateTableColumn(column); cell.updateTableView(this.handle.getGridView()); cell.updateIndex(modelRow); SpreadsheetCell spc = (SpreadsheetCell)cell.getItem(); double width = column.getWidth(); if (spc != null && spc.getColumn() == viewColumn && spc.getColumnSpan() > 1) { int max = ((TableView)getSkinnable()).getVisibleLeafColumns().size() - viewColumn; for (int i = 1, colSpan = spc.getColumnSpan(); i < colSpan && i < max; i++) { double tempWidth = snapSize(((TableView)getSkinnable()).getVisibleLeafColumn(viewColumn + i).getWidth()); width += tempWidth; }  }  if (spc != null && spc.getColumn() == viewColumn && ((cell.getText() != null && !cell.getText().isEmpty()) || cell.getGraphic() != null)) { cell.setWrapText(true); cell.impl_processCSS(false); maxHeight = Math.max(maxHeight, cell.prefHeight(width)); }  }  getChildren().remove(cell); this.rowHeightMap.put(Integer.valueOf(modelRow), Double.valueOf(maxHeight + padding)); Event.fireEvent((EventTarget)this.spreadsheetView, (Event)new SpreadsheetView.RowHeightEvent(modelRow, maxHeight + padding)); this.rectangleSelection.updateRectangle(); } public void resizeRowsToMaximum() { resizeRowsToFitContent(); Grid grid = this.spreadsheetView.getGrid(); double maxHeight = 0.0D; for (Iterator<Integer> iterator = this.rowHeightMap.keySet().iterator(); iterator.hasNext(); ) { int key = ((Integer)iterator.next()).intValue(); maxHeight = Math.max(maxHeight, ((Double)this.rowHeightMap.get(Integer.valueOf(key))).doubleValue()); }  this.rowHeightMap.clear(); int maxRows = this.handle.getView().getGrid().getRows().size(); for (int modelRow = 0; modelRow < maxRows; modelRow++) { if (grid.isRowResizable(modelRow)) { Event.fireEvent((EventTarget)this.spreadsheetView, (Event)new SpreadsheetView.RowHeightEvent(modelRow, maxHeight)); this.rowHeightMap.put(Integer.valueOf(modelRow), Double.valueOf(maxHeight)); }  }  this.rectangleSelection.updateRectangle(); } public void resizeRowsToDefault() { this.rowHeightMap.clear(); Grid grid = this.spreadsheetView.getGrid(); for (GridRow row : getFlow().getCells()) { if (grid.isRowResizable(this.spreadsheetView.getModelRow(row.getIndex()))) { double newHeight = row.computePrefHeight(-1.0D); if (row.getPrefHeight() != newHeight) { row.setRowHeight(newHeight); row.requestLayout(); }  }  }  getFlow().layoutChildren(); for (GridRow row : getFlow().getCells()) { double height = getRowHeight(this.spreadsheetView.getModelRow(row.getIndex())); if (row.getHeight() != height && grid.isRowResizable(this.spreadsheetView.getModelRow(row.getIndex()))) row.setRowHeight(height);  }  this.rectangleSelection.updateRectangle(); } public void resizeColumnToFitContent(TableColumn<ObservableList<SpreadsheetCell>, ?> tc, int maxRows) { TableColumn<ObservableList<SpreadsheetCell>, ?> col = tc; List<?> items = (List)itemsProperty().get(); if (items == null || items.isEmpty()) return;  Callback cellFactory = col.getCellFactory(); if (cellFactory == null) return;  TableCell<ObservableList<SpreadsheetCell>, ?> cell = (TableCell<ObservableList<SpreadsheetCell>, ?>)cellFactory.call(col); if (cell == null) return;  int indexColumn = this.handle.getGridView().getColumns().indexOf(tc); if (maxRows == 30 && this.handle.isColumnWidthSet(indexColumn)) return;  cell.getProperties().put("deferToParentPrefWidth", Boolean.TRUE); double padding = 10.0D; Node n = (cell.getSkin() == null) ? null : cell.getSkin().getNode(); if (n instanceof Region) { Region r = (Region)n; padding = r.snappedLeftInset() + r.snappedRightInset(); }  ObservableList<ObservableList<SpreadsheetCell>> gridRows = this.spreadsheetView.getGrid().getRows(); int rows = (maxRows == -1) ? items.size() : Math.min(items.size(), (maxRows == 30) ? 100 : maxRows); double maxWidth = 0.0D; boolean datePresent = false; cell.updateTableColumn(col); cell.updateTableView(this.handle.getGridView()); if (cell.getSkin() == null) cell.setSkin((Skin)new CellViewSkin((CellView)cell));  SpreadsheetColumn column = (SpreadsheetColumn)this.spreadsheetView.getColumns().get(indexColumn); double cellFilterWidth = 0.0D; for (int row = 0; row < rows; row++) { cell.updateIndex(row); if ((cell.getText() != null && !cell.getText().isEmpty()) || cell.getGraphic() != null) { getChildren().add(cell); if (((SpreadsheetCell)cell.getItem()).getItem() instanceof java.time.LocalDate) datePresent = true;  cell.impl_processCSS(false); double width = cell.prefWidth(-1.0D); if (row == this.spreadsheetView.getFilteredRow()) cellFilterWidth = width;  SpreadsheetCell spc = (SpreadsheetCell)((ObservableList)gridRows.get(row)).get(indexColumn); if (this.spreadsheetView.getColumnSpan(spc) > 1) for (int i = this.spreadsheetView.getViewColumn(spc.getColumn()); i < this.spreadsheetView.getViewColumn(spc.getColumn()) + this.spreadsheetView.getColumnSpan(spc); i++) { if (i != indexColumn) width -= ((SpreadsheetColumn)this.spreadsheetView.getColumns().get(i)).getWidth();  }   maxWidth = Math.max(maxWidth, width); getChildren().remove(cell); }  }  cell.updateIndex(-1); double widthMax = maxWidth + padding; if (this.handle.getGridView().getColumnResizePolicy() == TableView.CONSTRAINED_RESIZE_POLICY) widthMax = Math.max(widthMax, col.getWidth());  if (datePresent && widthMax < DATE_CELL_MIN_WIDTH) widthMax = DATE_CELL_MIN_WIDTH;  if (column.getFilter() != null) { cellFilterWidth += (column.getFilter().getMenuButton().getWidth() <= 0.0D) ? 24.0D : column.getFilter().getMenuButton().getWidth(); widthMax = Math.max(widthMax, cellFilterWidth); }  widthMax = snapSize(widthMax); if (col.getPrefWidth() == widthMax && col.getWidth() != widthMax) { col.impl_setWidth(widthMax); } else { col.setPrefWidth(widthMax); }  this.rectangleSelection.updateRectangle(); } protected final void init() { this.rectangleSelection = new RectangleSelection(this, (TableViewSpanSelectionModel)this.handle.getGridView().getSelectionModel()); getFlow().getVerticalBar().valueProperty().addListener(this.vbarValueListener); this.verticalHeader = new VerticalHeader(this.handle); getChildren().add(this.verticalHeader); ((HorizontalHeader)getTableHeaderRow()).init(); this.verticalHeader.init(this, (HorizontalHeader)getTableHeaderRow()); this.horizontalPickers = new HorizontalPicker((HorizontalHeader)getTableHeaderRow(), this.spreadsheetView); getChildren().add(this.horizontalPickers); getFlow().init(this.spreadsheetView); ((GridViewBehavior)getBehavior()).setGridViewSkin(this); } protected TableSelectionModel<ObservableList<SpreadsheetCell>> getSelectionModel() { return (TableSelectionModel<ObservableList<SpreadsheetCell>>)((TableView)getSkinnable()).getSelectionModel(); } protected final ObservableSet<Integer> getCurrentlyFixedRow() { return this.currentlyFixedRow; } public void resize(TableColumnBase<?, ?> tc, int maxRows) { if (tc.isResizable()) { int columnIndex = getColumns().indexOf(tc); TableColumn<ObservableList<SpreadsheetCell>, ?> tableColumn = (TableColumn)getColumns().get(columnIndex); resizeColumnToFitContent(tableColumn, maxRows); Event.fireEvent((EventTarget)this.spreadsheetView, (Event)new SpreadsheetView.ColumnWidthEvent(columnIndex, tableColumn.getWidth())); }  } protected void layoutChildren(double x, double y, double w, double h) { if (this.spreadsheetView == null) return;  double verticalHeaderWidth = this.verticalHeader.computeHeaderWidth(); double horizontalPickerHeight = this.spreadsheetView.getColumnPickers().isEmpty() ? 0.0D : 16.0D; if (this.spreadsheetView.isShowRowHeader() || !this.spreadsheetView.getRowPickers().isEmpty()) { x += verticalHeaderWidth; w -= verticalHeaderWidth; } else { x = 0.0D; }  y += horizontalPickerHeight; super.layoutChildren(x, y, w, h - horizontalPickerHeight); double baselineOffset = ((TableView)getSkinnable()).getLayoutBounds().getHeight() / 2.0D; double tableHeaderRowHeight = 0.0D; if (!this.spreadsheetView.getColumnPickers().isEmpty()) layoutInArea((Node)this.horizontalPickers, x, y - 16.0D, w, tableHeaderRowHeight, baselineOffset, HPos.CENTER, VPos.CENTER);  if (this.spreadsheetView.showColumnHeaderProperty().get()) { tableHeaderRowHeight = getTableHeaderRow().prefHeight(-1.0D); tableHeaderRowHeight = (tableHeaderRowHeight < DEFAULT_CELL_HEIGHT) ? DEFAULT_CELL_HEIGHT : tableHeaderRowHeight; layoutInArea((Node)getTableHeaderRow(), x, y, w, tableHeaderRowHeight, baselineOffset, HPos.CENTER, VPos.CENTER); y += tableHeaderRowHeight; }  if (this.spreadsheetView.isShowRowHeader() || !this.spreadsheetView.getRowPickers().isEmpty()) layoutInArea((Node)this.verticalHeader, x - verticalHeaderWidth, y - tableHeaderRowHeight, w, h, baselineOffset, HPos.CENTER, VPos.CENTER);  } protected void onFocusPreviousCell() { focusScroll(); } protected void onFocusNextCell() { focusScroll(); } private int getFixedRowSize() { int i = 0; for (Integer fixedRow : this.spreadsheetView.getFixedRows()) { if (!this.spreadsheetView.getHiddenRows().get(fixedRow.intValue())) i++;  }  return i; } void focusScroll() { TableFocusModel<?, ?> fm = getFocusModel(); if (fm == null) return;  int row = fm.getFocusedIndex(); if (!getFlow().getCells().isEmpty() && ((IndexedCell)getFlow().getCells().get(getFixedRowSize())).getIndex() > row && !this.spreadsheetView.getFixedRows().contains(Integer.valueOf(this.spreadsheetView.getModelRow(row)))) { this.flow.scrollTo(row); } else { this.flow.show(row); }  scrollHorizontally(); } protected void onSelectPreviousCell() { super.onSelectPreviousCell(); scrollHorizontally(); } protected void onSelectNextCell() { super.onSelectNextCell(); scrollHorizontally(); } protected VirtualFlow<TableRow<ObservableList<SpreadsheetCell>>> createVirtualFlow() { return new GridVirtualFlow<>(this); } protected TableHeaderRow createTableHeaderRow() { return new HorizontalHeader(this); } protected HorizontalHeader getHorizontalHeader() { return (HorizontalHeader)getTableHeaderRow(); } BooleanProperty getTableMenuButtonVisibleProperty() { return tableMenuButtonVisibleProperty(); } public void scrollHorizontally() { super.scrollHorizontally(); } protected void scrollHorizontally(TableColumn<ObservableList<SpreadsheetCell>, ?> col) { if (col == null || !col.isVisible()) return;  this.fixedColumnWidth = 0.0D; double pos = getFlow().getHorizontalBar().getValue(); int index = getColumns().indexOf(col); double start = 0.0D; for (int columnIndex = 0; columnIndex < index; columnIndex++) { if (!this.spreadsheetView.isColumnHidden(columnIndex)) { SpreadsheetColumn column = (SpreadsheetColumn)this.spreadsheetView.getColumns().get(columnIndex); if (column.isFixed()) this.fixedColumnWidth += column.getWidth();  start += column.getWidth(); }  }  double end = start + col.getWidth(); double headerWidth = this.handle.getView().getWidth() - snappedLeftInset() - snappedRightInset() - this.verticalHeader.getVerticalHeaderWidth(); double max = getFlow().getHorizontalBar().getMax(); if (start < pos + this.fixedColumnWidth && start >= 0.0D && start >= this.fixedColumnWidth) { double newPos = (start - this.fixedColumnWidth < 0.0D) ? start : (start - this.fixedColumnWidth); getFlow().getHorizontalBar().setValue(newPos); } else if (start > pos + headerWidth) { double delta = (start < 0.0D || end > headerWidth) ? (start - pos - this.fixedColumnWidth) : 0.0D; double newPos = (pos + delta > max) ? max : (pos + delta); getFlow().getHorizontalBar().setValue(newPos); }  }
/*      */   private void verticalScroll() { this.verticalHeader.requestLayout(); }
/*      */   GridVirtualFlow<?> getFlow() { return (GridVirtualFlow)this.flow; }
/*      */   private BitSet initRowToLayoutBitSet() { int rowCount = getItemCount(); BitSet bitSet = new BitSet(rowCount); for (int row = 0; row < rowCount; row++) { if (this.spreadsheetView.getFixedRows().contains(Integer.valueOf(this.spreadsheetView.getModelRow(row)))) { bitSet.set(row); } else { List<SpreadsheetCell> myRow = (List<SpreadsheetCell>)this.handle.getGridView().getItems().get(row); for (SpreadsheetCell cell : myRow) { if (this.spreadsheetView.getRowSpanFilter(cell) > 1) { bitSet.set(row); break; }  }  }  }  return bitSet; }
/*      */   public void computeFixedRowHeight() { this.fixedRowHeight = 0.0D; for (Iterator<Integer> iterator = getCurrentlyFixedRow().iterator(); iterator.hasNext(); ) { int i = ((Integer)iterator.next()).intValue(); this.fixedRowHeight += getRowHeight(i); }  }
/* 1104 */   protected TableFocusModel<ObservableList<SpreadsheetCell>, TableColumn<ObservableList<SpreadsheetCell>, ?>> getFocusModel() { return (TableFocusModel<ObservableList<SpreadsheetCell>, TableColumn<ObservableList<SpreadsheetCell>, ?>>)((TableView)getSkinnable()).getFocusModel(); }
/*      */ 
/*      */ 
/*      */   
/*      */   protected TablePositionBase<? extends TableColumn<ObservableList<SpreadsheetCell>, ?>> getFocusedCell() {
/* 1109 */     return (TablePositionBase<? extends TableColumn<ObservableList<SpreadsheetCell>, ?>>)((TableView)getSkinnable()).getFocusModel().getFocusedCell();
/*      */   }
/*      */ 
/*      */   
/*      */   protected ObservableList<? extends TableColumn<ObservableList<SpreadsheetCell>, ?>> getVisibleLeafColumns() {
/* 1114 */     return ((TableView)getSkinnable()).getVisibleLeafColumns();
/*      */   }
/*      */ 
/*      */   
/*      */   protected int getVisibleLeafIndex(TableColumn<ObservableList<SpreadsheetCell>, ?> tc) {
/* 1119 */     return ((TableView)getSkinnable()).getVisibleLeafIndex(tc);
/*      */   }
/*      */ 
/*      */   
/*      */   protected TableColumn<ObservableList<SpreadsheetCell>, ?> getVisibleLeafColumn(int col) {
/* 1124 */     return ((TableView)getSkinnable()).getVisibleLeafColumn(col);
/*      */   }
/*      */ 
/*      */   
/*      */   protected ObservableList<TableColumn<ObservableList<SpreadsheetCell>, ?>> getColumns() {
/* 1129 */     return ((TableView)getSkinnable()).getColumns();
/*      */   }
/*      */ 
/*      */   
/*      */   protected ObservableList<TableColumn<ObservableList<SpreadsheetCell>, ?>> getSortOrder() {
/* 1134 */     return ((TableView)getSkinnable()).getSortOrder();
/*      */   }
/*      */ 
/*      */   
/*      */   protected ObjectProperty<ObservableList<ObservableList<SpreadsheetCell>>> itemsProperty() {
/* 1139 */     return ((TableView)getSkinnable()).itemsProperty();
/*      */   }
/*      */ 
/*      */   
/*      */   protected ObjectProperty<Callback<TableView<ObservableList<SpreadsheetCell>>, TableRow<ObservableList<SpreadsheetCell>>>> rowFactoryProperty() {
/* 1144 */     return ((TableView)getSkinnable()).rowFactoryProperty();
/*      */   }
/*      */ 
/*      */   
/*      */   protected ObjectProperty<Node> placeholderProperty() {
/* 1149 */     return ((TableView)getSkinnable()).placeholderProperty();
/*      */   }
/*      */ 
/*      */   
/*      */   protected BooleanProperty tableMenuButtonVisibleProperty() {
/* 1154 */     return ((TableView)getSkinnable()).tableMenuButtonVisibleProperty();
/*      */   }
/*      */ 
/*      */   
/*      */   protected ObjectProperty<Callback<ResizeFeaturesBase, Boolean>> columnResizePolicyProperty() {
/* 1159 */     return ((TableView)getSkinnable()).columnResizePolicyProperty();
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean resizeColumn(TableColumn<ObservableList<SpreadsheetCell>, ?> tc, double delta) {
/* 1164 */     (getHorizontalHeader().getRootHeader()).lastColumnResized = getColumns().indexOf(tc);
/* 1165 */     boolean returnedValue = ((TableView)getSkinnable()).resizeColumn(tc, delta);
/* 1166 */     if (returnedValue) {
/* 1167 */       Event.fireEvent((EventTarget)this.spreadsheetView, (Event)new SpreadsheetView.ColumnWidthEvent(getColumns().indexOf(tc), tc.getWidth()));
/*      */     }
/* 1169 */     return returnedValue;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void edit(int index, TableColumn<ObservableList<SpreadsheetCell>, ?> column) {
/* 1174 */     ((TableView)getSkinnable()).edit(index, column);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public TableRow<ObservableList<SpreadsheetCell>> createCell() {
/*      */     TableRow<ObservableList<SpreadsheetCell>> cell;
/* 1181 */     if (((TableView)getSkinnable()).getRowFactory() != null) {
/* 1182 */       cell = (TableRow<ObservableList<SpreadsheetCell>>)((TableView)getSkinnable()).getRowFactory().call(getSkinnable());
/*      */     } else {
/* 1184 */       cell = new TableRow();
/*      */     } 
/*      */     
/* 1187 */     cell.updateTableView((TableView)getSkinnable());
/* 1188 */     return cell;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getItemCount() {
/* 1193 */     return (((TableView)getSkinnable()).getItems() == null) ? 0 : ((TableView)getSkinnable()).getItems().size();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHbarValue(double value) {
/* 1203 */     setHbarValue(value, 0);
/*      */   }
/*      */   
/*      */   public void setHbarValue(double value, int count) {
/* 1207 */     if (count > 5) {
/*      */       return;
/*      */     }
/* 1210 */     int newCount = count + 1;
/* 1211 */     if (this.flow.getScene() == null) {
/* 1212 */       Platform.runLater(() -> setHbarValue(value, newCount));
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 1217 */     getHBar().setValue(value);
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\spreadsheet\GridViewSkin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */