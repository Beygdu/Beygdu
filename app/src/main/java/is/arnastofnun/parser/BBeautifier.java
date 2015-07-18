package is.arnastofnun.parser;

import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import is.arnastofnun.utils.Bstring;

/**
 * Created by arnarjons on 18.7.2015.
 */
public class BBeautifier {

    private BParser parser;
    private WordResult wResult;

    private String searchWord;
    private String description;

    private String[] multiHitDescriptions;
    private int[] multiHitIds;
    private String title;
    private String warning;

    private ArrayList<Block> result;

    String[] patterns = { "Nf.", "Þf.", "Þgf.", "Ef.", "1. pers.", "2. pers.", "3 .pers.", "1.pers", "2.pers", "3.pers",
            "Stýfður", "Et.", "Ft." };

    public BBeautifier(BParser parser, String searchWord) {
        this.parser = parser;
        this.searchWord = searchWord;
    }

    public WordResult getWordResult() {
        createWordResult();

        if( wResult == null ) return null;
        return wResult;
    }
    ////////////////////////////////////////

    private String[] getRuleSet( Bstring str ) {

        if( str.cont("Sagnorð") ) {
            return new String[] { "th - Germynd", "th - Miðmynd", "tr - Germynd", "tr - Miðmynd", "tr - Nútíð", "tr - Þátíð", "tr - Et. Ft.",
                    "tr - 1. pers", "tr - 2. pers", "tr - 3. pers", "tr - 1.pers", "tr - 2.pers", "tr - 3.pers",
                    "tr - Karlkyn", "tr - Eintala", "tr - Fleirtala" };
        }
        else if( str.cont("Lýsingarorð") ) {
            return new String[] { "h2", "h3", "h4", "th", "tr - Nf.", "tr - Þf.", "tr - Þgf.", "tr - Ef." };
        }
        else if( str.cont("nafnorð") || str.cont("Töluorð") || str.cont("Afturbeygt") ) {
            return new String[] { "h2", "th", "tr - Nf.", "tr - Þf.", "tr - Þgf.", "tr - Ef." };
        }
        else if( str.cont("Atviksorð") ) {
            return new String[] { "tr - Frumstig", "th - Frumstig", "th - Miðstig", "th - Efsta stig" };
        }
        else if( str.cont("Persónu") || str.cont("Fornafn") || str.cont("Greinir") ) {
            return new String[] { "h2", "tr - Nf.", "tr - Þf.", "tr - Þgf.", "tr - Ef." };
        }
        else {
            System.out.println("getRuleSet - control string not recognized");
            return null;
        }
    }

    private String[] getTableColumns(String title, String blockTitle, String subBlockTitle, String tableTitle) {

        String[] HCWT = { "nafnorð", "Sagnorð", "Lýsingarorð", "Atviksorð", "Fornafn", "Greinir",
                "Töluorð" };
        String[] HCWTHelper = { "Afturbeygt", "Persónu", "Fornafn", "fornafn" };
        String[] HCBT = { "Persónuleg notkun - Germynd", "Persónuleg notkun - Miðmynd",
                "Ópersónuleg notkun - Germynd", "Ópersónuleg notkun - Miðmynd", "Boðháttur", "Sagnbót",
                "Lýsingarháttur nútíðar", "Lýsingarháttur þátíðar" };
        String[] HCSBT = { "Nafnháttur", "Framsöguháttur", "Viðtengingarháttur" };


        if( title.contains( HCWT[0] ) ) {
            // Nafnord
            return new String[] { "", "án greinis", "með greini" };
        }
        else if( title.contains( HCWT[2] ) || title.contains( HCWT[5] ) || title.contains( HCWT[6] ) ) {
            // Lysingarord, Greinar og Toluord
            return new String[] { "", "Karlkyn", "Kvenkyn", "Hvorugkyn" };
        }
        else if( title.contains( HCWT[3] ) ) {
            // Atviksord
            return new String[] { "", "Frumstig", "Miðstig", "Efsta stig" };
        }
        else if( title.contains( HCWT[2] ) || title.contains( HCWTHelper[3] ) ) {
            //Fornofn
            if( title.contains( HCWTHelper[0] ) ) {
                return new String[] { "", "Eintala" };
            }
            else if( title.contains( HCWTHelper[1] ) ) {
                return new String[] { "", "Eintala", "Fleirtala" };
            }
            else if( title.contains( HCWTHelper[2] ) ) {
                return new String[] { "", "Karlkyn", "Kvenkyn", "Hvorugkyn" };
            }
        }
        else if( title.contains( HCWT[1] ) ) {
            // Sagnord
            if( subBlockTitle.contains( HCSBT[0]) ) {
                return new String[] { "", "" };
            }
            else if( subBlockTitle.contains( HCSBT[1]) || subBlockTitle.contains( HCSBT[2] ) ) {
                return new String[] { "", "Eintala", "Fleirtala" };
            }
            else if( blockTitle.contains( HCBT[4] ) || blockTitle.contains( HCBT[5] ) ) {
                return new String[] { "", "Germynd", "Miðmynd" };
            }
            else if( blockTitle.contains( HCBT[6] ) || blockTitle.contains( HCBT[7] ) ) {
                return new String[] { "", "Karlkyn", "Kvenkyn", "Hvorugkyn" };
            }
        }

        return new String[] { "", "" };

    }

    private String[] getTableRows(String title, String blockTitle, String subBlockTitle, String tableTitle) {

        String[] HCWT = { "nafnorð", "Sagnorð", "Lýsingarorð", "Atviksorð", "Fornafn", "Greinir",
                "Töluorð" };
        String[] HCBT = { "Persónuleg notkun - Germynd", "Persónuleg notkun - Miðmynd",
                "Ópersónuleg notkun - Germynd", "Ópersónuleg notkun - Miðmynd", "Boðháttur", "Sagnbót",
                "Lýsingarháttur nútíðar", "Lýsingarháttur þátíðar" };
        String[] HCSBT = { "Nafnháttur", "Framsöguháttur", "Viðtengingarháttur" };

        if( title.contains( HCWT[0] ) ||
                title.contains( HCWT[2] ) ||
                title.contains( HCWT[4] ) ||
                title.contains( HCWT[5] ) ||
                title.contains( HCWT[6] ) ) {

            return new String[] { "", "Nf.", "Þf.", "Þgf.", "Ef." };

        }

        else if( title.contains( HCWT[3] ) ) {

            return new String[] { "", "" };

        }

        else if( title.contains( HCWT[2] ) ) {

            if( blockTitle.contains( HCBT[0] ) ||
                    blockTitle.contains( HCBT[1] ) ||
                    blockTitle.contains( HCBT[3] ) ) {

                if( subBlockTitle.contains( HCSBT[0] ) ) {
                    return new String[] { "", "" };
                }

                else if ( subBlockTitle.contains( HCSBT[1] ) ||
                        subBlockTitle.contains( HCSBT[2] )) {

                    return new String[] { "", "1. pers.", "2. pers.", "3. pers." };

                }

            }

            else if( blockTitle.contains( HCBT[2] ) ) {

                if( subBlockTitle.contains( HCSBT[0] ) ) {
                    return new String[] { "", "" };
                }

                else if ( subBlockTitle.contains( HCSBT[1] ) ||
                        subBlockTitle.contains( HCSBT[2] )) {

                    return new String[] { "", "3. pers." };

                }

            }

            else if( blockTitle.contains( HCBT[4] ) ) {
                return new String[] { "", "Stýfður", "Et.", "Ft." };
            }

            else if( blockTitle.contains( HCBT[5] ) ) {
                return new String[] { "", "" };
            }

            else if( blockTitle.contains( HCBT[6] ) || blockTitle.contains( HCBT[7] ) ) {
                return new String[] { "", "Nf.", "Þf.", "Þgf.", "Ef." };
            }

        }


        return new String[] { "", "" };

    }

    private boolean isValidResultString(String text, String[] ruleSet) {
        for( int i = 0; i < ruleSet.length; i++ ) {
            if( text.contains(ruleSet[i])) return true;
        }
        return false;
    }

    private String makeTitle(Bstring str, boolean isTitle) {

        if( isTitle ) return str.remFirst(" ").remFirst(" ").get();
        return "";

    }

    private Bstring destroyPointer(Bstring str) {
        return str.remFirst(" ").remFirst(" ");
    }

    private String[] manageMultiResult(Bstring str) {

        String[] split = str.get().split("");
        for( int i = 1; i < split.length-1; i++ ) {
            if( !split[i-1].equals("/") && split[i].equals(" ") && !split[i+1].equals("/") ) {
                split[i] = "!!";
            }
        }

        String reconString = "";

        for( String s : split ) {
            reconString += s;
        }

        return reconString.split("!!");
    }

    private String[] manageVerbSpecialCases(Bstring str) {

        String[] exceptionSets = { "ég", "þú", "hann", "hún", "það", "við", "þið", "þeir", "þær", "þau" };
        String[] dump = str.get().split(" ");

        String resultString = "";

        for( String s : dump ) {
            for( String e : exceptionSets ) {
                resultString += s + " ";
            }
        }

        resultString = resultString.substring(0, resultString.length()-1);

        if( resultString.contains("/") ) {
            return manageMultiResult(new Bstring(resultString));
        }
        else {
            return resultString.split(" ");
        }
    }

    private String[] beautifyInput(Bstring str, String wRTitle, String blockTitle, String subBlockTitle, String tableTitle) {

        str = destroyPointer(str);

        if( str.cont(patterns[0]) || str.cont(patterns[1]) || str.cont(patterns[2]) || str.cont(patterns[3]) ) {
            str.remFirst(".");
        }
        else if( str.cont(patterns[4]) || str.cont(patterns[5]) || str.cont(patterns[6]) ) {
            return manageVerbSpecialCases(str);
        }
        else if( str.cont(patterns[7]) || str.cont(patterns[8]) || str.cont(patterns[9]) ) {
            return manageVerbSpecialCases(str);
        }
        else if( str.cont(patterns[10]) ) {
            str.remFirst(" ");
        }
        else if( str.cont(patterns[11]) || str.cont(patterns[12]) ) {
            str.remFirst(".");
        }

        if( str.cont("/") ) {
            return manageMultiResult(str);
        }
        else {
            return str.get().split(" ");
        }

    }

    private void constructExceptionBlock(ArrayList<Bstring> rawData, String[] elements, WordResult wR) {

        ArrayList<String> firstRawTable = new ArrayList<String>();
        ArrayList<String> secondRawTable = new ArrayList<String>();

        String[] exceptionSet = { "Nf", "Þf", "Þgf", "Ef" };

        for( Bstring bs : rawData ) {

            if( bs.cont(".") ) {

                String[] tempList = { "" };
                bs = destroyPointer(bs);

                for( int i = 0; i < exceptionSet.length; i++ ) {

                    if( bs.cont(exceptionSet[i]) ) {
                        tempList = bs.get().split(exceptionSet[i]);
                        break;
                    }

                }

                if( tempList != null && !tempList[0].equals("") && tempList.length > 1 ) {
                    firstRawTable.add(tempList[1]);
                    secondRawTable.add(tempList[2]);
                }

            }

        }

        ArrayList<String> firstTableContent = new ArrayList<String>();
        ArrayList<String> secondTableContent = new ArrayList<String>();

        for( String s : firstRawTable ) {
            String[] tempList;
            if( s.contains("/")) {
                tempList = manageMultiResult(new Bstring(s));
            }
            else {
                tempList = s.split(" ");
            }

            for( String r : tempList ) {
                firstTableContent.add(r);
            }
        }

        for( String s : secondRawTable ) {
            String[] tempList;
            if( s.contains("/")) {
                tempList = manageMultiResult(new Bstring(s));
            }
            else {
                tempList = s.split(" ");
            }

            for( String r : tempList ) {
                secondTableContent.add(r);
            }
        }

        Tables firstTable = new Tables("Eintala", new String[] { "", "Karlkyn", "Kvenkyn", "Hvorugkyn" }, new String[] { "", "Nf.", "Þf.", "Þgf.", "Ef." }, firstTableContent);
        Tables secondTable = new Tables("Fleirtala", new String[] { "", "Karlkyn", "Kvenkyn", "Hvorugkyn" }, new String[] { "", "Nf.", "Þf.", "Þgf.", "Ef." }, secondTableContent);

        ArrayList<Tables> tables = new ArrayList<Tables>();
        tables.add(firstTable);
        tables.add(secondTable);

        SubBlock subBlock = new SubBlock("", tables);

        ArrayList<SubBlock> sB = new ArrayList<SubBlock>();
        sB.add(subBlock);

        Block newBlock = new Block("", sB);

        ArrayList<Block> blocks = new ArrayList<Block>();

        blocks.add(newBlock);

        wR.setBlocks(blocks);

        wResult = wR;

    }

    private Tables constructTable(ArrayList<Bstring> table, String[] elements, String wRTitle, String blockTitle, String subBlockTitle) {

        String tableTitle = makeTitle(table.get(0), table.get(0).cont(elements[3]));

        String[] tableRows = getTableRows(wRTitle, blockTitle, subBlockTitle, tableTitle);
        String[] tableColumns = getTableColumns(wRTitle, blockTitle, subBlockTitle, tableTitle);

        ArrayList<String> results = new ArrayList<String>();

        for( Bstring str : table ) {
            String[] tempList = beautifyInput(str, wRTitle, blockTitle, subBlockTitle, tableTitle);

            for( String cleanStr : tempList ) {
                results.add(cleanStr);
            }
        }

        return new Tables(tableTitle, tableColumns, tableRows, results);
    }

    private SubBlock constructSubBlock(ArrayList<Bstring> subBlock, String[] elements, String wRTitle, String blockTitle) {

        String subBlockTitle = makeTitle(subBlock.get(0), subBlock.get(0).cont(elements[2]));

        ArrayList<ArrayList<Bstring>> allTables = new ArrayList<ArrayList<Bstring>>();
        ArrayList<Bstring> table = new ArrayList<Bstring>();

        int counter = 0;

        for( Bstring str : subBlock ) {
            if( str.cont(elements[3]) ) {
                if( counter == 0 ) {
                    table.add(str);
                    counter++;
                }
                else {
                    allTables.add(table);
                    table = new ArrayList<Bstring>();
                    table.add(str);
                    counter = 1;
                }
            }
            else if( str.cont(elements[4]) ) {
                table.add(str);
                counter++;
            }
        }

        allTables.add(table);

        ArrayList<Tables> tables = new ArrayList<Tables>();

        for( ArrayList<Bstring> aList : allTables ) {
            Tables newTable = constructTable(aList, elements, wRTitle, blockTitle, subBlockTitle);
            tables.add(newTable);
        }

        return new SubBlock(subBlockTitle, tables);
    }

    /* SingleHit results */
    private Block constructBlock(ArrayList<Bstring> block, String[] elements, String wRTitle) {

        String blockTitle = makeTitle(block.get(0), block.get(0).cont(elements[1]));

        ArrayList<ArrayList<Bstring>> allSubBlocks = new ArrayList<ArrayList<Bstring>>();
        ArrayList<Bstring> sB = new ArrayList<Bstring>();

        int counter = 0;

        for( Bstring str : block ) {
            if( str.cont(elements[2]) ) {
                if( counter == 0 ) {
                    sB.add(str);
                    counter++;
                }
                else {
                    allSubBlocks.add(sB);
                    sB = new ArrayList<Bstring>();
                    sB.add(str);
                    counter = 1;
                }
            }
            else if( str.cont(elements[3]) || str.cont(elements[4]) ) {
                sB.add(str);
                counter++;
            }
        }

        allSubBlocks.add(sB);

        ArrayList<SubBlock> subBlocks = new ArrayList<SubBlock>();

        for( ArrayList<Bstring> aList : allSubBlocks ) {
            SubBlock newSubBlock = constructSubBlock(aList, elements, wRTitle, blockTitle);
            subBlocks.add(newSubBlock);
        }

        return new Block(blockTitle, subBlocks);

    }

    private void constructBlocks(ArrayList<Bstring> rawData, String[] elements, WordResult wR) {

        ArrayList<ArrayList<Bstring>> allBlocks = new ArrayList<ArrayList<Bstring>>();

        ArrayList<Bstring> oneBlock = new ArrayList<Bstring>();

        int counter = 0;

        for( Bstring str : rawData ) {

            if( str.cont(elements[1]) ) {
                if( counter == 0 ) {
                    oneBlock.add(str);
                    counter++;
                }
                else {
                    allBlocks.add(oneBlock);
                    oneBlock = new ArrayList<Bstring>();
                    oneBlock.add(str);
                    counter = 1;
                }
            }
            else if( str.cont(elements[2]) || str.cont(elements[3]) || str.cont(elements[4]) ) {
                oneBlock.add(str);
                counter++;
            }

        }

        allBlocks.add(oneBlock);

        ArrayList<Block> result = new ArrayList<Block>();

        for( ArrayList<Bstring> aList : allBlocks ) {

            Block newBlock = constructBlock(aList, elements, wR.getTitle());
            result.add(newBlock);
        }

        wR.setBlocks(result);

        this.wResult = wR;

    }

    private void constructSingleResults(WordResult wR) {
        try {

            String[] hardCE = new String[] { "alert", "Sagnorð", "Atviksorð", "Fornafn", "Greinir" };

            Document doc = parser.getDocument();
            String[] elements = parser.getElements();

            wR.setTitle(doc.getElementsByTag(elements[0]).text());

            try {
                wR.setWarning(doc.getElementsByClass( hardCE[0] ).toString());
            }
            catch ( Exception y ) {
                // Do Nothing
            }

            String[] ruleSet = getRuleSet(new Bstring(wR.getTitle()));

            ArrayList<Bstring> rawData = new ArrayList<Bstring>();

            ArrayList<Element> selectedElements = parser.getSelectedElements();

            for( Element e : selectedElements ) {
                if( e.text().length() > 0 ) {
                    if( wR.getTitle().contains( hardCE[1] ) || wR.getTitle().contains( hardCE[2] ) ) {
                        if( !isValidResultString(e.text(), ruleSet) ) {
                            rawData.add(new Bstring(e.tag().toString() + " - " + e.text()));
                        }
                    }
                    else {
                        if( isValidResultString(e.text(), ruleSet) ) {
                            rawData.add(new Bstring(e.tag().toString() + " - " + e.text()));
                        }
                    }
                }
            }

            if( wR.getTitle().contains(hardCE[3]) || wR.getTitle().contains(hardCE[4] )) {
                constructExceptionBlock(rawData, elements, wR);
                return;
            }

            constructBlocks(rawData, elements, wR);
        }
        catch ( Exception x ) {
            Log.w("BBeautifier constructS", x.toString());
            wResult = null;
        }
    }

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

            Log.w("BBeautifier constructIt", "no integer values in pre-constructed str");
            return 0;
        }
        catch ( Exception x ) {
            Log.w("BBeautifier constructIt", "Failure to construct integer from Element - " + x);
            return 0;
        }
    }

    /* MultiHit results */
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
}
