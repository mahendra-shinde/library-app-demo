INSERT into BOOKS ( book_author, book_status, title, book_category )
VALUES ('Yashwant Kanetkar', 'A', 'Let Us C', 'Programming');

INSERT into BOOKS ( book_author, book_status, title, book_category )
VALUES ('Yashwant Kanetkar', 'A', 'Let Us C++', 'Programming');

INSERT into MEMBERS (first_name, last_name, member_status)
VALUES ('Mahendra','Shinde','R');

INSERT into MEMBERS (first_name, last_name, member_status)
VALUES ('Sonali','Shinde','R');

INSERT into book_issues (book_id, member_id, issue_date, return_date_exp,  issue_status)
VALUES (1, 2, '2022-03-04','2022-03-12','P' );

UPDATE books SET book_status = 'N'
where book_id = 1;

COMMIT;
