## Команды управления принтером ##

| FPCommand          | ShtM | Atol | Datecs | en | ru |
|:-------------------|:-----|:-----|:-------|:---|:---|
| Beep               | 0x13 | 0x47 |        | Beep | Гудок|
| BeginReceipt       |      |      | 0x26   | Opening a non-fiscal receipt | Открыть не фискальный чек |
| CutPaper           | 0x25 | 0x75 |        | Cut receipt  | Отрезать чек |
| PrintString        | 0x17 | 0x4C | 0x2A   | Print string | Печать строки |
| EndReceipt         |      |      | 0x27   | Closing a non-fiscal receipt | Закрыть не фискальный чек |

---


## Команды чтения информации состояния ##
| FPCommand          | ShtM | Atol | Datecs | en | ru |
|:-------------------|:-----|:-----|:-------|:---|:---|
| ReadCashRegister   | 0x1A | 0x91 |        | Get cash totalizer value | Считать регистр |
| ReadFiscalizationParams | 0x69 |      | 0x63   | Get fiscalization parameters | Получить параметры фискализации |
| ReadFullStatus     | 0x11 | 0x3F |        | Get status | Запрос состояния |
| ReadShortStatus    |      |      | 0x4A   | Read status bytes | Получить байты статуса |

---


## Команды регистрации ##
| FPCommand          | ShtM | Atol | Datecs | en | ru |
|:-------------------|:-----|:-----|:-------|:---|:---|
| BeginFiscalReceipt | 0x8D | 0x92 |        | Open fiscal receipt | Открыть фискальный чек |
| PrintCashIn        | 0x50 | 0x49 |        | Cash-in | Внесение денег |
| PrintCashOut       | 0x51 | 0x4F |        | Cash-out | Выплата денег |
| PrintSaleRefund    | 0x82 | 0x57 |        | Sale refund | Возврат |
| EndFiscalReceipt   | 0x85 | 0x4A |        | Close fiscal receipt | Закрыть фискальный чек(со сдачей) |

---


## Команды управления ##
| FPCommand          | ShtM | Atol | Datecs | en | ru |
|:-------------------|:-----|:-----|:-------|:---|:---|
| CancelCurrentMode  |      | 0x48 |        | Cancel current FP mode | Выход из текущего режима |

---


## Команды отчётов ##
| FPCommand          | ShtM | Atol | Datecs | en | ru |
|:-------------------|:-----|:-----|:-------|:---|:---|
| BeginFiscalDay     | 0xE0 | 0x9A |        | Open fiscal day | Открыть смену |
| PrintXReport       | 0x40 | 0x67 |        | Print X report | Отчёт без гашения |
| PrintZReport       | 0x41 | 0x5A |        | Print Z report | Отчёт с гашением |
|                    |      |      |        |    |    |

---
