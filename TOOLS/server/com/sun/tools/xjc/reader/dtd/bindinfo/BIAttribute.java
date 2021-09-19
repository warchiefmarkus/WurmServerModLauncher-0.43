/*     */ package com.sun.tools.xjc.reader.dtd.bindinfo;
/*     */ 
/*     */ import com.sun.tools.xjc.generator.bean.field.FieldRenderer;
/*     */ import com.sun.tools.xjc.generator.bean.field.FieldRendererFactory;
/*     */ import java.util.ArrayList;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BIAttribute
/*     */ {
/*     */   private final BIElement parent;
/*     */   private final Element element;
/*     */   
/*     */   BIAttribute(BIElement _parent, Element _e) {
/*  55 */     this.parent = _parent;
/*  56 */     this.element = _e;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String name() {
/*  64 */     return this.element.getAttribute("name");
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
/*     */   public BIConversion getConversion() {
/*  76 */     if (this.element.getAttributeNode("convert") == null) {
/*  77 */       return null;
/*     */     }
/*  79 */     String cnv = this.element.getAttribute("convert");
/*  80 */     return this.parent.conversion(cnv);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final FieldRenderer getRealization() {
/*  90 */     Attr a = this.element.getAttributeNode("collection");
/*  91 */     if (a == null) return null;
/*     */     
/*  93 */     String v = this.element.getAttribute("collection").trim();
/*     */     
/*  95 */     FieldRendererFactory frf = this.parent.parent.model.options.getFieldRendererFactory();
/*  96 */     if (v.equals("array")) return frf.getArray(); 
/*  97 */     if (v.equals("list")) {
/*  98 */       return frf.getList(this.parent.parent.codeModel.ref(ArrayList.class));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 103 */     throw new InternalError("unexpected collection value: " + v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getPropertyName() {
/* 113 */     String r = DOMUtil.getAttribute(this.element, "property");
/* 114 */     if (r != null) return r; 
/* 115 */     return name();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\dtd\bindinfo\BIAttribute.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */