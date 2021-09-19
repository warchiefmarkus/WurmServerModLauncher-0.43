/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Iterables;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.Arrays;
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
/*     */ public abstract class PathType
/*     */ {
/*     */   private final boolean allowsMultipleRoots;
/*     */   private final String separator;
/*     */   private final String otherSeparators;
/*     */   private final Joiner joiner;
/*     */   private final Splitter splitter;
/*     */   
/*     */   public static PathType unix() {
/*  47 */     return UnixPathType.INSTANCE;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PathType windows() {
/*  70 */     return WindowsPathType.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected PathType(boolean allowsMultipleRoots, char separator, char... otherSeparators) {
/*  80 */     this.separator = String.valueOf(separator);
/*  81 */     this.allowsMultipleRoots = allowsMultipleRoots;
/*  82 */     this.otherSeparators = String.valueOf(otherSeparators);
/*  83 */     this.joiner = Joiner.on(separator);
/*  84 */     this.splitter = createSplitter(separator, otherSeparators);
/*     */   }
/*     */   
/*  87 */   private static final char[] regexReservedChars = "^$.?+*\\[]{}()".toCharArray();
/*     */   
/*     */   static {
/*  90 */     Arrays.sort(regexReservedChars);
/*     */   }
/*     */   
/*     */   private static boolean isRegexReserved(char c) {
/*  94 */     return (Arrays.binarySearch(regexReservedChars, c) >= 0);
/*     */   }
/*     */   
/*     */   private static Splitter createSplitter(char separator, char... otherSeparators) {
/*  98 */     if (otherSeparators.length == 0) {
/*  99 */       return Splitter.on(separator).omitEmptyStrings();
/*     */     }
/*     */ 
/*     */     
/* 103 */     StringBuilder patternBuilder = new StringBuilder();
/* 104 */     patternBuilder.append("[");
/* 105 */     appendToRegex(separator, patternBuilder);
/* 106 */     for (char other : otherSeparators) {
/* 107 */       appendToRegex(other, patternBuilder);
/*     */     }
/* 109 */     patternBuilder.append("]");
/* 110 */     return Splitter.onPattern(patternBuilder.toString()).omitEmptyStrings();
/*     */   }
/*     */   
/*     */   private static void appendToRegex(char separator, StringBuilder patternBuilder) {
/* 114 */     if (isRegexReserved(separator)) {
/* 115 */       patternBuilder.append("\\");
/*     */     }
/* 117 */     patternBuilder.append(separator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean allowsMultipleRoots() {
/* 124 */     return this.allowsMultipleRoots;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getSeparator() {
/* 132 */     return this.separator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getOtherSeparators() {
/* 140 */     return this.otherSeparators;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Joiner joiner() {
/* 147 */     return this.joiner;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Splitter splitter() {
/* 154 */     return this.splitter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ParseResult emptyPath() {
/* 161 */     return new ParseResult(null, (Iterable<String>)ImmutableList.of(""));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final URI toUri(URI fileSystemUri, String root, Iterable<String> names, boolean directory) {
/* 196 */     String path = toUriPath(root, names, directory);
/*     */ 
/*     */     
/*     */     try {
/* 200 */       return new URI(fileSystemUri.getScheme(), fileSystemUri.getUserInfo(), fileSystemUri.getHost(), fileSystemUri.getPort(), path, null, null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 208 */     catch (URISyntaxException e) {
/* 209 */       throw new AssertionError(e);
/*     */     } 
/*     */   }
/*     */   public abstract ParseResult parsePath(String paramString);
/*     */   
/*     */   public abstract String toString(@Nullable String paramString, Iterable<String> paramIterable);
/*     */   
/*     */   public final ParseResult fromUri(URI uri) {
/* 217 */     return parseUriPath(uri.getPath());
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract String toUriPath(String paramString, Iterable<String> paramIterable, boolean paramBoolean);
/*     */ 
/*     */   
/*     */   protected abstract ParseResult parseUriPath(String paramString);
/*     */   
/*     */   public static final class ParseResult
/*     */   {
/*     */     public ParseResult(@Nullable String root, Iterable<String> names) {
/* 229 */       this.root = root;
/* 230 */       this.names = (Iterable<String>)Preconditions.checkNotNull(names);
/*     */     }
/*     */     @Nullable
/*     */     private final String root;
/*     */     private final Iterable<String> names;
/*     */     
/*     */     public boolean isAbsolute() {
/* 237 */       return (this.root != null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isRoot() {
/* 244 */       return (this.root != null && Iterables.isEmpty(this.names));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String root() {
/* 252 */       return this.root;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Iterable<String> names() {
/* 259 */       return this.names;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\PathType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */