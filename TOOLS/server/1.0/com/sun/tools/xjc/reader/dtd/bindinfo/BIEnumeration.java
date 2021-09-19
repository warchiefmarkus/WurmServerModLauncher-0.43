/*    */ package 1.0.com.sun.tools.xjc.reader.dtd.bindinfo;
/*    */ 
/*    */ import com.sun.codemodel.JClassContainer;
/*    */ import com.sun.msv.datatype.xsd.NmtokenType;
/*    */ import com.sun.msv.datatype.xsd.XSDatatype;
/*    */ import com.sun.msv.grammar.Expression;
/*    */ import com.sun.msv.grammar.ExpressionPool;
/*    */ import com.sun.tools.xjc.grammar.xducer.EnumerationXducer;
/*    */ import com.sun.tools.xjc.grammar.xducer.Transducer;
/*    */ import com.sun.tools.xjc.reader.dtd.bindinfo.BIConversion;
/*    */ import com.sun.tools.xjc.reader.dtd.bindinfo.BIElement;
/*    */ import com.sun.tools.xjc.reader.dtd.bindinfo.BindInfo;
/*    */ import java.util.HashMap;
/*    */ import java.util.StringTokenizer;
/*    */ import org.dom4j.Element;
/*    */ 
/*    */ 
/*    */ public final class BIEnumeration
/*    */   implements BIConversion
/*    */ {
/*    */   private final Element e;
/*    */   private final Transducer xducer;
/*    */   
/*    */   private BIEnumeration(Element _e, Transducer _xducer) {
/* 25 */     this.e = _e;
/* 26 */     this.xducer = _xducer;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String name() {
/* 34 */     return this.e.attributeValue("name");
/*    */   }
/*    */   public Transducer getTransducer() {
/* 37 */     return this.xducer;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static com.sun.tools.xjc.reader.dtd.bindinfo.BIEnumeration create(Element dom, BindInfo parent) {
/* 45 */     return new com.sun.tools.xjc.reader.dtd.bindinfo.BIEnumeration(dom, (Transducer)new EnumerationXducer(parent.nameConverter, parent.classFactory.createClass((JClassContainer)parent.getTargetPackage(), dom.attributeValue("name"), null), buildMemberExp(dom), emptyHashMap, null));
/*    */   }
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
/*    */   static com.sun.tools.xjc.reader.dtd.bindinfo.BIEnumeration create(Element dom, BIElement parent) {
/* 61 */     return new com.sun.tools.xjc.reader.dtd.bindinfo.BIEnumeration(dom, (Transducer)new EnumerationXducer(parent.parent.nameConverter, parent.parent.classFactory.createClass((JClassContainer)parent.getClassObject(), dom.attributeValue("name"), null), buildMemberExp(dom), emptyHashMap, null));
/*    */   }
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
/* 74 */   private static final HashMap emptyHashMap = new HashMap();
/*    */   
/*    */   private static Expression buildMemberExp(Element dom) {
/* 77 */     String members = dom.attributeValue("members");
/* 78 */     if (members == null) members = "";
/*    */     
/* 80 */     ExpressionPool pool = new ExpressionPool();
/*    */     
/* 82 */     Expression exp = Expression.nullSet;
/* 83 */     StringTokenizer tokens = new StringTokenizer(members);
/* 84 */     while (tokens.hasMoreTokens()) {
/* 85 */       String token = tokens.nextToken();
/*    */       
/* 87 */       exp = pool.createChoice(exp, pool.createValue((XSDatatype)NmtokenType.theInstance, token));
/*    */     } 
/*    */ 
/*    */     
/* 91 */     return exp;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\dtd\bindinfo\BIEnumeration.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */