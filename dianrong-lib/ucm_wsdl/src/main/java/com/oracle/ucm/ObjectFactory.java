
package com.oracle.ucm;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.oracle.ucm package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GenericRequest_QNAME = new QName("http://www.oracle.com/UCM", "GenericRequest");
    private final static QName _GenericResponse_QNAME = new QName("http://www.oracle.com/UCM", "GenericResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.oracle.ucm
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Service }
     * 
     */
    public Service createService() {
        return new Service();
    }

    /**
     * Create an instance of {@link Field }
     * 
     */
    public Field createField() {
        return new Field();
    }

    /**
     * Create an instance of {@link Generic }
     * 
     */
    public Generic createGeneric() {
        return new Generic();
    }

    /**
     * Create an instance of {@link Container }
     * 
     */
    public Container createContainer() {
        return new Container();
    }

    /**
     * Create an instance of {@link ResultSet }
     * 
     */
    public ResultSet createResultSet() {
        return new ResultSet();
    }

    /**
     * Create an instance of {@link Row }
     * 
     */
    public Row createRow() {
        return new Row();
    }

    /**
     * Create an instance of {@link OptionList }
     * 
     */
    public OptionList createOptionList() {
        return new OptionList();
    }

    /**
     * Create an instance of {@link File }
     * 
     */
    public File createFile() {
        return new File();
    }

    /**
     * Create an instance of {@link Service.Document }
     * 
     */
    public Service.Document createServiceDocument() {
        return new Service.Document();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Generic }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/UCM", name = "GenericRequest")
    public JAXBElement<Generic> createGenericRequest(Generic value) {
        return new JAXBElement<Generic>(_GenericRequest_QNAME, Generic.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Generic }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/UCM", name = "GenericResponse")
    public JAXBElement<Generic> createGenericResponse(Generic value) {
        return new JAXBElement<Generic>(_GenericResponse_QNAME, Generic.class, null, value);
    }

}
