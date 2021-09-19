/*     */ package com.sun.dtdparser;
/*     */ 
/*     */ import java.io.CharConversionException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URL;
/*     */ import java.util.Locale;
/*     */ import org.xml.sax.InputSource;
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
/*     */ public class InputEntity
/*     */ {
/*     */   private int start;
/*     */   private int finish;
/*     */   private char[] buf;
/*  41 */   private int lineNumber = 1;
/*     */ 
/*     */   
/*     */   private boolean returnedFirstHalf = false;
/*     */ 
/*     */   
/*     */   private boolean maybeInCRLF = false;
/*     */ 
/*     */   
/*     */   private String name;
/*     */   
/*     */   private InputEntity next;
/*     */   
/*     */   private InputSource input;
/*     */   
/*     */   private Reader reader;
/*     */   
/*     */   private boolean isClosed;
/*     */   
/*     */   private DTDEventListener errHandler;
/*     */   
/*     */   private Locale locale;
/*     */   
/*     */   private StringBuffer rememberedText;
/*     */   
/*     */   private int startRemember;
/*     */   
/*     */   private boolean isPE;
/*     */   
/*     */   private static final int BUFSIZ = 8193;
/*     */   
/*  72 */   private static final char[] newline = new char[] { '\n' };
/*     */   
/*     */   public static InputEntity getInputEntity(DTDEventListener h, Locale l) {
/*  75 */     InputEntity retval = new InputEntity();
/*  76 */     retval.errHandler = h;
/*  77 */     retval.locale = l;
/*  78 */     return retval;
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
/*     */   public boolean isInternal() {
/*  92 */     return (this.reader == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDocument() {
/*  99 */     return (this.next == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isParameterEntity() {
/* 107 */     return this.isPE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 114 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void init(InputSource in, String name, InputEntity stack, boolean isPE) throws IOException, SAXException {
/* 124 */     this.input = in;
/* 125 */     this.isPE = isPE;
/* 126 */     this.reader = in.getCharacterStream();
/*     */     
/* 128 */     if (this.reader == null) {
/* 129 */       InputStream bytes = in.getByteStream();
/*     */       
/* 131 */       if (bytes == null) {
/* 132 */         this.reader = XmlReader.createReader((new URL(in.getSystemId())).openStream());
/*     */       }
/* 134 */       else if (in.getEncoding() != null) {
/* 135 */         this.reader = XmlReader.createReader(in.getByteStream(), in.getEncoding());
/*     */       } else {
/*     */         
/* 138 */         this.reader = XmlReader.createReader(in.getByteStream());
/*     */       } 
/* 140 */     }  this.next = stack;
/* 141 */     this.buf = new char[8193];
/* 142 */     this.name = name;
/* 143 */     checkRecursion(stack);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void init(char[] b, String name, InputEntity stack, boolean isPE) throws SAXException {
/* 152 */     this.next = stack;
/* 153 */     this.buf = b;
/* 154 */     this.finish = b.length;
/* 155 */     this.name = name;
/* 156 */     this.isPE = isPE;
/* 157 */     checkRecursion(stack);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkRecursion(InputEntity stack) throws SAXException {
/* 163 */     if (stack == null)
/*     */       return; 
/* 165 */     for (stack = stack.next; stack != null; stack = stack.next) {
/* 166 */       if (stack.name != null && stack.name.equals(this.name)) {
/* 167 */         fatal("P-069", new Object[] { this.name });
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public InputEntity pop() throws IOException {
/* 174 */     close();
/* 175 */     return this.next;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEOF() throws IOException, SAXException {
/* 185 */     if (this.start >= this.finish) {
/* 186 */       fillbuf();
/* 187 */       return (this.start >= this.finish);
/*     */     } 
/* 189 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEncoding() {
/* 198 */     if (this.reader == null)
/* 199 */       return null; 
/* 200 */     if (this.reader instanceof XmlReader) {
/* 201 */       return ((XmlReader)this.reader).getEncoding();
/*     */     }
/*     */ 
/*     */     
/* 205 */     if (this.reader instanceof InputStreamReader)
/* 206 */       return ((InputStreamReader)this.reader).getEncoding(); 
/* 207 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char getNameChar() throws IOException, SAXException {
/* 218 */     if (this.finish <= this.start)
/* 219 */       fillbuf(); 
/* 220 */     if (this.finish > this.start) {
/* 221 */       char c = this.buf[this.start++];
/* 222 */       if (XmlChars.isNameChar(c))
/* 223 */         return c; 
/* 224 */       this.start--;
/*     */     } 
/* 226 */     return Character.MIN_VALUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char getc() throws IOException, SAXException {
/* 236 */     if (this.finish <= this.start)
/* 237 */       fillbuf(); 
/* 238 */     if (this.finish > this.start) {
/* 239 */       char c = this.buf[this.start++];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 245 */       if (this.returnedFirstHalf) {
/* 246 */         if (c >= '?' && c <= '?') {
/* 247 */           this.returnedFirstHalf = false;
/* 248 */           return c;
/*     */         } 
/* 250 */         fatal("P-070", new Object[] { Integer.toHexString(c) });
/*     */       } 
/* 252 */       if ((c >= ' ' && c <= '퟿') || c == '\t' || (c >= '' && c <= '�'))
/*     */       {
/*     */ 
/*     */         
/* 256 */         return c;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 262 */       if (c == '\r' && !isInternal()) {
/* 263 */         this.maybeInCRLF = true;
/* 264 */         c = getc();
/* 265 */         if (c != '\n')
/* 266 */           ungetc(); 
/* 267 */         this.maybeInCRLF = false;
/*     */         
/* 269 */         this.lineNumber++;
/* 270 */         return '\n';
/*     */       } 
/* 272 */       if (c == '\n' || c == '\r') {
/* 273 */         if (!isInternal() && !this.maybeInCRLF)
/* 274 */           this.lineNumber++; 
/* 275 */         return c;
/*     */       } 
/*     */ 
/*     */       
/* 279 */       if (c >= '?' && c < '?') {
/* 280 */         this.returnedFirstHalf = true;
/* 281 */         return c;
/*     */       } 
/*     */       
/* 284 */       fatal("P-071", new Object[] { Integer.toHexString(c) });
/*     */     } 
/* 286 */     throw new EndOfInputException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean peekc(char c) throws IOException, SAXException {
/* 295 */     if (this.finish <= this.start)
/* 296 */       fillbuf(); 
/* 297 */     if (this.finish > this.start) {
/* 298 */       if (this.buf[this.start] == c) {
/* 299 */         this.start++;
/* 300 */         return true;
/*     */       } 
/* 302 */       return false;
/*     */     } 
/* 304 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void ungetc() {
/* 313 */     if (this.start == 0)
/* 314 */       throw new InternalError("ungetc"); 
/* 315 */     this.start--;
/*     */     
/* 317 */     if (this.buf[this.start] == '\n' || this.buf[this.start] == '\r') {
/* 318 */       if (!isInternal())
/* 319 */         this.lineNumber--; 
/* 320 */     } else if (this.returnedFirstHalf) {
/* 321 */       this.returnedFirstHalf = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean maybeWhitespace() throws IOException, SAXException {
/* 332 */     boolean isSpace = false;
/* 333 */     boolean sawCR = false;
/*     */ 
/*     */     
/*     */     while (true) {
/* 337 */       if (this.finish <= this.start)
/* 338 */         fillbuf(); 
/* 339 */       if (this.finish <= this.start) {
/* 340 */         return isSpace;
/*     */       }
/* 342 */       char c = this.buf[this.start++];
/* 343 */       if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
/* 344 */         isSpace = true;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 349 */         if ((c == '\n' || c == '\r') && !isInternal()) {
/* 350 */           if (c != '\n' || !sawCR) {
/* 351 */             this.lineNumber++;
/* 352 */             sawCR = false;
/*     */           } 
/* 354 */           if (c == '\r')
/* 355 */             sawCR = true; 
/*     */         }  continue;
/*     */       }  break;
/* 358 */     }  this.start--;
/* 359 */     return isSpace;
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
/*     */   public boolean parsedContent(DTDEventListener docHandler) throws IOException, SAXException {
/*     */     int first;
/*     */     int last;
/*     */     boolean sawContent;
/* 388 */     for (first = last = this.start, sawContent = false;; last++) {
/*     */ 
/*     */       
/* 391 */       if (last >= this.finish) {
/* 392 */         if (last > first) {
/*     */           
/* 394 */           docHandler.characters(this.buf, first, last - first);
/* 395 */           sawContent = true;
/* 396 */           this.start = last;
/*     */         } 
/* 398 */         if (isEOF())
/* 399 */           return sawContent; 
/* 400 */         first = this.start;
/* 401 */         last = first - 1;
/*     */       }
/*     */       else {
/*     */         
/* 405 */         char c = this.buf[last];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 416 */         if ((c <= ']' || c > '퟿') && (c >= '&' || c < ' ') && (c <= '<' || c >= ']') && (c <= '&' || c >= '<') && c != '\t' && (c < '' || c > '�'))
/*     */         
/*     */         { 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 426 */           if (c == '<' || c == '&') {
/*     */             break;
/*     */           }
/*     */           
/* 430 */           if (c == '\n')
/* 431 */           { if (!isInternal()) {
/* 432 */               this.lineNumber++;
/*     */ 
/*     */             
/*     */             }
/*     */             
/*     */              }
/*     */           
/* 439 */           else if (c == '\r')
/* 440 */           { if (!isInternal())
/*     */             {
/*     */               
/* 443 */               docHandler.characters(this.buf, first, last - first);
/* 444 */               docHandler.characters(newline, 0, 1);
/* 445 */               sawContent = true;
/* 446 */               this.lineNumber++;
/* 447 */               if (this.finish > last + 1 && 
/* 448 */                 this.buf[last + 1] == '\n') {
/* 449 */                 last++;
/*     */               }
/*     */ 
/*     */               
/* 453 */               first = this.start = last + 1;
/*     */             
/*     */             }
/*     */              }
/*     */           
/* 458 */           else if (c == ']')
/* 459 */           { switch (this.finish - last) {
/*     */ 
/*     */               
/*     */               case 2:
/* 463 */                 if (this.buf[last + 1] != ']') {
/*     */                   break;
/*     */                 }
/*     */               
/*     */               case 1:
/* 468 */                 if (this.reader == null || this.isClosed)
/*     */                   break; 
/* 470 */                 if (last == first)
/* 471 */                   throw new InternalError("fillbuf"); 
/* 472 */                 last--;
/* 473 */                 if (last > first) {
/*     */                   
/* 475 */                   docHandler.characters(this.buf, first, last - first);
/* 476 */                   sawContent = true;
/* 477 */                   this.start = last;
/*     */                 } 
/* 479 */                 fillbuf();
/* 480 */                 first = last = this.start;
/*     */                 break;
/*     */ 
/*     */ 
/*     */               
/*     */               default:
/* 486 */                 if (this.buf[last + 1] == ']' && this.buf[last + 2] == '>') {
/* 487 */                   fatal("P-072", null);
/*     */                 }
/*     */                 break;
/*     */             } 
/*     */             
/*     */              }
/* 493 */           else if (c >= '?' && c <= '?')
/* 494 */           { if (last + 1 >= this.finish) {
/* 495 */               if (last > first) {
/*     */                 
/* 497 */                 docHandler.characters(this.buf, first, last - first);
/* 498 */                 sawContent = true;
/* 499 */                 this.start = last + 1;
/*     */               } 
/* 501 */               if (isEOF()) {
/* 502 */                 fatal("P-081", new Object[] { Integer.toHexString(c) });
/*     */               }
/*     */               
/* 505 */               first = this.start;
/* 506 */               last = first;
/*     */             
/*     */             }
/* 509 */             else if (checkSurrogatePair(last)) {
/* 510 */               last++;
/*     */             } else {
/* 512 */               last--;
/*     */ 
/*     */ 
/*     */               
/*     */               break;
/*     */             }  }
/*     */           else
/* 519 */           { fatal("P-071", new Object[] { Integer.toHexString(c) }); }  } 
/*     */       } 
/* 521 */     }  if (last == first) {
/* 522 */       return sawContent;
/*     */     }
/* 524 */     docHandler.characters(this.buf, first, last - first);
/* 525 */     this.start = last;
/* 526 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean unparsedContent(DTDEventListener docHandler, boolean ignorableWhitespace, String whitespaceInvalidMessage) throws IOException, SAXException {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: ldc '![CDATA['
/*     */     //   3: aconst_null
/*     */     //   4: invokevirtual peek : (Ljava/lang/String;[C)Z
/*     */     //   7: ifne -> 12
/*     */     //   10: iconst_0
/*     */     //   11: ireturn
/*     */     //   12: aload_1
/*     */     //   13: invokeinterface startCDATA : ()V
/*     */     //   18: iconst_0
/*     */     //   19: istore #5
/*     */     //   21: iload_2
/*     */     //   22: istore #7
/*     */     //   24: aload_0
/*     */     //   25: getfield start : I
/*     */     //   28: istore #4
/*     */     //   30: iload #4
/*     */     //   32: aload_0
/*     */     //   33: getfield finish : I
/*     */     //   36: if_icmpge -> 395
/*     */     //   39: aload_0
/*     */     //   40: getfield buf : [C
/*     */     //   43: iload #4
/*     */     //   45: caload
/*     */     //   46: istore #6
/*     */     //   48: iload #6
/*     */     //   50: invokestatic isChar : (I)Z
/*     */     //   53: ifne -> 117
/*     */     //   56: iconst_0
/*     */     //   57: istore #7
/*     */     //   59: iload #6
/*     */     //   61: ldc 55296
/*     */     //   63: if_icmplt -> 94
/*     */     //   66: iload #6
/*     */     //   68: ldc 57343
/*     */     //   70: if_icmpgt -> 94
/*     */     //   73: aload_0
/*     */     //   74: iload #4
/*     */     //   76: invokespecial checkSurrogatePair : (I)Z
/*     */     //   79: ifeq -> 88
/*     */     //   82: iinc #4, 1
/*     */     //   85: goto -> 389
/*     */     //   88: iinc #4, -1
/*     */     //   91: goto -> 395
/*     */     //   94: aload_0
/*     */     //   95: ldc 'P-071'
/*     */     //   97: iconst_1
/*     */     //   98: anewarray java/lang/Object
/*     */     //   101: dup
/*     */     //   102: iconst_0
/*     */     //   103: aload_0
/*     */     //   104: getfield buf : [C
/*     */     //   107: iload #4
/*     */     //   109: caload
/*     */     //   110: invokestatic toHexString : (I)Ljava/lang/String;
/*     */     //   113: aastore
/*     */     //   114: invokespecial fatal : (Ljava/lang/String;[Ljava/lang/Object;)V
/*     */     //   117: iload #6
/*     */     //   119: bipush #10
/*     */     //   121: if_icmpne -> 144
/*     */     //   124: aload_0
/*     */     //   125: invokevirtual isInternal : ()Z
/*     */     //   128: ifne -> 389
/*     */     //   131: aload_0
/*     */     //   132: dup
/*     */     //   133: getfield lineNumber : I
/*     */     //   136: iconst_1
/*     */     //   137: iadd
/*     */     //   138: putfield lineNumber : I
/*     */     //   141: goto -> 389
/*     */     //   144: iload #6
/*     */     //   146: bipush #13
/*     */     //   148: if_icmpne -> 314
/*     */     //   151: aload_0
/*     */     //   152: invokevirtual isInternal : ()Z
/*     */     //   155: ifeq -> 161
/*     */     //   158: goto -> 389
/*     */     //   161: iload #7
/*     */     //   163: ifeq -> 233
/*     */     //   166: aload_3
/*     */     //   167: ifnull -> 198
/*     */     //   170: aload_0
/*     */     //   171: getfield errHandler : Lcom/sun/dtdparser/DTDEventListener;
/*     */     //   174: new org/xml/sax/SAXParseException
/*     */     //   177: dup
/*     */     //   178: getstatic com/sun/dtdparser/DTDParser.messages : Lcom/sun/dtdparser/DTDParser$Catalog;
/*     */     //   181: aload_0
/*     */     //   182: getfield locale : Ljava/util/Locale;
/*     */     //   185: aload_3
/*     */     //   186: invokevirtual getMessage : (Ljava/util/Locale;Ljava/lang/String;)Ljava/lang/String;
/*     */     //   189: aconst_null
/*     */     //   190: invokespecial <init> : (Ljava/lang/String;Lorg/xml/sax/Locator;)V
/*     */     //   193: invokeinterface error : (Lorg/xml/sax/SAXParseException;)V
/*     */     //   198: aload_1
/*     */     //   199: aload_0
/*     */     //   200: getfield buf : [C
/*     */     //   203: aload_0
/*     */     //   204: getfield start : I
/*     */     //   207: iload #4
/*     */     //   209: aload_0
/*     */     //   210: getfield start : I
/*     */     //   213: isub
/*     */     //   214: invokeinterface ignorableWhitespace : ([CII)V
/*     */     //   219: aload_1
/*     */     //   220: getstatic com/sun/dtdparser/InputEntity.newline : [C
/*     */     //   223: iconst_0
/*     */     //   224: iconst_1
/*     */     //   225: invokeinterface ignorableWhitespace : ([CII)V
/*     */     //   230: goto -> 265
/*     */     //   233: aload_1
/*     */     //   234: aload_0
/*     */     //   235: getfield buf : [C
/*     */     //   238: aload_0
/*     */     //   239: getfield start : I
/*     */     //   242: iload #4
/*     */     //   244: aload_0
/*     */     //   245: getfield start : I
/*     */     //   248: isub
/*     */     //   249: invokeinterface characters : ([CII)V
/*     */     //   254: aload_1
/*     */     //   255: getstatic com/sun/dtdparser/InputEntity.newline : [C
/*     */     //   258: iconst_0
/*     */     //   259: iconst_1
/*     */     //   260: invokeinterface characters : ([CII)V
/*     */     //   265: aload_0
/*     */     //   266: dup
/*     */     //   267: getfield lineNumber : I
/*     */     //   270: iconst_1
/*     */     //   271: iadd
/*     */     //   272: putfield lineNumber : I
/*     */     //   275: aload_0
/*     */     //   276: getfield finish : I
/*     */     //   279: iload #4
/*     */     //   281: iconst_1
/*     */     //   282: iadd
/*     */     //   283: if_icmple -> 303
/*     */     //   286: aload_0
/*     */     //   287: getfield buf : [C
/*     */     //   290: iload #4
/*     */     //   292: iconst_1
/*     */     //   293: iadd
/*     */     //   294: caload
/*     */     //   295: bipush #10
/*     */     //   297: if_icmpne -> 303
/*     */     //   300: iinc #4, 1
/*     */     //   303: aload_0
/*     */     //   304: iload #4
/*     */     //   306: iconst_1
/*     */     //   307: iadd
/*     */     //   308: putfield start : I
/*     */     //   311: goto -> 389
/*     */     //   314: iload #6
/*     */     //   316: bipush #93
/*     */     //   318: if_icmpeq -> 341
/*     */     //   321: iload #6
/*     */     //   323: bipush #32
/*     */     //   325: if_icmpeq -> 389
/*     */     //   328: iload #6
/*     */     //   330: bipush #9
/*     */     //   332: if_icmpeq -> 389
/*     */     //   335: iconst_0
/*     */     //   336: istore #7
/*     */     //   338: goto -> 389
/*     */     //   341: iload #4
/*     */     //   343: iconst_2
/*     */     //   344: iadd
/*     */     //   345: aload_0
/*     */     //   346: getfield finish : I
/*     */     //   349: if_icmpge -> 395
/*     */     //   352: aload_0
/*     */     //   353: getfield buf : [C
/*     */     //   356: iload #4
/*     */     //   358: iconst_1
/*     */     //   359: iadd
/*     */     //   360: caload
/*     */     //   361: bipush #93
/*     */     //   363: if_icmpne -> 386
/*     */     //   366: aload_0
/*     */     //   367: getfield buf : [C
/*     */     //   370: iload #4
/*     */     //   372: iconst_2
/*     */     //   373: iadd
/*     */     //   374: caload
/*     */     //   375: bipush #62
/*     */     //   377: if_icmpne -> 386
/*     */     //   380: iconst_1
/*     */     //   381: istore #5
/*     */     //   383: goto -> 395
/*     */     //   386: iconst_0
/*     */     //   387: istore #7
/*     */     //   389: iinc #4, 1
/*     */     //   392: goto -> 30
/*     */     //   395: iload #7
/*     */     //   397: ifeq -> 456
/*     */     //   400: aload_3
/*     */     //   401: ifnull -> 432
/*     */     //   404: aload_0
/*     */     //   405: getfield errHandler : Lcom/sun/dtdparser/DTDEventListener;
/*     */     //   408: new org/xml/sax/SAXParseException
/*     */     //   411: dup
/*     */     //   412: getstatic com/sun/dtdparser/DTDParser.messages : Lcom/sun/dtdparser/DTDParser$Catalog;
/*     */     //   415: aload_0
/*     */     //   416: getfield locale : Ljava/util/Locale;
/*     */     //   419: aload_3
/*     */     //   420: invokevirtual getMessage : (Ljava/util/Locale;Ljava/lang/String;)Ljava/lang/String;
/*     */     //   423: aconst_null
/*     */     //   424: invokespecial <init> : (Ljava/lang/String;Lorg/xml/sax/Locator;)V
/*     */     //   427: invokeinterface error : (Lorg/xml/sax/SAXParseException;)V
/*     */     //   432: aload_1
/*     */     //   433: aload_0
/*     */     //   434: getfield buf : [C
/*     */     //   437: aload_0
/*     */     //   438: getfield start : I
/*     */     //   441: iload #4
/*     */     //   443: aload_0
/*     */     //   444: getfield start : I
/*     */     //   447: isub
/*     */     //   448: invokeinterface ignorableWhitespace : ([CII)V
/*     */     //   453: goto -> 477
/*     */     //   456: aload_1
/*     */     //   457: aload_0
/*     */     //   458: getfield buf : [C
/*     */     //   461: aload_0
/*     */     //   462: getfield start : I
/*     */     //   465: iload #4
/*     */     //   467: aload_0
/*     */     //   468: getfield start : I
/*     */     //   471: isub
/*     */     //   472: invokeinterface characters : ([CII)V
/*     */     //   477: iload #5
/*     */     //   479: ifeq -> 493
/*     */     //   482: aload_0
/*     */     //   483: iload #4
/*     */     //   485: iconst_3
/*     */     //   486: iadd
/*     */     //   487: putfield start : I
/*     */     //   490: goto -> 516
/*     */     //   493: aload_0
/*     */     //   494: iload #4
/*     */     //   496: putfield start : I
/*     */     //   499: aload_0
/*     */     //   500: invokevirtual isEOF : ()Z
/*     */     //   503: ifeq -> 513
/*     */     //   506: aload_0
/*     */     //   507: ldc 'P-073'
/*     */     //   509: aconst_null
/*     */     //   510: invokespecial fatal : (Ljava/lang/String;[Ljava/lang/Object;)V
/*     */     //   513: goto -> 18
/*     */     //   516: aload_1
/*     */     //   517: invokeinterface endCDATA : ()V
/*     */     //   522: iconst_1
/*     */     //   523: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #558	-> 0
/*     */     //   #559	-> 10
/*     */     //   #560	-> 12
/*     */     //   #566	-> 18
/*     */     //   #571	-> 21
/*     */     //   #573	-> 24
/*     */     //   #574	-> 39
/*     */     //   #579	-> 48
/*     */     //   #580	-> 56
/*     */     //   #581	-> 59
/*     */     //   #582	-> 73
/*     */     //   #583	-> 82
/*     */     //   #584	-> 85
/*     */     //   #586	-> 88
/*     */     //   #587	-> 91
/*     */     //   #590	-> 94
/*     */     //   #593	-> 117
/*     */     //   #594	-> 124
/*     */     //   #595	-> 131
/*     */     //   #598	-> 144
/*     */     //   #600	-> 151
/*     */     //   #601	-> 158
/*     */     //   #603	-> 161
/*     */     //   #604	-> 166
/*     */     //   #605	-> 170
/*     */     //   #607	-> 198
/*     */     //   #609	-> 219
/*     */     //   #610	-> 230
/*     */     //   #612	-> 233
/*     */     //   #613	-> 254
/*     */     //   #615	-> 265
/*     */     //   #616	-> 275
/*     */     //   #617	-> 286
/*     */     //   #618	-> 300
/*     */     //   #622	-> 303
/*     */     //   #623	-> 311
/*     */     //   #625	-> 314
/*     */     //   #626	-> 321
/*     */     //   #627	-> 335
/*     */     //   #630	-> 341
/*     */     //   #631	-> 352
/*     */     //   #632	-> 380
/*     */     //   #633	-> 383
/*     */     //   #635	-> 386
/*     */     //   #573	-> 389
/*     */     //   #642	-> 395
/*     */     //   #643	-> 400
/*     */     //   #644	-> 404
/*     */     //   #646	-> 432
/*     */     //   #647	-> 453
/*     */     //   #649	-> 456
/*     */     //   #651	-> 477
/*     */     //   #652	-> 482
/*     */     //   #653	-> 490
/*     */     //   #655	-> 493
/*     */     //   #656	-> 499
/*     */     //   #657	-> 506
/*     */     //   #658	-> 513
/*     */     //   #659	-> 516
/*     */     //   #660	-> 522
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   48	347	6	c	C
/*     */     //   21	492	5	done	Z
/*     */     //   24	489	7	white	Z
/*     */     //   0	524	0	this	Lcom/sun/dtdparser/InputEntity;
/*     */     //   0	524	1	docHandler	Lcom/sun/dtdparser/DTDEventListener;
/*     */     //   0	524	2	ignorableWhitespace	Z
/*     */     //   0	524	3	whitespaceInvalidMessage	Ljava/lang/String;
/*     */     //   30	494	4	last	I
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean checkSurrogatePair(int offset) throws SAXException {
/* 667 */     if (offset + 1 >= this.finish) {
/* 668 */       return false;
/*     */     }
/* 670 */     char c1 = this.buf[offset++];
/* 671 */     char c2 = this.buf[offset];
/*     */     
/* 673 */     if (c1 >= '?' && c1 < '?' && c2 >= '?' && c2 <= '?')
/* 674 */       return true; 
/* 675 */     fatal("P-074", new Object[] { Integer.toHexString(c1 & Character.MAX_VALUE), Integer.toHexString(c2 & Character.MAX_VALUE) });
/*     */ 
/*     */ 
/*     */     
/* 679 */     return false;
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
/*     */   public boolean ignorableWhitespace(DTDEventListener handler) throws IOException, SAXException {
/* 693 */     boolean isSpace = false;
/*     */ 
/*     */ 
/*     */     
/* 697 */     int first = this.start; while (true) {
/* 698 */       if (this.finish <= this.start) {
/* 699 */         if (isSpace)
/* 700 */           handler.ignorableWhitespace(this.buf, first, this.start - first); 
/* 701 */         fillbuf();
/* 702 */         first = this.start;
/*     */       } 
/* 704 */       if (this.finish <= this.start) {
/* 705 */         return isSpace;
/*     */       }
/* 707 */       char c = this.buf[this.start++];
/* 708 */       switch (c) {
/*     */         case '\n':
/* 710 */           if (!isInternal()) {
/* 711 */             this.lineNumber++;
/*     */           }
/*     */         
/*     */         case '\t':
/*     */         case ' ':
/* 716 */           isSpace = true;
/*     */           continue;
/*     */         
/*     */         case '\r':
/* 720 */           isSpace = true;
/* 721 */           if (!isInternal())
/* 722 */             this.lineNumber++; 
/* 723 */           handler.ignorableWhitespace(this.buf, first, this.start - 1 - first);
/*     */           
/* 725 */           handler.ignorableWhitespace(newline, 0, 1);
/* 726 */           if (this.start < this.finish && this.buf[this.start] == '\n')
/* 727 */             this.start++; 
/* 728 */           first = this.start; continue;
/*     */       } 
/*     */       break;
/*     */     } 
/* 732 */     ungetc();
/* 733 */     if (isSpace)
/* 734 */       handler.ignorableWhitespace(this.buf, first, this.start - first); 
/* 735 */     return isSpace;
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
/*     */   public boolean peek(String next, char[] chars) throws IOException, SAXException {
/*     */     int len, i;
/* 753 */     if (chars != null) {
/* 754 */       len = chars.length;
/*     */     } else {
/* 756 */       len = next.length();
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 761 */     if (this.finish <= this.start || this.finish - this.start < len) {
/* 762 */       fillbuf();
/*     */     }
/*     */     
/* 765 */     if (this.finish <= this.start) {
/* 766 */       return false;
/*     */     }
/*     */     
/* 769 */     if (chars != null) {
/* 770 */       for (i = 0; i < len && this.start + i < this.finish; i++) {
/* 771 */         if (this.buf[this.start + i] != chars[i])
/* 772 */           return false; 
/*     */       } 
/*     */     } else {
/* 775 */       for (i = 0; i < len && this.start + i < this.finish; i++) {
/* 776 */         if (this.buf[this.start + i] != next.charAt(i)) {
/* 777 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 783 */     if (i < len) {
/* 784 */       if (this.reader == null || this.isClosed) {
/* 785 */         return false;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 794 */       if (len > this.buf.length) {
/* 795 */         fatal("P-077", new Object[] { new Integer(this.buf.length) });
/*     */       }
/* 797 */       fillbuf();
/* 798 */       return peek(next, chars);
/*     */     } 
/*     */     
/* 801 */     this.start += len;
/* 802 */     return true;
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
/*     */   public void startRemembering() {
/* 814 */     if (this.startRemember != 0)
/* 815 */       throw new InternalError(); 
/* 816 */     this.startRemember = this.start;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String rememberText() {
/*     */     String retval;
/* 825 */     if (this.rememberedText != null) {
/* 826 */       this.rememberedText.append(this.buf, this.startRemember, this.start - this.startRemember);
/*     */       
/* 828 */       retval = this.rememberedText.toString();
/*     */     } else {
/* 830 */       retval = new String(this.buf, this.startRemember, this.start - this.startRemember);
/*     */     } 
/*     */     
/* 833 */     this.startRemember = 0;
/* 834 */     this.rememberedText = null;
/* 835 */     return retval;
/*     */   }
/*     */ 
/*     */   
/*     */   private InputEntity getTopEntity() {
/* 840 */     InputEntity current = this;
/*     */ 
/*     */ 
/*     */     
/* 844 */     while (current != null && current.input == null)
/* 845 */       current = current.next; 
/* 846 */     return (current == null) ? this : current;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPublicId() {
/* 854 */     InputEntity where = getTopEntity();
/* 855 */     if (where == this)
/* 856 */       return this.input.getPublicId(); 
/* 857 */     return where.getPublicId();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSystemId() {
/* 865 */     InputEntity where = getTopEntity();
/* 866 */     if (where == this)
/* 867 */       return this.input.getSystemId(); 
/* 868 */     return where.getSystemId();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLineNumber() {
/* 876 */     InputEntity where = getTopEntity();
/* 877 */     if (where == this)
/* 878 */       return this.lineNumber; 
/* 879 */     return where.getLineNumber();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColumnNumber() {
/* 887 */     return -1;
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
/*     */   private void fillbuf() throws IOException, SAXException {
/* 907 */     if (this.reader == null || this.isClosed) {
/*     */       return;
/*     */     }
/*     */     
/* 911 */     if (this.startRemember != 0) {
/* 912 */       if (this.rememberedText == null)
/* 913 */         this.rememberedText = new StringBuffer(this.buf.length); 
/* 914 */       this.rememberedText.append(this.buf, this.startRemember, this.start - this.startRemember);
/*     */     } 
/*     */ 
/*     */     
/* 918 */     boolean extra = (this.finish > 0 && this.start > 0);
/*     */ 
/*     */     
/* 921 */     if (extra)
/* 922 */       this.start--; 
/* 923 */     int len = this.finish - this.start;
/*     */     
/* 925 */     System.arraycopy(this.buf, this.start, this.buf, 0, len);
/* 926 */     this.start = 0;
/* 927 */     this.finish = len;
/*     */     
/*     */     try {
/* 930 */       len = this.buf.length - len;
/* 931 */       len = this.reader.read(this.buf, this.finish, len);
/* 932 */     } catch (UnsupportedEncodingException e) {
/* 933 */       fatal("P-075", new Object[] { e.getMessage() });
/* 934 */     } catch (CharConversionException e) {
/* 935 */       fatal("P-076", new Object[] { e.getMessage() });
/*     */     } 
/* 937 */     if (len >= 0) {
/* 938 */       this.finish += len;
/*     */     } else {
/* 940 */       close();
/* 941 */     }  if (extra) {
/* 942 */       this.start++;
/*     */     }
/* 944 */     if (this.startRemember != 0)
/*     */     {
/* 946 */       this.startRemember = 1;
/*     */     }
/*     */   }
/*     */   
/*     */   public void close() {
/*     */     try {
/* 952 */       if (this.reader != null && !this.isClosed)
/* 953 */         this.reader.close(); 
/* 954 */       this.isClosed = true;
/* 955 */     } catch (IOException e) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void fatal(String messageId, Object[] params) throws SAXException {
/* 964 */     SAXParseException x = new SAXParseException(DTDParser.messages.getMessage(this.locale, messageId, params), null);
/*     */ 
/*     */     
/* 967 */     close();
/* 968 */     this.errHandler.fatalError(x);
/* 969 */     throw x;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\dtdparser\InputEntity.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */