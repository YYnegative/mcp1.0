package com.ebig.hdi.modules.drugs.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 厂商信息
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-04-23 09:27:40
 */
@Data
@TableName("hdi_org_factory_info")
public class OrgFactoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Long id;
	/**
	 * 厂商编码(所属区域编码+自编码规则生成)
	 */
	private String factoryCode;
	/**
	 * 厂商名称
	 */
	@NotBlank(message="厂商名称不能为空")
	private String factoryName;
	/**
	 * 统一社会信用代码
	 */
	@NotBlank(message="统一社会信用代码不能为空")
	private String creditCode;
	/**
	 * 所在国家编码
	 */
	@NotBlank(message="请选择国家")
	private String countryCode;
	/**
	 * 所在省编码
	 */
	@NotBlank(message="请选择省")
	private String provinceCode;
	/**
	 * 所在市编码
	 */
	@NotBlank(message="请选择市")
	private String cityCode;
	/**
	 * 所在区县编码
	 */
	private String areaCode;
	/**
	 * 厂商地址
	 */
	private String factoryAddress;
	/**
	 * 状态(0:停用;1:启用)
	 */
	@NotNull(message="请选择状态")
	private Integer status;
	/**
	 * 法人代表
	 */
	private String corporate;
	/**
	 * 联系电话
	 */
	private String phone;
	/**
	 * 邮箱地址
	 */
	private String email;
	/**
	 * 传真
	 */
	private String fax;
	/**
	 * 所属机构
	 */
	private Long deptId;
	/**
	 * 创建人id
	 */
	private Long createId;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 修改人id
	 */
	private Long editId;
	/**
	 * 修改时间
	 */
	private Date editTime;
	/**
	 * 是否删除(-1:已删除;0:正常)
	 */
	private Integer delFlag;
}
