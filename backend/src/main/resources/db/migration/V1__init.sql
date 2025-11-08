CREATE TABLE IF NOT EXISTS expense_reports (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    employee_name VARCHAR(255) NOT NULL,
    currency CHAR(3) NOT NULL,
    total NUMERIC(14,2) NOT NULL DEFAULT 0,
    status VARCHAR(32) NOT NULL,
    submitted_at TIMESTAMP,
    manager_comment TEXT,
    finance_note TEXT,
    paid_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS expense_items (
    id SERIAL PRIMARY KEY,
    expense_report_id BIGINT NOT NULL REFERENCES expense_reports(id) ON DELETE CASCADE,
    tx_date DATE NOT NULL,
    amount NUMERIC(14,2) NOT NULL,
    currency CHAR(3) NOT NULL,
    category VARCHAR(100) NOT NULL,
    cost_center VARCHAR(100) NOT NULL,
    attachment_path VARCHAR(500),
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS purchase_requests (
    id SERIAL PRIMARY KEY,
    employee_name VARCHAR(255) NOT NULL,
    vendor VARCHAR(255) NOT NULL,
    total NUMERIC(14,2) NOT NULL,
    justification TEXT NOT NULL,
    attachment_path VARCHAR(500),
    status VARCHAR(32) NOT NULL,
    submitted_at TIMESTAMP,
    manager_comment TEXT,
    finance_note TEXT,
    paid_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS settings (
    id BIGINT PRIMARY KEY,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS settings_categories (
    settings_id BIGINT NOT NULL REFERENCES settings(id) ON DELETE CASCADE,
    category VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS settings_cost_centers (
    settings_id BIGINT NOT NULL REFERENCES settings(id) ON DELETE CASCADE,
    cost_center VARCHAR(100) NOT NULL
);

INSERT INTO settings (id)
SELECT 1 WHERE NOT EXISTS (SELECT 1 FROM settings WHERE id = 1);

INSERT INTO settings_categories (settings_id, category)
SELECT 1, cat FROM (VALUES ('Travel'),('Meals'),('Supplies')) AS c(cat)
WHERE NOT EXISTS (SELECT 1 FROM settings_categories WHERE settings_id = 1);

INSERT INTO settings_cost_centers (settings_id, cost_center)
SELECT 1, cc FROM (VALUES ('OPS'),('HR'),('ENG')) AS c(cc)
WHERE NOT EXISTS (SELECT 1 FROM settings_cost_centers WHERE settings_id = 1);
