/*     */ package impl.org.controlsfx.skin;
/*     */ 
/*     */ import com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList;
/*     */ import com.sun.javafx.scene.control.behavior.BehaviorBase;
/*     */ import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
/*     */ import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
/*     */ import java.util.Collections;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.collections.ListChangeListener;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.scene.control.ComboBox;
/*     */ import javafx.scene.control.Control;
/*     */ import javafx.scene.control.ListCell;
/*     */ import javafx.scene.control.ListView;
/*     */ import javafx.scene.control.Skin;
/*     */ import javafx.scene.control.cell.CheckBoxListCell;
/*     */ import javafx.util.Callback;
/*     */ import org.controlsfx.control.CheckComboBox;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CheckComboBoxSkin<T>
/*     */   extends BehaviorSkinBase<CheckComboBox<T>, BehaviorBase<CheckComboBox<T>>>
/*     */ {
/*     */   private final ComboBox<T> comboBox;
/*     */   private final ListCell<T> buttonCell;
/*     */   private final CheckComboBox<T> control;
/*     */   private final ObservableList<T> items;
/*     */   private final ReadOnlyUnbackedObservableList<Integer> selectedIndices;
/*     */   private final ReadOnlyUnbackedObservableList<T> selectedItems;
/*     */   
/*     */   public CheckComboBoxSkin(final CheckComboBox<T> control) {
/*  82 */     super((Control)control, new BehaviorBase((Control)control, Collections.emptyList()));
/*     */     
/*  84 */     this.control = control;
/*  85 */     this.items = control.getItems();
/*     */     
/*  87 */     this.selectedIndices = (ReadOnlyUnbackedObservableList<Integer>)control.getCheckModel().getCheckedIndices();
/*  88 */     this.selectedItems = (ReadOnlyUnbackedObservableList<T>)control.getCheckModel().getCheckedItems();
/*     */     
/*  90 */     this.comboBox = new ComboBox<T>(this.items) {
/*     */         protected Skin<?> createDefaultSkin() {
/*  92 */           return (Skin<?>)new ComboBoxListViewSkin<T>(this)
/*     */             {
/*     */               protected boolean isHideOnClickEnabled() {
/*  95 */                 return false;
/*     */               }
/*     */             };
/*     */         }
/*     */       };
/* 100 */     this.comboBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
/*     */ 
/*     */     
/* 103 */     this.comboBox.setCellFactory(new Callback<ListView<T>, ListCell<T>>() {
/*     */           public ListCell<T> call(ListView<T> listView) {
/* 105 */             CheckBoxListCell<T> result = new CheckBoxListCell(item -> control.getItemBooleanProperty(item));
/* 106 */             result.converterProperty().bind((ObservableValue)control.converterProperty());
/* 107 */             return (ListCell<T>)result;
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */     
/* 113 */     this.buttonCell = new ListCell<T>()
/*     */       {
/*     */         
/*     */         protected void updateItem(T item, boolean empty)
/*     */         {
/* 118 */           setText(CheckComboBoxSkin.this.buildString());
/*     */         }
/*     */       };
/* 121 */     this.comboBox.setButtonCell(this.buttonCell);
/* 122 */     this.comboBox.setValue(buildString());
/*     */ 
/*     */ 
/*     */     
/* 126 */     this.selectedIndices.addListener(c -> this.buttonCell.updateIndex(0));
/*     */     
/* 128 */     getChildren().add(this.comboBox);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 139 */     return this.comboBox.minWidth(height);
/*     */   }
/*     */   
/*     */   protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 143 */     return this.comboBox.minHeight(width);
/*     */   }
/*     */   
/*     */   protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 147 */     return this.comboBox.prefWidth(height);
/*     */   }
/*     */   
/*     */   protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 151 */     return this.comboBox.prefHeight(width);
/*     */   }
/*     */   
/*     */   protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 155 */     return ((CheckComboBox)getSkinnable()).prefWidth(height);
/*     */   }
/*     */   
/*     */   protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 159 */     return ((CheckComboBox)getSkinnable()).prefHeight(width);
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
/*     */   protected String buildString() {
/* 171 */     StringBuilder sb = new StringBuilder();
/* 172 */     for (int i = 0, max = this.selectedItems.size(); i < max; i++) {
/* 173 */       T item = (T)this.selectedItems.get(i);
/* 174 */       if (this.control.getConverter() == null) {
/* 175 */         sb.append(item);
/*     */       } else {
/* 177 */         sb.append(this.control.getConverter().toString(item));
/*     */       } 
/* 179 */       if (i < max - 1) {
/* 180 */         sb.append(", ");
/*     */       }
/*     */     } 
/* 183 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\skin\CheckComboBoxSkin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */