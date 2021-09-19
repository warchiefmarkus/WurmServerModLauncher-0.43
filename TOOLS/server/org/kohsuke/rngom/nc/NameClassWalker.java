/*    */ package org.kohsuke.rngom.nc;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NameClassWalker
/*    */   implements NameClassVisitor<Void>
/*    */ {
/*    */   public Void visitChoice(NameClass nc1, NameClass nc2) {
/* 11 */     nc1.accept(this);
/* 12 */     return nc2.<Void>accept(this);
/*    */   }
/*    */   
/*    */   public Void visitNsName(String ns) {
/* 16 */     return null;
/*    */   }
/*    */   
/*    */   public Void visitNsNameExcept(String ns, NameClass nc) {
/* 20 */     return nc.<Void>accept(this);
/*    */   }
/*    */   
/*    */   public Void visitAnyName() {
/* 24 */     return null;
/*    */   }
/*    */   
/*    */   public Void visitAnyNameExcept(NameClass nc) {
/* 28 */     return nc.<Void>accept(this);
/*    */   }
/*    */   
/*    */   public Void visitName(QName name) {
/* 32 */     return null;
/*    */   }
/*    */   
/*    */   public Void visitNull() {
/* 36 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\nc\NameClassWalker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */