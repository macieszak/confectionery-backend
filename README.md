# Sklep internetowy - cukiernia.

## Główne założenia
Główne założenia aplikacji obejmują intuicyjny i łatwy w obsłudze interfejs, który umożliwi przeglądanie oferty i dokonywanie zakupów. Aplikacja ma zapewniać bezpieczne i szybkie doładowywanie konta użytkownika oraz możliwość śledzenia statusu zamówień. Ważnym aspektem jest także panel administracyjny, który umożliwi właścicielowi cukierni zarządzanie zamówieniami, użytkownikami oraz monitorowanie wszystkich operacji w systemie.

## Architektura systemu
Poniższy diagram przedstawia globalną architekturę aplikacji sklepu internetowego dla cukierni, zbudowanej na podstawie wzorca MVC (Model-View-Controller). Warstwa prezentacji (View) jest zaimplementowana po stronie klienta przy użyciu React, HTML i CSS, i komunikuje się z backendem za pomocą biblioteki Axios do wysyłania zapytań HTTP (REST API). Warstwa logiki biznesowej (Controller) i warstwa danych (Model) są zaimplementowane po stronie serwera z użyciem Spring Boot. Kontrolery REST w Spring Boot obsługują żądania przychodzące od klienta, przetwarzają logikę biznesową w serwisach i komunikują się z warstwą danych za pośrednictwem Hibernate. Warstwa danych zarządza przechowywaniem i operacjami na danych w bazie MySQL, korzystając z repozytoriów i modeli JPA. Diagram wyraźnie pokazuje przepływ danych między użytkownikiem, frontendem i backendem, mając na celu ułatwienie zrozumienia struktury i działania systemu.

![image](https://github.com/user-attachments/assets/67bc53d2-f423-4e17-b286-a954d30d34dd)

## Najważniejsze funkcjonalności aplikacji
- Możliwość przeglądania i filtrowania produktów według różnych kryteriów.
- Funkcjonalność logowania i rejestracji użytkowników z autoryzacją przy użyciu tokenów JWT.
- Dodawanie produktów do koszyka i listy ulubionych, z odpowiednim zarządzaniem sesją użytkownika.
- Dynamiczne zarządzanie stanem aplikacji oraz intuicyjna nawigacja, zapewniające płynne doświadczenie użytkownika.
- Profil użytkownika, który umożliwia zarządzanie danymi osobowymi, adresami oraz zamówieniami. Użytkownik może przeglądać historię swoich zamówień, edytować dane kontaktowe i dodawać nowe adresy dostawy.
- Panel administratora, który umożliwia dodawanie nowych produktów, edycję oraz usuwanie istniejących produktów. Administrator ma również możliwość zarządzania użytkownikami poprzez zmianę statusów ich kont oraz zarządzanie zamówieniami.

## Interfejsy aplikacji
### Zakładka 'Home'
![image](https://github.com/user-attachments/assets/647d126f-ce6c-421f-b395-c99e437783e4)
![image](https://github.com/user-attachments/assets/97e69fb5-7fc6-47dc-941c-b826a6aa10d7)

### Zakładka 'Products'
![image](https://github.com/user-attachments/assets/79d47530-1e3f-429b-a00a-3826180e0ef6)

### Okno logowania/rejestracji
![image](https://github.com/user-attachments/assets/3a61e0b8-6a4f-49d2-930b-9e8b15d70520)
![image](https://github.com/user-attachments/assets/db499769-1ead-44a9-994a-8e2243a4d062)

### Zakładka informacji o profilu użytkownika
![image](https://github.com/user-attachments/assets/4d4332d3-777b-4e23-938b-bc0b21c7eb4b)

### Zakładka 'Wallet'
![image](https://github.com/user-attachments/assets/23e5d5ac-68bf-4f83-aa98-daf577a13191)

### Zakładka 'Addresses'
![image](https://github.com/user-attachments/assets/0693c5e1-e5ca-4435-9dff-9efae2ec5354)

### Zakładka 'Order history'
![image](https://github.com/user-attachments/assets/47a78ed3-4cfa-4cea-9356-c9f7d495abb0)

### Zakładka 'Favorite Products'
![image](https://github.com/user-attachments/assets/8c364dec-45bc-463b-852f-7c6ea0841be3)

### Proces zakupowy
![image](https://github.com/user-attachments/assets/f83ba519-54eb-4ad6-9989-38e296dfc1b7)
![image](https://github.com/user-attachments/assets/7afbbbd1-2cb0-4d25-a8a3-059072845c50)
![image](https://github.com/user-attachments/assets/c5ff84d4-0061-4bce-9026-656840f796bd)

### Zakładka 'Products' administratora
![image](https://github.com/user-attachments/assets/624c82a4-50c4-4a5a-ab7b-66fb3e9aa1ed)

### Okno dodawania nowego produktu przez administatora
![image](https://github.com/user-attachments/assets/1c6cd4a3-1e1c-42ed-bacc-49ba7b8f01eb)

### Okno zarządzania produktem przez administatora
![image](https://github.com/user-attachments/assets/a4f45c9d-c4d2-44f5-97c4-24baa21ecf55)

### Zakładka 'Users' administratora
![image](https://github.com/user-attachments/assets/b159d1b9-e8fb-4d7f-993e-5ab3e5f2827e)

### Zakładka 'Orders' administratora
![image](https://github.com/user-attachments/assets/913da5d2-2ab8-487e-ae02-c04cbd5549c5)
