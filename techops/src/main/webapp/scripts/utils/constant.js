/**
 * Module representing a shirt.
 * @module controllers/login
 */
define({
        apiBase: window.document.location.origin + "@apibase@",
        // 20 seconds Time out
        reqTimeout: 20000,
        pageSize: 50,
        hackMaxPageSize: 5e7,
        loading: '正在加载...',
        submiting: '提交中...',
        createError: '创建失败',
        loadEmpty: '暂无数据',
        loadError: '加载失败',
        channelStatusArr: [{
            name: '请选择',
            value: ''
        }, {
            name: '开启',
            value: 'open'
        }, {
            name: '未开启',
            value: 'un_open'
        }, {
            name: '禁用',
            value: 'forbidden'
        }],
        projectStatusDic: [{
            name: '新增立项中',
            value: 'creating'
        }, {
            name: '尽调资料审核中',
            value: 'due_diligence_verifying'
        }, {
            name: '现场尽调中',
            value: 'due_diligence_at_the_scene'
        }, {
            name: '补件中',
            value: 'patching'
        }, {
            name: '渠道立项初审中',
            value: 'first_trial_approving'
        }, {
            name: '渠道立项批复中',
            value: 'final_approving'
        }, {
            name: '法律文件准备中',
            value: 'legal_document_preparing'
        }, {
            name: '取消立项确认中',
            value: 'cancel_pending'
        }, {
            name: '渠道立项已拒绝',
            value: 'rejected'
        }, {
            name: '渠道立项已取消',
            value: 'cancelled'
        }, {
            name: '额度调整建议中',
            value: 'quota_adjust_applying'
        }, {
            name: '额度调整批复中',
            value: 'quota_adjust_approving'
        }, {
            name: '法律文件更新中',
            value: 'legal_document_updating'
        }, {
            name: '渠道立项已完成',
            value: 'created_complete'
        }],
        projectProgressTracker: [{
            name: '立项申请已提交',
            value: ['creating', 'due_diligence_verifying', 'patching']
        }, {
            name: '尽调资料审核完成',
            value: ['due_diligence_at_the_scene']
        }, {
            name: '现场尽调完成',
            value: []
        }, {
            name: '尽调报告已上传',
            value: ['first_trial_approving']
        }, {
            name: '渠道立项初审完成',
            value: ['final_approving', 'cancel_pending', 'rejected', 'cancelled']
        }, {
            name: '批准立项',
            value: ['legal_document_preparing']
        }, {
            name: '法律文件已上传',
            value: []
        }, {
            name: '渠道立项已完成',
            value: ['created_complete', 'quota_adjust_applying', 'legal_document_updating', 'quota_adjust_approving']
        }],
        projectProgressButton: [{
            name: '补件',
            value: 'patching',
            url: 'modify',
            perm: 'projectAdd'
        }, {
            name: '审核尽调资料',
            value: 'due_diligence_verifying',
            url: 'research',
            perm: 'researchReview'
        }, {
            name: '上传尽调报告',
            value: 'due_diligence_at_the_scene',
            url: 'research/report',
            perm: 'researchReportUpload'
        }, {
            name: '渠道立项初审',
            value: 'first_trial_approving',
            url: 'review/request',
            perm: 'projectReview'
        }, {
            name: '渠道立项批复',
            value: 'final_approving',
            url: 'review/request/decision',
            perm: 'projectReviewDecision'
        }, {
            name: '上传法律文件',
            value: 'legal_document_preparing',
            url: 'law/attachment',
            perm: 'lawUpload'
        }, {
            name: '更新法律文件',
            value: 'legal_document_updating',
            url: 'law/attachment',
            perm: 'lawUpdate'
        }, {
            name: '取消立项审核',
            value: 'cancel_pending',
            url: 'cancel',
            perm: 'projectCancelReview'
        }, {
            name: '调整额度',
            value: 'created_complete',
            url: 'amount/request',
            perm: 'amountRequest'
        }, {
            name: '调整额度',
            value: 'quota_adjust_applying',
            url: 'amount/request',
            perm: 'amountRequest'
        }, {
            name: '额度调整批复',
            value: 'quota_adjust_approving',
            url: 'amount/request/decision',
            perm: 'amountDecisionApprove'
        }],
        organizationCodeArr: [{
            name: '请选择',
            value: ''
        }, {
            name: 'INS001_分公司',
            value: 'INS001'
        }, {
            name: 'INS002_小贷公司',
            value: 'INS002'
        }, {
            name: 'INS003_担保公司',
            value: 'INS003'
        }, {
            name: 'INS004_融资租赁',
            value: 'INS004'
        }, {
            name: 'INS005_企业',
            value: 'INS005'
        }, {
            name: 'INS006_保理公司',
            value: 'INS006'
        }, {
            name: 'INS007_中介',
            value: 'INS007'
        }, {
            name: 'INS008_线上',
            value: 'INS008'
        }, {
            name: 'INS099_其他',
            value: 'INS099'
        }],
        industryCodeArr: [{
            name: '请选择',
            value: ''
        }, {
            name: 'I001_农、林、牧、渔业',
            value: 'I001'
        }, {
            name: 'I002_采矿业',
            value: 'I002'
        }, {
            name: 'I003_制造业',
            value: 'I003'
        }, {
            name: 'I004_电力、热力、燃气及水的生产和供应业',
            value: 'I004'
        }, {
            name: 'I005_建筑业',
            value: 'I005'
        }, {
            name: 'I006_批发和零售业',
            value: 'I006'
        }, {
            name: 'I007_交通运输、仓储和邮政业',
            value: 'I007'
        }, {
            name: 'I008_住宿和餐饮业',
            value: 'I008'
        }, {
            name: 'I009_信息传输、软件和信息技术服务业',
            value: 'I009'
        }, {
            name: 'I010_金融业',
            value: 'I010'
        }, {
            name: 'I011_房地产业',
            value: 'I011'
        }, {
            name: 'I012_租赁和商务服务业',
            value: 'I012'
        }, {
            name: 'I013_科学研究和技术服务业',
            value: 'I013'
        }, {
            name: 'I014_水利、环境和公共设施管理业',
            value: 'I014'
        }, {
            name: 'I015_居民服务、修理和其他服务业',
            value: 'I015'
        }, {
            name: 'I016_教育',
            value: 'I016'
        }, {
            name: 'I017_卫生和社会工作',
            value: 'I017'
        }, {
            name: 'I018_文化、体育和娱乐业',
            value: 'I018'
        }, {
            name: 'I019_公共管理、社会保障喝社会组织',
            value: 'I019'
        }, {
            name: 'I020_贸易行业',
            value: 'I20'
        }, {
            name: 'I021_供应链',
            value: 'I21'
        }, {
            name: 'I099_其他',
            value: 'I099'
        }],
        curUnitTypes: [{
            name: '人民币',
            value: 'CNY'
        }],
        updateChargerArr: [{
            name: '请选择',
            value: ''
        }, {
            name: '是',
            value: 'true'
        }, {
            name: '否',
            value: 'false'
        }],
        adjustmentApprovalArr: [{
            name: '请选择',
            value: ''
        }, {
            name: '批准',
            value: 'approve'
        }, {
            name: '退回',
            value: 'suggesting'
        }, {
            name: '拒绝',
            value: 'reject'
        }],
        researchRequestItems: [{
            name: '请选择',
            value: ''
        }, {
            name: '通过',
            value: 'approve'
        }, {
            name: '拒绝',
            value: 'deny'
        }, {
            name: '需补件',
            value: 'notComplete'
        }],
        reviewRequestItems: [{
            name: '请选择',
            value: ''
        }, {
            name: '通过',
            value: 'approve'
        }, {
            name: '拒绝',
            value: 'deny'
        }, {
            name: '需补件',
            value: 'notComplete'
        }],
        reviewDecisionItems: [{
            name: '请选择',
            value: ''
        }, {
            name: '通过',
            value: 'approve'
        }, {
            name: '拒绝',
            value: 'deny'
        }, {
            name: '退回',
            value: 'returnBack'
        }],
        modlifyUploadFiletype: [{
            name: '公司项目或业务资料',
            value: 'company_project_or_business_doc'
        }, {
            name: '公司财务资料',
            value: 'company_financial_doc'
        }, {
            name: '其他',
            value: 'others'
        }],
        reviewUploadFileType: [{
            name: '基本资料',
            value: 'company_base_doc'
        },{
            name: '渠道调整补充资料',
            value: 'channel_adjust_supplementary_doc'
        },{
            name: '尽调报告',
            value: 'due_diligence_report'
        },{
            name: '战略合作协议',
            value: 'strategic_cooperation_agreement'
        },{
            name: '担保函',
            value: 'guarantee_doc'
        },{
            name: '额度调整补充资料',
            value: 'quota_adjust_supplementary_doc'
        },{
            name: '公司项目或业务资料',
            value: 'company_project_or_business_doc'
        }, {
            name: '公司财务资料',
            value: 'company_financial_doc'
        }, {
            name: '立项初审补充资料',
            value: 'project_first_trial_supplementary_doc'
        }, {
            name: '其他',
            value: 'others'
        }],
        amountType: [{
            name: '债权转让',
            value: 'CREDIT_ASSIGN'
        }, {
            name: '新增贷款',
            value: 'NEW_LOAN'
        }, {
            name: '债转或新增',
            value: 'CREDIT_OR_NEW'
        }, {
            name: '其他',
            value: 'OTHRE'
        }],
        lawType: [{
            name: '请选择',
            value: ''
        }, {
            name: '战略合作协议',
            value: 'strategic_cooperation_agreement'
        }, {
            name: '担保函',
            value: 'guarantee_doc'
        }, {
            name: '其他',
            value: 'others'
        }],
        nextStepTypes: [{
            name: '请选择',
            value: ''
        }, {
            name: '确认取消',
            value: 'cancel'
        }, {
            name: '无须取消，退回补件',
            value: 'back_patch'
        }, {
            name: '无须取消，继续上传法律文件',
            value: 'law_doc'
        }],
        amountRequestType: [{
            name: '请选择',
            value: ''
        }, {
            name: '增加额度',
            value: 'INCREASE'
        }, {
            name: '减少额度',
            value: 'DECREASE'
        }, {
            name: '冻结额度',
            value: 'FREEZE'
        }, {
            name: '恢复额度',
            value: 'RECOVER'
        }, {
            name: '更新额度期限',
            value: 'TIME_LIMIT'
        }],
        amountRequestObj: {
            increase: '增加额度',
            decrease: '减少额度',
            freeze: '冻结额度',
            recover: '恢复额度',
            time_limit: '更新额度期限'
        },
        amountAttachmentType: [{
            name: '额度调整补充资料',
            value: 'quota_adjust_supplementary_doc'
        }],
        amountDecisionType: [{
            name: '请选择',
            value: ''
        }, {
            name: '批准',
            value: 'approve'
        }, {
            name: '退回',
            value: 'suggesting'
        }, {
            name: '拒绝',
            value: 'reject'
        }],
        CITY_GROUP: {
            "北京": ["北京"],
            "天津": ["天津"],
            "河北": ["石家庄", "保定", "沧州", "承德", "邯郸", "衡水", "廊坊", "秦皇岛", "唐山", "邢台", "张家口"],
            "山西": ["太原", "长治", "大同", "晋城", "临汾", "朔州", "忻州", "阳泉", "运城", "晋中", "吕梁"],
            "内蒙古": ["呼和浩特", "包头", "阿拉善", "巴彦淖尔", "赤峰", "呼伦贝尔", "乌海", "乌兰察布", "锡林郭勒", "兴安", "鄂尔多斯", "通辽"],
            "辽宁": ["沈阳", "大连", "鞍山", "本溪", "朝阳", "丹东", "抚顺", "阜新", "葫芦岛", "锦州", "辽阳", "盘锦", "铁岭", "营口"],
            "吉林": ["长春", "白城", "白山", "吉林", "辽源", "四平", "松原", "通化", "延边"],
            "黑龙江": ["哈尔滨", "大庆", "大兴安岭", "鹤岗", "黑河", "鸡西", "佳木斯", "牡丹江", "七台河", "齐齐哈尔", "双鸭山", "绥化", "伊春"],
            "上海": ["上海"],
            "江苏": ["南京", "常州", "淮安", "连云港", "南通", "苏州", "宿迁", "泰州", "无锡", "徐州", "盐城", "扬州", "镇江"],
            "浙江": ["杭州", "宁波", "湖州", "嘉兴", "金华", "绍兴", "台州", "温州", "舟山", "衢州", "丽水"],
            "安徽": ["合肥", "安庆", "蚌埠", "巢湖", "池州", "滁州", "阜阳", "淮北", "淮南", "黄山", "六安", "马鞍山", "铜陵", "芜湖", "宣城", "亳州", "宿州"],
            "福建": ["福州", "厦门", "龙岩", "南平", "宁德", "莆田", "泉州", "三明", "漳州"],
            "江西": ["南昌", "抚州", "赣州", "吉安", "景德镇", "九江", "萍乡", "上饶", "新余", "宜春", "鹰谭"],
            "山东": ["济南", "青岛", "滨州", "德州", "东营", "菏泽", "济宁", "莱芜", "聊城", "临沂", "日照", "泰安", "威海", "潍坊", "烟台", "枣庄", "淄博"],
            "河南": ["郑州", "安阳", "焦作", "鹤壁", "开封", "洛阳", "南阳", "平顶山", "三门峡", "商丘", "新乡", "信阳", "许昌", "周口", "驻马店", "漯河", "濮阳"],
            "湖北": ["武汉", "鄂州", "恩施", "黄冈", "黄石", "荆门", "荆州", "十堰", "随州", "咸宁", "襄樊", "孝感", "宜昌", "天门"],
            "湖南": ["长沙", "常德", "郴州", "衡阳", "怀化", "娄底", "邵阳", "湘潭", "益阳", "永州", "岳阳", "张家界", "吉首", "株州"],
            "广东": ["广州", "深圳", "潮州", "东莞", "佛山", "惠州", "江门", "揭阳", "茂名", "梅州", "清远", "汕头", "汕尾", "韶关", "阳江", "云浮", "湛江", "肇庆", "中山", "河源", "珠海"],
            "广西": ["南宁", "百色", "北海", "桂林", "河池", "柳州", "梧州", "玉林", "崇左", "防城港", "贵港", "贺州", "来宾", "钦州"],
            "海南": ["海口", "三亚"],
            "重庆": ["重庆", "涪陵", "万州", "黔江"],
            "四川": ["成都", "巴中", "达州", "德阳", "广安", "广元", "乐山", "凉山", "眉山", "绵阳", "南充", "内江", "攀枝花", "遂宁", "雅安", "宜宾", "自贡", "泸州", "阿坝", "甘孜", "资阳"],
            "贵州": ["贵阳", "安顺", "毕节", "六盘水", "铜仁", "遵义", "黔东南", "黔南", "黔西南"],
            "云南": ["昆明", "西双版纳", "保山", "楚雄", "大理", "德宏", "红河", "丽江", "临沧", "怒江", "曲靖", "思茅", "文山", "玉溪", "昭通", "迪庆州"],
            "西藏": ["拉萨", "阿里", "昌都", "林芝", "那曲", "日喀则", "山南", "樟木口岸"],
            "陕西": ["西安", "安康", "宝鸡", "汉中", "商洛", "铜川", "渭南", "咸阳", "延安", "榆林"],
            "甘肃": ["兰州", "白银", "定西", "嘉峪关", "金昌", "酒泉", "临夏", "陇南", "平凉", "庆阳", "天水", "武威", "张掖", "甘南藏族"],
            "青海": ["西宁", "海东", "玉树", "海北藏族自治州", "黄南藏族自治州", "果洛藏族自治州", "海西蒙古族藏族自治州", "海南藏族自治州"],
            "宁夏": ["银川", "中卫", "固原", "石嘴山", "吴忠"],
            "新疆": ["乌鲁木齐", "阿克苏", "阿勒泰", "昌吉", "哈密", "和田", "喀什", "克拉玛依", "石河子", "塔城", "吐鲁番", "伊犁", "博尔塔拉蒙古自治州", "克孜勒苏柯尔克孜自治州", "巴音郭楞蒙古自治州"]
        },
        accountUpdateType: [{
            name: '请选择',
            value: ''
        }, {
            name: '渠道方账户',
            value: 'CHANNEL_ACCOUNT'
        }, {
            name: '借款人账户',
            value: 'BORROWER_ACCOUNT'
        }],
        accountProperty: [{
            name: '请选择',
            value: ''
        }, {
            name: '对公账户',
            value: 'public_account'
        }, {
            name: '对私账户',
            value: 'private_account'
        }],
        researchResultText: {
            'PASS': '通过',
            'REJECT': '拒绝',
            'SUPPLY_DOC': '需补件'
        },
        // 额度状态
        amountStatusText: {
            'APPROVING': '审批中',
            'NORMAL': '正常',
            'FREEZE': '冻结',
            'CANCEL': '取消'
        },
        amountStatusList: [{
            name: '立项审批中',
            value: 'APPROVING'
        }, {
            name: '正常',
            value: 'NORMAL'
        }, {
            name: '冻结',
            value: 'FREEZE'
        }, {
            name: '取消',
            value: 'CANCEL'
        }],
        permissions: {
            '^/channel/get$': 'channelSearch', //查询渠道--页面入口
            '^/channel/post$': 'channelCreate', //创建渠道--按钮
            '^/channel/.*/project$': 'projectSearch', //渠道立项查询菜单
            '^/channel/.*/adjustment/request$': 'channelAdjustmentRequest', //渠道调整按钮
            '^/channel/.*/adjustment/.*/approve$': 'channelAdjustmentApprove', //渠道调整批复
            '^/channel/[0-9]+/project/[0-9]+/setup/request/decision$': 'projectCancelReview', //立项取消批复
            '^/channel/[0-9]+/project/[0-9]+/research/review/approve$': 'researchReview', //尽调资料审核
            '^/channel/.+/project/.+/research/report$': 'researchReportUpload', //上传尽调报告
            '^/channel/.+/project/.+/review/approve$': 'projectReview', //渠道立项初审
            '^/channel/.+/project/.+/decision/approve$': 'projectReviewDecision', //渠道立项批复
            '^/channel/.+/project/.+/law/complete$': 'lawUpload', //上传法律文件 - 完成立项
            '^/channel/.+/project/.+/law/update$': 'lawUpdate', //更新法律文件
            '^/channel/[0-9]+/project/[0-9]+/amount/request$': 'amountRequest', //申请额度调整
            '^/channel/[0-9]+/project/[0-9]+/amount/review$': 'amountDecisionApprove', //额度调整批复
            '^/channel/.*/project/add$': 'projectAdd', //新增立项 +
            '^/channel/.+/project/.+/modify$': 'projectModify', //编辑立项
            '^/channel/[0-9]+/project/[0-9]+/amount/request/attachment/[0-9]+$': 'amountAdjustmentDownload', //额度调整资料下载
            '^/channel/[0-9]+/project/[0-9]+/review$': 'reviewResultDetail', //渠道立项初审所有详情
            '^/channel/[0-9]+/project/[0-9]+/research/report$': 'reportList' //获取尽调报告列表
        }
    }
);
