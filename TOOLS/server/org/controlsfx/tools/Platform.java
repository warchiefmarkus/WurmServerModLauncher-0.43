/*    */ package org.controlsfx.tools;
/*    */ 
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
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
/*    */ public enum Platform
/*    */ {
/* 38 */   WINDOWS("windows"),
/* 39 */   OSX("mac"),
/* 40 */   UNIX("unix"),
/* 41 */   UNKNOWN(""); private static Platform current;
/*    */   static {
/* 43 */     current = getCurrentPlatform();
/*    */   }
/*    */   private String platformId;
/*    */   
/*    */   Platform(String platformId) {
/* 48 */     this.platformId = platformId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getPlatformId() {
/* 56 */     return this.platformId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Platform getCurrent() {
/* 63 */     return current;
/*    */   }
/*    */   
/*    */   private static Platform getCurrentPlatform() {
/* 67 */     String osName = System.getProperty("os.name");
/* 68 */     if (osName.startsWith("Windows")) return WINDOWS; 
/* 69 */     if (osName.startsWith("Mac")) return OSX; 
/* 70 */     if (osName.startsWith("SunOS")) return UNIX; 
/* 71 */     if (osName.startsWith("Linux")) {
/* 72 */       String javafxPlatform = AccessController.<String>doPrivileged(new PrivilegedAction<String>()
/*    */           {
/*    */             public String run() {
/* 75 */               return System.getProperty("javafx.platform");
/*    */             }
/*    */           });
/* 78 */       if (!"android".equals(javafxPlatform) && !"Dalvik".equals(System.getProperty("java.vm.name")))
/* 79 */         return UNIX; 
/*    */     } 
/* 81 */     return UNKNOWN;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\tools\Platform.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */