/*     */ package org.controlsfx.control;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.beans.property.BooleanProperty;
/*     */ import javafx.beans.property.ObjectProperty;
/*     */ import javafx.beans.property.SimpleObjectProperty;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.collections.FXCollections;
/*     */ import javafx.collections.ListChangeListener;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.scene.control.ListCell;
/*     */ import javafx.scene.control.ListView;
/*     */ import javafx.scene.control.cell.CheckBoxListCell;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CheckListView<T>
/*     */   extends ListView<T>
/*     */ {
/*     */   private final Map<T, BooleanProperty> itemBooleanMap;
/*     */   private ObjectProperty<IndexedCheckModel<T>> checkModel;
/*     */   
/*     */   public CheckListView() {
/* 105 */     this(FXCollections.observableArrayList());
/*     */   }
/*     */   
/*     */   public BooleanProperty getItemBooleanProperty(int index) {
/*     */     if (index < 0 || index >= getItems().size())
/*     */       return null; 
/*     */     return getItemBooleanProperty((T)getItems().get(index));
/*     */   }
/*     */   
/*     */   public CheckListView(ObservableList<T> items) {
/* 115 */     super(items);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 164 */     this.checkModel = (ObjectProperty<IndexedCheckModel<T>>)new SimpleObjectProperty(this, "checkModel");
/*     */     this.itemBooleanMap = new HashMap<>();
/*     */     setCheckModel(new CheckListViewBitSetCheckModel<>(getItems(), this.itemBooleanMap));
/*     */     itemsProperty().addListener(ov -> setCheckModel(new CheckListViewBitSetCheckModel<>(getItems(), this.itemBooleanMap)));
/*     */     setCellFactory(listView -> new CheckBoxListCell(new Callback<T, ObservableValue<Boolean>>() {
/*     */             public ObservableValue<Boolean> call(Object item) {
/*     */               return (ObservableValue<Boolean>)CheckListView.this.getItemBooleanProperty(item);
/*     */             }
/*     */           }));
/*     */   }
/*     */   
/*     */   public final void setCheckModel(IndexedCheckModel<T> value) {
/* 176 */     checkModelProperty().set(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final IndexedCheckModel<T> getCheckModel() {
/* 183 */     return (this.checkModel == null) ? null : (IndexedCheckModel<T>)this.checkModel.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public BooleanProperty getItemBooleanProperty(T item) {
/*     */     return this.itemBooleanMap.get(item);
/*     */   }
/*     */ 
/*     */   
/*     */   public final ObjectProperty<IndexedCheckModel<T>> checkModelProperty() {
/* 193 */     return this.checkModel;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class CheckListViewBitSetCheckModel<T>
/*     */     extends CheckBitSetModelBase<T>
/*     */   {
/*     */     private final ObservableList<T> items;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     CheckListViewBitSetCheckModel(ObservableList<T> items, Map<T, BooleanProperty> itemBooleanMap) {
/* 232 */       super(itemBooleanMap);
/*     */       
/* 234 */       this.items = items;
/* 235 */       this.items.addListener(new ListChangeListener<T>() {
/*     */             public void onChanged(ListChangeListener.Change<? extends T> c) {
/* 237 */               CheckListView.CheckListViewBitSetCheckModel.this.updateMap();
/*     */             }
/*     */           });
/*     */       
/* 241 */       updateMap();
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
/*     */     public T getItem(int index) {
/* 253 */       return (T)this.items.get(index);
/*     */     }
/*     */     
/*     */     public int getItemCount() {
/* 257 */       return this.items.size();
/*     */     }
/*     */     
/*     */     public int getItemIndex(T item) {
/* 261 */       return this.items.indexOf(item);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\CheckListView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */