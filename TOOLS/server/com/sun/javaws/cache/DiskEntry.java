/*    */ package com.sun.javaws.cache;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.net.URL;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DiskEntry
/*    */ {
/*    */   private URL _url;
/*    */   private long _timestamp;
/*    */   private File _file;
/*    */   
/*    */   public DiskEntry(URL paramURL, File paramFile, long paramLong) {
/* 25 */     this._url = paramURL;
/* 26 */     this._timestamp = paramLong;
/* 27 */     this._file = paramFile;
/*    */   }
/*    */   public URL getURL() {
/* 30 */     return this._url;
/*    */   } public long getTimeStamp() {
/* 32 */     return this._timestamp;
/*    */   } public File getFile() {
/* 34 */     return this._file;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\cache\DiskEntry.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */