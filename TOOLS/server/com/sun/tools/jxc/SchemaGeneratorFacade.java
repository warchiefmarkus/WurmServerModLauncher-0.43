/*    */ package com.sun.tools.jxc;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
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
/*    */ public class SchemaGeneratorFacade
/*    */ {
/*    */   static Class array$Ljava$lang$String;
/*    */   
/*    */   public static void main(String[] args) throws Throwable {
/*    */     try {
/* 50 */       ClassLoader cl = SchemaGeneratorFacade.class.getClassLoader();
/* 51 */       if (cl == null) cl = ClassLoader.getSystemClassLoader();
/*    */       
/* 53 */       Class driver = cl.loadClass("com.sun.tools.jxc.SchemaGenerator");
/* 54 */       Method mainMethod = driver.getDeclaredMethod("main", new Class[] { (array$Ljava$lang$String == null) ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String });
/*    */       try {
/* 56 */         mainMethod.invoke(null, new Object[] { args });
/* 57 */       } catch (IllegalAccessException e) {
/* 58 */         throw e;
/* 59 */       } catch (InvocationTargetException e) {
/* 60 */         if (e.getTargetException() != null)
/* 61 */           throw e.getTargetException(); 
/*    */       } 
/* 63 */     } catch (UnsupportedClassVersionError e) {
/* 64 */       System.err.println("schemagen requires JDK 5.0 or later. Please download it from http://java.sun.com/j2se/1.5/");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\jxc\SchemaGeneratorFacade.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */