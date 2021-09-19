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
/*    */ public class UDAServiceType
/*    */   extends ServiceType
/*    */ {
/*    */   public static final String DEFAULT_NAMESPACE = "schemas-upnp-org";
/* 34 */   public static final Pattern PATTERN = Pattern.compile("urn:schemas-upnp-org:service:([a-zA-Z_0-9\\-]{1,64}):([0-9]+).*");
/*    */   
/*    */   public UDAServiceType(String type) {
/* 37 */     this(type, 1);
/*    */   }
/*    */   
/*    */   public UDAServiceType(String type, int version) {
/* 41 */     super("schemas-upnp-org", type, version);
/*    */   }
/*    */   
/*    */   public static UDAServiceType valueOf(String s) throws InvalidValueException {
/* 45 */     Matcher matcher = PATTERN.matcher(s);
/*    */     
/*    */     try {
/* 48 */       if (matcher.matches())
/* 49 */         return new UDAServiceType(matcher.group(1), Integer.valueOf(matcher.group(2)).intValue()); 
/* 50 */     } catch (RuntimeException e) {
/* 51 */       throw new InvalidValueException(String.format("Can't parse UDA service type string (namespace/type/version) '%s': %s", new Object[] { s, e
/* 52 */               .toString() }));
/*    */     } 
/* 54 */     throw new InvalidValueException("Can't parse UDA service type string (namespace/type/version): " + s);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\UDAServiceType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */