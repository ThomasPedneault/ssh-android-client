insert into user (userid, name, password) values (1, 'Ian', '');
insert into user (userid, name, password) values (2, 'Sandy', '');
insert into user (userid, name, password) values (3, 'Jim', '');

insert into identity (username, password, owner) values ('android', 'android', 1);
insert into identity (username, password, owner) values ('thomas', 'thomas', 3);
insert into identity (username, password, owner) values ('1621638', 'password', 2);

insert into serverinfo (ip, name, owner) values ('10.39.167.25', 'UNIX VM', 1);
insert into serverinfo (ip, name, owner) values ('linux2-cs.johnabbott.qc.ca', 'Linux2', 1);
