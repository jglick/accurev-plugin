package hudson.plugins.accurev;

import hudson.plugins.accurev.AccurevLauncher.ICmdOutputXmlParser;
import hudson.plugins.accurev.AccurevLauncher.UnhandledAccurevCommandOutput;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ParseShowReftrees implements ICmdOutputXmlParser<Map<String, AccurevReferenceTree>, Void> {
    public Map<String, AccurevReferenceTree> parse(XmlPullParser parser, Void context)
            throws UnhandledAccurevCommandOutput, IOException, XmlPullParserException {
        final Map<String, AccurevReferenceTree> reftrees = new HashMap<String, AccurevReferenceTree>();
        while (true) {
            switch (parser.next()) {
            case XmlPullParser.START_DOCUMENT:
                break;
            case XmlPullParser.END_DOCUMENT:
                return reftrees;
            case XmlPullParser.START_TAG:
                final String tagName = parser.getName();
                if ("Element".equalsIgnoreCase(tagName)) {
                    final String name = parser.getAttributeValue("", "Name");
                    final String storage = parser.getAttributeValue("", "Storage");
                    final String host = parser.getAttributeValue("", "Host");
                    final String streamNumber = parser.getAttributeValue("", "Stream");
                    final String depot = parser.getAttributeValue("", "depot");
                    try {
                        final Long streamNumberOrNull = streamNumber == null ? null : Long.valueOf(streamNumber);
                        reftrees.put(name, new AccurevReferenceTree(depot, streamNumberOrNull, name, host, storage));
                    } catch (NumberFormatException e) {
                        throw new UnhandledAccurevCommandOutput(e);
                    }
                }
                break;
            case XmlPullParser.END_TAG:
                break;
            case XmlPullParser.TEXT:
                break;
            }
        }
    }
}
