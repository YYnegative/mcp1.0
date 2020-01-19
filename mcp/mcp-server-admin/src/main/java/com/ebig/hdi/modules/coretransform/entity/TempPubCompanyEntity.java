package com.ebig.hdi.modules.coretransform.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * Pub_Company
 * 
 * @author frink
 * @email 
 * @date 2019-06-26 17:27:31
 */
@Data
@TableName("pub_company")
public class TempPubCompanyEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * M单位ID
	 */
	@TableId
	private String mcompanyid;
	/**
	 * U机构标识
	 */
	private String uorganid;
	/**
	 * 关联机构标识
	 */
	private String aorganid;
	/**
	 * 单位编码
	 */
	private String companyno;
	/**
	 * 单位名称
	 */
	private String companyname;
	/**
	 * 单位拼音
	 */
	private String companypy;
	/**
	 * 单位简称
	 */
	private String companyshortname;
	/**
	 * 单位操作码
	 */
	private String companycode;
	/**
	 * 单位类型
	 */
	private BigDecimal companytype;
	/**
	 * 税号
	 */
	private String taxnamber;
	/**
	 * 法人
	 */
	private String legalpersion;
	/**
	 * 邮编
	 */
	private String postcode;
	/**
	 * 联系人
	 */
	private String linkman;
	/**
	 * 联系电话
	 */
	private String linktel;
	/**
	 * 手机
	 */
	private String modie;
	/**
	 * 其他联系方式
	 */
	private String otherlink;
	/**
	 * 传真
	 */
	private String fax;
	/**
	 * 电子邮件
	 */
	private String email;
	/**
	 * 经营范围
	 */
	private String managerange;
	/**
	 * 主要经营品种
	 */
	private String managegoods;
	/**
	 * GSP标志
	 */
	private BigDecimal gspflag;
	/**
	 * ISO标志
	 */
	private BigDecimal isoflag;
	/**
	 * 企业性质
	 */
	private BigDecimal corptype;
	/**
	 * 盈利性质
	 */
	private BigDecimal customnature;
	/**
	 * 经济性质
	 */
	private BigDecimal economytype;
	/**
	 * 质量认证
	 */
	private BigDecimal qualitytype;
	/**
	 * 关联性质
	 */
	private BigDecimal relationtype;
	/**
	 * 企业类型
	 */
	private String comtype;
	/**
	 * 行政区域ID
	 */
	private String companyzoneid;
	/**
	 * 行政区域
	 */
	private String companyzone;
	/**
	 * 网址
	 */
	private String webaddr;
	/**
	 * 状态
	 */
	private BigDecimal usestatus;
	/**
	 * 原数据标识
	 */
	private String orgdataid;
	/**
	 * 数据来源
	 */
	private BigDecimal datasource;
	/**
	 * 录入人Id
	 */
	private String inputmanid;
	/**
	 * 录入人
	 */
	private String inputmanname;
	/**
	 * 录入日期
	 */
	private Date inputdate;
	/**
	 * 修改人ID
	 */
	private String editmanid;
	/**
	 * 修改人
	 */
	private String editmanname;
	/**
	 * 修改日期
	 */
	private Date editdate;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 仓库地址
	 */
	private String waraddress;
	/**
	 * 单位助记码信息
	 */
	private String companyinfo;
	/**
	 * 单位概况
	 */
	private String companysum;
	/**
	 * 排序号
	 */
	private BigDecimal orderno;
	/**
	 * 备注
	 */
	private String memo;
	/**
	 * UDF1
	 */
	private String udf1;
	/**
	 * UDF2
	 */
	private String udf2;
	/**
	 * UDF3
	 */
	private String udf3;
	/**
	 * UDF4
	 */
	private String udf4;
	/**
	 * UDF5
	 */
	private String udf5;
	/**
	 * 付款银行账号
	 */
	private String paybankaccount;
	/**
	 * 银行账号
	 */
	private String bankaccount;
	/**
	 * 开户行
	 */
	private String bankname;
	/**
	 * 科目代码
	 */
	private String subjectcode;
	/**
	 * 银行代码
	 */
	private String bankcode;
	/**
	 * 资料齐全标志
	 */
	private BigDecimal qccontrolflag;
	/**
	 * 上传下载标记，0上传 1下载
	 */
	private Integer udflag;


}
