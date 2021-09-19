/*    */ package com.sun.tools.xjc.reader.xmlschema.bindinfo;
/*    */ 
/*    */ import javax.xml.bind.annotation.XmlAttribute;
/*    */ import javax.xml.bind.annotation.XmlElement;
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
/*    */ @XmlRootElement(name = "typesafeEnumMember")
/*    */ public class BIEnumMember
/*    */   extends AbstractDeclarationImpl
/*    */ {
/*    */   @XmlAttribute
/* 54 */   public final String name = null; @XmlElement
/* 55 */   public final String javadoc = null;
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
/*    */   public QName getName() {
/* 71 */     return NAME;
/*    */   }
/*    */   
/* 74 */   public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "typesafeEnumMember");
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\bindinfo\BIEnumMember.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */