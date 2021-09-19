/*     */ package com.sun.activation.registries;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MailcapFile
/*     */ {
/*  54 */   private Map type_hash = new HashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   private Map fallback_hash = new HashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  65 */   private Map native_commands = new HashMap();
/*     */   
/*     */   private static boolean addReverse = false;
/*     */   
/*     */   static {
/*     */     try {
/*  71 */       addReverse = Boolean.getBoolean("javax.activation.addreverse");
/*  72 */     } catch (Throwable t) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MailcapFile(String new_fname) throws IOException {
/*  83 */     if (LogSupport.isLoggable())
/*  84 */       LogSupport.log("new MailcapFile: file " + new_fname); 
/*  85 */     FileReader reader = null;
/*     */     try {
/*  87 */       reader = new FileReader(new_fname);
/*  88 */       parse(new BufferedReader(reader));
/*     */     } finally {
/*  90 */       if (reader != null) {
/*     */         try {
/*  92 */           reader.close();
/*  93 */         } catch (IOException ex) {}
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MailcapFile(InputStream is) throws IOException {
/* 104 */     if (LogSupport.isLoggable())
/* 105 */       LogSupport.log("new MailcapFile: InputStream"); 
/* 106 */     parse(new BufferedReader(new InputStreamReader(is, "iso-8859-1")));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MailcapFile() {
/* 113 */     if (LogSupport.isLoggable()) {
/* 114 */       LogSupport.log("new MailcapFile: default");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map getMailcapList(String mime_type) {
/* 126 */     Map search_result = null;
/* 127 */     Map wildcard_result = null;
/*     */ 
/*     */     
/* 130 */     search_result = (Map)this.type_hash.get(mime_type);
/*     */ 
/*     */     
/* 133 */     int separator = mime_type.indexOf('/');
/* 134 */     String subtype = mime_type.substring(separator + 1);
/* 135 */     if (!subtype.equals("*")) {
/* 136 */       String type = mime_type.substring(0, separator + 1) + "*";
/* 137 */       wildcard_result = (Map)this.type_hash.get(type);
/*     */       
/* 139 */       if (wildcard_result != null)
/* 140 */         if (search_result != null) {
/* 141 */           search_result = mergeResults(search_result, wildcard_result);
/*     */         } else {
/*     */           
/* 144 */           search_result = wildcard_result;
/*     */         }  
/*     */     } 
/* 147 */     return search_result;
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
/*     */   public Map getMailcapFallbackList(String mime_type) {
/* 159 */     Map search_result = null;
/* 160 */     Map wildcard_result = null;
/*     */ 
/*     */     
/* 163 */     search_result = (Map)this.fallback_hash.get(mime_type);
/*     */ 
/*     */     
/* 166 */     int separator = mime_type.indexOf('/');
/* 167 */     String subtype = mime_type.substring(separator + 1);
/* 168 */     if (!subtype.equals("*")) {
/* 169 */       String type = mime_type.substring(0, separator + 1) + "*";
/* 170 */       wildcard_result = (Map)this.fallback_hash.get(type);
/*     */       
/* 172 */       if (wildcard_result != null)
/* 173 */         if (search_result != null) {
/* 174 */           search_result = mergeResults(search_result, wildcard_result);
/*     */         } else {
/*     */           
/* 177 */           search_result = wildcard_result;
/*     */         }  
/*     */     } 
/* 180 */     return search_result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getMimeTypes() {
/* 187 */     Set types = new HashSet(this.type_hash.keySet());
/* 188 */     types.addAll(this.fallback_hash.keySet());
/* 189 */     types.addAll(this.native_commands.keySet());
/* 190 */     String[] mts = new String[types.size()];
/* 191 */     mts = (String[])types.toArray((Object[])mts);
/* 192 */     return mts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getNativeCommands(String mime_type) {
/* 199 */     String[] cmds = null;
/* 200 */     List v = (List)this.native_commands.get(mime_type.toLowerCase(Locale.ENGLISH));
/*     */     
/* 202 */     if (v != null) {
/* 203 */       cmds = new String[v.size()];
/* 204 */       cmds = (String[])v.toArray((Object[])cmds);
/*     */     } 
/* 206 */     return cmds;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map mergeResults(Map first, Map second) {
/* 216 */     Iterator verb_enum = second.keySet().iterator();
/* 217 */     Map clonedHash = new HashMap(first);
/*     */ 
/*     */     
/* 220 */     while (verb_enum.hasNext()) {
/* 221 */       String verb = verb_enum.next();
/* 222 */       List cmdVector = (List)clonedHash.get(verb);
/* 223 */       if (cmdVector == null) {
/* 224 */         clonedHash.put(verb, second.get(verb));
/*     */         continue;
/*     */       } 
/* 227 */       List oldV = (List)second.get(verb);
/* 228 */       cmdVector = new ArrayList(cmdVector);
/* 229 */       cmdVector.addAll(oldV);
/* 230 */       clonedHash.put(verb, cmdVector);
/*     */     } 
/*     */     
/* 233 */     return clonedHash;
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
/*     */   public void appendToMailcap(String mail_cap) {
/* 247 */     if (LogSupport.isLoggable())
/* 248 */       LogSupport.log("appendToMailcap: " + mail_cap); 
/*     */     try {
/* 250 */       parse(new StringReader(mail_cap));
/* 251 */     } catch (IOException ex) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void parse(Reader reader) throws IOException {
/* 260 */     BufferedReader buf_reader = new BufferedReader(reader);
/* 261 */     String line = null;
/* 262 */     String continued = null;
/*     */     
/* 264 */     while ((line = buf_reader.readLine()) != null) {
/*     */ 
/*     */       
/* 267 */       line = line.trim();
/*     */       
/*     */       try {
/* 270 */         if (line.charAt(0) == '#')
/*     */           continue; 
/* 272 */         if (line.charAt(line.length() - 1) == '\\') {
/* 273 */           if (continued != null) {
/* 274 */             continued = continued + line.substring(0, line.length() - 1); continue;
/*     */           } 
/* 276 */           continued = line.substring(0, line.length() - 1); continue;
/* 277 */         }  if (continued != null) {
/*     */           
/* 279 */           continued = continued + line;
/*     */           
/*     */           try {
/* 282 */             parseLine(continued);
/* 283 */           } catch (MailcapParseException e) {}
/*     */ 
/*     */           
/* 286 */           continued = null;
/*     */           
/*     */           continue;
/*     */         } 
/*     */         try {
/* 291 */           parseLine(line);
/*     */         }
/* 293 */         catch (MailcapParseException e) {}
/*     */ 
/*     */       
/*     */       }
/* 297 */       catch (StringIndexOutOfBoundsException e) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void parseLine(String mailcapEntry) throws MailcapParseException, IOException {
/* 309 */     MailcapTokenizer tokenizer = new MailcapTokenizer(mailcapEntry);
/* 310 */     tokenizer.setIsAutoquoting(false);
/*     */     
/* 312 */     if (LogSupport.isLoggable()) {
/* 313 */       LogSupport.log("parse: " + mailcapEntry);
/*     */     }
/* 315 */     int currentToken = tokenizer.nextToken();
/* 316 */     if (currentToken != 2) {
/* 317 */       reportParseError(2, currentToken, tokenizer.getCurrentTokenValue());
/*     */     }
/*     */     
/* 320 */     String primaryType = tokenizer.getCurrentTokenValue().toLowerCase(Locale.ENGLISH);
/*     */     
/* 322 */     String subType = "*";
/*     */ 
/*     */ 
/*     */     
/* 326 */     currentToken = tokenizer.nextToken();
/* 327 */     if (currentToken != 47 && currentToken != 59)
/*     */     {
/* 329 */       reportParseError(47, 59, currentToken, tokenizer.getCurrentTokenValue());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 335 */     if (currentToken == 47) {
/*     */       
/* 337 */       currentToken = tokenizer.nextToken();
/* 338 */       if (currentToken != 2) {
/* 339 */         reportParseError(2, currentToken, tokenizer.getCurrentTokenValue());
/*     */       }
/*     */       
/* 342 */       subType = tokenizer.getCurrentTokenValue().toLowerCase(Locale.ENGLISH);
/*     */ 
/*     */ 
/*     */       
/* 346 */       currentToken = tokenizer.nextToken();
/*     */     } 
/*     */     
/* 349 */     String mimeType = primaryType + "/" + subType;
/*     */     
/* 351 */     if (LogSupport.isLoggable()) {
/* 352 */       LogSupport.log("  Type: " + mimeType);
/*     */     }
/*     */     
/* 355 */     Map commands = new LinkedHashMap();
/*     */ 
/*     */     
/* 358 */     if (currentToken != 59) {
/* 359 */       reportParseError(59, currentToken, tokenizer.getCurrentTokenValue());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 365 */     tokenizer.setIsAutoquoting(true);
/* 366 */     currentToken = tokenizer.nextToken();
/* 367 */     tokenizer.setIsAutoquoting(false);
/* 368 */     if (currentToken != 2 && currentToken != 59)
/*     */     {
/* 370 */       reportParseError(2, 59, currentToken, tokenizer.getCurrentTokenValue());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 375 */     if (currentToken == 2) {
/*     */ 
/*     */       
/* 378 */       List v = (List)this.native_commands.get(mimeType);
/* 379 */       if (v == null) {
/* 380 */         v = new ArrayList();
/* 381 */         v.add(mailcapEntry);
/* 382 */         this.native_commands.put(mimeType, v);
/*     */       } else {
/*     */         
/* 385 */         v.add(mailcapEntry);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 390 */     if (currentToken != 59) {
/* 391 */       currentToken = tokenizer.nextToken();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 396 */     if (currentToken == 59) {
/* 397 */       boolean isFallback = false;
/*     */ 
/*     */ 
/*     */       
/*     */       do {
/* 402 */         currentToken = tokenizer.nextToken();
/* 403 */         if (currentToken != 2) {
/* 404 */           reportParseError(2, currentToken, tokenizer.getCurrentTokenValue());
/*     */         }
/*     */         
/* 407 */         String paramName = tokenizer.getCurrentTokenValue().toLowerCase(Locale.ENGLISH);
/*     */ 
/*     */ 
/*     */         
/* 411 */         currentToken = tokenizer.nextToken();
/* 412 */         if (currentToken != 61 && currentToken != 59 && currentToken != 5)
/*     */         {
/*     */           
/* 415 */           reportParseError(61, 59, 5, currentToken, tokenizer.getCurrentTokenValue());
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 422 */         if (currentToken != 61) {
/*     */           continue;
/*     */         }
/*     */         
/* 426 */         tokenizer.setIsAutoquoting(true);
/* 427 */         currentToken = tokenizer.nextToken();
/* 428 */         tokenizer.setIsAutoquoting(false);
/* 429 */         if (currentToken != 2) {
/* 430 */           reportParseError(2, currentToken, tokenizer.getCurrentTokenValue());
/*     */         }
/*     */         
/* 433 */         String paramValue = tokenizer.getCurrentTokenValue();
/*     */ 
/*     */ 
/*     */         
/* 437 */         if (paramName.startsWith("x-java-")) {
/* 438 */           String commandName = paramName.substring(7);
/*     */ 
/*     */           
/* 441 */           if (commandName.equals("fallback-entry") && paramValue.equalsIgnoreCase("true")) {
/*     */             
/* 443 */             isFallback = true;
/*     */           }
/*     */           else {
/*     */             
/* 447 */             if (LogSupport.isLoggable()) {
/* 448 */               LogSupport.log("    Command: " + commandName + ", Class: " + paramValue);
/*     */             }
/* 450 */             List classes = (List)commands.get(commandName);
/* 451 */             if (classes == null) {
/* 452 */               classes = new ArrayList();
/* 453 */               commands.put(commandName, classes);
/*     */             } 
/* 455 */             if (addReverse) {
/* 456 */               classes.add(0, paramValue);
/*     */             } else {
/* 458 */               classes.add(paramValue);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 463 */         currentToken = tokenizer.nextToken();
/*     */       }
/* 465 */       while (currentToken == 59);
/*     */       
/* 467 */       Map masterHash = isFallback ? this.fallback_hash : this.type_hash;
/* 468 */       Map curcommands = (Map)masterHash.get(mimeType);
/*     */       
/* 470 */       if (curcommands == null) {
/* 471 */         masterHash.put(mimeType, commands);
/*     */       } else {
/* 473 */         if (LogSupport.isLoggable()) {
/* 474 */           LogSupport.log("Merging commands for type " + mimeType);
/*     */         }
/*     */         
/* 477 */         Iterator cn = curcommands.keySet().iterator();
/* 478 */         while (cn.hasNext()) {
/* 479 */           String cmdName = cn.next();
/* 480 */           List ccv = (List)curcommands.get(cmdName);
/* 481 */           List cv = (List)commands.get(cmdName);
/* 482 */           if (cv == null) {
/*     */             continue;
/*     */           }
/* 485 */           Iterator cvn = cv.iterator();
/* 486 */           while (cvn.hasNext()) {
/* 487 */             String clazz = cvn.next();
/* 488 */             if (!ccv.contains(clazz)) {
/* 489 */               if (addReverse) {
/* 490 */                 ccv.add(0, clazz); continue;
/*     */               } 
/* 492 */               ccv.add(clazz);
/*     */             } 
/*     */           } 
/*     */         } 
/* 496 */         cn = commands.keySet().iterator();
/* 497 */         while (cn.hasNext()) {
/* 498 */           String cmdName = cn.next();
/* 499 */           if (curcommands.containsKey(cmdName))
/*     */             continue; 
/* 501 */           List cv = (List)commands.get(cmdName);
/* 502 */           curcommands.put(cmdName, cv);
/*     */         } 
/*     */       } 
/* 505 */     } else if (currentToken != 5) {
/* 506 */       reportParseError(5, 59, currentToken, tokenizer.getCurrentTokenValue());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void reportParseError(int expectedToken, int actualToken, String actualTokenValue) throws MailcapParseException {
/* 514 */     throw new MailcapParseException("Encountered a " + MailcapTokenizer.nameForToken(actualToken) + " token (" + actualTokenValue + ") while expecting a " + MailcapTokenizer.nameForToken(expectedToken) + " token.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void reportParseError(int expectedToken, int otherExpectedToken, int actualToken, String actualTokenValue) throws MailcapParseException {
/* 523 */     throw new MailcapParseException("Encountered a " + MailcapTokenizer.nameForToken(actualToken) + " token (" + actualTokenValue + ") while expecting a " + MailcapTokenizer.nameForToken(expectedToken) + " or a " + MailcapTokenizer.nameForToken(otherExpectedToken) + " token.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void reportParseError(int expectedToken, int otherExpectedToken, int anotherExpectedToken, int actualToken, String actualTokenValue) throws MailcapParseException {
/* 533 */     if (LogSupport.isLoggable()) {
/* 534 */       LogSupport.log("PARSE ERROR: Encountered a " + MailcapTokenizer.nameForToken(actualToken) + " token (" + actualTokenValue + ") while expecting a " + MailcapTokenizer.nameForToken(expectedToken) + ", a " + MailcapTokenizer.nameForToken(otherExpectedToken) + ", or a " + MailcapTokenizer.nameForToken(anotherExpectedToken) + " token.");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 540 */     throw new MailcapParseException("Encountered a " + MailcapTokenizer.nameForToken(actualToken) + " token (" + actualTokenValue + ") while expecting a " + MailcapTokenizer.nameForToken(expectedToken) + ", a " + MailcapTokenizer.nameForToken(otherExpectedToken) + ", or a " + MailcapTokenizer.nameForToken(anotherExpectedToken) + " token.");
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\activation\registries\MailcapFile.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */