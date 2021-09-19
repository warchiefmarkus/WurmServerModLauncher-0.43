/*     */ package com.sun.xml.xsom.impl;
/*     */ 
/*     */ import com.sun.xml.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.xsom.XSAttributeUse;
/*     */ import com.sun.xml.xsom.impl.parser.SchemaDocumentImpl;
/*     */ import com.sun.xml.xsom.impl.scd.Iterators;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public abstract class AttributesHolder
/*     */   extends DeclarationImpl
/*     */ {
/*     */   protected final Map<UName, AttributeUseImpl> attributes;
/*     */   protected final Set<UName> prohibitedAtts;
/*     */   protected final Set<Ref.AttGroup> attGroups;
/*     */   
/*     */   public abstract void setWildcard(WildcardImpl paramWildcardImpl);
/*     */   
/*     */   public void addAttributeUse(UName name, AttributeUseImpl a) {
/*     */     this.attributes.put(name, a);
/*     */   }
/*     */   
/*     */   protected AttributesHolder(SchemaDocumentImpl _parent, AnnotationImpl _annon, Locator loc, ForeignAttributesImpl _fa, String _name, boolean _anonymous) {
/*  45 */     super(_parent, _annon, loc, _fa, _parent.getTargetNamespace(), _name, _anonymous);
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
/*  56 */     this.attributes = new LinkedHashMap<UName, AttributeUseImpl>();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  61 */     this.prohibitedAtts = new HashSet<UName>();
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
/*  93 */     this.attGroups = new HashSet<Ref.AttGroup>();
/*     */   } public void addProhibitedAttribute(UName name) { this.prohibitedAtts.add(name); } public List<XSAttributeUse> getAttributeUses() { List<XSAttributeUse> v = new ArrayList<XSAttributeUse>(); v.addAll(this.attributes.values()); for (XSAttGroupDecl agd : getAttGroups()) v.addAll(agd.getAttributeUses());  return v; }
/*  95 */   public void addAttGroup(Ref.AttGroup a) { this.attGroups.add(a); }
/*     */   public Iterator<XSAttributeUse> iterateAttributeUses() {
/*     */     return getAttributeUses().iterator();
/*     */   } public XSAttributeUse getDeclaredAttributeUse(String nsURI, String localName) {
/*     */     return this.attributes.get(new UName(nsURI, localName));
/* 100 */   } public Iterator<XSAttGroupDecl> iterateAttGroups() { return (Iterator)new Iterators.Adapter<XSAttGroupDecl, Ref.AttGroup>(this.attGroups.iterator()) {
/*     */         protected XSAttGroupDecl filter(Ref.AttGroup u) {
/* 102 */           return u.get(); }
/*     */       }; } public Iterator<AttributeUseImpl> iterateDeclaredAttributeUses() {
/*     */     return this.attributes.values().iterator();
/*     */   } public Collection<AttributeUseImpl> getDeclaredAttributeUses() {
/*     */     return this.attributes.values();
/*     */   } public Set<XSAttGroupDecl> getAttGroups() {
/* 108 */     return new AbstractSet<XSAttGroupDecl>() {
/*     */         public Iterator<XSAttGroupDecl> iterator() {
/* 110 */           return AttributesHolder.this.iterateAttGroups();
/*     */         }
/*     */         
/*     */         public int size() {
/* 114 */           return AttributesHolder.this.attGroups.size();
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\AttributesHolder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */