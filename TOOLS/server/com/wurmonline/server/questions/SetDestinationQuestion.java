/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.ServerEntry;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.behaviours.Seat;
/*     */ import com.wurmonline.server.behaviours.Vehicle;
/*     */ import com.wurmonline.server.behaviours.Vehicles;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import java.util.Properties;
/*     */ 
/*     */ public class SetDestinationQuestion
/*     */   extends Question
/*     */   implements TimeConstants
/*     */ {
/*     */   private static final int WIDTH = 350;
/*     */   private static final int HEIGHT = 250;
/*     */   private static final boolean RESIZEABLE = true;
/*     */   private static final boolean CLOSEABLE = true;
/*  25 */   private static final int[] RGB = new int[] { 200, 200, 200 };
/*     */   
/*     */   private static final String key = "dest";
/*     */   
/*     */   private static final String title = "Plot a course";
/*     */   
/*     */   private static final String question = "Plot a course for your boat:";
/*     */   
/*     */   private static final int CLEAR = 65536;
/*     */   private static final String cyan = "66,200,200";
/*     */   private static final String orange = "255,156,66";
/*     */   private static final String red = "255,66,66";
/*     */   private static final String white = "255,255,255";
/*     */   private Vehicle vehicle;
/*     */   
/*     */   public SetDestinationQuestion(Creature aResponder, Item aTarget) {
/*  41 */     super(aResponder, "Plot a course", "Plot a course for your boat:", 130, aTarget.getWurmId());
/*  42 */     if (aTarget.isBoat()) {
/*  43 */       this.vehicle = Vehicles.getVehicle(aTarget);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  50 */     if (!getResponder().isVehicleCommander() || getResponder().getVehicle() == -10L) {
/*     */       
/*  52 */       getResponder().getCommunicator()
/*  53 */         .sendNormalServerMessage("You must be embarked as the commander of a boat to plot a course. Try dragging the boat inland before embarking again.");
/*     */       return;
/*     */     } 
/*  56 */     String val = answers.getProperty("dest");
/*  57 */     if (val != null) {
/*     */       
/*  59 */       int serverId = Integer.parseInt(val);
/*  60 */       if (serverId == 65536) {
/*     */         
/*  62 */         if (this.vehicle.hasDestinationSet()) {
/*     */           
/*  64 */           this.vehicle.clearDestination();
/*  65 */           getResponder().getCommunicator().sendNormalServerMessage("This boat no longer has a course plotted.");
/*  66 */           alertPassengers();
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/*  71 */         getResponder().getCommunicator().sendNormalServerMessage("You decide not to plot a course.");
/*     */         
/*     */         return;
/*     */       } 
/*  75 */       if (this.vehicle.hasDestinationSet() && serverId == this.vehicle.getDestinationServer().getId()) {
/*     */         
/*  77 */         getResponder().getCommunicator().sendNormalServerMessage("You decide to keep your course set to " + this.vehicle
/*  78 */             .getDestinationServer().getName() + ".");
/*     */         return;
/*     */       } 
/*  81 */       ServerEntry entry = Servers.getServerWithId(serverId);
/*  82 */       if (entry != null) {
/*     */         
/*  84 */         if (Servers.isAvailableDestination(getResponder(), entry)) {
/*     */           
/*  86 */           this.vehicle.setDestination(entry);
/*  87 */           getResponder().getCommunicator()
/*  88 */             .sendNormalServerMessage("You plot a course to " + entry.getName() + ".");
/*  89 */           this.vehicle.checkPassengerPermissions(getResponder());
/*  90 */           alertPassengers();
/*  91 */           if (!entry.EPIC || Server.getInstance().isPS()) {
/*     */             
/*  93 */             this.vehicle.alertPassengersOfKingdom(entry, true);
/*  94 */             if ((entry.PVPSERVER && !Servers.localServer.PVPSERVER) || entry.isChaosServer()) {
/*  95 */               this.vehicle.alertAllPassengersOfEnemies(entry);
/*     */             }
/*     */           } 
/*     */         } else {
/*  99 */           getResponder().getCommunicator().sendNormalServerMessage("The waters between here and " + entry.getName() + " are too rough to navigate.");
/*     */         } 
/*     */       } else {
/* 102 */         getResponder().getCommunicator().sendNormalServerMessage("You decide to not plot a course.");
/*     */       } 
/*     */     } else {
/* 105 */       getResponder().getCommunicator().sendNormalServerMessage("You decide to not plot a course.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/* 111 */     if (this.vehicle == null) {
/*     */       return;
/*     */     }
/* 114 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/* 115 */     ServerEntry[] servers = Servers.getDestinations(getResponder());
/* 116 */     long cooldown = this.vehicle.getPlotCourseCooldowns();
/* 117 */     boolean isPvPBlocking = this.vehicle.isPvPBlocking();
/*     */     
/* 119 */     if (Servers.localServer.PVPSERVER) {
/*     */       
/* 121 */       String restriction = this.vehicle.checkCourseRestrictions();
/* 122 */       if (restriction != "") {
/*     */         
/* 124 */         buf.append("label{type='bold'; color='255,156,66'; text='Course Restrictions'};");
/* 125 */         buf.append("text{text='" + restriction + "'};");
/* 126 */         buf.append("text{text=''};");
/* 127 */         buf.append(createAnswerButton2());
/* 128 */         getResponder().getCommunicator().sendBml(350, 250 + servers.length * 20, true, true, buf.toString(), RGB[0], RGB[1], RGB[2], "Plot a course");
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 133 */     if (this.vehicle.hasDestinationSet()) {
/*     */       
/* 135 */       String color = ((this.vehicle.getDestinationServer()).PVPSERVER || this.vehicle.getDestinationServer().isChaosServer()) ? "255,66,66" : "66,200,200";
/*     */ 
/*     */ 
/*     */       
/* 139 */       String name = ((this.vehicle.getDestinationServer()).PVPSERVER || this.vehicle.getDestinationServer().isChaosServer()) ? (this.vehicle.getDestinationServer().getName() + " [PvP]") : this.vehicle.getDestinationServer().getName();
/* 140 */       buf.append("harray{label{type='bold'; color='255,255,255'; text='Current destination: '};");
/* 141 */       buf.append("label{color='" + color + "'; text='" + name + "'}}");
/*     */     } 
/* 143 */     buf.append("text{text=''};");
/* 144 */     if (servers.length == 0 || (servers.length == 1 && servers[0] == Servers.localServer)) {
/* 145 */       buf.append("text{text='There are no available destinations.'};");
/*     */     } else {
/*     */       
/* 148 */       for (ServerEntry lServer : servers) {
/*     */         
/* 150 */         if (lServer != Servers.localServer)
/*     */         {
/* 152 */           if (!lServer.LOGINSERVER || Server.getInstance().isPS()) {
/*     */ 
/*     */             
/* 155 */             boolean selected = false;
/* 156 */             if (this.vehicle.hasDestinationSet() && this.vehicle.getDestinationServer() == lServer)
/* 157 */               selected = true; 
/* 158 */             if (lServer.PVPSERVER || lServer.isChaosServer())
/*     */             
/* 160 */             { if (isPvPBlocking) {
/* 161 */                 buf.append("label{color='255,66,66' text='" + lServer.getName() + " [PvP] (PvP travel blocked)'};");
/* 162 */               } else if (cooldown > 0L) {
/* 163 */                 buf.append("label{color='255,66,66' text='" + lServer.getName() + " [PvP] (Available in " + 
/* 164 */                     Server.getTimeFor(cooldown) + ")'};");
/*     */               } else {
/* 166 */                 buf.append(createRadioWithLabel("dest", String.valueOf(lServer.getId()), lServer.getName() + " [PvP]", "255,66,66", selected));
/*     */               }  }
/*     */             else
/* 169 */             { buf.append(createRadioWithLabel("dest", String.valueOf(lServer.getId()), lServer.getName(), "66,200,200", selected)); } 
/*     */           }  } 
/* 171 */       }  if (this.vehicle.hasDestinationSet()) {
/* 172 */         buf.append(createRadioWithLabel("dest", String.valueOf(65536), "Clear destination", "255,255,255", false));
/*     */       } else {
/* 174 */         buf.append(createRadioWithLabel("dest", String.valueOf(65536), "No destination", "255,255,255", true));
/*     */       } 
/* 176 */       buf.append("text{text=''};");
/* 177 */       buf.append("text{text='Plotting a course will send you to that server when you sail across any border of " + Servers.localServer
/* 178 */           .getName() + ".'};");
/* 179 */       buf.append("text{text=''};");
/* 180 */       buf.append("text{text='You will appear on the opposite side of the selected server. For example, if you cross the northern border, you will appear on the southern side of the server you have selected.'};");
/*     */ 
/*     */       
/* 183 */       if (isPvPBlocking) {
/*     */         
/* 185 */         buf.append("text{text=''};");
/* 186 */         buf.append("text{text='You or a passenger has PvP travel blocked. This option can be toggled in the Profile.'};");
/*     */       } 
/*     */     } 
/* 189 */     buf.append("text{text=''};");
/* 190 */     buf.append(createAnswerButton2());
/* 191 */     getResponder().getCommunicator().sendBml(350, 250 + servers.length * 20, true, true, buf.toString(), RGB[0], RGB[1], RGB[2], "Plot a course");
/*     */   }
/*     */ 
/*     */   
/*     */   private String createRadioWithLabel(String group, String id, String message, String color, boolean selected) {
/* 196 */     String toReturn = "harray{radio{group='" + group + "'; id='" + id + "'; selected='" + selected + "'}";
/* 197 */     toReturn = toReturn + "label{color='" + color + "'; text='" + message + "'}}";
/* 198 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   private void alertPassengers() {
/* 203 */     if (this.vehicle.seats != null)
/*     */     {
/* 205 */       for (Seat lSeat : this.vehicle.seats) {
/*     */         
/* 207 */         if (lSeat.isOccupied() && lSeat != this.vehicle.getPilotSeat())
/*     */           
/*     */           try {
/*     */             
/* 211 */             Player passenger = Players.getInstance().getPlayer(lSeat.getOccupant());
/* 212 */             if (!this.vehicle.hasDestinationSet())
/*     */             {
/* 214 */               passenger.getCommunicator().sendNormalServerMessage(getResponder().getName() + " has cleared the plotted course.");
/*     */             }
/*     */             else
/*     */             {
/* 218 */               ServerEntry entry = this.vehicle.getDestinationServer();
/* 219 */               String msg = getResponder().getName() + " has plotted a course to " + entry.getName();
/* 220 */               if (!Servers.mayEnterServer((Creature)passenger, entry))
/* 221 */                 msg = msg + ", but you will not be able to travel with " + getResponder().getHimHerItString(); 
/* 222 */               passenger.getCommunicator().sendAlertServerMessage(msg + ".");
/*     */             }
/*     */           
/* 225 */           } catch (NoSuchPlayerException noSuchPlayerException) {} 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\SetDestinationQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */