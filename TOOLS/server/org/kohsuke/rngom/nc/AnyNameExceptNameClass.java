/*    */ package org.kohsuke.rngom.nc;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public class AnyNameExceptNameClass
/*    */   extends NameClass {
/*    */   private final NameClass nameClass;
/*    */   
/*    */   public AnyNameExceptNameClass(NameClass nameClass) {
/* 10 */     this.nameClass = nameClass;
/*    */   }
/*    */   
/*    */   public boolean contains(QName name) {
/* 14 */     return !this.nameClass.contains(name);
/*    */   }
/*    */   
/*    */   public int containsSpecificity(QName name) {
/* 18 */     return contains(name) ? 0 : -1;
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj) {
/* 22 */     if (obj == null || !(obj instanceof AnyNameExceptNameClass))
/* 23 */       return false; 
/* 24 */     return this.nameClass.equals(((AnyNameExceptNameClass)obj).nameClass);
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 28 */     return this.nameClass.hashCode() ^ 0xFFFFFFFF;
/*    */   }
/*    */   
/*    */   public <V> V accept(NameClassVisitor<V> visitor) {
/* 32 */     return visitor.visitAnyNameExcept(this.nameClass);
/*    */   }
/*    */   
/*    */   public boolean isOpen() {
/* 36 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\nc\AnyNameExceptNameClass.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */