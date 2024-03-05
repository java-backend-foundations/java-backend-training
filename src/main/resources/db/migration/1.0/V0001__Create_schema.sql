-- Leave a large ID space reserved for master-data and test-data
CREATE SEQUENCE HIBERNATE_SEQUENCE START WITH 1000000;

CREATE TABLE TASK_LIST (
ID NUMBER(19,0) NOT NULL AUTO_INCREMENT,
NAME VARCHAR(256),
PRIMARY KEY (ID)
);

CREATE TABLE PERSON (
ID NUMBER(19,0) NOT NULL AUTO_INCREMENT,
EMAIL VARCHAR(256) NOT NULL,
TASK_LIST_ID NUMBER(19,0),
PRIMARY KEY (ID),
FOREIGN KEY (TASK_LIST_ID) REFERENCES TASK_LIST(ID)
);

CREATE TABLE TASK_ITEM (
ID NUMBER(19,0) NOT NULL AUTO_INCREMENT,
NAME VARCHAR(256),
COMPLETED NUMBER(1,0) DEFAULT 0,
DEADLINE TIMESTAMP,
TASK_LIST_ID NUMBER(19,0),
PRIMARY KEY (ID),
FOREIGN KEY (TASK_LIST_ID) REFERENCES TASK_LIST(ID)
);