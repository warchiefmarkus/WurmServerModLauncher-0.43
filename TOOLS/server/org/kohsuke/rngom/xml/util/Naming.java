/*     */ package org.kohsuke.rngom.xml.util;
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
/*     */ public class Naming
/*     */ {
/*     */   private static final int CT_NAME = 1;
/*     */   private static final int CT_NMSTRT = 2;
/*     */   private static final String nameStartSingles = ":_ΆΌϚϜϞϠՙەऽলਫ਼ઍઽૠଽஜೞะຄຊຍລວະຽᄀᄉᄼᄾᅀᅌᅎᅐᅙᅣᅥᅧᅩᅵᆞᆨᆫᆺᇫᇰᇹὙὛὝιΩ℮〇";
/*     */   private static final String nameStartRanges = "AZazÀÖØöøÿĀıĴľŁňŊžƀǃǍǰǴǵǺȗɐʨʻˁΈΊΎΡΣώϐϖϢϳЁЌЎяёќўҁҐӄӇӈӋӌӐӫӮӵӸӹԱՖաֆאתװײءغفيٱڷںھۀێېۓۥۦअहक़ॡঅঌএঐওনপরশহড়ঢ়য়ৡৰৱਅਊਏਐਓਨਪਰਲਲ਼ਵਸ਼ਸਹਖ਼ੜੲੴઅઋએઑઓનપરલળવહଅଌଏଐଓନପରଲଳଶହଡ଼ଢ଼ୟୡஅஊஎஐஒகஙசஞடணதநபமவஷஹఅఌఎఐఒనపళవహౠౡಅಌಎಐಒನಪಳವಹೠೡഅഌഎഐഒനപഹൠൡกฮาำเๅກຂງຈດທນຟມຣສຫອຮາຳເໄཀཇཉཀྵႠჅაჶᄂᄃᄅᄇᄋᄌᄎᄒᅔᅕᅟᅡᅭᅮᅲᅳᆮᆯᆷᆸᆼᇂḀẛẠỹἀἕἘἝἠὅὈὍὐὗὟώᾀᾴᾶᾼῂῄῆῌῐΐῖΊῠῬῲῴῶῼKÅↀↂぁゔァヺㄅㄬ가힣一龥〡〩";
/*     */   private static final String nameSingles = "-.़়्ֿٰׄািৗਂ਼ਾਿ઼଼ௗൗัັ༹༵༷༾༿ྗྐྵ゙゚⃡·ːˑ·ـๆໆ々";
/*     */   private static final String nameRanges = "ֹֻֽׁׂًْ֑֣̀҃҆֡ۖۜ͠͡ͅ۝۪ۭ۟۠ۤۧۨँःाौ॑॔ॢॣঁঃীৄেৈো্ৢৣੀੂੇੈੋ੍ੰੱઁઃાૅેૉો્ଁଃାୃେୈୋ୍ୖୗஂஃாூெைொ்ఁఃాౄెైొ్ౕౖಂಃಾೄೆೈೊ್ೕೖംഃാൃെൈൊ്ิฺ็๎ິູົຼ່ໍ྄ཱ༘༙྆ྋྐྕྙྭྱྷ〪〯⃐⃜09٠٩۰۹०९০৯੦੯૦૯୦୯௧௯౦౯೦೯൦൯๐๙໐໙༠༩〱〵ゝゞーヾ";
/*  67 */   private static final byte[][] charTypeTable = new byte[256][]; static { int i;
/*  68 */     for (i = 0; i < "-.़়्ֿٰׄািৗਂ਼ਾਿ઼଼ௗൗัັ༹༵༷༾༿ྗྐྵ゙゚⃡·ːˑ·ـๆໆ々".length(); i++)
/*  69 */       setCharType("-.़়्ֿٰׄািৗਂ਼ਾਿ઼଼ௗൗัັ༹༵༷༾༿ྗྐྵ゙゚⃡·ːˑ·ـๆໆ々".charAt(i), 1); 
/*  70 */     for (i = 0; i < "ֹֻֽׁׂًْ֑֣̀҃҆֡ۖۜ͠͡ͅ۝۪ۭ۟۠ۤۧۨँःाौ॑॔ॢॣঁঃীৄেৈো্ৢৣੀੂੇੈੋ੍ੰੱઁઃાૅેૉો્ଁଃାୃେୈୋ୍ୖୗஂஃாூெைொ்ఁఃాౄెైొ్ౕౖಂಃಾೄೆೈೊ್ೕೖംഃാൃെൈൊ്ิฺ็๎ິູົຼ່ໍ྄ཱ༘༙྆ྋྐྕྙྭྱྷ〪〯⃐⃜09٠٩۰۹०९০৯੦੯૦૯୦୯௧௯౦౯೦೯൦൯๐๙໐໙༠༩〱〵ゝゞーヾ".length(); i += 2)
/*  71 */       setCharType("ֹֻֽׁׂًْ֑֣̀҃҆֡ۖۜ͠͡ͅ۝۪ۭ۟۠ۤۧۨँःाौ॑॔ॢॣঁঃীৄেৈো্ৢৣੀੂੇੈੋ੍ੰੱઁઃાૅેૉો્ଁଃାୃେୈୋ୍ୖୗஂஃாூெைொ்ఁఃాౄెైొ్ౕౖಂಃಾೄೆೈೊ್ೕೖംഃാൃെൈൊ്ิฺ็๎ິູົຼ່ໍ྄ཱ༘༙྆ྋྐྕྙྭྱྷ〪〯⃐⃜09٠٩۰۹०९০৯੦੯૦૯୦୯௧௯౦౯೦೯൦൯๐๙໐໙༠༩〱〵ゝゞーヾ".charAt(i), "ֹֻֽׁׂًْ֑֣̀҃҆֡ۖۜ͠͡ͅ۝۪ۭ۟۠ۤۧۨँःाौ॑॔ॢॣঁঃীৄেৈো্ৢৣੀੂੇੈੋ੍ੰੱઁઃાૅેૉો્ଁଃାୃେୈୋ୍ୖୗஂஃாூெைொ்ఁఃాౄెైొ్ౕౖಂಃಾೄೆೈೊ್ೕೖംഃാൃെൈൊ്ิฺ็๎ິູົຼ່ໍ྄ཱ༘༙྆ྋྐྕྙྭྱྷ〪〯⃐⃜09٠٩۰۹०९০৯੦੯૦૯୦୯௧௯౦౯೦೯൦൯๐๙໐໙༠༩〱〵ゝゞーヾ".charAt(i + 1), 1); 
/*  72 */     for (i = 0; i < ":_ΆΌϚϜϞϠՙەऽলਫ਼ઍઽૠଽஜೞะຄຊຍລວະຽᄀᄉᄼᄾᅀᅌᅎᅐᅙᅣᅥᅧᅩᅵᆞᆨᆫᆺᇫᇰᇹὙὛὝιΩ℮〇".length(); i++)
/*  73 */       setCharType(":_ΆΌϚϜϞϠՙەऽলਫ਼ઍઽૠଽஜೞะຄຊຍລວະຽᄀᄉᄼᄾᅀᅌᅎᅐᅙᅣᅥᅧᅩᅵᆞᆨᆫᆺᇫᇰᇹὙὛὝιΩ℮〇".charAt(i), 2); 
/*  74 */     for (i = 0; i < "AZazÀÖØöøÿĀıĴľŁňŊžƀǃǍǰǴǵǺȗɐʨʻˁΈΊΎΡΣώϐϖϢϳЁЌЎяёќўҁҐӄӇӈӋӌӐӫӮӵӸӹԱՖաֆאתװײءغفيٱڷںھۀێېۓۥۦअहक़ॡঅঌএঐওনপরশহড়ঢ়য়ৡৰৱਅਊਏਐਓਨਪਰਲਲ਼ਵਸ਼ਸਹਖ਼ੜੲੴઅઋએઑઓનપરલળવહଅଌଏଐଓନପରଲଳଶହଡ଼ଢ଼ୟୡஅஊஎஐஒகஙசஞடணதநபமவஷஹఅఌఎఐఒనపళవహౠౡಅಌಎಐಒನಪಳವಹೠೡഅഌഎഐഒനപഹൠൡกฮาำเๅກຂງຈດທນຟມຣສຫອຮາຳເໄཀཇཉཀྵႠჅაჶᄂᄃᄅᄇᄋᄌᄎᄒᅔᅕᅟᅡᅭᅮᅲᅳᆮᆯᆷᆸᆼᇂḀẛẠỹἀἕἘἝἠὅὈὍὐὗὟώᾀᾴᾶᾼῂῄῆῌῐΐῖΊῠῬῲῴῶῼKÅↀↂぁゔァヺㄅㄬ가힣一龥〡〩".length(); i += 2) {
/*  75 */       setCharType("AZazÀÖØöøÿĀıĴľŁňŊžƀǃǍǰǴǵǺȗɐʨʻˁΈΊΎΡΣώϐϖϢϳЁЌЎяёќўҁҐӄӇӈӋӌӐӫӮӵӸӹԱՖաֆאתװײءغفيٱڷںھۀێېۓۥۦअहक़ॡঅঌএঐওনপরশহড়ঢ়য়ৡৰৱਅਊਏਐਓਨਪਰਲਲ਼ਵਸ਼ਸਹਖ਼ੜੲੴઅઋએઑઓનપરલળવહଅଌଏଐଓନପରଲଳଶହଡ଼ଢ଼ୟୡஅஊஎஐஒகஙசஞடணதநபமவஷஹఅఌఎఐఒనపళవహౠౡಅಌಎಐಒನಪಳವಹೠೡഅഌഎഐഒനപഹൠൡกฮาำเๅກຂງຈດທນຟມຣສຫອຮາຳເໄཀཇཉཀྵႠჅაჶᄂᄃᄅᄇᄋᄌᄎᄒᅔᅕᅟᅡᅭᅮᅲᅳᆮᆯᆷᆸᆼᇂḀẛẠỹἀἕἘἝἠὅὈὍὐὗὟώᾀᾴᾶᾼῂῄῆῌῐΐῖΊῠῬῲῴῶῼKÅↀↂぁゔァヺㄅㄬ가힣一龥〡〩".charAt(i), "AZazÀÖØöøÿĀıĴľŁňŊžƀǃǍǰǴǵǺȗɐʨʻˁΈΊΎΡΣώϐϖϢϳЁЌЎяёќўҁҐӄӇӈӋӌӐӫӮӵӸӹԱՖաֆאתװײءغفيٱڷںھۀێېۓۥۦअहक़ॡঅঌএঐওনপরশহড়ঢ়য়ৡৰৱਅਊਏਐਓਨਪਰਲਲ਼ਵਸ਼ਸਹਖ਼ੜੲੴઅઋએઑઓનપરલળવહଅଌଏଐଓନପରଲଳଶହଡ଼ଢ଼ୟୡஅஊஎஐஒகஙசஞடணதநபமவஷஹఅఌఎఐఒనపళవహౠౡಅಌಎಐಒನಪಳವಹೠೡഅഌഎഐഒനപഹൠൡกฮาำเๅກຂງຈດທນຟມຣສຫອຮາຳເໄཀཇཉཀྵႠჅაჶᄂᄃᄅᄇᄋᄌᄎᄒᅔᅕᅟᅡᅭᅮᅲᅳᆮᆯᆷᆸᆼᇂḀẛẠỹἀἕἘἝἠὅὈὍὐὗὟώᾀᾴᾶᾼῂῄῆῌῐΐῖΊῠῬῲῴῶῼKÅↀↂぁゔァヺㄅㄬ가힣一龥〡〩".charAt(i + 1), 2);
/*     */     }
/*  77 */     byte[] other = new byte[256];
/*  78 */     for (int j = 0; j < 256; j++) {
/*  79 */       if (charTypeTable[j] == null)
/*  80 */         charTypeTable[j] = other; 
/*     */     }  }
/*     */   
/*     */   private static void setCharType(char c, int type) {
/*  84 */     int hi = c >> 8;
/*  85 */     if (charTypeTable[hi] == null)
/*  86 */       charTypeTable[hi] = new byte[256]; 
/*  87 */     charTypeTable[hi][c & 0xFF] = (byte)type;
/*     */   }
/*     */   
/*     */   private static void setCharType(char min, char max, int type) {
/*  91 */     byte[] shared = null;
/*     */     
/*  93 */     do { if ((min & 0xFF) != 0)
/*  94 */         continue;  for (; min + 255 <= max; min = (char)(min + 256)) {
/*  95 */         if (shared == null) {
/*  96 */           shared = new byte[256];
/*  97 */           for (int i = 0; i < 256; i++)
/*  98 */             shared[i] = (byte)type; 
/*     */         } 
/* 100 */         charTypeTable[min >> 8] = shared;
/* 101 */         if (min + 255 == max) {
/*     */           return;
/*     */         }
/*     */       } 
/* 105 */       setCharType(min, type);
/* 106 */       min = (char)(min + 1); } while (min != max);
/*     */   }
/*     */   
/*     */   private static boolean isNameStartChar(char c) {
/* 110 */     return (charTypeTable[c >> 8][c & 0xFF] == 2);
/*     */   }
/*     */   
/*     */   private static boolean isNameStartCharNs(char c) {
/* 114 */     return (isNameStartChar(c) && c != ':');
/*     */   }
/*     */   
/*     */   private static boolean isNameChar(char c) {
/* 118 */     return (charTypeTable[c >> 8][c & 0xFF] != 0);
/*     */   }
/*     */   
/*     */   private static boolean isNameCharNs(char c) {
/* 122 */     return (isNameChar(c) && c != ':');
/*     */   }
/*     */   
/*     */   public static boolean isName(String s) {
/* 126 */     int len = s.length();
/* 127 */     if (len == 0)
/* 128 */       return false; 
/* 129 */     if (!isNameStartChar(s.charAt(0)))
/* 130 */       return false; 
/* 131 */     for (int i = 1; i < len; i++) {
/* 132 */       if (!isNameChar(s.charAt(i)))
/* 133 */         return false; 
/* 134 */     }  return true;
/*     */   }
/*     */   
/*     */   public static boolean isNmtoken(String s) {
/* 138 */     int len = s.length();
/* 139 */     if (len == 0)
/* 140 */       return false; 
/* 141 */     for (int i = 0; i < len; i++) {
/* 142 */       if (!isNameChar(s.charAt(i)))
/* 143 */         return false; 
/* 144 */     }  return true;
/*     */   }
/*     */   
/*     */   public static boolean isNcname(String s) {
/* 148 */     int len = s.length();
/* 149 */     if (len == 0)
/* 150 */       return false; 
/* 151 */     if (!isNameStartCharNs(s.charAt(0)))
/* 152 */       return false; 
/* 153 */     for (int i = 1; i < len; i++) {
/* 154 */       if (!isNameCharNs(s.charAt(i)))
/* 155 */         return false; 
/* 156 */     }  return true;
/*     */   }
/*     */   
/*     */   public static boolean isQname(String s) {
/* 160 */     int len = s.length();
/* 161 */     if (len == 0)
/* 162 */       return false; 
/* 163 */     if (!isNameStartCharNs(s.charAt(0)))
/* 164 */       return false; 
/* 165 */     for (int i = 1; i < len; i++) {
/* 166 */       char c = s.charAt(i);
/* 167 */       if (!isNameChar(c)) {
/* 168 */         if (c == ':' && ++i < len && isNameStartCharNs(s.charAt(i))) {
/* 169 */           for (; ++i < len; i++) {
/* 170 */             if (!isNameCharNs(s.charAt(i)))
/* 171 */               return false; 
/* 172 */           }  return true;
/*     */         } 
/* 174 */         return false;
/*     */       } 
/*     */     } 
/* 177 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\xm\\util\Naming.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */