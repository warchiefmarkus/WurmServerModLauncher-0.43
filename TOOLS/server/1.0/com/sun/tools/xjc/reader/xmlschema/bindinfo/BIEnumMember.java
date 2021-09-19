/*    */ package 1.0.com.sun.tools.xjc.reader.xmlschema.bindinfo;
/*    */ 
/*    */ import com.sun.tools.xjc.grammar.xducer.EnumerationXducer;
/*    */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.AbstractDeclarationImpl;
/*    */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIGlobalBinding;
/*    */ import javax.xml.namespace.QName;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BIEnumMember
/*    */   extends AbstractDeclarationImpl
/*    */ {
/*    */   private final String memberName;
/*    */   private final String javadoc;
/*    */   
/*    */   public BIEnumMember(Locator loc, String _memberName, String _javadoc) {
/* 23 */     super(loc);
/* 24 */     this.memberName = _memberName;
/* 25 */     this.javadoc = _javadoc;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMemberName() {
/* 31 */     if (this.memberName == null) return null;
/*    */     
/* 33 */     BIGlobalBinding gb = getBuilder().getGlobalBinding();
/*    */     
/* 35 */     if (gb.isJavaNamingConventionEnabled())
/*    */     {
/* 37 */       return gb.getNameConverter().toConstantName(this.memberName);
/*    */     }
/* 39 */     return this.memberName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getJavadoc() {
/* 47 */     return this.javadoc;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public EnumerationXducer.MemberInfo createMemberInfo() {
/* 53 */     return new EnumerationXducer.MemberInfo(getMemberName(), this.javadoc);
/*    */   }
/*    */   public QName getName() {
/* 56 */     return NAME;
/*    */   }
/*    */   
/* 59 */   public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "typesafeEnumMember");
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\bindinfo\BIEnumMember.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */