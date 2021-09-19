/*     */ package com.sun.javaws.jardiff;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Map;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.JarOutputStream;
/*     */ 
/*     */ public class JarDiff implements JarDiffConstants {
/*  26 */   private static byte[] newBytes = new byte[2048]; private static final int DEFAULT_READ_SIZE = 2048;
/*  27 */   private static byte[] oldBytes = new byte[2048];
/*  28 */   private static ResourceBundle _resources = null;
/*     */ 
/*     */   
/*     */   private static boolean _debug;
/*     */ 
/*     */ 
/*     */   
/*     */   public static ResourceBundle getResources() {
/*  36 */     if (_resources == null) {
/*  37 */       _resources = ResourceBundle.getBundle("com/sun/javaws/jardiff/resources/strings");
/*     */     }
/*  39 */     return _resources;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void createPatch(String paramString1, String paramString2, OutputStream paramOutputStream, boolean paramBoolean) throws IOException {
/*  48 */     JarFile2 jarFile21 = new JarFile2(paramString1);
/*  49 */     JarFile2 jarFile22 = new JarFile2(paramString2);
/*     */ 
/*     */     
/*  52 */     HashMap hashMap = new HashMap();
/*  53 */     HashSet hashSet = new HashSet();
/*  54 */     HashSet hashSet1 = new HashSet();
/*  55 */     HashSet hashSet2 = new HashSet();
/*  56 */     HashSet hashSet3 = new HashSet();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  65 */     Iterator iterator = jarFile22.getJarEntries();
/*  66 */     if (iterator != null) {
/*  67 */       while (iterator.hasNext()) {
/*  68 */         JarEntry jarEntry = iterator.next();
/*  69 */         String str1 = jarEntry.getName();
/*     */ 
/*     */         
/*  72 */         String str2 = jarFile21.getBestMatch(jarFile22, jarEntry);
/*  73 */         if (str2 == null) {
/*     */           
/*  75 */           if (_debug) {
/*  76 */             System.out.println("NEW: " + str1);
/*     */           }
/*  78 */           hashSet3.add(str1);
/*     */ 
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/*  84 */         if (str2.equals(str1) && !hashSet2.contains(str2)) {
/*  85 */           if (_debug) {
/*  86 */             System.out.println(str1 + " added to implicit set!");
/*     */           }
/*  88 */           hashSet1.add(str1);
/*     */ 
/*     */ 
/*     */           
/*     */           continue;
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/*  97 */         if (!paramBoolean && (hashSet1.contains(str2) || hashSet2.contains(str2))) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 103 */           if (_debug)
/*     */           {
/* 105 */             System.out.println("NEW: " + str1);
/*     */           }
/* 107 */           hashSet3.add(str1);
/*     */         } else {
/*     */           
/* 110 */           if (_debug) {
/* 111 */             System.err.println("moved.put " + str1 + " " + str2);
/*     */           }
/* 113 */           hashMap.put(str1, str2);
/* 114 */           hashSet2.add(str2);
/*     */         } 
/*     */         
/* 117 */         if (hashSet1.contains(str2) && paramBoolean) {
/*     */           
/* 119 */           if (_debug) {
/* 120 */             System.err.println("implicit.remove " + str2);
/*     */             
/* 122 */             System.err.println("moved.put " + str2 + " " + str2);
/*     */           } 
/*     */           
/* 125 */           hashSet1.remove(str2);
/* 126 */           hashMap.put(str2, str2);
/* 127 */           hashSet2.add(str2);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 139 */     ArrayList arrayList = new ArrayList();
/* 140 */     iterator = jarFile21.getJarEntries();
/* 141 */     if (iterator != null) {
/* 142 */       while (iterator.hasNext()) {
/* 143 */         JarEntry jarEntry = iterator.next();
/* 144 */         String str = jarEntry.getName();
/* 145 */         if (!hashSet1.contains(str) && !hashSet2.contains(str) && !hashSet3.contains(str)) {
/*     */           
/* 147 */           if (_debug) {
/* 148 */             System.err.println("deleted.add " + str);
/*     */           }
/* 150 */           arrayList.add(str);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 156 */     if (_debug) {
/*     */       
/* 158 */       iterator = hashMap.keySet().iterator();
/* 159 */       if (iterator != null) {
/* 160 */         System.out.println("MOVED MAP!!!");
/* 161 */         while (iterator.hasNext()) {
/* 162 */           String str1 = (String)iterator.next();
/* 163 */           String str2 = (String)hashMap.get(str1);
/* 164 */           System.out.println("key is " + str1 + " value is " + str2);
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 169 */       iterator = (Iterator)hashSet1.iterator();
/* 170 */       if (iterator != null) {
/* 171 */         System.out.println("IMOVE MAP!!!");
/* 172 */         while (iterator.hasNext()) {
/* 173 */           String str = (String)iterator.next();
/* 174 */           System.out.println("key is " + str);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 179 */     JarOutputStream jarOutputStream = new JarOutputStream(paramOutputStream);
/*     */ 
/*     */     
/* 182 */     createIndex(jarOutputStream, arrayList, hashMap);
/*     */ 
/*     */     
/* 185 */     iterator = (Iterator)hashSet3.iterator();
/* 186 */     if (iterator != null)
/*     */     {
/* 188 */       while (iterator.hasNext()) {
/* 189 */         String str = (String)iterator.next();
/* 190 */         if (_debug) {
/* 191 */           System.out.println("New File: " + str);
/*     */         }
/* 193 */         writeEntry(jarOutputStream, jarFile22.getEntryByName(str), jarFile22);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 198 */     jarOutputStream.finish();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void createIndex(JarOutputStream paramJarOutputStream, List paramList, Map paramMap) throws IOException {
/* 209 */     StringWriter stringWriter = new StringWriter();
/*     */     
/* 211 */     stringWriter.write("version 1.0");
/* 212 */     stringWriter.write("\r\n");
/*     */ 
/*     */     
/* 215 */     for (byte b = 0; b < paramList.size(); b++) {
/* 216 */       String str = paramList.get(b);
/*     */       
/* 218 */       stringWriter.write("remove");
/* 219 */       stringWriter.write(" ");
/* 220 */       writeEscapedString(stringWriter, str);
/* 221 */       stringWriter.write("\r\n");
/*     */     } 
/*     */ 
/*     */     
/* 225 */     Iterator iterator = paramMap.keySet().iterator();
/*     */     
/* 227 */     if (iterator != null) {
/* 228 */       while (iterator.hasNext()) {
/* 229 */         String str1 = iterator.next();
/* 230 */         String str2 = (String)paramMap.get(str1);
/*     */         
/* 232 */         stringWriter.write("move");
/* 233 */         stringWriter.write(" ");
/* 234 */         writeEscapedString(stringWriter, str2);
/* 235 */         stringWriter.write(" ");
/* 236 */         writeEscapedString(stringWriter, str1);
/* 237 */         stringWriter.write("\r\n");
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 242 */     JarEntry jarEntry = new JarEntry("META-INF/INDEX.JD");
/* 243 */     byte[] arrayOfByte = stringWriter.toString().getBytes("UTF-8");
/*     */     
/* 245 */     stringWriter.close();
/* 246 */     paramJarOutputStream.putNextEntry(jarEntry);
/* 247 */     paramJarOutputStream.write(arrayOfByte, 0, arrayOfByte.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void writeEscapedString(Writer paramWriter, String paramString) throws IOException {
/* 252 */     int i = 0;
/* 253 */     int j = 0;
/* 254 */     char[] arrayOfChar = null;
/*     */     
/* 256 */     while ((i = paramString.indexOf(' ', i)) != -1) {
/* 257 */       if (j != i) {
/* 258 */         if (arrayOfChar == null) {
/* 259 */           arrayOfChar = paramString.toCharArray();
/*     */         }
/* 261 */         paramWriter.write(arrayOfChar, j, i - j);
/*     */       } 
/* 263 */       j = i;
/* 264 */       i++;
/* 265 */       paramWriter.write(92);
/*     */     } 
/* 267 */     if (j != 0) {
/* 268 */       paramWriter.write(arrayOfChar, j, arrayOfChar.length - j);
/*     */     }
/*     */     else {
/*     */       
/* 272 */       paramWriter.write(paramString);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void writeEntry(JarOutputStream paramJarOutputStream, JarEntry paramJarEntry, JarFile2 paramJarFile2) throws IOException {
/* 278 */     writeEntry(paramJarOutputStream, paramJarEntry, paramJarFile2.getJarFile().getInputStream(paramJarEntry));
/*     */   }
/*     */ 
/*     */   
/*     */   private static void writeEntry(JarOutputStream paramJarOutputStream, JarEntry paramJarEntry, InputStream paramInputStream) throws IOException {
/* 283 */     paramJarOutputStream.putNextEntry(paramJarEntry);
/*     */ 
/*     */     
/* 286 */     int i = paramInputStream.read(newBytes);
/*     */     
/* 288 */     while (i != -1) {
/* 289 */       paramJarOutputStream.write(newBytes, 0, i);
/* 290 */       i = paramInputStream.read(newBytes);
/*     */     } 
/* 292 */     paramInputStream.close();
/*     */   }
/*     */ 
/*     */   
/*     */   private static class JarFile2
/*     */   {
/*     */     private JarFile _jar;
/*     */     
/*     */     private List _entries;
/*     */     
/*     */     private HashMap _nameToEntryMap;
/*     */     
/*     */     private HashMap _crcToEntryMap;
/*     */ 
/*     */     
/*     */     public JarFile2(String param1String) throws IOException {
/* 308 */       this._jar = new JarFile(new File(param1String));
/* 309 */       index();
/*     */     }
/*     */     
/*     */     public JarFile getJarFile() {
/* 313 */       return this._jar;
/*     */     }
/*     */     
/*     */     public Iterator getJarEntries() {
/* 317 */       return this._entries.iterator();
/*     */     }
/*     */     
/*     */     public JarEntry getEntryByName(String param1String) {
/* 321 */       return (JarEntry)this._nameToEntryMap.get(param1String);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static boolean differs(InputStream param1InputStream1, InputStream param1InputStream2) throws IOException {
/* 329 */       int i = 0;
/*     */       
/* 331 */       byte b = 0;
/*     */       
/* 333 */       while (i != -1) {
/* 334 */         i = param1InputStream2.read(JarDiff.newBytes);
/* 335 */         int j = param1InputStream1.read(JarDiff.oldBytes);
/*     */         
/* 337 */         if (i != j) {
/* 338 */           if (JarDiff._debug) {
/* 339 */             System.out.println("\tread sizes differ: " + i + " " + j + " total " + b);
/*     */           }
/*     */           
/* 342 */           return true;
/*     */         } 
/* 344 */         if (i > 0) {
/* 345 */           while (--i >= 0) {
/* 346 */             b++;
/* 347 */             if (JarDiff.newBytes[i] != JarDiff.oldBytes[i]) {
/* 348 */               if (JarDiff._debug) {
/* 349 */                 System.out.println("\tbytes differ at " + b);
/*     */               }
/*     */               
/* 352 */               return true;
/*     */             } 
/*     */           } 
/* 355 */           i = 0;
/*     */         } 
/*     */       } 
/* 358 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getBestMatch(JarFile2 param1JarFile2, JarEntry param1JarEntry) throws IOException {
/* 363 */       if (contains(param1JarFile2, param1JarEntry)) {
/* 364 */         return param1JarEntry.getName();
/*     */       }
/*     */ 
/*     */       
/* 368 */       return hasSameContent(param1JarFile2, param1JarEntry);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(JarFile2 param1JarFile2, JarEntry param1JarEntry) throws IOException {
/* 373 */       JarEntry jarEntry = getEntryByName(param1JarEntry.getName());
/*     */ 
/*     */       
/* 376 */       if (jarEntry == null) {
/* 377 */         return false;
/*     */       }
/*     */       
/* 380 */       if (jarEntry.getCrc() != param1JarEntry.getCrc()) {
/* 381 */         return false;
/*     */       }
/*     */       
/* 384 */       InputStream inputStream1 = getJarFile().getInputStream(jarEntry);
/* 385 */       InputStream inputStream2 = param1JarFile2.getJarFile().getInputStream(param1JarEntry);
/* 386 */       boolean bool = differs(inputStream1, inputStream2);
/*     */       
/* 388 */       return !bool;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String hasSameContent(JarFile2 param1JarFile2, JarEntry param1JarEntry) throws IOException {
/* 394 */       String str = null;
/*     */       
/* 396 */       Long long_ = new Long(param1JarEntry.getCrc());
/*     */ 
/*     */       
/* 399 */       if (this._crcToEntryMap.containsKey(long_)) {
/*     */         
/* 401 */         LinkedList linkedList = (LinkedList)this._crcToEntryMap.get(long_);
/*     */         
/* 403 */         ListIterator listIterator = linkedList.listIterator(0);
/* 404 */         if (listIterator != null) {
/* 405 */           while (listIterator.hasNext()) {
/* 406 */             JarEntry jarEntry = listIterator.next();
/*     */ 
/*     */             
/* 409 */             InputStream inputStream1 = getJarFile().getInputStream(jarEntry);
/* 410 */             InputStream inputStream2 = param1JarFile2.getJarFile().getInputStream(param1JarEntry);
/*     */             
/* 412 */             if (!differs(inputStream1, inputStream2)) {
/* 413 */               str = jarEntry.getName();
/* 414 */               return str;
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } 
/*     */       
/* 420 */       return str;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void index() throws IOException {
/* 429 */       Enumeration enumeration = this._jar.entries();
/*     */       
/* 431 */       this._nameToEntryMap = new HashMap();
/* 432 */       this._crcToEntryMap = new HashMap();
/*     */       
/* 434 */       this._entries = new ArrayList();
/* 435 */       if (JarDiff._debug) {
/* 436 */         System.out.println("indexing: " + this._jar.getName());
/*     */       }
/* 438 */       if (enumeration != null) {
/* 439 */         while (enumeration.hasMoreElements()) {
/* 440 */           JarEntry jarEntry = enumeration.nextElement();
/*     */           
/* 442 */           long l = jarEntry.getCrc();
/*     */           
/* 444 */           Long long_ = new Long(l);
/*     */           
/* 446 */           if (JarDiff._debug) {
/* 447 */             System.out.println("\t" + jarEntry.getName() + " CRC " + l);
/*     */           }
/*     */ 
/*     */           
/* 451 */           this._nameToEntryMap.put(jarEntry.getName(), jarEntry);
/* 452 */           this._entries.add(jarEntry);
/*     */ 
/*     */           
/* 455 */           if (this._crcToEntryMap.containsKey(long_)) {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 460 */             LinkedList linkedList1 = (LinkedList)this._crcToEntryMap.get(long_);
/*     */ 
/*     */             
/* 463 */             linkedList1.add(jarEntry);
/*     */ 
/*     */             
/* 466 */             this._crcToEntryMap.put(long_, linkedList1);
/*     */ 
/*     */             
/*     */             continue;
/*     */           } 
/*     */           
/* 472 */           LinkedList linkedList = new LinkedList();
/* 473 */           linkedList.add(jarEntry);
/*     */ 
/*     */           
/* 476 */           this._crcToEntryMap.put(long_, linkedList);
/*     */         } 
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void showHelp() {
/* 487 */     System.out.println("JarDiff: [-nonminimal (for backward compatibility with 1.0.1/1.0] [-creatediff | -applydiff] [-output file] old.jar new.jar");
/*     */   }
/*     */ 
/*     */   
/*     */   public static void main(String[] paramArrayOfString) throws IOException {
/* 492 */     boolean bool1 = true;
/* 493 */     boolean bool2 = true;
/* 494 */     String str = "out.jardiff";
/*     */     
/* 496 */     for (byte b = 0; b < paramArrayOfString.length; b++) {
/*     */       
/* 498 */       if (paramArrayOfString[b].equals("-nonminimal") || paramArrayOfString[b].equals("-n")) {
/*     */         
/* 500 */         bool2 = false;
/*     */       }
/* 502 */       else if (paramArrayOfString[b].equals("-creatediff") || paramArrayOfString[b].equals("-c")) {
/*     */         
/* 504 */         bool1 = true;
/*     */       }
/* 506 */       else if (paramArrayOfString[b].equals("-applydiff") || paramArrayOfString[b].equals("-a")) {
/*     */         
/* 508 */         bool1 = false;
/*     */       }
/* 510 */       else if (paramArrayOfString[b].equals("-debug") || paramArrayOfString[b].equals("-d")) {
/*     */         
/* 512 */         _debug = true;
/*     */       }
/* 514 */       else if (paramArrayOfString[b].equals("-output") || paramArrayOfString[b].equals("-o")) {
/*     */         
/* 516 */         if (++b < paramArrayOfString.length) {
/* 517 */           str = paramArrayOfString[b];
/*     */         }
/*     */       }
/* 520 */       else if (paramArrayOfString[b].equals("-applydiff") || paramArrayOfString[b].equals("-a")) {
/*     */         
/* 522 */         bool1 = false;
/*     */       } else {
/*     */         
/* 525 */         if (b + 2 != paramArrayOfString.length) {
/* 526 */           showHelp();
/* 527 */           System.exit(0);
/*     */         } 
/* 529 */         if (bool1) {
/*     */           try {
/* 531 */             FileOutputStream fileOutputStream = new FileOutputStream(str);
/*     */             
/* 533 */             createPatch(paramArrayOfString[b], paramArrayOfString[b + 1], fileOutputStream, bool2);
/*     */             
/* 535 */             fileOutputStream.close();
/* 536 */           } catch (IOException iOException) {
/*     */             try {
/* 538 */               System.out.println(getResources().getString("jardiff.error.create") + " " + iOException);
/* 539 */             } catch (MissingResourceException missingResourceException) {}
/*     */           } 
/*     */         } else {
/*     */ 
/*     */           
/*     */           try {
/* 545 */             FileOutputStream fileOutputStream = new FileOutputStream(str);
/*     */             
/* 547 */             (new JarDiffPatcher()).applyPatch(null, paramArrayOfString[b], paramArrayOfString[b + 1], fileOutputStream);
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 552 */             fileOutputStream.close();
/* 553 */           } catch (IOException iOException) {
/*     */             try {
/* 555 */               System.out.println(getResources().getString("jardiff.error.apply") + " " + iOException);
/* 556 */             } catch (MissingResourceException missingResourceException) {}
/*     */           } 
/*     */         } 
/*     */         
/* 560 */         System.exit(0);
/*     */       } 
/*     */     } 
/* 563 */     showHelp();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\jardiff\JarDiff.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */