/*     */ package 1.0.com.sun.tools.xjc.reader.relaxng;
/*     */ 
/*     */ import com.sun.tools.xjc.Options;
/*     */ import com.sun.tools.xjc.reader.internalizer.DOMForest;
/*     */ import com.sun.tools.xjc.util.DOMUtils;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
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
/*     */ public class CustomizationConverter
/*     */ {
/*     */   private final Options options;
/*     */   
/*     */   public CustomizationConverter(Options opts) {
/*  37 */     this.options = opts;
/*     */   }
/*     */   
/*     */   public void fixup(DOMForest forest) {
/*  41 */     Document[] docs = forest.listDocuments();
/*  42 */     for (int i = 0; i < docs.length; i++)
/*  43 */       fixup(docs[i].getDocumentElement()); 
/*     */   }
/*     */   
/*     */   private void fixup(Element e) {
/*  47 */     if (!e.getNamespaceURI().equals("http://relaxng.org/ns/structure/1.0")) {
/*     */       return;
/*     */     }
/*     */     
/*  51 */     NodeList nl = e.getChildNodes();
/*  52 */     for (int i = 0; i < nl.getLength(); i++) {
/*  53 */       Node c = nl.item(i);
/*  54 */       if (c instanceof Element) {
/*  55 */         Element child = (Element)c;
/*  56 */         String childNS = child.getNamespaceURI();
/*     */         
/*  58 */         if (childNS.equals("http://relaxng.org/ns/structure/1.0")) {
/*  59 */           fixup(child);
/*     */         }
/*  61 */         else if (childNS.equals("http://java.sun.com/xml/ns/jaxb") || childNS.equals("http://java.sun.com/xml/ns/jaxb/xjc")) {
/*     */           
/*  63 */           fixup(e, child);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void fixup(Element parent, Element child) {
/*  76 */     String name = child.getLocalName().intern();
/*     */     
/*  78 */     if (name == "schemaBindings") {
/*  79 */       Element p = DOMUtils.getFirstChildElement(child, "http://java.sun.com/xml/ns/jaxb", "package");
/*  80 */       if (p == null)
/*     */         return; 
/*  82 */       parent.setAttributeNS("http://java.sun.com/xml/ns/jaxb", "package", p.getAttribute("name"));
/*     */       
/*     */       return;
/*     */     } 
/*  86 */     if (name == "class") {
/*  87 */       parent.setAttributeNS("http://java.sun.com/xml/ns/jaxb", "role", "class");
/*  88 */       copyAttribute(parent, child, "name");
/*     */       
/*     */       return;
/*     */     } 
/*  92 */     if (name == "property") {
/*  93 */       parent.setAttributeNS("http://java.sun.com/xml/ns/jaxb", "role", "field");
/*  94 */       copyAttribute(parent, child, "name");
/*     */       
/*     */       return;
/*     */     } 
/*  98 */     if (name == "javaType") {
/*  99 */       parent.setAttributeNS("http://java.sun.com/xml/ns/jaxb", "role", "primitive");
/* 100 */       copyAttribute(parent, child, "name");
/* 101 */       copyAttribute(parent, child, "parseMethod");
/* 102 */       copyAttribute(parent, child, "printMethod");
/* 103 */       copyAttribute(parent, child, "hasNsContext");
/*     */       
/*     */       return;
/*     */     } 
/* 107 */     if (name == "interface") {
/* 108 */       parent.setAttributeNS("http://java.sun.com/xml/ns/jaxb", "role", "interface");
/* 109 */       copyAttribute(parent, child, "name");
/*     */       
/*     */       return;
/*     */     } 
/* 113 */     if (name == "ignore") {
/* 114 */       parent.setAttributeNS("http://java.sun.com/xml/ns/jaxb", "role", "ignore");
/*     */       
/*     */       return;
/*     */     } 
/* 118 */     if (name == "super") {
/* 119 */       parent.setAttributeNS("http://java.sun.com/xml/ns/jaxb", "role", "superClass");
/*     */       
/*     */       return;
/*     */     } 
/* 123 */     if (name == "dom") {
/* 124 */       parent.setAttributeNS("http://java.sun.com/xml/ns/jaxb", "role", "dom");
/* 125 */       copyAttribute(parent, child, "type");
/*     */       
/*     */       return;
/*     */     } 
/* 129 */     if (name == "noMarshaller") {
/* 130 */       this.options.generateMarshallingCode = false;
/*     */       
/*     */       return;
/*     */     } 
/* 134 */     if (name == "noUnmarshaller") {
/* 135 */       this.options.generateUnmarshallingCode = false;
/* 136 */       this.options.generateValidatingUnmarshallingCode = false;
/*     */       
/*     */       return;
/*     */     } 
/* 140 */     if (name == "noValidator") {
/* 141 */       this.options.generateValidationCode = false;
/*     */       
/*     */       return;
/*     */     } 
/* 145 */     if (name == "noValidatingUnmarshaller") {
/* 146 */       this.options.generateValidatingUnmarshallingCode = false;
/*     */       return;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void copyAttribute(Element dst, Element src, String attName) {
/* 152 */     if (src.getAttributeNode(attName) != null)
/* 153 */       dst.setAttributeNS("http://java.sun.com/xml/ns/jaxb", attName, src.getAttribute(attName)); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\relaxng\CustomizationConverter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */