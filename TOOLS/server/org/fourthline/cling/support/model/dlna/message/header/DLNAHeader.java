/*     */ package org.fourthline.cling.support.model.dlna.message.header;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.model.message.header.InvalidHeaderException;
/*     */ import org.fourthline.cling.model.message.header.UpnpHeader;
/*     */ import org.seamless.util.Exceptions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DLNAHeader<T>
/*     */   extends UpnpHeader<T>
/*     */ {
/*  39 */   private static final Logger log = Logger.getLogger(DLNAHeader.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Type
/*     */   {
/*  46 */     TimeSeekRange("TimeSeekRange.dlna.org", new Class[] { TimeSeekRangeHeader.class }),
/*  47 */     XSeekRange("X-Seek-Range", new Class[] { TimeSeekRangeHeader.class }),
/*  48 */     PlaySpeed("PlaySpeed.dlna.org", new Class[] { PlaySpeedHeader.class }),
/*  49 */     AvailableSeekRange("availableSeekRange.dlna.org", new Class[] { AvailableSeekRangeHeader.class }),
/*  50 */     GetAvailableSeekRange("getAvailableSeekRange.dlna.org", new Class[] { GetAvailableSeekRangeHeader.class }),
/*  51 */     GetContentFeatures("getcontentFeatures.dlna.org", new Class[] { GetContentFeaturesHeader.class }),
/*  52 */     ContentFeatures("contentFeatures.dlna.org", new Class[] { ContentFeaturesHeader.class }),
/*  53 */     TransferMode("transferMode.dlna.org", new Class[] { TransferModeHeader.class }),
/*  54 */     FriendlyName("friendlyName.dlna.org", new Class[] { FriendlyNameHeader.class }),
/*  55 */     PeerManager("peerManager.dlna.org", new Class[] { PeerManagerHeader.class }),
/*  56 */     AvailableRange("Available-Range.dlna.org", new Class[] { AvailableRangeHeader.class }),
/*  57 */     SCID("scid.dlna.org", new Class[] { SCIDHeader.class }),
/*  58 */     RealTimeInfo("realTimeInfo.dlna.org", new Class[] { RealTimeInfoHeader.class }),
/*  59 */     ScmsFlag("scmsFlag.dlna.org", new Class[] { ScmsFlagHeader.class }),
/*  60 */     WCT("WCT.dlna.org", new Class[] { WCTHeader.class }),
/*  61 */     MaxPrate("Max-Prate.dlna.org", new Class[] { MaxPrateHeader.class }),
/*  62 */     EventType("Event-Type.dlna.org", new Class[] { EventTypeHeader.class }),
/*  63 */     Supported("Supported", new Class[] { SupportedHeader.class }),
/*  64 */     BufferInfo("Buffer-Info.dlna.org", new Class[] { BufferInfoHeader.class }),
/*  65 */     RTPH264DeInterleaving("rtp-h264-deint-buf-cap.dlna.org", new Class[] { BufferBytesHeader.class }),
/*  66 */     RTPAACDeInterleaving("rtp-aac-deint-buf-cap.dlna.org", new Class[] { BufferBytesHeader.class }),
/*  67 */     RTPAMRDeInterleaving("rtp-amr-deint-buf-cap.dlna.org", new Class[] { BufferBytesHeader.class }),
/*  68 */     RTPAMRWBPlusDeInterleaving("rtp-amrwbplus-deint-buf-cap.dlna.org", new Class[] { BufferBytesHeader.class }),
/*  69 */     PRAGMA("PRAGMA", new Class[] { PragmaHeader.class });
/*     */     
/*  71 */     private static Map<String, Type> byName = new HashMap<String, Type>()
/*     */       {
/*     */       
/*     */       };
/*     */ 
/*     */     
/*     */     private String httpName;
/*     */ 
/*     */     
/*     */     @SafeVarargs
/*     */     Type(String httpName, Class<? extends DLNAHeader>... headerClass) {
/*  82 */       this.httpName = httpName;
/*  83 */       this.headerTypes = headerClass;
/*     */     } private Class<? extends DLNAHeader>[] headerTypes; static {
/*     */     
/*     */     } public String getHttpName() {
/*  87 */       return this.httpName;
/*     */     }
/*     */     
/*     */     public Class<? extends DLNAHeader>[] getHeaderTypes() {
/*  91 */       return this.headerTypes;
/*     */     }
/*     */     
/*     */     public boolean isValidHeaderType(Class<? extends DLNAHeader> clazz) {
/*  95 */       for (Class<? extends DLNAHeader> permissibleType : getHeaderTypes()) {
/*  96 */         if (permissibleType.isAssignableFrom(clazz)) {
/*  97 */           return true;
/*     */         }
/*     */       } 
/* 100 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Type getByHttpName(String httpName) {
/* 107 */       if (httpName == null) return null; 
/* 108 */       return byName.get(httpName);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DLNAHeader newInstance(Type type, String headerValue) {
/* 130 */     DLNAHeader upnpHeader = null;
/* 131 */     for (int i = 0; i < (type.getHeaderTypes()).length && upnpHeader == null; i++) {
/* 132 */       Class<? extends DLNAHeader> headerClass = type.getHeaderTypes()[i];
/*     */       try {
/* 134 */         log.finest("Trying to parse '" + type + "' with class: " + headerClass.getSimpleName());
/* 135 */         upnpHeader = headerClass.newInstance();
/* 136 */         if (headerValue != null) {
/* 137 */           upnpHeader.setString(headerValue);
/*     */         }
/* 139 */       } catch (InvalidHeaderException ex) {
/* 140 */         log.finest("Invalid header value for tested type: " + headerClass.getSimpleName() + " - " + ex.getMessage());
/* 141 */         upnpHeader = null;
/* 142 */       } catch (Exception ex) {
/* 143 */         log.severe("Error instantiating header of type '" + type + "' with value: " + headerValue);
/* 144 */         log.log(Level.SEVERE, "Exception root cause: ", Exceptions.unwrap(ex));
/*     */       } 
/*     */     } 
/*     */     
/* 148 */     return upnpHeader;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\message\header\DLNAHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */