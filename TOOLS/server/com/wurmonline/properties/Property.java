/*    */ package com.wurmonline.properties;
/*    */ 
/*    */ import java.net.URL;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Property
/*    */ {
/* 13 */   private static final Logger logger = Logger.getLogger(Property.class.getName());
/*    */   
/*    */   private final String key;
/*    */   private final URL file;
/*    */   private final String defaultValue;
/*    */   private boolean isLoaded = false;
/*    */   private String value;
/*    */   
/*    */   public Property(String _key, URL _file, String _default) {
/* 22 */     this.key = _key;
/* 23 */     this.file = _file;
/* 24 */     if (this.file == null) {
/*    */       
/* 26 */       this.value = _default;
/* 27 */       this.isLoaded = true;
/*    */     } 
/* 29 */     this.defaultValue = _default;
/*    */   }
/*    */ 
/*    */   
/*    */   private void fetch() {
/* 34 */     this.value = PropertiesRepository.getInstance().getValueFor(this.file, this.key);
/* 35 */     if (this.value == null || this.value.isEmpty() || (this.value.startsWith("${") && this.value.endsWith("}")))
/* 36 */       this.value = this.defaultValue; 
/* 37 */     this.isLoaded = true;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getValue() {
/* 42 */     if (!this.isLoaded)
/* 43 */       fetch(); 
/* 44 */     return this.value;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public final int getIntValue() {
/*    */     try {
/* 51 */       return Integer.parseInt(getValue());
/*    */     }
/* 53 */     catch (NumberFormatException e) {
/*    */       
/* 55 */       logger.warning("Unable to get integer value from " + getValue());
/* 56 */       return 0;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean getBooleanValue() {
/* 62 */     return Boolean.parseBoolean(getValue());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\properties\Property.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */