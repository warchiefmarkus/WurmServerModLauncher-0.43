/*    */ package org.kohsuke.rngom.digested;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ import org.kohsuke.rngom.ast.builder.Annotations;
/*    */ import org.kohsuke.rngom.ast.builder.BuildException;
/*    */ import org.kohsuke.rngom.ast.builder.CommentList;
/*    */ import org.kohsuke.rngom.ast.om.Location;
/*    */ import org.kohsuke.rngom.ast.om.ParsedElementAnnotation;
/*    */ import org.kohsuke.rngom.ast.util.LocatorImpl;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ class Annotation
/*    */   implements Annotations<ElementWrapper, LocatorImpl, CommentListImpl> {
/* 14 */   private final DAnnotation a = new DAnnotation();
/*    */   
/*    */   public void addAttribute(String ns, String localName, String prefix, String value, LocatorImpl loc) throws BuildException {
/* 17 */     this.a.attributes.put(new QName(ns, localName, prefix), new DAnnotation.Attribute(ns, localName, prefix, value, (Locator)loc));
/*    */   }
/*    */ 
/*    */   
/*    */   public void addElement(ElementWrapper ea) throws BuildException {
/* 22 */     this.a.contents.add(ea.element);
/*    */   }
/*    */ 
/*    */   
/*    */   public void addComment(CommentListImpl comments) throws BuildException {}
/*    */ 
/*    */   
/*    */   public void addLeadingComment(CommentListImpl comments) throws BuildException {}
/*    */   
/*    */   DAnnotation getResult() {
/* 32 */     return this.a;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\digested\Annotation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */