CREATE TABLE employees
(
    id         SERIAL PRIMARY KEY,
    first_name VARCHAR(255)                                                 NOT NULL,
    last_name  VARCHAR(255)                                                 NOT NULL,
    role       VARCHAR(50) CHECK (role IN ('admin', 'manager', 'employee')) NOT NULL,
    company_id INT REFERENCES company (id)
);