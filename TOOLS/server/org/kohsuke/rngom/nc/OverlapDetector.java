/*    */ package org.kohsuke.rngom.nc;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ class OverlapDetector
/*    */   implements NameClassVisitor<Void> {
/*    */   private NameClass nc1;
/*    */   private NameClass nc2;
/*    */   private boolean overlaps = false;
/*    */   static final String IMPOSSIBLE = "\000";
/*    */   
/*    */   private OverlapDetector(NameClass nc1, NameClass nc2) {
/* 13 */     this.nc1 = nc1;
/* 14 */     this.nc2 = nc2;
/* 15 */     nc1.accept(this);
/* 16 */     nc2.accept(this);
/*    */   }
/*    */   
/*    */   private void probe(QName name) {
/* 20 */     if (this.nc1.contains(name) && this.nc2.contains(name))
/* 21 */       this.overlaps = true; 
/*    */   }
/*    */   
/*    */   public Void visitChoice(NameClass nc1, NameClass nc2) {
/* 25 */     nc1.accept(this);
/* 26 */     nc2.accept(this);
/* 27 */     return null;
/*    */   }
/*    */   
/*    */   public Void visitNsName(String ns) {
/* 31 */     probe(new QName(ns, "\000"));
/* 32 */     return null;
/*    */   }
/*    */   
/*    */   public Void visitNsNameExcept(String ns, NameClass ex) {
/* 36 */     probe(new QName(ns, "\000"));
/* 37 */     ex.accept(this);
/* 38 */     return null;
/*    */   }
/*    */   
/*    */   public Void visitAnyName() {
/* 42 */     probe(new QName("\000", "\000"));
/* 43 */     return null;
/*    */   }
/*    */   
/*    */   public Void visitAnyNameExcept(NameClass ex) {
/* 47 */     probe(new QName("\000", "\000"));
/* 48 */     ex.accept(this);
/* 49 */     return null;
/*    */   }
/*    */   
/*    */   public Void visitName(QName name) {
/* 53 */     probe(name);
/* 54 */     return null;
/*    */   }
/*    */   
/*    */   public Void visitNull() {
/* 58 */     return null;
/*    */   }
/*    */   
/*    */   static boolean overlap(NameClass nc1, NameClass nc2) {
/* 62 */     if (nc2 instanceof SimpleNameClass) {
/* 63 */       SimpleNameClass snc = (SimpleNameClass)nc2;
/* 64 */       return nc1.contains(snc.name);
/*    */     } 
/* 66 */     if (nc1 instanceof SimpleNameClass) {
/* 67 */       SimpleNameClass snc = (SimpleNameClass)nc1;
/* 68 */       return nc2.contains(snc.name);
/*    */     } 
/* 70 */     return (new OverlapDetector(nc1, nc2)).overlaps;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\nc\OverlapDetector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */