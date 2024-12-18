DELETE FROM RATING;
DELETE FROM QUESTION;
DELETE FROM SURVEY;

insert into SURVEY (id, name) VALUES (10, 'Survey1');
insert into SURVEY (id, name) VALUES (20, 'Survey2');

insert into QUESTION (id, text, survey_id) VALUES (1, 'Question1', 10);
insert into QUESTION (id, text, survey_id) VALUES (2, 'Question2', 10);

insert into QUESTION (id, text, survey_id) VALUES (3, 'Question3', 20);
insert into QUESTION (id, text, survey_id) VALUES (4, 'Question4', 20);
insert into QUESTION (id, text, survey_id) VALUES (5, 'Question5', 20);
insert into QUESTION (id, text, survey_id) VALUES (6, 'Question6', 20);
