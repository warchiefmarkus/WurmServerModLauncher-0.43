/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.FileSystems;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Jimfs
/*     */ {
/*     */   public static final String URI_SCHEME = "jimfs";
/*     */   
/*     */   public static FileSystem newFileSystem() {
/*  92 */     return newFileSystem(newRandomFileSystemName());
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FileSystem newFileSystem(String name) {
/* 110 */     return newFileSystem(name, Configuration.forCurrentPlatform());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FileSystem newFileSystem(Configuration configuration) {
/* 117 */     return newFileSystem(newRandomFileSystemName(), configuration);
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
/*     */   public static FileSystem newFileSystem(String name, Configuration configuration) {
/*     */     try {
/* 130 */       URI uri = new URI("jimfs", name, null, null);
/* 131 */       return newFileSystem(uri, configuration);
/* 132 */     } catch (URISyntaxException e) {
/* 133 */       throw new IllegalArgumentException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static FileSystem newFileSystem(URI uri, Configuration config) {
/* 139 */     Preconditions.checkArgument("jimfs".equals(uri.getScheme()), "uri (%s) must have scheme %s", new Object[] { uri, "jimfs" });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 145 */       JimfsFileSystem fileSystem = JimfsFileSystems.newFileSystem(JimfsFileSystemProvider.instance(), uri, config);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 155 */       ImmutableMap<String, ?> env = ImmutableMap.of("fileSystem", fileSystem);
/* 156 */       FileSystems.newFileSystem(uri, (Map<String, ?>)env, SystemJimfsFileSystemProvider.class.getClassLoader());
/*     */       
/* 158 */       return fileSystem;
/* 159 */     } catch (IOException e) {
/* 160 */       throw new AssertionError(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static String newRandomFileSystemName() {
/* 165 */     return UUID.randomUUID().toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\Jimfs.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */