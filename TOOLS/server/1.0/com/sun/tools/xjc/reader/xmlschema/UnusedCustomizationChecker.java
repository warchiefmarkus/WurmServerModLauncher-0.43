/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.tools.xjc.reader.xmlschema.BGMBuilder;
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
/*     */ import com.sun.xml.xsom.XSListSimpleType;
/*     */ import com.sun.xml.xsom.XSModelGroup;
/*     */ import com.sun.xml.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.xsom.XSNotation;
/*     */ import com.sun.xml.xsom.XSParticle;
/*     */ import com.sun.xml.xsom.XSRestrictionSimpleType;
/*     */ import com.sun.xml.xsom.XSSchema;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.XSUnionSimpleType;
/*     */ import com.sun.xml.xsom.XSWildcard;
/*     */ import com.sun.xml.xsom.visitor.XSSimpleTypeVisitor;
/*     */ import com.sun.xml.xsom.visitor.XSVisitor;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
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
/*     */ class UnusedCustomizationChecker
/*     */   implements XSVisitor, XSSimpleTypeVisitor
/*     */ {
/*     */   private final BGMBuilder builder;
/*     */   private Set visitedComponents;
/*     */   static final String ERR_UNACKNOWLEDGED_CUSTOMIZATION = "UnusedCustomizationChecker.UnacknolwedgedCustomization";
/*     */   
/*     */   void run() {
/*     */     for (Iterator itr = this.builder.schemas.iterateSchema(); itr.hasNext(); ) {
/*     */       XSSchema s = itr.next();
/*     */       schema(s);
/*     */       run(s.iterateAttGroupDecls());
/*     */       run(s.iterateAttributeDecls());
/*     */       run(s.iterateComplexTypes());
/*     */       run(s.iterateElementDecls());
/*     */       run(s.iterateModelGroupDecls());
/*     */       run(s.iterateNotations());
/*     */       run(s.iterateSimpleTypes());
/*     */     } 
/*     */   }
/*     */   
/*     */   UnusedCustomizationChecker(BGMBuilder _builder) {
/*  70 */     this.visitedComponents = new HashSet();
/*     */     this.builder = _builder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean check(XSComponent c) {
/*  78 */     if (!this.visitedComponents.add(c)) {
/*  79 */       return false;
/*     */     }
/*  81 */     BIDeclaration[] decls = this.builder.getBindInfo(c).getDecls();
/*  82 */     for (int i = 0; i < decls.length; i++) {
/*  83 */       if (!decls[i].isAcknowledged()) {
/*  84 */         this.builder.errorReporter.error(decls[i].getLocation(), "UnusedCustomizationChecker.UnacknolwedgedCustomization", decls[i].getName().getLocalPart());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  91 */         decls[i].markAsAcknowledged();
/*     */       } 
/*     */     } 
/*     */     
/*  95 */     return true;
/*     */   }
/*     */   private void run(Iterator itr) {
/*     */     while (itr.hasNext())
/*     */       ((XSComponent)itr.next()).visit(this); 
/*     */   }
/*     */   public void attGroupDecl(XSAttGroupDecl decl) {
/* 102 */     if (check((XSComponent)decl))
/* 103 */       attContainer((XSAttContainer)decl); 
/*     */   }
/*     */   public void annotation(XSAnnotation ann) {}
/*     */   public void attributeDecl(XSAttributeDecl decl) {
/* 107 */     if (check((XSComponent)decl))
/* 108 */       decl.getType().visit(this); 
/*     */   }
/*     */   
/*     */   public void attributeUse(XSAttributeUse use) {
/* 112 */     if (check((XSComponent)use))
/* 113 */       use.getDecl().visit(this); 
/*     */   }
/*     */   
/*     */   public void complexType(XSComplexType type) {
/* 117 */     if (check((XSComponent)type)) {
/*     */ 
/*     */       
/* 120 */       type.getContentType().visit(this);
/* 121 */       attContainer((XSAttContainer)type);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void attContainer(XSAttContainer cont) {
/* 126 */     for (Iterator iterator = cont.iterateAttGroups(); iterator.hasNext();) {
/* 127 */       ((XSAttGroupDecl)iterator.next()).visit(this);
/*     */     }
/* 129 */     for (Iterator itr = cont.iterateDeclaredAttributeUses(); itr.hasNext();) {
/* 130 */       ((XSAttributeUse)itr.next()).visit(this);
/*     */     }
/* 132 */     XSWildcard wc = cont.getAttributeWildcard();
/* 133 */     if (wc != null) wc.visit(this); 
/*     */   }
/*     */   
/*     */   public void schema(XSSchema schema) {
/* 137 */     check((XSComponent)schema);
/*     */   }
/*     */   
/*     */   public void facet(XSFacet facet) {
/* 141 */     check((XSComponent)facet);
/*     */   }
/*     */   
/*     */   public void notation(XSNotation notation) {
/* 145 */     check((XSComponent)notation);
/*     */   }
/*     */   
/*     */   public void wildcard(XSWildcard wc) {
/* 149 */     check((XSComponent)wc);
/*     */   }
/*     */   
/*     */   public void modelGroupDecl(XSModelGroupDecl decl) {
/* 153 */     if (check((XSComponent)decl))
/* 154 */       decl.getModelGroup().visit(this); 
/*     */   }
/*     */   
/*     */   public void modelGroup(XSModelGroup group) {
/* 158 */     if (check((XSComponent)group))
/* 159 */       for (int i = 0; i < group.getSize(); i++) {
/* 160 */         group.getChild(i).visit(this);
/*     */       } 
/*     */   }
/*     */   
/*     */   public void elementDecl(XSElementDecl decl) {
/* 165 */     if (check((XSComponent)decl))
/* 166 */       decl.getType().visit(this); 
/*     */   }
/*     */   
/*     */   public void simpleType(XSSimpleType simpleType) {
/* 170 */     if (check((XSComponent)simpleType))
/* 171 */       simpleType.visit(this); 
/*     */   }
/*     */   
/*     */   public void particle(XSParticle particle) {
/* 175 */     if (check((XSComponent)particle))
/* 176 */       particle.getTerm().visit(this); 
/*     */   }
/*     */   
/*     */   public void empty(XSContentType empty) {
/* 180 */     check((XSComponent)empty);
/*     */   }
/*     */   
/*     */   public void listSimpleType(XSListSimpleType type) {
/* 184 */     if (check((XSComponent)type))
/* 185 */       type.getItemType().visit(this); 
/*     */   }
/*     */   
/*     */   public void restrictionSimpleType(XSRestrictionSimpleType type) {
/* 189 */     if (check((XSComponent)type))
/* 190 */       type.getBaseType().visit(this); 
/*     */   }
/*     */   
/*     */   public void unionSimpleType(XSUnionSimpleType type) {
/* 194 */     if (check((XSComponent)type))
/* 195 */       for (int i = 0; i < type.getMemberSize(); i++)
/* 196 */         type.getMember(i).visit(this);  
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\UnusedCustomizationChecker.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */