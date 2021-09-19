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
/*    */ @Immutable
/*    */ public class CloneUtils
/*    */ {
/*    */   public static Object clone(Object obj) throws CloneNotSupportedException {
/* 46 */     if (obj == null) {
/* 47 */       return null;
/*    */     }
/* 49 */     if (obj instanceof Cloneable) {
/* 50 */       Method m; Class<?> clazz = obj.getClass();
/*    */       
/*    */       try {
/* 53 */         m = clazz.getMethod("clone", (Class[])null);
/* 54 */       } catch (NoSuchMethodException ex) {
/* 55 */         throw new NoSuchMethodError(ex.getMessage());
/*    */       } 
/*    */       try {
/* 58 */         return m.invoke(obj, (Object[])null);
/* 59 */       } catch (InvocationTargetException ex) {
/* 60 */         Throwable cause = ex.getCause();
/* 61 */         if (cause instanceof CloneNotSupportedException) {
/* 62 */           throw (CloneNotSupportedException)cause;
/*    */         }
/* 64 */         throw new Error("Unexpected exception", cause);
/*    */       }
/* 66 */       catch (IllegalAccessException ex) {
/* 67 */         throw new IllegalAccessError(ex.getMessage());
/*    */       } 
/*    */     } 
/* 70 */     throw new CloneNotSupportedException();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\clien\\utils\CloneUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */