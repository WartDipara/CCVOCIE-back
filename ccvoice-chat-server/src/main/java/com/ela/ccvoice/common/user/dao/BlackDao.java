package com.ela.ccvoice.common.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ela.ccvoice.common.user.domain.entity.Black;
import com.ela.ccvoice.common.user.mapper.BlackMapper;
import org.springframework.stereotype.Service;

/**
 * 黑名单 服务实现类
 */
@Service
public class BlackDao extends ServiceImpl<BlackMapper, Black> {

}
