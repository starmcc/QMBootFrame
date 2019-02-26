// ********基础js**********
/**
 * 全局初始化配置
 */
$(function () {

    // 禁用a标签自带的跳转效果。href="javascript:;"
    $(".qmui-a-click").attr('onclick', 'return false');
    $(".qmui-a-click").attr('href', 'javascript:;');
    // 当a标签节点被点击时替换iframe中的src地址达到切换页面效果
    $(".qmui-a-click").on('click', function () {
        let page = $(this).attr('qmui-page')
        let to = null;
        let js = null;
        $("pages").each(function (a) {
            if ($(this).attr('name') == page) {
                to = $(this).attr('to')
                js = $(this).attr('js')
                return
            }
        })
        // 当点击时直接load替换
        if (isEmpty(to)) {
            console.error("路由跳转失败！路由：[" + to + "]")
        }else {
            $('#qmui-app').load(to)
        }
        // 加载js
        if (isEmpty(js)) {
            console.error("路由js加载失败！js地址：[" + js + "]")
        }else {
            $.getScript(js)
        }

    })

    // 当li被点击时触发
    $(".qmui-li-click").on('click', function () {
        // 当检测到该节点被禁用时不做任何操作
        if ($(this).hasClass('disabled')) {
            return
        }
        // 该节点被点击时添加选中效果
        $('.qmui-li-click').removeClass('active')
        $(this).addClass('active')
    })

})

/**
 * 路由初始化
 */
function init(){
    let modules = $('module')
}

//判断字符是否为空的方法
function isEmpty(obj){
    if(typeof obj == "undefined" || obj == null || obj == ""){
        return true;
    }else{
        return false;
    }
}