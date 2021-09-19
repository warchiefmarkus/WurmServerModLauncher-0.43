/*     */ package org.controlsfx.control.spreadsheet;
/*     */ 
/*     */ import java.util.BitSet;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javafx.beans.value.ChangeListener;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.collections.FXCollections;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.event.ActionEvent;
/*     */ import javafx.event.Event;
/*     */ import javafx.event.EventHandler;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.CheckBox;
/*     */ import javafx.scene.control.CustomMenuItem;
/*     */ import javafx.scene.control.ListCell;
/*     */ import javafx.scene.control.ListView;
/*     */ import javafx.scene.control.MenuButton;
/*     */ import javafx.scene.control.MenuItem;
/*     */ import javafx.util.Callback;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FilterBase
/*     */   implements Filter
/*     */ {
/*     */   private final SpreadsheetView spv;
/*     */   private final int column;
/*     */   private MenuButton menuButton;
/*     */   private BitSet hiddenRows;
/*  57 */   private Set<String> stringSet = new HashSet<>();
/*  58 */   private Set<String> copySet = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Comparator ascendingComp;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Comparator descendingComp;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MenuButton getMenuButton() {
/*  76 */     if (this.menuButton == null) {
/*  77 */       this.menuButton = new MenuButton();
/*  78 */       this.menuButton.getStyleClass().add("filter-menu-button");
/*     */       
/*  80 */       this.menuButton.showingProperty().addListener(new ChangeListener<Boolean>()
/*     */           {
/*     */             public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
/*  83 */               if (newValue.booleanValue()) {
/*  84 */                 FilterBase.this.addMenuItems();
/*  85 */                 FilterBase.this.hiddenRows = new BitSet(FilterBase.this.spv.getHiddenRows().size());
/*  86 */                 FilterBase.this.hiddenRows.or(FilterBase.this.spv.getHiddenRows());
/*     */               } else {
/*  88 */                 for (int i = FilterBase.this.spv.getFilteredRow() + 1; i < FilterBase.this.spv.getGrid().getRowCount(); i++) {
/*  89 */                   FilterBase.this.hiddenRows.set(i, !FilterBase.this.copySet.contains(((SpreadsheetCell)((ObservableList)FilterBase.this.spv.getGrid().getRows().get(i)).get(FilterBase.this.column)).getText()));
/*     */                 }
/*  91 */                 FilterBase.this.spv.setHiddenRows(FilterBase.this.hiddenRows);
/*     */               } 
/*     */             }
/*     */           });
/*     */     } 
/*  96 */     return this.menuButton;
/*     */   }
/*     */   
/*     */   private void addMenuItems() {
/* 100 */     if (this.menuButton.getItems().isEmpty()) {
/* 101 */       final MenuItem sortItem = new MenuItem("Sort ascending");
/* 102 */       sortItem.setOnAction(new EventHandler<ActionEvent>()
/*     */           {
/*     */             public void handle(ActionEvent event) {
/* 105 */               if (FilterBase.this.spv.getComparator() == FilterBase.this.ascendingComp) {
/* 106 */                 FilterBase.this.spv.setComparator(FilterBase.this.descendingComp);
/* 107 */                 sortItem.setText("Remove sort");
/* 108 */               } else if (FilterBase.this.spv.getComparator() == FilterBase.this.descendingComp) {
/* 109 */                 FilterBase.this.spv.setComparator(null);
/* 110 */                 sortItem.setText("Sort ascending");
/*     */               } else {
/* 112 */                 FilterBase.this.spv.setComparator(FilterBase.this.ascendingComp);
/* 113 */                 sortItem.setText("Sort descending");
/*     */               } 
/*     */             }
/*     */           });
/*     */       
/* 118 */       ListView<String> listView = new ListView();
/* 119 */       listView.setCellFactory(new Callback<ListView<String>, ListCell<String>>()
/*     */           {
/*     */             public ListCell<String> call(ListView<String> param) {
/* 122 */               return new ListCell<String>()
/*     */                 {
/*     */                   public void updateItem(final String item, boolean empty) {
/* 125 */                     super.updateItem(item, empty);
/* 126 */                     setText(item);
/* 127 */                     if (item != null) {
/* 128 */                       CheckBox checkBox = new CheckBox();
/* 129 */                       checkBox.setSelected(FilterBase.this.copySet.contains(item));
/* 130 */                       checkBox.selectedProperty().addListener(new ChangeListener<Boolean>()
/*     */                           {
/*     */                             public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
/* 133 */                               if (newValue.booleanValue()) {
/* 134 */                                 FilterBase.this.copySet.add(item);
/*     */                               } else {
/* 136 */                                 FilterBase.this.copySet.remove(item);
/*     */                               } 
/*     */                             }
/*     */                           });
/* 140 */                       setGraphic((Node)checkBox);
/*     */                     } 
/*     */                   }
/*     */                 };
/*     */             }
/*     */           });
/*     */       
/* 147 */       for (int j = this.spv.getFilteredRow() + 1; j < this.spv.getGrid().getRowCount(); j++) {
/* 148 */         this.stringSet.add(((SpreadsheetCell)((ObservableList)this.spv.getGrid().getRows().get(j)).get(this.column)).getText());
/*     */       }
/* 150 */       listView.setItems(FXCollections.observableArrayList(this.stringSet));
/*     */       
/* 152 */       CustomMenuItem customMenuItem = new CustomMenuItem((Node)listView);
/* 153 */       customMenuItem.setHideOnClick(false);
/* 154 */       this.menuButton.getItems().addAll((Object[])new MenuItem[] { sortItem, (MenuItem)customMenuItem });
/*     */     } 
/*     */     
/* 157 */     this.copySet.clear();
/* 158 */     for (int i = this.spv.getFilteredRow() + 1; i < this.spv.getGrid().getRowCount(); i++) {
/* 159 */       if (!this.spv.getHiddenRows().get(i))
/* 160 */         this.copySet.add(((SpreadsheetCell)((ObservableList)this.spv.getGrid().getRows().get(i)).get(this.column)).getText()); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public FilterBase(SpreadsheetView spv, int column) {
/* 165 */     this.ascendingComp = new Comparator<ObservableList<SpreadsheetCell>>()
/*     */       {
/*     */         public int compare(ObservableList<SpreadsheetCell> o1, ObservableList<SpreadsheetCell> o2) {
/* 168 */           SpreadsheetCell cell1 = (SpreadsheetCell)o1.get(FilterBase.this.column);
/* 169 */           SpreadsheetCell cell2 = (SpreadsheetCell)o2.get(FilterBase.this.column);
/* 170 */           if (cell1.getRow() <= FilterBase.this.spv.getFilteredRow())
/* 171 */             return Integer.compare(cell1.getRow(), cell2.getRow()); 
/* 172 */           if (cell2.getRow() <= FilterBase.this.spv.getFilteredRow())
/* 173 */             return Integer.compare(cell1.getRow(), cell2.getRow()); 
/* 174 */           if (cell1.getCellType() == SpreadsheetCellType.INTEGER && cell2.getCellType() == SpreadsheetCellType.INTEGER)
/* 175 */             return Integer.compare(((Integer)cell1.getItem()).intValue(), ((Integer)cell2.getItem()).intValue()); 
/* 176 */           if (cell1.getCellType() == SpreadsheetCellType.DOUBLE && cell2.getCellType() == SpreadsheetCellType.DOUBLE) {
/* 177 */             return Double.compare(((Double)cell1.getItem()).doubleValue(), ((Double)cell2.getItem()).doubleValue());
/*     */           }
/* 179 */           return cell1.getText().compareToIgnoreCase(cell2.getText());
/*     */         }
/*     */       };
/*     */ 
/*     */     
/* 184 */     this.descendingComp = new Comparator<ObservableList<SpreadsheetCell>>()
/*     */       {
/*     */         public int compare(ObservableList<SpreadsheetCell> o1, ObservableList<SpreadsheetCell> o2) {
/* 187 */           SpreadsheetCell cell1 = (SpreadsheetCell)o1.get(FilterBase.this.column);
/* 188 */           SpreadsheetCell cell2 = (SpreadsheetCell)o2.get(FilterBase.this.column);
/* 189 */           if (cell1.getRow() <= FilterBase.this.spv.getFilteredRow())
/* 190 */             return Integer.compare(cell1.getRow(), cell2.getRow()); 
/* 191 */           if (cell2.getRow() <= FilterBase.this.spv.getFilteredRow())
/* 192 */             return Integer.compare(cell1.getRow(), cell2.getRow()); 
/* 193 */           if (cell1.getCellType() == SpreadsheetCellType.INTEGER && cell2.getCellType() == SpreadsheetCellType.INTEGER)
/* 194 */             return Integer.compare(((Integer)cell2.getItem()).intValue(), ((Integer)cell1.getItem()).intValue()); 
/* 195 */           if (cell1.getCellType() == SpreadsheetCellType.DOUBLE && cell2.getCellType() == SpreadsheetCellType.DOUBLE) {
/* 196 */             return Double.compare(((Double)cell2.getItem()).doubleValue(), ((Double)cell1.getItem()).doubleValue());
/*     */           }
/* 198 */           return cell2.getText().compareToIgnoreCase(cell1.getText());
/*     */         }
/*     */       };
/*     */     this.spv = spv;
/*     */     this.column = column;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\spreadsheet\FilterBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */