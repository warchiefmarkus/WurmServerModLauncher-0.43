/*     */ package 1.0.com.sun.xml.xsom.impl;
/*     */ 
/*     */ import com.sun.xml.xsom.XSAnnotation;
/*     */ import com.sun.xml.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.xsom.XSNotation;
/*     */ import com.sun.xml.xsom.XSSchema;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.XSType;
/*     */ import com.sun.xml.xsom.impl.SchemaSetImpl;
/*     */ import com.sun.xml.xsom.visitor.XSFunction;
/*     */ import com.sun.xml.xsom.visitor.XSVisitor;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SchemaImpl
/*     */   implements XSSchema
/*     */ {
/*     */   protected final SchemaSetImpl parent;
/*     */   private final String targetNamespace;
/*     */   private XSAnnotation annotation;
/*     */   private final Locator locator;
/*     */   
/*     */   public SchemaImpl(SchemaSetImpl _parent, Locator loc, String tns) {
/*  34 */     if (tns == null)
/*     */     {
/*  36 */       throw new IllegalArgumentException(); } 
/*  37 */     this.targetNamespace = tns;
/*  38 */     this.parent = _parent;
/*  39 */     this.locator = loc;
/*     */   }
/*     */ 
/*     */   
/*     */   public SchemaSetImpl getParent() {
/*  44 */     return this.parent;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTargetNamespace() {
/*  49 */     return this.targetNamespace;
/*     */   }
/*     */   
/*     */   public XSSchema getOwnerSchema() {
/*  53 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAnnotation(XSAnnotation a) {
/*  58 */     this.annotation = a;
/*     */   }
/*     */   public XSAnnotation getAnnotation() {
/*  61 */     return this.annotation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Locator getLocator() {
/*  72 */     return this.locator;
/*     */   }
/*     */ 
/*     */   
/*  76 */   private final Map atts = new HashMap();
/*     */   public void addAttributeDecl(XSAttributeDecl newDecl) {
/*  78 */     this.atts.put(newDecl.getName(), newDecl);
/*     */   }
/*     */   public XSAttributeDecl getAttributeDecl(String name) {
/*  81 */     return (XSAttributeDecl)this.atts.get(name);
/*     */   }
/*     */   public Iterator iterateAttributeDecls() {
/*  84 */     return this.atts.values().iterator();
/*     */   }
/*     */   
/*  87 */   private final Map elems = new HashMap();
/*     */   public void addElementDecl(XSElementDecl newDecl) {
/*  89 */     this.elems.put(newDecl.getName(), newDecl);
/*     */   }
/*     */   public XSElementDecl getElementDecl(String name) {
/*  92 */     return (XSElementDecl)this.elems.get(name);
/*     */   }
/*     */   public Iterator iterateElementDecls() {
/*  95 */     return this.elems.values().iterator();
/*     */   }
/*     */   
/*  98 */   private final Map attGroups = new HashMap();
/*     */   public void addAttGroupDecl(XSAttGroupDecl newDecl) {
/* 100 */     this.attGroups.put(newDecl.getName(), newDecl);
/*     */   }
/*     */   public XSAttGroupDecl getAttGroupDecl(String name) {
/* 103 */     return (XSAttGroupDecl)this.attGroups.get(name);
/*     */   }
/*     */   public Iterator iterateAttGroupDecls() {
/* 106 */     return this.attGroups.values().iterator();
/*     */   }
/*     */ 
/*     */   
/* 110 */   private final Map notations = new HashMap();
/*     */   public void addNotation(XSNotation newDecl) {
/* 112 */     this.notations.put(newDecl.getName(), newDecl);
/*     */   }
/*     */   public XSNotation getNotation(String name) {
/* 115 */     return (XSNotation)this.notations.get(name);
/*     */   }
/*     */   public Iterator iterateNotations() {
/* 118 */     return this.notations.values().iterator();
/*     */   }
/* 120 */   private final Map modelGroups = new HashMap();
/*     */   public void addModelGroupDecl(XSModelGroupDecl newDecl) {
/* 122 */     this.modelGroups.put(newDecl.getName(), newDecl);
/*     */   }
/*     */   public XSModelGroupDecl getModelGroupDecl(String name) {
/* 125 */     return (XSModelGroupDecl)this.modelGroups.get(name);
/*     */   }
/*     */   public Iterator iterateModelGroupDecls() {
/* 128 */     return this.modelGroups.values().iterator();
/*     */   }
/*     */   
/* 131 */   private final Map simpleTypes = new HashMap();
/*     */   public void addSimpleType(XSSimpleType newDecl) {
/* 133 */     this.simpleTypes.put(newDecl.getName(), newDecl);
/*     */   }
/*     */   public XSSimpleType getSimpleType(String name) {
/* 136 */     return (XSSimpleType)this.simpleTypes.get(name);
/*     */   }
/*     */   public Iterator iterateSimpleTypes() {
/* 139 */     return this.simpleTypes.values().iterator();
/*     */   }
/*     */   
/* 142 */   private final Map complexTypes = new HashMap();
/*     */   public void addComplexType(XSComplexType newDecl) {
/* 144 */     this.complexTypes.put(newDecl.getName(), newDecl);
/*     */   }
/*     */   public XSComplexType getComplexType(String name) {
/* 147 */     return (XSComplexType)this.complexTypes.get(name);
/*     */   }
/*     */   public Iterator iterateComplexTypes() {
/* 150 */     return this.complexTypes.values().iterator();
/*     */   }
/*     */   
/*     */   public XSType getType(String name) {
/* 154 */     XSType r = (XSType)this.complexTypes.get(name);
/* 155 */     if (r != null)
/* 156 */       return r; 
/* 157 */     return (XSType)this.simpleTypes.get(name);
/*     */   }
/*     */   public Iterator iterateTypes() {
/* 160 */     Iterator itr1 = iterateComplexTypes();
/* 161 */     Iterator itr2 = iterateSimpleTypes();
/*     */     
/* 163 */     return (Iterator)new Object(this, itr1, itr2);
/*     */   }
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
/*     */   public void visit(XSVisitor visitor) {
/* 180 */     visitor.schema(this);
/*     */   }
/*     */   
/*     */   public Object apply(XSFunction function) {
/* 184 */     return function.schema(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\SchemaImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */