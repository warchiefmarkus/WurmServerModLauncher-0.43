/*    */ package org.flywaydb.core.internal.util.scanner.classpath;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import java.util.Enumeration;
/*    */ import java.util.Set;
/*    */ import java.util.TreeSet;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ import org.osgi.framework.Bundle;
/*    */ import org.osgi.framework.FrameworkUtil;
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
/*    */ public class OsgiClassPathLocationScanner
/*    */   implements ClassPathLocationScanner
/*    */ {
/* 42 */   private static final Pattern bundleIdPattern = Pattern.compile("^\\d+");
/*    */   
/*    */   public Set<String> findResourceNames(String location, URL locationUrl) throws IOException {
/* 45 */     Set<String> resourceNames = new TreeSet<String>();
/*    */     
/* 47 */     Bundle bundle = getTargetBundleOrCurrent(FrameworkUtil.getBundle(getClass()), locationUrl);
/*    */     
/* 49 */     Enumeration<URL> entries = bundle.findEntries(locationUrl.getPath(), "*", true);
/*    */     
/* 51 */     if (entries != null) {
/* 52 */       while (entries.hasMoreElements()) {
/* 53 */         URL entry = entries.nextElement();
/* 54 */         String resourceName = getPathWithoutLeadingSlash(entry);
/*    */         
/* 56 */         resourceNames.add(resourceName);
/*    */       } 
/*    */     }
/*    */     
/* 60 */     return resourceNames;
/*    */   }
/*    */   
/*    */   private Bundle getTargetBundleOrCurrent(Bundle currentBundle, URL locationUrl) {
/*    */     try {
/* 65 */       Bundle targetBundle = currentBundle.getBundleContext().getBundle(getBundleId(locationUrl.getHost()));
/* 66 */       return (targetBundle != null) ? targetBundle : currentBundle;
/* 67 */     } catch (Exception e) {
/* 68 */       return currentBundle;
/*    */     } 
/*    */   }
/*    */   
/*    */   private long getBundleId(String host) {
/* 73 */     Matcher matcher = bundleIdPattern.matcher(host);
/* 74 */     if (matcher.find()) {
/* 75 */       return Double.valueOf(matcher.group()).longValue();
/*    */     }
/* 77 */     throw new IllegalArgumentException("There's no bundleId in passed URL");
/*    */   }
/*    */   
/*    */   private String getPathWithoutLeadingSlash(URL entry) {
/* 81 */     String path = entry.getPath();
/*    */     
/* 83 */     return path.startsWith("/") ? path.substring(1) : path;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\scanner\classpath\OsgiClassPathLocationScanner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */