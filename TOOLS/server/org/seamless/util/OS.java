/*    */ package org.seamless.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OS
/*    */ {
/*    */   public static boolean checkForLinux() {
/* 23 */     return checkForPresence("os.name", "linux");
/*    */   }
/*    */   
/*    */   public static boolean checkForHp() {
/* 27 */     return checkForPresence("os.name", "hp");
/*    */   }
/*    */   
/*    */   public static boolean checkForSolaris() {
/* 31 */     return checkForPresence("os.name", "sun");
/*    */   }
/*    */   
/*    */   public static boolean checkForWindows() {
/* 35 */     return checkForPresence("os.name", "win");
/*    */   }
/*    */   
/*    */   public static boolean checkForMac() {
/* 39 */     return checkForPresence("os.name", "mac");
/*    */   }
/*    */   
/*    */   private static boolean checkForPresence(String key, String value) {
/*    */     try {
/* 44 */       String tmp = System.getProperty(key);
/* 45 */       return (tmp != null && tmp.trim().toLowerCase().startsWith(value));
/*    */     }
/* 47 */     catch (Throwable t) {
/* 48 */       return false;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamles\\util\OS.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */