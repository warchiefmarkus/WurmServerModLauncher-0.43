/*    */ package com.google.common.jimfs;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import com.google.common.base.Ascii;
/*    */ import com.google.common.base.MoreObjects;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.PathMatcher;
/*    */ import java.util.regex.Pattern;
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
/*    */ final class PathMatchers
/*    */ {
/*    */   public static PathMatcher getPathMatcher(String syntaxAndPattern, String separators, ImmutableSet<PathNormalization> normalizations) {
/* 52 */     int syntaxSeparator = syntaxAndPattern.indexOf(':');
/* 53 */     Preconditions.checkArgument((syntaxSeparator > 0), "Must be of the form 'syntax:pattern': %s", new Object[] { syntaxAndPattern });
/*    */ 
/*    */     
/* 56 */     String syntax = Ascii.toLowerCase(syntaxAndPattern.substring(0, syntaxSeparator));
/* 57 */     String pattern = syntaxAndPattern.substring(syntaxSeparator + 1);
/*    */     
/* 59 */     switch (syntax) {
/*    */       case "glob":
/* 61 */         pattern = GlobToRegex.toRegex(pattern, separators);
/*    */       
/*    */       case "regex":
/* 64 */         return fromRegex(pattern, (Iterable<PathNormalization>)normalizations);
/*    */     } 
/* 66 */     throw new UnsupportedOperationException("Invalid syntax: " + syntaxAndPattern);
/*    */   }
/*    */ 
/*    */   
/*    */   private static PathMatcher fromRegex(String regex, Iterable<PathNormalization> normalizations) {
/* 71 */     return new RegexPathMatcher(PathNormalization.compilePattern(regex, normalizations));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @VisibleForTesting
/*    */   static final class RegexPathMatcher
/*    */     implements PathMatcher
/*    */   {
/*    */     private final Pattern pattern;
/*    */ 
/*    */     
/*    */     private RegexPathMatcher(Pattern pattern) {
/* 84 */       this.pattern = (Pattern)Preconditions.checkNotNull(pattern);
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean matches(Path path) {
/* 89 */       return this.pattern.matcher(path.toString()).matches();
/*    */     }
/*    */ 
/*    */     
/*    */     public String toString() {
/* 94 */       return MoreObjects.toStringHelper(this).addValue(this.pattern).toString();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\PathMatchers.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */