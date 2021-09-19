/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.base.Ascii;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableListMultimap;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Iterables;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.attribute.FileTime;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class PathURLConnection
/*     */   extends URLConnection
/*     */ {
/*     */   private static final String HTTP_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss 'GMT'";
/*     */   private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
/*     */   private InputStream stream;
/*  64 */   private ImmutableListMultimap<String, String> headers = ImmutableListMultimap.of();
/*     */   
/*     */   PathURLConnection(URL url) {
/*  67 */     super((URL)Preconditions.checkNotNull(url));
/*     */   }
/*     */   
/*     */   public void connect() throws IOException {
/*     */     long length;
/*  72 */     if (this.stream != null) {
/*     */       return;
/*     */     }
/*     */     
/*  76 */     Path path = Paths.get(toUri(this.url));
/*     */ 
/*     */     
/*  79 */     if (Files.isDirectory(path, new java.nio.file.LinkOption[0])) {
/*     */ 
/*     */       
/*  82 */       StringBuilder stringBuilder = new StringBuilder();
/*  83 */       try (DirectoryStream<Path> files = Files.newDirectoryStream(path)) {
/*  84 */         for (Path file : files) {
/*  85 */           stringBuilder.append(file.getFileName()).append('\n');
/*     */         }
/*     */       } 
/*  88 */       byte[] bytes = stringBuilder.toString().getBytes(StandardCharsets.UTF_8);
/*  89 */       this.stream = new ByteArrayInputStream(bytes);
/*  90 */       length = bytes.length;
/*     */     } else {
/*  92 */       this.stream = Files.newInputStream(path, new java.nio.file.OpenOption[0]);
/*  93 */       length = Files.size(path);
/*     */     } 
/*     */     
/*  96 */     FileTime lastModified = Files.getLastModifiedTime(path, new java.nio.file.LinkOption[0]);
/*  97 */     String contentType = (String)MoreObjects.firstNonNull(Files.probeContentType(path), "application/octet-stream");
/*     */ 
/*     */     
/* 100 */     ImmutableListMultimap.Builder<String, String> builder = ImmutableListMultimap.builder();
/* 101 */     builder.put("content-length", "" + length);
/* 102 */     builder.put("content-type", contentType);
/* 103 */     if (lastModified != null) {
/* 104 */       DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
/* 105 */       format.setTimeZone(TimeZone.getTimeZone("GMT"));
/* 106 */       builder.put("last-modified", format.format(new Date(lastModified.toMillis())));
/*     */     } 
/*     */     
/* 109 */     this.headers = builder.build();
/*     */   }
/*     */   
/*     */   private static URI toUri(URL url) throws IOException {
/*     */     try {
/* 114 */       return url.toURI();
/* 115 */     } catch (URISyntaxException e) {
/* 116 */       throw new IOException("URL " + url + " cannot be converted to a URI", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() throws IOException {
/* 122 */     connect();
/* 123 */     return this.stream;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, List<String>> getHeaderFields() {
/*     */     try {
/* 130 */       connect();
/* 131 */     } catch (IOException e) {
/* 132 */       return (Map<String, List<String>>)ImmutableMap.of();
/*     */     } 
/* 134 */     return (Map<String, List<String>>)this.headers.asMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getHeaderField(String name) {
/*     */     try {
/* 140 */       connect();
/* 141 */     } catch (IOException e) {
/* 142 */       return null;
/*     */     } 
/*     */ 
/*     */     
/* 146 */     return (String)Iterables.getFirst((Iterable)this.headers.get(Ascii.toLowerCase(name)), null);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\PathURLConnection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */