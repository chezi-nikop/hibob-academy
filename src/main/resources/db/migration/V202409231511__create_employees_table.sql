CREATE TABLE IF NOT EXISTS employees
(
    id         BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    role       VARCHAR(50)  NOT NULL,
    company_id BIGINT REFERENCES company (id)
);

CREATE INDEX idx_company_id on employees (company_id);