/*     */ package javax.xml.bind;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.logging.ConsoleHandler;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ 
/*     */ class ContextFinder
/*     */ {
/*  42 */   private static final Logger logger = Logger.getLogger("javax.xml.bind"); static {
/*     */     try {
/*  44 */       if (AccessController.doPrivileged(new GetPropertyAction("jaxb.debug")) != null)
/*     */       {
/*     */         
/*  47 */         logger.setUseParentHandlers(false);
/*  48 */         logger.setLevel(Level.ALL);
/*  49 */         ConsoleHandler handler = new ConsoleHandler();
/*  50 */         handler.setLevel(Level.ALL);
/*  51 */         logger.addHandler(handler);
/*     */       
/*     */       }
/*     */ 
/*     */     
/*     */     }
/*  57 */     catch (Throwable t) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String PLATFORM_DEFAULT_FACTORY_CLASS = "com.sun.xml.bind.v2.ContextFactory";
/*     */ 
/*     */ 
/*     */   
/*     */   private static void handleInvocationTargetException(InvocationTargetException x) throws JAXBException {
/*  68 */     Throwable t = x.getTargetException();
/*  69 */     if (t != null) {
/*  70 */       if (t instanceof JAXBException)
/*     */       {
/*  72 */         throw (JAXBException)t; } 
/*  73 */       if (t instanceof RuntimeException)
/*     */       {
/*  75 */         throw (RuntimeException)t; } 
/*  76 */       if (t instanceof Error) {
/*  77 */         throw (Error)t;
/*     */       }
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static JAXBException handleClassCastException(Class originalType, Class targetType) {
/*  94 */     URL targetTypeURL = which(targetType);
/*     */     
/*  96 */     return new JAXBException(Messages.format("JAXBContext.IllegalCast", originalType.getClassLoader().getResource("javax/xml/bind/JAXBContext.class"), targetTypeURL));
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
/*     */   static JAXBContext newInstance(String contextPath, String className, ClassLoader classLoader, Map properties) throws JAXBException {
/*     */     try {
/*     */       Class<?> spiClass;
/* 114 */       if (classLoader == null) {
/* 115 */         spiClass = Class.forName(className);
/*     */       } else {
/* 117 */         spiClass = classLoader.loadClass(className);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 126 */       Object context = null;
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 131 */         Method m = spiClass.getMethod("createContext", new Class[] { String.class, ClassLoader.class, Map.class });
/*     */         
/* 133 */         context = m.invoke(null, new Object[] { contextPath, classLoader, properties });
/* 134 */       } catch (NoSuchMethodException e) {}
/*     */ 
/*     */ 
/*     */       
/* 138 */       if (context == null) {
/*     */ 
/*     */         
/* 141 */         Method m = spiClass.getMethod("createContext", new Class[] { String.class, ClassLoader.class });
/*     */         
/* 143 */         context = m.invoke(null, new Object[] { contextPath, classLoader });
/*     */       } 
/*     */       
/* 146 */       if (!(context instanceof JAXBContext))
/*     */       {
/* 148 */         handleClassCastException(context.getClass(), JAXBContext.class);
/*     */       }
/* 150 */       return (JAXBContext)context;
/* 151 */     } catch (ClassNotFoundException x) {
/* 152 */       throw new JAXBException(Messages.format("ContextFinder.ProviderNotFound", className), x);
/*     */     
/*     */     }
/* 155 */     catch (InvocationTargetException x) {
/* 156 */       handleInvocationTargetException(x);
/*     */ 
/*     */       
/* 159 */       Throwable e = x;
/* 160 */       if (x.getTargetException() != null) {
/* 161 */         e = x.getTargetException();
/*     */       }
/* 163 */       throw new JAXBException(Messages.format("ContextFinder.CouldNotInstantiate", className, e), e);
/* 164 */     } catch (RuntimeException x) {
/*     */ 
/*     */       
/* 167 */       throw x;
/* 168 */     } catch (Exception x) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 173 */       throw new JAXBException(Messages.format("ContextFinder.CouldNotInstantiate", className, x), x);
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
/*     */   static JAXBContext newInstance(Class[] classes, Map properties, String className) throws JAXBException {
/*     */     Class<?> spi;
/*     */     Method m;
/* 187 */     ClassLoader cl = Thread.currentThread().getContextClassLoader();
/*     */     
/*     */     try {
/* 190 */       logger.fine("Trying to load " + className);
/* 191 */       if (cl != null)
/* 192 */       { spi = cl.loadClass(className); }
/*     */       else
/* 194 */       { spi = Class.forName(className); } 
/* 195 */     } catch (ClassNotFoundException e) {
/* 196 */       throw new JAXBException(e);
/*     */     } 
/*     */     
/* 199 */     if (logger.isLoggable(Level.FINE))
/*     */     {
/* 201 */       logger.fine("loaded " + className + " from " + which(spi));
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 206 */       m = spi.getMethod("createContext", new Class[] { Class[].class, Map.class });
/* 207 */     } catch (NoSuchMethodException e) {
/* 208 */       throw new JAXBException(e);
/*     */     } 
/*     */     try {
/* 211 */       Object context = m.invoke(null, new Object[] { classes, properties });
/* 212 */       if (!(context instanceof JAXBContext))
/*     */       {
/* 214 */         throw handleClassCastException(context.getClass(), JAXBContext.class);
/*     */       }
/* 216 */       return (JAXBContext)context;
/* 217 */     } catch (IllegalAccessException e) {
/* 218 */       throw new JAXBException(e);
/* 219 */     } catch (InvocationTargetException e) {
/* 220 */       handleInvocationTargetException(e);
/*     */       
/* 222 */       Throwable x = e;
/* 223 */       if (e.getTargetException() != null) {
/* 224 */         x = e.getTargetException();
/*     */       }
/* 226 */       throw new JAXBException(x);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static JAXBContext find(String factoryId, String contextPath, ClassLoader classLoader, Map properties) throws JAXBException {
/* 235 */     String jaxbContextFQCN = JAXBContext.class.getName();
/*     */ 
/*     */ 
/*     */     
/* 239 */     StringTokenizer packages = new StringTokenizer(contextPath, ":");
/*     */ 
/*     */     
/* 242 */     if (!packages.hasMoreTokens())
/*     */     {
/* 244 */       throw new JAXBException(Messages.format("ContextFinder.NoPackageInContextPath"));
/*     */     }
/*     */     
/* 247 */     logger.fine("Searching jaxb.properties");
/*     */     
/* 249 */     while (packages.hasMoreTokens()) {
/* 250 */       String packageName = packages.nextToken(":").replace('.', '/');
/*     */       
/* 252 */       StringBuilder propFileName = (new StringBuilder()).append(packageName).append("/jaxb.properties");
/*     */       
/* 254 */       Properties props = loadJAXBProperties(classLoader, propFileName.toString());
/* 255 */       if (props != null) {
/* 256 */         if (props.containsKey(factoryId)) {
/* 257 */           String str = props.getProperty(factoryId);
/* 258 */           return newInstance(contextPath, str, classLoader, properties);
/*     */         } 
/* 260 */         throw new JAXBException(Messages.format("ContextFinder.MissingProperty", packageName, factoryId));
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 265 */     logger.fine("Searching the system property");
/*     */ 
/*     */     
/* 268 */     String factoryClassName = AccessController.<String>doPrivileged(new GetPropertyAction(jaxbContextFQCN));
/* 269 */     if (factoryClassName != null) {
/* 270 */       return newInstance(contextPath, factoryClassName, classLoader, properties);
/*     */     }
/*     */     
/* 273 */     logger.fine("Searching META-INF/services");
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 278 */       StringBuilder resource = (new StringBuilder()).append("META-INF/services/").append(jaxbContextFQCN);
/* 279 */       InputStream resourceStream = classLoader.getResourceAsStream(resource.toString());
/*     */ 
/*     */       
/* 282 */       if (resourceStream != null) {
/* 283 */         BufferedReader r = new BufferedReader(new InputStreamReader(resourceStream, "UTF-8"));
/* 284 */         factoryClassName = r.readLine().trim();
/* 285 */         r.close();
/* 286 */         return newInstance(contextPath, factoryClassName, classLoader, properties);
/*     */       } 
/* 288 */       logger.fine("Unable to load:" + resource.toString());
/*     */     }
/* 290 */     catch (UnsupportedEncodingException e) {
/*     */       
/* 292 */       throw new JAXBException(e);
/* 293 */     } catch (IOException e) {
/* 294 */       throw new JAXBException(e);
/*     */     } 
/*     */ 
/*     */     
/* 298 */     logger.fine("Trying to create the platform default provider");
/* 299 */     return newInstance(contextPath, "com.sun.xml.bind.v2.ContextFactory", classLoader, properties);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static JAXBContext find(Class[] classes, Map properties) throws JAXBException {
/* 307 */     String jaxbContextFQCN = JAXBContext.class.getName();
/*     */ 
/*     */ 
/*     */     
/* 311 */     for (Class c : classes) {
/*     */       
/* 313 */       ClassLoader classLoader = AccessController.<ClassLoader>doPrivileged(new PrivilegedAction<ClassLoader>() {
/*     */             public ClassLoader run() {
/* 315 */               return c.getClassLoader();
/*     */             }
/*     */           });
/* 318 */       Package pkg = c.getPackage();
/* 319 */       if (pkg != null) {
/*     */         
/* 321 */         String packageName = pkg.getName().replace('.', '/');
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 330 */         String resourceName = packageName + "/jaxb.properties";
/* 331 */         logger.fine("Trying to locate " + resourceName);
/* 332 */         Properties props = loadJAXBProperties(classLoader, resourceName);
/* 333 */         if (props == null) {
/* 334 */           logger.fine("  not found");
/*     */         } else {
/* 336 */           logger.fine("  found");
/* 337 */           if (props.containsKey("javax.xml.bind.context.factory")) {
/*     */             
/* 339 */             String str = props.getProperty("javax.xml.bind.context.factory").trim();
/* 340 */             return newInstance(classes, properties, str);
/*     */           } 
/* 342 */           throw new JAXBException(Messages.format("ContextFinder.MissingProperty", packageName, "javax.xml.bind.context.factory"));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 348 */     logger.fine("Checking system property " + jaxbContextFQCN);
/* 349 */     String factoryClassName = AccessController.<String>doPrivileged(new GetPropertyAction(jaxbContextFQCN));
/* 350 */     if (factoryClassName != null) {
/* 351 */       logger.fine("  found " + factoryClassName);
/* 352 */       return newInstance(classes, properties, factoryClassName);
/*     */     } 
/* 354 */     logger.fine("  not found");
/*     */ 
/*     */     
/* 357 */     logger.fine("Checking META-INF/services");
/*     */     try {
/*     */       URL resourceURL;
/* 360 */       String resource = "META-INF/services/" + jaxbContextFQCN;
/* 361 */       ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
/*     */       
/* 363 */       if (classLoader == null) {
/* 364 */         resourceURL = ClassLoader.getSystemResource(resource);
/*     */       } else {
/* 366 */         resourceURL = classLoader.getResource(resource);
/*     */       } 
/* 368 */       if (resourceURL != null) {
/* 369 */         logger.fine("Reading " + resourceURL);
/* 370 */         BufferedReader r = new BufferedReader(new InputStreamReader(resourceURL.openStream(), "UTF-8"));
/* 371 */         factoryClassName = r.readLine().trim();
/* 372 */         return newInstance(classes, properties, factoryClassName);
/*     */       } 
/* 374 */       logger.fine("Unable to find: " + resource);
/*     */     }
/* 376 */     catch (UnsupportedEncodingException e) {
/*     */       
/* 378 */       throw new JAXBException(e);
/* 379 */     } catch (IOException e) {
/* 380 */       throw new JAXBException(e);
/*     */     } 
/*     */ 
/*     */     
/* 384 */     logger.fine("Trying to create the platform default provider");
/* 385 */     return newInstance(classes, properties, "com.sun.xml.bind.v2.ContextFactory");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Properties loadJAXBProperties(ClassLoader classLoader, String propFileName) throws JAXBException {
/* 393 */     Properties props = null;
/*     */     
/*     */     try {
/*     */       URL url;
/* 397 */       if (classLoader == null) {
/* 398 */         url = ClassLoader.getSystemResource(propFileName);
/*     */       } else {
/* 400 */         url = classLoader.getResource(propFileName);
/*     */       } 
/* 402 */       if (url != null) {
/* 403 */         logger.fine("loading props from " + url);
/* 404 */         props = new Properties();
/* 405 */         InputStream is = url.openStream();
/* 406 */         props.load(is);
/* 407 */         is.close();
/*     */       } 
/* 409 */     } catch (IOException ioe) {
/* 410 */       logger.log(Level.FINE, "Unable to load " + propFileName, ioe);
/* 411 */       throw new JAXBException(ioe.toString(), ioe);
/*     */     } 
/*     */     
/* 414 */     return props;
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
/*     */   static URL which(Class clazz, ClassLoader loader) {
/* 432 */     String classnameAsResource = clazz.getName().replace('.', '/') + ".class";
/*     */     
/* 434 */     if (loader == null) {
/* 435 */       loader = ClassLoader.getSystemClassLoader();
/*     */     }
/*     */     
/* 438 */     return loader.getResource(classnameAsResource);
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
/*     */   static URL which(Class clazz) {
/* 454 */     return which(clazz, clazz.getClassLoader());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bind\ContextFinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */