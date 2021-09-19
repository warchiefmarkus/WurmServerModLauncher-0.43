/*    */ package 1.0.com.sun.tools.xjc.reader.xmlschema.bindinfo;
/*    */ 
/*    */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.AbstractDeclarationImpl;
/*    */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIEnumMember;
/*    */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BindInfo;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class BIEnum
/*    */   extends AbstractDeclarationImpl
/*    */ {
/*    */   private final String className;
/*    */   private final String javadoc;
/*    */   private final HashMap members;
/*    */   
/*    */   public BIEnum(Locator loc, String _className, String _javadoc, HashMap _members) {
/* 32 */     super(loc);
/* 33 */     this.className = _className;
/* 34 */     this.javadoc = _javadoc;
/* 35 */     this.members = _members;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getClassName() {
/* 40 */     return this.className;
/*    */   }
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
/*    */ 
/*    */ 
/*    */   
/*    */   public HashMap getMembers() {
/* 56 */     return this.members;
/*    */   } public QName getName() {
/* 58 */     return NAME;
/*    */   }
/*    */   public void setParent(BindInfo p) {
/* 61 */     super.setParent(p);
/*    */     
/* 63 */     Iterator itr = this.members.entrySet().iterator();
/* 64 */     while (itr.hasNext()) {
/* 65 */       BIEnumMember mem = (BIEnumMember)((Map.Entry)itr.next()).getValue();
/* 66 */       mem.setParent(p);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/* 71 */   public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "enum");
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\bindinfo\BIEnum.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */