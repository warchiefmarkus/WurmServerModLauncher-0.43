/*    */ package org.kohsuke.rngom.binary;
/*    */ 
/*    */ import org.kohsuke.rngom.binary.visitor.PatternFunction;
/*    */ import org.kohsuke.rngom.binary.visitor.PatternVisitor;
/*    */ import org.kohsuke.rngom.nc.NameClass;
/*    */ import org.kohsuke.rngom.nc.SimpleNameClass;
/*    */ import org.xml.sax.Locator;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public final class AttributePattern extends Pattern {
/*    */   private NameClass nameClass;
/*    */   private Pattern p;
/*    */   private Locator loc;
/*    */   
/*    */   AttributePattern(NameClass nameClass, Pattern value, Locator loc) {
/* 16 */     super(false, 0, combineHashCode(29, nameClass.hashCode(), value.hashCode()));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 21 */     this.nameClass = nameClass;
/* 22 */     this.p = value;
/* 23 */     this.loc = loc;
/*    */   }
/*    */   
/*    */   Pattern expand(SchemaPatternBuilder b) {
/* 27 */     Pattern ep = this.p.expand(b);
/* 28 */     if (ep != this.p) {
/* 29 */       return b.makeAttribute(this.nameClass, ep, this.loc);
/*    */     }
/* 31 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   void checkRestrictions(int context, DuplicateAttributeDetector dad, Alphabet alpha) throws RestrictionViolationException {
/* 36 */     switch (context) {
/*    */       case 0:
/* 38 */         throw new RestrictionViolationException("start_contains_attribute");
/*    */       case 1:
/* 40 */         if (this.nameClass.isOpen())
/* 41 */           throw new RestrictionViolationException("open_name_class_not_repeated"); 
/*    */         break;
/*    */       case 3:
/* 44 */         throw new RestrictionViolationException("one_or_more_contains_group_contains_attribute");
/*    */       case 4:
/* 46 */         throw new RestrictionViolationException("one_or_more_contains_interleave_contains_attribute");
/*    */       case 6:
/* 48 */         throw new RestrictionViolationException("list_contains_attribute");
/*    */       case 5:
/* 50 */         throw new RestrictionViolationException("attribute_contains_attribute");
/*    */       case 7:
/* 52 */         throw new RestrictionViolationException("data_except_contains_attribute");
/*    */     } 
/* 54 */     if (!dad.addAttribute(this.nameClass)) {
/* 55 */       if (this.nameClass instanceof SimpleNameClass) {
/* 56 */         throw new RestrictionViolationException("duplicate_attribute_detail", ((SimpleNameClass)this.nameClass).name);
/*    */       }
/* 58 */       throw new RestrictionViolationException("duplicate_attribute");
/*    */     } 
/*    */     try {
/* 61 */       this.p.checkRestrictions(5, null, null);
/*    */     }
/* 63 */     catch (RestrictionViolationException e) {
/* 64 */       e.maybeSetLocator(this.loc);
/* 65 */       throw e;
/*    */     } 
/*    */   }
/*    */   
/*    */   boolean samePattern(Pattern other) {
/* 70 */     if (!(other instanceof AttributePattern))
/* 71 */       return false; 
/* 72 */     AttributePattern ap = (AttributePattern)other;
/* 73 */     return (this.nameClass.equals(ap.nameClass) && this.p == ap.p);
/*    */   }
/*    */   
/*    */   void checkRecursion(int depth) throws SAXException {
/* 77 */     this.p.checkRecursion(depth);
/*    */   }
/*    */   
/*    */   public void accept(PatternVisitor visitor) {
/* 81 */     visitor.visitAttribute(this.nameClass, this.p);
/*    */   }
/*    */   
/*    */   public Object apply(PatternFunction f) {
/* 85 */     return f.caseAttribute(this);
/*    */   }
/*    */   
/*    */   public Pattern getContent() {
/* 89 */     return this.p;
/*    */   }
/*    */   
/*    */   public NameClass getNameClass() {
/* 93 */     return this.nameClass;
/*    */   }
/*    */   
/*    */   public Locator getLocator() {
/* 97 */     return this.loc;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\binary\AttributePattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */