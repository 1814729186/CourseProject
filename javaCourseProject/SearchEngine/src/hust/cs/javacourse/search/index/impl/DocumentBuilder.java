package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractDocument;
import hust.cs.javacourse.search.index.AbstractDocumentBuilder;
import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.parse.impl.LengthTermTupleFilter;
import hust.cs.javacourse.search.parse.impl.PatternTermTupleFilter;
import hust.cs.javacourse.search.parse.impl.StopWordTermTupleFilter;
import hust.cs.javacourse.search.parse.impl.TermTupleScanner;
import hust.cs.javacourse.search.util.Config;
import hust.cs.javacourse.search.util.StopWords;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * <pre>
 *      DocumentBuilder是Document构造器的实现子类.派生自AbstractDocument
 *          Document构造器的功能应该是由解析文本文档得到的TermTupleStream，产生Document对象.
 * </pre>
 */
public class DocumentBuilder extends AbstractDocumentBuilder {
    /**
     * 停用词过滤器是使能
     */
    static boolean stopWordFilterUse = true;
    /**
     * 正则表达匹配过滤器使能
     */
    static boolean patternFilterUse = true;
    /**
     * 长度过滤器使能
     */
    static boolean lengthFilterUse = true;

    /**
     * 静态代码块，初始化使能信息和设置Config参数
     */
    static{
        Scanner configInput = null;
        //过滤器使能，默认为true
        try {
            //读入配置文件，配置过滤器相关参数信息，不考虑修改Config类代码，故在此尝试读入过滤器使能信息，main函数中读入参数设置
            File configFile = new File(Config.INDEX_DIR+"Config");

            if(configFile.isFile() && configFile.exists()){
                configInput = new Scanner(configFile);
                //循环读入信息
                String buf;
                while((buf = configInput.nextLine())!=null) {
                    buf = buf.trim();
                    //检查首个字符是否为# 注释行，跳过注释行
                    if(buf.equals("")||buf.charAt(0) == '#') continue;
                    //删除行尾注释信息
                    if(buf.indexOf('#')!=-1)
                        buf = buf.substring(0,buf.indexOf('#'));
                    //使能信息匹配
                    if(buf.matches("LengthTermTurpleFilter:\\w+")){//长度过滤器
                        if(buf.substring("LengthTermTurpleFilter:".length()).trim().equalsIgnoreCase("true")) lengthFilterUse=true;
                        else if(buf.substring("LengthTermTurpleFilter:".length()).trim().equalsIgnoreCase("false")) lengthFilterUse=false;
                    }else if(buf.matches("PatternTermTupleFilter:\\w+")) {//正则匹配过滤器
                        if(buf.substring("PatternTermTupleFilter:".length()).trim().equalsIgnoreCase("true")) patternFilterUse=true;
                        else if(buf.substring("PatternTermTupleFilter:".length()).trim().equalsIgnoreCase("false")) patternFilterUse=false;
                    }else if(buf.matches("StopWordTermTuplrFilter:\\w+")) {//停用词过滤器
                        if(buf.substring("StopWordTermTuplrFilter:".length()).trim().equalsIgnoreCase("true")) stopWordFilterUse=true;
                        else if(buf.substring("StopWordTermTuplrFilter:".length()).trim().equalsIgnoreCase("false")) stopWordFilterUse=false;
                    }
                    //过滤器参数设置
                    else if(buf.matches("LengthTermTurpleFilter settings:.+")){//长度信息配置
                        buf = buf.substring("LengthTermTurpleFilter settings:".length()).trim(); //剪切
                        if(buf.equalsIgnoreCase("(default)")) continue; //使用默认信息
                        else{
                            String []buf1 = buf.split(" ");
                            Config.TERM_FILTER_MINLENGTH = Integer.valueOf(buf1[0]);
                            Config.TERM_FILTER_MAXLENGTH = Integer.valueOf(buf1[1]);
                        }
                    }
                    else if(buf.matches("PatternTermTupleFilter settings:.+")) {//正则表达式信息配置
                        buf = buf.substring("PatternTermTupleFilter settings:".length()).trim(); //剪切
                        if(buf.equalsIgnoreCase("(default)")) continue; //使用默认信息
                        else{
                            Config.TERM_FILTER_PATTERN = buf;
                        }
                    }else if(buf.matches("StopWordTermTuplrFilter settings:.+")){
                        buf = buf.substring("StopWordTermTuplrFilter settings:".length()).trim(); //剪切
                        if(buf.equalsIgnoreCase("(default)")) continue; //使用默认信息
                        else{
                            StopWords.STOP_WORDS = buf.split("\\s|,");
                        }
                    }
                }
            }
        }catch (Exception e){

        }finally {
            if (configInput != null) configInput.close();
        }

    }
    /**
     * <pre>
     * 由解析文本文档得到的TermTupleStream,构造Document对象.
     * @param docId             : 文档id
     * @param docPath           : 文档绝对路径
     * @param termTupleStream   : 文档对应的TermTupleStream
     * @return ：Document对象
     * </pre>
     */
    public  AbstractDocument build(int docId, String docPath, AbstractTermTupleStream termTupleStream)throws IOException{
        //将流转化处理为三元组，调用构造函数创建Document对象
        //构建三元组列表对象
        List<AbstractTermTuple> tuples = new ArrayList<AbstractTermTuple>();
        //获得三元组对象并添加到三元组列表中
        AbstractTermTuple tuple;
        while((tuple = termTupleStream.next())!=null){
            //不重复添加三元组
            if(tuples.contains(tuple)) continue;
            tuples.add(tuple);
        }
        //构建实例，返回对象引用
        return (new Document(docId,docPath,tuples));
    }


    /**
     * <pre>
     * 由给定的File,构造Document对象.
     * 该方法利用输入参数file构造出AbstractTermTupleStream子类对象后,内部调用
     *      AbstractDocument build(int docId, String docPath, AbstractTermTupleStream termTupleStream)
     * @param docId     : 文档id
     * @param docPath   : 文档绝对路径
     * @param file      : 文档对应File对象
     * @return          : Document对象
     * </pre>
     */
    public AbstractDocument build(int docId, String docPath, File file){

        //使用工具类处理文件对象
        AbstractTermTupleStream termTupleStream = null;
        AbstractDocument document = null;
        try{
            termTupleStream = new TermTupleScanner(new BufferedReader(new InputStreamReader(new FileInputStream(file))));
            //停用词，装饰者模式
            if(stopWordFilterUse){
                termTupleStream = new StopWordTermTupleFilter(termTupleStream);
            }
            //正则表达过滤器
            if(patternFilterUse){
                termTupleStream = new PatternTermTupleFilter(termTupleStream);
            }
            //长度过滤器
            if(lengthFilterUse){
                termTupleStream = new LengthTermTupleFilter(termTupleStream);
            }
            document = build(docId,docPath,termTupleStream);
        }catch (IOException exception){
            exception.printStackTrace();
        }finally{
            if(termTupleStream!=null)
                termTupleStream.close();
        }
        return document;

    }


}
