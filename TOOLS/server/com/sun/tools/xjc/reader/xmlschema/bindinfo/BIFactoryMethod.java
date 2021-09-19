/*    */ package com.sun.tools.xjc.reader.xmlschema.bindinfo;
/*    */ 
/*    */ import com.sun.tools.xjc.model.CPropertyInfo;
/*    */ import com.sun.tools.xjc.reader.Ring;
/*    */ import com.sun.tools.xjc.reader.xmlschema.BGMBuilder;
/*    */ import com.sun.xml.xsom.XSComponent;
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
/*    */ @XmlRootElement(name = "factoryMethod")
/*    */ public class BIFactoryMethod
/*    */   extends AbstractDeclarationImpl
/*    */ {
/*    */   @XmlAttribute
/*    */   public String name;
/*    */   
/*    */   public static void handle(XSComponent source, CPropertyInfo prop) {
/* 28 */     BIInlineBinaryData inline = ((BGMBuilder)Ring.get(BGMBuilder.class)).getBindInfo(source).<BIInlineBinaryData>get(BIInlineBinaryData.class);
/* 29 */     if (inline != null) {
/* 30 */       prop.inlineBinaryData = true;
/* 31 */       inline.markAsAcknowledged();
/*    */     } 
/*    */   }
/*    */   
/*    */   public final QName getName() {
/* 36 */     return NAME;
/*    */   }
/*    */   
/* 39 */   public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "factoryMethod");
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\bindinfo\BIFactoryMethod.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */