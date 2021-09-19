/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.HashBasedTable;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Table;
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import javax.annotation.Nullable;
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
/*     */ public abstract class File
/*     */ {
/*     */   private final int id;
/*     */   private int links;
/*     */   private long creationTime;
/*     */   private long lastAccessTime;
/*     */   private long lastModifiedTime;
/*     */   @Nullable
/*     */   private Table<String, String, Object> attributes;
/*     */   
/*     */   File(int id) {
/*  51 */     this.id = id;
/*     */     
/*  53 */     long now = System.currentTimeMillis();
/*  54 */     this.creationTime = now;
/*  55 */     this.lastAccessTime = now;
/*  56 */     this.lastModifiedTime = now;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int id() {
/*  63 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long size() {
/*  71 */     return 0L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isDirectory() {
/*  78 */     return this instanceof Directory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isRegularFile() {
/*  85 */     return this instanceof RegularFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isSymbolicLink() {
/*  92 */     return this instanceof SymbolicLink;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract File copyWithoutContent(int paramInt);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void copyContentTo(File file) throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   ReadWriteLock contentLock() {
/* 117 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void opened() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void closed() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void deleted() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final boolean isRootDirectory() {
/* 142 */     return (isDirectory() && equals(((Directory)this).parent()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized int links() {
/* 149 */     return this.links;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void linked(DirectoryEntry entry) {
/* 157 */     Preconditions.checkNotNull(entry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void unlinked() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final synchronized void incrementLinkCount() {
/* 169 */     this.links++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final synchronized void decrementLinkCount() {
/* 176 */     this.links--;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized long getCreationTime() {
/* 183 */     return this.creationTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized long getLastAccessTime() {
/* 190 */     return this.lastAccessTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized long getLastModifiedTime() {
/* 197 */     return this.lastModifiedTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final synchronized void setCreationTime(long creationTime) {
/* 204 */     this.creationTime = creationTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final synchronized void setLastAccessTime(long lastAccessTime) {
/* 211 */     this.lastAccessTime = lastAccessTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final synchronized void setLastModifiedTime(long lastModifiedTime) {
/* 218 */     this.lastModifiedTime = lastModifiedTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void updateAccessTime() {
/* 225 */     setLastAccessTime(System.currentTimeMillis());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void updateModifiedTime() {
/* 232 */     setLastModifiedTime(System.currentTimeMillis());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized ImmutableSet<String> getAttributeNames(String view) {
/* 240 */     if (this.attributes == null) {
/* 241 */       return ImmutableSet.of();
/*     */     }
/* 243 */     return ImmutableSet.copyOf(this.attributes.row(view).keySet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   final synchronized ImmutableSet<String> getAttributeKeys() {
/* 251 */     if (this.attributes == null) {
/* 252 */       return ImmutableSet.of();
/*     */     }
/*     */     
/* 255 */     ImmutableSet.Builder<String> builder = ImmutableSet.builder();
/* 256 */     for (Table.Cell<String, String, Object> cell : (Iterable<Table.Cell<String, String, Object>>)this.attributes.cellSet()) {
/* 257 */       builder.add((String)cell.getRowKey() + ':' + (String)cell.getColumnKey());
/*     */     }
/* 259 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final synchronized Object getAttribute(String view, String attribute) {
/* 267 */     if (this.attributes == null) {
/* 268 */       return null;
/*     */     }
/* 270 */     return this.attributes.get(view, attribute);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized void setAttribute(String view, String attribute, Object value) {
/* 277 */     if (this.attributes == null) {
/* 278 */       this.attributes = (Table<String, String, Object>)HashBasedTable.create();
/*     */     }
/* 280 */     this.attributes.put(view, attribute, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized void deleteAttribute(String view, String attribute) {
/* 287 */     if (this.attributes != null) {
/* 288 */       this.attributes.remove(view, attribute);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final synchronized void copyBasicAttributes(File target) {
/* 296 */     target.setFileTimes(this.creationTime, this.lastModifiedTime, this.lastAccessTime);
/*     */   }
/*     */ 
/*     */   
/*     */   private synchronized void setFileTimes(long creationTime, long lastModifiedTime, long lastAccessTime) {
/* 301 */     this.creationTime = creationTime;
/* 302 */     this.lastModifiedTime = lastModifiedTime;
/* 303 */     this.lastAccessTime = lastAccessTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final synchronized void copyAttributes(File target) {
/* 310 */     copyBasicAttributes(target);
/* 311 */     target.putAll(this.attributes);
/*     */   }
/*     */   
/*     */   private synchronized void putAll(@Nullable Table<String, String, Object> attributes) {
/* 315 */     if (attributes != null && this.attributes != attributes) {
/* 316 */       if (this.attributes == null) {
/* 317 */         this.attributes = (Table<String, String, Object>)HashBasedTable.create();
/*     */       }
/* 319 */       this.attributes.putAll(attributes);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final String toString() {
/* 325 */     return MoreObjects.toStringHelper(this).add("id", id()).toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\File.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */