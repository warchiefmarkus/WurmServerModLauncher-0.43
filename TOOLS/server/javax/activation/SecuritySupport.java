/*     */ package javax.activation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
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
/*     */ class SecuritySupport
/*     */ {
/*     */   public static ClassLoader getContextClassLoader() {
/*  58 */     return AccessController.<ClassLoader>doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Object run() {
/*  61 */             ClassLoader cl = null;
/*     */             try {
/*  63 */               cl = Thread.currentThread().getContextClassLoader();
/*  64 */             } catch (SecurityException ex) {}
/*  65 */             return cl;
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public static InputStream getResourceAsStream(final Class c, final String name) throws IOException {
/*     */     try {
/*  73 */       return AccessController.<InputStream>doPrivileged(new PrivilegedExceptionAction() { private final Class val$c;
/*     */             
/*     */             public Object run() throws IOException {
/*  76 */               return c.getResourceAsStream(name);
/*     */             } private final String val$name; }
/*     */         );
/*  79 */     } catch (PrivilegedActionException e) {
/*  80 */       throw (IOException)e.getException();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static URL[] getResources(final ClassLoader cl, final String name) {
/*  85 */     return AccessController.<URL[]>doPrivileged(new PrivilegedAction() { private final ClassLoader val$cl; private final String val$name;
/*     */           
/*     */           public Object run() {
/*  88 */             URL[] ret = null;
/*     */             
/*  90 */             try { List v = new ArrayList();
/*  91 */               Enumeration e = cl.getResources(name);
/*  92 */               while (e != null && e.hasMoreElements()) {
/*  93 */                 URL url = e.nextElement();
/*  94 */                 if (url != null)
/*  95 */                   v.add(url); 
/*     */               } 
/*  97 */               if (v.size() > 0) {
/*  98 */                 ret = new URL[v.size()];
/*  99 */                 ret = v.<URL>toArray(ret);
/*     */               }  }
/* 101 */             catch (IOException ioex) {  }
/* 102 */             catch (SecurityException ex) {}
/* 103 */             return ret;
/*     */           } }
/*     */       );
/*     */   }
/*     */   
/*     */   public static URL[] getSystemResources(final String name) {
/* 109 */     return AccessController.<URL[]>doPrivileged(new PrivilegedAction() { private final String val$name;
/*     */           
/*     */           public Object run() {
/* 112 */             URL[] ret = null;
/*     */             
/* 114 */             try { List v = new ArrayList();
/* 115 */               Enumeration e = ClassLoader.getSystemResources(name);
/* 116 */               while (e != null && e.hasMoreElements()) {
/* 117 */                 URL url = e.nextElement();
/* 118 */                 if (url != null)
/* 119 */                   v.add(url); 
/*     */               } 
/* 121 */               if (v.size() > 0) {
/* 122 */                 ret = new URL[v.size()];
/* 123 */                 ret = v.<URL>toArray(ret);
/*     */               }  }
/* 125 */             catch (IOException ioex) {  }
/* 126 */             catch (SecurityException ex) {}
/* 127 */             return ret;
/*     */           } }
/*     */       );
/*     */   }
/*     */   
/*     */   public static InputStream openStream(final URL url) throws IOException {
/*     */     try {
/* 134 */       return AccessController.<InputStream>doPrivileged(new PrivilegedExceptionAction() { private final URL val$url;
/*     */             
/*     */             public Object run() throws IOException {
/* 137 */               return url.openStream();
/*     */             } }
/*     */         );
/* 140 */     } catch (PrivilegedActionException e) {
/* 141 */       throw (IOException)e.getException();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\activation\SecuritySupport.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */