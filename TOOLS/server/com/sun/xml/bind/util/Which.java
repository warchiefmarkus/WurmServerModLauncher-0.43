/*    */ package com.sun.xml.bind.util;
/*    */ 
/*    */ import java.net.URL;
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
/*    */ public class Which
/*    */ {
/*    */   public static String which(Class clazz) {
/* 53 */     return which(clazz.getName(), clazz.getClassLoader());
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
/*    */   public static String which(String classname, ClassLoader loader) {
/* 65 */     String classnameAsResource = classname.replace('.', '/') + ".class";
/*    */     
/* 67 */     if (loader == null) {
/* 68 */       loader = ClassLoader.getSystemClassLoader();
/*    */     }
/*    */     
/* 71 */     URL it = loader.getResource(classnameAsResource);
/* 72 */     if (it != null) {
/* 73 */       return it.toString();
/*    */     }
/* 75 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bin\\util\Which.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */