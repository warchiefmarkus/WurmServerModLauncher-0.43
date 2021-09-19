/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.msv.grammar.ExpressionPool;
/*     */ import com.sun.tools.xjc.grammar.ClassItem;
/*     */ import com.sun.tools.xjc.reader.xmlschema.BGMBuilder;
/*     */ import com.sun.tools.xjc.reader.xmlschema.NameGenerator;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIProperty;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSParticle;
/*     */ import com.sun.xml.xsom.XSTerm;
/*     */ import java.text.ParseException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   protected final BGMBuilder builder;
/*     */   protected final ExpressionPool pool;
/*     */   
/*     */   public abstract Expression build(XSParticle paramXSParticle, ClassItem paramClassItem);
/*     */   
/*     */   public abstract boolean checkFallback(XSParticle paramXSParticle, ClassItem paramClassItem);
/*     */   
/*     */   protected ParticleBinder(BGMBuilder builder) {
/*  55 */     this.builder = builder;
/*  56 */     this.pool = builder.pool;
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
/*     */   protected final boolean needSkippableElement(XSElementDecl e) {
/*  68 */     return (e.isGlobal() && e.getType().isComplexType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final boolean needSkip(XSTerm t) {
/*  77 */     return (isGlobalElementDecl(t) && this.builder.selector.bindToType((XSComponent)t) instanceof ClassItem);
/*     */   }
/*     */   
/*     */   protected final boolean isGlobalElementDecl(XSTerm t) {
/*  81 */     XSElementDecl e = t.asElementDecl();
/*  82 */     return (e != null && e.isGlobal());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final BIProperty getLocalPropCustomization(XSParticle p) {
/*  91 */     BIProperty cust = (BIProperty)this.builder.getBindInfo((XSComponent)p).get(BIProperty.NAME);
/*  92 */     if (cust != null) return cust;
/*     */ 
/*     */     
/*  95 */     cust = (BIProperty)this.builder.getBindInfo((XSComponent)p.getTerm()).get(BIProperty.NAME);
/*  96 */     if (cust != null) return cust;
/*     */ 
/*     */     
/*  99 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final String computeLabel(XSParticle p) {
/* 110 */     BIProperty cust = getLocalPropCustomization(p);
/* 111 */     if (cust != null && cust.getPropertyName(false) != null) {
/* 112 */       return cust.getPropertyName(false);
/*     */     }
/*     */ 
/*     */     
/* 116 */     XSTerm t = p.getTerm();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 125 */     if (t.isElementDecl())
/*     */     {
/* 127 */       return makeJavaName(t.asElementDecl().getName()); } 
/* 128 */     if (t.isModelGroupDecl())
/*     */     {
/* 130 */       return makeJavaName(t.asModelGroupDecl().getName()); } 
/* 131 */     if (t.isWildcard())
/*     */     {
/* 133 */       return "Any"; } 
/* 134 */     if (t.isModelGroup()) {
/*     */       try {
/* 136 */         return NameGenerator.getName(this.builder, t.asModelGroup());
/* 137 */       } catch (ParseException e) {
/*     */         
/* 139 */         this.builder.errorReporter.error(t.getLocator(), "DefaultParticleBinder.UnableToGenerateNameFromModelGroup");
/*     */         
/* 141 */         return "undefined";
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 147 */     _assert(false);
/* 148 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private String makeJavaName(String xmlName) {
/* 153 */     return this.builder.getNameConverter().toPropertyName(xmlName);
/*     */   }
/*     */   
/*     */   protected static void _assert(boolean b) {
/* 157 */     if (!b)
/* 158 */       throw new JAXBAssertionError(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\ParticleBinder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */