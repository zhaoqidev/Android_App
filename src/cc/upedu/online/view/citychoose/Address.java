package cc.upedu.online.view.citychoose;

/**
 * 城市信息
 */
public class Address {
	/** 城市信息 */
	public static final String CITIES[] = {
		"热","北京市" ,"上海市" ,"重庆市" ,"天津市" ,"昆明市" ,"珠海市" ,
			"青岛市" ,"成都市" ,"石家庄市" ,"太原市" ,"沈阳市" ,"长春市" ,
			"哈尔滨市" ,"南京市" ,"杭州市" ,"合肥市" ,"福州市" ,"南昌市" ,
			"济南市" ,"郑州市" ,"武汉市" ,"长沙市" ,"广州市" ,"海口市" ,
			"贵阳市" ,"西安市" ,"兰州市" ,"西宁市" ,"台湾省" ,"呼和浩特市" ,
			"拉萨市" ,"南宁市" ,"银川市" ,"乌鲁木齐市" ,"香港" ,"澳门" ,
		"A","阿坝藏族羌族自治州" , "阿克苏地区" , "阿拉尔市" , "阿拉善盟" , "阿勒泰地区" , "阿里地区" ,
			"安康市" , "安庆市" , "安顺市" , "安阳市" , "鞍山市" , "澳门特别行政区" ,
		"B","巴彦淖尔市" , "巴音郭楞蒙古自治州" , "巴中市" , "白城市" , "白山市" , "白银市" ,
			"百色市" , "蚌埠市" , "包头市" , "宝鸡市" , "保定市" , "保山市" , "北海市" ,
			"北京市" ,"本溪市" ,"毕节地区" ,"滨州市" ,"亳州市" , "博尔塔拉蒙古自治州" ,
		"C","沧州市" , "昌都地区" , "昌吉回族自治州" , "长春市" , "长沙市" , "长治市" ,
			"常德市" , "常州市" , "巢湖市" , "朝阳市" , "潮州市" , "郴州市" ,"成都市" ,
			"承德市" , "池州市" , "赤峰市" , "崇左市" , "滁州市" , "楚雄彝族自治州" ,
		"D","达州市" , "大理白族自治州" , "大连市" , "大庆市" , "大同市" , "大兴安岭地区" ,
			"丹东市" , "德宏傣族景颇族自治州" , "德阳市" , "德州市" , "迪庆藏族自治州" , "定西市" ,
			"东莞市" , "东营市" ,
		"E","鄂尔多斯市" , "鄂州市" , "恩施土家族苗族自治州" ,
		"F","防城港市" , "佛山市" , "福州市" , "抚顺市" , "抚州市" , "阜新市" , "阜阳市" ,
		"G", "甘南藏族自治州" , "甘孜藏族自治州" , "赣州市" , "固原市" , "广安市" , "广元市" ,
			"广州市" , "贵港市" , "贵阳市" , "桂林市" , "果洛藏族自治州" ,
		"H", "哈尔滨市" , "哈密地区" , "海北藏族自治州" , "海东地区" , "海口市" , "海南藏族自治州" ,
			"海西蒙古族藏族自治州" , "邯郸市" , "汉中市" , "杭州市" , "合肥市" , "和田地区" ,
			"河池市" ,  "河源市" , "贺州市" , "鹤壁市" , "鹤岗市" , "黑河市" ,
			"衡水市" , "衡阳市" , "红河哈尼族彝族自治州" , "呼和浩特市" , "呼伦贝尔市" , "湖州市" ,
			"葫芦岛市" , "怀化市" , "淮安市" , "淮北市" , "淮南市" , "黄冈市" ,
			"黄南藏族自治州" , "黄山市" , "黄石市" , "惠州市" , "荷泽市" ,
//		"I",},	
		"J", "鸡西市" , "吉安市" , "吉林市" , "济南市" , "济宁市" , "佳木斯市" ,
			"嘉兴市" , "嘉峪关市" , "江门市" , "焦作市" , "揭阳市" , "金昌市" ,
			"金华市" , "锦州市" , "晋城市" , "晋中市" , "荆门市" , "荆州市" ,
			"景德镇市" , "九江市" , "酒泉市" ,
		"K", "喀什地区" , "开封市" , "克拉玛依市" , "克孜勒苏柯尔克孜自治州" , "昆明市" ,
		"L", "拉萨市" , "来宾市" , "莱芜市" , "兰州市" , "廊坊市" , "乐山市" ,
			"丽江市" , "丽水市" , "连云港市" , "凉山彝族自治州" , "辽阳市" , "辽源市" ,
			"聊城市" ,  "林芝地区" , "临沧市" , "临汾市" , "临夏回族自治州" , "临沂市" ,
			"柳州市" , "六安市" , "六盘水市" , "龙岩市" , "陇南市" , "娄底市" ,
			"泸州市" , "吕梁市" , "洛阳市" , "漯河市" ,
		"M", "马鞍山市" , "茂名市" , "眉山市" , "梅州市" , "绵阳市" , "牡丹江市" ,
		"N", "内江市" , "那曲地区" , "南昌市" , "南充市" , "南京市" , "南宁市" ,
			"南平市" , "南通市" , "南阳市" , "宁波市" , "宁德市" , "怒江傈僳族自治州" ,
//		"O",},	
		"P", "攀枝花市" , "盘锦市" , "平顶山市" , "平凉市" , "萍乡市" , "莆田市" , "濮阳市" ,
		"Q", "七台河市" , "齐齐哈尔市" , "黔东南苗族侗族自治州" , "黔南布依族苗族自治州" , "黔西南布依族苗族自治州" , "钦州市" ,
			"秦皇岛市" , "青岛市" , "清远市" , "庆阳市" , "曲靖市" , "衢州市" , "泉州市" ,
		"R", "日喀则地区" , "日照市" ,
		"S", "三门峡市" , "三明市" , "三亚市" , "山南地区" , "汕头市" , "汕尾市" ,
			"商洛市" , "商丘市" , "上海市" , "上饶市" , "韶关市" , "邵阳市" ,
			"绍兴市" , "深圳市" , "沈阳市" , "十堰市" , "石河子市" , "石家庄市" ,
			"石嘴山市" , "双鸭山市" , "朔州市" , "思茅市" , "四平市" , "松原市" ,
			"苏州市" , "宿迁市" , "宿州市" , "绥化市" , "随州市" , "遂宁市" , "神农架" ,
		"T", "塔城地区" , "台州市" , "太原市" , "泰安市" , "泰州市" , "唐山市" ,
			"天津市" , "天水市" , "铁岭市" , "通化市" , "通辽市" , "铜川市" ,
			"铜陵市" , "铜仁地区" , "图木舒克市" , "吐鲁番地区" , "台湾省" ,
//		"U",},
//		"V",},
		"W", "无锡市" , "威海市" , "潍坊市" , "渭南市" , "温州市" , "文山壮族苗族自治州" ,
			"乌海市" , "乌兰察布市" , "乌鲁木齐市" , "吴忠市" , "芜湖市" , "梧州市" ,
			"五家渠市" , "武汉市" , "武威市" ,
		"X", "西安市" , "西宁市" , "西双版纳傣族自治州" , "锡林郭勒盟" , "厦门市" , "咸宁市" ,
			"咸阳市" , "香港特别行政区" , "湘潭市" , "湘西土家族苗族自治州" , "襄樊市" , "孝感市" ,
			"忻州市" , "新乡市" , "新余市" ,"信阳市" , "兴安盟" , "邢台市" ,
			"徐州市" , "许昌市" , "宣城市" ,
		"Y", "雅安市" , "烟台市" , "延安市" , "延边朝鲜族自治州" , "盐城市" , "扬州市" ,
			"阳江市" , "阳泉市" , "伊春市" , "伊犁哈萨克自治州" , "宜宾市" , "宜昌市" ,
			"宜春市" , "益阳市" , "银川市" , "鹰潭市" , "营口市" , "永州市" ,
			"榆林市" , "玉林市" , "玉树藏族自治州" , "玉溪市" , "岳阳市" , "云浮市" , "运城市" ,
		"Z", "枣庄市" , "湛江市" , "张家界市" , "张家口市" , "张掖市" , "漳州市" ,
			"昭通市" , "肇庆市" , "镇江市" , "郑州市" , "中山市" , "中卫市" ,
			"重庆市" , "舟山市" , "周口市" , "株洲市" , "珠海市" , "驻马店市" ,
			"资阳市" , "淄博市" , "自贡市" , "遵义市" ,
	};
	public static final String[] C_TITLE = {
		"热",
		"A","B","C","D","E","F","G","H",//"I"
		"J","K","L","M","N",//"O"
		"P","Q","R","S","T",//"U","V"
		"W","X","Y","Z"
	};
	/** 城市编号数组 */
	public static final int[] C_ID = {
		-1, 33 , 107 , 270 , 35 , 303 , 235 ,
			171 , 273 , 37 , 48 , 71 , 85 ,
			94 , 109 , 122 , 133 , 150 , 159 ,
			170 , 187 , 204 , 218 , 279 , 267 ,
			294 , 326 , 336 , 350 , 3536 , 59 ,
			319 , 253 , 358 , 363 , 3532 , 3534 ,
		-1, 291 , 370 , 3528 , 70 , 376 , 324 , 334 , 140 , 297 , 191 , 73 , 3534 ,
		-1, 66 , 369 , 289 , 92 , 90 , 339 , 262 , 135 , 60 , 328 , 42 , 306 ,
			257 , 33 , 75 , 300 , 185 , 147 , 368 ,
		-1, 45 , 320 , 367 , 85 , 218 , 51 , 224 , 112 , 145 , 83 , 250 , 227 , 273 ,
			44 , 148 , 62 , 266 , 142 , 311 ,
		-1, 287 , 315 , 72 , 99 , 49 , 106 , 76 , 316 , 277 , 183 , 318 , 346 ,
			248, 174 ,
		-1, 64 , 209 , 216 ,
		-1, 258 , 237 , 150 , 74 , 168 , 79 , 143 ,
		-1, 349 , 292 , 165 , 361 , 286 , 279 , 232 , 260 , 294 , 255 , 355 ,
		-1, 94 , 366 , 352 , 351 , 267 , 354 , 357 , 40 , 332 , 122 , 133 , 373 ,
			264 , 245 , 263 , 192 , 97 , 104 , 47 , 221 , 312 , 59 , 65 , 126 ,
			84 , 229 , 116 , 138 , 136 , 213 , 353 , 141 , 205 , 242 , 186 ,
//		-1,},
		-1, 96 , 166 , 86 , 170 , 177 , 101 , 125 , 337 , 238 , 194 , 251 , 338 ,
			128 , 77 , 52 , 54 , 210 , 212 , 160 , 162 , 344 ,
		-1, 372 , 188 , 364 , 371 , 303 ,
		-1, 319 , 265 , 181 , 336 , 46 , 282 , 308 , 132 , 115 , 293 , 80 , 88 ,
			184 , 325 , 310 , 57 , 348 , 182 , 254 , 146 , 295 , 157 , 347 , 230 ,
			276 , 58 , 189 , 197 ,
		-1, 137 , 240 , 284 , 243 , 278 , 103 ,
		-1, 281 , 323 , 159 , 283 , 109 , 253 , 156 , 114 , 199 , 123 , 158 , 317 ,
//		-1,,
		-1, 275 , 81 , 190 , 343 , 161 , 152 , 195 ,
		-1, 102 , 95 , 301 , 302 , 299 , 259 , 39 , 171 , 247 , 345 , 304 , 129 , 154 ,
		-1, 322 , 180 ,
		-1, 198 , 153 , 268 , 321 , 236 , 244 , 335 , 200 , 107 , 169 , 233 , 222 ,
			127 , 234 , 71 , 206 , 377 , 37 , 359 , 98 , 53 , 309 , 87 , 91 ,
			113 , 121 , 144 , 105 , 215 , 280 , 217 ,
		-1, 375 , 131 , 48 , 178 , 120 , 38 , 35 , 340 , 82 , 89 , 63 , 327 ,
			139 , 298 , 3529 , 365 , 3536 ,
//		-1,},
//		-1,},
		-1, 110 , 179 , 176 , 330 , 124 , 313 , 61 , 67 , 363 , 360 , 134 , 256 ,
			3530 , 204 , 341 ,
		-1, 326 , 350 , 314 , 69 , 151 , 214 , 329 , 3532 , 220 , 231 , 208 , 211 ,
			56 , 193 , 163 , 201 , 68 , 41 , 111 , 196 , 149 ,
		-1, 288 , 175 , 331 , 93 , 117 , 118 , 246 , 50 , 100 , 374 , 285 , 207 ,
			167 , 226 , 358 , 164 , 78 , 228 , 333 , 261 , 356 , 305 , 223 , 252 , 55 ,
		-1, 173 , 239 , 225 , 43 , 342 , 155 , 307 , 241 , 119 , 187 , 249 , 362 ,
			270 , 130 , 202 , 219 , 235 , 203 , 290 , 172 , 274 , 296
	};
}