/*     */ package com.sun.tools.xjc.model;
/*     */ 
/*     */ import com.sun.tools.xjc.model.nav.NClass;
/*     */ import com.sun.tools.xjc.model.nav.NType;
/*     */ import com.sun.xml.bind.v2.model.core.Adapter;
/*     */ import com.sun.xml.bind.v2.model.core.ElementPropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.core.ID;
/*     */ import com.sun.xml.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import java.util.AbstractList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.activation.MimeType;
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
/*     */ public final class CElementPropertyInfo
/*     */   extends CPropertyInfo
/*     */   implements ElementPropertyInfo<NType, NClass>
/*     */ {
/*     */   private final boolean required;
/*     */   private final MimeType expectedMimeType;
/*     */   private CAdapter adapter;
/*     */   private final boolean isValueList;
/*     */   private ID id;
/*  85 */   private final List<CTypeRef> types = new ArrayList<CTypeRef>();
/*     */   
/*  87 */   private final List<CNonElement> ref = new AbstractList<CNonElement>() {
/*     */       public CNonElement get(int index) {
/*  89 */         return ((CTypeRef)CElementPropertyInfo.this.getTypes().get(index)).getTarget();
/*     */       }
/*     */       public int size() {
/*  92 */         return CElementPropertyInfo.this.getTypes().size();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   public CElementPropertyInfo(String name, CollectionMode collection, ID id, MimeType expectedMimeType, XSComponent source, CCustomizations customizations, Locator locator, boolean required) {
/*  99 */     super(name, collection.col, source, customizations, locator);
/* 100 */     this.required = required;
/* 101 */     this.id = id;
/* 102 */     this.expectedMimeType = expectedMimeType;
/* 103 */     this.isValueList = collection.val;
/*     */   }
/*     */   
/*     */   public ID id() {
/* 107 */     return this.id;
/*     */   }
/*     */   
/*     */   public List<CTypeRef> getTypes() {
/* 111 */     return this.types;
/*     */   }
/*     */   
/*     */   public List<CNonElement> ref() {
/* 115 */     return this.ref;
/*     */   }
/*     */   
/*     */   public QName getSchemaType() {
/* 119 */     if (this.types.size() != 1)
/*     */     {
/*     */       
/* 122 */       return null;
/*     */     }
/* 124 */     CTypeRef t = this.types.get(0);
/* 125 */     if (needsExplicitTypeName(t.getTarget(), t.typeName)) {
/* 126 */       return t.typeName;
/*     */     }
/* 128 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public QName getXmlName() {
/* 136 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCollectionRequired() {
/* 141 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCollectionNillable() {
/* 146 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isRequired() {
/* 150 */     return this.required;
/*     */   }
/*     */   
/*     */   public boolean isValueList() {
/* 154 */     return this.isValueList;
/*     */   }
/*     */   
/*     */   public boolean isUnboxable() {
/* 158 */     if (!isCollection() && !this.required)
/*     */     {
/* 160 */       return false;
/*     */     }
/* 162 */     for (CTypeRef t : getTypes()) {
/* 163 */       if (t.isNillable())
/* 164 */         return false; 
/*     */     } 
/* 166 */     return super.isUnboxable();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOptionalPrimitive() {
/* 172 */     for (CTypeRef t : getTypes()) {
/* 173 */       if (t.isNillable())
/* 174 */         return false; 
/*     */     } 
/* 176 */     return (!isCollection() && !this.required && super.isUnboxable());
/*     */   }
/*     */   
/*     */   public <V> V accept(CPropertyVisitor<V> visitor) {
/* 180 */     return visitor.onElement(this);
/*     */   }
/*     */   
/*     */   public CAdapter getAdapter() {
/* 184 */     return this.adapter;
/*     */   }
/*     */   
/*     */   public void setAdapter(CAdapter a) {
/* 188 */     assert this.adapter == null;
/* 189 */     this.adapter = a;
/*     */   }
/*     */   
/*     */   public final PropertyKind kind() {
/* 193 */     return PropertyKind.ELEMENT;
/*     */   }
/*     */   
/*     */   public MimeType getExpectedMimeType() {
/* 197 */     return this.expectedMimeType;
/*     */   }
/*     */   
/*     */   public enum CollectionMode {
/* 201 */     NOT_REPEATED(false, false),
/* 202 */     REPEATED_ELEMENT(true, false),
/* 203 */     REPEATED_VALUE(true, true);
/*     */     private final boolean col;
/*     */     private final boolean val;
/*     */     
/*     */     CollectionMode(boolean col, boolean val) {
/* 208 */       this.col = col;
/* 209 */       this.val = val;
/*     */     }
/*     */     public boolean isRepeated() {
/* 212 */       return this.col;
/*     */     }
/*     */   }
/*     */   
/*     */   public QName collectElementNames(Map<QName, CPropertyInfo> table) {
/* 217 */     for (CTypeRef t : this.types) {
/* 218 */       QName n = t.getTagName();
/* 219 */       if (table.containsKey(n)) return n; 
/* 220 */       table.put(n, this);
/*     */     } 
/* 222 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\CElementPropertyInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */