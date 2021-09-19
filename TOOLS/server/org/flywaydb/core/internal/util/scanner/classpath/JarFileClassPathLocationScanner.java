/*     */ package org.flywaydb.core.internal.util.scanner.classpath;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.JarURLConnection;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
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
/*     */ public class JarFileClassPathLocationScanner
/*     */   implements ClassPathLocationScanner
/*     */ {
/*     */   public Set<String> findResourceNames(String location, URL locationUrl) throws IOException {
/*  34 */     JarFile jarFile = getJarFromUrl(locationUrl);
/*     */ 
/*     */     
/*     */     try {
/*  38 */       String prefix = jarFile.getName().toLowerCase().endsWith(".war") ? "WEB-INF/classes/" : "";
/*  39 */       return findResourceNamesFromJarFile(jarFile, prefix, location);
/*     */     } finally {
/*  41 */       jarFile.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JarFile getJarFromUrl(URL locationUrl) throws IOException {
/*  53 */     URLConnection con = locationUrl.openConnection();
/*  54 */     if (con instanceof JarURLConnection) {
/*     */       
/*  56 */       JarURLConnection jarCon = (JarURLConnection)con;
/*  57 */       jarCon.setUseCaches(false);
/*  58 */       return jarCon.getJarFile();
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  65 */     String urlFile = locationUrl.getFile();
/*     */     
/*  67 */     int separatorIndex = urlFile.indexOf("!/");
/*  68 */     if (separatorIndex != -1) {
/*  69 */       String jarFileUrl = urlFile.substring(0, separatorIndex);
/*  70 */       if (jarFileUrl.startsWith("file:")) {
/*     */         try {
/*  72 */           return new JarFile((new URL(jarFileUrl)).toURI().getSchemeSpecificPart());
/*  73 */         } catch (URISyntaxException ex) {
/*     */           
/*  75 */           return new JarFile(jarFileUrl.substring("file:".length()));
/*     */         } 
/*     */       }
/*  78 */       return new JarFile(jarFileUrl);
/*     */     } 
/*     */     
/*  81 */     return new JarFile(urlFile);
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
/*     */   private Set<String> findResourceNamesFromJarFile(JarFile jarFile, String prefix, String location) throws IOException {
/*  94 */     String toScan = prefix + location + (location.endsWith("/") ? "" : "/");
/*  95 */     Set<String> resourceNames = new TreeSet<String>();
/*     */     
/*  97 */     Enumeration<JarEntry> entries = jarFile.entries();
/*  98 */     while (entries.hasMoreElements()) {
/*  99 */       String entryName = ((JarEntry)entries.nextElement()).getName();
/* 100 */       if (entryName.startsWith(toScan)) {
/* 101 */         resourceNames.add(entryName.substring(prefix.length()));
/*     */       }
/*     */     } 
/*     */     
/* 105 */     return resourceNames;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\scanner\classpath\JarFileClassPathLocationScanner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */