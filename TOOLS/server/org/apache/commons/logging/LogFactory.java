/*      */ package org.apache.commons.logging;
/*      */ 
/*      */ import java.io.BufferedReader;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.PrintStream;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.net.URL;
/*      */ import java.net.URLConnection;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Properties;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class LogFactory
/*      */ {
/*      */   public static final String PRIORITY_KEY = "priority";
/*      */   public static final String TCCL_KEY = "use_tccl";
/*      */   public static final String FACTORY_PROPERTY = "org.apache.commons.logging.LogFactory";
/*      */   public static final String FACTORY_DEFAULT = "org.apache.commons.logging.impl.LogFactoryImpl";
/*      */   public static final String FACTORY_PROPERTIES = "commons-logging.properties";
/*      */   protected static final String SERVICE_ID = "META-INF/services/org.apache.commons.logging.LogFactory";
/*      */   public static final String DIAGNOSTICS_DEST_PROPERTY = "org.apache.commons.logging.diagnostics.dest";
/*      */   
/*      */   static {
/*      */     String str;
/*      */   }
/*      */   
/*  138 */   private static PrintStream diagnosticsStream = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String diagnosticPrefix;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String HASHTABLE_IMPLEMENTATION_PROPERTY = "org.apache.commons.logging.LogFactory.HashtableImpl";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String WEAK_HASHTABLE_CLASSNAME = "org.apache.commons.logging.impl.WeakHashtable";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final ClassLoader thisClassLoader;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  284 */   protected static Hashtable factories = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  301 */   protected static volatile LogFactory nullClassLoaderFactory = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final Hashtable createFactoryStore() {
/*      */     String str;
/*  319 */     Hashtable result = null;
/*      */     
/*      */     try {
/*  322 */       str = getSystemProperty("org.apache.commons.logging.LogFactory.HashtableImpl", null);
/*  323 */     } catch (SecurityException ex) {
/*      */ 
/*      */       
/*  326 */       str = null;
/*      */     } 
/*      */     
/*  329 */     if (str == null) {
/*  330 */       str = "org.apache.commons.logging.impl.WeakHashtable";
/*      */     }
/*      */     try {
/*  333 */       Class implementationClass = Class.forName(str);
/*  334 */       result = (Hashtable)implementationClass.newInstance();
/*  335 */     } catch (Throwable t) {
/*  336 */       handleThrowable(t);
/*      */ 
/*      */       
/*  339 */       if (!"org.apache.commons.logging.impl.WeakHashtable".equals(str))
/*      */       {
/*  341 */         if (isDiagnosticsEnabled()) {
/*      */           
/*  343 */           logDiagnostic("[ERROR] LogFactory: Load of custom hashtable failed");
/*      */         }
/*      */         else {
/*      */           
/*  347 */           System.err.println("[ERROR] LogFactory: Load of custom hashtable failed");
/*      */         } 
/*      */       }
/*      */     } 
/*  351 */     if (result == null) {
/*  352 */       result = new Hashtable();
/*      */     }
/*  354 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String trim(String src) {
/*  361 */     if (src == null) {
/*  362 */       return null;
/*      */     }
/*  364 */     return src.trim();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static void handleThrowable(Throwable t) {
/*  380 */     if (t instanceof ThreadDeath) {
/*  381 */       throw (ThreadDeath)t;
/*      */     }
/*  383 */     if (t instanceof VirtualMachineError) {
/*  384 */       throw (VirtualMachineError)t;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static LogFactory getFactory() throws LogConfigurationException {
/*  421 */     ClassLoader contextClassLoader = getContextClassLoaderInternal();
/*      */     
/*  423 */     if (contextClassLoader == null)
/*      */     {
/*      */ 
/*      */       
/*  427 */       if (isDiagnosticsEnabled()) {
/*  428 */         logDiagnostic("Context classloader is null.");
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*  433 */     LogFactory factory = getCachedFactory(contextClassLoader);
/*  434 */     if (factory != null) {
/*  435 */       return factory;
/*      */     }
/*      */     
/*  438 */     if (isDiagnosticsEnabled()) {
/*  439 */       logDiagnostic("[LOOKUP] LogFactory implementation requested for the first time for context classloader " + objectId(contextClassLoader));
/*      */ 
/*      */       
/*  442 */       logHierarchy("[LOOKUP] ", contextClassLoader);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  455 */     Properties props = getConfigurationFile(contextClassLoader, "commons-logging.properties");
/*      */ 
/*      */ 
/*      */     
/*  459 */     ClassLoader baseClassLoader = contextClassLoader;
/*  460 */     if (props != null) {
/*  461 */       String useTCCLStr = props.getProperty("use_tccl");
/*  462 */       if (useTCCLStr != null)
/*      */       {
/*      */         
/*  465 */         if (!Boolean.valueOf(useTCCLStr).booleanValue())
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  473 */           baseClassLoader = thisClassLoader;
/*      */         }
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  480 */     if (isDiagnosticsEnabled()) {
/*  481 */       logDiagnostic("[LOOKUP] Looking for system property [org.apache.commons.logging.LogFactory] to define the LogFactory subclass to use...");
/*      */     }
/*      */ 
/*      */     
/*      */     try {
/*  486 */       String factoryClass = getSystemProperty("org.apache.commons.logging.LogFactory", null);
/*  487 */       if (factoryClass != null) {
/*  488 */         if (isDiagnosticsEnabled()) {
/*  489 */           logDiagnostic("[LOOKUP] Creating an instance of LogFactory class '" + factoryClass + "' as specified by system property " + "org.apache.commons.logging.LogFactory");
/*      */         }
/*      */         
/*  492 */         factory = newFactory(factoryClass, baseClassLoader, contextClassLoader);
/*      */       }
/*  494 */       else if (isDiagnosticsEnabled()) {
/*  495 */         logDiagnostic("[LOOKUP] No system property [org.apache.commons.logging.LogFactory] defined.");
/*      */       }
/*      */     
/*  498 */     } catch (SecurityException e) {
/*  499 */       if (isDiagnosticsEnabled()) {
/*  500 */         logDiagnostic("[LOOKUP] A security exception occurred while trying to create an instance of the custom factory class: [" + trim(e.getMessage()) + "]. Trying alternative implementations...");
/*      */       
/*      */       }
/*      */     
/*      */     }
/*  505 */     catch (RuntimeException e) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  511 */       if (isDiagnosticsEnabled()) {
/*  512 */         logDiagnostic("[LOOKUP] An exception occurred while trying to create an instance of the custom factory class: [" + trim(e.getMessage()) + "] as specified by a system property.");
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  517 */       throw e;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  526 */     if (factory == null) {
/*  527 */       if (isDiagnosticsEnabled()) {
/*  528 */         logDiagnostic("[LOOKUP] Looking for a resource file of name [META-INF/services/org.apache.commons.logging.LogFactory] to define the LogFactory subclass to use...");
/*      */       }
/*      */       
/*      */       try {
/*  532 */         InputStream is = getResourceAsStream(contextClassLoader, "META-INF/services/org.apache.commons.logging.LogFactory");
/*      */         
/*  534 */         if (is != null) {
/*      */           BufferedReader bufferedReader;
/*      */ 
/*      */           
/*      */           try {
/*  539 */             bufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
/*  540 */           } catch (UnsupportedEncodingException e) {
/*  541 */             bufferedReader = new BufferedReader(new InputStreamReader(is));
/*      */           } 
/*      */           
/*  544 */           String factoryClassName = bufferedReader.readLine();
/*  545 */           bufferedReader.close();
/*      */           
/*  547 */           if (factoryClassName != null && !"".equals(factoryClassName)) {
/*  548 */             if (isDiagnosticsEnabled()) {
/*  549 */               logDiagnostic("[LOOKUP]  Creating an instance of LogFactory class " + factoryClassName + " as specified by file '" + "META-INF/services/org.apache.commons.logging.LogFactory" + "' which was present in the path of the context classloader.");
/*      */             }
/*      */ 
/*      */ 
/*      */             
/*  554 */             factory = newFactory(factoryClassName, baseClassLoader, contextClassLoader);
/*      */           }
/*      */         
/*      */         }
/*  558 */         else if (isDiagnosticsEnabled()) {
/*  559 */           logDiagnostic("[LOOKUP] No resource file with name 'META-INF/services/org.apache.commons.logging.LogFactory' found.");
/*      */         }
/*      */       
/*  562 */       } catch (Exception ex) {
/*      */ 
/*      */ 
/*      */         
/*  566 */         if (isDiagnosticsEnabled()) {
/*  567 */           logDiagnostic("[LOOKUP] A security exception occurred while trying to create an instance of the custom factory class: [" + trim(ex.getMessage()) + "]. Trying alternative implementations...");
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  579 */     if (factory == null) {
/*  580 */       if (props != null) {
/*  581 */         if (isDiagnosticsEnabled()) {
/*  582 */           logDiagnostic("[LOOKUP] Looking in properties file for entry with key 'org.apache.commons.logging.LogFactory' to define the LogFactory subclass to use...");
/*      */         }
/*      */ 
/*      */         
/*  586 */         String factoryClass = props.getProperty("org.apache.commons.logging.LogFactory");
/*  587 */         if (factoryClass != null) {
/*  588 */           if (isDiagnosticsEnabled()) {
/*  589 */             logDiagnostic("[LOOKUP] Properties file specifies LogFactory subclass '" + factoryClass + "'");
/*      */           }
/*      */           
/*  592 */           factory = newFactory(factoryClass, baseClassLoader, contextClassLoader);
/*      */ 
/*      */         
/*      */         }
/*  596 */         else if (isDiagnosticsEnabled()) {
/*  597 */           logDiagnostic("[LOOKUP] Properties file has no entry specifying LogFactory subclass.");
/*      */         }
/*      */       
/*      */       }
/*  601 */       else if (isDiagnosticsEnabled()) {
/*  602 */         logDiagnostic("[LOOKUP] No properties file available to determine LogFactory subclass from..");
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  609 */     if (factory == null) {
/*  610 */       if (isDiagnosticsEnabled()) {
/*  611 */         logDiagnostic("[LOOKUP] Loading the default LogFactory implementation 'org.apache.commons.logging.impl.LogFactoryImpl' via the same classloader that loaded this LogFactory class (ie not looking in the context classloader).");
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  626 */       factory = newFactory("org.apache.commons.logging.impl.LogFactoryImpl", thisClassLoader, contextClassLoader);
/*      */     } 
/*      */     
/*  629 */     if (factory != null) {
/*      */ 
/*      */ 
/*      */       
/*  633 */       cacheFactory(contextClassLoader, factory);
/*      */       
/*  635 */       if (props != null) {
/*  636 */         Enumeration names = props.propertyNames();
/*  637 */         while (names.hasMoreElements()) {
/*  638 */           String name = (String)names.nextElement();
/*  639 */           String value = props.getProperty(name);
/*  640 */           factory.setAttribute(name, value);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  645 */     return factory;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Log getLog(Class clazz) throws LogConfigurationException {
/*  657 */     return getFactory().getInstance(clazz);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Log getLog(String name) throws LogConfigurationException {
/*  671 */     return getFactory().getInstance(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void release(ClassLoader classLoader) {
/*  683 */     if (isDiagnosticsEnabled()) {
/*  684 */       logDiagnostic("Releasing factory for classloader " + objectId(classLoader));
/*      */     }
/*      */     
/*  687 */     Hashtable factories = LogFactory.factories;
/*  688 */     synchronized (factories) {
/*  689 */       if (classLoader == null) {
/*  690 */         if (nullClassLoaderFactory != null) {
/*  691 */           nullClassLoaderFactory.release();
/*  692 */           nullClassLoaderFactory = null;
/*      */         } 
/*      */       } else {
/*  695 */         LogFactory factory = (LogFactory)factories.get(classLoader);
/*  696 */         if (factory != null) {
/*  697 */           factory.release();
/*  698 */           factories.remove(classLoader);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void releaseAll() {
/*  713 */     if (isDiagnosticsEnabled()) {
/*  714 */       logDiagnostic("Releasing factory for all classloaders.");
/*      */     }
/*      */     
/*  717 */     Hashtable factories = LogFactory.factories;
/*  718 */     synchronized (factories) {
/*  719 */       Enumeration elements = factories.elements();
/*  720 */       while (elements.hasMoreElements()) {
/*  721 */         LogFactory element = elements.nextElement();
/*  722 */         element.release();
/*      */       } 
/*  724 */       factories.clear();
/*      */       
/*  726 */       if (nullClassLoaderFactory != null) {
/*  727 */         nullClassLoaderFactory.release();
/*  728 */         nullClassLoaderFactory = null;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static ClassLoader getClassLoader(Class clazz) {
/*      */     try {
/*  764 */       return clazz.getClassLoader();
/*  765 */     } catch (SecurityException ex) {
/*  766 */       if (isDiagnosticsEnabled()) {
/*  767 */         logDiagnostic("Unable to get classloader for class '" + clazz + "' due to security restrictions - " + ex.getMessage());
/*      */       }
/*      */       
/*  770 */       throw ex;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static ClassLoader getContextClassLoader() throws LogConfigurationException {
/*  794 */     return directGetContextClassLoader();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static ClassLoader getContextClassLoaderInternal() throws LogConfigurationException {
/*  814 */     return AccessController.<ClassLoader>doPrivileged(new PrivilegedAction()
/*      */         {
/*      */           public Object run() {
/*  817 */             return LogFactory.directGetContextClassLoader();
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static ClassLoader directGetContextClassLoader() throws LogConfigurationException {
/*  843 */     ClassLoader classLoader = null;
/*      */ 
/*      */     
/*      */     try {
/*  847 */       Method method = Thread.class.getMethod("getContextClassLoader", (Class[])null);
/*      */ 
/*      */       
/*      */       try {
/*  851 */         classLoader = (ClassLoader)method.invoke(Thread.currentThread(), (Object[])null);
/*  852 */       } catch (IllegalAccessException e) {
/*  853 */         throw new LogConfigurationException("Unexpected IllegalAccessException", e);
/*      */       }
/*  855 */       catch (InvocationTargetException e) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  872 */         if (!(e.getTargetException() instanceof SecurityException))
/*      */         {
/*      */ 
/*      */ 
/*      */           
/*  877 */           throw new LogConfigurationException("Unexpected InvocationTargetException", e.getTargetException());
/*      */         }
/*      */       } 
/*  880 */     } catch (NoSuchMethodException e) {
/*      */       
/*  882 */       classLoader = getClassLoader(LogFactory.class);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  898 */     return classLoader;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static LogFactory getCachedFactory(ClassLoader contextClassLoader) {
/*  916 */     if (contextClassLoader == null)
/*      */     {
/*      */ 
/*      */ 
/*      */       
/*  921 */       return nullClassLoaderFactory;
/*      */     }
/*  923 */     return (LogFactory)factories.get(contextClassLoader);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void cacheFactory(ClassLoader classLoader, LogFactory factory) {
/*  940 */     if (factory != null) {
/*  941 */       if (classLoader == null) {
/*  942 */         nullClassLoaderFactory = factory;
/*      */       } else {
/*  944 */         factories.put(classLoader, factory);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static LogFactory newFactory(String factoryClass, ClassLoader classLoader, ClassLoader contextClassLoader) throws LogConfigurationException {
/* 1000 */     Object result = AccessController.doPrivileged(new PrivilegedAction(factoryClass, classLoader) { private final String val$factoryClass;
/*      */           
/*      */           public Object run() {
/* 1003 */             return LogFactory.createFactory(this.val$factoryClass, this.val$classLoader);
/*      */           }
/*      */           private final ClassLoader val$classLoader; }
/*      */       );
/* 1007 */     if (result instanceof LogConfigurationException) {
/* 1008 */       LogConfigurationException ex = (LogConfigurationException)result;
/* 1009 */       if (isDiagnosticsEnabled()) {
/* 1010 */         logDiagnostic("An error occurred while loading the factory class:" + ex.getMessage());
/*      */       }
/* 1012 */       throw ex;
/*      */     } 
/* 1014 */     if (isDiagnosticsEnabled()) {
/* 1015 */       logDiagnostic("Created object " + objectId(result) + " to manage classloader " + objectId(contextClassLoader));
/*      */     }
/*      */     
/* 1018 */     return (LogFactory)result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static LogFactory newFactory(String factoryClass, ClassLoader classLoader) {
/* 1038 */     return newFactory(factoryClass, classLoader, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static Object createFactory(String factoryClass, ClassLoader classLoader) {
/* 1055 */     Class logFactoryClass = null;
/*      */     try {
/* 1057 */       if (classLoader != null) {
/*      */         
/*      */         try {
/*      */ 
/*      */ 
/*      */           
/* 1063 */           logFactoryClass = classLoader.loadClass(factoryClass);
/* 1064 */           if (LogFactory.class.isAssignableFrom(logFactoryClass)) {
/* 1065 */             if (isDiagnosticsEnabled()) {
/* 1066 */               logDiagnostic("Loaded class " + logFactoryClass.getName() + " from classloader " + objectId(classLoader));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           }
/* 1081 */           else if (isDiagnosticsEnabled()) {
/* 1082 */             logDiagnostic("Factory class " + logFactoryClass.getName() + " loaded from classloader " + objectId(logFactoryClass.getClassLoader()) + " does not extend '" + LogFactory.class.getName() + "' as loaded by this classloader.");
/*      */ 
/*      */ 
/*      */             
/* 1086 */             logHierarchy("[BAD CL TREE] ", classLoader);
/*      */           } 
/*      */ 
/*      */           
/* 1090 */           return logFactoryClass.newInstance();
/*      */         }
/* 1092 */         catch (ClassNotFoundException ex) {
/* 1093 */           if (classLoader == thisClassLoader)
/*      */           {
/* 1095 */             if (isDiagnosticsEnabled()) {
/* 1096 */               logDiagnostic("Unable to locate any class called '" + factoryClass + "' via classloader " + objectId(classLoader));
/*      */             }
/*      */             
/* 1099 */             throw ex;
/*      */           }
/*      */         
/* 1102 */         } catch (NoClassDefFoundError e) {
/* 1103 */           if (classLoader == thisClassLoader)
/*      */           {
/* 1105 */             if (isDiagnosticsEnabled()) {
/* 1106 */               logDiagnostic("Class '" + factoryClass + "' cannot be loaded" + " via classloader " + objectId(classLoader) + " - it depends on some other class that cannot be found.");
/*      */             }
/*      */ 
/*      */             
/* 1110 */             throw e;
/*      */           }
/*      */         
/* 1113 */         } catch (ClassCastException e) {
/* 1114 */           if (classLoader == thisClassLoader) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1120 */             boolean implementsLogFactory = implementsLogFactory(logFactoryClass);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1127 */             StringBuffer msg = new StringBuffer();
/* 1128 */             msg.append("The application has specified that a custom LogFactory implementation ");
/* 1129 */             msg.append("should be used but Class '");
/* 1130 */             msg.append(factoryClass);
/* 1131 */             msg.append("' cannot be converted to '");
/* 1132 */             msg.append(LogFactory.class.getName());
/* 1133 */             msg.append("'. ");
/* 1134 */             if (implementsLogFactory) {
/* 1135 */               msg.append("The conflict is caused by the presence of multiple LogFactory classes ");
/* 1136 */               msg.append("in incompatible classloaders. ");
/* 1137 */               msg.append("Background can be found in http://commons.apache.org/logging/tech.html. ");
/* 1138 */               msg.append("If you have not explicitly specified a custom LogFactory then it is likely ");
/* 1139 */               msg.append("that the container has set one without your knowledge. ");
/* 1140 */               msg.append("In this case, consider using the commons-logging-adapters.jar file or ");
/* 1141 */               msg.append("specifying the standard LogFactory from the command line. ");
/*      */             } else {
/* 1143 */               msg.append("Please check the custom implementation. ");
/*      */             } 
/* 1145 */             msg.append("Help can be found @http://commons.apache.org/logging/troubleshooting.html.");
/*      */             
/* 1147 */             if (isDiagnosticsEnabled()) {
/* 1148 */               logDiagnostic(msg.toString());
/*      */             }
/*      */             
/* 1151 */             throw new ClassCastException(msg.toString());
/*      */           } 
/*      */         } 
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1185 */       if (isDiagnosticsEnabled()) {
/* 1186 */         logDiagnostic("Unable to load factory class via classloader " + objectId(classLoader) + " - trying the classloader associated with this LogFactory.");
/*      */       }
/*      */       
/* 1189 */       logFactoryClass = Class.forName(factoryClass);
/* 1190 */       return logFactoryClass.newInstance();
/* 1191 */     } catch (Exception e) {
/*      */       
/* 1193 */       if (isDiagnosticsEnabled()) {
/* 1194 */         logDiagnostic("Unable to create LogFactory instance.");
/*      */       }
/* 1196 */       if (logFactoryClass != null && !LogFactory.class.isAssignableFrom(logFactoryClass)) {
/* 1197 */         return new LogConfigurationException("The chosen LogFactory implementation does not extend LogFactory. Please check your configuration.", e);
/*      */       }
/*      */ 
/*      */       
/* 1201 */       return new LogConfigurationException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean implementsLogFactory(Class logFactoryClass) {
/* 1218 */     boolean implementsLogFactory = false;
/* 1219 */     if (logFactoryClass != null) {
/*      */       try {
/* 1221 */         ClassLoader logFactoryClassLoader = logFactoryClass.getClassLoader();
/* 1222 */         if (logFactoryClassLoader == null) {
/* 1223 */           logDiagnostic("[CUSTOM LOG FACTORY] was loaded by the boot classloader");
/*      */         } else {
/* 1225 */           logHierarchy("[CUSTOM LOG FACTORY] ", logFactoryClassLoader);
/* 1226 */           Class factoryFromCustomLoader = Class.forName("org.apache.commons.logging.LogFactory", false, logFactoryClassLoader);
/*      */           
/* 1228 */           implementsLogFactory = factoryFromCustomLoader.isAssignableFrom(logFactoryClass);
/* 1229 */           if (implementsLogFactory) {
/* 1230 */             logDiagnostic("[CUSTOM LOG FACTORY] " + logFactoryClass.getName() + " implements LogFactory but was loaded by an incompatible classloader.");
/*      */           } else {
/*      */             
/* 1233 */             logDiagnostic("[CUSTOM LOG FACTORY] " + logFactoryClass.getName() + " does not implement LogFactory.");
/*      */           }
/*      */         
/*      */         } 
/* 1237 */       } catch (SecurityException e) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1243 */         logDiagnostic("[CUSTOM LOG FACTORY] SecurityException thrown whilst trying to determine whether the compatibility was caused by a classloader conflict: " + e.getMessage());
/*      */       }
/* 1245 */       catch (LinkageError e) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1252 */         logDiagnostic("[CUSTOM LOG FACTORY] LinkageError thrown whilst trying to determine whether the compatibility was caused by a classloader conflict: " + e.getMessage());
/*      */       }
/* 1254 */       catch (ClassNotFoundException e) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1262 */         logDiagnostic("[CUSTOM LOG FACTORY] LogFactory class cannot be loaded by classloader which loaded the custom LogFactory implementation. Is the custom factory in the right classloader?");
/*      */       } 
/*      */     }
/*      */     
/* 1266 */     return implementsLogFactory;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static InputStream getResourceAsStream(ClassLoader loader, String name) {
/* 1276 */     return AccessController.<InputStream>doPrivileged(new PrivilegedAction(loader, name) { private final ClassLoader val$loader; private final String val$name;
/*      */           
/*      */           public Object run() {
/* 1279 */             if (this.val$loader != null) {
/* 1280 */               return this.val$loader.getResourceAsStream(this.val$name);
/*      */             }
/* 1282 */             return ClassLoader.getSystemResourceAsStream(this.val$name);
/*      */           } }
/*      */       );
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Enumeration getResources(ClassLoader loader, String name) {
/* 1302 */     PrivilegedAction action = new PrivilegedAction(loader, name) { private final ClassLoader val$loader; private final String val$name;
/*      */         
/*      */         public Object run() {
/*      */           try {
/* 1306 */             if (this.val$loader != null) {
/* 1307 */               return this.val$loader.getResources(this.val$name);
/*      */             }
/* 1309 */             return ClassLoader.getSystemResources(this.val$name);
/*      */           }
/* 1311 */           catch (IOException e) {
/* 1312 */             if (LogFactory.isDiagnosticsEnabled()) {
/* 1313 */               LogFactory.logDiagnostic("Exception while trying to find configuration file " + this.val$name + ":" + e.getMessage());
/*      */             }
/*      */             
/* 1316 */             return null;
/* 1317 */           } catch (NoSuchMethodError e) {
/*      */ 
/*      */ 
/*      */             
/* 1321 */             return null;
/*      */           } 
/*      */         } }
/*      */       ;
/* 1325 */     Object result = AccessController.doPrivileged(action);
/* 1326 */     return (Enumeration)result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Properties getProperties(URL url) {
/* 1338 */     PrivilegedAction action = new PrivilegedAction(url) { private final URL val$url;
/*      */         
/*      */         public Object run() {
/* 1341 */           InputStream stream = null;
/*      */ 
/*      */ 
/*      */           
/*      */           try {
/* 1346 */             URLConnection connection = this.val$url.openConnection();
/* 1347 */             connection.setUseCaches(false);
/* 1348 */             stream = connection.getInputStream();
/* 1349 */             if (stream != null) {
/* 1350 */               Properties props = new Properties();
/* 1351 */               props.load(stream);
/* 1352 */               stream.close();
/* 1353 */               stream = null;
/* 1354 */               return props;
/*      */             } 
/* 1356 */           } catch (IOException e) {
/* 1357 */             if (LogFactory.isDiagnosticsEnabled()) {
/* 1358 */               LogFactory.logDiagnostic("Unable to read URL " + this.val$url);
/*      */             }
/*      */           } finally {
/* 1361 */             if (stream != null) {
/*      */               try {
/* 1363 */                 stream.close();
/* 1364 */               } catch (IOException e) {
/*      */                 
/* 1366 */                 if (LogFactory.isDiagnosticsEnabled()) {
/* 1367 */                   LogFactory.logDiagnostic("Unable to close stream for URL " + this.val$url);
/*      */                 }
/*      */               } 
/*      */             }
/*      */           } 
/*      */           
/* 1373 */           return null;
/*      */         } }
/*      */       ;
/* 1376 */     return AccessController.<Properties>doPrivileged(action);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final Properties getConfigurationFile(ClassLoader classLoader, String fileName) {
/* 1399 */     Properties props = null;
/* 1400 */     double priority = 0.0D;
/* 1401 */     URL propsUrl = null;
/*      */     try {
/* 1403 */       Enumeration urls = getResources(classLoader, fileName);
/*      */       
/* 1405 */       if (urls == null) {
/* 1406 */         return null;
/*      */       }
/*      */       
/* 1409 */       while (urls.hasMoreElements()) {
/* 1410 */         URL url = urls.nextElement();
/*      */         
/* 1412 */         Properties newProps = getProperties(url);
/* 1413 */         if (newProps != null) {
/* 1414 */           if (props == null) {
/* 1415 */             propsUrl = url;
/* 1416 */             props = newProps;
/* 1417 */             String priorityStr = props.getProperty("priority");
/* 1418 */             priority = 0.0D;
/* 1419 */             if (priorityStr != null) {
/* 1420 */               priority = Double.parseDouble(priorityStr);
/*      */             }
/*      */             
/* 1423 */             if (isDiagnosticsEnabled()) {
/* 1424 */               logDiagnostic("[LOOKUP] Properties file found at '" + url + "'" + " with priority " + priority);
/*      */             }
/*      */             continue;
/*      */           } 
/* 1428 */           String newPriorityStr = newProps.getProperty("priority");
/* 1429 */           double newPriority = 0.0D;
/* 1430 */           if (newPriorityStr != null) {
/* 1431 */             newPriority = Double.parseDouble(newPriorityStr);
/*      */           }
/*      */           
/* 1434 */           if (newPriority > priority) {
/* 1435 */             if (isDiagnosticsEnabled()) {
/* 1436 */               logDiagnostic("[LOOKUP] Properties file at '" + url + "'" + " with priority " + newPriority + " overrides file at '" + propsUrl + "'" + " with priority " + priority);
/*      */             }
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1442 */             propsUrl = url;
/* 1443 */             props = newProps;
/* 1444 */             priority = newPriority; continue;
/*      */           } 
/* 1446 */           if (isDiagnosticsEnabled()) {
/* 1447 */             logDiagnostic("[LOOKUP] Properties file at '" + url + "'" + " with priority " + newPriority + " does not override file at '" + propsUrl + "'" + " with priority " + priority);
/*      */           
/*      */           }
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/*      */     
/*      */     }
/* 1457 */     catch (SecurityException e) {
/* 1458 */       if (isDiagnosticsEnabled()) {
/* 1459 */         logDiagnostic("SecurityException thrown while trying to find/read config files.");
/*      */       }
/*      */     } 
/*      */     
/* 1463 */     if (isDiagnosticsEnabled()) {
/* 1464 */       if (props == null) {
/* 1465 */         logDiagnostic("[LOOKUP] No properties file of name '" + fileName + "' found.");
/*      */       } else {
/* 1467 */         logDiagnostic("[LOOKUP] Properties file of name '" + fileName + "' found at '" + propsUrl + '"');
/*      */       } 
/*      */     }
/*      */     
/* 1471 */     return props;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String getSystemProperty(String key, String def) throws SecurityException {
/* 1485 */     return AccessController.<String>doPrivileged(new PrivilegedAction(key, def) { private final String val$key; private final String val$def;
/*      */           
/*      */           public Object run() {
/* 1488 */             return System.getProperty(this.val$key, this.val$def);
/*      */           } }
/*      */       );
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static PrintStream initDiagnostics() {
/*      */     String dest;
/*      */     try {
/* 1502 */       dest = getSystemProperty("org.apache.commons.logging.diagnostics.dest", null);
/* 1503 */       if (dest == null) {
/* 1504 */         return null;
/*      */       }
/* 1506 */     } catch (SecurityException ex) {
/*      */ 
/*      */       
/* 1509 */       return null;
/*      */     } 
/*      */     
/* 1512 */     if (dest.equals("STDOUT"))
/* 1513 */       return System.out; 
/* 1514 */     if (dest.equals("STDERR")) {
/* 1515 */       return System.err;
/*      */     }
/*      */     
/*      */     try {
/* 1519 */       FileOutputStream fos = new FileOutputStream(dest, true);
/* 1520 */       return new PrintStream(fos);
/* 1521 */     } catch (IOException ex) {
/*      */       
/* 1523 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static boolean isDiagnosticsEnabled() {
/* 1538 */     return (diagnosticsStream != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final void logDiagnostic(String msg) {
/* 1560 */     if (diagnosticsStream != null) {
/* 1561 */       diagnosticsStream.print(diagnosticPrefix);
/* 1562 */       diagnosticsStream.println(msg);
/* 1563 */       diagnosticsStream.flush();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static final void logRawDiagnostic(String msg) {
/* 1574 */     if (diagnosticsStream != null) {
/* 1575 */       diagnosticsStream.println(msg);
/* 1576 */       diagnosticsStream.flush();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void logClassLoaderEnvironment(Class clazz) {
/*      */     ClassLoader classLoader;
/* 1598 */     if (!isDiagnosticsEnabled()) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1606 */       logDiagnostic("[ENV] Extension directories (java.ext.dir): " + System.getProperty("java.ext.dir"));
/* 1607 */       logDiagnostic("[ENV] Application classpath (java.class.path): " + System.getProperty("java.class.path"));
/* 1608 */     } catch (SecurityException ex) {
/* 1609 */       logDiagnostic("[ENV] Security setting prevent interrogation of system classpaths.");
/*      */     } 
/*      */     
/* 1612 */     String className = clazz.getName();
/*      */ 
/*      */     
/*      */     try {
/* 1616 */       classLoader = getClassLoader(clazz);
/* 1617 */     } catch (SecurityException ex) {
/*      */       
/* 1619 */       logDiagnostic("[ENV] Security forbids determining the classloader for " + className);
/*      */       
/*      */       return;
/*      */     } 
/* 1623 */     logDiagnostic("[ENV] Class " + className + " was loaded via classloader " + objectId(classLoader));
/* 1624 */     logHierarchy("[ENV] Ancestry of classloader which loaded " + className + " is ", classLoader);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void logHierarchy(String prefix, ClassLoader classLoader) {
/*      */     ClassLoader systemClassLoader;
/* 1635 */     if (!isDiagnosticsEnabled()) {
/*      */       return;
/*      */     }
/*      */     
/* 1639 */     if (classLoader != null) {
/* 1640 */       String classLoaderString = classLoader.toString();
/* 1641 */       logDiagnostic(prefix + objectId(classLoader) + " == '" + classLoaderString + "'");
/*      */     } 
/*      */     
/*      */     try {
/* 1645 */       systemClassLoader = ClassLoader.getSystemClassLoader();
/* 1646 */     } catch (SecurityException ex) {
/* 1647 */       logDiagnostic(prefix + "Security forbids determining the system classloader.");
/*      */       return;
/*      */     } 
/* 1650 */     if (classLoader != null) {
/* 1651 */       StringBuffer buf = new StringBuffer(prefix + "ClassLoader tree:");
/*      */       while (true) {
/* 1653 */         buf.append(objectId(classLoader));
/* 1654 */         if (classLoader == systemClassLoader) {
/* 1655 */           buf.append(" (SYSTEM) ");
/*      */         }
/*      */         
/*      */         try {
/* 1659 */           classLoader = classLoader.getParent();
/* 1660 */         } catch (SecurityException ex) {
/* 1661 */           buf.append(" --> SECRET");
/*      */           
/*      */           break;
/*      */         } 
/* 1665 */         buf.append(" --> ");
/* 1666 */         if (classLoader == null) {
/* 1667 */           buf.append("BOOT");
/*      */           break;
/*      */         } 
/*      */       } 
/* 1671 */       logDiagnostic(buf.toString());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String objectId(Object o) {
/* 1688 */     if (o == null) {
/* 1689 */       return "null";
/*      */     }
/* 1691 */     return o.getClass().getName() + "@" + System.identityHashCode(o);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static {
/* 1717 */     thisClassLoader = getClassLoader(LogFactory.class);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1729 */       ClassLoader classLoader = thisClassLoader;
/* 1730 */       if (thisClassLoader == null) {
/* 1731 */         str = "BOOTLOADER";
/*      */       } else {
/* 1733 */         str = objectId(classLoader);
/*      */       } 
/* 1735 */     } catch (SecurityException e) {
/* 1736 */       str = "UNKNOWN";
/*      */     } 
/* 1738 */     diagnosticPrefix = "[LogFactory from " + str + "] ";
/* 1739 */     diagnosticsStream = initDiagnostics();
/* 1740 */     logClassLoaderEnvironment(LogFactory.class);
/* 1741 */     factories = createFactoryStore();
/* 1742 */     if (isDiagnosticsEnabled())
/* 1743 */       logDiagnostic("BOOTSTRAP COMPLETED"); 
/*      */   }
/*      */   
/*      */   public abstract Object getAttribute(String paramString);
/*      */   
/*      */   public abstract String[] getAttributeNames();
/*      */   
/*      */   public abstract Log getInstance(Class paramClass) throws LogConfigurationException;
/*      */   
/*      */   public abstract Log getInstance(String paramString) throws LogConfigurationException;
/*      */   
/*      */   public abstract void release();
/*      */   
/*      */   public abstract void removeAttribute(String paramString);
/*      */   
/*      */   public abstract void setAttribute(String paramString, Object paramObject);
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\commons\logging\LogFactory.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */