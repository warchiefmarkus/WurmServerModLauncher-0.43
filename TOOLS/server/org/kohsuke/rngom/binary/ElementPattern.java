/*    */ package org.kohsuke.rngom.binary;
/*    */ 
/*    */ import org.kohsuke.rngom.binary.visitor.PatternFunction;
/*    */ import org.kohsuke.rngom.binary.visitor.PatternVisitor;
/*    */ import org.kohsuke.rngom.nc.NameClass;
/*    */ import org.xml.sax.Locator;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public final class ElementPattern extends Pattern {
/*    */   private Pattern p;
/*    */   private NameClass origNameClass;
/*    */   private NameClass nameClass;
/*    */   private boolean expanded = false;
/*    */   private boolean checkedRestrictions = false;
/*    */   private Locator loc;
/*    */   
/*    */   ElementPattern(NameClass nameClass, Pattern p, Locator loc) {
/* 18 */     super(false, 1, combineHashCode(23, nameClass.hashCode(), p.hashCode()));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 23 */     this.nameClass = nameClass;
/* 24 */     this.origNameClass = nameClass;
/* 25 */     this.p = p;
/* 26 */     this.loc = loc;
/*    */   }
/*    */ 
/*    */   
/*    */   void checkRestrictions(int context, DuplicateAttributeDetector dad, Alphabet alpha) throws RestrictionViolationException {
/* 31 */     if (alpha != null)
/* 32 */       alpha.addElement(this.origNameClass); 
/* 33 */     if (this.checkedRestrictions)
/*    */       return; 
/* 35 */     switch (context) {
/*    */       case 7:
/* 37 */         throw new RestrictionViolationException("data_except_contains_element");
/*    */       case 6:
/* 39 */         throw new RestrictionViolationException("list_contains_element");
/*    */       case 5:
/* 41 */         throw new RestrictionViolationException("attribute_contains_element");
/*    */     } 
/* 43 */     this.checkedRestrictions = true;
/*    */     try {
/* 45 */       this.p.checkRestrictions(1, new DuplicateAttributeDetector(), null);
/*    */     }
/* 47 */     catch (RestrictionViolationException e) {
/* 48 */       this.checkedRestrictions = false;
/* 49 */       e.maybeSetLocator(this.loc);
/* 50 */       throw e;
/*    */     } 
/*    */   }
/*    */   
/*    */   Pattern expand(SchemaPatternBuilder b) {
/* 55 */     if (!this.expanded) {
/* 56 */       this.expanded = true;
/* 57 */       this.p = this.p.expand(b);
/* 58 */       if (this.p.isNotAllowed())
/* 59 */         this.nameClass = NameClass.NULL; 
/*    */     } 
/* 61 */     return this;
/*    */   }
/*    */   
/*    */   boolean samePattern(Pattern other) {
/* 65 */     if (!(other instanceof ElementPattern))
/* 66 */       return false; 
/* 67 */     ElementPattern ep = (ElementPattern)other;
/* 68 */     return (this.nameClass.equals(ep.nameClass) && this.p == ep.p);
/*    */   }
/*    */   
/*    */   void checkRecursion(int depth) throws SAXException {
/* 72 */     this.p.checkRecursion(depth + 1);
/*    */   }
/*    */   
/*    */   public void accept(PatternVisitor visitor) {
/* 76 */     visitor.visitElement(this.nameClass, this.p);
/*    */   }
/*    */   
/*    */   public Object apply(PatternFunction f) {
/* 80 */     return f.caseElement(this);
/*    */   }
/*    */   
/*    */   void setContent(Pattern p) {
/* 84 */     this.p = p;
/*    */   }
/*    */   
/*    */   public Pattern getContent() {
/* 88 */     return this.p;
/*    */   }
/*    */   
/*    */   public NameClass getNameClass() {
/* 92 */     return this.nameClass;
/*    */   }
/*    */   
/*    */   public Locator getLocator() {
/* 96 */     return this.loc;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\binary\ElementPattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */