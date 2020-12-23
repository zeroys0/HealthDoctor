package net.leelink.healthdoctor.util;

public class Urls {
//    public static final String IP = "http://api.llky.net:";
    public static String IP = "";
    public static String H5_IP = "";
    public static String C_IP = "";

    public static Urls instance;

    public  String WEBSITE = IP+"/jk/doctoerAngel/";
    public  String C_WEBSITE =IP+"/sh/customer/";
    public  String U_WEBSITE = IP+"/sh/user/";
    public  String S_WEBSITE = IP+"/sysDict/";
    public  String H_WEBSITE = IP+"/jk/healthAngel/";
    public  String F_WEBSITE = IP+"/file-manager/";


    public static Urls getInstance(){
        if(instance ==null){
            return new Urls();
        }
        return instance;
    }

    //获取商户编码
    public static String PARTNER_CODE = "http://api.llky.net:8888/partner/user/";

    //h5健康数据地址
    public static final String WEB =H5_IP+"/#/AppData";

    public  String IMG_URL = IP+"/files";



    //发送短信验证码
    public  String SEND = U_WEBSITE +"send";

    //注册
    public  String REGIST = WEBSITE + "regist";

    //密码登录
    public  String LOGIN = WEBSITE+"login";

    //验证码登录
    public  String LOGIN_SMS = WEBSITE +"login-sms";

    //查询我的账户
    public  String BIND_BANK =WEBSITE +"bind-bank";

    //删除绑定银行卡
    public String BINK_BANK = WEBSITE +"bink-bank";

    //查询支持的银行
    public  String BANK = U_WEBSITE +"bank";

    //修改密码
    public  String PASSWORD = WEBSITE +"password";

    //获取省列表
    public  String GETPROVINCE = S_WEBSITE +"getProvince";

    //获取城市列表
    public  String GETCITY = S_WEBSITE +"getCity";

    //获取区县列表
    public  String GETCOUNTY = S_WEBSITE+"getCounty";

    //获取医院列表
    public  String HOSPITAL = S_WEBSITE +"hospital";

    //查询开关设置
    public  String OPEN_OPTION = WEBSITE +"open-option";

    //查询排班时间
    public  String RECEPTION = WEBSITE +"reception";

    //查询个人信息
    public  String USERINFO = WEBSITE +"userinfo";

    //设置问诊价格
    public  String SET_AMOUNT = WEBSITE +"set-amount";

    //查询患者分组列表
    public  String FAMILY_GROUP = H_WEBSITE +"family-group";

    //修改分组名称
    public  String GROUP_NAME = H_WEBSITE +"group-name";

    //是否成为家庭医生
    public  String IN_DOCKER = WEBSITE +"in-docker";

    //申请成为家庭医生
    public  String FAMILY_DOCTOR = WEBSITE +"family-doctor";

    //上传图片获得地址
    public  String PHOTO = F_WEBSITE+"photo";

    //上传音频获取音频地址
    public  String MP3 = F_WEBSITE +"mp3";

    //家庭医生签约申请/ 签约列表
    public  String FAMILY_APPLY = WEBSITE +"family-apply";

    //家庭医生 拒绝|接受签约
    public  String FAMILY_VERTIFY = WEBSITE +"family-vertify";

    //家庭医生 接触签约
    public  String FIMILY_SIGN = WEBSITE +"family-sign";

    //意见反馈
    public  String ADVICE = H_WEBSITE + "advice";

    //医单列表
    public  String ORDER = WEBSITE +"order";

    //拒接单
    public  String NO_CONFIRM = WEBSITE +"no-confim";

    //接单
    public  String CONFIRM  = WEBSITE +"confim";

    //收入列表
    public  String ACCOUNT = WEBSITE +"account";

    //查询用户评价
    public  String APPRAISE = WEBSITE +"appraise";

    //更换绑定手机号
    public  String PHONE_CHANGE = WEBSITE +"phone-change";

    //查询老人问诊记录
    public  String HEALTH_ORDER = WEBSITE +"health-order";

    //医生提现
    public String DOCTER_TX = WEBSITE +"docter-tx";

    //查询未读消息
    public String HISTORY = C_IP +"/history";


}
