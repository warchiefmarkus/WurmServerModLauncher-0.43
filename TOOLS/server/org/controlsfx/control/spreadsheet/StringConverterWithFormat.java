/*    */ package org.controlsfx.control.spreadsheet;
/*    */ 
/*    */ import javafx.util.StringConverter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class StringConverterWithFormat<T>
/*    */   extends StringConverter<T>
/*    */ {
/*    */   protected StringConverter<T> myConverter;
/*    */   
/*    */   public StringConverterWithFormat() {}
/*    */   
/*    */   public StringConverterWithFormat(StringConverter<T> specificStringConverter) {
/* 65 */     this.myConverter = specificStringConverter;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toStringFormat(T value, String format) {
/* 75 */     return toString(value);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\spreadsheet\StringConverterWithFormat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */