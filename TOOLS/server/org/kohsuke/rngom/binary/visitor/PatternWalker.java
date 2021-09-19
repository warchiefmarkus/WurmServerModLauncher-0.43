/*    */ package org.kohsuke.rngom.binary.visitor;
/*    */ 
/*    */ import org.kohsuke.rngom.binary.Pattern;
/*    */ import org.kohsuke.rngom.nc.NameClass;
/*    */ import org.relaxng.datatype.Datatype;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PatternWalker
/*    */   implements PatternVisitor
/*    */ {
/*    */   public void visitEmpty() {}
/*    */   
/*    */   public void visitNotAllowed() {}
/*    */   
/*    */   public void visitError() {}
/*    */   
/*    */   public void visitGroup(Pattern p1, Pattern p2) {
/* 24 */     visitBinary(p1, p2);
/*    */   }
/*    */   
/*    */   protected void visitBinary(Pattern p1, Pattern p2) {
/* 28 */     p1.accept(this);
/* 29 */     p2.accept(this);
/*    */   }
/*    */   
/*    */   public void visitInterleave(Pattern p1, Pattern p2) {
/* 33 */     visitBinary(p1, p2);
/*    */   }
/*    */   
/*    */   public void visitChoice(Pattern p1, Pattern p2) {
/* 37 */     visitBinary(p1, p2);
/*    */   }
/*    */   
/*    */   public void visitOneOrMore(Pattern p) {
/* 41 */     p.accept(this);
/*    */   }
/*    */   
/*    */   public void visitElement(NameClass nc, Pattern content) {
/* 45 */     content.accept(this);
/*    */   }
/*    */   
/*    */   public void visitAttribute(NameClass ns, Pattern value) {
/* 49 */     value.accept(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void visitData(Datatype dt) {}
/*    */ 
/*    */   
/*    */   public void visitDataExcept(Datatype dt, Pattern except) {}
/*    */ 
/*    */   
/*    */   public void visitValue(Datatype dt, Object obj) {}
/*    */ 
/*    */   
/*    */   public void visitText() {}
/*    */   
/*    */   public void visitList(Pattern p) {
/* 65 */     p.accept(this);
/*    */   }
/*    */   
/*    */   public void visitAfter(Pattern p1, Pattern p2) {}
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\binary\visitor\PatternWalker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */