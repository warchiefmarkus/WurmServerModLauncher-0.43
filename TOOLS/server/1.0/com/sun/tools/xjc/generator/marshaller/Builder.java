/*     */ package 1.0.com.sun.tools.xjc.generator.marshaller;
/*     */ 
/*     */ import com.sun.codemodel.JBlock;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.msv.grammar.AttributeExp;
/*     */ import com.sun.msv.grammar.ChoiceExp;
/*     */ import com.sun.msv.grammar.ConcurExp;
/*     */ import com.sun.msv.grammar.DataExp;
/*     */ import com.sun.msv.grammar.ElementExp;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.msv.grammar.ExpressionVisitorVoid;
/*     */ import com.sun.msv.grammar.MixedExp;
/*     */ import com.sun.msv.grammar.OneOrMoreExp;
/*     */ import com.sun.msv.grammar.OtherExp;
/*     */ import com.sun.msv.grammar.ValueExp;
/*     */ import com.sun.msv.grammar.xmlschema.OccurrenceExp;
/*     */ import com.sun.tools.xjc.generator.marshaller.Context;
/*     */ import com.sun.tools.xjc.generator.marshaller.PrintExceptionTryCatchBlockReference;
/*     */ import com.sun.tools.xjc.generator.util.BlockReference;
/*     */ import com.sun.tools.xjc.grammar.BGMWalker;
/*     */ import com.sun.tools.xjc.grammar.ClassItem;
/*     */ import com.sun.tools.xjc.grammar.ExternalItem;
/*     */ import com.sun.tools.xjc.grammar.FieldItem;
/*     */ import com.sun.tools.xjc.grammar.IgnoreItem;
/*     */ import com.sun.tools.xjc.grammar.InterfaceItem;
/*     */ import com.sun.tools.xjc.grammar.PrimitiveItem;
/*     */ import com.sun.tools.xjc.grammar.SuperClassItem;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class Builder
/*     */   extends BGMWalker
/*     */ {
/*     */   private final Context context;
/*     */   
/*     */   protected Builder(Context _context) {
/*  44 */     this.context = _context;
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
/*     */   public final void onChoice(ChoiceExp exp) {
/*  57 */     if (exp.exp1 == Expression.epsilon && exp.exp2 instanceof OneOrMoreExp) {
/*  58 */       onOneOrMore((OneOrMoreExp)exp.exp2);
/*     */       return;
/*     */     } 
/*  61 */     if (exp.exp2 == Expression.epsilon && exp.exp1 instanceof OneOrMoreExp) {
/*  62 */       onOneOrMore((OneOrMoreExp)exp.exp1);
/*     */       
/*     */       return;
/*     */     } 
/*  66 */     this.context.currentSide.onChoice(exp);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void onOneOrMore(OneOrMoreExp exp) {
/*  72 */     _onOneOrMore(exp.exp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void _onOneOrMore(Expression itemExp) {
/*  82 */     boolean oldOOM = this.context.inOneOrMore;
/*  83 */     this.context.inOneOrMore = true;
/*     */     
/*  85 */     this.context.currentSide.onZeroOrMore(itemExp);
/*     */     
/*  87 */     this.context.inOneOrMore = oldOOM;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void onNullSet() {
/*  95 */     getBlock(true)._throw((JExpression)JExpr._new(this.context.codeModel.ref(SAXException.class)).arg(JExpr.lit("this object doesn't have any XML representation")));
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
/*     */   public Object onIgnore(IgnoreItem exp) {
/* 109 */     if (exp.exp.isEpsilonReducible()) {
/* 110 */       return null;
/*     */     }
/*     */     
/* 113 */     exp.exp.visit((ExpressionVisitorVoid)this);
/* 114 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onConcur(ConcurExp exp) {
/* 119 */     throw new JAXBAssertionError(); }
/* 120 */   public void onMixed(MixedExp exp) { throw new JAXBAssertionError(); } public void onData(DataExp exp) {
/* 121 */     throw new JAXBAssertionError();
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
/*     */   public void onAnyString() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void onValue(ValueExp exp) {
/* 142 */     this.context.currentPass.onValue(exp);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object onSuper(SuperClassItem exp) {
/* 147 */     if (this.context.currentPass == this.context.skipPass) {
/* 148 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 153 */     getBlock(true).invoke(JExpr._super(), "serialize" + this.context.currentPass.getName()).arg((JExpression)this.context.$serializer);
/*     */ 
/*     */     
/* 156 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onAttribute(AttributeExp exp) {
/* 161 */     this.context.currentPass.onAttribute(exp);
/*     */   }
/*     */   
/*     */   public void onElement(ElementExp exp) {
/* 165 */     this.context.currentPass.onElement(exp);
/*     */   }
/*     */   
/*     */   public final Object onInterface(InterfaceItem exp) {
/* 169 */     this.context.currentSide.onMarshallableObject();
/* 170 */     return null;
/*     */   }
/*     */   
/*     */   public final Object onClass(ClassItem exp) {
/* 174 */     this.context.currentSide.onMarshallableObject();
/* 175 */     return null;
/*     */   }
/*     */   
/*     */   public Object onField(FieldItem item) {
/* 179 */     this.context.currentSide.onField(item);
/* 180 */     return null;
/*     */   }
/*     */   
/*     */   public final Object onExternal(ExternalItem item) {
/* 184 */     this.context.currentPass.onExternal(item);
/* 185 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Object onPrimitive(PrimitiveItem item) {
/* 191 */     this.context.pushNewBlock((BlockReference)new PrintExceptionTryCatchBlockReference(this.context));
/* 192 */     this.context.currentPass.onPrimitive(item);
/* 193 */     this.context.popBlock();
/* 194 */     return null;
/*     */   }
/*     */   
/*     */   public void onOther(OtherExp exp) {
/* 198 */     if (exp instanceof OccurrenceExp) {
/* 199 */       onOccurence((OccurrenceExp)exp);
/*     */     } else {
/* 201 */       super.onOther(exp);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onOccurence(OccurrenceExp exp) {
/* 206 */     _onOneOrMore(exp.itemExp);
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
/*     */   protected final JBlock getBlock(boolean create) {
/* 219 */     return this.context.getCurrentBlock().get(create);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\marshaller\Builder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */