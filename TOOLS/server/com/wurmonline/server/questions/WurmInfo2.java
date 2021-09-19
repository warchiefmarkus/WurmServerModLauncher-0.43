/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import java.util.Properties;
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
/*     */ public final class WurmInfo2
/*     */   extends Question
/*     */ {
/*     */   public WurmInfo2(Creature aResponder) {
/*  33 */     super(aResponder, "Cooking changelog", "Change log v1", 15, -10L);
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
/*     */   public void answer(Properties answers) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/*  55 */     StringBuilder buf = new StringBuilder();
/*  56 */     buf.append(getBmlHeader());
/*     */     
/*  58 */     buf.append("label{text=\"\"}");
/*  59 */     buf.append("label{type=\"bold\";text=\"General\"}");
/*  60 */     buf.append("label{text=\" * I've keep some of the ways that HFC was used for skill increases. But no junk food, \"}");
/*  61 */     buf.append("label{text=\"   e.g. nails are not used as an ingredient and if present in a container, they will stop the \"}");
/*  62 */     buf.append("label{text=\"   food item being made.\"}");
/*  63 */     buf.append("label{text=\" * Meats now have a material, e.g. Meat, Dragon and Meat, Game\"}");
/*  64 */     buf.append("label{text=\"    o As you can see it does not use the animal type, but a category as we have so \"}");
/*  65 */     buf.append("label{text=\"      many animal types. Some of the categories are: (there are 16 total)\"}");
/*  66 */     buf.append("label{text=\"       . Dragon\"}");
/*  67 */     buf.append("label{text=\"       . Game\"}");
/*  68 */     buf.append("label{text=\"       . Human\"}");
/*  69 */     buf.append("label{text=\"       . Humanoid\"}");
/*  70 */     buf.append("label{text=\"       . Snake\"}");
/*  71 */     buf.append("label{text=\" * Meat and fillets can now be put in FSB and Crates.\"}");
/*  72 */     buf.append("label{text=\" * Fish and fish fillets can now be put in FSB and Crates.\"}");
/*  73 */     buf.append("label{text=\" * Existing Zombified milk will lose its zombie status. New zombie milk should be fine.\"}");
/*  74 */     buf.append("label{text=\" * You will be able to seal some containers so long as they only have one liquid in them, \"}");
/*  75 */     buf.append("label{text=\"   this will stop their decay.\"}");
/*  76 */     buf.append("label{text=\"    o Small and Large Amphoria.\"}");
/*  77 */     buf.append("label{text=\"    o Pottery Jar.\"}");
/*  78 */     buf.append("label{text=\"    o Pottery Flask.\"}");
/*  79 */     buf.append("label{text=\"    o Water Skin.\"}");
/*  80 */     buf.append("label{text=\"    o Small Barrel.\"}");
/*  81 */     buf.append("harray{label{text=\" * \"};label{type=\"bold\";text=\"Bees\"};label{text=\" have been added.\"}}");
/*     */ 
/*     */     
/*  84 */     buf.append("harray{label{text=\" * Cooking will now be from \"};label{type=\"bold\";text=\"recipes\"};label{text=\", this does not mean that you cannot continue \"}}");
/*     */ 
/*     */     
/*  87 */     buf.append("label{text=\"   cooking like you used to though, although some recipes will have changed.\"}");
/*  88 */     buf.append("harray{label{text=\" * A personal \"};label{type=\"bold\";text=\"cookbook\"};label{text=\" is now available which has the recipes that you know about in.\"}}");
/*     */ 
/*     */     
/*  91 */     buf.append("label{text=\" * Hens eggs can now be found when foraging on grass tiles. And you will be able to put\"}");
/*  92 */     buf.append("label{text=\"   in FSB, but that makes them infertile.\"}");
/*  93 */     buf.append("label{text=\" * Old containers and tools - now with more use.\"}");
/*  94 */     buf.append("label{text=\"    o Sauce pan - had to change size of this, but that means some recipes need a \"}");
/*  95 */     buf.append("label{text=\"      new one which is larger.\"}");
/*  96 */     buf.append("label{text=\"    o Pottery bowl - can now be used to hold liquids as well, a lot of recipes use this.\"}");
/*  97 */     buf.append("label{text=\"    o Hand - used to make some mixes, and other things. Note if a recipe says a \"}");
/*  98 */     buf.append("label{text=\"      hand must be used, actually any active item will work.\"}");
/*  99 */     buf.append("label{text=\"    o Fork - how else were you going to mix some stuff!\"}");
/* 100 */     buf.append("label{text=\"    o Knife - used a lot in food preparation.\"}");
/* 101 */     buf.append("label{text=\"    o Spoon - An alternative way to mix things (so same ingredients can be used in \"}");
/* 102 */     buf.append("label{text=\"      multiple recipes. Also can be used to scoop.\"}");
/* 103 */     buf.append("label{text=\"    o Press - can be used to squash something.\"}");
/* 104 */     buf.append("label{text=\"    o Branch - branching out, could be used as a spit ...\"}");
/* 105 */     buf.append("label{text=\" * New containers and tools\"}");
/* 106 */     buf.append("label{text=\"    o Stoneware. - used to make things like breads, biscuits etc\"}");
/* 107 */     buf.append("label{text=\"    o Cake tin - used to make cakes\"}");
/* 108 */     buf.append("label{text=\"    o Pie dish - used to make pies and tarts\"}");
/* 109 */     buf.append("label{text=\"    o Roasting dish - used to roast food.\"}");
/* 110 */     buf.append("label{text=\"    o Plate - used to make salads and sandwiches on.\"}");
/* 111 */     buf.append("label{text=\"    o Mortar+Pestle - used to grind small things (e.g. spices)\"}");
/* 112 */     buf.append("label{text=\"    o Measuring Jug - used to get a specific amount of liquid, its volume can be \"}");
/* 113 */     buf.append("label{text=\"      adjusted (volume is same as weight for this).\"}");
/* 114 */     buf.append("label{text=\"    o Still - used for distilling.\"}");
/* 115 */     buf.append("label{text=\" * New crops\"}");
/* 116 */     buf.append("label{text=\"    o Carrots\"}");
/* 117 */     buf.append("label{text=\"    o Cabbage\"}");
/* 118 */     buf.append("label{text=\"    o Tomatos\"}");
/* 119 */     buf.append("label{text=\"    o Sugar Beet\"}");
/* 120 */     buf.append("label{text=\"    o Lettuce\"}");
/* 121 */     buf.append("label{text=\"    o Peas\"}");
/* 122 */     buf.append("label{text=\"    o Cucumbers\"}");
/* 123 */     buf.append("label{text=\" * New Bush\"}");
/* 124 */     buf.append("label{text=\"    o Hazelnut bush - now you know where the hazelnuts come from.\"}");
/* 125 */     buf.append("label{text=\" * New Tree\"}");
/* 126 */     buf.append("label{text=\"    o Orange tree - because it seemed like a good idea.\"}");
/* 127 */     buf.append("harray{label{text=\" * Spices - all can be planted in a \"};label{type=\"bold\";text=\"planter\"}label{text=\", except Nutmeg.\"}}");
/*     */ 
/*     */     
/* 130 */     buf.append("label{text=\"    o Cumin\"}");
/* 131 */     buf.append("label{text=\"    o Ginger\"}");
/* 132 */     buf.append("label{text=\"    o Paprika\"}");
/* 133 */     buf.append("label{text=\"    o Turmeric\"}");
/* 134 */     buf.append("harray{label{text=\" * New Herbs - all Herbs can be planted in a \"};label{type=\"bold\";text=\"planter\"};}");
/*     */     
/* 136 */     buf.append("label{text=\"    o Fennel\"}");
/* 137 */     buf.append("label{text=\"    o Mint\"}");
/* 138 */     buf.append("label{text=\" * New items that are only found by forage / botanize. Note all above spices and herbs \"}");
/* 139 */     buf.append("label{text=\"   and the new vegs can be found this way as well)\"}");
/* 140 */     buf.append("label{text=\"    o Cocoa bean\"}");
/* 141 */     buf.append("label{text=\"    o Nutmeg (note this is a spice but cannot be planted in a planter)\"}");
/* 142 */     buf.append("label{text=\"    o Raspberries\"}");
/* 143 */     buf.append("label{text=\"    o Hazelnut sprout.\"}");
/* 144 */     buf.append("label{text=\"    o Orange sprout.\"}");
/* 145 */     buf.append("label{text=\" * Rocksalt\"}");
/* 146 */     buf.append("label{text=\"    o Rock tiles that would of produced salt when mining will now be shown as \"}");
/* 147 */     buf.append("label{text=\"      Rocksalt veins (this may take a day or two to show), but have a limited life (e.g. \"}");
/* 148 */     buf.append("label{text=\"      you get 45-50 rocksalt from one).\"}");
/* 149 */     buf.append("label{text=\"    o The Rocksalt can then be ground into salt using a grindstone. You can get\"}");
/* 150 */     buf.append("label{text=\"      more than one salt from each Rocksalt bepending on your milling skill.\"}");
/* 151 */     buf.append("label{text=\"    o Veins that had salt in, will be unaffected, e.g.you will still be able to get the \"}");
/* 152 */     buf.append("label{text=\"      random salt when mining them.\"}");
/* 153 */     buf.append("label{text=\" * Trellis\"}");
/* 154 */     buf.append("label{text=\"    o A new trellis has been added for hops.\"}");
/* 155 */     buf.append("label{text=\"    o Trellis can now be harvested when their produce is in season (except ivy ones \"}");
/* 156 */     buf.append("label{text=\"      don't have a season).\"}");
/* 157 */     buf.append("label{text=\"    o To help plant your trellis in nice straight lines, you can plant them using a wall, \"}");
/* 158 */     buf.append("label{text=\"      fence or tile border. And have three options, on left, center and on right.\"}");
/* 159 */     buf.append("label{text=\"    o There is a limit of 4 planted trellis per tile. Any extras that are currently planted \"}");
/* 160 */     buf.append("label{text=\"      on same tile will become unplanted.\"}");
/* 161 */     buf.append("label{text=\" * Flowers\"}");
/* 162 */     buf.append("label{text=\"    o Flowers can now be used in some recipes, and therefore will now only go into \"}");
/* 163 */     buf.append("label{text=\"      a food storage bin. This also applies to rose petals, oleander, lavender and \"}");
/* 164 */     buf.append("label{text=\"      camellia.\"}");
/* 165 */     buf.append("label{text=\"    o Any existing flowers in bulk storage bins are fine, you can still take them out,\"}");
/* 166 */     buf.append("label{text=\"      but will not be able to put them back into the bulk storage bin, but they will go \"}");
/* 167 */     buf.append("label{text=\"      into the food storage bin.\"}");
/* 168 */     buf.append("label{text=\" * The goodness of food\"}");
/* 169 */     buf.append("label{text=\"    o Each meal made will have a bonus attached to it, so the same ingredients \"}");
/* 170 */     buf.append("label{text=\"      making the same meal (in same cooker and same container) will end up with \"}");
/* 171 */     buf.append("label{text=\"      this same bonus.\"}");
/* 172 */     buf.append("label{text=\"       . This bonus will give a timed affinity to a skill, but can be different per \"}");
/* 173 */     buf.append("label{text=\"         player, e.g. fish and chips may give a temp weaponsmithing affinity to \"}");
/* 174 */     buf.append("label{text=\"         Joe, but to Tom it gives carpentry, (also may not give it to any skill).\"}");
/* 175 */     buf.append("label{text=\"    o Nutrition has not been changed.\"}");
/*     */     
/* 177 */     buf.append("label{type=\"bold\";text=\"Bees\"}");
/* 178 */     buf.append("label{text=\" * Wild bee hives will appear in spring at random locations and they will vanish at the end \"}");
/* 179 */     buf.append("label{text=\"   of the year (in winter). Note they will be in different locations each year.\"}");
/* 180 */     buf.append("label{text=\" * As time passes honey will be made in hives together with bees wax, the amount will\"}");
/* 181 */     buf.append("label{text=\"   depend on nearby flowers, fields and trees.\"}");
/* 182 */     buf.append("label{text=\" * Each wild hive will start with one queen bee, this may increase by one every wurm \"}");
/* 183 */     buf.append("label{text=\"   month, to a maximum of two, so long as the hive has over a certain amount of honey in \"}");
/* 184 */     buf.append("label{text=\"   it. When there is two queen bees if there is a domestic hive nearby it may migrate to it.\"}");
/* 185 */     buf.append("label{text=\" * Domestic hives will be loadable. Even with a queen in it. So you can move it to \"}");
/* 186 */     buf.append("label{text=\"   somewhere, e.g. your own deed. Watch out bees sting!\"}");
/* 187 */     buf.append("label{text=\" * Domestic hives that had a queen in it, will go dormant over the winter period and will \"}");
/* 188 */     buf.append("label{text=\"   become active again in spring. But it is possible for the queen to die over winter if no \"}");
/* 189 */     buf.append("label{text=\"   honey is left in the hive (Note can put sugar in hive to keep the queen alive.\"}");
/* 190 */     buf.append("label{text=\" * Honey ( and beeswax) will be collectable from hives.. But you may need a bee \"}");
/* 191 */     buf.append("label{text=\"   smoker.. So bees do not sting you, note that this bee smoker is useful for other times, \"}");
/* 192 */     buf.append("label{text=\"   like if you want to chop down a tree that has a hive, or load/unload a domestic hive \"}");
/* 193 */     buf.append("label{text=\"   which has a queen in it.\"}");
/*     */     
/* 195 */     buf.append("label{type=\"bold\";text=\"Recipes\"}");
/* 196 */     buf.append("label{text=\" * As well as being able to examine a food container to see what it will make, you can \"}");
/* 197 */     buf.append("harray{label{text=\"   also use \"};label{type=\"bold\";text=\"LORE\"};label{text=\", to get hints on what ingredient you could add into the container to be \"}}");
/*     */ 
/*     */     
/* 200 */     buf.append("label{text=\"   able to make something.\"}");
/* 201 */     buf.append("label{text=\" * Some more specialised recipes will call for a meat of a specific category, or a specific \"}");
/* 202 */     buf.append("label{text=\"   fish, but most will use any meat or any fish or even any veg.\"}");
/* 203 */     buf.append("label{text=\" * Most new recipes only require one of each item, main exception is making sandwiches \"}");
/* 204 */     buf.append("label{text=\"   which normally requires 2 slices of bread.\"}");
/* 205 */     buf.append("label{text=\" * Some recipes are an intermediate step, or some sauce which is used later e.g. there is \"}");
/* 206 */     buf.append("label{text=\"   cake mix and white sauce.\"}");
/* 207 */     buf.append("label{text=\" * Lots of new food category types e.g.\"}");
/* 208 */     buf.append("label{text=\"    o Curry\"}");
/* 209 */     buf.append("label{text=\"    o Pizza\"}");
/* 210 */     buf.append("label{text=\"    o Cookies\"}");
/* 211 */     buf.append("label{text=\"    o Pie\"}");
/* 212 */     buf.append("label{text=\"    o Tarts\"}");
/* 213 */     buf.append("label{text=\"    o Biscuits\"}");
/* 214 */     buf.append("label{text=\"    o Scones\"}");
/* 215 */     buf.append("label{text=\"    o Salads\"}");
/* 216 */     buf.append("label{text=\" * And some of your old favorites like.\"}");
/* 217 */     buf.append("label{text=\"    o Cakes\"}");
/* 218 */     buf.append("label{text=\"    o Sandwiches\"}");
/* 219 */     buf.append("label{text=\"    o Tea\"}");
/* 220 */     buf.append("label{text=\"    o Wine\"}");
/* 221 */     buf.append("label{text=\"    o Meal\"}");
/* 222 */     buf.append("label{text=\" * And there are some new drinks which will need distilling.\"}");
/* 223 */     buf.append("label{text=\" * Note you will need to experiment to find their recipes, but do note some items need a \"}");
/* 224 */     buf.append("label{text=\"   mix before adding other items, e.g. you will now need a cake mix to make cakes.\"}");
/* 225 */     buf.append("label{text=\" * Some ingredients will only be found doing forage/botanize actions, whilst others, once \"}");
/* 226 */     buf.append("harray{label{text=\"   found, they may be able to be planted as a \"};label{type=\"bold\";text=\"crop\"};label{text=\" or even in a \"};label{type=\"bold\";text=\"planter\"};label{text=\" (e.g. most spices \"}}");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 231 */     buf.append("label{text=\"   and herbs can be planted in a planter).\"}");
/* 232 */     buf.append("label{text=\"    o Fresh is an attribute of an item when just found from foraging or picking, if you \"}");
/* 233 */     buf.append("label{text=\"      put it in a FSB it looses that attribute.\"}");
/* 234 */     buf.append("label{text=\" * Some recipes are nameable this means that whoever is the first to make the item for \"}");
/* 235 */     buf.append("label{text=\"   that recipe, will have their name added to the front of that recipe name, e.g. if Pifa was \"}");
/* 236 */     buf.append("label{text=\"   first to make a meat curry (assuming that was nameable) then it would show to \"}");
/* 237 */     buf.append("label{text=\"   everyone, when they discover it,  as ''Pifa's meat curry''.\"}");
/* 238 */     buf.append("label{text=\" * Note only one recipe can be named per person.\"}");
/* 239 */     buf.append("label{text=\" * Some recipes may only be makeable once you have that recipe in your cookbook,\"}");
/* 240 */     buf.append("label{text=\"   these recipes are only available from killing certain creatures.\"}");
/* 241 */     buf.append("label{text=\" * Recipes can be inscribed onto papryus (or paper), to do this you need to be looking at\"}");
/* 242 */     buf.append("label{text=\"   the recipe in your cookbook, and then use the reed pen on a blank papryus (or paper).\"}");
/* 243 */     buf.append("label{text=\"    o You can then mail these or trade them to others, where they can add it to their\"}");
/* 244 */     buf.append("label{text=\"      cookbook, if they don't know it, by either reading the recipe and selecting to add\"}");
/* 245 */     buf.append("label{text=\"      to their cookbook or activate it and r-click on the cookbook menu option.\"}");
/*     */     
/* 247 */     buf.append("label{type=\"bold\";text=\"Planter\"}");
/* 248 */     buf.append("label{text=\" * Items can be planted in a planter, e.g. a herb or a spice (not all spices).\"}");
/* 249 */     buf.append("label{text=\" * The planted item will start growing.\"}");
/* 250 */     buf.append("label{text=\" * After a while it will be available to be harvested.\"}");
/* 251 */     buf.append("label{text=\" * Harvesting will be available daily,\"}");
/* 252 */     buf.append("label{text=\" * Each time you harvest it will prolong its life\"}");
/* 253 */     buf.append("label{text=\" * Eventually it will get too woody to be harvested, then it is time to start afresh.\"}");
/*     */     
/* 255 */     buf.append("label{type=\"bold\";text=\"LORE\"}");
/* 256 */     buf.append("label{text=\"Using LORE on a container, will let you know what could be made, e.g.\"}");
/* 257 */     buf.append("label{text=\" * If the contents match a known recipe (known by you that is). You would get a message \"}");
/* 258 */     buf.append("label{text=\"   like:\"}");
/* 259 */     buf.append("label{text=\"    o 'The ingredients in the frying pan would make an omlette when cooked'.\"}");
/* 260 */     buf.append("label{text=\" * If the contents match an unknown recipe. Message would be like: \"}");
/* 261 */     buf.append("label{text=\"    o 'You think this may well work when cooked'.\"}");
/* 262 */     buf.append("label{text=\" * If the contents would make any recipe but has the incorrect amount of a liquid then \"}");
/* 263 */     buf.append("label{text=\"   you would get something like:\"}");
/* 264 */     buf.append("label{text=\"    o 'The ingredients in the saucepan would make tea when cooked, but...'\"}");
/* 265 */     buf.append("label{text=\"      'There is too much water, try between 300g and 400g.'\"}");
/* 266 */     buf.append("label{text=\" * Partial Matches\"}");
/* 267 */     buf.append("label{text=\"    o It performs checks in this order \"}");
/* 268 */     buf.append("label{text=\"       . Unknown recipe that is not nameable.\"}");
/* 269 */     buf.append("label{text=\"       . Unknown recipe that is nameable.\"}");
/* 270 */     buf.append("label{text=\"       . Known recipe that is not nameable.\"}");
/* 271 */     buf.append("label{text=\"       . Known recipe that is nameable.\"}");
/* 272 */     buf.append("label{text=\"    o If the contents form part of any recipe, it will give a hint as to what to add to \"}");
/* 273 */     buf.append("label{text=\"      make that recipe. E.g. 'have you tried adding a chopped onion?'.\"}");
/* 274 */     buf.append("label{text=\"       . Note the recipe is picked at random from a list of possible recipes and \"}");
/* 275 */     buf.append("label{text=\"         so is the shown ingredient.\"}");
/*     */     
/* 277 */     buf.append("label{type=\"bold\";text=\"Cookbook\"}");
/* 278 */     buf.append("label{text=\" * Every person has a cookbook, where your known recipes are shown.\"}");
/* 279 */     buf.append("label{text=\" * Some recipes are known by everyone by default, you have to find the others and \"}");
/* 280 */     buf.append("label{text=\"   make them for them to appear in your cookbook..\"}");
/* 281 */     buf.append("label{text=\" * The initial page of your cookbook allows you to select what recipes to view, i.e. \"}");
/* 282 */     buf.append("label{text=\"    o Target action - these are the ones where you use one item on another, e.g. \"}");
/* 283 */     buf.append("label{text=\"      grinding cereals to make flour.\"}");
/* 284 */     buf.append("label{text=\"    o Container action - these ones are when you use a tool of some kind on a \"}");
/* 285 */     buf.append("label{text=\"      container to change the contents of the container into a different item. E.g. \"}");
/* 286 */     buf.append("label{text=\"      using your hand  on a pottery bowl which containers flour, water, salt and butter \"}");
/* 287 */     buf.append("label{text=\"      to make pastry.\"}");
/* 288 */     buf.append("label{text=\"    o Heat - these ones are your basic cooking recipes, where you put ingredients \"}");
/* 289 */     buf.append("label{text=\"      into a food container, and put in an cooker and after the ingredients get hot the \"}");
/* 290 */     buf.append("label{text=\"      container items change to the result, e.g. putting maple sap into a saucepan in \"}");
/* 291 */     buf.append("label{text=\"      a lit oven, will result in maple syrup after sometime. Not all recipes work in all \"}");
/* 292 */     buf.append("label{text=\"      cookers.\"}");
/* 293 */     buf.append("label{text=\"    o Time - these ones are used for brewing.\"}");
/* 294 */     buf.append("label{text=\" * Also on the initial page you also have links to view recipes (that you know) by\"}");
/* 295 */     buf.append("label{text=\"    o Tool - this gives a list of the tools that you know are used for cooking, selecting \"}");
/* 296 */     buf.append("label{text=\"      a tool from that list will give you the known recipes that can be made from it.\"}");
/* 297 */     buf.append("label{text=\"    o Cooker - this will give a list of cookers, and selecting a cooker from that list will \"}");
/* 298 */     buf.append("label{text=\"      lead you to a list of known recipes that can be made in it\"}");
/* 299 */     buf.append("label{text=\"    o Container - this will give you a list of containers that can be used by known \"}");
/* 300 */     buf.append("label{text=\"      recipes, selecting one will then give a list of the known recipes that use that \"}");
/* 301 */     buf.append("label{text=\"      container.\"}");
/* 302 */     buf.append("label{text=\"    o Ingredients - gives a list of all your known ingredients, and again selecting one \"}");
/* 303 */     buf.append("label{text=\"      of them will then show a list of known recipes that use that ingredient.\"}");
/* 304 */     buf.append("label{text=\" * Also you can search your recipes.\"}");
/* 305 */     buf.append("label{text=\" * From any list of recipes, you can select one and view what you think is used to make \"}");
/* 306 */     buf.append("label{text=\"   that item\"}");
/* 307 */     buf.append("label{text=\"    o Note that there are optional ingredients, and unless you have used them for an \"}");
/* 308 */     buf.append("label{text=\"      ingredient, then they will not show in your version.\"}");
/* 309 */     buf.append("label{text=\"    o Note that some recipes may use any type of meat, or fish, or veg, or herb, or \"}");
/* 310 */     buf.append("label{text=\"      spice, when you attempt the same recipe with a different type, your recipe will \"}");
/* 311 */     buf.append("label{text=\"      be updated to show that information, e.g. if your recipe says that it uses beef \"}");
/* 312 */     buf.append("label{text=\"      meat, and you try with canine meat, then if it works, the recipe will update \"}");
/* 313 */     buf.append("label{text=\"      to show any meat. \"}");
/* 314 */     buf.append("label{text=\"    o Note that not all recipes can use all types.\"}");
/*     */     
/* 316 */     buf.append(createAnswerButton2());
/* 317 */     getResponder().getCommunicator().sendBml(480, 500, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getInfo() {
/* 327 */     return "";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\WurmInfo2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */