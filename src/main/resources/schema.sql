-- ************************************** customers

CREATE TABLE IF NOT EXISTS customers
(
 customer_id         uuid NOT NULL,
 customer_name       varchar(50) NOT NULL,
 customer_last_name  varchar(100) NOT NULL,
 customer_phone      varchar(50) NOT NULL,
 customer_email      varchar(50) NOT NULL,
 customer_created_at timestamp with time zone NOT NULL,
 CONSTRAINT PK_customers PRIMARY KEY ( customer_id ),
 CONSTRAINT ind_email UNIQUE ( customer_email )
);

-- ************************************** orders

CREATE TABLE IF NOT EXISTS orders
(
 order_id             uuid NOT NULL,
 order_total_value    numeric NOT NULL,
 order_payment_method varchar(50) NOT NULL,
 order_description    text NULL,
 order_created_at     timestamp with time zone NOT NULL,
 order_number         int NOT NULL,
 customer_id          uuid NOT NULL,
 CONSTRAINT PK_order PRIMARY KEY ( order_id ),
 CONSTRAINT FK_customer_order FOREIGN KEY ( customer_id ) REFERENCES customers ( customer_id ) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE INDEX IF NOT EXISTS fkIdx_31 ON orders
(
 customer_id
);







