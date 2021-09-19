/*     */ package 1.0.com.sun.tools.xjc.generator.marshaller;
/*     */ 
/*     */ import com.sun.codemodel.JBlock;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.msv.grammar.ChoiceExp;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.msv.grammar.ExpressionVisitorBoolean;
/*     */ import com.sun.msv.grammar.util.ExpressionFinder;
/*     */ import com.sun.tools.xjc.generator.field.FieldRenderer;
/*     */ import com.sun.tools.xjc.generator.marshaller.AbstractSideImpl;
/*     */ import com.sun.tools.xjc.generator.marshaller.Context;
/*     */ import com.sun.tools.xjc.generator.marshaller.FieldMarshallerGenerator;
/*     */ import com.sun.tools.xjc.generator.marshaller.NestedIfBlockProvider;
/*     */ import com.sun.tools.xjc.generator.util.BlockReference;
/*     */ import com.sun.tools.xjc.grammar.FieldItem;
/*     */ import com.sun.tools.xjc.grammar.TypeItem;
/*     */ import com.sun.tools.xjc.grammar.util.FieldItemCollector;
/*     */ import com.sun.tools.xjc.grammar.util.FieldMultiplicityCounter;
/*     */ import com.sun.tools.xjc.grammar.util.Multiplicity;
/*     */ import com.sun.tools.xjc.runtime.Util;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ import com.sun.xml.bind.JAXBObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Outside
/*     */   extends AbstractSideImpl
/*     */ {
/*     */   protected Outside(Context _context) {
/*  48 */     super(_context);
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
/*  82 */   private static final ExpressionFinder isNonOptimizable = (ExpressionFinder)new NonOptimizabilityChecker(null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onChoice(ChoiceExp exp) {
/*  94 */     Expression[] children = exp.getChildren();
/*     */     
/*  96 */     NestedIfBlockProvider nib = new NestedIfBlockProvider(this.context);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 102 */     Expression defaultBranch = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 110 */     boolean strong = true;
/* 111 */     if (this.context.inOneOrMore) strong = false;
/*     */ 
/*     */     
/* 114 */     FieldItem[] allFi = FieldItemCollector.collect((Expression)exp);
/*     */     
/* 116 */     for (int i = 0; i < children.length; i++) {
/* 117 */       Expression e = children[i];
/* 118 */       FieldItem[] fi = FieldItemCollector.collect(e);
/* 119 */       if (fi.length == 0) {
/*     */         
/* 121 */         if (defaultBranch == null) {
/* 122 */           defaultBranch = children[i];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 143 */         nib.startBlock(strong ? createStrongTest(children[i], allFi) : createWeakTest(fi));
/*     */ 
/*     */         
/* 146 */         this.context.build(children[i]);
/*     */       } 
/*     */     } 
/* 149 */     if (defaultBranch != null) {
/* 150 */       nib.startElse();
/* 151 */       this.context.build(defaultBranch);
/*     */     } 
/*     */     
/* 154 */     nib.end();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onZeroOrMore(Expression exp) {
/* 161 */     JExpression expr = createWeakTest(FieldItemCollector.collect(exp));
/*     */     
/* 163 */     if (expr == null) {
/*     */ 
/*     */       
/* 166 */       this.context.build(exp);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 171 */     this.context.pushNewBlock(createWhileBlock(this.context.getCurrentBlock(), expr));
/*     */     
/* 173 */     this.context.build(exp);
/* 174 */     this.context.popBlock();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMarshallableObject() {
/* 179 */     _assert(false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onField(FieldItem item) {
/* 185 */     FieldMarshallerGenerator fmg = this.context.getMarshaller(item);
/* 186 */     if (fmg == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 191 */     this.context.pushFieldItem(item);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 198 */     if (item.exp.visit((ExpressionVisitorBoolean)isNonOptimizable)) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 203 */       this.context.build(item.exp);
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 208 */     else if (item.multiplicity.max == null) {
/*     */       
/* 210 */       this.context.pushNewBlock(createWhileBlock(this.context.getCurrentBlock(), fmg.hasMore()));
/*     */       
/* 212 */       onTypeItem(item);
/* 213 */       this.context.popBlock();
/*     */     }
/*     */     else {
/*     */       
/* 217 */       for (int i = 0; i < item.multiplicity.min; i++) {
/* 218 */         onTypeItem(item);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 224 */       int repeatCount = item.multiplicity.max.intValue() - item.multiplicity.min;
/*     */       
/* 226 */       if (repeatCount > 0) {
/*     */         
/* 228 */         BlockReference parent = this.context.getCurrentBlock();
/* 229 */         this.context.pushNewBlock((BlockReference)new Object(this, parent, repeatCount, fmg));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 245 */         onTypeItem(item);
/* 246 */         this.context.popBlock();
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 251 */     this.context.popFieldItem(item);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void onTypeItem(FieldItem parent) {
/* 260 */     TypeItem[] types = parent.listTypes();
/* 261 */     TypeItem.sort(types);
/*     */     
/* 263 */     boolean haveSerializableObject = false;
/* 264 */     boolean haveOtherObject = false;
/* 265 */     for (int i = 0; i < types.length; i++) {
/* 266 */       if (types[i] instanceof com.sun.tools.xjc.grammar.PrimitiveItem) { haveOtherObject = true; }
/*     */       
/* 268 */       else if (types[i] instanceof com.sun.tools.xjc.grammar.InterfaceItem) { haveSerializableObject = true; }
/*     */       
/* 270 */       else if (types[i] instanceof com.sun.tools.xjc.grammar.ClassItem) { haveSerializableObject = true; }
/*     */       
/* 272 */       else if (types[i] instanceof com.sun.tools.xjc.grammar.ExternalItem) { haveOtherObject = true; }
/*     */       else
/* 274 */       { throw new JAXBAssertionError(); }
/*     */     
/*     */     } 
/* 277 */     if (haveSerializableObject && !haveOtherObject) {
/*     */       
/* 279 */       this.context.currentSide.onMarshallableObject();
/*     */       return;
/*     */     } 
/* 282 */     if (!haveSerializableObject && types.length == 1) {
/* 283 */       this.context.build((Expression)types[0]);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 288 */     JBlock block = getBlock(true).block();
/* 289 */     this.context.pushNewBlock(block);
/*     */     
/* 291 */     JCodeModel codeModel = this.context.codeModel;
/*     */ 
/*     */ 
/*     */     
/* 295 */     FieldMarshallerGenerator fmg = this.context.getCurrentFieldMarshaller();
/*     */     
/* 297 */     JVar $o = block.decl((JType)codeModel.ref(Object.class), "o", fmg.peek(false));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 302 */     NestedIfBlockProvider nib = new NestedIfBlockProvider(this.context);
/*     */     
/* 304 */     if (haveSerializableObject) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 311 */       nib.startBlock($o._instanceof((JType)codeModel.ref(JAXBObject.class)));
/* 312 */       this.context.currentSide.onMarshallableObject();
/*     */     } 
/*     */     
/* 315 */     for (int j = 0; j < types.length; j++) {
/* 316 */       if (types[j] instanceof com.sun.tools.xjc.grammar.PrimitiveItem || types[j] instanceof com.sun.tools.xjc.grammar.ExternalItem) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 323 */         nib.startBlock(instanceOf((JExpression)$o, types[j].getType()));
/* 324 */         this.context.build((Expression)types[j]);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 330 */     nib.startElse();
/*     */ 
/*     */     
/* 333 */     if (getBlock(false) != null) {
/* 334 */       getBlock(false).staticInvoke(this.context.getRuntime(Util.class), "handleTypeMismatchError").arg((JExpression)this.context.$serializer).arg(JExpr._this()).arg(JExpr.lit((fmg.owner().getFieldUse()).name)).arg((JExpression)$o);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 340 */     nib.end();
/* 341 */     this.context.popBlock();
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
/*     */   private JExpression createWeakTest(FieldItem[] fi) {
/* 365 */     JExpression expr = JExpr.FALSE;
/* 366 */     for (int i = 0; i < fi.length; i++) {
/* 367 */       FieldMarshallerGenerator fmg = this.context.getMarshaller(fi[i]);
/* 368 */       if (fmg != null) {
/* 369 */         expr = expr.cor(fmg.hasMore());
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 374 */     return expr;
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
/*     */   private JExpression createStrongTest(Expression branch, FieldItem[] fi) {
/* 394 */     JExpression expr = JExpr.TRUE;
/* 395 */     for (int i = 0; i < fi.length; i++) {
/* 396 */       FieldRenderer fr = this.context.getMarshaller(fi[i]).owner();
/*     */       
/* 398 */       Multiplicity m = FieldMultiplicityCounter.count(branch, fi[i]);
/*     */ 
/*     */       
/* 401 */       JExpression e = JExpr.TRUE, f = JExpr.TRUE;
/*     */       
/* 403 */       if (m.max != null && m.min == m.max.intValue()) {
/*     */         
/* 405 */         e = fr.ifCountEqual(m.min);
/*     */       }
/*     */       else {
/*     */         
/* 409 */         if (m.min != 0) {
/* 410 */           e = fr.ifCountGte(m.min);
/*     */         }
/* 412 */         if (m.max != null) {
/* 413 */           f = fr.ifCountLte(m.max.intValue());
/*     */         }
/*     */       } 
/* 416 */       expr = expr.cand(e).cand(f);
/*     */     } 
/*     */     
/* 419 */     return expr;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\marshaller\Outside.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */