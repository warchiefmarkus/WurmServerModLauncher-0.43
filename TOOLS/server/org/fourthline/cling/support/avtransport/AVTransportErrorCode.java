/*    */ package org.fourthline.cling.support.avtransport;
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
/*    */ public enum AVTransportErrorCode
/*    */ {
/* 23 */   TRANSITION_NOT_AVAILABLE(701, "The immediate transition from current to desired state not supported"),
/* 24 */   NO_CONTENTS(702, "The media does not contain any contents that can be played"),
/* 25 */   READ_ERROR(703, "The media cannot be read"),
/* 26 */   PLAYBACK_FORMAT_NOT_SUPPORTED(704, "The storage format of the currently loaded media is not supported for playback"),
/* 27 */   TRANSPORT_LOCKED(705, "The transport is 'hold locked', e.g. with a keyboard lock"),
/* 28 */   WRITE_ERROR(706, "The media cannot be written"),
/* 29 */   MEDIA_PROTECTED(707, "The media is write-protected or is of a not writable type"),
/* 30 */   RECORD_FORMAT_NOT_SUPPORTED(708, "The storage format of the currently loaded media is not supported for recording"),
/* 31 */   MEDIA_FULL(709, "There is no free space left on the loaded media"),
/* 32 */   SEEKMODE_NOT_SUPPORTED(710, "The specified seek mode is not supported by the device"),
/* 33 */   ILLEGAL_SEEK_TARGET(711, "The specified seek target is not specified in terms of the seek mode, or is not present on the media"),
/* 34 */   PLAYMODE_NOT_SUPPORTED(712, "The specified play mode is not supported by the device"),
/* 35 */   RECORDQUALITYMODE_NOT_SUPPORTED(713, "The specified record quality mode is not supported by the device"),
/* 36 */   ILLEGAL_MIME_TYPE(714, "The specified resource has a MIME-type which is not supported"),
/* 37 */   CONTENT_BUSY(715, "The resource is already being played by other means"),
/* 38 */   RESOURCE_NOT_FOUND(716, "The specified resource cannot be found in the network"),
/* 39 */   INVALID_INSTANCE_ID(718, "The specified instanceID is invalid for this AVTransport");
/*    */   
/*    */   private int code;
/*    */   private String description;
/*    */   
/*    */   AVTransportErrorCode(int code, String description) {
/* 45 */     this.code = code;
/* 46 */     this.description = description;
/*    */   }
/*    */   
/*    */   public int getCode() {
/* 50 */     return this.code;
/*    */   }
/*    */   
/*    */   public String getDescription() {
/* 54 */     return this.description;
/*    */   }
/*    */   
/*    */   public static AVTransportErrorCode getByCode(int code) {
/* 58 */     for (AVTransportErrorCode errorCode : values()) {
/* 59 */       if (errorCode.getCode() == code)
/* 60 */         return errorCode; 
/*    */     } 
/* 62 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\avtransport\AVTransportErrorCode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */