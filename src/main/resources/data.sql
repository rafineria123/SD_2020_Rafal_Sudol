CREATE TABLE IF NOT EXISTS  SPRING_SESSION (
	PRIMARY_ID CHAR(36) NOT NULL,
	SESSION_ID CHAR(36) NOT NULL,
	CREATION_TIME BIGINT NOT NULL,
	LAST_ACCESS_TIME BIGINT NOT NULL,
	MAX_INACTIVE_INTERVAL INT NOT NULL,
	EXPIRY_TIME BIGINT NOT NULL,
	PRINCIPAL_NAME VARCHAR(100),
	CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

CREATE UNIQUE INDEX IF NOT EXISTS SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);
CREATE INDEX IF NOT EXISTS SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
CREATE INDEX IF NOT EXISTS SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);

CREATE TABLE IF NOT EXISTS SPRING_SESSION_ATTRIBUTES (
	SESSION_PRIMARY_ID CHAR(36) NOT NULL,
	ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
	ATTRIBUTE_BYTES BLOB NOT NULL,
	CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
	CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION(PRIMARY_ID) ON DELETE CASCADE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;



INSERT INTO `shop` (`shop_id`, `image_url`, `name`) VALUES
(1, 'images/shop_1.png', 'Biedronka'),
(2, 'images/shop_2.png', 'Amazon'),
(3, 'images/shop_3.png', 'X-kom'),
(4, 'images/shop_4.png', 'Tesco'),
(5, 'images/shop_6.png', 'Media Markt'),
(6, 'images/shop_5.png', 'Pozostałe');

INSERT INTO `tag` (`tag_id`, `name`) VALUES
(1, 'Elektronika'),
(2, 'Gaming'),
(3, 'Moda'),
(4, 'Artykuły Spożywcze'),
(5, 'Sport i turystyka'),
(6, 'Pozostałe');


INSERT INTO `user` (`user_id`, `role`, `cr_date`, `email`, `login`, `password`, `status`, `information_id`, `rank_id`, `enabled`) VALUES
(1, 'USER', '2020-06-16 08:52:39', 'rafineria@gmail.com', 'rafineria', '$2a$10$09pC1fgfYgZPJBFhCRikOenf5uYnbP4E2kmsGUMofE9SzoAVz6EZy', 'Zatwierdzone', NULL, NULL, true ),
(2, 'USER', '2020-06-16 08:52:39', 'rafineria123@gmail.com', 'rafineria123', '$2a$10$rehd0BIqzYtkm9KNGmZ8PuPgBqIIIfzGT/a29FCyHuNrtIZdIu1XS', 'Zatwierdzone', NULL, NULL, true),
(3, 'ADMIN', '2020-06-16 08:52:39', 'artyleria@gmail.com', 'artyleria', '$2a$10$09pC1fgfYgZPJBFhCRikOenf5uYnbP4E2kmsGUMofE9SzoAVz6EZy', 'Zatwierdzone', NULL, NULL, true),
(4, 'USER', '2020-06-16 08:52:39', 'xkom@gma', 'xkom', '$2a$10$09pC1fgfYgZPJBFhCRikOenf5uYnbP4E2kmsGUMofE9SzoAVz6EZy', 'Zatwierdzone', NULL, NULL, true),
(5, 'USER', '2020-06-16 08:52:39', 'amazon@gma', 'amazon', '$2a$10$09pC1fgfYgZPJBFhCRikOenf5uYnbP4E2kmsGUMofE9SzoAVz6EZy', 'Zatwierdzone', NULL, NULL, true);

INSERT INTO `discount` (`discount_id`, `content`, `creationdate`, `current_price`, `discount_link`, `expire_date`, `image_url`, `old_price`, `shipment_price`, `status`, `title`, `shop_id`, `tag_id`, `user_id`, `type`) VALUES
(1, 'W Kaufland dostępne w dobrej cenie baterie alkaliczne AA oraz AAA 10szt. (wychodzi 1zł za sztukę). Dostępna duża ilość. Znalezione w Pszczynie, ale z informacji innych użytkowników okazja ogólnopolska.', '2020-06-16 08:52:39', 220, 'https://www.biedronka.pl/pl/dom-nf-10-06', '2020-06-16 08:52:39', 'images/discount_1.jpg', 255, 15, 'ZATWIERDZONE', 'Baterie alkaliczne AA', 6, 1, 1, 'OBNIZKA'),
(2, 'Oferta obowiązuje od 12.06 do 13.06.2020 r.', '2020-06-16 08:52:39', 5, 'https://www2.pl.avon.com/pl-home', '2020-06-16 08:52:39', 'images/discount_2.jpg', 10, 0, 'ZATWIERDZONE', 'Brzoskwinia @ Biedronka', 1, 4, 1, 'OBNIZKA'),
(3, 'W ramach usługi Xbox Game Pass w czerwcu zagramy w : Xbox Game Pass - nowe gry Battlefleet Gothic Armada 2 (PC) – 11 czerwca Battletech (PC) – 11 czerwca Dungeon of the End.', '2020-06-16 08:52:39', 1, 'https://www.x-kom.pl/p/568960-smartfon-telefon-realme-x3-superzoom-12256gb-glacier-blue-120hz.html', '2020-06-16 08:52:39', 'images/discount_3.jpg', 100, 0, 'ZATWIERDZONE', 'Xbox Game Pass - Czerwiec', 5, 2, 1, 'OBNIZKA'),
(4, 'W Biedronce od 12.06 kawa mielona Dallmayr Classic Krafti.', '2020-06-16 08:52:39', 15, 'https://www2.pl.avon.com/pl-home', '2020-06-16 08:52:39', 'images/discount_4.jpg', 20, 0, 'ZATWIERDZONE', 'Kawa Dallmayr Classic Krafti.', 1, 4, 2, 'OBNIZKA'),
(5, 'Dostawa gratis Przecena z 129.99$ na 84.99$. Wersja S kosztuje po przecenie 55.24$.', '2020-06-16 08:52:39', 350, 'https://www2.pl.avon.com/pl-home', '2020-06-16 08:52:39', 'images/discount_5.jpg', 500, 10, 'ZATWIERDZONE', 'IPRee namiot 3 w 1 dla 5-8 osób', 4, 5, 2, 'OBNIZKA'),
(6, 'W Tesco w Jarosławiu przecena płynu zimowego Borygo. Zostało jeszcze minimum 10 sztuk. Oferta lokalna. Małe miasto, ale może komuś się przyda ta informacja.', '2020-06-16 08:52:39', 17, 'https://www2.pl.avon.com/pl-home', '2020-06-16 08:52:39', 'images/discount_6.jpg', 35, 0, 'ZATWIERDZONE', 'Płyn do chłodnic samochodowych', 4, 6, 2, 'OBNIZKA'),
(7, 'Cześć,

W Media Expert jest dostępna gra Forza Horizon 3 na Xbox One za 39,99. Wg mnie cena przyzwoita, przecena ze 109. Zakupiona lokalnie w Millenium Hall w Rzeszowie, ale można zamówić online z darmową dostawą do sklepu.

Pozdrawiam', '2021-01-16 08:51:39', 39.99, 'https://www.mediaexpert.pl/gaming/gry/gry/gra-xbox-one-forza-horizon-3', '2021-02-25 08:52:39', 'images/discount_8.jpg', 109, 0, 'ZATWIERDZONE', 'Forza Horizon 3 Xbox One', 5, 2, 2, 'OBNIZKA'),
(8, 'Powstał portal z lekturami szkolnymi, do których dostęp jest całkowicie darmowy. Dostępne na komputerze, telefonie i tablecie. Miłej lektury!', '2021-01-16 08:52:39', 100, 'https://lektury.gov.pl/', '2021-02-20 22:52:39', 'images/discount_7.jpg', 100, 0, 'ZATWIERDZONE', 'Lektury za darmo. Darmowy portal z lekturami.', 6, 6, 2, 'KODPROCENT'),
(9, 'Dziś będąc w MM Rybnik po odbiór zamówienia internetowego wpadłem na niezłe ceny gier z Xbox One i PS4.

Te na focie kupiłem:
Red Dead Redemption 2 XOne za 69.97 zł
Dreams PS4 34,97 zł
The Last Guardian PS4 39,97 zł
Concrete Genie 39,00 zł

Było jeszcze kilka tytułów
Quantum Break XOne 13,97 zł
Nioh 2 59,97 zł
Detroit Become Human 39.99 zł
Death Stranding PS4 za 39,00 zł

Niestety nie spojrzałem czy RDR2 na PS4 też był w tej cenie.

Oznaczam jako lokalnie ale moim zdaniem warto zobaczyć też na miejscu u siebie w MM.', '2020-06-16 08:53:39', 69.97, 'https://mediamarkt.pl/agd-male/generator-pary-philips-perfectcare-elite-plus-gc9685-80', '2021-02-23 22:52:39', 'images/discount_9.jpg', 129, 15, 'ZATWIERDZONE', 'Gry Xbox PlayStation MediaMarkt Red Dead Redemption 2, Dreams, Death Stranding.', 5, 2, 2, 'OBNIZKA'),
(10, 'Od jutra powraca do sklepów Biedronka czekolada Milka darkmilk w niskiej cenie 1.99zł gdzie standardowa cena to przeważnie 3.99. Nie trzeba kupować dwóch lub większej ilości sztuk.', '2021-01-16 08:58:39', 1.99, 'https://www.biedronka.pl/pl/product,id,206465,name,czekolada-milka-250-300-g', '2021-02-28 22:52:39', 'images/discount_10.jpg', 3.99, 0, 'ZATWIERDZONE', 'Czekolada Milka Darkmilk za 1.99zł! - Biedronka', 1, 4, 2, 'OBNIZKA'),
(11, 'Kalendarze Adwentowe w Tesco - różne rodzaje', '2021-01-16 08:59:39', 1.99, 'https://tesco.pl/promocje/oferta-tygodnia/produkt/artykuly-spozywcze/czekolada/kalendarz-adwentowy-kinder/', '2021-02-28 22:52:39', 'images/discount_12.jpg', 3.99, 0, 'ZATWIERDZONE', 'Kalendarz Adwentowy 50g / Tesco (ogólnopolska)', 4, 4, 3, 'OBNIZKA'),
(12, 'Przecena butelek, bidonów dziecięcych ale tylko te dwa rodzaje są po 0,99 z 9,99. Sporo ich jest w Tesco Szczawno Zdrój .', '2021-01-17 08:59:39', 0.99, 'https://tesco.pl/promocje/oferta-tygodnia/produkt/samochod-dom-hobby/sprzet-agd/bidon/', '2021-03-01 22:52:39', 'images/discount_13.jpg', 9.99, 0, 'ZATWIERDZONE', 'Tesco butelka, bidon dla dzieci', 4, 5, 1, 'OBNIZKA'),
(13, 'Cena EUR: 84,50€
Dostawa: 15€

Niestety nie wysyła Amazon ale sprzedawca z dobrymi opiniami (100%), szybka wysyłka GLS.
Ochrona Amazon


Informacje ogólne
Wymiary 46 mm
Typ mechanizmu Quartz (baterie)
Wodoszczelność 50m (kąpiel w wannie/prysznic)
Przeznaczenie dla mężczyzn
Gwarancja 24 miesiące
Koperta i tarcza
Koperta stal z powłoką PVD
Kolor koperty metaliczna czerń
Tarcza analogowa
Kolor tarczy czarny
Kształt zegarka okrągły
Szkiełko mineralne
Pasek
Pasek skóra
Kolor paska brązowy
Dodatkowe informacje
Datownik tak
Dodatkowe funkcje 24-godzinowa sub-tarcza, dzień tygodnia', '2021-01-10 08:59:39', 383.58, 'https://www.amazon.it/dp/B01GFL3Y1E?smid=A2HVSO80A2I45', '2021-03-03 22:52:39', 'images/discount_11.jpg', 692, 68.09, 'ZATWIERDZONE', 'Zegarek TImberland TBL14816JLB.02 - Amazon.it - 99,50€', 2, 3, 1, 'OBNIZKA'),
(14, 'Działa tylko na wybranych kontach. Pewnie inni mają inne kody i zniżki. Będę aktualizował opis, jeśli pojawią się inne kody.', '2021-01-18 08:59:39', 0.99, 'https://www.uber.com/pl/pl/business/solutions/eats/overview/', '2021-03-01 22:52:39', 'images/discount_14.jpg', 20, 0, 'ZATWIERDZONE', 'Kupon uber eats 20/40', 6, 4, 1, 'KODNORMALNY'),
(15, 'Kup praliny Merci w Carrefour i odbierz 14% zniżki na zakupy w YES.
Podobna promocja jest też w Intermarche.

Aby wziąć udział w Promocji należy w Okresie Trwania Promocji:
a. dokonać aktywacji e-kuponu w aplikacji Mój Carrefour;
b. dokonać zakupu Produktów Promocyjnych (w tym płatności za Produkty Promocyjne) w Sklepie;
c. przed dokonaniem płatności za zakup Produktów Promocyjnych okazać w kasie do zeskanowania aktywną Kartę Mobilną lub uprzednio zdigitalizowaną Kartę lub Kartę Seniora nie później niżw momencie zakończenia przez kasjera skanowania kodów kreskowych produktów nabywanych przez Uczestnika. Jeżeli Karta Mobilna lub zdigitalizowana Karta lub Karta Seniora nie zostanie okazana kasjerowi przed zakończeniem transakcji (wciśnięcie klawisza "podsuma") Uczestnik nie będzie mógł wymienić e-kuponu w Aplikacji Mobilnej „Mój Carrefour” na drukowany kupon rabatowy. Późniejsza wymiana na podstawie paragonów fiskalnych lub innych dowodów dokonania transakcji nie będzie możliwa.', '2021-01-17 08:59:39', 0.99, 'https://cdn.yes.pl/media/wysiwyg/static/pdf/CARREFOUR_Merci_regulamin_promocji_walentynkowej_2021.pdf', '2021-03-04 22:52:39', 'images/discount_15.jpg', 14, 0, 'ZATWIERDZONE', '14% zniżki do YES przy zakupie pralin Merci', 6, 4, 1, 'KUPONPROCENT');

INSERT INTO `post` (`post_id`, `content`, `creationdate`, `status`, `title`, `user_id`, `shop_id`, `tag_id`) VALUES
(1, 'Czy Waszym zdaniem 68,35 zł za ładowarkę Liitokala Lii-500 razem z zasilaczem to dobra cena? Czym różni się sklep liitokala Official Store od LiitoKala Official Flagship Store?','2020-06-16 08:52:39','ZATWIERDZONE', 'Ładowarka Liitokala Lii-500 za 68 zł, czy warto w tej cenie?', 1, 6, 6),
(2, 'Szukam laptopa dla nie wymagającej, dlatego MacBook air, dysk najlepiej 256gb, Intel i5. Nie potrzebna mi maszyna z procesorem M1.

Głównie do przeglądania neta i simsów! Praca licencjacka itd.

Interesuje mnie tylko apple, rocznik 2020, 2019, 2018(ewentualnie). Specyfikacje może i znam, ale nie wiem w co jakiej cenie polować. WIdziałem w empiku za 4600 nie wiem czy to super oferta, a szukam nie dla siebie, więc chce znaleźć okazję super','2020-06-18 08:52:39','ZATWIERDZONE', 'Szukam Macbook Air, ale nie z m1', 1, 6, 6),
(3, 'Będę bardzo wdzięczny za odstąpienie kodu rabatowego do Media Expert (50/1000)
Z góry dziękuję za pomoc','2020-06-16 08:52:39','ZATWIERDZONE', 'Kod rabatowy ING do Media Expert', 2, 5, 1),
(4, 'Czy ktoś może mi wyłożyć dlaczego warto założyć VPN? I czy NordVPN to jest dobra opcja?

Rozumiem zasadę działania VPN
Wiem co daje VPN ogólnie
Pytam bardziej czy NordVPN to godny zaufania provider i
Czy jakikolwiek provider jest godny zaufania, czy to nie jest po prostu rerouting całej wiedzy o aktywności w internecie z mojego ISP do NordVPN lub innego
Jakie są gwarancje prywatności itd?

Nie interesuje mnie aspekt zmiany lokalizacji (hulu itd), raczej prywatnościowy.

Z góry dzięki','2020-06-17 08:52:39','ZATWIERDZONE', 'Dlaczego warto VPN', 2, 6, 6),
(5, 'Ktoś może używał tego sprzętu Sharp UA-HG50E-L

Prosze o konstruktywne opinie ','2020-06-19 08:52:39','ZATWIERDZONE', 'OCZYSZCZACZ-NAWILŻACZ SHARP', 3, 6, 6);
INSERT INTO `comment` (`comment_id`, `content`, `cr_date`, `discount_id`, `post_id`, `user_id`) VALUES
(1, 'Słownik podpowiedział nie to słowo co powinien. Moja wina że nie sprawdziłem. Ale nie zmienia to faktu że komentarze w tym stylu co napisałeś są bez sensu. Co to ma wnosić do obecnej sytuacji. Kiedyś może mogło być drożej, mogło być taniej. Dzisiaj taka cena jest najtańsza i na tym polega ten serwis.', '2020-06-16 08:52:39', 3, NULL, 1),
(2, 'Słownik podpowiedział nie to słowo co powinien. Moja wina że nie sprawdziłem. Ale nie zmienia to faktu że komentarze w tym stylu co napisałeś są bez sensu. Co to ma wnosić do obecnej sytuacji. Kiedyś może mogło być drożej, mogło być taniej. Dzisiaj taka cena jest najtańsza i na tym polega ten serwis.', '2020-06-16 08:52:39', NULL, 1, 1);


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












