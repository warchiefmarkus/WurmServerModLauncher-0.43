/*    */ package org.kohsuke.rngom.nc;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public final class NsNameClass
/*    */   extends NameClass {
/*    */   private final String namespaceUri;
/*    */   
/*    */   public NsNameClass(String namespaceUri) {
/* 10 */     this.namespaceUri = namespaceUri;
/*    */   }
/*    */   
/*    */   public boolean contains(QName name) {
/* 14 */     return this.namespaceUri.equals(name.getNamespaceURI());
/*    */   }
/*    */   
/*    */   public int containsSpecificity(QName name) {
/* 18 */     return contains(name) ? 1 : -1;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 22 */     return this.namespaceUri.hashCode();
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj) {
/* 26 */     if (obj == null || !(obj instanceof NsNameClass))
/* 27 */       return false; 
/* 28 */     return this.namespaceUri.equals(((NsNameClass)obj).namespaceUri);
/*    */   }
/*    */   
/*    */   public <V> V accept(NameClassVisitor<V> visitor) {
/* 32 */     return visitor.visitNsName(this.namespaceUri);
/*    */   }
/*    */   
/*    */   public boolean isOpen() {
/* 36 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\nc\NsNameClass.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */