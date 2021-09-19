/*    */ package org.flywaydb.core.internal.util.scanner;
/*    */ 
/*    */ import org.flywaydb.core.api.FlywayException;
/*    */ import org.flywaydb.core.internal.util.FeatureDetector;
/*    */ import org.flywaydb.core.internal.util.Location;
/*    */ import org.flywaydb.core.internal.util.scanner.classpath.ClassPathScanner;
/*    */ import org.flywaydb.core.internal.util.scanner.classpath.ResourceAndClassScanner;
/*    */ import org.flywaydb.core.internal.util.scanner.classpath.android.AndroidScanner;
/*    */ import org.flywaydb.core.internal.util.scanner.filesystem.FileSystemScanner;
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
/*    */ public class Scanner
/*    */ {
/*    */   private final ResourceAndClassScanner resourceAndClassScanner;
/*    */   private final ClassLoader classLoader;
/* 33 */   private final FileSystemScanner fileSystemScanner = new FileSystemScanner();
/*    */   
/*    */   public Scanner(ClassLoader classLoader) {
/* 36 */     this.classLoader = classLoader;
/* 37 */     if ((new FeatureDetector(classLoader)).isAndroidAvailable()) {
/* 38 */       this.resourceAndClassScanner = (ResourceAndClassScanner)new AndroidScanner(classLoader);
/*    */     } else {
/* 40 */       this.resourceAndClassScanner = (ResourceAndClassScanner)new ClassPathScanner(classLoader);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Resource[] scanForResources(Location location, String prefix, String suffix) {
/*    */     try {
/* 54 */       if (location.isFileSystem()) {
/* 55 */         return this.fileSystemScanner.scanForResources(location, prefix, suffix);
/*    */       }
/* 57 */       return this.resourceAndClassScanner.scanForResources(location, prefix, suffix);
/* 58 */     } catch (Exception e) {
/* 59 */       throw new FlywayException("Unable to scan for SQL migrations in location: " + location, e);
/*    */     } 
/*    */   }
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
/*    */   public Class<?>[] scanForClasses(Location location, Class<?> implementedInterface) throws Exception {
/* 75 */     return this.resourceAndClassScanner.scanForClasses(location, implementedInterface);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ClassLoader getClassLoader() {
/* 82 */     return this.classLoader;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\scanner\Scanner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */