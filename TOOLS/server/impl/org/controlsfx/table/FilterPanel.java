/*     */ package impl.org.controlsfx.table;
/*     */ 
/*     */ import com.sun.javafx.scene.control.skin.NestedTableColumnHeader;
/*     */ import com.sun.javafx.scene.control.skin.TableColumnHeader;
/*     */ import com.sun.javafx.scene.control.skin.TableViewSkin;
/*     */ import impl.org.controlsfx.i18n.Localization;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Supplier;
/*     */ import javafx.beans.InvalidationListener;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.beans.WeakInvalidationListener;
/*     */ import javafx.beans.value.ChangeListener;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.beans.value.WeakChangeListener;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.collections.transformation.FilteredList;
/*     */ import javafx.collections.transformation.SortedList;
/*     */ import javafx.event.ActionEvent;
/*     */ import javafx.geometry.Insets;
/*     */ import javafx.geometry.Pos;
/*     */ import javafx.geometry.Side;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Button;
/*     */ import javafx.scene.control.ContextMenu;
/*     */ import javafx.scene.control.CustomMenuItem;
/*     */ import javafx.scene.control.ListView;
/*     */ import javafx.scene.control.Skin;
/*     */ import javafx.scene.control.TableColumn;
/*     */ import javafx.scene.control.TextField;
/*     */ import javafx.scene.image.Image;
/*     */ import javafx.scene.image.ImageView;
/*     */ import javafx.scene.input.ContextMenuEvent;
/*     */ import javafx.scene.layout.HBox;
/*     */ import javafx.scene.layout.Priority;
/*     */ import javafx.scene.layout.VBox;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FilterPanel<T, R>
/*     */   extends VBox
/*     */ {
/*     */   private final ColumnFilter<T, R> columnFilter;
/*     */   private final FilteredList<FilterValue> filterList;
/*  61 */   private final TextField searchBox = new TextField();
/*     */   
/*     */   private boolean searchMode = false;
/*     */   
/*     */   private boolean bumpedWidth = false;
/*     */   
/*     */   private final ListView<FilterValue> checkListView;
/*  68 */   private final Collection<InvalidationListener> columnHeadersChangeListeners = new ArrayList<>();
/*     */   
/*  70 */   private static final Image filterIcon = new Image("/impl/org/controlsfx/table/filter.png"); private static final Supplier<ImageView> filterImageView; private final ChangeListener<Skin<?>> skinListener;
/*     */   static {
/*  72 */     filterImageView = (() -> {
/*     */         ImageView imageView = new ImageView(filterIcon);
/*     */         imageView.setFitHeight(15.0D);
/*     */         imageView.setPreserveRatio(true);
/*     */         return imageView;
/*     */       });
/*     */   }
/*  79 */   FilterPanel(ColumnFilter<T, R> columnFilter, ContextMenu contextMenu) { this.skinListener = ((w, o, n) -> {
/*     */         this.columnHeadersChangeListeners.clear();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         if (n instanceof TableViewSkin) {
/*     */           TableViewSkin<?> skin = (TableViewSkin)n;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           checkChangeContextMenu(skin, getColumnFilter().getTableColumn(), this);
/*     */         } 
/*     */       });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 107 */     columnFilter.setFilterPanel(this);
/* 108 */     this.columnFilter = columnFilter;
/* 109 */     getStyleClass().add("filter-panel");
/*     */ 
/*     */     
/* 112 */     setPadding(new Insets(3.0D));
/*     */     
/* 114 */     this.searchBox.setPromptText(Localization.getString("filterpanel.search.field"));
/* 115 */     getChildren().add(this.searchBox);
/*     */ 
/*     */ 
/*     */     
/* 119 */     this.filterList = new FilteredList((ObservableList)new SortedList(columnFilter.getFilterValues()), t -> true);
/* 120 */     this.checkListView = new ListView();
/* 121 */     this.checkListView.setItems((ObservableList)new SortedList((ObservableList)this.filterList, FilterValue::compareTo));
/*     */     
/* 123 */     getChildren().add(this.checkListView);
/*     */ 
/*     */     
/* 126 */     HBox buttonBox = new HBox();
/*     */     
/* 128 */     Button applyBttn = new Button(Localization.getString("filterpanel.apply.button"));
/* 129 */     HBox.setHgrow((Node)applyBttn, Priority.ALWAYS);
/*     */     
/* 131 */     applyBttn.setOnAction(e -> {
/*     */           if (this.searchMode) {
/*     */             this.filterList.forEach(());
/*     */ 
/*     */ 
/*     */             
/*     */             columnFilter.getFilterValues().stream().filter(()).forEach(());
/*     */ 
/*     */ 
/*     */             
/*     */             resetSearchFilter();
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/*     */           if (columnFilter.getTableFilter().isDirty()) {
/*     */             columnFilter.applyFilter();
/*     */ 
/*     */ 
/*     */             
/*     */             columnFilter.getTableFilter().getColumnFilters().stream().map(ColumnFilter::getFilterPanel).forEach(());
/*     */           } 
/*     */ 
/*     */           
/*     */           contextMenu.hide();
/*     */         });
/*     */ 
/*     */     
/* 159 */     buttonBox.getChildren().add(applyBttn);
/*     */ 
/*     */     
/* 162 */     Button unselectAllButton = new Button(Localization.getString("filterpanel.none.button"));
/* 163 */     HBox.setHgrow((Node)unselectAllButton, Priority.ALWAYS);
/*     */     
/* 165 */     unselectAllButton.setOnAction(e -> columnFilter.getFilterValues().forEach(()));
/* 166 */     buttonBox.getChildren().add(unselectAllButton);
/*     */ 
/*     */     
/* 169 */     Button selectAllButton = new Button(Localization.getString("filterpanel.all.button"));
/* 170 */     HBox.setHgrow((Node)selectAllButton, Priority.ALWAYS);
/*     */     
/* 172 */     selectAllButton.setOnAction(e -> columnFilter.getFilterValues().forEach(()));
/*     */ 
/*     */ 
/*     */     
/* 176 */     buttonBox.getChildren().add(selectAllButton);
/*     */     
/* 178 */     Button clearAllButton = new Button(Localization.getString("filterpanel.resetall.button"));
/* 179 */     HBox.setHgrow((Node)clearAllButton, Priority.ALWAYS);
/*     */     
/* 181 */     clearAllButton.setOnAction(e -> {
/*     */           columnFilter.resetAllFilters();
/*     */           columnFilter.getTableFilter().getColumnFilters().stream().forEach(());
/*     */           contextMenu.hide();
/*     */         });
/* 186 */     buttonBox.getChildren().add(clearAllButton);
/*     */     
/* 188 */     buttonBox.setAlignment(Pos.BASELINE_CENTER);
/*     */ 
/*     */     
/* 191 */     getChildren().add(buttonBox); }
/*     */   void selectAllValues() { this.checkListView.getItems().stream().forEach(item -> item.selectedProperty().set(true)); }
/*     */   void unSelectAllValues() {
/*     */     this.checkListView.getItems().stream().forEach(item -> item.selectedProperty().set(false));
/* 195 */   } public void resetSearchFilter() { this.filterList.setPredicate(t -> true);
/* 196 */     this.searchBox.clear(); }
/*     */   void selectValue(Object value) { this.checkListView.getItems().stream().filter(item -> item.getValue().equals(value)).forEach(item -> item.selectedProperty().set(true)); } void unSelectValue(Object value) {
/*     */     this.checkListView.getItems().stream().filter(item -> (item.getValue() == value)).forEach(item -> item.selectedProperty().set(false));
/*     */   } public static <T, R> CustomMenuItem getInMenuItem(ColumnFilter<T, R> columnFilter, ContextMenu contextMenu) {
/* 200 */     FilterPanel<T, R> filterPanel = new FilterPanel<>(columnFilter, contextMenu);
/*     */     
/* 202 */     CustomMenuItem menuItem = new CustomMenuItem();
/*     */     
/* 204 */     filterPanel.initializeListeners();
/*     */     
/* 206 */     menuItem.contentProperty().set(filterPanel);
/*     */     
/* 208 */     columnFilter.getTableFilter().getTableView().skinProperty().addListener((ChangeListener)new WeakChangeListener(filterPanel.skinListener));
/*     */     
/* 210 */     menuItem.setHideOnClick(false);
/* 211 */     return menuItem;
/*     */   }
/*     */   private void initializeListeners() {
/* 214 */     this.searchBox.textProperty().addListener(l -> {
/*     */           this.searchMode = !this.searchBox.getText().isEmpty();
/*     */           this.filterList.setPredicate(());
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void checkChangeContextMenu(TableViewSkin<?> skin, TableColumn<?, ?> column, FilterPanel filterPanel) {
/* 223 */     NestedTableColumnHeader header = skin.getTableHeaderRow().getRootHeader();
/* 224 */     InvalidationListener listener = filterPanel.getOrCreateChangeListener(header, column);
/* 225 */     header.getColumnHeaders().addListener((InvalidationListener)new WeakInvalidationListener(listener));
/* 226 */     changeContextMenu(header, column);
/*     */   }
/*     */   
/*     */   private InvalidationListener getOrCreateChangeListener(NestedTableColumnHeader header, TableColumn<?, ?> column) {
/* 230 */     InvalidationListener listener = obs -> changeContextMenu(header, column);
/*     */ 
/*     */     
/* 233 */     this.columnHeadersChangeListeners.add(listener);
/*     */     
/* 235 */     return listener;
/*     */   }
/*     */   
/*     */   private static void changeContextMenu(NestedTableColumnHeader header, TableColumn<?, ?> column) {
/* 239 */     TableColumnHeader headerSkin = scan(column, (TableColumnHeader)header);
/* 240 */     if (headerSkin != null) {
/* 241 */       headerSkin.setOnContextMenuRequested(ev -> {
/*     */             ContextMenu cMenu = column.getContextMenu();
/*     */             if (cMenu != null) {
/*     */               cMenu.show((Node)headerSkin, Side.BOTTOM, 5.0D, 5.0D);
/*     */             }
/*     */             ev.consume();
/*     */           });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static TableColumnHeader scan(TableColumn<?, ?> search, TableColumnHeader header) {
/* 254 */     if (search.equals(header.getTableColumn())) {
/* 255 */       return header;
/*     */     }
/*     */     
/* 258 */     if (header instanceof NestedTableColumnHeader) {
/* 259 */       NestedTableColumnHeader parent = (NestedTableColumnHeader)header;
/* 260 */       for (int i = 0; i < parent.getColumnHeaders().size(); i++) {
/* 261 */         TableColumnHeader result = scan(search, (TableColumnHeader)parent
/* 262 */             .getColumnHeaders().get(i));
/* 263 */         if (result != null) {
/* 264 */           return result;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 269 */     return null;
/*     */   }
/*     */   
/*     */   public ColumnFilter<T, R> getColumnFilter() {
/* 273 */     return this.columnFilter;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\table\FilterPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */