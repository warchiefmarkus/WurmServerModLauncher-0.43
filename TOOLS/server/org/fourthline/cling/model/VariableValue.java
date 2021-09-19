/*     */ package org.fourthline.cling.model;
/*     */ 
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.model.types.Datatype;
/*     */ import org.fourthline.cling.model.types.InvalidValueException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VariableValue
/*     */ {
/*  30 */   private static final Logger log = Logger.getLogger(VariableValue.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Datatype datatype;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Object value;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VariableValue(Datatype datatype, Object value) throws InvalidValueException {
/*  54 */     this.datatype = datatype;
/*  55 */     this.value = (value instanceof String) ? datatype.valueOf((String)value) : value;
/*     */     
/*  57 */     if (ModelUtil.ANDROID_RUNTIME) {
/*     */       return;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  75 */     if (!getDatatype().isValid(getValue())) {
/*  76 */       throw new InvalidValueException("Invalid value for " + getDatatype() + ": " + getValue());
/*     */     }
/*  78 */     logInvalidXML(toString());
/*     */   }
/*     */   
/*     */   public Datatype getDatatype() {
/*  82 */     return this.datatype;
/*     */   }
/*     */   
/*     */   public Object getValue() {
/*  86 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void logInvalidXML(String s) {
/*  93 */     int i = 0;
/*  94 */     while (i < s.length()) {
/*  95 */       int cp = s.codePointAt(i);
/*  96 */       if (cp != 9 && cp != 10 && cp != 13 && (cp < 32 || cp > 55295) && (cp < 57344 || cp > 65533) && (cp < 65536 || cp > 1114111))
/*     */       {
/*     */ 
/*     */         
/* 100 */         log.warning("Found invalid XML char code: " + cp);
/*     */       }
/* 102 */       i += Character.charCount(cp);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 108 */     return getDatatype().getString(getValue());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\VariableValue.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */