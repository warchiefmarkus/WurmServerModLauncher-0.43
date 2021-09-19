/*    */ package com.sun.tools.xjc.reader.xmlschema.bindinfo;
/*    */ 
/*    */ import com.sun.tools.xjc.model.CPropertyInfo;
/*    */ import com.sun.tools.xjc.reader.Ring;
/*    */ import com.sun.tools.xjc.reader.xmlschema.BGMBuilder;
/*    */ import com.sun.xml.xsom.XSComponent;
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
/*    */ @XmlRootElement(name = "inlineBinaryData")
/*    */ public class BIInlineBinaryData
/*    */   extends AbstractDeclarationImpl
/*    */ {
/*    */   public static void handle(XSComponent source, CPropertyInfo prop) {
/* 26 */     BIInlineBinaryData inline = ((BGMBuilder)Ring.get(BGMBuilder.class)).getBindInfo(source).<BIInlineBinaryData>get(BIInlineBinaryData.class);
/* 27 */     if (inline != null) {
/* 28 */       prop.inlineBinaryData = true;
/* 29 */       inline.markAsAcknowledged();
/*    */     } 
/*    */   }
/*    */   
/*    */   public final QName getName() {
/* 34 */     return NAME;
/*    */   }
/*    */   
/* 37 */   public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "inlineBinaryData");
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\bindinfo\BIInlineBinaryData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */