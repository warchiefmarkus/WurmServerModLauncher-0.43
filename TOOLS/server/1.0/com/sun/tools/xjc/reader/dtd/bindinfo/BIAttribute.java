/*    */ package 1.0.com.sun.tools.xjc.reader.dtd.bindinfo;
/*    */ 
/*    */ import com.sun.tools.xjc.generator.field.ArrayFieldRenderer;
/*    */ import com.sun.tools.xjc.generator.field.FieldRendererFactory;
/*    */ import com.sun.tools.xjc.generator.field.UntypedListFieldRenderer;
/*    */ import com.sun.tools.xjc.reader.dtd.bindinfo.BIConversion;
/*    */ import com.sun.tools.xjc.reader.dtd.bindinfo.BIElement;
/*    */ import java.util.ArrayList;
/*    */ import org.dom4j.Element;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BIAttribute
/*    */ {
/*    */   private final BIElement parent;
/*    */   private final Element element;
/*    */   
/*    */   BIAttribute(BIElement _parent, Element _e) {
/* 24 */     this.parent = _parent;
/* 25 */     this.element = _e;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final String name() {
/* 33 */     return this.element.attributeValue("name");
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
/*    */   public BIConversion getConversion() {
/* 45 */     String cnv = this.element.attributeValue("convert");
/* 46 */     if (cnv == null) return null;
/*    */     
/* 48 */     return this.parent.conversion(cnv);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final FieldRendererFactory getRealization() {
/* 58 */     String v = this.element.attributeValue("collection");
/* 59 */     if (v == null) return null;
/*    */     
/* 61 */     v = v.trim();
/* 62 */     if (v.equals("array")) return ArrayFieldRenderer.theFactory; 
/* 63 */     if (v.equals("list")) {
/* 64 */       return (FieldRendererFactory)new UntypedListFieldRenderer.Factory(this.parent.parent.codeModel.ref(ArrayList.class));
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 69 */     throw new InternalError("unexpected collection value: " + v);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final String getPropertyName() {
/* 79 */     String r = this.element.attributeValue("property");
/* 80 */     if (r != null) return r; 
/* 81 */     return name();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\dtd\bindinfo\BIAttribute.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */