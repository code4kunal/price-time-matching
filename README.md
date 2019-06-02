# price-time-matching
A spring boot based order matching service based on price-time-matching algorithm.

## Solution is based on following principles:
  
  1. Every incoming whose side is sales will be matched with existing buy orders and vice-versa.
  2. For every new buy order, sales order with least amount will be given priority. If there are more than
  one order for that amount, priority will be given to order with earlier timestamp.(FIFO).
  3. For every new sales order, buy order with max amount will be given priority. If there are more than 
  one order for that amount, priority will be given to order with earlier timestamp. (FIFO).
  4. Currently, just for simulation purpose, we have taken time(java.sql.time) instead of timestamp.
  5. New order will be fullfilled according to the size, only if there are suitable matching orders.
  6. For any new sell order, order can only be matched if its price is more than sell order's price.
  7. For any new buy order, order can only be matched if its price is less than buy order's price.
  
## Implementation: 
  The problem has been realised as a HTTP REST services, with endpoints for adding new order and fetching list of orders.
   
## Pre-requisites: 
  1. Install postgres server and create a database called as 'order_book'.
  2. Make sure Java 1.8 is installed on your machine.
  3. After cloning the repo, go to root of the project.
  4. `mvn clean install`
  5. `mvn spring-boot:run`
  
## OrderBookApplication
  Endpoints are as follows.
  1. POST `http://localhost:8080/book/order`  --> endpoint for creating new order.
  Request body: 
  `{
	"size": 1000,
	"price": 98,
	"time": "08:50:00",
	"side": "SELL"
   }`
   
   Response object will have processed order and also txns involved(order consumed while matching).
  
  
  2. GET `http://localhost:8080/book/order?active=false` --> endpoint for fetching existing orders with an active filter.
