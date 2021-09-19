/*     */ package impl.org.controlsfx.skin;
/*     */ 
/*     */ import com.sun.javafx.scene.control.skin.TableRowSkin;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.TableRow;
/*     */ import org.controlsfx.control.table.TableRowExpanderColumn;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExpandableTableRowSkin<S>
/*     */   extends TableRowSkin<S>
/*     */ {
/*     */   private final TableRow<S> tableRow;
/*     */   private TableRowExpanderColumn<S> expander;
/*  45 */   private Double tableRowPrefHeight = Double.valueOf(-1.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExpandableTableRowSkin(TableRow<S> tableRow, TableRowExpanderColumn<S> expander) {
/*  55 */     super(tableRow);
/*  56 */     this.tableRow = tableRow;
/*  57 */     this.expander = expander;
/*  58 */     tableRow.itemProperty().addListener((observable, oldValue, newValue) -> {
/*     */           if (oldValue != null) {
/*     */             Node expandedNode = this.expander.getExpandedNode(oldValue);
/*     */             if (expandedNode != null) {
/*     */               getChildren().remove(expandedNode);
/*     */             }
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Node getContent() {
/*  74 */     Node node = this.expander.getOrCreateExpandedNode(this.tableRow);
/*  75 */     if (!getChildren().contains(node)) getChildren().add(node); 
/*  76 */     return node;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Boolean isExpanded() {
/*  86 */     return Boolean.valueOf((((TableRow)getSkinnable()).getItem() != null && ((Boolean)this.expander.getCellData(((TableRow)getSkinnable()).getIndex())).booleanValue()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
/*  97 */     this.tableRowPrefHeight = Double.valueOf(super.computePrefHeight(width, topInset, rightInset, bottomInset, leftInset));
/*  98 */     return isExpanded().booleanValue() ? (this.tableRowPrefHeight.doubleValue() + getContent().prefHeight(width)) : this.tableRowPrefHeight.doubleValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void layoutChildren(double x, double y, double w, double h) {
/* 106 */     super.layoutChildren(x, y, w, h);
/* 107 */     if (isExpanded().booleanValue()) getContent().resizeRelocate(0.0D, this.tableRowPrefHeight.doubleValue(), w, h - this.tableRowPrefHeight.doubleValue()); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\skin\ExpandableTableRowSkin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */