/*     */ package org.flywaydb.core.internal.util.scanner.classpath.android;
/*     */ 
/*     */ import android.content.Context;
/*     */ import dalvik.system.DexFile;
/*     */ import dalvik.system.PathClassLoader;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import org.flywaydb.core.api.FlywayException;
/*     */ import org.flywaydb.core.api.android.ContextHolder;
/*     */ import org.flywaydb.core.internal.util.ClassUtils;
/*     */ import org.flywaydb.core.internal.util.Location;
/*     */ import org.flywaydb.core.internal.util.logging.Log;
/*     */ import org.flywaydb.core.internal.util.logging.LogFactory;
/*     */ import org.flywaydb.core.internal.util.scanner.Resource;
/*     */ import org.flywaydb.core.internal.util.scanner.classpath.ResourceAndClassScanner;
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
/*     */ public class AndroidScanner
/*     */   implements ResourceAndClassScanner
/*     */ {
/*  39 */   private static final Log LOG = LogFactory.getLog(AndroidScanner.class);
/*     */   
/*     */   private final Context context;
/*     */   
/*     */   private final PathClassLoader classLoader;
/*     */   
/*     */   public AndroidScanner(ClassLoader classLoader) {
/*  46 */     this.classLoader = (PathClassLoader)classLoader;
/*  47 */     this.context = ContextHolder.getContext();
/*  48 */     if (this.context == null) {
/*  49 */       throw new FlywayException("Unable to scan for Migrations! Context not set. Within an activity you can fix this with org.flywaydb.core.api.android.ContextHolder.setContext(this);");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Resource[] scanForResources(Location location, String prefix, String suffix) throws Exception {
/*  55 */     List<Resource> resources = new ArrayList<Resource>();
/*     */     
/*  57 */     String path = location.getPath();
/*  58 */     for (String asset : this.context.getAssets().list(path)) {
/*  59 */       if (asset.startsWith(prefix) && asset.endsWith(suffix) && asset
/*  60 */         .length() > (prefix + suffix).length()) {
/*  61 */         resources.add(new AndroidResource(this.context.getAssets(), path, asset));
/*     */       } else {
/*  63 */         LOG.debug("Filtering out asset: " + asset);
/*     */       } 
/*     */     } 
/*     */     
/*  67 */     return resources.<Resource>toArray(new Resource[resources.size()]);
/*     */   }
/*     */   
/*     */   public Class<?>[] scanForClasses(Location location, Class<?> implementedInterface) throws Exception {
/*  71 */     String pkg = location.getPath().replace("/", ".");
/*     */     
/*  73 */     List<Class<?>> classes = new ArrayList<Class<?>>();
/*     */     
/*  75 */     DexFile dex = new DexFile((this.context.getApplicationInfo()).sourceDir);
/*  76 */     Enumeration<String> entries = dex.entries();
/*  77 */     while (entries.hasMoreElements()) {
/*  78 */       String className = entries.nextElement();
/*  79 */       if (className.startsWith(pkg)) {
/*  80 */         Class<?> clazz = this.classLoader.loadClass(className);
/*  81 */         if (Modifier.isAbstract(clazz.getModifiers())) {
/*  82 */           LOG.debug("Skipping abstract class: " + className);
/*     */           
/*     */           continue;
/*     */         } 
/*  86 */         if (!implementedInterface.isAssignableFrom(clazz)) {
/*     */           continue;
/*     */         }
/*     */         
/*     */         try {
/*  91 */           ClassUtils.instantiate(className, (ClassLoader)this.classLoader);
/*  92 */         } catch (Exception e) {
/*  93 */           throw new FlywayException("Unable to instantiate class: " + className);
/*     */         } 
/*     */         
/*  96 */         classes.add(clazz);
/*  97 */         LOG.debug("Found class: " + className);
/*     */       } 
/*     */     } 
/* 100 */     return (Class[])classes.<Class<?>[]>toArray((Class<?>[][])new Class[classes.size()]);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\scanner\classpath\android\AndroidScanner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */