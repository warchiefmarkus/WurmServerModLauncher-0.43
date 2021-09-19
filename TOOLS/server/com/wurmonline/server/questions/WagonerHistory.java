/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.Delivery;
/*     */ import com.wurmonline.server.creatures.Wagoner;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.Properties;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ public class WagonerHistory
/*     */   extends Question
/*     */ {
/*  38 */   private static final Logger logger = Logger.getLogger(WagonerHistory.class.getName());
/*     */   
/*     */   private final Wagoner wagoner;
/*  41 */   private long deliveryId = -10L;
/*  42 */   private int sortBy = 1;
/*  43 */   private int pageNo = 1;
/*     */   
/*     */   private boolean inQueue = true;
/*     */   private boolean waitAccept = true;
/*     */   private boolean inProgress = true;
/*     */   private boolean delivered = true;
/*     */   private boolean rejected = true;
/*     */   private boolean cancelled = true;
/*     */   
/*     */   public WagonerHistory(Creature aResponder, Wagoner wagoner) {
/*  53 */     super(aResponder, "History of " + wagoner.getName(), "History of " + wagoner.getName(), 148, wagoner.getWurmId());
/*  54 */     this.wagoner = wagoner;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WagonerHistory(Creature aResponder, Wagoner wagoner, long deliveryId, int sortBy, int pageNo, boolean inQueue, boolean waitAccept, boolean inProgress, boolean delivered, boolean rejected, boolean cancelled) {
/*  61 */     super(aResponder, "History of " + wagoner.getName(), "History of " + wagoner.getName(), 148, wagoner.getWurmId());
/*  62 */     this.wagoner = wagoner;
/*  63 */     this.deliveryId = deliveryId;
/*  64 */     this.sortBy = sortBy;
/*  65 */     this.pageNo = pageNo;
/*  66 */     this.inQueue = inQueue;
/*  67 */     this.waitAccept = waitAccept;
/*  68 */     this.inProgress = inProgress;
/*  69 */     this.delivered = delivered;
/*  70 */     this.rejected = rejected;
/*  71 */     this.cancelled = cancelled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  82 */     setAnswer(answers);
/*  83 */     if (this.type == 0) {
/*     */       
/*  85 */       logger.log(Level.INFO, "Received answer for a question with NOQUESTION.");
/*     */       return;
/*     */     } 
/*  88 */     if (this.type == 148) {
/*     */       
/*  90 */       boolean close = getBooleanProp("close");
/*  91 */       if (close) {
/*     */         return;
/*     */       }
/*     */       
/*  95 */       boolean filter = getBooleanProp("filter");
/*  96 */       if (filter) {
/*     */         
/*  98 */         this.inQueue = getBooleanProp("inqueue");
/*  99 */         this.waitAccept = getBooleanProp("waitaccept");
/* 100 */         this.inProgress = getBooleanProp("inprogress");
/* 101 */         this.delivered = getBooleanProp("delivered");
/* 102 */         this.rejected = getBooleanProp("rejected");
/* 103 */         this.cancelled = getBooleanProp("cancelled");
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 108 */         for (String key : getAnswer().stringPropertyNames()) {
/*     */           
/* 110 */           if (key.startsWith("sort")) {
/*     */ 
/*     */             
/* 113 */             String sid = key.substring(4);
/* 114 */             this.sortBy = Integer.parseInt(sid);
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/* 119 */       WagonerHistory wh = new WagonerHistory(getResponder(), this.wagoner, this.deliveryId, this.sortBy, this.pageNo, this.inQueue, this.waitAccept, this.inProgress, this.delivered, this.rejected, this.cancelled);
/*     */       
/* 121 */       wh.sendQuestion();
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
/*     */   public void sendQuestion() {
/* 133 */     StringBuilder buf = new StringBuilder();
/* 134 */     buf.append(getBmlHeaderWithScrollAndQuestion());
/* 135 */     buf.append("label{text=\"\"}");
/*     */     
/* 137 */     Delivery[] deliveries = Delivery.getDeliveriesFor(this.target, this.inQueue, this.waitAccept, this.inProgress, this.rejected, this.delivered);
/* 138 */     int absSortBy = Math.abs(this.sortBy);
/* 139 */     final int upDown = Integer.signum(this.sortBy);
/* 140 */     switch (absSortBy) {
/*     */       
/*     */       case 1:
/* 143 */         Arrays.sort(deliveries, new Comparator<Delivery>()
/*     */             {
/*     */               
/*     */               public int compare(Delivery param1, Delivery param2)
/*     */               {
/* 148 */                 return param1.getSenderName().compareTo(param2.getSenderName()) * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */       case 2:
/* 153 */         Arrays.sort(deliveries, new Comparator<Delivery>()
/*     */             {
/*     */               
/*     */               public int compare(Delivery param1, Delivery param2)
/*     */               {
/* 158 */                 return param1.getReceiverName().compareTo(param2.getReceiverName()) * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */       case 3:
/* 163 */         Arrays.sort(deliveries, new Comparator<Delivery>()
/*     */             {
/*     */               
/*     */               public int compare(Delivery param1, Delivery param2)
/*     */               {
/* 168 */                 return param1.getStateName().compareTo(param2.getStateName()) * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */       case 4:
/* 173 */         Arrays.sort(deliveries, new Comparator<Delivery>()
/*     */             {
/*     */ 
/*     */               
/*     */               public int compare(Delivery param1, Delivery param2)
/*     */               {
/* 179 */                 if (param1.getCrates() < param2.getCrates()) {
/* 180 */                   return 1 * upDown;
/*     */                 }
/* 182 */                 return -1 * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */       case 5:
/* 187 */         Arrays.sort(deliveries, new Comparator<Delivery>()
/*     */             {
/*     */ 
/*     */               
/*     */               public int compare(Delivery param1, Delivery param2)
/*     */               {
/* 193 */                 return param1.getSenderCostString().compareTo(param2.getSenderCostString()) * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */       case 6:
/* 198 */         Arrays.sort(deliveries, new Comparator<Delivery>()
/*     */             {
/*     */ 
/*     */               
/*     */               public int compare(Delivery param1, Delivery param2)
/*     */               {
/* 204 */                 return param1.getReceiverCostString().compareTo(param2.getReceiverCostString()) * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */       case 7:
/* 209 */         Arrays.sort(deliveries, new Comparator<Delivery>()
/*     */             {
/*     */               
/*     */               public int compare(Delivery param1, Delivery param2)
/*     */               {
/* 214 */                 if (param1.getWhenDelivered() < param2.getWhenDelivered()) {
/* 215 */                   return 1 * upDown;
/*     */                 }
/* 217 */                 return -1 * upDown;
/*     */               }
/*     */             });
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 226 */     buf.append("table{rows=\"1\";cols=\"8\";label{text=\"\"};" + 
/*     */         
/* 228 */         colHeader("Sender", 1, this.sortBy) + 
/* 229 */         colHeader("Receiver", 2, this.sortBy) + 
/* 230 */         colHeader("Delivery State", 3, this.sortBy) + 
/* 231 */         colHeader("# Crates", 4, this.sortBy) + 
/* 232 */         colHeader("Sender Costs", 5, this.sortBy) + 
/* 233 */         colHeader("Receiver Costs", 6, this.sortBy) + 
/* 234 */         colHeader("When Delivered", 7, this.sortBy));
/*     */     
/* 236 */     for (Delivery delivery : deliveries)
/*     */     {
/* 238 */       buf.append("label{text=\"\"};label{text=\"" + delivery
/* 239 */           .getSenderName() + "\"};label{text=\"" + delivery
/* 240 */           .getReceiverName() + "\"};label{text=\"" + delivery
/* 241 */           .getStateName() + "\"};label{text=\"" + delivery
/* 242 */           .getCrates() + "\"};label{text=\"" + delivery
/* 243 */           .getSenderCostString() + "\"};label{text=\"" + delivery
/* 244 */           .getReceiverCostString() + "\"};label{text=\"" + delivery
/* 245 */           .getStringDelivered() + "\"};");
/*     */     }
/*     */     
/* 248 */     buf.append("}");
/* 249 */     buf.append("text{text=\"\"}");
/* 250 */     buf.append("harray{button{text=\"Filter\";id=\"filter\"};label{text=\" by \"}checkbox{id=\"waitaccept\";text=\"Waiting for accept  \"" + (this.waitAccept ? ";selected=\"true\"" : "") + "};checkbox{id=\"inqueue\";text=\"In queue  \"" + (this.inQueue ? ";selected=\"true\"" : "") + "};checkbox{id=\"inprogress\";text=\"In Progress  \"" + (this.inProgress ? ";selected=\"true\"" : "") + "};checkbox{id=\"delivered\";text=\"Delivered  \"" + (this.delivered ? ";selected=\"true\"" : "") + "};checkbox{id=\"rejected\";text=\"Rejected  \"" + (this.rejected ? ";selected=\"true\"" : "") + "};checkbox{id=\"cancelled\";text=\"Cancelled \"" + (this.cancelled ? ";selected=\"true\"" : "") + "};};");
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
/* 261 */     buf.append("}};null;null;}");
/*     */     
/* 263 */     getResponder().getCommunicator().sendBml(550, 500, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\WagonerHistory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */