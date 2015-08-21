package is.arnastofnun.parser;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import is.arnastofnun.utils.Bstring;

public class DataPrep {

    private BParser parser;
    private String searchWord;
    private WordResult wResult;

    ArrayList<String> debug;

    String[] patterns = { "Nf.", "Þf.", "Þgf.", "Ef.", "1. pers.", "2. pers.", "3 .pers.", "1.pers", "2.pers", "3.pers", "Stýfður", "Et.", "Ft." };

    public DataPrep(BParser parser, String searchWord) {
        this.parser = parser;
        this.searchWord = searchWord;
    }

    public WordResult getWordResult() {
        createWordResult();

        if( wResult == null ) return null;
        return wResult;
    }


    private String[] manageMultiResult(Bstring str) {

        String[] split = str.get().split("");
        for( int i = 1; i < str.len()-1; i++ ) {
            if( !split[i-1].equals("/") && split[i].equals(" ") && !split[i+1].equals("/") ) {
                split[i] = "!!";
            }
        }

        String reconstruct = "";

        for( String s : split ) {
            reconstruct += s;
        }

        return reconstruct.split("!!");

    }

    private boolean isSO(String str) {
        return str.contains("Sagnorð");
    }

    private String makeTitle(Bstring str, String tag) {

        if( str.cont(tag) ) {
            return str.remFirst(" ").remFirst(" ").get();
        }

        return "";
    }

    private ArrayList<Bstring> TagUsableNodes(ArrayList<Element> selected, String[] ruleSet) {

        ArrayList<Bstring> rawData = new ArrayList<Bstring>();



        for( Element element : selected ) {
            for( String rule : ruleSet ) {

                if( element.nodeName().equals(rule) ) {
                    rawData.add(new Bstring(rule + " - " + element.text()));
                }

            }
        }

        return rawData;

    }

    private ArrayList<Bstring> extractNodeByRuleSet(ArrayList<Bstring> selected, String[] ruleSet) {

        ArrayList<Bstring> rawData = new ArrayList<Bstring>();

        for( Bstring str : selected ) {
            for( String rule : ruleSet ) {

                if( str.cont(rule) ) {
                    rawData.add(str);
                }

            }
        }

        return rawData;

    }

    private boolean hasRule(Bstring str, String[] ruleSet) {
        for( String rule : ruleSet ) {
            if( str.cont(rule) ) {
                return true;
            }
        }
        return false;
    }

    private boolean hasRule(String str, String[] ruleSet) {
        for( String rule : ruleSet ) {
            if( str.contains(rule) ) {
                return true;
            }
        }
        return false;
    }

    private String getRule(Bstring str, String[] ruleSet) {
        for( String rule : ruleSet ) {
            if( str.cont(rule) ) {
                return rule;
            }
        }

        return null;
    }

    private ArrayList<Bstring> extractNodeByNegativeRuleSet(ArrayList<Bstring> selected, String[] ruleSet) {

        ArrayList<Bstring> rawData = new ArrayList<Bstring>();

        for( Bstring str : selected ) {

            if( !hasRule(str, ruleSet) ) {
                rawData.add(str);
            }

        }
        return rawData;

    }

    private ArrayList<Bstring> expellEmptyNodes(ArrayList<Bstring> selected) {

        ArrayList<Bstring> rawData = new ArrayList<Bstring>();

        for( Bstring str : selected ) {
            // 5 is magic number for -> "XX - "
            if( str.len() > 5 ) {
                rawData.add(str);
            }
        }

        return rawData;
    }

    private ArrayList<Bstring> expellExtraNodes(ArrayList<Bstring> selected, String[] extra) {

        ArrayList<Bstring> data = new ArrayList<Bstring>();

        for( Bstring str : selected ) {
            if( !hasRule(str, extra) ) {
                data.add(str);
            }
        }

        return data;

    }

    private ArrayList<Bstring> splitMultipleTableLine(ArrayList<Bstring> selected) {

        String[] exceptionSet = { "Nf.", "Þf.", "Þgf.", "Ef." };

        ArrayList<Bstring> fTable = new ArrayList<Bstring>();
        ArrayList<Bstring> sTable = new ArrayList<Bstring>();

        for( Bstring str : selected ) {

            if( hasRule(str, exceptionSet) ) {
                String[] tempList = str.get().split(getRule(str, exceptionSet));

                fTable.add(new Bstring("tr -" + tempList[1]));
                sTable.add(new Bstring("tr -" + tempList[2]));
            }

        }

        ArrayList<Bstring> rawData = new ArrayList<Bstring>();

        rawData.add(new Bstring("th - Eintala"));
        for( Bstring str : fTable ) {
            rawData.add(str);
        }
        rawData.add(new Bstring("th - Fleirtala"));
        for( Bstring str : sTable ) {
            rawData.add(str);
        }

        return rawData;

    }
    ///





    // Data extraction

    private String[] removeSoPrefixes(Bstring str, String[] prefixes) {

        String prefix = "NULL";

        for( String pref : prefixes ) {
            if( str.cont(pref) ) {
                prefix = pref;
            }
        }

        if( !prefix.equals("NULL") ) {
            String[] split = str.get().split(prefix);
            String temp = prefix + " " + split[1];
            split[1] = temp;
            return split;
        }

        return null;

    }

    private String[] getColumnNames(String pageTitle, String blockTitle, String subBlockTitle, String tableTitle) {

        if( pageTitle.contains("nafnorð") || pageTitle.contains("Lýsingarorð") || pageTitle.contains("Greinir") || pageTitle.contains("Fornafn") || pageTitle.contains("Afturbeygt") ||
                pageTitle.contains("Persónu") || pageTitle.contains("Töluorð") ) {
            return new String[] { "", "Nf.", "Þf.", "Þgf.", "Ef." };
        }
        if( pageTitle.contains("Atviksorð") ) {
            return new String[] { "" };
        }
        if( pageTitle.contains("Sagnorð") ) {
            if( subBlockTitle.contains("Nafnháttur") || blockTitle.contains("Lýsingarháttur nútíðar") || blockTitle.contains("Sagnbót") ) {
                return new String[] { "" };
            }

            if( blockTitle.contains("Persónuleg notkun - Germynd") ) {
                return new String[] { "", "1. pers.", "2. pers.", "3. pers." };
            }
            if( blockTitle.contains("Ópersónuleg notkun - Germynd (Gervifrumlag)") ) {
                return new String[] { "", "3. pers." };
            }
            if( blockTitle.contains("Boðháttur") ) {
                return new String[] { "", "Stýfður", "Et.", "Ft." };
            }
            if( blockTitle.contains("Lýsingarháttur þátíðar") ) {
                return new String[] { "", "Nf.", "Þf.", "Þgf.", "Ef." };
            }
        }
        return new String[] { "COLUMNS" };
    }

    private String[] getRowNames(String pageTitle, String blockTitle, String subBlockTitle, String tableTitle) {

        if( pageTitle.contains("nafnorð") ) {
            return new String[] { "", "án greinis", "með greini" };
        }
        if( pageTitle.contains("Lýsingarorð") || pageTitle.contains("Greinir") || pageTitle.contains("Fornafn") || pageTitle.contains("Töluorð") ) {
            return new String[] { "", "Karlkyn", "Kvenkyn", "Hvorugkyn" };
        }
        if( pageTitle.contains("Atviksorð") ) {
            return new String[] { "", "Frumstig", "Miðstig", "Efsta stig" };
        }
        if( pageTitle.contains("Afturbeygt") ) {
            return new String[] { "", "Eintala" };
        }
        if( pageTitle.contains("Persónu") ) {
            return new String[] { "", "Eintala", "Fleirtala" };
        }
        if( pageTitle.contains("Sagnorð") ) {
            if( subBlockTitle.contains("Nafnháttur") || blockTitle.contains("Lýsingarháttur nútíðar") ) {
                return new String[] { "" };
            }
            if( blockTitle.contains("Persónuleg notkun - Germynd") || blockTitle.contains("Ópersónuleg notkun - Germynd (Gervifrumlag)") ) {
                return new String[] { "", "Et.", "Ft." };
            }
            if( blockTitle.contains("Boðháttur") || blockTitle.contains("Sagnbót") ) {
                return new String[] { "", "Germynd", "Miðmynd" };
            }
            if( blockTitle.contains("Lýsingarháttur þátíðar") ) {
                return new String[] { "", "Karlkyn", "Kvenkyn", "Hvorugkyn" };
            }
        }
        return new String[] { "ROWS" };
    }

    private String[] beautifyInput(Bstring str, boolean isSO) {

        String[] soPrefixes = { "ég", "þú", "hann", "við", "þið", "þeir" };

        // Remove node tags
        str = str.remFirst(" ").remFirst(" ");

        String[] results = { "" };

        if( isSO ) {

            if( str.cont("að ") ) {
                results[0] = str.get();
                return results;
            }
            if( str.cont("f.") || str.cont("t.")) str = str.remFirst(" ");
            if( str.cont(". pers.") ) str = str.remFirst(" ").remFirst(" ");
            if( str.cont(".pers") || str.cont("Stýfður") ) str = str.remFirst(" ");

            // TODO : CHECK IF LOGIC BRAKES SO

            if( hasRule(str, soPrefixes )) { results[0] = str.get(); }

            else if( str.cont("/") ) { results = manageMultiResult(str); }
            else { results = str.get().split(" "); }

        }
        else {

            if( str.cont(".")) str = str.remFirst(" ");

            if( str.cont("/")) results = manageMultiResult(str);
            else results = str.get().split(" ");

        }

        return results;
    }

    private Tables constructSingleTable(String pageTitle, String blockTitle, String subBlockTitle, String[] elements, ArrayList<Bstring> rawData) {

        String tableTitle = "";
        try {
            tableTitle = makeTitle(rawData.get(0), elements[3]);
        }
        catch(IndexOutOfBoundsException e) {
            // Do nothing
        }

        ArrayList<String> results = new ArrayList<String>();

        for( Bstring str : rawData ) {

            if( str.cont(elements[4]) ) {
                System.out.println(isSO(pageTitle));
                String[] tempList = beautifyInput(str, isSO(pageTitle));

                for( String cleanStr : tempList ) {
                    results.add(cleanStr);
                }
            }

        }

        String[] columnNames = getColumnNames(pageTitle, blockTitle, subBlockTitle, tableTitle);
        String[] rowNames = getRowNames(pageTitle, blockTitle, subBlockTitle, tableTitle);

        return new Tables(tableTitle, columnNames, rowNames, results);

    }

    private SubBlock constructSingleSubBlock(String pageTitle, String blockTitle, String[] elements, ArrayList<Bstring> rawData) {

        ArrayList<Tables> tables = new ArrayList<Tables>();
        String subBlockTitle = "";
        try {
            subBlockTitle = makeTitle(rawData.get(0), elements[2]);
        }
        catch(IndexOutOfBoundsException e) {
            // Do nothing
        }

        ArrayList<ArrayList<Bstring>> rawTables = new ArrayList<ArrayList<Bstring>>();
        ArrayList<Bstring> oneTable = new ArrayList<Bstring>();

        int counter = 0;
        for( Bstring str : rawData ) {

            if( str.cont(elements[3]) ) {

                if( counter == 0 ) {
                    oneTable.add(str);
                    counter++;

                }
                else {
                    rawTables.add(oneTable);
                    oneTable = new ArrayList<Bstring>();
                    oneTable.add(str);
                    counter = 1;
                }

            }
            else if( str.cont(elements[4]) ) {
                oneTable.add(str);
                counter++;
            }

        }

        // Add last table
        rawTables.add(oneTable);

        for( ArrayList<Bstring> aList : rawTables ) {
            Tables nTable = constructSingleTable(pageTitle, blockTitle, subBlockTitle, elements, aList);
            tables.add(nTable);
        }

        return new SubBlock(subBlockTitle, tables);


    }

    private Block constructSingleBlock(String pageTitle, String[] elements, ArrayList<Bstring> rawData) {

        ArrayList<SubBlock> subBlocks = new ArrayList<SubBlock>();

        String blockTitle = makeTitle(rawData.get(0), elements[1]);

        ArrayList<ArrayList<Bstring>> rawSubBlocks = new ArrayList<ArrayList<Bstring>>();
        ArrayList<Bstring> oneSubBlock = new ArrayList<Bstring>();

        int counter = 0;
        for( Bstring str : rawData ) {

            if( str.cont(elements[2]) ) {

                if( counter == 0 ) {
                    oneSubBlock.add(str);
                    counter++;

                }
                else {
                    rawSubBlocks.add(oneSubBlock);
                    oneSubBlock = new ArrayList<Bstring>();
                    oneSubBlock.add(str);
                    counter = 1;
                }

            }
            else if( str.cont(elements[3]) || str.cont(elements[4]) ) {
                oneSubBlock.add(str);
                counter++;
            }

        }

        // Add the last subBlock
        rawSubBlocks.add(oneSubBlock);

        for( ArrayList<Bstring> aList : rawSubBlocks ) {
            SubBlock nSubBlock = constructSingleSubBlock(pageTitle, blockTitle, elements, aList);
            subBlocks.add(nSubBlock);
        }

        return new Block(blockTitle, subBlocks);

    }

    private ArrayList<Block> constructBlocks(ArrayList<Bstring> rawData, String pageTitle) {

        ArrayList<Block> blocks = new ArrayList<Block>();


        ArrayList<ArrayList<Bstring>> rawBlocks = new ArrayList<ArrayList<Bstring>>();
        ArrayList<Bstring> oneBlock = new ArrayList<Bstring>();

        String[] elements = parser.getElements();

        int counter = 0;
        for( Bstring str : rawData ) {

            if( str.cont(elements[1]) ) {
                if( counter == 0 ) {
                    oneBlock.add(str);
                    counter++;

                }
                else {
                    rawBlocks.add(oneBlock);
                    oneBlock = new ArrayList<Bstring>();
                    oneBlock.add(str);
                    counter = 1;
                }
            }
            else if( str.cont(elements[2]) || str.cont(elements[3])  || str.cont(elements[4]) ) {
                oneBlock.add(str);
                counter++;
            }

        }

        //Add the last rawBlock
        rawBlocks.add(oneBlock);

        for( ArrayList<Bstring> aList : rawBlocks ) {
            Block nBlock = constructSingleBlock(pageTitle, elements, aList);
            blocks.add(nBlock);
        }

        return blocks;
    }


    /*
      1 = Nafnord
      2 = Lysingarord
      3 = Atviksord
      4 = Greinir
      5 = Fornafn
      6 = Toluord
      7 = Sagnord
    */
    private String[] getNodeNames(int i) {
        if( i == 1 || i == 6 ) {
            return new String[] { "th", "tr" };
        }
        else if( i == 2 ) {
            return new String[] { "h3", "h4", "th", "tr" };
        }
        else if( i == 3 ) {
            return new String[] { "tr" };
        }
        else if( i == 4 || i == 5 ) {
            return new String[] { "tr" };
        }
        else if( i == 7) {
            return new String[] { "h3", "h4", "th", "tr" };
        }
        else {
            Assert("getNodeNames - integer not recognized", i+"");
            return null;
        }
    }

    private boolean isRule(String str, String[] rules) {

        for( String rule : rules ) {
            if( str.contains(rule) ) return true;
        }
        return false;
    }

    private ArrayList<Bstring> extractDocument(Document doc, String[] nodeNames) {

        ArrayList<Bstring> rawData = new ArrayList<Bstring>();

        for( Element element : doc.body().getAllElements() ) {

            if( isRule(element.nodeName(), nodeNames) ) {
                rawData.add( new Bstring( element.nodeName() + " - " + element.text() ) );
            }

        }

        return rawData;
    }

    private void constructNO(WordResult wR) {

        String[] nodeNames = getNodeNames(1);
        String[] ruleSet = new String[] { "h2 -", "th -", "tr - Nf.", "tr - Þf.", "tr - Þgf.", "tr - Ef." };

        ArrayList<Bstring> usableNodes = extractDocument(parser.getDocument(), nodeNames);

        wR.setDebugList(usableNodes);

        // Throw away empty nodes
        ArrayList<Bstring> rawData = expellEmptyNodes(usableNodes);
        rawData = expellExtraNodes(rawData, new String[] { "tr - án", "tr - með", "tr - Eintala", "tr - Fleirtala" });


        // Ready for WordResult buildup

        for( int i = 0; i < rawData.size(); i++ ) {
            if( rawData.get(i).cont("Athugið:") ) rawData.remove(i);
        }

        ArrayList<Block> blocks = constructBlocks(rawData, wR.getTitle());

        wR.setBlocks(blocks);


    }

    private void constructLO(WordResult wR) {

        String[] nodeNames = getNodeNames(2);
        String[] ruleSet = new String[] { "h2", "h3", "h4", "th", "tr - Nf.", "tr - Þf.", "tr - Þgf.", "tr - Ef." };

        ArrayList<Bstring> usableNodes = extractDocument(parser.getDocument(), nodeNames);

        wR.setDebugList(usableNodes);

        ArrayList<Bstring> rawData = expellEmptyNodes(usableNodes);
        rawData = expellExtraNodes(rawData, new String[] { "tr - Eintala", "tr - Fleirtala", "tr - Karlkyn"} );

        for( int i = 0; i < rawData.size(); i++ ) {
            if( rawData.get(i).cont("Athugið:") ) rawData.remove(i);
        }

        ArrayList<Block> blocks = constructBlocks(rawData, wR.getTitle());

        wR.setBlocks(blocks);


    }

    private void constructAO(WordResult wR) {

        String[] nodeNames = getNodeNames(3);
        String[] ruleSet = nodeNames;

        ArrayList<Bstring> usableNodes = extractDocument(parser.getDocument(), nodeNames);

        wR.setDebugList(usableNodes);


        ArrayList<Bstring> rawData = expellEmptyNodes(usableNodes);
        rawData = expellExtraNodes(rawData, new String[] { "tr - Frumstig"} );

        for( int i = 0; i < rawData.size(); i++ ) {
            if( rawData.get(i).cont("Athugið:") ) rawData.remove(i);
        }

        String[] data = beautifyInput(rawData.get(0), false);

        ArrayList<String> content = new ArrayList<String>();

        for( String str : data ) {
            content.add(str);
        }

        String[] columnNames = getColumnNames(wR.getTitle(), null, null, null);
        String[] rowNames = getRowNames(wR.getTitle(), null, null, null);

        Tables table = new Tables("", columnNames, rowNames, content);
        ArrayList<Tables> tbls = new ArrayList<Tables>();
        tbls.add(table);

        SubBlock subBlock = new SubBlock("", tbls);
        ArrayList<SubBlock> sbblck = new ArrayList<SubBlock>();
        sbblck.add(subBlock);

        Block block = new Block("", sbblck);

        ArrayList<Block> blocks = new ArrayList<Block>();
        blocks.add(block);

        wR.setBlocks(blocks);


    }

    private void constructGR(WordResult wR) {

        String[] nodeNames = getNodeNames(4);
        String[] ruleSet = new String[] { "h2", "tr - Nf.", "tr - Þf.", "tr - Þgf.", "tr - Ef." };

        ArrayList<Bstring> usableNodes = extractDocument(parser.getDocument(), nodeNames);

        wR.setDebugList(usableNodes);

        ArrayList<Bstring> rawData = expellEmptyNodes(usableNodes);
        rawData = expellExtraNodes(rawData, new String[] { "tr - Karlkyn", "tr - Eintala" } );

        for( int i = 0; i < rawData.size(); i++ ) {
            if( rawData.get(i).cont("Athugið:") ) rawData.remove(i);
        }

        rawData = splitMultipleTableLine(rawData);

        ArrayList<Block> blocks = constructBlocks(rawData, wR.getTitle());

        wR.setBlocks(blocks);

    }

    private void constructFO(WordResult wR) {

        String[] nodeNames = getNodeNames(5);
        String[] ruleSet = new String[] { "h2", "tr - Nf.", "tr - Þf.", "tr - Þgf.", "tr - Ef." };

        ArrayList<Bstring> usableNodes = extractDocument(parser.getDocument(), nodeNames);

        wR.setDebugList(usableNodes);

        ArrayList<Bstring> rawData = expellEmptyNodes(usableNodes);

        for( int i = 0; i < rawData.size(); i++ ) {
            if( rawData.get(i).cont("Athugið:") ) rawData.remove(i);
        }

        if( wR.getTitle().contains("Persónu") ) {
            rawData = expellExtraNodes(rawData, new String[] { "tr - Eintala" } );
        }
        else if( wR.getTitle().contains("Afturbeygt") ) {
            rawData = expellExtraNodes(rawData, new String[] { "tr - Eintala" } );
        }
        else {
            rawData = expellExtraNodes(rawData, new String[] { "tr - Karlkyn", "tr - Eintala" } );

            rawData = splitMultipleTableLine(rawData);
        }

        // Ready for WordResult buildup

        ArrayList<Block> blocks = constructBlocks(rawData, wR.getTitle());

        wR.setBlocks(blocks);
    }

    private void constructTO(WordResult wR) {

        String[] nodeNames = getNodeNames(6);
        String[] ruleSet = new String[] { "h2", "th", "tr - Nf.", "tr - Þf.", "tr - Þgf.", "tr - Ef." };

        ArrayList<Bstring> usableNodes = extractDocument(parser.getDocument(), nodeNames);

        wR.setDebugList(usableNodes);

        ArrayList<Bstring> rawData = expellEmptyNodes(usableNodes);
        rawData = expellExtraNodes(rawData, new String[] { "tr - Eintala", "tr - Fleirtala", "tr - Karlkyn" } );

        rawData = expellEmptyNodes(rawData);

        for( int i = 0; i < rawData.size(); i++ ) {
            if( rawData.get(i).cont("Athugið:") ) rawData.remove(i);
        }

        // Ready for WordResult buildup

        ArrayList<Block> blocks = constructBlocks(rawData, wR.getTitle());

        wR.setBlocks(blocks);

    }

    private void constructSO(WordResult wR) {

        String[] nodeNames = getNodeNames(7);
        String[] negRuleSet = new String[] { "tr - Nútíð", "tr - Et. Ft.", "tr - 1. pers.", "tr - 2. pers.", "tr - 3. pers.", "tr - Þátíð", "tr - 1.pers", "tr - 2.pers", "tr - 3.pers",
                "th - Germynd", "th - Miðmynd", "tr - Germynd", "tr - Miðmynd", "tr - Karlkyn", "tr - Eintala", "tr - Fleirtala" };

        ArrayList<Bstring> usableNodes = extractDocument(parser.getDocument(), nodeNames);

        wR.setDebugList(usableNodes);

        ArrayList<Bstring> rawData = expellEmptyNodes(usableNodes);
        rawData = expellExtraNodes(rawData, negRuleSet);

        for( int i = 0; i < rawData.size(); i++ ) {
            if( rawData.get(i).cont("Stýfður") ) rawData.get(i).add(" --");
            if( rawData.get(i).cont("Athugið:") ) rawData.remove(i);
        }

        for( Bstring str : rawData ) {
            System.out.println(str.get());
        }

        // Throw away empty nodes
        rawData = expellEmptyNodes(rawData);

        // Ready for WordResult buildup

        ArrayList<Block> blocks = constructBlocks(rawData, wR.getTitle());

        wR.setBlocks(blocks);

    }

    private void constructSingleResults(WordResult wR) {

        ArrayList<Element> selectedElements = this.parser.getSelectedElements();

        String pageTitle = selectedElements.get(0).text();
        wR.setTitle(pageTitle);

        // Nafnord
        if( pageTitle.contains("Nafnorð") || pageTitle.contains("nafnorð") ) {
            constructNO(wR);
        }
        // Lysingarord
        else if( pageTitle.contains("Lýsingarorð") ) {
            constructLO(wR);
        }
        // Atviksord
        else if( pageTitle.contains("Atviksorð") ) {
            constructAO(wR);
        }
        // Greinir
        else if( pageTitle.contains("Greinir") ) {
            constructGR(wR);
        }
        // Fornafn
        else if( pageTitle.contains("Fornafn") || pageTitle.contains("fornafn") ) {
            constructFO(wR);
        }
        //Toluord
        else if( pageTitle.contains("Töluorð") ) {
            constructTO(wR);
        }
        //Sagnord
        else if( pageTitle.contains("Sagnorð") ) {
            constructSO(wR);
        }
        else {
            Assert("constructSingleResults", "Page title not recongized");
            this.wResult = null;
            return;
        }

        this.wResult = wR;

    }









  /* MultiHit results */

    private int constructInt(Element e, String fR, String sR) {
        try {
            String res = e.getElementsByTag(fR).attr(sR).toString();
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(res);

            int start = 0;
            int stop = 0;
            while( matcher.find() ) {
                start = matcher.start();
                stop = matcher.end();
            }
            if( start != 0 && stop != 0 ) return Integer.parseInt(res.substring(start, stop));

            Assert("constructInt - no integer values in pre-constructed str");
            return 0;
        }
        catch ( Exception x ) {
            Assert("constructInt - Failure to construct integer from Element", x.toString());
            return 0;
        }
    }

    private void constructMultiResults(WordResult wR) {

        Document doc = parser.getDocument();

        String[] hardCE = new String[] { "li", "a", "onClick" };

        int count = doc.select(hardCE[0]).size();

        if( count > 0 ) {
            String[] multiHD = new String[count];
            int[] multiHI = new int[count];

            int iter = 0;

            for( Element e : doc.select(hardCE[0]) ) {
                multiHD[iter] = e.text();

                multiHI[iter] = constructInt( e, hardCE[1], hardCE[2] );

                iter++;
            }

            wR.setMultiHitDescriptions(multiHD);
            wR.setMultiHitIds(multiHI);

            wResult = wR;

            return;
        }

        wResult = null;

    }


    private void createWordResult() {
        WordResult wR = new WordResult();

        if( this.parser.containsNode("VO_beygingarmynd") ) {
            wR.setDescription("SingleHit");
            constructSingleResults(wR);
        }
        else if( this.parser.containsNode("alert") ) {
            wR.setDescription("Miss");
            this.wResult = wR;
            return;
        }
        else {
            wR.setDescription("MultiHit");
            constructMultiResults(wR);
        }
    }

    private void Assert(String str) {
        System.out.println(str);
    }

    private void Assert(String desc, String str) {
        System.out.println(desc + " - " + str);
    }




}
