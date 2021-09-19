/*    */ package com.sun.tools.xjc.reader.xmlschema.bindinfo;
/*    */ 
/*    */ import javax.xml.bind.annotation.XmlAttribute;
/*    */ import javax.xml.bind.annotation.XmlRootElement;
/*    */ import javax.xml.namespace.QName;
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
/*    */ @XmlRootElement(name = "dom")
/*    */ public class BIDom
/*    */   extends AbstractDeclarationImpl
/*    */ {
/*    */   @XmlAttribute
/*    */   String type;
/*    */   
/*    */   public final QName getName() {
/* 57 */     return NAME;
/*    */   }
/*    */   
/* 60 */   public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "dom");
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\bindinfo\BIDom.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */