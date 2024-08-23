IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='ec_products' AND xtype='U')
BEGIN
CREATE TABLE ec_products (
    id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,
    slug NVARCHAR(255) NOT NULL,
    image NVARCHAR(MAX) NOT NULL,
    categoryId UNIQUEIDENTIFIER NOT NULL,
    creationTime DATETIME NOT NULL,
    lastModificationTime DATETIME NOT NULL,
    creatorUserId UNIQUEIDENTIFIER NOT NULL,
    lastModifierUserId UNIQUEIDENTIFIER NOT NULL,
    ratingAverage FLOAT NOT NULL,
    price DECIMAL(18, 2) NOT NULL,
    priceOriginal DECIMAL(18, 2) NOT NULL,
    discount FLOAT NOT NULL,
    promotionId UNIQUEIDENTIFIER NOT NULL,
    quantity INT NOT NULL
);
END;


IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='ec_product_variants' AND xtype='U')
BEGIN
CREATE TABLE ec_product_variants (
    id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
    image NVARCHAR(MAX) NOT NULL,
    productId UNIQUEIDENTIFIER NOT NULL,
    size NVARCHAR(100) NOT NULL,
    color NVARCHAR(100) NOT NULL,
    quantity INT NOT NULL
);
END


IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='ec_categories' AND xtype='U')
BEGIN
CREATE TABLE ec_categories (
    id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
    parentId UNIQUEIDENTIFIER,
    name NVARCHAR(100) NOT NULL,
    slug NVARCHAR(255) NOT NULL,
    level INT
);
END;


IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='ec_colors' AND xtype='U')
BEGIN
CREATE TABLE ec_colors (
    id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    slug NVARCHAR(255) NOT NULL,
);
END;


IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='ec_sizes' AND xtype='U')
BEGIN
CREATE TABLE ec_sizes (
    id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    slug NVARCHAR(255) NOT NULL,
    type NVARCHAR(100) NOT NULL
);
END;


IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='ec_promotions' AND xtype='U')
BEGIN
CREATE TABLE ec_promotions (
    id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,
    slug NVARCHAR(255) NOT NULL,
    image NVARCHAR(MAX) NOT NULL,
    description NVARCHAR(MAX),
    creationTime DATETIME NOT NULL,
    lastModificationTime DATETIME NOT NULL,
    creatorUserId UNIQUEIDENTIFIER NOT NULL,
    lastModifierUserId UNIQUEIDENTIFIER NOT NULL,
    startDate DATETIME NOT NULL,
    endDate DATETIME NOT NULL,
    discount FLOAT NOT NULL,
);
END;


IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='ec_inventory' AND xtype='U')
BEGIN
CREATE TABLE ec_inventory (
    id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
    productVariantId UNIQUEIDENTIFIER NOT NULL,
    selled INT NOT NULL,
    available INT NOT NULL,
);
END

