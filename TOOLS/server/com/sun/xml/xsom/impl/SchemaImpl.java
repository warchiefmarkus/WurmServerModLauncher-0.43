/*     */ package com.sun.xml.xsom.impl;
/*     */ 
/*     */ import com.sun.xml.xsom.ForeignAttributes;
/*     */ import com.sun.xml.xsom.SCD;
/*     */ import com.sun.xml.xsom.XSAnnotation;
/*     */ import com.sun.xml.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSIdentityConstraint;
/*     */ import com.sun.xml.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.xsom.XSNotation;
/*     */ import com.sun.xml.xsom.XSSchema;
/*     */ import com.sun.xml.xsom.XSSchemaSet;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.XSType;
/*     */ import com.sun.xml.xsom.parser.SchemaDocument;
/*     */ import com.sun.xml.xsom.visitor.XSFunction;
/*     */ import com.sun.xml.xsom.visitor.XSVisitor;
/*     */ import java.text.ParseException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import org.xml.sax.Locator;
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
/*     */ public class SchemaImpl
/*     */   implements XSSchema
/*     */ {
/*     */   protected final SchemaSetImpl parent;
/*     */   private final String targetNamespace;
/*     */   private XSAnnotation annotation;
/*     */   private final Locator locator;
/*     */   
/*     */   public SchemaImpl(SchemaSetImpl _parent, Locator loc, String tns) {
/*  54 */     if (tns == null)
/*     */     {
/*  56 */       throw new IllegalArgumentException(); } 
/*  57 */     this.targetNamespace = tns;
/*  58 */     this.parent = _parent;
/*  59 */     this.locator = loc;
/*     */   }
/*     */   
/*     */   public SchemaDocument getSourceDocument() {
/*  63 */     return null;
/*     */   }
/*     */   
/*     */   public SchemaSetImpl getRoot() {
/*  67 */     return this.parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTargetNamespace() {
/*  74 */     return this.targetNamespace;
/*     */   }
/*     */   
/*     */   public XSSchema getOwnerSchema() {
/*  78 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAnnotation(XSAnnotation a) {
/*  83 */     this.annotation = a;
/*     */   }
/*     */   public XSAnnotation getAnnotation() {
/*  86 */     return this.annotation;
/*     */   }
/*     */   
/*     */   public XSAnnotation getAnnotation(boolean createIfNotExist) {
/*  90 */     if (createIfNotExist && this.annotation == null)
/*  91 */       this.annotation = new AnnotationImpl(); 
/*  92 */     return this.annotation;
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
/* 103 */     return this.locator;
/*     */   }
/*     */ 
/*     */   
/* 107 */   private final Map<String, XSAttributeDecl> atts = new HashMap<String, XSAttributeDecl>();
/* 108 */   private final Map<String, XSAttributeDecl> attsView = Collections.unmodifiableMap(this.atts);
/*     */   public void addAttributeDecl(XSAttributeDecl newDecl) {
/* 110 */     this.atts.put(newDecl.getName(), newDecl);
/*     */   }
/*     */   public Map<String, XSAttributeDecl> getAttributeDecls() {
/* 113 */     return this.attsView;
/*     */   }
/*     */   public XSAttributeDecl getAttributeDecl(String name) {
/* 116 */     return this.atts.get(name);
/*     */   }
/*     */   public Iterator<XSAttributeDecl> iterateAttributeDecls() {
/* 119 */     return this.atts.values().iterator();
/*     */   }
/*     */   
/* 122 */   private final Map<String, XSElementDecl> elems = new HashMap<String, XSElementDecl>();
/* 123 */   private final Map<String, XSElementDecl> elemsView = Collections.unmodifiableMap(this.elems);
/*     */   public void addElementDecl(XSElementDecl newDecl) {
/* 125 */     this.elems.put(newDecl.getName(), newDecl);
/*     */   }
/*     */   public Map<String, XSElementDecl> getElementDecls() {
/* 128 */     return this.elemsView;
/*     */   }
/*     */   public XSElementDecl getElementDecl(String name) {
/* 131 */     return this.elems.get(name);
/*     */   }
/*     */   public Iterator<XSElementDecl> iterateElementDecls() {
/* 134 */     return this.elems.values().iterator();
/*     */   }
/*     */   
/* 137 */   private final Map<String, XSAttGroupDecl> attGroups = new HashMap<String, XSAttGroupDecl>();
/* 138 */   private final Map<String, XSAttGroupDecl> attGroupsView = Collections.unmodifiableMap(this.attGroups);
/*     */   public void addAttGroupDecl(XSAttGroupDecl newDecl, boolean overwrite) {
/* 140 */     if (overwrite || !this.attGroups.containsKey(newDecl.getName()))
/* 141 */       this.attGroups.put(newDecl.getName(), newDecl); 
/*     */   }
/*     */   public Map<String, XSAttGroupDecl> getAttGroupDecls() {
/* 144 */     return this.attGroupsView;
/*     */   }
/*     */   public XSAttGroupDecl getAttGroupDecl(String name) {
/* 147 */     return this.attGroups.get(name);
/*     */   }
/*     */   public Iterator<XSAttGroupDecl> iterateAttGroupDecls() {
/* 150 */     return this.attGroups.values().iterator();
/*     */   }
/*     */ 
/*     */   
/* 154 */   private final Map<String, XSNotation> notations = new HashMap<String, XSNotation>();
/* 155 */   private final Map<String, XSNotation> notationsView = Collections.unmodifiableMap(this.notations);
/*     */   public void addNotation(XSNotation newDecl) {
/* 157 */     this.notations.put(newDecl.getName(), newDecl);
/*     */   }
/*     */   public Map<String, XSNotation> getNotations() {
/* 160 */     return this.notationsView;
/*     */   }
/*     */   public XSNotation getNotation(String name) {
/* 163 */     return this.notations.get(name);
/*     */   }
/*     */   public Iterator<XSNotation> iterateNotations() {
/* 166 */     return this.notations.values().iterator();
/*     */   }
/*     */   
/* 169 */   private final Map<String, XSModelGroupDecl> modelGroups = new HashMap<String, XSModelGroupDecl>();
/* 170 */   private final Map<String, XSModelGroupDecl> modelGroupsView = Collections.unmodifiableMap(this.modelGroups);
/*     */   public void addModelGroupDecl(XSModelGroupDecl newDecl, boolean overwrite) {
/* 172 */     if (overwrite || !this.modelGroups.containsKey(newDecl.getName()))
/* 173 */       this.modelGroups.put(newDecl.getName(), newDecl); 
/*     */   }
/*     */   public Map<String, XSModelGroupDecl> getModelGroupDecls() {
/* 176 */     return this.modelGroupsView;
/*     */   }
/*     */   public XSModelGroupDecl getModelGroupDecl(String name) {
/* 179 */     return this.modelGroups.get(name);
/*     */   }
/*     */   public Iterator<XSModelGroupDecl> iterateModelGroupDecls() {
/* 182 */     return this.modelGroups.values().iterator();
/*     */   }
/*     */ 
/*     */   
/* 186 */   private final Map<String, XSIdentityConstraint> idConstraints = new HashMap<String, XSIdentityConstraint>();
/* 187 */   private final Map<String, XSIdentityConstraint> idConstraintsView = Collections.unmodifiableMap(this.idConstraints);
/*     */   
/*     */   protected void addIdentityConstraint(IdentityConstraintImpl c) {
/* 190 */     this.idConstraints.put(c.getName(), c);
/*     */   }
/*     */   
/*     */   public Map<String, XSIdentityConstraint> getIdentityConstraints() {
/* 194 */     return this.idConstraintsView;
/*     */   }
/*     */   
/*     */   public XSIdentityConstraint getIdentityConstraint(String localName) {
/* 198 */     return this.idConstraints.get(localName);
/*     */   }
/*     */   
/* 201 */   private final Map<String, XSType> allTypes = new HashMap<String, XSType>();
/* 202 */   private final Map<String, XSType> allTypesView = Collections.unmodifiableMap(this.allTypes);
/*     */   
/* 204 */   private final Map<String, XSSimpleType> simpleTypes = new HashMap<String, XSSimpleType>();
/* 205 */   private final Map<String, XSSimpleType> simpleTypesView = Collections.unmodifiableMap(this.simpleTypes);
/*     */   public void addSimpleType(XSSimpleType newDecl, boolean overwrite) {
/* 207 */     if (overwrite || !this.simpleTypes.containsKey(newDecl.getName())) {
/* 208 */       this.simpleTypes.put(newDecl.getName(), newDecl);
/* 209 */       this.allTypes.put(newDecl.getName(), newDecl);
/*     */     } 
/*     */   }
/*     */   public Map<String, XSSimpleType> getSimpleTypes() {
/* 213 */     return this.simpleTypesView;
/*     */   }
/*     */   public XSSimpleType getSimpleType(String name) {
/* 216 */     return this.simpleTypes.get(name);
/*     */   }
/*     */   public Iterator<XSSimpleType> iterateSimpleTypes() {
/* 219 */     return this.simpleTypes.values().iterator();
/*     */   }
/*     */   
/* 222 */   private final Map<String, XSComplexType> complexTypes = new HashMap<String, XSComplexType>();
/* 223 */   private final Map<String, XSComplexType> complexTypesView = Collections.unmodifiableMap(this.complexTypes);
/*     */   public void addComplexType(XSComplexType newDecl, boolean overwrite) {
/* 225 */     if (overwrite || !this.complexTypes.containsKey(newDecl.getName())) {
/* 226 */       this.complexTypes.put(newDecl.getName(), newDecl);
/* 227 */       this.allTypes.put(newDecl.getName(), newDecl);
/*     */     } 
/*     */   }
/*     */   public Map<String, XSComplexType> getComplexTypes() {
/* 231 */     return this.complexTypesView;
/*     */   }
/*     */   public XSComplexType getComplexType(String name) {
/* 234 */     return this.complexTypes.get(name);
/*     */   }
/*     */   public Iterator<XSComplexType> iterateComplexTypes() {
/* 237 */     return this.complexTypes.values().iterator();
/*     */   }
/*     */   
/*     */   public Map<String, XSType> getTypes() {
/* 241 */     return this.allTypesView;
/*     */   }
/*     */   public XSType getType(String name) {
/* 244 */     return this.allTypes.get(name);
/*     */   }
/*     */   public Iterator<XSType> iterateTypes() {
/* 247 */     return this.allTypes.values().iterator();
/*     */   }
/*     */   
/*     */   public void visit(XSVisitor visitor) {
/* 251 */     visitor.schema(this);
/*     */   }
/*     */   
/*     */   public Object apply(XSFunction function) {
/* 255 */     return function.schema(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 261 */   private List<ForeignAttributes> foreignAttributes = null;
/* 262 */   private List<ForeignAttributes> readOnlyForeignAttributes = null;
/*     */   
/*     */   public void addForeignAttributes(ForeignAttributesImpl fa) {
/* 265 */     if (this.foreignAttributes == null)
/* 266 */       this.foreignAttributes = new ArrayList<ForeignAttributes>(); 
/* 267 */     this.foreignAttributes.add(fa);
/*     */   }
/*     */   
/*     */   public List<ForeignAttributes> getForeignAttributes() {
/* 271 */     if (this.readOnlyForeignAttributes == null)
/* 272 */       if (this.foreignAttributes == null) {
/* 273 */         this.readOnlyForeignAttributes = Collections.EMPTY_LIST;
/*     */       } else {
/* 275 */         this.readOnlyForeignAttributes = Collections.unmodifiableList(this.foreignAttributes);
/*     */       }  
/* 277 */     return this.readOnlyForeignAttributes;
/*     */   }
/*     */   
/*     */   public String getForeignAttribute(String nsUri, String localName) {
/* 281 */     for (ForeignAttributes fa : getForeignAttributes()) {
/* 282 */       String v = fa.getValue(nsUri, localName);
/* 283 */       if (v != null) return v; 
/*     */     } 
/* 285 */     return null;
/*     */   }
/*     */   
/*     */   public Collection<XSComponent> select(String scd, NamespaceContext nsContext) {
/*     */     try {
/* 290 */       return SCD.create(scd, nsContext).select((XSComponent)this);
/* 291 */     } catch (ParseException e) {
/* 292 */       throw new IllegalArgumentException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public XSComponent selectSingle(String scd, NamespaceContext nsContext) {
/*     */     try {
/* 298 */       return SCD.create(scd, nsContext).selectSingle((XSComponent)this);
/* 299 */     } catch (ParseException e) {
/* 300 */       throw new IllegalArgumentException(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\SchemaImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */