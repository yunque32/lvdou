package com.lvdou.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.lvdou.mapper.WuliuMapper;
import com.lvdou.pojo.Wuliu;
import com.lvdou.sellergoods.service.WuliuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceName = "com.lvdou.sellergoods.service.WuliuService")
@Transactional
public class WuliuServiceImpl implements WuliuService {
    @Autowired
    private WuliuMapper wuliuMapper;

    public Wuliu queryWuliuById(Long id){
        return wuliuMapper.selectByPrimaryKey(id);
    }
}
