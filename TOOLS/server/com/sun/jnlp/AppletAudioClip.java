/*     */ package com.sun.jnlp;
/*     */ 
/*     */ import com.sun.deploy.util.Trace;
/*     */ import com.sun.deploy.util.TraceLevel;
/*     */ import java.applet.AudioClip;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.Permission;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ final class AppletAudioClip
/*     */   implements AudioClip
/*     */ {
/*  44 */   private static Constructor acConstructor = null;
/*     */ 
/*     */   
/*  47 */   private URL url = null;
/*     */ 
/*     */   
/*  50 */   private AudioClip audioClip = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AppletAudioClip() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public AppletAudioClip(URL paramURL) {
/*  60 */     this.url = paramURL;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  65 */       InputStream inputStream = paramURL.openStream();
/*  66 */       createAppletAudioClip(inputStream);
/*     */     }
/*  68 */     catch (IOException iOException) {
/*     */ 
/*     */       
/*  71 */       Trace.println("IOException creating AppletAudioClip" + iOException, TraceLevel.BASIC);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*  76 */   private static Map audioClips = new HashMap();
/*     */ 
/*     */   
/*     */   public static synchronized AudioClip get(URL paramURL) {
/*  80 */     checkConnect(paramURL);
/*  81 */     AudioClip audioClip = (AudioClip)audioClips.get(paramURL);
/*  82 */     if (audioClip == null) {
/*  83 */       audioClip = new AppletAudioClip(paramURL);
/*  84 */       audioClips.put(paramURL, audioClip);
/*     */     } 
/*  86 */     return audioClip;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void createAppletAudioClip(InputStream paramInputStream) throws IOException {
/*  97 */     if (acConstructor == null) {
/*  98 */       Trace.println("Initializing AudioClip constructor.", TraceLevel.BASIC);
/*     */       try {
/* 100 */         acConstructor = AccessController.<Constructor>doPrivileged(new PrivilegedExceptionAction(this) { private final AppletAudioClip this$0;
/*     */               public Object run() throws NoSuchMethodException, SecurityException, ClassNotFoundException {
/* 102 */                 Class clazz = null;
/*     */                 
/*     */                 try {
/* 105 */                   clazz = Class.forName("com.sun.media.sound.JavaSoundAudioClip", true, ClassLoader.getSystemClassLoader());
/*     */ 
/*     */ 
/*     */                   
/* 109 */                   Trace.println("Loaded JavaSoundAudioClip", TraceLevel.BASIC);
/* 110 */                 } catch (ClassNotFoundException classNotFoundException) {
/* 111 */                   clazz = Class.forName("sun.audio.SunAudioClip", true, null);
/* 112 */                   Trace.println("Loaded SunAudioClip", TraceLevel.BASIC);
/*     */                 } 
/*     */                 
/* 115 */                 Class[] arrayOfClass = new Class[1];
/* 116 */                 arrayOfClass[0] = Class.forName("java.io.InputStream");
/* 117 */                 return clazz.getConstructor(arrayOfClass);
/*     */               } }
/*     */           );
/*     */       }
/* 121 */       catch (PrivilegedActionException privilegedActionException) {
/* 122 */         Trace.println("Got a PrivilegedActionException: " + privilegedActionException.getException(), TraceLevel.BASIC);
/*     */ 
/*     */ 
/*     */         
/* 126 */         throw new IOException("Failed to get AudioClip constructor: " + privilegedActionException.getException());
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 133 */       Object[] arrayOfObject = { paramInputStream };
/* 134 */       this.audioClip = acConstructor.newInstance(arrayOfObject);
/*     */ 
/*     */     
/*     */     }
/* 138 */     catch (Exception exception) {
/*     */       
/* 140 */       throw new IOException("Failed to construct the AudioClip: " + exception);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void checkConnect(URL paramURL) {
/* 146 */     SecurityManager securityManager = System.getSecurityManager();
/* 147 */     if (securityManager != null) {
/*     */       try {
/* 149 */         Permission permission = paramURL.openConnection().getPermission();
/*     */         
/* 151 */         if (permission != null)
/* 152 */         { securityManager.checkPermission(permission); }
/*     */         else
/* 154 */         { securityManager.checkConnect(paramURL.getHost(), paramURL.getPort()); } 
/* 155 */       } catch (IOException iOException) {
/* 156 */         securityManager.checkConnect(paramURL.getHost(), paramURL.getPort());
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void play() {
/* 165 */     if (this.audioClip != null) this.audioClip.play(); 
/*     */   }
/*     */   
/*     */   public synchronized void loop() {
/* 169 */     if (this.audioClip != null) this.audioClip.loop(); 
/*     */   }
/*     */   
/*     */   public synchronized void stop() {
/* 173 */     if (this.audioClip != null) this.audioClip.stop(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\jnlp\AppletAudioClip.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */