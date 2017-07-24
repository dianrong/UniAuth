/**
 * Module representing a shirt.
 * @module controllers/login
 */
define({
        apiBase: (function() {
            var origin = window.document.location.origin;
            if(origin.indexOf("techops") > 0) {
                return origin;
            } else {
                return origin + "/techops";
            }
        })(),
        logout: (function() {
            var origin = window.document.location.origin;
            if(origin.indexOf("techops") > 0) {
                return origin + "/logout/cas";
            } else {
                return origin + "/techops/logout/cas";
            }
        })(),
        errorCode: {
            LOGIN_REDIRECT_URL: 'LOGIN_REDIRECT_URL'
        },
        
        // 35 seconds Time out
        reqTimeout: 35000,
        pageSize: 20,
        showPageNum: 20,
        smallPageSize: 5,
        maxPageSize: 4999,
        hackMaxPageSize: 5e7,
        statusEnable: 0,
        statusDisable: 1,
        loading: '正在加载...',
        loading_code:'constant.loading',
        createError: '创建失败',
        createError_code:'constant.createError',
        submitFail: '提交失败',
        submitFail_code:'constant.submitError',
        loadEmpty: 'No Data',
        loadEmpty_code:'constant.loadEmpty',
        loadError: '加载失败',
        loadError_code:'constant.loadingError',
        treeNodeType: {
            group:'grp',
            memberUser:'mUser',
            ownerUser:'oUser'
        },
        commonStatus: [{
            name: '请选择',
            code:'constant.selectplacehodler'
        }, {
            name: '启用',
            code:'constant.enable',
            value: 0
        }, {
            name: '禁用',
            code:'constant.disable',
            value: 1
        }],
        success: [{
            name: '请选择',
            code:'constant.selectplacehodler'
        }, {
            name: '成功',
            code:'constant.success',
            value: 0
        }, {
            name: '失败',
            code:'constant.failure',
            value: 1
        }],
        auditOrderBy: [{
            name: '请选择',
            code:'constant.selectplacehodler'
        }, {
            name: '耗时',
            code:'constant.timeConsuming',
            value: 'req_elapse'
        }, {
            name: '日期',
            code:'constant.date',
            value: 'req_date'
        }, {
            name: '顺序',
            code:'constant.order',
            value: 'req_seq'
        }],
        ascDesc: [{
            name: '升序',
            code:'constant.order.asc',
            value: 'asc'
        }, {
            name: '降序',
            code:'constant.order.desc',
            value: 'desc'
        }],
        messageType: {
            info: 'info',
            danger: 'danger',
            success: 'success',
            warning: 'warning'
        },
        cfgFields: ['id', 'cfgKey', 'cfgTypeId'],
        httpMethods: ['ALL', 'GET', 'POST', 'PUT', 'DELETE', 'HEAD', 'OPTIONS', 'PATCH', 'TRACE'],
        permType: {
            'URI_PATTERN':'URI_PATTERN',
            'REGULAR_PATTERN':'REGULAR_PATTERN',
            'DOMAIN':'DOMAIN',
            'PRIVILEGE':'PRIVILEGE'
        }
    }
);
