/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URI;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.nio.file.spi.FileSystemProvider;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class JimfsFileSystems
/*     */ {
/*  42 */   private static final FileSystemProvider systemJimfsProvider = getSystemJimfsProvider();
/*     */   
/*     */   private static FileSystemProvider getSystemJimfsProvider() {
/*  45 */     for (FileSystemProvider provider : FileSystemProvider.installedProviders()) {
/*  46 */       if (provider.getScheme().equals("jimfs")) {
/*  47 */         return provider;
/*     */       }
/*     */     } 
/*  50 */     return null;
/*     */   }
/*     */   
/*  53 */   private static final Runnable DO_NOTHING = new Runnable()
/*     */     {
/*     */       public void run() {}
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Runnable removeFileSystemRunnable(URI uri) {
/*  64 */     if (systemJimfsProvider == null)
/*     */     {
/*  66 */       return DO_NOTHING;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  73 */       Method method = systemJimfsProvider.getClass().getDeclaredMethod("removeFileSystemRunnable", new Class[] { URI.class });
/*     */       
/*  75 */       return (Runnable)method.invoke(systemJimfsProvider, new Object[] { uri });
/*  76 */     } catch (NoSuchMethodException|java.lang.reflect.InvocationTargetException|IllegalAccessException e) {
/*  77 */       throw new RuntimeException("Unable to get Runnable for removing the FileSystem from the cache when it is closed", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JimfsFileSystem newFileSystem(JimfsFileSystemProvider provider, URI uri, Configuration config) throws IOException {
/*  88 */     PathService pathService = new PathService(config);
/*  89 */     FileSystemState state = new FileSystemState(removeFileSystemRunnable(uri));
/*     */     
/*  91 */     JimfsFileStore fileStore = createFileStore(config, pathService, state);
/*  92 */     FileSystemView defaultView = createDefaultView(config, fileStore, pathService);
/*  93 */     WatchServiceConfiguration watchServiceConfig = config.watchServiceConfig;
/*     */     
/*  95 */     JimfsFileSystem fileSystem = new JimfsFileSystem(provider, uri, fileStore, pathService, defaultView, watchServiceConfig);
/*     */ 
/*     */     
/*  98 */     pathService.setFileSystem(fileSystem);
/*  99 */     return fileSystem;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static JimfsFileStore createFileStore(Configuration config, PathService pathService, FileSystemState state) {
/* 107 */     AttributeService attributeService = new AttributeService(config);
/*     */ 
/*     */     
/* 110 */     HeapDisk disk = new HeapDisk(config);
/* 111 */     FileFactory fileFactory = new FileFactory(disk);
/*     */     
/* 113 */     Map<Name, Directory> roots = new HashMap<>();
/*     */ 
/*     */     
/* 116 */     for (String root : config.roots) {
/* 117 */       JimfsPath path = pathService.parsePath(root, new String[0]);
/* 118 */       if (!path.isAbsolute() && path.getNameCount() == 0) {
/* 119 */         throw new IllegalArgumentException("Invalid root path: " + root);
/*     */       }
/*     */       
/* 122 */       Name rootName = path.root();
/*     */       
/* 124 */       Directory rootDir = fileFactory.createRootDirectory(rootName);
/* 125 */       attributeService.setInitialAttributes(rootDir, (FileAttribute<?>[])new FileAttribute[0]);
/* 126 */       roots.put(rootName, rootDir);
/*     */     } 
/*     */     
/* 129 */     return new JimfsFileStore(new FileTree(roots), fileFactory, disk, attributeService, config.supportedFeatures, state);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static FileSystemView createDefaultView(Configuration config, JimfsFileStore fileStore, PathService pathService) throws IOException {
/* 138 */     JimfsPath workingDirPath = pathService.parsePath(config.workingDirectory, new String[0]);
/*     */     
/* 140 */     Directory dir = fileStore.getRoot(workingDirPath.root());
/* 141 */     if (dir == null) {
/* 142 */       throw new IllegalArgumentException("Invalid working dir path: " + workingDirPath);
/*     */     }
/*     */     
/* 145 */     for (Name name : workingDirPath.names()) {
/* 146 */       Directory newDir = (Directory)fileStore.directoryCreator().get();
/* 147 */       fileStore.setInitialAttributes(newDir, (FileAttribute<?>[])new FileAttribute[0]);
/* 148 */       dir.link(name, newDir);
/*     */       
/* 150 */       dir = newDir;
/*     */     } 
/*     */     
/* 153 */     return new FileSystemView(fileStore, dir, workingDirPath);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\JimfsFileSystems.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */