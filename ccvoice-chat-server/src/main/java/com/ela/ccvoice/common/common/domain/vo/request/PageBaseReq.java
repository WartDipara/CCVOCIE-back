package com.ela.ccvoice.common.common.domain.vo.request;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


@Data
@ApiModel("基础翻页请求")
public class PageBaseReq {

    @ApiModelProperty("当前页面索引")
    private Integer pageIndex = 1;

    @ApiModelProperty("页面大小")
    @Max(50)
    @Min(0)
    private Integer pageSize = 10;

    public Page plusPage() {
        return new Page(pageIndex, pageSize);
    }
}
