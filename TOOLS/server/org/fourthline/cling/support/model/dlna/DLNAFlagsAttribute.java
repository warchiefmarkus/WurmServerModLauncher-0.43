/*    */ package org.fourthline.cling.support.model.dlna;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import java.util.Locale;
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
/*    */ public class DLNAFlagsAttribute
/*    */   extends DLNAAttribute<EnumSet<DLNAFlags>>
/*    */ {
/*    */   public DLNAFlagsAttribute() {
/* 26 */     setValue(EnumSet.noneOf(DLNAFlags.class));
/*    */   }
/*    */   
/*    */   public DLNAFlagsAttribute(DLNAFlags... flags) {
/* 30 */     if (flags != null && flags.length > 0) {
/* 31 */       DLNAFlags first = flags[0];
/* 32 */       if (flags.length > 1) {
/* 33 */         System.arraycopy(flags, 1, flags, 0, flags.length - 1);
/* 34 */         setValue(EnumSet.of(first, flags));
/*    */       } else {
/* 36 */         setValue(EnumSet.of(first));
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void setString(String s, String cf) throws InvalidDLNAProtocolAttributeException {
/* 42 */     EnumSet<DLNAFlags> value = EnumSet.noneOf(DLNAFlags.class);
/*    */     try {
/* 44 */       int parseInt = Integer.parseInt(s.substring(0, s.length() - 24), 16);
/* 45 */       for (DLNAFlags op : DLNAFlags.values()) {
/* 46 */         int code = op.getCode() & parseInt;
/* 47 */         if (op.getCode() == code) {
/* 48 */           value.add(op);
/*    */         }
/*    */       } 
/* 51 */     } catch (Exception exception) {}
/*    */ 
/*    */     
/* 54 */     if (value.isEmpty()) {
/* 55 */       throw new InvalidDLNAProtocolAttributeException("Can't parse DLNA flags integer from: " + s);
/*    */     }
/* 57 */     setValue(value);
/*    */   }
/*    */   
/*    */   public String getString() {
/* 61 */     int code = 0;
/* 62 */     for (DLNAFlags op : getValue()) {
/* 63 */       code |= op.getCode();
/*    */     }
/* 65 */     return String.format(Locale.ROOT, "%08x%024x", new Object[] { Integer.valueOf(code), Integer.valueOf(0) });
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\DLNAFlagsAttribute.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */