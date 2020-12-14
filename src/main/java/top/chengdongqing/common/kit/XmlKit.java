package top.chengdongqing.common.kit;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * XML utility functions
 *
 * @author Luyao
 */
public class XmlKit {

    /**
     * Parses the xml to {@link Kv}
     *
     * @param xml the xml string to parse
     * @return the {@link Kv} instance with the key-value mappings from the xml string
     */
    public static Kv<String, String> parseXml(String xml) {
        if (StrKit.isBlank(xml)) throw new IllegalArgumentException("The xml can not be blank");

        Kv<String, String> params = new Kv<>();
        try (InputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))) {
            Document doc = newDocumentBuilder().parse(is);
            doc.getDocumentElement().normalize();
            NodeList nodes = doc.getDocumentElement().getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    params.add(node.getNodeName(), node.getTextContent());
                }
            }
            return params;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Transforms the {@link Map} instance to xml string
     *
     * @param map the map to transform
     * @return the xml string
     */
    public static String toXml(Map<String, String> map) {
        if (map == null || map.isEmpty()) throw new IllegalArgumentException("The map can not be null or empty");

        try (StringWriter writer = new StringWriter()) {
            Document document = newDocumentBuilder().newDocument();
            Element root = document.createElement("xml");
            map.forEach((key, value) -> {
                if (StrKit.isNotBlank(value)) {
                    Element element = document.createElement(key);
                    element.appendChild(document.createTextNode(value));
                    root.appendChild(element);
                }
            });
            document.appendChild(root);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Builds the xml document builder
     *
     * @return the built DocumentBuilder
     */
    private static DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        dbFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        dbFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        // dbFactory.setFeature("http://javax.xml.XMLConfigs/feature/secure-processing", true);
        dbFactory.setXIncludeAware(false);
        dbFactory.setExpandEntityReferences(false);
        return dbFactory.newDocumentBuilder();
    }
}
