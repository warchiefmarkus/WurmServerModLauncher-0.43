/*     */ package 1.0.com.sun.tools.xjc.grammar.ext;
/*     */ 
/*     */ import com.sun.codemodel.JBlock;
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JInvocation;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.msv.grammar.AttributeExp;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.msv.grammar.ExpressionPool;
/*     */ import com.sun.msv.grammar.NameClass;
/*     */ import com.sun.msv.grammar.NamespaceNameClass;
/*     */ import com.sun.msv.grammar.trex.ElementPattern;
/*     */ import com.sun.tools.xjc.generator.GeneratorContext;
/*     */ import com.sun.tools.xjc.generator.marshaller.FieldMarshallerGenerator;
/*     */ import com.sun.tools.xjc.grammar.ExternalItem;
/*     */ import com.sun.tools.xjc.reader.xmlschema.WildcardNameClassBuilder;
/*     */ import com.sun.xml.bind.JAXBObject;
/*     */ import com.sun.xml.bind.xmlschema.LaxWildcardPlug;
/*     */ import com.sun.xml.bind.xmlschema.StrictWildcardPlug;
/*     */ import com.sun.xml.xsom.XSWildcard;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WildcardItem
/*     */   extends ExternalItem
/*     */ {
/*     */   public final boolean errorIfNotFound;
/*     */   private final JClass refObject;
/*     */   
/*     */   public WildcardItem(JCodeModel codeModel, NameClass nc, boolean errorIfNotFound, Locator loc) {
/*  56 */     super("wildcard", nc, loc);
/*  57 */     this.refObject = codeModel.ref(Object.class);
/*  58 */     this.errorIfNotFound = errorIfNotFound;
/*     */   }
/*     */   
/*     */   public WildcardItem(JCodeModel codeModel, XSWildcard wc) {
/*  62 */     this(codeModel, WildcardNameClassBuilder.build(wc), (wc.getMode() == 2), wc.getLocator());
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
/*     */   public JType getType() {
/*  74 */     return (JType)this.refObject;
/*     */   }
/*     */ 
/*     */   
/*     */   public Expression createAGM(ExpressionPool pool) {
/*     */     LaxWildcardPlug laxWildcardPlug;
/*  80 */     if (this.errorIfNotFound) { StrictWildcardPlug strictWildcardPlug = new StrictWildcardPlug(this.elementName); }
/*  81 */     else { laxWildcardPlug = new LaxWildcardPlug(this.elementName); }
/*     */     
/*  83 */     return (Expression)laxWildcardPlug;
/*     */   }
/*     */   
/*     */   public Expression createValidationFragment() {
/*  87 */     return (Expression)new ElementPattern((NameClass)new NamespaceNameClass("http://java.sun.com/jaxb/xjc/dummy-elements"), (Expression)new AttributeExp(this.elementName, Expression.anyString));
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
/*     */   public void generateMarshaller(GeneratorContext context, JBlock block, FieldMarshallerGenerator fmg, JExpression $context) {
/*  99 */     block.invoke($context, "childAsBody").arg((JExpression)JExpr.cast((JType)context.getCodeModel().ref(JAXBObject.class), fmg.peek(true))).arg(JExpr.lit((fmg.owner().getFieldUse()).name));
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
/*     */   public JExpression generateUnmarshaller(GeneratorContext context, JExpression $unmarshallingContext, JBlock block, JExpression memento, JVar $uri, JVar $local, JVar $qname, JVar $atts) {
/* 113 */     JInvocation spawn = JExpr.invoke("spawnWildcard").arg(memento).arg((JExpression)$uri).arg((JExpression)$local).arg((JExpression)$qname).arg((JExpression)$atts);
/*     */ 
/*     */     
/* 116 */     return (JExpression)block.decl(getType(), "co", (JExpression)spawn);
/*     */   }
/*     */ 
/*     */   
/*     */   public JExpression createRootUnmarshaller(GeneratorContext context, JVar $unmarshallingContext) {
/* 121 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\ext\WildcardItem.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */