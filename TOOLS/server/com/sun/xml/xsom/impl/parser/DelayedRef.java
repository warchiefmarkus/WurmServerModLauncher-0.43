/*     */ package com.sun.xml.xsom.impl.parser;
/*     */ 
/*     */ import com.sun.xml.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import com.sun.xml.xsom.XSDeclaration;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSIdentityConstraint;
/*     */ import com.sun.xml.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.xsom.XSSchemaSet;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.XSTerm;
/*     */ import com.sun.xml.xsom.XSType;
/*     */ import com.sun.xml.xsom.impl.Ref;
/*     */ import com.sun.xml.xsom.impl.SchemaImpl;
/*     */ import com.sun.xml.xsom.impl.UName;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DelayedRef
/*     */   implements Patch
/*     */ {
/*     */   protected final XSSchemaSet schema;
/*     */   private PatcherManager manager;
/*     */   private UName name;
/*     */   private Locator source;
/*     */   private Object ref;
/*     */   
/*     */   DelayedRef(PatcherManager _manager, Locator _source, SchemaImpl _schema, UName _name) {
/*  80 */     this.ref = null; this.schema = (XSSchemaSet)_schema.getRoot(); this.manager = _manager; this.name = _name; this.source = _source; if (this.name == null) throw new InternalError();  this.manager.addPatcher(this);
/*     */   } public void run() throws SAXException { if (this.ref == null)
/*  82 */       resolve();  this.manager = null; this.name = null; this.source = null; } protected final Object _get() { if (this.ref == null) throw new InternalError("unresolved reference"); 
/*  83 */     return this.ref; }
/*     */    protected abstract Object resolveReference(UName paramUName);
/*     */   protected abstract String getErrorProperty();
/*     */   private void resolve() throws SAXException {
/*  87 */     this.ref = resolveReference(this.name);
/*  88 */     if (this.ref == null) {
/*  89 */       this.manager.reportError(Messages.format(getErrorProperty(), new Object[] { this.name.getQualifiedName() }), this.source);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void redefine(XSDeclaration d) {
/*  99 */     if (!d.getTargetNamespace().equals(this.name.getNamespaceURI()) || !d.getName().equals(this.name.getName())) {
/*     */       return;
/*     */     }
/*     */     
/* 103 */     this.ref = d;
/* 104 */     this.manager = null;
/* 105 */     this.name = null;
/* 106 */     this.source = null;
/*     */   }
/*     */   
/*     */   public static class Type
/*     */     extends DelayedRef implements Ref.Type {
/*     */     public Type(PatcherManager manager, Locator loc, SchemaImpl schema, UName name) {
/* 112 */       super(manager, loc, schema, name);
/*     */     }
/*     */     protected Object resolveReference(UName name) {
/* 115 */       Object o = this.schema.getSimpleType(name.getNamespaceURI(), name.getName());
/*     */       
/* 117 */       if (o != null) return o;
/*     */       
/* 119 */       return this.schema.getComplexType(name.getNamespaceURI(), name.getName());
/*     */     }
/*     */ 
/*     */     
/*     */     protected String getErrorProperty() {
/* 124 */       return "UndefinedType";
/*     */     }
/*     */     public XSType getType() {
/* 127 */       return (XSType)_get();
/*     */     } }
/*     */   
/*     */   public static class SimpleType extends DelayedRef implements Ref.SimpleType {
/*     */     public SimpleType(PatcherManager manager, Locator loc, SchemaImpl schema, UName name) {
/* 132 */       super(manager, loc, schema, name);
/*     */     } public XSSimpleType getType() {
/* 134 */       return (XSSimpleType)_get();
/*     */     }
/*     */     protected Object resolveReference(UName name) {
/* 137 */       return this.schema.getSimpleType(name.getNamespaceURI(), name.getName());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected String getErrorProperty() {
/* 143 */       return "UndefinedSimpleType";
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ComplexType extends DelayedRef implements Ref.ComplexType {
/*     */     public ComplexType(PatcherManager manager, Locator loc, SchemaImpl schema, UName name) {
/* 149 */       super(manager, loc, schema, name);
/*     */     }
/*     */     protected Object resolveReference(UName name) {
/* 152 */       return this.schema.getComplexType(name.getNamespaceURI(), name.getName());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected String getErrorProperty() {
/* 158 */       return "UndefinedCompplexType";
/*     */     }
/*     */     public XSComplexType getType() {
/* 161 */       return (XSComplexType)_get();
/*     */     } }
/*     */   
/*     */   public static class Element extends DelayedRef implements Ref.Element {
/*     */     public Element(PatcherManager manager, Locator loc, SchemaImpl schema, UName name) {
/* 166 */       super(manager, loc, schema, name);
/*     */     }
/*     */     protected Object resolveReference(UName name) {
/* 169 */       return this.schema.getElementDecl(name.getNamespaceURI(), name.getName());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected String getErrorProperty() {
/* 175 */       return "UndefinedElement";
/*     */     }
/*     */     
/* 178 */     public XSElementDecl get() { return (XSElementDecl)_get(); } public XSTerm getTerm() {
/* 179 */       return (XSTerm)get();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ModelGroup extends DelayedRef implements Ref.Term { public ModelGroup(PatcherManager manager, Locator loc, SchemaImpl schema, UName name) {
/* 184 */       super(manager, loc, schema, name);
/*     */     }
/*     */     protected Object resolveReference(UName name) {
/* 187 */       return this.schema.getModelGroupDecl(name.getNamespaceURI(), name.getName());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected String getErrorProperty() {
/* 193 */       return "UndefinedModelGroup";
/*     */     }
/*     */     
/* 196 */     public XSModelGroupDecl get() { return (XSModelGroupDecl)_get(); } public XSTerm getTerm() {
/* 197 */       return (XSTerm)get();
/*     */     } }
/*     */   
/*     */   public static class AttGroup extends DelayedRef implements Ref.AttGroup {
/*     */     public AttGroup(PatcherManager manager, Locator loc, SchemaImpl schema, UName name) {
/* 202 */       super(manager, loc, schema, name);
/*     */     }
/*     */     protected Object resolveReference(UName name) {
/* 205 */       return this.schema.getAttGroupDecl(name.getNamespaceURI(), name.getName());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected String getErrorProperty() {
/* 211 */       return "UndefinedAttributeGroup";
/*     */     }
/*     */     public XSAttGroupDecl get() {
/* 214 */       return (XSAttGroupDecl)_get();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Attribute extends DelayedRef implements Ref.Attribute { public Attribute(PatcherManager manager, Locator loc, SchemaImpl schema, UName name) {
/* 219 */       super(manager, loc, schema, name);
/*     */     }
/*     */     protected Object resolveReference(UName name) {
/* 222 */       return this.schema.getAttributeDecl(name.getNamespaceURI(), name.getName());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected String getErrorProperty() {
/* 228 */       return "UndefinedAttribute";
/*     */     }
/*     */     public XSAttributeDecl getAttribute() {
/* 231 */       return (XSAttributeDecl)_get();
/*     */     } }
/*     */   
/*     */   public static class IdentityConstraint extends DelayedRef implements Ref.IdentityConstraint {
/*     */     public IdentityConstraint(PatcherManager manager, Locator loc, SchemaImpl schema, UName name) {
/* 236 */       super(manager, loc, schema, name);
/*     */     }
/*     */     protected Object resolveReference(UName name) {
/* 239 */       return this.schema.getIdentityConstraint(name.getNamespaceURI(), name.getName());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected String getErrorProperty() {
/* 245 */       return "UndefinedIdentityConstraint";
/*     */     }
/*     */     public XSIdentityConstraint get() {
/* 248 */       return (XSIdentityConstraint)_get();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\parser\DelayedRef.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */