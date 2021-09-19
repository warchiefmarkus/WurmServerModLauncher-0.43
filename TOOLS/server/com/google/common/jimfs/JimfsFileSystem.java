/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.ImmutableSortedSet;
/*     */ import com.google.common.util.concurrent.ThreadFactoryBuilder;
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.nio.file.FileStore;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.PathMatcher;
/*     */ import java.nio.file.WatchService;
/*     */ import java.nio.file.attribute.UserPrincipalLookupService;
/*     */ import java.nio.file.spi.FileSystemProvider;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
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
/*     */ final class JimfsFileSystem
/*     */   extends FileSystem
/*     */ {
/*     */   private final JimfsFileSystemProvider provider;
/*     */   private final URI uri;
/*     */   private final JimfsFileStore fileStore;
/*     */   private final PathService pathService;
/* 179 */   private final UserPrincipalLookupService userLookupService = new UserLookupService(true);
/*     */ 
/*     */   
/*     */   private final FileSystemView defaultView;
/*     */ 
/*     */   
/*     */   private final WatchServiceConfiguration watchServiceConfig;
/*     */   
/*     */   @Nullable
/*     */   private ExecutorService defaultThreadPool;
/*     */ 
/*     */   
/*     */   JimfsFileSystem(JimfsFileSystemProvider provider, URI uri, JimfsFileStore fileStore, PathService pathService, FileSystemView defaultView, WatchServiceConfiguration watchServiceConfig) {
/* 192 */     this.provider = (JimfsFileSystemProvider)Preconditions.checkNotNull(provider);
/* 193 */     this.uri = (URI)Preconditions.checkNotNull(uri);
/* 194 */     this.fileStore = (JimfsFileStore)Preconditions.checkNotNull(fileStore);
/* 195 */     this.pathService = (PathService)Preconditions.checkNotNull(pathService);
/* 196 */     this.defaultView = (FileSystemView)Preconditions.checkNotNull(defaultView);
/* 197 */     this.watchServiceConfig = (WatchServiceConfiguration)Preconditions.checkNotNull(watchServiceConfig);
/*     */   }
/*     */ 
/*     */   
/*     */   public JimfsFileSystemProvider provider() {
/* 202 */     return this.provider;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI getUri() {
/* 209 */     return this.uri;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileSystemView getDefaultView() {
/* 216 */     return this.defaultView;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSeparator() {
/* 221 */     return this.pathService.getSeparator();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSortedSet<Path> getRootDirectories() {
/* 227 */     ImmutableSortedSet.Builder<JimfsPath> builder = ImmutableSortedSet.orderedBy(this.pathService);
/* 228 */     for (Name name : this.fileStore.getRootDirectoryNames()) {
/* 229 */       builder.add(this.pathService.createRoot(name));
/*     */     }
/* 231 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JimfsPath getWorkingDirectory() {
/* 238 */     return this.defaultView.getWorkingDirectoryPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   PathService getPathService() {
/* 246 */     return this.pathService;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JimfsFileStore getFileStore() {
/* 253 */     return this.fileStore;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<FileStore> getFileStores() {
/* 258 */     this.fileStore.state().checkOpen();
/* 259 */     return ImmutableSet.of(this.fileStore);
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<String> supportedFileAttributeViews() {
/* 264 */     return this.fileStore.supportedFileAttributeViews();
/*     */   }
/*     */ 
/*     */   
/*     */   public JimfsPath getPath(String first, String... more) {
/* 269 */     this.fileStore.state().checkOpen();
/* 270 */     return this.pathService.parsePath(first, more);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI toUri(JimfsPath path) {
/* 277 */     this.fileStore.state().checkOpen();
/* 278 */     return this.pathService.toUri(this.uri, path.toAbsolutePath());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JimfsPath toPath(URI uri) {
/* 285 */     this.fileStore.state().checkOpen();
/* 286 */     return this.pathService.fromUri(uri);
/*     */   }
/*     */ 
/*     */   
/*     */   public PathMatcher getPathMatcher(String syntaxAndPattern) {
/* 291 */     this.fileStore.state().checkOpen();
/* 292 */     return this.pathService.createPathMatcher(syntaxAndPattern);
/*     */   }
/*     */ 
/*     */   
/*     */   public UserPrincipalLookupService getUserPrincipalLookupService() {
/* 297 */     this.fileStore.state().checkOpen();
/* 298 */     return this.userLookupService;
/*     */   }
/*     */ 
/*     */   
/*     */   public WatchService newWatchService() throws IOException {
/* 303 */     return this.watchServiceConfig.newWatchService(this.defaultView, this.pathService);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized ExecutorService getDefaultThreadPool() {
/* 314 */     if (this.defaultThreadPool == null) {
/* 315 */       this.defaultThreadPool = Executors.newCachedThreadPool((new ThreadFactoryBuilder()).setDaemon(true).setNameFormat("JimfsFileSystem-" + this.uri.getHost() + "-defaultThreadPool-%s").build());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 323 */       this.fileStore.state().register(new Closeable()
/*     */           {
/*     */ 
/*     */             
/*     */             public void close()
/*     */             {
/* 329 */               JimfsFileSystem.this.defaultThreadPool.shutdown();
/*     */             }
/*     */           });
/*     */     } 
/* 333 */     return this.defaultThreadPool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReadOnly() {
/* 343 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 348 */     return this.fileStore.state().isOpen();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 353 */     this.fileStore.state().close();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\JimfsFileSystem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */