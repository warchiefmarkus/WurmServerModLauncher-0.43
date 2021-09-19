/*     */ package javax.activation;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MimeTypeParameterList
/*     */ {
/*     */   private Hashtable parameters;
/*     */   private static final String TSPECIALS = "()<>@,;:/[]?=\\\"";
/*     */   
/*     */   public MimeTypeParameterList() {
/*  67 */     this.parameters = new Hashtable();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MimeTypeParameterList(String parameterList) throws MimeTypeParseException {
/*  77 */     this.parameters = new Hashtable();
/*     */ 
/*     */     
/*  80 */     parse(parameterList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void parse(String parameterList) throws MimeTypeParseException {
/*  89 */     if (parameterList == null) {
/*     */       return;
/*     */     }
/*  92 */     int length = parameterList.length();
/*  93 */     if (length <= 0) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  98 */     int i = skipWhiteSpace(parameterList, 0); char c;
/*  99 */     for (; i < length && (c = parameterList.charAt(i)) == ';'; 
/* 100 */       i = skipWhiteSpace(parameterList, i)) {
/*     */       String value;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 106 */       i++;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 111 */       i = skipWhiteSpace(parameterList, i);
/*     */ 
/*     */       
/* 114 */       if (i >= length) {
/*     */         return;
/*     */       }
/*     */       
/* 118 */       int lastIndex = i;
/* 119 */       while (i < length && isTokenChar(parameterList.charAt(i))) {
/* 120 */         i++;
/*     */       }
/* 122 */       String name = parameterList.substring(lastIndex, i).toLowerCase(Locale.ENGLISH);
/*     */ 
/*     */ 
/*     */       
/* 126 */       i = skipWhiteSpace(parameterList, i);
/*     */       
/* 128 */       if (i >= length || parameterList.charAt(i) != '=') {
/* 129 */         throw new MimeTypeParseException("Couldn't find the '=' that separates a parameter name from its value.");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 134 */       i++;
/* 135 */       i = skipWhiteSpace(parameterList, i);
/*     */       
/* 137 */       if (i >= length) {
/* 138 */         throw new MimeTypeParseException("Couldn't find a value for parameter named " + name);
/*     */       }
/*     */ 
/*     */       
/* 142 */       c = parameterList.charAt(i);
/* 143 */       if (c == '"') {
/*     */         
/* 145 */         i++;
/* 146 */         if (i >= length) {
/* 147 */           throw new MimeTypeParseException("Encountered unterminated quoted parameter value.");
/*     */         }
/*     */         
/* 150 */         lastIndex = i;
/*     */ 
/*     */         
/* 153 */         while (i < length) {
/* 154 */           c = parameterList.charAt(i);
/* 155 */           if (c == '"')
/*     */             break; 
/* 157 */           if (c == '\\')
/*     */           {
/*     */ 
/*     */             
/* 161 */             i++;
/*     */           }
/* 163 */           i++;
/*     */         } 
/* 165 */         if (c != '"') {
/* 166 */           throw new MimeTypeParseException("Encountered unterminated quoted parameter value.");
/*     */         }
/*     */         
/* 169 */         value = unquote(parameterList.substring(lastIndex, i));
/*     */         
/* 171 */         i++;
/* 172 */       } else if (isTokenChar(c)) {
/*     */ 
/*     */         
/* 175 */         lastIndex = i;
/* 176 */         while (i < length && isTokenChar(parameterList.charAt(i)))
/* 177 */           i++; 
/* 178 */         value = parameterList.substring(lastIndex, i);
/*     */       } else {
/*     */         
/* 181 */         throw new MimeTypeParseException("Unexpected character encountered at index " + i);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 186 */       this.parameters.put(name, value);
/*     */     } 
/* 188 */     if (i < length) {
/* 189 */       throw new MimeTypeParseException("More characters encountered in input than expected.");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 200 */     return this.parameters.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 209 */     return this.parameters.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String get(String name) {
/* 220 */     return (String)this.parameters.get(name.trim().toLowerCase(Locale.ENGLISH));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(String name, String value) {
/* 231 */     this.parameters.put(name.trim().toLowerCase(Locale.ENGLISH), value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(String name) {
/* 240 */     this.parameters.remove(name.trim().toLowerCase(Locale.ENGLISH));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration getNames() {
/* 249 */     return this.parameters.keys();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 256 */     StringBuffer buffer = new StringBuffer();
/* 257 */     buffer.ensureCapacity(this.parameters.size() * 16);
/*     */ 
/*     */     
/* 260 */     Enumeration keys = this.parameters.keys();
/* 261 */     while (keys.hasMoreElements()) {
/* 262 */       String key = keys.nextElement();
/* 263 */       buffer.append("; ");
/* 264 */       buffer.append(key);
/* 265 */       buffer.append('=');
/* 266 */       buffer.append(quote((String)this.parameters.get(key)));
/*     */     } 
/*     */     
/* 269 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isTokenChar(char c) {
/* 278 */     return (c > ' ' && c < '' && "()<>@,;:/[]?=\\\"".indexOf(c) < 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int skipWhiteSpace(String rawdata, int i) {
/* 286 */     int length = rawdata.length();
/* 287 */     while (i < length && Character.isWhitespace(rawdata.charAt(i)))
/* 288 */       i++; 
/* 289 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String quote(String value) {
/* 296 */     boolean needsQuotes = false;
/*     */ 
/*     */     
/* 299 */     int length = value.length();
/* 300 */     for (int i = 0; i < length && !needsQuotes; i++) {
/* 301 */       needsQuotes = !isTokenChar(value.charAt(i));
/*     */     }
/*     */     
/* 304 */     if (needsQuotes) {
/* 305 */       StringBuffer buffer = new StringBuffer();
/* 306 */       buffer.ensureCapacity((int)(length * 1.5D));
/*     */ 
/*     */       
/* 309 */       buffer.append('"');
/*     */ 
/*     */       
/* 312 */       for (int j = 0; j < length; j++) {
/* 313 */         char c = value.charAt(j);
/* 314 */         if (c == '\\' || c == '"')
/* 315 */           buffer.append('\\'); 
/* 316 */         buffer.append(c);
/*     */       } 
/*     */ 
/*     */       
/* 320 */       buffer.append('"');
/*     */       
/* 322 */       return buffer.toString();
/*     */     } 
/* 324 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String unquote(String value) {
/* 333 */     int valueLength = value.length();
/* 334 */     StringBuffer buffer = new StringBuffer();
/* 335 */     buffer.ensureCapacity(valueLength);
/*     */     
/* 337 */     boolean escaped = false;
/* 338 */     for (int i = 0; i < valueLength; i++) {
/* 339 */       char currentChar = value.charAt(i);
/* 340 */       if (!escaped && currentChar != '\\') {
/* 341 */         buffer.append(currentChar);
/* 342 */       } else if (escaped) {
/* 343 */         buffer.append(currentChar);
/* 344 */         escaped = false;
/*     */       } else {
/* 346 */         escaped = true;
/*     */       } 
/*     */     } 
/*     */     
/* 350 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\activation\MimeTypeParameterList.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */