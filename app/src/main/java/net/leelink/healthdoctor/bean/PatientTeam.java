package net.leelink.healthdoctor.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class PatientTeam implements Parcelable {


    private String groupId;
    private String groupName;
    private int groupCount;
    private List<CareOlderVoListBean> careOlderVoList;

    protected PatientTeam(Parcel in) {
        groupId = in.readString();
        groupName = in.readString();
        groupCount = in.readInt();
    }

    public static final Creator<PatientTeam> CREATOR = new Creator<PatientTeam>() {
        @Override
        public PatientTeam createFromParcel(Parcel in) {
            return new PatientTeam(in);
        }

        @Override
        public PatientTeam[] newArray(int size) {
            return new PatientTeam[size];
        }
    };

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(int groupCount) {
        this.groupCount = groupCount;
    }

    public List<CareOlderVoListBean> getCareOlderVoList() {
        return careOlderVoList;
    }

    public void setCareOlderVoList(List<CareOlderVoListBean> careOlderVoList) {
        this.careOlderVoList = careOlderVoList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(groupId);
        dest.writeString(groupName);
        dest.writeInt(groupCount);
    }

    public static class CareOlderVoListBean implements Parcelable{
        private String elderlyId;
        private String headImgPath;
        private String elderlyName;
        private int sex;
        private int age;
        private String clientId;
        private String updateTime;

        protected CareOlderVoListBean(Parcel in) {
            elderlyId = in.readString();
            headImgPath = in.readString();
            elderlyName = in.readString();
            sex = in.readInt();
            age = in.readInt();
            clientId = in.readString();
            updateTime = in.readString();
        }

        public static final Creator<CareOlderVoListBean> CREATOR = new Creator<CareOlderVoListBean>() {
            @Override
            public CareOlderVoListBean createFromParcel(Parcel in) {
                return new CareOlderVoListBean(in);
            }

            @Override
            public CareOlderVoListBean[] newArray(int size) {
                return new CareOlderVoListBean[size];
            }
        };

        public String getElderlyId() {
            return elderlyId;
        }

        public void setElderlyId(String elderlyId) {
            this.elderlyId = elderlyId;
        }

        public String getHeadImgPath() {
            return headImgPath;
        }

        public void setHeadImgPath(String headImgPath) {
            this.headImgPath = headImgPath;
        }

        public String getElderlyName() {
            return elderlyName;
        }

        public void setElderlyName(String elderlyName) {
            this.elderlyName = elderlyName;
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

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(elderlyId);
            dest.writeString(headImgPath);
            dest.writeString(elderlyName);
            dest.writeInt(sex);
            dest.writeInt(age);
            dest.writeString(clientId);
            dest.writeString(updateTime);
        }
    }
}
