package top.chengdongqing.common.kit;

import org.apache.commons.lang3.StringUtils;
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
import java.util.HashMap;
import java.util.Map;

/**
 * XML处理工具类
 *
 * @author Luyao
 */
public class XmlKit {

    /**
     * xml转map
     *
     * @param xml xml字符串
     * @return map对象
     */
    public static Map<String, String> xmlToMap(String xml) {
        if (StringUtils.isBlank(xml)) throw new IllegalArgumentException("The xml can not be blank");

        Map<String, String> params = new HashMap<>();
        try (InputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))) {
            Document doc = newDocumentBuilder().parse(is);
            doc.getDocumentElement().normalize();
            NodeList nodes = doc.getDocumentElement().getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    params.put(node.getNodeName(), node.getTextContent());
                }
            }
            return params;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * map转xml
     *
     * @param map
     * @return
     */
    public static String mapToXml(Map<String, String> map) {
        if (map == null || map.isEmpty()) throw new IllegalArgumentException("The map can not be null or empty");

        Document document = newDocumentBuilder().newDocument();
        Element root = document.createElement("xml");
        map.forEach((key, value) -> {
            if (StringUtils.isNotBlank(value)) {
                Element element = document.createElement(key);
                element.appendChild(document.createTextNode(value));
                root.appendChild(element);
            }
        });
        document.appendChild(root);

        try (StringWriter writer = new StringWriter()) {
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
     * 构建xml文档
     *
     * @return
     */
    private static DocumentBuilder newDocumentBuilder() {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            dbFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            dbFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            dbFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            dbFactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
            dbFactory.setXIncludeAware(false);
            dbFactory.setExpandEntityReferences(false);
            return dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}