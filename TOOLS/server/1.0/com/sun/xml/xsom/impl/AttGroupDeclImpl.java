/*    */ package 1.0.com.sun.xml.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.xsom.XSAttGroupDecl;
/*    */ import com.sun.xml.xsom.XSAttributeUse;
/*    */ import com.sun.xml.xsom.XSDeclaration;
/*    */ import com.sun.xml.xsom.XSWildcard;
/*    */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*    */ import com.sun.xml.xsom.impl.AttributesHolder;
/*    */ import com.sun.xml.xsom.impl.SchemaImpl;
/*    */ import com.sun.xml.xsom.impl.UName;
/*    */ import com.sun.xml.xsom.impl.WildcardImpl;
/*    */ import com.sun.xml.xsom.impl.parser.DelayedRef;
/*    */ import com.sun.xml.xsom.visitor.XSFunction;
/*    */ import com.sun.xml.xsom.visitor.XSVisitor;
/*    */ import java.util.Iterator;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AttGroupDeclImpl
/*    */   extends AttributesHolder
/*    */   implements XSAttGroupDecl
/*    */ {
/*    */   private WildcardImpl wildcard;
/*    */   
/*    */   public AttGroupDeclImpl(SchemaImpl _parent, AnnotationImpl _annon, Locator _loc, String _name, WildcardImpl _wildcard) {
/* 28 */     this(_parent, _annon, _loc, _name);
/* 29 */     setWildcard(_wildcard);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public AttGroupDeclImpl(SchemaImpl _parent, AnnotationImpl _annon, Locator _loc, String _name) {
/* 35 */     super(_parent, _annon, _loc, _name, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setWildcard(WildcardImpl wc) {
/* 40 */     this.wildcard = wc; } public XSWildcard getAttributeWildcard() {
/* 41 */     return (XSWildcard)this.wildcard;
/*    */   }
/*    */   public XSAttributeUse getAttributeUse(String nsURI, String localName) {
/* 44 */     UName name = new UName(nsURI, localName);
/* 45 */     XSAttributeUse o = null;
/*    */     
/* 47 */     Iterator itr = iterateAttGroups();
/* 48 */     while (itr.hasNext() && o == null) {
/* 49 */       o = ((XSAttGroupDecl)itr.next()).getAttributeUse(nsURI, localName);
/*    */     }
/* 51 */     if (o == null) o = (XSAttributeUse)this.attributes.get(name);
/*    */     
/* 53 */     return o;
/*    */   }
/*    */   
/*    */   public void redefine(com.sun.xml.xsom.impl.AttGroupDeclImpl ag) {
/* 57 */     for (Iterator itr = this.attGroups.iterator(); itr.hasNext(); ) {
/* 58 */       DelayedRef.AttGroup r = itr.next();
/* 59 */       r.redefine((XSDeclaration)ag);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void visit(XSVisitor visitor) {
/* 64 */     visitor.attGroupDecl(this);
/*    */   }
/*    */   public Object apply(XSFunction function) {
/* 67 */     return function.attGroupDecl(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\AttGroupDeclImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */