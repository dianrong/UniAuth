
package com.oracle.ucm;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Service complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="Service"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="User" type="{http://www.oracle.com/UCM}Container" minOccurs="0"/&gt;
 *         &lt;element name="Document" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;extension base="{http://www.oracle.com/UCM}Container"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="File" type="{http://www.oracle.com/UCM}File" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *                 &lt;anyAttribute processContents='skip' namespace='##other'/&gt;
 *               &lt;/extension&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="IdcService" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Service", propOrder = {
    "user",
    "document"
})
public class Service {

    @XmlElement(name = "User")
    protected Container user;
    @XmlElement(name = "Document")
    protected Service.Document document;
    @XmlAttribute(name = "IdcService")
    @XmlSchemaType(name = "anySimpleType")
    protected String idcService;

    /**
     * 获取user属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Container }
     *     
     */
    public Container getUser() {
        return user;
    }

    /**
     * 设置user属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Container }
     *     
     */
    public void setUser(Container value) {
        this.user = value;
    }

    /**
     * 获取document属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Service.Document }
     *     
     */
    public Service.Document getDocument() {
        return document;
    }

    /**
     * 设置document属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Service.Document }
     *     
     */
    public void setDocument(Service.Document value) {
        this.document = value;
    }

    /**
     * 获取idcService属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdcService() {
        return idcService;
    }

    /**
     * 设置idcService属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdcService(String value) {
        this.idcService = value;
    }


    /**
     * <p>anonymous complex type的 Java 类。
     * 
     * <p>以下模式片段指定包含在此类中的预期内容。
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;extension base="{http://www.oracle.com/UCM}Container"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="File" type="{http://www.oracle.com/UCM}File" maxOccurs="unbounded" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *       &lt;anyAttribute processContents='skip' namespace='##other'/&gt;
     *     &lt;/extension&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "file"
    })
    public static class Document
        extends Container
    {

        @XmlElement(name = "File")
        protected List<File> file;

        /**
         * Gets the value of the file property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the file property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getFile().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link File }
         * 
         * 
         */
        public List<File> getFile() {
            if (file == null) {
                file = new ArrayList<File>();
            }
            return this.file;
        }

    }

}
