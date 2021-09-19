/*    */ package com.wurmonline.server.bodys;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.shared.exceptions.WurmServerException;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ public final class BodyFactory
/*    */ {
/* 28 */   private static final Map<Byte, BodyTemplate> bodyTemplates = new HashMap<>();
/*    */ 
/*    */   
/*    */   static {
/* 32 */     bodyTemplates.put(Byte.valueOf((byte)0), new BodyHuman());
/* 33 */     bodyTemplates.put(Byte.valueOf((byte)3), new BodyDog());
/* 34 */     bodyTemplates.put(Byte.valueOf((byte)1), new BodyHorse());
/* 35 */     bodyTemplates.put(Byte.valueOf((byte)4), new BodyEttin());
/* 36 */     bodyTemplates.put(Byte.valueOf((byte)5), new BodyCyclops());
/* 37 */     bodyTemplates.put(Byte.valueOf((byte)2), new BodyBear());
/* 38 */     bodyTemplates.put(Byte.valueOf((byte)6), new BodyDragon());
/* 39 */     bodyTemplates.put(Byte.valueOf((byte)7), new BodyBird());
/* 40 */     bodyTemplates.put(Byte.valueOf((byte)8), new BodySpider());
/* 41 */     bodyTemplates.put(Byte.valueOf((byte)9), new BodySnake());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Body getBody(Creature creature, byte typ, short centimetersHigh, short centimetersLong, short centimetersWide) throws Exception {
/* 53 */     BodyTemplate template = bodyTemplates.get(Byte.valueOf(typ));
/*    */     
/* 55 */     if (template != null)
/*    */     {
/* 57 */       return new Body(template, creature, centimetersHigh, centimetersLong, centimetersWide);
/*    */     }
/*    */     
/* 60 */     throw new WurmServerException("No such bodytype: " + Byte.toString(typ));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\bodys\BodyFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */