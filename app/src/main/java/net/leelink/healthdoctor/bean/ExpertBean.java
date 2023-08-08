package net.leelink.healthdoctor.bean;

public class ExpertBean {


    private Integer id;
    private Integer organId;
    private String typeName;
    private Object updateBy;
    private String updateTime;
    private Integer createBy;
    private String createTime;
    private String updateName;
    private String organName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrganId() {
        return organId;
    }

    public void setOrganId(Integer organId) {
        this.organId = organId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
