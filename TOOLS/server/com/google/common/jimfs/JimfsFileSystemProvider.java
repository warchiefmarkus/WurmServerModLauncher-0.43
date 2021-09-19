/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URI;
/*     */ import java.nio.channels.AsynchronousFileChannel;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.SeekableByteChannel;
/*     */ import java.nio.file.AccessMode;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.FileStore;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.FileSystems;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.ProviderMismatchException;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.nio.file.attribute.DosFileAttributes;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.nio.file.spi.FileSystemProvider;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ExecutorService;
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
/*     */ final class JimfsFileSystemProvider
/*     */   extends FileSystemProvider
/*     */ {
/*  65 */   private static final JimfsFileSystemProvider INSTANCE = new JimfsFileSystemProvider();
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/*  70 */       Handler.register();
/*  71 */     } catch (Throwable e) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static JimfsFileSystemProvider instance() {
/*  80 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getScheme() {
/*  85 */     return "jimfs";
/*     */   }
/*     */ 
/*     */   
/*     */   public FileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException {
/*  90 */     throw new UnsupportedOperationException("This method should not be called directly;use an overload of Jimfs.newFileSystem() to create a FileSystem.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileSystem getFileSystem(URI uri) {
/*  97 */     throw new UnsupportedOperationException("This method should not be called directly; use FileSystems.getFileSystem(URI) instead.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileSystem newFileSystem(Path path, Map<String, ?> env) throws IOException {
/* 104 */     JimfsPath checkedPath = checkPath(path);
/* 105 */     Preconditions.checkNotNull(env);
/*     */     
/* 107 */     URI pathUri = checkedPath.toUri();
/* 108 */     URI jarUri = URI.create("jar:" + pathUri);
/*     */ 
/*     */     
/*     */     try {
/* 112 */       return FileSystems.newFileSystem(jarUri, env);
/* 113 */     } catch (Exception e) {
/*     */ 
/*     */       
/* 116 */       throw new UnsupportedOperationException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Path getPath(URI uri) {
/* 122 */     throw new UnsupportedOperationException("This method should not be called directly; use Paths.get(URI) instead.");
/*     */   }
/*     */ 
/*     */   
/*     */   private static JimfsPath checkPath(Path path) {
/* 127 */     if (path instanceof JimfsPath) {
/* 128 */       return (JimfsPath)path;
/*     */     }
/* 130 */     throw new ProviderMismatchException("path " + path + " is not associated with a Jimfs file system");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static JimfsFileSystem getFileSystem(Path path) {
/* 138 */     return (JimfsFileSystem)checkPath(path).getFileSystem();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static FileSystemView getDefaultView(JimfsPath path) {
/* 145 */     return getFileSystem(path).getDefaultView();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FileChannel newFileChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
/* 151 */     JimfsPath checkedPath = checkPath(path);
/* 152 */     if (!checkedPath.getJimfsFileSystem().getFileStore().supportsFeature(Feature.FILE_CHANNEL)) {
/* 153 */       throw new UnsupportedOperationException();
/*     */     }
/* 155 */     return newJimfsFileChannel(checkedPath, options, attrs);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private JimfsFileChannel newJimfsFileChannel(JimfsPath path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
/* 161 */     ImmutableSet<OpenOption> opts = Options.getOptionsForChannel(options);
/* 162 */     FileSystemView view = getDefaultView(path);
/* 163 */     RegularFile file = view.getOrCreateRegularFile(path, (Set<OpenOption>)opts, attrs);
/* 164 */     return new JimfsFileChannel(file, (Set<OpenOption>)opts, view.state());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
/* 170 */     JimfsPath checkedPath = checkPath(path);
/* 171 */     JimfsFileChannel channel = newJimfsFileChannel(checkedPath, options, attrs);
/* 172 */     return checkedPath.getJimfsFileSystem().getFileStore().supportsFeature(Feature.FILE_CHANNEL) ? channel : new DowngradedSeekableByteChannel(channel);
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
/*     */   public AsynchronousFileChannel newAsynchronousFileChannel(Path path, Set<? extends OpenOption> options, @Nullable ExecutorService executor, FileAttribute<?>... attrs) throws IOException {
/* 185 */     JimfsFileChannel channel = (JimfsFileChannel)newFileChannel(path, options, attrs);
/* 186 */     if (executor == null) {
/* 187 */       JimfsFileSystem fileSystem = (JimfsFileSystem)path.getFileSystem();
/* 188 */       executor = fileSystem.getDefaultThreadPool();
/*     */     } 
/* 190 */     return channel.asAsynchronousFileChannel(executor);
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream newInputStream(Path path, OpenOption... options) throws IOException {
/* 195 */     JimfsPath checkedPath = checkPath(path);
/* 196 */     ImmutableSet<OpenOption> opts = Options.getOptionsForInputStream(options);
/* 197 */     FileSystemView view = getDefaultView(checkedPath);
/* 198 */     RegularFile file = view.getOrCreateRegularFile(checkedPath, (Set<OpenOption>)opts, NO_ATTRS);
/* 199 */     return new JimfsInputStream(file, view.state());
/*     */   }
/*     */   
/* 202 */   private static final FileAttribute<?>[] NO_ATTRS = (FileAttribute<?>[])new FileAttribute[0];
/*     */ 
/*     */   
/*     */   public OutputStream newOutputStream(Path path, OpenOption... options) throws IOException {
/* 206 */     JimfsPath checkedPath = checkPath(path);
/* 207 */     ImmutableSet<OpenOption> opts = Options.getOptionsForOutputStream(options);
/* 208 */     FileSystemView view = getDefaultView(checkedPath);
/* 209 */     RegularFile file = view.getOrCreateRegularFile(checkedPath, (Set<OpenOption>)opts, NO_ATTRS);
/* 210 */     return new JimfsOutputStream(file, opts.contains(StandardOpenOption.APPEND), view.state());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DirectoryStream<Path> newDirectoryStream(Path dir, DirectoryStream.Filter<? super Path> filter) throws IOException {
/* 216 */     JimfsPath checkedPath = checkPath(dir);
/* 217 */     return getDefaultView(checkedPath).newDirectoryStream(checkedPath, filter, (Set<? super LinkOption>)Options.FOLLOW_LINKS, checkedPath);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException {
/* 223 */     JimfsPath checkedPath = checkPath(dir);
/* 224 */     FileSystemView view = getDefaultView(checkedPath);
/* 225 */     view.createDirectory(checkedPath, attrs);
/*     */   }
/*     */ 
/*     */   
/*     */   public void createLink(Path link, Path existing) throws IOException {
/* 230 */     JimfsPath linkPath = checkPath(link);
/* 231 */     JimfsPath existingPath = checkPath(existing);
/* 232 */     Preconditions.checkArgument(linkPath.getFileSystem().equals(existingPath.getFileSystem()), "link and existing paths must belong to the same file system instance");
/*     */ 
/*     */     
/* 235 */     FileSystemView view = getDefaultView(linkPath);
/* 236 */     view.link(linkPath, getDefaultView(existingPath), existingPath);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void createSymbolicLink(Path link, Path target, FileAttribute<?>... attrs) throws IOException {
/* 242 */     JimfsPath linkPath = checkPath(link);
/* 243 */     JimfsPath targetPath = checkPath(target);
/* 244 */     Preconditions.checkArgument(linkPath.getFileSystem().equals(targetPath.getFileSystem()), "link and target paths must belong to the same file system instance");
/*     */ 
/*     */     
/* 247 */     FileSystemView view = getDefaultView(linkPath);
/* 248 */     view.createSymbolicLink(linkPath, targetPath, attrs);
/*     */   }
/*     */ 
/*     */   
/*     */   public Path readSymbolicLink(Path link) throws IOException {
/* 253 */     JimfsPath checkedPath = checkPath(link);
/* 254 */     return getDefaultView(checkedPath).readSymbolicLink(checkedPath);
/*     */   }
/*     */ 
/*     */   
/*     */   public void delete(Path path) throws IOException {
/* 259 */     JimfsPath checkedPath = checkPath(path);
/* 260 */     FileSystemView view = getDefaultView(checkedPath);
/* 261 */     view.deleteFile(checkedPath, FileSystemView.DeleteMode.ANY);
/*     */   }
/*     */ 
/*     */   
/*     */   public void copy(Path source, Path target, CopyOption... options) throws IOException {
/* 266 */     copy(source, target, Options.getCopyOptions(options), false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void move(Path source, Path target, CopyOption... options) throws IOException {
/* 271 */     copy(source, target, Options.getMoveOptions(options), true);
/*     */   }
/*     */ 
/*     */   
/*     */   private void copy(Path source, Path target, ImmutableSet<CopyOption> options, boolean move) throws IOException {
/* 276 */     JimfsPath sourcePath = checkPath(source);
/* 277 */     JimfsPath targetPath = checkPath(target);
/*     */     
/* 279 */     FileSystemView sourceView = getDefaultView(sourcePath);
/* 280 */     FileSystemView targetView = getDefaultView(targetPath);
/* 281 */     sourceView.copy(sourcePath, targetView, targetPath, (Set<CopyOption>)options, move);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSameFile(Path path, Path path2) throws IOException {
/* 286 */     if (path.equals(path2)) {
/* 287 */       return true;
/*     */     }
/*     */     
/* 290 */     if (!(path instanceof JimfsPath) || !(path2 instanceof JimfsPath)) {
/* 291 */       return false;
/*     */     }
/*     */     
/* 294 */     JimfsPath checkedPath = (JimfsPath)path;
/* 295 */     JimfsPath checkedPath2 = (JimfsPath)path2;
/*     */     
/* 297 */     FileSystemView view = getDefaultView(checkedPath);
/* 298 */     FileSystemView view2 = getDefaultView(checkedPath2);
/*     */     
/* 300 */     return view.isSameFile(checkedPath, view2, checkedPath2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isHidden(Path path) throws IOException {
/* 311 */     JimfsPath checkedPath = checkPath(path);
/* 312 */     FileSystemView view = getDefaultView(checkedPath);
/* 313 */     if (getFileStore(path).supportsFileAttributeView("dos")) {
/* 314 */       return ((DosFileAttributes)view.<DosFileAttributes>readAttributes(checkedPath, DosFileAttributes.class, (Set<? super LinkOption>)Options.NOFOLLOW_LINKS)).isHidden();
/*     */     }
/*     */ 
/*     */     
/* 318 */     return (path.getNameCount() > 0 && path.getFileName().toString().startsWith("."));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FileStore getFileStore(Path path) throws IOException {
/* 324 */     return getFileSystem(path).getFileStore();
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkAccess(Path path, AccessMode... modes) throws IOException {
/* 329 */     JimfsPath checkedPath = checkPath(path);
/* 330 */     getDefaultView(checkedPath).checkAccess(checkedPath);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <V extends java.nio.file.attribute.FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
/* 337 */     JimfsPath checkedPath = checkPath(path);
/* 338 */     return getDefaultView(checkedPath).getFileAttributeView(checkedPath, type, (Set<? super LinkOption>)Options.getLinkOptions(options));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends java.nio.file.attribute.BasicFileAttributes> A readAttributes(Path path, Class<A> type, LinkOption... options) throws IOException {
/* 345 */     JimfsPath checkedPath = checkPath(path);
/* 346 */     return getDefaultView(checkedPath).readAttributes(checkedPath, type, (Set<? super LinkOption>)Options.getLinkOptions(options));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) throws IOException {
/* 353 */     JimfsPath checkedPath = checkPath(path);
/* 354 */     return (Map<String, Object>)getDefaultView(checkedPath).readAttributes(checkedPath, attributes, (Set<? super LinkOption>)Options.getLinkOptions(options));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttribute(Path path, String attribute, Object value, LinkOption... options) throws IOException {
/* 361 */     JimfsPath checkedPath = checkPath(path);
/* 362 */     getDefaultView(checkedPath).setAttribute(checkedPath, attribute, value, (Set<? super LinkOption>)Options.getLinkOptions(options));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\JimfsFileSystemProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */