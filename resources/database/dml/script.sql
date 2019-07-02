-- Central Master
INSERT INTO central_master(id_central_number, central_number, central_code, central_number_pstn, central_smgr_ip, central_lang, central_access_pwd)
VALUES(1, '2000', 'Central1', '+51940984200', '135.169.18.60', 'es-PE', '2000');

-- Extensions
INSERT INTO extension(id_extension, id_central_number, extension_number, extension_number_pstn, extension_area_name, extension_intent_code, extension_intent_confidence, extension_person_fullname, extension_person_email, extension_person_phone)
VALUES(1, 1, '2001', '+123456789', 'AREA DE FACTURACION', 'INTENT_FACTURACION', '0.8', 'Juan Carlos', 'juancarlos@gmail.com', '+51321456987');
INSERT INTO parlanadb.extension(id_extension, id_central_number, extension_number, extension_number_pstn, extension_area_name, extension_intent_code, extension_intent_confidence, extension_person_fullname, extension_person_email, extension_person_phone)
VALUES(2, 1, '2002', '+9873654321', 'AREA DE CORTE', 'INTENT_CORTE_LINEA', '0.8', 'Sofia', 'sofia@gmail.com', '+51654789321');
