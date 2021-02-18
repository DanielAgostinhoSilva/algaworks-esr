-- DELETE FROM restaurante WHERE id = 1;
-- DELETE FROM restaurante WHERE id = 2;
-- DELETE FROM restaurante WHERE id = 3;
--
-- DELETE FROM cozinha WHERE id = 1;
-- DELETE FROM cozinha WHERE id = 2;
--
-- DELETE FROM cidade WHERE id = 1;
-- DELETE FROM cidade WHERE id = 2;
-- DELETE FROM cidade WHERE id = 3;
-- DELETE FROM cidade WHERE id = 4;
-- DELETE FROM cidade WHERE id = 5;
--
-- DELETE FROM estado WHERE id = 1;
-- DELETE FROM estado WHERE id = 2;
-- DELETE FROM estado WHERE id = 3;
--
-- DELETE FROM forma_pagamento WHERE id = 1;
-- DELETE FROM forma_pagamento WHERE id = 2;
-- DELETE FROM forma_pagamento WHERE id = 3;
--
-- DELETE FROM permissao WHERE id = 1;
-- DELETE FROM permissao WHERE id = 2;

DELETE FROM restaurante where id > 0;
DELETE FROM cozinha where id > 0;
DELETE FROM cidade where id > 0;
DELETE FROM estado where id > 0;
DELETE FROM forma_pagamento where id > 0;
DELETE FROM permissao where id > 0;
