CREATE TABLE response
(
    id         BIGSERIAL PRIMARY KEY,
    responseId BIGINT NOT NULL,
    feedbackId BIGINT NOT NULL,
    content    TEXT   NOT NULL,
    date       DATE DEFAULT CURRENT_DATE
);

CREATE INDEX idx_feedback_id on response (feedbackId);