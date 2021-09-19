/*     */ package 1.0.com.sun.tools.xjc.grammar.ext;
/*     */ 
/*     */ import com.sun.codemodel.JAssignmentTarget;
/*     */ import com.sun.codemodel.JBlock;
/*     */ import com.sun.codemodel.JCatchBlock;
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JTryBlock;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.msv.grammar.NameClass;
/*     */ import com.sun.tools.xjc.generator.GeneratorContext;
/*     */ import com.sun.tools.xjc.generator.marshaller.FieldMarshallerGenerator;
/*     */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*     */ import com.sun.tools.xjc.grammar.ext.AbstractDOMItem;
/*     */ import com.sun.tools.xjc.grammar.ext.DOMItemFactory;
/*     */ import com.sun.tools.xjc.runtime.ContentHandlerAdaptor;
/*     */ import com.sun.tools.xjc.runtime.W3CDOMUnmarshallingEventHandler;
/*     */ import com.sun.xml.bind.unmarshaller.DOMScanner;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.w3c.dom.Element;
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
/*     */ class W3CDOMItem
/*     */   extends AbstractDOMItem
/*     */ {
/*  38 */   public static DOMItemFactory factory = (DOMItemFactory)new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public W3CDOMItem(NameClass _elementName, AnnotatedGrammar grammar, Locator loc) {
/*  45 */     super(_elementName, grammar, loc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void generateMarshaller(GeneratorContext context, JBlock block, FieldMarshallerGenerator fmg, JExpression $context) {
/*  52 */     block.invoke((JExpression)JExpr._new(this.codeModel.ref(DOMScanner.class)), "parse").arg((JExpression)JExpr.cast((JType)this.codeModel.ref(Element.class), fmg.peek(true))).arg((JExpression)JExpr._new(context.getRuntime(ContentHandlerAdaptor.class)).arg($context));
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
/*     */   public JExpression generateUnmarshaller(GeneratorContext context, JExpression $context, JBlock block, JExpression memento, JVar $uri, JVar $local, JVar $qname, JVar $atts) {
/*  75 */     JVar $v = block.decl((JType)this.codeModel.ref(Element.class), "ur", JExpr._null());
/*  76 */     JClass handlerClass = context.getRuntime(W3CDOMUnmarshallingEventHandler.class);
/*     */     
/*  78 */     JTryBlock tryBlock = block._try();
/*     */     
/*  80 */     block = tryBlock.body();
/*  81 */     JVar $u = block.decl((JType)handlerClass, "u", (JExpression)JExpr._new(handlerClass).arg($context));
/*  82 */     block.invoke($context, "pushContentHandler").arg((JExpression)$u).arg(memento);
/*  83 */     block.invoke((JExpression)$context.invoke("getCurrentHandler"), "enterElement").arg((JExpression)$uri).arg((JExpression)$local).arg((JExpression)$qname).arg((JExpression)$atts);
/*     */     
/*  85 */     block.assign((JAssignmentTarget)$v, (JExpression)$u.invoke("getOwner"));
/*     */     
/*  87 */     JCatchBlock catchBlock = tryBlock._catch(this.codeModel.ref(ParserConfigurationException.class));
/*     */ 
/*     */     
/*  90 */     catchBlock.body().invoke("handleGenericException").arg((JExpression)catchBlock.param("e"));
/*     */ 
/*     */     
/*  93 */     return (JExpression)$v;
/*     */   }
/*     */   
/*     */   public JType getType() {
/*  97 */     return (JType)this.codeModel.ref(Element.class);
/*     */   }
/*     */   
/*     */   public JExpression createRootUnmarshaller(GeneratorContext context, JVar $unmarshallingContext) {
/* 101 */     JClass handlerClass = context.getRuntime(W3CDOMUnmarshallingEventHandler.class);
/*     */ 
/*     */ 
/*     */     
/* 105 */     return (JExpression)JExpr._new(handlerClass).arg((JExpression)$unmarshallingContext);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\ext\W3CDOMItem.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */