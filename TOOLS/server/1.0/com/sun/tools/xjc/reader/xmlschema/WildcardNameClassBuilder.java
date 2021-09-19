/*    */ package 1.0.com.sun.tools.xjc.reader.xmlschema;
/*    */ 
/*    */ import com.sun.msv.grammar.ChoiceNameClass;
/*    */ import com.sun.msv.grammar.DifferenceNameClass;
/*    */ import com.sun.msv.grammar.NameClass;
/*    */ import com.sun.msv.grammar.NamespaceNameClass;
/*    */ import com.sun.xml.bind.JAXBAssertionError;
/*    */ import com.sun.xml.xsom.XSWildcard;
/*    */ import com.sun.xml.xsom.visitor.XSWildcardFunction;
/*    */ import java.util.Iterator;
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
/*    */ public final class WildcardNameClassBuilder
/*    */   implements XSWildcardFunction
/*    */ {
/* 29 */   private static final XSWildcardFunction theInstance = new com.sun.tools.xjc.reader.xmlschema.WildcardNameClassBuilder();
/*    */ 
/*    */   
/*    */   public static NameClass build(XSWildcard wc) {
/* 33 */     return (NameClass)wc.apply(theInstance);
/*    */   }
/*    */   
/*    */   public Object any(XSWildcard.Any wc) {
/* 37 */     return NameClass.ALL;
/*    */   }
/*    */   
/*    */   public Object other(XSWildcard.Other wc) {
/* 41 */     return new DifferenceNameClass(NameClass.ALL, (NameClass)new ChoiceNameClass((NameClass)new NamespaceNameClass(""), (NameClass)new NamespaceNameClass(wc.getOtherNamespace())));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object union(XSWildcard.Union wc) {
/*    */     ChoiceNameClass choiceNameClass;
/* 49 */     NameClass nc = null;
/* 50 */     for (Iterator itr = wc.iterateNamespaces(); itr.hasNext(); ) {
/* 51 */       NamespaceNameClass namespaceNameClass; String ns = itr.next();
/*    */       
/* 53 */       if (nc == null) { namespaceNameClass = new NamespaceNameClass(ns); continue; }
/*    */       
/* 55 */       choiceNameClass = new ChoiceNameClass((NameClass)namespaceNameClass, (NameClass)new NamespaceNameClass(ns));
/*    */     } 
/*    */     
/* 58 */     if (choiceNameClass == null)
/*    */     {
/* 60 */       throw new JAXBAssertionError();
/*    */     }
/* 62 */     return choiceNameClass;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\WildcardNameClassBuilder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */