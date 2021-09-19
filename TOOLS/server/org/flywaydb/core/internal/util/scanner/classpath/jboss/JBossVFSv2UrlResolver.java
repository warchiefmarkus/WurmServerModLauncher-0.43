/*    */ package org.flywaydb.core.internal.util.scanner.classpath.jboss;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Method;
/*    */ import java.net.URL;
/*    */ import org.flywaydb.core.api.FlywayException;
/*    */ import org.flywaydb.core.internal.util.scanner.classpath.UrlResolver;
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
/*    */ public class JBossVFSv2UrlResolver
/*    */   implements UrlResolver
/*    */ {
/*    */   public URL toStandardJavaUrl(URL url) throws IOException {
/*    */     try {
/* 31 */       Class<?> vfsClass = Class.forName("org.jboss.virtual.VFS");
/* 32 */       Class<?> vfsUtilsClass = Class.forName("org.jboss.virtual.VFSUtils");
/* 33 */       Class<?> virtualFileClass = Class.forName("org.jboss.virtual.VirtualFile");
/*    */       
/* 35 */       Method getRootMethod = vfsClass.getMethod("getRoot", new Class[] { URL.class });
/* 36 */       Method getRealURLMethod = vfsUtilsClass.getMethod("getRealURL", new Class[] { virtualFileClass });
/*    */       
/* 38 */       Object root = getRootMethod.invoke(null, new Object[] { url });
/* 39 */       return (URL)getRealURLMethod.invoke(null, new Object[] { root });
/* 40 */     } catch (Exception e) {
/* 41 */       throw new FlywayException("JBoss VFS v2 call failed", e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\scanner\classpath\jboss\JBossVFSv2UrlResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */