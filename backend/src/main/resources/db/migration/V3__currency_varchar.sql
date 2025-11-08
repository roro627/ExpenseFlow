ALTER TABLE expense_reports
    ALTER COLUMN currency TYPE VARCHAR(3)
    USING TRIM(currency);

ALTER TABLE expense_items
    ALTER COLUMN currency TYPE VARCHAR(3)
    USING TRIM(currency);
