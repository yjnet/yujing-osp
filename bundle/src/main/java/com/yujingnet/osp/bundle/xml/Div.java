//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.04.10 at 12:31:04 AM CDT 
//


package com.yujingnet.osp.bundle.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for div complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="div">
 *   &lt;complexContent>
 *     &lt;extension base="{}ul">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="div" type="{}div"/>
 *         &lt;element name="ul" type="{}ul"/>
 *         &lt;element name="span" type="{}span"/>
 *       &lt;/choice>
 *       &lt;attribute name="query-statement" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="node-number" type="{http://www.w3.org/2001/XMLSchema}long" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "div", propOrder = {
    "divOrUlOrSpan"
})
public class Div
    extends Ul
{

    @XmlElements({
        @XmlElement(name = "div", type = Div.class),
        @XmlElement(name = "ul", type = Ul.class),
        @XmlElement(name = "span", type = Span.class)
    })
    protected List<Object> divOrUlOrSpan;
    @XmlAttribute(name = "query-statement")
    protected String queryStatement;
    @XmlAttribute(name = "node-number")
    protected Long nodeNumber;

    /**
     * Gets the value of the divOrUlOrSpan property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the divOrUlOrSpan property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDivOrUlOrSpan().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Div }
     * {@link Ul }
     * {@link Span }
     * 
     * 
     */
    public List<Object> getDivOrUlOrSpan() {
        if (divOrUlOrSpan == null) {
            divOrUlOrSpan = new ArrayList<Object>();
        }
        return this.divOrUlOrSpan;
    }

    /**
     * Gets the value of the queryStatement property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQueryStatement() {
        return queryStatement;
    }

    /**
     * Sets the value of the queryStatement property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQueryStatement(String value) {
        this.queryStatement = value;
    }

    /**
     * Gets the value of the nodeNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getNodeNumber() {
        return nodeNumber;
    }

    /**
     * Sets the value of the nodeNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setNodeNumber(Long value) {
        this.nodeNumber = value;
    }

}
