Концепция:
У нас есть Люди(класс такой), у каждого по аккаунту. Каждый получает задачу перевести деньги на другой акк базовую сумму. Босс получает задачу начислить всем аккаунтам по 1000. Начисляет через банк, где привязанны все аккаунты.

Перевод реализован тем, что вначале делается списание с одного аккаунта, потом перевод на другой (эта механика в классе TransferTest).
Причем вывод один из двух: обычный и предпочтительный через рандом.


Я сделал lock на предпочтительные отдельным, чтобы новые предпочтительные выводы не пытались захватить лок на баланс, а вначале увеличивали желание на вывод, даже если какой-то предпочтительный сейчас выводит.
Тогда не произойдет такого,что кто-то хотел вывести предпочтительным, но не успел захватить лок, поэтому вывел обычным.

Деадлока не должно произойти потому, что захват preferred лока с двумя локами происходит в строгой последовательности. Вначале захватывается lock на изменение баланса, потом на preferred для чтения или изменения.
Вывод одновременно с депозитом не произойдет, так как у нас на вывод средств или пополнение нужно захватить lock.

Пример отработки программы:
Account with id 0 has balance: 57
Account with id 1 has balance: 279
Account with id 2 has balance: 294
Account with id 3 has balance: 195
Account with id 4 has balance: 180
Account with id 5 has balance: 267
Account with id 6 has balance: 117
Account with id 7 has balance: 78
Account with id 8 has balance: 162
Account with id 9 has balance: 198
Thread id: 10: start transfer from 6 to 0
Thread id: 13: start transfer from 8 to 3
Thread id: 12: start transfer from 6 to 2
Thread id: 11: start transfer from 0 to 1
Thread id: 10: end transfer from 6 to 0
Thread id: 13: end transfer from 8 to 3
Thread id: 14: start transfer from 8 to 4
Thread id: 16: start transfer from 7 to 6
Thread id: 11: end transfer from 0 to 1
Thread id: 15: start transfer from 4 to 5
Thread id: 17: start transfer from 0 to 7
Thread id: 15: end transfer from 4 to 5
Thread id: 18: start transfer from 2 to 8
Thread id: 19: start transfer from 6 to 9
Thread id: 18: end transfer from 2 to 8
Thread id: 14: end transfer from 8 to 4
Boss start given prize to all
Thread id: 17: end transfer from 0 to 7
Thread id: 12: end transfer from 6 to 2
Thread id: 19: end transfer from 6 to 9
Thread id: 16: end transfer from 7 to 6