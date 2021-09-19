/*     */ package org.kohsuke.rngom.binary;
/*     */ 
/*     */ import org.kohsuke.rngom.binary.visitor.PatternFunction;
/*     */ import org.kohsuke.rngom.binary.visitor.PatternVisitor;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ public class RefPattern extends Pattern {
/*     */   private Pattern p;
/*     */   private Locator refLoc;
/*     */   private String name;
/*  13 */   private int checkRecursionDepth = -1;
/*     */   private boolean combineImplicit = false;
/*  15 */   private byte combineType = 0;
/*  16 */   private byte replacementStatus = 0;
/*     */   
/*     */   private boolean expanded = false;
/*     */   
/*     */   static final byte REPLACEMENT_KEEP = 0;
/*     */   static final byte REPLACEMENT_REQUIRE = 1;
/*     */   static final byte REPLACEMENT_IGNORE = 2;
/*     */   static final byte COMBINE_NONE = 0;
/*     */   static final byte COMBINE_CHOICE = 1;
/*     */   static final byte COMBINE_INTERLEAVE = 2;
/*     */   
/*     */   RefPattern(String name) {
/*  28 */     this.name = name;
/*     */   }
/*     */   
/*     */   Pattern getPattern() {
/*  32 */     return this.p;
/*     */   }
/*     */   
/*     */   void setPattern(Pattern p) {
/*  36 */     this.p = p;
/*     */   }
/*     */   
/*     */   Locator getRefLocator() {
/*  40 */     return this.refLoc;
/*     */   }
/*     */   
/*     */   void setRefLocator(Locator loc) {
/*  44 */     this.refLoc = loc;
/*     */   }
/*     */   
/*     */   void checkRecursion(int depth) throws SAXException {
/*  48 */     if (this.checkRecursionDepth == -1) {
/*  49 */       this.checkRecursionDepth = depth;
/*  50 */       this.p.checkRecursion(depth);
/*  51 */       this.checkRecursionDepth = -2;
/*     */     }
/*  53 */     else if (depth == this.checkRecursionDepth) {
/*     */       
/*  55 */       throw new SAXParseException(SchemaBuilderImpl.localizer.message("recursive_reference", this.name), this.refLoc);
/*     */     } 
/*     */   }
/*     */   
/*     */   Pattern expand(SchemaPatternBuilder b) {
/*  60 */     if (!this.expanded) {
/*  61 */       this.p = this.p.expand(b);
/*  62 */       this.expanded = true;
/*     */     } 
/*  64 */     return this.p;
/*     */   }
/*     */   
/*     */   boolean samePattern(Pattern other) {
/*  68 */     return false;
/*     */   }
/*     */   
/*     */   public void accept(PatternVisitor visitor) {
/*  72 */     this.p.accept(visitor);
/*     */   }
/*     */   
/*     */   public Object apply(PatternFunction f) {
/*  76 */     return f.caseRef(this);
/*     */   }
/*     */   
/*     */   byte getReplacementStatus() {
/*  80 */     return this.replacementStatus;
/*     */   }
/*     */   
/*     */   void setReplacementStatus(byte replacementStatus) {
/*  84 */     this.replacementStatus = replacementStatus;
/*     */   }
/*     */   
/*     */   boolean isCombineImplicit() {
/*  88 */     return this.combineImplicit;
/*     */   }
/*     */   
/*     */   void setCombineImplicit() {
/*  92 */     this.combineImplicit = true;
/*     */   }
/*     */   
/*     */   byte getCombineType() {
/*  96 */     return this.combineType;
/*     */   }
/*     */   
/*     */   void setCombineType(byte combineType) {
/* 100 */     this.combineType = combineType;
/*     */   }
/*     */   
/*     */   String getName() {
/* 104 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\binary\RefPattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */