INSERT INTO `branches` 
VALUES 
(2,'부산 사하점','부산광역시 부산진구','2025-05-24 03:10:30','2025-06-04 00:59:10'),
(3,'제주도 서귀포점','제주특별시 서귀포','2025-06-05 02:48:40','2025-06-05 02:48:40'),
(4,'부산 해운대점','부산광역시 해운대','2025-06-05 03:05:13','2025-06-05 03:05:13'),
(5,'부산 해d운대점','부산광역시 해운대','2025-06-05 03:05:33','2025-06-05 03:05:33'),
(6,'부산 송정점','부산광역시 해운대','2025-06-05 03:07:00','2025-06-05 03:07:00');

INSERT INTO `positions` VALUES (2,'과장'),(3,'대리'),(1,'부장'),(4,'사원'),(5,'점장');

SELECT * FROM positions;

INSERT INTO `employees` VALUES (46,2,4,3,25291020,'qwe123','$2a$10$Uo7xXA6GWl4CCwOgttq41OQ5gBLSnrxAgb8pAUqnFjezLpmgFXqle','ko','01012345678','2000-01-01','kohj0686@naver.com','APPROVED','2025-06-05 03:26:03','2025-06-05 03:43:09','EMPLOYED'),(47,2,4,3,25853780,'qwe1234','$2a$10$pbGWjZEM5r09zv1JAesz7uHjHWH6uEBUlJCkykWA9QG3qdyrIBr02','ko','01012345679','2000-01-01','kohj@naver.com','PENDING','2025-06-05 03:36:28','2025-06-05 03:36:28','EMPLOYED');

SELECT * FROM employees;
INSERT INTO `authorities` VALUES (1,'ADMIN'), (2,'MANAGER'),(3,'STAFF');

SELECT * FROM purchase_orders;
ALTER TABLE purchase_orders
CHANGE COLUMN purchase_order_date purchase_order_at DATETIME;

SELECT * FROM purchase_order_approvals;

SELECT * FROM book_reception_approvals;
ALTER TABLE book_reception_approvals
CHANGE COLUMN book_isbn book_title VARCHAR(255);
ALTER TABLE book_reception_approvals
CHANGE COLUMN branch_id branch_name VARCHAR(255) NOT NULL;
ALTER TABLE book_reception_approvals
MODIFY COLUMN reception_employee_id BIGINT NULL;
ALTER TABLE book_reception_approvals
ADD COLUMN book_isbn VARCHAR(255) NOT NULL;
