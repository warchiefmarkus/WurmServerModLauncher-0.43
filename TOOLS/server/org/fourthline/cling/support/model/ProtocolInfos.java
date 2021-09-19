/*    */ package org.fourthline.cling.support.model;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import org.fourthline.cling.model.ModelUtil;
/*    */ import org.fourthline.cling.model.types.InvalidValueException;
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
/*    */ public class ProtocolInfos
/*    */   extends ArrayList<ProtocolInfo>
/*    */ {
/*    */   public ProtocolInfos(ProtocolInfo... info) {
/* 29 */     for (ProtocolInfo protocolInfo : info) {
/* 30 */       add(protocolInfo);
/*    */     }
/*    */   }
/*    */   
/*    */   public ProtocolInfos(String s) throws InvalidValueException {
/* 35 */     String[] infos = ModelUtil.fromCommaSeparatedList(s);
/* 36 */     if (infos != null)
/* 37 */       for (String info : infos) {
/* 38 */         add(new ProtocolInfo(info));
/*    */       } 
/*    */   }
/*    */   
/*    */   public String toString() {
/* 43 */     return ModelUtil.toCommaSeparatedList(toArray((Object[])new ProtocolInfo[size()]));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\ProtocolInfos.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */