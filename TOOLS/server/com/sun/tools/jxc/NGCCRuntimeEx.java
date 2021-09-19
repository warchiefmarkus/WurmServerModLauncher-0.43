/*     */ package com.sun.tools.jxc;
/*     */ 
/*     */ import com.sun.tools.jxc.gen.config.NGCCRuntime;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.regex.Pattern;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class NGCCRuntimeEx
/*     */   extends NGCCRuntime
/*     */ {
/*     */   private final ErrorHandler errorHandler;
/*     */   
/*     */   public NGCCRuntimeEx(ErrorHandler errorHandler) {
/*  64 */     this.errorHandler = errorHandler;
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
/*     */   public File getBaseDir(String baseDir) throws SAXException {
/*  76 */     File dir = new File(baseDir);
/*  77 */     if (dir.exists()) {
/*  78 */       return dir;
/*     */     }
/*  80 */     SAXParseException e = new SAXParseException(Messages.BASEDIR_DOESNT_EXIST.format(new Object[] { dir.getAbsolutePath() }, ), getLocator());
/*     */ 
/*     */     
/*  83 */     this.errorHandler.error(e);
/*  84 */     throw e;
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
/*     */   public List<Pattern> getIncludePatterns(List<String> includeContent) {
/*  97 */     List<Pattern> includeRegexList = new ArrayList<Pattern>();
/*  98 */     for (int i = 0; i < includeContent.size(); i++) {
/*  99 */       String includes = includeContent.get(i);
/* 100 */       String regex = convertToRegex(includes);
/* 101 */       Pattern pattern = Pattern.compile(regex);
/* 102 */       includeRegexList.add(pattern);
/*     */     } 
/* 104 */     return includeRegexList;
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
/*     */   public List getExcludePatterns(List<String> excludeContent) {
/* 117 */     List<Pattern> excludeRegexList = new ArrayList();
/* 118 */     for (int i = 0; i < excludeContent.size(); i++) {
/* 119 */       String excludes = excludeContent.get(i);
/* 120 */       String regex = convertToRegex(excludes);
/* 121 */       Pattern pattern = Pattern.compile(regex);
/* 122 */       excludeRegexList.add(pattern);
/*     */     } 
/* 124 */     return excludeRegexList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String convertToRegex(String pattern) {
/* 133 */     StringBuilder regex = new StringBuilder();
/* 134 */     char nc = ' ';
/* 135 */     if (pattern.length() > 0)
/*     */     {
/* 137 */       for (int i = 0; i < pattern.length(); i++) {
/* 138 */         char c = pattern.charAt(i);
/* 139 */         int j = i;
/* 140 */         nc = ' ';
/* 141 */         if (j + 1 != pattern.length()) {
/* 142 */           nc = pattern.charAt(j + 1);
/*     */         }
/*     */         
/* 145 */         if (c == '.' && nc != '.') {
/* 146 */           regex.append('\\');
/* 147 */           regex.append('.');
/*     */         }
/* 149 */         else if (c != '.' || nc != '.') {
/*     */ 
/*     */           
/* 152 */           if (c == '*' && nc == '*') {
/* 153 */             regex.append(".*");
/*     */             break;
/*     */           } 
/* 156 */           if (c == '*') {
/* 157 */             regex.append("[^\\.]+");
/*     */           
/*     */           }
/* 160 */           else if (c == '?') {
/* 161 */             regex.append("[^\\.]");
/*     */           } else {
/*     */             
/* 164 */             regex.append(c);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/* 169 */     return regex.toString();
/*     */   }
/*     */   
/*     */   protected void unexpectedX(String token) throws SAXException {
/* 173 */     this.errorHandler.error(new SAXParseException(Messages.UNEXPECTED_NGCC_TOKEN.format(new Object[] { token, Integer.valueOf(getLocator().getLineNumber()), Integer.valueOf(getLocator().getColumnNumber()) }, ), getLocator()));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\jxc\NGCCRuntimeEx.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */