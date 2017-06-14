-- init administrator account
UPDATE `user` set password_date = null WHERE email = 'first.admin@test.com';