package net.leelink.healthdoctor.util;

public class Urls {
//    public static final String IP = "http://api.llky.net:";
    public static final String IP = "http://221.238.204.114:";

    public static final String WEBSITE = IP+"8888/jk/doctoerAngel/";
    public static final String C_WEBSITE =IP+"8888/sh/customer/";
    public static final String U_WEBSITE = IP+"8888/sh/user/";
    public static final String S_WEBSITE = IP+"8888/sysDict/";
    public static final String H_WEBSITE = IP+"8888/jk/healthAngel/";
    public static final String F_WEBSITE = IP+"8888/file-manager/";


    //h5健康数据地址
    public static final String WEB =IP+"7966/#/AppData";

    public static final String IMG_URL = IP+"8888/files";



    //发送短信验证码
    public static final String SEND = U_WEBSITE +"send";

    //注册
    public static final String REGIST = WEBSITE + "regist";

    //密码登录
    public static final String LOGIN = WEBSITE+"login";

    //验证码登录
    public static final String LOGIN_SMS = WEBSITE +"login-sms";

    //查询我的账户
    public static final String BIND_BANK =WEBSITE +"bind-bank";

    //查询支持的银行
    public static final String BANK = U_WEBSITE +"bank";

    //修改密码
    public static final String PASSWORD = WEBSITE +"password";

    //获取省列表
    public static final String GETPROVINCE = S_WEBSITE +"getProvince";

    //获取城市列表
    public static final String GETCITY = S_WEBSITE +"getCity";

    //获取区县列表
    public static final String GETCOUNTY = S_WEBSITE+"getCounty";

    //获取医院列表
    public static final String HOSPITAL = S_WEBSITE +"hospital";

    //查询开关设置
    public static final String OPEN_OPTION = WEBSITE +"open-option";

    //查询排班时间
    public static final String RECEPTION = WEBSITE +"reception";

    //查询个人信息
    public static final String USERINFO = WEBSITE +"userinfo";

    //设置问诊价格
    public static final String SET_AMOUNT = WEBSITE +"set-amount";

    //查询患者分组列表
    public static final String FAMILY_GROUP = H_WEBSITE +"family-group";

    //是否成为家庭医生
    public static final String IN_DOCKER = WEBSITE +"in-docker";

    //申请成为家庭医生
    public static final String FAMILY_DOCTOR = WEBSITE +"family-doctor";

    //上传图片获得地址
    public static final String PHOTO = F_WEBSITE+"photo";

    //上传音频获取音频地址
    public static final String MP3 = F_WEBSITE +"mp3";

    //家庭医生签约申请/ 签约列表
    public static final String FAMILY_APPLY = WEBSITE +"family-apply";

    //家庭医生 拒绝|接受签约
    public static final String FAMILY_VERTIFY = WEBSITE +"family-vertify";

    //家庭医生 接触签约
    public static final String FIMILY_SIGN = WEBSITE +"family-sign";

    //意见反馈
    public static final String ADVICE = H_WEBSITE + "advice";

    //医单列表
    public static final String ORDER = WEBSITE +"order";


    //一键定位
    public static final String OPENGPS = WEBSITE +"openGPS";

    //查询定位历史
    public static final String GPSRECORD = WEBSITE +"gpsRecord";

    //获取定时提醒列表
    public static final String REMINDLIST = WEBSITE +"remindList";

    //新增定时提醒
    public static final String ADDREMIND = WEBSITE +"addRemind";

    //修改定时提醒
    public static final String UPDATEREMIND = WEBSITE +"updateRemind";

    //删除定时提醒
    public static final String DELETEREMIND = WEBSITE +"deleteRemind";

    //数据录入
    public static final String INPUT = WEBSITE +"input";

    //录入的历史记录
    public static final String INPUTLIST = WEBSITE +"inputList";

    //获取公共模板
    public static final String VOICETEMPLATE = WEBSITE +"voiceTemplate";

    //语音播报内容
    public static final String VOICECONTENT = WEBSITE +"voiceContent";

    //语音播报历史记录
    public static final String FINDMSG = WEBSITE +"findMsg";

    //发送语音播报
    public static final String SENDMESSAGE = WEBSITE +"sendMessage";

    //定时发送
    public static final String SENDMESSAGEBYTIME = WEBSITE +"sendMessageByTime";

    //查询绑定的设备
    public static final String MYBIND = WEBSITE +"mybind";

    //绑定\解除设备 查询可绑定设备
    public static final String BIND = WEBSITE +"bind";

    //获取版本信息
    public static final String VERSION =IP+"8888/app/version";   //获取版本更新

    //我的信息
    public static final String INFO = WEBSITE +"info";

    //查询健康数据
    public static final String HEALTHDATA = WEBSITE +"heatlhData";

    //常见食物列表
    public static final String FOODRECORD = WEBSITE  +"foodRecord";

    //新增/查询饮食记录
    public static final String RECORD = WEBSITE +"record";

    //查询最近添加的食物
    public static final String RECENTRECORD = WEBSITE +"recentRecord";

    //查询附近机构
    public static final String NEARORGAN = WEBSITE  +"nearOrgan";

    //查询城市列表
    public static final String GETALLCITY = IP+"8888/sysDict/getAllCity";

    //设置亲人号码
    public static final String RELATIVE = WEBSITE +"relative";

    //获取亲人号码列表
    public static final String RELATIVECONTACTLIST = WEBSITE +"relativeContactList";

    //新增紧急联系人
    public static final String URGENTPEOPLE = WEBSITE +"urgentPeople";

    //编辑紧急联系人
    public static final String UPDATEURGENTPEOPLE = WEBSITE +"updateUrgentPeople";

    //惠民政策列表
    public static final String BENEFIT = WEBSITE +"benefit";

    //健康知识列表
    public static final String KNOWLEDGE = WEBSITE +"knowledge";

    //查询调查问卷模板
    public static final String QUESTIONTEMP = WEBSITE +"questionTemp";

    //申请高龄补贴
    public static final String EVALUTION = WEBSITE +"evalution";

    //获取组织列表
    public static final String ORGAN = IP+"8888/sh/user/organ";

    //设备维修
    public static final String EQUIP = WEBSITE  +"equip";

    //IC卡充值
    public static final String ICPAY = WEBSITE +"icPay";

    //充值账单
    public static final String ACCOUNT = WEBSITE +"account";

    //查询所有可订阅的服务
    public static final String SERVICE = WEBSITE +"service";

    //查询三诺血糖仪测量血糖记录
    public static final String SANNUOBLOODSUGARLIST = WEBSITE +"sannuoBloodSugarList";

    //查询三诺血尿酸记录
    public static final String SANNUOBLOODURICLIST = WEBSITE +"sannuoBloodUricList";

    //上传血糖数据
    public static final String UPLOADBLOODSUGAR = WEBSITE +"uploadBloodSugar";

    //上传血尿酸数据
    public static final String UPLOADUA = WEBSITE +"uploadUa";

    //查询套餐列表
    public static final String MEAL = WEBSITE +"meal";

    //我的套餐
    public static final String MEAL_MINE = WEBSITE +"meal-mine";


    //蓝牙登录(长桑)
    public static final String ENTERPRISELOGIN = "https://test-health.vita-course.com/gromit/entry/enterpriselogin";

    //上传标定参数

    //获取积分记录
    public static final String INTEGRAL = WEBSITE +"integral";



}
