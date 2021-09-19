/*    */ package org.flywaydb.core.internal.util;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.UnsupportedEncodingException;
/*    */ import java.net.URL;
/*    */ import java.net.URLDecoder;
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
/*    */ public class UrlUtils
/*    */ {
/*    */   public static String toFilePath(URL url) {
/*    */     try {
/* 42 */       String filePath = (new File(URLDecoder.decode(url.getPath().replace("+", "%2b"), "UTF-8"))).getAbsolutePath();
/* 43 */       if (filePath.endsWith("/")) {
/* 44 */         return filePath.substring(0, filePath.length() - 1);
/*    */       }
/* 46 */       return filePath;
/* 47 */     } catch (UnsupportedEncodingException e) {
/* 48 */       throw new IllegalStateException("Can never happen", e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\UrlUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */