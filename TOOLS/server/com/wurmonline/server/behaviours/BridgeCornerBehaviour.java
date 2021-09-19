/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.Features;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.highways.HighwayPos;
/*     */ import com.wurmonline.server.highways.MethodsHighways;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.structures.BridgePart;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
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
/*     */ final class BridgeCornerBehaviour
/*     */   extends Behaviour
/*     */   implements MiscConstants
/*     */ {
/*     */   BridgeCornerBehaviour() {
/*  40 */     super((short)60);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item source, boolean onSurface, BridgePart bridgePart) {
/*  47 */     List<ActionEntry> toReturn = new LinkedList<>();
/*     */ 
/*     */     
/*  50 */     if (source.isSign() || source.isStreetLamp()) {
/*     */       
/*  52 */       toReturn.add(Actions.actionEntrys[176]);
/*     */     }
/*  54 */     else if (Features.Feature.HIGHWAYS.isEnabled() && source.isRoadMarker()) {
/*     */       
/*  56 */       HighwayPos highwayPos = MethodsHighways.getHighwayPos(bridgePart);
/*  57 */       if (highwayPos != null)
/*     */       {
/*  59 */         if (MethodsHighways.middleOfHighway(highwayPos) && !MethodsHighways.containsMarker(highwayPos, (byte)0)) {
/*     */           
/*  61 */           byte pLinks = MethodsHighways.getPossibleLinksFrom(highwayPos, source);
/*  62 */           if (MethodsHighways.canPlantMarker(null, highwayPos, source, pLinks))
/*  63 */             toReturn.add(new ActionEntry((short)176, "Plant", "planting")); 
/*  64 */           toReturn.add(new ActionEntry((short)759, "View possible protected tiles", "viewing"));
/*  65 */           if (pLinks != 0)
/*  66 */             toReturn.add(new ActionEntry((short)748, "View possible links", "viewing")); 
/*     */         } 
/*     */       }
/*     */     } 
/*  70 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, boolean onSurface, BridgePart bridgePart) {
/*  76 */     List<ActionEntry> toReturn = new LinkedList<>();
/*     */     
/*  78 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item source, boolean onSurface, BridgePart bridgePart, int encodedTile, short action, float counter) {
/*  85 */     boolean done = true;
/*     */     
/*  87 */     if (action == 1) {
/*     */       
/*  89 */       done = action(act, performer, onSurface, bridgePart, encodedTile, action, counter);
/*     */     } else {
/*  91 */       if (action == 176 && (source.isSign() || source.isStreetLamp())) {
/*     */         
/*  93 */         if (performer.getPower() > 0) {
/*  94 */           return MethodsItems.plantSignFinish(performer, source, true, bridgePart.getTileX(), bridgePart.getTileY(), performer.isOnSurface(), performer.getBridgeId(), false, -1L);
/*     */         }
/*  96 */         return MethodsItems.plantSign(performer, source, counter, true, bridgePart.getTileX(), bridgePart.getTileY(), performer.isOnSurface(), performer.getBridgeId(), false, -1L);
/*     */       } 
/*  98 */       if (action == 176 && source.isRoadMarker() && Features.Feature.HIGHWAYS.isEnabled())
/*     */       
/* 100 */       { HighwayPos highwayPos = MethodsHighways.getHighwayPos(bridgePart);
/* 101 */         if (highwayPos != null) {
/*     */           
/* 103 */           if (MethodsHighways.middleOfHighway(highwayPos) && !MethodsHighways.containsMarker(highwayPos, (byte)0))
/*     */           {
/* 105 */             byte pLinks = MethodsHighways.getPossibleLinksFrom(highwayPos, source);
/* 106 */             if (!MethodsHighways.canPlantMarker(performer, highwayPos, source, pLinks)) {
/* 107 */               done = true;
/* 108 */             } else if (performer.getPower() > 0) {
/* 109 */               done = MethodsItems.plantSignFinish(performer, source, true, highwayPos.getTilex(), highwayPos.getTiley(), highwayPos
/* 110 */                   .isOnSurface(), highwayPos.getBridgeId(), false, -1L);
/*     */             } else {
/* 112 */               done = MethodsItems.plantSign(performer, source, counter, true, highwayPos.getTilex(), highwayPos.getTiley(), highwayPos
/* 113 */                   .isOnSurface(), highwayPos.getBridgeId(), false, -1L);
/* 114 */             }  if (done && source.isPlanted())
/*     */             {
/*     */               
/* 117 */               MethodsHighways.autoLink(source, pLinks);
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 122 */             performer.getCommunicator().sendNormalServerMessage("Not a valid tile.");
/* 123 */             return true;
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 128 */           performer.getCommunicator().sendNormalServerMessage("Not a valid tile.");
/* 129 */           return true;
/*     */         }
/*     */          }
/* 132 */       else if (action == 748 && source.isRoadMarker() && Features.Feature.HIGHWAYS.isEnabled())
/*     */       
/* 134 */       { HighwayPos highwayPos = MethodsHighways.getHighwayPos(bridgePart);
/* 135 */         if (highwayPos != null) {
/*     */           
/* 137 */           if (MethodsHighways.middleOfHighway(highwayPos) && !MethodsHighways.containsMarker(highwayPos, (byte)0))
/*     */           {
/* 139 */             done = MarkerBehaviour.showLinks(performer, source, act, counter, highwayPos);
/*     */           }
/*     */           else
/*     */           {
/* 143 */             performer.getCommunicator().sendNormalServerMessage("Not a valid tile.");
/* 144 */             return true;
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 149 */           performer.getCommunicator().sendNormalServerMessage("Not a valid tile.");
/* 150 */           return true;
/*     */         }
/*     */          }
/* 153 */       else if (action == 759 && source.isRoadMarker() && Features.Feature.HIGHWAYS.isEnabled())
/*     */       
/* 155 */       { HighwayPos highwayPos = MethodsHighways.getHighwayPos(bridgePart);
/* 156 */         if (highwayPos != null) {
/*     */           
/* 158 */           if (MethodsHighways.middleOfHighway(highwayPos) && !MethodsHighways.containsMarker(highwayPos, (byte)0))
/*     */           {
/* 160 */             done = MarkerBehaviour.showProtection(performer, source, act, counter, highwayPos);
/*     */           }
/*     */           else
/*     */           {
/* 164 */             performer.getCommunicator().sendNormalServerMessage("Not a valid tile.");
/* 165 */             return true;
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 170 */           performer.getCommunicator().sendNormalServerMessage("Not a valid tile.");
/* 171 */           return true;
/*     */         }  }
/*     */       else
/*     */       
/* 175 */       { return action(act, performer, onSurface, bridgePart, encodedTile, action, counter); } 
/* 176 */     }  return done;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, boolean onSurface, BridgePart bridgePart, int encodedTile, short action, float counter) {
/* 183 */     if (action == 1) {
/*     */       
/* 185 */       HighwayPos highwayPos = MethodsHighways.getHighwayPos(bridgePart);
/* 186 */       if (highwayPos != null && MethodsHighways.middleOfHighway(highwayPos)) {
/* 187 */         performer.getCommunicator().sendNormalServerMessage("This outlines where signs and road markers could be planted.");
/*     */       } else {
/* 189 */         performer.getCommunicator().sendNormalServerMessage("This outlines where signs can be planted.");
/*     */       } 
/* 191 */     }  return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\BridgeCornerBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */