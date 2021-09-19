/*    */ package org.kohsuke.rngom.binary.visitor;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import org.kohsuke.rngom.binary.Pattern;
/*    */ import org.kohsuke.rngom.nc.NameClass;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ChildElementFinder
/*    */   extends PatternWalker
/*    */ {
/* 19 */   private final Set children = new HashSet();
/*    */ 
/*    */   
/*    */   public static class Element
/*    */   {
/*    */     public final NameClass nc;
/*    */     
/*    */     public final Pattern content;
/*    */     
/*    */     public Element(NameClass nc, Pattern content) {
/* 29 */       this.nc = nc;
/* 30 */       this.content = content;
/*    */     }
/*    */     
/*    */     public boolean equals(Object o) {
/* 34 */       if (this == o) return true; 
/* 35 */       if (!(o instanceof Element)) return false;
/*    */       
/* 37 */       Element element = (Element)o;
/*    */       
/* 39 */       if ((this.content != null) ? !this.content.equals(element.content) : (element.content != null)) return false; 
/* 40 */       if ((this.nc != null) ? !this.nc.equals(element.nc) : (element.nc != null)) return false;
/*    */       
/* 42 */       return true;
/*    */     }
/*    */ 
/*    */     
/*    */     public int hashCode() {
/* 47 */       int result = (this.nc != null) ? this.nc.hashCode() : 0;
/* 48 */       result = 29 * result + ((this.content != null) ? this.content.hashCode() : 0);
/* 49 */       return result;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Set getChildren() {
/* 57 */     return this.children;
/*    */   }
/*    */   
/*    */   public void visitElement(NameClass nc, Pattern content) {
/* 61 */     this.children.add(new Element(nc, content));
/*    */   }
/*    */   
/*    */   public void visitAttribute(NameClass ns, Pattern value) {}
/*    */   
/*    */   public void visitList(Pattern p) {}
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\binary\visitor\ChildElementFinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */