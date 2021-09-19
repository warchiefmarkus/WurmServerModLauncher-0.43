/*    */ package org.kohsuke.rngom.binary;
/*    */ 
/*    */ import org.kohsuke.rngom.nc.NameClass;
/*    */ import org.relaxng.datatype.Datatype;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ public class SchemaPatternBuilder
/*    */   extends PatternBuilder {
/*  9 */   private final Pattern unexpandedNotAllowed = new NotAllowedPattern()
/*    */     {
/*    */       boolean isNotAllowed() {
/* 12 */         return false;
/*    */       }
/*    */       Pattern expand(SchemaPatternBuilder b) {
/* 15 */         return b.makeNotAllowed();
/*    */       }
/*    */     };
/*    */   private boolean idTypes;
/* 19 */   private final TextPattern text = new TextPattern();
/* 20 */   private final PatternInterner schemaInterner = new PatternInterner();
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean hasIdTypes() {
/* 25 */     return this.idTypes;
/*    */   }
/*    */   
/*    */   Pattern makeElement(NameClass nameClass, Pattern content, Locator loc) {
/* 29 */     Pattern p = new ElementPattern(nameClass, content, loc);
/* 30 */     return this.schemaInterner.intern(p);
/*    */   }
/*    */   
/*    */   Pattern makeAttribute(NameClass nameClass, Pattern value, Locator loc) {
/* 34 */     if (value == this.notAllowed)
/* 35 */       return value; 
/* 36 */     Pattern p = new AttributePattern(nameClass, value, loc);
/* 37 */     return this.schemaInterner.intern(p);
/*    */   }
/*    */   
/*    */   Pattern makeData(Datatype dt) {
/* 41 */     noteDatatype(dt);
/* 42 */     Pattern p = new DataPattern(dt);
/* 43 */     return this.schemaInterner.intern(p);
/*    */   }
/*    */   
/*    */   Pattern makeDataExcept(Datatype dt, Pattern except, Locator loc) {
/* 47 */     noteDatatype(dt);
/* 48 */     Pattern p = new DataExceptPattern(dt, except, loc);
/* 49 */     return this.schemaInterner.intern(p);
/*    */   }
/*    */   
/*    */   Pattern makeValue(Datatype dt, Object obj) {
/* 53 */     noteDatatype(dt);
/* 54 */     Pattern p = new ValuePattern(dt, obj);
/* 55 */     return this.schemaInterner.intern(p);
/*    */   }
/*    */   
/*    */   Pattern makeText() {
/* 59 */     return this.text;
/*    */   }
/*    */   
/*    */   Pattern makeOneOrMore(Pattern p) {
/* 63 */     if (p == this.text)
/* 64 */       return p; 
/* 65 */     return super.makeOneOrMore(p);
/*    */   }
/*    */   
/*    */   Pattern makeUnexpandedNotAllowed() {
/* 69 */     return this.unexpandedNotAllowed;
/*    */   }
/*    */   
/*    */   Pattern makeError() {
/* 73 */     Pattern p = new ErrorPattern();
/* 74 */     return this.schemaInterner.intern(p);
/*    */   }
/*    */   
/*    */   Pattern makeChoice(Pattern p1, Pattern p2) {
/* 78 */     if (p1 == this.notAllowed || p1 == p2)
/* 79 */       return p2; 
/* 80 */     if (p2 == this.notAllowed)
/* 81 */       return p1; 
/* 82 */     return super.makeChoice(p1, p2);
/*    */   }
/*    */   
/*    */   Pattern makeList(Pattern p, Locator loc) {
/* 86 */     if (p == this.notAllowed)
/* 87 */       return p; 
/* 88 */     Pattern p1 = new ListPattern(p, loc);
/* 89 */     return this.schemaInterner.intern(p1);
/*    */   }
/*    */   
/*    */   Pattern makeMixed(Pattern p) {
/* 93 */     return makeInterleave(this.text, p);
/*    */   }
/*    */   
/*    */   private void noteDatatype(Datatype dt) {
/* 97 */     if (dt.getIdType() != 0)
/* 98 */       this.idTypes = true; 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\binary\SchemaPatternBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */