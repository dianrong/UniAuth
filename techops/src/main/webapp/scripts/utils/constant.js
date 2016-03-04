/**
 * Module representing a shirt.
 * @module controllers/login
 */
define({
        apiBase: (function() {
            var origin = window.document.location.origin;
            if(origin.indexOf("://techops") > 0) {
                return origin;
            } else {
                return origin + "/techops";
            }
        })(),
        logout: (function() {
            var origin = window.document.location.origin;
            if(origin.indexOf("://techops") > 0) {
                return origin + "/logout/cas";
            } else {
                return origin + "/techops/logout/cas";
            }
        })(),
        errorCode: {
            LOGIN_REDIRECT_URL: 'LOGIN_REDIRECT_URL'
        },
        // 30 seconds Time out
        reqTimeout: 30000,
        pageSize: 50,
        hackMaxPageSize: 5e7,
        loading: '正在加载...',
        submiting: '提交中...',
        createError: '创建失败',
        loadEmpty: '暂无数据',
        loadError: '加载失败',
        treeNodeType: {
            group:'grp',
            memberUser:'mUser',
            ownerUser:'oUser'
        },
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
