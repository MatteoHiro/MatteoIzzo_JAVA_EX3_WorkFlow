CREATE DATABASE WORK_FLOW;

USE WORK_FLOW;

CREATE TABLE User (
    id_user INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    username VARCHAR(255) NOT NULL,
    role_job VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    seniority VARCHAR(255) NOT NULL,
    data_creazione_utente TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Document (
    id_document INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    name_doc VARCHAR(255) NOT NULL,
    state_doc VARCHAR(255) NOT NULL,
    productionDate TIMESTAMP,  -- Timestamp per la data di produzione
    modifyDateTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,  -- Timestamp per la data di modifica
    id_user INT,
    FOREIGN KEY (id_user) REFERENCES User(id_user) ON DELETE SET NULL  -- Imposta NULL quando l'utente viene eliminato
);

CREATE TABLE document_version (
    documento_id INT,  -- Riferimento al documento originale
    modify_date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,  -- Data dell'ultima modifica
    user_id INT,  -- ID dell'utente che ha modificato il documento
    PRIMARY KEY (documento_id, modify_date_time),  -- Chiave primaria composta da documento_id e modify_date_time
    FOREIGN KEY (documento_id) REFERENCES Document(id_document),  -- Riferimento al documento originale senza ON DELETE CASCADE
    FOREIGN KEY (user_id) REFERENCES User(id_user)  -- Riferimento all'utente che ha modificato
);



