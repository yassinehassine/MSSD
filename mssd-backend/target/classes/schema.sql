-- Portfolio table schema (updated)
CREATE TABLE IF NOT EXISTS portfolio (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    formation_id BIGINT NOT NULL,
    image_path VARCHAR(255),
    client_name VARCHAR(255),
    project_date DATE,
    project_url VARCHAR(255),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    CONSTRAINT fk_portfolio_formation FOREIGN KEY (formation_id) REFERENCES formations(id)
);
