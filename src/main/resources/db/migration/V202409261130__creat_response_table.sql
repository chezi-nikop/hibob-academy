CREATE TABLE if not exists response
(
    id         BIGSERIAL PRIMARY KEY,
    responder_id BIGINT NOT NULL,
    feedback_id BIGINT NOT NULL,
    content    TEXT   NOT NULL,
    company_Id BIGINT NOT NULL,
    date       DATE DEFAULT CURRENT_DATE
);

CREATE INDEX idx_feedback_id on response (feedback_id, company_Id);