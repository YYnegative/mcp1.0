/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ebig.mcp.server.api.http.entity;

import com.baomidou.mybatisplus.annotations.TableLogic;
import lombok.Data;
import org.springframework.jdbc.core.RowMapper;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据字典
 *
 * @author Mark sunlightcs@gmail.com
 * @since 3.1.0 2018-01-27
 */
@Data
public class SysDictEntity implements RowMapper, Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	/**
	 * 字典名称
	 */
	@NotBlank(message="字典名称不能为空")
	private String name;
	/**
	 * 字典类型
	 */
	@NotBlank(message="字典类型不能为空")
	private String type;
	/**
	 * 父字典编码
	 */
	private String parentCode;
	/**
	 * 字典码
	 */
	@NotBlank(message="字典码不能为空")
	private String code;
	/**
	 * 字典值
	 */
	@NotBlank(message="字典值不能为空")
	private String value;
	/**
	 * 排序
	 */
	private Integer orderNum;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 删除标记  -1：已删除  0：正常
	 */
	@TableLogic
	private Integer delFlag;

	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		SysDictEntity vo = new SysDictEntity();
		vo.setId(rs.getLong("id"));
		vo.setCode(rs.getString("code"));
		vo.setType(rs.getString("type"));
		vo.setValue(rs.getString("value"));
		return vo;
	}
}
