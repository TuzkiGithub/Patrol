package tech.piis.modules.managment.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.piis.modules.managment.domain.FullPartOrg;
import tech.piis.modules.managment.mapper.FullPartOrgMapper;
import tech.piis.modules.managment.service.FullPartOrgService;


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
public class IFullPartOrgServiceImpl implements FullPartOrgService {

    @Autowired
    private FullPartOrgMapper fullPartOrgMapper;


}
