/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.Features;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.Creatures;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.creatures.Wagoner;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.players.PermissionsPlayerList;
/*     */ import com.wurmonline.server.questions.ManageObjectList;
/*     */ import com.wurmonline.server.questions.ManagePermissions;
/*     */ import com.wurmonline.server.questions.PermissionsHistory;
/*     */ import com.wurmonline.server.questions.WagonerDismissQuestion;
/*     */ import com.wurmonline.server.questions.WagonerHistory;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.Nullable;
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
/*     */ final class WagonerContractBehaviour
/*     */   extends ItemBehaviour
/*     */ {
/*  49 */   private static final Logger logger = Logger.getLogger(WagonerContractBehaviour.class.getName());
/*     */ 
/*     */   
/*     */   WagonerContractBehaviour() {
/*  53 */     super((short)59);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item target) {
/*  59 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, target);
/*  60 */     toReturn.addAll(getBehavioursForWagonerContract(performer, null, target));
/*  61 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
/*  67 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, source, target);
/*  68 */     toReturn.addAll(getBehavioursForWagonerContract(performer, source, target));
/*  69 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item target, short action, float counter) {
/*  75 */     boolean[] ans = wagonerContractActions(act, performer, null, target, action, counter);
/*     */     
/*  77 */     if (ans[0]) {
/*  78 */       return ans[1];
/*     */     }
/*  80 */     return super.action(act, performer, target, action, counter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
/*  87 */     boolean[] ans = wagonerContractActions(act, performer, source, target, action, counter);
/*     */     
/*  89 */     if (ans[0]) {
/*  90 */       return ans[1];
/*     */     }
/*  92 */     return super.action(act, performer, source, target, action, counter);
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
/*     */   private List<ActionEntry> getBehavioursForWagonerContract(Creature performer, @Nullable Item source, Item contract) {
/* 105 */     List<ActionEntry> toReturn = new LinkedList<>();
/*     */     
/* 107 */     if (Features.Feature.WAGONER.isEnabled() && contract.getData() != -1L) {
/*     */ 
/*     */       
/* 110 */       Wagoner wagoner = Wagoner.getWagoner(contract.getData());
/* 111 */       if (wagoner != null && wagoner.getVillageId() != -1) {
/*     */         
/* 113 */         List<ActionEntry> waglist = new LinkedList<>();
/* 114 */         waglist.add(new ActionEntry((short)-2, "Permissions", "viewing"));
/* 115 */         waglist.add(Actions.actionEntrys[863]);
/* 116 */         waglist.add(new ActionEntry((short)691, "Permissions History", "viewing"));
/* 117 */         waglist.add(Actions.actionEntrys[919]);
/* 118 */         waglist.add(new ActionEntry((short)566, "Manage chat options", "managing"));
/* 119 */         waglist.add(Actions.actionEntrys[920]);
/* 120 */         if (!waglist.isEmpty()) {
/*     */           
/* 122 */           toReturn.add(new ActionEntry((short)-(waglist.size() - 2), wagoner.getName(), "wagoner"));
/* 123 */           toReturn.addAll(waglist);
/*     */         } 
/* 125 */         if (Servers.isThisATestServer()) {
/*     */           
/* 127 */           List<ActionEntry> testlist = new LinkedList<>();
/* 128 */           if (wagoner.getState() == 0) {
/*     */ 
/*     */             
/* 131 */             testlist.add(new ActionEntry((short)140, "Send to bed", "testing"));
/* 132 */             testlist.add(new ActionEntry((short)111, "Test delivery", "testing"));
/*     */           } 
/* 134 */           if (wagoner.getState() == 2)
/*     */           {
/* 136 */             testlist.add(new ActionEntry((short)30, "Wake up", "testing"));
/*     */           }
/* 138 */           if (wagoner.getState() == 14) {
/*     */ 
/*     */             
/* 141 */             testlist.add(new ActionEntry((short)644, "Force Park", "parking"));
/* 142 */             testlist.add(new ActionEntry((short)636, "Send Home", "parking"));
/*     */           }
/* 144 */           else if (wagoner.getState() == 15) {
/*     */             
/* 146 */             testlist.add(new ActionEntry((short)917, "Cancel driving", "cancelling"));
/*     */           } 
/* 148 */           testlist.add(new ActionEntry((short)185, "Show state", "checking"));
/* 149 */           if (!testlist.isEmpty()) {
/*     */             
/* 151 */             toReturn.add(new ActionEntry((short)-testlist.size(), "Test only", "test only"));
/* 152 */             toReturn.addAll(testlist);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 157 */     return toReturn;
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
/*     */   public boolean[] wagonerContractActions(Action act, Creature performer, @Nullable Item source, Item contract, short action, float counter) {
/* 173 */     if (Features.Feature.WAGONER.isEnabled() && contract.getTemplateId() == 1129 && contract.getData() != -1L) {
/*     */ 
/*     */       
/* 176 */       Wagoner wagoner = Wagoner.getWagoner(contract.getData());
/* 177 */       if (wagoner == null) {
/*     */         
/* 179 */         performer.getCommunicator().sendNormalServerMessage("Cannot find the wagoner associated with this contract.");
/* 180 */         contract.setData(-10L);
/* 181 */         contract.setDescription("");
/* 182 */         return new boolean[] { true, true };
/*     */       } 
/* 184 */       if (wagoner.getVillageId() == -1) {
/*     */         
/* 186 */         performer.getCommunicator().sendNormalServerMessage("Wagoner is in progress of being dismissed..");
/* 187 */         return new boolean[] { true, true };
/*     */       } 
/* 189 */       if (action == 863) {
/*     */         
/* 191 */         ManageObjectList.Type molt = ManageObjectList.Type.WAGONER;
/*     */ 
/*     */         
/*     */         try {
/* 195 */           Creature creature = Creatures.getInstance().getCreature(contract.getData());
/* 196 */           ManagePermissions mp = new ManagePermissions(performer, molt, (PermissionsPlayerList.ISettings)creature, false, -10L, false, null, "");
/* 197 */           mp.sendQuestion();
/*     */         }
/* 199 */         catch (NoSuchCreatureException e) {
/*     */           
/* 201 */           logger.log(Level.WARNING, "Cannot find the wagoner (" + contract.getData() + ") associated with the contract." + e.getMessage(), (Throwable)e);
/* 202 */           performer.getCommunicator().sendNormalServerMessage("Cannot find the wagoner associated with this contract.");
/*     */         } 
/* 204 */         return new boolean[] { true, true };
/*     */       } 
/* 206 */       if (action == 691) {
/*     */         
/* 208 */         PermissionsHistory ph = new PermissionsHistory(performer, contract.getData());
/* 209 */         ph.sendQuestion();
/* 210 */         return new boolean[] { true, true };
/*     */       } 
/* 212 */       if (action == 919) {
/*     */         
/* 214 */         WagonerHistory wh = new WagonerHistory(performer, wagoner);
/* 215 */         wh.sendQuestion();
/* 216 */         return new boolean[] { true, true };
/*     */       } 
/* 218 */       if (action == 920) {
/*     */         
/* 220 */         WagonerDismissQuestion wdq = new WagonerDismissQuestion(performer, wagoner);
/* 221 */         wdq.sendQuestion();
/* 222 */         return new boolean[] { true, true };
/*     */       } 
/* 224 */       if (action == 140) {
/*     */         
/* 226 */         if (Servers.isThisATestServer())
/*     */         {
/*     */           
/* 229 */           if (wagoner.getState() == 0)
/*     */           {
/* 231 */             wagoner.forceStateChange((byte)1);
/*     */           }
/*     */         }
/* 234 */         return new boolean[] { true, true };
/*     */       } 
/* 236 */       if (action == 111) {
/*     */         
/* 238 */         if (Servers.isThisATestServer())
/*     */         {
/*     */           
/* 241 */           if (wagoner.getState() == 0)
/*     */           {
/* 243 */             wagoner.forceStateChange((byte)4);
/*     */           }
/*     */         }
/* 246 */         return new boolean[] { true, true };
/*     */       } 
/* 248 */       if (action == 30) {
/*     */         
/* 250 */         if (Servers.isThisATestServer())
/*     */         {
/*     */           
/* 253 */           if (wagoner.getState() == 2)
/*     */           {
/* 255 */             wagoner.forceStateChange((byte)3);
/*     */           }
/*     */         }
/* 258 */         return new boolean[] { true, true };
/*     */       } 
/* 260 */       if (action == 917) {
/*     */ 
/*     */         
/* 263 */         if (Servers.isThisATestServer())
/*     */         {
/* 265 */           wagoner.forceStateChange((byte)14);
/*     */         }
/* 267 */         return new boolean[] { true, true };
/*     */       } 
/* 269 */       if (action == 636) {
/*     */ 
/*     */         
/* 272 */         if (Servers.isThisATestServer()) {
/*     */           
/* 274 */           wagoner.setGoalWaystoneId(wagoner.getHomeWaystoneId());
/* 275 */           wagoner.calculateRoute();
/* 276 */           wagoner.forceStateChange((byte)9);
/*     */         } 
/* 278 */         return new boolean[] { true, true };
/*     */       } 
/* 280 */       if (action == 644) {
/*     */ 
/*     */         
/* 283 */         if (Features.Feature.WAGONER.isEnabled()) {
/*     */           
/* 285 */           wagoner.setGoalWaystoneId(wagoner.getHomeWaystoneId());
/* 286 */           wagoner.forceStateChange((byte)10);
/*     */         } 
/* 288 */         return new boolean[] { true, true };
/*     */       } 
/*     */     } 
/* 291 */     return new boolean[] { false, false };
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\WagonerContractBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */