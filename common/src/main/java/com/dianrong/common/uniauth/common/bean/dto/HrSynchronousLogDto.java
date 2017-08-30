package com.dianrong.common.uniauth.common.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
@ApiModel("同步数据操作日志")
public class HrSynchronousLogDto implements Serializable {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("同步开始时间")
    private Date synchronousStartTime;

    @ApiModelProperty("同步结束时间")
    private Date synchronousEndTime;

    @ApiModelProperty("日志类型")
    private String synchronousType;

    @ApiModelProperty("处理的内容")
    private String processContent;

    @ApiModelProperty("处理服务器的ip")
    private String computerIp;

    @ApiModelProperty("同步结果(success|failure)")
    private String synchronousResult;

    @ApiModelProperty("失败原因")
    private String failureMsg;

    @ApiModelProperty("记录创建时间")
    private Date createDate;

    @ApiModelProperty("记录更新时间")
    private Date lastUpdate;
}
