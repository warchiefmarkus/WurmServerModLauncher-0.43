/*     */ package com.sun.tools.xjc.reader.dtd.bindinfo;
/*     */ 
/*     */ import com.sun.tools.xjc.model.CClassInfo;
/*     */ import com.sun.xml.bind.api.impl.NameConverter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.w3c.dom.Element;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BIElement
/*     */ {
/*     */   final BindInfo parent;
/*     */   private final Element e;
/*     */   public final CClassInfo clazz;
/*     */   private final List<BIContent> contents;
/*     */   private final Map<String, BIConversion> conversions;
/*     */   private BIContent rest;
/*     */   private final Map<String, BIAttribute> attributes;
/*     */   private final List<BIConstructor> constructors;
/*     */   private final String className;
/*     */   
/*     */   BIElement(BindInfo bi, Element _e) {
/* 148 */     this.contents = new ArrayList<BIContent>();
/*     */ 
/*     */     
/* 151 */     this.conversions = new HashMap<String, BIConversion>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 161 */     this.attributes = new HashMap<String, BIAttribute>();
/*     */ 
/*     */     
/* 164 */     this.constructors = new ArrayList<BIConstructor>(); this.parent = bi; this.e = _e; Element c = DOMUtil.getElement(this.e, "content"); if (c != null)
/*     */       if (DOMUtil.getAttribute(c, "property") != null) { this.rest = BIContent.create(c, this); }
/*     */       else { for (Element p : DOMUtil.getChildElements(c)) { if (p.getLocalName().equals("rest")) { this.rest = BIContent.create(p, this); continue; }
/*     */            this.contents.add(BIContent.create(p, this)); }
/*     */          }
/*     */         for (Element atr : DOMUtil.getChildElements(this.e, "attribute")) { BIAttribute a = new BIAttribute(this, atr); this.attributes.put(a.name(), a); }
/*     */      if (isClass()) { String className = DOMUtil.getAttribute(this.e, "class"); if (className == null)
/*     */         className = NameConverter.standard.toClassName(name());  this.className = className; }
/*     */     else { this.className = null; }
/*     */      for (Element conv : DOMUtil.getChildElements(this.e, "conversion")) { BIConversion bIConversion = new BIUserConversion(bi, conv); this.conversions.put(bIConversion.name(), bIConversion); }
/*     */      for (Element en : DOMUtil.getChildElements(this.e, "enumeration")) { BIConversion bIConversion = BIEnumeration.create(en, this); this.conversions.put(bIConversion.name(), bIConversion); }
/*     */      for (Element element : DOMUtil.getChildElements(this.e, "constructor"))
/* 176 */       this.constructors.add(new BIConstructor(element));  String name = name(); QName tagName = new QName("", name); this.clazz = new CClassInfo(this.parent.model, this.parent.getTargetPackage(), this.className, getLocation(), null, tagName, null, null); } public String name() { return DOMUtil.getAttribute(this.e, "name"); }
/*     */   
/*     */   public Locator getLocation() {
/*     */     return DOMLocator.getLocationInfo(this.e);
/*     */   }
/*     */   
/*     */   public boolean isClass() {
/* 183 */     return "class".equals(this.e.getAttribute("type"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRoot() {
/* 190 */     return "true".equals(this.e.getAttribute("root"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/* 201 */     return this.className;
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
/*     */   public void declareConstructors(CClassInfo src) {
/* 215 */     for (BIConstructor c : this.constructors) {
/* 216 */       c.createDeclaration(src);
/*     */     }
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
/*     */   public BIConversion getConversion() {
/* 231 */     String cnv = DOMUtil.getAttribute(this.e, "convert");
/* 232 */     if (cnv == null) return null;
/*     */     
/* 234 */     return conversion(cnv);
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
/*     */   public BIConversion conversion(String name) {
/* 247 */     BIConversion r = this.conversions.get(name);
/* 248 */     if (r != null) return r;
/*     */ 
/*     */     
/* 251 */     return this.parent.conversion(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<BIContent> getContents() {
/* 259 */     return this.contents;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BIAttribute attribute(String name) {
/* 269 */     return this.attributes.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BIContent getRest() {
/* 277 */     return this.rest;
/*     */   }
/*     */   
/*     */   public Locator getSourceLocation() {
/* 281 */     return DOMLocator.getLocationInfo(this.e);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\dtd\bindinfo\BIElement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */