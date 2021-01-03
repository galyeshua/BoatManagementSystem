package bms.utils.xml;

import bms.exception.General;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Convertor {

    public static boolean isFileExists(String filePath){
        File xmlFile = new File(filePath);
        return xmlFile.exists();
    }

    public static String stringFromXmlFilePath(String filePath){
        File xmlFile = new File(filePath);

        if (!xmlFile.getName().endsWith(".xml"))
            throw new General.IllegalFileTypeException();

        String content = null;
        try {
            content = new Scanner(xmlFile).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            throw new General.FileNotFoundException();
        }

        return content;
    }


    public static void saveXmlFromString(String xmlContent, String filePath) throws IOException {
        File xmlFile = new File(filePath);

        if (xmlFile.exists())
            throw new General.FileAlreadyExistException();
        else
            xmlFile.createNewFile();

        try(Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(xmlFile), StandardCharsets.UTF_8))){
            writer.write(xmlContent);
        }
    }
}
