/*     */ package org.fourthline.cling.support.model.dlna.types;
/*     */ 
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.fourthline.cling.model.types.InvalidValueException;
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
/*     */ public class BufferInfoType
/*     */ {
/*  28 */   static final Pattern pattern = Pattern.compile("^dejitter=(\\d{1,10})(;CDB=(\\d{1,10});BTM=(0|1|2))?(;TD=(\\d{1,10}))?(;BFR=(0|1))?$", 2);
/*     */   private Long dejitterSize;
/*     */   private CodedDataBuffer cdb;
/*     */   private Long targetDuration;
/*     */   private Boolean fullnessReports;
/*     */   
/*     */   public BufferInfoType(Long dejitterSize) {
/*  35 */     this.dejitterSize = dejitterSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public BufferInfoType(Long dejitterSize, CodedDataBuffer cdb, Long targetDuration, Boolean fullnessReports) {
/*  40 */     this.dejitterSize = dejitterSize;
/*  41 */     this.cdb = cdb;
/*  42 */     this.targetDuration = targetDuration;
/*  43 */     this.fullnessReports = fullnessReports;
/*     */   }
/*     */   
/*     */   public static BufferInfoType valueOf(String s) throws InvalidValueException {
/*  47 */     Matcher matcher = pattern.matcher(s);
/*  48 */     if (matcher.matches()) {
/*     */       try {
/*  50 */         Long dejitterSize = Long.valueOf(Long.parseLong(matcher.group(1)));
/*  51 */         CodedDataBuffer cdb = null;
/*  52 */         Long targetDuration = null;
/*  53 */         Boolean fullnessReports = null;
/*     */         
/*  55 */         if (matcher.group(2) != null)
/*     */         {
/*  57 */           cdb = new CodedDataBuffer(Long.valueOf(Long.parseLong(matcher.group(3))), CodedDataBuffer.TransferMechanism.values()[Integer.parseInt(matcher.group(4))]);
/*     */         }
/*  59 */         if (matcher.group(5) != null) {
/*  60 */           targetDuration = Long.valueOf(Long.parseLong(matcher.group(6)));
/*     */         }
/*  62 */         if (matcher.group(7) != null) {
/*  63 */           fullnessReports = Boolean.valueOf(matcher.group(8).equals("1"));
/*     */         }
/*  65 */         return new BufferInfoType(dejitterSize, cdb, targetDuration, fullnessReports);
/*  66 */       } catch (NumberFormatException numberFormatException) {}
/*     */     }
/*     */     
/*  69 */     throw new InvalidValueException("Can't parse BufferInfoType: " + s);
/*     */   }
/*     */   
/*     */   public String getString() {
/*  73 */     String s = "dejitter=" + this.dejitterSize.toString();
/*  74 */     if (this.cdb != null) {
/*  75 */       s = s + ";CDB=" + this.cdb.getSize().toString() + ";BTM=" + this.cdb.getTranfer().ordinal();
/*     */     }
/*  77 */     if (this.targetDuration != null) {
/*  78 */       s = s + ";TD=" + this.targetDuration.toString();
/*     */     }
/*  80 */     if (this.fullnessReports != null) {
/*  81 */       s = s + ";BFR=" + (this.fullnessReports.booleanValue() ? "1" : "0");
/*     */     }
/*  83 */     return s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Long getDejitterSize() {
/*  90 */     return this.dejitterSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CodedDataBuffer getCdb() {
/*  97 */     return this.cdb;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Long getTargetDuration() {
/* 104 */     return this.targetDuration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean isFullnessReports() {
/* 111 */     return this.fullnessReports;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\types\BufferInfoType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */