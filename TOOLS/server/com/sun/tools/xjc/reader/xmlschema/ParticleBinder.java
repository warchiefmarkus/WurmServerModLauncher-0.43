/*     */ package com.sun.tools.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.codemodel.JJavaName;
/*     */ import com.sun.tools.xjc.model.CClassInfo;
/*     */ import com.sun.tools.xjc.reader.Ring;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIDeclaration;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIProperty;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSModelGroup;
/*     */ import com.sun.xml.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.xsom.XSParticle;
/*     */ import com.sun.xml.xsom.XSTerm;
/*     */ import com.sun.xml.xsom.XSWildcard;
/*     */ import com.sun.xml.xsom.visitor.XSTermVisitor;
/*     */ import java.text.ParseException;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ParticleBinder
/*     */ {
/*  64 */   protected final BGMBuilder builder = (BGMBuilder)Ring.get(BGMBuilder.class);
/*     */ 
/*     */   
/*     */   protected ParticleBinder() {
/*  68 */     Ring.add(ParticleBinder.class, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void build(XSParticle p) {
/*  77 */     build(p, Collections.emptySet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void build(XSParticle paramXSParticle, Collection<XSParticle> paramCollection);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean checkFallback(XSParticle paramXSParticle);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final CClassInfo getCurrentBean() {
/* 104 */     return getClassSelector().getCurrentBean();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final BIProperty getLocalPropCustomization(XSParticle p) {
/* 112 */     return getLocalCustomization(p, BIProperty.class);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final <T extends BIDeclaration> T getLocalCustomization(XSParticle p, Class<T> type) {
/* 117 */     BIDeclaration bIDeclaration = this.builder.getBindInfo((XSComponent)p).get(type);
/* 118 */     if (bIDeclaration != null) return (T)bIDeclaration;
/*     */ 
/*     */     
/* 121 */     bIDeclaration = this.builder.getBindInfo((XSComponent)p.getTerm()).get(type);
/* 122 */     if (bIDeclaration != null) return (T)bIDeclaration;
/*     */     
/* 124 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final String computeLabel(XSParticle p) {
/* 134 */     BIProperty cust = getLocalPropCustomization(p);
/* 135 */     if (cust != null && cust.getPropertyName(false) != null) {
/* 136 */       return cust.getPropertyName(false);
/*     */     }
/*     */ 
/*     */     
/* 140 */     XSTerm t = p.getTerm();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 149 */     if (t.isElementDecl())
/*     */     {
/* 151 */       return makeJavaName(p, t.asElementDecl().getName()); } 
/* 152 */     if (t.isModelGroupDecl())
/*     */     {
/* 154 */       return makeJavaName(p, t.asModelGroupDecl().getName()); } 
/* 155 */     if (t.isWildcard())
/*     */     {
/* 157 */       return makeJavaName(p, "Any"); } 
/* 158 */     if (t.isModelGroup()) {
/*     */       try {
/* 160 */         return getSpecDefaultName(t.asModelGroup(), p.isRepeated());
/* 161 */       } catch (ParseException e) {
/*     */         
/* 163 */         getErrorReporter().error(t.getLocator(), "DefaultParticleBinder.UnableToGenerateNameFromModelGroup", new Object[0]);
/*     */         
/* 165 */         return "undefined";
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 170 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */   
/*     */   protected final String makeJavaName(boolean isRepeated, String xmlName) {
/* 175 */     String name = this.builder.getNameConverter().toPropertyName(xmlName);
/* 176 */     if (this.builder.getGlobalBinding().isSimpleMode() && isRepeated)
/* 177 */       name = JJavaName.getPluralForm(name); 
/* 178 */     return name;
/*     */   }
/*     */   
/*     */   protected final String makeJavaName(XSParticle p, String xmlName) {
/* 182 */     return makeJavaName(p.isRepeated(), xmlName);
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
/*     */   protected final String getSpecDefaultName(XSModelGroup mg, final boolean repeated) throws ParseException {
/* 200 */     final StringBuilder name = new StringBuilder();
/*     */     
/* 202 */     mg.visit(new XSTermVisitor()
/*     */         {
/*     */ 
/*     */ 
/*     */           
/* 207 */           private int count = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 212 */           private boolean rep = repeated;
/*     */           
/*     */           public void wildcard(XSWildcard wc) {
/* 215 */             append("any");
/*     */           }
/*     */           
/*     */           public void modelGroupDecl(XSModelGroupDecl mgd) {
/* 219 */             modelGroup(mgd.getModelGroup());
/*     */           }
/*     */           
/*     */           public void modelGroup(XSModelGroup mg) {
/*     */             String operator;
/* 224 */             if (mg.getCompositor() == XSModelGroup.CHOICE) { operator = "Or"; }
/* 225 */             else { operator = "And"; }
/*     */             
/* 227 */             int size = mg.getSize();
/* 228 */             for (int i = 0; i < size; i++) {
/* 229 */               XSParticle p = mg.getChild(i);
/* 230 */               boolean oldRep = this.rep;
/* 231 */               this.rep |= p.isRepeated();
/* 232 */               p.getTerm().visit(this);
/* 233 */               this.rep = oldRep;
/*     */               
/* 235 */               if (this.count == 3)
/* 236 */                 return;  if (i != size - 1) name.append(operator); 
/*     */             } 
/*     */           }
/*     */           
/*     */           public void elementDecl(XSElementDecl ed) {
/* 241 */             append(ed.getName());
/*     */           }
/*     */           
/*     */           private void append(String token) {
/* 245 */             if (this.count < 3) {
/* 246 */               name.append(ParticleBinder.this.makeJavaName(this.rep, token));
/* 247 */               this.count++;
/*     */             } 
/*     */           }
/*     */         });
/*     */     
/* 252 */     if (name.length() == 0) throw new ParseException("no element", -1);
/*     */     
/* 254 */     return name.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ErrorReporter getErrorReporter() {
/* 260 */     return (ErrorReporter)Ring.get(ErrorReporter.class);
/*     */   }
/*     */   protected final ClassSelector getClassSelector() {
/* 263 */     return (ClassSelector)Ring.get(ClassSelector.class);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\ParticleBinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */