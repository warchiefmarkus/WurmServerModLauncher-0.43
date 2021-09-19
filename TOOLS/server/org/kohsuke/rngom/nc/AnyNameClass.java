/*    */ package org.kohsuke.rngom.nc;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ 
/*    */ final class AnyNameClass
/*    */   extends NameClass
/*    */ {
/*    */   public boolean contains(QName name) {
/* 10 */     return true;
/*    */   }
/*    */   
/*    */   public int containsSpecificity(QName name) {
/* 14 */     return 0;
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj) {
/* 18 */     return (obj == this);
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 22 */     return AnyNameClass.class.hashCode();
/*    */   }
/*    */   
/*    */   public <V> V accept(NameClassVisitor<V> visitor) {
/* 26 */     return visitor.visitAnyName();
/*    */   }
/*    */   
/*    */   public boolean isOpen() {
/* 30 */     return true;
/*    */   }
/*    */   
/*    */   private static Object readReplace() {
/* 34 */     return NameClass.ANY;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\nc\AnyNameClass.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */