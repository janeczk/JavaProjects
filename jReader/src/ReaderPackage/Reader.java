package ReaderPackage;
import java.io.IOException;

/**
 * Class contains metdhods to read characters, int-s, double-s and hexadecimal values
 * All functions reads characters and calculates the proper values
 * @author MSI
 *
 */
public class Reader
{
    private static char mC = '\0';
    private static boolean ungetted = false;



    //--- PUBLICZNE i nie trzeba obiektu klasy aby je wywolac
    /**
     * Reads one character from the input stream System.in
     * @return  returns read character
     * or @return one previous read char
     * only one char could be ungetted
     * in calse of IOException returns character with code 0
     */
    public static char getChar(){
        try {
            if (!ungetted) {
                mC = (char)System.in.read();
            } else {
                ungetted = false;
            }
        }catch(IOException e){
            mC = '\0';
        }
        return mC;
    }

    /**
     * Returns one character to the input for reloading. Only one char could be returned!
     * @param c - zwracany znak do ponownego odczytu
     */

    public static void ungetChar( char c ) {
        mC = c;
        ungetted = true;
    }
    // nic nie zwraca

//======================================
    /**
     * Reads the integer value written in char case
     * @return the value of integer  number
     */
    public static int readInt () throws IOException{
        boolean sign = getSign();
        int result = readNum();
        if (sign) {
            result = -result;
        }
        return result;
    }

    /**
     * Reads the double value written in char case
     * @return the value of double  num
     */
    public static double readDouble() throws IOException {
        boolean sign = getSign();
        int result = readNum();
        char c = getChar();
        if (c != '.') {
            ungetChar(c);
            return (double)result;
        } else {
            double res = 0.0;
            for(double coef = 1.0; isDecDigit(c = getChar()); res += (coef *= 0.1) * (double)charDecDigit2Int(c));
            ungetChar(c);
            return sign ? (double)(-result) - res : (double)result + res;
        }
    }

    /**
     * Reads the hexadecimal value written in char case.
     * @throws IOException in case of wrong hexadecimal number prefix 0X, 0x
     *         IOException( "Wrong hexadecimal prefix! Shoud be 0x or 0X" );
     * @return value of proper hexadecimal value
     */
    public static int readHex()  throws IOException {
        skipSpaces();
        char c = getChar();
        if( c != '0' ){
            ungetChar( c );
            throw new IOException("Wrong hexadecimal prefix! Shoud be 0x or 0X");
        }
        c = getChar();
        if( c != 'x' && c!= 'X' ) {
            ungetChar( c );
            throw new IOException("Wrong hexadecimal prefix! Shoud be 0x or 0X");
        }
        int result = 0;
        for(result = 0; isHexDigit(c = getChar()); result = result * 16 + (isDecDigit(c) ? charDecDigit2Int(c) : charHexDigit2Int(c))) ;
        ungetChar(c);
        return result;
    }

    //================ P R I V A T E INTERFACE ==========================================
    private static int readNum() throws IOException {
        skipSpaces();
        char c = getChar();
        if (!isDecDigit(c)) {
            ungetChar(c);
            throw new IOException("IS_NOT_DEC");
        } else {
            ungetChar(c);
            int result;
            for(result = 0; isDecDigit(c = getChar()); result = 10 * result + charDecDigit2Int(c));
            ungetChar(c);
            return result;
        }
    } // czyta liczbe calkowita ze znakiem (pomija biale znaki przed zznakiem)

    private static boolean getSign(){
        skipSpaces();
        char c = getChar();
        if(c=='-'){
            return true;
        }else {
            ungetChar(c);
            return false;
        }
    }  // return true or false; true - in case '-' (pomija biale znaki przed)

    private static void skipSpaces(){
        char c;
        for(c = getChar(); c == ' ' || c == '\t' || c == '\n'; c = getChar());
        ungetChar(c);
    } // nic nie zwraca
    /// pomija biale znaki (wszystkie: spacje tabulatory, nowe linie - oba zanki)


    private static boolean isDecDigit( char c ){
        return ( c>='0' && c<='9' );
    }
    private static boolean isHexDigit( char c ) {
        return ( isDecDigit( c ) || isHexLetter( c ) ) ;
    }
    private static char upperCase( char c ) {
        return ( c>='a' && c <='f' ) ? (char)( c-32 ) : c ;
    }
    private static boolean isHexLetter( char c ) {
        return ( c >='A' && c <='F' ) || ( c>= 'a' && c<= 'f' );
    }
    private static int charDecDigit2Int( char c ) {
        return c-'0' ;
    }
    private static int charHexDigit2Int( char c ){
        return charDecDigit2Int( upperCase( c ) ) - 7;
    }
}
