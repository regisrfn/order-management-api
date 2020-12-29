-- *************** SqlDBM: PostgreSQL ****************;
-- ***************************************************;


-- ************************************** "public"."Order"

CREATE TABLE IF NOT EXISTS orders
(
 order_id             uuid NOT NULL,
 order_total_value    numeric NOT NULL,
 order_payment_method varchar(50) NOT NULL,
 order_description    text NULL,
 order_created_at     timestamp with time zone NOT NULL,
 CONSTRAINT PK_order PRIMARY KEY ( order_id )
);







