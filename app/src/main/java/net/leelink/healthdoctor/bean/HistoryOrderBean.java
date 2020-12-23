package net.leelink.healthdoctor.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class HistoryOrderBean implements Parcelable {


    private String orderId;
    private String headImg;
    private String elderlyName;
    private String department;
    private int sex;
    private int age;
    private String clientId;
    private double actPayPrice;
    private int state;
    private String remark;
    private String createTime;
    private Object appointTime;
    private int orderType;
    private double payPrice;
    private int payMethod;
    private String orderNo;
    private String payTime;
    private Object imgFirstPath;
    private Object imgSecondPath;
    private Object imgThirdPath;
    private Object imgForthPath;
    private Object imgFifthPath;
    private Object imgSixthPath;
    private Object imgSeventhPath;
    private Object imgEighthPath;

    protected HistoryOrderBean(Parcel in) {
        orderId = in.readString();
        headImg = in.readString();
        elderlyName = in.readString();
        department = in.readString();
        sex = in.readInt();
        age = in.readInt();
        clientId = in.readString();
        actPayPrice = in.readDouble();
        state = in.readInt();
        remark = in.readString();
        createTime = in.readString();
        orderType = in.readInt();
        payPrice = in.readDouble();
        payMethod = in.readInt();
        orderNo = in.readString();
        payTime = in.readString();
    }

    public static final Creator<HistoryOrderBean> CREATOR = new Creator<HistoryOrderBean>() {
        @Override
        public HistoryOrderBean createFromParcel(Parcel in) {
            return new HistoryOrderBean(in);
        }

        @Override
        public HistoryOrderBean[] newArray(int size) {
            return new HistoryOrderBean[size];
        }
    };

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getElderlyName() {
        return elderlyName;
    }

    public void setElderlyName(String elderlyName) {
        this.elderlyName = elderlyName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public double getActPayPrice() {
        return actPayPrice;
    }

    public void setActPayPrice(double actPayPrice) {
        this.actPayPrice = actPayPrice;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Object getAppointTime() {
        return appointTime;
    }

    public void setAppointTime(Object appointTime) {
        this.appointTime = appointTime;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public double getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(double payPrice) {
        this.payPrice = payPrice;
    }

    public int getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(int payMethod) {
        this.payMethod = payMethod;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public Object getImgFirstPath() {
        return imgFirstPath;
    }

    public void setImgFirstPath(Object imgFirstPath) {
        this.imgFirstPath = imgFirstPath;
    }

    public Object getImgSecondPath() {
        return imgSecondPath;
    }

    public void setImgSecondPath(Object imgSecondPath) {
        this.imgSecondPath = imgSecondPath;
    }

    public Object getImgThirdPath() {
        return imgThirdPath;
    }

    public void setImgThirdPath(Object imgThirdPath) {
        this.imgThirdPath = imgThirdPath;
    }

    public Object getImgForthPath() {
        return imgForthPath;
    }

    public void setImgForthPath(Object imgForthPath) {
        this.imgForthPath = imgForthPath;
    }

    public Object getImgFifthPath() {
        return imgFifthPath;
    }

    public void setImgFifthPath(Object imgFifthPath) {
        this.imgFifthPath = imgFifthPath;
    }

    public Object getImgSixthPath() {
        return imgSixthPath;
    }

    public void setImgSixthPath(Object imgSixthPath) {
        this.imgSixthPath = imgSixthPath;
    }

    public Object getImgSeventhPath() {
        return imgSeventhPath;
    }

    public void setImgSeventhPath(Object imgSeventhPath) {
        this.imgSeventhPath = imgSeventhPath;
    }

    public Object getImgEighthPath() {
        return imgEighthPath;
    }

    public void setImgEighthPath(Object imgEighthPath) {
        this.imgEighthPath = imgEighthPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderId);
        dest.writeString(headImg);
        dest.writeString(elderlyName);
        dest.writeString(department);
        dest.writeInt(sex);
        dest.writeInt(age);
        dest.writeString(clientId);
        dest.writeDouble(actPayPrice);
        dest.writeInt(state);
        dest.writeString(remark);
        dest.writeString(createTime);
        dest.writeInt(orderType);
        dest.writeDouble(payPrice);
        dest.writeInt(payMethod);
        dest.writeString(orderNo);
        dest.writeString(payTime);
    }
}
