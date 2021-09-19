/*     */ package org.controlsfx.property.editor;
/*     */ 
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.scene.Node;
/*     */ import org.controlsfx.control.PropertySheet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractPropertyEditor<T, C extends Node>
/*     */   implements PropertyEditor<T>
/*     */ {
/*     */   private final PropertySheet.Item property;
/*     */   private final C control;
/*     */   private boolean suspendUpdate;
/*     */   
/*     */   public AbstractPropertyEditor(PropertySheet.Item property, C control) {
/*  67 */     this(property, control, !property.isEditable());
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
/*     */   public AbstractPropertyEditor(PropertySheet.Item property, C control, boolean readonly) {
/*  80 */     this.control = control;
/*  81 */     this.property = property;
/*  82 */     if (!readonly) {
/*  83 */       getObservableValue().addListener((o, oldValue, newValue) -> {
/*     */             if (!this.suspendUpdate) {
/*     */               this.suspendUpdate = true;
/*     */               
/*     */               this.property.setValue(getValue());
/*     */               this.suspendUpdate = false;
/*     */             } 
/*     */           });
/*  91 */       if (property.getObservableValue().isPresent()) {
/*  92 */         ((ObservableValue)property.getObservableValue().get()).addListener((o, oldValue, newValue) -> {
/*     */               if (!this.suspendUpdate) {
/*     */                 this.suspendUpdate = true;
/*     */                 setValue((T)property.getValue());
/*     */                 this.suspendUpdate = false;
/*     */               } 
/*     */             });
/*     */       }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final PropertySheet.Item getProperty() {
/* 123 */     return this.property;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public C getEditor() {
/* 130 */     return this.control;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T getValue() {
/* 137 */     return (T)getObservableValue().getValue();
/*     */   }
/*     */   
/*     */   protected abstract ObservableValue<T> getObservableValue();
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\property\editor\AbstractPropertyEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */