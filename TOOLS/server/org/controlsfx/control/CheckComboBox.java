/*     */ package org.controlsfx.control;
/*     */ 
/*     */ import impl.org.controlsfx.skin.CheckComboBoxSkin;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javafx.beans.property.BooleanProperty;
/*     */ import javafx.beans.property.ObjectProperty;
/*     */ import javafx.beans.property.SimpleObjectProperty;
/*     */ import javafx.collections.FXCollections;
/*     */ import javafx.collections.ListChangeListener;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.scene.control.Skin;
/*     */ import javafx.util.StringConverter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CheckComboBox<T>
/*     */   extends ControlsFXControl
/*     */ {
/*     */   private final ObservableList<T> items;
/*     */   private final Map<T, BooleanProperty> itemBooleanMap;
/*     */   
/*     */   public CheckComboBox() {
/* 104 */     this(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CheckComboBox(ObservableList<T> items) {
/* 114 */     int initialSize = (items == null) ? 32 : items.size();
/*     */     
/* 116 */     this.itemBooleanMap = new HashMap<>(initialSize);
/* 117 */     this.items = (items == null) ? FXCollections.observableArrayList() : items;
/* 118 */     setCheckModel(new CheckComboBoxBitSetCheckModel<>(this.items, this.itemBooleanMap));
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
/*     */   protected Skin<?> createDefaultSkin() {
/* 131 */     return (Skin<?>)new CheckComboBoxSkin(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObservableList<T> getItems() {
/* 139 */     return this.items;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BooleanProperty getItemBooleanProperty(int index) {
/* 147 */     if (index < 0 || index >= this.items.size()) return null; 
/* 148 */     return getItemBooleanProperty((T)getItems().get(index));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BooleanProperty getItemBooleanProperty(T item) {
/* 156 */     return this.itemBooleanMap.get(item);
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
/* 168 */   private ObjectProperty<IndexedCheckModel<T>> checkModel = (ObjectProperty<IndexedCheckModel<T>>)new SimpleObjectProperty(this, "checkModel");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setCheckModel(IndexedCheckModel<T> value) {
/* 180 */     checkModelProperty().set(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final IndexedCheckModel<T> getCheckModel() {
/* 187 */     return (this.checkModel == null) ? null : (IndexedCheckModel<T>)this.checkModel.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ObjectProperty<IndexedCheckModel<T>> checkModelProperty() {
/* 197 */     return this.checkModel;
/*     */   }
/*     */ 
/*     */   
/* 201 */   private ObjectProperty<StringConverter<T>> converter = (ObjectProperty<StringConverter<T>>)new SimpleObjectProperty(this, "converter");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ObjectProperty<StringConverter<T>> converterProperty() {
/* 209 */     return this.converter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setConverter(StringConverter<T> value) {
/* 218 */     converterProperty().set(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final StringConverter<T> getConverter() {
/* 226 */     return (StringConverter<T>)converterProperty().get();
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
/*     */   private static class CheckComboBoxBitSetCheckModel<T>
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
/*     */     CheckComboBoxBitSetCheckModel(ObservableList<T> items, Map<T, BooleanProperty> itemBooleanMap) {
/* 265 */       super(itemBooleanMap);
/*     */       
/* 267 */       this.items = items;
/* 268 */       this.items.addListener(new ListChangeListener<T>() {
/*     */             public void onChanged(ListChangeListener.Change<? extends T> c) {
/* 270 */               CheckComboBox.CheckComboBoxBitSetCheckModel.this.updateMap();
/*     */             }
/*     */           });
/*     */       
/* 274 */       updateMap();
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
/* 286 */       return (T)this.items.get(index);
/*     */     }
/*     */     
/*     */     public int getItemCount() {
/* 290 */       return this.items.size();
/*     */     }
/*     */     
/*     */     public int getItemIndex(T item) {
/* 294 */       return this.items.indexOf(item);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\CheckComboBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */