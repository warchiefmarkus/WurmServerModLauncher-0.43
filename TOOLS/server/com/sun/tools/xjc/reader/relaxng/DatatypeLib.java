/*    */ package com.sun.tools.xjc.reader.relaxng;
/*    */ 
/*    */ import com.sun.tools.xjc.model.CBuiltinLeafInfo;
/*    */ import com.sun.tools.xjc.model.TypeUse;
/*    */ import com.sun.tools.xjc.reader.xmlschema.SimpleTypeBuilder;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ final class DatatypeLib
/*    */ {
/*    */   public final String nsUri;
/* 59 */   private final Map<String, TypeUse> types = new HashMap<String, TypeUse>();
/*    */   
/*    */   public DatatypeLib(String nsUri) {
/* 62 */     this.nsUri = nsUri;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   TypeUse get(String name) {
/* 69 */     return this.types.get(name);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 75 */   public static final DatatypeLib BUILTIN = new DatatypeLib("");
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 80 */   public static final DatatypeLib XMLSCHEMA = new DatatypeLib("http://www.w3.org/2001/XMLSchema-datatypes");
/*    */   
/*    */   static {
/* 83 */     BUILTIN.types.put("token", CBuiltinLeafInfo.TOKEN);
/* 84 */     BUILTIN.types.put("string", CBuiltinLeafInfo.STRING);
/* 85 */     XMLSCHEMA.types.putAll(SimpleTypeBuilder.builtinConversions);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\relaxng\DatatypeLib.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */