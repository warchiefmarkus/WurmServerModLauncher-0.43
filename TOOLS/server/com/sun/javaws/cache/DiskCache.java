/*      */ package com.sun.javaws.cache;
/*      */ 
/*      */ import com.sun.deploy.util.Trace;
/*      */ import com.sun.deploy.util.TraceLevel;
/*      */ import com.sun.javaws.jnl.IconDesc;
/*      */ import com.sun.javaws.jnl.InformationDesc;
/*      */ import com.sun.javaws.jnl.JARDesc;
/*      */ import com.sun.javaws.jnl.LaunchDesc;
/*      */ import com.sun.javaws.jnl.LaunchDescFactory;
/*      */ import com.sun.javaws.jnl.ResourcesDesc;
/*      */ import com.sun.javaws.util.VersionID;
/*      */ import com.sun.javaws.util.VersionString;
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Comparator;
/*      */ import java.util.Date;
/*      */ import java.util.Iterator;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.StringTokenizer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class DiskCache
/*      */ {
/*      */   private static final int BUF_SIZE = 32768;
/*      */   static final char DIRECTORY_TYPE = 'D';
/*      */   static final char TEMP_TYPE = 'X';
/*      */   static final char VERSION_TYPE = 'V';
/*      */   static final char INDIRECT_TYPE = 'I';
/*      */   static final char RESOURCE_TYPE = 'R';
/*      */   static final char APPLICATION_TYPE = 'A';
/*      */   static final char EXTENSION_TYPE = 'E';
/*      */   static final char MUFFIN_TYPE = 'P';
/*      */   private File _baseDir;
/*      */   static final char MAIN_FILE_TAG = 'M';
/*      */   static final char NATIVELIB_FILE_TAG = 'N';
/*      */   static final char TIMESTAMP_FILE_TAG = 'T';
/*      */   static final char CERTIFICATE_FILE_TAG = 'C';
/*      */   static final char LAP_FILE_TAG = 'L';
/*      */   static final char MAPPED_IMAGE_FILE_TAG = 'B';
/*      */   static final char MUFFIN_ATTR_FILE_TAG = 'U';
/*      */   static final int MUFFIN_TAG_INDEX = 0;
/*      */   static final int MUFFIN_MAXSIZE_INDEX = 1;
/*      */   private static final String LAST_ACCESS_FILE = "lastAccessed";
/*      */   private static final String ORPHAN_LIST_FILE = "orphans";
/*      */   private static final String BEGIN_CERT_MARK = "-----BEGIN CERTIFICATE-----";
/*      */   private static final String END_CERT_MARK = "-----END CERTIFICATE-----";
/*      */   
/*      */   public DiskCache(File paramFile) {
/*   85 */     this._baseDir = paramFile;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   long getLastUpdate() {
/*   94 */     File file = new File(this._baseDir, "lastAccessed");
/*   95 */     return file.lastModified();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void recordLastUpdate() {
/*  101 */     File file = new File(this._baseDir, "lastAccessed");
/*      */     try {
/*  103 */       FileOutputStream fileOutputStream = new FileOutputStream(file);
/*  104 */       fileOutputStream.write(46);
/*  105 */       fileOutputStream.close();
/*  106 */     } catch (IOException iOException) {}
/*      */   }
/*      */   
/*      */   boolean canWrite() {
/*  110 */     boolean bool = this._baseDir.canWrite();
/*      */     
/*  112 */     if (!bool) {
/*  113 */       Trace.println("Cannot write to cache: " + this._baseDir.getAbsolutePath(), TraceLevel.BASIC);
/*      */     }
/*      */ 
/*      */     
/*  117 */     return bool;
/*      */   }
/*      */ 
/*      */   
/*      */   String getBaseDirForHost(URL paramURL) {
/*      */     try {
/*  123 */       URL uRL = new URL(paramURL.getProtocol(), paramURL.getHost(), paramURL.getPort(), "");
/*  124 */       String str = keyToFileLocation('R', 'M', uRL, null);
/*  125 */       int i = str.lastIndexOf(File.separator);
/*  126 */       return str.substring(0, i);
/*  127 */     } catch (MalformedURLException malformedURLException) {
/*  128 */       Trace.ignoredException(malformedURLException);
/*      */       
/*  130 */       return null;
/*      */     } 
/*      */   }
/*      */   private void removeEmptyDirs(URL paramURL) {
/*  134 */     String str = getBaseDirForHost(paramURL);
/*  135 */     if (str != null) {
/*  136 */       removeEmptyDirs(new File(str));
/*      */     }
/*      */   }
/*      */   
/*      */   private void removeEmptyDirs(File paramFile) {
/*  141 */     if (paramFile.isDirectory()) {
/*  142 */       File[] arrayOfFile = paramFile.listFiles();
/*  143 */       boolean bool = false;
/*  144 */       if (arrayOfFile != null) {
/*  145 */         for (byte b = 0; b < arrayOfFile.length; b++) {
/*  146 */           removeEmptyDirs(arrayOfFile[b]);
/*  147 */           if (arrayOfFile[b].exists()) {
/*  148 */             bool = true;
/*      */           }
/*      */         } 
/*      */       }
/*  152 */       if (!bool)
/*  153 */         try { paramFile.delete(); }
/*  154 */         catch (Exception exception)
/*  155 */         { Trace.ignoredException(exception); }
/*      */          
/*      */     } 
/*      */   }
/*      */   
/*      */   private File getOrphanFileForHost(URL paramURL) {
/*      */     try {
/*  162 */       return new File(getBaseDirForHost(paramURL), "orphans");
/*  163 */     } catch (Exception exception) {
/*  164 */       Trace.ignoredException(exception);
/*      */       
/*  166 */       return null;
/*      */     } 
/*      */   }
/*      */   private void removeOrphans(URL paramURL) {
/*  170 */     File file = getOrphanFileForHost(paramURL);
/*  171 */     if (file != null && file.exists()) {
/*  172 */       BufferedReader bufferedReader = null;
/*  173 */       PrintStream printStream = null;
/*  174 */       boolean bool = false;
/*  175 */       ArrayList arrayList = new ArrayList();
/*      */       try {
/*  177 */         FileInputStream fileInputStream = new FileInputStream(file);
/*  178 */         bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
/*      */         String str;
/*  180 */         while ((str = bufferedReader.readLine()) != null) {
/*  181 */           arrayList.add(str);
/*      */         }
/*  183 */         for (int i = arrayList.size() - 1; i >= 0; i--) {
/*  184 */           File file1 = new File(arrayList.get(i));
/*  185 */           file1.delete();
/*  186 */           if (!file1.exists()) {
/*  187 */             bool = true;
/*  188 */             arrayList.remove(i);
/*      */           } 
/*      */         } 
/*  191 */       } catch (IOException iOException) {
/*  192 */         Trace.ignoredException(iOException);
/*      */       } finally {
/*  194 */         if (bufferedReader != null)
/*  195 */           try { bufferedReader.close(); }
/*  196 */           catch (IOException iOException)
/*  197 */           { Trace.ignoredException(iOException); }
/*      */            
/*      */       } 
/*  200 */       if (bool) {
/*  201 */         try { if (arrayList.isEmpty()) {
/*  202 */             Trace.println("emptying orphans file", TraceLevel.CACHE);
/*  203 */             file.delete();
/*      */           } else {
/*  205 */             printStream = new PrintStream(new FileOutputStream(file));
/*  206 */             for (byte b = 0; b < arrayList.size(); b++) {
/*      */               
/*  208 */               Trace.println("Remaining orphan: " + arrayList.get(b), TraceLevel.CACHE);
/*  209 */               printStream.println(arrayList.get(b));
/*      */             } 
/*      */           }  }
/*  212 */         catch (Exception exception)
/*  213 */         { Trace.ignoredException(exception); }
/*      */         finally
/*  215 */         { if (printStream != null) {
/*  216 */             printStream.close();
/*      */           } }
/*      */       
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private void addOrphan(URL paramURL, File paramFile) {
/*  224 */     Trace.println("addOrphan: " + paramFile, TraceLevel.CACHE);
/*  225 */     File file = getOrphanFileForHost(paramURL);
/*  226 */     PrintStream printStream = null;
/*  227 */     if (file != null) {
/*  228 */       try { printStream = new PrintStream(new FileOutputStream(file.getPath(), true));
/*  229 */         printStream.println(paramFile.getCanonicalPath()); }
/*  230 */       catch (Exception exception)
/*  231 */       { Trace.ignoredException(exception); }
/*      */       finally
/*  233 */       { if (printStream != null) printStream.close();
/*      */          }
/*      */     
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
/*      */   File getTempCacheFile(URL paramURL, String paramString) throws IOException {
/*  247 */     String str = keyToFileLocation('X', 'M', paramURL, paramString);
/*      */     
/*  249 */     File file1 = new File(str);
/*  250 */     File file2 = file1.getParentFile();
/*  251 */     file2.mkdirs();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  256 */     return File.createTempFile("java-" + file1.getName(), "tmp", file2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   File createNativeLibDir(URL paramURL, String paramString) throws IOException {
/*  262 */     File file = getFileFromCache('R', 'N', paramURL, paramString, false);
/*      */     
/*  264 */     file.mkdirs();
/*  265 */     return file;
/*      */   }
/*      */   File getNativeLibDir(URL paramURL, String paramString) throws IOException {
/*  268 */     return getFileFromCache('R', 'N', paramURL, paramString, false);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void insertMuffinEntry(URL paramURL, File paramFile, int paramInt, long paramLong) throws IOException {
/*  274 */     File file1 = getFileFromCache('P', 'M', paramURL, null, false);
/*  275 */     if (file1.exists()) {
/*  276 */       paramFile.delete();
/*  277 */       throw new IOException("insert failed in cache: target already exixts");
/*      */     } 
/*      */ 
/*      */     
/*  281 */     File file2 = file1.getParentFile();
/*  282 */     if (file2 != null) {
/*  283 */       file2.mkdirs();
/*      */     }
/*  285 */     if (!paramFile.renameTo(file1)) {
/*  286 */       throw new IOException("rename failed in cache");
/*      */     }
/*      */     
/*  289 */     putMuffinAttributes(paramURL, paramInt, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   long getMuffinSize(URL paramURL) throws IOException {
/*  294 */     long l = 0L;
/*  295 */     File file = getFileFromCache('P', 'M', paramURL, null, true);
/*      */     
/*  297 */     if (file != null && file.exists()) {
/*  298 */       l += file.length();
/*      */     }
/*  300 */     return l;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void insertEntry(char paramChar, URL paramURL, String paramString, File paramFile, long paramLong) throws IOException {
/*  311 */     putTimeStamp(paramChar, paramURL, paramString, paramLong);
/*      */ 
/*      */     
/*  314 */     putFileInCache(paramChar, 'M', paramURL, paramString, paramFile);
/*      */ 
/*      */     
/*  317 */     recordLastUpdate();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   File putMappedImage(URL paramURL, String paramString, File paramFile) throws IOException {
/*  327 */     if (paramFile.getPath().endsWith(".ico")) {
/*  328 */       String str = paramURL.getFile();
/*  329 */       if (!str.endsWith(".ico")) {
/*  330 */         str = str + ".ico";
/*  331 */         paramURL = new URL(paramURL.getProtocol(), paramURL.getHost(), paramURL.getPort(), str);
/*      */       } 
/*      */     } 
/*      */     
/*  335 */     File file = putFileInCache('R', 'B', paramURL, paramString, paramFile);
/*      */ 
/*      */ 
/*      */     
/*  339 */     recordLastUpdate();
/*  340 */     return file;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   File getMappedImage(char paramChar1, char paramChar2, URL paramURL, String paramString, boolean paramBoolean) throws IOException {
/*  350 */     File file = getFileFromCache(paramChar1, paramChar2, paramURL, paramString, paramBoolean);
/*  351 */     if (file == null || !file.exists()) {
/*  352 */       String str = paramURL.getFile();
/*  353 */       if (!str.endsWith(".ico")) {
/*  354 */         str = str + ".ico";
/*  355 */         paramURL = new URL(paramURL.getProtocol(), paramURL.getHost(), paramURL.getPort(), str);
/*  356 */         file = getFileFromCache(paramChar1, paramChar2, paramURL, paramString, paramBoolean);
/*      */       } 
/*      */     } 
/*  359 */     return file;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void putLaunchFile(char paramChar, URL paramURL, String paramString1, String paramString2) throws IOException {
/*  366 */     byte[] arrayOfByte = paramString2.getBytes("UTF8");
/*  367 */     storeAtomic(paramChar, 'M', paramURL, paramString1, arrayOfByte);
/*  368 */     putTimeStamp(paramChar, paramURL, paramString1, (new Date()).getTime());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   String getLaunchFile(char paramChar, URL paramURL, String paramString1, String paramString2) throws IOException {
/*  375 */     byte[] arrayOfByte = getEntryContent(paramChar, 'M', paramURL, paramString1);
/*  376 */     if (arrayOfByte == null) return null; 
/*  377 */     return new String(arrayOfByte, "UTF8");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void putMuffinAttributes(URL paramURL, int paramInt, long paramLong) throws IOException {
/*  386 */     PrintStream printStream = new PrintStream(getOutputStream('P', 'U', paramURL, null));
/*      */     try {
/*  388 */       printStream.println(paramInt);
/*  389 */       printStream.println(paramLong);
/*      */     } finally {
/*  391 */       if (printStream != null) printStream.close();
/*      */     
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   void putTimeStamp(char paramChar, URL paramURL, String paramString, long paramLong) throws IOException {
/*  398 */     if (paramLong == 0L) paramLong = (new Date()).getTime(); 
/*  399 */     PrintStream printStream = new PrintStream(getOutputStream(paramChar, 'T', paramURL, paramString));
/*      */     try {
/*  401 */       printStream.println(paramLong);
/*  402 */       printStream.println("# " + new Date(paramLong));
/*      */     } finally {
/*  404 */       printStream.close();
/*      */     } 
/*      */   }
/*      */   
/*      */   long[] getMuffinAttributes(URL paramURL) throws IOException {
/*  409 */     BufferedReader bufferedReader = null;
/*  410 */     long l1 = -1L;
/*  411 */     long l2 = -1L;
/*      */     try {
/*  413 */       InputStream inputStream = getInputStream('P', 'U', paramURL, null);
/*  414 */       bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
/*  415 */       String str = bufferedReader.readLine();
/*      */       try {
/*  417 */         l1 = Integer.parseInt(str);
/*  418 */       } catch (NumberFormatException numberFormatException) {
/*  419 */         throw new IOException(numberFormatException.getMessage());
/*      */       } 
/*  421 */       str = bufferedReader.readLine();
/*      */       try {
/*  423 */         l2 = Long.parseLong(str);
/*  424 */       } catch (NumberFormatException numberFormatException) {
/*  425 */         throw new IOException(numberFormatException.getMessage());
/*      */       } 
/*      */     } finally {
/*  428 */       if (bufferedReader != null) bufferedReader.close(); 
/*      */     } 
/*  430 */     return new long[] { l1, l2 };
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   long getTimeStamp(char paramChar, URL paramURL, String paramString) {
/*  436 */     BufferedReader bufferedReader = null;
/*      */     try {
/*  438 */       InputStream inputStream = getInputStream(paramChar, 'T', paramURL, paramString);
/*  439 */       bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
/*  440 */       String str = bufferedReader.readLine();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*  446 */     catch (IOException iOException) {
/*  447 */       return 0L;
/*      */     } finally {
/*      */       try {
/*  450 */         if (bufferedReader != null) {
/*  451 */           bufferedReader.close();
/*      */         }
/*  453 */       } catch (IOException iOException) {
/*  454 */         Trace.ignoredException(iOException);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   DiskCacheEntry getMuffinEntry(char paramChar, URL paramURL) throws IOException {
/*  463 */     File file1 = getFileFromCache(paramChar, 'M', paramURL, null, true);
/*  464 */     if (file1 == null) {
/*  465 */       return null;
/*      */     }
/*  467 */     File file2 = getFileFromCache(paramChar, 'U', paramURL, null, true);
/*  468 */     return new DiskCacheEntry(paramChar, paramURL, null, file1, -1L, null, null, file2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   DiskCacheEntry getCacheEntry(char paramChar, URL paramURL, String paramString) throws IOException {
/*  479 */     File file1 = getFileFromCache(paramChar, 'M', paramURL, paramString, true);
/*  480 */     if (file1 == null) {
/*  481 */       return null;
/*      */     }
/*      */     
/*  484 */     File file2 = getFileFromCache(paramChar, 'N', paramURL, paramString, true);
/*      */ 
/*      */     
/*  487 */     File file3 = getMappedImage(paramChar, 'B', paramURL, paramString, true);
/*      */ 
/*      */ 
/*      */     
/*  491 */     long l = getTimeStamp(paramChar, paramURL, paramString);
/*      */     
/*  493 */     return new DiskCacheEntry(paramChar, paramURL, paramString, file1, l, file2, file3, null);
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
/*      */   DiskCacheEntry[] getCacheEntries(char paramChar, URL paramURL, String paramString, boolean paramBoolean) throws IOException {
/*  510 */     if (paramString == null) {
/*  511 */       DiskCacheEntry diskCacheEntry1 = getCacheEntry(paramChar, paramURL, null);
/*  512 */       if (diskCacheEntry1 == null) {
/*  513 */         return new DiskCacheEntry[0];
/*      */       }
/*  515 */       return new DiskCacheEntry[] { diskCacheEntry1 };
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  520 */     ArrayList arrayList = getCacheEntries(paramChar, paramURL);
/*      */     
/*  522 */     VersionString versionString = new VersionString(paramString);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  527 */     DiskCacheEntry diskCacheEntry = null;
/*  528 */     Iterator iterator = arrayList.iterator();
/*  529 */     while (iterator.hasNext()) {
/*  530 */       DiskCacheEntry diskCacheEntry1 = iterator.next();
/*  531 */       String str = diskCacheEntry1.getVersionId();
/*      */       
/*  533 */       if (str == null) {
/*  534 */         iterator.remove(); continue;
/*  535 */       }  if (!versionString.contains(str)) {
/*  536 */         if (diskCacheEntry == null && versionString.containsGreaterThan(str)) {
/*  537 */           diskCacheEntry = diskCacheEntry1;
/*      */         }
/*  539 */         iterator.remove();
/*      */       } 
/*      */     } 
/*  542 */     if (!paramBoolean && arrayList.size() == 0 && diskCacheEntry != null) {
/*  543 */       arrayList.add(diskCacheEntry);
/*      */     }
/*      */     
/*  546 */     DiskCacheEntry[] arrayOfDiskCacheEntry = new DiskCacheEntry[arrayList.size()];
/*  547 */     return arrayList.<DiskCacheEntry>toArray(arrayOfDiskCacheEntry);
/*      */   }
/*      */   
/*      */   void removeMuffinEntry(DiskCacheEntry paramDiskCacheEntry) {
/*  551 */     char c = paramDiskCacheEntry.getType();
/*  552 */     URL uRL = paramDiskCacheEntry.getLocation();
/*  553 */     String str = paramDiskCacheEntry.getVersionId();
/*  554 */     deleteEntry(c, 'M', uRL, str);
/*  555 */     deleteEntry(c, 'U', uRL, str);
/*      */   }
/*      */ 
/*      */   
/*      */   void removeEntry(DiskCacheEntry paramDiskCacheEntry) {
/*  560 */     char c = paramDiskCacheEntry.getType();
/*  561 */     URL uRL = paramDiskCacheEntry.getLocation();
/*      */     
/*  563 */     removeOrphans(uRL);
/*  564 */     String str = paramDiskCacheEntry.getVersionId();
/*  565 */     deleteEntry(c, 'M', uRL, str);
/*  566 */     deleteEntry(c, 'T', uRL, str);
/*  567 */     deleteEntry(c, 'C', uRL, str);
/*  568 */     deleteEntry(c, 'N', uRL, str);
/*  569 */     deleteEntry(c, 'B', uRL, str);
/*  570 */     deleteEntry(c, 'L', uRL, str);
/*      */ 
/*      */ 
/*      */     
/*  574 */     if (c == 'R') {
/*  575 */       deleteEntry('I', 'M', uRL, str);
/*      */     }
/*  577 */     removeEmptyDirs(uRL);
/*  578 */     recordLastUpdate();
/*      */   }
/*      */   
/*      */   private void deleteEntry(char paramChar1, char paramChar2, URL paramURL, String paramString) {
/*  582 */     File file = null;
/*      */     try {
/*  584 */       if (paramChar2 == 'B') {
/*  585 */         file = getMappedImage(paramChar1, paramChar2, paramURL, paramString, false);
/*      */       } else {
/*  587 */         file = getFileFromCache(paramChar1, paramChar2, paramURL, paramString, false);
/*      */       } 
/*  589 */       deleteFile(file);
/*  590 */     } catch (IOException iOException) {
/*  591 */       Trace.ignoredException(iOException);
/*      */     } 
/*  593 */     if (file != null && file.exists())
/*      */     {
/*  595 */       if (paramChar1 == 'R' && paramChar2 == 'M') {
/*  596 */         addOrphan(paramURL, file);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   DiskCacheEntry getCacheEntryFromFile(File paramFile) {
/*  602 */     DiskCacheEntry diskCacheEntry = fileToEntry(paramFile);
/*  603 */     if (diskCacheEntry != null) {
/*      */       try {
/*  605 */         if (diskCacheEntry.getType() == 'P') {
/*  606 */           return getMuffinEntry(diskCacheEntry.getType(), diskCacheEntry.getLocation());
/*      */         }
/*  608 */         return getCacheEntry(diskCacheEntry.getType(), diskCacheEntry.getLocation(), diskCacheEntry.getVersionId());
/*      */       
/*      */       }
/*  611 */       catch (IOException iOException) {
/*  612 */         Trace.ignoredException(iOException);
/*      */       } 
/*      */     }
/*  615 */     return diskCacheEntry;
/*      */   }
/*      */   
/*      */   boolean isMainMuffinFile(File paramFile) throws IOException {
/*  619 */     DiskCacheEntry diskCacheEntry = fileToEntry(paramFile);
/*  620 */     return paramFile.equals(getFileFromCache('P', 'M', diskCacheEntry.getLocation(), null, false));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ArrayList getCacheEntries(char paramChar, URL paramURL) throws IOException {
/*  628 */     ArrayList arrayList = new ArrayList();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  637 */     String str1 = keyToFileLocation(paramChar, 'M', paramURL, "MATCH");
/*      */ 
/*      */ 
/*      */     
/*  641 */     int i = str1.indexOf(File.separator + 'V' + "MATCH" + File.separator);
/*  642 */     if (i == -1) {
/*  643 */       throw new IllegalStateException("the javaws cache is corrupted");
/*      */     }
/*  645 */     String str2 = str1.substring(0, i);
/*  646 */     File file1 = new File(str2);
/*  647 */     File[] arrayOfFile = file1.listFiles();
/*  648 */     if (arrayOfFile == null) return arrayList;  int j;
/*  649 */     for (j = 0; j < arrayOfFile.length; j++) {
/*  650 */       String str = arrayOfFile[j].getName();
/*  651 */       if (arrayOfFile[j].isDirectory() && str.length() > 1 && str.charAt(0) == 'V') {
/*      */         
/*  653 */         String str3 = str.substring(1);
/*  654 */         File file = getFileFromCache(paramChar, 'M', paramURL, str3, true);
/*      */         
/*  656 */         if (file != null) {
/*  657 */           DiskCacheEntry diskCacheEntry = getCacheEntry(paramChar, paramURL, str3);
/*  658 */           arrayList.add(diskCacheEntry);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  663 */     j = arrayList.size();
/*  664 */     if (j > 1) {
/*  665 */       DiskCacheEntry[] arrayOfDiskCacheEntry = new DiskCacheEntry[j];
/*  666 */       arrayOfDiskCacheEntry = arrayList.<DiskCacheEntry>toArray(arrayOfDiskCacheEntry);
/*      */       
/*  668 */       Arrays.sort(arrayOfDiskCacheEntry, new Comparator(this) { private final DiskCache this$0;
/*      */             public int compare(Object param1Object1, Object param1Object2) {
/*  670 */               DiskCacheEntry diskCacheEntry1 = (DiskCacheEntry)param1Object1;
/*  671 */               DiskCacheEntry diskCacheEntry2 = (DiskCacheEntry)param1Object2;
/*  672 */               VersionID versionID1 = new VersionID(diskCacheEntry1.getVersionId());
/*  673 */               VersionID versionID2 = new VersionID(diskCacheEntry2.getVersionId());
/*  674 */               return versionID1.isGreaterThan(versionID2) ? -1 : 1;
/*      */             } }
/*      */         );
/*      */       
/*  678 */       for (byte b = 0; b < j; b++) {
/*  679 */         arrayList.set(b, arrayOfDiskCacheEntry[b]);
/*      */       }
/*      */     } 
/*      */     
/*  683 */     File file2 = getFileFromCache(paramChar, 'M', paramURL, null, true);
/*  684 */     if (file2 != null) {
/*  685 */       DiskCacheEntry diskCacheEntry = getCacheEntry(paramChar, paramURL, null);
/*  686 */       arrayList.add(diskCacheEntry);
/*      */     } 
/*      */     
/*  689 */     return arrayList;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getCacheVersions(char paramChar, URL paramURL) throws IOException {
/*  696 */     ArrayList arrayList = new ArrayList();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  705 */     String str1 = keyToFileLocation(paramChar, 'M', paramURL, "MATCH");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  711 */     int i = str1.indexOf(File.separator + 'V' + "MATCH" + File.separator);
/*      */     
/*  713 */     if (i == -1) {
/*  714 */       throw new IllegalStateException("the javaws cache is corrupted");
/*      */     }
/*  716 */     String str2 = str1.substring(0, i);
/*  717 */     File file = new File(str2);
/*  718 */     File[] arrayOfFile = file.listFiles();
/*  719 */     if (arrayOfFile != null) {
/*  720 */       for (byte b = 0; b < arrayOfFile.length; b++) {
/*  721 */         String str = arrayOfFile[b].getName();
/*  722 */         if (arrayOfFile[b].isDirectory() && str.length() > 1 && str.charAt(0) == 'V') {
/*      */           
/*  724 */           String str3 = str.substring(1);
/*  725 */           File file1 = getFileFromCache(paramChar, 'M', paramURL, str3, true);
/*      */           
/*  727 */           if (file1 != null) {
/*  728 */             arrayList.add(str3);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/*  733 */     return arrayList.<String>toArray(new String[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void visitDiskCache(char paramChar, DiskCacheVisitor paramDiskCacheVisitor) throws IOException {
/*  743 */     visitDiskCacheHelper(this._baseDir, 0, paramChar, paramDiskCacheVisitor);
/*      */   }
/*      */   
/*      */   private void visitDiskCacheHelper(File paramFile, int paramInt, char paramChar, DiskCacheVisitor paramDiskCacheVisitor) throws IOException {
/*  747 */     String str = paramFile.getName();
/*      */     
/*  749 */     if (paramFile.isDirectory() && (str.length() <= 2 || paramFile.getName().charAt(1) != 'N')) {
/*      */       
/*  751 */       File[] arrayOfFile = paramFile.listFiles();
/*  752 */       for (byte b = 0; b < arrayOfFile.length; b++) {
/*  753 */         visitDiskCacheHelper(arrayOfFile[b], paramInt + 1, paramChar, paramDiskCacheVisitor);
/*      */       
/*      */       }
/*      */     }
/*  757 */     else if (str.length() > 2 && paramInt > 3) {
/*  758 */       char c1 = str.charAt(0);
/*  759 */       char c2 = str.charAt(1);
/*  760 */       if (c1 == paramChar && c2 == 'M') {
/*  761 */         DiskCacheEntry diskCacheEntry = getCacheEntryFromFile(paramFile);
/*  762 */         if (diskCacheEntry == null) {
/*  763 */           throw new IllegalStateException("the javaws cache is corrupted");
/*      */         }
/*      */         
/*  766 */         paramDiskCacheVisitor.visitEntry(diskCacheEntry);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static class MuffinAccessVisitor
/*      */     implements DiskCacheVisitor {
/*      */     private DiskCache _diskCache;
/*      */     private URL _theURL;
/*  775 */     private URL[] _urls = new URL[255];
/*  776 */     private int _counter = 0;
/*      */     
/*      */     MuffinAccessVisitor(DiskCache param1DiskCache, URL param1URL) {
/*  779 */       this._diskCache = param1DiskCache;
/*  780 */       this._theURL = param1URL;
/*      */     }
/*      */     
/*      */     public void visitEntry(DiskCacheEntry param1DiskCacheEntry) {
/*  784 */       URL uRL = param1DiskCacheEntry.getLocation();
/*  785 */       if (uRL == null)
/*  786 */         return;  if (uRL.getHost().equals(this._theURL.getHost())) {
/*  787 */         this._urls[this._counter++] = uRL;
/*      */       }
/*      */     }
/*      */     
/*      */     public URL[] getAccessibleMuffins() {
/*  792 */       return this._urls;
/*      */     }
/*      */   }
/*      */   
/*      */   File getMuffinFileForURL(URL paramURL) {
/*      */     try {
/*  798 */       return getFileFromCache('P', 'M', paramURL, null, false);
/*  799 */     } catch (IOException iOException) {
/*  800 */       return null;
/*      */     } 
/*      */   }
/*      */   
/*      */   URL[] getAccessibleMuffins(URL paramURL) throws IOException {
/*  805 */     MuffinAccessVisitor muffinAccessVisitor = new MuffinAccessVisitor(this, paramURL);
/*  806 */     visitDiskCache('P', muffinAccessVisitor);
/*  807 */     return muffinAccessVisitor.getAccessibleMuffins();
/*      */   }
/*      */   
/*      */   private static class DeleteVisitor implements DiskCacheVisitor {
/*      */     private DiskCache _diskCache;
/*      */     
/*      */     DeleteVisitor(DiskCache param1DiskCache) {
/*  814 */       this._diskCache = param1DiskCache;
/*      */     }
/*      */     public void visitEntry(DiskCacheEntry param1DiskCacheEntry) {
/*  817 */       this._diskCache.removeEntry(param1DiskCacheEntry);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SizeVisitor
/*      */     implements DiskCacheVisitor {
/*      */     private DiskCache _diskCache;
/*  824 */     long _size = 0L;
/*      */     
/*      */     SizeVisitor(DiskCache param1DiskCache) {
/*  827 */       this._diskCache = param1DiskCache;
/*  828 */       this._size = 0L;
/*      */     }
/*      */     
/*      */     public void visitEntry(DiskCacheEntry param1DiskCacheEntry) {
/*  832 */       if (param1DiskCacheEntry.getDirectory() != null && param1DiskCacheEntry.getDirectory().exists()) {
/*      */         
/*  834 */         File[] arrayOfFile = param1DiskCacheEntry.getDirectory().listFiles();
/*  835 */         for (byte b = 0; b < arrayOfFile.length; b++) {
/*  836 */           this._size += arrayOfFile[b].length();
/*      */         }
/*      */       } else {
/*      */         
/*  840 */         this._size += param1DiskCacheEntry.getFile().length();
/*      */       } 
/*      */     }
/*      */     public long getSize() {
/*  844 */       return this._size;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   long getCacheSize() throws IOException {
/*  850 */     Trace.println("Computing diskcache size: " + this._baseDir.getAbsoluteFile(), TraceLevel.CACHE);
/*      */     
/*  852 */     SizeVisitor sizeVisitor = new SizeVisitor(this);
/*  853 */     visitDiskCache('R', sizeVisitor);
/*  854 */     return sizeVisitor.getSize();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void uninstallCache() {
/*  862 */     deleteFile(this._baseDir);
/*      */ 
/*      */     
/*  865 */     if (this._baseDir.exists()) {
/*  866 */       recordLastUpdate();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void deleteFile(File paramFile) {
/*  875 */     if (paramFile.isDirectory()) {
/*  876 */       File[] arrayOfFile = paramFile.listFiles();
/*  877 */       if (arrayOfFile != null) {
/*  878 */         for (byte b = 0; b < arrayOfFile.length; b++) {
/*  879 */           deleteFile(arrayOfFile[b]);
/*      */         }
/*      */       }
/*      */     } 
/*  883 */     paramFile.delete();
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
/*      */   private OutputStream getOutputStream(char paramChar1, char paramChar2, URL paramURL, String paramString) throws IOException {
/*  896 */     File file = getFileFromCache(paramChar1, paramChar2, paramURL, paramString, false);
/*      */     
/*  898 */     file.getParentFile().mkdirs();
/*  899 */     file.createNewFile();
/*      */     
/*  901 */     recordLastUpdate();
/*      */     
/*  903 */     return new FileOutputStream(file);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private InputStream getInputStream(char paramChar1, char paramChar2, URL paramURL, String paramString) throws IOException {
/*  912 */     return new FileInputStream(getFileFromCache(paramChar1, paramChar2, paramURL, paramString, false));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   byte[] getLapData(char paramChar, URL paramURL, String paramString) throws IOException {
/*  918 */     return getEntryContent(paramChar, 'L', paramURL, paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   void putLapData(char paramChar, URL paramURL, String paramString, byte[] paramArrayOfbyte) throws IOException {
/*  923 */     storeAtomic(paramChar, 'L', paramURL, paramString, paramArrayOfbyte);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private byte[] getEntryContent(char paramChar1, char paramChar2, URL paramURL, String paramString) throws IOException {
/*  930 */     File file = getFileFromCache(paramChar1, paramChar2, paramURL, paramString, true);
/*  931 */     if (file == null) return null;
/*      */     
/*  933 */     long l = file.length();
/*  934 */     if (l > 1073741824L) return null; 
/*  935 */     BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
/*  936 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream((int)l);
/*      */     
/*  938 */     byte[] arrayOfByte = new byte[32768];
/*      */     try {
/*  940 */       int i = bufferedInputStream.read(arrayOfByte);
/*  941 */       while (i >= 0) {
/*  942 */         byteArrayOutputStream.write(arrayOfByte, 0, i);
/*  943 */         i = bufferedInputStream.read(arrayOfByte);
/*      */       } 
/*      */     } finally {
/*  946 */       byteArrayOutputStream.close();
/*  947 */       bufferedInputStream.close();
/*      */     } 
/*      */     
/*  950 */     return byteArrayOutputStream.toByteArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void storeAtomic(char paramChar1, char paramChar2, URL paramURL, String paramString, byte[] paramArrayOfbyte) throws IOException {
/*  958 */     File file = getTempCacheFile(paramURL, paramString);
/*  959 */     ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(paramArrayOfbyte);
/*  960 */     BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
/*  961 */     byte[] arrayOfByte = new byte[32768];
/*      */     try {
/*  963 */       int i = byteArrayInputStream.read(arrayOfByte);
/*  964 */       while (i >= 0) {
/*  965 */         bufferedOutputStream.write(arrayOfByte, 0, i);
/*  966 */         i = byteArrayInputStream.read(arrayOfByte);
/*      */       } 
/*      */     } finally {
/*  969 */       bufferedOutputStream.close();
/*  970 */       byteArrayInputStream.close();
/*      */     } 
/*      */ 
/*      */     
/*  974 */     putFileInCache(paramChar1, paramChar2, paramURL, paramString, file);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private File putFileInCache(char paramChar1, char paramChar2, URL paramURL, String paramString, File paramFile) throws IOException {
/*  981 */     File file = new File(keyToFileLocation(paramChar1, paramChar2, paramURL, paramString));
/*      */ 
/*      */     
/*  984 */     removeOrphans(paramURL);
/*      */ 
/*      */     
/*  987 */     file.delete();
/*      */ 
/*      */     
/*  990 */     if (!paramFile.renameTo(file)) {
/*      */       
/*  992 */       deleteEntry(paramChar1, paramChar2, paramURL, paramString);
/*      */       
/*  994 */       if (paramChar1 == 'R' && paramChar2 == 'M') {
/*  995 */         PrintStream printStream = new PrintStream(getOutputStream('I', 'M', paramURL, paramString));
/*      */         
/*      */         try {
/*  998 */           printStream.println(paramFile.getCanonicalPath());
/*      */         } finally {
/* 1000 */           printStream.close();
/*      */         } 
/* 1002 */         return paramFile;
/*      */       } 
/* 1004 */       throw new IOException("rename failed in cache to: " + file);
/*      */     } 
/* 1006 */     if (paramChar1 == 'R' && paramChar2 == 'M') {
/*      */ 
/*      */       
/* 1009 */       File file1 = getFileFromCache('I', paramChar2, paramURL, paramString, false);
/*      */       
/* 1011 */       if (file1.exists()) {
/*      */ 
/*      */         
/* 1014 */         deleteEntry(paramChar1, paramChar2, paramURL, paramString);
/*      */         
/* 1016 */         deleteEntry('I', paramChar2, paramURL, paramString);
/*      */       } 
/*      */     } 
/* 1019 */     return file;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   File getFileFromCache(char paramChar1, char paramChar2, URL paramURL, String paramString, boolean paramBoolean) throws IOException {
/* 1025 */     BufferedReader bufferedReader = null;
/*      */ 
/*      */     
/* 1028 */     if (paramChar1 == 'R' && paramChar2 == 'M') {
/* 1029 */       File file1 = getFileFromCache('I', paramChar2, paramURL, paramString, false);
/*      */       
/* 1031 */       if (file1.exists())
/* 1032 */         try { InputStream inputStream = getInputStream('I', 'M', paramURL, paramString);
/*      */           
/* 1034 */           bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
/* 1035 */           String str = bufferedReader.readLine();
/* 1036 */           File file2 = new File(str);
/* 1037 */           return file2; }
/* 1038 */         catch (IOException iOException)
/*      */         
/*      */         { 
/*      */ 
/*      */           
/* 1043 */           if (paramBoolean) {
/* 1044 */             return null;
/*      */           } }
/*      */         finally
/* 1047 */         { if (bufferedReader != null) {
/* 1048 */             bufferedReader.close();
/*      */           } }
/*      */          
/*      */     } 
/* 1052 */     File file = new File(keyToFileLocation(paramChar1, paramChar2, paramURL, paramString));
/* 1053 */     if (paramBoolean && 
/* 1054 */       !file.exists()) {
/* 1055 */       return null;
/*      */     }
/*      */     
/* 1058 */     return file;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private DiskCacheEntry fileToEntry(File paramFile) {
/* 1065 */     char c = Character.MIN_VALUE;
/* 1066 */     URL uRL = null;
/* 1067 */     String str1 = null;
/* 1068 */     long l = 0L;
/*      */     
/* 1070 */     String str2 = paramFile.getAbsolutePath();
/*      */     
/* 1072 */     String str3 = this._baseDir.getAbsolutePath();
/* 1073 */     if (!str2.startsWith(str3)) return null;
/*      */ 
/*      */     
/* 1076 */     str2 = str2.substring(str3.length());
/*      */     
/* 1078 */     StringTokenizer stringTokenizer = new StringTokenizer(str2, File.separator, false);
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1083 */       String str4 = stringTokenizer.nextToken();
/*      */ 
/*      */       
/* 1086 */       String str5 = stringTokenizer.nextToken();
/* 1087 */       if (str5.length() < 1) return null; 
/* 1088 */       str5 = str5.substring(1);
/*      */ 
/*      */       
/* 1091 */       String str6 = stringTokenizer.nextToken();
/* 1092 */       if (str6.length() < 1) return null; 
/* 1093 */       int i = 0;
/*      */       try {
/* 1095 */         i = Integer.parseInt(str6.substring(1));
/* 1096 */         if (i == 80) i = -1; 
/* 1097 */       } catch (NumberFormatException numberFormatException) {
/* 1098 */         return null;
/*      */       } 
/*      */ 
/*      */       
/* 1102 */       StringBuffer stringBuffer = new StringBuffer();
/* 1103 */       while (stringTokenizer.hasMoreElements()) {
/* 1104 */         String str = stringTokenizer.nextToken();
/* 1105 */         str = removeEscapes(str);
/* 1106 */         if (str.length() < 1) return null; 
/* 1107 */         c = str.charAt(0);
/* 1108 */         if (c == 'V') {
/* 1109 */           str1 = str.substring(1); continue;
/*      */         } 
/* 1111 */         stringBuffer.append('/');
/* 1112 */         stringBuffer.append(str.substring(2));
/*      */       } 
/*      */       
/* 1115 */       uRL = new URL(str4, str5, i, stringBuffer.toString());
/* 1116 */     } catch (MalformedURLException malformedURLException) {
/* 1117 */       return null;
/* 1118 */     } catch (NoSuchElementException noSuchElementException) {
/*      */       
/* 1120 */       return null;
/*      */     } 
/*      */     
/* 1123 */     return new DiskCacheEntry(c, uRL, str1, paramFile, 0L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String removeEscapes(String paramString) {
/* 1130 */     if (paramString == null || paramString.indexOf('&') == -1) return paramString;
/*      */     
/* 1132 */     StringBuffer stringBuffer = new StringBuffer(paramString.length());
/* 1133 */     byte b = 0;
/* 1134 */     while (b < paramString.length() - 1) {
/* 1135 */       char c1 = paramString.charAt(b);
/* 1136 */       char c2 = paramString.charAt(b + 1);
/* 1137 */       if (c1 == '&' && c2 == 'p') {
/*      */         
/* 1139 */         b++;
/* 1140 */         stringBuffer.append('%');
/* 1141 */       } else if (c1 == '&' && c2 == 'c') {
/*      */         
/* 1143 */         b++;
/* 1144 */         stringBuffer.append(':');
/* 1145 */       } else if (c1 != '&' || c2 != '&') {
/*      */ 
/*      */         
/* 1148 */         stringBuffer.append(c1);
/*      */       } 
/* 1150 */       b++;
/*      */     } 
/* 1152 */     if (b < paramString.length()) {
/* 1153 */       stringBuffer.append(paramString.charAt(b));
/*      */     }
/* 1155 */     return stringBuffer.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String keyToFileLocation(char paramChar1, char paramChar2, URL paramURL, String paramString) {
/* 1160 */     StringBuffer stringBuffer = new StringBuffer(paramURL.toString().length() + ((paramString == null) ? 0 : paramString.length()) * 2);
/*      */ 
/*      */     
/* 1163 */     stringBuffer.append(paramURL.getProtocol());
/* 1164 */     stringBuffer.append(File.separatorChar);
/*      */ 
/*      */     
/* 1167 */     stringBuffer.append('D');
/* 1168 */     stringBuffer.append(paramURL.getHost());
/* 1169 */     stringBuffer.append(File.separatorChar);
/*      */ 
/*      */     
/* 1172 */     String str = null;
/* 1173 */     if (paramURL.getPort() == -1 && paramURL.getProtocol().equals("http")) {
/*      */       
/* 1175 */       str = "P80";
/*      */     } else {
/* 1177 */       str = "P" + (new Integer(paramURL.getPort())).toString();
/*      */     } 
/* 1179 */     stringBuffer.append(str);
/* 1180 */     stringBuffer.append(File.separatorChar);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1186 */     if (paramString != null) {
/* 1187 */       stringBuffer.append('V');
/* 1188 */       stringBuffer.append(paramString);
/* 1189 */       stringBuffer.append(File.separatorChar);
/*      */     } 
/*      */ 
/*      */     
/* 1193 */     stringBuffer.append(convertURLfile(paramChar1, paramChar2, paramURL.getFile()));
/*      */     
/* 1195 */     return this._baseDir.getAbsolutePath() + File.separator + stringBuffer.toString();
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
/*      */   private String convertURLfile(char paramChar1, char paramChar2, String paramString) {
/* 1208 */     String str = null; int i;
/* 1209 */     if ((i = paramString.indexOf(";")) != -1) {
/* 1210 */       str = paramString.substring(i);
/* 1211 */       paramString = paramString.substring(0, i);
/*      */     }  int j;
/* 1213 */     if ((j = paramString.indexOf("?")) != -1) {
/* 1214 */       str = paramString.substring(j) + str;
/* 1215 */       paramString = paramString.substring(0, j);
/*      */     } 
/*      */     
/* 1218 */     if (str != null) {
/* 1219 */       Trace.println("     URL: " + paramString + "\n  PARAMS: " + str, TraceLevel.CACHE);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1225 */     StringBuffer stringBuffer = new StringBuffer(paramString.length() * 2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1231 */     int k = -1;
/* 1232 */     for (byte b = 0; b < paramString.length(); b++) {
/* 1233 */       if (paramString.charAt(b) == '/') {
/* 1234 */         stringBuffer.append(File.separatorChar);
/* 1235 */         stringBuffer.append('D');
/* 1236 */         stringBuffer.append('M');
/* 1237 */         k = stringBuffer.length();
/* 1238 */       } else if (paramString.charAt(b) == ':') {
/* 1239 */         stringBuffer.append("&c");
/* 1240 */       } else if (paramString.charAt(b) == '&') {
/* 1241 */         stringBuffer.append("&&");
/* 1242 */       } else if (paramString.charAt(b) == '%') {
/* 1243 */         stringBuffer.append("&p");
/*      */       } else {
/* 1245 */         stringBuffer.append(paramString.charAt(b));
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1251 */     if (k == -1) {
/*      */       
/* 1253 */       stringBuffer.insert(0, paramChar1);
/* 1254 */       stringBuffer.insert(1, paramChar2);
/*      */     } else {
/* 1256 */       stringBuffer.setCharAt(k - 2, paramChar1);
/* 1257 */       stringBuffer.setCharAt(k - 1, paramChar2);
/*      */     } 
/* 1259 */     return stringBuffer.toString();
/*      */   }
/*      */   
/*      */   long getOrphanSize() {
/* 1263 */     long l = 0L;
/*      */     try {
/* 1265 */       Iterator iterator = getOrphans();
/* 1266 */       while (iterator.hasNext()) {
/* 1267 */         l += ((DiskCacheEntry)iterator.next()).getSize();
/*      */       }
/* 1269 */     } catch (Exception exception) {
/* 1270 */       Trace.ignoredException(exception);
/*      */     } 
/* 1272 */     return l;
/*      */   }
/*      */   
/*      */   void cleanResources() {
/*      */     try {
/* 1277 */       Iterator iterator = getOrphans();
/* 1278 */       while (iterator.hasNext()) {
/* 1279 */         removeEntry(iterator.next());
/*      */       }
/* 1281 */     } catch (Exception exception) {
/* 1282 */       Trace.ignoredException(exception);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Iterator getOrphans() {
/* 1290 */     ArrayList arrayList1 = new ArrayList();
/* 1291 */     ArrayList arrayList2 = new ArrayList();
/* 1292 */     DiskCacheVisitor diskCacheVisitor = new DiskCacheVisitor(this, arrayList1) { private final ArrayList val$appResources; private final DiskCache this$0;
/*      */         public void visitEntry(DiskCacheEntry param1DiskCacheEntry) {
/* 1294 */           LaunchDesc launchDesc = null;
/*      */           try {
/* 1296 */             launchDesc = LaunchDescFactory.buildDescriptor(param1DiskCacheEntry.getFile());
/* 1297 */           } catch (Exception exception) {
/* 1298 */             Trace.ignoredException(exception);
/*      */           } 
/* 1300 */           if (launchDesc != null) {
/* 1301 */             ResourcesDesc resourcesDesc = launchDesc.getResources();
/* 1302 */             JARDesc[] arrayOfJARDesc = resourcesDesc.getEagerOrAllJarDescs(true);
/* 1303 */             if (arrayOfJARDesc != null) {
/* 1304 */               for (byte b = 0; b < arrayOfJARDesc.length; b++) {
/*      */                 try {
/* 1306 */                   File file = this.this$0.getFileFromCache('R', 'M', arrayOfJARDesc[b].getLocation(), arrayOfJARDesc[b].getVersion(), false);
/*      */ 
/*      */                   
/* 1309 */                   if (file != null) {
/* 1310 */                     this.val$appResources.add(file);
/*      */                   }
/* 1312 */                 } catch (IOException iOException) {}
/*      */               } 
/*      */             }
/*      */             
/* 1316 */             InformationDesc informationDesc = launchDesc.getInformation();
/* 1317 */             if (informationDesc != null) {
/* 1318 */               IconDesc[] arrayOfIconDesc = informationDesc.getIcons();
/* 1319 */               if (arrayOfIconDesc != null) {
/* 1320 */                 for (byte b = 0; b < arrayOfIconDesc.length; b++) {
/*      */                   try {
/* 1322 */                     File file = this.this$0.getFileFromCache('R', 'M', arrayOfIconDesc[b].getLocation(), arrayOfIconDesc[b].getVersion(), false);
/*      */ 
/*      */                     
/* 1325 */                     if (file != null) {
/* 1326 */                       this.val$appResources.add(file);
/*      */                     }
/* 1328 */                   } catch (IOException iOException) {}
/*      */                 } 
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } }
/*      */       ;
/*      */ 
/*      */     
/*      */     try {
/* 1338 */       visitDiskCache('A', diskCacheVisitor);
/* 1339 */       visitDiskCache('E', diskCacheVisitor);
/*      */       
/* 1341 */       diskCacheVisitor = new DiskCacheVisitor(this, arrayList1, arrayList2) { private final ArrayList val$appResources;
/*      */           public void visitEntry(DiskCacheEntry param1DiskCacheEntry) {
/* 1343 */             if (!this.val$appResources.contains(param1DiskCacheEntry.getFile()))
/* 1344 */               this.val$orphanResources.add(param1DiskCacheEntry); 
/*      */           }
/*      */           private final ArrayList val$orphanResources; private final DiskCache this$0; }
/*      */         ;
/* 1348 */       visitDiskCache('R', diskCacheVisitor);
/* 1349 */       visitDiskCache('I', diskCacheVisitor);
/* 1350 */     } catch (IOException iOException) {
/* 1351 */       Trace.ignoredException(iOException);
/*      */     } 
/* 1353 */     return arrayList2.iterator();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Iterator getJnlpCacheEntries() {
/* 1362 */     ArrayList arrayList = new ArrayList();
/*      */     try {
/* 1364 */       DiskCacheVisitor diskCacheVisitor = new DiskCacheVisitor(this, arrayList) { private final ArrayList val$al; private final DiskCache this$0;
/*      */           public void visitEntry(DiskCacheEntry param1DiskCacheEntry) {
/* 1366 */             this.val$al.add(param1DiskCacheEntry);
/*      */           } }
/*      */         ;
/* 1369 */       visitDiskCache('A', diskCacheVisitor);
/* 1370 */       visitDiskCache('E', diskCacheVisitor);
/* 1371 */     } catch (IOException iOException) {
/*      */       
/* 1373 */       Trace.ignoredException(iOException);
/*      */     } 
/* 1375 */     return arrayList.iterator();
/*      */   }
/*      */   
/*      */   public static interface DiskCacheVisitor {
/*      */     void visitEntry(DiskCacheEntry param1DiskCacheEntry);
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\cache\DiskCache.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */