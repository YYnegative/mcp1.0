package com.ebig.mcp.server.api.http.vo;

import lombok.Data;
import org.springframework.jdbc.core.RowMapper;

import javax.validation.constraints.NotBlank;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @description:
 * @author: wenchao
 * @time: 2019-10-16 8:59
 */
@Data
public class GoodsSupplierConsumablesVo extends GoodsSupplierVo implements RowMapper {

    /**
     * 耗材编码
     */
    private String consumablesCode;

    /**
     * 耗材名称
     */
    @NotBlank
    private String consumablesName;

    @Override
    public GoodsSupplierConsumablesVo mapRow(ResultSet rs, int rowNum) throws SQLException {
        GoodsSupplierConsumablesVo vo = new GoodsSupplierConsumablesVo();
        vo.setId(rs.getLong("id"));
        vo.setConsumablesName(rs.getString("consumables_name"));
        vo.setSupplierCode(rs.getString("supplier_code"));
        return vo;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GoodsSupplierConsumablesVo) {
            GoodsSupplierConsumablesVo entity= (GoodsSupplierConsumablesVo) obj;
            if(this.getConsumablesName().equals(entity.getConsumablesName())
                    && this.getSupplierCode().equals(entity.getSupplierCode())){
                return true;
            }
        }
        return false;
    }
    @Override
    public int hashCode() {
        int result = this.getSupplierCode().hashCode();
        result = 19 * result + this.getConsumablesName().hashCode();
        return result;

    }
}
