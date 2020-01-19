# view_hdi_goods_specs_platform(平台商品规格视图)
DROP VIEW IF EXISTS view_hdi_goods_specs_platform;
CREATE VIEW view_hdi_goods_specs_platform AS 
select 1 as goods_type,d.id as goods_id,d.drugs_code as goods_code,d.drugs_name as goods_name,d.common_name,d.dept_id,d.goods_unit,
s.id as goods_specs_id,s.specs_code,s.specs,f.id as factory_id,f.factory_code,f.factory_name
from hdi_goods_platform_drugs_specs s left join hdi_goods_platform_drugs d on s.drugs_id=d.id
left join hdi_org_factory_info f on d.factory_id=f.id
union
select 2 as goods_type,d.id as goods_id,d.reagent_code as goods_code,d.reagent_name as goods_name,d.common_name,d.dept_id,d.goods_unit,
s.id as goods_specs_id,s.specs_code,s.specs,f.id as factory_id,f.factory_code,f.factory_name
from hdi_goods_platform_reagent_specs s left join hdi_goods_platform_reagent d on s.reagen_id=d.id
left join hdi_org_factory_info f on d.factory_id=f.id
union
select 3 as goods_type,d.id as goods_id,d.consumables_code as goods_code,d.consumables_name as goods_name,d.common_name,d.dept_id,d.goods_unit,
s.id as goods_specs_id,s.specs_code,s.specs,f.id as factory_id,f.factory_code,f.factory_name
from hdi_goods_platform_consumables_specs s left join hdi_goods_platform_consumables d on s.consumables_id=d.id
left join hdi_org_factory_info f on d.factory_id=f.id;

# view_hdi_goods_specs_supplier(供应商商品规格视图)
DROP VIEW IF EXISTS view_hdi_goods_specs_supplier;
CREATE VIEW view_hdi_goods_specs_supplier AS
SELECT 1 AS goods_type,d.supplier_id,d.id AS goods_id,d.type_name,d.drugs_code AS goods_code,d.drugs_name AS goods_name,d.common_name,d.goods_nature,d.dept_id,d.goods_unit,d.status,d.agent_id,d.pic_url,d.approvals,d.store_way,d.convert_unit,d.del_flag,d.sunshine_pno AS sunshine_pno,
s.id AS goods_specs_id,s.sources_specs_id,s.specs_code,s.specs,f.id AS factory_id,f.factory_code,f.factory_name
FROM hdi_goods_supplier_drugs_specs s LEFT JOIN hdi_goods_supplier_drugs d ON s.drugs_id=d.id
LEFT JOIN hdi_org_factory_info f ON d.factory_id=f.id
UNION
SELECT 2 AS goods_type,d.supplier_id,d.id AS goods_id,d.type_name,d.reagent_code AS goods_code,d.reagent_name AS goods_name,d.common_name,d.goods_nature,d.dept_id,d.goods_unit,d.status,d.agent_id,d.pic_url,d.approvals,d.store_way,d.convert_unit,d.del_flag,d.sunshine_pno AS sunshine_pno,
s.id AS goods_specs_id,s.sources_specs_id,s.specs_code,s.specs,f.id AS factory_id,f.factory_code,f.factory_name
FROM hdi_goods_supplier_reagent_specs s LEFT JOIN hdi_goods_supplier_reagent d ON s.reagen_id=d.id
LEFT JOIN hdi_org_factory_info f ON d.factory_id=f.id
UNION
SELECT g.goods_type,g.supplier_id,g.goods_id,g.type_name,g.goods_code,g.goods_name,g.common_name,g.goods_nature,g.dept_id,g.goods_unit,g.status,g.agent_id,g.pic_url,h.approvals,g.store_way,g.convert_unit,g.del_flag,g.sunshine_pno AS sunshine_pno,g.goods_specs_id,
g.sources_specs_id,g.specs_code,g.specs,g.factory_id,g.factory_code,g.factory_name FROM
(SELECT e.*,f.factory_code,f.factory_name FROM
(SELECT 3 AS goods_type,d.supplier_id,d.id AS goods_id,d.type_name,d.consumables_code AS goods_code,d.consumables_name AS goods_name,d.common_name,d.goods_nature,d.dept_id,d.goods_unit,d.status,d.agent_id,d.pic_url,d.factory_id,d.store_way,d.convert_unit,d.del_flag,d.sunshine_pno AS sunshine_pno,
(SELECT a.id AS approvals_id FROM (SELECT id,consumables_id,approvals,
CASE WHEN approvals REGEXP '食药监械' THEN SUBSTRING_INDEX(SUBSTRING_INDEX(approvals, '字', -1), '第', 1) WHEN approvals REGEXP '械注' 
THEN SUBSTRING(approvals, 5, 4) WHEN approvals REGEXP '械备' THEN SUBSTRING(approvals, 5, 4) ELSE '0' END AS approvals_year 
FROM hdi_goods_supplier_consumables_approvals WHERE STATUS = 1) a WHERE a.consumables_id = d.id ORDER BY a.approvals_year DESC LIMIT 1) AS approvals_id,
s.id AS goods_specs_id,s.sources_specs_id,s.specs_code,s.specs
FROM hdi_goods_supplier_consumables_specs s LEFT JOIN hdi_goods_supplier_consumables d ON s.consumables_id=d.id
) e LEFT JOIN hdi_org_factory_info f ON e.factory_id=f.id
) g LEFT JOIN hdi_goods_supplier_consumables_approvals h ON g.approvals_id=h.id;

# view_hdi_goods_specs_hospital(医院商品规格视图)
DROP VIEW IF EXISTS view_hdi_goods_specs_hospital;
CREATE VIEW view_hdi_goods_specs_hospital AS 
SELECT 1 AS goods_type,d.hospital_id,d.id AS goods_id,d.drugs_code AS goods_code,d.drugs_name AS goods_name,d.common_name,d.dept_id,d.goods_unit,
s.id AS goods_specs_id,s.sources_specs_id,s.specs_code,s.specs,f.id AS factory_id,f.factory_code,f.factory_name
FROM hdi_goods_hospital_drugs_specs s LEFT JOIN hdi_goods_hospital_drugs d ON s.drugs_id=d.id
LEFT JOIN hdi_org_factory_info f ON d.factory_id=f.id
UNION
SELECT 2 AS goods_type,d.hospital_id,d.id AS goods_id,d.reagent_code AS goods_code,d.reagent_name AS goods_name,d.common_name,d.dept_id,d.goods_unit,
s.id AS goods_specs_id,s.sources_specs_id,s.specs_code,s.specs,f.id AS factory_id,f.factory_code,f.factory_name
FROM hdi_goods_hospital_reagent_specs s LEFT JOIN hdi_goods_hospital_reagent d ON s.reagen_id=d.id
LEFT JOIN hdi_org_factory_info f ON d.factory_id=f.id
UNION
SELECT 3 AS goods_type,d.hospital_id,d.id AS goods_id,d.consumables_code AS goods_code,d.consumables_name AS goods_name,d.common_name,d.dept_id,d.goods_unit,
s.id AS goods_specs_id,s.sources_specs_id,s.specs_code,s.specs,f.id AS factory_id,f.factory_code,f.factory_name
FROM hdi_goods_hospital_consumables_specs s LEFT JOIN hdi_goods_hospital_consumables d ON s.consumables_id=d.id
LEFT JOIN hdi_org_factory_info f ON d.factory_id=f.id;

# view_hdi_goods_supplier(供应商商品视图)
DROP VIEW IF EXISTS view_hdi_goods_supplier;
CREATE VIEW view_hdi_goods_supplier AS 
select a.id,1 as goods_type,a.supplier_id,b.supplier_name,a.drugs_code as goods_code,a.drugs_name as goods_name,a.common_name,a.factory_id,c.factory_name,a.status,a.dept_id,a.del_flag
from hdi_goods_supplier_drugs a left join hdi_org_supplier_info b on a.supplier_id=b.id 
left join hdi_org_factory_info c on a.factory_id=c.id
union
select a.id,2 as goods_type,a.supplier_id,b.supplier_name,a.reagent_code as goods_code,a.reagent_name as goods_name,a.common_name,a.factory_id,c.factory_name,a.status,a.dept_id,a.del_flag
from hdi_goods_supplier_reagent a left join hdi_org_supplier_info b on a.supplier_id=b.id 
left join hdi_org_factory_info c on a.factory_id=c.id
union
select a.id,3 as goods_type,a.supplier_id,b.supplier_name,a.consumables_code as goods_code,a.consumables_name as goods_name,a.common_name,a.factory_id,c.factory_name,a.status,a.dept_id,a.del_flag
from hdi_goods_supplier_consumables a left join hdi_org_supplier_info b on a.supplier_id=b.id 
left join hdi_org_factory_info c on a.factory_id=c.id;

# view_hdi_goods_supplier_hospital(供应商合作医院商品视图)
DROP VIEW IF EXISTS view_hdi_goods_supplier_hospital;
CREATE VIEW view_hdi_goods_supplier_hospital AS 
select a.*,b.id,b.goods_type,b.goods_code,b.goods_name,b.common_name,b.type_id,b.type_name,b.dept_id,b.factory_id,b.factory_code,b.factory_name from (
select r.supplier_id,s.supplier_code,s.supplier_name,r.hospital_id,h.hospital_grade,h.hospital_code,h.hospital_name 
from hdi_org_supplier_hospital_ref r 
left join hdi_org_hospital_info h on r.hospital_id=h.id 
left join hdi_org_supplier_info s on r.supplier_id=s.id
where r.del_flag=0
) a,  
(
select d.id,0 as goods_type,d.hospital_id,d.drugs_code as goods_code,d.drugs_name as goods_name,d.common_name,d.type_id,d.type_name,d.dept_id,d.factory_id,f.factory_code,f.factory_name 
from hdi_goods_hospital_drugs d left join hdi_org_factory_info f on d.factory_id=f.id
union
select r.id,1 as goods_type,r.hospital_id,r.reagent_code as goods_code,r.reagent_name as goods_name,r.common_name,r.type_id,r.type_name,r.dept_id,r.factory_id,f.factory_code,f.factory_name 
from hdi_goods_hospital_reagent r left join hdi_org_factory_info f on r.factory_id=f.id
union
select c.id,2 as goods_type,c.hospital_id,c.consumables_code as goods_code,c.consumables_name as goods_name,c.common_name,c.type_id,c.type_name,c.dept_id,c.factory_id,f.factory_code,f.factory_name 
from hdi_goods_hospital_consumables c left join hdi_org_factory_info f on c.factory_id=f.id
) b where a.hospital_id=b.hospital_id;

# view_hdi_goods_specs_match(平台供应商医院商品匹对视图)
DROP VIEW IF EXISTS view_hdi_goods_specs_match;
CREATE VIEW view_hdi_goods_specs_match AS 
select i.*,u.sources_supplier_id as supplier_sources_id, u.sources_hospital_id as hospital_sources_id
from (
select a.goods_type,a.platform_goods_id,p.goods_code as platform_goods_code,p.goods_name as platform_goods_name,p.common_name as platform_common_name,
p.dept_id as platform_dept_id,p.goods_unit as platform_goods_unit,a.platform_goods_specs_id,p.specs_code as platform_goods_specs_code,p.specs as platform_goods_specs_name,
p.factory_id as platform_factory_id,p.factory_code as platform_factory_code,p.factory_name as platform_factory_name,
s.supplier_id,a.supplier_goods_id,s.goods_code as supplier_goods_code,s.goods_name as supplier_goods_name,s.common_name as supplier_common_name,
s.dept_id as supplier_dept_id,s.goods_unit as supplier_goods_unit,a.supplier_goods_specs_id,s.sources_specs_id as supplier_sources_specs_id,s.specs_code as supplier_goods_specs_code,s.specs as supplier_goods_specs_name,
s.factory_id as supplier_factory_id,s.factory_code as supplier_factory_code,s.factory_name as supplier_factory_name,
h.hospital_id,a.hospital_goods_id,h.goods_code as hospital_goods_code,h.goods_name as hospital_goods_name,h.common_name as hospital_common_name,
h.dept_id as hospital_dept_id,h.goods_unit as hospital_goods_unit,a.hospital_goods_specs_id,h.sources_specs_id as hospital_sources_specs_id,h.specs_code as hospital_goods_specs_code,h.specs as hospital_goods_specs_name,
h.factory_id as hospital_factory_id,h.factory_code as hospital_factory_code,h.factory_name as hospital_factory_name
from (
select b.tgoods_type as goods_type,b.pgoods_id as platform_goods_id,b.pspecs_id as platform_goods_specs_id,b.tgoods_id as supplier_goods_id,
b.tspecs_id as supplier_goods_specs_id,c.tgoods_id as hospital_goods_id,c.tspecs_id as hospital_goods_specs_id
from 
(select torg_type,tgoods_type,tgoods_id,pgoods_id,tspecs_id,pspecs_id 
from hdi_unicode_goods_ship where torg_type=1 and ship_flag=1 and del_flag=0) b,
(select torg_type,tgoods_type,tgoods_id,pgoods_id,tspecs_id,pspecs_id 
from hdi_unicode_goods_ship where torg_type=0 and ship_flag=1 and del_flag=0) c
where b.tgoods_type=c.tgoods_type and b.pgoods_id=c.pgoods_id and b.pspecs_id=c.pspecs_id
) a,view_hdi_goods_specs_platform p,view_hdi_goods_specs_supplier s,view_hdi_goods_specs_hospital h
where a.goods_type=p.goods_type and a.platform_goods_id=p.goods_id and a.platform_goods_specs_id=p.goods_specs_id 
and a.goods_type=s.goods_type and a.supplier_goods_id=s.goods_id and a.supplier_goods_specs_id=s.goods_specs_id
and a.goods_type=h.goods_type and a.hospital_goods_id=h.goods_id and a.hospital_goods_specs_id=h.goods_specs_id
) i left join (select supplier_id,sources_supplier_id,hospital_id,sources_hospital_id from hdi_unicode_supply_ship where ship_flag=1) u on i.supplier_id=u.supplier_id and i.hospital_id=u.hospital_id;

# view_mcp_platform_supplier_goods_specs_match(平台供应商商品规格匹配)
DROP VIEW IF EXISTS view_mcp_platform_supplier_goods_specs_match;
CREATE VIEW view_mcp_platform_supplier_goods_specs_match AS
select a.goods_type,a.platform_goods_id,p.goods_code as platform_goods_code,p.goods_name as platform_goods_name,p.common_name as platform_common_name,
p.dept_id as platform_dept_id,p.goods_unit as platform_goods_unit,a.platform_goods_specs_id,p.specs_code as platform_goods_specs_code,p.specs as platform_goods_specs_name,
p.factory_id as platform_factory_id,p.factory_code as platform_factory_code,p.factory_name as platform_factory_name,
s.supplier_id,a.supplier_goods_id,s.type_name AS supplier_goods_type_name,s.goods_code as supplier_goods_code,s.goods_name as supplier_goods_name,s.common_name as supplier_common_name,
s.dept_id as supplier_dept_id,s.goods_unit as supplier_goods_unit,s.status as supplier_goods_status,s.del_flag as supplier_goods_del_flag,a.supplier_goods_specs_id,s.sources_specs_id as supplier_sources_specs_id,s.specs_code as supplier_goods_specs_code,s.specs as supplier_goods_specs_name,
s.factory_id as supplier_factory_id,s.factory_code as supplier_factory_code,s.factory_name as supplier_factory_name,s.agent_id AS agent_id,s.approvals AS approvals
from (
select b.tgoods_type as goods_type,b.pgoods_id as platform_goods_id,b.pspecs_id as platform_goods_specs_id,b.tgoods_id as supplier_goods_id,
b.tspecs_id as supplier_goods_specs_id
from 
(select torg_type,tgoods_type,tgoods_id,pgoods_id,tspecs_id,pspecs_id 
from hdi_unicode_goods_ship where torg_type=1 and ship_flag=1 and del_flag=0) b
) a,view_hdi_goods_specs_platform p,view_hdi_goods_specs_supplier s
where a.goods_type=p.goods_type and a.platform_goods_id=p.goods_id and a.platform_goods_specs_id=p.goods_specs_id 
and a.goods_type=s.goods_type and a.supplier_goods_id=s.goods_id and a.supplier_goods_specs_id=s.goods_specs_id;

# view_hdi_core_accept_detail(验收单明细视图)
DROP VIEW IF EXISTS view_hdi_core_accept_detail;
CREATE VIEW view_hdi_core_accept_detail AS 
select g.*,h.supplier_goods_id,h.supplier_goods_code,h.supplier_goods_name,h.supplier_common_name,h.supplier_dept_id,h.supplier_goods_unit,h.supplier_goods_specs_id,
h.supplier_goods_specs_code,h.supplier_goods_specs_name,h.supplier_factory_id,h.supplier_factory_code,h.supplier_factory_name,h.hospital_common_name,
h.hospital_factory_id,h.hospital_factory_code,h.hospital_factory_name from (
select a.accept_detail_id,a.accept_master_id,b.acceptno as accept_no,b.his_supplyid as sources_supplier_id,b.sources_supplier_code,b.sources_supplier_name,
b.supplier_id,b.supplier_code,b.supplier_name,b.uorganid as sources_hospital_id,b.sources_hospital_code,b.sources_hospital_name,
b.horg_id as hospital_id,b.hospital_code,b.hospital_name,b.storehouseid as store_house_id,b.ystorehouseid as sources_store_house_id,b.storehouse_no,b.storehouse_name,
b.dept_id,b.del_flag,a.goodsclass as goods_type,a.ygoodsid as sources_hospital_goods_id,a.ygoodsno as sources_hospital_goods_no,a.ygoodsname as sources_hospital_goods_name,
a.ygoodstypeid as sources_hospital_goods_specs_id,a.ygoodstypeno as sources_hospital_goods_specs_no,a.ygoodstypename as sources_hospital_goods_specs_name,
a.goodsid as hospital_goods_id,a.goodsno as hospital_goods_code,a.goodsname as hospital_goods_name,a.goodstypeid as hospital_goods_specs_id,a.goodstypeno as hospital_goods_specs_code,
a.goodstype as hospital_goods_specs_name,a.goodsunit as hospital_goods_unit,a.accept_qty as accept_number,a.lotid as lot_id,a.lotno as lot_no,a.proddate,a.invadate,
a.sourceid as supply_master_id,a.orgdatadtlid as supply_detail_id,f.supply_qty as supply_number,f.supply_unitprice as supply_price
from hdi_core_accept_detail a left join hdi_core_accept_master b on a.accept_master_id=b.accept_master_id
left join hdi_core_supply_detail f on a.sourcedtlid=f.supply_detail_id
) g left join view_hdi_goods_specs_match h on g.goods_type = h.goods_type and g.hospital_goods_id = h.hospital_goods_id
and g.hospital_goods_specs_id = h.hospital_goods_specs_id and g.supplier_id = h.supplier_id and g.hospital_id = h.hospital_id

# view_hdi_license_factory(厂商证照视图)
DROP VIEW IF EXISTS view_hdi_license_factory;
CREATE VIEW view_hdi_license_factory AS 
select a.*,b.supplier_name,d.factory_name,c.type as license_type,c.name as classify_name,c.is_warning,c.early_date,datediff(a.end_time, now()) as lose_time, 
case when c.is_warning=0 and date_format(now(),'%Y-%m-%d %H:%i:%s') <= a.end_time  then 0
when c.is_warning=1 and datediff(a.end_time, now()) between 0 and c.early_date and a.new_license_id is null then 1
when c.is_warning=1 and datediff(a.end_time, now()) > c.early_date and a.new_license_id is not null then 3
when c.is_warning=1 and datediff(a.end_time, now()) between 0 and c.early_date and a.new_license_id is not null then 3
when c.is_warning=1 and datediff(a.end_time, now()) > c.early_date then 0
when c.is_warning=1 and datediff(a.end_time, now()) < c.early_date then 2 
when c.is_warning=0 and date_format(now(),'%Y-%m-%d %H:%i:%s') > a.end_time  then 2
end as license_status
from hdi_license_factory_info a left join hdi_org_supplier_info b on a.supplier_id=b.id 
left join hdi_org_factory_info d on a.factory_id=d.id
left join hdi_license_classify_info c on a.classify_id=c.id;

# view_hdi_license_supplier(供应商证照视图)
DROP VIEW IF EXISTS view_hdi_license_supplier;
CREATE VIEW view_hdi_license_supplier AS 
select a.*,b.supplier_name,c.type as license_type,c.name as classify_name,c.is_warning,c.early_date,datediff(a.end_time, now()) as lose_time,
case when c.is_warning=0 and date_format(now(),'%Y-%m-%d %H:%i:%s') <= a.end_time  then 0
when c.is_warning=1 and datediff(a.end_time, now()) between 0 and c.early_date and a.new_license_id is null then 1
when c.is_warning=1 and datediff(a.end_time, now()) > c.early_date and a.new_license_id is not null then 3
when c.is_warning=1 and datediff(a.end_time, now()) between 0 and c.early_date and a.new_license_id is not null then 3
when c.is_warning=1 and datediff(a.end_time, now()) > c.early_date then 0
when c.is_warning=1 and datediff(a.end_time, now()) < c.early_date then 2 
when c.is_warning=0 and date_format(now(),'%Y-%m-%d %H:%i:%s') > a.end_time  then 2
end as license_status
from hdi_license_supplier_info a left join hdi_org_supplier_info b on a.supplier_id=b.id 
left join hdi_license_classify_info c on a.classify_id=c.id;

# view_hdi_license_agent(代理商证照视图)
DROP VIEW IF EXISTS view_hdi_license_agent;
CREATE VIEW view_hdi_license_agent AS 
select a.*,b.supplier_name,d.agent_name,d.credit_code,c.type as license_type,c.name as classify_name,c.is_warning,c.early_date,datediff(a.end_time, now()) as lose_time, 
case when c.is_warning=0 and date_format(now(),'%Y-%m-%d %H:%i:%s') <= a.end_time  then 0
when c.is_warning=1 and datediff(a.end_time, now()) between 0 and c.early_date and a.new_license_id is null then 1
when c.is_warning=1 and datediff(a.end_time, now()) > c.early_date and a.new_license_id is not null then 3
when c.is_warning=1 and datediff(a.end_time, now()) between 0 and c.early_date and a.new_license_id is not null then 3
when c.is_warning=1 and datediff(a.end_time, now()) > c.early_date then 0
when c.is_warning=1 and datediff(a.end_time, now()) < c.early_date then 2 
when c.is_warning=0 and date_format(now(),'%Y-%m-%d %H:%i:%s') > a.end_time  then 2
end as license_status
from hdi_license_agent_info a left join hdi_org_supplier_info b on a.supplier_id=b.id 
left join hdi_org_agent_info d on a.agent_id=d.id 
left join hdi_license_classify_info c on a.classify_id=c.id;

# view_hdi_license_goods(供应商商品证照视图)
DROP VIEW IF EXISTS view_hdi_license_goods;
CREATE VIEW view_hdi_license_goods AS 
select c.*,d.type as license_type,d.name as classify_name,d.is_warning,d.early_date,datediff(c.end_time, now()) as lose_time,
case when d.is_warning=0 and date_format(now(),'%Y-%m-%d %H:%i:%s') <= c.end_time  then 0
when d.is_warning=1 and datediff(c.end_time, now()) between 0 and d.early_date and c.new_license_id is null then 1
when d.is_warning=1 and datediff(c.end_time, now()) > d.early_date and c.new_license_id is not null then 3
when d.is_warning=1 and datediff(c.end_time, now()) between 0 and d.early_date and c.new_license_id is not null then 3
when d.is_warning=1 and datediff(c.end_time, now()) > d.early_date then 0
when d.is_warning=1 and datediff(c.end_time, now()) < d.early_date then 2 
when d.is_warning=0 and date_format(now(),'%Y-%m-%d %H:%i:%s') > c.end_time  then 2
end as license_status
from (select a.*,b.supplier_name,b.goods_code,b.goods_name,b.common_name,b.factory_id,b.factory_name from
hdi_license_goods_info a,view_hdi_goods_supplier b where a.goods_id=b.id and a.goods_type=b.goods_type
) c left join hdi_license_classify_info d on c.classify_id=d.id;

# view_hdi_license_warning(证照预警视图)
DROP VIEW IF EXISTS view_hdi_license_warning;
CREATE VIEW view_hdi_license_warning AS 
select id,supplier_id,supplier_name,license_type,classify_id,classify_name,name,number,begin_time,end_time,pic_url,status,dept_id,
create_id,create_time,edit_id,edit_time,del_flag,new_license_id,is_warning,early_date,lose_time,license_status
from view_hdi_license_supplier where is_warning=1 and del_flag=0 and license_status in (1,2) and new_license_id is null
union
select id,supplier_id,supplier_name,license_type,classify_id,classify_name,name,number,begin_time,end_time,pic_url,status,dept_id,
create_id,create_time,edit_id,edit_time,del_flag,new_license_id,is_warning,early_date,lose_time,license_status
from view_hdi_license_goods where is_warning=1 and del_flag=0 and license_status in (1,2) and new_license_id is null
union
select id,supplier_id,supplier_name,license_type,classify_id,classify_name,name,number,begin_time,end_time,pic_url,status,dept_id,
create_id,create_time,edit_id,edit_time,del_flag,new_license_id,is_warning,early_date,lose_time,license_status
from view_hdi_license_factory where is_warning=1 and del_flag=0 and license_status in (1,2) and new_license_id is null
union
select id,supplier_id,supplier_name,license_type,classify_id,classify_name,name,number,begin_time,end_time,pic_url,status,dept_id,
create_id,create_time,edit_id,edit_time,del_flag,new_license_id,is_warning,early_date,lose_time,license_status
from view_hdi_license_agent where is_warning=1 and del_flag=0 and license_status in (1,2) and new_license_id is null;
