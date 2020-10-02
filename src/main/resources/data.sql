

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
(1, 'USER', '2020-06-16 08:52:39', 'rafineria@gmail.com', 'rafineria', '$2a$10$09pC1fgfYgZPJBFhCRikOenf5uYnbP4E2kmsGUMofE9SzoAVz6EZy', 'active', NULL, NULL),
(2, 'USER', '2020-06-16 08:52:39', 'rafineria123@gmail.com', 'rafineria123', '$2a$10$rehd0BIqzYtkm9KNGmZ8PuPgBqIIIfzGT/a29FCyHuNrtIZdIu1XS', 'active', NULL, NULL);

INSERT INTO `discount` (`discount_id`, `content`, `cr_date`, `current_price`, `discount_link`, `expire_date`, `image_url`, `old_price`, `shipment_price`, `status`, `title`, `shop_id`, `tag_id`, `user_id`) VALUES
(1, 'W Kaufland dostępne w dobrej cenie baterie alkaliczne AA oraz AAA 10szt. (wychodzi 1zł za sztukę). Dostępna duża ilość. Znalezione w Pszczynie, ale z informacji innych użytkowników okazja ogólnopolska.', '2020-06-16 08:52:39', 220, 'https://www.biedronka.pl/pl/dom-nf-10-06', '2020-06-16 08:52:39', 'images/discount_1.jpg', 255, 15, 'active', 'Baterie alkaliczne AA', 6, 1, 1),
(2, 'Oferta obowiązuje od 12.06 do 13.06.2020 r.', '2020-06-16 08:52:39', 5, 'https://www2.pl.avon.com/pl-home', '2020-06-16 08:52:39', 'images/discount_2.jpg', 10, 0, 'active', 'Brzoskwinia @ Biedronka', 1, 5, 1),
(3, 'W ramach usługi Xbox Game Pass w czerwcu zagramy w : Xbox Game Pass - nowe gry Battlefleet Gothic Armada 2 (PC) – 11 czerwca Battletech (PC) – 11 czerwca Dungeon of the End.', '2020-06-16 08:52:39', 1, 'https://www.x-kom.pl/p/568960-smartfon-telefon-realme-x3-superzoom-12256gb-glacier-blue-120hz.html', '2020-06-16 08:52:39', 'images/discount_3.jpg', 100, 0, 'active', 'Xbox Game Pass - Czerwiec', 5, 3, 1),
(4, 'W Biedronce od 12.06 kawa mielona Dallmayr Classic Krafti.', '2020-06-16 08:52:39', 15, 'https://www2.pl.avon.com/pl-home', '2020-06-16 08:52:39', 'images/discount_4.jpg', 20, 0, 'active', 'Kawa Dallmayr Classic Krafti.', 1, 3, 2),
(5, 'Dostawa gratis Przecena z 129.99$ na 84.99$. Wersja S kosztuje po przecenie 55.24$.', '2020-06-16 08:52:39', 350, 'https://www2.pl.avon.com/pl-home', '2020-06-16 08:52:39', 'images/discount_5.jpg', 500, 10, 'active', 'IPRee namiot 3 w 1 dla 5-8 osób', 4, 6, 2),
(6, 'W Tesco w Jarosławiu przecena płynu zimowego Borygo. Zostało jeszcze minimum 10 sztuk. Oferta lokalna. Małe miasto, ale może komuś się przyda ta informacja.', '2020-06-16 08:52:39', 17, 'https://www2.pl.avon.com/pl-home', '2020-06-16 08:52:39', 'images/discount_6.jpg', 35, 0, 'active', 'Płyn do chłodnic samochodowych', 2, 2, 2);

INSERT INTO `comment` (`comment_id`, `content`, `cr_date`, `upper_comment_id`, `discount_id`, `post_id`, `user_id`) VALUES
(1, 'Słownik podpowiedział nie to słowo co powinien. Moja wina że nie sprawdziłem. Ale nie zmienia to faktu że komentarze w tym stylu co napisałeś są bez sensu. Co to ma wnosić do obecnej sytuacji. Kiedyś może mogło być drożej, mogło być taniej. Dzisiaj taka cena jest najtańsza i na tym polega ten serwis.', '2020-06-16 08:52:39', NULL, 3, NULL, 1);






INSERT INTO `rating` (`rating_id`, `comment_id`, `discount_id`, `user_id`) VALUES
(1, NULL, 3, 1),
(2, NULL, 3, 1),
(3, 1, NULL, 1);












