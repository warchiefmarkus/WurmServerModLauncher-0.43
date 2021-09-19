/*    */ package org.fourthline.cling.model.types;
/*    */ 
/*    */ import java.util.logging.Logger;
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
/*    */ public class UDAServiceId
/*    */   extends ServiceId
/*    */ {
/* 34 */   private static Logger log = Logger.getLogger(UDAServiceId.class.getName());
/*    */   
/*    */   public static final String DEFAULT_NAMESPACE = "upnp-org";
/*    */   
/*    */   public static final String BROKEN_DEFAULT_NAMESPACE = "schemas-upnp-org";
/*    */   
/* 40 */   public static final Pattern PATTERN = Pattern.compile("urn:upnp-org:serviceId:([a-zA-Z_0-9\\-:\\.]{1,64})");
/*    */ 
/*    */ 
/*    */   
/* 44 */   public static final Pattern BROKEN_PATTERN = Pattern.compile("urn:schemas-upnp-org:service:([a-zA-Z_0-9\\-:\\.]{1,64})");
/*    */   
/*    */   public UDAServiceId(String id) {
/* 47 */     super("upnp-org", id);
/*    */   }
/*    */   
/*    */   public static UDAServiceId valueOf(String s) throws InvalidValueException {
/* 51 */     Matcher matcher = PATTERN.matcher(s);
/* 52 */     if (matcher.matches() && matcher.groupCount() >= 1) {
/* 53 */       return new UDAServiceId(matcher.group(1));
/*    */     }
/*    */     
/* 56 */     matcher = BROKEN_PATTERN.matcher(s);
/* 57 */     if (matcher.matches() && matcher.groupCount() >= 1) {
/* 58 */       return new UDAServiceId(matcher.group(1));
/*    */     }
/*    */ 
/*    */     
/* 62 */     matcher = Pattern.compile("urn:upnp-orgerviceId:urnchemas-upnp-orgervice:([a-zA-Z_0-9\\-:\\.]{1,64})").matcher(s);
/* 63 */     if (matcher.matches()) {
/* 64 */       log.warning("UPnP specification violation, recovering from Eyecon garbage: " + s);
/* 65 */       return new UDAServiceId(matcher.group(1));
/*    */     } 
/*    */ 
/*    */     
/* 69 */     if ("ContentDirectory".equals(s) || "ConnectionManager"
/* 70 */       .equals(s) || "RenderingControl"
/* 71 */       .equals(s) || "AVTransport"
/* 72 */       .equals(s)) {
/* 73 */       log.warning("UPnP specification violation, fixing broken Service ID: " + s);
/* 74 */       return new UDAServiceId(s);
/*    */     } 
/*    */     
/* 77 */     throw new InvalidValueException("Can't parse UDA service ID string (upnp-org/id): " + s);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\UDAServiceId.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */