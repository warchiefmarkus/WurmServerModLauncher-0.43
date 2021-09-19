/*     */ package javax.xml.stream;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.Properties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class FactoryFinder
/*     */ {
/*     */   private static boolean debug = false;
/*     */   
/*     */   static {
/*     */     try {
/*  19 */       debug = (System.getProperty("xml.stream.debug") != null);
/*  20 */     } catch (Exception x) {}
/*     */   }
/*     */ 
/*     */   
/*     */   private static void debugPrintln(String msg) {
/*  25 */     if (debug) {
/*  26 */       System.err.println("STREAM: " + msg);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ClassLoader findClassLoader() throws FactoryConfigurationError {
/*     */     ClassLoader classLoader;
/*     */     try {
/*  36 */       Class<?> clazz = Class.forName(FactoryFinder.class.getName() + "$ClassLoaderFinderConcrete");
/*     */       
/*  38 */       ClassLoaderFinder clf = (ClassLoaderFinder)clazz.newInstance();
/*  39 */       classLoader = clf.getContextClassLoader();
/*  40 */     } catch (LinkageError le) {
/*     */       
/*  42 */       classLoader = FactoryFinder.class.getClassLoader();
/*  43 */     } catch (ClassNotFoundException x) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  48 */       classLoader = FactoryFinder.class.getClassLoader();
/*  49 */     } catch (Exception x) {
/*     */       
/*  51 */       throw new FactoryConfigurationError(x.toString(), x);
/*     */     } 
/*  53 */     return classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Object newInstance(String className, ClassLoader classLoader) throws FactoryConfigurationError {
/*     */     try {
/*     */       Class<?> spiClass;
/*  65 */       if (classLoader == null) {
/*  66 */         spiClass = Class.forName(className);
/*     */       } else {
/*  68 */         spiClass = classLoader.loadClass(className);
/*     */       } 
/*  70 */       return spiClass.newInstance();
/*  71 */     } catch (ClassNotFoundException x) {
/*  72 */       throw new FactoryConfigurationError("Provider " + className + " not found", x);
/*     */     }
/*  74 */     catch (Exception x) {
/*  75 */       throw new FactoryConfigurationError("Provider " + className + " could not be instantiated: " + x, x);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Object find(String factoryId) throws FactoryConfigurationError {
/*  84 */     return find(factoryId, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Object find(String factoryId, String fallbackClassName) throws FactoryConfigurationError {
/*  91 */     ClassLoader classLoader = findClassLoader();
/*  92 */     return find(factoryId, fallbackClassName, classLoader);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Object find(String factoryId, String fallbackClassName, ClassLoader classLoader) throws FactoryConfigurationError {
/*     */     try {
/* 116 */       String systemProp = System.getProperty(factoryId);
/*     */       
/* 118 */       if (systemProp != null) {
/* 119 */         debugPrintln("found system property" + systemProp);
/* 120 */         return newInstance(systemProp, classLoader);
/*     */       } 
/* 122 */     } catch (SecurityException se) {}
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 127 */       String javah = System.getProperty("java.home");
/* 128 */       String configFile = javah + File.separator + "lib" + File.separator + "jaxp.properties";
/*     */       
/* 130 */       File f = new File(configFile);
/* 131 */       if (f.exists()) {
/* 132 */         Properties props = new Properties();
/* 133 */         props.load(new FileInputStream(f));
/* 134 */         String factoryClassName = props.getProperty(factoryId);
/* 135 */         debugPrintln("found java.home property " + factoryClassName);
/* 136 */         return newInstance(factoryClassName, classLoader);
/*     */       } 
/* 138 */     } catch (Exception ex) {
/* 139 */       if (debug) ex.printStackTrace();
/*     */     
/*     */     } 
/* 142 */     String serviceId = "META-INF/services/" + factoryId;
/*     */     
/*     */     try {
/* 145 */       InputStream is = null;
/* 146 */       if (classLoader == null) {
/* 147 */         is = ClassLoader.getSystemResourceAsStream(serviceId);
/*     */       } else {
/* 149 */         is = classLoader.getResourceAsStream(serviceId);
/*     */       } 
/*     */       
/* 152 */       if (is != null) {
/* 153 */         debugPrintln("found " + serviceId);
/* 154 */         BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
/*     */ 
/*     */         
/* 157 */         String factoryClassName = rd.readLine();
/* 158 */         rd.close();
/*     */         
/* 160 */         if (factoryClassName != null && !"".equals(factoryClassName)) {
/*     */           
/* 162 */           debugPrintln("loaded from services: " + factoryClassName);
/* 163 */           return newInstance(factoryClassName, classLoader);
/*     */         } 
/*     */       } 
/* 166 */     } catch (Exception ex) {
/* 167 */       if (debug) ex.printStackTrace();
/*     */     
/*     */     } 
/* 170 */     if (fallbackClassName == null) {
/* 171 */       throw new FactoryConfigurationError("Provider for " + factoryId + " cannot be found", null);
/*     */     }
/*     */ 
/*     */     
/* 175 */     debugPrintln("loaded from fallback value: " + fallbackClassName);
/* 176 */     return newInstance(fallbackClassName, classLoader);
/*     */   }
/*     */   
/*     */   private static abstract class ClassLoaderFinder
/*     */   {
/*     */     private ClassLoaderFinder() {}
/*     */     
/*     */     abstract ClassLoader getContextClassLoader();
/*     */   }
/*     */   
/*     */   static class ClassLoaderFinderConcrete
/*     */     extends ClassLoaderFinder
/*     */   {
/*     */     ClassLoader getContextClassLoader() {
/* 190 */       return Thread.currentThread().getContextClassLoader();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\stream\FactoryFinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */