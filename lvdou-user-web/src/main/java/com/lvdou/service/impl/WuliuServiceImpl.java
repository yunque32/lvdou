package com.lvdou.service.impl;

import com.lvdou.mapper.WuliuMapper;
import com.lvdou.pojo.Wuliu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WuliuServiceImpl   {
    @Autowired
    private WuliuMapper wuliuMapper;

    public Wuliu queryWuliuById(Long id){
        return wuliuMapper.selectByPrimaryKey(id);

    }
}
