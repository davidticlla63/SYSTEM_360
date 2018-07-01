--
-- JBoss, Home of Professional Open Source
-- Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
-- contributors by the @authors tag. See the copyright.txt in the
-- distribution for a full listing of individual contributors.
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
-- http://www.apache.org/licenses/LICENSE-2.0
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

-- ----------------------------
-- Records of empresa
-- ----------------------------
INSERT INTO "public"."empresa" (id,nit,carnet_identidad,ciudad,direccion,estado,fecha_modificacion,fecha_registro,foto_perfil,peso_foto,razon_social,representante_legal,telefono,usuario_registro,actividad_economica) VALUES ('1', '0', '0', 'SANTA CRUZ DE LA SIERRA', 'Calle s/n Barrio S/N', 'AC', '2016-01-01 11:54:57.168', '2016-01-01 00:00:00', null, '65974', 'MUNDO VIRTUAL', 'MUNDO VIRTUAL', '70000000', 'admin', 'VENTA AL CREDITO Y AL CONTADO');

-- ----------------------------
-- Records of sucursal
-- ----------------------------
INSERT INTO "public"."sucursal" (id,usuario_registro,actividad,descripcion,direccion,estado,fecha_modificacion,fecha_registro,nit,nombre,numero_sucursal,telefono,id_empresa) VALUES ('1', 'admin','VENTA AL CREDITO Y CONTADO','SUCURSAL PRINCIPAL', 'Calle s/n Barrio S/N', 'AC', null, '2016-01-01 00:00:00', '30000000', 'CASA MATRIZ', '0', '3420000', '1');
-- ----------------------------
--  Sequence structure for sucursal_id_seq
-- ----------------------------
 ALTER SEQUENCE "public"."sucursal_id_seq" RESTART WITH 2;

-- ----------------------------
-- Records of usuario
-- ----------------------------
INSERT INTO "public"."usuario"(id,email ,encargado_venta,fecha_registro,foto_perfil,login,nombre,password,peso_foto,state,usuario_registro,fecha_modificacion,id_sucursal) VALUES (1,'admin.admin@gmail.com','TRUE','2016-01-01 00:00:00',null,'erp360','ADMINISTRADOR','erp360', 0,'SU','admin',null,1);
INSERT INTO "public"."usuario"(id,email ,encargado_venta,fecha_registro,foto_perfil,login,nombre,password,peso_foto,state,usuario_registro,fecha_modificacion,id_sucursal) VALUES (2,'demo.demo@gmail.com','TRUE','2016-01-01 00:00:00',null,'demo','USUARIO DEMO','demo', 0,'IN','admin',null,1);
-- ----------------------------
--  Sequence structure for usuario_id_seq
-- ----------------------------
ALTER SEQUENCE "public"."usuario_id_seq" RESTART WITH 3;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO "public"."roles"(id,estado,fecha_modificacion,fecha_registro,nombre,usuario_registro,descripcion) VALUES(1,'SU',null,'2016-01-01 00:00:00','ADMINISTRADOR','admin','Grupo de Usuario Super Us.');
-- ----------------------------
--  Sequence structure for roles_id_seq
-- ----------------------------
ALTER SEQUENCE "public"."roles_id_seq" RESTART WITH 2;


-- ----------------------------
-- Records of usuario_rol
-- ----------------------------
INSERT INTO "public"."usuario_rol"(id,estado,fecha_modificacion ,fecha_registro ,usuario_registro,id_roles, id_usuario) values(1,'AC',null,'2016-01-01 00:00:00','admin',1,1);
INSERT INTO "public"."usuario_rol"(id,estado,fecha_modificacion ,fecha_registro ,usuario_registro,id_roles, id_usuario) values(2,'AC',null,'2016-01-01 00:00:00','admin',1,2);
-- ----------------------------
--  Sequence structure for usuario_rol_id_seq
-- ----------------------------
ALTER SEQUENCE "public"."usuario_rol_id_seq" RESTART WITH 3;

-- ----------------------------
-- Records of modulo
-- ----------------------------
INSERT INTO "public"."modulo" (id,nombre,estado) VALUES (1,'SEGURIDAD','AC');
INSERT INTO "public"."modulo" (id,nombre,estado) VALUES (2,'PARAMETRIZACION','AC');
INSERT INTO "public"."modulo" (id,nombre,estado) VALUES (3,'COBRANZA','AC');
INSERT INTO "public"."modulo" (id,nombre,estado) VALUES (4,'INVENTARIO','AC');
INSERT INTO "public"."modulo" (id,nombre,estado) VALUES (5,'REPORTE','AC');


-- ----------------------------
-- Records of permiso
-- ----------------------------
INSERT INTO "public"."permiso"(id,nombre,id_modulo,estado) VALUES ('1', 'USUARIO',1,'AC');
INSERT INTO "public"."permiso"(id,nombre,id_modulo,estado) VALUES ('2', 'ROL',1,'AC');
INSERT INTO "public"."permiso"(id,nombre,id_modulo,estado) VALUES ('3', 'PERMISO',1,'AC');
INSERT INTO "public"."permiso"(id,nombre,id_modulo,estado) VALUES ('4', 'EMPRESA',2,'AC');
INSERT INTO "public"."permiso"(id,nombre,id_modulo,estado) VALUES ('5', 'SUCURSAL',2,'AC');
INSERT INTO "public"."permiso"(id,nombre,id_modulo,estado) VALUES ('6', 'GESTION',2,'AC');
INSERT INTO "public"."permiso"(id,nombre,id_modulo,estado) VALUES ('7', 'NOTA DE VENTA',3,'AC');
INSERT INTO "public"."permiso"(id,nombre,id_modulo,estado) VALUES ('8', 'CLIENTE',3,'AC');
INSERT INTO "public"."permiso"(id,nombre,id_modulo,estado) VALUES ('9', 'COBROS',3,'AC');
INSERT INTO "public"."permiso"(id,nombre,id_modulo,estado) VALUES ('10', 'PARAMETRIZACION COBRANZA',3,'AC');
INSERT INTO "public"."permiso"(id,nombre,id_modulo,estado) VALUES ('11', 'CATALOGO',4,'AC');
INSERT INTO "public"."permiso"(id,nombre,id_modulo,estado) VALUES ('12', 'KARDEX',4,'AC');
INSERT INTO "public"."permiso"(id,nombre,id_modulo,estado) VALUES ('13', 'ORDEN INGRESO',4,'AC');
INSERT INTO "public"."permiso"(id,nombre,id_modulo,estado) VALUES ('14', 'ORDEN SALIDA',4,'AC');
INSERT INTO "public"."permiso"(id,nombre,id_modulo,estado) VALUES ('15', 'ORDEN TRASPASO',4,'AC');
INSERT INTO "public"."permiso"(id,nombre,id_modulo,estado) VALUES ('16', 'TOMA INVENTARIO',4,'AC');
INSERT INTO "public"."permiso"(id,nombre,id_modulo,estado) VALUES ('17', 'PROVEEDOR',4,'AC');
INSERT INTO "public"."permiso"(id,nombre,id_modulo,estado) VALUES ('18', 'ALMACEN',4,'AC');
INSERT INTO "public"."permiso"(id,nombre,id_modulo,estado) VALUES ('19', 'GRUPO PRODUCTO',4,'AC');
INSERT INTO "public"."permiso"(id,nombre,id_modulo,estado) VALUES ('20', 'LINEA PRODUCTO',4,'AC');
INSERT INTO "public"."permiso"(id,nombre,id_modulo,estado) VALUES ('21', 'PRODUCTO',4,'AC');
INSERT INTO "public"."permiso"(id,nombre,id_modulo,estado) VALUES ('22', 'UNIDAD MEDIDA',4,'AC');
INSERT INTO "public"."permiso"(id,nombre,id_modulo,estado) VALUES ('23', 'PARAMETROS INVENTARIO',4,'AC');
INSERT INTO "public"."permiso"(id,nombre,id_modulo,estado) VALUES ('24', 'CUENTAS CLIENTE',5,'AC');
INSERT INTO "public"."permiso"(id,nombre,id_modulo,estado) VALUES ('25', 'VENTAS',5,'AC');

-- ----------------------------
-- Records of privilegio
-- ----------------------------
INSERT INTO "public"."privilegio"(id,estado,fechamodificacion,fecharegistro, usuarioregistro, id_permiso,id_roles)VALUES(1,'AC',null,'2015-01-01 00:00:00','admin', 1,1);
INSERT INTO "public"."privilegio"(id,estado,fechamodificacion,fecharegistro, usuarioregistro, id_permiso,id_roles)VALUES(2,'AC',null,'2015-01-01 00:00:00','admin', 2,1);
INSERT INTO "public"."privilegio"(id,estado,fechamodificacion,fecharegistro, usuarioregistro, id_permiso,id_roles)VALUES(3,'AC',null,'2015-01-01 00:00:00','admin', 3,1);
INSERT INTO "public"."privilegio"(id,estado,fechamodificacion,fecharegistro, usuarioregistro, id_permiso,id_roles)VALUES(4,'AC',null,'2015-01-01 00:00:00','admin', 4,1);
INSERT INTO "public"."privilegio"(id,estado,fechamodificacion,fecharegistro, usuarioregistro, id_permiso,id_roles)VALUES(5,'AC',null,'2015-01-01 00:00:00','admin', 5,1);
INSERT INTO "public"."privilegio"(id,estado,fechamodificacion,fecharegistro, usuarioregistro, id_permiso,id_roles)VALUES(6,'AC',null,'2015-01-01 00:00:00','admin', 6,1);
INSERT INTO "public"."privilegio"(id,estado,fechamodificacion,fecharegistro, usuarioregistro, id_permiso,id_roles)VALUES(7,'AC',null,'2015-01-01 00:00:00','admin', 7,1);
INSERT INTO "public"."privilegio"(id,estado,fechamodificacion,fecharegistro, usuarioregistro, id_permiso,id_roles)VALUES(8,'AC',null,'2015-01-01 00:00:00','admin', 8,1);
INSERT INTO "public"."privilegio"(id,estado,fechamodificacion,fecharegistro, usuarioregistro, id_permiso,id_roles)VALUES(9,'AC',null,'2015-01-01 00:00:00','admin', 9,1);
INSERT INTO "public"."privilegio"(id,estado,fechamodificacion,fecharegistro, usuarioregistro, id_permiso,id_roles)VALUES(10,'AC',null,'2015-01-01 00:00:00','admin', 10,1);
INSERT INTO "public"."privilegio"(id,estado,fechamodificacion,fecharegistro, usuarioregistro, id_permiso,id_roles)VALUES(11,'AC',null,'2015-01-01 00:00:00','admin', 11,1);
INSERT INTO "public"."privilegio"(id,estado,fechamodificacion,fecharegistro, usuarioregistro, id_permiso,id_roles)VALUES(12,'AC',null,'2015-01-01 00:00:00','admin', 12,1);
INSERT INTO "public"."privilegio"(id,estado,fechamodificacion,fecharegistro, usuarioregistro, id_permiso,id_roles)VALUES(13,'AC',null,'2015-01-01 00:00:00','admin', 13,1);
INSERT INTO "public"."privilegio"(id,estado,fechamodificacion,fecharegistro, usuarioregistro, id_permiso,id_roles)VALUES(14,'AC',null,'2015-01-01 00:00:00','admin', 14,1);
INSERT INTO "public"."privilegio"(id,estado,fechamodificacion,fecharegistro, usuarioregistro, id_permiso,id_roles)VALUES(15,'AC',null,'2015-01-01 00:00:00','admin', 15,1);
INSERT INTO "public"."privilegio"(id,estado,fechamodificacion,fecharegistro, usuarioregistro, id_permiso,id_roles)VALUES(16,'AC',null,'2015-01-01 00:00:00','admin', 16,1);
INSERT INTO "public"."privilegio"(id,estado,fechamodificacion,fecharegistro, usuarioregistro, id_permiso,id_roles)VALUES(17,'AC',null,'2015-01-01 00:00:00','admin', 17,1);
INSERT INTO "public"."privilegio"(id,estado,fechamodificacion,fecharegistro, usuarioregistro, id_permiso,id_roles)VALUES(18,'AC',null,'2015-01-01 00:00:00','admin', 18,1);
INSERT INTO "public"."privilegio"(id,estado,fechamodificacion,fecharegistro, usuarioregistro, id_permiso,id_roles)VALUES(19,'AC',null,'2015-01-01 00:00:00','admin', 19,1);
INSERT INTO "public"."privilegio"(id,estado,fechamodificacion,fecharegistro, usuarioregistro, id_permiso,id_roles)VALUES(20,'AC',null,'2015-01-01 00:00:00','admin', 20,1);
INSERT INTO "public"."privilegio"(id,estado,fechamodificacion,fecharegistro, usuarioregistro, id_permiso,id_roles)VALUES(21,'AC',null,'2015-01-01 00:00:00','admin', 21,1);
INSERT INTO "public"."privilegio"(id,estado,fechamodificacion,fecharegistro, usuarioregistro, id_permiso,id_roles)VALUES(22,'AC',null,'2015-01-01 00:00:00','admin', 22,1);
INSERT INTO "public"."privilegio"(id,estado,fechamodificacion,fecharegistro, usuarioregistro, id_permiso,id_roles)VALUES(23,'AC',null,'2015-01-01 00:00:00','admin', 23,1);
INSERT INTO "public"."privilegio"(id,estado,fechamodificacion,fecharegistro, usuarioregistro, id_permiso,id_roles)VALUES(24,'AC',null,'2015-01-01 00:00:00','admin', 24,1);
INSERT INTO "public"."privilegio"(id,estado,fechamodificacion,fecharegistro, usuarioregistro, id_permiso,id_roles)VALUES(25,'AC',null,'2015-01-01 00:00:00','admin', 25,1);

-- ----------------------------
--  Sequence structure for privilegio_id_seq
-- ----------------------------
ALTER SEQUENCE "public"."privilegio_id_seq" RESTART WITH 26;


-- ----------------------------
-- Records of gestion
-- ----------------------------
INSERT INTO "public"."gestion" (id,estado,estado_cierre,fecha_modificacion,fecha_registro,gestion,iniciada,usuario_registro,id_empresa) VALUES ('1','AC','AC',null,'2016-01-01 00:00:00','2016','FALSE','admin','1');


-- ----------------------------
-- Records of parametro_empresa
-- ----------------------------
INSERT INTO "public"."parametro_empresa" (id,centro_costo,codificacion_etandar,estado,fecha_modificacion,fecha_registro,nivel_maximo,usuario_registro,id_empresa) VALUES ('1', TRUE, '9.99.999.9999.99999', 'AC', null, '2016-01-01 12:23:42.037', '5', 'admin', '1');


-- ----------------------------
-- Records of parametro_cobranza
-- ----------------------------
INSERT INTO "public"."parametro_cobranza" (id,cuotas_permitidas,estado,fecha_modificacion,fecha_registro,porcentaje_cuota_inicial,tipo_disminucion_stock,usuario_registro,coeficiente_interes_mensual)VALUES ('1', '3', 'AC', null, '2016-01-01 12:23:42.037', '30','VOS', 'admin','2.25');

-- ----------------------------
-- Records of unidad_medida
-- ----------------------------
INSERT INTO "public"."unidad_medida" (id,codigo,descripcion,estado,fecha_registro,nombre,usuario_registro) VALUES ('1','000001', 'PIEZA', 'AC', '2016-05-28 10:38:14.323', 'PIEZA', 'admin');
-- ----------------------------
--  Sequence structure for unidad_medida_id_seq
-- ----------------------------
ALTER SEQUENCE "public"."unidad_medida_id_seq" RESTART WITH 2;

-- ----------------------------
-- Records of grupo_producto
-- ----------------------------
INSERT INTO "public"."grupo_producto"(id,codigo,descripcion,porcentaje_venta_credito,porcentaje_venta_contado,estado,fecha_registro,nombre,usuario_registro) VALUES ('1', 'GP000001', 'MUEBLES TV',25,25, 'AC', '2016-01-01 12:23:42.037', 'MUEBLES TV', 'admin');
ALTER SEQUENCE "public"."grupo_producto_id_seq" RESTART WITH 2;


-- ----------------------------
-- Records of linea_producto
-- ----------------------------
INSERT INTO "public"."linea_producto"(id,codigo,descripcion,estado,id_grupo_producto,fecha_registro,nombre,usuario_registro) VALUES ('1', 'LP000001', 'LAVADORAS LG', 'AC','1', '2016-01-01 12:23:42.037', 'LAVADORAS LG', 'admin');
ALTER SEQUENCE "public"."linea_producto_id_seq" RESTART WITH 2;


-- ----------------------------
-- Records of producto
-- ----------------------------
INSERT INTO "public"."producto" (id,codigo,descripcion,estado,fecha_registro,nombre,precio_unitario,usuario_registro,id_linea_producto,id_unidad_medida) VALUES ('1', '000001', 'PRODUCTO DEMO', 'AC', '2016-06-01 11:08:41.009', 'PRODUCTO DEMO', '0', 'admin','1' ,'1');
ALTER SEQUENCE "public"."producto_id_seq" RESTART WITH 2;


-- ----------------------------
-- Records of cliente
-- ----------------------------
--INSERT INTO "public"."cliente" (id,ci,codigo,correo,dias_permitidos,direccion,estado,fecha_modificacion,fecha_registro,nit,nombre,permitir_credito,razon_social,telefono,tipo,usuario_registro,id_empresa,id_tipo_cliente) VALUES ('1', '10666626', '1', 'mbr.bejarano@gmail.com', '30', 'Urb. El dorado 2', 'AC', null, '2016-01-01 00:00:00', '10666626', 'Mauricio Bejarano', 'SI', 'ERP360', '77897764', 'JURIDICO', 'admin', '1', '1');
-- ----------------------------
--  Sequence structure for cliente_id_seq
-- ----------------------------
--ALTER SEQUENCE "public"."cliente_id_seq" RESTART WITH 2;

-- ----------------------------
-- Records of almacen
-- ----------------------------
INSERT INTO "public"."almacen" (id,codigo,direccion,estado,fecha_registro,nombre,telefono,tipo_almacen,usuario_registro,id_gestion) VALUES ('1', 'AL000001', 'Av. Principal', 'AC', '2016-01-01 00:00:00', 'ALMACEN CENTRAL', '3984798', 'ALMACEN CENTRAL', 'admin', '1'); 
-- ----------------------------
--  Sequence structure for almacen_id_seq
-- ----------------------------
ALTER SEQUENCE "public"."almacen_id_seq" RESTART WITH 2;

-- ----------------------------
-- Records of almacen_encargado
-- ----------------------------
INSERT INTO "public"."almacen_encargado"(id,estado,fecha_registro,usuario_registro,id_almacen,id_encargado) VALUES ('1', 'AC', '2016-01-01 00:00:00', 'admin', '1', '1'); 
-- ----------------------------
--  Sequence structure for almacen_encargado_id_seq
-- ----------------------------
 ALTER SEQUENCE "public"."almacen_encargado_id_seq" RESTART WITH 2;
 
 -- ----------------------------
-- Records of proveedor
-- ----------------------------
INSERT INTO "public"."proveedor" (id,usuario_registro,ci_contacto,codigo,direccion,direccion_contacto,email_contacto,estado,fecha_registro,nit,nombre,nombre_contacto,telefono_contacto,id_gestion,id_empresa) VALUES ('1', 'admin', '18249189', 'PV0000001', 'Av. El corral #89', 'Av. El corral #89', 'mbr.bejarano@gmail.com', 'AC', '2016-01-01 00:00:00', '18249189', 'IMPORT EXPORT MUEBLES SRL', 'JUAN CARLOS B.', '78294234', '1', '1'); 
-- ----------------------------
--  Sequence structure for proveedor_id_seq
-- ----------------------------
ALTER SEQUENCE "public"."proveedor_id_seq" RESTART WITH 2;

 -- ----------------------------
-- Records of parametro_inventario
-- ----------------------------
INSERT INTO "public"."parametro_inventario" (id,estado,fecha_modificacion,fecha_registro,tipo_valuacion,usuario_registro)  VALUES ('1', 'AC', null, '2016-01-01 00:00:00', 'PEPS', 'admin');
-- ----------------------------

--CREATE OR REPLACE FUNCTION proc_movimiento_caja()
--RETURNS trigger AS '
--DECLARE
--var_correlativo text;
--var_gestion int;
--var_fecha_registro timestamp without time zone;
--var_usuario text;
--var_total_importe_nacional double precision;
--var_total_importe_extranjero double precision;
--var_moneda text;
--var_tipo_cambio double precision;
--BEGIN
--var_gestion = NEW.id_gestion;
--var_fecha_registro = NEW.fecha_registro;
--var_usuario = NEW.usuario_registro;
--var_moneda = NEW.moneda;
--var_tipo_cambio = NEW.tipo_cambio;
--var_correlativo = ( SELECT to_char( ( count(em)::integer + 1), ''FM999999'') FROM movimiento_caja em WHERE (em.estado=''AC'' OR em.estado=''IN'' OR em.estado=''PR'') AND em.id_gestion= var_gestion);
--INSERT INTO movimiento_caja (correlativo,estado,fecha_aprobacion,fecha_documento,fecha_modificacion,fecha_registro,moneda,motivo_ingreso,numero_documento,observacion,
--tipo,tipo_cambio,tipo_documento,total_importe_extranjero,total_importe_nacional,usuario_registro,id_gestion,id_usuario_aprobacion)
--VALUES (var_correlativo, ''PR'', var_fecha_registro, var_fecha_registro, null, var_fecha_registro, var_moneda, ''INGRESO POR CUOTA INICIAL VENTA DE PRODUCTO(S) AL CREDITO'', ''0'', ''Ninguna'', ''INGRESO'', var_tipo_cambio, ''N/A'', var_total_importe_extranjero, var_total_importe_nacional, var_usuario, var_gestion, ''3'');
--NEW.id_movimiento_caja := (SELECT currval(''movimiento_caja_id_seq''));
--RETURN NEW;
--END' LANGUAGE 'plpgsql'
-- -----------------------------------------------------
--CREATE TRIGGER trigger_movimiento_caja
--BEFORE INSERT ON reserva_venta
--FOR EACH ROW
--EXECUTE PROCEDURE proc_movimiento_caja()


