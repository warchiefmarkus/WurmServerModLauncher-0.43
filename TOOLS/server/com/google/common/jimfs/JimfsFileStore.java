/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.ImmutableSortedSet;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.FileStore;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.nio.file.attribute.FileAttributeView;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
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
/*     */ final class JimfsFileStore
/*     */   extends FileStore
/*     */ {
/*     */   private final FileTree tree;
/*     */   private final HeapDisk disk;
/*     */   private final AttributeService attributes;
/*     */   private final FileFactory factory;
/*     */   private final ImmutableSet<Feature> supportedFeatures;
/*     */   private final FileSystemState state;
/*     */   private final Lock readLock;
/*     */   private final Lock writeLock;
/*     */   
/*     */   public JimfsFileStore(FileTree tree, FileFactory factory, HeapDisk disk, AttributeService attributes, ImmutableSet<Feature> supportedFeatures, FileSystemState state) {
/*  71 */     this.tree = (FileTree)Preconditions.checkNotNull(tree);
/*  72 */     this.factory = (FileFactory)Preconditions.checkNotNull(factory);
/*  73 */     this.disk = (HeapDisk)Preconditions.checkNotNull(disk);
/*  74 */     this.attributes = (AttributeService)Preconditions.checkNotNull(attributes);
/*  75 */     this.supportedFeatures = (ImmutableSet<Feature>)Preconditions.checkNotNull(supportedFeatures);
/*  76 */     this.state = (FileSystemState)Preconditions.checkNotNull(state);
/*     */     
/*  78 */     ReadWriteLock lock = new ReentrantReadWriteLock();
/*  79 */     this.readLock = lock.readLock();
/*  80 */     this.writeLock = lock.writeLock();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   FileSystemState state() {
/*  89 */     return this.state;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Lock readLock() {
/*  96 */     return this.readLock;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Lock writeLock() {
/* 103 */     return this.writeLock;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableSortedSet<Name> getRootDirectoryNames() {
/* 110 */     this.state.checkOpen();
/* 111 */     return this.tree.getRootDirectoryNames();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   Directory getRoot(Name name) {
/* 119 */     DirectoryEntry entry = this.tree.getRoot(name);
/* 120 */     return (entry == null) ? null : (Directory)entry.file();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean supportsFeature(Feature feature) {
/* 127 */     return this.supportedFeatures.contains(feature);
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
/*     */ 
/*     */   
/*     */   DirectoryEntry lookUp(File workingDirectory, JimfsPath path, Set<? super LinkOption> options) throws IOException {
/* 141 */     this.state.checkOpen();
/* 142 */     return this.tree.lookUp(workingDirectory, path, options);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Supplier<RegularFile> regularFileCreator() {
/* 149 */     this.state.checkOpen();
/* 150 */     return this.factory.regularFileCreator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Supplier<Directory> directoryCreator() {
/* 157 */     this.state.checkOpen();
/* 158 */     return this.factory.directoryCreator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Supplier<SymbolicLink> symbolicLinkCreator(JimfsPath target) {
/* 165 */     this.state.checkOpen();
/* 166 */     return this.factory.symbolicLinkCreator(target);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   File copyWithoutContent(File file, AttributeCopyOption attributeCopyOption) throws IOException {
/* 174 */     File copy = this.factory.copyWithoutContent(file);
/* 175 */     setInitialAttributes(copy, (FileAttribute<?>[])new FileAttribute[0]);
/* 176 */     this.attributes.copyAttributes(file, copy, attributeCopyOption);
/* 177 */     return copy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setInitialAttributes(File file, FileAttribute<?>... attrs) {
/* 185 */     this.state.checkOpen();
/* 186 */     this.attributes.setInitialAttributes(file, attrs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   <V extends FileAttributeView> V getFileAttributeView(FileLookup lookup, Class<V> type) {
/* 195 */     this.state.checkOpen();
/* 196 */     return this.attributes.getFileAttributeView(lookup, type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableMap<String, Object> readAttributes(File file, String attributes) {
/* 203 */     this.state.checkOpen();
/* 204 */     return this.attributes.readAttributes(file, attributes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   <A extends java.nio.file.attribute.BasicFileAttributes> A readAttributes(File file, Class<A> type) {
/* 213 */     this.state.checkOpen();
/* 214 */     return this.attributes.readAttributes(file, type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setAttribute(File file, String attribute, Object value) {
/* 221 */     this.state.checkOpen();
/*     */     
/* 223 */     this.attributes.setAttribute(file, attribute, value, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableSet<String> supportedFileAttributeViews() {
/* 230 */     this.state.checkOpen();
/* 231 */     return this.attributes.supportedFileAttributeViews();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String name() {
/* 238 */     return "jimfs";
/*     */   }
/*     */ 
/*     */   
/*     */   public String type() {
/* 243 */     return "jimfs";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isReadOnly() {
/* 248 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getTotalSpace() throws IOException {
/* 253 */     this.state.checkOpen();
/* 254 */     return this.disk.getTotalSpace();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getUsableSpace() throws IOException {
/* 259 */     this.state.checkOpen();
/* 260 */     return getUnallocatedSpace();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getUnallocatedSpace() throws IOException {
/* 265 */     this.state.checkOpen();
/* 266 */     return this.disk.getUnallocatedSpace();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsFileAttributeView(Class<? extends FileAttributeView> type) {
/* 271 */     this.state.checkOpen();
/* 272 */     return this.attributes.supportsFileAttributeView(type);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsFileAttributeView(String name) {
/* 277 */     this.state.checkOpen();
/* 278 */     return this.attributes.supportedFileAttributeViews().contains(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public <V extends java.nio.file.attribute.FileStoreAttributeView> V getFileStoreAttributeView(Class<V> type) {
/* 283 */     this.state.checkOpen();
/* 284 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getAttribute(String attribute) throws IOException {
/* 289 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\JimfsFileStore.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */