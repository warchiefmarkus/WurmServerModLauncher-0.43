/*     */ package com.sun.tools.xjc.reader.dtd.bindinfo;
/*     */ 
/*     */ import com.sun.tools.xjc.model.CBuiltinLeafInfo;
/*     */ import com.sun.tools.xjc.model.CClassInfoParent;
/*     */ import com.sun.tools.xjc.model.CEnumConstant;
/*     */ import com.sun.tools.xjc.model.CEnumLeafInfo;
/*     */ import com.sun.tools.xjc.model.CNonElement;
/*     */ import com.sun.tools.xjc.model.Model;
/*     */ import com.sun.tools.xjc.model.TypeUse;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.StringTokenizer;
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
/*     */ public final class BIEnumeration
/*     */   implements BIConversion
/*     */ {
/*     */   private final Element e;
/*     */   private final TypeUse xducer;
/*     */   
/*     */   private BIEnumeration(Element _e, TypeUse _xducer) {
/*  58 */     this.e = _e;
/*  59 */     this.xducer = _xducer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String name() {
/*  67 */     return DOMUtil.getAttribute(this.e, "name");
/*     */   }
/*     */   public TypeUse getTransducer() {
/*  70 */     return this.xducer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static BIEnumeration create(Element dom, BindInfo parent) {
/*  78 */     return new BIEnumeration(dom, (TypeUse)new CEnumLeafInfo(parent.model, null, (CClassInfoParent)new CClassInfoParent.Package(parent.getTargetPackage()), DOMUtil.getAttribute(dom, "name"), (CNonElement)CBuiltinLeafInfo.STRING, buildMemberList(parent.model, dom), null, null, DOMLocator.getLocationInfo(dom)));
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
/*     */   static BIEnumeration create(Element dom, BIElement parent) {
/*  94 */     return new BIEnumeration(dom, (TypeUse)new CEnumLeafInfo(parent.parent.model, null, (CClassInfoParent)parent.clazz, DOMUtil.getAttribute(dom, "name"), (CNonElement)CBuiltinLeafInfo.STRING, buildMemberList(parent.parent.model, dom), null, null, DOMLocator.getLocationInfo(dom)));
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
/*     */   private static List<CEnumConstant> buildMemberList(Model model, Element dom) {
/* 108 */     List<CEnumConstant> r = new ArrayList<CEnumConstant>();
/*     */     
/* 110 */     String members = DOMUtil.getAttribute(dom, "members");
/* 111 */     if (members == null) members = "";
/*     */     
/* 113 */     StringTokenizer tokens = new StringTokenizer(members);
/* 114 */     while (tokens.hasMoreTokens()) {
/* 115 */       String token = tokens.nextToken();
/* 116 */       r.add(new CEnumConstant(model.getNameConverter().toConstantName(token), null, token, null));
/*     */     } 
/*     */ 
/*     */     
/* 120 */     return r;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\dtd\bindinfo\BIEnumeration.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */