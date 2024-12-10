package com.ela.ccvoice.common.common.utils.disc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UrlInfo {
    String title;//标题
    String description;//描述
    String image;//网站logo
}
