package cn.tech.wings.cloud.common.social.constant;

/**
 * @author: ZHENGXIAOCONG
 * @Description:
 * @date 2022/3/113:10
 */
public interface WxConfigProperties {

    /**
     * redis key前缀
     */
    String keyPrefix = "wx";

    /**
     * PowerShare 公众号
     */
    String MP_APP_ID = "wxcb360a39200e1290";
    String MP_APP_SECRET = "b104442921e1544e90c7122f587237dd";

    /**
     *  公众号
     */
    String ELECTRIC_SELL_MP_APP_ID = "";
    String ELECTRIC_SELL_MP_APP_SECRET = "";


    /**
     *   售电
     */
    String ELECTRIC_SELL_MINI_APP_ID = "wx0f87de2b248f86a9";
    String ELECTRIC_SELL_MINI_APP_SECRET = "aa7e459370a94e1a263480ede063d4fd";


    /**
     *  用电
     */
    String ELECTRIC_USE_MINI_APP_ID = "wx48ed3a4c0b7bb74f";
    String ELECTRIC_USE_MINI_APP_SECRET = "f5016636b12390e7ccbd58f0c9591cdb";


    /**
     *  经纪
     */
    String ELECTRIC_AGENT_MINI_APP_ID = "wxc653a1148e50b769";
    String ELECTRIC_AGENT_MINI_APP_SECRET = "a07f16d8ff0d85918b30ebd4ec2c7fbb";

    /**
     * 测试appId
     */
    String TEST_USE_MINI_APP_ID = "wx096149736f5679ce";
    String TEST_USE_MINI_APP_SECRET = "1cd17f2ec58ff129ff0ca78a97c263ec";
}

