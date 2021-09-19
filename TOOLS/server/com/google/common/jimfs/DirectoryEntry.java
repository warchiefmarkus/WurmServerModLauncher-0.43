/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.nio.file.FileAlreadyExistsException;
/*     */ import java.nio.file.NoSuchFileException;
/*     */ import java.nio.file.NotDirectoryException;
/*     */ import java.nio.file.NotLinkException;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Objects;
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
/*     */ final class DirectoryEntry
/*     */ {
/*     */   private final Directory directory;
/*     */   private final Name name;
/*     */   @Nullable
/*     */   private final File file;
/*     */   @Nullable
/*     */   DirectoryEntry next;
/*     */   
/*     */   DirectoryEntry(Directory directory, Name name, @Nullable File file) {
/*  50 */     this.directory = (Directory)Preconditions.checkNotNull(directory);
/*  51 */     this.name = (Name)Preconditions.checkNotNull(name);
/*  52 */     this.file = file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean exists() {
/*  59 */     return (this.file != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DirectoryEntry requireExists(Path pathForException) throws NoSuchFileException {
/*  69 */     if (!exists()) {
/*  70 */       throw new NoSuchFileException(pathForException.toString());
/*     */     }
/*  72 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DirectoryEntry requireDoesNotExist(Path pathForException) throws FileAlreadyExistsException {
/*  83 */     if (exists()) {
/*  84 */       throw new FileAlreadyExistsException(pathForException.toString());
/*     */     }
/*  86 */     return this;
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
/*     */   public DirectoryEntry requireDirectory(Path pathForException) throws NoSuchFileException, NotDirectoryException {
/*  98 */     requireExists(pathForException);
/*  99 */     if (!file().isDirectory()) {
/* 100 */       throw new NotDirectoryException(pathForException.toString());
/*     */     }
/* 102 */     return this;
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
/*     */   public DirectoryEntry requireSymbolicLink(Path pathForException) throws NoSuchFileException, NotLinkException {
/* 114 */     requireExists(pathForException);
/* 115 */     if (!file().isSymbolicLink()) {
/* 116 */       throw new NotLinkException(pathForException.toString());
/*     */     }
/* 118 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Directory directory() {
/* 125 */     return this.directory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Name name() {
/* 132 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File file() {
/* 141 */     Preconditions.checkState(exists());
/* 142 */     return this.file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public File fileOrNull() {
/* 150 */     return this.file;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 155 */     if (obj instanceof DirectoryEntry) {
/* 156 */       DirectoryEntry other = (DirectoryEntry)obj;
/* 157 */       return (this.directory.equals(other.directory) && this.name.equals(other.name) && Objects.equals(this.file, other.file));
/*     */     } 
/*     */ 
/*     */     
/* 161 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 166 */     return Objects.hash(new Object[] { this.directory, this.name, this.file });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 171 */     return MoreObjects.toStringHelper(this).add("directory", this.directory).add("name", this.name).add("file", this.file).toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\DirectoryEntry.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */