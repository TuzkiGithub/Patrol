package tech.piis.modules.person.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tech.piis.modules.person.domain.po.OrganizationPO;
import tech.piis.modules.person.domain.vo.OrganizationVO;

import java.util.List;

/**
 * ClassName : OrganizationMapper
 * Package : tech.piis.modules.person.mapper
 * Description :
 *
 * @author : chenhui@xvco.com
 */
public interface OrganizationMapper extends BaseMapper<OrganizationPO>{
    /**
     * 根据机构ID 获取机构所属巡察/巡视机构全称
     * @param organId
     * @return
     */
    String getInspectionOrganWholeName(String organId);

    List<OrganizationVO> selectListByOrgName(@Param("orgName")String orgName);
}
