/*    */ package 1.0.com.sun.xml.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.xsom.XSAttGroupDecl;
/*    */ import com.sun.xml.xsom.XSAttributeUse;
/*    */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*    */ import com.sun.xml.xsom.impl.AttributeUseImpl;
/*    */ import com.sun.xml.xsom.impl.DeclarationImpl;
/*    */ import com.sun.xml.xsom.impl.Ref;
/*    */ import com.sun.xml.xsom.impl.SchemaImpl;
/*    */ import com.sun.xml.xsom.impl.UName;
/*    */ import com.sun.xml.xsom.impl.WildcardImpl;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.TreeMap;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AttributesHolder
/*    */   extends DeclarationImpl
/*    */ {
/*    */   protected final Map attributes;
/*    */   protected final Set prohibitedAtts;
/*    */   
/*    */   protected AttributesHolder(SchemaImpl _parent, AnnotationImpl _annon, Locator loc, String _name, boolean _anonymous) {
/* 30 */     super(_parent, _annon, loc, _parent.getTargetNamespace(), _name, _anonymous);
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
/* 41 */     this.attributes = new TreeMap(UName.comparator);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 46 */     this.prohibitedAtts = new HashSet();
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
/* 73 */     this.attGroups = new HashSet();
/*    */   } protected final Set attGroups; public abstract void setWildcard(WildcardImpl paramWildcardImpl); public void addAttributeUse(UName name, AttributeUseImpl a) { this.attributes.put(name, a); } public void addAttGroup(Ref.AttGroup a) {
/* 75 */     this.attGroups.add(a);
/*    */   } public void addProhibitedAttribute(UName name) {
/*    */     this.prohibitedAtts.add(name);
/*    */   }
/*    */   public Iterator iterateAttGroups() {
/* 80 */     return (Iterator)new Object(this);
/*    */   }
/*    */   
/*    */   public Iterator iterateAttributeUses() {
/*    */     List v = new ArrayList();
/*    */     v.addAll(this.attributes.values());
/*    */     Iterator itr = iterateAttGroups();
/*    */     while (itr.hasNext()) {
/*    */       Iterator jtr = ((XSAttGroupDecl)itr.next()).iterateAttributeUses();
/*    */       while (jtr.hasNext())
/*    */         v.add(jtr.next()); 
/*    */     } 
/*    */     return v.iterator();
/*    */   }
/*    */   
/*    */   public XSAttributeUse getDeclaredAttributeUse(String nsURI, String localName) {
/*    */     return (XSAttributeUse)this.attributes.get(new UName(nsURI, localName));
/*    */   }
/*    */   
/*    */   public Iterator iterateDeclaredAttributeUses() {
/*    */     return this.attributes.values().iterator();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\AttributesHolder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */