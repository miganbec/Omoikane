create view base_para_precios as
select p.id_articulo,
       p.descripcion,
       p.costo,
       p.impuestos           as porcentajeImpuestos,
       linea.descuento       as porcentajeDescuentoLinea,
       grupo.descuento       as porcentajeDescuentoGrupo,
       precios.descuento     as porcentajeDescuentoProducto,
       p.utilidad            as porcentajeUtilidad

from
  ramcachearticulos as p,
  precios as precios,
  lineas as linea,
  grupos as grupo
where
  p.id_articulo = precios.id_articulo
  AND linea.id_linea = p.id_linea
  AND grupo.id_grupo = p.id_grupo

