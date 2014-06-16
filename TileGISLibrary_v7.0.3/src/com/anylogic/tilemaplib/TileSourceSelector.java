/*   1:    */ package com.anylogic.tilemaplib;
/*   2:    */ 
/*   3:    */ import com.xj.anylogic.engine.Agent;
import com.xj.anylogic.engine.AgentArrayList;
import com.xj.anylogic.engine.Engine;
import com.xj.anylogic.engine.presentation.Panel;
import com.xj.anylogic.engine.presentation.*;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

/*   4:    */
/*   5:    */
/*   6:    */
/*   7:    */
/*   8:    */
/*   9:    */
/*  10:    */
/*  11:    */
/*  12:    */
/*  13:    */
/*  14:    */
/*  15:    */
/*  16:    */
/*  17:    */
/*  18:    */
/*  19:    */
/*  20:    */
/*  21:    */ 
/*  22:    */ public class TileSourceSelector
/*  23:    */   extends Agent
/*  24:    */ {
/*  25:    */   public TileGIS map;
/*  26:    */   public TileImageProvider[] sources;
/*  27:    */   public TileImageProvider kosmosnimki;
/*  28:    */   public TileImageProvider landsat;
/*  29:    */   public TileImageProvider germanStyle;
/*  30:    */   
/*  31:    */   public TileGIS _map_DefaultValue_xjal()
/*  32:    */   {
/*  33: 65 */     TileSourceSelector self = this;
/*  34: 66 */     return null;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void set_map(TileGIS map)
/*  38:    */   {
/*  39: 70 */     if (map == this.map) {
/*  40: 71 */       return;
/*  41:    */     }
/*  42: 73 */     TileGIS _oldValue_xjal = this.map;
/*  43: 74 */     this.map = map;
/*  44: 75 */     onChange_map_xjal(_oldValue_xjal);
/*  45: 76 */     onChange();
/*  46:    */   }
/*  47:    */   
/*  48:    */   void onChange_map()
/*  49:    */   {
/*  50: 86 */     onChange_map_xjal(this.map);
/*  51:    */   }
/*  52:    */   
/*  53:    */   void onChange_map_xjal(TileGIS oldValue) {}
/*  54:    */   
/*  55:    */   public void setParametersToDefaultValues()
/*  56:    */   {
/*  57: 95 */     super.setParametersToDefaultValues();
/*  58: 96 */     this.map = _map_DefaultValue_xjal();
/*  59:    */   }
/*  60:    */   
/*  61:113 */   public ViewArea _origin_VA = new ViewArea(this, "[Origin]", 0.0D, 0.0D, ViewArea.Alignment.CENTER, ViewArea.Scaling.NONE, 1.0D, 100.0D, 100.0D);
/*  62:    */   
/*  63:    */   public int getViewAreas(Map<String, ViewArea> _output)
/*  64:    */   {
/*  65:116 */     if (_output != null) {
/*  66:117 */       _output.put("_origin_VA", this._origin_VA);
/*  67:    */     }
/*  68:119 */     return 1 + super.getViewAreas(_output);
/*  69:    */   }
/*  70:    */   
/*  71:121 */   static final Font _combobox_Font = new Font("Dialog", 0, 11);
/*  72:122 */   static final Font _text2_Font = new Font("SansSerif", 0, 16);
/*  73:    */   static final int _combobox = 1;
/*  74:    */   static final int _text2 = 2;
/*  75:    */   static final int _presentation = 0;
/*  76:    */   static final int _icon = -1;
/*  77:    */   
/*  78:    */   public void executeShapeControlAction(int _shape, int index, String value)
/*  79:    */   {
/*  80:140 */     switch (_shape)
/*  81:    */     {
/*  82:    */     case 1: 
/*  83:142 */       this.map.setImageProvider(this.sources[this.combobox.getValueIndex()]);
/*  84:    */       
/*  85:144 */       break;
/*  86:    */     default: 
/*  87:146 */       super.executeShapeControlAction(_shape, index, value);
/*  88:    */     }
/*  89:    */   }
/*  90:    */   
/*  91:156 */   ShapeComboBox combobox = new ShapeComboBox(this, true, 13, 24, 217, 20, UtilitiesColor.controlDefault, UtilitiesColor.controlDefault, _combobox_Font, new String[] { "MapQuest-OSM", "Kosmosnimki", "LandSat", "GermanStyle" }, false)
/*  97:    */   {
/*  98:    */     private static final long serialVersionUID = 1386333693731L;
/*  99:    */     
/* 100:    */     public void action()
/* 101:    */     {
/* 102:165 */       TileSourceSelector.this.executeShapeControlAction(1, 0, this.value);
/* 103:    */     }
/* 104:    */   };
/* 105:172 */   ShapeText text2 = new ShapeText(
/* 106:173 */     true, 13.0D, 4.0D, 0.0D, 
/* 107:174 */     UtilitiesColor.royalBlue, "Tile source:", 
/* 108:175 */     _text2_Font, TextAlignment.ALIGNMENT_CENTER);
/* 109:    */   ShapeTopLevelPresentationGroup presentation;
/* 110:    */   ShapeGroup icon;
/* 111:    */   private static final long serialVersionUID = 1388633710382L;
/* 112:    */   
/* 113:    */   public Object getPersistentShape(int _shape)
/* 114:    */   {
/* 115:182 */     switch (_shape)
/* 116:    */     {
/* 117:    */     case 0: 
/* 118:183 */       return this.presentation;
/* 119:    */     case -1: 
/* 120:184 */       return this.icon;
/* 121:    */     case 1: 
/* 122:186 */       return this.combobox;
/* 123:    */     case 2: 
/* 124:187 */       return this.text2;
/* 125:    */     }
/* 126:188 */     return null;
/* 127:    */   }
/* 128:    */   
/* 129:    */   private void drawModelElements_Parameters_xjal(Panel _panel, Graphics2D _g, boolean _publicOnly)
/* 130:    */   {
/* 131:193 */     if (!_publicOnly) {
/* 132:194 */       drawParameter(_panel, _g, -150, 30, 10, 0, "map", this.map, 0);
/* 133:    */     }
/* 134:    */   }
/* 135:    */   
/* 136:    */   private void drawModelElements_PlainVariables_xjal(Panel _panel, Graphics2D _g, boolean _publicOnly)
/* 137:    */   {
/* 138:199 */     if (!_publicOnly) {
/* 139:200 */       drawPlainVariable(_panel, _g, -280, 220, 10, 0, "sources", this.sources, false);
/* 140:    */     }
/* 141:202 */     if (!_publicOnly) {
/* 142:203 */       drawPlainVariable(_panel, _g, -280, 27, 10, 0, "kosmosnimki", this.kosmosnimki, false);
/* 143:    */     }
/* 144:205 */     if (!_publicOnly) {
/* 145:206 */       drawPlainVariable(_panel, _g, -280, 77, 10, 0, "landsat", this.landsat, false);
/* 146:    */     }
/* 147:208 */     if (!_publicOnly) {
/* 148:209 */       drawPlainVariable(_panel, _g, -280, 127, 10, 0, "germanStyle", this.germanStyle, false);
/* 149:    */     }
/* 150:    */   }
/* 151:    */   
/* 152:    */   public void drawModelElements(Panel _panel, Graphics2D _g, boolean _publicOnly)
/* 153:    */   {
/* 154:215 */     drawModelElements_Parameters_xjal(_panel, _g, _publicOnly);
/* 155:216 */     drawModelElements_PlainVariables_xjal(_panel, _g, _publicOnly);
/* 156:    */   }
/* 157:    */   
/* 158:    */   private boolean onClickModelAt_Parameters_xjal(Panel _panel, double _x, double _y, int _clickCount, boolean _publicOnly)
/* 159:    */   {
/* 160:221 */     if ((!_publicOnly) && (modelElementContains(_x, _y, -150.0D, 30.0D)))
/* 161:    */     {
/* 162:222 */       _panel.addInspect(-150.0D, 30.0D, this, "map");
/* 163:223 */       return true;
/* 164:    */     }
/* 165:225 */     return false;
/* 166:    */   }
/* 167:    */   
/* 168:    */   private boolean onClickModelAt_PlainVariables_xjal(Panel _panel, double _x, double _y, int _clickCount, boolean _publicOnly)
/* 169:    */   {
/* 170:229 */     if ((!_publicOnly) && (modelElementContains(_x, _y, -280.0D, 220.0D)))
/* 171:    */     {
/* 172:230 */       _panel.addInspect(-280.0D, 220.0D, this, "sources");
/* 173:231 */       return true;
/* 174:    */     }
/* 175:233 */     if ((!_publicOnly) && (modelElementContains(_x, _y, -280.0D, 27.0D)))
/* 176:    */     {
/* 177:234 */       _panel.addInspect(-280.0D, 27.0D, this, "kosmosnimki");
/* 178:235 */       return true;
/* 179:    */     }
/* 180:237 */     if ((!_publicOnly) && (modelElementContains(_x, _y, -280.0D, 77.0D)))
/* 181:    */     {
/* 182:238 */       _panel.addInspect(-280.0D, 77.0D, this, "landsat");
/* 183:239 */       return true;
/* 184:    */     }
/* 185:241 */     if ((!_publicOnly) && (modelElementContains(_x, _y, -280.0D, 127.0D)))
/* 186:    */     {
/* 187:242 */       _panel.addInspect(-280.0D, 127.0D, this, "germanStyle");
/* 188:243 */       return true;
/* 189:    */     }
/* 190:245 */     return false;
/* 191:    */   }
/* 192:    */   
/* 193:    */   public boolean onClickModelAt(Panel _panel, double _x, double _y, int _clickCount, boolean _publicOnly)
/* 194:    */   {
/* 195:250 */     if (onClickModelAt_Parameters_xjal(_panel, _x, _y, _clickCount, _publicOnly)) {
/* 196:250 */       return true;
/* 197:    */     }
/* 198:251 */     if (onClickModelAt_PlainVariables_xjal(_panel, _x, _y, _clickCount, _publicOnly)) {
/* 199:251 */       return true;
/* 200:    */     }
/* 201:252 */     return false;
/* 202:    */   }
/* 203:    */   
/* 204:    */   public TileSourceSelector(Engine engine, Agent owner, AgentArrayList<? extends TileSourceSelector> collection)
/* 205:    */   {
/* 206:261 */     super(engine, owner, collection);
/* 207:    */   }
/* 208:    */   
/* 209:    */   public void create()
/* 210:    */   {
/* 211:267 */     setupPlainVariables_xjal();
/* 212:    */     
/* 213:269 */     this.presentation = new ShapeTopLevelPresentationGroup(this, true, 0.0D, 0.0D, 0.0D, 0.0D, new Object[] { this.text2, this.combobox });
/* 214:270 */     this.icon = new ShapeGroup(this, true, 0.0D, 0.0D, 0.0D, new Object[0]);
/* 215:    */     
/* 216:    */ 
/* 217:273 */     assignInitialConditions_xjal();
/* 218:274 */     onCreate();
/* 219:    */   }
/* 220:    */   
/* 221:    */   public void start()
/* 222:    */   {
/* 223:279 */     onStartup();
/* 224:    */   }
/* 225:    */   
/* 226:    */   public void onStartup()
/* 227:    */   {
/* 228:283 */     super.onStartup();
/* 229:    */     
/* 230:285 */     this.sources = new TileImageProvider[] { new TileImageProvider(), this.kosmosnimki, this.landsat, this.germanStyle };
/* 231:    */   }
/* 232:    */   
/* 233:    */   public void setupPlainVariables_xjal()
/* 234:    */   {
/* 235:293 */     this.kosmosnimki = 
/* 236:294 */       new TileImageProvider("Kosmosnimki", "http://irs.gis-lab.info/?layers=irs&request=GetTile&z={zoom}&x={x}&y={y}", null, "Экспресс Космоснимки © ИТЦ СКАНЭКС", "http://kosmosnimki.ru/terms.html", null);
/* 237:    */     
/* 238:296 */     this.landsat = 
/* 239:297 */       new TileImageProvider("LandSat", "http://irs.gis-lab.info/?layers=landsat&request=GetTile&z={zoom}&x={x}&y={y}", null, null, null, null);
/* 240:    */     
/* 241:299 */     this.germanStyle = 
/* 242:300 */       new TileImageProvider("GermanStyle", "http://{rand4L}.tile.openstreetmap.de/tiles/osmde/{zoom}/{x}/{y}.png", "© OpenStreetMap", null, null, null);
/* 243:    */   }
/* 244:    */   
/* 245:    */   private void writeObject(ObjectOutputStream _stream)
/* 246:    */     throws IOException
/* 247:    */   {
/* 248:310 */     _stream.defaultWriteObject();
/* 249:311 */     writeCustomData(_stream);
/* 250:    */   }
/* 251:    */   
/* 252:    */   private void readObject(ObjectInputStream _stream)
/* 253:    */     throws IOException, ClassNotFoundException
/* 254:    */   {
/* 255:315 */     _stream.defaultReadObject();
/* 256:316 */     this._origin_VA.restoreOwner(this);
/* 257:317 */    // finishReadObject_xjal(_stream, TileSourceSelector.class);
/* 258:    */   }
/* 259:    */ }



/* Location:           D:\Universidad Nacional\SEPRO\3.Programa\Plataformas\AnyLogic\Cadena de Suministro Industria Panificadora Palmira\SCBIP\GIS_alternatives_for_AnyLogic\TileGISLibrary\

 * Qualified Name:     com.anylogic.tilemaplib.TileSourceSelector

 * JD-Core Version:    0.7.0.1

 */