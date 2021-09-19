/*     */ package com.wurmonline.server.creatures.ai;
/*     */ 
/*     */ import java.util.LinkedList;
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
/*     */ public final class DecisionStack
/*     */ {
/*  29 */   private static final Logger logger = Logger.getLogger(DecisionStack.class.getName());
/*     */   
/*     */   private static final int MAX_ORDERS = 5;
/*     */   
/*  33 */   private final LinkedList<Order> orders = new LinkedList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DecisionStack() {
/*  40 */     if (logger.isLoggable(Level.FINEST))
/*     */     {
/*  42 */       logger.finest("Created new DecisionStack");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearOrders() {
/*  53 */     this.orders.clear();
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
/*     */   public boolean addOrder(Order order) {
/*  66 */     if (mayReceiveOrders() && order != null) {
/*     */       
/*  68 */       this.orders.addLast(order);
/*  69 */       return true;
/*     */     } 
/*     */ 
/*     */     
/*  73 */     return false;
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
/*     */   public boolean removeOrder(Order order) {
/*  87 */     if (order != null)
/*     */     {
/*  89 */       return this.orders.remove(order);
/*     */     }
/*     */ 
/*     */     
/*  93 */     logger.warning("Tried to remove a null Order from " + this);
/*  94 */     return false;
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
/*     */   public Order getFirst() {
/* 106 */     return this.orders.getFirst();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasOrders() {
/* 117 */     return !this.orders.isEmpty();
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
/*     */   public boolean mayReceiveOrders() {
/* 129 */     return (this.orders.size() < 5);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getNumberOfOrders() {
/* 139 */     return this.orders.size();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\ai\DecisionStack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */