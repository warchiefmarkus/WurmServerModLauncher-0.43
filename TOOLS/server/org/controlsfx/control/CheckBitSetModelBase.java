/*     */ package org.controlsfx.control;
/*     */ 
/*     */ import com.sun.javafx.collections.MappingChange;
/*     */ import com.sun.javafx.collections.NonIterableChange;
/*     */ import com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList;
/*     */ import java.util.BitSet;
/*     */ import java.util.Map;
/*     */ import javafx.beans.InvalidationListener;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.beans.property.BooleanProperty;
/*     */ import javafx.beans.property.SimpleBooleanProperty;
/*     */ import javafx.collections.ListChangeListener;
/*     */ import javafx.collections.ObservableList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class CheckBitSetModelBase<T>
/*     */   implements IndexedCheckModel<T>
/*     */ {
/*     */   private final Map<T, BooleanProperty> itemBooleanMap;
/*     */   private final BitSet checkedIndices;
/*     */   private final ReadOnlyUnbackedObservableList<Integer> checkedIndicesList;
/*     */   private final ReadOnlyUnbackedObservableList<T> checkedItemsList;
/*     */   
/*     */   CheckBitSetModelBase(Map<T, BooleanProperty> itemBooleanMap) {
/*  67 */     this.itemBooleanMap = itemBooleanMap;
/*     */     
/*  69 */     this.checkedIndices = new BitSet();
/*     */     
/*  71 */     this.checkedIndicesList = new ReadOnlyUnbackedObservableList<Integer>() {
/*     */         public Integer get(int index) {
/*  73 */           if (index < 0 || index >= CheckBitSetModelBase.this.getItemCount()) return Integer.valueOf(-1);
/*     */           
/*  75 */           int pos = 0, val = CheckBitSetModelBase.this.checkedIndices.nextSetBit(0);
/*  76 */           for (; val >= 0 || pos == index; 
/*  77 */             pos++, val = CheckBitSetModelBase.this.checkedIndices.nextSetBit(val + 1)) {
/*  78 */             if (pos == index) return Integer.valueOf(val);
/*     */           
/*     */           } 
/*  81 */           return Integer.valueOf(-1);
/*     */         }
/*     */         
/*     */         public int size() {
/*  85 */           return CheckBitSetModelBase.this.checkedIndices.cardinality();
/*     */         }
/*     */         
/*     */         public boolean contains(Object o) {
/*  89 */           if (o instanceof Number) {
/*  90 */             Number n = (Number)o;
/*  91 */             int index = n.intValue();
/*     */             
/*  93 */             return (index >= 0 && index < CheckBitSetModelBase.this.checkedIndices.length() && CheckBitSetModelBase.this
/*  94 */               .checkedIndices.get(index));
/*     */           } 
/*     */           
/*  97 */           return false;
/*     */         }
/*     */       };
/*     */     
/* 101 */     this.checkedItemsList = new ReadOnlyUnbackedObservableList<T>() {
/*     */         public T get(int i) {
/* 103 */           int pos = ((Integer)CheckBitSetModelBase.this.checkedIndicesList.get(i)).intValue();
/* 104 */           if (pos < 0 || pos >= CheckBitSetModelBase.this.getItemCount()) return null; 
/* 105 */           return CheckBitSetModelBase.this.getItem(pos);
/*     */         }
/*     */         
/*     */         public int size() {
/* 109 */           return CheckBitSetModelBase.this.checkedIndices.cardinality();
/*     */         }
/*     */       };
/*     */     
/* 113 */     final MappingChange.Map<Integer, T> map = f -> getItem(f.intValue());
/*     */     
/* 115 */     this.checkedIndicesList.addListener(new ListChangeListener<Integer>()
/*     */         {
/*     */           public void onChanged(ListChangeListener.Change<? extends Integer> c)
/*     */           {
/* 119 */             boolean hasRealChangeOccurred = false;
/* 120 */             while (c.next() && !hasRealChangeOccurred) {
/* 121 */               hasRealChangeOccurred = (c.wasAdded() || c.wasRemoved());
/*     */             }
/*     */             
/* 124 */             if (hasRealChangeOccurred) {
/* 125 */               c.reset();
/* 126 */               CheckBitSetModelBase.this.checkedItemsList.callObservers((ListChangeListener.Change)new MappingChange(c, map, (ObservableList)CheckBitSetModelBase.this.checkedItemsList));
/*     */             } 
/* 128 */             c.reset();
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 135 */     getCheckedItems().addListener(new ListChangeListener<T>() {
/*     */           public void onChanged(ListChangeListener.Change<? extends T> c) {
/* 137 */             while (c.next()) {
/* 138 */               if (c.wasAdded()) {
/* 139 */                 for (T item : c.getAddedSubList()) {
/* 140 */                   BooleanProperty p = CheckBitSetModelBase.this.getItemBooleanProperty(item);
/* 141 */                   if (p != null) {
/* 142 */                     p.set(true);
/*     */                   }
/*     */                 } 
/*     */               }
/*     */               
/* 147 */               if (c.wasRemoved()) {
/* 148 */                 for (T item : c.getRemoved()) {
/* 149 */                   BooleanProperty p = CheckBitSetModelBase.this.getItemBooleanProperty(item);
/* 150 */                   if (p != null) {
/* 151 */                     p.set(false);
/*     */                   }
/*     */                 } 
/*     */               }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   BooleanProperty getItemBooleanProperty(T item) {
/* 178 */     return this.itemBooleanMap.get(item);
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
/*     */   public ObservableList<Integer> getCheckedIndices() {
/* 193 */     return (ObservableList<Integer>)this.checkedIndicesList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObservableList<T> getCheckedItems() {
/* 201 */     return (ObservableList<T>)this.checkedItemsList;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkAll() {
/* 207 */     for (int i = 0; i < getItemCount(); i++) {
/* 208 */       check(i);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkIndices(int... indices) {
/* 215 */     for (int i = 0; i < indices.length; i++) {
/* 216 */       check(indices[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearCheck(T item) {
/* 222 */     int index = getItemIndex(item);
/* 223 */     clearCheck(index);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearChecks() {
/* 229 */     for (int index = 0; index < this.checkedIndices.length(); index++) {
/* 230 */       clearCheck(index);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearCheck(int index) {
/* 237 */     if (index < 0 || index >= getItemCount())
/* 238 */       return;  this.checkedIndices.clear(index);
/*     */     
/* 240 */     int changeIndex = this.checkedIndicesList.indexOf(Integer.valueOf(index));
/* 241 */     this.checkedIndicesList.callObservers((ListChangeListener.Change)new NonIterableChange.SimpleRemovedChange(changeIndex, changeIndex, Integer.valueOf(index), (ObservableList)this.checkedIndicesList));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 247 */     return this.checkedIndices.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isChecked(T item) {
/* 252 */     int index = getItemIndex(item);
/* 253 */     return isChecked(index);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isChecked(int index) {
/* 259 */     return this.checkedIndices.get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void check(int index) {
/* 265 */     if (index < 0 || index >= getItemCount())
/* 266 */       return;  this.checkedIndices.set(index);
/* 267 */     int changeIndex = this.checkedIndicesList.indexOf(Integer.valueOf(index));
/* 268 */     this.checkedIndicesList.callObservers((ListChangeListener.Change)new NonIterableChange.SimpleAddChange(changeIndex, changeIndex + 1, (ObservableList)this.checkedIndicesList));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void check(T item) {
/* 274 */     int index = getItemIndex(item);
/* 275 */     check(index);
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
/*     */   protected void updateMap() {
/* 289 */     this.itemBooleanMap.clear();
/* 290 */     for (int i = 0; i < getItemCount(); i++) {
/* 291 */       final int index = i;
/* 292 */       T item = getItem(index);
/*     */       
/* 294 */       final SimpleBooleanProperty booleanProperty = new SimpleBooleanProperty(item, "selected", false);
/* 295 */       this.itemBooleanMap.put(item, simpleBooleanProperty);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 300 */       simpleBooleanProperty.addListener(new InvalidationListener() {
/*     */             public void invalidated(Observable o) {
/* 302 */               if (booleanProperty.get()) {
/* 303 */                 CheckBitSetModelBase.this.checkedIndices.set(index);
/* 304 */                 int changeIndex = CheckBitSetModelBase.this.checkedIndicesList.indexOf(Integer.valueOf(index));
/* 305 */                 CheckBitSetModelBase.this.checkedIndicesList.callObservers((ListChangeListener.Change)new NonIterableChange.SimpleAddChange(changeIndex, changeIndex + 1, (ObservableList)CheckBitSetModelBase.this.checkedIndicesList));
/*     */               } else {
/* 307 */                 int changeIndex = CheckBitSetModelBase.this.checkedIndicesList.indexOf(Integer.valueOf(index));
/* 308 */                 CheckBitSetModelBase.this.checkedIndices.clear(index);
/* 309 */                 CheckBitSetModelBase.this.checkedIndicesList.callObservers((ListChangeListener.Change)new NonIterableChange.SimpleRemovedChange(changeIndex, changeIndex, Integer.valueOf(index), (ObservableList)CheckBitSetModelBase.this.checkedIndicesList));
/*     */               } 
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */   
/*     */   public abstract T getItem(int paramInt);
/*     */   
/*     */   public abstract int getItemCount();
/*     */   
/*     */   public abstract int getItemIndex(T paramT);
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\CheckBitSetModelBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */