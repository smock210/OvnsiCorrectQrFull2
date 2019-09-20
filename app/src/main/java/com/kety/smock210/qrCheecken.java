package com.kety.smock210.ovnsicorrectqrfull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by smock210 on 20.11.2016.
 */

public class qrCheecken {

    private static  String[] english = {"A","B","C","D","E","F","G","H","I","J","K","L","M","O","P",
            "T","X"};

    private static String[] russian = {"А","Б","С","Д","Е","Ф","Г","Н","И","Ж","К","Л","М","О","Р",
            "Т","Х"};


    public a1919 checkQr(String lastScannedCode) {
        String error = "";
        StringBuilder fullError = new StringBuilder();
        StringBuilder strBud = new StringBuilder();
        lastScannedCode = lastScannedCode.toUpperCase();
        String[] str = lastScannedCode.split("\\|");
        StringBuilder buf = new StringBuilder();
        ArrayList<Errors> products = new ArrayList<Errors>();
        boolean bud = false;
        boolean bicF = false;
        boolean rsF = false;
        boolean acRsF = false;



        if (!checkLatin(str[0])) {
            error = "Неправильный кодированный набор";//Анализ ДШК - Кодированный набор должен быть либо ST00011, либо ST00012");
            fullError.append("- алиас " + str[0] + " содержит символы не принадлежащие латинице").append(System.getProperty("line.separator"));
        } else {
            if (!getEncodingQr(str[0])) {
                error = "Неправильный кодированный набор";//Анализ ДШК - Кодированный набор должен быть либо ST00011, либо ST00012");
                fullError.append("- Кодированный набор должен быть либо ST00011, либо ST00012\")").append(System.getProperty("line.separator"));
            }
        }
        if (error.length() > 0) {
            products.add(new Errors(error, fullError.toString(), true));
            error = "";
            fullError.setLength(0);
        }

        Pattern p2 = Pattern.compile("[аА-яЯ]+");
        Matcher m2 = p2.matcher(lastScannedCode);
        if (m2.find()) {
            //scanText.setText("Анализ ДШК");
        } else {
            error = "Неправильная кодировка содержания";
            if (str[0].equals("ST00011")) {
                fullError.append("- Для данного ШК кодировка текста должна быть windows, или кодированный набор ST00012");
            } else {
                fullError.append("- Для данного ШК кодировка текста должна быть utf-8, или кодированный набор ST00011");
            }

        }
        if (error.length() > 0) {
            products.add(new Errors(error, fullError.toString(), true));
            error = "";
            fullError.setLength(0);
        }

        StringBuilder newWord = new StringBuilder("");
        boolean fi = true;

        for(int i=0;i<lastScannedCode.length();i++) {
            fi = true;
            for(int j=0; j<english.length;j++) {
                if(Character.toString(lastScannedCode.charAt(i)).equals(russian[j])) {
                    newWord=newWord.append(english[j]);
                    fi = false;
                }
            }
            if (fi) newWord=newWord.append(Character.toString(lastScannedCode.charAt(i)));
        }

        String newStr = newWord.toString();
        String[] str1 = newStr.split("\\|");
        //бллок проверки обязательных реквизитов
        int name = -1;
        int PersonalAcc = -1;
        int BankName = -1;
        int BIC = -1;
        int CorrespAcc = -1;
        int innIndex = -1;

        int name1 = -1;
        int PersonalAcc1 = -1;
        int BankName1 = -1;
        int BIC1 = -1;
        int CorrespAcc1 = -1;
        int innIndex1 = -1;


        for (int j = 0; j < str.length; j++) {
            if (getTegQr(str[j]).equals("NAME")) {
                name = j;
            }
            if (getTegQr(str[j]).equals("PERSONALACC")) {
                PersonalAcc = j;
            }
            if (getTegQr(str[j]).equals("BANKNAME")) {
                BankName = j;
            }
            if (getTegQr(str[j]).equals("BIC")) {
                BIC = j;
            }
            if (getTegQr(str[j]).equals("CORRESPACC")) {
                CorrespAcc = j;
            }
            if (getTegQr(str[j]).equals("PAYEEINN")) {
                innIndex = j;
            }
        }

        for (int j = 0; j < str1.length; j++) {
            if (getTegQr(str1[j]).equals("NAME")) {
                name1 = j;
            }
            if (getTegQr(str1[j]).equals("PERSONALACC")) {
                PersonalAcc1 = j;
            }
            if (getTegQr(str1[j]).equals("BANKNAME")) {
                BankName1 = j;
            }
            if (getTegQr(str1[j]).equals("BIC")) {
                BIC1 = j;
            }
            if (getTegQr(str1[j]).equals("CORRESPACC")) {
                CorrespAcc1 = j;
            }
            if (getTegQr(str1[j]).equals("PAYEEINN")) {
                innIndex1 = j;
            }
        }

        if (name == -1 && name1 == -1) {
            error = "Ошибка в заполнении обязательного блока";
            fullError.append("- В ШК отсутствует псевдоним Наименование получателя – Name и/или его значение").append(System.getProperty("line.separator"));
        } else {
            if (name > -1) {
                if (getParamQr(lastScannedCode, "NAME").trim().length() < 1) {
                    error = "Ошибка в заполнении обязательного блока";
                    fullError.append("- В ШК отсутствует псевдоним Наименование получателя – Name и/или его значение").append(System.getProperty("line.separator"));
                } else {
                    if (getParamQr(lastScannedCode, "NAME").length() > 160) {
                        error = "Ошибка в заполнении обязательного блока";
                        fullError.append("- Некорректная длина Наименования получателя (не более 160 символов)").append(System.getProperty("line.separator"));
                    }
                }
            }

        }
        if (PersonalAcc == -1  && PersonalAcc1 == -1) {
            error = "Ошибка в заполнении обязательного блока";
            fullError.append("- В ШК отсутствует псевдоним Расчетный счет получателя – PersonalAcc и/или его значение").append(System.getProperty("line.separator"));
        } else {
            if (PersonalAcc > -1) {
                if (getParamQr(lastScannedCode, "PERSONALACC").trim().length() < 1) {
                    error = "Ошибка в заполнении обязательного блока";
                    fullError.append("- В ШК отсутствует псевдоним Расчетный счет получателя – PersonalAcc и/или его значение").append(System.getProperty("line.separator"));
                    rsF = true;
                } else {
                    if (getParamQr(lastScannedCode, "PERSONALACC").trim().length() != 20) {
                        error = "Ошибка в заполнении обязательного блока";
                        fullError.append("- Некорректная длина расчетного счета (должен быть 20 цифр)").append(System.getProperty("line.separator"));
                        rsF = true;
                    }
                }
            }
        }
        if (BankName == -1 && BankName1 == -1) {
            error = "Ошибка в заполнении обязательного блока";
            fullError.append("- В ШК отсутствует псевдоним Банк получатель – BankName и/или его значение").append(System.getProperty("line.separator"));
        } else {
            if (BankName > -1) {
                if (getParamQr(lastScannedCode, "BANKNAME").trim().length() < 1) {
                    error = "Ошибка в заполнении обязательного блока";
                    fullError.append("- В ШК отсутствует псевдоним Банк получатель – BankName и/или его значение").append(System.getProperty("line.separator"));
                }
            }

        }
        if (BIC == -1 && BIC1 == -1) {
            error = "Ошибка в заполнении обязательного блока";
            fullError.append("- В ШК отсутствует псевдоним БИК – BIC и/или его значение").append(System.getProperty("line.separator"));
        } else {
            if (BIC > -1) {
                if (getParamQr(lastScannedCode, "BIC").trim().length() < 1) {
                    error = "Ошибка в заполнении обязательного блока";
                    fullError.append("- В ШК отсутствует псевдоним БИК – BIC и/или его значение").append(System.getProperty("line.separator"));
                } else {
                    if (getParamQr(lastScannedCode, "BIC").trim().length() != 9) {
                        error = "Ошибка в заполнении обязательного блока";
                        fullError.append("- Некорректная длина БИК банка (должен быть 9 цифр)").append(System.getProperty("line.separator"));
                        bicF = true;
                    }
                }
            }
        }
        if (CorrespAcc == -1 && CorrespAcc1 == -1) {
            error = "Ошибка в заполнении обязательного блока";
            fullError.append("- В ШК отсутствует псевдоним Кор. сч. банка – CorrespAcc и/или его значение").append(System.getProperty("line.separator"));
        } else {
            if (CorrespAcc > -1) {
                if (getParamQr(lastScannedCode, "CORRESPACC").trim().length() < 1) {
                    error = "Ошибка в заполнении обязательного блока";
                    if (checkAccBud(getParamQr(lastScannedCode, "PERSONALACC").trim(), getParamQr(lastScannedCode, "BIC").trim(), getParamQr(lastScannedCode, "CORRESPACC").trim())) {
                        fullError.append("- В ШК отсутствует псевдоним Кор. сч. банка – CorrespAcc и/или его значение.").append(System.getProperty("line.separator")).append("Для бюджетных ПУ должен быть 0").append(System.getProperty("line.separator"));
                    } else {
                        fullError.append("- В ШК отсутствует псевдоним Кор. сч. банка – CorrespAcc и/или его значение.").append(System.getProperty("line.separator"));
                    }
                } else {
                    if (checkAccBud(getParamQr(lastScannedCode, "PERSONALACC").trim(), getParamQr(lastScannedCode, "BIC").trim(), getParamQr(lastScannedCode, "CORRESPACC").trim())) {
                        if (!getParamQr(lastScannedCode, "CORRESPACC").equals("0") && !getParamQr(lastScannedCode, "CORRESPACC").equals("00000000000000000000")) {
                            error = "Ошибка в заполнении обязательного блока";
                            fullError.append("- Для бюджетных поставщиков Кор. Счет должен принимать значение 0(ноль)").append(System.getProperty("line.separator"));
                            acRsF  = true;
                        }
                    } else {
                        if (getParamQr(lastScannedCode, "CORRESPACC").length() != 20) {
                            error = "Ошибка в заполнении обязательного блока";
                            fullError.append("- Некорректная длина Кор. сч. банка (должен быть 20 цифр)").append(System.getProperty("line.separator"));
                            acRsF  = true;
                        }
                    }
                }
            }
        }


        if (name == 1 && PersonalAcc == 2 && BankName == 3 && BIC == 4 && CorrespAcc == 5) {
            //scanText.setText("Анализ ДШК");
        } else {
            if (name >-1 && PersonalAcc >-1 && BankName >-1 && BIC >-1 && CorrespAcc >-1) {
                error = "Ошибка в заполнении обязательного блока";
                fullError.append("- Нарушен порядок следования псевдонимов в блоке основных реквизитов, правильный порядок – Name, PersonalAcc, BankName, BIC, CorrespAcc").append(System.getProperty("line.separator"));
            }
        }

        if (error.length() > 0) {
            products.add(new Errors(error, fullError.toString(), true));
            error = "";
            fullError.setLength(0);
        }

        if (innIndex == -1 && innIndex1 == -1) {
            if (checkAccBud(getParamQr(lastScannedCode, "PERSONALACC").trim(), getParamQr(lastScannedCode, "BIC").trim(), getParamQr(lastScannedCode, "CORRESPACC").trim())) {
                error = "В ШК отсутствует PAYEEINN";
                fullError.append("- Для бюджетных ПУ ИНН получателя является обязательным реквизитом").append(System.getProperty("line.separator"));
                bud = true;
            } else {
                error = "В ШК отсутствует PAYEEINN, для корректной работы данного ШК в Сириусе должна быть установлена галочка 'Не проверять ИНН'";
            }
        }
        if (error.length() > 0) products.add(new Errors(error, fullError.toString(), true));
        error = "";
        fullError.setLength(0);

        boolean first = false;
        boolean first2 = false;
        boolean first3 = false;

        if (getParamQr(lastScannedCode, "BIC").length() == 9 && getParamQr(lastScannedCode, "PERSONALACC").length() == 20) {
            if (!checkAcc(getParamQr(lastScannedCode, "BIC").trim(), getParamQr(lastScannedCode, "PERSONALACC").trim())) {
                first2 = true;
                error = "Ошибка ключевания БИК и р/с";
                fullError.append("- Расчетный счет и БИК не прошли проверку по ключеванию").append(System.getProperty("line.separator"));
            }
        }
        if (error.length() > 0) {
            products.add(new Errors(error, fullError.toString(), true));
            error = "";
            fullError.setLength(0);
        }

        if (getParamQr(lastScannedCode, "BIC").length() == 9 && getParamQr(lastScannedCode, "CORRESPACC").length() == 20) {
            if (!checkBicCor(getParamQr(lastScannedCode, "CORRESPACC").trim(), getParamQr(lastScannedCode, "BIC").trim())) {
                first3 = true;
                error = "БИК не соответствует кор. счету";
            }
        }
        if (error.length() > 0) {
            products.add(new Errors(error, fullError.toString(), true));
            error = "";
            fullError.setLength(0);
        }
        Pattern p1 = Pattern.compile("[\n\t\r]+");
        String tempLast = "....."+lastScannedCode+".....";
        Matcher m1 = p1.matcher(tempLast);
        if (m1.find()) {
            error = "Некорректные символы в содержании";//Анализ ДШК - ДШК содержит нечитаемые(\\\t, \\\n, \\\r, ;) символы - позиция с " + m1.start() +"-"+m1.end());
            //products.add(new Errors(error, "- В позиции " + m1.start() + " используется скрытый символ", true));
            while (m1.find()) {
                fullError.append("- <font color=\"#ff0000\">" + tempLast.substring(m1.start()-4,m1.end()+4) + "</font>").append(System.getProperty("line.separator"));

                // buffer = "-" -> "-text-" -> "-text-text-" -> "-text-text-text-"
            }
            products.add(new Errors(error, "- " + fullError.toString(), true));
            fullError.setLength(0);

            error = "";
            //return;
        }

        if (error.length() > 0) products.add(new Errors(error, fullError.toString(), first));
        error = "";


        if (lastScannedCode.indexOf(" =") > -1) {
            error = "- В позиции " + lastScannedCode.indexOf(" =") + " перед/после знака \"=\" не должно быть пробела";
        }
        if (error.length() > 0) {
            products.add(new Errors(error, fullError.toString(), true));
            error = "";
            fullError.setLength(0);
        }
        if (lastScannedCode.indexOf("= ") > -1) {
            error = "- В позиции " + lastScannedCode.indexOf("= ") + " перед/после знака \"=\" не должно быть пробела";
        }
        if (error.length() > 0) {
            products.add(new Errors(error, fullError.toString(), true));
            error = "";
            fullError.setLength(0);
        }
        if (lastScannedCode.indexOf(" |") > -1) {
            error = "- В позиции " + lastScannedCode.indexOf(" |") + " перед/после знака \"=\" не должно быть пробела";
        }
        if (error.length() > 0) {
            products.add(new Errors(error, fullError.toString(), true));
            error = "";
            fullError.setLength(0);
        }
        if (lastScannedCode.indexOf("| ") > -1) {
            error = "- В позиции " + lastScannedCode.indexOf("| ") + " перед/после знака \"=\" не должно быть пробела";
        }
        if (error.length() > 0) {
            products.add(new Errors(error, fullError.toString(), true));
            error = "";
            fullError.setLength(0);
        }


        if (lastScannedCode.indexOf(";") > -1) {
            error = "- Использование знака \";\" запрещено";
        }
        if (error.length() > 0) {
            products.add(new Errors(error, fullError.toString(), true));
            error = "";
            fullError.setLength(0);
        }
        for (int j = 0; j < str.length; j++) {
            first = false;
            //Обязательные параметры
            if (getTegQr(str[j]).equals("BANKNAME")) {
                if (getParamQr(lastScannedCode, "BANKNAME").length() == 0) {
                    //strBud.append("Анализ ДШК - реквизиты BankName не может являтся пустым").append(System.getProperty("line.separator"));
                    //return;
                    first = true;
                } else {
                    if (getParamQr(lastScannedCode, "BANKNAME").length() >45){
                        //Некорректная длина Наименования Банка получателя (не более 45 символов)
                        error = "- Некорректная длина Наименования Банка получателя (не более 45 символов)";
                    }
                }
            }

            if (getTegQr(str[j]).equals("CORRESPACC")) {
                if (getParamQr(lastScannedCode, "CORRESPACC").length() == 0 || first3 || acRsF) {
                    //strBud.append("Анализ ДШК - реквизит CorrespAcc не могжет являтся пустым").append(System.getProperty("line.separator"));
                    //return;
                    first = true;
                }
            }


            if (j > 0) {
                if (!getAliasName(str[j]))
                    error = "- Псевдоним " + getTegQr(str[j]) + " должен содержать буквы только в одной раскладке";
            }

            if (getTegQr(str[j]).equals("UIN")) {
                String UIN = getParamQr(lastScannedCode, "UIN");
                if (UIN.length() != 20 && UIN.length() != 25) {
                    error = "- Некорректная длина УИН (должен быть 20 или 25 цифр)";
                    //return;
                    first = true;
                } else {

                    if (!checkUIN(UIN)) {
                        error = "- Некорректно указана контрольная цифра в УИН/Индекс документа";
                        first = true;
                    }
                }
            }

            if (getTegQr(str[j]).equals("DOCIDX")) {
                String DOCIDX = getParamQr(lastScannedCode, "DOCIDX");
                if (bud) {
                    if (DOCIDX.length() != 20) {
                        error = "- Некорректная длина Индекса (должен быть 20 цифр)";
                        //return;
                        first = true;
                    } else {

                        if (!checkUIN(DOCIDX)) {
                            error = "- Некорректно указана контрольная цифра в УИН/Индекс документа";
                            first = true;
                        }
                    }
                }
            }

            if (getTegQr(str[j]).equals("BIC") && (first2 || first3 || bicF)) {
                first = true;
            }
            if (getTegQr(str[j]).equals("PERSONALACC") && (first2 || rsF)) {
                first = true;
            }

            /////////////////////

            if (getTegQr(str[j]).equals("PAYERINN")) {
                if (!checkInn(getParamQr(lastScannedCode, "PAYERINN")) && getParamQr(lastScannedCode, "PAYERINN").length() != 12) {
                    error = "- Некорректная длина ИНН плательщика (должен быть 12 цифр)";
                    //return;
                    first = true;
                }  else {
                    if (!checkInn(getParamQr(lastScannedCode, "PAYERINN"))) {
                        error = "- Некорректный контрольный разряд в ИНН плательщика";
                        // return;
                        first = true;
                    }
                }
            }
            if (getTegQr(str[j]).equals("PAYEEINN")) {

                if (getParamQr(lastScannedCode, "PAYEEINN").length() != 10 && getParamQr(lastScannedCode, "PAYEEINN").length() != 12) {
                    error = "- Некорректная длина ИНН получателя (должен быть 10 или 12 цифр)";
                    // return;
                    first = true;
                } else {
                    if (!checkInn(getParamQr(lastScannedCode, "PAYEEINN"))) {
                        error = "- Некорректный контрольный разряд в ИНН получателя";
                        // return;
                        first = true;
                    }
                }
            }

            if (getTegQr(str[j]).equals("SUM")) {
                String SUM = getParamQr(lastScannedCode, "SUM");
                if (SUM.indexOf(".") > -1 || SUM.indexOf(",") > -1) {
                    error = "- В псевдониме SUM должна отражаться сумма в копейках без использования каких-либо разделителей";
                    //return;
                    first = true;
                }
                if (SUM.indexOf("-") > -1) {
                    error = "- Сумма в ДШК не может быть отрицательной";
                    //return;
                    first = true;
                }
            }

            if (getTegQr(str[j]).equals("KPP")) {
                String KPP = getParamQr(lastScannedCode, "KPP");
                if (KPP.length() != 9) {
                    error = "- Некорректная длина КПП получателя (должен быть 9 цифр)";
                    //return;
                    first = true;
                }
            }
            if (getTegQr(str[j]).equals("CBC")) {
                String CBC = getParamQr(lastScannedCode, "CBC");
                if (CBC.length() != 20) {
                    error = "- Некорректная длина КБК (должен быть 20 цифр)";
                    //return;
                    first = true;
                }
            }

            if (!getTegQr(str[j]).equals("CBC") && (getTegQr(str[j]).equals("СВС") || getTegQr(str[j]).equals("КБК") || getTegQr(str[j]).equals("KBK") || getTegQr(str1[j]).equals("CBC") || getTegQr(str1[j]).equals("KBK"))) {
                    error = "- Алиас КБК должен писаться строга литинскими буквами CBC";
                    //return;
                    first = true;
            }
            if (getTegQr(str[j]).equals("OKTMO")) {
                String OKTMO = getParamQr(lastScannedCode, "OKTMO");
                if (OKTMO.length() != 8 && OKTMO.length() != 11) {
                    error = "- Некорректная длина ОКТМО (должен быть 8 или 11 цифр)";
                    //return;
                    first = true;
                }
            }
            if (error.length() > 0) products.add(new Errors(error, fullError.toString(), first));
            error = "";
            String[] chars = str[j].split("=");



            if (chars.length > 1) {
                m1 = p1.matcher(chars[0]);
                if (m1.find()) {

                    buf.append("<b>");
                    if (m1.start() > 0) {
                        buf.append((" " + chars[0]).substring(0, m1.start() - 1));

                        buf.append("<font color=\"#ff0000\">");
                        buf.append((" " + chars[0] + " ").substring(m1.start() - 1, m1.start() + 1));
                    } else {
                        buf.append(" ");

                        buf.append("<font color=\"#ff0000\">");
                        buf.append((" " + chars[0] + " ").substring(0, m1.start() + 1));
                    }
                    buf.append("</font>");
                    buf.append(( chars[0] +" ").substring(m1.start()+1));
                    buf.append("=</b>");
                    //return;
                } else  {buf.append("<b>" + chars[0] + "=</b>");}

                if (first) {
                    buf.append("<font color=\"#ff0000\">" + chars[1] + " </font>").append("<br/>");
                } else {

                    m1 = p1.matcher(chars[1]);
                    if (m1.find()) {


                        buf.append((" " + chars[1]).substring(0,m1.start()).trim());
                        buf.append("<font color=\"#ff0000\">");
                        buf.append((" " + chars[1] +" ").substring(m1.start(),m1.start()+3).trim());
                        buf.append("</font>");
                        buf.append(( chars[1] +" ").substring(m1.start()+2).trim());
                        buf.append("<br/>");
                        //return;
                    } else {buf.append(chars[1]).append("<br/>");}
                }
            } else {
                buf.append("<b>" + str[j] + "</b><br/>");
            }

        }
        a1919 a = new a1919(products, buf);
        return a;
    }
    private boolean checkLatin(String s) {
        Pattern p = Pattern.compile("[aA-zZ0-9]*");
        Matcher m = p.matcher(s);
        if (!m.matches() ){
            return false;
        } else {
            return true;
        }
    }

    private boolean checkAccBud(String rs, String bic, String AcRs) {
        String[] scheta = new String[]{"40101","40302","405012","405034","406011","406013","406034","407011","407013", "407034"};
        String[] bica = new String[]{"000","001","002"};
        if (rs.length() != 20) return false;
        if (bic.length() != 9) return false;
        ArrayList<String> arrayListS = new ArrayList<String>(Arrays.asList(scheta));
        ArrayList<String> arrayListB = new ArrayList<String>(Arrays.asList(bica));
        String rs_5 = rs.substring(0,5);
        String rs_6 = rs.substring(0,5)+rs.substring(13,14);
        String b = bic.substring(6);
        if ((arrayListS.indexOf(rs_5)>-1 || arrayListS.indexOf(rs_6)>-1) && arrayListB.indexOf(b)>-1){
            return true;
        }
        return false;
    }




    private boolean checkBicCor(String acRs, String bic) {
        String bic_5 = bic.substring(bic.length() - 3);
        String acRs_6 = acRs.substring(acRs.length() - 3);
        if (bic_5.equals(acRs_6)){
            return true;
        }
        return false;
    }


    private boolean checkUIN(String uin) {
        int first;
        int razn=0;
        int k =0;
        char[] bicChar = uin.toCharArray();
        for (int i = 0; i<uin.length()-1; i++){
            k++;
            razn += Integer.parseInt(Character.toString(bicChar[i])) * k;
            if (k>9) k=0;
        }

        razn = razn % 11;
        if (razn>9){
            razn = 0;
            k = 2;
            for (int i = 0; i<uin.length()-1; i++){
                k++;
                razn += Integer.parseInt(Character.toString(bicChar[i])) * k;
                if (k>9) k=0;
            }
            first = razn % 11;

        } else {
            first = razn;
        }
        if (Character.toString(bicChar[uin.length()-1]).equals(Integer.toString(first))) return true;

        return false;
    }

    public boolean getEncodingQr(String qr) {
        if (qr.equals("ST00011") || qr.equals("ST00012")){
            return true;
        }
        return false;
    }

    public String getParamQr(String str, String teg){
        String[] sQr = str.split("\\|");
        for (int i=1; i< sQr.length; i++){
            String[] chars = sQr[i].split("=");
            if (chars.length>1){
                if (chars[0].equals(teg)){
                    return chars[1];}
            }
        }
        return "";
    }

    public String getTegQr(String str){
        str = " " + str +" ";
        String[] chars = str.split("=");
        if (chars.length>1)  return chars[0].trim();
        else return  "";
    }
    public boolean getAliasName(String str){
        str = " " +str +" ";
        String[] chars = str.split("=");

        Pattern p = Pattern.compile("[aA-zZ0-9]*");
        Pattern p2 = Pattern.compile("[аА-яЯёЁ0-9]*");
        if (chars.length>1){

            Matcher m = p.matcher(chars[0].trim());
            Matcher m2 = p2.matcher(chars[0].trim());
            if (!m.matches() && !m2.matches()){

                return false;
            } else {
                return true;
            }
        }
        return false;

    }

    public boolean checkAcc(String bic, String acc){
        if (bic.length() != 9 || acc.length()!=20) return false;
        int[] m = new int[23];
        char[] bicChar = bic.toCharArray();
        char[] accChar = acc.toCharArray();

        try {
            String bicEnd = bic.substring(6);
            if (bicEnd.equals("001") || bicEnd.equals("000") || bicEnd.equals("002")){
                m[0] = 0;
                m[1] = Integer.parseInt(Character.toString(bicChar[4]));
                m[2] = Integer.parseInt(Character.toString(bicChar[5]));
            } else {
                m[0] = Integer.parseInt(Character.toString(bicChar[6]));
                m[1] = Integer.parseInt(Character.toString(bicChar[7]));
                m[2] = Integer.parseInt(Character.toString(bicChar[8]));
            }
            m[3] = Integer.parseInt(Character.toString(accChar[0]));
            m[4] = Integer.parseInt(Character.toString(accChar[1]));
            m[5] = Integer.parseInt(Character.toString(accChar[2]));
            m[6] = Integer.parseInt(Character.toString(accChar[3]));
            m[7] = Integer.parseInt(Character.toString(accChar[4]));
            m[8] = Integer.parseInt(Character.toString(accChar[5]));
            m[9] = Integer.parseInt(Character.toString(accChar[6]));
            m[10] = Integer.parseInt(Character.toString(accChar[7]));
            m[11] = Integer.parseInt(Character.toString(accChar[8]));
            m[12] = Integer.parseInt(Character.toString(accChar[9]));
            m[13] = Integer.parseInt(Character.toString(accChar[10]));
            m[14] = Integer.parseInt(Character.toString(accChar[11]));
            m[15] = Integer.parseInt(Character.toString(accChar[12]));
            m[16] = Integer.parseInt(Character.toString(accChar[13]));
            m[17] = Integer.parseInt(Character.toString(accChar[14]));
            m[18] = Integer.parseInt(Character.toString(accChar[15]));
            m[19] = Integer.parseInt(Character.toString(accChar[16]));
            m[20] = Integer.parseInt(Character.toString(accChar[17]));
            m[21] = Integer.parseInt(Character.toString(accChar[18]));
            m[22] = Integer.parseInt(Character.toString(accChar[19]));

            int itogo = little(little(m[0],7)+little(m[1],1)+little(m[2],3)+little(m[3],7)+little(m[4],1)+
                    little(m[5],3)+little(m[6],7)+little(m[7],1)+little(m[8],3)+little(m[9],7)+little(m[10],1)+little(m[11],3)+
                    little(m[12],7)+little(m[13],1)+little(m[14],3)+little(m[15],7)+little(m[16],1)+little(m[17],3)+
                    little(m[18],7)+little(m[19],1)+little(m[20],3)+little(m[21],7)+little(m[22],1),1);


            if (itogo == 0) return true; else return false;
        } catch (Exception e) {
            return false;
        }

        //return true;
    }

    public boolean checkInn(String inn){
        int[] k_10 = new int[]{2,4,10,3,5,9,4,6,8};
        int[] k_12_1 = new int[]{3,7,2,4,10,3,5,9,4,6,8};
        int[] k_12_2 = new int[]{7,2,4,10,3,5,9,4,6,8};
        try {
            Long.parseLong(inn);
            if (inn.length()!=10 && inn.length()!=12){
                return false;
            }
            if (inn.length() == 10){
                int sum = 0;
                char[] chars = inn.toCharArray();
                for (int i=0; i< 9; i++){
                    sum += Integer.parseInt(Character.toString(chars[i])) * k_10[i];
                }

                int ost = sum % 11;
                if (ost==10) ost = 0;
                if (Integer.parseInt(Character.toString(chars[9])) != ost){
                    return false;
                }
            }
            boolean first = false;
            if (inn.length()==12){
                int sum = 0;
                char[] chars = inn.toCharArray();
                for (int i=0; i< 11; i++){
                    sum += Integer.parseInt(Character.toString(chars[i])) * k_12_1[i];
                }
                int ost = sum % 11;
                if (ost==10) ost = 0;
                if (Integer.parseInt(Character.toString(chars[9])) != ost){
                    return false;
                }
                sum = 0;
                for (int i=0; i< 10; i++){
                    sum += Integer.parseInt(Character.toString(chars[i])) * k_12_2[i];
                }
                ost = sum % 11;
                if (ost==10) ost = 0;
                if (Integer.parseInt(Character.toString(chars[9])) != ost){
                    return false;
                }
            }
        } catch (Exception e){
            return false;
        }
        return true;
    }

    public int little(int n1, int n2){
        int s = n1 * n2;
        String s2 = Integer.toString(s);
        char[] chars = s2.toCharArray();
        return Integer.parseInt(Character.toString(chars[chars.length - 1]));
    }


}
