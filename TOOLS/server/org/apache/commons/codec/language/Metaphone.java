/*     */ package org.apache.commons.codec.language;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.apache.commons.codec.EncoderException;
/*     */ import org.apache.commons.codec.StringEncoder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Metaphone
/*     */   implements StringEncoder
/*     */ {
/*     */   private static final String VOWELS = "AEIOU";
/*     */   private static final String FRONTV = "EIY";
/*     */   private static final String VARSON = "CSPTG";
/*  63 */   private int maxCodeLen = 4;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String metaphone(String txt) {
/*  83 */     boolean hard = false;
/*  84 */     if (txt == null || txt.length() == 0) {
/*  85 */       return "";
/*     */     }
/*     */     
/*  88 */     if (txt.length() == 1) {
/*  89 */       return txt.toUpperCase(Locale.ENGLISH);
/*     */     }
/*     */     
/*  92 */     char[] inwd = txt.toUpperCase(Locale.ENGLISH).toCharArray();
/*     */     
/*  94 */     StringBuffer local = new StringBuffer(40);
/*  95 */     StringBuffer code = new StringBuffer(10);
/*     */     
/*  97 */     switch (inwd[0]) {
/*     */       case 'G':
/*     */       case 'K':
/*     */       case 'P':
/* 101 */         if (inwd[1] == 'N') {
/* 102 */           local.append(inwd, 1, inwd.length - 1); break;
/*     */         } 
/* 104 */         local.append(inwd);
/*     */         break;
/*     */       
/*     */       case 'A':
/* 108 */         if (inwd[1] == 'E') {
/* 109 */           local.append(inwd, 1, inwd.length - 1); break;
/*     */         } 
/* 111 */         local.append(inwd);
/*     */         break;
/*     */       
/*     */       case 'W':
/* 115 */         if (inwd[1] == 'R') {
/* 116 */           local.append(inwd, 1, inwd.length - 1);
/*     */           break;
/*     */         } 
/* 119 */         if (inwd[1] == 'H') {
/* 120 */           local.append(inwd, 1, inwd.length - 1);
/* 121 */           local.setCharAt(0, 'W'); break;
/*     */         } 
/* 123 */         local.append(inwd);
/*     */         break;
/*     */       
/*     */       case 'X':
/* 127 */         inwd[0] = 'S';
/* 128 */         local.append(inwd);
/*     */         break;
/*     */       default:
/* 131 */         local.append(inwd);
/*     */         break;
/*     */     } 
/* 134 */     int wdsz = local.length();
/* 135 */     int n = 0;
/*     */     
/* 137 */     while (code.length() < getMaxCodeLen() && n < wdsz) {
/*     */       
/* 139 */       char symb = local.charAt(n);
/*     */       
/* 141 */       if (symb != 'C' && isPreviousChar(local, n, symb)) {
/* 142 */         n++;
/*     */       } else {
/* 144 */         switch (symb) { case 'A': case 'E': case 'I': case 'O':
/*     */           case 'U':
/* 146 */             if (n == 0) {
/* 147 */               code.append(symb);
/*     */             }
/*     */             break;
/*     */           case 'B':
/* 151 */             if (isPreviousChar(local, n, 'M') && isLastChar(wdsz, n)) {
/*     */               break;
/*     */             }
/*     */             
/* 155 */             code.append(symb);
/*     */             break;
/*     */           
/*     */           case 'C':
/* 159 */             if (isPreviousChar(local, n, 'S') && !isLastChar(wdsz, n) && "EIY".indexOf(local.charAt(n + 1)) >= 0) {
/*     */               break;
/*     */             }
/*     */ 
/*     */             
/* 164 */             if (regionMatch(local, n, "CIA")) {
/* 165 */               code.append('X');
/*     */               break;
/*     */             } 
/* 168 */             if (!isLastChar(wdsz, n) && "EIY".indexOf(local.charAt(n + 1)) >= 0) {
/*     */               
/* 170 */               code.append('S');
/*     */               break;
/*     */             } 
/* 173 */             if (isPreviousChar(local, n, 'S') && isNextChar(local, n, 'H')) {
/*     */               
/* 175 */               code.append('K');
/*     */               break;
/*     */             } 
/* 178 */             if (isNextChar(local, n, 'H')) {
/* 179 */               if (n == 0 && wdsz >= 3 && isVowel(local, 2)) {
/*     */ 
/*     */                 
/* 182 */                 code.append('K'); break;
/*     */               } 
/* 184 */               code.append('X');
/*     */               break;
/*     */             } 
/* 187 */             code.append('K');
/*     */             break;
/*     */           
/*     */           case 'D':
/* 191 */             if (!isLastChar(wdsz, n + 1) && isNextChar(local, n, 'G') && "EIY".indexOf(local.charAt(n + 2)) >= 0) {
/*     */ 
/*     */               
/* 194 */               code.append('J'); n += 2; break;
/*     */             } 
/* 196 */             code.append('T');
/*     */             break;
/*     */           
/*     */           case 'G':
/* 200 */             if (isLastChar(wdsz, n + 1) && isNextChar(local, n, 'H')) {
/*     */               break;
/*     */             }
/*     */             
/* 204 */             if (!isLastChar(wdsz, n + 1) && isNextChar(local, n, 'H') && !isVowel(local, n + 2)) {
/*     */               break;
/*     */             }
/*     */ 
/*     */             
/* 209 */             if (n > 0 && (regionMatch(local, n, "GN") || regionMatch(local, n, "GNED"))) {
/*     */               break;
/*     */             }
/*     */ 
/*     */             
/* 214 */             if (isPreviousChar(local, n, 'G')) {
/*     */               
/* 216 */               hard = true;
/*     */             } else {
/* 218 */               hard = false;
/*     */             } 
/* 220 */             if (!isLastChar(wdsz, n) && "EIY".indexOf(local.charAt(n + 1)) >= 0 && !hard) {
/*     */ 
/*     */               
/* 223 */               code.append('J'); break;
/*     */             } 
/* 225 */             code.append('K');
/*     */             break;
/*     */           
/*     */           case 'H':
/* 229 */             if (isLastChar(wdsz, n)) {
/*     */               break;
/*     */             }
/* 232 */             if (n > 0 && "CSPTG".indexOf(local.charAt(n - 1)) >= 0) {
/*     */               break;
/*     */             }
/*     */             
/* 236 */             if (isVowel(local, n + 1)) {
/* 237 */               code.append('H');
/*     */             }
/*     */             break;
/*     */           case 'F':
/*     */           case 'J':
/*     */           case 'L':
/*     */           case 'M':
/*     */           case 'N':
/*     */           case 'R':
/* 246 */             code.append(symb);
/*     */             break;
/*     */           case 'K':
/* 249 */             if (n > 0) {
/* 250 */               if (!isPreviousChar(local, n, 'C'))
/* 251 */                 code.append(symb); 
/*     */               break;
/*     */             } 
/* 254 */             code.append(symb);
/*     */             break;
/*     */           
/*     */           case 'P':
/* 258 */             if (isNextChar(local, n, 'H')) {
/*     */               
/* 260 */               code.append('F'); break;
/*     */             } 
/* 262 */             code.append(symb);
/*     */             break;
/*     */           
/*     */           case 'Q':
/* 266 */             code.append('K');
/*     */             break;
/*     */           case 'S':
/* 269 */             if (regionMatch(local, n, "SH") || regionMatch(local, n, "SIO") || regionMatch(local, n, "SIA")) {
/*     */ 
/*     */               
/* 272 */               code.append('X'); break;
/*     */             } 
/* 274 */             code.append('S');
/*     */             break;
/*     */           
/*     */           case 'T':
/* 278 */             if (regionMatch(local, n, "TIA") || regionMatch(local, n, "TIO")) {
/*     */               
/* 280 */               code.append('X');
/*     */               break;
/*     */             } 
/* 283 */             if (regionMatch(local, n, "TCH")) {
/*     */               break;
/*     */             }
/*     */ 
/*     */             
/* 288 */             if (regionMatch(local, n, "TH")) {
/* 289 */               code.append('0'); break;
/*     */             } 
/* 291 */             code.append('T');
/*     */             break;
/*     */           
/*     */           case 'V':
/* 295 */             code.append('F'); break;
/*     */           case 'W': case 'Y':
/* 297 */             if (!isLastChar(wdsz, n) && isVowel(local, n + 1))
/*     */             {
/* 299 */               code.append(symb);
/*     */             }
/*     */             break;
/*     */           case 'X':
/* 303 */             code.append('K'); code.append('S');
/*     */             break;
/*     */           case 'Z':
/* 306 */             code.append('S'); break; }
/*     */         
/* 308 */         n++;
/*     */       } 
/* 310 */       if (code.length() > getMaxCodeLen()) {
/* 311 */         code.setLength(getMaxCodeLen());
/*     */       }
/*     */     } 
/* 314 */     return code.toString();
/*     */   }
/*     */   
/*     */   private boolean isVowel(StringBuffer string, int index) {
/* 318 */     return ("AEIOU".indexOf(string.charAt(index)) >= 0);
/*     */   }
/*     */   
/*     */   private boolean isPreviousChar(StringBuffer string, int index, char c) {
/* 322 */     boolean matches = false;
/* 323 */     if (index > 0 && index < string.length())
/*     */     {
/* 325 */       matches = (string.charAt(index - 1) == c);
/*     */     }
/* 327 */     return matches;
/*     */   }
/*     */   
/*     */   private boolean isNextChar(StringBuffer string, int index, char c) {
/* 331 */     boolean matches = false;
/* 332 */     if (index >= 0 && index < string.length() - 1)
/*     */     {
/* 334 */       matches = (string.charAt(index + 1) == c);
/*     */     }
/* 336 */     return matches;
/*     */   }
/*     */   
/*     */   private boolean regionMatch(StringBuffer string, int index, String test) {
/* 340 */     boolean matches = false;
/* 341 */     if (index >= 0 && index + test.length() - 1 < string.length()) {
/*     */       
/* 343 */       String substring = string.substring(index, index + test.length());
/* 344 */       matches = substring.equals(test);
/*     */     } 
/* 346 */     return matches;
/*     */   }
/*     */   
/*     */   private boolean isLastChar(int wdsz, int n) {
/* 350 */     return (n + 1 == wdsz);
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
/*     */   public Object encode(Object pObject) throws EncoderException {
/* 367 */     if (!(pObject instanceof String)) {
/* 368 */       throw new EncoderException("Parameter supplied to Metaphone encode is not of type java.lang.String");
/*     */     }
/* 370 */     return metaphone((String)pObject);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String encode(String pString) {
/* 380 */     return metaphone(pString);
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
/*     */   public boolean isMetaphoneEqual(String str1, String str2) {
/* 392 */     return metaphone(str1).equals(metaphone(str2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxCodeLen() {
/* 399 */     return this.maxCodeLen;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxCodeLen(int maxCodeLen) {
/* 405 */     this.maxCodeLen = maxCodeLen;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\commons\codec\language\Metaphone.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */