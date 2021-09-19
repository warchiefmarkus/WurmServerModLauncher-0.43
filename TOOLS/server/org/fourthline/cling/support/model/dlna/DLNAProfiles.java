/*     */ package org.fourthline.cling.support.model.dlna;
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
/*     */ public enum DLNAProfiles
/*     */ {
/*  24 */   NONE("", ""),
/*     */ 
/*     */   
/*  27 */   JPEG_SM("JPEG_SM", "image/jpeg"),
/*  28 */   JPEG_MED("JPEG_MED", "image/jpeg"),
/*  29 */   JPEG_LRG("JPEG_LRG", "image/jpeg"),
/*  30 */   JPEG_TN("JPEG_TN", "image/jpeg"),
/*  31 */   JPEG_SM_ICO("JPEG_SM_ICO", "image/jpeg"),
/*  32 */   JPEG_LRG_ICO("JPEG_LRG_ICO", "image/jpeg"),
/*     */   
/*  34 */   PNG_TN("PNG_TN", "image/png"),
/*  35 */   PNG_SM_ICO("PNG_SM_ICO", "image/png"),
/*  36 */   PNG_LRG_ICO("PNG_LRG_ICO", "image/png"),
/*  37 */   PNG_LRG("PNG_LRG", "image/png"),
/*     */ 
/*     */   
/*  40 */   LPCM("LPCM", "audio/L16"),
/*  41 */   LPCM_LOW("LPCM_low", "audio/L16"),
/*     */   
/*  43 */   MP3("MP3", "audio/mpeg"),
/*  44 */   MP3X("MP3X", "audio/mpeg"),
/*     */   
/*  46 */   WMABASE("WMABASE", "audio/x-ms-wma"),
/*  47 */   WMAFULL("WMAFULL", "audio/x-ms-wma"),
/*  48 */   WMAPRO("WMAPRO", "audio/x-ms-wma"),
/*     */   
/*  50 */   AAC_ADTS("AAC_ADTS", "audio/vnd.dlna.adts"),
/*  51 */   AAC_ADTS_320("AAC_ADTS_320", "audio/vnd.dlna.adts"),
/*  52 */   AAC_ISO("AAC_ISO", "audio/mp4"),
/*  53 */   AAC_ISO_320("AAC_ISO_320", "audio/mp4"),
/*  54 */   AAC_LTP_ISO("AAC_LTP_ISO", "audio/mp4"),
/*  55 */   AAC_LTP_MULT5_ISO("AAC_LTP_MULT5_ISO", "audio/mp4"),
/*  56 */   AAC_LTP_MULT7_ISO("AAC_LTP_MULT7_ISO", "audio/mp4"),
/*  57 */   AAC_MULT5_ADTS("AAC_MULT5_ADTS", "audio/vnd.dlna.adts"),
/*  58 */   AAC_MULT5_ISO("AAC_MULT5_ISO", "audio/mp4"),
/*     */   
/*  60 */   HEAAC_L2_ADTS("HEAAC_L2_ADTS", "audio/vnd.dlna.adts"),
/*  61 */   HEAAC_L2_ISO("HEAAC_L2_ISO", "audio/mp4"),
/*  62 */   HEAAC_L3_ADTS("HEAAC_L3_ADTS", "audio/vnd.dlna.adts"),
/*  63 */   HEAAC_L3_ISO("HEAAC_L3_ISO", "audio/mp4"),
/*  64 */   HEAAC_MULT5_ADTS("HEAAC_MULT5_ADTS", "audio/vnd.dlna.adts"),
/*  65 */   HEAAC_MULT5_ISO("HEAAC_MULT5_ISO", "audio/mp4"),
/*  66 */   HEAAC_L2_ADTS_320("HEAAC_L2_ADTS_320", "audio/vnd.dlna.adts"),
/*  67 */   HEAAC_L2_ISO_320("HEAAC_L2_ISO_320", "audio/mp4"),
/*     */   
/*  69 */   BSAC_ISO("BSAC_ISO", "audio/mp4"),
/*  70 */   BSAC_MULT5_ISO("BSAC_MULT5_ISO", "audio/mp4"),
/*     */   
/*  72 */   HEAACv2_L2("HEAACv2_L2", "audio/mp4"),
/*  73 */   HEAACv2_L2_ADTS("HEAACv2_L2", "audio/vnd.dlna.adts"),
/*  74 */   HEAACv2_L2_320("HEAACv2_L2_320", "audio/mp4"),
/*  75 */   HEAACv2_L2_320_ADTS("HEAACv2_L2_320", "audio/vnd.dlna.adts"),
/*  76 */   HEAACv2_L3("HEAACv2_L3", "audio/mp4"),
/*  77 */   HEAACv2_L3_ADTS("HEAACv2_L3", "vnd.dlna.adts"),
/*  78 */   HEAACv2_MULT5("HEAACv2_MULT5", "audio/mp4"),
/*  79 */   HEAACv2_MULT5_ADTS("HEAACv2_MULT5", "vnd.dlna.adts"),
/*     */   
/*  81 */   AC3("AC3", "audio/vnd.dolby.dd-raw"),
/*     */   
/*  83 */   AMR("AMR_3GPP", "audio/mp4"),
/*  84 */   THREE_GPP("AMR_3GPP", "audio/3gpp"),
/*     */   
/*  86 */   AMR_WBplus("AMR_WBplus", "audio/3gpp"),
/*  87 */   ATRAC3("ATRAC3plus", "audio/x-sony-oma"),
/*     */ 
/*     */   
/*  90 */   WMVMED_BASE("WMVMED_BASE", "video/x-ms-wmv"),
/*  91 */   WMVMED_FULL("WMVMED_FULL", "video/x-ms-wmv"),
/*  92 */   WMVMED_PRO("WMVMED_PRO", "video/x-ms-wmv"),
/*  93 */   WMVHIGH_FULL("WMVHIGH_FULL", "video/x-ms-wmv"),
/*  94 */   WMVHIGH_PRO("WMVHIGH_PRO", "video/x-ms-wmv"),
/*  95 */   WMVHM_BASE("WMVHM_BASE", "video/x-ms-wmv"),
/*  96 */   WMVSPLL_BASE("WMVSPLL_BASE", "video/x-ms-wmv"),
/*  97 */   WMVSPML_BASE("WMVSPML_BASE", "video/x-ms-wmv"),
/*  98 */   WMVSPML_MP3("WMVSPML_MP3", "video/x-ms-wmv"),
/*     */   
/* 100 */   MPEG1("MPEG1", "video/mpeg"),
/*     */   
/* 102 */   MPEG_PS_NTSC("MPEG_PS_NTSC", "video/mpeg"),
/* 103 */   MPEG_PS_NTSC_XAC3("MPEG_PS_NTSC_XAC3", "video/mpeg"),
/* 104 */   MPEG_PS_PAL("MPEG_PS_PAL", "video/mpeg"),
/* 105 */   MPEG_PS_PAL_XAC3("MPEG_PS_PAL_XAC3", "video/mpeg"),
/*     */   
/* 107 */   MPEG_TS_MP_LL_AAC("MPEG_TS_MP_LL_AAC", "video/vnd.dlna.mpeg-tts"),
/* 108 */   MPEG_TS_MP_LL_AAC_T("MPEG_TS_MP_LL_AAC_T", "video/vnd.dlna.mpeg-tts"),
/* 109 */   MPEG_TS_MP_LL_AAC_ISO("MPEG_TS_MP_LL_AAC_ISO", "video/mpeg"),
/*     */   
/* 111 */   MPEG_TS_SD_50_L2_T("MPEG_TS_SD_50_L2_T", "video/vnd.dlna.mpeg-tts"),
/* 112 */   MPEG_TS_SD_60_L2_T("MPEG_TS_SD_60_L2_T", "video/vnd.dlna.mpeg-tts"),
/* 113 */   MPEG_TS_SD_60_AC3_T("MPEG_TS_SD_60_AC3_T", "video/vnd.dlna.mpeg-tts"),
/*     */   
/* 115 */   MPEG_TS_SD_EU("MPEG_TS_SD_EU", "video/vnd.dlna.mpeg-tts"),
/* 116 */   MPEG_TS_SD_EU_T("MPEG_TS_SD_EU_T", "video/vnd.dlna.mpeg-tts"),
/* 117 */   MPEG_TS_SD_EU_ISO("MPEG_TS_SD_EU_ISO", "video/mpeg"),
/* 118 */   MPEG_TS_SD_50_AC3_T("MPEG_TS_SD_50_AC3_T", "video/vnd.dlna.mpeg-tts"),
/*     */   
/* 120 */   MPEG_TS_SD_NA("MPEG_TS_SD_NA", "video/vnd.dlna.mpeg-tts"),
/* 121 */   MPEG_TS_SD_NA_T("MPEG_TS_SD_NA_T", "video/vnd.dlna.mpeg-tts"),
/* 122 */   MPEG_TS_SD_NA_ISO("MPEG_TS_SD_NA_ISO", "video/mpeg"),
/* 123 */   MPEG_TS_SD_NA_XAC3("MPEG_TS_SD_NA_XAC3", "video/vnd.dlna.mpeg-tts"),
/* 124 */   MPEG_TS_SD_NA_XAC3_T("MPEG_TS_SD_NA_XAC3_T", "video/vnd.dlna.mpeg-tts"),
/* 125 */   MPEG_TS_SD_NA_XAC3_ISO("MPEG_TS_SD_NA_XAC3_ISO", "video/mpeg"),
/*     */   
/* 127 */   MPEG_TS_HD_NA("MPEG_TS_HD_NA", "video/vnd.dlna.mpeg-tts"),
/* 128 */   MPEG_TS_HD_NA_T("MPEG_TS_HD_NA_T", "video/vnd.dlna.mpeg-tts"),
/* 129 */   MPEG_TS_HD_50_L2_T("MPEG_TS_HD_50_L2_T", "video/vnd.dlna.mpeg-tts"),
/* 130 */   MPEG_TS_HD_50_L2_ISO("MPEG_TS_HD_50_L2_ISO", "video/mpeg"),
/* 131 */   MPEG_TS_HD_60_L2_T("MPEG_TS_HD_60_L2_T", "video/vnd.dlna.mpeg-tts"),
/* 132 */   MPEG_TS_HD_60_L2_ISO("MPEG_TS_HD_60_L2_ISO", "video/mpeg"),
/*     */   
/* 134 */   MPEG_TS_HD_NA_ISO("MPEG_TS_HD_NA_ISO", "video/mpeg"),
/* 135 */   MPEG_TS_HD_NA_XAC3("MPEG_TS_HD_NA_XAC3", "video/vnd.dlna.mpeg-tts"),
/* 136 */   MPEG_TS_HD_NA_XAC3_T("MPEG_TS_HD_NA_XAC3_T", "video/vnd.dlna.mpeg-tts"),
/* 137 */   MPEG_TS_HD_NA_XAC3_ISO("MPEG_TS_HD_NA_XAC3_ISO", "video/mpeg"),
/*     */   
/* 139 */   MPEG_ES_PAL("MPEG_ES_PAL", "video/mpeg"),
/* 140 */   MPEG_ES_NTSC("MPEG_ES_NTSC", "video/mpeg"),
/* 141 */   MPEG_ES_PAL_XAC3("MPEG_ES_PAL_XAC3", "video/mpeg"),
/* 142 */   MPEG_ES_NTSC_XAC3("MPEG_ES_NTSC_XAC3", "video/mpeg"),
/*     */   
/* 144 */   MPEG4_P2_MP4_SP_AAC("MPEG4_P2_MP4_SP_AAC", "video/mp4"),
/* 145 */   MPEG4_P2_MP4_SP_HEAAC("MPEG4_P2_MP4_SP_HEAAC", "video/mp4"),
/* 146 */   MPEG4_P2_MP4_SP_ATRAC3plus("MPEG4_P2_MP4_SP_ATRAC3plus", "video/mp4"),
/* 147 */   MPEG4_P2_MP4_SP_AAC_LTP("MPEG4_P2_MP4_SP_AAC_LTP", "video/mp4"),
/* 148 */   MPEG4_P2_MP4_SP_L2_AAC("MPEG4_P2_MP4_SP_L2_AAC", "video/mp4"),
/* 149 */   MPEG4_P2_MP4_SP_L2_AMR("MPEG4_P2_MP4_SP_L2_AMR", "video/mp4"),
/* 150 */   MPEG4_P2_MP4_SP_VGA_AAC("MPEG4_P2_MP4_SP_VGA_AAC", "video/mp4"),
/* 151 */   MPEG4_P2_MP4_SP_VGA_HEAAC("MPEG4_P2_MP4_SP_VGA_HEAAC", "video/mp4"),
/* 152 */   MPEG4_P2_MP4_ASP_AAC("MPEG4_P2_MP4_ASP_AAC", "video/mp4"),
/* 153 */   MPEG4_P2_MP4_ASP_HEAAC("MPEG4_P2_MP4_ASP_HEAAC", "video/mp4"),
/* 154 */   MPEG4_P2_MP4_ASP_HEAAC_MULT5("MPEG4_P2_MP4_ASP_HEAAC_MULT5", "video/mp4"),
/* 155 */   MPEG4_P2_MP4_ASP_ATRAC3plus("MPEG4_P2_MP4_ASP_ATRAC3plus", "video/mp4"),
/* 156 */   MPEG4_P2_MP4_ASP_L5_SO_AAC("MPEG4_P2_MP4_ASP_L5_SO_AAC", "video/mp4"),
/* 157 */   MPEG4_P2_MP4_ASP_L5_SO_HEAAC("MPEG4_P2_MP4_ASP_L5_SO_HEAAC", "video/mp4"),
/* 158 */   MPEG4_P2_MP4_ASP_L5_SO_HEAAC_MULT5("MPEG4_P2_MP4_ASP_L5_SO_HEAAC_MULT5", "video/mp4"),
/* 159 */   MPEG4_P2_MP4_ASP_L4_SO_AAC("MPEG4_P2_MP4_ASP_L4_SO_AAC", "video/mp4"),
/* 160 */   MPEG4_P2_MP4_ASP_L4_SO_HEAAC("MPEG4_P2_MP4_ASP_L4_SO_HEAAC", "video/mp4"),
/* 161 */   MPEG4_P2_MP4_ASP_L4_SO_HEAAC_MULT5("MPEG4_P2_MP4_ASP_L4_SO_HEAAC_MULT5", "video/mp4"),
/*     */   
/* 163 */   MPEG4_H263_MP4_P0_L10_AAC("MPEG4_H263_MP4_P0_L10_AAC", "video/3gpp"),
/* 164 */   MPEG4_H263_MP4_P0_L10_AAC_LTP("MPEG4_H263_MP4_P0_L10_AAC_LTP", "video/3gpp"),
/*     */   
/* 166 */   MPEG4_P2_TS_SP_AAC("MPEG4_P2_TS_SP_AAC", "video/vnd.dlna.mpeg-tts"),
/* 167 */   MPEG4_P2_TS_SP_AAC_T("MPEG4_P2_TS_SP_AAC_T", "video/vnd.dlna.mpeg-tts"),
/* 168 */   MPEG4_P2_TS_SP_AAC_ISO("MPEG4_P2_TS_SP_AAC_ISO", "video/mpeg"),
/* 169 */   MPEG4_P2_TS_SP_MPEG1_L3("MPEG4_P2_TS_SP_MPEG1_L3", "video/vnd.dlna.mpeg-tts"),
/* 170 */   MPEG4_P2_TS_SP_MPEG1_L3_T("MPEG4_P2_TS_SP_MPEG1_L3_T", "video/vnd.dlna.mpeg-tts"),
/* 171 */   MPEG4_P2_TS_SP_MPEG1_L3_ISO("MPEG4_P2_TS_SP_MPEG1_L3_ISO", "video/mpeg"),
/* 172 */   MPEG4_P2_TS_SP_AC3_L3("MPEG4_P2_TS_SP_AC3_L3", "video/vnd.dlna.mpeg-tts"),
/* 173 */   MPEG4_P2_TS_SP_AC3_T("MPEG4_P2_TS_SP_AC3_T", "video/vnd.dlna.mpeg-tts"),
/* 174 */   MPEG4_P2_TS_SP_AC3_ISO("MPEG4_P2_TS_SP_AC3_ISO", "video/mpeg"),
/* 175 */   MPEG4_P2_TS_SP_MPEG2_L2("MPEG4_P2_TS_SP_MPEG2_L2", "video/vnd.dlna.mpeg-tts"),
/* 176 */   MPEG4_P2_TS_SP_MPEG2_L2_T("MPEG4_P2_TS_SP_MPEG2_L2_T", "video/vnd.dlna.mpeg-tts"),
/* 177 */   MPEG4_P2_TS_SP_MPEG2_L2_ISO("MPEG4_P2_TS_SP_MPEG2_L2_ISO", "video/mpeg"),
/* 178 */   MPEG4_P2_TS_ASP_AAC("MPEG4_P2_TS_ASP_AAC", "video/vnd.dlna.mpeg-tts"),
/* 179 */   MPEG4_P2_TS_ASP_AAC_T("MPEG4_P2_TS_ASP_AAC_T", "video/vnd.dlna.mpeg-tts"),
/* 180 */   MPEG4_P2_TS_ASP_AAC_ISO("MPEG4_P2_TS_ASP_AAC_ISO", "video/mpeg"),
/* 181 */   MPEG4_P2_TS_ASP_MPEG1_L3("MPEG4_P2_TS_ASP_MPEG1_L3", "video/vnd.dlna.mpeg-tts"),
/* 182 */   MPEG4_P2_TS_ASP_MPEG1_L3_T("MPEG4_P2_TS_ASP_MPEG1_L3_T", "video/vnd.dlna.mpeg-tts"),
/* 183 */   MPEG4_P2_TS_ASP_MPEG1_L3_ISO("MPEG4_P2_TS_ASP_MPEG1_L3_ISO", "video/mpeg"),
/* 184 */   MPEG4_P2_TS_ASP_AC3_L3("MPEG4_P2_TS_ASP_AC3_L3", "video/vnd.dlna.mpeg-tts"),
/* 185 */   MPEG4_P2_TS_ASP_AC3_T("MPEG4_P2_TS_ASP_AC3_T", "video/vnd.dlna.mpeg-tts"),
/* 186 */   MPEG4_P2_TS_ASP_AC3_ISO("MPEG4_P2_TS_ASP_AC3_ISO", "video/mpeg"),
/* 187 */   MPEG4_P2_TS_CO_AC3("MPEG4_P2_TS_CO_AC3", "video/vnd.dlna.mpeg-tts"),
/* 188 */   MPEG4_P2_TS_CO_AC3_T("MPEG4_P2_TS_CO_AC3_T", "video/vnd.dlna.mpeg-tts"),
/* 189 */   MPEG4_P2_TS_CO_AC3_ISO("MPEG4_P2_TS_CO_AC3_ISO", "video/mpeg"),
/* 190 */   MPEG4_P2_TS_CO_MPEG2_L2("MPEG4_P2_TS_CO_MPEG2_L2", "video/vnd.dlna.mpeg-tts"),
/* 191 */   MPEG4_P2_TS_CO_MPEG2_L2_T("MPEG4_P2_TS_CO_MPEG2_L2_T", "video/vnd.dlna.mpeg-tts"),
/* 192 */   MPEG4_P2_TS_CO_MPEG2_L2_ISO("MPEG4_P2_TS_CO_MPEG2_L2_ISO", "video/mpeg"),
/*     */   
/* 194 */   MPEG4_P2_ASF_SP_G726("MPEG4_P2_ASF_SP_G726", "video/x-ms-asf"),
/* 195 */   MPEG4_P2_ASF_ASP_L5_SO_G726("MPEG4_P2_ASF_ASP_L5_SO_G726", "video/x-ms-asf"),
/* 196 */   MPEG4_P2_ASF_ASP_L4_SO_G726("MPEG4_P2_ASF_ASP_L4_SO_G726", "video/x-ms-asf"),
/*     */   
/* 198 */   MPEG4_H263_3GPP_P0_L10_AMR_WBplus("MPEG4_H263_3GPP_P0_L10_AMR_WBplus", "video/3gpp"),
/* 199 */   MPEG4_P2_3GPP_SP_L0B_AAC("MPEG4_P2_3GPP_SP_L0B_AAC", "video/3gpp"),
/* 200 */   MPEG4_P2_3GPP_SP_L0B_AMR("MPEG4_P2_3GPP_SP_L0B_AMR", "video/3gpp"),
/* 201 */   MPEG4_H263_3GPP_P3_L10_AMR("MPEG4_H263_3GPP_P3_L10_AMR", "video/3gpp"),
/*     */   
/* 203 */   AVC_MP4_MP_SD_AAC_MULT5("AVC_MP4_MP_SD_AAC_MULT5", "video/mp4"),
/* 204 */   AVC_MP4_MP_SD_HEAAC_L2("AVC_MP4_MP_SD_HEAAC_L2", "video/mp4"),
/* 205 */   AVC_MP4_MP_SD_MPEG1_L3("AVC_MP4_MP_SD_MPEG1_L3", "video/mp4"),
/* 206 */   AVC_MP4_MP_SD_AC3("AVC_MP4_MP_SD_AC3", "video/mp4"),
/* 207 */   AVC_MP4_MP_SD_AAC_LTP("AVC_MP4_MP_SD_AAC_LTP", "video/mp4"),
/* 208 */   AVC_MP4_MP_SD_AAC_LTP_MULT5("AVC_MP4_MP_SD_AAC_LTP_MULT5", "video/mp4"),
/* 209 */   AVC_MP4_MP_SD_AAC_LTP_MULT7("AVC_MP4_MP_SD_AAC_LTP_MULT7", "video/mp4"),
/* 210 */   AVC_MP4_MP_SD_ATRAC3plus("AVC_MP4_MP_SD_ATRAC3plus", "video/mp4"),
/* 211 */   AVC_MP4_MP_SD_BSAC("AVC_MP4_MP_SD_BSAC", "video/mp4"),
/*     */   
/* 213 */   AVC_MP4_MP_HD_720p_AAC("AVC_MP4_MP_HD_720p_AAC", "video/mp4"),
/* 214 */   AVC_MP4_MP_HD_1080i_AAC("AVC_MP4_MP_HD_1080i_AAC", "video/mp4"),
/*     */   
/* 216 */   AVC_MP4_HP_HD_AAC("AVC_MP4_HP_HD_AAC", "video/mp4"),
/*     */   
/* 218 */   AVC_MP4_BL_L3L_SD_AAC("AVC_MP4_BL_L3L_SD_AAC", "video/mp4"),
/* 219 */   AVC_MP4_BL_L3L_SD_HEAAC("AVC_MP4_BL_L3L_SD_HEAAC", "video/mp4"),
/* 220 */   AVC_MP4_BL_L3_SD_AAC("AVC_MP4_BL_L3_SD_AAC", "video/mp4"),
/* 221 */   AVC_MP4_BL_CIF30_AAC_MULT5("AVC_MP4_BL_CIF30_AAC_MULT5", "video/mp4"),
/* 222 */   AVC_MP4_BL_CIF30_HEAAC_L2("AVC_MP4_BL_CIF30_HEAAC_L2", "video/mp4"),
/* 223 */   AVC_MP4_BL_CIF30_MPEG1_L3("AVC_MP4_BL_CIF30_MPEG1_L3", "video/mp4"),
/* 224 */   AVC_MP4_BL_CIF30_AC3("AVC_MP4_BL_CIF30_AC3", "video/mp4"),
/* 225 */   AVC_MP4_BL_CIF30_AAC_LTP("AVC_MP4_BL_CIF30_AAC_LTP", "video/mp4"),
/* 226 */   AVC_MP4_BL_CIF30_AAC_LTP_MULT5("AVC_MP4_BL_CIF30_AAC_LTP_MULT5", "video/mp4"),
/* 227 */   AVC_MP4_BL_L2_CIF30_AAC("AVC_MP4_BL_L2_CIF30_AAC", "video/mp4"),
/* 228 */   AVC_MP4_BL_CIF30_BSAC("AVC_MP4_BL_CIF30_BSAC", "video/mp4"),
/* 229 */   AVC_MP4_BL_CIF30_BSAC_MULT5("AVC_MP4_BL_CIF30_BSAC_MULT5", "video/mp4"),
/* 230 */   AVC_MP4_BL_CIF15_HEAAC("AVC_MP4_BL_CIF15_HEAAC", "video/mp4"),
/* 231 */   AVC_MP4_BL_CIF15_AMR("AVC_MP4_BL_CIF15_AMR", "video/mp4"),
/* 232 */   AVC_MP4_BL_CIF15_AAC("AVC_MP4_BL_CIF15_AAC", "video/mp4"),
/* 233 */   AVC_MP4_BL_CIF15_AAC_520("AVC_MP4_BL_CIF15_AAC_520", "video/mp4"),
/* 234 */   AVC_MP4_BL_CIF15_AAC_LTP("AVC_MP4_BL_CIF15_AAC_LTP", "video/mp4"),
/* 235 */   AVC_MP4_BL_CIF15_AAC_LTP_520("AVC_MP4_BL_CIF15_AAC_LTP_520", "video/mp4"),
/* 236 */   AVC_MP4_BL_CIF15_BSAC("AVC_MP4_BL_CIF15_BSAC", "video/mp4"),
/* 237 */   AVC_MP4_BL_L12_CIF15_HEAAC("AVC_MP4_BL_L12_CIF15_HEAAC", "video/mp4"),
/* 238 */   AVC_MP4_BL_L1B_QCIF15_HEAAC("AVC_MP4_BL_L1B_QCIF15_HEAAC", "video/mp4"),
/*     */   
/* 240 */   AVC_TS_MP_SD_AAC_MULT5("AVC_TS_MP_SD_AAC_MULT5", "video/vnd.dlna.mpeg-tts"),
/* 241 */   AVC_TS_MP_SD_AAC_MULT5_T("AVC_TS_MP_SD_AAC_MULT5_T", "video/vnd.dlna.mpeg-tts"),
/* 242 */   AVC_TS_MP_SD_AAC_MULT5_ISO("AVC_TS_MP_SD_AAC_MULT5_ISO", "video/mpeg"),
/* 243 */   AVC_TS_MP_SD_HEAAC_L2("AVC_TS_MP_SD_HEAAC_L2", "video/vnd.dlna.mpeg-tts"),
/* 244 */   AVC_TS_MP_SD_HEAAC_L2_T("AVC_TS_MP_SD_HEAAC_L2_T", "video/vnd.dlna.mpeg-tts"),
/* 245 */   AVC_TS_MP_SD_HEAAC_L2_ISO("AVC_TS_MP_SD_HEAAC_L2_ISO", "video/mpeg"),
/* 246 */   AVC_TS_MP_SD_MPEG1_L3("AVC_TS_MP_SD_MPEG1_L3", "video/vnd.dlna.mpeg-tts"),
/* 247 */   AVC_TS_MP_SD_MPEG1_L3_T("AVC_TS_MP_SD_MPEG1_L3_T", "video/vnd.dlna.mpeg-tts"),
/* 248 */   AVC_TS_MP_SD_MPEG1_L3_ISO("AVC_TS_MP_SD_MPEG1_L3_ISO", "video/mpeg"),
/* 249 */   AVC_TS_MP_SD_AC3("AVC_TS_MP_SD_AC3", "video/vnd.dlna.mpeg-tts"),
/* 250 */   AVC_TS_MP_SD_AC3_T("AVC_TS_MP_SD_AC3_T", "video/vnd.dlna.mpeg-tts"),
/* 251 */   AVC_TS_MP_SD_AC3_ISO("AVC_TS_MP_SD_AC3_ISO", "video/mpeg"),
/* 252 */   AVC_TS_MP_SD_AAC_LTP("AVC_TS_MP_SD_AAC_LTP", "video/vnd.dlna.mpeg-tts"),
/* 253 */   AVC_TS_MP_SD_AAC_LTP_T("AVC_TS_MP_SD_AAC_LTP_T", "video/vnd.dlna.mpeg-tts"),
/* 254 */   AVC_TS_MP_SD_AAC_LTP_ISO("AVC_TS_MP_SD_AAC_LTP_ISO", "video/mpeg"),
/* 255 */   AVC_TS_MP_SD_AAC_LTP_MULT5("AVC_TS_MP_SD_AAC_LTP_MULT5", "video/vnd.dlna.mpeg-tts"),
/* 256 */   AVC_TS_MP_SD_AAC_LTP_MULT5_T("AVC_TS_MP_SD_AAC_LTP_MULT5_T", "video/vnd.dlna.mpeg-tts"),
/* 257 */   AVC_TS_MP_SD_AAC_LTP_MULT5_ISO("AVC_TS_MP_SD_AAC_LTP_MULT5_ISO", "video/mpeg"),
/* 258 */   AVC_TS_MP_SD_AAC_LTP_MULT7("AVC_TS_MP_SD_AAC_LTP_MULT7", "video/vnd.dlna.mpeg-tts"),
/* 259 */   AVC_TS_MP_SD_AAC_LTP_MULT7_T("AVC_TS_MP_SD_AAC_LTP_MULT7_T", "video/vnd.dlna.mpeg-tts"),
/* 260 */   AVC_TS_MP_SD_AAC_LTP_MULT7_ISO("AVC_TS_MP_SD_AAC_LTP_MULT7_ISO", "video/mpeg"),
/* 261 */   AVC_TS_MP_SD_BSAC("AVC_TS_MP_SD_BSAC", "video/vnd.dlna.mpeg-tts"),
/* 262 */   AVC_TS_MP_SD_BSAC_T("AVC_TS_MP_SD_BSAC_T", "video/vnd.dlna.mpeg-tts"),
/* 263 */   AVC_TS_MP_SD_BSAC_ISO("AVC_TS_MP_SD_BSAC_ISO", "video/mpeg"),
/*     */   
/* 265 */   AVC_TS_HD_24_AC3("AVC_TS_HD_24_AC3", "video/vnd.dlna.mpeg-tts"),
/* 266 */   AVC_TS_HD_24_AC3_T("AVC_TS_HD_24_AC3_T", "video/vnd.dlna.mpeg-tts"),
/* 267 */   AVC_TS_HD_24_AC3_ISO("AVC_TS_HD_24_AC3_ISO", "video/mpeg"),
/*     */   
/* 269 */   AVC_TS_HD_50_LPCM_T("AVC_TS_HD_50_LPCM_T", "video/vnd.dlna.mpeg-tts"),
/* 270 */   AVC_TS_HD_50_AC3("AVC_TS_HD_50_AC3", "video/vnd.dlna.mpeg-tts"),
/* 271 */   AVC_TS_HD_50_AC3_T("AVC_TS_HD_50_AC3_T", "video/vnd.dlna.mpeg-tts"),
/* 272 */   AVC_TS_HD_50_AC3_ISO("AVC_TS_HD_50_AC3_ISO", "video/mpeg"),
/*     */   
/* 274 */   AVC_TS_HD_60_AC3("AVC_TS_HD_60_AC3", "video/vnd.dlna.mpeg-tts"),
/* 275 */   AVC_TS_HD_60_AC3_T("AVC_TS_HD_60_AC3_T", "video/vnd.dlna.mpeg-tts"),
/* 276 */   AVC_TS_HD_60_AC3_ISO("AVC_TS_HD_60_AC3_ISO", "video/mpeg"),
/*     */   
/* 278 */   AVC_TS_BL_CIF30_AAC_MULT5("AVC_TS_BL_CIF30_AAC_MULT5", "video/vnd.dlna.mpeg-tts"),
/* 279 */   AVC_TS_BL_CIF30_AAC_MULT5_T("AVC_TS_BL_CIF30_AAC_MULT5_T", "video/vnd.dlna.mpeg-tts"),
/* 280 */   AVC_TS_BL_CIF30_AAC_MULT5_ISO("AVC_TS_BL_CIF30_AAC_MULT5_ISO", "video/mpeg"),
/* 281 */   AVC_TS_BL_CIF30_HEAAC_L2("AVC_TS_BL_CIF30_HEAAC_L2", "video/vnd.dlna.mpeg-tts"),
/* 282 */   AVC_TS_BL_CIF30_HEAAC_L2_T("AVC_TS_BL_CIF30_HEAAC_L2_T", "video/vnd.dlna.mpeg-tts"),
/* 283 */   AVC_TS_BL_CIF30_HEAAC_L2_ISO("AVC_TS_BL_CIF30_HEAAC_L2_ISO", "video/mpeg"),
/* 284 */   AVC_TS_BL_CIF30_MPEG1_L3("AVC_TS_BL_CIF30_MPEG1_L3", "video/vnd.dlna.mpeg-tts"),
/* 285 */   AVC_TS_BL_CIF30_MPEG1_L3_T("AVC_TS_BL_CIF30_MPEG1_L3_T", "video/vnd.dlna.mpeg-tts"),
/* 286 */   AVC_TS_BL_CIF30_MPEG1_L3_ISO("AVC_TS_BL_CIF30_MPEG1_L3_ISO", "video/mpeg"),
/* 287 */   AVC_TS_BL_CIF30_AC3("AVC_TS_BL_CIF30_AC3", "video/vnd.dlna.mpeg-tts"),
/* 288 */   AVC_TS_BL_CIF30_AC3_T("AVC_TS_BL_CIF30_AC3_T", "video/vnd.dlna.mpeg-tts"),
/* 289 */   AVC_TS_BL_CIF30_AC3_ISO("AVC_TS_BL_CIF30_AC3_ISO", "video/mpeg"),
/* 290 */   AVC_TS_BL_CIF30_AAC_LTP("AVC_TS_BL_CIF30_AAC_LTP", "video/vnd.dlna.mpeg-tts"),
/* 291 */   AVC_TS_BL_CIF30_AAC_LTP_T("AVC_TS_BL_CIF30_AAC_LTP_T", "video/vnd.dlna.mpeg-tts"),
/* 292 */   AVC_TS_BL_CIF30_AAC_LTP_ISO("AVC_TS_BL_CIF30_AAC_LTP_ISO", "video/mpeg"),
/* 293 */   AVC_TS_BL_CIF30_AAC_LTP_MULT5("AVC_TS_BL_CIF30_AAC_LTP_MULT5", "video/vnd.dlna.mpeg-tts"),
/* 294 */   AVC_TS_BL_CIF30_AAC_LTP_MULT5_T("AVC_TS_BL_CIF30_AAC_LTP_MULT5_T", "video/vnd.dlna.mpeg-tts"),
/* 295 */   AVC_TS_BL_CIF30_AAC_LTP_MULT5_ISO("AVC_TS_BL_CIF30_AAC_LTP_MULT5_ISO", "video/mpeg"),
/* 296 */   AVC_TS_BL_CIF30_AAC_940("AVC_TS_BL_CIF30_AAC_940", "video/vnd.dlna.mpeg-tts"),
/* 297 */   AVC_TS_BL_CIF30_AAC_940_T("AVC_TS_BL_CIF30_AAC_940_T", "video/vnd.dlna.mpeg-tts"),
/* 298 */   AVC_TS_BL_CIF30_AAC_940_ISO("AVC_TS_BL_CIF30_AAC_940_ISO", "video/mpeg"),
/*     */   
/* 300 */   AVC_TS_MP_HD_AAC_MULT5("AVC_TS_MP_HD_AAC_MULT5", "video/vnd.dlna.mpeg-tts"),
/* 301 */   AVC_TS_MP_HD_AAC_MULT5_T("AVC_TS_MP_HD_AAC_MULT5_T", "video/vnd.dlna.mpeg-tts"),
/* 302 */   AVC_TS_MP_HD_AAC_MULT5_ISO("AVC_TS_MP_HD_AAC_MULT5_ISO", "video/mpeg"),
/* 303 */   AVC_TS_MP_HD_HEAAC_L2("AVC_TS_MP_HD_HEAAC_L2", "video/vnd.dlna.mpeg-tts"),
/* 304 */   AVC_TS_MP_HD_HEAAC_L2_T("AVC_TS_MP_HD_HEAAC_L2_T", "video/vnd.dlna.mpeg-tts"),
/* 305 */   AVC_TS_MP_HD_HEAAC_L2_ISO("AVC_TS_MP_HD_HEAAC_L2_ISO", "video/mpeg"),
/* 306 */   AVC_TS_MP_HD_MPEG1_L3("AVC_TS_MP_HD_MPEG1_L3", "video/vnd.dlna.mpeg-tts"),
/* 307 */   AVC_TS_MP_HD_MPEG1_L3_T("AVC_TS_MP_HD_MPEG1_L3_T", "video/vnd.dlna.mpeg-tts"),
/* 308 */   AVC_TS_MP_HD_MPEG1_L3_ISO("AVC_TS_MP_HD_MPEG1_L3_ISO", "video/mpeg"),
/* 309 */   AVC_TS_MP_HD_AC3("AVC_TS_MP_HD_AC3", "video/vnd.dlna.mpeg-tts"),
/* 310 */   AVC_TS_MP_HD_AC3_T("AVC_TS_MP_HD_AC3_T", "video/vnd.dlna.mpeg-tts"),
/* 311 */   AVC_TS_MP_HD_AC3_ISO("AVC_TS_MP_HD_AC3_ISO", "video/mpeg"),
/* 312 */   AVC_TS_MP_HD_AAC("AVC_TS_MP_HD_AAC", "video/vnd.dlna.mpeg-tts"),
/* 313 */   AVC_TS_MP_HD_AAC_T("AVC_TS_MP_HD_AAC_T", "video/vnd.dlna.mpeg-tts"),
/* 314 */   AVC_TS_MP_HD_AAC_ISO("AVC_TS_MP_HD_AAC_ISO", "video/mpeg"),
/* 315 */   AVC_TS_MP_HD_AAC_LTP("AVC_TS_MP_HD_AAC_LTP", "video/vnd.dlna.mpeg-tts"),
/* 316 */   AVC_TS_MP_HD_AAC_LTP_T("AVC_TS_MP_HD_AAC_LTP_T", "video/vnd.dlna.mpeg-tts"),
/* 317 */   AVC_TS_MP_HD_AAC_LTP_ISO("AVC_TS_MP_HD_AAC_LTP_ISO", "video/mpeg"),
/* 318 */   AVC_TS_MP_HD_AAC_LTP_MULT5("AVC_TS_MP_HD_AAC_LTP_MULT5", "video/vnd.dlna.mpeg-tts"),
/* 319 */   AVC_TS_MP_HD_AAC_LTP_MULT5_T("AVC_TS_MP_HD_AAC_LTP_MULT5_T", "video/vnd.dlna.mpeg-tts"),
/* 320 */   AVC_TS_MP_HD_AAC_LTP_MULT5_ISO("AVC_TS_MP_HD_AAC_LTP_MULT5_ISO", "video/mpeg"),
/* 321 */   AVC_TS_MP_HD_AAC_LTP_MULT7("AVC_TS_MP_HD_AAC_LTP_MULT7", "video/vnd.dlna.mpeg-tts"),
/* 322 */   AVC_TS_MP_HD_AAC_LTP_MULT7_T("AVC_TS_MP_HD_AAC_LTP_MULT7_T", "video/vnd.dlna.mpeg-tts"),
/* 323 */   AVC_TS_MP_HD_AAC_LTP_MULT7_ISO("AVC_TS_MP_HD_AAC_LTP_MULT7_ISO", "video/mpeg"),
/*     */   
/* 325 */   AVC_TS_BL_CIF15_AAC("AVC_TS_BL_CIF15_AAC", "video/vnd.dlna.mpeg-tts"),
/* 326 */   AVC_TS_BL_CIF15_AAC_T("AVC_TS_BL_CIF15_AAC_T", "video/vnd.dlna.mpeg-tts"),
/* 327 */   AVC_TS_BL_CIF15_AAC_ISO("AVC_TS_BL_CIF15_AAC_ISO", "video/mpeg"),
/* 328 */   AVC_TS_BL_CIF15_AAC_540("AVC_TS_BL_CIF15_AAC_540", "video/vnd.dlna.mpeg-tts"),
/* 329 */   AVC_TS_BL_CIF15_AAC_540_T("AVC_TS_BL_CIF15_AAC_540_T", "video/vnd.dlna.mpeg-tts"),
/* 330 */   AVC_TS_BL_CIF15_AAC_540_ISO("AVC_TS_BL_CIF15_AAC_540_ISO", "video/mpeg"),
/* 331 */   AVC_TS_BL_CIF15_AAC_LTP("AVC_TS_BL_CIF15_AAC_LTP", "video/vnd.dlna.mpeg-tts"),
/* 332 */   AVC_TS_BL_CIF15_AAC_LTP_T("AVC_TS_BL_CIF15_AAC_LTP_T", "video/vnd.dlna.mpeg-tts"),
/* 333 */   AVC_TS_BL_CIF15_AAC_LTP_ISO("AVC_TS_BL_CIF15_AAC_LTP_ISO", "video/mpeg"),
/* 334 */   AVC_TS_BL_CIF15_BSAC("AVC_TS_BL_CIF15_BSAC", "video/vnd.dlna.mpeg-tts"),
/* 335 */   AVC_TS_BL_CIF15_BSAC_T("AVC_TS_BL_CIF15_BSAC_T", "video/vnd.dlna.mpeg-tts"),
/* 336 */   AVC_TS_BL_CIF15_BSAC_ISO("AVC_TS_BL_CIF15_BSAC_ISO", "video/mpeg"),
/*     */ 
/*     */   
/* 339 */   AVC_3GPP_BL_CIF30_AMR_WBplus("AVC_3GPP_BL_CIF30_AMR_WBplus", "video/3gpp"),
/* 340 */   AVC_3GPP_BL_CIF15_AMR_WBplus("AVC_3GPP_BL_CIF15_AMR_WBplus", "video/3gpp"),
/* 341 */   AVC_3GPP_BL_QCIF15_AAC("AVC_3GPP_BL_QCIF15_AAC", "video/3gpp"),
/* 342 */   AVC_3GPP_BL_QCIF15_AAC_LTP("AVC_3GPP_BL_QCIF15_AAC_LTP", "video/3gpp"),
/* 343 */   AVC_3GPP_BL_QCIF15_HEAAC("AVC_3GPP_BL_QCIF15_HEAAC", "video/3gpp"),
/* 344 */   AVC_3GPP_BL_QCIF15_AMR_WBplus("AVC_3GPP_BL_QCIF15_AMR_WBplus", "video/3gpp"),
/* 345 */   AVC_3GPP_BL_QCIF15_AMR("AVC_3GPP_BL_QCIF15_AMR", "video/3gpp"),
/*     */   
/* 347 */   AVC_MP4_LPCM("AVC_MP4_LPCM", "video/mp4"),
/*     */   
/* 349 */   AVI("AVI", "video/avi"),
/* 350 */   AVI_XMS("AVI", "video/x-msvideo"),
/* 351 */   DIVX("AVI", "video/divx"),
/*     */   
/* 353 */   MATROSKA("MATROSKA", "video/x-matroska"),
/* 354 */   MATROSKA_MKV("MATROSKA", "video/x-mkv"),
/*     */   
/* 356 */   VC1_ASF_AP_L1_WMA("VC1_ASF_AP_L1_WMA", "video/x-ms-asf"),
/* 357 */   VC1_ASF_AP_L2_WMA("VC1_ASF_AP_L2_WMA", "video/x-ms-asf"),
/* 358 */   VC1_ASF_AP_L3_WMA("VC1_ASF_AP_L3_WMA", "video/x-ms-asf"),
/*     */   
/* 360 */   VC1_ASF_AP_L1_WMA_WMV("VC1_ASF_AP_L1_WMA", "video/x-ms-wmv"),
/* 361 */   VC1_ASF_AP_L2_WMA_WMV("VC1_ASF_AP_L2_WMA", "video/x-ms-wmv"),
/* 362 */   VC1_ASF_AP_L3_WMA_WMV("VC1_ASF_AP_L3_WMA", "video/x-ms-wmv");
/*     */   
/*     */   private String code;
/*     */   private String contentFormat;
/*     */   
/*     */   DLNAProfiles(String code, String contentFormat) {
/* 368 */     this.code = code;
/* 369 */     this.contentFormat = contentFormat;
/*     */   }
/*     */   
/*     */   public String getCode() {
/* 373 */     return this.code;
/*     */   }
/*     */   
/*     */   public String getContentFormat() {
/* 377 */     return this.contentFormat;
/*     */   }
/*     */   
/*     */   class DLNAMimeTypes {
/*     */     public static final String MIME_IMAGE_JPEG = "image/jpeg";
/*     */     public static final String MIME_IMAGE_PNG = "image/png";
/*     */     public static final String MIME_AUDIO_3GP = "audio/3gpp";
/*     */     public static final String MIME_AUDIO_ADTS = "audio/vnd.dlna.adts";
/*     */     public static final String MIME_AUDIO_ATRAC = "audio/x-sony-oma";
/*     */     public static final String MIME_AUDIO_DOLBY_DIGITAL = "audio/vnd.dolby.dd-raw";
/*     */     public static final String MIME_AUDIO_LPCM = "audio/L16";
/*     */     public static final String MIME_AUDIO_MPEG = "audio/mpeg";
/*     */     public static final String MIME_AUDIO_MPEG_4 = "audio/mp4";
/*     */     public static final String MIME_AUDIO_WMA = "audio/x-ms-wma";
/*     */     public static final String MIME_VIDEO_3GP = "video/3gpp";
/*     */     public static final String MIME_VIDEO_ASF = "video/x-ms-asf";
/*     */     public static final String MIME_VIDEO_MPEG = "video/mpeg";
/*     */     public static final String MIME_VIDEO_MPEG_4 = "video/mp4";
/*     */     public static final String MIME_VIDEO_MPEG_TS = "video/vnd.dlna.mpeg-tts";
/*     */     public static final String MIME_VIDEO_WMV = "video/x-ms-wmv";
/*     */     public static final String MIME_VIDEO_DIVX = "video/divx";
/*     */     public static final String MIME_VIDEO_AVI = "video/avi";
/*     */     public static final String MIME_VIDEO_XMS_AVI = "video/x-msvideo";
/*     */     public static final String MIME_VIDEO_MATROSKA = "video/x-matroska";
/*     */     public static final String MIME_VIDEO_MKV = "video/x-mkv";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\DLNAProfiles.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */