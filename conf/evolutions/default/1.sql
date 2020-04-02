-- !Ups

CREATE TABLE "Color" (
    "name" TEXT PRIMARY KEY
);
INSERT INTO "Color" VALUES
    ('Black'), ('White'), ('Red'), ('Silver'), ('Blue'), ('Yellow'), ('Orange'), ('Gold'), ('Grey'), ('Green');

CREATE TABLE "Model" (
    "name" TEXT PRIMARY KEY
);
INSERT INTO "Model" VALUES
   ('Audi'), ('BMW'), ('Ford'), ('Honda'), ('Lexus'), ('Nissan'), ('Opel'), ('Toyota'), ('Volkswagen'), ('LADA'), ('Mitsubishi');

CREATE TABLE "Car" (
   "id" SERIAL,
   "model" TEXT REFERENCES "Model"("name"),
   "color" TEXT REFERENCES "Color"("name"),
   "number" TEXT,
   "manufacture_year" INTEGER
);

-- !Downs
DROP TABLE "Color";
DROP TABLE "Model";
DROP TABLE "Car";