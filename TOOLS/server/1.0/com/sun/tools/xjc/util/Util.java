/*    */ package 1.0.com.sun.tools.xjc.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Util
/*    */ {
/*    */   public static final String getSystemProperty(String name) {
/*    */     try {
/* 24 */       return System.getProperty(name);
/* 25 */     } catch (SecurityException e) {
/* 26 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final String getSystemProperty(Class clazz, String name) {
/* 35 */     return getSystemProperty(clazz.getName() + '.' + name);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int calculateInitialHashMapCapacity(int count, float loadFactor) {
/* 45 */     int initialCapacity = (int)Math.ceil((count / loadFactor)) + 1;
/*    */     
/* 47 */     if (initialCapacity < 16) {
/* 48 */       return 16;
/*    */     }
/* 50 */     return initialCapacity;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xj\\util\Util.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */