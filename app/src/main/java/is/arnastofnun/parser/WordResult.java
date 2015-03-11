package is.arnastofnun.parser;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Arnar Jonsson
 * @since 20.11.2014
 * @version 0.4
 */
public class WordResult implements Serializable {

    private String searchWord;
    private String description;

    private String[] multiHitDescriptions;
    private int[] multiHitIds;

    private String title;
    private String warning;





    private ArrayList<Block> result = new ArrayList<Block>();

    public WordResult() {

    }

    /////
    // Get/Set Methods
    /////

    public void setDescription(String dscrp) {
        this.description = dscrp;
    }

    public String getDescription() {
        return this.description;
    }

    public void setSearchWord(String sWord) {
        this.searchWord = sWord;
    }


    public String getSearchWord() {
        return this.searchWord;
    }

    public void setMultiHitDescriptions(String[] multiHitDescriptions) {
        this.multiHitDescriptions = multiHitDescriptions;
    }

    public String[] getMultiHitDescriptions() {
        return this.multiHitDescriptions;
    }

    public void setMultiHitIds(int[] multiHitIds) {
        this.multiHitIds = multiHitIds;
    }

    public int[] getMultiHitIds() {
        return this.multiHitIds;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    public String getWarning() {
        return this.warning;
    }

    public void setBlocks(ArrayList<Block> result) {
        this.result = result;
    }

    public ArrayList<Block> getBlocks() {
        return this.result;
    }


    /////
    // Public Methods
    /////

    public void constructWordResult(HTMLParser parser, String[] elements) {

        if( this.description.equals("MultiHit") ) {
            constructMultiHitResults(parser);
        }
        else if( this.description.equals("SingleHit") ) {
            constructSingleHitResult(parser, elements);
        }
        else {
            //Do Nothing
        }

    }

    /////
    // Private Methods
    /////
    private int constructInt(String a) {

        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(a);
        int start = 0;
        int end = 0;
        while( matcher.find() ) {
            start = matcher.start();
            end = matcher.end();
        }

        int result = 0;
        try {
            result = Integer.parseInt(a.substring(start, end));
        }
        catch( NumberFormatException e) {
            result = 0;
        }

        return result;
    }

    private int[] manageMultipleIds(String[] a) {

        int[] returnValue = new int[a.length];
        int iterations;
        for( int i = 0; i < a.length; i++ ) {
            iterations = constructInt(a[i]);
            returnValue[i] = iterations;
        }

        return returnValue;
    }

    private void constructMultiHitResults(HTMLParser parser) {

        Document doc = parser.getDocument();

        this.multiHitDescriptions = new String[doc.select("li").size()];
        String[] tempId = new String[doc.select("li").size()];

        int count = 0;

        for( Element e : doc.select("li") ) {
            this.multiHitDescriptions[count] = e.text();

            tempId[count] = e.getElementsByTag("a").attr("onClick").toString();

            count++;
        }

        this.multiHitIds = manageMultipleIds(tempId);
    }

    private String destroyPointers(String str) {
        String temp = str.substring(str.indexOf(" ")+1, str.length());
        temp = temp.substring(temp.indexOf(" ")+1, temp.length());
        return temp;
    }

    private String destroyPointer(String str) {
        return str.substring(str.indexOf(" ")+1, str.length());
    }

    public String arrayToString(String[] array) {

        StringBuilder builder = new StringBuilder();

        for (String string : array) {
            builder.append(string);
        }
        return builder.toString();

    }
    /*
    public ArrayList<String> constructTableResults(String str) {

      str = destroyPointers(str.substring(str.indexOf(" "), str.length()));

      if( str.contains(".") ) {
        str = destroyPointer(str);
      }

      ArrayList<String> returnList = new ArrayList<String>();

      if(str.contains("/")) {
        String[] split = str.split("");
        for(int i = 1; i < split.length-1; i++) {
      if( !split[i-1].equals("/") && split[i].equals(" ") && !split[i+1].equals("/") ) {
        split[i] = "1234";
      }
        }
        String[] temp = new String[split.length-1];
        for( int i = 0; i < temp.length; i++ ) {
      temp[i] = split[i+1];
        }
        String st = arrayToString(temp);
        String[] tempT = st.split("1234");
        for( int i = 0; i < tempT.length; i++ ) {
      returnList.add(tempT[i]);

        }

      }
      else {
        String[] split = str.split("\\s+");
        for( int i = 0; i < split.length; i++ ) {
      returnList.add(split[i]);
        }
      }

      return returnList;

    }
    */
    private boolean isNotVerb(String str) {

        String[] starters = { "ég", "við", "þú", "þið", "hann", "hún", "það", "þeir", "þær", "þau" };

        for( String string : starters ) {

            if( str.contains(string) ) {
                return true;
            }

        }

        return false;
    }

    private ArrayList<String> constructTableResults(String str) {

        ArrayList<String> firstIteration = new ArrayList<String>();
        ArrayList<String> secondIteration = new ArrayList<String>();

        if( this.title.contains("Sagnorð") ) {
            if( str.contains("Stýfður") ) {
                secondIteration.add(str.substring(str.indexOf(" "), str.length()));
                secondIteration.add("--");
            }
            else if( str.contains(".") ) {
                str = destroyPointer(str);
                if( str.contains("/") ) {
                    String[] split = str.split("");
                    for(int i = 1; i < split.length-1; i++) {
                        if( !split[i-1].equals("/") && split[i].equals(" ") && !split[i+1].equals("/") ) {
                            split[i] = "1234";
                        }
                    }
                    String[] temp = new String[split.length-1];
                    for( int i = 0; i < temp.length; i++ ) {
                        temp[i] = split[i+1];
                    }
                    String st = arrayToString(temp);
                    String[] tempT = st.split("1234");
                    for( int i = 0; i < tempT.length; i++ ) {
                        firstIteration.add(tempT[i]);
                    }

                    for( String string : firstIteration ) {

                        if( string.contains(".") && !string.contains("/") ) {
                            String[] spliti = str.split(" ");
                            for( String rts : spliti ) {
                                secondIteration.add(rts);
                            }
                        }
                        else {
                            if( isNotVerb(str) ) {
                                for( String rts : firstIteration ) {
                                    secondIteration.add(rts);
                                }
                            }
                            else {
                                String[] spl = str.split(" ");
                                for( String rtts : spl ) {
                                    secondIteration.add(rtts);
                                }
                            }
                        }

                    }

                }
                else {
                    String[] split = str.split(" ");
                    for( String rts : split ) {
                        secondIteration.add(rts);
                    }
                }
            }
            else {
                secondIteration.add(str);
            }
        }

        else {
            if( str.contains(".") ) {
                str = str.substring(str.indexOf(" "), str.length());
            }

            if( str.contains("/") ) {
                String[] split = str.split("");
                for(int i = 1; i < split.length-1; i++) {
                    if( !split[i-1].equals("/") && split[i].equals(" ") && !split[i+1].equals("/") ) {
                        split[i] = "1234";
                    }
                }
                String[] temp = new String[split.length-1];
                for( int i = 0; i < temp.length; i++ ) {
                    temp[i] = split[i+1];
                }
                String st = arrayToString(temp);
                String[] tempT = st.split("1234");
                for( int i = 0; i < tempT.length; i++ ) {
                    firstIteration.add(tempT[i]);
                }

                for( String rts : firstIteration ) {

                    if( rts.contains(" ") && !rts.contains("/") ) {
                        String[] splitii = str.split(" ");
                        for( String rtss : splitii ) {
                            secondIteration.add(rtss);
                        }
                    }
                    else {
                        for( String rtsss : firstIteration ) {
                            secondIteration.add(rtsss);
                        }
                    }

                }

            }
            else {
                String[] split = str.split(" ");
                for( String rts : split ) {
                    secondIteration.add(rts);
                }
            }
        }

        ArrayList<String> tempList = secondIteration;
        secondIteration = new ArrayList<String>();

        for( String resultString : tempList ) {
            if( resultString.length() > 0 ) {
                secondIteration.add(resultString);
            }
        }



        return secondIteration;
    }

    private String[] getTableColumns(String blockTitle, String subBlockTitle, String tableTitle) {

        if( this.title.contains("Persónu") ) {
            return new String[] { "", "Eintala", "Fleirtala" };
        }
        if( this.title.contains("Töluorð") ) {
            return new String[] { "", "Karlkyn", "Kvenkyn", "Hvorugkyn" };
        }
        if( this.title.contains("Afturbeygt") ) {
            return new String[] { "", "" };
        }
        if( this.title.contains("Lýsingarorð") ) {
            return new String[] { "", "Karlkyn", "Kvenkyn", "Hvorugkyn" };
        }
        if( this.title.contains("nafnorð") ) {
            return new String[] { "", "án greinis", "með greini" };
        }
        if( this.title.contains("Atviksorð") ) {
            return new String[] { "", "Frumstig", "Miðstig", "Efsta stig" };
        }
        if( this.title.contains("Sagnorð") ) {
            if( blockTitle.contains("Persónuleg notkun - Germynd") || blockTitle.contains("Persónuleg notkun - Miðmynd")  ) {
                if( subBlockTitle.contains("Nafnháttur") ) {
                    return new String[] { "", "" };
                }
                else {
                    return new String[] { "", "Eintala", "Fleirtala" };
                }
            }
            if( blockTitle.contains("Boðháttur") ) {
                return new String[] { "", "Germynd", "Miðmynd" };
            }
            if( blockTitle.contains("Lýsingarháttur nútíðar") ) {
                return new String[] { "", "" };
            }
            if( blockTitle.contains("Sagnbót")) {
                return new String[] { "", "Germynd", "Miðmynd" };
            }
            if( blockTitle.contains("Ópersónuleg notkun - Germynd (Gervifrumlag)")) {
                return new String[] { "", "Eintala", "Fleirtala" };
            }
            if( blockTitle.contains("Lýsingarháttur þátíðar") ) {
                return new String[] { "", "Karlkyn", "Kvenkyn", "Hvorugkyn" };
            }
            if( blockTitle.contains("Ópersónuleg notkun - Miðmynd (Frumlag í þágufalli)") ) {
                return new String[] { "", "Eintala", "Fleirtala" };
            }

        }
        if( this.title.contains("Greinir") ) {
            return new String[] { "", "Karlkyn", "Kvenkyn", "Hvorugkyn" };
        }
        if( this.title.contains("Fornafn") ) {
            return new String[] { "", "Karlkyn", "Kvenkyn", "Hvorugkyn" };
        }

        return new String[] { "", "" };
    }

    private String[] getTableRows(String blockTitle, String subBlockTitle, String tableTitle) {

        if( this.title.contains("Töluorð") || this.title.contains("Afturbeygt") ||
                this.title.contains("Persónu") || this.title.contains("nafnorð") ||
                this.title.contains("Lýsingarorð") ) {
            return new String[] { "", "Nf.", "Þf.", "Þgf.", "Ef." };
        }
        else if( this.title.contains("Sagnorð") ) {
            if( blockTitle.contains("Persónuleg notkun - Germynd") || blockTitle.contains("Persónuleg notkun - Miðmynd") ) {
                if( subBlockTitle.contains("Nafnháttur") ) {
                    return new String[] { "", "" };
                }
                else {
                    return new String[] { "", "1. pers.", "2. pers.", "3. pers." };
                }
            }
            if( blockTitle.contains("Boðháttur") ) {
                return new String[] { "", "Stýfður", "Eintala", "Fleirtala" };
            }
            if( blockTitle.contains("Lýsingarháttur nútíðar") ) {
                return new String[] { "" };
            }
            if( blockTitle.contains("Sagnbót")) {
                return new String[] { "", "" };
            }
            if( blockTitle.contains("Ópersónuleg notkun - Germynd (Gervifrumlag)")) {
                return new String[] { "", "3. pers." };
            }
            if( blockTitle.contains("Lýsingarháttur þátíðar") ) {
                return new String[] { "", "Nf.", "Þf.", "Þgf.", "Ef." };
            }
            if( blockTitle.contains("Ópersónuleg notkun - Miðmynd (Frumlag í þágufalli)") ) {
                return new String[] { "", "1. pers.", "2. pers.", "3. pers." };
            }

        }
        else if( this.title.contains("Greinir") ) {
            return new String[] { "", "Nf.", "Þf.", "Þgf.", "Ef." };
        }
        else if( this.title.contains("Fornafn") ) {
            return new String[] { "", "Nf.", "Þf.", "Þgf.", "Ef." };
        }
        else if( this.title.contains("Atviksorð") ) {
            return new String[] { "", "" };
        }

        return new String[] { "", "" };
    }

    private void constructExceptionBlock(ArrayList<String> rawData) {

        ArrayList<String> rawFirstTable = new ArrayList<String>();
        ArrayList<String> rawSecondTable = new ArrayList<String>();

        String[] exceptionSet = { "Nf", "Þf", "Þgf", "Ef" };

        for( String str : rawData ) {

            if( str.contains(".") ) {
                str = destroyPointers(str);

                if(str.contains("Nf.")) {
                    String[] splitString = str.split(exceptionSet[0]);
                    rawFirstTable.add(splitString[1]);
                    rawSecondTable.add(splitString[2]);
                }
                if(str.contains("Þf.")) {
                    String[] splitString = str.split(exceptionSet[1]);
                    rawFirstTable.add(splitString[1]);
                    rawSecondTable.add(splitString[2]);
                }
                if(str.contains("Þgf.")) {
                    String[] splitString = str.split(exceptionSet[2]);
                    rawFirstTable.add(splitString[1]);
                    rawSecondTable.add(splitString[2]);
                }
                if(str.contains("Ef.")) {
                    String[] splitString = str.split(exceptionSet[3]);
                    rawFirstTable.add(splitString[1]);
                    rawSecondTable.add(splitString[2]);
                }

            }

        }


        ArrayList<String> firstTableContent = new ArrayList<String>();
        ArrayList<String> secondTableContent = new ArrayList<String>();

        for( String str : rawFirstTable ) {
            ArrayList<String> tempRes = constructTableResults(str);

            for( String string : tempRes ) {
                firstTableContent.add(string);
            }
        }

        for( String str : rawSecondTable ) {
            ArrayList<String> tempRes = constructTableResults(str);

            for( String string : tempRes ) {
                secondTableContent.add(string);
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


        this.result.add(newBlock);

    }


    private Tables constructTables(ArrayList<String> rawTables, String[] elements, String blockTitle, String subBlockTitle) {



        String tableTitle = "";
        if( rawTables.get(0).contains(elements[3]) ) {
            tableTitle = destroyPointers(rawTables.get(0));
        }

        String[] tableRow = getTableRows(blockTitle, subBlockTitle, tableTitle);

        String[] tableColumns = getTableColumns(blockTitle, subBlockTitle, tableTitle);

        ArrayList<String> tableResults = new ArrayList<String>();

        for( String str : rawTables ) {
            if( str.contains(elements[4]) ) {

                str = destroyPointers(str);

                ArrayList<String> tempArrayList = constructTableResults(str);

                for( int i = 0; i < tempArrayList.size(); i++ ) {
                    tableResults.add(tempArrayList.get(i));
                }


            }


        }


        return new Tables(tableTitle, tableColumns, tableRow, tableResults);
    }

    private SubBlock constructSubBlock(ArrayList<String> rawSubBlock, String[] elements, String blockTitle) {

        String subBlockTitle = "";
        if( rawSubBlock.get(0).contains(elements[2]) ) {
            subBlockTitle = destroyPointers(rawSubBlock.get(0));
        }

        ArrayList<ArrayList<String>> allTables = new ArrayList<ArrayList<String>>();
        ArrayList<String> oneTable = new ArrayList<String>();
        int counter = 0;

        for( String str : rawSubBlock ) {

            if( str.contains(elements[3]) ) {
                if( counter == 0 ) {
                    oneTable.add(str);
                    counter++;
                }
                else {
                    allTables.add(oneTable);
                    oneTable = new ArrayList<String>();
                    oneTable.add(str);
                    counter = 1;
                }
            }

            if( str.contains(elements[4]) ) {
                oneTable.add(str);
                counter++;
            }

        }

        allTables.add(oneTable);

        ArrayList<Tables> tables = new ArrayList<Tables>();

        for( ArrayList<String> aList : allTables ) {

            Tables newTable = constructTables(aList, elements, blockTitle, subBlockTitle);
            tables.add(newTable);

        }

        return new SubBlock(subBlockTitle, tables);


    }

    private Block constructBlock(ArrayList<String> rawBlock, String[] elements) {

        String blockTitle = "";

        if( rawBlock.get(0).contains(elements[1]) ) {
            blockTitle = destroyPointers(rawBlock.get(0));
        }


        ArrayList<ArrayList<String>> allSubBlocks = new ArrayList<ArrayList<String>>();
        ArrayList<String> oneSubBlock = new ArrayList<String>();
        int counter = 0;

        for( String str : rawBlock ) {

            if( str.contains(elements[2]) ) {
                if( counter == 0 ) {
                    oneSubBlock.add(str);
                    counter++;
                }
                else {
                    allSubBlocks.add(oneSubBlock);
                    oneSubBlock = new ArrayList<String>();
                    oneSubBlock.add(str);
                    counter = 1;
                }
            }

            if( str.contains(elements[3]) ) {
                oneSubBlock.add(str);
                counter++;
            }

            if( str.contains(elements[4]) ) {
                oneSubBlock.add(str);
                counter++;
            }

        }

        allSubBlocks.add(oneSubBlock);

        ArrayList<SubBlock> sB = new ArrayList<SubBlock>();

        for( ArrayList<String> aList : allSubBlocks ) {

            SubBlock newSubBlock = constructSubBlock(aList, elements, blockTitle);
            sB.add(newSubBlock);

        }

        return new Block(blockTitle, sB);

    }

    private void constructBlocks(ArrayList<String> rawData, String[] elements) {

        ArrayList<ArrayList<String>> allBlocks = new ArrayList<ArrayList<String>>();

        ArrayList<String> oneBlock = new ArrayList<String>();

        int counter = 0;

        for( String str : rawData ) {

            if( str.contains(elements[1]) ) {
                if( counter == 0 ) {
                    oneBlock.add(str);
                    counter++;
                }
                else {
                    allBlocks.add(oneBlock);
                    oneBlock = new ArrayList<String>();
                    oneBlock.add(str);
                    counter = 1;
                }
            }

            if( str.contains(elements[2]) ) {
                oneBlock.add(str);
                counter++;
            }

            if( str.contains(elements[3]) ) {
                oneBlock.add(str);
                counter++;
            }

            if( str.contains(elements[4]) ) {
                oneBlock.add(str);
                counter++;
            }

        }

        allBlocks.add(oneBlock);

        for( ArrayList<String> aList : allBlocks ) {

            Block newBlock = constructBlock(aList, elements);
            this.result.add(newBlock);

        }

    }

    private String[] getRuleSet() {

        if( this.title.contains("Sagnorð") ) {
            return new String[] { "th - Germynd", "th - Miðmynd", "tr - Germynd", "tr - Miðmynd", "tr - Nútíð", "tr - Þátíð", "tr - Et. Ft.",
                    "tr - 1. pers", "tr - 2. pers", "tr - 3. pers", "tr - 1.pers", "tr - 2.pers", "tr - 3.pers",
                    "tr - Karlkyn", "tr - Eintala", "tr - Fleirtala" };
        }
        else if( this.title.contains("Lýsingarorð") ) {
            return new String[] { "h2", "h3", "h4", "th", "tr - Nf.", "tr - Þf.", "tr - Þgf.", "tr - Ef." };
        }
        else if( this.title.contains("nafnorð") ) {
            return new String[] { "h2", "th", "tr - Nf.", "tr - Þf.", "tr - Þgf.", "tr - Ef." };
        }
        else if( this.title.contains("Atviksorð") ) {
            return new String[] { "tr - Frumstig", "th - Frumstig", "th - Miðstig", "th - Efsta stig" };
        }
        else if( this.title.contains("Töluorð") ) {
            return new String[] { "h2", "th", "tr - Nf.", "tr - Þf.", "tr - Þgf.", "tr - Ef." };
        }
        else if( this.title.contains("Persónu") ) {
            return new String[] { "h2", "tr - Nf.", "tr - Þf.", "tr - Þgf.", "tr - Ef." };
        }
        else if( this.title.contains("Afturbeygt") ) {
            return new String[] { "h2", "th", "tr - Nf.", "tr - Þf.", "tr - Þgf.", "tr - Ef." };
        }
        else if( this.title.contains("Fornafn") ) {
            return new String[] { "h2", "tr - Nf.", "tr - Þf.", "tr - Þgf.", "tr - Ef." };
        }
        else if( this.title.contains("Greinir") ) {
            return new String[] { "h2", "tr - Nf.", "tr - Þf.", "tr - Þgf.", "tr - Ef." };
        }

        return null;
    }

    private boolean isValidResultString(String str, String[] ruleSets) {

        for( int i = 0; i < ruleSets.length; i++ ) {
            if( str.contains(ruleSets[i]) ) {
                return true;
            }
        }
        return false;

    }

    private void constructSingleHitResult(HTMLParser parser, String[] elements) {

        Document doc = parser.getDocument();

        this.title = doc.getElementsByTag(elements[0]).text();

        try {
            this.warning = doc.getElementsByClass("alert").text();
        }
        catch( NullPointerException e ) {
            //Do nothing
        }


        ArrayList<Element> selectedElements = parser.getSelectedElements();

        ArrayList<String> rawData = new ArrayList<String>();


        for( Element element : selectedElements ) {

            if( element.hasText() ) {
                rawData.add(element.tag().toString() + " - " + element.text());
            }

        }

        String[] ruleSets = getRuleSet();

        ArrayList<String> data = new ArrayList<String>();

        if( this.title.contains("Sagnorð") || this.title.contains("Atviksorð") ) {

            for( String str : rawData ) {

                if( !isValidResultString(str, ruleSets) ) {
                    data.add(str);
                }

            }

        }
        else {

            for( String str : rawData ) {

                if( isValidResultString(str, ruleSets) ) {
                    data.add(str);
                }

            }
        }

        if( (this.title.contains("Fornafn") && (!this.title.contains("Persónu") || !this.title.contains("Afturbeygt") )) || this.title.contains("Greinir") ) {
            constructExceptionBlock(data);
            return;
        }

        constructBlocks(data, elements);


    }
}
