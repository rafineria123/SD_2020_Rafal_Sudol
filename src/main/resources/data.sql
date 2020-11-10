

INSERT INTO `shop` (`shop_id`, `image_url`, `name`) VALUES
(1, 'images/shop_1.png', 'Biedronka'),
(2, 'images/shop_2.png', 'Avon'),
(3, 'images/shop_3.png', 'X-kom'),
(4, 'images/shop_4.png', 'Tesco'),
(5, 'images/shop_3.png', 'Empik'),
(6, 'images/shop_4.png', 'Media Markt');

INSERT INTO `tag` (`tag_id`, `name`) VALUES
(1, 'Elektronika'),
(2, 'Motoryzacja'),
(3, 'Gaming'),
(4, 'Moda'),
(5, 'Artykuły Spożywcze'),
(6, 'Sport i turystyka');


INSERT INTO `user` (`user_id`, `role`, `cr_date`, `email`, `login`, `password`, `status`, `information_id`, `rank_id`) VALUES
(1, 'USER', '2020-06-16 08:52:39', 'rafineria@gmail.com', 'rafineria', '$2a$10$09pC1fgfYgZPJBFhCRikOenf5uYnbP4E2kmsGUMofE9SzoAVz6EZy', 'Zatwierdzone', NULL, NULL),
(2, 'USER', '2020-06-16 08:52:39', 'rafineria123@gmail.com', 'rafineria123', '$2a$10$rehd0BIqzYtkm9KNGmZ8PuPgBqIIIfzGT/a29FCyHuNrtIZdIu1XS', 'Zatwierdzone', NULL, NULL),
(3, 'USER', '2020-06-16 08:52:39', 'artyleria@gmail.com', 'artyleria', '$2a$10$09pC1fgfYgZPJBFhCRikOenf5uYnbP4E2kmsGUMofE9SzoAVz6EZy', 'Zatwierdzone', NULL, NULL),
(4, 'USER', '2020-06-16 08:52:39', 'xkom@gmail.com', 'xkom', '$2a$10$09pC1fgfYgZPJBFhCRikOenf5uYnbP4E2kmsGUMofE9SzoAVz6EZy', 'Zatwierdzone', NULL, NULL);

INSERT INTO `discount` (`discount_id`, `content`, `creationdate`, `current_price`, `discount_link`, `expire_date`, `image_url`, `old_price`, `shipment_price`, `status`, `title`, `shop_id`, `tag_id`, `user_id`) VALUES
(1, 'W Kaufland dostępne w dobrej cenie baterie alkaliczne AA oraz AAA 10szt. (wychodzi 1zł za sztukę). Dostępna duża ilość. Znalezione w Pszczynie, ale z informacji innych użytkowników okazja ogólnopolska.', '2020-06-16 08:52:39', 220, 'https://www.biedronka.pl/pl/dom-nf-10-06', '2020-06-16 08:52:39', 'images/discount_1.jpg', 255, 15, 'Zatwierdzone', 'Baterie alkaliczne AA', 6, 1, 1),
(2, 'Oferta obowiązuje od 12.06 do 13.06.2020 r.', '2020-06-16 08:52:39', 5, 'https://www2.pl.avon.com/pl-home', '2020-06-16 08:52:39', 'images/discount_2.jpg', 10, 0, 'Zatwierdzone', 'Brzoskwinia @ Biedronka', 1, 5, 1),
(3, 'W ramach usługi Xbox Game Pass w czerwcu zagramy w : Xbox Game Pass - nowe gry Battlefleet Gothic Armada 2 (PC) – 11 czerwca Battletech (PC) – 11 czerwca Dungeon of the End.', '2020-06-16 08:52:39', 1, 'https://www.x-kom.pl/p/568960-smartfon-telefon-realme-x3-superzoom-12256gb-glacier-blue-120hz.html', '2020-06-16 08:52:39', 'images/discount_3.jpg', 100, 0, 'Zatwierdzone', 'Xbox Game Pass - Czerwiec', 5, 3, 1),
(4, 'W Biedronce od 12.06 kawa mielona Dallmayr Classic Krafti.', '2020-06-16 08:52:39', 15, 'https://www2.pl.avon.com/pl-home', '2020-06-16 08:52:39', 'images/discount_4.jpg', 20, 0, 'Zatwierdzone', 'Kawa Dallmayr Classic Krafti.', 1, 3, 2),
(5, 'Dostawa gratis Przecena z 129.99$ na 84.99$. Wersja S kosztuje po przecenie 55.24$.', '2020-06-16 08:52:39', 350, 'https://www2.pl.avon.com/pl-home', '2020-06-16 08:52:39', 'images/discount_5.jpg', 500, 10, 'Zatwierdzone', 'IPRee namiot 3 w 1 dla 5-8 osób', 4, 6, 2),
(6, 'W Tesco w Jarosławiu przecena płynu zimowego Borygo. Zostało jeszcze minimum 10 sztuk. Oferta lokalna. Małe miasto, ale może komuś się przyda ta informacja.', '2020-06-16 08:52:39', 17, 'https://www2.pl.avon.com/pl-home', '2020-06-16 08:52:39', 'images/discount_6.jpg', 35, 0, 'Zatwierdzone', 'Płyn do chłodnic samochodowych', 2, 2, 2);

INSERT INTO `comment` (`comment_id`, `content`, `cr_date`, `upper_comment_id`, `discount_id`, `post_id`, `user_id`) VALUES
(1, 'Słownik podpowiedział nie to słowo co powinien. Moja wina że nie sprawdziłem. Ale nie zmienia to faktu że komentarze w tym stylu co napisałeś są bez sensu. Co to ma wnosić do obecnej sytuacji. Kiedyś może mogło być drożej, mogło być taniej. Dzisiaj taka cena jest najtańsza i na tym polega ten serwis.', '2020-06-16 08:52:39', NULL, 3, NULL, 1);


INSERT INTO `conversation` (`conversation_id`) VALUES
(1),
(2);

INSERT INTO `user_conversation` (`user_id`,	`conversation_id`) VALUES
(1,1),
(2,1),
(1,2),
(3,2);




INSERT INTO `message` (`message_id`, `content`, `cr_date`,	`status`, `conversation_id`, `user_id`) VALUES
(1, 'Słownik podpowiedział nie to słowo co powinien. Moja wina że nie sprawdziłem.Ale nie zmienia to faktu że komentarze w tym stylu co napisałeś są bez sensu.', '2020-06-16 08:52:39', 'odczytane', 1, 1),
(2, 'hehe xD', '2020-06-17 08:52:39', 'nieodczytane', 1, 2),
(3, 'tak tak byczq', '2020-06-18 08:52:39', 'nieodczytane', 1, 2),
(4, 'jak ja umiem c++', '2020-06-19 08:52:39', 'odczytane', 2, 3),
(5, 'tylko ja chcę się rozwijać robiąc bardziej ambitne projekty', '2020-06-20 08:52:39', 'odczytane', 2, 3),
(6, 'więc nie mam czasu na jakieś "projekty"', '2020-06-21 08:52:39', 'odczytane', 2, 3),
(7, 'to se w assemblerze apke rob"', '2020-06-22 08:52:39', 'nieodczytane', 2, 1),
(8, '!', '2020-06-23 08:52:39', 'nieodczytane', 2, 1);






INSERT INTO `rating` (`rating_id`, `comment_id`, `discount_id`, `user_id`) VALUES
(1, NULL, 3, 1),
(2, NULL, 3, 1),
(3, 1, NULL, 1);












