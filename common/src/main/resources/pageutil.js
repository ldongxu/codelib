/**
 * @author liudongxu06
 * @date 2017/10/12
 *
 * 分页的默认基本样式需要引入Bootstrap，也可以自定义样式class。
 */
(function ($) {
    if (typeof $ === 'undefined') {
        throwError('pageUtil requires jQuery.');
    }
    var pluginName = 'pagination';
    $.fn[pluginName] = function (options) {
        if (typeof options === 'undefined') {
            return this;
        }
        var container = $(this);
        var attributes = $.extend({}, $.fn[pluginName].defaults, options);
        var pagination = {
            initialize: function (page) {
                var self = this;
                var param = attributes.queryParam;
                param.page = page;
                param.pageSize = attributes.pageSize;

                $.getJSON(attributes.url, param, function (result) {
                    var ret = JSON.parse(result);
                    var total = ret.total;
                    var totalPage = Math.ceil(total / attributes.pageSize);
                    var cur = {
                        currentPage: page,
                        totalPage: totalPage
                    };
                    self.createTemplate(cur);
                    ret.totalPage=totalPage;
                    ret.currentPage=page;
                    attributes.dataCallback(ret);
                })
            },
            createTemplate: function (args) {
                var self = this;
                var currentPage = parseInt(args.currentPage);
                var totalPage = parseInt(args.totalPage);

                var pageRange = parseInt(attributes.pageRange);
                var rangeStart = parseInt(attributes.page);
                if (container.find('li[data-num]').length>0){
                    var curPages = [];
                    container.find('li[data-num]').each(function (i, v) {
                        curPages.push(parseInt($(this).attr('data-num')));
                    });
                    rangeStart = curPages.min();
                }

                if (currentPage<rangeStart){
                    var c = currentPage-pageRange;
                    if (c>0){
                        rangeStart=c;
                    }else {
                        rangeStart =1;
                    }
                }
                var rangeEnd = rangeStart+pageRange-1;
                if (rangeEnd>totalPage){
                    rangeEnd = totalPage;
                }

                var ulclass = attributes.ulClass;
                var prevText = attributes.prevText;
                var nextText = attributes.nextText;

                var html =  '<ul class="'+ulclass+'">';
                if (currentPage > 1) {
                    var pre = currentPage-1;
                    html += '<li data-num="'+pre+'"><a href="javascript:;"><span>'+prevText+'</span></a></li>';//上一页
                }

                for (var i=rangeStart;i<=rangeEnd;i++){
                    var cl = '';
                    if (i==currentPage){
                        cl = attributes.activeClass;
                    }
                    html += '<li  class="'+cl+'" data-num="'+i+'"><a href="javascript:;">'+i+'</a></li>';
                }
                if (currentPage<totalPage){
                    var next = currentPage+1;
                    html += '<li data-num="'+next+'"><a href="javascript:;"><span >'+nextText+'</span></a></li>';//下一页
                }
                html += '</ul>';
                container.html(html);
                container.find('li').each(function () {
                    var p = $(this).attr('data-num');
                    $(this).click(function () {
                        self.initialize(p);
                    })
                })
            },
            setQueryParam:function (param) {
                attributes.queryParam = param;
            }
        };
        pagination.initialize(attributes.page);
        return pagination;
    };

    $.fn[pluginName].defaults = {
        page: 1,
        pageSize: 10,
        pageRange: 5,
        prevText: '&laquo;',
        nextText: '&raquo;',
        activeClass: 'active',
        ulClass: 'pagination',
        url: '',
        queryParam: {},
        dataCallback: function (result) {//回调返回JSON数据{total: , data: ,totalPage, currentPage:}
            // console.log(result);
        }
    };

    // Array min
    Array.prototype.min = function () {
        var min = this[0];
        this.forEach(function (ele, index, arr) {
            if (ele < min) {
                min = ele;
            }
        });
        return min;
    };

    // Throw error
    function throwError(content) {
        throw new Error('pageUtil: ' + content);
    }


})(window.jQuery);