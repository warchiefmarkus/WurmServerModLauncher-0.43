/*     */ package 1.0.com.sun.tools.xjc.reader.dtd.bindinfo;
/*     */ 
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JPrimitiveType;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.tools.xjc.generator.util.WhitespaceNormalizer;
/*     */ import com.sun.tools.xjc.grammar.xducer.IdentityTransducer;
/*     */ import com.sun.tools.xjc.grammar.xducer.Transducer;
/*     */ import com.sun.tools.xjc.grammar.xducer.UserTransducer;
/*     */ import com.sun.tools.xjc.grammar.xducer.WhitespaceTransducer;
/*     */ import com.sun.tools.xjc.reader.dtd.bindinfo.BIConversion;
/*     */ import com.sun.tools.xjc.reader.dtd.bindinfo.BindInfo;
/*     */ import com.sun.tools.xjc.reader.dtd.bindinfo.DOM4JLocator;
/*     */ import java.util.Map;
/*     */ import org.dom4j.DocumentFactory;
/*     */ import org.dom4j.Element;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BIUserConversion
/*     */   implements BIConversion
/*     */ {
/*     */   private final BindInfo owner;
/*     */   private final Element e;
/*     */   
/*     */   BIUserConversion(BindInfo bi, Element _e) {
/*  31 */     this.owner = bi;
/*  32 */     this.e = _e;
/*     */   }
/*     */   
/*     */   private static void add(Map m, BIConversion c) {
/*  36 */     m.put(c.name(), c);
/*     */   }
/*     */ 
/*     */   
/*     */   static void addBuiltinConversions(BindInfo bi, Map m) {
/*  41 */     DocumentFactory f = DocumentFactory.getInstance();
/*     */     
/*  43 */     add(m, new com.sun.tools.xjc.reader.dtd.bindinfo.BIUserConversion(bi, f.createElement("conversion").addAttribute("name", "boolean").addAttribute("type", "java.lang.Boolean").addAttribute("parse", "getBoolean")));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  49 */     add(m, new com.sun.tools.xjc.reader.dtd.bindinfo.BIUserConversion(bi, f.createElement("conversion").addAttribute("name", "byte").addAttribute("type", "java.lang.Byte").addAttribute("parse", "parseByte")));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  55 */     add(m, new com.sun.tools.xjc.reader.dtd.bindinfo.BIUserConversion(bi, f.createElement("conversion").addAttribute("name", "short").addAttribute("type", "java.lang.Short").addAttribute("parse", "parseShort")));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  61 */     add(m, new com.sun.tools.xjc.reader.dtd.bindinfo.BIUserConversion(bi, f.createElement("conversion").addAttribute("name", "int").addAttribute("type", "java.lang.Integer").addAttribute("parse", "parseInt")));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  67 */     add(m, new com.sun.tools.xjc.reader.dtd.bindinfo.BIUserConversion(bi, f.createElement("conversion").addAttribute("name", "long").addAttribute("type", "java.lang.Long").addAttribute("parse", "parseLong")));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  73 */     add(m, new com.sun.tools.xjc.reader.dtd.bindinfo.BIUserConversion(bi, f.createElement("conversion").addAttribute("name", "float").addAttribute("type", "java.lang.Float").addAttribute("parse", "parseFloat")));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  79 */     add(m, new com.sun.tools.xjc.reader.dtd.bindinfo.BIUserConversion(bi, f.createElement("conversion").addAttribute("name", "double").addAttribute("type", "java.lang.Double").addAttribute("parse", "parseDouble")));
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
/*     */   public Locator getSourceLocation() {
/*  97 */     return DOM4JLocator.getLocationInfo(this.e);
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
/*     */   private String attValue(String name, String defaultValue) {
/* 109 */     String r = this.e.attributeValue(name);
/* 110 */     if (r == null) return defaultValue; 
/* 111 */     return r;
/*     */   }
/*     */   
/*     */   public String name() {
/* 115 */     return this.e.attributeValue("name");
/*     */   }
/*     */   
/*     */   public Transducer getTransducer() {
/*     */     JClass jClass;
/* 120 */     String ws = this.e.attributeValue("whitespace");
/* 121 */     if (ws == null) ws = "collapse";
/*     */     
/* 123 */     String type = this.e.attributeValue("type");
/*     */     
/* 125 */     if (type == null) {
/*     */       
/* 127 */       jClass = this.owner.getTargetPackage().ref(name());
/*     */     } else {
/* 129 */       int idx = type.lastIndexOf('.');
/* 130 */       if (idx < 0) {
/*     */         
/*     */         try {
/* 133 */           JPrimitiveType jPrimitiveType = JType.parse(this.owner.codeModel, type);
/* 134 */         } catch (IllegalArgumentException e) {
/*     */           
/* 136 */           jClass = this.owner.getTargetPackage().ref(type);
/*     */         } 
/*     */       } else {
/*     */         try {
/* 140 */           jClass = this.owner.codeModel.ref(type);
/* 141 */         } catch (ClassNotFoundException e) {
/*     */           
/* 143 */           throw new NoClassDefFoundError(e.getMessage());
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*     */     try {
/* 149 */       return WhitespaceTransducer.create((Transducer)new UserTransducer((JType)jClass, attValue("parse", "new"), attValue("print", "toString"), false), this.owner.codeModel, WhitespaceNormalizer.parse(ws));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 157 */     catch (IllegalArgumentException e) {
/*     */       
/* 159 */       this.owner.errorReceiver.error(new SAXParseException(e.getMessage(), getSourceLocation(), e));
/*     */ 
/*     */       
/* 162 */       return (Transducer)new IdentityTransducer(this.owner.codeModel);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\dtd\bindinfo\BIUserConversion.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */