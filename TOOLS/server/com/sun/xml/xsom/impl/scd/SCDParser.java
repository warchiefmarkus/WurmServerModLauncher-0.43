/*     */ package com.sun.xml.xsom.impl.scd;
/*     */ public class SCDParser implements SCDParserConstants {
/*     */   private NamespaceContext nsc;
/*     */   public SCDParserTokenManager token_source;
/*     */   SimpleCharStream jj_input_stream;
/*     */   public Token token;
/*     */   public Token jj_nt;
/*     */   private int jj_ntk;
/*     */   private int jj_gen;
/*     */   
/*     */   public SCDParser(String text, NamespaceContext nsc) {
/*  12 */     this(new StringReader(text));
/*  13 */     this.nsc = nsc;
/*     */   }
/*     */   private String trim(String s) {
/*  16 */     return s.substring(1, s.length() - 1);
/*     */   }
/*     */   private String resolvePrefix(String prefix) throws ParseException {
/*     */     try {
/*  20 */       String r = this.nsc.getNamespaceURI(prefix);
/*     */       
/*  22 */       if (prefix.equals(""))
/*  23 */         return r; 
/*  24 */       if (!r.equals(""))
/*  25 */         return r; 
/*  26 */     } catch (IllegalArgumentException e) {}
/*     */ 
/*     */     
/*  29 */     throw new ParseException("Unbound prefix: " + prefix);
/*     */   }
/*     */ 
/*     */   
/*     */   public final UName QName() throws ParseException {
/*  34 */     Token l = null;
/*  35 */     Token p = jj_consume_token(12);
/*  36 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*     */       case 15:
/*  38 */         jj_consume_token(15);
/*  39 */         l = jj_consume_token(12);
/*     */         break;
/*     */       default:
/*  42 */         this.jj_la1[0] = this.jj_gen;
/*     */         break;
/*     */     } 
/*  45 */     if (l == null) {
/*  46 */       return new UName(resolvePrefix(""), p.image);
/*     */     }
/*  48 */     return new UName(resolvePrefix(p.image), l.image);
/*     */   }
/*     */ 
/*     */   
/*     */   public final String Prefix() throws ParseException {
/*     */     Token p;
/*  54 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*     */       case 12:
/*  56 */         p = jj_consume_token(12);
/*  57 */         return resolvePrefix(p.image);
/*     */     } 
/*     */     
/*  60 */     this.jj_la1[1] = this.jj_gen;
/*  61 */     return resolvePrefix("");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final List RelativeSchemaComponentPath() throws ParseException {
/*  67 */     List<Step.Any> steps = new ArrayList();
/*     */     
/*  69 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*     */       case 16:
/*     */       case 17:
/*  72 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*     */           case 16:
/*  74 */             jj_consume_token(16);
/*  75 */             steps.add(new Step.Any((Axis)Axis.ROOT));
/*     */             break;
/*     */           case 17:
/*  78 */             jj_consume_token(17);
/*  79 */             steps.add(new Step.Any(Axis.DESCENDANTS));
/*     */             break;
/*     */         } 
/*  82 */         this.jj_la1[2] = this.jj_gen;
/*  83 */         jj_consume_token(-1);
/*  84 */         throw new ParseException();
/*     */ 
/*     */       
/*     */       default:
/*  88 */         this.jj_la1[3] = this.jj_gen;
/*     */         break;
/*     */     } 
/*  91 */     Step s = Step();
/*  92 */     steps.add(s);
/*     */     
/*     */     while (true) {
/*  95 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*     */         case 16:
/*     */         case 17:
/*     */           break;
/*     */         
/*     */         default:
/* 101 */           this.jj_la1[4] = this.jj_gen;
/*     */           break;
/*     */       } 
/* 104 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*     */         case 16:
/* 106 */           jj_consume_token(16);
/*     */           break;
/*     */         case 17:
/* 109 */           jj_consume_token(17);
/* 110 */           steps.add(new Step.Any(Axis.DESCENDANTS));
/*     */           break;
/*     */         default:
/* 113 */           this.jj_la1[5] = this.jj_gen;
/* 114 */           jj_consume_token(-1);
/* 115 */           throw new ParseException();
/*     */       } 
/* 117 */       s = Step();
/* 118 */       steps.add(s);
/*     */     } 
/* 120 */     return steps;
/*     */   }
/*     */   
/*     */   public final Step Step() throws ParseException { Step s;
/*     */     String p;
/*     */     Token n;
/* 126 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*     */       case 18:
/*     */       case 19:
/* 129 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*     */           case 18:
/* 131 */             jj_consume_token(18);
/*     */             break;
/*     */           case 19:
/* 134 */             jj_consume_token(19);
/*     */             break;
/*     */           default:
/* 137 */             this.jj_la1[6] = this.jj_gen;
/* 138 */             jj_consume_token(-1);
/* 139 */             throw new ParseException();
/*     */         } 
/* 141 */         s = NameOrWildcard(Axis.ATTRIBUTE);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 323 */         return s;case 12: case 20: case 45: switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) { case 20: jj_consume_token(20); break;default: this.jj_la1[7] = this.jj_gen; break; }  s = NameOrWildcard(Axis.ELEMENT); switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) { case 13: Predicate(s); return s; }  this.jj_la1[8] = this.jj_gen; return s;case 21: jj_consume_token(21); s = NameOrWildcard(Axis.SUBSTITUTION_GROUP); return s;case 22: case 23: switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) { case 22: jj_consume_token(22); break;case 23: jj_consume_token(23); break;default: this.jj_la1[9] = this.jj_gen; jj_consume_token(-1); throw new ParseException(); }  s = NameOrWildcardOrAnonymous(Axis.TYPE_DEFINITION); return s;case 24: jj_consume_token(24); s = NameOrWildcard(Axis.BASETYPE); return s;case 25: jj_consume_token(25); s = NameOrWildcard(Axis.PRIMITIVE_TYPE); return s;case 26: jj_consume_token(26); s = NameOrWildcardOrAnonymous(Axis.ITEM_TYPE); return s;case 27: jj_consume_token(27); s = NameOrWildcardOrAnonymous(Axis.MEMBER_TYPE); switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) { case 13: Predicate(s); return s; }  this.jj_la1[10] = this.jj_gen; return s;case 28: jj_consume_token(28); s = NameOrWildcardOrAnonymous(Axis.SCOPE); return s;case 29: jj_consume_token(29); s = NameOrWildcard(Axis.ATTRIBUTE_GROUP); return s;case 30: jj_consume_token(30); s = NameOrWildcard(Axis.MODEL_GROUP_DECL); return s;case 31: jj_consume_token(31); s = NameOrWildcard(Axis.IDENTITY_CONSTRAINT); return s;case 32: jj_consume_token(32); s = NameOrWildcard(Axis.REFERENCED_KEY); return s;case 33: jj_consume_token(33); s = NameOrWildcard(Axis.NOTATION); return s;case 34: jj_consume_token(34); s = new Step.Any((Axis)Axis.MODELGROUP_SEQUENCE); switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) { case 13: Predicate(s); return s; }  this.jj_la1[11] = this.jj_gen; return s;case 35: jj_consume_token(35); s = new Step.Any((Axis)Axis.MODELGROUP_CHOICE); switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) { case 13: Predicate(s); return s; }  this.jj_la1[12] = this.jj_gen; return s;case 36: jj_consume_token(36); s = new Step.Any((Axis)Axis.MODELGROUP_ALL); switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) { case 13: Predicate(s); return s; }  this.jj_la1[13] = this.jj_gen; return s;case 37: jj_consume_token(37); s = new Step.Any((Axis)Axis.MODELGROUP_ANY); switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) { case 13: Predicate(s); return s; }  this.jj_la1[14] = this.jj_gen; return s;case 38: jj_consume_token(38); s = new Step.Any((Axis)Axis.WILDCARD); switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) { case 13: Predicate(s); return s; }  this.jj_la1[15] = this.jj_gen; return s;case 39: jj_consume_token(39); s = new Step.Any((Axis)Axis.ATTRIBUTE_WILDCARD); return s;case 40: jj_consume_token(40); s = new Step.Any((Axis)Axis.FACET); return s;case 41: jj_consume_token(41); n = jj_consume_token(14); s = new Step.Facet(Axis.FACET, n.image); return s;case 42: jj_consume_token(42); s = new Step.Any(Axis.DESCENDANTS); return s;case 43: jj_consume_token(43); p = Prefix(); s = new Step.Schema(Axis.X_SCHEMA, p); return s;case 44: jj_consume_token(44); s = new Step.Any((Axis)Axis.X_SCHEMA); return s;
/*     */     } 
/*     */     this.jj_la1[16] = this.jj_gen;
/*     */     jj_consume_token(-1);
/*     */     throw new ParseException(); } public final Step NameOrWildcard(Axis<? extends XSDeclaration> a) throws ParseException {
/*     */     UName un;
/* 329 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*     */       case 12:
/* 331 */         un = QName();
/* 332 */         return new Step.Named(a, un);
/*     */       
/*     */       case 45:
/* 335 */         jj_consume_token(45);
/* 336 */         return new Step.Any((Axis)a);
/*     */     } 
/*     */     
/* 339 */     this.jj_la1[17] = this.jj_gen;
/* 340 */     jj_consume_token(-1);
/* 341 */     throw new ParseException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Step NameOrWildcardOrAnonymous(Axis<? extends XSDeclaration> a) throws ParseException {
/*     */     UName un;
/* 348 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*     */       case 12:
/* 350 */         un = QName();
/* 351 */         return new Step.Named(a, un);
/*     */       
/*     */       case 45:
/* 354 */         jj_consume_token(45);
/* 355 */         return new Step.Any((Axis)a);
/*     */       
/*     */       case 46:
/* 358 */         jj_consume_token(46);
/* 359 */         return new Step.AnonymousType((Axis)a);
/*     */     } 
/*     */     
/* 362 */     this.jj_la1[18] = this.jj_gen;
/* 363 */     jj_consume_token(-1);
/* 364 */     throw new ParseException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int Predicate(Step s) throws ParseException {
/* 371 */     Token t = jj_consume_token(13);
/* 372 */     return s.predicate = Integer.parseInt(trim(t.image));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 381 */   private final int[] jj_la1 = new int[19]; private static int[] jj_la1_0; private static int[] jj_la1_1; private Vector jj_expentries; private int[] jj_expentry;
/*     */   private int jj_kind;
/*     */   
/*     */   static {
/* 385 */     jj_la1_0();
/* 386 */     jj_la1_1();
/*     */   }
/*     */   private static void jj_la1_0() {
/* 389 */     jj_la1_0 = new int[] { 32768, 4096, 196608, 196608, 196608, 196608, 786432, 1048576, 8192, 12582912, 8192, 8192, 8192, 8192, 8192, 8192, -258048, 4096, 4096 };
/*     */   }
/*     */   private static void jj_la1_1() {
/* 392 */     jj_la1_1 = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 16383, 8192, 24576 };
/*     */   }
/*     */   
/*     */   public SCDParser(InputStream stream) {
/* 396 */     this(stream, (String)null);
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
/*     */   public void ReInit(InputStream stream) {
/* 408 */     ReInit(stream, null);
/*     */   } public void ReInit(InputStream stream, String encoding) {
/*     */     
/* 411 */     try { this.jj_input_stream.ReInit(stream, encoding, 1, 1); } catch (UnsupportedEncodingException e) { throw new RuntimeException(e); }
/* 412 */      this.token_source.ReInit(this.jj_input_stream);
/* 413 */     this.token = new Token();
/* 414 */     this.jj_ntk = -1;
/* 415 */     this.jj_gen = 0;
/* 416 */     for (int i = 0; i < 19; ) { this.jj_la1[i] = -1; i++; }
/*     */   
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
/*     */   public void ReInit(Reader stream) {
/* 429 */     this.jj_input_stream.ReInit(stream, 1, 1);
/* 430 */     this.token_source.ReInit(this.jj_input_stream);
/* 431 */     this.token = new Token();
/* 432 */     this.jj_ntk = -1;
/* 433 */     this.jj_gen = 0;
/* 434 */     for (int i = 0; i < 19; ) { this.jj_la1[i] = -1; i++; }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void ReInit(SCDParserTokenManager tm) {
/* 446 */     this.token_source = tm;
/* 447 */     this.token = new Token();
/* 448 */     this.jj_ntk = -1;
/* 449 */     this.jj_gen = 0;
/* 450 */     for (int i = 0; i < 19; ) { this.jj_la1[i] = -1; i++; }
/*     */   
/*     */   }
/*     */   private final Token jj_consume_token(int kind) throws ParseException {
/*     */     Token oldToken;
/* 455 */     if ((oldToken = this.token).next != null) { this.token = this.token.next; }
/* 456 */     else { this.token = this.token.next = this.token_source.getNextToken(); }
/* 457 */      this.jj_ntk = -1;
/* 458 */     if (this.token.kind == kind) {
/* 459 */       this.jj_gen++;
/* 460 */       return this.token;
/*     */     } 
/* 462 */     this.token = oldToken;
/* 463 */     this.jj_kind = kind;
/* 464 */     throw generateParseException();
/*     */   }
/*     */   
/*     */   public final Token getNextToken() {
/* 468 */     if (this.token.next != null) { this.token = this.token.next; }
/* 469 */     else { this.token = this.token.next = this.token_source.getNextToken(); }
/* 470 */      this.jj_ntk = -1;
/* 471 */     this.jj_gen++;
/* 472 */     return this.token;
/*     */   }
/*     */   
/*     */   public final Token getToken(int index) {
/* 476 */     Token t = this.token;
/* 477 */     for (int i = 0; i < index; i++) {
/* 478 */       if (t.next != null) { t = t.next; }
/* 479 */       else { t = t.next = this.token_source.getNextToken(); }
/*     */     
/* 481 */     }  return t;
/*     */   }
/*     */   
/*     */   private final int jj_ntk() {
/* 485 */     if ((this.jj_nt = this.token.next) == null) {
/* 486 */       return this.jj_ntk = (this.token.next = this.token_source.getNextToken()).kind;
/*     */     }
/* 488 */     return this.jj_ntk = this.jj_nt.kind;
/*     */   }
/*     */   
/* 491 */   public SCDParser(InputStream stream, String encoding) { this.jj_expentries = new Vector();
/*     */     
/* 493 */     this.jj_kind = -1; try { this.jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch (UnsupportedEncodingException e) { throw new RuntimeException(e); }  this.token_source = new SCDParserTokenManager(this.jj_input_stream); this.token = new Token(); this.jj_ntk = -1; this.jj_gen = 0; for (int i = 0; i < 19; ) { this.jj_la1[i] = -1; i++; }  } public SCDParser(Reader stream) { this.jj_expentries = new Vector(); this.jj_kind = -1; this.jj_input_stream = new SimpleCharStream(stream, 1, 1); this.token_source = new SCDParserTokenManager(this.jj_input_stream); this.token = new Token(); this.jj_ntk = -1; this.jj_gen = 0; for (int i = 0; i < 19; ) { this.jj_la1[i] = -1; i++; }  } public SCDParser(SCDParserTokenManager tm) { this.jj_expentries = new Vector(); this.jj_kind = -1; this.token_source = tm; this.token = new Token(); this.jj_ntk = -1; this.jj_gen = 0; for (int i = 0; i < 19; ) {
/*     */       this.jj_la1[i] = -1;
/*     */       i++;
/* 496 */     }  } public ParseException generateParseException() { this.jj_expentries.removeAllElements();
/* 497 */     boolean[] la1tokens = new boolean[47]; int i;
/* 498 */     for (i = 0; i < 47; i++) {
/* 499 */       la1tokens[i] = false;
/*     */     }
/* 501 */     if (this.jj_kind >= 0) {
/* 502 */       la1tokens[this.jj_kind] = true;
/* 503 */       this.jj_kind = -1;
/*     */     } 
/* 505 */     for (i = 0; i < 19; i++) {
/* 506 */       if (this.jj_la1[i] == this.jj_gen) {
/* 507 */         for (int k = 0; k < 32; k++) {
/* 508 */           if ((jj_la1_0[i] & 1 << k) != 0) {
/* 509 */             la1tokens[k] = true;
/*     */           }
/* 511 */           if ((jj_la1_1[i] & 1 << k) != 0) {
/* 512 */             la1tokens[32 + k] = true;
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/* 517 */     for (i = 0; i < 47; i++) {
/* 518 */       if (la1tokens[i]) {
/* 519 */         this.jj_expentry = new int[1];
/* 520 */         this.jj_expentry[0] = i;
/* 521 */         this.jj_expentries.addElement(this.jj_expentry);
/*     */       } 
/*     */     } 
/* 524 */     int[][] exptokseq = new int[this.jj_expentries.size()][];
/* 525 */     for (int j = 0; j < this.jj_expentries.size(); j++) {
/* 526 */       exptokseq[j] = this.jj_expentries.elementAt(j);
/*     */     }
/* 528 */     return new ParseException(this.token, exptokseq, tokenImage); }
/*     */ 
/*     */   
/*     */   public final void enable_tracing() {}
/*     */   
/*     */   public final void disable_tracing() {}
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\scd\SCDParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */