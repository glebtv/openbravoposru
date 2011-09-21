//    Исходный код для Openbravo POS, автоматизированной системы продаж для работы
//    с сенсорным экраном, предоставлен ТОО "Норд-Трейдинг ЛТД", Республика Казахстан,
//    в период 2008-2010 годов на условиях лицензионного соглашения GNU LGPL.
//
//    Исходный код данного файл разработан в рамках проекта Openbravo POS ru
//    для использования системы Openbravo POS на территории бывшего СССР
//    <http://code.google.com/p/openbravoposru/>.
//
//    Openbravo POS является свободным программным обеспечением. Вы имеете право
//    любым доступным образом его распространять и/или модифицировать соблюдая
//    условия изложенные в GNU Lesser General Public License версии 3 и выше.
//
//    Данный исходный распространяется как есть, без каких либо гарантий на его
//    использование в каких либо целях, включая коммерческое применение. Данный
//    исход код может быть использован для связи с сторонними библиотеками
//    распространяемыми под другими лицензионными соглашениями. Подробности
//    смотрите в описании лицензионного соглашение GNU Lesser General Public License.
//
//    Ознакомится с условиями изложенными в GNU Lesser General Public License
//    вы можете в файле lgpl-3.0.txt каталога licensing проекта Openbravo POS ru.
//    А также на сайте <http://www.gnu.org/licenses/>.

package com.openbravo.pos.printer.aurafr;

import com.openbravo.pos.printer.*;

public interface AuraFRReaderWritter {
    public void connectDevice() throws TicketPrinterException;
     public void sendInitMessage() throws TicketPrinterException;
     public void sendTextMessage(String sText) throws TicketPrinterException;
     public void sendBeepMessage() throws TicketPrinterException;
     public void sendCutTicketMessage(int iFlag) throws TicketPrinterException;
     public void sendStampTitleReportMessage() throws TicketPrinterException;
     public void sendOpenDrawerMessage() throws TicketPrinterException;
     public void sendOpenTicket(int iFlag, String sTypeTicket) throws TicketPrinterException;
     public void sendRegistrationLine(int iFlag, double dProductPrice, double dSaleUnits, int iProductTax) throws TicketPrinterException;
     public void sendRefundLine(int iFlag, double dProductPrice, double dSaleUnits) throws TicketPrinterException;
     public void sendSelectModeMessage(int iMode) throws TicketPrinterException;
     public void sendCancelModeMessage() throws TicketPrinterException;
     public void sendCloseTicketMessage(int iFlag, int iType, double dPaid) throws TicketPrinterException;
     public void printXReport(int iType) throws TicketPrinterException;
     public void printZReport() throws TicketPrinterException;
     public void disconnectDevice();
}
