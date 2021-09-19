/*     */ package 1.0.com.sun.tools.xjc.generator.marshaller;
/*     */ 
/*     */ import com.sun.codemodel.JBlock;
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JInvocation;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.msv.grammar.ChoiceExp;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.tools.xjc.generator.LookupTableUse;
/*     */ import com.sun.tools.xjc.generator.marshaller.AbstractSideImpl;
/*     */ import com.sun.tools.xjc.generator.marshaller.Context;
/*     */ import com.sun.tools.xjc.generator.marshaller.FieldMarshallerGenerator;
/*     */ import com.sun.tools.xjc.generator.marshaller.IfThenElseBlockReference;
/*     */ import com.sun.tools.xjc.generator.marshaller.NestedIfBlockProvider;
/*     */ import com.sun.tools.xjc.grammar.ClassItem;
/*     */ import com.sun.tools.xjc.grammar.FieldItem;
/*     */ import com.sun.tools.xjc.grammar.TypeItem;
/*     */ import com.sun.tools.xjc.grammar.util.TypeItemCollector;
/*     */ import com.sun.tools.xjc.grammar.xducer.SerializerContext;
/*     */ import com.sun.tools.xjc.runtime.Util;
/*     */ import com.sun.tools.xjc.runtime.ValidatableObject;
/*     */ import com.sun.xml.bind.JAXBObject;
/*     */ import com.sun.xml.bind.ProxyGroup;
/*     */ import javax.xml.bind.Element;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class Inside
/*     */   extends AbstractSideImpl
/*     */ {
/*     */   public Inside(Context _context) {
/*  35 */     super(_context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isImplentingElement(TypeItem t) {
/*  42 */     JType jt = t.getType();
/*  43 */     if (jt.isPrimitive()) return false; 
/*  44 */     JClass jc = (JClass)jt;
/*     */     
/*  46 */     return this.context.codeModel.ref(Element.class).isAssignableFrom(jc);
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
/*     */   private boolean tryOptimizedChoice1(Expression[] children, TypeItem[][] types) {
/*  65 */     FieldMarshallerGenerator fmg = this.context.getCurrentFieldMarshaller();
/*     */     
/*  67 */     Expression rest = Expression.nullSet;
/*  68 */     int count = 0;
/*     */     
/*  70 */     for (int i = 0; i < children.length; i++) {
/*  71 */       if ((types[i]).length != 1)
/*     */       {
/*     */         
/*  74 */         return false;
/*     */       }
/*  76 */       if (isImplentingElement(types[i][0])) {
/*  77 */         if (!(children[i] instanceof ClassItem)) {
/*  78 */           return false;
/*     */         }
/*  80 */         count++;
/*     */       } else {
/*     */         
/*  83 */         rest = this.context.pool.createChoice(rest, children[i]);
/*     */       } 
/*     */     } 
/*     */     
/*  87 */     if (count == 0) {
/*  88 */       return false;
/*     */     }
/*  90 */     if (rest == Expression.nullSet) {
/*     */ 
/*     */       
/*  93 */       onMarshallableObject();
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */       
/* 101 */       IfThenElseBlockReference ifb = new IfThenElseBlockReference(this.context, fmg.peek(false)._instanceof((JType)this.context.codeModel.ref(Element.class)));
/*     */ 
/*     */       
/* 104 */       this.context.pushNewBlock(ifb.createThenProvider());
/* 105 */       onMarshallableObject();
/* 106 */       this.context.popBlock();
/*     */       
/* 108 */       this.context.pushNewBlock(ifb.createElseProvider());
/* 109 */       this.context.build(rest);
/* 110 */       this.context.popBlock();
/*     */     } 
/*     */     
/* 113 */     return true;
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
/*     */   private boolean tryOptimizedChoice2(ChoiceExp exp, Expression[] children) {
/* 126 */     LookupTableUse tableUse = this.context.genContext.getLookupTableBuilder().buildTable(exp);
/* 127 */     if (tableUse == null) return false;
/*     */     
/* 129 */     NestedIfBlockProvider nib = null;
/* 130 */     FieldMarshallerGenerator fmg = this.context.getCurrentFieldMarshaller();
/*     */     
/* 132 */     if (tableUse.anomaly != null) {
/* 133 */       if (!(tableUse.anomaly instanceof ClassItem)) {
/* 134 */         return false;
/*     */       }
/* 136 */       JClass vo = this.context.getRuntime(ValidatableObject.class);
/* 137 */       JInvocation jInvocation = JExpr.cast((JType)vo, (JExpression)this.context.codeModel.ref(ProxyGroup.class).staticInvoke("blindWrap").arg(fmg.peek(false)).arg(vo.dotclass()).arg(JExpr._null())).invoke("getPrimaryInterface");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 144 */       ClassItem ancls = (ClassItem)tableUse.anomaly;
/* 145 */       nib = new NestedIfBlockProvider(this.context);
/* 146 */       nib.startBlock(jInvocation.ne(ancls.getTypeAsDefined().dotclass()));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 151 */     if (this.context.currentPass != this.context.skipPass)
/*     */     {
/*     */       
/* 154 */       if (this.context.currentPass == this.context.uriPass) {
/* 155 */         getBlock(true).invoke((JExpression)this.context.$serializer.invoke("getNamespaceContext"), "declareNamespace").arg(JExpr.lit(tableUse.switchAttName.namespaceURI)).arg(JExpr._null()).arg(JExpr.FALSE);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 161 */         tableUse.table.declareNamespace(this.context.getCurrentBlock(), fmg.peek(false), (SerializerContext)this.context);
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 166 */       else if (this.context.currentPass != this.context.bodyPass) {
/*     */ 
/*     */         
/* 169 */         if (this.context.currentPass == this.context.attPass) {
/* 170 */           JBlock block = getBlock(true);
/*     */ 
/*     */           
/* 173 */           block.invoke((JExpression)this.context.$serializer, "startAttribute").arg(JExpr.lit(tableUse.switchAttName.namespaceURI)).arg(JExpr.lit(tableUse.switchAttName.localName));
/*     */ 
/*     */ 
/*     */           
/* 177 */           block.invoke((JExpression)this.context.$serializer, "text").arg(tableUse.table.reverseLookup(fmg.peek(false), (SerializerContext)this.context)).arg(JExpr.lit((fmg.owner().getFieldUse()).name));
/*     */ 
/*     */ 
/*     */           
/* 181 */           block.invoke((JExpression)this.context.$serializer, "endAttribute");
/*     */         } else {
/* 183 */           _assert(false);
/*     */         } 
/*     */       } 
/*     */     }
/* 187 */     if (nib != null) {
/* 188 */       nib.end();
/*     */     }
/* 190 */     onMarshallableObject();
/*     */     
/* 192 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChoice(ChoiceExp exp) {
/* 197 */     FieldMarshallerGenerator fmg = this.context.getCurrentFieldMarshaller();
/*     */     
/* 199 */     Expression[] children = exp.getChildren();
/* 200 */     TypeItem[][] types = new TypeItem[children.length][];
/*     */     
/* 202 */     for (int i = 0; i < children.length; i++) {
/* 203 */       types[i] = TypeItemCollector.collect(children[i]);
/*     */     }
/*     */     
/* 206 */     if (tryOptimizedChoice1(children, types))
/*     */       return; 
/* 208 */     if (tryOptimizedChoice2(exp, children)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 214 */     NestedIfBlockProvider nib = new NestedIfBlockProvider(this.context);
/*     */     
/* 216 */     for (int j = 0; j < children.length; j++) {
/* 217 */       if ((types[j]).length == 0) {
/*     */ 
/*     */         
/* 220 */         nib.startBlock(fmg.hasMore().not());
/*     */       } else {
/*     */         
/* 223 */         JExpression testExp = null;
/* 224 */         for (int k = 0; k < (types[j]).length; k++) {
/* 225 */           JType t = types[j][k].getType();
/* 226 */           JExpression e = instanceOf(fmg.peek(false), t);
/*     */           
/* 228 */           if (testExp == null) { testExp = e; }
/* 229 */           else { testExp = testExp.cor(e); }
/*     */         
/* 231 */         }  nib.startBlock(testExp);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 242 */       this.context.build(children[j]);
/*     */     } 
/*     */     
/* 245 */     nib.startElse();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 251 */     if (getBlock(false) != null) {
/* 252 */       getBlock(false).staticInvoke(this.context.getRuntime(Util.class), "handleTypeMismatchError").arg((JExpression)this.context.$serializer).arg(JExpr._this()).arg(JExpr.lit((fmg.owner().getFieldUse()).name)).arg(fmg.peek(false));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 258 */     nib.end();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onZeroOrMore(Expression exp) {
/* 268 */     JExpression expr = this.context.getCurrentFieldMarshaller().hasMore();
/*     */ 
/*     */     
/* 271 */     this.context.pushNewBlock(createWhileBlock(this.context.getCurrentBlock(), expr));
/*     */     
/* 273 */     this.context.build(exp);
/* 274 */     this.context.popBlock();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMarshallableObject() {
/* 281 */     FieldMarshallerGenerator fm = this.context.getCurrentFieldMarshaller();
/*     */     
/* 283 */     if (this.context.currentPass == this.context.skipPass) {
/* 284 */       fm.increment(this.context.getCurrentBlock());
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */     
/* 292 */     JClass joRef = this.context.codeModel.ref(JAXBObject.class);
/*     */     
/* 294 */     getBlock(true).invoke((JExpression)this.context.$serializer, "childAs" + this.context.currentPass.getName()).arg((JExpression)JExpr.cast((JType)joRef, fm.peek(true))).arg(JExpr.lit((fm.owner().getFieldUse()).name));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onField(FieldItem item) {
/* 305 */     _assert(false);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\marshaller\Inside.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */