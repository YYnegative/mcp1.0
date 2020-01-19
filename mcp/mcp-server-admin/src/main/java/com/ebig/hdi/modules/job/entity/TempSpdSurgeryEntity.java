package com.ebig.hdi.modules.job.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 手术申请单
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-07-11 20:04:25
 */
@TableName("temp_spd_surgery")
@Data
public class TempSpdSurgeryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 手术单ID
	 */
	@TableId
	private String surgeryid;
	/**
	 * 机构标识
	 */
	private String uorganid;
	/**
	 * 手术名称
	 */
	private String opsubtitle;
	/**
	 * 住院号码
	 */
	private String customno;
	/**
	 * 唯一住院号
	 */
	private String customid;
	/**
	 * 病人姓名
	 */
	private String customname;
	/**
	 * 病人性别
	 */
	private Integer customsex;
	/**
	 * 病人年龄
	 */
	private String customage;
	/**
	 * 病人床号
	 */
	private String bedno;
	/**
	 * 申请医生工号
	 */
	private String apppovemanid;
	/**
	 * 申请医生姓名
	 */
	private String apppovemanname;
	/**
	 * 库房科室
	 */
	private String storehouseid;
	/**
	 * 库房科室编号
	 */
	private String storehouseno;
	/**
	 * 库房科室名称
	 */
	private String storehousename;
	/**
	 * 申请日期
	 */
	private Date apppovedate;
	/**
	 * 术前诊断
	 */
	private String podx;
	/**
	 * 原始标识
	 */
	private String originid;
	/**
	 * 手术编码
	 */
	private String originno;
	/**
	 * 手术医生工号
	 */
	private String surgeonno;
	/**
	 * 手术医生姓名
	 */
	private String surgeonname;
	/**
	 * 助I工号
	 */
	private String assistantno1;
	/**
	 * 助I姓名
	 */
	private String assistantname1;
	/**
	 * 助II工号
	 */
	private String assistantno2;
	/**
	 * 助II姓名
	 */
	private String assistantname2;
	/**
	 * 助III工号
	 */
	private String assistantno3;
	/**
	 * 助III姓名
	 */
	private String assistantname3;
	/**
	 * 麻醉方式
	 */
	private String anestype;
	/**
	 * 麻醉医生工号
	 */
	private String anesmanno;
	/**
	 * 麻醉医生姓名
	 */
	private String anesmanname;
	/**
	 * 手术体位
	 */
	private String posture;
	/**
	 * 手术检查
	 */
	private String examined;
	/**
	 * 特殊感染
	 */
	private String infection;
	/**
	 * 手术器械
	 */
	private String instruments;
	/**
	 * 备注
	 */
	private String memo;
	/**
	 * 原手术领用单标识
	 */
	private String orgdataid;
	/**
	 * 原手术领用单号
	 */
	private String orgdatano;
	/**
	 * 数据来源
	 */
	private BigDecimal datasource;
	/**
	 * 状态
	 */
	private BigDecimal surgerystatus;
	/**
	 * 打印标识
	 */
	private BigDecimal printflag;
	/**
	 * 打印时间
	 */
	private Date printtime;
	/**
	 * 打印人ID
	 */
	private String printmanid;
	/**
	 * 打印人
	 */
	private String printmanname;
	/**
	 * 错误原因
	 */
	private String returnerrmsg;
	/**
	 * 执行科室ID
	 */
	private String perfstorehouseid;
	/**
	 * 执行库房科室编号
	 */
	private String perfstorehouseno;
	/**
	 * 执行库房科室名称
	 */
	private String perfstorehousename;
	/**
	 * 手术间名称
	 */
	private String roomname;
	/**
	 * 手术间ID
	 */
	private String roomid;
	/**
	 * 高值清点结束
	 */
	private BigDecimal hvchecked;
	/**
	 * 低值清点结束
	 */
	private BigDecimal dvchecked;
	/**
	 * 清台回退标识
	 */
	private BigDecimal rollbackflag;
	/**
	 * 
	 */
	private Integer udflag;

}
