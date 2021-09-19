/*    */ package org.fourthline.cling.model.types;
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
/*    */ public class CharacterDatatype
/*    */   extends AbstractDatatype<Character>
/*    */ {
/*    */   public boolean isHandlingJavaType(Class<char> type) {
/* 28 */     return (type == char.class || Character.class.isAssignableFrom(type));
/*    */   }
/*    */   
/*    */   public Character valueOf(String s) throws InvalidValueException {
/* 32 */     if (s.equals("")) return null; 
/* 33 */     return Character.valueOf(s.charAt(0));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\CharacterDatatype.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */