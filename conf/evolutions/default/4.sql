-- !Ups

ALTER TABLE "Car" ADD CONSTRAINT unique_number UNIQUE (number);
CREATE INDEX ON "Car"(number);
-- !Downs

ALTER TABLE "Car" DROP CONSTRAINT unique_number;