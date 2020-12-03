package tech.piis.modules.managment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tech.piis.modules.managment.domain.po.OrganizationPO;
import tech.piis.modules.managment.domain.vo.OrganizationVO;

import java.util.List;

/**
 * ClassName : OrganizationMapper
 * Package : tech.piis.modules.managment.mapper
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
