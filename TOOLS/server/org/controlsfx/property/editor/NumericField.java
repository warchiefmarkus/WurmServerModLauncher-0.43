/*     */ package org.controlsfx.property.editor;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import javafx.beans.binding.NumberExpression;
/*     */ import javafx.beans.property.SimpleDoubleProperty;
/*     */ import javafx.beans.property.SimpleLongProperty;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.scene.control.IndexRange;
/*     */ import javafx.scene.control.TextField;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class NumericField
/*     */   extends TextField
/*     */ {
/*     */   private final NumericValidator<? extends Number> value;
/*     */   
/*     */   public NumericField(Class<? extends Number> cls) {
/*  48 */     if (cls == byte.class || cls == Byte.class || cls == short.class || cls == Short.class || cls == int.class || cls == Integer.class || cls == long.class || cls == Long.class || cls == BigInteger.class) {
/*     */ 
/*     */       
/*  51 */       this.value = new LongValidator(this);
/*     */     } else {
/*  53 */       this.value = new DoubleValidator(this);
/*     */     } 
/*     */     
/*  56 */     focusedProperty().addListener((observable, oldValue, newValue) -> {
/*     */           if (!newValue.booleanValue()) {
/*     */             this.value.setValue(this.value.toNumber(getText()));
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public final ObservableValue<Number> valueProperty() {
/*  64 */     return (ObservableValue)this.value;
/*     */   }
/*     */   
/*     */   public void replaceText(int start, int end, String text) {
/*  68 */     if (replaceValid(start, end, text).booleanValue()) {
/*  69 */       super.replaceText(start, end, text);
/*     */     }
/*     */   }
/*     */   
/*     */   public void replaceSelection(String text) {
/*  74 */     IndexRange range = getSelection();
/*  75 */     if (replaceValid(range.getStart(), range.getEnd(), text).booleanValue()) {
/*  76 */       super.replaceSelection(text);
/*     */     }
/*     */   }
/*     */   
/*     */   private Boolean replaceValid(int start, int end, String fragment) {
/*     */     try {
/*  82 */       String newText = getText().substring(0, start) + fragment + getText().substring(end);
/*  83 */       if (newText.isEmpty()) return Boolean.valueOf(true); 
/*  84 */       this.value.toNumber(newText);
/*  85 */       return Boolean.valueOf(true);
/*  86 */     } catch (Throwable ex) {
/*  87 */       return Boolean.valueOf(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static interface NumericValidator<T extends Number>
/*     */     extends NumberExpression {
/*     */     void setValue(Number param1Number);
/*     */     
/*     */     T toNumber(String param1String);
/*     */   }
/*     */   
/*     */   static class DoubleValidator
/*     */     extends SimpleDoubleProperty implements NumericValidator<Double> {
/*     */     private NumericField field;
/*     */     
/*     */     public DoubleValidator(NumericField field) {
/* 103 */       super(field, "value", 0.0D);
/* 104 */       this.field = field;
/*     */     }
/*     */     
/*     */     protected void invalidated() {
/* 108 */       this.field.setText(Double.toString(get()));
/*     */     }
/*     */ 
/*     */     
/*     */     public Double toNumber(String s) {
/* 113 */       if (s == null || s.trim().isEmpty()) return Double.valueOf(0.0D); 
/* 114 */       String d = s.trim();
/* 115 */       if (d.endsWith("f") || d.endsWith("d") || d.endsWith("F") || d.endsWith("D")) {
/* 116 */         throw new NumberFormatException("There should be no alpha symbols");
/*     */       }
/* 118 */       return new Double(d);
/*     */     }
/*     */   }
/*     */   
/*     */   static class LongValidator
/*     */     extends SimpleLongProperty
/*     */     implements NumericValidator<Long>
/*     */   {
/*     */     private NumericField field;
/*     */     
/*     */     public LongValidator(NumericField field) {
/* 129 */       super(field, "value", 0L);
/* 130 */       this.field = field;
/*     */     }
/*     */     
/*     */     protected void invalidated() {
/* 134 */       this.field.setText(Long.toString(get()));
/*     */     }
/*     */ 
/*     */     
/*     */     public Long toNumber(String s) {
/* 139 */       if (s == null || s.trim().isEmpty()) return Long.valueOf(0L); 
/* 140 */       String d = s.trim();
/* 141 */       return new Long(d);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\property\editor\NumericField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */