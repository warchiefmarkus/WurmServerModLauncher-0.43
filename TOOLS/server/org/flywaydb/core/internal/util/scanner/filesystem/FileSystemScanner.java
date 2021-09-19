/*     */ package org.flywaydb.core.internal.util.scanner.filesystem;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import org.flywaydb.core.internal.util.Location;
/*     */ import org.flywaydb.core.internal.util.logging.Log;
/*     */ import org.flywaydb.core.internal.util.logging.LogFactory;
/*     */ import org.flywaydb.core.internal.util.scanner.Resource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileSystemScanner
/*     */ {
/*  32 */   private static final Log LOG = LogFactory.getLog(FileSystemScanner.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Resource[] scanForResources(Location location, String prefix, String suffix) throws IOException {
/*  45 */     String path = location.getPath();
/*  46 */     LOG.debug("Scanning for filesystem resources at '" + path + "' (Prefix: '" + prefix + "', Suffix: '" + suffix + "')");
/*     */     
/*  48 */     File dir = new File(path);
/*  49 */     if (!dir.isDirectory() || !dir.canRead()) {
/*  50 */       LOG.warn("Unable to resolve location filesystem:" + path);
/*  51 */       return new Resource[0];
/*     */     } 
/*     */     
/*  54 */     Set<Resource> resources = new TreeSet<Resource>();
/*     */     
/*  56 */     Set<String> resourceNames = findResourceNames(path, prefix, suffix);
/*  57 */     for (String resourceName : resourceNames) {
/*  58 */       resources.add(new FileSystemResource(resourceName));
/*  59 */       LOG.debug("Found filesystem resource: " + resourceName);
/*     */     } 
/*     */     
/*  62 */     return resources.<Resource>toArray(new Resource[resources.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Set<String> findResourceNames(String path, String prefix, String suffix) throws IOException {
/*  76 */     Set<String> resourceNames = findResourceNamesFromFileSystem(path, new File(path));
/*  77 */     return filterResourceNames(resourceNames, prefix, suffix);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Set<String> findResourceNamesFromFileSystem(String scanRootLocation, File folder) throws IOException {
/*  90 */     LOG.debug("Scanning for resources in path: " + folder.getPath() + " (" + scanRootLocation + ")");
/*     */     
/*  92 */     Set<String> resourceNames = new TreeSet<String>();
/*     */     
/*  94 */     File[] files = folder.listFiles();
/*  95 */     for (File file : files) {
/*  96 */       if (file.canRead()) {
/*  97 */         if (file.isDirectory()) {
/*  98 */           resourceNames.addAll(findResourceNamesFromFileSystem(scanRootLocation, file));
/*     */         } else {
/* 100 */           resourceNames.add(file.getPath());
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 105 */     return resourceNames;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Set<String> filterResourceNames(Set<String> resourceNames, String prefix, String suffix) {
/* 117 */     Set<String> filteredResourceNames = new TreeSet<String>();
/* 118 */     for (String resourceName : resourceNames) {
/* 119 */       String fileName = resourceName.substring(resourceName.lastIndexOf(File.separator) + 1);
/* 120 */       if (fileName.startsWith(prefix) && fileName.endsWith(suffix) && fileName
/* 121 */         .length() > (prefix + suffix).length()) {
/* 122 */         filteredResourceNames.add(resourceName); continue;
/*     */       } 
/* 124 */       LOG.debug("Filtering out resource: " + resourceName + " (filename: " + fileName + ")");
/*     */     } 
/*     */     
/* 127 */     return filteredResourceNames;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\scanner\filesystem\FileSystemScanner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */