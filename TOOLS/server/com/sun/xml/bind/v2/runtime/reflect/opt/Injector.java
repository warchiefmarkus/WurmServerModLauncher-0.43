/*     */ package com.sun.xml.bind.v2.runtime.reflect.opt;
/*     */ 
/*     */ import com.sun.xml.bind.Util;
/*     */ import com.sun.xml.bind.v2.runtime.reflect.Accessor;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
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
/*     */ final class Injector
/*     */ {
/*  71 */   private static final Map<ClassLoader, WeakReference<Injector>> injectors = Collections.synchronizedMap(new WeakHashMap<ClassLoader, WeakReference<Injector>>());
/*     */ 
/*     */   
/*  74 */   private static final Logger logger = Util.getClassLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Class inject(ClassLoader cl, String className, byte[] image) {
/*  83 */     Injector injector = get(cl);
/*  84 */     if (injector != null) {
/*  85 */       return injector.inject(className, image);
/*     */     }
/*  87 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Class find(ClassLoader cl, String className) {
/*  94 */     Injector injector = get(cl);
/*  95 */     if (injector != null) {
/*  96 */       return injector.find(className);
/*     */     }
/*  98 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Injector get(ClassLoader cl) {
/* 108 */     Injector injector = null;
/* 109 */     WeakReference<Injector> wr = injectors.get(cl);
/* 110 */     if (wr != null)
/* 111 */       injector = wr.get(); 
/* 112 */     if (injector == null)
/*     */       try {
/* 114 */         injectors.put(cl, new WeakReference<Injector>(injector = new Injector(cl)));
/* 115 */       } catch (SecurityException e) {
/* 116 */         logger.log(Level.FINE, "Unable to set up a back-door for the injector", e);
/* 117 */         return null;
/*     */       }  
/* 119 */     return injector;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 125 */   private final Map<String, Class> classes = (Map)new HashMap<String, Class<?>>();
/*     */ 
/*     */   
/*     */   private final ClassLoader parent;
/*     */ 
/*     */   
/*     */   private final boolean loadable;
/*     */   
/*     */   private static final Method defineClass;
/*     */   
/*     */   private static final Method resolveClass;
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/* 140 */       defineClass = ClassLoader.class.getDeclaredMethod("defineClass", new Class[] { String.class, byte[].class, int.class, int.class });
/* 141 */       resolveClass = ClassLoader.class.getDeclaredMethod("resolveClass", new Class[] { Class.class });
/* 142 */     } catch (NoSuchMethodException e) {
/*     */       
/* 144 */       throw new NoSuchMethodError(e.getMessage());
/*     */     } 
/* 146 */     AccessController.doPrivileged(new PrivilegedAction<Void>()
/*     */         {
/*     */           public Void run()
/*     */           {
/* 150 */             Injector.defineClass.setAccessible(true);
/* 151 */             Injector.resolveClass.setAccessible(true);
/* 152 */             return null;
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private Injector(ClassLoader parent) {
/* 158 */     this.parent = parent;
/* 159 */     assert parent != null;
/*     */     
/* 161 */     boolean loadable = false;
/*     */     
/*     */     try {
/* 164 */       loadable = (parent.loadClass(Accessor.class.getName()) == Accessor.class);
/* 165 */     } catch (ClassNotFoundException e) {}
/*     */ 
/*     */ 
/*     */     
/* 169 */     this.loadable = loadable;
/*     */   }
/*     */ 
/*     */   
/*     */   private synchronized Class inject(String className, byte[] image) {
/* 174 */     if (!this.loadable) {
/* 175 */       return null;
/*     */     }
/* 177 */     Class c = this.classes.get(className);
/* 178 */     if (c == null) {
/*     */       
/*     */       try {
/* 181 */         c = (Class)defineClass.invoke(this.parent, new Object[] { className.replace('/', '.'), image, Integer.valueOf(0), Integer.valueOf(image.length) });
/* 182 */         resolveClass.invoke(this.parent, new Object[] { c });
/* 183 */       } catch (IllegalAccessException e) {
/* 184 */         logger.log(Level.FINE, "Unable to inject " + className, e);
/* 185 */         return null;
/* 186 */       } catch (InvocationTargetException e) {
/* 187 */         logger.log(Level.FINE, "Unable to inject " + className, e);
/* 188 */         return null;
/* 189 */       } catch (SecurityException e) {
/* 190 */         logger.log(Level.FINE, "Unable to inject " + className, e);
/* 191 */         return null;
/* 192 */       } catch (LinkageError e) {
/* 193 */         logger.log(Level.FINE, "Unable to inject " + className, e);
/* 194 */         return null;
/*     */       } 
/* 196 */       this.classes.put(className, c);
/*     */     } 
/* 198 */     return c;
/*     */   }
/*     */   
/*     */   private synchronized Class find(String className) {
/* 202 */     return this.classes.get(className);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\reflect\opt\Injector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */