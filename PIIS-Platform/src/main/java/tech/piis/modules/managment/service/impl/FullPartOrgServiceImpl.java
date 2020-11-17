package tech.piis.modules.managment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.piis.modules.core.mapper.PiisDocumentMapper;
import tech.piis.modules.managment.domain.FullPartOrgPO;
import tech.piis.modules.managment.mapper.FullPartOrgMapper;
import tech.piis.modules.managment.service.IFullPartOrgService;

import java.util.Arrays;
import java.util.List;


/**
 * ClassName : IFullPartOrgService
 * Package : tech.piis.modules.managment.service.impl
 * Description :
 * 专兼职管理 业务层处理
 *
 * @author : chenhui@xvco.com
 */
@Slf4j
@Transactional
@Service
public class FullPartOrgServiceImpl implements IFullPartOrgService {

    @Autowired
    private FullPartOrgMapper fullPartOrgMapper;
    @Autowired
    private PiisDocumentMapper piisDocumentMapper;
    /**
     * 新增专兼职管理
     * @param fullPartOrgPO
     * @return
     */
    @Override
    public int save(FullPartOrgPO fullPartOrgPO) {
        return fullPartOrgMapper.insert(fullPartOrgPO);
    }

    /**
     * 修改专兼职管理
     * @param fullPartOrgPO
     * @return
     */
    @Override
    public int update(FullPartOrgPO fullPartOrgPO) {
        return fullPartOrgMapper.updateById(fullPartOrgPO);
    }

    /**
     * 删除专兼职管理
     * @param fullPartOrgIds
     * @return
     */
    @Override
    public int deleteByFullPartOrgId(String[] fullPartOrgIds) {
        List<String> list = Arrays.asList(fullPartOrgIds);
        list.forEach(id ->{
            QueryWrapper<FullPartOrgPO> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("FULL_ID", id);
            FullPartOrgPO fullPartOrgPO = fullPartOrgMapper.selectOne(queryWrapper);
            piisDocumentMapper.deleteById(fullPartOrgPO.getPiisDocId());
        });
        return fullPartOrgMapper.deleteBatchIds(list);
    }

    /**
     * 专兼职管理列表
     * @param fullPartOrgPO
     * @return
     */
    @Override
    public List<FullPartOrgPO> selectByWholeName(FullPartOrgPO fullPartOrgPO) {
        QueryWrapper<FullPartOrgPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(fullPartOrgPO.getOrgName()), "ORG_NAME", fullPartOrgPO.getOrgName());
        List<FullPartOrgPO> fullPartOrgList = fullPartOrgMapper.selectList(queryWrapper);
        fullPartOrgList.forEach(fullPartOrg ->{
            String fileName = piisDocumentMapper.selectById(fullPartOrg.getPiisDocId()).getFileName();
            fullPartOrg.setFileName(fileName);

        } );
        return fullPartOrgList;
    }

}
