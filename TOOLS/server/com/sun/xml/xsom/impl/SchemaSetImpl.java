/*     */ package com.sun.xml.xsom.impl;
/*     */ 
/*     */ import com.sun.xml.xsom.SCD;
/*     */ import com.sun.xml.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.xsom.XSAttributeUse;
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSContentType;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSFacet;
/*     */ import com.sun.xml.xsom.XSIdentityConstraint;
/*     */ import com.sun.xml.xsom.XSListSimpleType;
/*     */ import com.sun.xml.xsom.XSModelGroup;
/*     */ import com.sun.xml.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.xsom.XSNotation;
/*     */ import com.sun.xml.xsom.XSParticle;
/*     */ import com.sun.xml.xsom.XSRestrictionSimpleType;
/*     */ import com.sun.xml.xsom.XSSchema;
/*     */ import com.sun.xml.xsom.XSSchemaSet;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.XSType;
/*     */ import com.sun.xml.xsom.XSUnionSimpleType;
/*     */ import com.sun.xml.xsom.XSVariety;
/*     */ import com.sun.xml.xsom.XSWildcard;
/*     */ import com.sun.xml.xsom.impl.scd.Iterators;
/*     */ import com.sun.xml.xsom.visitor.XSContentTypeFunction;
/*     */ import com.sun.xml.xsom.visitor.XSContentTypeVisitor;
/*     */ import com.sun.xml.xsom.visitor.XSFunction;
/*     */ import com.sun.xml.xsom.visitor.XSSimpleTypeFunction;
/*     */ import com.sun.xml.xsom.visitor.XSSimpleTypeVisitor;
/*     */ import com.sun.xml.xsom.visitor.XSVisitor;
/*     */ import java.text.ParseException;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Vector;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SchemaSetImpl
/*     */   implements XSSchemaSet
/*     */ {
/*  66 */   private final Map<String, XSSchema> schemas = new HashMap<String, XSSchema>();
/*  67 */   private final Vector<XSSchema> schemas2 = new Vector<XSSchema>();
/*  68 */   private final List<XSSchema> readonlySchemaList = Collections.unmodifiableList(this.schemas2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SchemaImpl createSchema(String targetNamespace, Locator location) {
/*  75 */     SchemaImpl obj = (SchemaImpl)this.schemas.get(targetNamespace);
/*  76 */     if (obj == null) {
/*  77 */       obj = new SchemaImpl(this, location, targetNamespace);
/*  78 */       this.schemas.put(targetNamespace, obj);
/*  79 */       this.schemas2.add(obj);
/*     */     } 
/*  81 */     return obj;
/*     */   }
/*     */   
/*     */   public int getSchemaSize() {
/*  85 */     return this.schemas.size();
/*     */   }
/*     */   public XSSchema getSchema(String targetNamespace) {
/*  88 */     return this.schemas.get(targetNamespace);
/*     */   }
/*     */   public XSSchema getSchema(int idx) {
/*  91 */     return this.schemas2.get(idx);
/*     */   }
/*     */   public Iterator<XSSchema> iterateSchema() {
/*  94 */     return this.schemas2.iterator();
/*     */   }
/*     */   
/*     */   public final Collection<XSSchema> getSchemas() {
/*  98 */     return this.readonlySchemaList;
/*     */   }
/*     */   
/*     */   public XSType getType(String ns, String localName) {
/* 102 */     XSSchema schema = getSchema(ns);
/* 103 */     if (schema == null) return null;
/*     */     
/* 105 */     return schema.getType(localName);
/*     */   }
/*     */   
/*     */   public XSSimpleType getSimpleType(String ns, String localName) {
/* 109 */     XSSchema schema = getSchema(ns);
/* 110 */     if (schema == null) return null;
/*     */     
/* 112 */     return schema.getSimpleType(localName);
/*     */   }
/*     */   
/*     */   public XSElementDecl getElementDecl(String ns, String localName) {
/* 116 */     XSSchema schema = getSchema(ns);
/* 117 */     if (schema == null) return null;
/*     */     
/* 119 */     return schema.getElementDecl(localName);
/*     */   }
/*     */   
/*     */   public XSAttributeDecl getAttributeDecl(String ns, String localName) {
/* 123 */     XSSchema schema = getSchema(ns);
/* 124 */     if (schema == null) return null;
/*     */     
/* 126 */     return schema.getAttributeDecl(localName);
/*     */   }
/*     */   
/*     */   public XSModelGroupDecl getModelGroupDecl(String ns, String localName) {
/* 130 */     XSSchema schema = getSchema(ns);
/* 131 */     if (schema == null) return null;
/*     */     
/* 133 */     return schema.getModelGroupDecl(localName);
/*     */   }
/*     */   
/*     */   public XSAttGroupDecl getAttGroupDecl(String ns, String localName) {
/* 137 */     XSSchema schema = getSchema(ns);
/* 138 */     if (schema == null) return null;
/*     */     
/* 140 */     return schema.getAttGroupDecl(localName);
/*     */   }
/*     */   
/*     */   public XSComplexType getComplexType(String ns, String localName) {
/* 144 */     XSSchema schema = getSchema(ns);
/* 145 */     if (schema == null) return null;
/*     */     
/* 147 */     return schema.getComplexType(localName);
/*     */   }
/*     */   
/*     */   public XSIdentityConstraint getIdentityConstraint(String ns, String localName) {
/* 151 */     XSSchema schema = getSchema(ns);
/* 152 */     if (schema == null) return null;
/*     */     
/* 154 */     return schema.getIdentityConstraint(localName);
/*     */   }
/*     */   
/*     */   public Iterator<XSElementDecl> iterateElementDecls() {
/* 158 */     return (Iterator)new Iterators.Map<XSElementDecl, XSSchema>(iterateSchema()) {
/*     */         protected Iterator<XSElementDecl> apply(XSSchema u) {
/* 160 */           return u.iterateElementDecls();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public Iterator<XSType> iterateTypes() {
/* 166 */     return (Iterator)new Iterators.Map<XSType, XSSchema>(iterateSchema()) {
/*     */         protected Iterator<XSType> apply(XSSchema u) {
/* 168 */           return u.iterateTypes();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public Iterator<XSAttributeDecl> iterateAttributeDecls() {
/* 174 */     return (Iterator)new Iterators.Map<XSAttributeDecl, XSSchema>(iterateSchema()) {
/*     */         protected Iterator<XSAttributeDecl> apply(XSSchema u) {
/* 176 */           return u.iterateAttributeDecls();
/*     */         }
/*     */       };
/*     */   }
/*     */   public Iterator<XSAttGroupDecl> iterateAttGroupDecls() {
/* 181 */     return (Iterator)new Iterators.Map<XSAttGroupDecl, XSSchema>(iterateSchema()) {
/*     */         protected Iterator<XSAttGroupDecl> apply(XSSchema u) {
/* 183 */           return u.iterateAttGroupDecls();
/*     */         }
/*     */       };
/*     */   }
/*     */   public Iterator<XSModelGroupDecl> iterateModelGroupDecls() {
/* 188 */     return (Iterator)new Iterators.Map<XSModelGroupDecl, XSSchema>(iterateSchema()) {
/*     */         protected Iterator<XSModelGroupDecl> apply(XSSchema u) {
/* 190 */           return u.iterateModelGroupDecls();
/*     */         }
/*     */       };
/*     */   }
/*     */   public Iterator<XSSimpleType> iterateSimpleTypes() {
/* 195 */     return (Iterator)new Iterators.Map<XSSimpleType, XSSchema>(iterateSchema()) {
/*     */         protected Iterator<XSSimpleType> apply(XSSchema u) {
/* 197 */           return u.iterateSimpleTypes();
/*     */         }
/*     */       };
/*     */   }
/*     */   public Iterator<XSComplexType> iterateComplexTypes() {
/* 202 */     return (Iterator)new Iterators.Map<XSComplexType, XSSchema>(iterateSchema()) {
/*     */         protected Iterator<XSComplexType> apply(XSSchema u) {
/* 204 */           return u.iterateComplexTypes();
/*     */         }
/*     */       };
/*     */   }
/*     */   public Iterator<XSNotation> iterateNotations() {
/* 209 */     return (Iterator)new Iterators.Map<XSNotation, XSSchema>(iterateSchema()) {
/*     */         protected Iterator<XSNotation> apply(XSSchema u) {
/* 211 */           return u.iterateNotations();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public Iterator<XSIdentityConstraint> iterateIdentityConstraints() {
/* 217 */     return (Iterator)new Iterators.Map<XSIdentityConstraint, XSSchema>(iterateSchema()) {
/*     */         protected Iterator<XSIdentityConstraint> apply(XSSchema u) {
/* 219 */           return u.getIdentityConstraints().values().iterator();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public Collection<XSComponent> select(String scd, NamespaceContext nsContext) {
/*     */     try {
/* 226 */       return SCD.create(scd, nsContext).select(this);
/* 227 */     } catch (ParseException e) {
/* 228 */       throw new IllegalArgumentException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public XSComponent selectSingle(String scd, NamespaceContext nsContext) {
/*     */     try {
/* 234 */       return SCD.create(scd, nsContext).selectSingle(this);
/* 235 */     } catch (ParseException e) {
/* 236 */       throw new IllegalArgumentException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 241 */   public final EmptyImpl empty = new EmptyImpl(); public XSContentType getEmpty() {
/* 242 */     return this.empty;
/*     */   } public XSSimpleType getAnySimpleType() {
/* 244 */     return (XSSimpleType)this.anySimpleType;
/* 245 */   } public final AnySimpleType anySimpleType = new AnySimpleType();
/*     */   
/*     */   private class AnySimpleType
/*     */     extends DeclarationImpl implements XSRestrictionSimpleType, Ref.SimpleType {
/*     */     AnySimpleType() {
/* 250 */       super(null, null, null, null, "http://www.w3.org/2001/XMLSchema", "anySimpleType", false);
/*     */     }
/*     */     public SchemaImpl getOwnerSchema() {
/* 253 */       return SchemaSetImpl.this.createSchema("http://www.w3.org/2001/XMLSchema", null);
/*     */     }
/* 255 */     public XSSimpleType asSimpleType() { return (XSSimpleType)this; } public XSComplexType asComplexType() {
/* 256 */       return null;
/*     */     }
/*     */     public boolean isDerivedFrom(XSType t) {
/* 259 */       return (t == this || t == SchemaSetImpl.this.anyType);
/*     */     }
/*     */     
/* 262 */     public boolean isSimpleType() { return true; }
/* 263 */     public boolean isComplexType() { return false; }
/* 264 */     public XSContentType asEmpty() { return null; }
/* 265 */     public XSParticle asParticle() { return null; }
/* 266 */     public XSType getBaseType() { return (XSType)SchemaSetImpl.this.anyType; }
/* 267 */     public XSSimpleType getSimpleBaseType() { return null; }
/* 268 */     public int getDerivationMethod() { return 2; }
/* 269 */     public Iterator<XSFacet> iterateDeclaredFacets() { return Iterators.empty(); }
/* 270 */     public Collection<? extends XSFacet> getDeclaredFacets() { return Collections.EMPTY_LIST; }
/* 271 */     public void visit(XSSimpleTypeVisitor visitor) { visitor.restrictionSimpleType(this); }
/* 272 */     public void visit(XSContentTypeVisitor visitor) { visitor.simpleType((XSSimpleType)this); }
/* 273 */     public void visit(XSVisitor visitor) { visitor.simpleType((XSSimpleType)this); }
/* 274 */     public <T> T apply(XSSimpleTypeFunction<T> f) { return (T)f.restrictionSimpleType(this); }
/* 275 */     public <T> T apply(XSContentTypeFunction<T> f) { return (T)f.simpleType((XSSimpleType)this); }
/* 276 */     public <T> T apply(XSFunction<T> f) { return (T)f.simpleType((XSSimpleType)this); }
/* 277 */     public XSVariety getVariety() { return XSVariety.ATOMIC; }
/* 278 */     public XSSimpleType getPrimitiveType() { return (XSSimpleType)this; }
/* 279 */     public boolean isPrimitive() { return true; }
/* 280 */     public XSListSimpleType getBaseListType() { return null; }
/* 281 */     public XSUnionSimpleType getBaseUnionType() { return null; }
/* 282 */     public XSFacet getFacet(String name) { return null; }
/* 283 */     public XSFacet getDeclaredFacet(String name) { return null; } public List<XSFacet> getDeclaredFacets(String name) {
/* 284 */       return Collections.EMPTY_LIST;
/*     */     }
/* 286 */     public boolean isRestriction() { return true; }
/* 287 */     public boolean isList() { return false; }
/* 288 */     public boolean isUnion() { return false; }
/* 289 */     public boolean isFinal(XSVariety v) { return false; }
/* 290 */     public XSRestrictionSimpleType asRestriction() { return this; }
/* 291 */     public XSListSimpleType asList() { return null; }
/* 292 */     public XSUnionSimpleType asUnion() { return null; }
/* 293 */     public XSSimpleType getType() { return (XSSimpleType)this; }
/* 294 */     public XSSimpleType getRedefinedBy() { return null; } public int getRedefinedCount() {
/* 295 */       return 0;
/*     */     }
/*     */     public XSType[] listSubstitutables() {
/* 298 */       return Util.listSubstitutables((XSType)this);
/*     */     } }
/*     */   
/*     */   public XSComplexType getAnyType() {
/* 302 */     return this.anyType;
/* 303 */   } public final AnyType anyType = new AnyType();
/*     */   private class AnyType extends DeclarationImpl implements XSComplexType, Ref.Type { private final WildcardImpl anyWildcard;
/*     */     AnyType() {
/* 306 */       super(null, null, null, null, "http://www.w3.org/2001/XMLSchema", "anyType", false);
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
/* 352 */       this.anyWildcard = new WildcardImpl.Any(null, null, null, null, 3);
/* 353 */       this.contentType = new ParticleImpl(null, null, new ModelGroupImpl(null, null, null, null, XSModelGroup.SEQUENCE, new ParticleImpl[] { new ParticleImpl(null, null, this.anyWildcard, null, -1, 0) }), null, 1, 1);
/*     */     }
/*     */     
/*     */     private final XSContentType contentType;
/*     */     
/*     */     public SchemaImpl getOwnerSchema() {
/*     */       return SchemaSetImpl.this.createSchema("http://www.w3.org/2001/XMLSchema", null);
/*     */     }
/*     */     
/*     */     public boolean isAbstract() {
/*     */       return false;
/*     */     }
/*     */     
/*     */     public XSWildcard getAttributeWildcard() {
/*     */       return this.anyWildcard;
/*     */     }
/*     */     
/*     */     public XSAttributeUse getAttributeUse(String nsURI, String localName) {
/*     */       return null;
/*     */     }
/*     */     
/*     */     public Iterator<XSAttributeUse> iterateAttributeUses() {
/*     */       return Iterators.empty();
/*     */     }
/*     */     
/*     */     public XSAttributeUse getDeclaredAttributeUse(String nsURI, String localName) {
/*     */       return null;
/*     */     }
/*     */     
/*     */     public Iterator<XSAttributeUse> iterateDeclaredAttributeUses() {
/*     */       return Iterators.empty();
/*     */     }
/*     */     
/*     */     public Iterator<XSAttGroupDecl> iterateAttGroups() {
/*     */       return Iterators.empty();
/*     */     }
/*     */     
/*     */     public Collection<XSAttributeUse> getAttributeUses() {
/*     */       return Collections.EMPTY_LIST;
/*     */     }
/*     */     
/*     */     public Collection<? extends XSAttributeUse> getDeclaredAttributeUses() {
/*     */       return Collections.EMPTY_LIST;
/*     */     }
/*     */     
/*     */     public Collection<? extends XSAttGroupDecl> getAttGroups() {
/*     */       return Collections.EMPTY_LIST;
/*     */     }
/*     */     
/*     */     public boolean isFinal(int i) {
/*     */       return false;
/*     */     }
/*     */     
/*     */     public boolean isSubstitutionProhibited(int i) {
/*     */       return false;
/*     */     }
/*     */     
/*     */     public boolean isMixed() {
/*     */       return true;
/*     */     }
/*     */     
/*     */     public XSContentType getContentType() {
/*     */       return this.contentType;
/*     */     }
/*     */     
/*     */     public XSContentType getExplicitContent() {
/*     */       return null;
/*     */     }
/*     */     
/*     */     public XSType getBaseType() {
/*     */       return (XSType)this;
/*     */     }
/*     */     
/*     */     public XSSimpleType asSimpleType() {
/*     */       return null;
/*     */     }
/*     */     
/*     */     public XSComplexType asComplexType() {
/*     */       return this;
/*     */     }
/*     */     
/*     */     public boolean isDerivedFrom(XSType t) {
/*     */       return (t == this);
/*     */     }
/*     */     
/*     */     public boolean isSimpleType() {
/*     */       return false;
/*     */     }
/*     */     
/*     */     public boolean isComplexType() {
/*     */       return true;
/*     */     }
/*     */     
/*     */     public XSContentType asEmpty() {
/*     */       return null;
/*     */     }
/*     */     
/*     */     public int getDerivationMethod() {
/*     */       return 2;
/*     */     }
/*     */     
/*     */     public XSElementDecl getScope() {
/*     */       return null;
/*     */     }
/*     */     
/*     */     public void visit(XSVisitor visitor) {
/*     */       visitor.complexType(this);
/*     */     }
/*     */     
/*     */     public <T> T apply(XSFunction<T> f) {
/*     */       return (T)f.complexType(this);
/*     */     }
/*     */     
/*     */     public XSType getType() {
/*     */       return (XSType)this;
/*     */     }
/*     */     
/*     */     public XSComplexType getRedefinedBy() {
/*     */       return null;
/*     */     }
/*     */     
/*     */     public int getRedefinedCount() {
/*     */       return 0;
/*     */     }
/*     */     
/*     */     public XSType[] listSubstitutables() {
/*     */       return Util.listSubstitutables((XSType)this);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\SchemaSetImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */