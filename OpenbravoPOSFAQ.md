## General Questions ##
### Does Openbravo POS support a web interface? ###
Openbravo POS is a desktop application because:

  1. Openbravo POS has to deal with POS hardware: receipt printers, customer displays   and other devices. It is more easy to deal with them as a desktop application.
  1. The user interface is oriented to touch screens and Openbravo POS needs very sophisticated user controls that HTML cannot provide.

### What kind of peripherals does Openbravo POS support? ###
These are the peripherals that Openbravo POS supports:

  * Receipt printers and Customer displays: any that supports ESC/POS protocol or that has a JavaPOS driver available.
  * Cash drawer: any that connects to the receipt printers.
  * Barcode scanner: any that emulates the keyboard.
  * Touch screen any that has a driver for the operating system where Openbravo POS is going to run.
  * Scale: any that supports the Samsung protocol or the Dialog1 protocol.
  * Magnetic card swipe: any that emulates the keyboard.

### Can Openbravo POS run on Windows CE? ###
It is not possible to run Openbravo POS on Windows CE or to port Openbravo POS to Windows CE. However, the requirements of Openbravo POS are very low and you can cut costs using low processors like VIA processors and Linux operating system.

### Where can I get the source code of Openbravo POS? ###
Openbravo POS source code it is available from [Openbravo Mercurial server](https://code.openbravo.com/pos), [there are instructions](http://wiki.openbravo.com/wiki/Mercurial_Manual_for_Openbravo_Developers) on how to setup a Mercurial client to access to it. To get written access please contact [collaborate@openbravo.com](mailto:collaborate@openbravo.com).

### Can I run Openbravo POS using a resolution of 800x600? ###
The preferred resolution for Openbravo POS is 1024x768. But with some configuration changes you can make Openbravo POS work in 800x600.

In the resources panel edit the resource **Ticket.Buttons** and modify the value of the property **cat-height** to **150**.

And in the configuration panel modify the value of the property **Screen** to **fullscreen**.

Restart the application and you will see the sales panel and most functions to fit properly in your 800x600 monitor. Only few panels does not fit properly in 800x600.

### Any particular combination of peripherals that you would recommend? ###
  * Touch screen: [ELO 1515 LCD Desktop Touchmonitor](http://www.elotouch.com/Products/LCDs/1515L/default.asp).
  * Barcode scanner: [Metrologic Voyager 9520](http://www.metrologic.com/corporate/products/pos/ms9520/).
  * Ticket printers: [Epson TM T88IV with serial interface](http://www.pos.epson-europe.com/products/printers/tmt88iv/index.htm).
  * Customer display: [Cash Epson DM D110/D210 with serial interface](http://www.pos.epson-europe.com/products/customer-displays/dm-d110-d120/index.htm).

### Can Openbravo POS run with OpenJDK? ###
No, Openbravo POS does not run with OpenSDK that is actually the Java framework installed by default in some Linux distributions like Ubuntu. You have to use Sun's Java 2 Platform Standard Edition 6.0 or higher.

If OpenJDK is installed, Openbravo POS  throws the following exception:

```
 net.sf.jasperreports.engine.JRException: 
 javax.imageio.IIOException: Invalid argument to native writeImage
```

To fix this install Sun's Java 2 Platform Standard Edition 6.0 or higher. In Ubuntu this can be done executing.
```
 $ sudo apt-get install sun-java6-bin sun-java6-jre
 $ sudo update-java-alternatives -s java-6-sun
```

If you want to maintain OpenJDK installed in your machine it may be necessary to specify to Openbravo POS the path where the Sun's Java 2 Platform Standard Edition resides. For example if it resides in `/usr/lib/jvm/java-6-sun` you can include the following two lines in start.sh:
```
 JAVA_HOME=/usr/lib/jvm/java-6-sun
 PATH=/usr/lib/jvm/java-6-sun/bin:$PATH
```

### Dialogs sometimes does not show all images and the right window boundary is off the screen ###
Java does not work properly under Linux with Compiz activated. To fix this one solution is just to disable Compiz. For example in Unbuntu go to System / Preferences / Appearance, and in the tab Visual Effects select None. The other solution is to set the environment variable AWT\_TOOLKIT to MToolkit before starting Openbravo POS. You can do this adding to `/home/username/.bashrc` the line.
```
 export AWT_TOOLKIT=MToolkit
```

### Sometimes appear strange images in text boxes ###
Java does not work properly under Windows Vista with Aero activated. To fix this just turn off Aero effects.

## Software configuration ##
### How Openbravo POS decides which language to use for the user interface? ###
By default Openbravo POS takes the language to use from the operating system. But you can also change then language in the configuration panel of Openbravo POS. In this panel you can also change the properties related to the format of numeric values, currencies and dates.

### How can I change the receipts printed to include my logo and other details ###
Openbravo receipts are fully configurable and is based on templates, you can change these templates in the resources panel. Log in as administrator and go to **Administration / Maintenance / Resources**. Here edit the record **Printer.Ticket** that contains the template of the receipt that is printed after a sale operation is closed. There are other templates in this panel for other moments of the operation of Openbravo POS you are also free to modify.

### How can I modify the reports and create new ones ###
Openbravo POS 2.10 and later versions provide an structure for reports that makes it easier to modify and create new reports. There is a tutorial here [Openbravo POS Reports and Charts Tutorial](http://wiki.openbravo.com/wiki/Openbravo_POS_Reports_and_Charts_Tutorial)

### How can I manage the taxes depending on the customer ###
Taxes architecture in Openbravo POS is very similar to Openbravo ERP and you can set a different taxes schema depending on the customer. This is explained in detail here [Openbravo POS Taxes Management](http://wiki.openbravo.com/wiki/Openbravo_POS_Taxes_Management).

### How can I give a discount in Openbravo POS? ###
There are many ways to do it. For example:

  * On the sales panel, type in a number (into the field that takes item code) and hit the minus (-) key. Make sure that the dropdown just below the field where you keyed in the number is set to 0% tax. The number you keyed in would be the discount amount. Similarly, if you hit the + key, that number would be added to the total amount.

  * Set up a new item called "DISCOUNT" in the products screen with itemcode, say 0. Set it to 0 price and 0 tax and a new category. Then on the sales panel, key in 0 for the item code and hit Enter. Click on the "pencil" icon and modify the price. So the receipt would read "DISCOUNT" and an appropriate price against it.

  * Create a new scripting button that adds a new line with the discount. An example is described in the [Openbravo POS Scripting Tutorial](http://wiki.openbravo.com/wiki/Openbravo_POS_Scripting_Tutorial).

In addition to this, you could as well provide discount on individual items directly by selecting the item on the sales panel and then clicking on the pencil icon, modifying the price and hitting Enter. But this would not show as "DISCOUNT" on the receipt and hence your customer would not even know that you have given him a discount.

### How can I work with the restaurant module ###
To work with the restaurant module to be able to show the tables layout and the reservations panel. Go to the configuration panel and in the option ''Tickets'', select ''restaurant''.

### Openbravo POS cannot print reports under Linux ###
Openbravo POS shows a message ''Error printing report, See the console for details'' when trying to print a report because of the java platform bug http://bugs.sun.com/view_bug.do?bug_id=6633656. You can have it work by specifying the Orientation in every printer in CUPS. Depending on your system go to ''System -> Printing'' and for each printer select  the Job Options tab and specify Orientation to whatever else instead of ''Automatic Rotation''.

### Openbravo POS can't find database driver in Linux ###
You might get this error when starting up Openbravo POS: "Danger: Cannot connect to database. Database driver not found."

This is because the path for the JDK is not specified. For example, if your JDK resides in /opt, you can include the following two lines (adjust as necessary) in start.sh:
```
 export JAVA_HOME=/opt/jdk1.6.0_07
 export PATH=/opt/jdk1.6.0_07/bin:$PATH
```

For Ubuntu 8.10 or 10.04:
  * Log in as -root- and edit /opt/<openbravopos-2.30 folder>/start.sh file.
  * Add the following lines somewhere in the file:
```
 JAVA_HOME=/usr/lib/jvm/java-6-sun
 PATH=/usr/lib/jvm/java-6-sun/bin:$PATH
```

### Openbravo POS does not start, it shows the message _could not find the main class_ ###

Openbravo POS 2.30 needs java 1.6 o greater version, if you have installed java 1.5 or a previous version this message appears when trying to execute Openbravo POS. To know what version is the default version of java in your system open a console and type `java -version`.

To fix this problem remove any previous version of java and install java 1.6 or a greater java version.

### How can I reset / remove data in Openbravo POS ###
#### Full Reset ####
If you have been testing and configuring Openbravo POS and you want to remove all the receipts you created but maintaining the master data: Products, categories, ... Execute the following SQL commands:
```
 DELETE FROM TICKETLINES;
 DELETE FROM TAXLINES;
 DELETE FROM TICKETS;
 DELETE FROM PAYMENTS;
 DELETE FROM RECEIPTS;
 DELETE FROM SHAREDTICKETS;
 DELETE FROM STOCKDIARY;
 DELETE FROM CLOSEDCASH;
 UPDATE STOCKCURRENT SET UNITS = 0;
 UPDATE CUSTOMERS SET CURDATE = NULL;
 UPDATE CUSTOMERS SET CURDEBT = 0;
```

If you want also reset the receipt number counters, execute for HSQLDB and PostgreSQL:
```
 ALTER SEQUENCE TICKETSNUM RESTART WITH 1;
 ALTER SEQUENCE TICKETSNUM_REFUND RESTART WITH 1;
 ALTER SEQUENCE TICKETSNUM_PAYMENT RESTART WITH 1;
```

for MySQL:
```
 UPDATE TICKETSNUM SET ID = 0;
 UPDATE TICKETSNUM_REFUND SET ID = 0;
 UPDATE TICKETSNUM_PAYMENT SET ID = 0;
```

for Oracle:
```
 DROP SEQUENCE TICKETSNUM;
 CREATE SEQUENCE TICKETSNUM START WITH 1;
 DROP SEQUENCE TICKETSNUM_REFUND;
 CREATE SEQUENCE TICKETSNUM_REFUND START WITH 1;
 DROP SEQUENCE TICKETSNUM_PAYMENT;
 CREATE SEQUENCE TICKETSNUM_PAYMENT START WITH 1;
```

for Derby:
```
 DELETE FROM TICKETSNUM;
 ALTER TABLE TICKETSNUM ALTER COLUMN ID RESTART WITH 0;
 INSERT INTO TICKETSNUM VALUES (DEFAULT);
 DELETE FROM TICKETSNUM_REFUND;
 ALTER TABLE TICKETSNUM_REFUND ALTER COLUMN ID RESTART WITH 0;
 INSERT INTO TICKETSNUM_REFUND VALUES (DEFAULT);
 DELETE FROM TICKETSNUM_PAYMENT;
 ALTER TABLE TICKETSNUM_PAYMENT ALTER COLUMN ID RESTART WITH 0;
 INSERT INTO TICKETSNUM_PAYMENT VALUES (DEFAULT);
```

If you want also delete products and categories, execute:

```
 DELETE FROM STOCKCURRENT;
 DELETE FROM PRODUCTS_COM;
 DELETE FROM PRODUCTS_CAT;
 DELETE FROM PRODUCTS;
 DELETE FROM CATEGORIES;
```

If you want also delete customers, execute:

```
 DELETE FROM CUSTOMERS;
```

If you forgot the administrator password and you cannot login as administrator, to reset the administrator password, execute the following SQL sentence:

```
 UPDATE PEOPLE SET APPPASSWORD = NULL WHERE NAME = 'Admin';
```

To execute the previous SQL commands use the SQL client tool you prefer for the database engine you use. One tool that fits for all the database engines supported by Openbravo POS is [Squirrel SQL](http://www.squirrelsql.org/).

#### Partial Reset ####
If you wish to delete / archive transactions, then you may find it more convenient to use cascade deletes.  In order to use cascade deletes, you need to change the default foreign keys.

Execute the following commands against your DB (Note: tested with PostgreSQL)
```
 alter table receipts drop constraint receipts_fk_money;
 alter table receipts add constraint receipts_fk_money foreign key (money) references closedcash(money) on delete cascade; 
 
 alter table ticketlines drop constraint ticketlines_fk_ticket;
 alter table ticketlines add constraint ticketlines_fk_ticket foreign key (ticket) references tickets(id) on delete cascade;
 
 alter table tickets drop constraint tickets_fk_id;
 alter table tickets add constraint tickets_fk_id foreign key (id) references receipts(id) on delete cascade;
 
 alter table taxlines drop constraint taxlines_receipt;
 alter table taxlines add constraint taxlines_receipt foreign key (receipt) references receipts(id) on delete cascade;
 
 alter table payments drop constraint payments_fk_receipt;
 alter table payments add constraint payments_fk_receipt foreign key (receipt) references receipts(id) on delete cascade;
```

You will now be able to delete records easily from deleting records from closed cash:
```
 delete from closedcash where host = 'hostname' and sequence <= x;
```

## Right-to-left language support (Arabic and Hebrew) ##
### How do I write Arabic product names and other items using OpenBravo and MySQL ###
Arabic characters may appear garbled once saved into the database if the proper settings are not used.

on MYSQL make sure that your database/tables and varchar fields are set to either:
**UTF8 OR CP1256** codpages.

on OpenBravo make sure you set append the JDBC URL with :
`?characterEncoding=utf8` i.e.:
```
jdbc:mysql://localhost:3306/MyDBPROD?characterEncoding=utf8
```

## Hardware configuration ##
### What are the parameters used by Openbravo POS to communicate to a serial printer ###
The serial parameters used by Openbravo POS to connect to serial receipt printers and customer displays are:

  * Bauds: 9600.
  * Data bits: 8.
  * Stop bits: 1.
  * Parity: None.

### How to install a receipt printer that has a USB connector ###
Openbravo POS supports ESC/POS receipt printers that connects directly to a serial, parallel or usb ports (linux only).

Please note that a USB cable has a limited length (without using a repeater cable) so you may find using a serial printer a better option.  The only problem is that commodity hardware often provides a number of USB ports and increasingly  one or no serial ports.

#### Linux USB ####
The usb port in linux is usually detected as `/dev/usb/lp[x]` where 'x' is enumerated for each USB printer you install.

There are no drivers to load for most popular Linux distributions - just plug in your USB printer and configure the printer to type = "file" and file = "/dev/usb/lp0". The cash drawer also works although there are reports this does not under Windows. (http://forge.openbravo.com/plugins/espforum/view.php?group_id=101&forumid=434921&topicid=7016849&topid=7016850)

Where you connect multiple devices with the same name, it is helpful to edit udev rules in order to identify the printers by their serial numbers (to ensure you map them the same way each time the computer is booted)

For example, we use the following rules for Epson TM-T88iv printers on USB in the file `/etc/udev/rules.d/99-epson-printers.rules`

```
 # rules for map multiple usb printers 

 SUBSYSTEMS=="usb", DRIVERS=="usb", ATTRS{product}=="TM-T88IV", ATTRS{serial}=="J4FF031842", SYMLINK+="usb/epson1", GROUP="lp"
 SUBSYSTEMS=="usb", DRIVERS=="usb", ATTRS{product}=="TM-T88IV", ATTRS{serial}=="J4FF031845", SYMLINK+="usb/epson2", GROUP="lp"
 SUBSYSTEMS=="usb", DRIVERS=="usb", ATTRS{product}=="TM-T88IV", ATTRS{serial}=="J4FF029748", SYMLINK+="usb/epson3", GROUP="lp"
```

Just substitute the serial number of your printer to make it work for you.  You will notice the following devices on your system:
```
 /dev/usb/epson1
 /dev/usb/epson2
 /dev/usb/epson3
```

#### Windows USB ####
There are two methods that allow the use of the ESC/POS protocol with an USB port in Openbravo POS on Windows.

##### Share the printer #####
  * Install the receipt printer using the **Generic / Text only** driver
  * Share the printer.
  * Execute the following commands to map the printer to the port **LPT1:**

```
 NET USE LTP1: /DELETE 
 NET USE LTP1: \\COMPUTER NAME\PRINTER /PERSISTENT:YES 
```

  * Configure Openbravo POS to use a receipt printer in mode **file** and select the port **LPT1:**
  * If you are running XP Home, you may need to run the Home Networking Wizard and select "Share folders and printers on this computer" and restart in order to enable printer sharing if you see the following error:
```
    System Error 67 Network Name Not Found.
```

##### Install a serial emulation driver #####
Several USB receipt printers has a driver that creates a virtual serial port connected to the receipt printer. After installed this driver you only have to configure Openbravo POS to use the receipt printer as if was connected to a regular serial port.

For example for the USB Epson TM T88IV there is a serial emulation driver that can be downloaded from here http://www.posguys.com/download/TM-T88IV/TMCOMUSB130a.exe .

### How to install a receipt printer and a cash drawer that have a javapos driver ###
This small guide has been copied from [a forum message](http://forge.openbravo.com/plugins/espforum/view.php?group_id=101&forumid=434921&topicid=5482085#5483763) for an USB Star receipt printer.

  * Unpack the `jpos.jar` file from the Openbravo POS library directory, and add into `jpos/res/jpos.properties` the relevant lines to use an XML file for configuration (mine is named `jpos.xml`)
  * Pack this back up into the `jpos.jar` file
  * Run the Star config utility, and add JavaPOS entries for the printer and cash drawer if you're using that as well. Take a note of the names they're given (my printer is TSP743II\_USB)
  * Save the config from here as `jpos.xml` - place it in the same directory as the final built Openbravo POS `.jar` file.
  * Add the Star JavaPos drivers to the classpath of the project. The classpath is defined in the file `start.bat` for Windows or `start.sh` for Linux. And this example shows the lines to add for the printer Star TSP100 in Windows.

```
 set STARHOME=libstar 
 set CP=%CP%;%STARHOME%/jcl.jar 
 set CP=%CP%;%STARHOME%/jpos191-controls.jar 
 set CP=%CP%;%STARHOME%/stario.jar 
 set CP=%CP%;%STARHOME%/starjavapos.jar 
 set CP=%CP%;%STARHOME%/xercesimpl.jar 
 set CP=%CP%;%STARHOME%/xml-apis.jar  
```

  * Add the location of `jpos.xml` and other options to java.

```
 -Djpos.config.populatorFile="%USERPROFILE%/jpos.xml"
 -Xms32m -Xmx512m 
```

  * Rebuild Openbravo POS
  * The name from the Star config utility needs to be put into the config section of Openbravo POS. Assuming you're using a revision of POS since 310, you will be able to use the printer without a cash drawer.