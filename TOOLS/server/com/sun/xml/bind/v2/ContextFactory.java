/*     */ package com.sun.xml.bind.v2;
/*     */ 
/*     */ import com.sun.istack.FinalArrayList;
/*     */ import com.sun.xml.bind.Util;
/*     */ import com.sun.xml.bind.api.JAXBRIContext;
/*     */ import com.sun.xml.bind.api.TypeReference;
/*     */ import com.sun.xml.bind.v2.model.annotation.RuntimeAnnotationReader;
/*     */ import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.bind.v2.util.TypeCast;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.logging.Level;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.bind.JAXBException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ContextFactory
/*     */ {
/*     */   public static final String USE_JAXB_PROPERTIES = "_useJAXBProperties";
/*     */   
/*     */   public static JAXBContext createContext(Class[] classes, Map<String, Object> properties) throws JAXBException {
/*     */     Map<Class<?>, Class<?>> subclassReplacements;
/*  79 */     if (properties == null) {
/*  80 */       properties = Collections.emptyMap();
/*     */     } else {
/*  82 */       properties = new HashMap<String, Object>(properties);
/*     */     } 
/*  84 */     String defaultNsUri = getPropertyValue(properties, "com.sun.xml.bind.defaultNamespaceRemap", String.class);
/*     */     
/*  86 */     Boolean c14nSupport = getPropertyValue(properties, "com.sun.xml.bind.c14n", Boolean.class);
/*  87 */     if (c14nSupport == null) {
/*  88 */       c14nSupport = Boolean.valueOf(false);
/*     */     }
/*  90 */     Boolean allNillable = getPropertyValue(properties, "com.sun.xml.bind.treatEverythingNillable", Boolean.class);
/*  91 */     if (allNillable == null) {
/*  92 */       allNillable = Boolean.valueOf(false);
/*     */     }
/*  94 */     Boolean xmlAccessorFactorySupport = getPropertyValue(properties, "com.sun.xml.bind.XmlAccessorFactory", Boolean.class);
/*     */     
/*  96 */     if (xmlAccessorFactorySupport == null) {
/*  97 */       xmlAccessorFactorySupport = Boolean.valueOf(false);
/*  98 */       Util.getClassLogger().log(Level.FINE, "Property com.sun.xml.bind.XmlAccessorFactoryis not active.  Using JAXB's implementation");
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 103 */     RuntimeAnnotationReader ar = getPropertyValue(properties, JAXBRIContext.ANNOTATION_READER, RuntimeAnnotationReader.class);
/*     */ 
/*     */     
/*     */     try {
/* 107 */       subclassReplacements = TypeCast.checkedCast(getPropertyValue(properties, "com.sun.xml.bind.subclassReplacements", Map.class), Class.class, Class.class);
/*     */     }
/* 109 */     catch (ClassCastException e) {
/* 110 */       throw new JAXBException(Messages.INVALID_TYPE_IN_MAP.format(new Object[0]), e);
/*     */     } 
/*     */     
/* 113 */     if (!properties.isEmpty()) {
/* 114 */       throw new JAXBException(Messages.UNSUPPORTED_PROPERTY.format(new Object[] { properties.keySet().iterator().next() }));
/*     */     }
/*     */     
/* 117 */     return (JAXBContext)createContext(classes, Collections.emptyList(), subclassReplacements, defaultNsUri, c14nSupport.booleanValue(), ar, xmlAccessorFactorySupport.booleanValue(), allNillable.booleanValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> T getPropertyValue(Map<String, Object> properties, String keyName, Class<T> type) throws JAXBException {
/* 125 */     Object o = properties.get(keyName);
/* 126 */     if (o == null) return null;
/*     */     
/* 128 */     properties.remove(keyName);
/* 129 */     if (!type.isInstance(o)) {
/* 130 */       throw new JAXBException(Messages.INVALID_PROPERTY_VALUE.format(new Object[] { keyName, o }));
/*     */     }
/* 132 */     return type.cast(o);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JAXBRIContext createContext(Class[] classes, Collection<TypeReference> typeRefs, Map<Class<?>, Class<?>> subclassReplacements, String defaultNsUri, boolean c14nSupport, RuntimeAnnotationReader ar, boolean xmlAccessorFactorySupport, boolean allNillable) throws JAXBException {
/* 139 */     return (JAXBRIContext)new JAXBContextImpl(classes, typeRefs, subclassReplacements, defaultNsUri, c14nSupport, ar, xmlAccessorFactorySupport, allNillable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JAXBContext createContext(String contextPath, ClassLoader classLoader, Map<String, Object> properties) throws JAXBException {
/* 148 */     FinalArrayList<Class<?>> classes = new FinalArrayList();
/* 149 */     StringTokenizer tokens = new StringTokenizer(contextPath, ":");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 156 */     while (tokens.hasMoreTokens()) {
/* 157 */       List<Class<?>> indexedClasses; boolean foundJaxbIndex = false, foundObjectFactory = foundJaxbIndex;
/* 158 */       String pkg = tokens.nextToken();
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 163 */         Class<?> o = classLoader.loadClass(pkg + ".ObjectFactory");
/* 164 */         classes.add(o);
/* 165 */         foundObjectFactory = true;
/* 166 */       } catch (ClassNotFoundException e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 172 */         indexedClasses = loadIndexedClasses(pkg, classLoader);
/* 173 */       } catch (IOException e) {
/*     */         
/* 175 */         throw new JAXBException(e);
/*     */       } 
/* 177 */       if (indexedClasses != null) {
/* 178 */         classes.addAll(indexedClasses);
/* 179 */         foundJaxbIndex = true;
/*     */       } 
/*     */       
/* 182 */       if (!foundObjectFactory && !foundJaxbIndex) {
/* 183 */         throw new JAXBException(Messages.BROKEN_CONTEXTPATH.format(new Object[] { pkg }));
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 188 */     return createContext((Class[])classes.toArray((Object[])new Class[classes.size()]), properties);
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
/*     */   private static List<Class> loadIndexedClasses(String pkg, ClassLoader classLoader) throws IOException, JAXBException {
/* 201 */     String resource = pkg.replace('.', '/') + "/jaxb.index";
/* 202 */     InputStream resourceAsStream = classLoader.getResourceAsStream(resource);
/*     */     
/* 204 */     if (resourceAsStream == null) {
/* 205 */       return null;
/*     */     }
/*     */     
/* 208 */     BufferedReader in = new BufferedReader(new InputStreamReader(resourceAsStream, "UTF-8"));
/*     */     
/*     */     try {
/* 211 */       FinalArrayList<Class<?>> classes = new FinalArrayList();
/* 212 */       String className = in.readLine();
/* 213 */       while (className != null) {
/* 214 */         className = className.trim();
/* 215 */         if (className.startsWith("#") || className.length() == 0) {
/* 216 */           className = in.readLine();
/*     */           
/*     */           continue;
/*     */         } 
/* 220 */         if (className.endsWith(".class")) {
/* 221 */           throw new JAXBException(Messages.ILLEGAL_ENTRY.format(new Object[] { className }));
/*     */         }
/*     */         
/*     */         try {
/* 225 */           classes.add(classLoader.loadClass(pkg + '.' + className));
/* 226 */         } catch (ClassNotFoundException e) {
/* 227 */           throw new JAXBException(Messages.ERROR_LOADING_CLASS.format(new Object[] { className, resource }, ), e);
/*     */         } 
/*     */         
/* 230 */         className = in.readLine();
/*     */       } 
/* 232 */       return (List)classes;
/*     */     } finally {
/* 234 */       in.close();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\ContextFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */