/*     */ package 1.0.com.sun.tools.xjc.grammar;
/*     */ 
/*     */ import com.sun.codemodel.JBlock;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.msv.grammar.ExpressionPool;
/*     */ import com.sun.msv.grammar.NameClass;
/*     */ import com.sun.msv.grammar.trex.ElementPattern;
/*     */ import com.sun.tools.xjc.generator.GeneratorContext;
/*     */ import com.sun.tools.xjc.generator.marshaller.FieldMarshallerGenerator;
/*     */ import com.sun.tools.xjc.grammar.JavaItemVisitor;
/*     */ import com.sun.tools.xjc.grammar.TypeItem;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ExternalItem
/*     */   extends TypeItem
/*     */ {
/*     */   public final NameClass elementName;
/*     */   
/*     */   public ExternalItem(String displayName, NameClass _elementName, Locator loc) {
/*  37 */     super(displayName, loc);
/*  38 */     this.elementName = _elementName;
/*     */ 
/*     */     
/*  41 */     this.exp = (Expression)new ElementPattern(NameClass.ALL, Expression.epsilon);
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
/*     */   public abstract Expression createAGM(ExpressionPool paramExpressionPool);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Expression createValidationFragment();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void generateMarshaller(GeneratorContext paramGeneratorContext, JBlock paramJBlock, FieldMarshallerGenerator paramFieldMarshallerGenerator, JExpression paramJExpression);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract JExpression generateUnmarshaller(GeneratorContext paramGeneratorContext, JExpression paramJExpression1, JBlock paramJBlock, JExpression paramJExpression2, JVar paramJVar1, JVar paramJVar2, JVar paramJVar3, JVar paramJVar4);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Object visitJI(JavaItemVisitor visitor) {
/* 118 */     return visitor.onExternal(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\ExternalItem.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */