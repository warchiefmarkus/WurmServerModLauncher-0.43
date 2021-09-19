/*     */ package com.sun.tools.xjc.reader.dtd;
/*     */ 
/*     */ import com.sun.tools.xjc.model.CBuiltinLeafInfo;
/*     */ import com.sun.tools.xjc.model.CClassInfo;
/*     */ import com.sun.tools.xjc.model.CElementPropertyInfo;
/*     */ import com.sun.tools.xjc.model.CNonElement;
/*     */ import com.sun.tools.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.xjc.model.CReferencePropertyInfo;
/*     */ import com.sun.tools.xjc.model.CTypeRef;
/*     */ import com.sun.tools.xjc.model.CValuePropertyInfo;
/*     */ import com.sun.tools.xjc.model.TypeUse;
/*     */ import com.sun.tools.xjc.reader.dtd.bindinfo.BIConversion;
/*     */ import com.sun.tools.xjc.reader.dtd.bindinfo.BIElement;
/*     */ import com.sun.xml.bind.v2.model.core.ID;
/*     */ import com.sun.xml.bind.v2.model.core.WildcardMode;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ final class Element
/*     */   extends Term
/*     */   implements Comparable<Element>
/*     */ {
/*     */   final String name;
/*     */   private final TDTDReader owner;
/*     */   private short contentModelType;
/*     */   private Term contentModel;
/*     */   boolean isReferenced;
/*     */   private CClassInfo classInfo;
/*     */   private boolean classInfoComputed;
/* 109 */   final List<CPropertyInfo> attributes = new ArrayList<CPropertyInfo>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 114 */   private final List<Block> normalizedBlocks = new ArrayList<Block>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean mustBeClass;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Locator locator;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Element(TDTDReader owner, String name) {
/* 130 */     this.owner = owner;
/* 131 */     this.name = name;
/*     */   }
/*     */   
/*     */   void normalize(List<Block> r, boolean optional) {
/* 135 */     Block o = new Block(optional, false);
/* 136 */     o.elements.add(this);
/* 137 */     r.add(o);
/*     */   }
/*     */   
/*     */   void addAllElements(Block b) {
/* 141 */     b.elements.add(this);
/*     */   }
/*     */   
/*     */   boolean isOptional() {
/* 145 */     return false;
/*     */   }
/*     */   
/*     */   boolean isRepeated() {
/* 149 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void define(short contentModelType, Term contentModel, Locator locator) {
/* 157 */     assert this.contentModel == null;
/* 158 */     this.contentModelType = contentModelType;
/* 159 */     this.contentModel = contentModel;
/* 160 */     this.locator = locator;
/* 161 */     contentModel.normalize(this.normalizedBlocks, false);
/*     */     
/* 163 */     for (Block b : this.normalizedBlocks) {
/* 164 */       if (b.isRepeated || b.elements.size() > 1) {
/* 165 */         for (Element e : b.elements) {
/* 166 */           (this.owner.getOrCreateElement(e.name)).mustBeClass = true;
/*     */         }
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TypeUse getConversion() {
/* 177 */     assert this.contentModel == Term.EMPTY;
/*     */     
/* 179 */     BIElement e = this.owner.bindInfo.element(this.name);
/* 180 */     if (e != null) {
/* 181 */       BIConversion conv = e.getConversion();
/* 182 */       if (conv != null)
/* 183 */         return conv.getTransducer(); 
/*     */     } 
/* 185 */     return (TypeUse)CBuiltinLeafInfo.STRING;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   CClassInfo getClassInfo() {
/* 192 */     if (!this.classInfoComputed) {
/* 193 */       this.classInfoComputed = true;
/* 194 */       this.classInfo = calcClass();
/*     */     } 
/* 196 */     return this.classInfo;
/*     */   }
/*     */   
/*     */   private CClassInfo calcClass() {
/* 200 */     BIElement e = this.owner.bindInfo.element(this.name);
/* 201 */     if (e == null) {
/* 202 */       if (this.contentModelType != 2 || !this.attributes.isEmpty() || this.mustBeClass)
/*     */       {
/*     */         
/* 205 */         return createDefaultClass(); } 
/* 206 */       if (this.contentModel != Term.EMPTY) {
/* 207 */         throw new UnsupportedOperationException("mixed content model not supported");
/*     */       }
/*     */       
/* 210 */       if (this.isReferenced) {
/* 211 */         return null;
/*     */       }
/*     */       
/* 214 */       return createDefaultClass();
/*     */     } 
/*     */     
/* 217 */     return e.clazz;
/*     */   }
/*     */ 
/*     */   
/*     */   private CClassInfo createDefaultClass() {
/* 222 */     String className = this.owner.model.getNameConverter().toClassName(this.name);
/* 223 */     QName tagName = new QName("", this.name);
/*     */     
/* 225 */     return new CClassInfo(this.owner.model, this.owner.getTargetPackage(), className, this.locator, null, tagName, null, null);
/*     */   }
/*     */   void bind() {
/*     */     CReferencePropertyInfo rp;
/* 229 */     CClassInfo ci = getClassInfo();
/* 230 */     assert ci != null || this.attributes.isEmpty();
/* 231 */     for (CPropertyInfo p : this.attributes) {
/* 232 */       ci.addProperty(p);
/*     */     }
/* 234 */     switch (this.contentModelType) {
/*     */       case 1:
/* 236 */         rp = new CReferencePropertyInfo("Content", true, true, null, null, this.locator);
/* 237 */         rp.setWildcard(WildcardMode.SKIP);
/* 238 */         ci.addProperty((CPropertyInfo)rp);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 243 */         if (this.contentModel != Term.EMPTY) {
/* 244 */           throw new UnsupportedOperationException("mixed content model unsupported yet");
/*     */         }
/* 246 */         if (ci != null) {
/*     */           
/* 248 */           CValuePropertyInfo p = new CValuePropertyInfo("value", null, null, this.locator, getConversion(), null);
/* 249 */           ci.addProperty((CPropertyInfo)p);
/*     */         } 
/*     */         return;
/*     */       
/*     */       case 0:
/* 254 */         assert ci != null;
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 259 */     List<Block> n = new ArrayList<Block>();
/* 260 */     this.contentModel.normalize(n, false);
/*     */ 
/*     */     
/* 263 */     Set<String> names = new HashSet<String>();
/* 264 */     boolean collision = false;
/*     */ 
/*     */     
/* 267 */     label83: for (Block b : n) {
/* 268 */       for (Element e : b.elements) {
/* 269 */         if (!names.add(e.name)) {
/* 270 */           collision = true; break label83;
/*     */         } 
/*     */       } 
/*     */     } 
/* 274 */     if (collision) {
/*     */       
/* 276 */       Block all = new Block(true, true);
/* 277 */       for (Block b : n)
/* 278 */         all.elements.addAll(b.elements); 
/* 279 */       n.clear();
/* 280 */       n.add(all);
/*     */     } 
/*     */ 
/*     */     
/* 284 */     for (Block b : n) {
/*     */       CElementPropertyInfo p;
/* 286 */       if (b.isRepeated || b.elements.size() > 1) {
/*     */         
/* 288 */         StringBuilder name = new StringBuilder();
/* 289 */         for (Element e : b.elements) {
/* 290 */           if (name.length() > 0)
/* 291 */             name.append("Or"); 
/* 292 */           name.append(this.owner.model.getNameConverter().toPropertyName(e.name));
/*     */         } 
/* 294 */         p = new CElementPropertyInfo(name.toString(), CElementPropertyInfo.CollectionMode.REPEATED_ELEMENT, ID.NONE, null, null, null, this.locator, !b.isOptional);
/* 295 */         for (Element e : b.elements) {
/* 296 */           CClassInfo child = this.owner.getOrCreateElement(e.name).getClassInfo();
/* 297 */           assert child != null;
/* 298 */           p.getTypes().add(new CTypeRef((CNonElement)child, new QName("", e.name), null, false, null));
/*     */         } 
/*     */       } else {
/*     */         CNonElement cNonElement;
/* 302 */         String name = ((Element)b.elements.iterator().next()).name;
/* 303 */         String propName = this.owner.model.getNameConverter().toPropertyName(name);
/*     */ 
/*     */         
/* 306 */         Element ref = this.owner.getOrCreateElement(name);
/* 307 */         if (ref.getClassInfo() != null) {
/* 308 */           CClassInfo cClassInfo = ref.getClassInfo();
/*     */         } else {
/* 310 */           cNonElement = ref.getConversion().getInfo();
/*     */         } 
/*     */         
/* 313 */         p = new CElementPropertyInfo(propName, cNonElement.isCollection() ? CElementPropertyInfo.CollectionMode.REPEATED_VALUE : CElementPropertyInfo.CollectionMode.NOT_REPEATED, ID.NONE, null, null, null, this.locator, !b.isOptional);
/*     */ 
/*     */         
/* 316 */         p.getTypes().add(new CTypeRef(cNonElement.getInfo(), new QName("", name), null, false, null));
/*     */       } 
/* 318 */       ci.addProperty((CPropertyInfo)p);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int compareTo(Element that) {
/* 323 */     return this.name.compareTo(that.name);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\dtd\Element.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */