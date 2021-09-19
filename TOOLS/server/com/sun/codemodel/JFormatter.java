/*     */ package com.sun.codemodel;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class JFormatter
/*     */ {
/*     */   private HashMap<String, ReferenceList> collectedReferences;
/*     */   private HashSet<JClass> importedClasses;
/*     */   
/*     */   private enum Mode
/*     */   {
/*  51 */     COLLECTING,
/*     */ 
/*     */ 
/*     */     
/*  55 */     PRINTING;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   private Mode mode = Mode.PRINTING;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int indentLevel;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String indentSpace;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final PrintWriter pw;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private char lastChar;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean atBeginningOfLine;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JPackage javaLang;
/*     */ 
/*     */ 
/*     */   
/*     */   static final char CLOSE_TYPE_ARGS = '￿';
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JFormatter(PrintWriter s) {
/* 102 */     this(s, "    ");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JFormatter(Writer w) {
/* 110 */     this(new PrintWriter(w));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 117 */     this.pw.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPrinting() {
/* 127 */     return (this.mode == Mode.PRINTING);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JFormatter o() {
/* 134 */     this.indentLevel--;
/* 135 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JFormatter i() {
/* 142 */     this.indentLevel++;
/* 143 */     return this;
/*     */   }
/*     */   
/*     */   private boolean needSpace(char c1, char c2) {
/* 147 */     if (c1 == ']' && c2 == '{') return true; 
/* 148 */     if (c1 == ';') return true; 
/* 149 */     if (c1 == Character.MAX_VALUE) {
/*     */       
/* 151 */       if (c2 == '(')
/* 152 */         return false; 
/* 153 */       return true;
/*     */     } 
/* 155 */     if (c1 == ')' && c2 == '{') return true; 
/* 156 */     if (c1 == ',' || c1 == '=') return true; 
/* 157 */     if (c2 == '=') return true; 
/* 158 */     if (Character.isDigit(c1)) {
/* 159 */       if (c2 == '(' || c2 == ')' || c2 == ';' || c2 == ',')
/* 160 */         return false; 
/* 161 */       return true;
/*     */     } 
/* 163 */     if (Character.isJavaIdentifierPart(c1)) {
/* 164 */       switch (c2) {
/*     */         case '+':
/*     */         case '>':
/*     */         case '@':
/*     */         case '{':
/*     */         case '}':
/* 170 */           return true;
/*     */       } 
/* 172 */       return Character.isJavaIdentifierStart(c2);
/*     */     } 
/*     */     
/* 175 */     if (Character.isJavaIdentifierStart(c2)) {
/* 176 */       switch (c1) {
/*     */         case ')':
/*     */         case '+':
/*     */         case ']':
/*     */         case '}':
/* 181 */           return true;
/*     */       } 
/* 183 */       return false;
/*     */     } 
/*     */     
/* 186 */     if (Character.isDigit(c2)) {
/* 187 */       if (c1 == '(') return false; 
/* 188 */       return true;
/*     */     } 
/* 190 */     return false;
/*     */   }
/*     */   
/* 193 */   public JFormatter(PrintWriter s, String space) { this.lastChar = Character.MIN_VALUE;
/* 194 */     this.atBeginningOfLine = true; this.pw = s;
/*     */     this.indentSpace = space;
/*     */     this.collectedReferences = new HashMap<String, ReferenceList>();
/* 197 */     this.importedClasses = new HashSet<JClass>(); } private void spaceIfNeeded(char c) { if (this.atBeginningOfLine) {
/* 198 */       for (int i = 0; i < this.indentLevel; i++)
/* 199 */         this.pw.print(this.indentSpace); 
/* 200 */       this.atBeginningOfLine = false;
/* 201 */     } else if (this.lastChar != '\000' && needSpace(this.lastChar, c)) {
/* 202 */       this.pw.print(' ');
/*     */     }  }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JFormatter p(char c) {
/* 211 */     if (this.mode == Mode.PRINTING) {
/* 212 */       if (c == Character.MAX_VALUE) {
/* 213 */         this.pw.print('>');
/*     */       } else {
/* 215 */         spaceIfNeeded(c);
/* 216 */         this.pw.print(c);
/*     */       } 
/* 218 */       this.lastChar = c;
/*     */     } 
/* 220 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JFormatter p(String s) {
/* 229 */     if (this.mode == Mode.PRINTING) {
/* 230 */       spaceIfNeeded(s.charAt(0));
/* 231 */       this.pw.print(s);
/* 232 */       this.lastChar = s.charAt(s.length() - 1);
/*     */     } 
/* 234 */     return this;
/*     */   }
/*     */   
/*     */   public JFormatter t(JType type) {
/* 238 */     if (type.isReference()) {
/* 239 */       return t((JClass)type);
/*     */     }
/* 241 */     return g(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JFormatter t(JClass type) {
/*     */     String shortName;
/*     */     ReferenceList tl;
/* 253 */     switch (this.mode) {
/*     */ 
/*     */       
/*     */       case PRINTING:
/* 257 */         if (this.importedClasses.contains(type)) {
/* 258 */           p(type.name()); break;
/*     */         } 
/* 260 */         if (type.outer() != null) {
/* 261 */           t(type.outer()).p('.').p(type.name()); break;
/*     */         } 
/* 263 */         p(type.fullName());
/*     */         break;
/*     */       
/*     */       case COLLECTING:
/* 267 */         shortName = type.name();
/* 268 */         if (this.collectedReferences.containsKey(shortName)) {
/* 269 */           ((ReferenceList)this.collectedReferences.get(shortName)).add(type); break;
/*     */         } 
/* 271 */         tl = new ReferenceList();
/* 272 */         tl.add(type);
/* 273 */         this.collectedReferences.put(shortName, tl);
/*     */         break;
/*     */     } 
/*     */     
/* 277 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JFormatter id(String id) {
/*     */     ReferenceList tl;
/* 284 */     switch (this.mode) {
/*     */       case PRINTING:
/* 286 */         p(id);
/*     */         break;
/*     */       
/*     */       case COLLECTING:
/* 290 */         if (this.collectedReferences.containsKey(id)) {
/* 291 */           if (!((ReferenceList)this.collectedReferences.get(id)).getClasses().isEmpty()) {
/* 292 */             for (JClass type : ((ReferenceList)this.collectedReferences.get(id)).getClasses()) {
/* 293 */               if (type.outer() != null) {
/* 294 */                 ((ReferenceList)this.collectedReferences.get(id)).setId(false);
/* 295 */                 return this;
/*     */               } 
/*     */             } 
/*     */           }
/* 299 */           ((ReferenceList)this.collectedReferences.get(id)).setId(true);
/*     */           
/*     */           break;
/*     */         } 
/* 303 */         tl = new ReferenceList();
/* 304 */         tl.setId(true);
/* 305 */         this.collectedReferences.put(id, tl);
/*     */         break;
/*     */     } 
/*     */     
/* 309 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JFormatter nl() {
/* 316 */     if (this.mode == Mode.PRINTING) {
/* 317 */       this.pw.println();
/* 318 */       this.lastChar = Character.MIN_VALUE;
/* 319 */       this.atBeginningOfLine = true;
/*     */     } 
/* 321 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JFormatter g(JGenerable g) {
/* 330 */     g.generate(this);
/* 331 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JFormatter g(Collection<? extends JGenerable> list) {
/* 338 */     boolean first = true;
/* 339 */     if (!list.isEmpty()) {
/* 340 */       for (JGenerable item : list) {
/* 341 */         if (!first)
/* 342 */           p(','); 
/* 343 */         g(item);
/* 344 */         first = false;
/*     */       } 
/*     */     }
/* 347 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JFormatter d(JDeclaration d) {
/* 356 */     d.declare(this);
/* 357 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JFormatter s(JStatement s) {
/* 366 */     s.state(this);
/* 367 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JFormatter b(JVar v) {
/* 376 */     v.bind(this);
/* 377 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void write(JDefinedClass c) {
/* 385 */     this.mode = Mode.COLLECTING;
/* 386 */     d(c);
/*     */     
/* 388 */     this.javaLang = c.owner()._package("java.lang");
/*     */ 
/*     */     
/* 391 */     for (ReferenceList tl : this.collectedReferences.values()) {
/* 392 */       if (!tl.collisions(c) && !tl.isId()) {
/* 393 */         assert tl.getClasses().size() == 1;
/*     */ 
/*     */         
/* 396 */         this.importedClasses.add(tl.getClasses().get(0));
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 401 */     this.importedClasses.add(c);
/*     */ 
/*     */     
/* 404 */     this.mode = Mode.PRINTING;
/*     */     
/* 406 */     assert c.parentContainer().isPackage() : "this method is only for a pacakge-level class";
/* 407 */     JPackage pkg = (JPackage)c.parentContainer();
/* 408 */     if (!pkg.isUnnamed()) {
/* 409 */       nl().d(pkg);
/* 410 */       nl();
/*     */     } 
/*     */ 
/*     */     
/* 414 */     JClass[] imports = (JClass[])this.importedClasses.toArray((Object[])new JClass[this.importedClasses.size()]);
/* 415 */     Arrays.sort((Object[])imports);
/* 416 */     for (JClass clazz : imports) {
/*     */ 
/*     */ 
/*     */       
/* 420 */       if (!supressImport(clazz, c)) {
/* 421 */         p("import").p(clazz.fullName()).p(';').nl();
/*     */       }
/*     */     } 
/* 424 */     nl();
/*     */     
/* 426 */     d(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean supressImport(JClass clazz, JClass c) {
/* 437 */     if (clazz._package().isUnnamed()) {
/* 438 */       return true;
/*     */     }
/* 440 */     String packageName = clazz._package().name();
/* 441 */     if (packageName.equals("java.lang")) {
/* 442 */       return true;
/*     */     }
/* 444 */     if (clazz._package() == c._package())
/*     */     {
/*     */ 
/*     */       
/* 448 */       if (clazz.outer() == null) {
/* 449 */         return true;
/*     */       }
/*     */     }
/* 452 */     return false;
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
/*     */   final class ReferenceList
/*     */   {
/* 475 */     private final ArrayList<JClass> classes = new ArrayList<JClass>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean id;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean collisions(JDefinedClass enclosingClass) {
/* 488 */       if (this.classes.size() > 1) {
/* 489 */         return true;
/*     */       }
/*     */       
/* 492 */       if (this.id && this.classes.size() != 0) {
/* 493 */         return true;
/*     */       }
/* 495 */       for (JClass c : this.classes) {
/* 496 */         if (c._package() == JFormatter.this.javaLang) {
/*     */           
/* 498 */           Iterator<JDefinedClass> itr = enclosingClass._package().classes();
/* 499 */           while (itr.hasNext()) {
/*     */ 
/*     */ 
/*     */             
/* 503 */             JDefinedClass n = itr.next();
/* 504 */             if (n.name().equals(c.name()))
/* 505 */               return true; 
/*     */           } 
/*     */         } 
/* 508 */         if (c.outer() != null) {
/* 509 */           return true;
/*     */         }
/*     */       } 
/* 512 */       return false;
/*     */     }
/*     */     
/*     */     public void add(JClass clazz) {
/* 516 */       if (!this.classes.contains(clazz))
/* 517 */         this.classes.add(clazz); 
/*     */     }
/*     */     
/*     */     public List<JClass> getClasses() {
/* 521 */       return this.classes;
/*     */     }
/*     */     
/*     */     public void setId(boolean value) {
/* 525 */       this.id = value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isId() {
/* 533 */       return (this.id && this.classes.size() == 0);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JFormatter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */