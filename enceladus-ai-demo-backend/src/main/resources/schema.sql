DROP TABLE IF EXISTS task;

CREATE TABLE task (
    id BIGINT AUTO_INCREMENT PRIMARY KEY ,
    description VARCHAR(255),
    creation_date TIMESTAMP,
    due_date DATE
);
