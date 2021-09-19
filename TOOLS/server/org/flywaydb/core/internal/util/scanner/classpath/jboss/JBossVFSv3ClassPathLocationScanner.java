/*    */ package org.flywaydb.core.internal.util.scanner.classpath.jboss;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.TreeSet;
/*    */ import org.flywaydb.core.internal.util.UrlUtils;
/*    */ import org.flywaydb.core.internal.util.logging.Log;
/*    */ import org.flywaydb.core.internal.util.logging.LogFactory;
/*    */ import org.flywaydb.core.internal.util.scanner.classpath.ClassPathLocationScanner;
/*    */ import org.jboss.vfs.VFS;
/*    */ import org.jboss.vfs.VirtualFile;
/*    */ import org.jboss.vfs.VirtualFileFilter;
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
/*    */ public class JBossVFSv3ClassPathLocationScanner
/*    */   implements ClassPathLocationScanner
/*    */ {
/* 36 */   private static final Log LOG = LogFactory.getLog(JBossVFSv3ClassPathLocationScanner.class);
/*    */   
/*    */   public Set<String> findResourceNames(String location, URL locationUrl) throws IOException {
/* 39 */     String filePath = UrlUtils.toFilePath(locationUrl);
/* 40 */     String classPathRootOnDisk = filePath.substring(0, filePath.length() - location.length());
/* 41 */     if (!classPathRootOnDisk.endsWith("/")) {
/* 42 */       classPathRootOnDisk = classPathRootOnDisk + "/";
/*    */     }
/* 44 */     LOG.debug("Scanning starting at classpath root on JBoss VFS: " + classPathRootOnDisk);
/*    */     
/* 46 */     Set<String> resourceNames = new TreeSet<String>();
/*    */     
/* 48 */     List<VirtualFile> files = VFS.getChild(filePath).getChildrenRecursively(new VirtualFileFilter() {
/*    */           public boolean accepts(VirtualFile file) {
/* 50 */             return file.isFile();
/*    */           }
/*    */         });
/* 53 */     for (VirtualFile file : files) {
/* 54 */       resourceNames.add(file.getPathName().substring(classPathRootOnDisk.length()));
/*    */     }
/*    */     
/* 57 */     return resourceNames;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\scanner\classpath\jboss\JBossVFSv3ClassPathLocationScanner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */