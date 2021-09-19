/*     */ package 1.0.com.sun.tools.xjc.reader.dtd.bindinfo;
/*     */ 
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.tools.xjc.generator.field.ArrayFieldRenderer;
/*     */ import com.sun.tools.xjc.generator.field.FieldRendererFactory;
/*     */ import com.sun.tools.xjc.generator.field.UntypedListFieldRenderer;
/*     */ import com.sun.tools.xjc.grammar.FieldItem;
/*     */ import com.sun.tools.xjc.reader.dtd.bindinfo.BIElement;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ import java.util.ArrayList;
/*     */ import org.dom4j.Element;
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
/*     */ public abstract class BIContent
/*     */ {
/*     */   protected final Element element;
/*     */   protected final BIElement parent;
/*     */   
/*     */   private BIContent(Element e, BIElement _parent) {
/*  36 */     this.element = e;
/*  37 */     this.parent = _parent;
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
/*     */   public final FieldRendererFactory getRealization() {
/*  53 */     String v = this.element.attributeValue("collection");
/*  54 */     if (v == null) return null;
/*     */     
/*  56 */     v = v.trim();
/*  57 */     if (v.equals("array")) return ArrayFieldRenderer.theFactory; 
/*  58 */     if (v.equals("list")) {
/*  59 */       return (FieldRendererFactory)new UntypedListFieldRenderer.Factory(this.parent.parent.codeModel.ref(ArrayList.class));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  64 */     throw new InternalError("unexpected collection value: " + v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getPropertyName() {
/*  74 */     String r = this.element.attributeValue("property");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  79 */     if (r != null) return r; 
/*  80 */     return this.element.attributeValue("name");
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
/*     */   public final JClass getType() {
/*     */     try {
/*  93 */       String type = this.element.attributeValue("supertype");
/*  94 */       if (type == null) return null;
/*     */ 
/*     */       
/*  97 */       int idx = type.lastIndexOf('.');
/*  98 */       if (idx < 0) return this.parent.parent.codeModel.ref(type); 
/*  99 */       return this.parent.parent.getTargetPackage().ref(type);
/* 100 */     } catch (ClassNotFoundException e) {
/*     */       
/* 102 */       throw new NoClassDefFoundError(e.getMessage());
/*     */     } 
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
/*     */   public final Expression wrap(Expression exp) throws MismatchException {
/* 116 */     if (!checkMatch(exp.peelOccurence())) {
/* 117 */       throw new MismatchException();
/*     */     }
/*     */     
/* 120 */     FieldItem fi = new FieldItem(getPropertyName(), exp, (JType)getType(), null);
/*     */     
/* 122 */     fi.realization = getRealization();
/*     */     
/* 124 */     return (Expression)fi;
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
/*     */   static com.sun.tools.xjc.reader.dtd.bindinfo.BIContent create(Element e, BIElement _parent) {
/* 144 */     String tagName = e.getName();
/*     */     
/* 146 */     if (tagName.equals("element-ref")) {
/* 147 */       return (com.sun.tools.xjc.reader.dtd.bindinfo.BIContent)new Object(e, _parent);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 155 */     if (tagName.equals("choice")) {
/* 156 */       return (com.sun.tools.xjc.reader.dtd.bindinfo.BIContent)new Object(e, _parent);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 162 */     if (tagName.equals("sequence")) {
/* 163 */       return (com.sun.tools.xjc.reader.dtd.bindinfo.BIContent)new Object(e, _parent);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 169 */     if (tagName.equals("rest") || tagName.equals("content"))
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 174 */       return (com.sun.tools.xjc.reader.dtd.bindinfo.BIContent)new Object(e, _parent);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 185 */     throw new JAXBAssertionError();
/*     */   }
/*     */   
/*     */   protected abstract boolean checkMatch(Expression paramExpression);
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\dtd\bindinfo\BIContent.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */