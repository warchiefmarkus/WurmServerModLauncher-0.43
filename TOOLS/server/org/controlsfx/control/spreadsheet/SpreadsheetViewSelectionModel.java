/*     */ package org.controlsfx.control.spreadsheet;
/*     */ 
/*     */ import impl.org.controlsfx.spreadsheet.GridViewBehavior;
/*     */ import impl.org.controlsfx.spreadsheet.TableViewSpanSelectionModel;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.scene.control.SelectionMode;
/*     */ import javafx.scene.control.TableColumnBase;
/*     */ import javafx.scene.control.TablePosition;
/*     */ import javafx.util.Pair;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SpreadsheetViewSelectionModel
/*     */ {
/*     */   private final TableViewSpanSelectionModel selectionModel;
/*     */   private final SpreadsheetView spv;
/*     */   
/*     */   SpreadsheetViewSelectionModel(SpreadsheetView spv, TableViewSpanSelectionModel selectionModel) {
/*  53 */     this.spv = spv;
/*  54 */     this.selectionModel = selectionModel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void clearAndSelect(int row, SpreadsheetColumn column) {
/*  65 */     this.selectionModel.clearAndSelect(this.spv.getFilteredRow(row), column.column);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final void clearAndSelectView(int row, SpreadsheetColumn column) {
/*  75 */     this.selectionModel.clearAndSelect(row, column.column);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void select(int row, SpreadsheetColumn column) {
/*  85 */     this.selectionModel.select(this.spv.getFilteredRow(row), column.column);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void clearSelection() {
/*  92 */     this.selectionModel.clearSelection();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ObservableList<TablePosition> getSelectedCells() {
/* 102 */     return this.selectionModel.getSelectedCells();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void selectAll() {
/* 109 */     this.selectionModel.selectAll();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TablePosition getFocusedCell() {
/* 118 */     return this.selectionModel.getTableView().getFocusModel().getFocusedCell();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void focus(int row, SpreadsheetColumn column) {
/* 128 */     this.selectionModel.getTableView().getFocusModel().focus(row, column.column);
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
/*     */   public final void setSelectionMode(SelectionMode value) {
/* 140 */     this.selectionModel.setSelectionMode(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SelectionMode getSelectionMode() {
/* 149 */     return this.selectionModel.getSelectionMode();
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
/*     */   public void selectCells(List<Pair<Integer, Integer>> selectedCells) {
/* 163 */     this.selectionModel.verifySelectedCells(selectedCells);
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
/*     */   public void selectCells(Pair<Integer, Integer>... selectedCells) {
/* 177 */     this.selectionModel.verifySelectedCells(Arrays.asList(selectedCells));
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
/*     */   public void selectRange(int minRow, SpreadsheetColumn minColumn, int maxRow, SpreadsheetColumn maxColumn) {
/* 190 */     this.selectionModel.selectRange(this.spv.getFilteredRow(minRow), (TableColumnBase)minColumn.column, this.spv.getFilteredRow(maxRow), (TableColumnBase)maxColumn.column);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearAndSelectLeftCell() {
/* 199 */     TablePosition<ObservableList<SpreadsheetCell>, ?> position = getFocusedCell();
/* 200 */     int row = position.getRow();
/* 201 */     int column = position.getColumn();
/* 202 */     column--;
/* 203 */     if (column < 0) {
/* 204 */       if (row == 0) {
/* 205 */         column++;
/*     */       } else {
/* 207 */         column = this.selectionModel.getTableView().getVisibleLeafColumns().size() - 1;
/* 208 */         row--;
/* 209 */         this.selectionModel.direction = new Pair(Integer.valueOf(-1), Integer.valueOf(-1));
/*     */       } 
/* 211 */       clearAndSelectView(row, (SpreadsheetColumn)this.spv.getColumns().get(this.spv.getModelColumn(column)));
/*     */     } else {
/* 213 */       ((GridViewBehavior)this.spv.getCellsViewSkin().getBehavior()).selectCell(0, -1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearAndSelectRightCell() {
/* 223 */     TablePosition<ObservableList<SpreadsheetCell>, ?> position = getFocusedCell();
/* 224 */     int row = position.getRow();
/* 225 */     int column = position.getColumn();
/* 226 */     column++;
/* 227 */     if (column >= this.selectionModel.getTableView().getVisibleLeafColumns().size()) {
/* 228 */       if (row == this.spv.getGrid().getRowCount() - 1) {
/* 229 */         column--;
/*     */       } else {
/* 231 */         this.selectionModel.direction = new Pair(Integer.valueOf(1), Integer.valueOf(1));
/* 232 */         column = 0;
/* 233 */         row++;
/*     */       } 
/* 235 */       clearAndSelectView(row, (SpreadsheetColumn)this.spv.getColumns().get(this.spv.getModelColumn(column)));
/*     */     } else {
/* 237 */       ((GridViewBehavior)this.spv.getCellsViewSkin().getBehavior()).selectCell(0, 1);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\spreadsheet\SpreadsheetViewSelectionModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */