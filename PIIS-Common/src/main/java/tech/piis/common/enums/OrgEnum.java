package tech.piis.common.enums;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.common.enums
 * User: Tuzki
 * Date: 2020/11/10
 * Time: 9:04
 * Description:光大一级子公司枚举类
 */
public enum OrgEnum {

    ALL("0000000000", "全部"),
    EB("0000000000", "光大集团"),
    EB_GROUP("0100000000", "集团总部"),
    EB_BANK("0200000000", "光大银行"),
    EB_BOND("0300000000", "光大证券"),
    EB_INSURANCE("0400000000", "光大永明"),
    EB_TRUST("0500000000", "光大信托"),
    EB_FINANCIAL("0500000000", "光大金控"),
    EB_INDUSTRY("0700000000", "光大实业"),
    EB_HK("0800000000", "光大香港"),
    EB_HOSTEL("0900000000", "青旅集团"),
    EB_HEALTH("1000000000", "光大健康"),
    EB_TRAVEL("1100000000", "中青旅控股"),
    EB_MEDICINE("1200000000", "嘉事堂"),
    EB_METAL("1300000000", "光大金瓯"),
    EB_TECH("1800000000", "光大科技");

    private String orgId;
    private String orgName;

    OrgEnum(String orgId, String orgName) {
        this.orgId = orgId;
        this.orgName = orgName;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
