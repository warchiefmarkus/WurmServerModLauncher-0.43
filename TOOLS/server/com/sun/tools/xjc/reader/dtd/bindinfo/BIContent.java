/*     */ package com.sun.tools.xjc.reader.dtd.bindinfo;
/*     */ 
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.tools.xjc.Options;
/*     */ import com.sun.tools.xjc.generator.bean.field.FieldRenderer;
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BIContent
/*     */ {
/*     */   protected final Element element;
/*     */   protected final BIElement parent;
/*     */   private final Options opts;
/*     */   
/*     */   private BIContent(Element e, BIElement _parent) {
/*  60 */     this.element = e;
/*  61 */     this.parent = _parent;
/*  62 */     this.opts = this.parent.parent.model.options;
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
/*     */   
/*     */   public final FieldRenderer getRealization() {
/*  80 */     String v = DOMUtil.getAttribute(this.element, "collection");
/*  81 */     if (v == null) return null;
/*     */     
/*  83 */     v = v.trim();
/*  84 */     if (v.equals("array")) return this.opts.getFieldRendererFactory().getArray(); 
/*  85 */     if (v.equals("list")) {
/*  86 */       return this.opts.getFieldRendererFactory().getList(this.parent.parent.codeModel.ref(ArrayList.class));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  91 */     throw new InternalError("unexpected collection value: " + v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getPropertyName() {
/* 101 */     String r = DOMUtil.getAttribute(this.element, "property");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 106 */     if (r != null) return r; 
/* 107 */     return DOMUtil.getAttribute(this.element, "name");
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
/*     */   public final JClass getType() {
/*     */     try {
/* 120 */       String type = DOMUtil.getAttribute(this.element, "supertype");
/* 121 */       if (type == null) return null;
/*     */ 
/*     */       
/* 124 */       int idx = type.lastIndexOf('.');
/* 125 */       if (idx < 0) return this.parent.parent.codeModel.ref(type); 
/* 126 */       return this.parent.parent.getTargetPackage().ref(type);
/* 127 */     } catch (ClassNotFoundException e) {
/*     */       
/* 129 */       throw new NoClassDefFoundError(e.getMessage());
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
/*     */   static BIContent create(Element e, BIElement _parent) {
/* 144 */     return new BIContent(e, _parent);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\dtd\bindinfo\BIContent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */