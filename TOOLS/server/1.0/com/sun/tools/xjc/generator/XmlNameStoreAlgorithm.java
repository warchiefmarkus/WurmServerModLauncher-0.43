/*     */ package 1.0.com.sun.tools.xjc.generator;
/*     */ 
/*     */ import com.sun.codemodel.JBlock;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.msv.grammar.NameClass;
/*     */ import com.sun.msv.grammar.NameClassAndExpression;
/*     */ import com.sun.msv.grammar.NameClassVisitor;
/*     */ import com.sun.msv.grammar.SimpleNameClass;
/*     */ import com.sun.tools.xjc.generator.ClassContext;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class XmlNameStoreAlgorithm
/*     */ {
/*     */   static Class class$java$lang$String;
/*     */   static Class class$javax$xml$namespace$QName;
/*     */   
/*     */   public abstract JExpression getNamespaceURI();
/*     */   
/*     */   public abstract JExpression getLocalPart();
/*     */   
/*     */   public abstract JType getType(JCodeModel paramJCodeModel);
/*     */   
/*     */   public abstract void onNameUnmarshalled(JCodeModel paramJCodeModel, JBlock paramJBlock, JVar paramJVar1, JVar paramJVar2);
/*     */   
/*     */   public abstract void populate(ClassContext paramClassContext);
/*     */   
/*     */   public static com.sun.tools.xjc.generator.XmlNameStoreAlgorithm get(NameClassAndExpression item) {
/*  75 */     return get(item.getNameClass());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static com.sun.tools.xjc.generator.XmlNameStoreAlgorithm get(NameClass nc) {
/*  82 */     if (nc instanceof SimpleNameClass) {
/*  83 */       return (com.sun.tools.xjc.generator.XmlNameStoreAlgorithm)new Simple((SimpleNameClass)nc, null);
/*     */     }
/*  85 */     Set namespaces = new HashSet();
/*  86 */     nc.simplify().visit((NameClassVisitor)new Object(namespaces));
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
/* 117 */     if (namespaces.size() == 1) {
/* 118 */       return (com.sun.tools.xjc.generator.XmlNameStoreAlgorithm)new UniqueNamespace(namespaces.iterator().next(), null);
/*     */     }
/* 120 */     return (com.sun.tools.xjc.generator.XmlNameStoreAlgorithm)Any.access$200();
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
/*     */   static Class class$(String x0) {
/*     */     
/* 182 */     try { return Class.forName(x0); } catch (ClassNotFoundException x1) { throw new NoClassDefFoundError(x1.getMessage()); }
/*     */   
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\XmlNameStoreAlgorithm.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */