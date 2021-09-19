/*    */ package com.sun.tools.xjc.reader;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URL;
/*    */ import org.xml.sax.InputSource;
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
/*    */ public class Util
/*    */ {
/*    */   public static Object getFileOrURL(String fileOrURL) throws IOException {
/*    */     try {
/* 55 */       return new URL(fileOrURL);
/* 56 */     } catch (MalformedURLException e) {
/* 57 */       return (new File(fileOrURL)).getCanonicalFile();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static InputSource getInputSource(String fileOrURL) {
/*    */     try {
/* 66 */       Object o = getFileOrURL(fileOrURL);
/* 67 */       if (o instanceof URL) {
/* 68 */         return new InputSource(escapeSpace(((URL)o).toExternalForm()));
/*    */       }
/* 70 */       String url = ((File)o).toURL().toExternalForm();
/* 71 */       return new InputSource(escapeSpace(url));
/*    */     }
/* 73 */     catch (IOException e) {
/* 74 */       return new InputSource(fileOrURL);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static String escapeSpace(String url) {
/* 80 */     StringBuffer buf = new StringBuffer();
/* 81 */     for (int i = 0; i < url.length(); i++) {
/*    */       
/* 83 */       if (url.charAt(i) == ' ') {
/* 84 */         buf.append("%20");
/*    */       } else {
/* 86 */         buf.append(url.charAt(i));
/*    */       } 
/* 88 */     }  return buf.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\Util.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */