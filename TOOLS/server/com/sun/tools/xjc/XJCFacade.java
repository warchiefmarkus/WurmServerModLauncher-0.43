/*    */ package com.sun.tools.xjc;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class XJCFacade
/*    */ {
/*    */   static Class array$Ljava$lang$String;
/*    */   
/*    */   public static void main(String[] args) throws Throwable {
/* 54 */     String v = "2.0";
/*    */     
/* 56 */     for (int i = 0; i < args.length; i++) {
/* 57 */       if (args[i].equals("-source") && 
/* 58 */         i + 1 < args.length) {
/* 59 */         v = parseVersion(args[i + 1]);
/*    */       }
/*    */     } 
/*    */ 
/*    */     
/*    */     try {
/* 65 */       ClassLoader cl = ClassLoaderBuilder.createProtectiveClassLoader(XJCFacade.class.getClassLoader(), v);
/*    */       
/* 67 */       Class driver = cl.loadClass("com.sun.tools.xjc.Driver");
/* 68 */       Method mainMethod = driver.getDeclaredMethod("main", new Class[] { (array$Ljava$lang$String == null) ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String });
/*    */       try {
/* 70 */         mainMethod.invoke(null, new Object[] { args });
/* 71 */       } catch (IllegalAccessException e) {
/* 72 */         throw e;
/* 73 */       } catch (InvocationTargetException e) {
/* 74 */         if (e.getTargetException() != null)
/* 75 */           throw e.getTargetException(); 
/*    */       } 
/* 77 */     } catch (UnsupportedClassVersionError e) {
/* 78 */       System.err.println("XJC requires JDK 5.0 or later. Please download it from http://java.sun.com/j2se/1.5/");
/*    */     } 
/*    */   }
/*    */   
/*    */   private static String parseVersion(String version) {
/* 83 */     if (version.equals("1.0")) {
/* 84 */       return version;
/*    */     }
/*    */ 
/*    */     
/* 88 */     return "2.0";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\XJCFacade.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */