/*    */ package org.kohsuke.rngom.nc;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public class SimpleNameClass
/*    */   extends NameClass {
/*    */   public final QName name;
/*    */   
/*    */   public SimpleNameClass(QName name) {
/* 10 */     this.name = name;
/*    */   }
/*    */   
/*    */   public SimpleNameClass(String nsUri, String localPart) {
/* 14 */     this(new QName(nsUri, localPart));
/*    */   }
/*    */   
/*    */   public boolean contains(QName name) {
/* 18 */     return this.name.equals(name);
/*    */   }
/*    */   
/*    */   public int containsSpecificity(QName name) {
/* 22 */     return contains(name) ? 2 : -1;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 26 */     return this.name.hashCode();
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj) {
/* 30 */     if (obj == null || !(obj instanceof SimpleNameClass))
/* 31 */       return false; 
/* 32 */     SimpleNameClass other = (SimpleNameClass)obj;
/* 33 */     return this.name.equals(other.name);
/*    */   }
/*    */   
/*    */   public <V> V accept(NameClassVisitor<V> visitor) {
/* 37 */     return visitor.visitName(this.name);
/*    */   }
/*    */   
/*    */   public boolean isOpen() {
/* 41 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\nc\SimpleNameClass.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */