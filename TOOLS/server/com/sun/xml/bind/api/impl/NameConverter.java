/*     */ package com.sun.xml.bind.api.impl;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface NameConverter
/*     */ {
/* 104 */   public static final NameConverter standard = new Standard();
/*     */   
/*     */   public static class Standard extends NameUtil implements NameConverter {
/*     */     public String toClassName(String s) {
/* 108 */       return toMixedCaseName(toWordList(s), true);
/*     */     }
/*     */     public String toVariableName(String s) {
/* 111 */       return toMixedCaseName(toWordList(s), false);
/*     */     }
/*     */     public String toInterfaceName(String token) {
/* 114 */       return toClassName(token);
/*     */     }
/*     */     public String toPropertyName(String s) {
/* 117 */       String prop = toClassName(s);
/*     */ 
/*     */       
/* 120 */       if (prop.equals("Class"))
/* 121 */         prop = "Clazz"; 
/* 122 */       return prop;
/*     */     }
/*     */     public String toConstantName(String token) {
/* 125 */       return super.toConstantName(token);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toPackageName(String nsUri) {
/* 137 */       int idx = nsUri.indexOf(':');
/* 138 */       String scheme = "";
/* 139 */       if (idx >= 0) {
/* 140 */         scheme = nsUri.substring(0, idx);
/* 141 */         if (scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("urn")) {
/* 142 */           nsUri = nsUri.substring(idx + 1);
/*     */         }
/*     */       } 
/*     */       
/* 146 */       ArrayList<String> tokens = tokenize(nsUri, "/: ");
/* 147 */       if (tokens.size() == 0) {
/* 148 */         return null;
/*     */       }
/*     */ 
/*     */       
/* 152 */       if (tokens.size() > 1) {
/*     */ 
/*     */ 
/*     */         
/* 156 */         String lastToken = tokens.get(tokens.size() - 1);
/* 157 */         idx = lastToken.lastIndexOf('.');
/* 158 */         if (idx > 0) {
/* 159 */           lastToken = lastToken.substring(0, idx);
/* 160 */           tokens.set(tokens.size() - 1, lastToken);
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 165 */       String domain = tokens.get(0);
/* 166 */       idx = domain.indexOf(':');
/* 167 */       if (idx >= 0) domain = domain.substring(0, idx); 
/* 168 */       ArrayList<String> r = reverse(tokenize(domain, scheme.equals("urn") ? ".-" : "."));
/* 169 */       if (((String)r.get(r.size() - 1)).equalsIgnoreCase("www"))
/*     */       {
/* 171 */         r.remove(r.size() - 1);
/*     */       }
/*     */ 
/*     */       
/* 175 */       tokens.addAll(1, r);
/* 176 */       tokens.remove(0);
/*     */ 
/*     */       
/* 179 */       for (int i = 0; i < tokens.size(); i++) {
/*     */ 
/*     */         
/* 182 */         String token = tokens.get(i);
/* 183 */         token = removeIllegalIdentifierChars(token);
/*     */ 
/*     */         
/* 186 */         if (!NameUtil.isJavaIdentifier(token)) {
/* 187 */           token = '_' + token;
/*     */         }
/*     */         
/* 190 */         tokens.set(i, token.toLowerCase());
/*     */       } 
/*     */ 
/*     */       
/* 194 */       return combine(tokens, '.');
/*     */     }
/*     */ 
/*     */     
/*     */     private static String removeIllegalIdentifierChars(String token) {
/* 199 */       StringBuffer newToken = new StringBuffer();
/* 200 */       for (int i = 0; i < token.length(); i++) {
/* 201 */         char c = token.charAt(i);
/*     */         
/* 203 */         if (i == 0 && !Character.isJavaIdentifierStart(c)) {
/*     */           
/* 205 */           newToken.append('_').append(c);
/* 206 */         } else if (!Character.isJavaIdentifierPart(c)) {
/*     */           
/* 208 */           newToken.append('_');
/*     */         } else {
/*     */           
/* 211 */           newToken.append(c);
/*     */         } 
/*     */       } 
/* 214 */       return newToken.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     private static ArrayList<String> tokenize(String str, String sep) {
/* 219 */       StringTokenizer tokens = new StringTokenizer(str, sep);
/* 220 */       ArrayList<String> r = new ArrayList<String>();
/*     */       
/* 222 */       while (tokens.hasMoreTokens()) {
/* 223 */         r.add(tokens.nextToken());
/*     */       }
/* 225 */       return r;
/*     */     }
/*     */     
/*     */     private static <T> ArrayList<T> reverse(List<T> a) {
/* 229 */       ArrayList<T> r = new ArrayList<T>();
/*     */       
/* 231 */       for (int i = a.size() - 1; i >= 0; i--) {
/* 232 */         r.add(a.get(i));
/*     */       }
/* 234 */       return r;
/*     */     }
/*     */     
/*     */     private static String combine(List<E> r, char sep) {
/* 238 */       StringBuilder buf = new StringBuilder(r.get(0).toString());
/*     */       
/* 240 */       for (int i = 1; i < r.size(); i++) {
/* 241 */         buf.append(sep);
/* 242 */         buf.append(r.get(i));
/*     */       } 
/*     */       
/* 245 */       return buf.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 255 */   public static final NameConverter jaxrpcCompatible = new Standard() {
/*     */       protected boolean isPunct(char c) {
/* 257 */         return (c == '.' || c == '-' || c == ';' || c == '·' || c == '·' || c == '۝' || c == '۞');
/*     */       }
/*     */       
/*     */       protected boolean isLetter(char c) {
/* 261 */         return (super.isLetter(c) || c == '_');
/*     */       }
/*     */       
/*     */       protected int classify(char c0) {
/* 265 */         if (c0 == '_') return 2; 
/* 266 */         return super.classify(c0);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 273 */   public static final NameConverter smart = new Standard() {
/*     */       public String toConstantName(String token) {
/* 275 */         String name = super.toConstantName(token);
/* 276 */         if (NameUtil.isJavaIdentifier(name)) {
/* 277 */           return name;
/*     */         }
/* 279 */         return '_' + name;
/*     */       }
/*     */     };
/*     */   
/*     */   String toClassName(String paramString);
/*     */   
/*     */   String toInterfaceName(String paramString);
/*     */   
/*     */   String toPropertyName(String paramString);
/*     */   
/*     */   String toConstantName(String paramString);
/*     */   
/*     */   String toVariableName(String paramString);
/*     */   
/*     */   String toPackageName(String paramString);
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\api\impl\NameConverter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */