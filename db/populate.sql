use RASBET;

insert into Role (rolename) values ("user");
insert into Role (rolename) values ("admin");
insert into Role (rolename) values ("specialist");

insert into Sport values (1, "Football", "Collective", 1, 2);
insert into Sport values (2, "Basket", "Collective", 0, 2);
insert into Sport values (3, "MotoGP", "Individual", 0, 8);

insert into Game values("testBasket1", 2, "active", null, "2022-11-20 20:00:00", null, 0);
insert into Participant (idGame, name, odd, idx) values ("testBasket1","Basket1",1.5, 1);
insert into Participant (idGame, name, odd, idx) values ("testBasket1","Basket2",2, 2);

insert into Game values("testBasket2",2, "active", null, "2022-11-21 20:00:00", null, 0);
insert into Participant (idGame, name, odd, idx) values ("testBasket2","Basket2",1, 1);
insert into Participant (idGame, name, odd, idx) values ("testBasket2","Basket3",3, 2);

insert into Game values("testBasket3",2, "active", null, "2022-11-22 20:00:00", null, 0);
insert into Participant (idGame, name, odd, idx) values ("testBasket3","Basket3",2, 1);
insert into Participant (idGame, name, odd, idx) values ("testBasket3","Basket1",1.5, 2);

insert into Game values("testMotoGP", 3, "active", null, "2022-11-22 20:00:00", null, 0);
insert into Participant (idGame, name, odd, idx) values ("testMotoGP","Part1",2, 1);
insert into Participant (idGame, name, odd, idx) values ("testMotoGP","Part2",1.5,2);
insert into Participant (idGame, name, odd, idx) values ("testMotoGP","Part3",2, 3);
insert into Participant (idGame, name, odd, idx) values ("testMotoGP","Part4",1.5,4);
insert into Participant (idGame, name, odd, idx) values ("testMotoGP","Part5",2, 5);
insert into Participant (idGame, name, odd, idx) values ("testMotoGP","Part6",1.5,6);
insert into Participant (idGame, name, odd, idx) values ("testMotoGP","Part7",2, 7);
insert into Participant (idGame, name, odd, idx) values ("testMotoGP","Part8",1.5,8);

insert into User (email, name, pwd, address, phone_number, nif, cc, birth, role, codeUser) values ("hansolo@mail.pt", "Han Solo", "pwd", "Moimenta da Beira", "999399991", "888288888", "77377777", "1976-03-21", 1, "1111111111");
insert into User (email, name, pwd, address, phone_number, nif, cc, birth, role) values ("admin1@rasbet.pt", "José Souto", "pwd", "Vila do Conde", "999999991", "888888888", "77777777", "1980-08-15", 2);
insert into User (email, name, pwd, address, phone_number, nif, cc, birth, role) values ("specialist1@rasbet.pt", "Luna Lovegood", "pwd", "Celorico de Basto", "999999992", "888888887", "77773778", "1991-05-28", 3);

insert into Promotion values ("friendBonus", 10, 0, 1);
insert into Promotion values ("bonus", 5, 0, 0);
insert into Promotion values ("multi", 1.05, 1.5, 0);

insert into Currency values ("EUR", 1); -- Euro
insert into Currency values ("BGN", 1.96); -- Bulgaria (1 EUR = 1.96 BGN)
insert into Currency values ("HRK", 7.53); -- Croatia (1 EUR = 7.53 HRK)
insert into Currency values ("CZK", 24.35); -- Czechia (1 EUR = 24.35 CZK)
insert into Currency values ("HUF", 410.30); -- Hungary (1 EUR = 410.30 HUF)
insert into Currency values ("PLN", 4.69); -- Poland (1 EUR = 4.69 PLN)
insert into Currency values ("RON", 4.91); -- Romania (1 EUR = 4.91 RON)
insert into Currency values ("SEK", 10.84); -- Sweden (1 EUR = 10.84 SEK)
