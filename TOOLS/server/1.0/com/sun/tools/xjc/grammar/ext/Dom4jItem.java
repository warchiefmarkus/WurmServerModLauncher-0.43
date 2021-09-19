/*    */ package 1.0.com.sun.tools.xjc.grammar.ext;
/*    */ 
/*    */ import com.sun.codemodel.JBlock;
/*    */ import com.sun.codemodel.JClass;
/*    */ import com.sun.codemodel.JExpr;
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.codemodel.JType;
/*    */ import com.sun.codemodel.JVar;
/*    */ import com.sun.msv.grammar.NameClass;
/*    */ import com.sun.tools.xjc.generator.GeneratorContext;
/*    */ import com.sun.tools.xjc.generator.marshaller.FieldMarshallerGenerator;
/*    */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*    */ import com.sun.tools.xjc.grammar.ext.AbstractDOMItem;
/*    */ import com.sun.tools.xjc.grammar.ext.DOMItemFactory;
/*    */ import com.sun.tools.xjc.runtime.ContentHandlerAdaptor;
/*    */ import com.sun.tools.xjc.runtime.Dom4jUnmarshallingEventHandler;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class Dom4jItem
/*    */   extends AbstractDOMItem
/*    */ {
/*    */   private final JType elementType;
/* 38 */   public static DOMItemFactory factory = (DOMItemFactory)new Object();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Dom4jItem(NameClass _elementName, AnnotatedGrammar grammar, Locator loc) {
/* 45 */     super(_elementName, grammar, loc);
/*    */     
/* 47 */     this.elementType = createPhantomType("org.dom4j.Element");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void generateMarshaller(GeneratorContext context, JBlock block, FieldMarshallerGenerator fmg, JExpression $context) {
/* 56 */     block = block.block();
/* 57 */     block.directStatement("org.dom4j.io.SAXWriter w = new org.dom4j.io.SAXWriter();");
/* 58 */     JExpression $w = JExpr.direct("w");
/* 59 */     block.invoke($w, "setContentHandler").arg((JExpression)JExpr._new(context.getRuntime(ContentHandlerAdaptor.class)).arg($context));
/*    */ 
/*    */     
/* 62 */     block.invoke($w, "write").arg((JExpression)JExpr.cast(this.elementType, fmg.peek(true)));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JExpression generateUnmarshaller(GeneratorContext context, JExpression $context, JBlock block, JExpression memento, JVar $uri, JVar $local, JVar $qname, JVar $atts) {
/* 71 */     JClass handlerClass = context.getRuntime(Dom4jUnmarshallingEventHandler.class);
/* 72 */     JVar $u = block.decl((JType)handlerClass, "u", (JExpression)JExpr._new(handlerClass).arg($context));
/*    */     
/* 74 */     block.invoke($context, "pushContentHandler").arg((JExpression)$u).arg(memento);
/* 75 */     block.invoke((JExpression)$context.invoke("getCurrentHandler"), "enterElement").arg((JExpression)$uri).arg((JExpression)$local).arg((JExpression)$qname).arg((JExpression)$atts);
/*    */ 
/*    */ 
/*    */     
/* 79 */     return (JExpression)$u.invoke("getOwner");
/*    */   }
/*    */   
/*    */   public JType getType() {
/* 83 */     return this.elementType;
/*    */   }
/*    */   
/*    */   public JExpression createRootUnmarshaller(GeneratorContext context, JVar $unmarshallingContext) {
/* 87 */     JClass handlerClass = context.getRuntime(Dom4jUnmarshallingEventHandler.class);
/*    */ 
/*    */ 
/*    */     
/* 91 */     return (JExpression)JExpr._new(handlerClass).arg((JExpression)$unmarshallingContext);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\ext\Dom4jItem.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */