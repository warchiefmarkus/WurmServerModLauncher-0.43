/*    */ package com.google.common.jimfs;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.nio.file.InvalidPathException;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class UnixPathType
/*    */   extends PathType
/*    */ {
/* 35 */   static final PathType INSTANCE = new UnixPathType();
/*    */   
/*    */   private UnixPathType() {
/* 38 */     super(false, '/', new char[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   public PathType.ParseResult parsePath(String path) {
/* 43 */     if (path.isEmpty()) {
/* 44 */       return emptyPath();
/*    */     }
/*    */     
/* 47 */     checkValid(path);
/*    */     
/* 49 */     String root = path.startsWith("/") ? "/" : null;
/* 50 */     return new PathType.ParseResult(root, splitter().split(path));
/*    */   }
/*    */   
/*    */   private static void checkValid(String path) {
/* 54 */     int nulIndex = path.indexOf(false);
/* 55 */     if (nulIndex != -1) {
/* 56 */       throw new InvalidPathException(path, "nul character not allowed", nulIndex);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString(@Nullable String root, Iterable<String> names) {
/* 62 */     StringBuilder builder = new StringBuilder();
/* 63 */     if (root != null) {
/* 64 */       builder.append(root);
/*    */     }
/* 66 */     joiner().appendTo(builder, names);
/* 67 */     return builder.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toUriPath(String root, Iterable<String> names, boolean directory) {
/* 72 */     StringBuilder builder = new StringBuilder();
/* 73 */     for (String name : names) {
/* 74 */       builder.append('/').append(name);
/*    */     }
/*    */     
/* 77 */     if (directory || builder.length() == 0) {
/* 78 */       builder.append('/');
/*    */     }
/* 80 */     return builder.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public PathType.ParseResult parseUriPath(String uriPath) {
/* 85 */     Preconditions.checkArgument(uriPath.startsWith("/"), "uriPath (%s) must start with /", new Object[] { uriPath });
/* 86 */     return parsePath(uriPath);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\UnixPathType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */