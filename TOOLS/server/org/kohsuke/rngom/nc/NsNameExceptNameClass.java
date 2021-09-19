/*    */ package org.kohsuke.rngom.nc;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public class NsNameExceptNameClass
/*    */   extends NameClass {
/*    */   private final NameClass nameClass;
/*    */   private final String namespaceURI;
/*    */   
/*    */   public NsNameExceptNameClass(String namespaceURI, NameClass nameClass) {
/* 11 */     this.namespaceURI = namespaceURI;
/* 12 */     this.nameClass = nameClass;
/*    */   }
/*    */   
/*    */   public boolean contains(QName name) {
/* 16 */     return (this.namespaceURI.equals(name.getNamespaceURI()) && !this.nameClass.contains(name));
/*    */   }
/*    */ 
/*    */   
/*    */   public int containsSpecificity(QName name) {
/* 21 */     return contains(name) ? 1 : -1;
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj) {
/* 25 */     if (obj == null || !(obj instanceof NsNameExceptNameClass))
/* 26 */       return false; 
/* 27 */     NsNameExceptNameClass other = (NsNameExceptNameClass)obj;
/* 28 */     return (this.namespaceURI.equals(other.namespaceURI) && this.nameClass.equals(other.nameClass));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 33 */     return this.namespaceURI.hashCode() ^ this.nameClass.hashCode();
/*    */   }
/*    */   
/*    */   public <V> V accept(NameClassVisitor<V> visitor) {
/* 37 */     return visitor.visitNsNameExcept(this.namespaceURI, this.nameClass);
/*    */   }
/*    */   
/*    */   public boolean isOpen() {
/* 41 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\nc\NsNameExceptNameClass.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */