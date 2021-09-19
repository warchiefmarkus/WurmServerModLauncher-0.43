/*     */ package org.fourthline.cling.support.model;
/*     */ 
/*     */ import java.net.URI;
/*     */ import org.seamless.util.MimeType;
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
/*     */ public class Res
/*     */ {
/*     */   protected URI importUri;
/*     */   protected ProtocolInfo protocolInfo;
/*     */   protected Long size;
/*     */   protected String duration;
/*     */   protected Long bitrate;
/*     */   protected Long sampleFrequency;
/*     */   protected Long bitsPerSample;
/*     */   protected Long nrAudioChannels;
/*     */   protected Long colorDepth;
/*     */   protected String protection;
/*     */   protected String resolution;
/*     */   protected String value;
/*     */   
/*     */   public Res() {}
/*     */   
/*     */   public Res(String httpGetMimeType, Long size, String duration, Long bitrate, String value) {
/*  45 */     this(new ProtocolInfo(Protocol.HTTP_GET, "*", httpGetMimeType, "*"), size, duration, bitrate, value);
/*     */   }
/*     */   
/*     */   public Res(MimeType httpGetMimeType, Long size, String duration, Long bitrate, String value) {
/*  49 */     this(new ProtocolInfo(httpGetMimeType), size, duration, bitrate, value);
/*     */   }
/*     */   
/*     */   public Res(MimeType httpGetMimeType, Long size, String value) {
/*  53 */     this(new ProtocolInfo(httpGetMimeType), size, value);
/*     */   }
/*     */   
/*     */   public Res(ProtocolInfo protocolInfo, Long size, String value) {
/*  57 */     this.protocolInfo = protocolInfo;
/*  58 */     this.size = size;
/*  59 */     this.value = value;
/*     */   }
/*     */   
/*     */   public Res(ProtocolInfo protocolInfo, Long size, String duration, Long bitrate, String value) {
/*  63 */     this.protocolInfo = protocolInfo;
/*  64 */     this.size = size;
/*  65 */     this.duration = duration;
/*  66 */     this.bitrate = bitrate;
/*  67 */     this.value = value;
/*     */   }
/*     */   
/*     */   public Res(URI importUri, ProtocolInfo protocolInfo, Long size, String duration, Long bitrate, Long sampleFrequency, Long bitsPerSample, Long nrAudioChannels, Long colorDepth, String protection, String resolution, String value) {
/*  71 */     this.importUri = importUri;
/*  72 */     this.protocolInfo = protocolInfo;
/*  73 */     this.size = size;
/*  74 */     this.duration = duration;
/*  75 */     this.bitrate = bitrate;
/*  76 */     this.sampleFrequency = sampleFrequency;
/*  77 */     this.bitsPerSample = bitsPerSample;
/*  78 */     this.nrAudioChannels = nrAudioChannels;
/*  79 */     this.colorDepth = colorDepth;
/*  80 */     this.protection = protection;
/*  81 */     this.resolution = resolution;
/*  82 */     this.value = value;
/*     */   }
/*     */   
/*     */   public URI getImportUri() {
/*  86 */     return this.importUri;
/*     */   }
/*     */   
/*     */   public void setImportUri(URI importUri) {
/*  90 */     this.importUri = importUri;
/*     */   }
/*     */   
/*     */   public ProtocolInfo getProtocolInfo() {
/*  94 */     return this.protocolInfo;
/*     */   }
/*     */   
/*     */   public void setProtocolInfo(ProtocolInfo protocolInfo) {
/*  98 */     this.protocolInfo = protocolInfo;
/*     */   }
/*     */   
/*     */   public Long getSize() {
/* 102 */     return this.size;
/*     */   }
/*     */   
/*     */   public void setSize(Long size) {
/* 106 */     this.size = size;
/*     */   }
/*     */   
/*     */   public String getDuration() {
/* 110 */     return this.duration;
/*     */   }
/*     */   
/*     */   public void setDuration(String duration) {
/* 114 */     this.duration = duration;
/*     */   }
/*     */   
/*     */   public Long getBitrate() {
/* 118 */     return this.bitrate;
/*     */   }
/*     */   
/*     */   public void setBitrate(Long bitrate) {
/* 122 */     this.bitrate = bitrate;
/*     */   }
/*     */   
/*     */   public Long getSampleFrequency() {
/* 126 */     return this.sampleFrequency;
/*     */   }
/*     */   
/*     */   public void setSampleFrequency(Long sampleFrequency) {
/* 130 */     this.sampleFrequency = sampleFrequency;
/*     */   }
/*     */   
/*     */   public Long getBitsPerSample() {
/* 134 */     return this.bitsPerSample;
/*     */   }
/*     */   
/*     */   public void setBitsPerSample(Long bitsPerSample) {
/* 138 */     this.bitsPerSample = bitsPerSample;
/*     */   }
/*     */   
/*     */   public Long getNrAudioChannels() {
/* 142 */     return this.nrAudioChannels;
/*     */   }
/*     */   
/*     */   public void setNrAudioChannels(Long nrAudioChannels) {
/* 146 */     this.nrAudioChannels = nrAudioChannels;
/*     */   }
/*     */   
/*     */   public Long getColorDepth() {
/* 150 */     return this.colorDepth;
/*     */   }
/*     */   
/*     */   public void setColorDepth(Long colorDepth) {
/* 154 */     this.colorDepth = colorDepth;
/*     */   }
/*     */   
/*     */   public String getProtection() {
/* 158 */     return this.protection;
/*     */   }
/*     */   
/*     */   public void setProtection(String protection) {
/* 162 */     this.protection = protection;
/*     */   }
/*     */   
/*     */   public String getResolution() {
/* 166 */     return this.resolution;
/*     */   }
/*     */   
/*     */   public void setResolution(String resolution) {
/* 170 */     this.resolution = resolution;
/*     */   }
/*     */   
/*     */   public void setResolution(int x, int y) {
/* 174 */     this.resolution = x + "x" + y;
/*     */   }
/*     */   
/*     */   public int getResolutionX() {
/* 178 */     return (getResolution() != null && (getResolution().split("x")).length == 2) ? 
/* 179 */       Integer.valueOf(getResolution().split("x")[0]).intValue() : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getResolutionY() {
/* 184 */     return (getResolution() != null && (getResolution().split("x")).length == 2) ? 
/* 185 */       Integer.valueOf(getResolution().split("x")[1]).intValue() : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getValue() {
/* 190 */     return this.value;
/*     */   }
/*     */   
/*     */   public void setValue(String value) {
/* 194 */     this.value = value;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\Res.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */