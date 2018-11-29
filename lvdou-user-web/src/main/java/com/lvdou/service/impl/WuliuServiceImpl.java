package com.lvdou.service.impl;

import com.lvdou.mapper.WuliuMapper;
import com.lvdou.pojo.Wuliu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class WuliuServiceImpl   {
    @Resource
    private WuliuMapper wuliuMapper;

    public Wuliu queryWuliuById(Long id){
        return wuliuMapper.selectByPrimaryKey(id);

    }
}
