package org.maneau.fastwordssearch;

import org.junit.Test;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;
import static org.maneau.fastwordssearch.WordUtils.unaccent;

public class WordUtilsTest {

    private static final String accents = "È,É,Ê,Ë,Û,Ù,Ï,Î,À,Â,Ô,è,é,ê,ë,û,ù,ï,î,à,â,ô,Ç,ç,Ã,ã,Õ,õ";
    private static final String expected = "E,E,E,E,U,U,I,I,A,A,O,e,e,e,e,u,u,i,i,a,a,o,C,c,A,a,O,o";

    private static final String accents2 = "çÇáéíóúýÁÉÍÓÚÝàèìòùÀÈÌÒÙãõñäëïöüÿÄËÏÖÜÃÕÑâêîôûÂÊÎÔÛ";
    private static final String expected2 = "cCaeiouyAEIOUYaeiouAEIOUaonaeiouyAEIOUAONaeiouAEIOU";

    private static final String accents3 = "Gisele Bündchen da Conceição e Silva foi batizada assim em homenagem à sua conterrânea de Horizontina, RS.";
    private static final String expected3 = "Gisele Bundchen da Conceicao e Silva foi batizada assim em homenagem a sua conterranea de Horizontina, RS.";

    @Test
    public void isNotEmpty_deal_with_null() {
        //noinspection ConstantConditions
        assertFalse(WordUtils.isNotEmptyList(null));
        assertFalse(WordUtils.isNotEmptyList(emptyList()));
        assertTrue(WordUtils.isNotEmptyList(singletonList("1")));
    }

    @Test
    public void replacingAllAccents() {
        assertEquals(expected, unaccent(accents));
        assertEquals(expected2, unaccent(accents2));
        assertEquals(expected3, unaccent(accents3));
    }

    @Test
    public void noReplacingSpecialChar() {
        String special = "hello world! LİKE";
        assertEquals(special, unaccent(special));
    }
}