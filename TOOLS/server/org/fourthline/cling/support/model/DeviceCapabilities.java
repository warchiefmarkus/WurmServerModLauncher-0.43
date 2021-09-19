/*    */ package org.fourthline.cling.support.model;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.fourthline.cling.model.ModelUtil;
/*    */ import org.fourthline.cling.model.action.ActionArgumentValue;
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
/*    */ public class DeviceCapabilities
/*    */ {
/*    */   private StorageMedium[] playMedia;
/* 29 */   private StorageMedium[] recMedia = new StorageMedium[] { StorageMedium.NOT_IMPLEMENTED };
/* 30 */   private RecordQualityMode[] recQualityModes = new RecordQualityMode[] { RecordQualityMode.NOT_IMPLEMENTED };
/*    */   
/*    */   public DeviceCapabilities(Map<String, ActionArgumentValue> args) {
/* 33 */     this(
/* 34 */         StorageMedium.valueOfCommaSeparatedList((String)((ActionArgumentValue)args.get("PlayMedia")).getValue()), 
/* 35 */         StorageMedium.valueOfCommaSeparatedList((String)((ActionArgumentValue)args.get("RecMedia")).getValue()), 
/* 36 */         RecordQualityMode.valueOfCommaSeparatedList((String)((ActionArgumentValue)args.get("RecQualityModes")).getValue()));
/*    */   }
/*    */ 
/*    */   
/*    */   public DeviceCapabilities(StorageMedium[] playMedia) {
/* 41 */     this.playMedia = playMedia;
/*    */   }
/*    */   
/*    */   public DeviceCapabilities(StorageMedium[] playMedia, StorageMedium[] recMedia, RecordQualityMode[] recQualityModes) {
/* 45 */     this.playMedia = playMedia;
/* 46 */     this.recMedia = recMedia;
/* 47 */     this.recQualityModes = recQualityModes;
/*    */   }
/*    */   
/*    */   public StorageMedium[] getPlayMedia() {
/* 51 */     return this.playMedia;
/*    */   }
/*    */   
/*    */   public StorageMedium[] getRecMedia() {
/* 55 */     return this.recMedia;
/*    */   }
/*    */   
/*    */   public RecordQualityMode[] getRecQualityModes() {
/* 59 */     return this.recQualityModes;
/*    */   }
/*    */   
/*    */   public String getPlayMediaString() {
/* 63 */     return ModelUtil.toCommaSeparatedList((Object[])this.playMedia);
/*    */   }
/*    */   
/*    */   public String getRecMediaString() {
/* 67 */     return ModelUtil.toCommaSeparatedList((Object[])this.recMedia);
/*    */   }
/*    */   
/*    */   public String getRecQualityModesString() {
/* 71 */     return ModelUtil.toCommaSeparatedList((Object[])this.recQualityModes);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\DeviceCapabilities.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */