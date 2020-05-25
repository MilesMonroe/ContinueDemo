package com.example.log_catcher.test_demo.xml_json_test.xml;

import com.example.log_catcher.util.ERROR;
import com.example.log_catcher.util.LogHelper;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**本来用于解析xml文件时使用
 *
 */
public class XmlSaxHandlerHelper extends DefaultHandler {

    private String nodeName;

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        LogHelper.getInstance().w("========>startDocument");

    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        LogHelper.getInstance().w("========>endDocument");

    }

    @Override
    /**【uri是命名空间|localName是不带命名空间前缀的标签名|qName是带命名空间前缀的标签名|attributes可以得到所有的属性名和对应的值】**/
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        LogHelper.getInstance().w("===>startElement:uri="+uri+",localName="+localName+",qName="+qName+",attributes="+attributes.toString());
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        LogHelper.getInstance().w("====>endElement:uri="+uri+",localName="+localName+",qName="+qName);
    }

    @Override
    /**【ch存放标签中的内容，start是起始位置，length是内容长度】**/
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        LogHelper.getInstance().w("characters:"+new String(ch, start, length));
    }

    /**
     * 入参为String类型的xml解析
     * @param inputXmlContent 是根据xml文件读到的String型xml内容，所以不应该xml的文件名
     *
     * 备注:不该传入xml的文件名
     */
    public static int parseXmlBySax(String inputXmlContent){

        try {
            LogHelper.getInstance().w("--------------parseXmlBySax in------------------\n\n");
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();

            XmlSaxHandlerHelper xmlHandler = new XmlSaxHandlerHelper();

            //解析方法1:根据string开始解析
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler(xmlHandler);
            xmlReader.parse(new InputSource(new StringReader(inputXmlContent)));

            //解析方法2:或者用InputStream流解析
//            saxParser.parse(inputStream, xmlHandler);
            LogHelper.getInstance().w("--------------parseXmlBySax out------------------\n\n");
            return ERROR.SUCCESS;

        } catch (SAXException e) {
            e.printStackTrace();
            return ERROR.ERR_FAIL;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return ERROR.ERR_FAIL;
        } catch (IOException e) {
            e.printStackTrace();
            return ERROR.ERR_FAIL;
        }
    }

    /**
     * 入参为InputStream类型的xml解析
     * @param input_xml
     */
    public static int parseXmlBySax(InputStream input_xml){

        try {
            LogHelper.getInstance().w("--------------parseXmlBySax in------------------\n\n");
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();

            XmlSaxHandlerHelper xmlHandler = new XmlSaxHandlerHelper();

//            //解析方法1:根据string开始解析
//            XMLReader xmlReader = saxParser.getXMLReader();
//            xmlReader.setContentHandler(xmlHandler);
//            xmlReader.parse(new InputSource(new StringReader(input_xml)));

            //解析方法2:或者用InputStream流解析
            saxParser.parse(input_xml, xmlHandler);
            LogHelper.getInstance().w("--------------parseXmlBySax out------------------\n\n");

            return ERROR.SUCCESS;
        } catch (SAXException e) {
            e.printStackTrace();
            return ERROR.ERR_FAIL;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return ERROR.ERR_FAIL;
        } catch (IOException e) {
            e.printStackTrace();
            return ERROR.ERR_FAIL;
        }
    }

    /**
     * 入参为xmlName的xml解析
     * @param xmlName
     */
    public static int parseXmlBySax(File xmlName){
        InputStream file_input;

        try {
            file_input= new FileInputStream(xmlName);
            LogHelper.getInstance().w("--------------parseXmlBySax in------------------\n\n");
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();

            XmlSaxHandlerHelper xmlHandler = new XmlSaxHandlerHelper();

//            //解析方法1:根据string开始解析
//            XMLReader xmlReader = saxParser.getXMLReader();
//            xmlReader.setContentHandler(xmlHandler);
//            xmlReader.parse(new InputSource(new StringReader(input_xml)));

            //解析方法2:或者用InputStream流解析
            saxParser.parse(file_input, xmlHandler);
            LogHelper.getInstance().w("--------------parseXmlBySax out------------------\n\n");
            return ERROR.SUCCESS;
        } catch (SAXException e) {
            e.printStackTrace();
            return ERROR.ERR_FAIL;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return ERROR.ERR_FAIL;
        } catch (IOException e) {
            e.printStackTrace();
            return ERROR.ERR_FAIL;
        }
    }

}
