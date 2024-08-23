IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='ec_product_childs' AND xtype='U')
BEGIN
DROP TABLE ec_product_childs;
END;

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='ec_product_variants' AND xtype='U')
BEGIN
DROP TABLE ec_product_variants;
END;


IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='ec_product_variant_colors' AND xtype='U')
BEGIN
CREATE TABLE ec_product_variant_colors (
    id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
    image NVARCHAR(MAX) NOT NULL,
    productId UNIQUEIDENTIFIER NOT NULL,
    name NVARCHAR(200) NOT NULL,
    quantity INT NOT NULL
);
END


IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='ec_product_variant_sizes' AND xtype='U')
BEGIN
CREATE TABLE ec_product_variant_sizes (
    id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
    productColorVariantId UNIQUEIDENTIFIER NOT NULL,
    name NVARCHAR(200) NOT NULL,
    quantity INT NOT NULL
);
END
