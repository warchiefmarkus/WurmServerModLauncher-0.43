/*    */ package com.sun.xml.bind.v2.util;
/*    */ 
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
/*    */ public class TypeCast
/*    */ {
/*    */   public static <K, V> Map<K, V> checkedCast(Map<?, ?> m, Class<K> keyType, Class<V> valueType) {
/* 49 */     if (m == null)
/* 50 */       return null; 
/* 51 */     for (Map.Entry<?, ?> e : m.entrySet()) {
/* 52 */       if (!keyType.isInstance(e.getKey()))
/* 53 */         throw new ClassCastException(e.getKey().getClass().toString()); 
/* 54 */       if (!valueType.isInstance(e.getValue()))
/* 55 */         throw new ClassCastException(e.getValue().getClass().toString()); 
/*    */     } 
/* 57 */     return (Map)m;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v\\util\TypeCast.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */