/*     */ package com.sun.tools.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.tools.xjc.reader.gbind.Choice;
/*     */ import com.sun.tools.xjc.reader.gbind.Element;
/*     */ import com.sun.tools.xjc.reader.gbind.Expression;
/*     */ import com.sun.tools.xjc.reader.gbind.OneOrMore;
/*     */ import com.sun.tools.xjc.reader.gbind.Sequence;
/*     */ import com.sun.xml.xsom.XSDeclaration;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSModelGroup;
/*     */ import com.sun.xml.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.xsom.XSParticle;
/*     */ import com.sun.xml.xsom.XSWildcard;
/*     */ import com.sun.xml.xsom.visitor.XSTermFunction;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ExpressionBuilder
/*     */   implements XSTermFunction<Expression>
/*     */ {
/*     */   private GWildcardElement wildcard;
/*     */   private final Map<QName, GElementImpl> decls;
/*     */   private XSParticle current;
/*     */   
/*     */   public static Expression createTree(XSParticle p) {
/*  64 */     return (new ExpressionBuilder()).particle(p);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ExpressionBuilder() {
/*  73 */     this.wildcard = null;
/*     */     
/*  75 */     this.decls = new HashMap<QName, GElementImpl>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression wildcard(XSWildcard wc) {
/*  84 */     if (this.wildcard == null)
/*  85 */       this.wildcard = new GWildcardElement(); 
/*  86 */     this.wildcard.merge(wc);
/*  87 */     this.wildcard.particles.add(this.current);
/*  88 */     return (Expression)this.wildcard;
/*     */   }
/*     */   
/*     */   public Expression modelGroupDecl(XSModelGroupDecl decl) {
/*  92 */     return modelGroup(decl.getModelGroup());
/*     */   }
/*     */   public Expression modelGroup(XSModelGroup group) {
/*     */     Sequence sequence;
/*  96 */     XSModelGroup.Compositor comp = group.getCompositor();
/*  97 */     if (comp == XSModelGroup.CHOICE) {
/*     */       Choice choice;
/*     */ 
/*     */ 
/*     */       
/* 102 */       Expression expression = Expression.EPSILON;
/* 103 */       for (XSParticle p : group.getChildren()) {
/* 104 */         if (expression == null) { expression = particle(p); }
/* 105 */         else { choice = new Choice(expression, particle(p)); }
/*     */       
/* 107 */       }  return (Expression)choice;
/*     */     } 
/* 109 */     Expression e = Expression.EPSILON;
/* 110 */     for (XSParticle p : group.getChildren()) {
/* 111 */       if (e == null) { e = particle(p); }
/* 112 */       else { sequence = new Sequence(e, particle(p)); }
/*     */     
/* 114 */     }  return (Expression)sequence;
/*     */   }
/*     */ 
/*     */   
/*     */   public Element elementDecl(XSElementDecl decl) {
/* 119 */     QName n = BGMBuilder.getName((XSDeclaration)decl);
/*     */     
/* 121 */     GElementImpl e = this.decls.get(n);
/* 122 */     if (e == null) {
/* 123 */       this.decls.put(n, e = new GElementImpl(n, decl));
/*     */     }
/* 125 */     e.particles.add(this.current);
/* 126 */     assert this.current.getTerm() == decl;
/*     */     
/* 128 */     return e;
/*     */   } public Expression particle(XSParticle p) {
/*     */     OneOrMore oneOrMore;
/*     */     Choice choice;
/* 132 */     this.current = p;
/* 133 */     Expression e = (Expression)p.getTerm().apply(this);
/*     */     
/* 135 */     if (p.isRepeated()) {
/* 136 */       oneOrMore = new OneOrMore(e);
/*     */     }
/* 138 */     if (p.getMinOccurs() == 0) {
/* 139 */       choice = new Choice((Expression)oneOrMore, Expression.EPSILON);
/*     */     }
/* 141 */     return (Expression)choice;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\ExpressionBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */