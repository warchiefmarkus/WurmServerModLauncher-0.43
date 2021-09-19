/*    */ package org.fourthline.cling.model.types;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import org.fourthline.cling.model.ModelUtil;
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
/*    */ public class DLNACaps
/*    */ {
/*    */   final String[] caps;
/*    */   
/*    */   public DLNACaps(String[] caps) {
/* 32 */     this.caps = caps;
/*    */   }
/*    */   
/*    */   public String[] getCaps() {
/* 36 */     return this.caps;
/*    */   }
/*    */   
/*    */   public static DLNACaps valueOf(String s) throws InvalidValueException {
/* 40 */     if (s == null || s.length() == 0) return new DLNACaps(new String[0]); 
/* 41 */     String[] caps = s.split(",");
/* 42 */     String[] trimmed = new String[caps.length];
/* 43 */     for (int i = 0; i < caps.length; i++) {
/* 44 */       trimmed[i] = caps[i].trim();
/*    */     }
/* 46 */     return new DLNACaps(trimmed);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 51 */     if (this == o) return true; 
/* 52 */     if (o == null || getClass() != o.getClass()) return false;
/*    */     
/* 54 */     DLNACaps dlnaCaps = (DLNACaps)o;
/*    */     
/* 56 */     if (!Arrays.equals((Object[])this.caps, (Object[])dlnaCaps.caps)) return false;
/*    */     
/* 58 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 63 */     return Arrays.hashCode((Object[])this.caps);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 68 */     return ModelUtil.toCommaSeparatedList((Object[])getCaps());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\DLNACaps.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */