/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ final class FileFactory
/*     */ {
/*  34 */   private final AtomicInteger idGenerator = new AtomicInteger();
/*     */ 
/*     */   
/*     */   private final HeapDisk disk;
/*     */ 
/*     */   
/*     */   private final Supplier<Directory> directorySupplier;
/*     */   
/*     */   private final Supplier<RegularFile> regularFileSupplier;
/*     */ 
/*     */   
/*     */   private int nextFileId() {
/*  46 */     return this.idGenerator.getAndIncrement();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Directory createDirectory() {
/*  53 */     return Directory.create(nextFileId());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Directory createRootDirectory(Name name) {
/*  60 */     return Directory.createRoot(nextFileId(), name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   RegularFile createRegularFile() {
/*  68 */     return RegularFile.create(nextFileId(), this.disk);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   SymbolicLink createSymbolicLink(JimfsPath target) {
/*  76 */     return SymbolicLink.create(nextFileId(), target);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File copyWithoutContent(File file) throws IOException {
/*  83 */     return file.copyWithoutContent(nextFileId());
/*     */   }
/*     */ 
/*     */   
/*     */   public FileFactory(HeapDisk disk) {
/*  88 */     this.directorySupplier = new DirectorySupplier();
/*     */     
/*  90 */     this.regularFileSupplier = new RegularFileSupplier();
/*     */     this.disk = (HeapDisk)Preconditions.checkNotNull(disk);
/*     */   }
/*     */ 
/*     */   
/*     */   public Supplier<Directory> directoryCreator() {
/*  96 */     return this.directorySupplier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Supplier<RegularFile> regularFileCreator() {
/* 103 */     return this.regularFileSupplier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Supplier<SymbolicLink> symbolicLinkCreator(JimfsPath target) {
/* 110 */     return new SymbolicLinkSupplier(target);
/*     */   }
/*     */   
/*     */   private final class DirectorySupplier
/*     */     implements Supplier<Directory> {
/*     */     public Directory get() {
/* 116 */       return FileFactory.this.createDirectory();
/*     */     }
/*     */     private DirectorySupplier() {} }
/*     */   
/*     */   private final class RegularFileSupplier implements Supplier<RegularFile> { private RegularFileSupplier() {}
/*     */     
/*     */     public RegularFile get() {
/* 123 */       return FileFactory.this.createRegularFile();
/*     */     } }
/*     */ 
/*     */   
/*     */   private final class SymbolicLinkSupplier
/*     */     implements Supplier<SymbolicLink> {
/*     */     private final JimfsPath target;
/*     */     
/*     */     protected SymbolicLinkSupplier(JimfsPath target) {
/* 132 */       this.target = (JimfsPath)Preconditions.checkNotNull(target);
/*     */     }
/*     */ 
/*     */     
/*     */     public SymbolicLink get() {
/* 137 */       return FileFactory.this.createSymbolicLink(this.target);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\FileFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */