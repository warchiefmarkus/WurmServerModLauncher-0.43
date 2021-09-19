/*    */ package 1.0.com.sun.tools.xjc.grammar.xducer;
/*    */ 
/*    */ import com.sun.codemodel.JCodeModel;
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.msv.datatype.xsd.WhiteSpaceProcessor;
/*    */ import com.sun.tools.xjc.generator.util.WhitespaceNormalizer;
/*    */ import com.sun.tools.xjc.grammar.xducer.DeserializerContext;
/*    */ import com.sun.tools.xjc.grammar.xducer.Transducer;
/*    */ import com.sun.tools.xjc.grammar.xducer.TransducerDecorator;
/*    */ import com.sun.xml.bind.JAXBAssertionError;
/*    */ import com.sun.xml.xsom.XSFacet;
/*    */ import com.sun.xml.xsom.XSSimpleType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WhitespaceTransducer
/*    */   extends TransducerDecorator
/*    */ {
/*    */   private final JCodeModel codeModel;
/*    */   private final WhitespaceNormalizer ws;
/*    */   
/*    */   private WhitespaceTransducer(Transducer _core, JCodeModel _codeModel, WhitespaceNormalizer _ws) {
/* 27 */     super(_core);
/* 28 */     this.codeModel = _codeModel;
/* 29 */     this.ws = _ws;
/*    */   }
/*    */ 
/*    */   
/*    */   public static Transducer create(Transducer _core, JCodeModel _codeModel, WhitespaceNormalizer _ws) {
/* 34 */     if (_ws == WhitespaceNormalizer.PRESERVE) {
/* 35 */       return _core;
/*    */     }
/* 37 */     return (Transducer)new com.sun.tools.xjc.grammar.xducer.WhitespaceTransducer(_core, _codeModel, _ws);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Transducer create(Transducer _core, JCodeModel _codeModel, WhiteSpaceProcessor wsf) {
/* 44 */     return create(_core, _codeModel, getNormalizer(wsf));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Transducer create(Transducer _core, JCodeModel _codeModel, XSSimpleType t) {
/* 51 */     XSFacet f = t.getFacet("whiteSpace");
/* 52 */     if (f == null) {
/* 53 */       return _core;
/*    */     }
/* 55 */     return create(_core, _codeModel, WhitespaceNormalizer.parse(f.getValue()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isBuiltin() {
/* 61 */     return this.core.isBuiltin();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public JExpression generateDeserializer(JExpression literal, DeserializerContext context) {
/* 67 */     return super.generateDeserializer(this.ws.generate(this.codeModel, literal), context);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static WhitespaceNormalizer getNormalizer(WhiteSpaceProcessor proc) {
/* 73 */     if (proc == WhiteSpaceProcessor.theCollapse) {
/* 74 */       return WhitespaceNormalizer.COLLAPSE;
/*    */     }
/* 76 */     if (proc == WhiteSpaceProcessor.theReplace) {
/* 77 */       return WhitespaceNormalizer.REPLACE;
/*    */     }
/* 79 */     if (proc == WhiteSpaceProcessor.thePreserve) {
/* 80 */       return WhitespaceNormalizer.PRESERVE;
/*    */     }
/* 82 */     throw new JAXBAssertionError();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\xducer\WhitespaceTransducer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */