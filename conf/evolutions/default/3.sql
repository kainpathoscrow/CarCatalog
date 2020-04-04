-- !Ups

ALTER TABLE "Car" ADD created_at TIMESTAMP NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc');

-- !Downs

ALTER TABLE "Car" DROP created_at;