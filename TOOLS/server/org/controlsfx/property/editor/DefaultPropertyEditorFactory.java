/*     */ package org.controlsfx.property.editor;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.time.LocalDate;
/*     */ import java.util.Arrays;
/*     */ import java.util.Optional;
/*     */ import javafx.scene.paint.Color;
/*     */ import javafx.scene.paint.Paint;
/*     */ import javafx.scene.text.Font;
/*     */ import javafx.util.Callback;
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
/*     */ public class DefaultPropertyEditorFactory
/*     */   implements Callback<PropertySheet.Item, PropertyEditor<?>>
/*     */ {
/*     */   public PropertyEditor<?> call(PropertySheet.Item item) {
/*  57 */     Class<?> type = item.getType();
/*     */ 
/*     */ 
/*     */     
/*  61 */     if (item.getPropertyEditorClass().isPresent()) {
/*  62 */       Optional<PropertyEditor<?>> ed = Editors.createCustomEditor(item);
/*  63 */       if (ed.isPresent()) return ed.get();
/*     */     
/*     */     } 
/*  66 */     if (type == String.class) {
/*  67 */       return Editors.createTextEditor(item);
/*     */     }
/*     */     
/*  70 */     if (isNumber(type)) {
/*  71 */       return Editors.createNumericEditor(item);
/*     */     }
/*     */     
/*  74 */     if (type == boolean.class || type == Boolean.class) {
/*  75 */       return Editors.createCheckEditor(item);
/*     */     }
/*     */     
/*  78 */     if (type == LocalDate.class) {
/*  79 */       return Editors.createDateEditor(item);
/*     */     }
/*     */     
/*  82 */     if (type == Color.class || type == Paint.class) {
/*  83 */       return Editors.createColorEditor(item);
/*     */     }
/*     */     
/*  86 */     if (type != null && type.isEnum()) {
/*  87 */       return Editors.createChoiceEditor(item, Arrays.asList(type.getEnumConstants()));
/*     */     }
/*     */     
/*  90 */     if (type == Font.class) {
/*  91 */       return Editors.createFontEditor(item);
/*     */     }
/*     */     
/*  94 */     return null;
/*     */   }
/*     */   
/*  97 */   private static Class<?>[] numericTypes = new Class[] { byte.class, Byte.class, short.class, Short.class, int.class, Integer.class, long.class, Long.class, float.class, Float.class, double.class, Double.class, BigInteger.class, BigDecimal.class };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isNumber(Class<?> type) {
/* 109 */     if (type == null) return false; 
/* 110 */     for (Class<?> cls : numericTypes) {
/* 111 */       if (type == cls) return true; 
/*     */     } 
/* 113 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\property\editor\DefaultPropertyEditorFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */