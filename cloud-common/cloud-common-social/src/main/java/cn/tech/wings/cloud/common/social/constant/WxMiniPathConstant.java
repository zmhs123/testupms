package cn.tech.wings.cloud.common.social.constant;

/**
 * @author zhengxc
 * @date 2022/7/13
 */
public interface WxMiniPathConstant {


    /**
     * 在线签约详情页面
     */
    String SIGN_ONLINE_DETAIL_PATH = "/pageB/signUp/details";


    /**
     * 在线签约详情页面  参数
     */
    String SIGN_ONLINE_DETAIL_PARAM = "signId={}";

    /**
     * 电量收集(前端给到)
     */
    String COLLECT_ELECTRIC_PATH = "pageC/collectElectricity/previewPageUrl";

    /**
     * 用电端——电量账单(前端给到)
     */
    String ELECTRICITY_BILL_PATH = "/pageA/bill/bill";

    /**
     * 电量收集H5中间页(前端给到)
     */
    String COLLECT_ELECTRIC_H5_PATH = "/#/pageA/keepRecord/empty";


}
