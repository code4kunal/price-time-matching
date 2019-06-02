CREATE TABLE order_book (
  order_id bigserial primary key,
  time time,
  size bigint,
  price bigint,
  side VARCHAR(10)
);

Alter table order_book add COLUMN is_active boolean DEFAULT true;

INSERT  into order_book (time, size, price, side, is_active) values(	'8:00:01', 750, 101 , 'SELL', true);
INSERT  into order_book (time, size, price, side, is_active) values(	'8:00:01', 750, 97 , 'BUY', true);
INSERT  into order_book (time, size, price, side, is_active) values(	'8:00:02', 250, 102 , 'SELL', true);
INSERT  into order_book (time, size, price, side, is_active) values(	'8:00:04', 250, 100 , 'BUY', true);
INSERT  into order_book (time, size, price, side, is_active) values(	'8:00:05', 500, 101 , 'SELL', true);
INSERT  into order_book (time, size, price, side, is_active) values(	'8:00:10', 500, 100 , 'BUY', true);
INSERT  into order_book (time, size, price, side, is_active) values(	'8:00:10', 150, 96 , 'BUY', true);
INSERT  into order_book (time, size, price, side, is_active) values(	'8:00:02', 750, 101 , 'SELL', true);




