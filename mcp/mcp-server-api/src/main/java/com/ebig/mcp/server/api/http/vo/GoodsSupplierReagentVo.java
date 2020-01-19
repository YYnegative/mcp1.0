package com.ebig.mcp.server.api.http.vo;

import lombok.Data;
import org.springframework.jdbc.core.RowMapper;

import javax.validation.constraints.NotBlank;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @description:
 * @author: wenchao
 * @time: 2019-10-16 9:50
 */
@Data
public class GoodsSupplierReagentVo extends GoodsSupplierVo implements RowMapper {
    /**
     * 试剂编码
     */
    private String reagentCode;
    /**
     * 试剂名称
     */
    @NotBlank
    private String reagentName;

    @Override
    public GoodsSupplierReagentVo mapRow(ResultSet rs, int rowNum) throws SQLException {
        GoodsSupplierReagentVo vo = new GoodsSupplierReagentVo();
        vo.setId(rs.getLong("id"));
        vo.setReagentName(rs.getString("reagent_name"));
        vo.setSupplierCode(rs.getString("supplier_code"));
        return vo;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GoodsSupplierReagentVo) {
            GoodsSupplierReagentVo entity= (GoodsSupplierReagentVo) obj;
            if(this.getReagentName().equals(entity.getReagentName())
                    && this.getSupplierCode().equals(entity.getSupplierCode())){
                return true;
            }
        }
        return false;
    }   @Override
    public int hashCode() {
        int result = this.getSupplierCode().hashCode();
        result = 19 * result + this.getReagentName().hashCode();
        return result;

    }

}
