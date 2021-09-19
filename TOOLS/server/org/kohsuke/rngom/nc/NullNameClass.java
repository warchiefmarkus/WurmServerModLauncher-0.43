/*    */ package org.kohsuke.rngom.nc;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ 
/*    */ final class NullNameClass
/*    */   extends NameClass
/*    */ {
/*    */   public boolean contains(QName name) {
/* 10 */     return false;
/*    */   }
/*    */   
/*    */   public int containsSpecificity(QName name) {
/* 14 */     return -1;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 18 */     return NullNameClass.class.hashCode();
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj) {
/* 22 */     return (this == obj);
/*    */   }
/*    */   
/*    */   public <V> V accept(NameClassVisitor<V> visitor) {
/* 26 */     return visitor.visitNull();
/*    */   }
/*    */   
/*    */   public boolean isOpen() {
/* 30 */     return false;
/*    */   }
/*    */   
/*    */   private Object readResolve() {
/* 34 */     return NameClass.NULL;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\nc\NullNameClass.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */