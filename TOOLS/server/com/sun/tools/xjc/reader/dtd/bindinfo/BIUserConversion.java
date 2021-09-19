/*     */ package com.sun.tools.xjc.reader.dtd.bindinfo;
/*     */ 
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JClassAlreadyExistsException;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JDefinedClass;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JInvocation;
/*     */ import com.sun.codemodel.JMethod;
/*     */ import com.sun.codemodel.JPackage;
/*     */ import com.sun.codemodel.JPrimitiveType;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.tools.xjc.model.CAdapter;
/*     */ import com.sun.tools.xjc.model.CBuiltinLeafInfo;
/*     */ import com.sun.tools.xjc.model.TypeUse;
/*     */ import com.sun.tools.xjc.model.TypeUseFactory;
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import java.util.Map;
/*     */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.w3c.dom.Element;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
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
/*     */ 
/*     */ 
/*     */ 
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
/*  78 */     this.owner = bi;
/*  79 */     this.e = _e;
/*     */   }
/*     */   
/*     */   private static void add(Map<String, BIConversion> m, BIConversion c) {
/*  83 */     m.put(c.name(), c);
/*     */   }
/*     */ 
/*     */   
/*     */   static void addBuiltinConversions(BindInfo bi, Map<String, BIConversion> m) {
/*  88 */     add(m, new BIUserConversion(bi, parse("<conversion name='boolean' type='java.lang.Boolean' parse='getBoolean' />")));
/*  89 */     add(m, new BIUserConversion(bi, parse("<conversion name='byte' type='java.lang.Byte' parse='parseByte' />")));
/*  90 */     add(m, new BIUserConversion(bi, parse("<conversion name='short' type='java.lang.Short' parse='parseShort' />")));
/*  91 */     add(m, new BIUserConversion(bi, parse("<conversion name='int' type='java.lang.Integer' parse='parseInt' />")));
/*  92 */     add(m, new BIUserConversion(bi, parse("<conversion name='long' type='java.lang.Long' parse='parseLong' />")));
/*  93 */     add(m, new BIUserConversion(bi, parse("<conversion name='float' type='java.lang.Float' parse='parseFloat' />")));
/*  94 */     add(m, new BIUserConversion(bi, parse("<conversion name='double' type='java.lang.Double' parse='parseDouble' />")));
/*     */   }
/*     */   
/*     */   private static Element parse(String text) {
/*     */     try {
/*  99 */       DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
/* 100 */       dbf.setNamespaceAware(true);
/* 101 */       InputSource is = new InputSource(new StringReader(text));
/* 102 */       return dbf.newDocumentBuilder().parse(is).getDocumentElement();
/* 103 */     } catch (SAXException x) {
/* 104 */       throw new Error(x);
/* 105 */     } catch (IOException x) {
/* 106 */       throw new Error(x);
/* 107 */     } catch (ParserConfigurationException x) {
/* 108 */       throw new Error(x);
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
/*     */   
/*     */   public Locator getSourceLocation() {
/* 123 */     return DOMLocator.getLocationInfo(this.e);
/*     */   }
/*     */   
/*     */   public String name() {
/* 127 */     return DOMUtil.getAttribute(this.e, "name");
/*     */   }
/*     */   public TypeUse getTransducer() {
/*     */     JPrimitiveType jPrimitiveType;
/*     */     JDefinedClass jDefinedClass1;
/* 132 */     String ws = DOMUtil.getAttribute(this.e, "whitespace");
/* 133 */     if (ws == null) ws = "collapse";
/*     */     
/* 135 */     String type = DOMUtil.getAttribute(this.e, "type");
/* 136 */     if (type == null) type = name(); 
/* 137 */     JType t = null;
/*     */     
/* 139 */     int idx = type.lastIndexOf('.');
/* 140 */     if (idx < 0) {
/*     */       
/*     */       try {
/* 143 */         jPrimitiveType = JPrimitiveType.parse(this.owner.codeModel, type);
/* 144 */       } catch (IllegalArgumentException e) {
/*     */         
/* 146 */         type = this.owner.getTargetPackage().name() + '.' + type;
/*     */       } 
/*     */     }
/* 149 */     if (jPrimitiveType == null) {
/*     */       
/*     */       try {
/* 152 */         JDefinedClass cls = this.owner.codeModel._class(type);
/* 153 */         cls.hide();
/* 154 */         jDefinedClass1 = cls;
/* 155 */       } catch (JClassAlreadyExistsException e) {
/* 156 */         jDefinedClass1 = e.getExistingClass();
/*     */       } 
/*     */     }
/*     */     
/* 160 */     String parse = DOMUtil.getAttribute(this.e, "parse");
/* 161 */     if (parse == null) parse = "new";
/*     */     
/* 163 */     String print = DOMUtil.getAttribute(this.e, "print");
/* 164 */     if (print == null) print = "toString";
/*     */     
/* 166 */     JDefinedClass adapter = generateAdapter(this.owner.codeModel, parse, print, jDefinedClass1.boxify());
/*     */ 
/*     */     
/* 169 */     return TypeUseFactory.adapt((TypeUse)CBuiltinLeafInfo.STRING, new CAdapter((JClass)adapter));
/*     */   }
/*     */   
/*     */   private JDefinedClass generateAdapter(JCodeModel cm, String parseMethod, String printMethod, JClass inMemoryType) {
/*     */     JExpression inv;
/* 174 */     JDefinedClass adapter = null;
/*     */     
/* 176 */     int id = 1;
/* 177 */     while (adapter == null) {
/*     */       try {
/* 179 */         JPackage pkg = this.owner.getTargetPackage();
/* 180 */         adapter = pkg._class("Adapter" + id);
/* 181 */       } catch (JClassAlreadyExistsException e) {
/*     */ 
/*     */ 
/*     */         
/* 185 */         id++;
/*     */       } 
/*     */     } 
/*     */     
/* 189 */     adapter._extends(cm.ref(XmlAdapter.class).narrow(String.class).narrow(inMemoryType));
/*     */     
/* 191 */     JMethod unmarshal = adapter.method(1, (JType)inMemoryType, "unmarshal");
/* 192 */     JVar $value = unmarshal.param(String.class, "value");
/*     */ 
/*     */ 
/*     */     
/* 196 */     if (parseMethod.equals("new")) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 201 */       JInvocation jInvocation = JExpr._new(inMemoryType).arg((JExpression)$value);
/*     */     } else {
/* 203 */       int i = parseMethod.lastIndexOf('.');
/* 204 */       if (i < 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 210 */         JInvocation jInvocation = inMemoryType.staticInvoke(parseMethod).arg((JExpression)$value);
/*     */       } else {
/* 212 */         inv = JExpr.direct(parseMethod + "(value)");
/*     */       } 
/*     */     } 
/* 215 */     unmarshal.body()._return(inv);
/*     */ 
/*     */     
/* 218 */     JMethod marshal = adapter.method(1, String.class, "marshal");
/* 219 */     $value = marshal.param((JType)inMemoryType, "value");
/*     */     
/* 221 */     int idx = printMethod.lastIndexOf('.');
/* 222 */     if (idx < 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 227 */       JInvocation jInvocation = $value.invoke(printMethod);
/*     */     } else {
/*     */       
/* 230 */       inv = JExpr.direct(printMethod + "(value)");
/*     */     } 
/* 232 */     marshal.body()._return(inv);
/*     */     
/* 234 */     return adapter;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\dtd\bindinfo\BIUserConversion.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */