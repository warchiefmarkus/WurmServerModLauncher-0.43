/*     */ package com.sun.tools.xjc.model;
/*     */ 
/*     */ import com.sun.tools.xjc.model.nav.NClass;
/*     */ import com.sun.tools.xjc.model.nav.NType;
/*     */ import com.sun.tools.xjc.model.nav.NavigatorImpl;
/*     */ import com.sun.xml.bind.v2.model.core.Adapter;
/*     */ import com.sun.xml.bind.v2.model.core.ID;
/*     */ import com.sun.xml.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.bind.v2.model.core.ReferencePropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.core.WildcardMode;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.activation.MimeType;
/*     */ import javax.xml.bind.annotation.W3CDomHandler;
/*     */ import javax.xml.namespace.QName;
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
/*     */ public final class CReferencePropertyInfo
/*     */   extends CPropertyInfo
/*     */   implements ReferencePropertyInfo<NType, NClass>
/*     */ {
/*  69 */   private final Set<CElement> elements = new HashSet<CElement>();
/*     */   
/*     */   private final boolean isMixed;
/*     */   
/*     */   private WildcardMode wildcard;
/*     */   
/*     */   public CReferencePropertyInfo(String name, boolean collection, boolean isMixed, XSComponent source, CCustomizations customizations, Locator locator) {
/*  76 */     super(name, (collection || isMixed), source, customizations, locator);
/*  77 */     this.isMixed = isMixed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<? extends CTypeInfo> ref() {
/*     */     final class RefList
/*     */       extends HashSet<CTypeInfo>
/*     */     {
/*     */       RefList() {
/*  91 */         super(CReferencePropertyInfo.this.elements.size());
/*  92 */         addAll((Collection)CReferencePropertyInfo.this.elements);
/*     */       }
/*     */       
/*     */       public boolean addAll(Collection<? extends CTypeInfo> col) {
/*  96 */         boolean r = false;
/*  97 */         for (CTypeInfo e : col) {
/*  98 */           if (e instanceof CElementInfo)
/*     */           {
/*     */ 
/*     */ 
/*     */             
/* 103 */             r |= addAll((Collection)((CElementInfo)e).getSubstitutionMembers());
/*     */           }
/* 105 */           r |= add(e);
/*     */         } 
/* 107 */         return r;
/*     */       }
/*     */     };
/*     */     
/* 111 */     RefList r = new RefList();
/* 112 */     if (this.wildcard != null) {
/* 113 */       if (this.wildcard.allowDom)
/* 114 */         r.add(CWildcardTypeInfo.INSTANCE); 
/* 115 */       if (this.wildcard.allowTypedObject)
/*     */       {
/*     */         
/* 118 */         r.add(CBuiltinLeafInfo.ANYTYPE); } 
/*     */     } 
/* 120 */     if (isMixed()) {
/* 121 */       r.add(CBuiltinLeafInfo.STRING);
/*     */     }
/*     */     
/* 124 */     return r;
/*     */   }
/*     */   
/*     */   public Set<CElement> getElements() {
/* 128 */     return this.elements;
/*     */   }
/*     */   
/*     */   public boolean isMixed() {
/* 132 */     return this.isMixed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public QName getXmlName() {
/* 140 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUnboxable() {
/* 149 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOptionalPrimitive() {
/* 155 */     return false;
/*     */   }
/*     */   
/*     */   public <V> V accept(CPropertyVisitor<V> visitor) {
/* 159 */     return visitor.onReference(this);
/*     */   }
/*     */   
/*     */   public CAdapter getAdapter() {
/* 163 */     return null;
/*     */   }
/*     */   
/*     */   public final PropertyKind kind() {
/* 167 */     return PropertyKind.REFERENCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ID id() {
/* 175 */     return ID.NONE;
/*     */   }
/*     */   
/*     */   public WildcardMode getWildcard() {
/* 179 */     return this.wildcard;
/*     */   }
/*     */   
/*     */   public void setWildcard(WildcardMode mode) {
/* 183 */     this.wildcard = mode;
/*     */   }
/*     */ 
/*     */   
/*     */   public NClass getDOMHandler() {
/* 188 */     if (getWildcard() != null) {
/* 189 */       return NavigatorImpl.create(W3CDomHandler.class);
/*     */     }
/* 191 */     return null;
/*     */   }
/*     */   
/*     */   public MimeType getExpectedMimeType() {
/* 195 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCollectionNillable() {
/* 200 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCollectionRequired() {
/* 205 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public QName getSchemaType() {
/* 210 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public QName collectElementNames(Map<QName, CPropertyInfo> table) {
/* 215 */     for (CElement e : this.elements) {
/* 216 */       QName n = e.getElementName();
/* 217 */       if (table.containsKey(n))
/* 218 */         return n; 
/* 219 */       table.put(n, this);
/*     */     } 
/* 221 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\CReferencePropertyInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */