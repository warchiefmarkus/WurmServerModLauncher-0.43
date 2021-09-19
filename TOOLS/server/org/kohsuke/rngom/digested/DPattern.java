/*    */ package org.kohsuke.rngom.digested;
/*    */ 
/*    */ import org.kohsuke.rngom.ast.om.ParsedPattern;
/*    */ import org.kohsuke.rngom.parse.Parseable;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class DPattern
/*    */   implements ParsedPattern
/*    */ {
/*    */   Locator location;
/*    */   DAnnotation annotation;
/*    */   DPattern next;
/*    */   DPattern prev;
/*    */   
/*    */   public Locator getLocation() {
/* 26 */     return this.location;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DAnnotation getAnnotation() {
/* 36 */     if (this.annotation == null)
/* 37 */       return DAnnotation.EMPTY; 
/* 38 */     return this.annotation;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract boolean isNullable();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract <V> V accept(DPatternVisitor<V> paramDPatternVisitor);
/*    */ 
/*    */ 
/*    */   
/*    */   public Parseable createParseable() {
/* 54 */     return new PatternParseable(this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final boolean isElement() {
/* 61 */     return this instanceof DElementPattern;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final boolean isAttribute() {
/* 68 */     return this instanceof DAttributePattern;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\digested\DPattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */