/*     */ package 1.0.com.sun.tools.xjc.reader.decorator;
/*     */ 
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.msv.grammar.NameClass;
/*     */ import com.sun.msv.grammar.NameClassAndExpression;
/*     */ import com.sun.msv.grammar.SimpleNameClass;
/*     */ import com.sun.msv.reader.GrammarReader;
/*     */ import com.sun.msv.reader.State;
/*     */ import com.sun.msv.util.StartTagInfo;
/*     */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*     */ import com.sun.tools.xjc.reader.NameConverter;
/*     */ import com.sun.tools.xjc.reader.decorator.Decorator;
/*     */ import com.sun.tools.xjc.reader.decorator.Messages;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DecoratorImpl
/*     */   implements Decorator
/*     */ {
/*     */   protected final GrammarReader reader;
/*     */   protected final AnnotatedGrammar grammar;
/*     */   protected final JCodeModel codeModel;
/*     */   protected final NameConverter nameConverter;
/*     */   
/*     */   protected DecoratorImpl(GrammarReader _reader, AnnotatedGrammar _grammar, NameConverter nc) {
/*  33 */     this.reader = _reader;
/*  34 */     this.grammar = _grammar;
/*  35 */     this.codeModel = this.grammar.codeModel;
/*  36 */     this.nameConverter = nc;
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
/*     */   protected final String getAttribute(StartTagInfo tag, String attName) {
/*  70 */     return tag.getAttribute("http://java.sun.com/xml/ns/jaxb", attName);
/*     */   }
/*     */   
/*     */   protected final String getAttribute(StartTagInfo tag, String attName, String defaultValue) {
/*  74 */     String r = getAttribute(tag, attName);
/*  75 */     if (r != null) return r; 
/*  76 */     return defaultValue;
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
/*     */   protected final String decideName(State state, Expression exp, String role, String suffix, Locator loc) {
/*  89 */     StartTagInfo tag = state.getStartTag();
/*     */     
/*  91 */     String name = getAttribute(tag, "name");
/*     */     
/*  93 */     if (name == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  98 */       name = tag.getAttribute("name");
/*  99 */       if (name != null) name = xmlNameToJavaName(role, name + suffix);
/*     */     
/*     */     } 
/* 102 */     if (name == null)
/*     */     {
/*     */ 
/*     */       
/* 106 */       if (exp instanceof NameClassAndExpression) {
/* 107 */         NameClass nc = ((NameClassAndExpression)exp).getNameClass();
/* 108 */         if (nc instanceof SimpleNameClass) {
/* 109 */           name = xmlNameToJavaName(role, ((SimpleNameClass)nc).localName + suffix);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 115 */     if (name == null) {
/* 116 */       if (state.getParentState() instanceof com.sun.msv.reader.ExpressionState || state.getParentState() instanceof com.sun.msv.reader.trex.DefineState)
/*     */       {
/*     */         
/* 119 */         return decideName(state.getParentState(), exp, role, suffix, loc);
/*     */       }
/*     */       
/* 122 */       this.reader.controller.error(new SAXParseException(Messages.format("NameNeeded"), loc));
/*     */       
/* 124 */       return "DUMMY";
/*     */     } 
/*     */     
/* 127 */     return name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String xmlNameToJavaName(String role, String name) {
/* 136 */     if (role.equals("field")) return this.nameConverter.toPropertyName(name); 
/* 137 */     if (role.equals("interface")) return this.nameConverter.toInterfaceName(name); 
/* 138 */     if (role.equals("class")) return this.nameConverter.toClassName(name);
/*     */     
/* 140 */     throw new JAXBAssertionError(role);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\decorator\DecoratorImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */