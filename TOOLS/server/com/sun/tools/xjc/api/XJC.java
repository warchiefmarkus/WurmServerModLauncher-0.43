/*    */ package com.sun.tools.xjc.api;
/*    */ 
/*    */ import com.sun.tools.xjc.api.impl.j2s.JavaCompilerImpl;
/*    */ import com.sun.tools.xjc.api.impl.s2j.SchemaCompilerImpl;
/*    */ import com.sun.xml.bind.api.impl.NameConverter;
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
/*    */ public final class XJC
/*    */ {
/*    */   public static JavaCompiler createJavaCompiler() {
/* 57 */     return (JavaCompiler)new JavaCompilerImpl();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static SchemaCompiler createSchemaCompiler() {
/* 67 */     return (SchemaCompiler)new SchemaCompilerImpl();
/*    */   }
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
/*    */   public static String getDefaultPackageName(String namespaceUri) {
/* 83 */     if (namespaceUri == null) throw new IllegalArgumentException(); 
/* 84 */     return NameConverter.standard.toPackageName(namespaceUri);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\api\XJC.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */