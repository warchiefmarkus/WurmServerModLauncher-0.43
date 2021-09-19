/*      */ package com.sun.xml.xsom.impl.scd;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ 
/*      */ 
/*      */ public class SCDParserTokenManager
/*      */   implements SCDParserConstants
/*      */ {
/*   10 */   public PrintStream debugStream = System.out; public void setDebugStream(PrintStream ds) {
/*   11 */     this.debugStream = ds;
/*      */   }
/*      */   private final int jjStopStringLiteralDfa_0(int pos, long active0) {
/*   14 */     switch (pos) {
/*      */       
/*      */       case 0:
/*   17 */         if ((active0 & 0x3C08000000L) != 0L) {
/*      */           
/*   19 */           this.jjmatchedKind = 12;
/*   20 */           return 103;
/*      */         } 
/*   22 */         if ((active0 & 0x400000L) != 0L) {
/*      */           
/*   24 */           this.jjmatchedKind = 12;
/*   25 */           return 55;
/*      */         } 
/*   27 */         if ((active0 & 0x30000000000L) != 0L) {
/*      */           
/*   29 */           this.jjmatchedKind = 12;
/*   30 */           return 68;
/*      */         } 
/*   32 */         if ((active0 & 0x2000000L) != 0L) {
/*      */           
/*   34 */           this.jjmatchedKind = 12;
/*   35 */           return 81;
/*      */         } 
/*   37 */         if ((active0 & 0x200000000L) != 0L) {
/*      */           
/*   39 */           this.jjmatchedKind = 12;
/*   40 */           return 23;
/*      */         } 
/*   42 */         if ((active0 & 0x40000000000L) != 0L) {
/*      */           
/*   44 */           this.jjmatchedKind = 12;
/*   45 */           return 34;
/*      */         } 
/*   47 */         if ((active0 & 0x100000L) != 0L) {
/*      */           
/*   49 */           this.jjmatchedKind = 12;
/*   50 */           return 91;
/*      */         } 
/*   52 */         if ((active0 & 0x18C1F4240000L) != 0L) {
/*      */           
/*   54 */           this.jjmatchedKind = 12;
/*   55 */           return 1;
/*      */         } 
/*   57 */         if ((active0 & 0x1000000L) != 0L) {
/*      */           
/*   59 */           this.jjmatchedKind = 12;
/*   60 */           return 16;
/*      */         } 
/*   62 */         return -1;
/*      */       case 1:
/*   64 */         if ((active0 & 0x1FFFFF740000L) != 0L) {
/*      */           
/*   66 */           this.jjmatchedKind = 12;
/*   67 */           this.jjmatchedPos = 1;
/*   68 */           return 1;
/*      */         } 
/*   70 */         return -1;
/*      */       case 2:
/*   72 */         if ((active0 & 0x1FFFFF740000L) != 0L) {
/*      */           
/*   74 */           this.jjmatchedKind = 12;
/*   75 */           this.jjmatchedPos = 2;
/*   76 */           return 1;
/*      */         } 
/*   78 */         return -1;
/*      */       case 3:
/*   80 */         if ((active0 & 0x4100000000L) != 0L) {
/*      */           
/*   82 */           if (this.jjmatchedPos < 2) {
/*      */             
/*   84 */             this.jjmatchedKind = 12;
/*   85 */             this.jjmatchedPos = 2;
/*      */           } 
/*   87 */           return -1;
/*      */         } 
/*   89 */         if ((active0 & 0x1FBEFF740000L) != 0L) {
/*      */           
/*   91 */           this.jjmatchedKind = 12;
/*   92 */           this.jjmatchedPos = 3;
/*   93 */           return 1;
/*      */         } 
/*   95 */         return -1;
/*      */       case 4:
/*   97 */         if ((active0 & 0x4100000000L) != 0L) {
/*      */           
/*   99 */           if (this.jjmatchedPos < 2) {
/*      */             
/*  101 */             this.jjmatchedKind = 12;
/*  102 */             this.jjmatchedPos = 2;
/*      */           } 
/*  104 */           return -1;
/*      */         } 
/*  106 */         if ((active0 & 0x400000L) != 0L) {
/*      */           
/*  108 */           if (this.jjmatchedPos < 3) {
/*      */             
/*  110 */             this.jjmatchedKind = 12;
/*  111 */             this.jjmatchedPos = 3;
/*      */           } 
/*  113 */           return -1;
/*      */         } 
/*  115 */         if ((active0 & 0x1FBEFF340000L) != 0L) {
/*      */           
/*  117 */           this.jjmatchedKind = 12;
/*  118 */           this.jjmatchedPos = 4;
/*  119 */           return 1;
/*      */         } 
/*  121 */         return -1;
/*      */       case 5:
/*  123 */         if ((active0 & 0x4000000000L) != 0L) {
/*      */           
/*  125 */           if (this.jjmatchedPos < 2) {
/*      */             
/*  127 */             this.jjmatchedKind = 12;
/*  128 */             this.jjmatchedPos = 2;
/*      */           } 
/*  130 */           return -1;
/*      */         } 
/*  132 */         if ((active0 & 0x33C50000000L) != 0L) {
/*      */           
/*  134 */           if (this.jjmatchedPos < 4) {
/*      */             
/*  136 */             this.jjmatchedKind = 12;
/*  137 */             this.jjmatchedPos = 4;
/*      */           } 
/*  139 */           return -1;
/*      */         } 
/*  141 */         if ((active0 & 0x400000L) != 0L) {
/*      */           
/*  143 */           if (this.jjmatchedPos < 3) {
/*      */             
/*  145 */             this.jjmatchedKind = 12;
/*  146 */             this.jjmatchedPos = 3;
/*      */           } 
/*  148 */           return -1;
/*      */         } 
/*  150 */         if ((active0 & 0x1C82AF340000L) != 0L) {
/*      */           
/*  152 */           this.jjmatchedKind = 12;
/*  153 */           this.jjmatchedPos = 5;
/*  154 */           return 1;
/*      */         } 
/*  156 */         return -1;
/*      */       case 6:
/*  158 */         if ((active0 & 0x33C50000000L) != 0L) {
/*      */           
/*  160 */           if (this.jjmatchedPos < 4) {
/*      */             
/*  162 */             this.jjmatchedKind = 12;
/*  163 */             this.jjmatchedPos = 4;
/*      */           } 
/*  165 */           return -1;
/*      */         } 
/*  167 */         if ((active0 & 0x1C82AF340000L) != 0L) {
/*      */           
/*  169 */           if (this.jjmatchedPos != 6) {
/*      */             
/*  171 */             this.jjmatchedKind = 12;
/*  172 */             this.jjmatchedPos = 6;
/*      */           } 
/*  174 */           return 1;
/*      */         } 
/*  176 */         return -1;
/*      */       case 7:
/*  178 */         if ((active0 & 0x100000L) != 0L) {
/*      */           
/*  180 */           if (this.jjmatchedPos < 6) {
/*      */             
/*  182 */             this.jjmatchedKind = 12;
/*  183 */             this.jjmatchedPos = 6;
/*      */           } 
/*  185 */           return -1;
/*      */         } 
/*  187 */         if ((active0 & 0x13C00000000L) != 0L) {
/*      */           
/*  189 */           if (this.jjmatchedPos < 4) {
/*      */             
/*  191 */             this.jjmatchedKind = 12;
/*  192 */             this.jjmatchedPos = 4;
/*      */           } 
/*  194 */           return -1;
/*      */         } 
/*  196 */         if ((active0 & 0x1C82AF240000L) != 0L) {
/*      */           
/*  198 */           this.jjmatchedKind = 12;
/*  199 */           this.jjmatchedPos = 7;
/*  200 */           return 1;
/*      */         } 
/*  202 */         return -1;
/*      */       case 8:
/*  204 */         if ((active0 & 0x480AA240000L) != 0L) {
/*      */           
/*  206 */           this.jjmatchedKind = 12;
/*  207 */           this.jjmatchedPos = 8;
/*  208 */           return 1;
/*      */         } 
/*  210 */         if ((active0 & 0x180205000000L) != 0L) {
/*      */           
/*  212 */           if (this.jjmatchedPos < 7) {
/*      */             
/*  214 */             this.jjmatchedKind = 12;
/*  215 */             this.jjmatchedPos = 7;
/*      */           } 
/*  217 */           return -1;
/*      */         } 
/*  219 */         if ((active0 & 0x100000L) != 0L) {
/*      */           
/*  221 */           if (this.jjmatchedPos < 6) {
/*      */             
/*  223 */             this.jjmatchedKind = 12;
/*  224 */             this.jjmatchedPos = 6;
/*      */           } 
/*  226 */           return -1;
/*      */         } 
/*  228 */         if ((active0 & 0x1C00000000L) != 0L) {
/*      */           
/*  230 */           if (this.jjmatchedPos < 4) {
/*      */             
/*  232 */             this.jjmatchedKind = 12;
/*  233 */             this.jjmatchedPos = 4;
/*      */           } 
/*  235 */           return -1;
/*      */         } 
/*  237 */         return -1;
/*      */       case 9:
/*  239 */         if ((active0 & 0x80AA200000L) != 0L) {
/*      */           
/*  241 */           if (this.jjmatchedPos != 9) {
/*      */             
/*  243 */             this.jjmatchedKind = 12;
/*  244 */             this.jjmatchedPos = 9;
/*      */           } 
/*  246 */           return 1;
/*      */         } 
/*  248 */         if ((active0 & 0x180205000000L) != 0L) {
/*      */           
/*  250 */           if (this.jjmatchedPos < 7) {
/*      */             
/*  252 */             this.jjmatchedKind = 12;
/*  253 */             this.jjmatchedPos = 7;
/*      */           } 
/*  255 */           return -1;
/*      */         } 
/*  257 */         if ((active0 & 0x40000040000L) != 0L) {
/*      */           
/*  259 */           if (this.jjmatchedPos < 8) {
/*      */             
/*  261 */             this.jjmatchedKind = 12;
/*  262 */             this.jjmatchedPos = 8;
/*      */           } 
/*  264 */           return -1;
/*      */         } 
/*  266 */         if ((active0 & 0x1C00000000L) != 0L) {
/*      */           
/*  268 */           if (this.jjmatchedPos < 4) {
/*      */             
/*  270 */             this.jjmatchedKind = 12;
/*  271 */             this.jjmatchedPos = 4;
/*      */           } 
/*  273 */           return -1;
/*      */         } 
/*  275 */         return -1;
/*      */       case 10:
/*  277 */         if ((active0 & 0x100000000000L) != 0L) {
/*      */           
/*  279 */           if (this.jjmatchedPos < 7) {
/*      */             
/*  281 */             this.jjmatchedKind = 12;
/*  282 */             this.jjmatchedPos = 7;
/*      */           } 
/*  284 */           return -1;
/*      */         } 
/*  286 */         if ((active0 & 0x8000000L) != 0L) {
/*      */           
/*  288 */           if (this.jjmatchedPos < 9) {
/*      */             
/*  290 */             this.jjmatchedKind = 12;
/*  291 */             this.jjmatchedPos = 9;
/*      */           } 
/*  293 */           return -1;
/*      */         } 
/*  295 */         if ((active0 & 0x40000040000L) != 0L) {
/*      */           
/*  297 */           if (this.jjmatchedPos < 8) {
/*      */             
/*  299 */             this.jjmatchedKind = 12;
/*  300 */             this.jjmatchedPos = 8;
/*      */           } 
/*  302 */           return -1;
/*      */         } 
/*  304 */         if ((active0 & 0x80A2200000L) != 0L) {
/*      */           
/*  306 */           this.jjmatchedKind = 12;
/*  307 */           this.jjmatchedPos = 10;
/*  308 */           return 1;
/*      */         } 
/*  310 */         if ((active0 & 0xC00000000L) != 0L) {
/*      */           
/*  312 */           if (this.jjmatchedPos < 4) {
/*      */             
/*  314 */             this.jjmatchedKind = 12;
/*  315 */             this.jjmatchedPos = 4;
/*      */           } 
/*  317 */           return -1;
/*      */         } 
/*  319 */         return -1;
/*      */       case 11:
/*  321 */         if ((active0 & 0x40000000000L) != 0L) {
/*      */           
/*  323 */           if (this.jjmatchedPos < 8) {
/*      */             
/*  325 */             this.jjmatchedKind = 12;
/*  326 */             this.jjmatchedPos = 8;
/*      */           } 
/*  328 */           return -1;
/*      */         } 
/*  330 */         if ((active0 & 0x8000000L) != 0L) {
/*      */           
/*  332 */           if (this.jjmatchedPos < 9) {
/*      */             
/*  334 */             this.jjmatchedKind = 12;
/*  335 */             this.jjmatchedPos = 9;
/*      */           } 
/*  337 */           return -1;
/*      */         } 
/*  339 */         if ((active0 & 0xC00000000L) != 0L) {
/*      */           
/*  341 */           if (this.jjmatchedPos < 4) {
/*      */             
/*  343 */             this.jjmatchedKind = 12;
/*  344 */             this.jjmatchedPos = 4;
/*      */           } 
/*  346 */           return -1;
/*      */         } 
/*  348 */         if ((active0 & 0x80A2200000L) != 0L) {
/*      */           
/*  350 */           this.jjmatchedKind = 12;
/*  351 */           this.jjmatchedPos = 11;
/*  352 */           return 1;
/*      */         } 
/*  354 */         return -1;
/*      */       case 12:
/*  356 */         if ((active0 & 0x8000000000L) != 0L) {
/*      */           
/*  358 */           if (this.jjmatchedPos < 11) {
/*      */             
/*  360 */             this.jjmatchedKind = 12;
/*  361 */             this.jjmatchedPos = 11;
/*      */           } 
/*  363 */           return -1;
/*      */         } 
/*  365 */         if ((active0 & 0xC00000000L) != 0L) {
/*      */           
/*  367 */           if (this.jjmatchedPos < 4) {
/*      */             
/*  369 */             this.jjmatchedKind = 12;
/*  370 */             this.jjmatchedPos = 4;
/*      */           } 
/*  372 */           return -1;
/*      */         } 
/*  374 */         if ((active0 & 0xA2200000L) != 0L) {
/*      */           
/*  376 */           this.jjmatchedKind = 12;
/*  377 */           this.jjmatchedPos = 12;
/*  378 */           return 1;
/*      */         } 
/*  380 */         return -1;
/*      */       case 13:
/*  382 */         if ((active0 & 0x8000000000L) != 0L) {
/*      */           
/*  384 */           if (this.jjmatchedPos < 11) {
/*      */             
/*  386 */             this.jjmatchedKind = 12;
/*  387 */             this.jjmatchedPos = 11;
/*      */           } 
/*  389 */           return -1;
/*      */         } 
/*  391 */         if ((active0 & 0x2000000L) != 0L) {
/*      */           
/*  393 */           if (this.jjmatchedPos < 12) {
/*      */             
/*  395 */             this.jjmatchedKind = 12;
/*  396 */             this.jjmatchedPos = 12;
/*      */           } 
/*  398 */           return -1;
/*      */         } 
/*  400 */         if ((active0 & 0x400000000L) != 0L) {
/*      */           
/*  402 */           if (this.jjmatchedPos < 4) {
/*      */             
/*  404 */             this.jjmatchedKind = 12;
/*  405 */             this.jjmatchedPos = 4;
/*      */           } 
/*  407 */           return -1;
/*      */         } 
/*  409 */         if ((active0 & 0xA0200000L) != 0L) {
/*      */           
/*  411 */           this.jjmatchedKind = 12;
/*  412 */           this.jjmatchedPos = 13;
/*  413 */           return 1;
/*      */         } 
/*  415 */         return -1;
/*      */       case 14:
/*  417 */         if ((active0 & 0x8000000000L) != 0L) {
/*      */           
/*  419 */           if (this.jjmatchedPos < 11) {
/*      */             
/*  421 */             this.jjmatchedKind = 12;
/*  422 */             this.jjmatchedPos = 11;
/*      */           } 
/*  424 */           return -1;
/*      */         } 
/*  426 */         if ((active0 & 0x20000000L) != 0L) {
/*      */           
/*  428 */           if (this.jjmatchedPos < 13) {
/*      */             
/*  430 */             this.jjmatchedKind = 12;
/*  431 */             this.jjmatchedPos = 13;
/*      */           } 
/*  433 */           return -1;
/*      */         } 
/*  435 */         if ((active0 & 0x2000000L) != 0L) {
/*      */           
/*  437 */           if (this.jjmatchedPos < 12) {
/*      */             
/*  439 */             this.jjmatchedKind = 12;
/*  440 */             this.jjmatchedPos = 12;
/*      */           } 
/*  442 */           return -1;
/*      */         } 
/*  444 */         if ((active0 & 0x400000000L) != 0L) {
/*      */           
/*  446 */           if (this.jjmatchedPos < 4) {
/*      */             
/*  448 */             this.jjmatchedKind = 12;
/*  449 */             this.jjmatchedPos = 4;
/*      */           } 
/*  451 */           return -1;
/*      */         } 
/*  453 */         if ((active0 & 0x80200000L) != 0L) {
/*      */           
/*  455 */           this.jjmatchedKind = 12;
/*  456 */           this.jjmatchedPos = 14;
/*  457 */           return 1;
/*      */         } 
/*  459 */         return -1;
/*      */       case 15:
/*  461 */         if ((active0 & 0x20000000L) != 0L) {
/*      */           
/*  463 */           if (this.jjmatchedPos < 13) {
/*      */             
/*  465 */             this.jjmatchedKind = 12;
/*  466 */             this.jjmatchedPos = 13;
/*      */           } 
/*  468 */           return -1;
/*      */         } 
/*  470 */         if ((active0 & 0x80200000L) != 0L) {
/*      */           
/*  472 */           this.jjmatchedKind = 12;
/*  473 */           this.jjmatchedPos = 15;
/*  474 */           return 1;
/*      */         } 
/*  476 */         return -1;
/*      */       case 16:
/*  478 */         if ((active0 & 0x80200000L) != 0L) {
/*      */           
/*  480 */           this.jjmatchedKind = 12;
/*  481 */           this.jjmatchedPos = 16;
/*  482 */           return 1;
/*      */         } 
/*  484 */         return -1;
/*      */       case 17:
/*  486 */         if ((active0 & 0x80200000L) != 0L) {
/*      */           
/*  488 */           if (this.jjmatchedPos < 16) {
/*      */             
/*  490 */             this.jjmatchedKind = 12;
/*  491 */             this.jjmatchedPos = 16;
/*      */           } 
/*  493 */           return -1;
/*      */         } 
/*  495 */         return -1;
/*      */     } 
/*  497 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   private final int jjStartNfa_0(int pos, long active0) {
/*  502 */     return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
/*      */   }
/*      */   
/*      */   private final int jjStopAtPos(int pos, int kind) {
/*  506 */     this.jjmatchedKind = kind;
/*  507 */     this.jjmatchedPos = pos;
/*  508 */     return pos + 1;
/*      */   }
/*      */   
/*      */   private final int jjStartNfaWithStates_0(int pos, int kind, int state) {
/*  512 */     this.jjmatchedKind = kind;
/*  513 */     this.jjmatchedPos = pos; 
/*  514 */     try { this.curChar = this.input_stream.readChar(); }
/*  515 */     catch (IOException e) { return pos + 1; }
/*  516 */      return jjMoveNfa_0(state, pos + 1);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa0_0() {
/*  520 */     switch (this.curChar) {
/*      */       
/*      */       case '*':
/*  523 */         return jjStopAtPos(0, 45);
/*      */       case '/':
/*  525 */         this.jjmatchedKind = 16;
/*  526 */         return jjMoveStringLiteralDfa1_0(131072L);
/*      */       case '0':
/*  528 */         return jjStopAtPos(0, 46);
/*      */       case ':':
/*  530 */         return jjStopAtPos(0, 15);
/*      */       case '@':
/*  532 */         return jjStopAtPos(0, 19);
/*      */       case 'a':
/*  534 */         return jjMoveStringLiteralDfa1_0(825170853888L);
/*      */       case 'b':
/*  536 */         return jjMoveStringLiteralDfa1_0(16777216L);
/*      */       case 'c':
/*  538 */         return jjMoveStringLiteralDfa1_0(4398046511104L);
/*      */       case 'e':
/*  540 */         return jjMoveStringLiteralDfa1_0(1048576L);
/*      */       case 'f':
/*  542 */         return jjMoveStringLiteralDfa1_0(3298534883328L);
/*      */       case 'g':
/*  544 */         return jjMoveStringLiteralDfa1_0(1073741824L);
/*      */       case 'i':
/*  546 */         return jjMoveStringLiteralDfa1_0(2214592512L);
/*      */       case 'k':
/*  548 */         return jjMoveStringLiteralDfa1_0(4294967296L);
/*      */       case 'm':
/*  550 */         return jjMoveStringLiteralDfa1_0(257832255488L);
/*      */       case 'n':
/*  552 */         return jjMoveStringLiteralDfa1_0(8589934592L);
/*      */       case 'p':
/*  554 */         return jjMoveStringLiteralDfa1_0(33554432L);
/*      */       case 's':
/*  556 */         return jjMoveStringLiteralDfa1_0(270532608L);
/*      */       case 't':
/*  558 */         return jjMoveStringLiteralDfa1_0(4194304L);
/*      */       case 'x':
/*  560 */         return jjMoveStringLiteralDfa1_0(26388279066624L);
/*      */       case '~':
/*  562 */         return jjStopAtPos(0, 23);
/*      */     } 
/*  564 */     return jjMoveNfa_0(0, 0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa1_0(long active0) {
/*      */     try {
/*  569 */       this.curChar = this.input_stream.readChar();
/*  570 */     } catch (IOException e) {
/*  571 */       jjStopStringLiteralDfa_0(0, active0);
/*  572 */       return 1;
/*      */     } 
/*  574 */     switch (this.curChar) {
/*      */       
/*      */       case '-':
/*  577 */         return jjMoveStringLiteralDfa2_0(active0, 26388279066624L);
/*      */       case '/':
/*  579 */         if ((active0 & 0x20000L) != 0L)
/*  580 */           return jjStopAtPos(1, 17); 
/*      */         break;
/*      */       case 'a':
/*  583 */         return jjMoveStringLiteralDfa2_0(active0, 3298551660544L);
/*      */       case 'c':
/*  585 */         return jjMoveStringLiteralDfa2_0(active0, 268435456L);
/*      */       case 'd':
/*  587 */         return jjMoveStringLiteralDfa2_0(active0, 2147483648L);
/*      */       case 'e':
/*  589 */         return jjMoveStringLiteralDfa2_0(active0, 4429185024L);
/*      */       case 'l':
/*  591 */         return jjMoveStringLiteralDfa2_0(active0, 1048576L);
/*      */       case 'n':
/*  593 */         return jjMoveStringLiteralDfa2_0(active0, 824633720832L);
/*      */       case 'o':
/*  595 */         return jjMoveStringLiteralDfa2_0(active0, 4664334483456L);
/*      */       case 'r':
/*  597 */         return jjMoveStringLiteralDfa2_0(active0, 1107296256L);
/*      */       case 't':
/*  599 */         return jjMoveStringLiteralDfa2_0(active0, 604241920L);
/*      */       case 'u':
/*  601 */         return jjMoveStringLiteralDfa2_0(active0, 2097152L);
/*      */       case 'y':
/*  603 */         return jjMoveStringLiteralDfa2_0(active0, 4194304L);
/*      */     } 
/*      */ 
/*      */     
/*  607 */     return jjStartNfa_0(0, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa2_0(long old0, long active0) {
/*  611 */     if ((active0 &= old0) == 0L)
/*  612 */       return jjStartNfa_0(0, old0);  try {
/*  613 */       this.curChar = this.input_stream.readChar();
/*  614 */     } catch (IOException e) {
/*  615 */       jjStopStringLiteralDfa_0(1, active0);
/*  616 */       return 2;
/*      */     } 
/*  618 */     switch (this.curChar) {
/*      */       
/*      */       case 'b':
/*  621 */         return jjMoveStringLiteralDfa3_0(active0, 2097152L);
/*      */       case 'c':
/*  623 */         return jjMoveStringLiteralDfa3_0(active0, 3298534883328L);
/*      */       case 'd':
/*  625 */         return jjMoveStringLiteralDfa3_0(active0, 257698037760L);
/*      */       case 'e':
/*  627 */         return jjMoveStringLiteralDfa3_0(active0, 2215641088L);
/*      */       case 'i':
/*  629 */         return jjMoveStringLiteralDfa3_0(active0, 33554432L);
/*      */       case 'm':
/*  631 */         return jjMoveStringLiteralDfa3_0(active0, 4398180728832L);
/*      */       case 'o':
/*  633 */         return jjMoveStringLiteralDfa3_0(active0, 1342177280L);
/*      */       case 'p':
/*  635 */         return jjMoveStringLiteralDfa3_0(active0, 4194304L);
/*      */       case 's':
/*  637 */         return jjMoveStringLiteralDfa3_0(active0, 26388295843840L);
/*      */       case 't':
/*  639 */         return jjMoveStringLiteralDfa3_0(active0, 9127067648L);
/*      */       case 'y':
/*  641 */         return jjMoveStringLiteralDfa3_0(active0, 828928688128L);
/*      */     } 
/*      */ 
/*      */     
/*  645 */     return jjStartNfa_0(1, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa3_0(long old0, long active0) {
/*  649 */     if ((active0 &= old0) == 0L)
/*  650 */       return jjStartNfa_0(1, old0);  try {
/*  651 */       this.curChar = this.input_stream.readChar();
/*  652 */     } catch (IOException e) {
/*  653 */       jjStopStringLiteralDfa_0(2, active0);
/*  654 */       return 3;
/*      */     } 
/*  656 */     switch (this.curChar) {
/*      */       
/*      */       case ':':
/*  659 */         return jjMoveStringLiteralDfa4_0(active0, 279172874240L);
/*      */       case 'A':
/*  661 */         return jjMoveStringLiteralDfa4_0(active0, 549755813888L);
/*      */       case 'a':
/*  663 */         return jjMoveStringLiteralDfa4_0(active0, 8589934592L);
/*      */       case 'b':
/*  665 */         return jjMoveStringLiteralDfa4_0(active0, 134217728L);
/*      */       case 'c':
/*  667 */         return jjMoveStringLiteralDfa4_0(active0, 26388279066624L);
/*      */       case 'e':
/*  669 */         return jjMoveStringLiteralDfa4_0(active0, 3556253892608L);
/*      */       case 'm':
/*  671 */         return jjMoveStringLiteralDfa4_0(active0, 101711872L);
/*      */       case 'n':
/*  673 */         return jjMoveStringLiteralDfa4_0(active0, 2147483648L);
/*      */       case 'p':
/*  675 */         return jjMoveStringLiteralDfa4_0(active0, 4398314946560L);
/*      */       case 'r':
/*  677 */         return jjMoveStringLiteralDfa4_0(active0, 537133056L);
/*      */       case 's':
/*  679 */         return jjMoveStringLiteralDfa4_0(active0, 2097152L);
/*      */       case 'u':
/*  681 */         return jjMoveStringLiteralDfa4_0(active0, 1073741824L);
/*      */     } 
/*      */ 
/*      */     
/*  685 */     return jjStartNfa_0(2, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa4_0(long old0, long active0) {
/*  689 */     if ((active0 &= old0) == 0L)
/*  690 */       return jjStartNfa_0(2, old0);  try {
/*  691 */       this.curChar = this.input_stream.readChar();
/*  692 */     } catch (IOException e) {
/*  693 */       jjStopStringLiteralDfa_0(3, active0);
/*  694 */       return 4;
/*      */     } 
/*  696 */     switch (this.curChar) {
/*      */       
/*      */       case ':':
/*  699 */         if ((active0 & 0x100000000L) != 0L)
/*  700 */           return jjStopAtPos(4, 32); 
/*  701 */         return jjMoveStringLiteralDfa5_0(active0, 274882101248L);
/*      */       case 'T':
/*  703 */         return jjMoveStringLiteralDfa5_0(active0, 83886080L);
/*      */       case 'e':
/*  705 */         return jjMoveStringLiteralDfa5_0(active0, 403701760L);
/*      */       case 'h':
/*  707 */         return jjMoveStringLiteralDfa5_0(active0, 26388279066624L);
/*      */       case 'i':
/*  709 */         return jjMoveStringLiteralDfa5_0(active0, 570687488L);
/*      */       case 'l':
/*  711 */         return jjMoveStringLiteralDfa5_0(active0, 257698037760L);
/*      */       case 'o':
/*  713 */         return jjMoveStringLiteralDfa5_0(active0, 4398046511104L);
/*      */       case 'p':
/*  715 */         return jjMoveStringLiteralDfa5_0(active0, 1073741824L);
/*      */       case 't':
/*  717 */         return jjMoveStringLiteralDfa5_0(active0, 3859030212608L);
/*      */     } 
/*      */ 
/*      */     
/*  721 */     return jjStartNfa_0(3, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa5_0(long old0, long active0) {
/*  725 */     if ((active0 &= old0) == 0L)
/*  726 */       return jjStartNfa_0(3, old0);  try {
/*  727 */       this.curChar = this.input_stream.readChar();
/*  728 */     } catch (IOException e) {
/*  729 */       jjStopStringLiteralDfa_0(4, active0);
/*  730 */       return 5;
/*      */     } 
/*  732 */     switch (this.curChar) {
/*      */       
/*      */       case '*':
/*  735 */         if ((active0 & 0x4000000000L) != 0L)
/*  736 */           return jjStopAtPos(5, 38); 
/*      */         break;
/*      */       case ':':
/*  739 */         if ((active0 & 0x400000L) != 0L)
/*  740 */           return jjStopAtPos(5, 22); 
/*  741 */         return jjMoveStringLiteralDfa6_0(active0, 3557575098368L);
/*      */       case 'b':
/*  743 */         return jjMoveStringLiteralDfa6_0(active0, 537133056L);
/*      */       case 'e':
/*  745 */         return jjMoveStringLiteralDfa6_0(active0, 26388279066624L);
/*      */       case 'i':
/*  747 */         return jjMoveStringLiteralDfa6_0(active0, 10739515392L);
/*      */       case 'n':
/*  749 */         return jjMoveStringLiteralDfa6_0(active0, 4398047559680L);
/*      */       case 'r':
/*  751 */         return jjMoveStringLiteralDfa6_0(active0, 134217728L);
/*      */       case 't':
/*  753 */         return jjMoveStringLiteralDfa6_0(active0, 549789368320L);
/*      */       case 'y':
/*  755 */         return jjMoveStringLiteralDfa6_0(active0, 83886080L);
/*      */     } 
/*      */ 
/*      */     
/*  759 */     return jjStartNfa_0(4, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa6_0(long old0, long active0) {
/*  763 */     if ((active0 &= old0) == 0L)
/*  764 */       return jjStartNfa_0(4, old0);  try {
/*  765 */       this.curChar = this.input_stream.readChar();
/*  766 */     } catch (IOException e) {
/*  767 */       jjStopStringLiteralDfa_0(5, active0);
/*  768 */       return 6;
/*      */     } 
/*  770 */     switch (this.curChar) {
/*      */       
/*      */       case ':':
/*  773 */         if ((active0 & 0x10000000L) != 0L)
/*  774 */           return jjStopAtPos(6, 28); 
/*  775 */         if ((active0 & 0x40000000L) != 0L)
/*  776 */           return jjStopAtPos(6, 30); 
/*  777 */         if ((active0 & 0x20000000000L) != 0L) {
/*      */           
/*  779 */           this.jjmatchedKind = 41;
/*  780 */           this.jjmatchedPos = 6;
/*      */         } 
/*  782 */         return jjMoveStringLiteralDfa7_0(active0, 1357209665536L);
/*      */       case 'T':
/*  784 */         return jjMoveStringLiteralDfa7_0(active0, 134217728L);
/*      */       case 'e':
/*  786 */         return jjMoveStringLiteralDfa7_0(active0, 4398046511104L);
/*      */       case 'i':
/*  788 */         return jjMoveStringLiteralDfa7_0(active0, 33554432L);
/*      */       case 'm':
/*  790 */         return jjMoveStringLiteralDfa7_0(active0, 26388279066624L);
/*      */       case 'o':
/*  792 */         return jjMoveStringLiteralDfa7_0(active0, 8589934592L);
/*      */       case 'p':
/*  794 */         return jjMoveStringLiteralDfa7_0(active0, 83886080L);
/*      */       case 'r':
/*  796 */         return jjMoveStringLiteralDfa7_0(active0, 549755813888L);
/*      */       case 't':
/*  798 */         return jjMoveStringLiteralDfa7_0(active0, 2150629376L);
/*      */       case 'u':
/*  800 */         return jjMoveStringLiteralDfa7_0(active0, 537133056L);
/*      */     } 
/*      */ 
/*      */     
/*  804 */     return jjStartNfa_0(5, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa7_0(long old0, long active0) {
/*  808 */     if ((active0 &= old0) == 0L)
/*  809 */       return jjStartNfa_0(5, old0);  try {
/*  810 */       this.curChar = this.input_stream.readChar();
/*  811 */     } catch (IOException e) {
/*  812 */       jjStopStringLiteralDfa_0(6, active0);
/*  813 */       return 7;
/*      */     } 
/*  815 */     switch (this.curChar) {
/*      */       
/*      */       case '*':
/*  818 */         if ((active0 & 0x2000000000L) != 0L)
/*  819 */           return jjStopAtPos(7, 37); 
/*  820 */         if ((active0 & 0x10000000000L) != 0L)
/*  821 */           return jjStopAtPos(7, 40); 
/*      */         break;
/*      */       case ':':
/*  824 */         return jjMoveStringLiteralDfa8_0(active0, 1048576L);
/*      */       case 'a':
/*  826 */         return jjMoveStringLiteralDfa8_0(active0, 26456998543360L);
/*      */       case 'c':
/*  828 */         return jjMoveStringLiteralDfa8_0(active0, 34359738368L);
/*      */       case 'e':
/*  830 */         return jjMoveStringLiteralDfa8_0(active0, 83886080L);
/*      */       case 'i':
/*  832 */         return jjMoveStringLiteralDfa8_0(active0, 549755813888L);
/*      */       case 'n':
/*  834 */         return jjMoveStringLiteralDfa8_0(active0, 4406636445696L);
/*      */       case 's':
/*  836 */         return jjMoveStringLiteralDfa8_0(active0, 17179869184L);
/*      */       case 't':
/*  838 */         return jjMoveStringLiteralDfa8_0(active0, 537133056L);
/*      */       case 'u':
/*  840 */         return jjMoveStringLiteralDfa8_0(active0, 2097152L);
/*      */       case 'v':
/*  842 */         return jjMoveStringLiteralDfa8_0(active0, 33554432L);
/*      */       case 'y':
/*  844 */         return jjMoveStringLiteralDfa8_0(active0, 2281701376L);
/*      */     } 
/*      */ 
/*      */     
/*  848 */     return jjStartNfa_0(6, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa8_0(long old0, long active0) {
/*  852 */     if ((active0 &= old0) == 0L)
/*  853 */       return jjStartNfa_0(6, old0);  try {
/*  854 */       this.curChar = this.input_stream.readChar();
/*  855 */     } catch (IOException e) {
/*  856 */       jjStopStringLiteralDfa_0(7, active0);
/*  857 */       return 8;
/*      */     } 
/*  859 */     switch (this.curChar) {
/*      */       
/*      */       case ':':
/*  862 */         if ((active0 & 0x100000L) != 0L)
/*  863 */           return jjStopAtPos(8, 20); 
/*  864 */         return jjMoveStringLiteralDfa9_0(active0, 26396952887296L);
/*      */       case 'C':
/*  866 */         return jjMoveStringLiteralDfa9_0(active0, 2147483648L);
/*      */       case 'b':
/*  868 */         return jjMoveStringLiteralDfa9_0(active0, 549755813888L);
/*      */       case 'e':
/*  870 */         return jjMoveStringLiteralDfa9_0(active0, 17750556672L);
/*      */       case 'h':
/*  872 */         return jjMoveStringLiteralDfa9_0(active0, 34359738368L);
/*      */       case 'l':
/*  874 */         return jjMoveStringLiteralDfa9_0(active0, 68719476736L);
/*      */       case 'p':
/*  876 */         return jjMoveStringLiteralDfa9_0(active0, 134217728L);
/*      */       case 't':
/*  878 */         return jjMoveStringLiteralDfa9_0(active0, 4398048608256L);
/*      */     } 
/*      */ 
/*      */     
/*  882 */     return jjStartNfa_0(7, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa9_0(long old0, long active0) {
/*  886 */     if ((active0 &= old0) == 0L)
/*  887 */       return jjStartNfa_0(7, old0);  try {
/*  888 */       this.curChar = this.input_stream.readChar();
/*  889 */     } catch (IOException e) {
/*  890 */       jjStopStringLiteralDfa_0(8, active0);
/*  891 */       return 9;
/*      */     } 
/*  893 */     switch (this.curChar) {
/*      */       
/*      */       case ':':
/*  896 */         if ((active0 & 0x1000000L) != 0L)
/*  897 */           return jjStopAtPos(9, 24); 
/*  898 */         if ((active0 & 0x4000000L) != 0L)
/*  899 */           return jjStopAtPos(9, 26); 
/*  900 */         if ((active0 & 0x200000000L) != 0L)
/*  901 */           return jjStopAtPos(9, 33); 
/*  902 */         if ((active0 & 0x80000000000L) != 0L) {
/*      */           
/*  904 */           this.jjmatchedKind = 43;
/*  905 */           this.jjmatchedPos = 9;
/*      */         } 
/*  907 */         return jjMoveStringLiteralDfa10_0(active0, 21990232817664L);
/*      */       case 'G':
/*  909 */         return jjMoveStringLiteralDfa10_0(active0, 536870912L);
/*      */       case 'T':
/*  911 */         return jjMoveStringLiteralDfa10_0(active0, 33554432L);
/*      */       case 'e':
/*  913 */         return jjMoveStringLiteralDfa10_0(active0, 134217728L);
/*      */       case 'i':
/*  915 */         return jjMoveStringLiteralDfa10_0(active0, 2097152L);
/*      */       case 'l':
/*  917 */         if ((active0 & 0x1000000000L) != 0L)
/*  918 */           return jjStopAtPos(9, 36); 
/*      */         break;
/*      */       case 'o':
/*  921 */         return jjMoveStringLiteralDfa10_0(active0, 36507222016L);
/*      */       case 'q':
/*  923 */         return jjMoveStringLiteralDfa10_0(active0, 17179869184L);
/*      */       case 'u':
/*  925 */         return jjMoveStringLiteralDfa10_0(active0, 549755813888L);
/*      */     } 
/*      */ 
/*      */     
/*  929 */     return jjStartNfa_0(8, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa10_0(long old0, long active0) {
/*  933 */     if ((active0 &= old0) == 0L)
/*  934 */       return jjStartNfa_0(8, old0);  try {
/*  935 */       this.curChar = this.input_stream.readChar();
/*  936 */     } catch (IOException e) {
/*  937 */       jjStopStringLiteralDfa_0(9, active0);
/*  938 */       return 10;
/*      */     } 
/*  940 */     switch (this.curChar) {
/*      */       
/*      */       case '*':
/*  943 */         if ((active0 & 0x100000000000L) != 0L)
/*  944 */           return jjStopAtPos(10, 44); 
/*      */         break;
/*      */       case ':':
/*  947 */         if ((active0 & 0x40000L) != 0L)
/*  948 */           return jjStopAtPos(10, 18); 
/*  949 */         return jjMoveStringLiteralDfa11_0(active0, 4398180728832L);
/*      */       case 'i':
/*  951 */         return jjMoveStringLiteralDfa11_0(active0, 34359738368L);
/*      */       case 'n':
/*  953 */         return jjMoveStringLiteralDfa11_0(active0, 2147483648L);
/*      */       case 'o':
/*  955 */         return jjMoveStringLiteralDfa11_0(active0, 2097152L);
/*      */       case 'r':
/*  957 */         return jjMoveStringLiteralDfa11_0(active0, 536870912L);
/*      */       case 't':
/*  959 */         return jjMoveStringLiteralDfa11_0(active0, 549755813888L);
/*      */       case 'u':
/*  961 */         return jjMoveStringLiteralDfa11_0(active0, 17179869184L);
/*      */       case 'y':
/*  963 */         return jjMoveStringLiteralDfa11_0(active0, 33554432L);
/*      */     } 
/*      */ 
/*      */     
/*  967 */     return jjStartNfa_0(9, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa11_0(long old0, long active0) {
/*  971 */     if ((active0 &= old0) == 0L)
/*  972 */       return jjStartNfa_0(9, old0);  try {
/*  973 */       this.curChar = this.input_stream.readChar();
/*  974 */     } catch (IOException e) {
/*  975 */       jjStopStringLiteralDfa_0(10, active0);
/*  976 */       return 11;
/*      */     } 
/*  978 */     switch (this.curChar) {
/*      */       
/*      */       case '*':
/*  981 */         if ((active0 & 0x40000000000L) != 0L)
/*  982 */           return jjStopAtPos(11, 42); 
/*      */         break;
/*      */       case ':':
/*  985 */         if ((active0 & 0x8000000L) != 0L)
/*  986 */           return jjStopAtPos(11, 27); 
/*      */         break;
/*      */       case 'c':
/*  989 */         return jjMoveStringLiteralDfa12_0(active0, 34359738368L);
/*      */       case 'e':
/*  991 */         return jjMoveStringLiteralDfa12_0(active0, 566935683072L);
/*      */       case 'n':
/*  993 */         return jjMoveStringLiteralDfa12_0(active0, 2097152L);
/*      */       case 'o':
/*  995 */         return jjMoveStringLiteralDfa12_0(active0, 536870912L);
/*      */       case 'p':
/*  997 */         return jjMoveStringLiteralDfa12_0(active0, 33554432L);
/*      */       case 't':
/*  999 */         return jjMoveStringLiteralDfa12_0(active0, 2147483648L);
/*      */     } 
/*      */ 
/*      */     
/* 1003 */     return jjStartNfa_0(10, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa12_0(long old0, long active0) {
/* 1007 */     if ((active0 &= old0) == 0L)
/* 1008 */       return jjStartNfa_0(10, old0);  try {
/* 1009 */       this.curChar = this.input_stream.readChar();
/* 1010 */     } catch (IOException e) {
/* 1011 */       jjStopStringLiteralDfa_0(11, active0);
/* 1012 */       return 12;
/*      */     } 
/* 1014 */     switch (this.curChar) {
/*      */       
/*      */       case ':':
/* 1017 */         return jjMoveStringLiteralDfa13_0(active0, 549755813888L);
/*      */       case 'G':
/* 1019 */         return jjMoveStringLiteralDfa13_0(active0, 2097152L);
/*      */       case 'e':
/* 1021 */         if ((active0 & 0x800000000L) != 0L)
/* 1022 */           return jjStopAtPos(12, 35); 
/* 1023 */         return jjMoveStringLiteralDfa13_0(active0, 33554432L);
/*      */       case 'n':
/* 1025 */         return jjMoveStringLiteralDfa13_0(active0, 17179869184L);
/*      */       case 'r':
/* 1027 */         return jjMoveStringLiteralDfa13_0(active0, 2147483648L);
/*      */       case 'u':
/* 1029 */         return jjMoveStringLiteralDfa13_0(active0, 536870912L);
/*      */     } 
/*      */ 
/*      */     
/* 1033 */     return jjStartNfa_0(11, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa13_0(long old0, long active0) {
/* 1037 */     if ((active0 &= old0) == 0L)
/* 1038 */       return jjStartNfa_0(11, old0);  try {
/* 1039 */       this.curChar = this.input_stream.readChar();
/* 1040 */     } catch (IOException e) {
/* 1041 */       jjStopStringLiteralDfa_0(12, active0);
/* 1042 */       return 13;
/*      */     } 
/* 1044 */     switch (this.curChar) {
/*      */       
/*      */       case ':':
/* 1047 */         return jjMoveStringLiteralDfa14_0(active0, 549789368320L);
/*      */       case 'a':
/* 1049 */         return jjMoveStringLiteralDfa14_0(active0, 2147483648L);
/*      */       case 'c':
/* 1051 */         return jjMoveStringLiteralDfa14_0(active0, 17179869184L);
/*      */       case 'p':
/* 1053 */         return jjMoveStringLiteralDfa14_0(active0, 536870912L);
/*      */       case 'r':
/* 1055 */         return jjMoveStringLiteralDfa14_0(active0, 2097152L);
/*      */     } 
/*      */ 
/*      */     
/* 1059 */     return jjStartNfa_0(12, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa14_0(long old0, long active0) {
/* 1063 */     if ((active0 &= old0) == 0L)
/* 1064 */       return jjStartNfa_0(12, old0);  try {
/* 1065 */       this.curChar = this.input_stream.readChar();
/* 1066 */     } catch (IOException e) {
/* 1067 */       jjStopStringLiteralDfa_0(13, active0);
/* 1068 */       return 14;
/*      */     } 
/* 1070 */     switch (this.curChar) {
/*      */       
/*      */       case '*':
/* 1073 */         if ((active0 & 0x8000000000L) != 0L)
/* 1074 */           return jjStopAtPos(14, 39); 
/*      */         break;
/*      */       case ':':
/* 1077 */         if ((active0 & 0x2000000L) != 0L)
/* 1078 */           return jjStopAtPos(14, 25); 
/* 1079 */         return jjMoveStringLiteralDfa15_0(active0, 536870912L);
/*      */       case 'e':
/* 1081 */         if ((active0 & 0x400000000L) != 0L)
/* 1082 */           return jjStopAtPos(14, 34); 
/*      */         break;
/*      */       case 'i':
/* 1085 */         return jjMoveStringLiteralDfa15_0(active0, 2147483648L);
/*      */       case 'o':
/* 1087 */         return jjMoveStringLiteralDfa15_0(active0, 2097152L);
/*      */     } 
/*      */ 
/*      */     
/* 1091 */     return jjStartNfa_0(13, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa15_0(long old0, long active0) {
/* 1095 */     if ((active0 &= old0) == 0L)
/* 1096 */       return jjStartNfa_0(13, old0);  try {
/* 1097 */       this.curChar = this.input_stream.readChar();
/* 1098 */     } catch (IOException e) {
/* 1099 */       jjStopStringLiteralDfa_0(14, active0);
/* 1100 */       return 15;
/*      */     } 
/* 1102 */     switch (this.curChar) {
/*      */       
/*      */       case ':':
/* 1105 */         if ((active0 & 0x20000000L) != 0L)
/* 1106 */           return jjStopAtPos(15, 29); 
/*      */         break;
/*      */       case 'n':
/* 1109 */         return jjMoveStringLiteralDfa16_0(active0, 2147483648L);
/*      */       case 'u':
/* 1111 */         return jjMoveStringLiteralDfa16_0(active0, 2097152L);
/*      */     } 
/*      */ 
/*      */     
/* 1115 */     return jjStartNfa_0(14, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa16_0(long old0, long active0) {
/* 1119 */     if ((active0 &= old0) == 0L)
/* 1120 */       return jjStartNfa_0(14, old0);  try {
/* 1121 */       this.curChar = this.input_stream.readChar();
/* 1122 */     } catch (IOException e) {
/* 1123 */       jjStopStringLiteralDfa_0(15, active0);
/* 1124 */       return 16;
/*      */     } 
/* 1126 */     switch (this.curChar) {
/*      */       
/*      */       case 'p':
/* 1129 */         return jjMoveStringLiteralDfa17_0(active0, 2097152L);
/*      */       case 't':
/* 1131 */         return jjMoveStringLiteralDfa17_0(active0, 2147483648L);
/*      */     } 
/*      */ 
/*      */     
/* 1135 */     return jjStartNfa_0(15, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa17_0(long old0, long active0) {
/* 1139 */     if ((active0 &= old0) == 0L)
/* 1140 */       return jjStartNfa_0(15, old0);  try {
/* 1141 */       this.curChar = this.input_stream.readChar();
/* 1142 */     } catch (IOException e) {
/* 1143 */       jjStopStringLiteralDfa_0(16, active0);
/* 1144 */       return 17;
/*      */     } 
/* 1146 */     switch (this.curChar) {
/*      */       
/*      */       case ':':
/* 1149 */         return jjMoveStringLiteralDfa18_0(active0, 2149580800L);
/*      */     } 
/*      */ 
/*      */     
/* 1153 */     return jjStartNfa_0(16, active0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa18_0(long old0, long active0) {
/* 1157 */     if ((active0 &= old0) == 0L)
/* 1158 */       return jjStartNfa_0(16, old0);  try {
/* 1159 */       this.curChar = this.input_stream.readChar();
/* 1160 */     } catch (IOException e) {
/* 1161 */       jjStopStringLiteralDfa_0(17, active0);
/* 1162 */       return 18;
/*      */     } 
/* 1164 */     switch (this.curChar) {
/*      */       
/*      */       case ':':
/* 1167 */         if ((active0 & 0x200000L) != 0L)
/* 1168 */           return jjStopAtPos(18, 21); 
/* 1169 */         if ((active0 & 0x80000000L) != 0L) {
/* 1170 */           return jjStopAtPos(18, 31);
/*      */         }
/*      */         break;
/*      */     } 
/*      */     
/* 1175 */     return jjStartNfa_0(17, active0);
/*      */   }
/*      */   
/*      */   private final void jjCheckNAdd(int state) {
/* 1179 */     if (this.jjrounds[state] != this.jjround) {
/*      */       
/* 1181 */       this.jjstateSet[this.jjnewStateCnt++] = state;
/* 1182 */       this.jjrounds[state] = this.jjround;
/*      */     } 
/*      */   }
/*      */   
/*      */   private final void jjAddStates(int start, int end) {
/*      */     do {
/* 1188 */       this.jjstateSet[this.jjnewStateCnt++] = jjnextStates[start];
/* 1189 */     } while (start++ != end);
/*      */   }
/*      */   
/*      */   private final void jjCheckNAddTwoStates(int state1, int state2) {
/* 1193 */     jjCheckNAdd(state1);
/* 1194 */     jjCheckNAdd(state2);
/*      */   }
/*      */   
/*      */   private final void jjCheckNAddStates(int start, int end) {
/*      */     do {
/* 1199 */       jjCheckNAdd(jjnextStates[start]);
/* 1200 */     } while (start++ != end);
/*      */   }
/*      */   
/*      */   private final void jjCheckNAddStates(int start) {
/* 1204 */     jjCheckNAdd(jjnextStates[start]);
/* 1205 */     jjCheckNAdd(jjnextStates[start + 1]);
/*      */   }
/* 1207 */   static final long[] jjbitVec0 = new long[] { 0L, -16384L, -17590038560769L, 8388607L };
/*      */ 
/*      */   
/* 1210 */   static final long[] jjbitVec2 = new long[] { 0L, 0L, 0L, -36028797027352577L };
/*      */ 
/*      */   
/* 1213 */   static final long[] jjbitVec3 = new long[] { 9219994337134247935L, 9223372036854775294L, -1L, -274156627316187121L };
/*      */ 
/*      */   
/* 1216 */   static final long[] jjbitVec4 = new long[] { 16777215L, -65536L, -576458553280167937L, 3L };
/*      */ 
/*      */   
/* 1219 */   static final long[] jjbitVec5 = new long[] { 0L, 0L, -17179879616L, 4503588160110591L };
/*      */ 
/*      */   
/* 1222 */   static final long[] jjbitVec6 = new long[] { -8194L, -536936449L, -65533L, 234134404065073567L };
/*      */ 
/*      */   
/* 1225 */   static final long[] jjbitVec7 = new long[] { -562949953421312L, -8547991553L, 127L, 1979120929931264L };
/*      */ 
/*      */   
/* 1228 */   static final long[] jjbitVec8 = new long[] { 576460743713488896L, -562949953419266L, 9007199254740991999L, 412319973375L };
/*      */ 
/*      */   
/* 1231 */   static final long[] jjbitVec9 = new long[] { 2594073385365405664L, 17163091968L, 271902628478820320L, 844440767823872L };
/*      */ 
/*      */   
/* 1234 */   static final long[] jjbitVec10 = new long[] { 247132830528276448L, 7881300924956672L, 2589004636761075680L, 4294967296L };
/*      */ 
/*      */   
/* 1237 */   static final long[] jjbitVec11 = new long[] { 2579997437506199520L, 15837691904L, 270153412153034720L, 0L };
/*      */ 
/*      */   
/* 1240 */   static final long[] jjbitVec12 = new long[] { 283724577500946400L, 12884901888L, 283724577500946400L, 13958643712L };
/*      */ 
/*      */   
/* 1243 */   static final long[] jjbitVec13 = new long[] { 288228177128316896L, 12884901888L, 0L, 0L };
/*      */ 
/*      */   
/* 1246 */   static final long[] jjbitVec14 = new long[] { 3799912185593854L, 63L, 2309621682768192918L, 31L };
/*      */ 
/*      */   
/* 1249 */   static final long[] jjbitVec15 = new long[] { 0L, 4398046510847L, 0L, 0L };
/*      */ 
/*      */   
/* 1252 */   static final long[] jjbitVec16 = new long[] { 0L, 0L, -4294967296L, 36028797018898495L };
/*      */ 
/*      */   
/* 1255 */   static final long[] jjbitVec17 = new long[] { 5764607523034749677L, 12493387738468353L, -756383734487318528L, 144405459145588743L };
/*      */ 
/*      */   
/* 1258 */   static final long[] jjbitVec18 = new long[] { -1L, -1L, -4026531841L, 288230376151711743L };
/*      */ 
/*      */   
/* 1261 */   static final long[] jjbitVec19 = new long[] { -3233808385L, 4611686017001275199L, 6908521828386340863L, 2295745090394464220L };
/*      */ 
/*      */   
/* 1264 */   static final long[] jjbitVec20 = new long[] { 83837761617920L, 0L, 7L, 0L };
/*      */ 
/*      */   
/* 1267 */   static final long[] jjbitVec21 = new long[] { 4389456576640L, -2L, -8587837441L, 576460752303423487L };
/*      */ 
/*      */   
/* 1270 */   static final long[] jjbitVec22 = new long[] { 35184372088800L, 0L, 0L, 0L };
/*      */ 
/*      */   
/* 1273 */   static final long[] jjbitVec23 = new long[] { -1L, -1L, 274877906943L, 0L };
/*      */ 
/*      */   
/* 1276 */   static final long[] jjbitVec24 = new long[] { -1L, -1L, 68719476735L, 0L };
/*      */ 
/*      */   
/* 1279 */   static final long[] jjbitVec25 = new long[] { 0L, 0L, 36028797018963968L, -36028797027352577L };
/*      */ 
/*      */   
/* 1282 */   static final long[] jjbitVec26 = new long[] { 16777215L, -65536L, -576458553280167937L, 196611L };
/*      */ 
/*      */   
/* 1285 */   static final long[] jjbitVec27 = new long[] { -1L, 12884901951L, -17179879488L, 4503588160110591L };
/*      */ 
/*      */   
/* 1288 */   static final long[] jjbitVec28 = new long[] { -8194L, -536936449L, -65413L, 234134404065073567L };
/*      */ 
/*      */   
/* 1291 */   static final long[] jjbitVec29 = new long[] { -562949953421312L, -8547991553L, -4899916411759099777L, 1979120929931286L };
/*      */ 
/*      */   
/* 1294 */   static final long[] jjbitVec30 = new long[] { 576460743713488896L, -277081224642561L, 9007199254740991999L, 288017070894841855L };
/*      */ 
/*      */   
/* 1297 */   static final long[] jjbitVec31 = new long[] { -864691128455135250L, 281268803485695L, -3186861885341720594L, 1125692414638495L };
/*      */ 
/*      */   
/* 1300 */   static final long[] jjbitVec32 = new long[] { -3211631683292264476L, 9006925953907079L, -869759877059465234L, 281204393786303L };
/*      */ 
/*      */   
/* 1303 */   static final long[] jjbitVec33 = new long[] { -878767076314341394L, 281215949093263L, -4341532606274353172L, 280925229301191L };
/*      */ 
/*      */   
/* 1306 */   static final long[] jjbitVec34 = new long[] { -4327961440926441490L, 281212990012895L, -4327961440926441492L, 281214063754719L };
/*      */ 
/*      */   
/* 1309 */   static final long[] jjbitVec35 = new long[] { -4323457841299070996L, 281212992110031L, 0L, 0L };
/*      */ 
/*      */   
/* 1312 */   static final long[] jjbitVec36 = new long[] { 576320014815068158L, 67076095L, 4323293666156225942L, 67059551L };
/*      */ 
/*      */   
/* 1315 */   static final long[] jjbitVec37 = new long[] { -4422530440275951616L, -558551906910465L, 215680200883507167L, 0L };
/*      */ 
/*      */   
/* 1318 */   static final long[] jjbitVec38 = new long[] { 0L, 0L, 0L, 9126739968L };
/*      */ 
/*      */   
/* 1321 */   static final long[] jjbitVec39 = new long[] { 17732914942836896L, -2L, -6876561409L, 8646911284551352319L };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final int jjMoveNfa_0(int startState, int curPos) {
/* 1327 */     int startsAt = 0;
/* 1328 */     this.jjnewStateCnt = 148;
/* 1329 */     int i = 1;
/* 1330 */     this.jjstateSet[0] = startState;
/* 1331 */     int kind = Integer.MAX_VALUE;
/*      */     
/*      */     while (true) {
/* 1334 */       if (++this.jjround == Integer.MAX_VALUE)
/* 1335 */         ReInitRounds(); 
/* 1336 */       if (this.curChar < '@') {
/*      */         
/* 1338 */         long l = 1L << this.curChar;
/*      */         
/*      */         do {
/* 1341 */           switch (this.jjstateSet[--i]) {
/*      */             
/*      */             case 1:
/*      */             case 34:
/* 1345 */               if ((0x3FF600000000000L & l) == 0L)
/*      */                 break; 
/* 1347 */               if (kind > 12)
/* 1348 */                 kind = 12; 
/* 1349 */               jjCheckNAdd(1);
/*      */               break;
/*      */             case 91:
/* 1352 */               if ((0x3FF600000000000L & l) == 0L)
/*      */                 break; 
/* 1354 */               if (kind > 12)
/* 1355 */                 kind = 12; 
/* 1356 */               jjCheckNAdd(1);
/*      */               break;
/*      */             case 16:
/* 1359 */               if ((0x3FF600000000000L & l) == 0L)
/*      */                 break; 
/* 1361 */               if (kind > 12)
/* 1362 */                 kind = 12; 
/* 1363 */               jjCheckNAdd(1);
/*      */               break;
/*      */             case 55:
/* 1366 */               if ((0x3FF600000000000L & l) == 0L)
/*      */                 break; 
/* 1368 */               if (kind > 12)
/* 1369 */                 kind = 12; 
/* 1370 */               jjCheckNAdd(1);
/*      */               break;
/*      */             case 68:
/* 1373 */               if ((0x3FF600000000000L & l) == 0L)
/*      */                 break; 
/* 1375 */               if (kind > 12)
/* 1376 */                 kind = 12; 
/* 1377 */               jjCheckNAdd(1);
/*      */               break;
/*      */             case 103:
/* 1380 */               if ((0x3FF600000000000L & l) == 0L)
/*      */                 break; 
/* 1382 */               if (kind > 12)
/* 1383 */                 kind = 12; 
/* 1384 */               jjCheckNAdd(1);
/*      */               break;
/*      */             case 81:
/* 1387 */               if ((0x3FF600000000000L & l) == 0L)
/*      */                 break; 
/* 1389 */               if (kind > 12)
/* 1390 */                 kind = 12; 
/* 1391 */               jjCheckNAdd(1);
/*      */               break;
/*      */             case 23:
/* 1394 */               if ((0x3FF600000000000L & l) == 0L)
/*      */                 break; 
/* 1396 */               if (kind > 12)
/* 1397 */                 kind = 12; 
/* 1398 */               jjCheckNAdd(1);
/*      */               break;
/*      */             case 3:
/* 1401 */               if ((0x3FF000000000000L & l) != 0L) {
/* 1402 */                 jjAddStates(0, 1);
/*      */               }
/*      */               break;
/*      */           } 
/* 1406 */         } while (i != startsAt);
/*      */       }
/* 1408 */       else if (this.curChar < '') {
/*      */         
/* 1410 */         long l = 1L << (this.curChar & 0x3F);
/*      */         
/*      */         do {
/* 1413 */           switch (this.jjstateSet[--i]) {
/*      */             
/*      */             case 34:
/* 1416 */               if ((0x7FFFFFE87FFFFFEL & l) != 0L) {
/*      */                 
/* 1418 */                 if (kind > 12)
/* 1419 */                   kind = 12; 
/* 1420 */                 jjCheckNAdd(1);
/*      */               } 
/* 1422 */               if (this.curChar == 'a')
/* 1423 */                 this.jjstateSet[this.jjnewStateCnt++] = 33; 
/*      */               break;
/*      */             case 91:
/* 1426 */               if ((0x7FFFFFE87FFFFFEL & l) != 0L) {
/*      */                 
/* 1428 */                 if (kind > 12)
/* 1429 */                   kind = 12; 
/* 1430 */                 jjCheckNAdd(1);
/*      */               } 
/* 1432 */               if (this.curChar == 'n')
/* 1433 */                 this.jjstateSet[this.jjnewStateCnt++] = 90; 
/*      */               break;
/*      */             case 16:
/* 1436 */               if ((0x7FFFFFE87FFFFFEL & l) != 0L) {
/*      */                 
/* 1438 */                 if (kind > 12)
/* 1439 */                   kind = 12; 
/* 1440 */                 jjCheckNAdd(1);
/*      */               } 
/* 1442 */               if (this.curChar == 'o')
/* 1443 */                 this.jjstateSet[this.jjnewStateCnt++] = 15; 
/*      */               break;
/*      */             case 55:
/* 1446 */               if ((0x7FFFFFE87FFFFFEL & l) != 0L) {
/*      */                 
/* 1448 */                 if (kind > 12)
/* 1449 */                   kind = 12; 
/* 1450 */                 jjCheckNAdd(1);
/*      */               } 
/* 1452 */               if (this.curChar == 'o')
/* 1453 */                 this.jjstateSet[this.jjnewStateCnt++] = 54; 
/*      */               break;
/*      */             case 68:
/* 1456 */               if ((0x7FFFFFE87FFFFFEL & l) != 0L) {
/*      */                 
/* 1458 */                 if (kind > 12)
/* 1459 */                   kind = 12; 
/* 1460 */                 jjCheckNAdd(1);
/*      */               } 
/* 1462 */               if (this.curChar == 'r')
/* 1463 */                 this.jjstateSet[this.jjnewStateCnt++] = 67; 
/*      */               break;
/*      */             case 103:
/* 1466 */               if ((0x7FFFFFE87FFFFFEL & l) != 0L) {
/*      */                 
/* 1468 */                 if (kind > 12)
/* 1469 */                   kind = 12; 
/* 1470 */                 jjCheckNAdd(1);
/*      */               } 
/* 1472 */               if (this.curChar == 'a') {
/* 1473 */                 this.jjstateSet[this.jjnewStateCnt++] = 146;
/* 1474 */               } else if (this.curChar == 'i') {
/* 1475 */                 this.jjstateSet[this.jjnewStateCnt++] = 139;
/* 1476 */               }  if (this.curChar == 'a') {
/* 1477 */                 this.jjstateSet[this.jjnewStateCnt++] = 132;
/* 1478 */               } else if (this.curChar == 'i') {
/* 1479 */                 this.jjstateSet[this.jjnewStateCnt++] = 122;
/* 1480 */               }  if (this.curChar == 'a') {
/* 1481 */                 this.jjstateSet[this.jjnewStateCnt++] = 112; break;
/* 1482 */               }  if (this.curChar == 'i')
/* 1483 */                 this.jjstateSet[this.jjnewStateCnt++] = 102; 
/*      */               break;
/*      */             case 0:
/* 1486 */               if ((0x7FFFFFE87FFFFFEL & l) != 0L) {
/*      */                 
/* 1488 */                 if (kind > 12)
/* 1489 */                   kind = 12; 
/* 1490 */                 jjCheckNAdd(1);
/*      */               }
/* 1492 */               else if (this.curChar == '[') {
/* 1493 */                 this.jjstateSet[this.jjnewStateCnt++] = 3;
/* 1494 */               }  if (this.curChar == 'm') {
/* 1495 */                 jjAddStates(2, 7); break;
/* 1496 */               }  if (this.curChar == 'e') {
/* 1497 */                 this.jjstateSet[this.jjnewStateCnt++] = 91; break;
/* 1498 */               }  if (this.curChar == 'p') {
/* 1499 */                 this.jjstateSet[this.jjnewStateCnt++] = 81; break;
/* 1500 */               }  if (this.curChar == 'l') {
/* 1501 */                 this.jjstateSet[this.jjnewStateCnt++] = 74; break;
/* 1502 */               }  if (this.curChar == 'f') {
/* 1503 */                 this.jjstateSet[this.jjnewStateCnt++] = 68; break;
/* 1504 */               }  if (this.curChar == 't') {
/* 1505 */                 this.jjstateSet[this.jjnewStateCnt++] = 55; break;
/* 1506 */               }  if (this.curChar == 'w') {
/* 1507 */                 this.jjstateSet[this.jjnewStateCnt++] = 44; break;
/* 1508 */               }  if (this.curChar == 'c') {
/* 1509 */                 this.jjstateSet[this.jjnewStateCnt++] = 34; break;
/* 1510 */               }  if (this.curChar == 'n') {
/* 1511 */                 this.jjstateSet[this.jjnewStateCnt++] = 23; break;
/* 1512 */               }  if (this.curChar == 'b') {
/* 1513 */                 this.jjstateSet[this.jjnewStateCnt++] = 16; break;
/* 1514 */               }  if (this.curChar == 'o')
/* 1515 */                 this.jjstateSet[this.jjnewStateCnt++] = 10; 
/*      */               break;
/*      */             case 81:
/* 1518 */               if ((0x7FFFFFE87FFFFFEL & l) != 0L) {
/*      */                 
/* 1520 */                 if (kind > 12)
/* 1521 */                   kind = 12; 
/* 1522 */                 jjCheckNAdd(1);
/*      */               } 
/* 1524 */               if (this.curChar == 'a')
/* 1525 */                 this.jjstateSet[this.jjnewStateCnt++] = 80; 
/*      */               break;
/*      */             case 23:
/* 1528 */               if ((0x7FFFFFE87FFFFFEL & l) != 0L) {
/*      */                 
/* 1530 */                 if (kind > 12)
/* 1531 */                   kind = 12; 
/* 1532 */                 jjCheckNAdd(1);
/*      */               } 
/* 1534 */               if (this.curChar == 'u')
/* 1535 */                 this.jjstateSet[this.jjnewStateCnt++] = 22; 
/*      */               break;
/*      */             case 1:
/* 1538 */               if ((0x7FFFFFE87FFFFFEL & l) == 0L)
/*      */                 break; 
/* 1540 */               if (kind > 12)
/* 1541 */                 kind = 12; 
/* 1542 */               jjCheckNAdd(1);
/*      */               break;
/*      */             case 2:
/* 1545 */               if (this.curChar == '[')
/* 1546 */                 this.jjstateSet[this.jjnewStateCnt++] = 3; 
/*      */               break;
/*      */             case 4:
/* 1549 */               if (this.curChar == ']')
/* 1550 */                 kind = 13; 
/*      */               break;
/*      */             case 5:
/* 1553 */               if (this.curChar == 'd' && kind > 14)
/* 1554 */                 kind = 14; 
/*      */               break;
/*      */             case 6:
/*      */             case 12:
/* 1558 */               if (this.curChar == 'e')
/* 1559 */                 jjCheckNAdd(5); 
/*      */               break;
/*      */             case 7:
/* 1562 */               if (this.curChar == 'r')
/* 1563 */                 this.jjstateSet[this.jjnewStateCnt++] = 6; 
/*      */               break;
/*      */             case 8:
/* 1566 */               if (this.curChar == 'e')
/* 1567 */                 this.jjstateSet[this.jjnewStateCnt++] = 7; 
/*      */               break;
/*      */             case 9:
/* 1570 */               if (this.curChar == 'd')
/* 1571 */                 this.jjstateSet[this.jjnewStateCnt++] = 8; 
/*      */               break;
/*      */             case 10:
/* 1574 */               if (this.curChar == 'r')
/* 1575 */                 this.jjstateSet[this.jjnewStateCnt++] = 9; 
/*      */               break;
/*      */             case 11:
/* 1578 */               if (this.curChar == 'o')
/* 1579 */                 this.jjstateSet[this.jjnewStateCnt++] = 10; 
/*      */               break;
/*      */             case 13:
/* 1582 */               if (this.curChar == 'd')
/* 1583 */                 this.jjstateSet[this.jjnewStateCnt++] = 12; 
/*      */               break;
/*      */             case 14:
/* 1586 */               if (this.curChar == 'n')
/* 1587 */                 this.jjstateSet[this.jjnewStateCnt++] = 13; 
/*      */               break;
/*      */             case 15:
/* 1590 */               if (this.curChar == 'u')
/* 1591 */                 this.jjstateSet[this.jjnewStateCnt++] = 14; 
/*      */               break;
/*      */             case 17:
/* 1594 */               if (this.curChar == 'b')
/* 1595 */                 this.jjstateSet[this.jjnewStateCnt++] = 16; 
/*      */               break;
/*      */             case 18:
/* 1598 */               if (this.curChar == 'c' && kind > 14)
/* 1599 */                 kind = 14; 
/*      */               break;
/*      */             case 19:
/* 1602 */               if (this.curChar == 'i')
/* 1603 */                 this.jjstateSet[this.jjnewStateCnt++] = 18; 
/*      */               break;
/*      */             case 20:
/* 1606 */               if (this.curChar == 'r')
/* 1607 */                 this.jjstateSet[this.jjnewStateCnt++] = 19; 
/*      */               break;
/*      */             case 21:
/* 1610 */               if (this.curChar == 'e')
/* 1611 */                 this.jjstateSet[this.jjnewStateCnt++] = 20; 
/*      */               break;
/*      */             case 22:
/* 1614 */               if (this.curChar == 'm')
/* 1615 */                 this.jjstateSet[this.jjnewStateCnt++] = 21; 
/*      */               break;
/*      */             case 24:
/* 1618 */               if (this.curChar == 'n')
/* 1619 */                 this.jjstateSet[this.jjnewStateCnt++] = 23; 
/*      */               break;
/*      */             case 25:
/* 1622 */               if (this.curChar == 'y' && kind > 14)
/* 1623 */                 kind = 14; 
/*      */               break;
/*      */             case 26:
/* 1626 */               if (this.curChar == 't')
/* 1627 */                 this.jjstateSet[this.jjnewStateCnt++] = 25; 
/*      */               break;
/*      */             case 27:
/* 1630 */               if (this.curChar == 'i')
/* 1631 */                 this.jjstateSet[this.jjnewStateCnt++] = 26; 
/*      */               break;
/*      */             case 28:
/* 1634 */               if (this.curChar == 'l')
/* 1635 */                 this.jjstateSet[this.jjnewStateCnt++] = 27; 
/*      */               break;
/*      */             case 29:
/* 1638 */               if (this.curChar == 'a')
/* 1639 */                 this.jjstateSet[this.jjnewStateCnt++] = 28; 
/*      */               break;
/*      */             case 30:
/* 1642 */               if (this.curChar == 'n')
/* 1643 */                 this.jjstateSet[this.jjnewStateCnt++] = 29; 
/*      */               break;
/*      */             case 31:
/* 1646 */               if (this.curChar == 'i')
/* 1647 */                 this.jjstateSet[this.jjnewStateCnt++] = 30; 
/*      */               break;
/*      */             case 32:
/* 1650 */               if (this.curChar == 'd')
/* 1651 */                 this.jjstateSet[this.jjnewStateCnt++] = 31; 
/*      */               break;
/*      */             case 33:
/* 1654 */               if (this.curChar == 'r')
/* 1655 */                 this.jjstateSet[this.jjnewStateCnt++] = 32; 
/*      */               break;
/*      */             case 35:
/* 1658 */               if (this.curChar == 'c')
/* 1659 */                 this.jjstateSet[this.jjnewStateCnt++] = 34; 
/*      */               break;
/*      */             case 36:
/* 1662 */               if (this.curChar == 'e' && kind > 14)
/* 1663 */                 kind = 14; 
/*      */               break;
/*      */             case 37:
/* 1666 */               if (this.curChar == 'c')
/* 1667 */                 jjCheckNAdd(36); 
/*      */               break;
/*      */             case 38:
/* 1670 */               if (this.curChar == 'a')
/* 1671 */                 this.jjstateSet[this.jjnewStateCnt++] = 37; 
/*      */               break;
/*      */             case 39:
/* 1674 */               if (this.curChar == 'p')
/* 1675 */                 this.jjstateSet[this.jjnewStateCnt++] = 38; 
/*      */               break;
/*      */             case 40:
/* 1678 */               if (this.curChar == 'S')
/* 1679 */                 this.jjstateSet[this.jjnewStateCnt++] = 39; 
/*      */               break;
/*      */             case 41:
/* 1682 */               if (this.curChar == 'e')
/* 1683 */                 this.jjstateSet[this.jjnewStateCnt++] = 40; 
/*      */               break;
/*      */             case 42:
/* 1686 */               if (this.curChar == 't')
/* 1687 */                 this.jjstateSet[this.jjnewStateCnt++] = 41; 
/*      */               break;
/*      */             case 43:
/* 1690 */               if (this.curChar == 'i')
/* 1691 */                 this.jjstateSet[this.jjnewStateCnt++] = 42; 
/*      */               break;
/*      */             case 44:
/* 1694 */               if (this.curChar == 'h')
/* 1695 */                 this.jjstateSet[this.jjnewStateCnt++] = 43; 
/*      */               break;
/*      */             case 45:
/* 1698 */               if (this.curChar == 'w')
/* 1699 */                 this.jjstateSet[this.jjnewStateCnt++] = 44; 
/*      */               break;
/*      */             case 46:
/* 1702 */               if (this.curChar == 's' && kind > 14)
/* 1703 */                 kind = 14; 
/*      */               break;
/*      */             case 47:
/*      */             case 57:
/* 1707 */               if (this.curChar == 't')
/* 1708 */                 jjCheckNAdd(46); 
/*      */               break;
/*      */             case 48:
/* 1711 */               if (this.curChar == 'i')
/* 1712 */                 this.jjstateSet[this.jjnewStateCnt++] = 47; 
/*      */               break;
/*      */             case 49:
/* 1715 */               if (this.curChar == 'g')
/* 1716 */                 this.jjstateSet[this.jjnewStateCnt++] = 48; 
/*      */               break;
/*      */             case 50:
/* 1719 */               if (this.curChar == 'i')
/* 1720 */                 this.jjstateSet[this.jjnewStateCnt++] = 49; 
/*      */               break;
/*      */             case 51:
/* 1723 */               if (this.curChar == 'D')
/* 1724 */                 this.jjstateSet[this.jjnewStateCnt++] = 50; 
/*      */               break;
/*      */             case 52:
/* 1727 */               if (this.curChar == 'l')
/* 1728 */                 this.jjstateSet[this.jjnewStateCnt++] = 51; 
/*      */               break;
/*      */             case 53:
/* 1731 */               if (this.curChar == 'a')
/* 1732 */                 this.jjstateSet[this.jjnewStateCnt++] = 52; 
/*      */               break;
/*      */             case 54:
/* 1735 */               if (this.curChar == 't')
/* 1736 */                 this.jjstateSet[this.jjnewStateCnt++] = 53; 
/*      */               break;
/*      */             case 56:
/* 1739 */               if (this.curChar == 't')
/* 1740 */                 this.jjstateSet[this.jjnewStateCnt++] = 55; 
/*      */               break;
/*      */             case 58:
/* 1743 */               if (this.curChar == 'i')
/* 1744 */                 this.jjstateSet[this.jjnewStateCnt++] = 57; 
/*      */               break;
/*      */             case 59:
/* 1747 */               if (this.curChar == 'g')
/* 1748 */                 this.jjstateSet[this.jjnewStateCnt++] = 58; 
/*      */               break;
/*      */             case 60:
/* 1751 */               if (this.curChar == 'i')
/* 1752 */                 this.jjstateSet[this.jjnewStateCnt++] = 59; 
/*      */               break;
/*      */             case 61:
/* 1755 */               if (this.curChar == 'D')
/* 1756 */                 this.jjstateSet[this.jjnewStateCnt++] = 60; 
/*      */               break;
/*      */             case 62:
/* 1759 */               if (this.curChar == 'n')
/* 1760 */                 this.jjstateSet[this.jjnewStateCnt++] = 61; 
/*      */               break;
/*      */             case 63:
/* 1763 */               if (this.curChar == 'o')
/* 1764 */                 this.jjstateSet[this.jjnewStateCnt++] = 62; 
/*      */               break;
/*      */             case 64:
/* 1767 */               if (this.curChar == 'i')
/* 1768 */                 this.jjstateSet[this.jjnewStateCnt++] = 63; 
/*      */               break;
/*      */             case 65:
/* 1771 */               if (this.curChar == 't')
/* 1772 */                 this.jjstateSet[this.jjnewStateCnt++] = 64; 
/*      */               break;
/*      */             case 66:
/* 1775 */               if (this.curChar == 'c')
/* 1776 */                 this.jjstateSet[this.jjnewStateCnt++] = 65; 
/*      */               break;
/*      */             case 67:
/* 1779 */               if (this.curChar == 'a')
/* 1780 */                 this.jjstateSet[this.jjnewStateCnt++] = 66; 
/*      */               break;
/*      */             case 69:
/* 1783 */               if (this.curChar == 'f')
/* 1784 */                 this.jjstateSet[this.jjnewStateCnt++] = 68; 
/*      */               break;
/*      */             case 70:
/* 1787 */               if (this.curChar == 'h' && kind > 14)
/* 1788 */                 kind = 14; 
/*      */               break;
/*      */             case 71:
/*      */             case 134:
/*      */             case 141:
/* 1793 */               if (this.curChar == 't')
/* 1794 */                 jjCheckNAdd(70); 
/*      */               break;
/*      */             case 72:
/* 1797 */               if (this.curChar == 'g')
/* 1798 */                 this.jjstateSet[this.jjnewStateCnt++] = 71; 
/*      */               break;
/*      */             case 73:
/* 1801 */               if (this.curChar == 'n')
/* 1802 */                 this.jjstateSet[this.jjnewStateCnt++] = 72; 
/*      */               break;
/*      */             case 74:
/* 1805 */               if (this.curChar == 'e')
/* 1806 */                 this.jjstateSet[this.jjnewStateCnt++] = 73; 
/*      */               break;
/*      */             case 75:
/* 1809 */               if (this.curChar == 'l')
/* 1810 */                 this.jjstateSet[this.jjnewStateCnt++] = 74; 
/*      */               break;
/*      */             case 76:
/* 1813 */               if (this.curChar == 'n' && kind > 14)
/* 1814 */                 kind = 14; 
/*      */               break;
/*      */             case 77:
/* 1817 */               if (this.curChar == 'r')
/* 1818 */                 jjCheckNAdd(76); 
/*      */               break;
/*      */             case 78:
/* 1821 */               if (this.curChar == 'e')
/* 1822 */                 this.jjstateSet[this.jjnewStateCnt++] = 77; 
/*      */               break;
/*      */             case 79:
/* 1825 */               if (this.curChar == 't')
/* 1826 */                 this.jjstateSet[this.jjnewStateCnt++] = 78; 
/*      */               break;
/*      */             case 80:
/* 1829 */               if (this.curChar == 't')
/* 1830 */                 this.jjstateSet[this.jjnewStateCnt++] = 79; 
/*      */               break;
/*      */             case 82:
/* 1833 */               if (this.curChar == 'p')
/* 1834 */                 this.jjstateSet[this.jjnewStateCnt++] = 81; 
/*      */               break;
/*      */             case 83:
/* 1837 */               if (this.curChar == 'o')
/* 1838 */                 jjCheckNAdd(76); 
/*      */               break;
/*      */             case 84:
/* 1841 */               if (this.curChar == 'i')
/* 1842 */                 this.jjstateSet[this.jjnewStateCnt++] = 83; 
/*      */               break;
/*      */             case 85:
/* 1845 */               if (this.curChar == 't')
/* 1846 */                 this.jjstateSet[this.jjnewStateCnt++] = 84; 
/*      */               break;
/*      */             case 86:
/* 1849 */               if (this.curChar == 'a')
/* 1850 */                 this.jjstateSet[this.jjnewStateCnt++] = 85; 
/*      */               break;
/*      */             case 87:
/* 1853 */               if (this.curChar == 'r')
/* 1854 */                 this.jjstateSet[this.jjnewStateCnt++] = 86; 
/*      */               break;
/*      */             case 88:
/* 1857 */               if (this.curChar == 'e')
/* 1858 */                 this.jjstateSet[this.jjnewStateCnt++] = 87; 
/*      */               break;
/*      */             case 89:
/* 1861 */               if (this.curChar == 'm')
/* 1862 */                 this.jjstateSet[this.jjnewStateCnt++] = 88; 
/*      */               break;
/*      */             case 90:
/* 1865 */               if (this.curChar == 'u')
/* 1866 */                 this.jjstateSet[this.jjnewStateCnt++] = 89; 
/*      */               break;
/*      */             case 92:
/* 1869 */               if (this.curChar == 'e')
/* 1870 */                 this.jjstateSet[this.jjnewStateCnt++] = 91; 
/*      */               break;
/*      */             case 93:
/* 1873 */               if (this.curChar == 'm')
/* 1874 */                 jjAddStates(2, 7); 
/*      */               break;
/*      */             case 94:
/*      */             case 104:
/*      */             case 114:
/*      */             case 124:
/* 1880 */               if (this.curChar == 'v')
/* 1881 */                 jjCheckNAdd(36); 
/*      */               break;
/*      */             case 95:
/* 1884 */               if (this.curChar == 'i')
/* 1885 */                 this.jjstateSet[this.jjnewStateCnt++] = 94; 
/*      */               break;
/*      */             case 96:
/* 1888 */               if (this.curChar == 's')
/* 1889 */                 this.jjstateSet[this.jjnewStateCnt++] = 95; 
/*      */               break;
/*      */             case 97:
/* 1892 */               if (this.curChar == 'u')
/* 1893 */                 this.jjstateSet[this.jjnewStateCnt++] = 96; 
/*      */               break;
/*      */             case 98:
/* 1896 */               if (this.curChar == 'l')
/* 1897 */                 this.jjstateSet[this.jjnewStateCnt++] = 97; 
/*      */               break;
/*      */             case 99:
/* 1900 */               if (this.curChar == 'c')
/* 1901 */                 this.jjstateSet[this.jjnewStateCnt++] = 98; 
/*      */               break;
/*      */             case 100:
/* 1904 */               if (this.curChar == 'n')
/* 1905 */                 this.jjstateSet[this.jjnewStateCnt++] = 99; 
/*      */               break;
/*      */             case 101:
/* 1908 */               if (this.curChar == 'I')
/* 1909 */                 this.jjstateSet[this.jjnewStateCnt++] = 100; 
/*      */               break;
/*      */             case 102:
/* 1912 */               if (this.curChar == 'n')
/* 1913 */                 this.jjstateSet[this.jjnewStateCnt++] = 101; 
/*      */               break;
/*      */             case 105:
/* 1916 */               if (this.curChar == 'i')
/* 1917 */                 this.jjstateSet[this.jjnewStateCnt++] = 104; 
/*      */               break;
/*      */             case 106:
/* 1920 */               if (this.curChar == 's')
/* 1921 */                 this.jjstateSet[this.jjnewStateCnt++] = 105; 
/*      */               break;
/*      */             case 107:
/* 1924 */               if (this.curChar == 'u')
/* 1925 */                 this.jjstateSet[this.jjnewStateCnt++] = 106; 
/*      */               break;
/*      */             case 108:
/* 1928 */               if (this.curChar == 'l')
/* 1929 */                 this.jjstateSet[this.jjnewStateCnt++] = 107; 
/*      */               break;
/*      */             case 109:
/* 1932 */               if (this.curChar == 'c')
/* 1933 */                 this.jjstateSet[this.jjnewStateCnt++] = 108; 
/*      */               break;
/*      */             case 110:
/* 1936 */               if (this.curChar == 'n')
/* 1937 */                 this.jjstateSet[this.jjnewStateCnt++] = 109; 
/*      */               break;
/*      */             case 111:
/* 1940 */               if (this.curChar == 'I')
/* 1941 */                 this.jjstateSet[this.jjnewStateCnt++] = 110; 
/*      */               break;
/*      */             case 112:
/* 1944 */               if (this.curChar == 'x')
/* 1945 */                 this.jjstateSet[this.jjnewStateCnt++] = 111; 
/*      */               break;
/*      */             case 113:
/* 1948 */               if (this.curChar == 'a')
/* 1949 */                 this.jjstateSet[this.jjnewStateCnt++] = 112; 
/*      */               break;
/*      */             case 115:
/* 1952 */               if (this.curChar == 'i')
/* 1953 */                 this.jjstateSet[this.jjnewStateCnt++] = 114; 
/*      */               break;
/*      */             case 116:
/* 1956 */               if (this.curChar == 's')
/* 1957 */                 this.jjstateSet[this.jjnewStateCnt++] = 115; 
/*      */               break;
/*      */             case 117:
/* 1960 */               if (this.curChar == 'u')
/* 1961 */                 this.jjstateSet[this.jjnewStateCnt++] = 116; 
/*      */               break;
/*      */             case 118:
/* 1964 */               if (this.curChar == 'l')
/* 1965 */                 this.jjstateSet[this.jjnewStateCnt++] = 117; 
/*      */               break;
/*      */             case 119:
/* 1968 */               if (this.curChar == 'c')
/* 1969 */                 this.jjstateSet[this.jjnewStateCnt++] = 118; 
/*      */               break;
/*      */             case 120:
/* 1972 */               if (this.curChar == 'x')
/* 1973 */                 this.jjstateSet[this.jjnewStateCnt++] = 119; 
/*      */               break;
/*      */             case 121:
/* 1976 */               if (this.curChar == 'E')
/* 1977 */                 this.jjstateSet[this.jjnewStateCnt++] = 120; 
/*      */               break;
/*      */             case 122:
/* 1980 */               if (this.curChar == 'n')
/* 1981 */                 this.jjstateSet[this.jjnewStateCnt++] = 121; 
/*      */               break;
/*      */             case 123:
/* 1984 */               if (this.curChar == 'i')
/* 1985 */                 this.jjstateSet[this.jjnewStateCnt++] = 122; 
/*      */               break;
/*      */             case 125:
/* 1988 */               if (this.curChar == 'i')
/* 1989 */                 this.jjstateSet[this.jjnewStateCnt++] = 124; 
/*      */               break;
/*      */             case 126:
/* 1992 */               if (this.curChar == 's')
/* 1993 */                 this.jjstateSet[this.jjnewStateCnt++] = 125; 
/*      */               break;
/*      */             case 127:
/* 1996 */               if (this.curChar == 'u')
/* 1997 */                 this.jjstateSet[this.jjnewStateCnt++] = 126; 
/*      */               break;
/*      */             case 128:
/* 2000 */               if (this.curChar == 'l')
/* 2001 */                 this.jjstateSet[this.jjnewStateCnt++] = 127; 
/*      */               break;
/*      */             case 129:
/* 2004 */               if (this.curChar == 'c')
/* 2005 */                 this.jjstateSet[this.jjnewStateCnt++] = 128; 
/*      */               break;
/*      */             case 130:
/* 2008 */               if (this.curChar == 'x')
/* 2009 */                 this.jjstateSet[this.jjnewStateCnt++] = 129; 
/*      */               break;
/*      */             case 131:
/* 2012 */               if (this.curChar == 'E')
/* 2013 */                 this.jjstateSet[this.jjnewStateCnt++] = 130; 
/*      */               break;
/*      */             case 132:
/* 2016 */               if (this.curChar == 'x')
/* 2017 */                 this.jjstateSet[this.jjnewStateCnt++] = 131; 
/*      */               break;
/*      */             case 133:
/* 2020 */               if (this.curChar == 'a')
/* 2021 */                 this.jjstateSet[this.jjnewStateCnt++] = 132; 
/*      */               break;
/*      */             case 135:
/* 2024 */               if (this.curChar == 'g')
/* 2025 */                 this.jjstateSet[this.jjnewStateCnt++] = 134; 
/*      */               break;
/*      */             case 136:
/* 2028 */               if (this.curChar == 'n')
/* 2029 */                 this.jjstateSet[this.jjnewStateCnt++] = 135; 
/*      */               break;
/*      */             case 137:
/* 2032 */               if (this.curChar == 'e')
/* 2033 */                 this.jjstateSet[this.jjnewStateCnt++] = 136; 
/*      */               break;
/*      */             case 138:
/* 2036 */               if (this.curChar == 'L')
/* 2037 */                 this.jjstateSet[this.jjnewStateCnt++] = 137; 
/*      */               break;
/*      */             case 139:
/* 2040 */               if (this.curChar == 'n')
/* 2041 */                 this.jjstateSet[this.jjnewStateCnt++] = 138; 
/*      */               break;
/*      */             case 140:
/* 2044 */               if (this.curChar == 'i')
/* 2045 */                 this.jjstateSet[this.jjnewStateCnt++] = 139; 
/*      */               break;
/*      */             case 142:
/* 2048 */               if (this.curChar == 'g')
/* 2049 */                 this.jjstateSet[this.jjnewStateCnt++] = 141; 
/*      */               break;
/*      */             case 143:
/* 2052 */               if (this.curChar == 'n')
/* 2053 */                 this.jjstateSet[this.jjnewStateCnt++] = 142; 
/*      */               break;
/*      */             case 144:
/* 2056 */               if (this.curChar == 'e')
/* 2057 */                 this.jjstateSet[this.jjnewStateCnt++] = 143; 
/*      */               break;
/*      */             case 145:
/* 2060 */               if (this.curChar == 'L')
/* 2061 */                 this.jjstateSet[this.jjnewStateCnt++] = 144; 
/*      */               break;
/*      */             case 146:
/* 2064 */               if (this.curChar == 'x')
/* 2065 */                 this.jjstateSet[this.jjnewStateCnt++] = 145; 
/*      */               break;
/*      */             case 147:
/* 2068 */               if (this.curChar == 'a') {
/* 2069 */                 this.jjstateSet[this.jjnewStateCnt++] = 146;
/*      */               }
/*      */               break;
/*      */           } 
/* 2073 */         } while (i != startsAt);
/*      */       }
/*      */       else {
/*      */         
/* 2077 */         int hiByte = this.curChar >> 8;
/* 2078 */         int i1 = hiByte >> 6;
/* 2079 */         long l1 = 1L << (hiByte & 0x3F);
/* 2080 */         int i2 = (this.curChar & 0xFF) >> 6;
/* 2081 */         long l2 = 1L << (this.curChar & 0x3F);
/*      */         
/*      */         do {
/* 2084 */           switch (this.jjstateSet[--i]) {
/*      */             
/*      */             case 1:
/*      */             case 34:
/* 2088 */               if (!jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 2090 */               if (kind > 12)
/* 2091 */                 kind = 12; 
/* 2092 */               jjCheckNAdd(1);
/*      */               break;
/*      */             case 91:
/* 2095 */               if (!jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 2097 */               if (kind > 12)
/* 2098 */                 kind = 12; 
/* 2099 */               jjCheckNAdd(1);
/*      */               break;
/*      */             case 16:
/* 2102 */               if (!jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 2104 */               if (kind > 12)
/* 2105 */                 kind = 12; 
/* 2106 */               jjCheckNAdd(1);
/*      */               break;
/*      */             case 55:
/* 2109 */               if (!jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 2111 */               if (kind > 12)
/* 2112 */                 kind = 12; 
/* 2113 */               jjCheckNAdd(1);
/*      */               break;
/*      */             case 68:
/* 2116 */               if (!jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 2118 */               if (kind > 12)
/* 2119 */                 kind = 12; 
/* 2120 */               jjCheckNAdd(1);
/*      */               break;
/*      */             case 103:
/* 2123 */               if (!jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 2125 */               if (kind > 12)
/* 2126 */                 kind = 12; 
/* 2127 */               jjCheckNAdd(1);
/*      */               break;
/*      */             case 0:
/* 2130 */               if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 2132 */               if (kind > 12)
/* 2133 */                 kind = 12; 
/* 2134 */               jjCheckNAdd(1);
/*      */               break;
/*      */             case 81:
/* 2137 */               if (!jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 2139 */               if (kind > 12)
/* 2140 */                 kind = 12; 
/* 2141 */               jjCheckNAdd(1);
/*      */               break;
/*      */             case 23:
/* 2144 */               if (!jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */                 break; 
/* 2146 */               if (kind > 12)
/* 2147 */                 kind = 12; 
/* 2148 */               jjCheckNAdd(1);
/*      */               break;
/*      */           } 
/*      */         
/* 2152 */         } while (i != startsAt);
/*      */       } 
/* 2154 */       if (kind != Integer.MAX_VALUE) {
/*      */         
/* 2156 */         this.jjmatchedKind = kind;
/* 2157 */         this.jjmatchedPos = curPos;
/* 2158 */         kind = Integer.MAX_VALUE;
/*      */       } 
/* 2160 */       curPos++;
/* 2161 */       if ((i = this.jjnewStateCnt) == (startsAt = 148 - (this.jjnewStateCnt = startsAt)))
/* 2162 */         return curPos;  
/* 2163 */       try { this.curChar = this.input_stream.readChar(); }
/* 2164 */       catch (IOException e) { return curPos; }
/*      */     
/*      */     } 
/* 2167 */   } static final int[] jjnextStates = new int[] { 3, 4, 103, 113, 123, 133, 140, 147 };
/*      */ 
/*      */ 
/*      */   
/*      */   private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2) {
/* 2172 */     switch (hiByte) {
/*      */       
/*      */       case 0:
/* 2175 */         return ((jjbitVec2[i2] & l2) != 0L);
/*      */       case 1:
/* 2177 */         return ((jjbitVec3[i2] & l2) != 0L);
/*      */       case 2:
/* 2179 */         return ((jjbitVec4[i2] & l2) != 0L);
/*      */       case 3:
/* 2181 */         return ((jjbitVec5[i2] & l2) != 0L);
/*      */       case 4:
/* 2183 */         return ((jjbitVec6[i2] & l2) != 0L);
/*      */       case 5:
/* 2185 */         return ((jjbitVec7[i2] & l2) != 0L);
/*      */       case 6:
/* 2187 */         return ((jjbitVec8[i2] & l2) != 0L);
/*      */       case 9:
/* 2189 */         return ((jjbitVec9[i2] & l2) != 0L);
/*      */       case 10:
/* 2191 */         return ((jjbitVec10[i2] & l2) != 0L);
/*      */       case 11:
/* 2193 */         return ((jjbitVec11[i2] & l2) != 0L);
/*      */       case 12:
/* 2195 */         return ((jjbitVec12[i2] & l2) != 0L);
/*      */       case 13:
/* 2197 */         return ((jjbitVec13[i2] & l2) != 0L);
/*      */       case 14:
/* 2199 */         return ((jjbitVec14[i2] & l2) != 0L);
/*      */       case 15:
/* 2201 */         return ((jjbitVec15[i2] & l2) != 0L);
/*      */       case 16:
/* 2203 */         return ((jjbitVec16[i2] & l2) != 0L);
/*      */       case 17:
/* 2205 */         return ((jjbitVec17[i2] & l2) != 0L);
/*      */       case 30:
/* 2207 */         return ((jjbitVec18[i2] & l2) != 0L);
/*      */       case 31:
/* 2209 */         return ((jjbitVec19[i2] & l2) != 0L);
/*      */       case 33:
/* 2211 */         return ((jjbitVec20[i2] & l2) != 0L);
/*      */       case 48:
/* 2213 */         return ((jjbitVec21[i2] & l2) != 0L);
/*      */       case 49:
/* 2215 */         return ((jjbitVec22[i2] & l2) != 0L);
/*      */       case 159:
/* 2217 */         return ((jjbitVec23[i2] & l2) != 0L);
/*      */       case 215:
/* 2219 */         return ((jjbitVec24[i2] & l2) != 0L);
/*      */     } 
/* 2221 */     if ((jjbitVec0[i1] & l1) != 0L)
/* 2222 */       return true; 
/* 2223 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final boolean jjCanMove_1(int hiByte, int i1, int i2, long l1, long l2) {
/* 2228 */     switch (hiByte) {
/*      */       
/*      */       case 0:
/* 2231 */         return ((jjbitVec25[i2] & l2) != 0L);
/*      */       case 1:
/* 2233 */         return ((jjbitVec3[i2] & l2) != 0L);
/*      */       case 2:
/* 2235 */         return ((jjbitVec26[i2] & l2) != 0L);
/*      */       case 3:
/* 2237 */         return ((jjbitVec27[i2] & l2) != 0L);
/*      */       case 4:
/* 2239 */         return ((jjbitVec28[i2] & l2) != 0L);
/*      */       case 5:
/* 2241 */         return ((jjbitVec29[i2] & l2) != 0L);
/*      */       case 6:
/* 2243 */         return ((jjbitVec30[i2] & l2) != 0L);
/*      */       case 9:
/* 2245 */         return ((jjbitVec31[i2] & l2) != 0L);
/*      */       case 10:
/* 2247 */         return ((jjbitVec32[i2] & l2) != 0L);
/*      */       case 11:
/* 2249 */         return ((jjbitVec33[i2] & l2) != 0L);
/*      */       case 12:
/* 2251 */         return ((jjbitVec34[i2] & l2) != 0L);
/*      */       case 13:
/* 2253 */         return ((jjbitVec35[i2] & l2) != 0L);
/*      */       case 14:
/* 2255 */         return ((jjbitVec36[i2] & l2) != 0L);
/*      */       case 15:
/* 2257 */         return ((jjbitVec37[i2] & l2) != 0L);
/*      */       case 16:
/* 2259 */         return ((jjbitVec16[i2] & l2) != 0L);
/*      */       case 17:
/* 2261 */         return ((jjbitVec17[i2] & l2) != 0L);
/*      */       case 30:
/* 2263 */         return ((jjbitVec18[i2] & l2) != 0L);
/*      */       case 31:
/* 2265 */         return ((jjbitVec19[i2] & l2) != 0L);
/*      */       case 32:
/* 2267 */         return ((jjbitVec38[i2] & l2) != 0L);
/*      */       case 33:
/* 2269 */         return ((jjbitVec20[i2] & l2) != 0L);
/*      */       case 48:
/* 2271 */         return ((jjbitVec39[i2] & l2) != 0L);
/*      */       case 49:
/* 2273 */         return ((jjbitVec22[i2] & l2) != 0L);
/*      */       case 159:
/* 2275 */         return ((jjbitVec23[i2] & l2) != 0L);
/*      */       case 215:
/* 2277 */         return ((jjbitVec24[i2] & l2) != 0L);
/*      */     } 
/* 2279 */     if ((jjbitVec0[i1] & l1) != 0L)
/* 2280 */       return true; 
/* 2281 */     return false;
/*      */   }
/*      */   
/* 2284 */   public static final String[] jjstrLiteralImages = new String[] { "", null, null, null, null, null, null, null, null, null, null, null, null, null, null, ":", "/", "//", "attribute::", "@", "element::", "substitutionGroup::", "type::", "~", "baseType::", "primitiveType::", "itemType::", "memberType::", "scope::", "attributeGroup::", "group::", "identityContraint::", "key::", "notation::", "model::sequence", "model::choice", "model::all", "model::*", "any::*", "anyAttribute::*", "facet::*", "facet::", "component::*", "x-schema::", "x-schema::*", "*", "0" };
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
/* 2298 */   public static final String[] lexStateNames = new String[] { "DEFAULT" };
/*      */ 
/*      */   
/* 2301 */   static final long[] jjtoToken = new long[] { 140737488351233L };
/*      */ 
/*      */   
/* 2304 */   static final long[] jjtoSkip = new long[] { 62L };
/*      */   
/*      */   protected SimpleCharStream input_stream;
/*      */   
/* 2308 */   private final int[] jjrounds = new int[148];
/* 2309 */   private final int[] jjstateSet = new int[296]; protected char curChar; int curLexState;
/*      */   int defaultLexState;
/*      */   int jjnewStateCnt;
/*      */   int jjround;
/*      */   int jjmatchedPos;
/*      */   int jjmatchedKind;
/*      */   
/*      */   public SCDParserTokenManager(SimpleCharStream stream, int lexState) {
/* 2317 */     this(stream);
/* 2318 */     SwitchTo(lexState);
/*      */   }
/*      */   
/*      */   public void ReInit(SimpleCharStream stream) {
/* 2322 */     this.jjmatchedPos = this.jjnewStateCnt = 0;
/* 2323 */     this.curLexState = this.defaultLexState;
/* 2324 */     this.input_stream = stream;
/* 2325 */     ReInitRounds();
/*      */   }
/*      */ 
/*      */   
/*      */   private final void ReInitRounds() {
/* 2330 */     this.jjround = -2147483647;
/* 2331 */     for (int i = 148; i-- > 0;)
/* 2332 */       this.jjrounds[i] = Integer.MIN_VALUE; 
/*      */   }
/*      */   
/*      */   public void ReInit(SimpleCharStream stream, int lexState) {
/* 2336 */     ReInit(stream);
/* 2337 */     SwitchTo(lexState);
/*      */   }
/*      */   
/*      */   public void SwitchTo(int lexState) {
/* 2341 */     if (lexState >= 1 || lexState < 0) {
/* 2342 */       throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", 2);
/*      */     }
/* 2344 */     this.curLexState = lexState;
/*      */   }
/*      */ 
/*      */   
/*      */   protected Token jjFillToken() {
/* 2349 */     Token t = Token.newToken(this.jjmatchedKind);
/* 2350 */     t.kind = this.jjmatchedKind;
/* 2351 */     String im = jjstrLiteralImages[this.jjmatchedKind];
/* 2352 */     t.image = (im == null) ? this.input_stream.GetImage() : im;
/* 2353 */     t.beginLine = this.input_stream.getBeginLine();
/* 2354 */     t.beginColumn = this.input_stream.getBeginColumn();
/* 2355 */     t.endLine = this.input_stream.getEndLine();
/* 2356 */     t.endColumn = this.input_stream.getEndColumn();
/* 2357 */     return t;
/*      */   }
/*      */   public SCDParserTokenManager(SimpleCharStream stream) {
/* 2360 */     this.curLexState = 0;
/* 2361 */     this.defaultLexState = 0;
/*      */     this.input_stream = stream;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Token getNextToken() {
/* 2370 */     Token specialToken = null;
/*      */     
/* 2372 */     int curPos = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     while (true) {
/*      */       try {
/* 2379 */         this.curChar = this.input_stream.BeginToken();
/*      */       }
/* 2381 */       catch (IOException e) {
/*      */         
/* 2383 */         this.jjmatchedKind = 0;
/* 2384 */         Token matchedToken = jjFillToken();
/* 2385 */         return matchedToken;
/*      */       } 
/*      */       
/* 2388 */       try { this.input_stream.backup(0);
/* 2389 */         while (this.curChar <= ' ' && (0x100003600L & 1L << this.curChar) != 0L) {
/* 2390 */           this.curChar = this.input_stream.BeginToken();
/*      */         } }
/* 2392 */       catch (IOException e1) { continue; }
/* 2393 */        this.jjmatchedKind = Integer.MAX_VALUE;
/* 2394 */       this.jjmatchedPos = 0;
/* 2395 */       curPos = jjMoveStringLiteralDfa0_0();
/* 2396 */       if (this.jjmatchedKind != Integer.MAX_VALUE) {
/*      */         
/* 2398 */         if (this.jjmatchedPos + 1 < curPos)
/* 2399 */           this.input_stream.backup(curPos - this.jjmatchedPos - 1); 
/* 2400 */         if ((jjtoToken[this.jjmatchedKind >> 6] & 1L << (this.jjmatchedKind & 0x3F)) != 0L) {
/*      */           
/* 2402 */           Token matchedToken = jjFillToken();
/* 2403 */           return matchedToken;
/*      */         } 
/*      */         
/*      */         continue;
/*      */       } 
/*      */       break;
/*      */     } 
/* 2410 */     int error_line = this.input_stream.getEndLine();
/* 2411 */     int error_column = this.input_stream.getEndColumn();
/* 2412 */     String error_after = null;
/* 2413 */     boolean EOFSeen = false; try {
/* 2414 */       this.input_stream.readChar(); this.input_stream.backup(1);
/* 2415 */     } catch (IOException e1) {
/* 2416 */       EOFSeen = true;
/* 2417 */       error_after = (curPos <= 1) ? "" : this.input_stream.GetImage();
/* 2418 */       if (this.curChar == '\n' || this.curChar == '\r') {
/* 2419 */         error_line++;
/* 2420 */         error_column = 0;
/*      */       } else {
/*      */         
/* 2423 */         error_column++;
/*      */       } 
/* 2425 */     }  if (!EOFSeen) {
/* 2426 */       this.input_stream.backup(1);
/* 2427 */       error_after = (curPos <= 1) ? "" : this.input_stream.GetImage();
/*      */     } 
/* 2429 */     throw new TokenMgrError(EOFSeen, this.curLexState, error_line, error_column, error_after, this.curChar, 0);
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\scd\SCDParserTokenManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */