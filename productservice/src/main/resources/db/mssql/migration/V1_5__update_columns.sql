IF EXISTS (SELECT * FROM sysobjects WHERE name='ec_products' AND xtype='U')
BEGIN
ALTER TABLE ec_products
ALTER COLUMN ratingAverage FLOAT NULL;
END;

IF EXISTS (SELECT * FROM sysobjects WHERE name='ec_products' AND xtype='U')
BEGIN
ALTER TABLE ec_products
ALTER COLUMN discount FLOAT NULL;
END;

IF EXISTS (SELECT * FROM sysobjects WHERE name='ec_products' AND xtype='U')
BEGIN
ALTER TABLE ec_products
ALTER COLUMN promotionId UNIQUEIDENTIFIER NULL;
END;