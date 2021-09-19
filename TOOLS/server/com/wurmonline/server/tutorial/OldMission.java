/*     */ package com.wurmonline.server.tutorial;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public final class OldMission
/*     */   implements MiscConstants
/*     */ {
/*  33 */   private static final Logger logger = Logger.getLogger(OldMission.class.getName());
/*     */   
/*     */   public final int number;
/*  36 */   public String title = "";
/*  37 */   public String missionDescription = "";
/*  38 */   public String missionDescription2 = "";
/*  39 */   public String missionDescription3 = "";
/*     */   public boolean setNewbieItemByte = false;
/*  41 */   public String doneString = "";
/*     */   
/*  43 */   public String checkBoxString = "";
/*     */   
/*  45 */   public int itemTemplateRewardId = -1;
/*  46 */   public int itemTemplateRewardNumbers = 1;
/*  47 */   public float itemTemplateRewardQL = 10.0F;
/*     */   public static final int FINISHED = 9999;
/*  49 */   private static final Map<Integer, OldMission> wlmissions = new HashMap<>();
/*  50 */   private static final Map<Integer, OldMission> blmissions = new HashMap<>();
/*     */ 
/*     */   
/*     */   static {
/*  54 */     createMissions();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasCheckBox() {
/*  59 */     return !this.checkBoxString.equals("");
/*     */   }
/*     */ 
/*     */   
/*     */   private OldMission(int _number, byte kingdom) {
/*  64 */     this.number = _number;
/*  65 */     if (kingdom == 3) {
/*  66 */       blmissions.put(Integer.valueOf(this.number), this);
/*     */     } else {
/*  68 */       wlmissions.put(Integer.valueOf(this.number), this);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private OldMission(int aNumber, byte aKingdom, String aTitle, String aMissionDescriptionOne, String aMissionDescriptionTwo, String aMissionDescriptionThree, String aCheckBoxString, int aItemTemplateRewardId, int aItemTemplateRewardNumbers, float aItemTemplateRewardQL, boolean aSetNewbieItemByte) {
/*  76 */     this.number = aNumber;
/*  77 */     if (aKingdom == 3) {
/*  78 */       blmissions.put(Integer.valueOf(this.number), this);
/*     */     } else {
/*  80 */       wlmissions.put(Integer.valueOf(this.number), this);
/*  81 */     }  this.title = aTitle;
/*  82 */     this.missionDescription = aMissionDescriptionOne;
/*  83 */     this.missionDescription2 = aMissionDescriptionTwo;
/*  84 */     this.missionDescription3 = aMissionDescriptionThree;
/*  85 */     this.checkBoxString = aCheckBoxString;
/*  86 */     this.itemTemplateRewardId = aItemTemplateRewardId;
/*  87 */     this.itemTemplateRewardNumbers = aItemTemplateRewardNumbers;
/*  88 */     this.itemTemplateRewardQL = aItemTemplateRewardQL;
/*  89 */     this.setNewbieItemByte = aSetNewbieItemByte;
/*     */   }
/*     */ 
/*     */   
/*     */   public static OldMission getMission(int num, byte kingdom) {
/*  94 */     if (kingdom == 3) {
/*  95 */       return blmissions.get(Integer.valueOf(num));
/*     */     }
/*  97 */     return wlmissions.get(Integer.valueOf(num));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void createMissions() {
/* 105 */     long start = System.currentTimeMillis();
/*     */     
/* 107 */     OldMission mj0 = new OldMission(0, (byte)1);
/* 108 */     mj0.title = "Initial instructions";
/* 109 */     mj0.missionDescription = "Welcome to these lands. We are at war, I have little time and you have a lot to learn in order to survive.";
/* 110 */     mj0.missionDescription2 = "I suggest we get you started immediately.";
/* 111 */     mj0.missionDescription3 = "Please press F2 to check your skills, and F3 to look at your inventory. Also press c in order to see your character window.";
/* 112 */     mj0.checkBoxString = "I have done that now";
/*     */     
/* 114 */     OldMission ml0 = new OldMission(0, (byte)3);
/* 115 */     ml0.title = "Initial instructions";
/* 116 */     ml0.missionDescription = "Stop fooling around! We are at war, I have little time and you have a lot to learn in order to survive.";
/* 117 */     ml0.missionDescription2 = "We need to get you started immediately.";
/* 118 */     ml0.missionDescription3 = "Press F2 to check your skills, and F3 to look at your inventory. Also press c in order to see your character window.";
/* 119 */     ml0.checkBoxString = "I have done that now";
/*     */     
/* 121 */     OldMission mj1 = new OldMission(1, (byte)1);
/* 122 */     mj1.title = "Equipping";
/* 123 */     mj1.missionDescription = "Okay, click the hatchet in the inventory window. Notice how it becomes 'selected' at the bottom of the inventory window.";
/* 124 */     mj1.checkBoxString = "I have done that now";
/*     */     
/* 126 */     OldMission ml1 = new OldMission(1, (byte)3);
/* 127 */     ml1.title = "Equipping";
/* 128 */     ml1.missionDescription = "Open your inventory window for gods sake and click the hatchet. It should become 'selected' at the bottom of the inventory window.";
/* 129 */     ml1.checkBoxString = "I have done that now";
/*     */     
/* 131 */     OldMission mj2 = new OldMission(2, (byte)1);
/* 132 */     mj2.title = "Wearing";
/* 133 */     mj2.missionDescription = "You should wear the shield on your left arm.";
/* 134 */     mj2.missionDescription2 = "Put the shield in the Shield slot. Good against dangerous creatures. Make sure you wield your sword.";
/*     */     
/* 136 */     mj2.checkBoxString = "Ok that worked";
/*     */     
/* 138 */     OldMission ml2 = new OldMission(2, (byte)3);
/* 139 */     ml2.title = "Wearing";
/* 140 */     ml2.missionDescription = "You should wear the shield on your left arm.";
/* 141 */     ml2.missionDescription2 = "Pput the shield in the Shield slot. Dangerous creatures or the enemy may show up. Make sure you wield your sword.";
/*     */     
/* 143 */     ml2.checkBoxString = "Yes, master";
/*     */     
/* 145 */     OldMission mj3 = new OldMission(3, (byte)1);
/* 146 */     mj3.title = "Cutting wood";
/* 147 */     mj3.missionDescription = "You must learn how to gather resources. Select your hatchet in your inventory by double-clicking it and go find a tree outside this village.";
/* 148 */     mj3.missionDescription2 = "Right-click the tree and cut it down.";
/* 149 */     mj3.missionDescription3 = "Then chop the tree up and bring some wood.";
/* 150 */     mj3.doneString = "Take the wood by right-clicking it and selecting Take and bring it back with you.";
/*     */     
/* 152 */     OldMission ml3 = new OldMission(3, (byte)3);
/* 153 */     ml3.title = "Cutting wood";
/* 154 */     ml3.missionDescription = "Time to stop loitering! Now go find a tree outside this village.";
/* 155 */     ml3.missionDescription2 = "Right-click the tree and cut it down.";
/* 156 */     ml3.missionDescription3 = "Then chop the tree up and bring some wood.";
/* 157 */     ml3.doneString = "Take the wood by right-clicking it and selecting Take and bring it back with you.";
/*     */     
/* 159 */     OldMission mj4 = new OldMission(4, (byte)1);
/* 160 */     mj4.title = "Creating kindling";
/* 161 */     mj4.missionDescription = "In order to make kindling, use an axe, a saw or a knife and right-click a log.";
/*     */     
/* 163 */     OldMission ml4 = new OldMission(4, (byte)3);
/* 164 */     ml4.title = "Creating kindling";
/* 165 */     ml4.missionDescription = "In order to make kindling, use an axe, a saw or a knife and right-click a log.";
/*     */     
/* 167 */     OldMission m = new OldMission(5, (byte)1);
/* 168 */     m.title = "Lighting a fire";
/* 169 */     m.missionDescription = "Now use the flint and steel on the kindling in order to light it.";
/*     */     
/* 171 */     m = new OldMission(5, (byte)3);
/* 172 */     m.title = "Lighting a fire";
/* 173 */     m.missionDescription = "Use the flint and steel on the kindling in order to light it.";
/*     */     
/* 175 */     m = new OldMission(6, (byte)1);
/* 176 */     m.title = "Gathering food";
/* 177 */     m.missionDescription = "In order to keep well fed you may of course kill animals or farm crops.";
/* 178 */     m.missionDescription2 = "The easiest way to find food is however to look on grass tiles for it.";
/* 179 */     m.missionDescription3 = "Go out and forage now. Right-click on green grass tiles.";
/*     */     
/* 181 */     m = new OldMission(6, (byte)3);
/* 182 */     m.title = "Gathering food";
/* 183 */     m.missionDescription = "The mycelium that grows all around is a blessing.";
/* 184 */     m.missionDescription2 = "If you stand on it it will fill you up. You may also absorb mycelium in order to heal your wounds.";
/* 185 */     m.checkBoxString = "Understood";
/*     */     
/* 187 */     m = new OldMission(7, (byte)1);
/* 188 */     m.title = "The bartender";
/* 189 */     m.missionDescription = "There is a bartender around here somewhere. Go find him and ask for refreshments.";
/* 190 */     m.missionDescription2 = "You will receive free refreshments the first 24 hours here.";
/* 191 */     m.missionDescription3 = "This means that you have a good reason to stay closeby and start learning things.";
/*     */     
/* 193 */     m = new OldMission(7, (byte)3);
/* 194 */     m.title = "The bartender";
/* 195 */     m.missionDescription = "There is a bartender around here somewhere. Go find him and ask for refreshments.";
/* 196 */     m.missionDescription2 = "You will receive free refreshments the first 24 hours here.";
/* 197 */     m.missionDescription3 = "This means that you have a good reason to stay closeby and start learning things.";
/*     */     
/* 199 */     m = new OldMission(8, (byte)1);
/* 200 */     m.title = "Digging";
/* 201 */     m.missionDescription = "A very common way to find resources like clay is to use a shovel and dig.";
/* 202 */     m.missionDescription2 = "You also need to flatten land in order to build on it.";
/* 203 */     m.missionDescription3 = "Go out and dig some dirt now. Drop the dirt before returning since it is pretty heavy to carry around.";
/*     */     
/* 205 */     m = new OldMission(8, (byte)3);
/* 206 */     m.title = "Digging";
/* 207 */     m.missionDescription = "A very common way to find resources like clay is to use a shovel and dig.";
/* 208 */     m.missionDescription2 = "You also need to flatten land in order to build on it.";
/* 209 */     m.missionDescription3 = "Go out and dig some dirt now. Drop the dirt before returning since it is pretty heavy to carry around.";
/*     */     
/* 211 */     m = new OldMission(9, (byte)1);
/* 212 */     m.title = "Planks";
/* 213 */     m.missionDescription = "With the saw you have you may create planks. I want you to try one now.";
/* 214 */     m.missionDescription2 = "When that is done all you need are some nails and you can start building a house.";
/*     */     
/* 216 */     m = new OldMission(9, (byte)3);
/* 217 */     m.title = "Planks";
/* 218 */     m.missionDescription = "With the saw you have you may create planks. Try one now.";
/* 219 */     m.missionDescription2 = "When that is done all you need are some nails and you can start building a house.";
/*     */     
/* 221 */     m = new OldMission(10, (byte)1);
/* 222 */     m.title = "Mining";
/* 223 */     m.missionDescription = "Now go use the pickaxe on some rock to get the feel of mining. Select tunnel which eventually will open up into the mountain.";
/* 224 */     m.missionDescription2 = "You may find valuable ore inside a mountain, and even precious gems.";
/* 225 */     m.missionDescription3 = "If you find iron ore, you may smelt it in a fire and use the iron to create an anvil and nails.";
/* 226 */     m.itemTemplateRewardId = 59;
/* 227 */     m.itemTemplateRewardNumbers = 1;
/* 228 */     m.itemTemplateRewardQL = 10.0F;
/* 229 */     m.setNewbieItemByte = false;
/*     */     
/* 231 */     m = new OldMission(10, (byte)3);
/* 232 */     m.title = "Mining";
/* 233 */     m.missionDescription = "Now go use the pickaxe on some rock to get the feel of mining. Select tunnel which eventually will open up into the mountain.";
/* 234 */     m.missionDescription2 = "You may find valuable ore inside a mountain, and even precious gems.";
/* 235 */     m.missionDescription3 = "If you find iron ore, you may smelt it in a fire and use the iron to create an anvil nails.";
/* 236 */     m.itemTemplateRewardId = 59;
/* 237 */     m.itemTemplateRewardNumbers = 1;
/* 238 */     m.itemTemplateRewardQL = 10.0F;
/* 239 */     m.setNewbieItemByte = false;
/*     */     
/* 241 */     m = new OldMission(11, (byte)1);
/* 242 */     m.title = "Final words";
/* 243 */     m.missionDescription = "That's all. A few final advice:";
/* 244 */     m.missionDescription2 = "Try to find a bed when logging off. If you sleep long enough you will be invigorated and receive a bonus for a while when you return.";
/* 245 */     m.missionDescription3 = "When it comes to fighting.. I can't teach you that. Initially, just do as much as possible. Standing still doing nothing is usually less effective. Good luck!";
/* 246 */     m.checkBoxString = "Thank you, I am done here.";
/*     */     
/* 248 */     m = new OldMission(11, (byte)3);
/* 249 */     m.title = "Final words";
/* 250 */     m.missionDescription = "That's all. A few final advice:";
/* 251 */     m.missionDescription2 = "Try to find a bed when logging off. If you sleep long enough you will be invigorated and receive a bonus for a while when you return.";
/* 252 */     m.missionDescription3 = "When it comes to fighting.. I can't teach you that. Initially, just do as much as possible. Standing still doing nothing is usually less effective. Stay alive!";
/* 253 */     m.checkBoxString = "I am ready to venture into the darkness!";
/*     */     
/* 255 */     OldMission ml9999 = new OldMission(9999, (byte)3);
/* 256 */     ml9999.title = "Continuing the instructions";
/* 257 */     ml9999.missionDescription = "Will you continue to follow my instructions?";
/* 258 */     ml9999.checkBoxString = "Yes";
/*     */     
/* 260 */     OldMission mj9999 = new OldMission(9999, (byte)1);
/* 261 */     mj9999.title = "Continuing the instructions";
/* 262 */     mj9999.missionDescription = "Have you come for more instructions?";
/* 263 */     mj9999.checkBoxString = "Yes";
/*     */     
/* 265 */     logger.info("Finished creating " + wlmissions.size() + " WL and " + blmissions.size() + " BL Tutorial Missions, that took " + (
/* 266 */         System.currentTimeMillis() - start) + " ms");
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\tutorial\OldMission.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */