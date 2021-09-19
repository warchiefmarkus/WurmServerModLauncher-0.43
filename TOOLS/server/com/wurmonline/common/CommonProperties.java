/*    */ package com.wurmonline.common;
/*    */ 
/*    */ import com.wurmonline.properties.Property;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum CommonProperties
/*    */ {
/* 12 */   VERSION("version", "build.properties", "UNKNOWN"),
/* 13 */   BUILD_TIME("build-time", "build.properties", "unknown"),
/* 14 */   COMMIT("git-sha-1", "build.properties", "unknown");
/*    */   
/*    */   Property property;
/*    */ 
/*    */   
/*    */   CommonProperties(String _key, String _file, String _default) {
/* 20 */     this.property = new Property(_key, CommonProperties.class.getResource(_file), _default);
/*    */   }
/*    */ 
/*    */   
/*    */   public Property getProperty() {
/* 25 */     return this.property;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\common\CommonProperties.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */