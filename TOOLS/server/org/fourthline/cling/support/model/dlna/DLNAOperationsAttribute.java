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
/*    */ public class DLNAOperationsAttribute
/*    */   extends DLNAAttribute<EnumSet<DLNAOperations>>
/*    */ {
/*    */   public DLNAOperationsAttribute() {
/* 26 */     setValue(EnumSet.of(DLNAOperations.NONE));
/*    */   }
/*    */   
/*    */   public DLNAOperationsAttribute(DLNAOperations... op) {
/* 30 */     if (op != null && op.length > 0) {
/* 31 */       DLNAOperations first = op[0];
/* 32 */       if (op.length > 1) {
/* 33 */         System.arraycopy(op, 1, op, 0, op.length - 1);
/* 34 */         setValue(EnumSet.of(first, op));
/*    */       } else {
/* 36 */         setValue(EnumSet.of(first));
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void setString(String s, String cf) throws InvalidDLNAProtocolAttributeException {
/* 42 */     EnumSet<DLNAOperations> value = EnumSet.noneOf(DLNAOperations.class);
/*    */     try {
/* 44 */       int parseInt = Integer.parseInt(s, 16);
/* 45 */       for (DLNAOperations op : DLNAOperations.values()) {
/* 46 */         int code = op.getCode() & parseInt;
/* 47 */         if (op != DLNAOperations.NONE && op.getCode() == code) {
/* 48 */           value.add(op);
/*    */         }
/*    */       } 
/* 51 */     } catch (NumberFormatException numberFormatException) {}
/*    */ 
/*    */     
/* 54 */     if (value.isEmpty()) {
/* 55 */       throw new InvalidDLNAProtocolAttributeException("Can't parse DLNA operations integer from: " + s);
/*    */     }
/* 57 */     setValue(value);
/*    */   }
/*    */   
/*    */   public String getString() {
/* 61 */     int code = DLNAOperations.NONE.getCode();
/* 62 */     for (DLNAOperations op : getValue()) {
/* 63 */       code |= op.getCode();
/*    */     }
/* 65 */     return String.format(Locale.ROOT, "%02x", new Object[] { Integer.valueOf(code) });
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\DLNAOperationsAttribute.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */