/*    */ package org.fourthline.cling.model.types;
/*    */ 
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
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
/*    */ public class UDADeviceType
/*    */   extends DeviceType
/*    */ {
/*    */   public static final String DEFAULT_NAMESPACE = "schemas-upnp-org";
/* 34 */   public static final Pattern PATTERN = Pattern.compile("urn:schemas-upnp-org:device:([a-zA-Z_0-9\\-]{1,64}):([0-9]+).*");
/*    */   
/*    */   public UDADeviceType(String type) {
/* 37 */     super("schemas-upnp-org", type, 1);
/*    */   }
/*    */   
/*    */   public UDADeviceType(String type, int version) {
/* 41 */     super("schemas-upnp-org", type, version);
/*    */   }
/*    */   
/*    */   public static UDADeviceType valueOf(String s) throws InvalidValueException {
/* 45 */     Matcher matcher = PATTERN.matcher(s);
/*    */     
/*    */     try {
/* 48 */       if (matcher.matches())
/* 49 */         return new UDADeviceType(matcher.group(1), Integer.valueOf(matcher.group(2)).intValue()); 
/* 50 */     } catch (RuntimeException e) {
/* 51 */       throw new InvalidValueException(String.format("Can't parse UDA device type string (namespace/type/version) '%s': %s", new Object[] { s, e
/* 52 */               .toString() }));
/*    */     } 
/*    */     
/* 55 */     throw new InvalidValueException("Can't parse UDA device type string (namespace/type/version): " + s);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\UDADeviceType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */