package com.phantoms.helper.common.constants;

/**
 * 淘宝应用相关常量
 * 
 * @author zyj
 * @version [1.0, 2014年5月20日]
 * @since [base/1.0]
 */
public interface WxConstants {
    /** WX TICKET **/
    public static final String COM_VERIFY_TICKET = "COM_VERIFY_TICKET";

    /** wx com_access_toker**/
    public static final String COM_ACCESS_TOKEN = "COM_ACCESS_TOKEN";

    /** wx authorization_access_token 需要添加appid参数 **/
    public static final String AUTHORIZATION_ACCESS_TOKEN = "AUTHORIZATION_ACCESS_TOKEN_";


    /**
     * 微信实时消息订阅推送频道
     */
    public static final String WX_USER_NEWS_CHANNEL = "WX_USER_NEWS_CHANNEL";

    /**
     * 微信临时二维码队列池
     */
    public static final String WX_TEMP_QRCODE_QUENE = "WX_TEMP_QRCODE_POOL_QUENE_";

    /**
     * 微信临时二维码队列池容量
     */
    public static final String WX_TEMP_QRCODE_QUENE_CAPACITY = "WX_TEMP_QRCODE_POOL_QUENE_CAPACITY_";

    /**
     * 预览微信素材key
     * 
     */
    public static final String PREVIEW_MATETIAL_KEY = "PREVIEW_MATETIAL_KEY";


    /**
     * 预览素材文章key
     *
     */
    public static final String PREVIEW_ARTICLE_KEY = "PREVIEW_ARTICLE_KEY";

    /**
     * 预览素材文章key
     *
     */
    public static final String PREVIEW_DEFAULTREPLY_KEY = "PREVIEW_DEFAULT_REPLY_KEY";

    /**
     *  用户授权连接
     */
    public static final String AUTHORIZER_AUTH_URL = "https://mp.weixin.qq.com/cgi-bin/componentloginpage?component_appid=";

    /**
     * 微信网页授权登录URL
     */
    public static final String WX_USER_AUTH_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=AUTHORIZE_APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE&component_appid=COMPONENT_APPID#wechat_redirect";
   
    /**
     * 获取微信用户授权登录 信息
     */
    public static final String WX_USER_INFO_URL = "https://api.weixin.qq.com/sns/oauth2/component/access_token?appid=AUTHORIZE_APPID&code=CODE&grant_type=authorization_code&component_appid=COMPONENT_APPID&component_access_token=COMPONENT_ACCESS_TOKEN";

    /**
     * 创建二维码 access token
     */
    public static final String QRCODE_TICKET = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=";

    public static final String charset="UTF-8";
    public static final String user_info_list = "user_info_list";

    int DOC_SIZE = 1000 * 1000;
    String CURRENT_USER = "current_user";
    public static final String SCENEID_OUT_OF_RANGE = "场景值超过微信支持范围，请联系管理员处理";
    String MENU = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=";

    static class wxUser{
        public static final String SUBSCRIBE = "subscribe";
        public static final String OPENID = "openid";
        public static final String NICKNAME = "nickname";
        public static final String SEX = "sex";
        public static final String LANGUAGE = "language";
        public static final String CITY = "city";
        public static final String PROVINCE = "province";
        public static final String COUNTRY = "country";
        public static final String HEADIMGURL = "headimgurl";
        public static final String SUBSCRIBE_TIME = "subscribe_time";
        public static final String UNIONID = "unionid";
        public static final String REMARK = "remark";
        public static final String GROUPID = "groupid";
        public static final String TAGID_LIST = "tagid_list";
        public static final String PUBLICNAME = "publicName";
        public static final String WEIXIN_OPENID = "weixin.openid";
        public static final String TAGNAME_LIST = "tagName_list";
        public static final String SUBSCRIBE_DATE = "subscribe_date";
    }

    public static class ResponBody{
        public static final String CONTENT = "content";
        public static final String MSG_TYPE = "MsgType";
        public static final String MSG_ID = "MsgId";
        public static final String TO_USER_NAME = "ToUserName";
        public static final String FORM_USER_NAME = "FromUserName";

        public static final String TEXT="text";
        public static final String IMAGE = "image";
        public static final String VOICE = "voice";
        public static final String VIDEO = "video";
        public static final String LOCATION = "location";
        public static final String LINK = "link";
        public static final String EVENT = "event";
        public static final String EVENTKEY = "EventKey";
        public static final String LATITUDE = "Latitude";
        public static final String LONGITUDE = "Longitude";
        public static final String PRECISION = "Precision";
        public static final String PIC_URL = "Pic_url";
        public static final String LOCATION_X = "Location_X";
        public static final String LOCATION_Y = "Location_Y";
        public static final String SCALE = "Scale";
        public static final String LABEL = "Label";
        public static final String FORMAT = "format";
        public static final String TITLE = "Title";
        public static final String DESCRIPTION = "Description";
        public static final String URL = "Url";
        public static final String THUMBMEDIAID = "ThumbMediaId";
        public static final String RECOGNITION = "Recognition";
        public static final String SUBSCRIBE = "subscribe";
        public static final String UNSUBSCRIBE = "unsubscribe";
        public static final String TICKET = "Ticket";
        public static final String LIST = "list";
        public static final String NEW_USER = "new_user";
        public static final String CANCEL_USER = "cancel_user";
        public static final String CUMULATE_USER = "cumulate_user";
        public static final String MEDIAID = "MediaId";
        public static final String SHORTVIDEO = "shortvideo";
        public static final String unknown = "unknown";
        public static final String MASSSENDJOBFINISH = "MASSSENDJOBFINISH";
    }

    /**
     * 微信事件推送Key
     */
    public static class WxEventKey{
        /**
         * 关注（subscribe）
         */
        public static final String SUBSCRIBE = "subscribe";
        /**
         * 取消关注（unsubscribe）
         */
        public static final String UNSUBSCRIBE = "unsubscribe";
        /**
         * 上报地理位置（LOCATION）
         */
        public static final String LOCATION = "LOCATION";
        /**
         * 扫描带参数二维码（SCAN）
         */
        public static final String SCAN = "SCAN";
        /**
         * 自定义菜单拉取消息（CLICK）
         */
        public static final String CLICK = "CLICK";
        /**
         * 点击菜单跳转（VIEW）
         */
        public static final String VIEW = "VIEW";
        /**
         * 扫码推事件（scancode_push）
         */
        public static final String SCANCODE_PUSH = "scancode_push";
        /**
         * 扫描显示消息接受中（scancode_waitmsg）
         */
        public static final String SCANCODE_WAITMSG = "scancode_waitmsg";
        /**
         * 弹出系统拍照发图（pic_sysphoto）
         */
        public static final String PIC_SYSPHOTO = "pic_sysphoto";
        /**
         * 弹出拍照或者相册发图（pic_photo_or_album）
         */
        public static final String PIC_PHOTO_OR_ALBUM = "pic_photo_or_album";
        /**
         * 弹出微信相册发图器（pic_weixin）
         */
        public static final String PIC_WEIXIN = "pic_weixin";
        /**
         * 弹出地理位置选择器（location_select）
         */
        public static final String LOCATION_SELECT = "location_select";
        /**
         * 模板消息送达情况提醒（TEMPLATESENDJOBFINISH）
         */
        public static final String TEMPLATE_SEND_JOB_FINISH = "TEMPLATESENDJOBFINISH";
        /**
         * 群发消息后的通知（MASSSENDJOBFINISH）
         */
        public static final String MASS_SEND_JOB_FINISH = "MASSSENDJOBFINISH";
        /**
         * 微信小店订单支付后的通知（merchant_order）
         */
        public static final String MERCHANT_ORDER = "merchant_order";
        /**
         * 资质认证成功（qualification_verify_success）
         */
        public static final String QUALIFICATION_VERIFY_SUCCESS = "qualification_verify_success";
        /**
         * 资质认证失败（qualification_verify_fail）
         */
        public static final String QUALIFICATION_VERIFY_FAIL= "qualification_verify_fail";
        /**
         * 名称认证成功（naming_verify_success）
         */
        public static final String NAMING_VERIFY_SUCCESS = "naming_verify_success";
        /**
         * 名称认证失败（naming_verify_fail）
         */
        public static final String NAMING_VERIFY_FAIL = "naming_verify_fail";
        /**
         * 年审通知（annual_renew）
         */
        public static final String ANNUAL_RENEW = "annual_renew";
        /**
         * 认证过期失效通知（verify_expired）
         */
        public static final String VERIFY_EXPIRED = "verify_expired";
    }
    
    
    public static class WebMessage{
        public static final String SAVE_SUCCESS = "save success";
        public static final String SAVE_FAIL = "save fail";
        public static final String CHECK_CONDITION = "删除失败，请检查是否勾选了消息";
        public static final String DELETE_SUCCESS = "删除成功";
        public static final String NOT_DELETE = "微信仅支持图文、视频类型消息的删除";
        public static final String WEI_SERVER_ERROR = "微信方服务器删除异常";
    }
}
