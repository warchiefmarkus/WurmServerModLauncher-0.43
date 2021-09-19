/*    */ package com.sun.javaws.security;
/*    */ 
/*    */ import com.sun.jnlp.JNLPClassLoader;
/*    */ import com.sun.jnlp.PrintServiceImpl;
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
/*    */ public class JavaWebStartSecurity
/*    */   extends SecurityManager
/*    */ {
/*    */   private JNLPClassLoader currentJNLPClassLoader() {
/* 31 */     Class[] arrayOfClass = getClassContext();
/* 32 */     for (byte b = 0; b < arrayOfClass.length; b++) {
/* 33 */       ClassLoader classLoader1 = arrayOfClass[b].getClassLoader();
/*    */       
/* 35 */       if (classLoader1 instanceof JNLPClassLoader) {
/* 36 */         return (JNLPClassLoader)classLoader1;
/*    */       }
/*    */     } 
/*    */     
/* 40 */     ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
/* 41 */     if (classLoader instanceof JNLPClassLoader) {
/* 42 */       return (JNLPClassLoader)classLoader;
/*    */     }
/*    */ 
/*    */     
/* 46 */     return (JNLPClassLoader)null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void checkAwtEventQueueAccess() {
/* 51 */     if (!AppContextUtil.isApplicationAppContext() && currentJNLPClassLoader() != null)
/*    */     {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 58 */       super.checkAwtEventQueueAccess();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Class[] getExecutionStackContext() {
/* 70 */     return getClassContext();
/*    */   }
/*    */ 
/*    */   
/*    */   public void checkPrintJobAccess() {
/*    */     try {
/* 76 */       super.checkPrintJobAccess();
/*    */     }
/* 78 */     catch (SecurityException securityException) {
/*    */       
/* 80 */       if (PrintServiceImpl.requestPrintPermission()) {
/*    */         return;
/*    */       }
/*    */       
/* 84 */       throw securityException;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\security\JavaWebStartSecurity.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */