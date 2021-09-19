/*     */ package com.wurmonline.website.news;
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
/*     */ public class News
/*     */ {
/*     */   private long timestamp;
/*     */   private String title;
/*     */   private String text;
/*     */   private String postedBy;
/*     */   private String id;
/*     */   
/*     */   public News() {}
/*     */   
/*     */   public News(String aTitle, String aText, String aPostedBy) {
/*  32 */     this.timestamp = System.currentTimeMillis();
/*  33 */     this.title = aTitle;
/*  34 */     this.text = aText;
/*  35 */     this.postedBy = aPostedBy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/*  44 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setId(String aId) {
/*  53 */     this.id = aId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPostedBy() {
/*  62 */     return this.postedBy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getText() {
/*  71 */     return this.text;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getTimestamp() {
/*  80 */     return this.timestamp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTitle() {
/*  89 */     return this.title;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPostedBy(String string) {
/*  98 */     this.postedBy = string;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setText(String string) {
/* 107 */     this.text = string;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimestamp(long l) {
/* 116 */     this.timestamp = l;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTitle(String string) {
/* 125 */     this.title = string;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\website\news\News.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */