/*     */ package 1.0.com.sun.tools.xjc.reader.dtd.bindinfo;
/*     */ 
/*     */ import com.sun.codemodel.JClassContainer;
/*     */ import com.sun.codemodel.JDefinedClass;
/*     */ import com.sun.tools.xjc.grammar.ClassItem;
/*     */ import com.sun.tools.xjc.reader.annotator.AnnotatorController;
/*     */ import com.sun.tools.xjc.reader.dtd.bindinfo.BIAttribute;
/*     */ import com.sun.tools.xjc.reader.dtd.bindinfo.BIConstructor;
/*     */ import com.sun.tools.xjc.reader.dtd.bindinfo.BIContent;
/*     */ import com.sun.tools.xjc.reader.dtd.bindinfo.BIConversion;
/*     */ import com.sun.tools.xjc.reader.dtd.bindinfo.BIEnumeration;
/*     */ import com.sun.tools.xjc.reader.dtd.bindinfo.BIUserConversion;
/*     */ import com.sun.tools.xjc.reader.dtd.bindinfo.BindInfo;
/*     */ import com.sun.tools.xjc.reader.dtd.bindinfo.DOM4JLocator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Vector;
/*     */ import org.dom4j.Element;
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
/*     */ public final class BIElement
/*     */ {
/*     */   final BindInfo parent;
/*     */   private final Element e;
/*     */   private final Vector contents;
/*     */   private final Map conversions;
/*     */   private BIContent rest;
/*     */   private final Map attributes;
/*     */   private final Vector constructors;
/*     */   private final JDefinedClass clazz;
/*     */   
/*     */   BIElement(BindInfo bi, Element _e) {
/* 108 */     this.contents = new Vector();
/*     */ 
/*     */     
/* 111 */     this.conversions = new HashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 121 */     this.attributes = new HashMap();
/*     */ 
/*     */     
/* 124 */     this.constructors = new Vector(); this.parent = bi; this.e = _e; Element c = this.e.element("content"); if (c != null)
/*     */       if (c.attribute("property") != null) { this.rest = BIContent.create(c, this); }
/*     */       else { Iterator iterator = c.elementIterator(); while (iterator.hasNext()) { Element p = iterator.next(); if (p.getName().equals("rest")) { this.rest = BIContent.create(p, this); continue; }
/*     */            this.contents.add(BIContent.create(p, this)); }
/*     */          }
/*     */         Iterator itr = this.e.elementIterator("attribute"); while (itr.hasNext()) { BIAttribute a = new BIAttribute(this, itr.next()); this.attributes.put(a.name(), a); }
/*     */      if (isClass()) { String className = this.e.attributeValue("class"); if (className == null)
/*     */         className = this.parent.nameConverter.toClassName(name());  this.clazz = this.parent.classFactory.createInterface((JClassContainer)this.parent.getTargetPackage(), className, null); }
/*     */     else { this.clazz = null; }
/*     */      itr = this.e.elementIterator("conversion"); while (itr.hasNext()) {
/*     */       BIUserConversion bIUserConversion = new BIUserConversion(bi, itr.next()); this.conversions.put(bIUserConversion.name(), bIUserConversion);
/*     */     }  itr = this.e.elementIterator("enumeration"); while (itr.hasNext()) {
/*     */       BIEnumeration bIEnumeration = BIEnumeration.create(itr.next(), this); this.conversions.put(bIEnumeration.name(), bIEnumeration);
/*     */     }  itr = this.e.elementIterator("constructor"); while (itr.hasNext())
/* 138 */       this.constructors.add(new BIConstructor(itr.next()));  } public String name() { return this.e.attributeValue("name"); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isClass() {
/* 145 */     return "class".equals(this.e.attributeValue("type"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRoot() {
/* 152 */     return "true".equals(this.e.attributeValue("root"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDefinedClass getClassObject() {
/* 163 */     return this.clazz;
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
/*     */   public void declareConstructors(ClassItem src, AnnotatorController controller) {
/* 177 */     for (int i = 0; i < this.constructors.size(); i++) {
/* 178 */       ((BIConstructor)this.constructors.get(i)).createDeclaration(src, controller);
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
/* 193 */     String cnv = this.e.attributeValue("convert");
/* 194 */     if (cnv == null) return null;
/*     */     
/* 196 */     return conversion(cnv);
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
/* 209 */     BIConversion r = (BIConversion)this.conversions.get(name);
/* 210 */     if (r != null) return r;
/*     */ 
/*     */     
/* 213 */     return this.parent.conversion(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator iterateContents() {
/* 220 */     return this.contents.iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BIAttribute attribute(String name) {
/* 229 */     return (BIAttribute)this.attributes.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BIContent getRest() {
/* 237 */     return this.rest;
/*     */   }
/*     */   
/*     */   public Locator getSourceLocation() {
/* 241 */     return DOM4JLocator.getLocationInfo(this.e);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\dtd\bindinfo\BIElement.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */