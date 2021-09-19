/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import java.nio.file.InvalidPathException;
/*     */ import java.util.Iterator;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ final class WindowsPathType
/*     */   extends PathType
/*     */ {
/*  36 */   static final WindowsPathType INSTANCE = new WindowsPathType();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   private static final Pattern WORKING_DIR_WITH_DRIVE = Pattern.compile("^[a-zA-Z]:([^\\\\].*)?$");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   private static final Pattern TRAILING_SPACES = Pattern.compile("[ ]+(\\\\|$)");
/*     */   
/*     */   private WindowsPathType() {
/*  60 */     super(true, '\\', new char[] { '/' });
/*     */   }
/*     */ 
/*     */   
/*     */   public PathType.ParseResult parsePath(String path) {
/*  65 */     String root, original = path;
/*  66 */     path = path.replace('/', '\\');
/*     */     
/*  68 */     if (WORKING_DIR_WITH_DRIVE.matcher(path).matches()) {
/*  69 */       throw new InvalidPathException(original, "Jimfs does not currently support the Windows syntax for a relative path on a specific drive (e.g. \"C:foo\\bar\"");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  76 */     if (path.startsWith("\\\\"))
/*  77 */     { root = parseUncRoot(path, original); }
/*  78 */     else { if (path.startsWith("\\")) {
/*  79 */         throw new InvalidPathException(original, "Jimfs does not currently support the Windows syntax for an absolute path on the current drive (e.g. \"\\foo\\bar\"");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  84 */       root = parseDriveRoot(path); }
/*     */ 
/*     */ 
/*     */     
/*  88 */     int startIndex = (root == null || root.length() > 3) ? 0 : root.length();
/*  89 */     for (int i = startIndex; i < path.length(); i++) {
/*  90 */       char c = path.charAt(i);
/*  91 */       if (isReserved(c)) {
/*  92 */         throw new InvalidPathException(original, "Illegal char <" + c + ">", i);
/*     */       }
/*     */     } 
/*     */     
/*  96 */     Matcher trailingSpaceMatcher = TRAILING_SPACES.matcher(path);
/*  97 */     if (trailingSpaceMatcher.find()) {
/*  98 */       throw new InvalidPathException(original, "Trailing char < >", trailingSpaceMatcher.start());
/*     */     }
/*     */     
/* 101 */     if (root != null) {
/* 102 */       path = path.substring(root.length());
/*     */       
/* 104 */       if (!root.endsWith("\\")) {
/* 105 */         root = root + "\\";
/*     */       }
/*     */     } 
/*     */     
/* 109 */     return new PathType.ParseResult(root, splitter().split(path));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   private static final Pattern UNC_ROOT = Pattern.compile("^(\\\\\\\\)([^\\\\]+)?(\\\\[^\\\\]+)?");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String parseUncRoot(String path, String original) {
/* 122 */     Matcher uncMatcher = UNC_ROOT.matcher(path);
/* 123 */     if (uncMatcher.find()) {
/* 124 */       String host = uncMatcher.group(2);
/* 125 */       if (host == null) {
/* 126 */         throw new InvalidPathException(original, "UNC path is missing hostname");
/*     */       }
/* 128 */       String share = uncMatcher.group(3);
/* 129 */       if (share == null) {
/* 130 */         throw new InvalidPathException(original, "UNC path is missing sharename");
/*     */       }
/*     */       
/* 133 */       return path.substring(uncMatcher.start(), uncMatcher.end());
/*     */     } 
/*     */     
/* 136 */     throw new InvalidPathException(original, "Invalid UNC path");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 143 */   private static final Pattern DRIVE_LETTER_ROOT = Pattern.compile("^[a-zA-Z]:\\\\");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String parseDriveRoot(String path) {
/* 150 */     Matcher drivePathMatcher = DRIVE_LETTER_ROOT.matcher(path);
/* 151 */     if (drivePathMatcher.find()) {
/* 152 */       return path.substring(drivePathMatcher.start(), drivePathMatcher.end());
/*     */     }
/* 154 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isReserved(char c) {
/* 161 */     switch (c) {
/*     */       case '"':
/*     */       case '*':
/*     */       case ':':
/*     */       case '<':
/*     */       case '>':
/*     */       case '?':
/*     */       case '|':
/* 169 */         return true;
/*     */     } 
/* 171 */     return (c <= '\037');
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString(@Nullable String root, Iterable<String> names) {
/* 177 */     StringBuilder builder = new StringBuilder();
/* 178 */     if (root != null) {
/* 179 */       builder.append(root);
/*     */     }
/* 181 */     joiner().appendTo(builder, names);
/* 182 */     return builder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toUriPath(String root, Iterable<String> names, boolean directory) {
/* 187 */     if (root.startsWith("\\\\")) {
/* 188 */       root = root.replace('\\', '/');
/*     */     } else {
/* 190 */       root = "/" + root.replace('\\', '/');
/*     */     } 
/*     */     
/* 193 */     StringBuilder builder = new StringBuilder();
/* 194 */     builder.append(root);
/*     */     
/* 196 */     Iterator<String> iter = names.iterator();
/* 197 */     if (iter.hasNext()) {
/* 198 */       builder.append(iter.next());
/* 199 */       while (iter.hasNext()) {
/* 200 */         builder.append('/').append(iter.next());
/*     */       }
/*     */     } 
/*     */     
/* 204 */     if (directory && builder.charAt(builder.length() - 1) != '/') {
/* 205 */       builder.append('/');
/*     */     }
/*     */     
/* 208 */     return builder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public PathType.ParseResult parseUriPath(String uriPath) {
/* 213 */     uriPath = uriPath.replace('/', '\\');
/* 214 */     if (uriPath.charAt(0) == '\\' && uriPath.charAt(1) != '\\')
/*     */     {
/*     */       
/* 217 */       uriPath = uriPath.substring(1);
/*     */     }
/* 219 */     return parsePath(uriPath);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\WindowsPathType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */