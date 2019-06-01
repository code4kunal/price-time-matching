CREATE TABLE order_book (
  order_id bigserial primary key,
  time time,
  size bigint,
  price bigint,
  side VARCHAR(10)
);

INSERT  into order_book values(	1,'8:00:01', 750, 101 , 'SELL');
INSERT  into order_book values(	2,'8:00:01', 750, 97 , 'BUY');
INSERT  into order_book values(	3,'8:00:02', 250, 102 , 'SELL');
INSERT  into order_book values(	4,'8:00:04', 250, 100 , 'BUY');
INSERT  into order_book values(	5,'8:00:05', 500, 101 , 'SELL');
INSERT  into order_book values(	6,'8:00:10', 500, 100 , 'BUY');
INSERT  into order_book values(	7,'8:00:10', 150, 96 , 'BUY');
INSERT  into order_book values(	8,'8:00:02', 750, 101 , 'SELL');







