
CREATE TABLE IF NOT EXISTS employee (
  id        BIGSERIAL PRIMARY KEY,
  nom       TEXT        NOT NULL,
  tel       TEXT        NOT NULL UNIQUE,
  salaire   NUMERIC(12,2) NOT NULL CHECK (salaire >= 0)
);


CREATE TABLE IF NOT EXISTS service (
  id        BIGSERIAL PRIMARY KEY,
  libelle   TEXT NOT NULL UNIQUE
);


CREATE TABLE IF NOT EXISTS admin (
  id_employee BIGINT PRIMARY KEY,
  CONSTRAINT fk_admin_employee
    FOREIGN KEY (id_employee)
      REFERENCES employee(id)
      ON DELETE RESTRICT ON UPDATE RESTRICT
);


CREATE TABLE IF NOT EXISTS manager (
  id_employee BIGINT PRIMARY KEY,
  prime       NUMERIC(12,2) NOT NULL CHECK (prime >= 0),
  service_id  BIGINT NOT NULL UNIQUE,
  CONSTRAINT fk_manager_employee
    FOREIGN KEY (id_employee)
      REFERENCES employee(id)
      ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT fk_manager_service
    FOREIGN KEY (service_id)
      REFERENCES service(id)
      ON DELETE RESTRICT ON UPDATE RESTRICT
);


CREATE TABLE IF NOT EXISTS developer (
  id_employee BIGINT PRIMARY KEY,
  specialite  TEXT NOT NULL CHECK (specialite IN ('FULLSTACK','FRONTEND','BACKEND')),
  service_id  BIGINT NOT NULL,
  CONSTRAINT fk_developer_employee
    FOREIGN KEY (id_employee)
      REFERENCES employee(id)
      ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT fk_developer_service
    FOREIGN KEY (service_id)
      REFERENCES service(id)
      ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- Index
CREATE INDEX IF NOT EXISTS idx_employee_nom ON employee (nom);
CREATE INDEX IF NOT EXISTS idx_developer_service_specialite ON developer (service_id, specialite);
