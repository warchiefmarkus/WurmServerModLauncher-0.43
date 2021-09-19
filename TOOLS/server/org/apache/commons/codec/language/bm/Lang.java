/*     */ package org.apache.commons.codec.language.bm;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Scanner;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Lang
/*     */ {
/*     */   private static final class LangRule
/*     */   {
/*     */     private final boolean acceptOnMatch;
/*     */     private final Set<String> languages;
/*     */     private final Pattern pattern;
/*     */     
/*     */     private LangRule(Pattern pattern, Set<String> languages, boolean acceptOnMatch) {
/*  88 */       this.pattern = pattern;
/*  89 */       this.languages = languages;
/*  90 */       this.acceptOnMatch = acceptOnMatch;
/*     */     }
/*     */     
/*     */     public boolean matches(String txt) {
/*  94 */       return this.pattern.matcher(txt).find();
/*     */     }
/*     */   }
/*     */   
/*  98 */   private static final Map<NameType, Lang> Langs = new EnumMap<NameType, Lang>(NameType.class);
/*     */   
/*     */   private static final String LANGUAGE_RULES_RN = "org/apache/commons/codec/language/bm/lang.txt";
/*     */   
/*     */   static {
/* 103 */     for (NameType s : NameType.values()) {
/* 104 */       Langs.put(s, loadFromResource("org/apache/commons/codec/language/bm/lang.txt", Languages.getInstance(s)));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final Languages languages;
/*     */   
/*     */   private final List<LangRule> rules;
/*     */ 
/*     */   
/*     */   public static Lang instance(NameType nameType) {
/* 116 */     return Langs.get(nameType);
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
/*     */   public static Lang loadFromResource(String languageRulesResourceName, Languages languages) {
/* 135 */     List<LangRule> rules = new ArrayList<LangRule>();
/* 136 */     InputStream lRulesIS = Lang.class.getClassLoader().getResourceAsStream(languageRulesResourceName);
/*     */     
/* 138 */     if (lRulesIS == null) {
/* 139 */       throw new IllegalStateException("Unable to resolve required resource:org/apache/commons/codec/language/bm/lang.txt");
/*     */     }
/*     */     
/* 142 */     Scanner scanner = new Scanner(lRulesIS, "UTF-8");
/* 143 */     boolean inExtendedComment = false;
/* 144 */     while (scanner.hasNextLine()) {
/* 145 */       String rawLine = scanner.nextLine();
/* 146 */       String line = rawLine;
/*     */       
/* 148 */       if (inExtendedComment) {
/* 149 */         if (line.endsWith("*/")) {
/* 150 */           inExtendedComment = false;
/*     */         }
/*     */         
/*     */         continue;
/*     */       } 
/* 155 */       if (line.startsWith("/*")) {
/* 156 */         inExtendedComment = true;
/*     */         continue;
/*     */       } 
/* 159 */       int cmtI = line.indexOf("//");
/* 160 */       if (cmtI >= 0)
/*     */       {
/* 162 */         line = line.substring(0, cmtI);
/*     */       }
/*     */ 
/*     */       
/* 166 */       line = line.trim();
/*     */       
/* 168 */       if (line.length() == 0) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */       
/* 173 */       String[] parts = line.split("\\s+");
/*     */ 
/*     */       
/* 176 */       if (parts.length != 3) {
/*     */         
/* 178 */         System.err.println("Warning: malformed line '" + rawLine + "'");
/*     */         
/*     */         continue;
/*     */       } 
/* 182 */       Pattern pattern = Pattern.compile(parts[0]);
/* 183 */       String[] langs = parts[1].split("\\+");
/* 184 */       boolean accept = parts[2].equals("true");
/*     */       
/* 186 */       rules.add(new LangRule(pattern, new HashSet(Arrays.asList((Object[])langs)), accept));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 191 */     return new Lang(rules, languages);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Lang(List<LangRule> rules, Languages languages) {
/* 198 */     this.rules = Collections.unmodifiableList(rules);
/* 199 */     this.languages = languages;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String guessLanguage(String text) {
/* 210 */     Languages.LanguageSet ls = guessLanguages(text);
/* 211 */     return ls.isSingleton() ? ls.getAny() : "any";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Languages.LanguageSet guessLanguages(String input) {
/* 222 */     String text = input.toLowerCase(Locale.ENGLISH);
/*     */ 
/*     */     
/* 225 */     Set<String> langs = new HashSet<String>(this.languages.getLanguages());
/* 226 */     for (LangRule rule : this.rules) {
/* 227 */       if (rule.matches(text)) {
/*     */         
/* 229 */         if (rule.acceptOnMatch) {
/*     */           
/* 231 */           langs.retainAll(rule.languages);
/*     */           continue;
/*     */         } 
/* 234 */         langs.removeAll(rule.languages);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 242 */     Languages.LanguageSet ls = Languages.LanguageSet.from(langs);
/* 243 */     return ls.equals(Languages.NO_LANGUAGES) ? Languages.ANY_LANGUAGE : ls;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\commons\codec\language\bm\Lang.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */