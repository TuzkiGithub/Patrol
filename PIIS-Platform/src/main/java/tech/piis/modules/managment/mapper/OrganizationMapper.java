package tech.piis.modules.managment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.modules.managment.domain.Organization;

/**
 * ClassName : OrganizationMapper
 * Package : tech.piis.modules.managment.mapper
 * Description :
 *
 * @author : chenhui@xvco.com
 */
public interface OrganizationMapper extends BaseMapper<Organization>{
    /**
     * 根据机构ID 获取机构所属巡察/巡视机构全称
     * @param organId
     * @return
     */
    String getInspectionOrganWholeName(String organId);
}
