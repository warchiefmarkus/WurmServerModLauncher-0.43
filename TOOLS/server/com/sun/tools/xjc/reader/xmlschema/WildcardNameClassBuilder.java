/*    */ package com.sun.tools.xjc.reader.xmlschema;
/*    */ 
/*    */ import com.sun.xml.xsom.XSWildcard;
/*    */ import com.sun.xml.xsom.visitor.XSWildcardFunction;
/*    */ import java.util.Iterator;
/*    */ import org.kohsuke.rngom.nc.AnyNameExceptNameClass;
/*    */ import org.kohsuke.rngom.nc.ChoiceNameClass;
/*    */ import org.kohsuke.rngom.nc.NameClass;
/*    */ import org.kohsuke.rngom.nc.NsNameClass;
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
/*    */   implements XSWildcardFunction<NameClass>
/*    */ {
/* 60 */   private static final XSWildcardFunction<NameClass> theInstance = new WildcardNameClassBuilder();
/*    */ 
/*    */   
/*    */   public static NameClass build(XSWildcard wc) {
/* 64 */     return (NameClass)wc.apply(theInstance);
/*    */   }
/*    */   
/*    */   public NameClass any(XSWildcard.Any wc) {
/* 68 */     return NameClass.ANY;
/*    */   }
/*    */   
/*    */   public NameClass other(XSWildcard.Other wc) {
/* 72 */     return (NameClass)new AnyNameExceptNameClass((NameClass)new ChoiceNameClass((NameClass)new NsNameClass(""), (NameClass)new NsNameClass(wc.getOtherNamespace())));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public NameClass union(XSWildcard.Union wc) {
/*    */     ChoiceNameClass choiceNameClass;
/* 79 */     NameClass nc = null;
/* 80 */     for (Iterator<String> itr = wc.iterateNamespaces(); itr.hasNext(); ) {
/* 81 */       NsNameClass nsNameClass; String ns = itr.next();
/*    */       
/* 83 */       if (nc == null) { nsNameClass = new NsNameClass(ns); continue; }
/*    */       
/* 85 */       choiceNameClass = new ChoiceNameClass((NameClass)nsNameClass, (NameClass)new NsNameClass(ns));
/*    */     } 
/*    */ 
/*    */     
/* 89 */     assert choiceNameClass != null;
/*    */     
/* 91 */     return (NameClass)choiceNameClass;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\WildcardNameClassBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */