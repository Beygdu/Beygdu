package is.arnastofnun.utils;

import android.util.Log;

/**
 * Class Bstring
 * A special string that helps error handling in the construction of the WordResult class
 * Used by BBeautifier.class
 *
 * @author Arnarjons
 */
public class Bstring {

    private String data;

    public Bstring() {
        data = "";
    }

    public Bstring(Object o) {

        try {
            data = (String) o;
        }
        catch (Exception e) {
            data = null;
            //Log.w("Bstring constructor", "Failed to cast object to Bstring");
        }

    }

    /**
     * get(Object o)
     * @return Bstring as a regular java string object
     */
    public String get() {
        return data;
    }

    /**
     * add(Object o)
     * @param o Object to be added to the Bstring
     * @return a new Bstring if successful
     */
    public Bstring add(Object o) {

        try {
            data += (String) o;
        }
        catch (Exception e) {
            //Log.w("Bstring add", "Failed to add object to Bstring");
        }

        return this;

    }

    /**
     * remFirst()
     * @return The Bstring without its first entity
     */
    public Bstring remFirst() {

        try {
            data = data.substring(1, data.length());
        }
        catch (Exception e) {
            //Log.w("Bstring remFirst", "Failed to remove first entity of Bstring");
        }

        return this;
    }

    /**
     * remFirst(Object o)
     * @param o Object
     * @return The Bstring with every entity before (and including) o removed
     */
    public Bstring remFirst(Object o) {

        try {
            int index = data.indexOf((String) o);
            data = data.substring(index+1, data.length());
        }
        catch (Exception e) {
            //Log.w("Bstring remFirst", "Failed to remove everything up to (and including) the selected entity");
        }

        return this;
    }

    /**
     * remLast()
     * @return The Bstring without its last entity
     */
    public Bstring remLast() {

        try {
            data = data.substring(0, data.length()-1);
        }
        catch (Exception e) {
            //Log.w("Bstring remLast", "Failed to remove last entity of Bstring");
        }

        return this;
    }

    /**
     * remLast(Object o)
     * @param o Object
     * @return The Bstring with every entity after (and including) o removed
     */
    public Bstring remLast(Object o) {

        try {
            int index = data.indexOf((String) o);
            data = data.substring(0, index);
        }
        catch (Exception e) {
            //Log.w("Bstring remLast", "Failed to remove everything after (and including) the selected entity");
        }

        return this;
    }

    /**
     * cont(Object o)
     * @param o Object
     * @return True if the Bstring contains o, false otherwise
     */
    public boolean cont(Object o) {

        try {
            return data.contains((String) o);
        }
        catch (Exception e) {
            //Log.w("Bstring cont", "Object o not recognized");
            return false;
        }
    }

    /**
     * split(Object o)
     * @param o Object
     * @return Array of Bstrings split on o
     */
    public Bstring[] split(Object o) {
        try {
            String[] splits = data.split((String) o);
            Bstring[] rsplit = new Bstring[splits.length];

            int count = 0;
            for(String s : splits) {
                rsplit[count++] = new Bstring(s);
            }
            return rsplit;
        }
        catch (Exception e) {
            //Log.w("Bstring split", "Failed to split the Bstring into an array");
            return null;
        }
    }

    /**
     * equ(Object o)
     * @param o Object
     * @return True of o equals the Bstring, false otherwise
     */
    public boolean equ(Object o) {
        try {
            return data.equals((String) o);
        }
        catch (Exception e) {

            try {
                Bstring temp = (Bstring) o;
                return data.equals(temp.get());
            }
            catch (Exception f) {
                //Log.w("Bstring equ", "Failed to recognize object o");
                return false;
            }

        }
    }

    /**
     * arrayToBstring
     * @param o Object array
     * @return Bstring containing o
     */
    public static Bstring arrayToBstring(Object[] o) {
        Bstring rString = new Bstring();

        try {
            for( String s : (String[]) o) {
                rString.add(s);
            }
        }
        catch (Exception e) {
            //Log.w("Bstring arrayToBstring", "Failed to recognize object array");
            return null;
        }

        return rString;
    }

    /**
     * len(Bstring b)
     * @param b Bstring
     * @return length of the Bstring, -1 if it does not contain anything
     */
    public int len() {
        try {
            return data.length();
        }
        catch (Exception e) {
            //Log.w("Bstring len", "The Bstring does not contain anything");
            return -1;
        }
    }

}
