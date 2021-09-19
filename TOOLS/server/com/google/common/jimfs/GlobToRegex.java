/*     */ package com.google.common.jimfs;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import java.util.regex.PatternSyntaxException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class GlobToRegex
/*     */ {
/*     */   public static String toRegex(String glob, String separators) {
/*  49 */     return (new GlobToRegex(glob, separators)).convert();
/*     */   }
/*     */   
/*  52 */   private static final InternalCharMatcher REGEX_RESERVED = InternalCharMatcher.anyOf("^$.?+*\\[]{}()");
/*     */   
/*     */   private final String glob;
/*     */   
/*     */   private final String separators;
/*     */   
/*     */   private final InternalCharMatcher separatorMatcher;
/*  59 */   private final StringBuilder builder = new StringBuilder();
/*  60 */   private final Deque<State> states = new ArrayDeque<>();
/*     */   private int index;
/*     */   
/*     */   private GlobToRegex(String glob, String separators) {
/*  64 */     this.glob = (String)Preconditions.checkNotNull(glob);
/*  65 */     this.separators = separators;
/*  66 */     this.separatorMatcher = InternalCharMatcher.anyOf(separators);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String convert() {
/*  77 */     pushState(NORMAL);
/*  78 */     for (this.index = 0; this.index < this.glob.length(); this.index++) {
/*  79 */       currentState().process(this, this.glob.charAt(this.index));
/*     */     }
/*  81 */     currentState().finish(this);
/*  82 */     return this.builder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void pushState(State state) {
/*  89 */     this.states.push(state);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void popState() {
/*  96 */     this.states.pop();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private State currentState() {
/* 103 */     return this.states.peek();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PatternSyntaxException syntaxError(String desc) {
/* 110 */     throw new PatternSyntaxException(desc, this.glob, this.index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void appendExact(char c) {
/* 117 */     this.builder.append(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void append(char c) {
/* 124 */     if (this.separatorMatcher.matches(c)) {
/* 125 */       appendSeparator();
/*     */     } else {
/* 127 */       appendNormal(c);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void appendNormal(char c) {
/* 135 */     if (REGEX_RESERVED.matches(c)) {
/* 136 */       this.builder.append('\\');
/*     */     }
/* 138 */     this.builder.append(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void appendSeparator() {
/* 145 */     if (this.separators.length() == 1) {
/* 146 */       appendNormal(this.separators.charAt(0));
/*     */     } else {
/* 148 */       this.builder.append('[');
/* 149 */       for (int i = 0; i < this.separators.length(); i++) {
/* 150 */         appendInBracket(this.separators.charAt(i));
/*     */       }
/* 152 */       this.builder.append("]");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void appendNonSeparator() {
/* 160 */     this.builder.append("[^");
/* 161 */     for (int i = 0; i < this.separators.length(); i++) {
/* 162 */       appendInBracket(this.separators.charAt(i));
/*     */     }
/* 164 */     this.builder.append(']');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void appendQuestionMark() {
/* 171 */     appendNonSeparator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void appendStar() {
/* 178 */     appendNonSeparator();
/* 179 */     this.builder.append('*');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void appendStarStar() {
/* 186 */     this.builder.append(".*");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void appendBracketStart() {
/* 193 */     this.builder.append('[');
/* 194 */     appendNonSeparator();
/* 195 */     this.builder.append("&&[");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void appendBracketEnd() {
/* 202 */     this.builder.append("]]");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void appendInBracket(char c) {
/* 210 */     if (c == '\\') {
/* 211 */       this.builder.append('\\');
/*     */     }
/*     */     
/* 214 */     this.builder.append(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void appendCurlyBraceStart() {
/* 221 */     this.builder.append('(');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void appendSubpatternSeparator() {
/* 228 */     this.builder.append('|');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void appendCurlyBraceEnd() {
/* 235 */     this.builder.append(')');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static abstract class State
/*     */   {
/*     */     private State() {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void finish(GlobToRegex converter) {}
/*     */ 
/*     */ 
/*     */     
/*     */     abstract void process(GlobToRegex param1GlobToRegex, char param1Char);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 257 */   private static final State NORMAL = new State()
/*     */     {
/*     */       void process(GlobToRegex converter, char c)
/*     */       {
/* 261 */         switch (c) {
/*     */           case '?':
/* 263 */             converter.appendQuestionMark();
/*     */             return;
/*     */           case '[':
/* 266 */             converter.appendBracketStart();
/* 267 */             converter.pushState(GlobToRegex.BRACKET_FIRST_CHAR);
/*     */             return;
/*     */           case '{':
/* 270 */             converter.appendCurlyBraceStart();
/* 271 */             converter.pushState(GlobToRegex.CURLY_BRACE);
/*     */             return;
/*     */           case '*':
/* 274 */             converter.pushState(GlobToRegex.STAR);
/*     */             return;
/*     */           case '\\':
/* 277 */             converter.pushState(GlobToRegex.ESCAPE);
/*     */             return;
/*     */         } 
/* 280 */         converter.append(c);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public String toString() {
/* 286 */         return "NORMAL";
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 293 */   private static final State ESCAPE = new State()
/*     */     {
/*     */       void process(GlobToRegex converter, char c)
/*     */       {
/* 297 */         converter.append(c);
/* 298 */         converter.popState();
/*     */       }
/*     */ 
/*     */       
/*     */       void finish(GlobToRegex converter) {
/* 303 */         throw converter.syntaxError("Hanging escape (\\) at end of pattern");
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 308 */         return "ESCAPE";
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 315 */   private static final State STAR = new State()
/*     */     {
/*     */       void process(GlobToRegex converter, char c)
/*     */       {
/* 319 */         if (c == '*') {
/* 320 */           converter.appendStarStar();
/* 321 */           converter.popState();
/*     */         } else {
/* 323 */           converter.appendStar();
/* 324 */           converter.popState();
/* 325 */           converter.currentState().process(converter, c);
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*     */       void finish(GlobToRegex converter) {
/* 331 */         converter.appendStar();
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 336 */         return "STAR";
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 343 */   private static final State BRACKET_FIRST_CHAR = new State()
/*     */     {
/*     */       void process(GlobToRegex converter, char c)
/*     */       {
/* 347 */         if (c == ']')
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 357 */           throw converter.syntaxError("Empty []");
/*     */         }
/* 359 */         if (c == '!') {
/* 360 */           converter.appendExact('^');
/* 361 */         } else if (c == '-') {
/* 362 */           converter.appendExact(c);
/*     */         } else {
/* 364 */           converter.appendInBracket(c);
/*     */         } 
/* 366 */         converter.popState();
/* 367 */         converter.pushState(GlobToRegex.BRACKET);
/*     */       }
/*     */ 
/*     */       
/*     */       void finish(GlobToRegex converter) {
/* 372 */         throw converter.syntaxError("Unclosed [");
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 377 */         return "BRACKET_FIRST_CHAR";
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 384 */   private static final State BRACKET = new State()
/*     */     {
/*     */       void process(GlobToRegex converter, char c)
/*     */       {
/* 388 */         if (c == ']') {
/* 389 */           converter.appendBracketEnd();
/* 390 */           converter.popState();
/*     */         } else {
/* 392 */           converter.appendInBracket(c);
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*     */       void finish(GlobToRegex converter) {
/* 398 */         throw converter.syntaxError("Unclosed [");
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 403 */         return "BRACKET";
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 410 */   private static final State CURLY_BRACE = new State()
/*     */     {
/*     */       void process(GlobToRegex converter, char c)
/*     */       {
/* 414 */         switch (c) {
/*     */           case '?':
/* 416 */             converter.appendQuestionMark();
/*     */             return;
/*     */           case '[':
/* 419 */             converter.appendBracketStart();
/* 420 */             converter.pushState(GlobToRegex.BRACKET_FIRST_CHAR);
/*     */             return;
/*     */           case '{':
/* 423 */             throw converter.syntaxError("{ not allowed in subpattern group");
/*     */           case '*':
/* 425 */             converter.pushState(GlobToRegex.STAR);
/*     */             return;
/*     */           case '\\':
/* 428 */             converter.pushState(GlobToRegex.ESCAPE);
/*     */             return;
/*     */           case '}':
/* 431 */             converter.appendCurlyBraceEnd();
/* 432 */             converter.popState();
/*     */             return;
/*     */           case ',':
/* 435 */             converter.appendSubpatternSeparator();
/*     */             return;
/*     */         } 
/* 438 */         converter.append(c);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       void finish(GlobToRegex converter) {
/* 444 */         throw converter.syntaxError("Unclosed {");
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 449 */         return "CURLY_BRACE";
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\GlobToRegex.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */