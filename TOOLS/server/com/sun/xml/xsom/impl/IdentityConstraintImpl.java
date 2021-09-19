/*    */ package com.sun.xml.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.xsom.XSElementDecl;
/*    */ import com.sun.xml.xsom.XSIdentityConstraint;
/*    */ import com.sun.xml.xsom.XSXPath;
/*    */ import com.sun.xml.xsom.impl.parser.SchemaDocumentImpl;
/*    */ import com.sun.xml.xsom.visitor.XSFunction;
/*    */ import com.sun.xml.xsom.visitor.XSVisitor;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IdentityConstraintImpl
/*    */   extends ComponentImpl
/*    */   implements XSIdentityConstraint, Ref.IdentityConstraint
/*    */ {
/*    */   private XSElementDecl parent;
/*    */   private final short category;
/*    */   private final String name;
/*    */   private final XSXPath selector;
/*    */   private final List<XSXPath> fields;
/*    */   private final Ref.IdentityConstraint refer;
/*    */   
/*    */   public IdentityConstraintImpl(SchemaDocumentImpl _owner, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl fa, short category, String name, XPathImpl selector, List<XPathImpl> fields, Ref.IdentityConstraint refer) {
/* 32 */     super(_owner, _annon, _loc, fa);
/* 33 */     this.category = category;
/* 34 */     this.name = name;
/* 35 */     this.selector = selector;
/* 36 */     selector.setParent(this);
/* 37 */     this.fields = Collections.unmodifiableList((List)fields);
/* 38 */     for (XPathImpl xp : fields)
/* 39 */       xp.setParent(this); 
/* 40 */     this.refer = refer;
/*    */   }
/*    */ 
/*    */   
/*    */   public void visit(XSVisitor visitor) {
/* 45 */     visitor.identityConstraint(this);
/*    */   }
/*    */   
/*    */   public <T> T apply(XSFunction<T> function) {
/* 49 */     return (T)function.identityConstraint(this);
/*    */   }
/*    */   
/*    */   public void setParent(ElementDecl parent) {
/* 53 */     this.parent = parent;
/* 54 */     parent.getOwnerSchema().addIdentityConstraint(this);
/*    */   }
/*    */   
/*    */   public XSElementDecl getParent() {
/* 58 */     return this.parent;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 62 */     return this.name;
/*    */   }
/*    */   
/*    */   public String getTargetNamespace() {
/* 66 */     return getParent().getTargetNamespace();
/*    */   }
/*    */   
/*    */   public short getCategory() {
/* 70 */     return this.category;
/*    */   }
/*    */   
/*    */   public XSXPath getSelector() {
/* 74 */     return this.selector;
/*    */   }
/*    */   
/*    */   public List<XSXPath> getFields() {
/* 78 */     return this.fields;
/*    */   }
/*    */   
/*    */   public XSIdentityConstraint getReferencedKey() {
/* 82 */     if (this.category == 1) {
/* 83 */       return this.refer.get();
/*    */     }
/* 85 */     throw new IllegalStateException("not a keyref");
/*    */   }
/*    */   
/*    */   public XSIdentityConstraint get() {
/* 89 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\IdentityConstraintImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */