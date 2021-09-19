/*    */ package org.apache.http.client.utils;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ import org.apache.http.annotation.Immutable;
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
/*    */ @Immutable
/*    */ public class JdkIdn
/*    */   implements Idn
/*    */ {
/*    */   private final Method toUnicode;
/*    */   
/*    */   public JdkIdn() throws ClassNotFoundException {
/* 52 */     Class<?> clazz = Class.forName("java.net.IDN");
/*    */     try {
/* 54 */       this.toUnicode = clazz.getMethod("toUnicode", new Class[] { String.class });
/* 55 */     } catch (SecurityException e) {
/*    */       
/* 57 */       throw new IllegalStateException(e.getMessage(), e);
/* 58 */     } catch (NoSuchMethodException e) {
/*    */       
/* 60 */       throw new IllegalStateException(e.getMessage(), e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public String toUnicode(String punycode) {
/*    */     try {
/* 66 */       return (String)this.toUnicode.invoke(null, new Object[] { punycode });
/* 67 */     } catch (IllegalAccessException e) {
/* 68 */       throw new IllegalStateException(e.getMessage(), e);
/* 69 */     } catch (InvocationTargetException e) {
/* 70 */       Throwable t = e.getCause();
/* 71 */       throw new RuntimeException(t.getMessage(), t);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\clien\\utils\JdkIdn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */