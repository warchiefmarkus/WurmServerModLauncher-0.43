/*    */ package org.fourthline.cling.support.model.dlna.message.header;
/*    */ 
/*    */ import java.util.EnumMap;
/*    */ import org.fourthline.cling.model.message.header.InvalidHeaderException;
/*    */ import org.fourthline.cling.support.model.dlna.DLNAAttribute;
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
/*    */ public class ContentFeaturesHeader
/*    */   extends DLNAHeader<EnumMap<DLNAAttribute.Type, DLNAAttribute>>
/*    */ {
/*    */   public ContentFeaturesHeader() {
/* 27 */     setValue(new EnumMap<>(DLNAAttribute.Type.class));
/*    */   }
/*    */ 
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/* 32 */     if (s.length() != 0) {
/* 33 */       String[] atts = s.split(";");
/* 34 */       for (String att : atts) {
/* 35 */         String[] attNameValue = att.split("=");
/* 36 */         if (attNameValue.length == 2) {
/* 37 */           DLNAAttribute.Type type = DLNAAttribute.Type.valueOfAttributeName(attNameValue[0]);
/* 38 */           if (type != null) {
/* 39 */             DLNAAttribute dlnaAttrinute = DLNAAttribute.newInstance(type, attNameValue[1], "");
/* 40 */             ((EnumMap<DLNAAttribute.Type, DLNAAttribute>)getValue()).put(type, dlnaAttrinute);
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String getString() {
/* 49 */     String s = "";
/* 50 */     for (DLNAAttribute.Type type : DLNAAttribute.Type.values()) {
/* 51 */       String value = ((EnumMap)getValue()).containsKey(type) ? ((DLNAAttribute)((EnumMap)getValue()).get(type)).getString() : null;
/* 52 */       if (value != null && value.length() != 0) {
/* 53 */         s = s + ((s.length() == 0) ? "" : ";") + type.getAttributeName() + "=" + value;
/*    */       }
/*    */     } 
/* 56 */     return s;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\message\header\ContentFeaturesHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */