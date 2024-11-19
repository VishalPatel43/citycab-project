INSERT INTO app_user (name, email, password, mobile_number, birthdate, gender)
VALUES ('Aarav Sharma', 'aarav.sharma@example.com', '$2a$12$dcM67itpJtX8g1sMVpj04eukROMPIWTfhs010XsMSV2Y83dfiP76K',
        '+91-9000000001', '1990-01-01', 'MALE'),
       ('Anaya Patel', 'anaya.patel@example.com', '$2a$12$dcM67itpJtX8g1sMVpj04eukROMPIWTfhs010XsMSV2Y83dfiP76K',
        '+91-9000000002', '1985-05-05', 'FEMALE'),
       ('Vivaan Singh', 'vivaan.singh@example.com', '$2a$12$dcM67itpJtX8g1sMVpj04eukROMPIWTfhs010XsMSV2Y83dfiP76K',
        '+91-9000000003', '1992-07-20', 'MALE'),
       ('Diya Mehta', 'diya.mehta@example.com', '$2a$12$dcM67itpJtX8g1sMVpj04eukROMPIWTfhs010XsMSV2Y83dfiP76K',
        '+91-9000000004', '1988-10-10', 'FEMALE'),
       ('Vishal Patel', 'patel@email.com', '$2a$12$dcM67itpJtX8g1sMVpj04eukROMPIWTfhs010XsMSV2Y83dfiP76K',
        '+91-7046298181', '1998-04-03', 'MALE')
;

INSERT INTO user_roles (user_id, role)
VALUES (1, 'RIDER'), -- Aarav Sharma as RIDER
       (2, 'RIDER'), -- Anaya Patel as RIDER
       (3, 'RIDER'), -- Vivaan Singh as DRIVER
       (4, 'RIDER'), -- Diya Mehta as RIDER
       (5, 'RIDER'), -- Vishal Patel as RIDER
       (5, 'ADMIN'); -- Vishal Patel as ADMIN

