/*    */ package org.fourthline.cling.support.renderingcontrol.lastchange;
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
/*    */ public class RenderingControlLastChangeParser
/*    */   extends LastChangeParser
/*    */ {
/*    */   public static final String NAMESPACE_URI = "urn:schemas-upnp-org:metadata-1-0/RCS/";
/*    */   public static final String SCHEMA_RESOURCE = "org/fourthline/cling/support/renderingcontrol/metadata-1.0-rcs.xsd";
/*    */   
/*    */   protected String getNamespace() {
/* 36 */     return "urn:schemas-upnp-org:metadata-1-0/RCS/";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Source[] getSchemaSources() {
/* 43 */     if (!ModelUtil.ANDROID_RUNTIME) {
/* 44 */       return new Source[] { new StreamSource(
/* 45 */             Thread.currentThread().getContextClassLoader().getResourceAsStream("org/fourthline/cling/support/renderingcontrol/metadata-1.0-rcs.xsd")) };
/*    */     }
/*    */     
/* 48 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Set<Class<? extends EventedValue>> getEventedVariables() {
/* 53 */     return RenderingControlVariable.ALL;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\renderingcontrol\lastchange\RenderingControlLastChangeParser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */