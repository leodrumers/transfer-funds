INSERT INTO CURRENCY (CURRENCY) values ('USD');
INSERT INTO ACCOUNT (NAME, FUNDS, CREATE_DATE, CURRENCY_ID) VALUES ('Jhon doe', 200000, CURRENT_TIMESTAMP, 1);
INSERT INTO ACCOUNT (NAME, FUNDS, CREATE_DATE, CURRENCY_ID) VALUES ('Steve Jobs', 10000, CURRENT_TIMESTAMP, 1);
INSERT INTO ACCOUNT (NAME, FUNDS, CREATE_DATE, CURRENCY_ID) VALUES ('Bill Gates', 50000, CURRENT_TIMESTAMP, 1);
INSERT INTO ACCOUNT (NAME, FUNDS, CREATE_DATE, CURRENCY_ID) VALUES ('Jeff', 250000, CURRENT_TIMESTAMP, 1);

insert into transfer_history (amount, description, status, transfer_date, origin_account, destination_account) values (10000, 'Mi transfer', 'SUCCESS', CURRENT_TIMESTAMP, 1, 2);
insert into transfer_history (amount, description, status, transfer_date, origin_account, destination_account) values (10000, 'Mi transfer', 'SUCCESS', CURRENT_TIMESTAMP, 1, 3);
insert into transfer_history (amount, description, status, transfer_date, origin_account, destination_account) values (10000, 'Mi transfer', 'SUCCESS', CURRENT_TIMESTAMP, 1, 4);