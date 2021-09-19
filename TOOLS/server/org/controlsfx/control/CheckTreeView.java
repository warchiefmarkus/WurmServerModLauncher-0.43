/*     */ package org.controlsfx.control;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.beans.property.BooleanProperty;
/*     */ import javafx.beans.property.ObjectProperty;
/*     */ import javafx.beans.property.SimpleObjectProperty;
/*     */ import javafx.collections.FXCollections;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.scene.control.CheckBoxTreeItem;
/*     */ import javafx.scene.control.TreeItem;
/*     */ import javafx.scene.control.TreeView;
/*     */ import javafx.scene.control.cell.CheckBoxTreeCell;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CheckTreeView<T>
/*     */   extends TreeView<T>
/*     */ {
/*     */   private ObjectProperty<CheckModel<TreeItem<T>>> checkModel;
/*     */   
/*     */   public CheckTreeView() {
/* 106 */     this((CheckBoxTreeItem<T>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CheckTreeView(CheckBoxTreeItem<T> root) {
/* 116 */     super((TreeItem)root);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 155 */     this.checkModel = (ObjectProperty<CheckModel<TreeItem<T>>>)new SimpleObjectProperty(this, "checkModel");
/*     */     rootProperty().addListener(o -> updateCheckModel());
/*     */     updateCheckModel();
/*     */     setCellFactory(CheckBoxTreeCell.forTreeView());
/*     */   }
/*     */   
/*     */   protected void updateCheckModel() {
/*     */     if (getRoot() != null)
/*     */       setCheckModel(new CheckTreeViewCheckModel<>(this)); 
/*     */   }
/*     */   
/*     */   public final void setCheckModel(CheckModel<TreeItem<T>> value) {
/* 167 */     checkModelProperty().set(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final CheckModel<TreeItem<T>> getCheckModel() {
/* 174 */     return (this.checkModel == null) ? null : (CheckModel<TreeItem<T>>)this.checkModel.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public BooleanProperty getItemBooleanProperty(int index) {
/*     */     CheckBoxTreeItem<T> treeItem = (CheckBoxTreeItem<T>)getTreeItem(index);
/*     */     return treeItem.selectedProperty();
/*     */   }
/*     */   
/*     */   public final ObjectProperty<CheckModel<TreeItem<T>>> checkModelProperty() {
/* 184 */     return this.checkModel;
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
/*     */   private static class CheckTreeViewCheckModel<T>
/*     */     implements CheckModel<TreeItem<T>>
/*     */   {
/*     */     private final CheckTreeView<T> treeView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final TreeItem<T> root;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 215 */     private ObservableList<TreeItem<T>> checkedItems = FXCollections.observableArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     CheckTreeViewCheckModel(CheckTreeView<T> treeView) {
/* 226 */       this.treeView = treeView;
/* 227 */       this.root = treeView.getRoot();
/* 228 */       this.root.addEventHandler(CheckBoxTreeItem.checkBoxSelectionChangedEvent(), e -> {
/*     */             CheckBoxTreeItem<T> treeItem = e.getTreeItem();
/*     */ 
/*     */             
/*     */             if (treeItem.isSelected()) {
/*     */               check((TreeItem<T>)treeItem);
/*     */             } else {
/*     */               clearCheck((TreeItem<T>)treeItem);
/*     */             } 
/*     */           });
/*     */ 
/*     */       
/* 240 */       clearChecks();
/* 241 */       for (int i = 0; i < treeView.getExpandedItemCount(); i++) {
/* 242 */         CheckBoxTreeItem<T> treeItem = (CheckBoxTreeItem<T>)treeView.getTreeItem(i);
/* 243 */         if (treeItem.isSelected() && !treeItem.isIndeterminate()) {
/* 244 */           check((TreeItem<T>)treeItem);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getItemCount() {
/* 258 */       return this.treeView.getExpandedItemCount();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public ObservableList<TreeItem<T>> getCheckedItems() {
/* 264 */       return this.checkedItems;
/*     */     }
/*     */     
/*     */     public void checkAll() {
/* 268 */       iterateOverTree(this::check);
/*     */     }
/*     */     
/*     */     public void clearCheck(TreeItem<T> item) {
/* 272 */       if (item instanceof CheckBoxTreeItem) {
/* 273 */         ((CheckBoxTreeItem)item).setSelected(false);
/*     */       }
/* 275 */       this.checkedItems.remove(item);
/*     */     }
/*     */     
/*     */     public void clearChecks() {
/* 279 */       List<TreeItem<T>> items = new ArrayList<>((Collection<? extends TreeItem<T>>)this.checkedItems);
/* 280 */       for (TreeItem<T> item : items) {
/* 281 */         clearCheck(item);
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 286 */       return this.checkedItems.isEmpty();
/*     */     }
/*     */     
/*     */     public boolean isChecked(TreeItem<T> item) {
/* 290 */       return this.checkedItems.contains(item);
/*     */     }
/*     */     
/*     */     public void check(TreeItem<T> item) {
/* 294 */       if (item instanceof CheckBoxTreeItem) {
/* 295 */         ((CheckBoxTreeItem)item).setSelected(true);
/*     */       }
/* 297 */       if (!this.checkedItems.contains(item)) {
/* 298 */         this.checkedItems.add(item);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void iterateOverTree(Consumer<TreeItem<T>> consumer) {
/* 311 */       processNode(consumer, this.root);
/*     */     }
/*     */     
/*     */     private void processNode(Consumer<TreeItem<T>> consumer, TreeItem<T> node) {
/* 315 */       if (node == null)
/* 316 */         return;  consumer.accept(node);
/* 317 */       processChildren(consumer, (List<TreeItem<T>>)node.getChildren());
/*     */     }
/*     */     
/*     */     private void processChildren(Consumer<TreeItem<T>> consumer, List<TreeItem<T>> children) {
/* 321 */       if (children == null)
/* 322 */         return;  for (TreeItem<T> child : children)
/* 323 */         processNode(consumer, child); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\CheckTreeView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */