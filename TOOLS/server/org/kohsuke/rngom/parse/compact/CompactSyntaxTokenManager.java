/*      */ package org.kohsuke.rngom.parse.compact;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class CompactSyntaxTokenManager
/*      */   implements CompactSyntaxConstants
/*      */ {
/*   41 */   public PrintStream debugStream = System.out; public void setDebugStream(PrintStream ds) {
/*   42 */     this.debugStream = ds;
/*      */   }
/*      */   private final int jjStopStringLiteralDfa_0(int pos, long active0) {
/*   45 */     switch (pos) {
/*      */       
/*      */       case 0:
/*   48 */         if ((active0 & 0x1F8C0FE4E0L) != 0L) {
/*      */           
/*   50 */           this.jjmatchedKind = 54;
/*   51 */           return 43;
/*      */         } 
/*   53 */         if ((active0 & 0x800000000000000L) != 0L) {
/*      */           
/*   55 */           this.jjmatchedKind = 60;
/*   56 */           return -1;
/*      */         } 
/*   58 */         return -1;
/*      */       case 1:
/*   60 */         if ((active0 & 0x1F8C0FE4E0L) != 0L) {
/*      */           
/*   62 */           this.jjmatchedKind = 54;
/*   63 */           this.jjmatchedPos = 1;
/*   64 */           return 43;
/*      */         } 
/*   66 */         if ((active0 & 0x800000000000000L) != 0L) {
/*      */           
/*   68 */           if (this.jjmatchedPos == 0) {
/*      */             
/*   70 */             this.jjmatchedKind = 60;
/*   71 */             this.jjmatchedPos = 0;
/*      */           } 
/*   73 */           return -1;
/*      */         } 
/*   75 */         return -1;
/*      */       case 2:
/*   77 */         if ((active0 & 0x1F8C0FE4A0L) != 0L) {
/*      */           
/*   79 */           this.jjmatchedKind = 54;
/*   80 */           this.jjmatchedPos = 2;
/*   81 */           return 43;
/*      */         } 
/*   83 */         if ((active0 & 0x40L) != 0L)
/*   84 */           return 43; 
/*   85 */         return -1;
/*      */       case 3:
/*   87 */         if ((active0 & 0x1F0C0BE4A0L) != 0L) {
/*      */           
/*   89 */           this.jjmatchedKind = 54;
/*   90 */           this.jjmatchedPos = 3;
/*   91 */           return 43;
/*      */         } 
/*   93 */         if ((active0 & 0x80040000L) != 0L)
/*   94 */           return 43; 
/*   95 */         return -1;
/*      */       case 4:
/*   97 */         if ((active0 & 0xE0C09E480L) != 0L) {
/*      */           
/*   99 */           this.jjmatchedKind = 54;
/*  100 */           this.jjmatchedPos = 4;
/*  101 */           return 43;
/*      */         } 
/*  103 */         if ((active0 & 0x1100020020L) != 0L)
/*  104 */           return 43; 
/*  105 */         return -1;
/*      */       case 5:
/*  107 */         if ((active0 & 0x20C09E480L) != 0L) {
/*      */           
/*  109 */           this.jjmatchedKind = 54;
/*  110 */           this.jjmatchedPos = 5;
/*  111 */           return 43;
/*      */         } 
/*  113 */         if ((active0 & 0xC00000000L) != 0L)
/*  114 */           return 43; 
/*  115 */         return -1;
/*      */       case 6:
/*  117 */         if ((active0 & 0x208092000L) != 0L) {
/*      */           
/*  119 */           this.jjmatchedKind = 54;
/*  120 */           this.jjmatchedPos = 6;
/*  121 */           return 43;
/*      */         } 
/*  123 */         if ((active0 & 0x400C480L) != 0L)
/*  124 */           return 43; 
/*  125 */         return -1;
/*      */       case 7:
/*  127 */         if ((active0 & 0x8092000L) != 0L) {
/*      */           
/*  129 */           this.jjmatchedKind = 54;
/*  130 */           this.jjmatchedPos = 7;
/*  131 */           return 43;
/*      */         } 
/*  133 */         if ((active0 & 0x200000000L) != 0L)
/*  134 */           return 43; 
/*  135 */         return -1;
/*      */       case 8:
/*  137 */         if ((active0 & 0x80000L) != 0L) {
/*      */           
/*  139 */           this.jjmatchedKind = 54;
/*  140 */           this.jjmatchedPos = 8;
/*  141 */           return 43;
/*      */         } 
/*  143 */         if ((active0 & 0x8012000L) != 0L)
/*  144 */           return 43; 
/*  145 */         return -1;
/*      */     } 
/*  147 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   private final int jjStartNfa_0(int pos, long active0) {
/*  152 */     return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
/*      */   }
/*      */   
/*      */   private final int jjStopAtPos(int pos, int kind) {
/*  156 */     this.jjmatchedKind = kind;
/*  157 */     this.jjmatchedPos = pos;
/*  158 */     return pos + 1;
/*      */   }
/*      */   
/*      */   private final int jjStartNfaWithStates_0(int pos, int kind, int state) {
/*  162 */     this.jjmatchedKind = kind;
/*  163 */     this.jjmatchedPos = pos; 
/*  164 */     try { this.curChar = this.input_stream.readChar(); }
/*  165 */     catch (IOException e) { return pos + 1; }
/*  166 */      return jjMoveNfa_0(state, pos + 1);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa0_0() {
/*  170 */     switch (this.curChar) {
/*      */       
/*      */       case '&':
/*  173 */         this.jjmatchedKind = 21;
/*  174 */         return jjMoveStringLiteralDfa1_0(8L);
/*      */       case '(':
/*  176 */         return jjStopAtPos(0, 28);
/*      */       case ')':
/*  178 */         return jjStopAtPos(0, 29);
/*      */       case '*':
/*  180 */         return jjStopAtPos(0, 25);
/*      */       case '+':
/*  182 */         return jjStopAtPos(0, 23);
/*      */       case ',':
/*  184 */         return jjStopAtPos(0, 22);
/*      */       case '-':
/*  186 */         return jjStopAtPos(0, 30);
/*      */       case '=':
/*  188 */         return jjStopAtPos(0, 2);
/*      */       case '>':
/*  190 */         return jjMoveStringLiteralDfa1_0(576460752303423488L);
/*      */       case '?':
/*  192 */         return jjStopAtPos(0, 24);
/*      */       case '[':
/*  194 */         return jjStopAtPos(0, 1);
/*      */       case ']':
/*  196 */         return jjStopAtPos(0, 9);
/*      */       case 'a':
/*  198 */         return jjMoveStringLiteralDfa1_0(134217728L);
/*      */       case 'd':
/*  200 */         return jjMoveStringLiteralDfa1_0(81984L);
/*      */       case 'e':
/*  202 */         return jjMoveStringLiteralDfa1_0(8657174528L);
/*      */       case 'g':
/*  204 */         return jjMoveStringLiteralDfa1_0(1024L);
/*      */       case 'i':
/*  206 */         return jjMoveStringLiteralDfa1_0(32896L);
/*      */       case 'l':
/*  208 */         return jjMoveStringLiteralDfa1_0(2147483648L);
/*      */       case 'm':
/*  210 */         return jjMoveStringLiteralDfa1_0(4294967296L);
/*      */       case 'n':
/*  212 */         return jjMoveStringLiteralDfa1_0(532480L);
/*      */       case 'p':
/*  214 */         return jjMoveStringLiteralDfa1_0(17179869184L);
/*      */       case 's':
/*  216 */         return jjMoveStringLiteralDfa1_0(34359738400L);
/*      */       case 't':
/*  218 */         return jjMoveStringLiteralDfa1_0(68719738880L);
/*      */       case '{':
/*  220 */         return jjStopAtPos(0, 11);
/*      */       case '|':
/*  222 */         this.jjmatchedKind = 20;
/*  223 */         return jjMoveStringLiteralDfa1_0(16L);
/*      */       case '}':
/*  225 */         return jjStopAtPos(0, 12);
/*      */       case '~':
/*  227 */         return jjStopAtPos(0, 8);
/*      */     } 
/*  229 */     return jjMoveNfa_0(3, 0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa1_0(long active0) {
/*      */     try {
/*  234 */       this.curChar = this.input_stream.readChar();
/*  235 */     } catch (IOException e) {
/*  236 */       jjStopStringLiteralDfa_0(0, active0);
/*  237 */       return 1;
/*      */     } 
/*  239 */     switch (this.curChar) {
/*      */       
/*      */       case '=':
/*  242 */         if ((active0 & 0x8L) != 0L)
/*  243 */           return jjStopAtPos(1, 3); 
/*  244 */         if ((active0 & 0x10L) != 0L)
/*  245 */           return jjStopAtPos(1, 4); 
/*      */         break;
/*      */       case '>':
/*  248 */         if ((active0 & 0x800000000000000L) != 0L)
/*  249 */           return jjStopAtPos(1, 59); 
/*      */         break;
/*      */       case 'a':
/*  252 */         return jjMoveStringLiteralDfa2_0(active0, 17179942912L);
/*      */       case 'e':
/*  254 */         return jjMoveStringLiteralDfa2_0(active0, 278528L);
/*      */       case 'i':
/*  256 */         return jjMoveStringLiteralDfa2_0(active0, 6442451008L);
/*      */       case 'l':
/*  258 */         return jjMoveStringLiteralDfa2_0(active0, 67108864L);
/*      */       case 'm':
/*  260 */         return jjMoveStringLiteralDfa2_0(active0, 131072L);
/*      */       case 'n':
/*  262 */         return jjMoveStringLiteralDfa2_0(active0, 32896L);
/*      */       case 'o':
/*  264 */         return jjMoveStringLiteralDfa2_0(active0, 68720001024L);
/*      */       case 'r':
/*  266 */         return jjMoveStringLiteralDfa2_0(active0, 1024L);
/*      */       case 't':
/*  268 */         return jjMoveStringLiteralDfa2_0(active0, 34493956128L);
/*      */       case 'x':
/*  270 */         return jjMoveStringLiteralDfa2_0(active0, 8589934592L);
/*      */     } 
/*      */ 
/*      */     
/*  274 */     return jjStartNfa_0(0, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa2_0(long old0, long active0) {
/*  278 */     if ((active0 &= old0) == 0L)
/*  279 */       return jjStartNfa_0(0, old0);  try {
/*  280 */       this.curChar = this.input_stream.readChar();
/*  281 */     } catch (IOException e) {
/*  282 */       jjStopStringLiteralDfa_0(1, active0);
/*  283 */       return 2;
/*      */     } 
/*  285 */     switch (this.curChar) {
/*      */       
/*      */       case 'a':
/*  288 */         return jjMoveStringLiteralDfa3_0(active0, 1056L);
/*      */       case 'c':
/*  290 */         return jjMoveStringLiteralDfa3_0(active0, 128L);
/*      */       case 'e':
/*  292 */         return jjMoveStringLiteralDfa3_0(active0, 67108864L);
/*      */       case 'f':
/*  294 */         return jjMoveStringLiteralDfa3_0(active0, 16384L);
/*      */       case 'h':
/*  296 */         return jjMoveStringLiteralDfa3_0(active0, 32768L);
/*      */       case 'k':
/*  298 */         return jjMoveStringLiteralDfa3_0(active0, 68719476736L);
/*      */       case 'm':
/*  300 */         return jjMoveStringLiteralDfa3_0(active0, 8192L);
/*      */       case 'p':
/*  302 */         return jjMoveStringLiteralDfa3_0(active0, 131072L);
/*      */       case 'r':
/*  304 */         return jjMoveStringLiteralDfa3_0(active0, 51539607552L);
/*      */       case 's':
/*  306 */         return jjMoveStringLiteralDfa3_0(active0, 2147483648L);
/*      */       case 't':
/*  308 */         return jjMoveStringLiteralDfa3_0(active0, 8724742144L);
/*      */       case 'v':
/*  310 */         if ((active0 & 0x40L) != 0L)
/*  311 */           return jjStartNfaWithStates_0(2, 6, 43); 
/*      */         break;
/*      */       case 'x':
/*  314 */         return jjMoveStringLiteralDfa3_0(active0, 4295229440L);
/*      */     } 
/*      */ 
/*      */     
/*  318 */     return jjStartNfa_0(1, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa3_0(long old0, long active0) {
/*  322 */     if ((active0 &= old0) == 0L)
/*  323 */       return jjStartNfa_0(1, old0);  try {
/*  324 */       this.curChar = this.input_stream.readChar();
/*  325 */     } catch (IOException e) {
/*  326 */       jjStopStringLiteralDfa_0(2, active0);
/*  327 */       return 3;
/*      */     } 
/*  329 */     switch (this.curChar) {
/*      */       
/*      */       case 'A':
/*  332 */         return jjMoveStringLiteralDfa4_0(active0, 524288L);
/*      */       case 'a':
/*  334 */         return jjMoveStringLiteralDfa4_0(active0, 81920L);
/*      */       case 'e':
/*  336 */         return jjMoveStringLiteralDfa4_0(active0, 98784288768L);
/*      */       case 'i':
/*  338 */         return jjMoveStringLiteralDfa4_0(active0, 34359738368L);
/*      */       case 'l':
/*  340 */         return jjMoveStringLiteralDfa4_0(active0, 128L);
/*      */       case 'm':
/*  342 */         return jjMoveStringLiteralDfa4_0(active0, 67109888L);
/*      */       case 'r':
/*  344 */         return jjMoveStringLiteralDfa4_0(active0, 134217760L);
/*      */       case 't':
/*  346 */         if ((active0 & 0x40000L) != 0L)
/*  347 */           return jjStartNfaWithStates_0(3, 18, 43); 
/*  348 */         if ((active0 & 0x80000000L) != 0L)
/*  349 */           return jjStartNfaWithStates_0(3, 31, 43); 
/*  350 */         return jjMoveStringLiteralDfa4_0(active0, 131072L);
/*      */     } 
/*      */ 
/*      */     
/*  354 */     return jjStartNfa_0(2, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa4_0(long old0, long active0) {
/*  358 */     if ((active0 &= old0) == 0L)
/*  359 */       return jjStartNfa_0(2, old0);  try {
/*  360 */       this.curChar = this.input_stream.readChar();
/*  361 */     } catch (IOException e) {
/*  362 */       jjStopStringLiteralDfa_0(3, active0);
/*  363 */       return 4;
/*      */     } 
/*  365 */     switch (this.curChar) {
/*      */       
/*      */       case 'd':
/*  368 */         if ((active0 & 0x100000000L) != 0L)
/*  369 */           return jjStartNfaWithStates_0(4, 32, 43); 
/*      */         break;
/*      */       case 'e':
/*  372 */         return jjMoveStringLiteralDfa5_0(active0, 67108864L);
/*      */       case 'i':
/*  374 */         return jjMoveStringLiteralDfa5_0(active0, 134217728L);
/*      */       case 'l':
/*  376 */         return jjMoveStringLiteralDfa5_0(active0, 524288L);
/*      */       case 'm':
/*  378 */         return jjMoveStringLiteralDfa5_0(active0, 1024L);
/*      */       case 'n':
/*  380 */         if ((active0 & 0x1000000000L) != 0L)
/*  381 */           return jjStartNfaWithStates_0(4, 36, 43); 
/*  382 */         return jjMoveStringLiteralDfa5_0(active0, 51539607552L);
/*      */       case 'r':
/*  384 */         return jjMoveStringLiteralDfa5_0(active0, 8589967360L);
/*      */       case 's':
/*  386 */         return jjMoveStringLiteralDfa5_0(active0, 8192L);
/*      */       case 't':
/*  388 */         if ((active0 & 0x20L) != 0L)
/*  389 */           return jjStartNfaWithStates_0(4, 5, 43); 
/*  390 */         return jjMoveStringLiteralDfa5_0(active0, 65536L);
/*      */       case 'u':
/*  392 */         return jjMoveStringLiteralDfa5_0(active0, 16512L);
/*      */       case 'y':
/*  394 */         if ((active0 & 0x20000L) != 0L) {
/*  395 */           return jjStartNfaWithStates_0(4, 17, 43);
/*      */         }
/*      */         break;
/*      */     } 
/*      */     
/*  400 */     return jjStartNfa_0(3, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa5_0(long old0, long active0) {
/*  404 */     if ((active0 &= old0) == 0L)
/*  405 */       return jjStartNfa_0(3, old0);  try {
/*  406 */       this.curChar = this.input_stream.readChar();
/*  407 */     } catch (IOException e) {
/*  408 */       jjStopStringLiteralDfa_0(4, active0);
/*  409 */       return 5;
/*      */     } 
/*  411 */     switch (this.curChar) {
/*      */       
/*      */       case 'a':
/*  414 */         return jjMoveStringLiteralDfa6_0(active0, 1024L);
/*      */       case 'b':
/*  416 */         return jjMoveStringLiteralDfa6_0(active0, 134217728L);
/*      */       case 'd':
/*  418 */         return jjMoveStringLiteralDfa6_0(active0, 128L);
/*      */       case 'g':
/*  420 */         if ((active0 & 0x800000000L) != 0L)
/*  421 */           return jjStartNfaWithStates_0(5, 35, 43); 
/*      */         break;
/*      */       case 'i':
/*  424 */         return jjMoveStringLiteralDfa6_0(active0, 32768L);
/*      */       case 'l':
/*  426 */         return jjMoveStringLiteralDfa6_0(active0, 540672L);
/*      */       case 'n':
/*  428 */         return jjMoveStringLiteralDfa6_0(active0, 8657043456L);
/*      */       case 'p':
/*  430 */         return jjMoveStringLiteralDfa6_0(active0, 8192L);
/*      */       case 't':
/*  432 */         if ((active0 & 0x400000000L) != 0L)
/*  433 */           return jjStartNfaWithStates_0(5, 34, 43); 
/*      */         break;
/*      */       case 'y':
/*  436 */         return jjMoveStringLiteralDfa6_0(active0, 65536L);
/*      */     } 
/*      */ 
/*      */     
/*  440 */     return jjStartNfa_0(4, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa6_0(long old0, long active0) {
/*  444 */     if ((active0 &= old0) == 0L)
/*  445 */       return jjStartNfa_0(4, old0);  try {
/*  446 */       this.curChar = this.input_stream.readChar();
/*  447 */     } catch (IOException e) {
/*  448 */       jjStopStringLiteralDfa_0(5, active0);
/*  449 */       return 6;
/*      */     } 
/*  451 */     switch (this.curChar) {
/*      */       
/*      */       case 'a':
/*  454 */         return jjMoveStringLiteralDfa7_0(active0, 8589942784L);
/*      */       case 'e':
/*  456 */         if ((active0 & 0x80L) != 0L)
/*  457 */           return jjStartNfaWithStates_0(6, 7, 43); 
/*      */         break;
/*      */       case 'o':
/*  460 */         return jjMoveStringLiteralDfa7_0(active0, 524288L);
/*      */       case 'p':
/*  462 */         return jjMoveStringLiteralDfa7_0(active0, 65536L);
/*      */       case 'r':
/*  464 */         if ((active0 & 0x400L) != 0L)
/*  465 */           return jjStartNfaWithStates_0(6, 10, 43); 
/*      */         break;
/*      */       case 't':
/*  468 */         if ((active0 & 0x4000L) != 0L)
/*  469 */           return jjStartNfaWithStates_0(6, 14, 43); 
/*  470 */         if ((active0 & 0x8000L) != 0L)
/*  471 */           return jjStartNfaWithStates_0(6, 15, 43); 
/*  472 */         if ((active0 & 0x4000000L) != 0L)
/*  473 */           return jjStartNfaWithStates_0(6, 26, 43); 
/*      */         break;
/*      */       case 'u':
/*  476 */         return jjMoveStringLiteralDfa7_0(active0, 134217728L);
/*      */     } 
/*      */ 
/*      */     
/*  480 */     return jjStartNfa_0(5, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa7_0(long old0, long active0) {
/*  484 */     if ((active0 &= old0) == 0L)
/*  485 */       return jjStartNfa_0(5, old0);  try {
/*  486 */       this.curChar = this.input_stream.readChar();
/*  487 */     } catch (IOException e) {
/*  488 */       jjStopStringLiteralDfa_0(6, active0);
/*  489 */       return 7;
/*      */     } 
/*  491 */     switch (this.curChar) {
/*      */       
/*      */       case 'c':
/*  494 */         return jjMoveStringLiteralDfa8_0(active0, 8192L);
/*      */       case 'e':
/*  496 */         return jjMoveStringLiteralDfa8_0(active0, 65536L);
/*      */       case 'l':
/*  498 */         if ((active0 & 0x200000000L) != 0L)
/*  499 */           return jjStartNfaWithStates_0(7, 33, 43); 
/*      */         break;
/*      */       case 't':
/*  502 */         return jjMoveStringLiteralDfa8_0(active0, 134217728L);
/*      */       case 'w':
/*  504 */         return jjMoveStringLiteralDfa8_0(active0, 524288L);
/*      */     } 
/*      */ 
/*      */     
/*  508 */     return jjStartNfa_0(6, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa8_0(long old0, long active0) {
/*  512 */     if ((active0 &= old0) == 0L)
/*  513 */       return jjStartNfa_0(6, old0);  try {
/*  514 */       this.curChar = this.input_stream.readChar();
/*  515 */     } catch (IOException e) {
/*  516 */       jjStopStringLiteralDfa_0(7, active0);
/*  517 */       return 8;
/*      */     } 
/*  519 */     switch (this.curChar) {
/*      */       
/*      */       case 'e':
/*  522 */         if ((active0 & 0x2000L) != 0L)
/*  523 */           return jjStartNfaWithStates_0(8, 13, 43); 
/*  524 */         if ((active0 & 0x8000000L) != 0L)
/*  525 */           return jjStartNfaWithStates_0(8, 27, 43); 
/*  526 */         return jjMoveStringLiteralDfa9_0(active0, 524288L);
/*      */       case 's':
/*  528 */         if ((active0 & 0x10000L) != 0L) {
/*  529 */           return jjStartNfaWithStates_0(8, 16, 43);
/*      */         }
/*      */         break;
/*      */     } 
/*      */     
/*  534 */     return jjStartNfa_0(7, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa9_0(long old0, long active0) {
/*  538 */     if ((active0 &= old0) == 0L)
/*  539 */       return jjStartNfa_0(7, old0);  try {
/*  540 */       this.curChar = this.input_stream.readChar();
/*  541 */     } catch (IOException e) {
/*  542 */       jjStopStringLiteralDfa_0(8, active0);
/*  543 */       return 9;
/*      */     } 
/*  545 */     switch (this.curChar) {
/*      */       
/*      */       case 'd':
/*  548 */         if ((active0 & 0x80000L) != 0L) {
/*  549 */           return jjStartNfaWithStates_0(9, 19, 43);
/*      */         }
/*      */         break;
/*      */     } 
/*      */     
/*  554 */     return jjStartNfa_0(8, active0);
/*      */   }
/*      */   
/*      */   private final void jjCheckNAdd(int state) {
/*  558 */     if (this.jjrounds[state] != this.jjround) {
/*      */       
/*  560 */       this.jjstateSet[this.jjnewStateCnt++] = state;
/*  561 */       this.jjrounds[state] = this.jjround;
/*      */     } 
/*      */   }
/*      */   
/*      */   private final void jjAddStates(int start, int end) {
/*      */     do {
/*  567 */       this.jjstateSet[this.jjnewStateCnt++] = jjnextStates[start];
/*  568 */     } while (start++ != end);
/*      */   }
/*      */   
/*      */   private final void jjCheckNAddTwoStates(int state1, int state2) {
/*  572 */     jjCheckNAdd(state1);
/*  573 */     jjCheckNAdd(state2);
/*      */   }
/*      */   
/*      */   private final void jjCheckNAddStates(int start, int end) {
/*      */     do {
/*  578 */       jjCheckNAdd(jjnextStates[start]);
/*  579 */     } while (start++ != end);
/*      */   }
/*      */   
/*      */   private final void jjCheckNAddStates(int start) {
/*  583 */     jjCheckNAdd(jjnextStates[start]);
/*  584 */     jjCheckNAdd(jjnextStates[start + 1]);
/*      */   }
/*  586 */   static final long[] jjbitVec0 = new long[] { -2L, -1L, -1L, -1L };
/*      */ 
/*      */   
/*  589 */   static final long[] jjbitVec2 = new long[] { 0L, 0L, -1L, -1L };
/*      */ 
/*      */   
/*  592 */   static final long[] jjbitVec3 = new long[] { 0L, -16384L, -17590038560769L, 8388607L };
/*      */ 
/*      */   
/*  595 */   static final long[] jjbitVec4 = new long[] { 0L, 0L, 0L, -36028797027352577L };
/*      */ 
/*      */   
/*  598 */   static final long[] jjbitVec5 = new long[] { 9219994337134247935L, 9223372036854775294L, -1L, -274156627316187121L };
/*      */ 
/*      */   
/*  601 */   static final long[] jjbitVec6 = new long[] { 16777215L, -65536L, -576458553280167937L, 3L };
/*      */ 
/*      */   
/*  604 */   static final long[] jjbitVec7 = new long[] { 0L, 0L, -17179879616L, 4503588160110591L };
/*      */ 
/*      */   
/*  607 */   static final long[] jjbitVec8 = new long[] { -8194L, -536936449L, -65533L, 234134404065073567L };
/*      */ 
/*      */   
/*  610 */   static final long[] jjbitVec9 = new long[] { -562949953421312L, -8547991553L, 127L, 1979120929931264L };
/*      */ 
/*      */   
/*  613 */   static final long[] jjbitVec10 = new long[] { 576460743713488896L, -562949953419266L, 9007199254740991999L, 412319973375L };
/*      */ 
/*      */   
/*  616 */   static final long[] jjbitVec11 = new long[] { 2594073385365405664L, 17163091968L, 271902628478820320L, 844440767823872L };
/*      */ 
/*      */   
/*  619 */   static final long[] jjbitVec12 = new long[] { 247132830528276448L, 7881300924956672L, 2589004636761075680L, 4294967296L };
/*      */ 
/*      */   
/*  622 */   static final long[] jjbitVec13 = new long[] { 2579997437506199520L, 15837691904L, 270153412153034720L, 0L };
/*      */ 
/*      */   
/*  625 */   static final long[] jjbitVec14 = new long[] { 283724577500946400L, 12884901888L, 283724577500946400L, 13958643712L };
/*      */ 
/*      */   
/*  628 */   static final long[] jjbitVec15 = new long[] { 288228177128316896L, 12884901888L, 0L, 0L };
/*      */ 
/*      */   
/*  631 */   static final long[] jjbitVec16 = new long[] { 3799912185593854L, 63L, 2309621682768192918L, 31L };
/*      */ 
/*      */   
/*  634 */   static final long[] jjbitVec17 = new long[] { 0L, 4398046510847L, 0L, 0L };
/*      */ 
/*      */   
/*  637 */   static final long[] jjbitVec18 = new long[] { 0L, 0L, -4294967296L, 36028797018898495L };
/*      */ 
/*      */   
/*  640 */   static final long[] jjbitVec19 = new long[] { 5764607523034749677L, 12493387738468353L, -756383734487318528L, 144405459145588743L };
/*      */ 
/*      */   
/*  643 */   static final long[] jjbitVec20 = new long[] { -1L, -1L, -4026531841L, 288230376151711743L };
/*      */ 
/*      */   
/*  646 */   static final long[] jjbitVec21 = new long[] { -3233808385L, 4611686017001275199L, 6908521828386340863L, 2295745090394464220L };
/*      */ 
/*      */   
/*  649 */   static final long[] jjbitVec22 = new long[] { 83837761617920L, 0L, 7L, 0L };
/*      */ 
/*      */   
/*  652 */   static final long[] jjbitVec23 = new long[] { 4389456576640L, -2L, -8587837441L, 576460752303423487L };
/*      */ 
/*      */   
/*  655 */   static final long[] jjbitVec24 = new long[] { 35184372088800L, 0L, 0L, 0L };
/*      */ 
/*      */   
/*  658 */   static final long[] jjbitVec25 = new long[] { -1L, -1L, 274877906943L, 0L };
/*      */ 
/*      */   
/*  661 */   static final long[] jjbitVec26 = new long[] { -1L, -1L, 68719476735L, 0L };
/*      */ 
/*      */   
/*  664 */   static final long[] jjbitVec27 = new long[] { 0L, 0L, 36028797018963968L, -36028797027352577L };
/*      */ 
/*      */   
/*  667 */   static final long[] jjbitVec28 = new long[] { 16777215L, -65536L, -576458553280167937L, 196611L };
/*      */ 
/*      */   
/*  670 */   static final long[] jjbitVec29 = new long[] { -1L, 12884901951L, -17179879488L, 4503588160110591L };
/*      */ 
/*      */   
/*  673 */   static final long[] jjbitVec30 = new long[] { -8194L, -536936449L, -65413L, 234134404065073567L };
/*      */ 
/*      */   
/*  676 */   static final long[] jjbitVec31 = new long[] { -562949953421312L, -8547991553L, -4899916411759099777L, 1979120929931286L };
/*      */ 
/*      */   
/*  679 */   static final long[] jjbitVec32 = new long[] { 576460743713488896L, -277081224642561L, 9007199254740991999L, 288017070894841855L };
/*      */ 
/*      */   
/*  682 */   static final long[] jjbitVec33 = new long[] { -864691128455135250L, 281268803485695L, -3186861885341720594L, 1125692414638495L };
/*      */ 
/*      */   
/*  685 */   static final long[] jjbitVec34 = new long[] { -3211631683292264476L, 9006925953907079L, -869759877059465234L, 281204393786303L };
/*      */ 
/*      */   
/*  688 */   static final long[] jjbitVec35 = new long[] { -878767076314341394L, 281215949093263L, -4341532606274353172L, 280925229301191L };
/*      */ 
/*      */   
/*  691 */   static final long[] jjbitVec36 = new long[] { -4327961440926441490L, 281212990012895L, -4327961440926441492L, 281214063754719L };
/*      */ 
/*      */   
/*  694 */   static final long[] jjbitVec37 = new long[] { -4323457841299070996L, 281212992110031L, 0L, 0L };
/*      */ 
/*      */   
/*  697 */   static final long[] jjbitVec38 = new long[] { 576320014815068158L, 67076095L, 4323293666156225942L, 67059551L };
/*      */ 
/*      */   
/*  700 */   static final long[] jjbitVec39 = new long[] { -4422530440275951616L, -558551906910465L, 215680200883507167L, 0L };
/*      */ 
/*      */   
/*  703 */   static final long[] jjbitVec40 = new long[] { 0L, 0L, 0L, 9126739968L };
/*      */ 
/*      */   
/*  706 */   static final long[] jjbitVec41 = new long[] { 17732914942836896L, -2L, -6876561409L, 8646911284551352319L };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final int jjMoveNfa_0(int startState, int curPos) {
/*  712 */     int startsAt = 0;
/*  713 */     this.jjnewStateCnt = 43;
/*  714 */     int i = 1;
/*  715 */     this.jjstateSet[0] = startState;
/*  716 */     int kind = Integer.MAX_VALUE;
/*      */     
/*      */     while (true) {
/*  719 */       if (++this.jjround == Integer.MAX_VALUE)
/*  720 */         ReInitRounds(); 
/*  721 */       if (this.curChar < '@') {
/*      */         
/*  723 */         long l = 1L << this.curChar;
/*      */         
/*      */         do {
/*  726 */           switch (this.jjstateSet[--i]) {
/*      */             
/*      */             case 3:
/*  729 */               if ((0xFFFFFFFFFFFFF9FFL & l) != 0L)
/*      */               {
/*  731 */                 if (kind > 60)
/*  732 */                   kind = 60; 
/*      */               }
/*  734 */               if ((0x100000601L & l) != 0L) {
/*      */                 
/*  736 */                 if (kind > 39)
/*  737 */                   kind = 39; 
/*  738 */                 jjCheckNAdd(0);
/*      */               }
/*  740 */               else if (this.curChar == '\'') {
/*  741 */                 this.jjstateSet[this.jjnewStateCnt++] = 31;
/*  742 */               } else if (this.curChar == '"') {
/*  743 */                 this.jjstateSet[this.jjnewStateCnt++] = 22;
/*  744 */               } else if (this.curChar == '#') {
/*      */                 
/*  746 */                 if (kind > 42)
/*  747 */                   kind = 42; 
/*  748 */                 jjCheckNAdd(5);
/*      */               } 
/*  750 */               if (this.curChar == '\'') {
/*  751 */                 jjCheckNAddTwoStates(13, 14); break;
/*  752 */               }  if (this.curChar == '"') {
/*  753 */                 jjCheckNAddTwoStates(10, 11); break;
/*  754 */               }  if (this.curChar == '#')
/*  755 */                 this.jjstateSet[this.jjnewStateCnt++] = 1; 
/*      */               break;
/*      */             case 43:
/*  758 */               if ((0x3FF600000000000L & l) != 0L) {
/*  759 */                 jjCheckNAddTwoStates(39, 40);
/*  760 */               } else if (this.curChar == ':') {
/*  761 */                 this.jjstateSet[this.jjnewStateCnt++] = 41;
/*  762 */               }  if ((0x3FF600000000000L & l) != 0L) {
/*  763 */                 jjCheckNAddTwoStates(36, 38);
/*  764 */               } else if (this.curChar == ':') {
/*  765 */                 this.jjstateSet[this.jjnewStateCnt++] = 37;
/*  766 */               }  if ((0x3FF600000000000L & l) != 0L) {
/*      */                 
/*  768 */                 if (kind > 54)
/*  769 */                   kind = 54; 
/*  770 */                 jjCheckNAdd(35);
/*      */               } 
/*      */               break;
/*      */             case 0:
/*  774 */               if ((0x100000601L & l) == 0L)
/*      */                 break; 
/*  776 */               if (kind > 39)
/*  777 */                 kind = 39; 
/*  778 */               jjCheckNAdd(0);
/*      */               break;
/*      */             case 1:
/*  781 */               if (this.curChar != '#')
/*      */                 break; 
/*  783 */               if (kind > 40)
/*  784 */                 kind = 40; 
/*  785 */               jjCheckNAdd(2);
/*      */               break;
/*      */             case 2:
/*  788 */               if ((0xFFFFFFFFFFFFFBFEL & l) == 0L)
/*      */                 break; 
/*  790 */               if (kind > 40)
/*  791 */                 kind = 40; 
/*  792 */               jjCheckNAdd(2);
/*      */               break;
/*      */             case 4:
/*  795 */               if (this.curChar != '#')
/*      */                 break; 
/*  797 */               if (kind > 42)
/*  798 */                 kind = 42; 
/*  799 */               jjCheckNAdd(5);
/*      */               break;
/*      */             case 5:
/*  802 */               if ((0xFFFFFFFFFFFFFBFEL & l) == 0L)
/*      */                 break; 
/*  804 */               if (kind > 42)
/*  805 */                 kind = 42; 
/*  806 */               jjCheckNAdd(5);
/*      */               break;
/*      */             case 8:
/*  809 */               if ((0x3FF600000000000L & l) == 0L)
/*      */                 break; 
/*  811 */               if (kind > 55)
/*  812 */                 kind = 55; 
/*  813 */               this.jjstateSet[this.jjnewStateCnt++] = 8;
/*      */               break;
/*      */             case 9:
/*  816 */               if (this.curChar == '"')
/*  817 */                 jjCheckNAddTwoStates(10, 11); 
/*      */               break;
/*      */             case 10:
/*  820 */               if ((0xFFFFFFFBFFFFFFFEL & l) != 0L)
/*  821 */                 jjCheckNAddTwoStates(10, 11); 
/*      */               break;
/*      */             case 11:
/*      */             case 20:
/*  825 */               if (this.curChar == '"' && kind > 58)
/*  826 */                 kind = 58; 
/*      */               break;
/*      */             case 12:
/*  829 */               if (this.curChar == '\'')
/*  830 */                 jjCheckNAddTwoStates(13, 14); 
/*      */               break;
/*      */             case 13:
/*  833 */               if ((0xFFFFFF7FFFFFFFFEL & l) != 0L)
/*  834 */                 jjCheckNAddTwoStates(13, 14); 
/*      */               break;
/*      */             case 14:
/*      */             case 29:
/*  838 */               if (this.curChar == '\'' && kind > 58)
/*  839 */                 kind = 58; 
/*      */               break;
/*      */             case 15:
/*  842 */               if (this.curChar == '"')
/*  843 */                 jjCheckNAddStates(0, 2); 
/*      */               break;
/*      */             case 16:
/*  846 */               if ((0xFFFFFFFBFFFFFFFFL & l) != 0L)
/*  847 */                 jjCheckNAddStates(0, 2); 
/*      */               break;
/*      */             case 17:
/*      */             case 19:
/*  851 */               if (this.curChar == '"')
/*  852 */                 jjCheckNAdd(16); 
/*      */               break;
/*      */             case 18:
/*  855 */               if (this.curChar == '"')
/*  856 */                 jjAddStates(3, 4); 
/*      */               break;
/*      */             case 21:
/*  859 */               if (this.curChar == '"')
/*  860 */                 this.jjstateSet[this.jjnewStateCnt++] = 20; 
/*      */               break;
/*      */             case 22:
/*  863 */               if (this.curChar == '"')
/*  864 */                 this.jjstateSet[this.jjnewStateCnt++] = 15; 
/*      */               break;
/*      */             case 23:
/*  867 */               if (this.curChar == '"')
/*  868 */                 this.jjstateSet[this.jjnewStateCnt++] = 22; 
/*      */               break;
/*      */             case 24:
/*  871 */               if (this.curChar == '\'')
/*  872 */                 jjCheckNAddStates(5, 7); 
/*      */               break;
/*      */             case 25:
/*  875 */               if ((0xFFFFFF7FFFFFFFFFL & l) != 0L)
/*  876 */                 jjCheckNAddStates(5, 7); 
/*      */               break;
/*      */             case 26:
/*      */             case 28:
/*  880 */               if (this.curChar == '\'')
/*  881 */                 jjCheckNAdd(25); 
/*      */               break;
/*      */             case 27:
/*  884 */               if (this.curChar == '\'')
/*  885 */                 jjAddStates(8, 9); 
/*      */               break;
/*      */             case 30:
/*  888 */               if (this.curChar == '\'')
/*  889 */                 this.jjstateSet[this.jjnewStateCnt++] = 29; 
/*      */               break;
/*      */             case 31:
/*  892 */               if (this.curChar == '\'')
/*  893 */                 this.jjstateSet[this.jjnewStateCnt++] = 24; 
/*      */               break;
/*      */             case 32:
/*  896 */               if (this.curChar == '\'')
/*  897 */                 this.jjstateSet[this.jjnewStateCnt++] = 31; 
/*      */               break;
/*      */             case 33:
/*  900 */               if ((0xFFFFFFFFFFFFF9FFL & l) != 0L && kind > 60)
/*  901 */                 kind = 60; 
/*      */               break;
/*      */             case 35:
/*  904 */               if ((0x3FF600000000000L & l) == 0L)
/*      */                 break; 
/*  906 */               if (kind > 54)
/*  907 */                 kind = 54; 
/*  908 */               jjCheckNAdd(35);
/*      */               break;
/*      */             case 36:
/*  911 */               if ((0x3FF600000000000L & l) != 0L)
/*  912 */                 jjCheckNAddTwoStates(36, 38); 
/*      */               break;
/*      */             case 37:
/*  915 */               if (this.curChar == '*' && kind > 56)
/*  916 */                 kind = 56; 
/*      */               break;
/*      */             case 38:
/*  919 */               if (this.curChar == ':')
/*  920 */                 this.jjstateSet[this.jjnewStateCnt++] = 37; 
/*      */               break;
/*      */             case 39:
/*  923 */               if ((0x3FF600000000000L & l) != 0L)
/*  924 */                 jjCheckNAddTwoStates(39, 40); 
/*      */               break;
/*      */             case 40:
/*  927 */               if (this.curChar == ':')
/*  928 */                 this.jjstateSet[this.jjnewStateCnt++] = 41; 
/*      */               break;
/*      */             case 42:
/*  931 */               if ((0x3FF600000000000L & l) == 0L)
/*      */                 break; 
/*  933 */               if (kind > 57)
/*  934 */                 kind = 57; 
/*  935 */               this.jjstateSet[this.jjnewStateCnt++] = 42;
/*      */               break;
/*      */           } 
/*      */         
/*  939 */         } while (i != startsAt);
/*      */       }
/*  941 */       else if (this.curChar < '') {
/*      */         
/*  943 */         long l = 1L << (this.curChar & 0x3F);
/*      */         
/*      */         do {
/*  946 */           switch (this.jjstateSet[--i]) {
/*      */             
/*      */             case 3:
/*  949 */               if (kind > 60)
/*  950 */                 kind = 60; 
/*  951 */               if ((0x7FFFFFE87FFFFFEL & l) != 0L) {
/*      */                 
/*  953 */                 if (kind > 54)
/*  954 */                   kind = 54; 
/*  955 */                 jjCheckNAddStates(10, 14); break;
/*      */               } 
/*  957 */               if (this.curChar == '\\')
/*  958 */                 this.jjstateSet[this.jjnewStateCnt++] = 7; 
/*      */               break;
/*      */             case 43:
/*  961 */               if ((0x7FFFFFE87FFFFFEL & l) != 0L)
/*  962 */                 jjCheckNAddTwoStates(39, 40); 
/*  963 */               if ((0x7FFFFFE87FFFFFEL & l) != 0L)
/*  964 */                 jjCheckNAddTwoStates(36, 38); 
/*  965 */               if ((0x7FFFFFE87FFFFFEL & l) != 0L) {
/*      */                 
/*  967 */                 if (kind > 54)
/*  968 */                   kind = 54; 
/*  969 */                 jjCheckNAdd(35);
/*      */               } 
/*      */               break;
/*      */             case 2:
/*  973 */               if (kind > 40)
/*  974 */                 kind = 40; 
/*  975 */               this.jjstateSet[this.jjnewStateCnt++] = 2;
/*      */               break;
/*      */             case 5:
/*  978 */               if (kind > 42)
/*  979 */                 kind = 42; 
/*  980 */               this.jjstateSet[this.jjnewStateCnt++] = 5;
/*      */               break;
/*      */             case 6:
/*  983 */               if (this.curChar == '\\')
/*  984 */                 this.jjstateSet[this.jjnewStateCnt++] = 7; 
/*      */               break;
/*      */             case 7:
/*      */             case 8:
/*  988 */               if ((0x7FFFFFE87FFFFFEL & l) == 0L)
/*      */                 break; 
/*  990 */               if (kind > 55)
/*  991 */                 kind = 55; 
/*  992 */               jjCheckNAdd(8);
/*      */               break;
/*      */             case 10:
/*  995 */               jjAddStates(15, 16);
/*      */               break;
/*      */             case 13:
/*  998 */               jjAddStates(17, 18);
/*      */               break;
/*      */             case 16:
/* 1001 */               jjAddStates(0, 2);
/*      */               break;
/*      */             case 25:
/* 1004 */               jjAddStates(5, 7);
/*      */               break;
/*      */             case 33:
/* 1007 */               if (kind > 60)
/* 1008 */                 kind = 60; 
/*      */               break;
/*      */             case 34:
/* 1011 */               if ((0x7FFFFFE87FFFFFEL & l) == 0L)
/*      */                 break; 
/* 1013 */               if (kind > 54)
/* 1014 */                 kind = 54; 
/* 1015 */               jjCheckNAddStates(10, 14);
/*      */               break;
/*      */             case 35:
/* 1018 */               if ((0x7FFFFFE87FFFFFEL & l) == 0L)
/*      */                 break; 
/* 1020 */               if (kind > 54)
/* 1021 */                 kind = 54; 
/* 1022 */               jjCheckNAdd(35);
/*      */               break;
/*      */             case 36:
/* 1025 */               if ((0x7FFFFFE87FFFFFEL & l) != 0L)
/* 1026 */                 jjCheckNAddTwoStates(36, 38); 
/*      */               break;
/*      */             case 39:
/* 1029 */               if ((0x7FFFFFE87FFFFFEL & l) != 0L)
/* 1030 */                 jjCheckNAddTwoStates(39, 40); 
/*      */               break;
/*      */             case 41:
/*      */             case 42:
/* 1034 */               if ((0x7FFFFFE87FFFFFEL & l) == 0L)
/*      */                 break; 
/* 1036 */               if (kind > 57)
/* 1037 */                 kind = 57; 
/* 1038 */               jjCheckNAdd(42);
/*      */               break;
/*      */           } 
/*      */         
/* 1042 */         } while (i != startsAt);
/*      */       }
/*      */       else {
/*      */         
/* 1046 */         int hiByte = this.curChar >> 8;
/* 1047 */         int i1 = hiByte >> 6;
/* 1048 */         long l1 = 1L << (hiByte & 0x3F);
/* 1049 */         int i2 = (this.curChar & 0xFF) >> 6;
/* 1050 */         long l2 = 1L << (this.curChar & 0x3F);
/*      */         
/*      */         do {
/* 1053 */           switch (this.jjstateSet[--i]) {
/*      */             
/*      */             case 3:
/* 1056 */               if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/*      */               {
/* 1058 */                 if (kind > 60)
/* 1059 */                   kind = 60; 
/*      */               }
/* 1061 */               if (jjCanMove_1(hiByte, i1, i2, l1, l2)) {
/*      */                 
/* 1063 */                 if (kind > 54)
/* 1064 */                   kind = 54; 
/* 1065 */                 jjCheckNAddStates(10, 14);
/*      */               } 
/*      */               break;
/*      */             case 43:
/* 1069 */               if (jjCanMove_2(hiByte, i1, i2, l1, l2)) {
/*      */                 
/* 1071 */                 if (kind > 54)
/* 1072 */                   kind = 54; 
/* 1073 */                 jjCheckNAdd(35);
/*      */               } 
/* 1075 */               if (jjCanMove_2(hiByte, i1, i2, l1, l2))
/* 1076 */                 jjCheckNAddTwoStates(36, 38); 
/* 1077 */               if (jjCanMove_2(hiByte, i1, i2, l1, l2))
/* 1078 */                 jjCheckNAddTwoStates(39, 40); 
/*      */               break;
/*      */             case 2:
/* 1081 */               if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 1083 */               if (kind > 40)
/* 1084 */                 kind = 40; 
/* 1085 */               this.jjstateSet[this.jjnewStateCnt++] = 2;
/*      */               break;
/*      */             case 5:
/* 1088 */               if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 1090 */               if (kind > 42)
/* 1091 */                 kind = 42; 
/* 1092 */               this.jjstateSet[this.jjnewStateCnt++] = 5;
/*      */               break;
/*      */             case 7:
/* 1095 */               if (!jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 1097 */               if (kind > 55)
/* 1098 */                 kind = 55; 
/* 1099 */               jjCheckNAdd(8);
/*      */               break;
/*      */             case 8:
/* 1102 */               if (!jjCanMove_2(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 1104 */               if (kind > 55)
/* 1105 */                 kind = 55; 
/* 1106 */               jjCheckNAdd(8);
/*      */               break;
/*      */             case 10:
/* 1109 */               if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/* 1110 */                 jjAddStates(15, 16); 
/*      */               break;
/*      */             case 13:
/* 1113 */               if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/* 1114 */                 jjAddStates(17, 18); 
/*      */               break;
/*      */             case 16:
/* 1117 */               if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/* 1118 */                 jjAddStates(0, 2); 
/*      */               break;
/*      */             case 25:
/* 1121 */               if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/* 1122 */                 jjAddStates(5, 7); 
/*      */               break;
/*      */             case 33:
/* 1125 */               if (jjCanMove_0(hiByte, i1, i2, l1, l2) && kind > 60)
/* 1126 */                 kind = 60; 
/*      */               break;
/*      */             case 34:
/* 1129 */               if (!jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 1131 */               if (kind > 54)
/* 1132 */                 kind = 54; 
/* 1133 */               jjCheckNAddStates(10, 14);
/*      */               break;
/*      */             case 35:
/* 1136 */               if (!jjCanMove_2(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 1138 */               if (kind > 54)
/* 1139 */                 kind = 54; 
/* 1140 */               jjCheckNAdd(35);
/*      */               break;
/*      */             case 36:
/* 1143 */               if (jjCanMove_2(hiByte, i1, i2, l1, l2))
/* 1144 */                 jjCheckNAddTwoStates(36, 38); 
/*      */               break;
/*      */             case 39:
/* 1147 */               if (jjCanMove_2(hiByte, i1, i2, l1, l2))
/* 1148 */                 jjCheckNAddTwoStates(39, 40); 
/*      */               break;
/*      */             case 41:
/* 1151 */               if (!jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 1153 */               if (kind > 57)
/* 1154 */                 kind = 57; 
/* 1155 */               jjCheckNAdd(42);
/*      */               break;
/*      */             case 42:
/* 1158 */               if (!jjCanMove_2(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 1160 */               if (kind > 57)
/* 1161 */                 kind = 57; 
/* 1162 */               jjCheckNAdd(42);
/*      */               break;
/*      */           } 
/*      */         
/* 1166 */         } while (i != startsAt);
/*      */       } 
/* 1168 */       if (kind != Integer.MAX_VALUE) {
/*      */         
/* 1170 */         this.jjmatchedKind = kind;
/* 1171 */         this.jjmatchedPos = curPos;
/* 1172 */         kind = Integer.MAX_VALUE;
/*      */       } 
/* 1174 */       curPos++;
/* 1175 */       if ((i = this.jjnewStateCnt) == (startsAt = 43 - (this.jjnewStateCnt = startsAt)))
/* 1176 */         return curPos;  
/* 1177 */       try { this.curChar = this.input_stream.readChar(); }
/* 1178 */       catch (IOException e) { return curPos; }
/*      */     
/*      */     } 
/*      */   }
/*      */   private final int jjMoveStringLiteralDfa0_1() {
/* 1183 */     return jjMoveNfa_1(1, 0);
/*      */   }
/*      */ 
/*      */   
/*      */   private final int jjMoveNfa_1(int startState, int curPos) {
/* 1188 */     int startsAt = 0;
/* 1189 */     this.jjnewStateCnt = 10;
/* 1190 */     int i = 1;
/* 1191 */     this.jjstateSet[0] = startState;
/* 1192 */     int kind = Integer.MAX_VALUE;
/*      */     
/*      */     while (true) {
/* 1195 */       if (++this.jjround == Integer.MAX_VALUE)
/* 1196 */         ReInitRounds(); 
/* 1197 */       if (this.curChar < '@') {
/*      */         
/* 1199 */         long l = 1L << this.curChar;
/*      */         
/*      */         do {
/* 1202 */           switch (this.jjstateSet[--i]) {
/*      */             
/*      */             case 1:
/* 1205 */               if ((0xFFFFFFFFFFFFF9FFL & l) != 0L)
/*      */               {
/* 1207 */                 if (kind > 60)
/* 1208 */                   kind = 60; 
/*      */               }
/* 1210 */               if ((0x100000601L & l) != 0L) {
/*      */                 
/* 1212 */                 if (kind > 39)
/* 1213 */                   kind = 39; 
/* 1214 */                 jjCheckNAdd(0);
/*      */               } 
/* 1216 */               if ((0x401L & l) != 0L)
/* 1217 */                 jjCheckNAddStates(19, 22); 
/*      */               break;
/*      */             case 0:
/* 1220 */               if ((0x100000601L & l) == 0L)
/*      */                 break; 
/* 1222 */               if (kind > 39)
/* 1223 */                 kind = 39; 
/* 1224 */               jjCheckNAdd(0);
/*      */               break;
/*      */             case 2:
/* 1227 */               if ((0x401L & l) != 0L)
/* 1228 */                 jjCheckNAddStates(19, 22); 
/*      */               break;
/*      */             case 3:
/* 1231 */               if ((0x100000200L & l) != 0L)
/* 1232 */                 jjCheckNAddTwoStates(3, 6); 
/*      */               break;
/*      */             case 4:
/* 1235 */               if (this.curChar != '#')
/*      */                 break; 
/* 1237 */               if (kind > 43)
/* 1238 */                 kind = 43; 
/* 1239 */               jjCheckNAdd(5);
/*      */               break;
/*      */             case 5:
/* 1242 */               if ((0xFFFFFFFFFFFFFBFEL & l) == 0L)
/*      */                 break; 
/* 1244 */               if (kind > 43)
/* 1245 */                 kind = 43; 
/* 1246 */               jjCheckNAdd(5);
/*      */               break;
/*      */             case 6:
/* 1249 */               if (this.curChar == '#')
/* 1250 */                 this.jjstateSet[this.jjnewStateCnt++] = 4; 
/*      */               break;
/*      */             case 7:
/* 1253 */               if ((0x100000200L & l) != 0L)
/* 1254 */                 jjCheckNAddTwoStates(7, 8); 
/*      */               break;
/*      */             case 8:
/* 1257 */               if (this.curChar != '#')
/*      */                 break; 
/* 1259 */               if (kind > 44)
/* 1260 */                 kind = 44; 
/* 1261 */               jjCheckNAdd(9);
/*      */               break;
/*      */             case 9:
/* 1264 */               if ((0xFFFFFFFFFFFFFBFEL & l) == 0L)
/*      */                 break; 
/* 1266 */               if (kind > 44)
/* 1267 */                 kind = 44; 
/* 1268 */               jjCheckNAdd(9);
/*      */               break;
/*      */           } 
/*      */         
/* 1272 */         } while (i != startsAt);
/*      */       }
/* 1274 */       else if (this.curChar < '') {
/*      */         
/* 1276 */         long l = 1L << (this.curChar & 0x3F);
/*      */         
/*      */         do {
/* 1279 */           switch (this.jjstateSet[--i]) {
/*      */             
/*      */             case 1:
/* 1282 */               if (kind > 60)
/* 1283 */                 kind = 60; 
/*      */               break;
/*      */             case 5:
/* 1286 */               if (kind > 43)
/* 1287 */                 kind = 43; 
/* 1288 */               this.jjstateSet[this.jjnewStateCnt++] = 5;
/*      */               break;
/*      */             case 9:
/* 1291 */               if (kind > 44)
/* 1292 */                 kind = 44; 
/* 1293 */               this.jjstateSet[this.jjnewStateCnt++] = 9;
/*      */               break;
/*      */           } 
/*      */         
/* 1297 */         } while (i != startsAt);
/*      */       }
/*      */       else {
/*      */         
/* 1301 */         int hiByte = this.curChar >> 8;
/* 1302 */         int i1 = hiByte >> 6;
/* 1303 */         long l1 = 1L << (hiByte & 0x3F);
/* 1304 */         int i2 = (this.curChar & 0xFF) >> 6;
/* 1305 */         long l2 = 1L << (this.curChar & 0x3F);
/*      */         
/*      */         do {
/* 1308 */           switch (this.jjstateSet[--i]) {
/*      */             
/*      */             case 1:
/* 1311 */               if (jjCanMove_0(hiByte, i1, i2, l1, l2) && kind > 60)
/* 1312 */                 kind = 60; 
/*      */               break;
/*      */             case 5:
/* 1315 */               if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 1317 */               if (kind > 43)
/* 1318 */                 kind = 43; 
/* 1319 */               this.jjstateSet[this.jjnewStateCnt++] = 5;
/*      */               break;
/*      */             case 9:
/* 1322 */               if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 1324 */               if (kind > 44)
/* 1325 */                 kind = 44; 
/* 1326 */               this.jjstateSet[this.jjnewStateCnt++] = 9;
/*      */               break;
/*      */           } 
/*      */         
/* 1330 */         } while (i != startsAt);
/*      */       } 
/* 1332 */       if (kind != Integer.MAX_VALUE) {
/*      */         
/* 1334 */         this.jjmatchedKind = kind;
/* 1335 */         this.jjmatchedPos = curPos;
/* 1336 */         kind = Integer.MAX_VALUE;
/*      */       } 
/* 1338 */       curPos++;
/* 1339 */       if ((i = this.jjnewStateCnt) == (startsAt = 10 - (this.jjnewStateCnt = startsAt)))
/* 1340 */         return curPos;  
/* 1341 */       try { this.curChar = this.input_stream.readChar(); }
/* 1342 */       catch (IOException e) { return curPos; }
/*      */     
/*      */     } 
/*      */   }
/*      */   private final int jjMoveStringLiteralDfa0_2() {
/* 1347 */     return jjMoveNfa_2(1, 0);
/*      */   }
/*      */ 
/*      */   
/*      */   private final int jjMoveNfa_2(int startState, int curPos) {
/* 1352 */     int startsAt = 0;
/* 1353 */     this.jjnewStateCnt = 7;
/* 1354 */     int i = 1;
/* 1355 */     this.jjstateSet[0] = startState;
/* 1356 */     int kind = Integer.MAX_VALUE;
/*      */     
/*      */     while (true) {
/* 1359 */       if (++this.jjround == Integer.MAX_VALUE)
/* 1360 */         ReInitRounds(); 
/* 1361 */       if (this.curChar < '@') {
/*      */         
/* 1363 */         long l = 1L << this.curChar;
/*      */         
/*      */         do {
/* 1366 */           switch (this.jjstateSet[--i]) {
/*      */             
/*      */             case 1:
/* 1369 */               if ((0xFFFFFFFFFFFFF9FFL & l) != 0L)
/*      */               {
/* 1371 */                 if (kind > 60)
/* 1372 */                   kind = 60; 
/*      */               }
/* 1374 */               if ((0x100000601L & l) != 0L) {
/*      */                 
/* 1376 */                 if (kind > 39)
/* 1377 */                   kind = 39; 
/* 1378 */                 jjCheckNAdd(0);
/*      */               } 
/* 1380 */               if ((0x401L & l) != 0L)
/* 1381 */                 jjCheckNAddTwoStates(2, 5); 
/*      */               break;
/*      */             case 0:
/* 1384 */               if ((0x100000601L & l) == 0L)
/*      */                 break; 
/* 1386 */               if (kind > 39)
/* 1387 */                 kind = 39; 
/* 1388 */               jjCheckNAdd(0);
/*      */               break;
/*      */             case 2:
/* 1391 */               if ((0x100000200L & l) != 0L)
/* 1392 */                 jjCheckNAddTwoStates(2, 5); 
/*      */               break;
/*      */             case 3:
/* 1395 */               if (this.curChar != '#')
/*      */                 break; 
/* 1397 */               if (kind > 41)
/* 1398 */                 kind = 41; 
/* 1399 */               jjCheckNAdd(4);
/*      */               break;
/*      */             case 4:
/* 1402 */               if ((0xFFFFFFFFFFFFFBFEL & l) == 0L)
/*      */                 break; 
/* 1404 */               if (kind > 41)
/* 1405 */                 kind = 41; 
/* 1406 */               jjCheckNAdd(4);
/*      */               break;
/*      */             case 5:
/* 1409 */               if (this.curChar == '#')
/* 1410 */                 this.jjstateSet[this.jjnewStateCnt++] = 3; 
/*      */               break;
/*      */             case 6:
/* 1413 */               if ((0xFFFFFFFFFFFFF9FFL & l) != 0L && kind > 60) {
/* 1414 */                 kind = 60;
/*      */               }
/*      */               break;
/*      */           } 
/* 1418 */         } while (i != startsAt);
/*      */       }
/* 1420 */       else if (this.curChar < '') {
/*      */         
/* 1422 */         long l = 1L << (this.curChar & 0x3F);
/*      */         
/*      */         do {
/* 1425 */           switch (this.jjstateSet[--i]) {
/*      */             
/*      */             case 1:
/* 1428 */               if (kind > 60)
/* 1429 */                 kind = 60; 
/*      */               break;
/*      */             case 4:
/* 1432 */               if (kind > 41)
/* 1433 */                 kind = 41; 
/* 1434 */               this.jjstateSet[this.jjnewStateCnt++] = 4;
/*      */               break;
/*      */           } 
/*      */         
/* 1438 */         } while (i != startsAt);
/*      */       }
/*      */       else {
/*      */         
/* 1442 */         int hiByte = this.curChar >> 8;
/* 1443 */         int i1 = hiByte >> 6;
/* 1444 */         long l1 = 1L << (hiByte & 0x3F);
/* 1445 */         int i2 = (this.curChar & 0xFF) >> 6;
/* 1446 */         long l2 = 1L << (this.curChar & 0x3F);
/*      */         
/*      */         do {
/* 1449 */           switch (this.jjstateSet[--i]) {
/*      */             
/*      */             case 1:
/* 1452 */               if (jjCanMove_0(hiByte, i1, i2, l1, l2) && kind > 60)
/* 1453 */                 kind = 60; 
/*      */               break;
/*      */             case 4:
/* 1456 */               if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 1458 */               if (kind > 41)
/* 1459 */                 kind = 41; 
/* 1460 */               this.jjstateSet[this.jjnewStateCnt++] = 4;
/*      */               break;
/*      */           } 
/*      */         
/* 1464 */         } while (i != startsAt);
/*      */       } 
/* 1466 */       if (kind != Integer.MAX_VALUE) {
/*      */         
/* 1468 */         this.jjmatchedKind = kind;
/* 1469 */         this.jjmatchedPos = curPos;
/* 1470 */         kind = Integer.MAX_VALUE;
/*      */       } 
/* 1472 */       curPos++;
/* 1473 */       if ((i = this.jjnewStateCnt) == (startsAt = 7 - (this.jjnewStateCnt = startsAt)))
/* 1474 */         return curPos;  
/* 1475 */       try { this.curChar = this.input_stream.readChar(); }
/* 1476 */       catch (IOException e) { return curPos; }
/*      */     
/*      */     } 
/* 1479 */   } static final int[] jjnextStates = new int[] { 16, 17, 18, 19, 21, 25, 26, 27, 28, 30, 35, 36, 38, 39, 40, 10, 11, 13, 14, 3, 6, 7, 8 };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2) {
/* 1485 */     switch (hiByte) {
/*      */       
/*      */       case 0:
/* 1488 */         return ((jjbitVec2[i2] & l2) != 0L);
/*      */     } 
/* 1490 */     if ((jjbitVec0[i1] & l1) != 0L)
/* 1491 */       return true; 
/* 1492 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean jjCanMove_1(int hiByte, int i1, int i2, long l1, long l2) {
/* 1497 */     switch (hiByte) {
/*      */       
/*      */       case 0:
/* 1500 */         return ((jjbitVec4[i2] & l2) != 0L);
/*      */       case 1:
/* 1502 */         return ((jjbitVec5[i2] & l2) != 0L);
/*      */       case 2:
/* 1504 */         return ((jjbitVec6[i2] & l2) != 0L);
/*      */       case 3:
/* 1506 */         return ((jjbitVec7[i2] & l2) != 0L);
/*      */       case 4:
/* 1508 */         return ((jjbitVec8[i2] & l2) != 0L);
/*      */       case 5:
/* 1510 */         return ((jjbitVec9[i2] & l2) != 0L);
/*      */       case 6:
/* 1512 */         return ((jjbitVec10[i2] & l2) != 0L);
/*      */       case 9:
/* 1514 */         return ((jjbitVec11[i2] & l2) != 0L);
/*      */       case 10:
/* 1516 */         return ((jjbitVec12[i2] & l2) != 0L);
/*      */       case 11:
/* 1518 */         return ((jjbitVec13[i2] & l2) != 0L);
/*      */       case 12:
/* 1520 */         return ((jjbitVec14[i2] & l2) != 0L);
/*      */       case 13:
/* 1522 */         return ((jjbitVec15[i2] & l2) != 0L);
/*      */       case 14:
/* 1524 */         return ((jjbitVec16[i2] & l2) != 0L);
/*      */       case 15:
/* 1526 */         return ((jjbitVec17[i2] & l2) != 0L);
/*      */       case 16:
/* 1528 */         return ((jjbitVec18[i2] & l2) != 0L);
/*      */       case 17:
/* 1530 */         return ((jjbitVec19[i2] & l2) != 0L);
/*      */       case 30:
/* 1532 */         return ((jjbitVec20[i2] & l2) != 0L);
/*      */       case 31:
/* 1534 */         return ((jjbitVec21[i2] & l2) != 0L);
/*      */       case 33:
/* 1536 */         return ((jjbitVec22[i2] & l2) != 0L);
/*      */       case 48:
/* 1538 */         return ((jjbitVec23[i2] & l2) != 0L);
/*      */       case 49:
/* 1540 */         return ((jjbitVec24[i2] & l2) != 0L);
/*      */       case 159:
/* 1542 */         return ((jjbitVec25[i2] & l2) != 0L);
/*      */       case 215:
/* 1544 */         return ((jjbitVec26[i2] & l2) != 0L);
/*      */     } 
/* 1546 */     if ((jjbitVec3[i1] & l1) != 0L)
/* 1547 */       return true; 
/* 1548 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean jjCanMove_2(int hiByte, int i1, int i2, long l1, long l2) {
/* 1553 */     switch (hiByte) {
/*      */       
/*      */       case 0:
/* 1556 */         return ((jjbitVec27[i2] & l2) != 0L);
/*      */       case 1:
/* 1558 */         return ((jjbitVec5[i2] & l2) != 0L);
/*      */       case 2:
/* 1560 */         return ((jjbitVec28[i2] & l2) != 0L);
/*      */       case 3:
/* 1562 */         return ((jjbitVec29[i2] & l2) != 0L);
/*      */       case 4:
/* 1564 */         return ((jjbitVec30[i2] & l2) != 0L);
/*      */       case 5:
/* 1566 */         return ((jjbitVec31[i2] & l2) != 0L);
/*      */       case 6:
/* 1568 */         return ((jjbitVec32[i2] & l2) != 0L);
/*      */       case 9:
/* 1570 */         return ((jjbitVec33[i2] & l2) != 0L);
/*      */       case 10:
/* 1572 */         return ((jjbitVec34[i2] & l2) != 0L);
/*      */       case 11:
/* 1574 */         return ((jjbitVec35[i2] & l2) != 0L);
/*      */       case 12:
/* 1576 */         return ((jjbitVec36[i2] & l2) != 0L);
/*      */       case 13:
/* 1578 */         return ((jjbitVec37[i2] & l2) != 0L);
/*      */       case 14:
/* 1580 */         return ((jjbitVec38[i2] & l2) != 0L);
/*      */       case 15:
/* 1582 */         return ((jjbitVec39[i2] & l2) != 0L);
/*      */       case 16:
/* 1584 */         return ((jjbitVec18[i2] & l2) != 0L);
/*      */       case 17:
/* 1586 */         return ((jjbitVec19[i2] & l2) != 0L);
/*      */       case 30:
/* 1588 */         return ((jjbitVec20[i2] & l2) != 0L);
/*      */       case 31:
/* 1590 */         return ((jjbitVec21[i2] & l2) != 0L);
/*      */       case 32:
/* 1592 */         return ((jjbitVec40[i2] & l2) != 0L);
/*      */       case 33:
/* 1594 */         return ((jjbitVec22[i2] & l2) != 0L);
/*      */       case 48:
/* 1596 */         return ((jjbitVec41[i2] & l2) != 0L);
/*      */       case 49:
/* 1598 */         return ((jjbitVec24[i2] & l2) != 0L);
/*      */       case 159:
/* 1600 */         return ((jjbitVec25[i2] & l2) != 0L);
/*      */       case 215:
/* 1602 */         return ((jjbitVec26[i2] & l2) != 0L);
/*      */     } 
/* 1604 */     if ((jjbitVec3[i1] & l1) != 0L)
/* 1605 */       return true; 
/* 1606 */     return false;
/*      */   }
/*      */   
/* 1609 */   public static final String[] jjstrLiteralImages = new String[] { "", "[", "=", "&=", "|=", "start", "div", "include", "~", "]", "grammar", "{", "}", "namespace", "default", "inherit", "datatypes", "empty", "text", "notAllowed", "|", "&", ",", "+", "?", "*", "element", "attribute", "(", ")", "-", "list", "mixed", "external", "parent", "string", "token", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, ">>", null };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1620 */   public static final String[] lexStateNames = new String[] { "DEFAULT", "AFTER_SINGLE_LINE_COMMENT", "AFTER_DOCUMENTATION" };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1625 */   public static final int[] jjnewLexState = new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 2, -1, 1, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1630 */   static final long[] jjtoToken = new long[] { 2287840842771070975L };
/*      */ 
/*      */   
/* 1633 */   static final long[] jjtoSkip = new long[] { 22539988369408L };
/*      */ 
/*      */   
/* 1636 */   static final long[] jjtoSpecial = new long[] { 21990232555520L };
/*      */   
/*      */   protected JavaCharStream input_stream;
/*      */   
/* 1640 */   private final int[] jjrounds = new int[43];
/* 1641 */   private final int[] jjstateSet = new int[86];
/*      */   StringBuffer image;
/*      */   int jjimageLen;
/*      */   int lengthOfMatch;
/*      */   protected char curChar;
/*      */   int curLexState;
/*      */   int defaultLexState;
/*      */   int jjnewStateCnt;
/*      */   int jjround;
/*      */   int jjmatchedPos;
/*      */   int jjmatchedKind;
/*      */   
/*      */   public CompactSyntaxTokenManager(JavaCharStream stream, int lexState) {
/* 1654 */     this(stream);
/* 1655 */     SwitchTo(lexState);
/*      */   }
/*      */   
/*      */   public void ReInit(JavaCharStream stream) {
/* 1659 */     this.jjmatchedPos = this.jjnewStateCnt = 0;
/* 1660 */     this.curLexState = this.defaultLexState;
/* 1661 */     this.input_stream = stream;
/* 1662 */     ReInitRounds();
/*      */   }
/*      */ 
/*      */   
/*      */   private final void ReInitRounds() {
/* 1667 */     this.jjround = -2147483647;
/* 1668 */     for (int i = 43; i-- > 0;)
/* 1669 */       this.jjrounds[i] = Integer.MIN_VALUE; 
/*      */   }
/*      */   
/*      */   public void ReInit(JavaCharStream stream, int lexState) {
/* 1673 */     ReInit(stream);
/* 1674 */     SwitchTo(lexState);
/*      */   }
/*      */   
/*      */   public void SwitchTo(int lexState) {
/* 1678 */     if (lexState >= 3 || lexState < 0) {
/* 1679 */       throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", 2);
/*      */     }
/* 1681 */     this.curLexState = lexState;
/*      */   }
/*      */ 
/*      */   
/*      */   protected Token jjFillToken() {
/* 1686 */     Token t = Token.newToken(this.jjmatchedKind);
/* 1687 */     t.kind = this.jjmatchedKind;
/* 1688 */     String im = jjstrLiteralImages[this.jjmatchedKind];
/* 1689 */     t.image = (im == null) ? this.input_stream.GetImage() : im;
/* 1690 */     t.beginLine = this.input_stream.getBeginLine();
/* 1691 */     t.beginColumn = this.input_stream.getBeginColumn();
/* 1692 */     t.endLine = this.input_stream.getEndLine();
/* 1693 */     t.endColumn = this.input_stream.getEndColumn();
/* 1694 */     return t;
/*      */   }
/*      */   public CompactSyntaxTokenManager(JavaCharStream stream) {
/* 1697 */     this.curLexState = 0;
/* 1698 */     this.defaultLexState = 0;
/*      */     this.input_stream = stream;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Token getNextToken() {
/* 1707 */     Token specialToken = null;
/*      */     
/* 1709 */     int curPos = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     while (true) {
/*      */       try {
/* 1716 */         this.curChar = this.input_stream.BeginToken();
/*      */       }
/* 1718 */       catch (IOException e) {
/*      */         
/* 1720 */         this.jjmatchedKind = 0;
/* 1721 */         Token matchedToken = jjFillToken();
/* 1722 */         matchedToken.specialToken = specialToken;
/* 1723 */         return matchedToken;
/*      */       } 
/* 1725 */       this.image = null;
/* 1726 */       this.jjimageLen = 0;
/*      */       
/* 1728 */       switch (this.curLexState) {
/*      */         
/*      */         case 0:
/* 1731 */           this.jjmatchedKind = Integer.MAX_VALUE;
/* 1732 */           this.jjmatchedPos = 0;
/* 1733 */           curPos = jjMoveStringLiteralDfa0_0();
/*      */           break;
/*      */         case 1:
/* 1736 */           this.jjmatchedKind = Integer.MAX_VALUE;
/* 1737 */           this.jjmatchedPos = 0;
/* 1738 */           curPos = jjMoveStringLiteralDfa0_1();
/*      */           break;
/*      */         case 2:
/* 1741 */           this.jjmatchedKind = Integer.MAX_VALUE;
/* 1742 */           this.jjmatchedPos = 0;
/* 1743 */           curPos = jjMoveStringLiteralDfa0_2();
/*      */           break;
/*      */       } 
/* 1746 */       if (this.jjmatchedKind != Integer.MAX_VALUE) {
/*      */         
/* 1748 */         if (this.jjmatchedPos + 1 < curPos)
/* 1749 */           this.input_stream.backup(curPos - this.jjmatchedPos - 1); 
/* 1750 */         if ((jjtoToken[this.jjmatchedKind >> 6] & 1L << (this.jjmatchedKind & 0x3F)) != 0L) {
/*      */           
/* 1752 */           Token matchedToken = jjFillToken();
/* 1753 */           matchedToken.specialToken = specialToken;
/* 1754 */           if (jjnewLexState[this.jjmatchedKind] != -1)
/* 1755 */             this.curLexState = jjnewLexState[this.jjmatchedKind]; 
/* 1756 */           return matchedToken;
/*      */         } 
/*      */ 
/*      */         
/* 1760 */         if ((jjtoSpecial[this.jjmatchedKind >> 6] & 1L << (this.jjmatchedKind & 0x3F)) != 0L) {
/*      */           
/* 1762 */           Token matchedToken = jjFillToken();
/* 1763 */           if (specialToken == null) {
/* 1764 */             specialToken = matchedToken;
/*      */           } else {
/*      */             
/* 1767 */             matchedToken.specialToken = specialToken;
/* 1768 */             specialToken = specialToken.next = matchedToken;
/*      */           } 
/* 1770 */           SkipLexicalActions(matchedToken);
/*      */         } else {
/*      */           
/* 1773 */           SkipLexicalActions(null);
/* 1774 */         }  if (jjnewLexState[this.jjmatchedKind] != -1)
/* 1775 */           this.curLexState = jjnewLexState[this.jjmatchedKind];  continue;
/*      */       } 
/*      */       break;
/*      */     } 
/* 1779 */     int error_line = this.input_stream.getEndLine();
/* 1780 */     int error_column = this.input_stream.getEndColumn();
/* 1781 */     String error_after = null;
/* 1782 */     boolean EOFSeen = false; try {
/* 1783 */       this.input_stream.readChar(); this.input_stream.backup(1);
/* 1784 */     } catch (IOException e1) {
/* 1785 */       EOFSeen = true;
/* 1786 */       error_after = (curPos <= 1) ? "" : this.input_stream.GetImage();
/* 1787 */       if (this.curChar == '\n' || this.curChar == '\r') {
/* 1788 */         error_line++;
/* 1789 */         error_column = 0;
/*      */       } else {
/*      */         
/* 1792 */         error_column++;
/*      */       } 
/* 1794 */     }  if (!EOFSeen) {
/* 1795 */       this.input_stream.backup(1);
/* 1796 */       error_after = (curPos <= 1) ? "" : this.input_stream.GetImage();
/*      */     } 
/* 1798 */     throw new TokenMgrError(EOFSeen, this.curLexState, error_line, error_column, error_after, this.curChar, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void SkipLexicalActions(Token matchedToken) {
/* 1804 */     switch (this.jjmatchedKind) {
/*      */     
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\parse\compact\CompactSyntaxTokenManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */