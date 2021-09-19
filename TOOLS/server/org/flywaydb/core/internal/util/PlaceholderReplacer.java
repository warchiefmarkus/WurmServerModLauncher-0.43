/*     */ package org.flywaydb.core.internal.util;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.flywaydb.core.api.FlywayException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlaceholderReplacer
/*     */ {
/*  34 */   public static final PlaceholderReplacer NO_PLACEHOLDERS = new PlaceholderReplacer(new HashMap<Object, Object>(), "", "")
/*     */     {
/*     */       public String replacePlaceholders(String input) {
/*  37 */         return input;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Map<String, String> placeholders;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String placeholderPrefix;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String placeholderSuffix;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PlaceholderReplacer(Map<String, String> placeholders, String placeholderPrefix, String placeholderSuffix) {
/*  64 */     this.placeholders = placeholders;
/*  65 */     this.placeholderPrefix = placeholderPrefix;
/*  66 */     this.placeholderSuffix = placeholderSuffix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String replacePlaceholders(String input) {
/*  76 */     String noPlaceholders = input;
/*     */     
/*  78 */     for (String placeholder : this.placeholders.keySet()) {
/*  79 */       String searchTerm = this.placeholderPrefix + placeholder + this.placeholderSuffix;
/*  80 */       String value = this.placeholders.get(placeholder);
/*  81 */       noPlaceholders = StringUtils.replaceAll(noPlaceholders, searchTerm, (value == null) ? "" : value);
/*     */     } 
/*  83 */     checkForUnmatchedPlaceholderExpression(noPlaceholders);
/*     */     
/*  85 */     return noPlaceholders;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkForUnmatchedPlaceholderExpression(String input) {
/*  96 */     String regex = Pattern.quote(this.placeholderPrefix) + "(.+?)" + Pattern.quote(this.placeholderSuffix);
/*  97 */     Matcher matcher = Pattern.compile(regex).matcher(input);
/*     */     
/*  99 */     Set<String> unmatchedPlaceHolderExpressions = new TreeSet<String>();
/* 100 */     while (matcher.find()) {
/* 101 */       unmatchedPlaceHolderExpressions.add(matcher.group());
/*     */     }
/*     */     
/* 104 */     if (!unmatchedPlaceHolderExpressions.isEmpty())
/* 105 */       throw new FlywayException("No value provided for placeholder expressions: " + 
/* 106 */           StringUtils.collectionToCommaDelimitedString(unmatchedPlaceHolderExpressions) + ".  Check your configuration!"); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\PlaceholderReplacer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */