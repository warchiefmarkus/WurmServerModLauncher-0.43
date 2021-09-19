/*     */ package com.sun.tools.xjc.reader.xmlschema.ct;
/*     */ 
/*     */ import com.sun.tools.xjc.model.CClass;
/*     */ import com.sun.tools.xjc.reader.xmlschema.WildcardNameClassBuilder;
/*     */ import com.sun.xml.xsom.XSAttContainer;
/*     */ import com.sun.xml.xsom.XSAttributeUse;
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSContentType;
/*     */ import com.sun.xml.xsom.XSDeclaration;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSModelGroup;
/*     */ import com.sun.xml.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.xsom.XSParticle;
/*     */ import com.sun.xml.xsom.XSType;
/*     */ import com.sun.xml.xsom.XSWildcard;
/*     */ import com.sun.xml.xsom.visitor.XSTermFunction;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import org.kohsuke.rngom.nc.ChoiceNameClass;
/*     */ import org.kohsuke.rngom.nc.NameClass;
/*     */ import org.kohsuke.rngom.nc.SimpleNameClass;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class ExtendedComplexTypeBuilder
/*     */   extends CTBuilder
/*     */ {
/*  74 */   private final Map<XSComplexType, NameClass[]> characteristicNameClasses = (Map)new HashMap<XSComplexType, NameClass>();
/*     */   
/*     */   public boolean isApplicable(XSComplexType ct) {
/*  77 */     XSType baseType = ct.getBaseType();
/*  78 */     return (baseType != this.schemas.getAnyType() && baseType.isComplexType() && ct.getDerivationMethod() == 1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void build(XSComplexType ct) {
/*  84 */     XSComplexType baseType = ct.getBaseType().asComplexType();
/*     */ 
/*     */     
/*  87 */     CClass baseClass = this.selector.bindToType(baseType, (XSComponent)ct, true);
/*  88 */     assert baseClass != null;
/*     */     
/*  90 */     this.selector.getCurrentBean().setBaseClass(baseClass);
/*     */ 
/*     */     
/*  93 */     ComplexTypeBindingMode baseTypeFlag = this.builder.getBindingMode(baseType);
/*     */     
/*  95 */     XSContentType explicitContent = ct.getExplicitContent();
/*     */     
/*  97 */     if (!checkIfExtensionSafe(baseType, ct)) {
/*     */       
/*  99 */       this.errorReceiver.error(ct.getLocator(), Messages.ERR_NO_FURTHER_EXTENSION.format(new Object[] { baseType.getName(), ct.getName() }));
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 108 */     if (explicitContent != null && explicitContent.asParticle() != null) {
/*     */       
/* 110 */       if (baseTypeFlag == ComplexTypeBindingMode.NORMAL) {
/*     */ 
/*     */         
/* 113 */         this.builder.recordBindingMode(ct, this.bgmBuilder.getParticleBinder().checkFallback(explicitContent.asParticle()) ? ComplexTypeBindingMode.FALLBACK_REST : ComplexTypeBindingMode.NORMAL);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 118 */         this.bgmBuilder.getParticleBinder().build(explicitContent.asParticle());
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 123 */         this.builder.recordBindingMode(ct, baseTypeFlag);
/*     */       } 
/*     */     } else {
/*     */       
/* 127 */       this.builder.recordBindingMode(ct, baseTypeFlag);
/*     */     } 
/*     */ 
/*     */     
/* 131 */     this.green.attContainer((XSAttContainer)ct);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean checkIfExtensionSafe(XSComplexType baseType, XSComplexType thisType) {
/*     */     ChoiceNameClass choiceNameClass;
/* 165 */     XSComplexType lastType = getLastRestrictedType(baseType);
/*     */     
/* 167 */     if (lastType == null) {
/* 168 */       return true;
/*     */     }
/* 170 */     NameClass anc = NameClass.NULL;
/*     */     
/* 172 */     Iterator<XSAttributeUse> itr = thisType.iterateDeclaredAttributeUses();
/* 173 */     while (itr.hasNext()) {
/* 174 */       choiceNameClass = new ChoiceNameClass(anc, getNameClass((XSDeclaration)((XSAttributeUse)itr.next()).getDecl()));
/*     */     }
/*     */     
/* 177 */     NameClass enc = getNameClass(thisType.getExplicitContent());
/*     */ 
/*     */     
/* 180 */     while (lastType != lastType.getBaseType()) {
/* 181 */       if (checkCollision((NameClass)choiceNameClass, enc, lastType)) {
/* 182 */         return false;
/*     */       }
/* 184 */       if (lastType.getBaseType().isSimpleType())
/*     */       {
/*     */         
/* 187 */         return true;
/*     */       }
/* 189 */       lastType = lastType.getBaseType().asComplexType();
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 194 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean checkCollision(NameClass anc, NameClass enc, XSComplexType type) {
/*     */     ChoiceNameClass choiceNameClass;
/* 204 */     NameClass[] chnc = this.characteristicNameClasses.get(type);
/* 205 */     if (chnc == null) {
/* 206 */       ChoiceNameClass choiceNameClass1; chnc = new NameClass[2];
/* 207 */       chnc[0] = getNameClass(type.getContentType());
/*     */ 
/*     */       
/* 210 */       NameClass nc = NameClass.NULL;
/* 211 */       Iterator<XSAttributeUse> itr = type.iterateAttributeUses();
/* 212 */       while (itr.hasNext())
/* 213 */         choiceNameClass = new ChoiceNameClass(anc, getNameClass((XSDeclaration)((XSAttributeUse)itr.next()).getDecl())); 
/* 214 */       XSWildcard wc = type.getAttributeWildcard();
/* 215 */       if (wc != null)
/* 216 */         choiceNameClass1 = new ChoiceNameClass(nc, WildcardNameClassBuilder.build(wc)); 
/* 217 */       chnc[1] = (NameClass)choiceNameClass1;
/*     */       
/* 219 */       this.characteristicNameClasses.put(type, chnc);
/*     */     } 
/*     */     
/* 222 */     return (chnc[0].hasOverlapWith(enc) || chnc[1].hasOverlapWith((NameClass)choiceNameClass));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private NameClass getNameClass(XSContentType t) {
/* 230 */     if (t == null) return NameClass.NULL; 
/* 231 */     XSParticle p = t.asParticle();
/* 232 */     if (p == null) return NameClass.NULL; 
/* 233 */     return (NameClass)p.getTerm().apply(this.contentModelNameClassBuilder);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private NameClass getNameClass(XSDeclaration decl) {
/* 240 */     return (NameClass)new SimpleNameClass(decl.getTargetNamespace(), decl.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 246 */   private final XSTermFunction<NameClass> contentModelNameClassBuilder = new XSTermFunction<NameClass>() {
/*     */       public NameClass wildcard(XSWildcard wc) {
/* 248 */         return WildcardNameClassBuilder.build(wc);
/*     */       }
/*     */       
/*     */       public NameClass modelGroupDecl(XSModelGroupDecl decl) {
/* 252 */         return modelGroup(decl.getModelGroup());
/*     */       }
/*     */       public NameClass modelGroup(XSModelGroup group) {
/*     */         ChoiceNameClass choiceNameClass;
/* 256 */         NameClass nc = NameClass.NULL;
/* 257 */         for (int i = 0; i < group.getSize(); i++)
/* 258 */           choiceNameClass = new ChoiceNameClass(nc, (NameClass)group.getChild(i).getTerm().apply(this)); 
/* 259 */         return (NameClass)choiceNameClass;
/*     */       }
/*     */       
/*     */       public NameClass elementDecl(XSElementDecl decl) {
/* 263 */         return ExtendedComplexTypeBuilder.this.getNameClass((XSDeclaration)decl);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private XSComplexType getLastRestrictedType(XSComplexType t) {
/* 277 */     if (t.getBaseType() == this.schemas.getAnyType())
/* 278 */       return null; 
/* 279 */     if (t.getDerivationMethod() == 2) {
/* 280 */       return t;
/*     */     }
/* 282 */     XSComplexType baseType = t.getBaseType().asComplexType();
/* 283 */     if (baseType != null) {
/* 284 */       return getLastRestrictedType(baseType);
/*     */     }
/* 286 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\ct\ExtendedComplexTypeBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */