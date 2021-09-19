/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.auto.service.AutoService;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.collect.MapMaker;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.channels.SeekableByteChannel;
/*     */ import java.nio.file.AccessMode;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.FileStore;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.FileSystemAlreadyExistsException;
/*     */ import java.nio.file.FileSystemNotFoundException;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.nio.file.spi.FileSystemProvider;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @AutoService(FileSystemProvider.class)
/*     */ public final class SystemJimfsFileSystemProvider
/*     */   extends FileSystemProvider
/*     */ {
/*     */   static final String FILE_SYSTEM_KEY = "fileSystem";
/*  90 */   private static final ConcurrentMap<URI, FileSystem> fileSystems = (new MapMaker()).weakValues().makeMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getScheme() {
/* 101 */     return "jimfs";
/*     */   }
/*     */ 
/*     */   
/*     */   public FileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException {
/* 106 */     Preconditions.checkArgument(uri.getScheme().equalsIgnoreCase("jimfs"), "uri (%s) scheme must be '%s'", new Object[] { uri, "jimfs" });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 111 */     Preconditions.checkArgument(isValidFileSystemUri(uri), "uri (%s) may not have a path, query or fragment", new Object[] { uri });
/*     */     
/* 113 */     Preconditions.checkArgument(env.get("fileSystem") instanceof FileSystem, "env map (%s) must contain key '%s' mapped to an instance of %s", new Object[] { env, "fileSystem", FileSystem.class });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 118 */     FileSystem fileSystem = (FileSystem)env.get("fileSystem");
/* 119 */     if (fileSystems.putIfAbsent(uri, fileSystem) != null) {
/* 120 */       throw new FileSystemAlreadyExistsException(uri.toString());
/*     */     }
/* 122 */     return fileSystem;
/*     */   }
/*     */ 
/*     */   
/*     */   public FileSystem getFileSystem(URI uri) {
/* 127 */     FileSystem fileSystem = fileSystems.get(uri);
/* 128 */     if (fileSystem == null) {
/* 129 */       throw new FileSystemNotFoundException(uri.toString());
/*     */     }
/* 131 */     return fileSystem;
/*     */   }
/*     */ 
/*     */   
/*     */   public Path getPath(URI uri) {
/* 136 */     Preconditions.checkArgument("jimfs".equalsIgnoreCase(uri.getScheme()), "uri scheme does not match this provider: %s", new Object[] { uri });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 141 */     String path = uri.getPath();
/* 142 */     Preconditions.checkArgument(!Strings.isNullOrEmpty(path), "uri must have a path: %s", new Object[] { uri });
/*     */     
/* 144 */     return toPath(getFileSystem(toFileSystemUri(uri)), uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isValidFileSystemUri(URI uri) {
/* 153 */     return (Strings.isNullOrEmpty(uri.getPath()) && Strings.isNullOrEmpty(uri.getQuery()) && Strings.isNullOrEmpty(uri.getFragment()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static URI toFileSystemUri(URI uri) {
/*     */     try {
/* 163 */       return new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), null, null, null);
/*     */     }
/* 165 */     catch (URISyntaxException e) {
/* 166 */       throw new AssertionError(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Path toPath(FileSystem fileSystem, URI uri) {
/*     */     try {
/* 178 */       Method toPath = fileSystem.getClass().getDeclaredMethod("toPath", new Class[] { URI.class });
/* 179 */       return (Path)toPath.invoke(fileSystem, new Object[] { uri });
/* 180 */     } catch (NoSuchMethodException e) {
/* 181 */       throw new IllegalArgumentException("invalid file system: " + fileSystem);
/* 182 */     } catch (InvocationTargetException|IllegalAccessException e) {
/* 183 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public FileSystem newFileSystem(Path path, Map<String, ?> env) throws IOException {
/* 189 */     FileSystemProvider realProvider = path.getFileSystem().provider();
/* 190 */     return realProvider.newFileSystem(path, env);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Runnable removeFileSystemRunnable(final URI uri) {
/* 199 */     return new Runnable()
/*     */       {
/*     */         public void run() {
/* 202 */           SystemJimfsFileSystemProvider.fileSystems.remove(uri);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
/* 210 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DirectoryStream<Path> newDirectoryStream(Path dir, DirectoryStream.Filter<? super Path> filter) throws IOException {
/* 216 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException {
/* 221 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void delete(Path path) throws IOException {
/* 226 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void copy(Path source, Path target, CopyOption... options) throws IOException {
/* 231 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void move(Path source, Path target, CopyOption... options) throws IOException {
/* 236 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSameFile(Path path, Path path2) throws IOException {
/* 241 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isHidden(Path path) throws IOException {
/* 246 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public FileStore getFileStore(Path path) throws IOException {
/* 251 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkAccess(Path path, AccessMode... modes) throws IOException {
/* 256 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <V extends java.nio.file.attribute.FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
/* 262 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends java.nio.file.attribute.BasicFileAttributes> A readAttributes(Path path, Class<A> type, LinkOption... options) throws IOException {
/* 268 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) throws IOException {
/* 274 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttribute(Path path, String attribute, Object value, LinkOption... options) throws IOException {
/* 280 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\SystemJimfsFileSystemProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */