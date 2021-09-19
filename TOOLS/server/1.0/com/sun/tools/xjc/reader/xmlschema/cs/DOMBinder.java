/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema.cs;
/*     */ 
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.msv.grammar.ExpressionPool;
/*     */ import com.sun.tools.xjc.grammar.TypeItem;
/*     */ import com.sun.tools.xjc.grammar.ext.DOMItemFactory;
/*     */ import com.sun.tools.xjc.reader.xmlschema.AbstractXSFunctionImpl;
/*     */ import com.sun.tools.xjc.reader.xmlschema.BGMBuilder;
/*     */ import com.sun.tools.xjc.reader.xmlschema.WildcardNameClassBuilder;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIXDom;
/*     */ import com.sun.tools.xjc.reader.xmlschema.cs.ClassSelector;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ import com.sun.xml.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.xsom.XSAttributeUse;
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSContentType;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSModelGroup;
/*     */ import com.sun.xml.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.xsom.XSParticle;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.XSTerm;
/*     */ import com.sun.xml.xsom.XSWildcard;
/*     */ import com.sun.xml.xsom.visitor.XSFunction;
/*     */ import com.sun.xml.xsom.visitor.XSTermFunction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class DOMBinder
/*     */   extends AbstractXSFunctionImpl
/*     */ {
/*     */   private final BGMBuilder builder;
/*     */   private final ClassSelector selector;
/*     */   private final ExpressionPool pool;
/*     */   
/*     */   DOMBinder(ClassSelector _selector) {
/*  47 */     this.selector = _selector;
/*  48 */     this.builder = this.selector.builder;
/*  49 */     this.pool = this.builder.pool;
/*     */   }
/*     */ 
/*     */   
/*     */   public Expression bind(XSComponent sc) {
/*  54 */     return (Expression)sc.apply((XSFunction)this);
/*     */   }
/*     */   public TypeItem bind(XSElementDecl sc) {
/*  57 */     return (TypeItem)sc.apply((XSFunction)this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object particle(XSParticle p) {
/*  64 */     BIXDom c = (BIXDom)this.builder.getBindInfo((XSComponent)p).get(BIXDom.NAME);
/*  65 */     if (c == null) return null;
/*     */     
/*  67 */     return (new Builder(this, c)).particle(p);
/*     */   }
/*     */   
/*     */   private Expression bindTerm(XSTerm t) {
/*  71 */     BIXDom c = (BIXDom)this.builder.getBindInfo((XSComponent)t).get(BIXDom.NAME);
/*  72 */     if (c == null) return null;
/*     */     
/*  74 */     return (Expression)t.apply((XSTermFunction)new Builder(this, c));
/*     */   }
/*     */ 
/*     */   
/*     */   public Object wildcard(XSWildcard wc) {
/*  79 */     Expression exp = bindTerm((XSTerm)wc);
/*  80 */     if (exp != null) return exp;
/*     */     
/*  82 */     if ((wc.getMode() == 3 || wc.getMode() == 1) && (this.builder.getGlobalBinding()).smartWildcardDefaultBinding) {
/*     */       
/*     */       try {
/*     */         
/*  86 */         return DOMItemFactory.getInstance("W3C").create(WildcardNameClassBuilder.build(wc), this.builder.grammar, wc.getLocator());
/*     */ 
/*     */       
/*     */       }
/*  90 */       catch (com.sun.tools.xjc.grammar.ext.DOMItemFactory.UndefinedNameException e) {
/*     */         
/*  92 */         e.printStackTrace();
/*  93 */         throw new JAXBAssertionError();
/*     */       } 
/*     */     }
/*     */     
/*  97 */     return null;
/*     */   }
/*     */   
/*     */   public Object modelGroupDecl(XSModelGroupDecl decl) {
/* 101 */     return bindTerm((XSTerm)decl);
/*     */   }
/*     */   
/*     */   public Object modelGroup(XSModelGroup group) {
/* 105 */     return bindTerm((XSTerm)group);
/*     */   }
/*     */   
/*     */   public Object elementDecl(XSElementDecl decl) {
/* 109 */     return bindTerm((XSTerm)decl);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object attGroupDecl(XSAttGroupDecl decl) {
/* 175 */     return null;
/*     */   }
/*     */   
/*     */   public Object attributeDecl(XSAttributeDecl decl) {
/* 179 */     return null;
/*     */   }
/*     */   
/*     */   public Object attributeUse(XSAttributeUse use) {
/* 183 */     return null;
/*     */   }
/*     */   
/*     */   public Object complexType(XSComplexType type) {
/* 187 */     return null;
/*     */   }
/*     */   
/*     */   public Object simpleType(XSSimpleType simpleType) {
/* 191 */     return null;
/*     */   }
/*     */   
/*     */   public Object empty(XSContentType empty) {
/* 195 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\cs\DOMBinder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */