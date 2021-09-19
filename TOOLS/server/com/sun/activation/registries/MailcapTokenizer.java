/*     */ package com.sun.activation.registries;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MailcapTokenizer
/*     */ {
/*     */   public static final int UNKNOWN_TOKEN = 0;
/*     */   public static final int START_TOKEN = 1;
/*     */   public static final int STRING_TOKEN = 2;
/*     */   public static final int EOI_TOKEN = 5;
/*     */   public static final int SLASH_TOKEN = 47;
/*     */   public static final int SEMICOLON_TOKEN = 59;
/*     */   public static final int EQUALS_TOKEN = 61;
/*     */   private String data;
/*     */   private int dataIndex;
/*     */   private int dataLength;
/*     */   private int currentToken;
/*     */   private String currentTokenValue;
/*     */   private boolean isAutoquoting;
/*     */   private char autoquoteChar;
/*     */   
/*     */   public MailcapTokenizer(String inputString) {
/*  63 */     this.data = inputString;
/*  64 */     this.dataIndex = 0;
/*  65 */     this.dataLength = inputString.length();
/*     */     
/*  67 */     this.currentToken = 1;
/*  68 */     this.currentTokenValue = "";
/*     */     
/*  70 */     this.isAutoquoting = false;
/*  71 */     this.autoquoteChar = ';';
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
/*     */   public void setIsAutoquoting(boolean value) {
/*  85 */     this.isAutoquoting = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCurrentToken() {
/*  94 */     return this.currentToken;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String nameForToken(int token) {
/* 101 */     String name = "really unknown";
/*     */     
/* 103 */     switch (token) {
/*     */       case 0:
/* 105 */         name = "unknown";
/*     */         break;
/*     */       case 1:
/* 108 */         name = "start";
/*     */         break;
/*     */       case 2:
/* 111 */         name = "string";
/*     */         break;
/*     */       case 5:
/* 114 */         name = "EOI";
/*     */         break;
/*     */       case 47:
/* 117 */         name = "'/'";
/*     */         break;
/*     */       case 59:
/* 120 */         name = "';'";
/*     */         break;
/*     */       case 61:
/* 123 */         name = "'='";
/*     */         break;
/*     */     } 
/*     */     
/* 127 */     return name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCurrentTokenValue() {
/* 136 */     return this.currentTokenValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int nextToken() {
/* 145 */     if (this.dataIndex < this.dataLength) {
/*     */       
/* 147 */       while (this.dataIndex < this.dataLength && isWhiteSpaceChar(this.data.charAt(this.dataIndex)))
/*     */       {
/* 149 */         this.dataIndex++;
/*     */       }
/*     */       
/* 152 */       if (this.dataIndex < this.dataLength) {
/*     */         
/* 154 */         char c = this.data.charAt(this.dataIndex);
/* 155 */         if (this.isAutoquoting) {
/* 156 */           if (c == ';' || c == '=') {
/* 157 */             this.currentToken = c;
/* 158 */             this.currentTokenValue = (new Character(c)).toString();
/* 159 */             this.dataIndex++;
/*     */           } else {
/* 161 */             processAutoquoteToken();
/*     */           }
/*     */         
/* 164 */         } else if (isStringTokenChar(c)) {
/* 165 */           processStringToken();
/* 166 */         } else if (c == '/' || c == ';' || c == '=') {
/* 167 */           this.currentToken = c;
/* 168 */           this.currentTokenValue = (new Character(c)).toString();
/* 169 */           this.dataIndex++;
/*     */         } else {
/* 171 */           this.currentToken = 0;
/* 172 */           this.currentTokenValue = (new Character(c)).toString();
/* 173 */           this.dataIndex++;
/*     */         } 
/*     */       } else {
/*     */         
/* 177 */         this.currentToken = 5;
/* 178 */         this.currentTokenValue = null;
/*     */       } 
/*     */     } else {
/* 181 */       this.currentToken = 5;
/* 182 */       this.currentTokenValue = null;
/*     */     } 
/*     */     
/* 185 */     return this.currentToken;
/*     */   }
/*     */ 
/*     */   
/*     */   private void processStringToken() {
/* 190 */     int initialIndex = this.dataIndex;
/*     */ 
/*     */     
/* 193 */     while (this.dataIndex < this.dataLength && isStringTokenChar(this.data.charAt(this.dataIndex)))
/*     */     {
/* 195 */       this.dataIndex++;
/*     */     }
/*     */     
/* 198 */     this.currentToken = 2;
/* 199 */     this.currentTokenValue = this.data.substring(initialIndex, this.dataIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   private void processAutoquoteToken() {
/* 204 */     int initialIndex = this.dataIndex;
/*     */ 
/*     */ 
/*     */     
/* 208 */     boolean foundTerminator = false;
/* 209 */     while (this.dataIndex < this.dataLength && !foundTerminator) {
/* 210 */       char c = this.data.charAt(this.dataIndex);
/* 211 */       if (c != this.autoquoteChar) {
/* 212 */         this.dataIndex++; continue;
/*     */       } 
/* 214 */       foundTerminator = true;
/*     */     } 
/*     */ 
/*     */     
/* 218 */     this.currentToken = 2;
/* 219 */     this.currentTokenValue = fixEscapeSequences(this.data.substring(initialIndex, this.dataIndex));
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isSpecialChar(char c) {
/* 224 */     boolean lAnswer = false;
/*     */     
/* 226 */     switch (c) {
/*     */       case '"':
/*     */       case '(':
/*     */       case ')':
/*     */       case ',':
/*     */       case '/':
/*     */       case ':':
/*     */       case ';':
/*     */       case '<':
/*     */       case '=':
/*     */       case '>':
/*     */       case '?':
/*     */       case '@':
/*     */       case '[':
/*     */       case '\\':
/*     */       case ']':
/* 242 */         lAnswer = true;
/*     */         break;
/*     */     } 
/*     */     
/* 246 */     return lAnswer;
/*     */   }
/*     */   
/*     */   private static boolean isControlChar(char c) {
/* 250 */     return Character.isISOControl(c);
/*     */   }
/*     */   
/*     */   private static boolean isWhiteSpaceChar(char c) {
/* 254 */     return Character.isWhitespace(c);
/*     */   }
/*     */   
/*     */   private static boolean isStringTokenChar(char c) {
/* 258 */     return (!isSpecialChar(c) && !isControlChar(c) && !isWhiteSpaceChar(c));
/*     */   }
/*     */   
/*     */   private static String fixEscapeSequences(String inputString) {
/* 262 */     int inputLength = inputString.length();
/* 263 */     StringBuffer buffer = new StringBuffer();
/* 264 */     buffer.ensureCapacity(inputLength);
/*     */     
/* 266 */     for (int i = 0; i < inputLength; i++) {
/* 267 */       char currentChar = inputString.charAt(i);
/* 268 */       if (currentChar != '\\') {
/* 269 */         buffer.append(currentChar);
/*     */       }
/* 271 */       else if (i < inputLength - 1) {
/* 272 */         char nextChar = inputString.charAt(i + 1);
/* 273 */         buffer.append(nextChar);
/*     */ 
/*     */         
/* 276 */         i++;
/*     */       } else {
/* 278 */         buffer.append(currentChar);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 283 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\activation\registries\MailcapTokenizer.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */