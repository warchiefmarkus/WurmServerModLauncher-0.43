/*     */ package org.fourthline.cling.support.model.dlna;
/*     */ 
/*     */ import java.util.EnumMap;
/*     */ import java.util.Map;
/*     */ import org.fourthline.cling.model.types.InvalidValueException;
/*     */ import org.fourthline.cling.support.model.Protocol;
/*     */ import org.fourthline.cling.support.model.ProtocolInfo;
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
/*     */ public class DLNAProtocolInfo
/*     */   extends ProtocolInfo
/*     */ {
/*  34 */   protected final Map<DLNAAttribute.Type, DLNAAttribute> attributes = new EnumMap<>(DLNAAttribute.Type.class);
/*     */   
/*     */   public DLNAProtocolInfo(String s) throws InvalidValueException {
/*  37 */     super(s);
/*  38 */     parseAdditionalInfo();
/*     */   }
/*     */   
/*     */   public DLNAProtocolInfo(MimeType contentFormatMimeType) {
/*  42 */     super(contentFormatMimeType);
/*     */   }
/*     */   
/*     */   public DLNAProtocolInfo(DLNAProfiles profile) {
/*  46 */     super(MimeType.valueOf(profile.getContentFormat()));
/*  47 */     this.attributes.put(DLNAAttribute.Type.DLNA_ORG_PN, new DLNAProfileAttribute(profile));
/*  48 */     this.additionalInfo = getAttributesString();
/*     */   }
/*     */   
/*     */   public DLNAProtocolInfo(DLNAProfiles profile, EnumMap<DLNAAttribute.Type, DLNAAttribute> attributes) {
/*  52 */     super(MimeType.valueOf(profile.getContentFormat()));
/*  53 */     this.attributes.putAll(attributes);
/*  54 */     this.attributes.put(DLNAAttribute.Type.DLNA_ORG_PN, new DLNAProfileAttribute(profile));
/*  55 */     this.additionalInfo = getAttributesString();
/*     */   }
/*     */   
/*     */   public DLNAProtocolInfo(Protocol protocol, String network, String contentFormat, String additionalInfo) {
/*  59 */     super(protocol, network, contentFormat, additionalInfo);
/*  60 */     parseAdditionalInfo();
/*     */   }
/*     */   
/*     */   public DLNAProtocolInfo(Protocol protocol, String network, String contentFormat, EnumMap<DLNAAttribute.Type, DLNAAttribute> attributes) {
/*  64 */     super(protocol, network, contentFormat, "");
/*  65 */     this.attributes.putAll(attributes);
/*  66 */     this.additionalInfo = getAttributesString();
/*     */   }
/*     */   
/*     */   public DLNAProtocolInfo(ProtocolInfo template) {
/*  70 */     this(template.getProtocol(), template
/*  71 */         .getNetwork(), template
/*  72 */         .getContentFormat(), template
/*  73 */         .getAdditionalInfo());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(DLNAAttribute.Type type) {
/*  78 */     return this.attributes.containsKey(type);
/*     */   }
/*     */   
/*     */   public DLNAAttribute getAttribute(DLNAAttribute.Type type) {
/*  82 */     return this.attributes.get(type);
/*     */   }
/*     */   
/*     */   public Map<DLNAAttribute.Type, DLNAAttribute> getAttributes() {
/*  86 */     return this.attributes;
/*     */   }
/*     */   
/*     */   protected String getAttributesString() {
/*  90 */     String s = "";
/*  91 */     for (DLNAAttribute.Type type : DLNAAttribute.Type.values()) {
/*  92 */       String value = this.attributes.containsKey(type) ? ((DLNAAttribute)this.attributes.get(type)).getString() : null;
/*  93 */       if (value != null && value.length() != 0)
/*  94 */         s = s + ((s.length() == 0) ? "" : ";") + type.getAttributeName() + "=" + value; 
/*     */     } 
/*  96 */     return s;
/*     */   }
/*     */   
/*     */   protected void parseAdditionalInfo() {
/* 100 */     if (this.additionalInfo != null) {
/* 101 */       String[] atts = this.additionalInfo.split(";");
/* 102 */       for (String att : atts) {
/* 103 */         String[] attNameValue = att.split("=");
/* 104 */         if (attNameValue.length == 2) {
/*     */           
/* 106 */           DLNAAttribute.Type type = DLNAAttribute.Type.valueOfAttributeName(attNameValue[0]);
/* 107 */           if (type != null) {
/*     */             
/* 109 */             DLNAAttribute dlnaAttrinute = DLNAAttribute.newInstance(type, attNameValue[1], getContentFormat());
/* 110 */             this.attributes.put(type, dlnaAttrinute);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\DLNAProtocolInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */