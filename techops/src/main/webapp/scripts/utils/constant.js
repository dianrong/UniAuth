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
        // 35 seconds Time out
        reqTimeout: 35000,
        pageSize: 20,
        maxPageSize: 4999,
        hackMaxPageSize: 5e7,
        loading: '正在加载...',
        createError: '创建失败',
        submitFail: '提交失败',
        loadEmpty: '暂无数据',
        loadError: '加载失败',
        treeNodeType: {
            group:'grp',
            memberUser:'mUser',
            ownerUser:'oUser'
        },
        commonStatus: [{
            name: '请选择'
        }, {
            name: '启用',
            value: 0
        }, {
            name: '禁用',
            value: 1
        }],
        success: [{
            name: '请选择'
        }, {
            name: '成功',
            value: 0
        }, {
            name: '失败',
            value: 1
        }],
        auditOrderBy: [{
            name: '请选择'
        }, {
            name: '耗时',
            value: 'req_elapse'
        }, {
            name: '日期',
            value: 'req_date'
        }, {
            name: '顺序',
            value: 'req_seq'
        }],
        ascDesc: [{
            name: '升序',
            value: 'asc'
        }, {
            name: '降序',
            value: 'desc'
        }],
        messageType: {
            info: 'info',
            danger: 'danger',
            success: 'success',
            warning: 'warning'
        }
    }
);
