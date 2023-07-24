CREATE TABLE todo (
  id BIGSERIAL PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  done BOOLEAN NOT NULL DEFAULT 'false'
);