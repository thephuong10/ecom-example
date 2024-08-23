CREATE PROCEDURE sp_GetCategoryById
    @CategoryId UNIQUEIDENTIFIER
AS
BEGIN
WITH CategoryHierarchy AS (
    SELECT
        id,
        name,
        slug,
        parentId,
        level
    FROM
        ec_categories
    WHERE
        id = @CategoryId

    UNION ALL

    SELECT
        c.id,
        c.name,
        c.slug,
        c.parentId,
        c.level
    FROM
        ec_categories c
        INNER JOIN
        CategoryHierarchy ch
        ON
        c.id = ch.parentId
)

SELECT
    *
FROM
    CategoryHierarchy
END;