/*     */ package com.sun.tools.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.tools.xjc.reader.Ring;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIDeclaration;
/*     */ import com.sun.xml.xsom.XSAnnotation;
/*     */ import com.sun.xml.xsom.XSAttContainer;
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
/*     */ import com.sun.xml.xsom.XSUnionSimpleType;
/*     */ import com.sun.xml.xsom.XSWildcard;
/*     */ import com.sun.xml.xsom.XSXPath;
/*     */ import com.sun.xml.xsom.visitor.XSSimpleTypeVisitor;
/*     */ import com.sun.xml.xsom.visitor.XSVisitor;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class UnusedCustomizationChecker
/*     */   extends BindingComponent
/*     */   implements XSVisitor, XSSimpleTypeVisitor
/*     */ {
/*  85 */   private final BGMBuilder builder = (BGMBuilder)Ring.get(BGMBuilder.class);
/*  86 */   private final SimpleTypeBuilder stb = (SimpleTypeBuilder)Ring.get(SimpleTypeBuilder.class);
/*     */   
/*  88 */   private final Set<XSComponent> visitedComponents = new HashSet<XSComponent>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void run() {
/*  94 */     for (XSSchema s : ((XSSchemaSet)Ring.get(XSSchemaSet.class)).getSchemas()) {
/*  95 */       schema(s);
/*  96 */       run(s.getAttGroupDecls());
/*  97 */       run(s.getAttributeDecls());
/*  98 */       run(s.getComplexTypes());
/*  99 */       run(s.getElementDecls());
/* 100 */       run(s.getModelGroupDecls());
/* 101 */       run(s.getNotations());
/* 102 */       run(s.getSimpleTypes());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void run(Map<String, ? extends XSComponent> col) {
/* 107 */     for (XSComponent c : col.values()) {
/* 108 */       c.visit(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean check(XSComponent c) {
/* 118 */     if (!this.visitedComponents.add(c)) {
/* 119 */       return false;
/*     */     }
/* 121 */     for (BIDeclaration decl : this.builder.getBindInfo(c).getDecls()) {
/* 122 */       check(decl, c);
/*     */     }
/* 124 */     checkExpectedContentTypes(c);
/*     */     
/* 126 */     return true;
/*     */   }
/*     */   
/*     */   private void checkExpectedContentTypes(XSComponent c) {
/* 130 */     if (c.getForeignAttribute("http://www.w3.org/2005/05/xmlmime", "expectedContentTypes") == null)
/*     */       return; 
/* 132 */     if (c instanceof XSParticle) {
/*     */       return;
/*     */     }
/*     */     
/* 136 */     if (!this.stb.isAcknowledgedXmimeContentTypes(c))
/*     */     {
/* 138 */       getErrorReporter().warning(c.getLocator(), "UnusedCustomizationChecker.WarnUnusedExpectedContentTypes", new Object[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   private void check(BIDeclaration decl, XSComponent c) {
/* 143 */     if (!decl.isAcknowledged()) {
/* 144 */       getErrorReporter().error(decl.getLocation(), "UnusedCustomizationChecker.UnacknolwedgedCustomization", new Object[] { decl.getName().getLocalPart() });
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 149 */       getErrorReporter().error(c.getLocator(), "UnusedCustomizationChecker.UnacknolwedgedCustomization.Relevant", new Object[0]);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 154 */       decl.markAsAcknowledged();
/*     */     } 
/* 156 */     for (BIDeclaration d : decl.getChildren()) {
/* 157 */       check(d, c);
/*     */     }
/*     */   }
/*     */   
/*     */   public void annotation(XSAnnotation ann) {}
/*     */   
/*     */   public void attGroupDecl(XSAttGroupDecl decl) {
/* 164 */     if (check((XSComponent)decl))
/* 165 */       attContainer((XSAttContainer)decl); 
/*     */   }
/*     */   
/*     */   public void attributeDecl(XSAttributeDecl decl) {
/* 169 */     if (check((XSComponent)decl))
/* 170 */       decl.getType().visit(this); 
/*     */   }
/*     */   
/*     */   public void attributeUse(XSAttributeUse use) {
/* 174 */     if (check((XSComponent)use))
/* 175 */       use.getDecl().visit(this); 
/*     */   }
/*     */   
/*     */   public void complexType(XSComplexType type) {
/* 179 */     if (check((XSComponent)type)) {
/*     */ 
/*     */       
/* 182 */       type.getContentType().visit(this);
/* 183 */       attContainer((XSAttContainer)type);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void attContainer(XSAttContainer cont) {
/* 188 */     for (Iterator<XSAttGroupDecl> iterator = cont.iterateAttGroups(); iterator.hasNext();) {
/* 189 */       ((XSAttGroupDecl)iterator.next()).visit(this);
/*     */     }
/* 191 */     for (Iterator<XSAttributeUse> itr = cont.iterateDeclaredAttributeUses(); itr.hasNext();) {
/* 192 */       ((XSAttributeUse)itr.next()).visit(this);
/*     */     }
/* 194 */     XSWildcard wc = cont.getAttributeWildcard();
/* 195 */     if (wc != null) wc.visit(this); 
/*     */   }
/*     */   
/*     */   public void schema(XSSchema schema) {
/* 199 */     check((XSComponent)schema);
/*     */   }
/*     */   
/*     */   public void facet(XSFacet facet) {
/* 203 */     check((XSComponent)facet);
/*     */   }
/*     */   
/*     */   public void notation(XSNotation notation) {
/* 207 */     check((XSComponent)notation);
/*     */   }
/*     */   
/*     */   public void wildcard(XSWildcard wc) {
/* 211 */     check((XSComponent)wc);
/*     */   }
/*     */   
/*     */   public void modelGroupDecl(XSModelGroupDecl decl) {
/* 215 */     if (check((XSComponent)decl))
/* 216 */       decl.getModelGroup().visit(this); 
/*     */   }
/*     */   
/*     */   public void modelGroup(XSModelGroup group) {
/* 220 */     if (check((XSComponent)group))
/* 221 */       for (int i = 0; i < group.getSize(); i++) {
/* 222 */         group.getChild(i).visit(this);
/*     */       } 
/*     */   }
/*     */   
/*     */   public void elementDecl(XSElementDecl decl) {
/* 227 */     if (check((XSComponent)decl)) {
/* 228 */       decl.getType().visit(this);
/* 229 */       for (XSIdentityConstraint id : decl.getIdentityConstraints())
/* 230 */         id.visit(this); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void simpleType(XSSimpleType simpleType) {
/* 235 */     if (check((XSComponent)simpleType))
/* 236 */       simpleType.visit(this); 
/*     */   }
/*     */   
/*     */   public void particle(XSParticle particle) {
/* 240 */     if (check((XSComponent)particle))
/* 241 */       particle.getTerm().visit(this); 
/*     */   }
/*     */   
/*     */   public void empty(XSContentType empty) {
/* 245 */     check((XSComponent)empty);
/*     */   }
/*     */   
/*     */   public void listSimpleType(XSListSimpleType type) {
/* 249 */     if (check((XSComponent)type))
/* 250 */       type.getItemType().visit(this); 
/*     */   }
/*     */   
/*     */   public void restrictionSimpleType(XSRestrictionSimpleType type) {
/* 254 */     if (check((XSComponent)type))
/* 255 */       type.getBaseType().visit(this); 
/*     */   }
/*     */   
/*     */   public void unionSimpleType(XSUnionSimpleType type) {
/* 259 */     if (check((XSComponent)type))
/* 260 */       for (int i = 0; i < type.getMemberSize(); i++) {
/* 261 */         type.getMember(i).visit(this);
/*     */       } 
/*     */   }
/*     */   
/*     */   public void identityConstraint(XSIdentityConstraint id) {
/* 266 */     if (check((XSComponent)id)) {
/* 267 */       id.getSelector().visit(this);
/* 268 */       for (XSXPath xp : id.getFields())
/* 269 */         xp.visit(this); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void xpath(XSXPath xp) {
/* 274 */     check((XSComponent)xp);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\UnusedCustomizationChecker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */