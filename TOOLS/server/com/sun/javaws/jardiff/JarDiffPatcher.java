/*     */ package com.sun.javaws.jardiff;
/*     */ 
/*     */ import com.sun.javaws.cache.Patcher;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.LineNumberReader;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.Set;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.JarOutputStream;
/*     */ 
/*     */ public class JarDiffPatcher
/*     */   implements JarDiffConstants, Patcher {
/*     */   private static final int DEFAULT_READ_SIZE = 2048;
/*  27 */   private static byte[] newBytes = new byte[2048];
/*  28 */   private static byte[] oldBytes = new byte[2048];
/*  29 */   private static ResourceBundle _resources = JarDiff.getResources();
/*     */   
/*     */   public static ResourceBundle getResources() {
/*  32 */     return JarDiff.getResources();
/*     */   }
/*     */ 
/*     */   
/*     */   public void applyPatch(Patcher.PatchDelegate paramPatchDelegate, String paramString1, String paramString2, OutputStream paramOutputStream) throws IOException {
/*  37 */     File file1 = new File(paramString1);
/*  38 */     File file2 = new File(paramString2);
/*  39 */     JarOutputStream jarOutputStream = new JarOutputStream(paramOutputStream);
/*  40 */     JarFile jarFile1 = new JarFile(file1);
/*  41 */     JarFile jarFile2 = new JarFile(file2);
/*  42 */     HashSet hashSet = new HashSet();
/*  43 */     HashMap hashMap = new HashMap();
/*     */ 
/*     */     
/*  46 */     determineNameMapping(jarFile2, hashSet, hashMap);
/*     */ 
/*     */     
/*  49 */     Object[] arrayOfObject = hashMap.keySet().toArray();
/*     */ 
/*     */ 
/*     */     
/*  53 */     HashSet hashSet1 = new HashSet();
/*     */     
/*  55 */     Enumeration enumeration1 = jarFile1.entries();
/*  56 */     if (enumeration1 != null) {
/*  57 */       while (enumeration1.hasMoreElements()) {
/*  58 */         hashSet1.add(((JarEntry)enumeration1.nextElement()).getName());
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  68 */     double d1 = (hashSet1.size() + arrayOfObject.length + jarFile2.size());
/*  69 */     double d2 = 0.0D;
/*     */ 
/*     */     
/*  72 */     hashSet1.removeAll(hashSet);
/*  73 */     d1 -= hashSet.size();
/*     */ 
/*     */ 
/*     */     
/*  77 */     Enumeration enumeration2 = jarFile2.entries();
/*  78 */     if (enumeration2 != null) {
/*  79 */       while (enumeration2.hasMoreElements()) {
/*  80 */         JarEntry jarEntry = enumeration2.nextElement();
/*     */ 
/*     */ 
/*     */         
/*  84 */         if (!"META-INF/INDEX.JD".equals(jarEntry.getName())) {
/*     */           
/*  86 */           updateDelegate(paramPatchDelegate, d2, d1);
/*  87 */           d2++;
/*     */           
/*  89 */           writeEntry(jarOutputStream, jarEntry, jarFile2);
/*     */ 
/*     */ 
/*     */           
/*  93 */           boolean bool = hashSet1.remove(jarEntry.getName());
/*     */ 
/*     */ 
/*     */           
/*  97 */           if (bool) d1--;
/*     */ 
/*     */           
/*     */           continue;
/*     */         } 
/* 102 */         d1--;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 110 */     for (byte b = 0; b < arrayOfObject.length; b++) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 115 */       String str1 = (String)arrayOfObject[b];
/* 116 */       String str2 = (String)hashMap.get(str1);
/*     */ 
/*     */       
/* 119 */       JarEntry jarEntry1 = jarFile1.getJarEntry(str2);
/*     */       
/* 121 */       if (jarEntry1 == null) {
/* 122 */         String str = "move" + str2 + " " + str1;
/* 123 */         handleException("jardiff.error.badmove", str);
/*     */       } 
/*     */ 
/*     */       
/* 127 */       JarEntry jarEntry2 = new JarEntry(str1);
/* 128 */       jarEntry2.setTime(jarEntry1.getTime());
/* 129 */       jarEntry2.setSize(jarEntry1.getSize());
/* 130 */       jarEntry2.setCompressedSize(jarEntry1.getCompressedSize());
/* 131 */       jarEntry2.setCrc(jarEntry1.getCrc());
/* 132 */       jarEntry2.setMethod(jarEntry1.getMethod());
/* 133 */       jarEntry2.setExtra(jarEntry1.getExtra());
/* 134 */       jarEntry2.setComment(jarEntry1.getComment());
/*     */ 
/*     */       
/* 137 */       updateDelegate(paramPatchDelegate, d2, d1);
/* 138 */       d2++;
/*     */       
/* 140 */       writeEntry(jarOutputStream, jarEntry2, jarFile1.getInputStream(jarEntry1));
/*     */ 
/*     */ 
/*     */       
/* 144 */       boolean bool = hashSet1.remove(str2);
/*     */ 
/*     */ 
/*     */       
/* 148 */       if (bool) d1--;
/*     */     
/*     */     } 
/*     */ 
/*     */     
/* 153 */     Iterator iterator = hashSet1.iterator();
/* 154 */     if (iterator != null) {
/* 155 */       while (iterator.hasNext()) {
/*     */         
/* 157 */         String str = iterator.next();
/* 158 */         JarEntry jarEntry = jarFile1.getJarEntry(str);
/*     */         
/* 160 */         updateDelegate(paramPatchDelegate, d2, d1);
/* 161 */         d2++;
/*     */         
/* 163 */         writeEntry(jarOutputStream, jarEntry, jarFile1);
/*     */       } 
/*     */     }
/*     */     
/* 167 */     updateDelegate(paramPatchDelegate, d2, d1);
/*     */     
/* 169 */     jarOutputStream.finish();
/*     */   }
/*     */   
/*     */   private void updateDelegate(Patcher.PatchDelegate paramPatchDelegate, double paramDouble1, double paramDouble2) {
/* 173 */     if (paramPatchDelegate != null) {
/* 174 */       paramPatchDelegate.patching((int)(paramDouble1 / paramDouble2));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void determineNameMapping(JarFile paramJarFile, Set paramSet, Map paramMap) throws IOException {
/* 180 */     InputStream inputStream = paramJarFile.getInputStream(paramJarFile.getEntry("META-INF/INDEX.JD"));
/*     */     
/* 182 */     if (inputStream == null) {
/* 183 */       handleException("jardiff.error.noindex", null);
/*     */     }
/*     */     
/* 186 */     LineNumberReader lineNumberReader = new LineNumberReader(new InputStreamReader(inputStream, "UTF-8"));
/*     */     
/* 188 */     String str = lineNumberReader.readLine();
/*     */     
/* 190 */     if (str == null || !str.equals("version 1.0")) {
/* 191 */       handleException("jardiff.error.badheader", str);
/*     */     }
/*     */ 
/*     */     
/* 195 */     while ((str = lineNumberReader.readLine()) != null) {
/* 196 */       if (str.startsWith("remove")) {
/* 197 */         List list = getSubpaths(str.substring("remove".length()));
/*     */ 
/*     */         
/* 200 */         if (list.size() != 1) {
/* 201 */           handleException("jardiff.error.badremove", str);
/*     */         }
/*     */         
/* 204 */         paramSet.add(list.get(0)); continue;
/*     */       } 
/* 206 */       if (str.startsWith("move")) {
/* 207 */         List list = getSubpaths(str.substring("move".length()));
/*     */         
/* 209 */         if (list.size() != 2) {
/* 210 */           handleException("jardiff.error.badmove", str);
/*     */         }
/*     */ 
/*     */         
/* 214 */         if (paramMap.put(list.get(1), list.get(0)) != null)
/*     */         {
/* 216 */           handleException("jardiff.error.badmove", str); } 
/*     */         continue;
/*     */       } 
/* 219 */       if (str.length() > 0) {
/* 220 */         handleException("jardiff.error.badcommand", str);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleException(String paramString1, String paramString2) throws IOException {
/*     */     try {
/* 228 */       throw new IOException(getResources().getString(paramString1) + " " + paramString2);
/* 229 */     } catch (MissingResourceException missingResourceException) {
/* 230 */       System.err.println("Fatal error: " + paramString1);
/* 231 */       (new Throwable()).printStackTrace(System.err);
/* 232 */       System.exit(-1);
/*     */       return;
/*     */     } 
/*     */   }
/*     */   private List getSubpaths(String paramString) {
/* 237 */     byte b = 0;
/* 238 */     int i = paramString.length();
/* 239 */     ArrayList arrayList = new ArrayList();
/*     */     
/* 241 */     while (b < i) {
/* 242 */       while (b < i && Character.isWhitespace(paramString.charAt(b)))
/*     */       {
/* 244 */         b++;
/*     */       }
/* 246 */       if (b < i) {
/* 247 */         byte b1 = b;
/* 248 */         byte b2 = b1;
/* 249 */         String str = null;
/*     */         
/* 251 */         while (b < i) {
/* 252 */           char c = paramString.charAt(b);
/* 253 */           if (c == '\\' && b + 1 < i && paramString.charAt(b + 1) == ' ') {
/*     */ 
/*     */             
/* 256 */             if (str == null) {
/* 257 */               str = paramString.substring(b2, b);
/*     */             } else {
/*     */               
/* 260 */               str = str + paramString.substring(b2, b);
/*     */             } 
/* 262 */             b2 = ++b;
/*     */           }
/* 264 */           else if (Character.isWhitespace(c)) {
/*     */             break;
/*     */           } 
/* 267 */           b++;
/*     */         } 
/* 269 */         if (b2 != b) {
/* 270 */           if (str == null) {
/* 271 */             str = paramString.substring(b2, b);
/*     */           } else {
/*     */             
/* 274 */             str = str + paramString.substring(b2, b);
/*     */           } 
/*     */         }
/* 277 */         arrayList.add(str);
/*     */       } 
/*     */     } 
/* 280 */     return arrayList;
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeEntry(JarOutputStream paramJarOutputStream, JarEntry paramJarEntry, JarFile paramJarFile) throws IOException {
/* 285 */     writeEntry(paramJarOutputStream, paramJarEntry, paramJarFile.getInputStream(paramJarEntry));
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeEntry(JarOutputStream paramJarOutputStream, JarEntry paramJarEntry, InputStream paramInputStream) throws IOException {
/* 290 */     paramJarOutputStream.putNextEntry(paramJarEntry);
/*     */ 
/*     */     
/* 293 */     int i = paramInputStream.read(newBytes);
/*     */     
/* 295 */     while (i != -1) {
/* 296 */       paramJarOutputStream.write(newBytes, 0, i);
/* 297 */       i = paramInputStream.read(newBytes);
/*     */     } 
/* 299 */     paramInputStream.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\jardiff\JarDiffPatcher.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */