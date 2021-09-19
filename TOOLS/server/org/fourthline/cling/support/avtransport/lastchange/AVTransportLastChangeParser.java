/*    */ package org.fourthline.cling.support.avtransport.lastchange;
/*    */ 
/*    */ import java.util.Set;
/*    */ import javax.xml.transform.Source;
/*    */ import javax.xml.transform.stream.StreamSource;
/*    */ import org.fourthline.cling.model.ModelUtil;
/*    */ import org.fourthline.cling.support.lastchange.EventedValue;
/*    */ import org.fourthline.cling.support.lastchange.LastChangeParser;
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
/*    */ public class AVTransportLastChangeParser
/*    */   extends LastChangeParser
/*    */ {
/*    */   public static final String NAMESPACE_URI = "urn:schemas-upnp-org:metadata-1-0/AVT/";
/*    */   public static final String SCHEMA_RESOURCE = "org/fourthline/cling/support/avtransport/metadata-1.01-avt.xsd";
/*    */   
/*    */   protected String getNamespace() {
/* 36 */     return "urn:schemas-upnp-org:metadata-1-0/AVT/";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Source[] getSchemaSources() {
/* 43 */     if (!ModelUtil.ANDROID_RUNTIME) {
/* 44 */       return new Source[] { new StreamSource(
/* 45 */             Thread.currentThread().getContextClassLoader().getResourceAsStream("org/fourthline/cling/support/avtransport/metadata-1.01-avt.xsd")) };
/*    */     }
/*    */     
/* 48 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Set<Class<? extends EventedValue>> getEventedVariables() {
/* 53 */     return AVTransportVariable.ALL;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\avtransport\lastchange\AVTransportLastChangeParser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */