/*     */ package impl.org.controlsfx.table;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiPredicate;
/*     */ import java.util.stream.Stream;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.beans.value.ChangeListener;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.beans.value.WeakChangeListener;
/*     */ import javafx.collections.FXCollections;
/*     */ import javafx.collections.ListChangeListener;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.collections.WeakListChangeListener;
/*     */ import javafx.scene.control.ContextMenu;
/*     */ import javafx.scene.control.CustomMenuItem;
/*     */ import javafx.scene.control.TableColumn;
/*     */ import javafx.stage.WindowEvent;
/*     */ import org.controlsfx.control.table.TableFilter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ColumnFilter<T, R>
/*     */ {
/*     */   private final TableFilter<T> tableFilter;
/*     */   private final TableColumn<T, R> tableColumn;
/*     */   private final ObservableList<FilterValue<T, R>> filterValues;
/*  54 */   private final DupeCounter<R> filterValuesDupeCounter = new DupeCounter<>(false);
/*  55 */   private final DupeCounter<R> visibleValuesDupeCounter = new DupeCounter<>(false);
/*  56 */   private final HashSet<R> unselectedValues = new HashSet<>(); private boolean lastFilter = false; private boolean isDirty = false; private BiPredicate<String, String> searchStrategy; private volatile FilterPanel filterPanel; private boolean initialized;
/*  57 */   private final HashMap<CellIdentity<T>, ChangeListener<R>> trackedCells = new HashMap<>(); private final ListChangeListener<T> backingListListener; private final ListChangeListener<T> itemsListener; private final ChangeListener<R> changeListener;
/*     */   private final ListChangeListener<FilterValue<T, R>> filterValueListChangeListener;
/*     */   
/*     */   public ColumnFilter(TableFilter<T> tableFilter, TableColumn<T, R> tableColumn) {
/*  61 */     this.searchStrategy = ((inputString, subjectString) -> subjectString.contains(inputString));
/*     */ 
/*     */     
/*  64 */     this.initialized = false;
/*     */     
/*  66 */     this.backingListListener = (lc -> {
/*     */         while (lc.next()) {
/*     */           if (lc.wasAdded()) {
/*     */             lc.getAddedSubList().stream().forEach(());
/*     */           }
/*     */ 
/*     */           
/*     */           if (lc.wasRemoved()) {
/*     */             lc.getRemoved().stream().forEach(());
/*     */           }
/*     */         } 
/*     */       });
/*     */     
/*  79 */     this.itemsListener = (lc -> {
/*     */         while (lc.next()) {
/*     */           if (lc.wasAdded()) {
/*     */             lc.getAddedSubList().stream().map(getTableColumn()::getCellObservableValue).forEach(this::addVisibleItem);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           if (lc.wasRemoved()) {
/*     */             lc.getRemoved().stream().map(getTableColumn()::getCellObservableValue).forEach(this::removeVisibleItem);
/*     */           }
/*     */         } 
/*     */       });
/*     */ 
/*     */     
/*  94 */     this.changeListener = ((observable, oldValue, newValue) -> {
/*     */         if (this.filterValuesDupeCounter.add((R)newValue) == 1) {
/*     */           getFilterValues().add(new FilterValue<>(newValue, (ColumnFilter)this));
/*     */         }
/*     */         
/*     */         removeValue((R)oldValue);
/*     */       });
/* 101 */     this.filterValueListChangeListener = (lc -> {
/*     */         while (lc.next()) {
/*     */           if (lc.wasRemoved()) {
/*     */             lc.getRemoved().stream().filter(()).forEach(this.unselectedValues::remove);
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           if (lc.wasUpdated()) {
/*     */             int from = lc.getFrom();
/*     */ 
/*     */ 
/*     */             
/*     */             int to = lc.getTo();
/*     */ 
/*     */ 
/*     */             
/*     */             lc.getList().subList(from, to).forEach(());
/*     */           } 
/*     */         } 
/*     */       });
/*     */ 
/*     */ 
/*     */     
/* 126 */     this.tableFilter = tableFilter;
/* 127 */     this.tableColumn = tableColumn;
/*     */     
/* 129 */     this.filterValues = FXCollections.observableArrayList(cb -> new Observable[] { (Observable)cb.selectedProperty() });
/* 130 */     attachContextMenu();
/*     */   }
/*     */   void setFilterPanel(FilterPanel filterPanel) {
/* 133 */     this.filterPanel = filterPanel;
/*     */   }
/*     */   FilterPanel getFilterPanel() {
/* 136 */     return this.filterPanel;
/*     */   }
/*     */   public void initialize() {
/* 139 */     if (!this.initialized) {
/* 140 */       initializeListeners();
/* 141 */       initializeValues();
/* 142 */       this.initialized = true;
/*     */     } 
/*     */   }
/*     */   public boolean isInitialized() {
/* 146 */     return this.initialized;
/*     */   }
/*     */   
/*     */   public void selectValue(Object value) {
/* 150 */     this.filterPanel.selectValue(value);
/*     */   }
/*     */   public void unselectValue(Object value) {
/* 153 */     this.filterPanel.unSelectValue(value);
/*     */   }
/*     */   public void selectAllValues() {
/* 156 */     this.filterPanel.selectAllValues();
/*     */   }
/*     */   public void unSelectAllValues() {
/* 159 */     this.filterPanel.unSelectAllValues();
/*     */   }
/*     */   public boolean wasLastFiltered() {
/* 162 */     return this.lastFilter;
/*     */   }
/*     */   public boolean hasUnselections() {
/* 165 */     return (this.unselectedValues.size() != 0);
/*     */   }
/*     */   public void setSearchStrategy(BiPredicate<String, String> searchStrategy) {
/* 168 */     this.searchStrategy = searchStrategy;
/*     */   }
/*     */   public BiPredicate<String, String> getSearchStrategy() {
/* 171 */     return this.searchStrategy;
/*     */   }
/*     */   public boolean isFiltered() {
/* 174 */     return (this.isDirty || this.unselectedValues.size() > 0);
/*     */   }
/*     */   public boolean valueIsVisible(R value) {
/* 177 */     return (this.visibleValuesDupeCounter.get(value) > 0);
/*     */   }
/*     */   public void applyFilter() {
/* 180 */     this.tableFilter.executeFilter();
/* 181 */     this.lastFilter = true;
/* 182 */     this.tableFilter.getColumnFilters().stream().filter(c -> !c.equals(this)).forEach(c -> c.lastFilter = false);
/* 183 */     this.tableFilter.getColumnFilters().stream().flatMap(c -> c.filterValues.stream()).forEach(FilterValue::refreshScope);
/* 184 */     this.isDirty = false;
/*     */   }
/*     */   
/*     */   public void resetAllFilters() {
/* 188 */     this.tableFilter.getColumnFilters().stream().flatMap(c -> c.filterValues.stream()).forEach(fv -> fv.selectedProperty().set(true));
/* 189 */     this.tableFilter.resetFilter();
/* 190 */     this.tableFilter.getColumnFilters().stream().forEach(c -> c.lastFilter = false);
/* 191 */     this.tableFilter.getColumnFilters().stream().flatMap(c -> c.filterValues.stream()).forEach(FilterValue::refreshScope);
/* 192 */     this.isDirty = false;
/*     */   }
/*     */   
/*     */   public ObservableList<FilterValue<T, R>> getFilterValues() {
/* 196 */     return this.filterValues;
/*     */   }
/*     */   
/*     */   public TableColumn<T, R> getTableColumn() {
/* 200 */     return this.tableColumn;
/*     */   }
/*     */   public TableFilter<T> getTableFilter() {
/* 203 */     return this.tableFilter;
/*     */   }
/*     */   public boolean evaluate(T item) {
/* 206 */     ObservableValue<R> value = this.tableColumn.getCellObservableValue(item);
/*     */     
/* 208 */     return (this.unselectedValues.size() == 0 || 
/* 209 */       !this.unselectedValues.contains(value.getValue()));
/*     */   }
/*     */   
/*     */   private void initializeValues() {
/* 213 */     this.tableFilter.getBackingList().stream()
/* 214 */       .forEach(t -> addBackingItem((T)t, this.tableColumn.getCellObservableValue(t)));
/* 215 */     this.tableFilter.getTableView().getItems().stream()
/* 216 */       .map(this.tableColumn::getCellObservableValue).forEach(this::addVisibleItem);
/*     */   }
/*     */ 
/*     */   
/*     */   private void addBackingItem(T item, ObservableValue<R> cellValue) {
/* 221 */     if (cellValue == null) {
/*     */       return;
/*     */     }
/* 224 */     if (this.filterValuesDupeCounter.add((R)cellValue.getValue()) == 1) {
/* 225 */       this.filterValues.add(new FilterValue<>(cellValue.getValue(), (ColumnFilter)this));
/*     */     }
/*     */ 
/*     */     
/* 229 */     CellIdentity<T> trackedCellValue = new CellIdentity<>(item);
/*     */     
/* 231 */     WeakChangeListener weakChangeListener = new WeakChangeListener(this.changeListener);
/* 232 */     cellValue.addListener((ChangeListener)weakChangeListener);
/* 233 */     this.trackedCells.put(trackedCellValue, weakChangeListener);
/*     */   }
/*     */   private void removeBackingItem(T item, ObservableValue<R> cellValue) {
/* 236 */     if (cellValue == null) {
/*     */       return;
/*     */     }
/* 239 */     removeValue((R)cellValue.getValue());
/*     */ 
/*     */     
/* 242 */     ChangeListener<R> listener = this.trackedCells.get(new CellIdentity<>(item));
/* 243 */     cellValue.removeListener(listener);
/* 244 */     this.trackedCells.remove(new CellIdentity<>(item));
/*     */   }
/*     */   private void removeValue(R value) {
/* 247 */     boolean removedLastDuplicate = (this.filterValuesDupeCounter.remove(value) == 0);
/* 248 */     if (removedLastDuplicate) {
/*     */ 
/*     */       
/* 251 */       Optional<FilterValue<T, R>> existingFilterValue = getFilterValues().stream().filter(fv -> Objects.equals(fv.getValue(), value)).findAny();
/*     */       
/* 253 */       if (existingFilterValue.isPresent())
/* 254 */         getFilterValues().remove(existingFilterValue.get()); 
/*     */     } 
/*     */   }
/*     */   private void addVisibleItem(ObservableValue<R> cellValue) {
/* 258 */     if (cellValue != null)
/* 259 */       this.visibleValuesDupeCounter.add((R)cellValue.getValue()); 
/*     */   }
/*     */   
/*     */   private void removeVisibleItem(ObservableValue<R> cellValue) {
/* 263 */     if (cellValue != null) {
/* 264 */       this.visibleValuesDupeCounter.remove((R)cellValue.getValue());
/*     */     }
/*     */   }
/*     */   
/*     */   private void initializeListeners() {
/* 269 */     this.tableFilter.getBackingList().addListener((ListChangeListener)new WeakListChangeListener(this.backingListListener));
/*     */ 
/*     */     
/* 272 */     this.tableFilter.getTableView().getItems().addListener((ListChangeListener)new WeakListChangeListener(this.itemsListener));
/*     */ 
/*     */     
/* 275 */     this.filterValues.addListener((ListChangeListener)new WeakListChangeListener(this.filterValueListChangeListener));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void attachContextMenu() {
/* 281 */     ContextMenu contextMenu = new ContextMenu();
/*     */     
/* 283 */     CustomMenuItem item = FilterPanel.getInMenuItem(this, contextMenu);
/*     */     
/* 285 */     contextMenu.getStyleClass().add("column-filter");
/* 286 */     contextMenu.getItems().add(item);
/*     */     
/* 288 */     this.tableColumn.setContextMenu(contextMenu);
/*     */     
/* 290 */     contextMenu.setOnShowing(ae -> initialize());
/*     */   }
/*     */   
/*     */   private static final class CellIdentity<T> {
/*     */     private final T item;
/*     */     
/*     */     CellIdentity(T item) {
/* 297 */       this.item = item;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 302 */       return (this.item == ((CellIdentity)other).item);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 307 */       return System.identityHashCode(this.item);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\table\ColumnFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */