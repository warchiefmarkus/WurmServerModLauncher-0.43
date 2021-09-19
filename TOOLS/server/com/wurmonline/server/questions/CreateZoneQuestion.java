/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Constants;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.behaviours.Terraforming;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.structures.DbFence;
/*     */ import com.wurmonline.server.structures.Fence;
/*     */ import com.wurmonline.server.structures.Structures;
/*     */ import com.wurmonline.server.utils.BMLBuilder;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.zones.FocusZone;
/*     */ import com.wurmonline.server.zones.NoSuchZoneException;
/*     */ import com.wurmonline.server.zones.Zone;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import com.wurmonline.shared.constants.StructureConstantsEnum;
/*     */ import java.io.IOException;
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
/*     */ 
/*     */ public class CreateZoneQuestion
/*     */   extends Question
/*     */   implements TimeConstants
/*     */ {
/*  53 */   private static final Logger logger = Logger.getLogger(CreateZoneQuestion.class.getName());
/*     */   private FocusZone[] allZones;
/*  55 */   final float minDirtDist = 1.0F;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CreateZoneQuestion(Creature aResponder) {
/*  66 */     super(aResponder, "Creating a special zone", "Select the type and boundaries of the special zone", 98, aResponder
/*  67 */         .getWurmId());
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
/*  78 */     String sizex = answers.getProperty("sizex");
/*  79 */     String sizey = answers.getProperty("sizey");
/*  80 */     String typeS = answers.getProperty("typedd");
/*  81 */     String zname = answers.getProperty("zname");
/*  82 */     String zdesc = answers.getProperty("zdesc");
/*  83 */     String delzone = answers.getProperty("deldd");
/*  84 */     int sx = 0;
/*  85 */     int sy = 0;
/*  86 */     byte zoneType = 0;
/*     */     
/*     */     try {
/*  89 */       if (typeS != null && typeS.length() > 0) {
/*     */         
/*  91 */         int index = Integer.parseInt(typeS);
/*  92 */         if (index == 0) {
/*  93 */           zoneType = 2;
/*  94 */         } else if (index == 1) {
/*  95 */           zoneType = 3;
/*  96 */         } else if (index == 2) {
/*  97 */           zoneType = 4;
/*  98 */         } else if (index == 3) {
/*  99 */           zoneType = 5;
/* 100 */         } else if (index == 4) {
/* 101 */           zoneType = 6;
/* 102 */         } else if (index == 6) {
/* 103 */           zoneType = 8;
/* 104 */         } else if (index == 7) {
/* 105 */           zoneType = 15;
/* 106 */         } else if (index == 8) {
/* 107 */           zoneType = 9;
/* 108 */         } else if (index == 9) {
/* 109 */           zoneType = 10;
/* 110 */         } else if (index == 10) {
/* 111 */           zoneType = 11;
/* 112 */         } else if (index == 11) {
/* 113 */           zoneType = 12;
/* 114 */         } else if (index == 12) {
/* 115 */           zoneType = 13;
/* 116 */         } else if (index == 13) {
/* 117 */           zoneType = 14;
/* 118 */         } else if (index == 14) {
/* 119 */           zoneType = 16;
/* 120 */         } else if (index == 15) {
/* 121 */           zoneType = 17;
/* 122 */         } else if (index == 16) {
/* 123 */           zoneType = 18;
/*     */         } 
/*     */       } 
/* 126 */       if (zoneType != 0) {
/*     */         
/* 128 */         if (zoneType == 6) {
/*     */           
/* 130 */           if (getResponder().getPower() < 4) {
/*     */             
/* 132 */             getResponder().getCommunicator().sendNormalServerMessage("Only Arch angels+ may create Hota zones since these have extended functionality.");
/*     */             
/*     */             return;
/*     */           } 
/* 136 */           if (FocusZone.getHotaZone() != null) {
/*     */             
/* 138 */             getResponder().getCommunicator().sendNormalServerMessage("There is already a Hota PvP zone on this server.");
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 143 */         if (sizex != null && sizex.length() > 0)
/* 144 */           sx = Integer.parseInt(sizex); 
/* 145 */         if (sizey != null && sizey.length() > 0)
/* 146 */           sy = Integer.parseInt(sizey); 
/* 147 */         if (zname != null && zname.length() > 2) {
/*     */           
/* 149 */           if (zoneType < 7 || zoneType == 11 || zoneType == 12 || zoneType == 14 || zoneType == 16 || zoneType == 17 || zoneType == 18) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 157 */             FocusZone fz = new FocusZone(Zones.safeTileX(getResponder().getTileX() - sx), Zones.safeTileX(getResponder().getTileX() + sx), Zones.safeTileY(getResponder().getTileY() - sy), Zones.safeTileY(getResponder().getTileY() + sy), zoneType, zname, zdesc, true);
/*     */             
/* 159 */             getResponder().getCommunicator().sendNormalServerMessage("Created the zone " + zname + " XY:" + fz
/* 160 */                 .getStartX() + "," + fz.getStartY() + " to " + fz
/* 161 */                 .getEndX() + "," + fz.getEndY());
/* 162 */             if (zoneType == 6)
/*     */             {
/* 164 */               Servers.localServer.setNextHota(System.currentTimeMillis() + (
/* 165 */                   Servers.isThisATestServer() ? 300000L : 115200000L));
/*     */             }
/*     */           }
/*     */           else {
/*     */             
/* 170 */             int stx = Zones.safeTileX(getResponder().getTileX() - sx);
/* 171 */             int endtx = Zones.safeTileX(getResponder().getTileX() + sx);
/* 172 */             int sty = Zones.safeTileY(getResponder().getTileY() - sy);
/* 173 */             int endty = Zones.safeTileY(getResponder().getTileY() + sy);
/* 174 */             if (zoneType == 8) {
/*     */               
/* 176 */               if (getResponder().getPower() < 4) {
/*     */                 
/* 178 */                 logger.severe(getResponder().getName() + " has attempted to use Focus Zone Flatten DIRT with power level " + 
/*     */                     
/* 180 */                     getResponder().getPower() + "! Needs " + '\004' + "at least!");
/*     */                 
/*     */                 return;
/*     */               } 
/* 184 */               if (Constants.devmode) getResponder().getCommunicator().sendAlertServerMessage("Made flatten zone. Player standing on [" + 
/* 185 */                     getResponder().getTileX() + ", " + getResponder().getTileY() + "]. Creating zones with StartX " + stx + ", EndX " + endtx + ", StartY " + sty + ", EndY " + endty);
/*     */ 
/*     */               
/* 188 */               Terraforming.flattenImmediately(getResponder(), stx, endtx, sty, endty, 1.0F, 0, false);
/*     */             }
/* 190 */             else if (zoneType == 15) {
/*     */               
/* 192 */               if (getResponder().getPower() < 4) {
/*     */                 
/* 194 */                 logger.severe(getResponder().getName() + " has attempted to use Focus Zone Flatten ROCK with power level " + 
/*     */                     
/* 196 */                     getResponder().getPower() + "! Needs " + '\004' + "at least!");
/*     */                 
/*     */                 return;
/*     */               } 
/* 200 */               if (Constants.devmode) getResponder().getCommunicator().sendAlertServerMessage("Made flatten zone. Player standing on [" + 
/* 201 */                     getResponder().getTileX() + ", " + getResponder().getTileY() + "]. Creating zones with StartX " + stx + ", EndX " + endtx + ", StartY " + sty + ", EndY " + endty);
/*     */ 
/*     */               
/* 204 */               Terraforming.flattenImmediately(getResponder(), stx, endtx, sty, endty, 1.0F, 0, true);
/*     */             }
/* 206 */             else if (zoneType == 9) {
/*     */               
/* 208 */               Structures.createRandomStructure(getResponder(), stx, endtx, sty, endty, getResponder().getTileX(), 
/* 209 */                   getResponder().getTileY(), (byte)14, zname);
/*     */             
/*     */             }
/* 212 */             else if (zoneType == 10) {
/*     */               
/* 214 */               Structures.createRandomStructure(getResponder(), stx, endtx, sty, endty, getResponder().getTileX(), 
/* 215 */                   getResponder().getTileY(), (byte)15, zname);
/*     */             }
/* 217 */             else if (zoneType == 13) {
/*     */               
/* 219 */               if (getResponder().getCurrentVillage() != null) {
/*     */                 
/* 221 */                 Village v = getResponder().getCurrentVillage();
/* 222 */                 int xa = Zones.safeTileX(v.getStartX() + sx);
/* 223 */                 int xe = Zones.safeTileX(v.getEndX() - sx + 1);
/* 224 */                 int ya = Zones.safeTileY(v.getStartY() + sy);
/* 225 */                 int ye = Zones.safeTileY(v.getEndY() - sy + 1);
/* 226 */                 for (int x = xa; x <= xe; x++) {
/*     */                   
/* 228 */                   for (int y = ya; y <= ye; y++) {
/*     */                     
/* 230 */                     if (x == xa || x == xe) {
/*     */                       
/* 232 */                       if (Tiles.decodeHeight(Server.surfaceMesh.getTile(x, y)) > 0) {
/*     */                         
/*     */                         try {
/* 235 */                           Zone zone = Zones.getZone(x, y, true);
/*     */                           
/* 237 */                           DbFence dbFence = new DbFence(StructureConstantsEnum.FENCE_STONEWALL_HIGH, x, y, 0, 1.0F, Tiles.TileBorderDirection.DIR_DOWN, zone.getId(), 0);
/* 238 */                           dbFence.setState(dbFence.getFinishState());
/* 239 */                           dbFence.setQualityLevel(80.0F);
/* 240 */                           dbFence.improveOrigQualityLevel(80.0F);
/* 241 */                           dbFence.save();
/* 242 */                           zone.addFence((Fence)dbFence);
/*     */                         }
/* 244 */                         catch (NoSuchZoneException nsz) {
/*     */                           
/* 246 */                           getResponder().getCommunicator().sendAlertServerMessage("Failed to create fence due to a server exception? - No Zone at " + x + "," + y);
/*     */ 
/*     */ 
/*     */                         
/*     */                         }
/* 251 */                         catch (IOException iox) {
/*     */                           
/* 253 */                           logger.log(Level.WARNING, iox.getMessage(), iox);
/*     */                         } 
/*     */                       }
/* 256 */                     } else if (y == ya || y == ye) {
/*     */                       
/* 258 */                       if (Tiles.decodeHeight(Server.surfaceMesh.getTile(x, y)) > 0) {
/*     */                         
/*     */                         try {
/* 261 */                           Zone zone = Zones.getZone(x, y, true);
/*     */                           
/* 263 */                           DbFence dbFence = new DbFence(StructureConstantsEnum.FENCE_STONEWALL_HIGH, x, y, 0, 1.0F, Tiles.TileBorderDirection.DIR_HORIZ, zone.getId(), 0);
/* 264 */                           dbFence.setState(dbFence.getFinishState());
/* 265 */                           dbFence.setQualityLevel(80.0F);
/* 266 */                           dbFence.improveOrigQualityLevel(80.0F);
/* 267 */                           dbFence.save();
/* 268 */                           zone.addFence((Fence)dbFence);
/*     */                         }
/* 270 */                         catch (NoSuchZoneException nsz) {
/*     */                           
/* 272 */                           getResponder().getCommunicator().sendAlertServerMessage("Failed to create fence due to a server exception? - No Zone at " + x + "," + y);
/*     */ 
/*     */ 
/*     */                         
/*     */                         }
/* 277 */                         catch (IOException iox) {
/*     */                           
/* 279 */                           logger.log(Level.WARNING, iox.getMessage(), iox);
/*     */                         } 
/*     */                       }
/*     */                     } 
/*     */                   } 
/*     */                 } 
/*     */               } else {
/* 286 */                 getResponder().getCommunicator().sendNormalServerMessage("You're not in a village, fool!");
/*     */               }
/*     */             
/*     */             } 
/*     */           } 
/*     */         } else {
/*     */           
/* 293 */           getResponder().getCommunicator().sendNormalServerMessage("The name must be at least 3 characters.");
/*     */         }
/*     */       
/*     */       } 
/* 297 */     } catch (NumberFormatException nfe) {
/*     */       
/* 299 */       getResponder().getCommunicator().sendNormalServerMessage("The values were incorrect.");
/*     */     } 
/*     */     
/*     */     try {
/* 303 */       if (delzone != null && delzone.length() > 0) {
/*     */         
/* 305 */         int index = Integer.parseInt(delzone);
/* 306 */         if (index > 0) {
/*     */           
/*     */           try {
/* 309 */             this.allZones[index - 1].delete();
/* 310 */             getResponder().getCommunicator().sendNormalServerMessage("Deleted the " + this.allZones[index - 1]
/* 311 */                 .getName() + " zone.");
/*     */           }
/* 313 */           catch (IOException ex) {
/*     */             
/* 315 */             logger.log(Level.INFO, ex.getMessage(), ex);
/*     */           } 
/*     */         }
/*     */       } 
/* 319 */     } catch (NumberFormatException nfe) {
/*     */       
/* 321 */       logger.log(Level.INFO, nfe.getMessage());
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
/* 333 */     this.allZones = FocusZone.getAllZones();
/* 334 */     String[] zoneNames = new String[this.allZones.length + 1];
/* 335 */     zoneNames[0] = "None";
/* 336 */     for (int i = 0; i < this.allZones.length; i++) {
/* 337 */       zoneNames[i + 1] = this.allZones[i].getName();
/*     */     }
/* 339 */     BMLBuilder bml = BMLBuilder.createNormalWindow(Integer.toString(getId()), "Manage Focus Zones", 
/* 340 */         BMLBuilder.createGenericBuilder()
/* 341 */         .addLabel("What type of zone do you wish to create?")
/* 342 */         .addDropdown("typedd", "17", new String[] { 
/*     */             "PvP", "Name", "Name with Popup", "Non-PvP", "HOTA (Arch+)", "Battlecamp", "Flatten Dirt (Arch+)", "Flatten Rock (Arch+)", "Wooden House", "Stone House", 
/*     */             "Premium Only Spawn", "No Build", "Tall Walls", "Fog", "Replenish Dirt", "Replenish Trees", "Replenish Ores", "None"
/* 345 */           }).addText("")
/* 346 */         .addLabel("What dimensions should the zone have?")
/* 347 */         .addString(BMLBuilder.createHorizArrayNode(false)
/* 348 */           .addInput("sizex", "0", 4, 1)
/* 349 */           .addLabel("East/West Distance from Center (You)")
/* 350 */           .toString())
/* 351 */         .addString(BMLBuilder.createHorizArrayNode(false)
/* 352 */           .addInput("sizey", "0", 4, 1)
/* 353 */           .addLabel("North/South Distance from Center (You)")
/* 354 */           .toString())
/* 355 */         .addText("")
/* 356 */         .addText("A type Flatten will flatten the ground to your level (even if flying!) by raising dirt, lowering rock to 10.0 dirt below the dirt level if it is higher.")
/*     */         
/* 358 */         .addText("A House type will create a house around you, complete with random windows.")
/* 359 */         .addText("")
/* 360 */         .addLabel("Zone Name:")
/* 361 */         .addInput("zname", "", 30, 1)
/* 362 */         .addLabel("Description (Used for Popups):")
/* 363 */         .addInput("zdesc", "", 1000, 1)
/* 364 */         .addText("")
/* 365 */         .addLabel("Delete Zone:")
/* 366 */         .addDropdown("deldd", "0", zoneNames)
/* 367 */         .addText("")
/* 368 */         .addButton("submit", "Send", null, null, null, true));
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
/* 429 */     getResponder().getCommunicator().sendBml(400, 390, true, true, bml.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\CreateZoneQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */