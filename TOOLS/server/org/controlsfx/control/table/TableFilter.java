/*     */ package org.controlsfx.control.table;
/*     */ 
/*     */ import impl.org.controlsfx.table.ColumnFilter;
/*     */ import java.util.Collection;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiPredicate;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.collections.FXCollections;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.collections.transformation.FilteredList;
/*     */ import javafx.collections.transformation.SortedList;
/*     */ import javafx.scene.control.TableColumn;
/*     */ import javafx.scene.control.TableView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class TableFilter<T>
/*     */ {
/*     */   private final TableView<T> tableView;
/*     */   private final ObservableList<T> backingList;
/*     */   private final FilteredList<T> filteredList;
/*  58 */   private final ObservableList<ColumnFilter<T, ?>> columnFilters = FXCollections.observableArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public TableFilter(TableView<T> tableView) {
/*  66 */     this(tableView, false);
/*     */   }
/*     */   
/*     */   private TableFilter(TableView<T> tableView, boolean isLazy) {
/*  70 */     this.tableView = tableView;
/*  71 */     this.backingList = tableView.getItems();
/*  72 */     this.filteredList = new FilteredList((ObservableList)new SortedList(this.backingList));
/*  73 */     SortedList<T> sortedControlList = new SortedList((ObservableList)this.filteredList);
/*     */     
/*  75 */     this.filteredList.setPredicate(v -> true);
/*     */     
/*  77 */     sortedControlList.comparatorProperty().bind((ObservableValue)tableView.comparatorProperty());
/*  78 */     tableView.setItems((ObservableList)sortedControlList);
/*     */     
/*  80 */     applyForAllColumns(isLazy);
/*  81 */     tableView.getStylesheets().add("/impl/org/controlsfx/table/tablefilter.css");
/*     */     
/*  83 */     if (!isLazy) {
/*  84 */       this.columnFilters.forEach(ColumnFilter::initialize);
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
/*     */   public void setSearchStrategy(BiPredicate<String, String> searchStrategy) {
/*  98 */     this.columnFilters.forEach(cf -> cf.setSearchStrategy(searchStrategy));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObservableList<T> getBackingList() {
/* 105 */     return this.backingList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FilteredList<T> getFilteredList() {
/* 112 */     return this.filteredList;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void applyForAllColumns(boolean isLazy) {
/* 118 */     this.columnFilters.setAll((Collection)this.tableView.getColumns().stream().flatMap(this::extractNestedColumns)
/* 119 */         .map(c -> new ColumnFilter(this, c)).collect(Collectors.toList()));
/*     */   }
/*     */   private <S> Stream<TableColumn<T, ?>> extractNestedColumns(TableColumn<T, S> tableColumn) {
/* 122 */     if (tableColumn.getColumns().size() == 0) {
/* 123 */       return Stream.of(tableColumn);
/*     */     }
/* 125 */     return tableColumn.getColumns().stream().flatMap(this::extractNestedColumns);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void selectValue(TableColumn<?, ?> column, Object value) {
/* 133 */     this.columnFilters.stream().filter(c -> (c.getTableColumn() == column))
/* 134 */       .forEach(c -> c.selectValue(value));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void unselectValue(TableColumn<?, ?> column, Object value) {
/* 140 */     this.columnFilters.stream().filter(c -> (c.getTableColumn() == column))
/* 141 */       .forEach(c -> c.unselectValue(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void selectAllValues(TableColumn<?, ?> column) {
/* 149 */     this.columnFilters.stream().filter(c -> (c.getTableColumn() == column))
/* 150 */       .forEach(ColumnFilter::selectAllValues);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unSelectAllValues(TableColumn<?, ?> column) {
/* 157 */     this.columnFilters.stream().filter(c -> (c.getTableColumn() == column))
/* 158 */       .forEach(ColumnFilter::unSelectAllValues);
/*     */   }
/*     */   public void executeFilter() {
/* 161 */     if (this.columnFilters.stream().filter(ColumnFilter::isFiltered).findAny().isPresent()) {
/* 162 */       this.filteredList.setPredicate(item -> !this.columnFilters.stream().filter(()).findAny().isPresent());
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 167 */       resetFilter();
/*     */     } 
/*     */   }
/*     */   public void resetFilter() {
/* 171 */     this.filteredList.setPredicate(item -> true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TableView<T> getTableView() {
/* 177 */     return this.tableView;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ObservableList<ColumnFilter<T, ?>> getColumnFilters() {
/* 183 */     return this.columnFilters;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<ColumnFilter<T, ?>> getColumnFilter(TableColumn<T, ?> tableColumn) {
/* 189 */     return this.columnFilters.stream().filter(f -> f.getTableColumn().equals(tableColumn)).findAny();
/*     */   }
/*     */   public boolean isDirty() {
/* 192 */     return this.columnFilters.stream().filter(ColumnFilter::isFiltered).findAny().isPresent();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Builder<T> forTableView(TableView<T> tableView) {
/* 201 */     return new Builder<>(tableView);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Builder<T>
/*     */   {
/*     */     private final TableView<T> tableView;
/*     */     
/*     */     private volatile boolean lazyInd = false;
/*     */ 
/*     */     
/*     */     private Builder(TableView<T> tableView) {
/* 214 */       this.tableView = tableView;
/*     */     }
/*     */     public Builder<T> lazy(boolean isLazy) {
/* 217 */       this.lazyInd = isLazy;
/* 218 */       return this;
/*     */     }
/*     */     public TableFilter<T> apply() {
/* 221 */       return new TableFilter<>(this.tableView, this.lazyInd);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\table\TableFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */