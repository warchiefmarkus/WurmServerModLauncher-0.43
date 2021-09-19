/*    */ package org.flywaydb.core.internal.util.scanner.classpath;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
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
/*    */ public class DefaultUrlResolver
/*    */   implements UrlResolver
/*    */ {
/*    */   public URL toStandardJavaUrl(URL url) throws IOException {
/* 26 */     return url;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\scanner\classpath\DefaultUrlResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */