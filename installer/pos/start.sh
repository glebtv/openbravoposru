#!/bin/sh

#    Openbravo POS is a point of sales application designed for touch screens.
#    Copyright (C) 2007-2009 Openbravo, S.L.
#    http://sourceforge.net/projects/openbravopos
#
#    This file is part of Openbravo POS.
#
#    Openbravo POS is free software: you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation, either version 3 of the License, or
#    (at your option) any later version.
#
#    Openbravo POS is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU General Public License for more details.
#
#    You should have received a copy of the GNU General Public License
#    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.

DIRNAME=`dirname $0`

CP=$DIRNAME/openbravopos.jar

CP=$CP:$DIRNAME/lib/jasperreports-4.5.0.jar
CP=$CP:$DIRNAME/lib/jcommon-1.0.17.jar
CP=$CP:$DIRNAME/lib/jfreechart-1.0.14.jar
CP=$CP:$DIRNAME/lib/jdt-compiler-3.1.1.jar
CP=$CP:$DIRNAME/lib/commons-beanutils-1.8.3.jar
CP=$CP:$DIRNAME/lib/commons-digester-1.7.jar
CP=$CP:$DIRNAME/lib/iText-2.1.7.jar
CP=$CP:$DIRNAME/lib/poi-3.7-20101029.jar
CP=$CP:$DIRNAME/lib/barcode4j-light-2.1.jar
CP=$CP:$DIRNAME/lib/commons-codec-1.5.jar
CP=$CP:$DIRNAME/lib/velocity-1.6.2.jar
CP=$CP:$DIRNAME/lib/oro-2.0.8.jar
CP=$CP:$DIRNAME/lib/commons-collections-3.2.1.jar
CP=$CP:$DIRNAME/lib/commons-lang-2.4.jar
CP=$CP:$DIRNAME/lib/bsh-2.1b5.jar
CP=$CP:$DIRNAME/lib/RXTXcomm.jar
CP=$CP:$DIRNAME/lib/jpos113.jar
CP=$CP:$DIRNAME/lib/swingx-1.0.jar
CP=$CP:$DIRNAME/lib/substance.jar
CP=$CP:$DIRNAME/lib/substance-swingx.jar
CP=$CP:$DIRNAME/lib/AbsoluteLayout.jar

# Apache Axis SOAP libraries.
CP=$CP:$DIRNAME/lib/axis.jar
CP=$CP:$DIRNAME/lib/jaxrpc.jar
CP=$CP:$DIRNAME/lib/saaj.jar
CP=$CP:$DIRNAME/lib/wsdl4j.jar
CP=$CP:$DIRNAME/lib/commons-discovery-0.4.jar
CP=$CP:$DIRNAME/lib/commons-logging-1.1.1.jar

CP=$CP:$DIRNAME/locales/
CP=$CP:$DIRNAME/reports/

CP=$CP:$DIRNAME/lib/freefont-20100919

# Select the library folder
case "`uname -s`" in
Linux)
    case "`uname -m`" in
    i686) LIBRARYPATH=/lib/Linux/i686-unknown-linux-gnu;;
    ia64) LIBRARYPATH=/lib/Linux/ia64-unknown-linux-gnu;;
    x86_64|amd64) LIBRARYPATH=/lib/Linux/x86_64-unknown-linux-gnu;;
    esac;;
SunOS)
    case "`uname -m`" in
    sparc32) LIBRARYPATH=/Solaris/sparc-solaris/sparc32-sun-solaris2.8;;
    sparc64) LIBRARYPATH=/Solaris/sparc-solaris/sparc64-sun-solaris2.8;;
    esac;;
Darwin) LIBRARYPATH=/lib/Mac_OS_X;;
CYGWIN*|MINGW32*) LIBRARYPATH=/lib/Windows/i368-mingw32;;
esac

# start Openbravo POS
java -cp $CP -Djava.util.logging.config.file=$DIRNAME/logging.properties -Dswing.defaultlaf=javax.swing.plaf.metal.MetalLookAndFeel -Djava.library.path=$DIRNAME$LIBRARYPATH -Ddirname.path=$DIRNAME/ com.openbravo.pos.forms.StartPOS "$@"
