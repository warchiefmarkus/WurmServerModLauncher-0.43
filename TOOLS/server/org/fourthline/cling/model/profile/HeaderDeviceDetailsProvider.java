/*    */ package org.fourthline.cling.model.profile;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.regex.Pattern;
/*    */ import org.fourthline.cling.model.meta.DeviceDetails;
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
/*    */ public class HeaderDeviceDetailsProvider
/*    */   implements DeviceDetailsProvider
/*    */ {
/*    */   private final DeviceDetails defaultDeviceDetails;
/*    */   private final Map<Key, DeviceDetails> headerDetails;
/*    */   
/*    */   public static class Key
/*    */   {
/*    */     final String headerName;
/*    */     final String valuePattern;
/*    */     final Pattern pattern;
/*    */     
/*    */     public Key(String headerName, String valuePattern) {
/* 47 */       this.headerName = headerName;
/* 48 */       this.valuePattern = valuePattern;
/* 49 */       this.pattern = Pattern.compile(valuePattern, 2);
/*    */     }
/*    */     
/*    */     public String getHeaderName() {
/* 53 */       return this.headerName;
/*    */     }
/*    */     
/*    */     public String getValuePattern() {
/* 57 */       return this.valuePattern;
/*    */     }
/*    */     
/*    */     public boolean isValuePatternMatch(String value) {
/* 61 */       return this.pattern.matcher(value).matches();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HeaderDeviceDetailsProvider(DeviceDetails defaultDeviceDetails) {
/* 70 */     this(defaultDeviceDetails, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public HeaderDeviceDetailsProvider(DeviceDetails defaultDeviceDetails, Map<Key, DeviceDetails> headerDetails) {
/* 75 */     this.defaultDeviceDetails = defaultDeviceDetails;
/* 76 */     this.headerDetails = (headerDetails != null) ? headerDetails : new HashMap<>();
/*    */   }
/*    */   
/*    */   public DeviceDetails getDefaultDeviceDetails() {
/* 80 */     return this.defaultDeviceDetails;
/*    */   }
/*    */   
/*    */   public Map<Key, DeviceDetails> getHeaderDetails() {
/* 84 */     return this.headerDetails;
/*    */   }
/*    */   
/*    */   public DeviceDetails provide(RemoteClientInfo info) {
/* 88 */     if (info == null || info.getRequestHeaders().isEmpty()) return getDefaultDeviceDetails();
/*    */     
/* 90 */     for (Key key : getHeaderDetails().keySet()) {
/*    */       List<String> headerValues;
/* 92 */       if ((headerValues = info.getRequestHeaders().get(key.getHeaderName())) == null)
/* 93 */         continue;  for (String headerValue : headerValues) {
/* 94 */         if (key.isValuePatternMatch(headerValue))
/* 95 */           return getHeaderDetails().get(key); 
/*    */       } 
/*    */     } 
/* 98 */     return getDefaultDeviceDetails();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\profile\HeaderDeviceDetailsProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */