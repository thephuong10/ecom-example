IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='ec_product_childs' AND xtype='U')
BEGIN
CREATE TABLE ec_product_childs (
    id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
    image NVARCHAR(MAX) NOT NULL,
    productId UNIQUEIDENTIFIER NOT NULL,
    quantity INT NOT NULL,
);
END;