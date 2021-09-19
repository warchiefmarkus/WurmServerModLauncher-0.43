/*    */ package 1.0.com.sun.xml.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.xsom.XSFacet;
/*    */ import com.sun.xml.xsom.XSRestrictionSimpleType;
/*    */ import com.sun.xml.xsom.XSVariety;
/*    */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*    */ import com.sun.xml.xsom.impl.Ref;
/*    */ import com.sun.xml.xsom.impl.SchemaImpl;
/*    */ import com.sun.xml.xsom.impl.SimpleTypeImpl;
/*    */ import com.sun.xml.xsom.visitor.XSSimpleTypeFunction;
/*    */ import com.sun.xml.xsom.visitor.XSSimpleTypeVisitor;
/*    */ import java.util.Iterator;
/*    */ import java.util.Vector;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RestrictionSimpleTypeImpl
/*    */   extends SimpleTypeImpl
/*    */   implements XSRestrictionSimpleType
/*    */ {
/*    */   private final Vector facets;
/*    */   
/*    */   public RestrictionSimpleTypeImpl(SchemaImpl _parent, AnnotationImpl _annon, Locator _loc, String _name, boolean _anonymous, Ref.SimpleType _baseType) {
/* 30 */     super(_parent, _annon, _loc, _name, _anonymous, _baseType);
/*    */ 
/*    */ 
/*    */     
/* 34 */     this.facets = new Vector();
/*    */   } public void addFacet(XSFacet facet) {
/* 36 */     this.facets.add(facet);
/*    */   }
/*    */   public Iterator iterateDeclaredFacets() {
/* 39 */     return this.facets.iterator();
/*    */   }
/*    */   
/*    */   public XSFacet getDeclaredFacet(String name) {
/* 43 */     int len = this.facets.size();
/* 44 */     for (int i = 0; i < len; i++) {
/* 45 */       XSFacet f = this.facets.get(i);
/* 46 */       if (f.getName().equals(name))
/* 47 */         return f; 
/*    */     } 
/* 49 */     return null;
/*    */   }
/*    */   
/*    */   public XSFacet getFacet(String name) {
/* 53 */     XSFacet f = getDeclaredFacet(name);
/* 54 */     if (f != null) return f;
/*    */ 
/*    */     
/* 57 */     return getSimpleBaseType().getFacet(name);
/*    */   }
/*    */   public XSVariety getVariety() {
/* 60 */     return getSimpleBaseType().getVariety();
/*    */   }
/*    */   public void visit(XSSimpleTypeVisitor visitor) {
/* 63 */     visitor.restrictionSimpleType(this);
/*    */   }
/*    */   public Object apply(XSSimpleTypeFunction function) {
/* 66 */     return function.restrictionSimpleType(this);
/*    */   }
/*    */   
/* 69 */   public boolean isRestriction() { return true; } public XSRestrictionSimpleType asRestriction() {
/* 70 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\RestrictionSimpleTypeImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */