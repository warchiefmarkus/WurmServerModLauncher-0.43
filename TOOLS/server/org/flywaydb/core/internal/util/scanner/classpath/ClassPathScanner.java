/*     */ package org.flywaydb.core.internal.util.scanner.classpath;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.net.URL;
/*     */ import java.net.URLDecoder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import org.flywaydb.core.api.FlywayException;
/*     */ import org.flywaydb.core.internal.util.ClassUtils;
/*     */ import org.flywaydb.core.internal.util.FeatureDetector;
/*     */ import org.flywaydb.core.internal.util.Location;
/*     */ import org.flywaydb.core.internal.util.UrlUtils;
/*     */ import org.flywaydb.core.internal.util.logging.Log;
/*     */ import org.flywaydb.core.internal.util.logging.LogFactory;
/*     */ import org.flywaydb.core.internal.util.scanner.Resource;
/*     */ import org.flywaydb.core.internal.util.scanner.classpath.jboss.JBossVFSv2UrlResolver;
/*     */ import org.flywaydb.core.internal.util.scanner.classpath.jboss.JBossVFSv3ClassPathLocationScanner;
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
/*     */ public class ClassPathScanner
/*     */   implements ResourceAndClassScanner
/*     */ {
/*  45 */   private static final Log LOG = LogFactory.getLog(ClassPathScanner.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ClassLoader classLoader;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   private final Map<Location, List<URL>> locationUrlCache = new HashMap<Location, List<URL>>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   private final Map<String, ClassPathLocationScanner> locationScannerCache = new HashMap<String, ClassPathLocationScanner>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  65 */   private final Map<ClassPathLocationScanner, Map<URL, Set<String>>> resourceNameCache = new HashMap<ClassPathLocationScanner, Map<URL, Set<String>>>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassPathScanner(ClassLoader classLoader) {
/*  73 */     this.classLoader = classLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public Resource[] scanForResources(Location path, String prefix, String suffix) throws IOException {
/*  78 */     LOG.debug("Scanning for classpath resources at '" + path + "' (Prefix: '" + prefix + "', Suffix: '" + suffix + "')");
/*     */     
/*  80 */     Set<Resource> resources = new TreeSet<Resource>();
/*     */     
/*  82 */     Set<String> resourceNames = findResourceNames(path, prefix, suffix);
/*  83 */     for (String resourceName : resourceNames) {
/*  84 */       resources.add(new ClassPathResource(resourceName, this.classLoader));
/*  85 */       LOG.debug("Found resource: " + resourceName);
/*     */     } 
/*     */     
/*  88 */     return resources.<Resource>toArray(new Resource[resources.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?>[] scanForClasses(Location location, Class<?> implementedInterface) throws Exception {
/*  93 */     LOG.debug("Scanning for classes at '" + location + "' (Implementing: '" + implementedInterface.getName() + "')");
/*     */     
/*  95 */     List<Class<?>> classes = new ArrayList<Class<?>>();
/*     */     
/*  97 */     Set<String> resourceNames = findResourceNames(location, "", ".class");
/*  98 */     for (String resourceName : resourceNames) {
/*  99 */       String className = toClassName(resourceName);
/* 100 */       Class<?> clazz = this.classLoader.loadClass(className);
/*     */       
/* 102 */       if (Modifier.isAbstract(clazz.getModifiers()) || clazz.isEnum() || clazz.isAnonymousClass()) {
/* 103 */         LOG.debug("Skipping non-instantiable class: " + className);
/*     */         
/*     */         continue;
/*     */       } 
/* 107 */       if (!implementedInterface.isAssignableFrom(clazz)) {
/*     */         continue;
/*     */       }
/*     */       
/*     */       try {
/* 112 */         ClassUtils.instantiate(className, this.classLoader);
/* 113 */       } catch (Exception e) {
/* 114 */         throw new FlywayException("Unable to instantiate class: " + className, e);
/*     */       } 
/*     */       
/* 117 */       classes.add(clazz);
/* 118 */       LOG.debug("Found class: " + className);
/*     */     } 
/*     */     
/* 121 */     return (Class[])classes.<Class<?>[]>toArray((Class<?>[][])new Class[classes.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String toClassName(String resourceName) {
/* 131 */     String nameWithDots = resourceName.replace("/", ".");
/* 132 */     return nameWithDots.substring(0, nameWithDots.length() - ".class".length());
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
/*     */   private Set<String> findResourceNames(Location location, String prefix, String suffix) throws IOException {
/* 146 */     Set<String> resourceNames = new TreeSet<String>();
/*     */     
/* 148 */     List<URL> locationsUrls = getLocationUrlsForPath(location);
/* 149 */     for (URL locationUrl : locationsUrls) {
/* 150 */       LOG.debug("Scanning URL: " + locationUrl.toExternalForm());
/*     */       
/* 152 */       UrlResolver urlResolver = createUrlResolver(locationUrl.getProtocol());
/* 153 */       URL resolvedUrl = urlResolver.toStandardJavaUrl(locationUrl);
/*     */       
/* 155 */       String protocol = resolvedUrl.getProtocol();
/* 156 */       ClassPathLocationScanner classPathLocationScanner = createLocationScanner(protocol);
/* 157 */       if (classPathLocationScanner == null) {
/* 158 */         String scanRoot = UrlUtils.toFilePath(resolvedUrl);
/* 159 */         LOG.warn("Unable to scan location: " + scanRoot + " (unsupported protocol: " + protocol + ")"); continue;
/*     */       } 
/* 161 */       Set<String> names = (Set<String>)((Map)this.resourceNameCache.get(classPathLocationScanner)).get(resolvedUrl);
/* 162 */       if (names == null) {
/* 163 */         names = classPathLocationScanner.findResourceNames(location.getPath(), resolvedUrl);
/* 164 */         ((Map<URL, Set<String>>)this.resourceNameCache.get(classPathLocationScanner)).put(resolvedUrl, names);
/*     */       } 
/* 166 */       resourceNames.addAll(names);
/*     */     } 
/*     */ 
/*     */     
/* 170 */     return filterResourceNames(resourceNames, prefix, suffix);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<URL> getLocationUrlsForPath(Location location) throws IOException {
/* 181 */     if (this.locationUrlCache.containsKey(location)) {
/* 182 */       return this.locationUrlCache.get(location);
/*     */     }
/*     */     
/* 185 */     LOG.debug("Determining location urls for " + location + " using ClassLoader " + this.classLoader + " ...");
/*     */     
/* 187 */     List<URL> locationUrls = new ArrayList<URL>();
/*     */     
/* 189 */     if (this.classLoader.getClass().getName().startsWith("com.ibm")) {
/*     */       
/* 191 */       Enumeration<URL> urls = this.classLoader.getResources(location.getPath() + "/flyway.location");
/* 192 */       if (!urls.hasMoreElements()) {
/* 193 */         LOG.warn("Unable to resolve location " + location + " (ClassLoader: " + this.classLoader + ")" + " On WebSphere an empty file named flyway.location must be present on the classpath location for WebSphere to find it!");
/*     */       }
/*     */       
/* 196 */       while (urls.hasMoreElements()) {
/* 197 */         URL url = urls.nextElement();
/* 198 */         locationUrls.add(new URL(URLDecoder.decode(url.toExternalForm(), "UTF-8").replace("/flyway.location", "")));
/*     */       } 
/*     */     } else {
/* 201 */       Enumeration<URL> urls = this.classLoader.getResources(location.getPath());
/* 202 */       if (!urls.hasMoreElements()) {
/* 203 */         LOG.warn("Unable to resolve location " + location);
/*     */       }
/*     */       
/* 206 */       while (urls.hasMoreElements()) {
/* 207 */         locationUrls.add(urls.nextElement());
/*     */       }
/*     */     } 
/*     */     
/* 211 */     this.locationUrlCache.put(location, locationUrls);
/*     */     
/* 213 */     return locationUrls;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private UrlResolver createUrlResolver(String protocol) {
/* 223 */     if ((new FeatureDetector(this.classLoader)).isJBossVFSv2Available() && protocol.startsWith("vfs")) {
/* 224 */       return (UrlResolver)new JBossVFSv2UrlResolver();
/*     */     }
/*     */     
/* 227 */     return new DefaultUrlResolver();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ClassPathLocationScanner createLocationScanner(String protocol) {
/* 237 */     if (this.locationScannerCache.containsKey(protocol)) {
/* 238 */       return this.locationScannerCache.get(protocol);
/*     */     }
/*     */     
/* 241 */     if ("file".equals(protocol)) {
/* 242 */       FileSystemClassPathLocationScanner locationScanner = new FileSystemClassPathLocationScanner();
/* 243 */       this.locationScannerCache.put(protocol, locationScanner);
/* 244 */       this.resourceNameCache.put(locationScanner, new HashMap<URL, Set<String>>());
/* 245 */       return locationScanner;
/*     */     } 
/*     */     
/* 248 */     if ("jar".equals(protocol) || "zip"
/* 249 */       .equals(protocol) || "wsjar"
/* 250 */       .equals(protocol)) {
/*     */       
/* 252 */       JarFileClassPathLocationScanner locationScanner = new JarFileClassPathLocationScanner();
/* 253 */       this.locationScannerCache.put(protocol, locationScanner);
/* 254 */       this.resourceNameCache.put(locationScanner, new HashMap<URL, Set<String>>());
/* 255 */       return locationScanner;
/*     */     } 
/*     */     
/* 258 */     FeatureDetector featureDetector = new FeatureDetector(this.classLoader);
/* 259 */     if (featureDetector.isJBossVFSv3Available() && "vfs".equals(protocol)) {
/* 260 */       JBossVFSv3ClassPathLocationScanner locationScanner = new JBossVFSv3ClassPathLocationScanner();
/* 261 */       this.locationScannerCache.put(protocol, locationScanner);
/* 262 */       this.resourceNameCache.put(locationScanner, new HashMap<URL, Set<String>>());
/* 263 */       return (ClassPathLocationScanner)locationScanner;
/*     */     } 
/* 265 */     if (featureDetector.isOsgiFrameworkAvailable() && ("bundle"
/* 266 */       .equals(protocol) || "bundleresource"
/* 267 */       .equals(protocol))) {
/*     */       
/* 269 */       OsgiClassPathLocationScanner locationScanner = new OsgiClassPathLocationScanner();
/* 270 */       this.locationScannerCache.put(protocol, locationScanner);
/* 271 */       this.resourceNameCache.put(locationScanner, new HashMap<URL, Set<String>>());
/* 272 */       return locationScanner;
/*     */     } 
/*     */     
/* 275 */     return null;
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
/* 287 */     Set<String> filteredResourceNames = new TreeSet<String>();
/* 288 */     for (String resourceName : resourceNames) {
/* 289 */       String fileName = resourceName.substring(resourceName.lastIndexOf("/") + 1);
/* 290 */       if (fileName.startsWith(prefix) && fileName.endsWith(suffix) && fileName
/* 291 */         .length() > (prefix + suffix).length()) {
/* 292 */         filteredResourceNames.add(resourceName); continue;
/*     */       } 
/* 294 */       LOG.debug("Filtering out resource: " + resourceName + " (filename: " + fileName + ")");
/*     */     } 
/*     */     
/* 297 */     return filteredResourceNames;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\scanner\classpath\ClassPathScanner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */