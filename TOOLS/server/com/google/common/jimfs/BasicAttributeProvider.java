/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.attribute.BasicFileAttributeView;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.nio.file.attribute.FileAttributeView;
/*     */ import java.nio.file.attribute.FileTime;
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
/*     */ final class BasicAttributeProvider
/*     */   extends AttributeProvider
/*     */ {
/*  39 */   private static final ImmutableSet<String> ATTRIBUTES = ImmutableSet.of("size", "fileKey", "isDirectory", "isRegularFile", "isSymbolicLink", "isOther", (Object[])new String[] { "creationTime", "lastAccessTime", "lastModifiedTime" });
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
/*     */   public String name() {
/*  53 */     return "basic";
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<String> fixedAttributes() {
/*  58 */     return ATTRIBUTES;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object get(File file, String attribute) {
/*  63 */     switch (attribute) {
/*     */       case "size":
/*  65 */         return Long.valueOf(file.size());
/*     */       case "fileKey":
/*  67 */         return Integer.valueOf(file.id());
/*     */       case "isDirectory":
/*  69 */         return Boolean.valueOf(file.isDirectory());
/*     */       case "isRegularFile":
/*  71 */         return Boolean.valueOf(file.isRegularFile());
/*     */       case "isSymbolicLink":
/*  73 */         return Boolean.valueOf(file.isSymbolicLink());
/*     */       case "isOther":
/*  75 */         return Boolean.valueOf((!file.isDirectory() && !file.isRegularFile() && !file.isSymbolicLink()));
/*     */       case "creationTime":
/*  77 */         return FileTime.fromMillis(file.getCreationTime());
/*     */       case "lastAccessTime":
/*  79 */         return FileTime.fromMillis(file.getLastAccessTime());
/*     */       case "lastModifiedTime":
/*  81 */         return FileTime.fromMillis(file.getLastModifiedTime());
/*     */     } 
/*  83 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(File file, String view, String attribute, Object value, boolean create) {
/*  89 */     switch (attribute) {
/*     */       case "creationTime":
/*  91 */         checkNotCreate(view, attribute, create);
/*  92 */         file.setCreationTime(((FileTime)checkType(view, attribute, value, FileTime.class)).toMillis());
/*     */         break;
/*     */       case "lastAccessTime":
/*  95 */         checkNotCreate(view, attribute, create);
/*  96 */         file.setLastAccessTime(((FileTime)checkType(view, attribute, value, FileTime.class)).toMillis());
/*     */         break;
/*     */       case "lastModifiedTime":
/*  99 */         checkNotCreate(view, attribute, create);
/* 100 */         file.setLastModifiedTime(((FileTime)checkType(view, attribute, value, FileTime.class)).toMillis());
/*     */         break;
/*     */       case "size":
/*     */       case "fileKey":
/*     */       case "isDirectory":
/*     */       case "isRegularFile":
/*     */       case "isSymbolicLink":
/*     */       case "isOther":
/* 108 */         throw unsettable(view, attribute);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<BasicFileAttributeView> viewType() {
/* 115 */     return BasicFileAttributeView.class;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicFileAttributeView view(FileLookup lookup, ImmutableMap<String, FileAttributeView> inheritedViews) {
/* 121 */     return new View(lookup);
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<BasicFileAttributes> attributesType() {
/* 126 */     return BasicFileAttributes.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicFileAttributes readAttributes(File file) {
/* 131 */     return new Attributes(file);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class View
/*     */     extends AbstractAttributeView
/*     */     implements BasicFileAttributeView
/*     */   {
/*     */     protected View(FileLookup lookup) {
/* 140 */       super(lookup);
/*     */     }
/*     */ 
/*     */     
/*     */     public String name() {
/* 145 */       return "basic";
/*     */     }
/*     */ 
/*     */     
/*     */     public BasicFileAttributes readAttributes() throws IOException {
/* 150 */       return new BasicAttributeProvider.Attributes(lookupFile());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setTimes(@Nullable FileTime lastModifiedTime, @Nullable FileTime lastAccessTime, @Nullable FileTime createTime) throws IOException {
/* 159 */       File file = lookupFile();
/*     */       
/* 161 */       if (lastModifiedTime != null) {
/* 162 */         file.setLastModifiedTime(lastModifiedTime.toMillis());
/*     */       }
/*     */       
/* 165 */       if (lastAccessTime != null) {
/* 166 */         file.setLastAccessTime(lastAccessTime.toMillis());
/*     */       }
/*     */       
/* 169 */       if (createTime != null) {
/* 170 */         file.setCreationTime(createTime.toMillis());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class Attributes
/*     */     implements BasicFileAttributes
/*     */   {
/*     */     private final FileTime lastModifiedTime;
/*     */     
/*     */     private final FileTime lastAccessTime;
/*     */     private final FileTime creationTime;
/*     */     private final boolean regularFile;
/*     */     private final boolean directory;
/*     */     private final boolean symbolicLink;
/*     */     private final long size;
/*     */     private final Object fileKey;
/*     */     
/*     */     protected Attributes(File file) {
/* 190 */       this.lastModifiedTime = FileTime.fromMillis(file.getLastModifiedTime());
/* 191 */       this.lastAccessTime = FileTime.fromMillis(file.getLastAccessTime());
/* 192 */       this.creationTime = FileTime.fromMillis(file.getCreationTime());
/* 193 */       this.regularFile = file.isRegularFile();
/* 194 */       this.directory = file.isDirectory();
/* 195 */       this.symbolicLink = file.isSymbolicLink();
/* 196 */       this.size = file.size();
/* 197 */       this.fileKey = Integer.valueOf(file.id());
/*     */     }
/*     */ 
/*     */     
/*     */     public FileTime lastModifiedTime() {
/* 202 */       return this.lastModifiedTime;
/*     */     }
/*     */ 
/*     */     
/*     */     public FileTime lastAccessTime() {
/* 207 */       return this.lastAccessTime;
/*     */     }
/*     */ 
/*     */     
/*     */     public FileTime creationTime() {
/* 212 */       return this.creationTime;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isRegularFile() {
/* 217 */       return this.regularFile;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDirectory() {
/* 222 */       return this.directory;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isSymbolicLink() {
/* 227 */       return this.symbolicLink;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isOther() {
/* 232 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public long size() {
/* 237 */       return this.size;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object fileKey() {
/* 242 */       return this.fileKey;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\BasicAttributeProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */