insert into SURVEY (id, name) VALUES (1, 'Teamwork Survey');

insert into QUESTION (id, survey_id, text) VALUES (1, 1, 'what extent do you think your team has clear goals?');
insert into QUESTION (id, survey_id, text) VALUES (2, 1, 'How effective is communication within your team?');
insert into QUESTION (id, survey_id, text) VALUES (3, 1, 'How well is your team able to resolve conflicts constructively?');
insert into QUESTION (id, survey_id, text) VALUES (4, 1, 'How motivated and engaged do you feel by your team leader?');
insert into QUESTION (id, survey_id, text) VALUES (5, 1, 'How well does your team overcome common challenges to' ||
                                                         ' ensure successful collaboration?');
