/*    */ package com.wurmonline.server.items;
/*    */ 
/*    */ import com.wurmonline.server.MiscConstants;
/*    */ import com.wurmonline.server.economy.MonetaryConstants;
/*    */ import com.wurmonline.shared.constants.ModelConstants;
/*    */ import java.io.IOException;
/*    */ import java.util.logging.Logger;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ItemTemplateCreatorKingdom
/*    */   extends ItemTemplateCreator
/*    */   implements MonetaryConstants, MiscConstants, ModelConstants, ItemTypes
/*    */ {
/* 36 */   private static final Logger logger = Logger.getLogger(ItemTemplateCreatorKingdom.class.getName());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void initializeTemplates() throws IOException {
/* 46 */     createItemTemplate(384, "guard tower", "towers", "excellent", "good", "ok", "poor", "A high guard tower.", new short[] { 52, 25, 31, 67, 44, 85, 86, 49, 98, 123, 194, 239 }, (short)60, (short)1, 0, 19353600L, 400, 400, 600, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.guardtower.jenn.", 20.0F, 500000, (byte)15);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 53 */     createItemTemplate(430, "guard tower", "towers", "excellent", "good", "ok", "poor", "A high guard tower.", new short[] { 52, 25, 31, 67, 44, 85, 86, 49, 98, 123, 194, 239 }, (short)60, (short)1, 0, 19353600L, 400, 400, 600, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.guardtower.hots.", 20.0F, 500000, (byte)15);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 60 */     createItemTemplate(528, "guard tower", "towers", "excellent", "good", "ok", "poor", "A high guard tower.", new short[] { 52, 25, 31, 67, 44, 85, 86, 49, 98, 123, 194, 239 }, (short)60, (short)1, 0, 19353600L, 400, 400, 600, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.guardtower.molr.", 20.0F, 500000, (byte)15);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 68 */     createItemTemplate(638, "guard tower", "towers", "excellent", "good", "ok", "poor", "A high guard tower.", new short[] { 52, 25, 31, 67, 44, 85, 86, 49, 98, 123, 194, 239 }, (short)60, (short)1, 0, 19353600L, 400, 400, 600, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.guardtower.free.", 20.0F, 500000, (byte)15);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 78 */     createItemTemplate(1431, "kingdom stone", "kingdom stones", "excellent", "good", "ok", "poor", "A stone that can be claimed for your kingdom to apply a buff.", new short[] { 108, 25, 31, 123, 52, 40, 98, 49, 109 }, (short)60, (short)1, 0, Long.MAX_VALUE, 200, 250, 400, -10, EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.portal.10.", 25.0F, 30000, (byte)15, 10000, true);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\ItemTemplateCreatorKingdom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */