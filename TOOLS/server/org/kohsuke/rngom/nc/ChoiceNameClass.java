/*    */ package org.kohsuke.rngom.nc;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public class ChoiceNameClass
/*    */   extends NameClass {
/*    */   private final NameClass nameClass1;
/*    */   private final NameClass nameClass2;
/*    */   
/*    */   public ChoiceNameClass(NameClass nameClass1, NameClass nameClass2) {
/* 11 */     this.nameClass1 = nameClass1;
/* 12 */     this.nameClass2 = nameClass2;
/*    */   }
/*    */   
/*    */   public boolean contains(QName name) {
/* 16 */     return (this.nameClass1.contains(name) || this.nameClass2.contains(name));
/*    */   }
/*    */   
/*    */   public int containsSpecificity(QName name) {
/* 20 */     return Math.max(this.nameClass1.containsSpecificity(name), this.nameClass2.containsSpecificity(name));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 26 */     return this.nameClass1.hashCode() ^ this.nameClass2.hashCode();
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj) {
/* 30 */     if (obj == null || !(obj instanceof ChoiceNameClass))
/* 31 */       return false; 
/* 32 */     ChoiceNameClass other = (ChoiceNameClass)obj;
/* 33 */     return (this.nameClass1.equals(other.nameClass1) && this.nameClass2.equals(other.nameClass2));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <V> V accept(NameClassVisitor<V> visitor) {
/* 39 */     return visitor.visitChoice(this.nameClass1, this.nameClass2);
/*    */   }
/*    */   
/*    */   public boolean isOpen() {
/* 43 */     return (this.nameClass1.isOpen() || this.nameClass2.isOpen());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\nc\ChoiceNameClass.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */