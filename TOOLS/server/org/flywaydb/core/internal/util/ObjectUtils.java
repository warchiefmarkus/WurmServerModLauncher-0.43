/*    */ package org.flywaydb.core.internal.util;
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
/*    */ public class ObjectUtils
/*    */ {
/*    */   public static boolean nullSafeEquals(Object o1, Object o2) {
/* 33 */     if (o1 == o2) {
/* 34 */       return true;
/*    */     }
/* 36 */     if (o1 == null || o2 == null) {
/* 37 */       return false;
/*    */     }
/* 39 */     return o1.equals(o2);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\ObjectUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */