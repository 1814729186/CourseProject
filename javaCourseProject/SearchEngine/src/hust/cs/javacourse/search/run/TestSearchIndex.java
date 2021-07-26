package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.Sort;
import hust.cs.javacourse.search.query.impl.IndexSearcher;
import hust.cs.javacourse.search.query.impl.SimpleSorter;
import hust.cs.javacourse.search.util.Config;

import javax.swing.plaf.nimbus.AbstractRegionPainter;
import java.io.*;
import java.util.Scanner;

/**
 * 测试搜索
 */
public class TestSearchIndex {
    /**
     *  搜索程序入口
     * @param args ：命令行参数
     * @throws IOException : 可能抛出的IO异常
     */
    public static void main(String[] args)throws IOException{
        //单个词查询测试
        /*Sort simpleSorter = new SimpleSorter();
        String indexFile = Config.INDEX_DIR+"index.dat";
        IndexSearcher indexSearcher = new IndexSearcher();
        indexSearcher.open(indexFile);

        AbstractHit[] hits = indexSearcher.search(new Term("coronavirus"),simpleSorter);
        for(AbstractHit hit:hits){
            System.out.println("---------------------------------");
            System.out.println(hit);
        }*/
        Sort simpleSorter = new SimpleSorter();
        String indexFile = Config.INDEX_DIR+"index.dat";
        IndexSearcher indexSearcher = new IndexSearcher();
        indexSearcher.open(indexFile);
        //读取检索词文件，挨个查询相关信息，并将查新结果输出到文件
        FileInputStream searchWordsInput = null;
        PrintWriter searchResultOutput = null;
        File searchFile = new File(Config.PROJECT_HOME_DIR +"/text/用于检索的测试词/用于检索的测试词.txt");
        File resultFile = new File(Config.INDEX_DIR+"search_result.txt");
        try{
            if(searchFile.isFile() && searchFile.exists()){
                searchWordsInput = new FileInputStream(searchFile);
                searchResultOutput = new PrintWriter(resultFile);
                byte[]bufBytes = new byte[(int)searchFile.length()];
                String buf = new String();
                searchWordsInput.read(bufBytes);    //读入整个文件
                buf = new String(bufBytes).trim();
                String[] searchWords = buf.split("\\s+");//用空格、换行符等进行分割
                for(String searchword:searchWords){
                    //int length = searchword.length();
                    if (searchword.startsWith("\uFEFF")) {
                        searchword = searchword.substring(1);
                    }
                    AbstractHit[] hits = indexSearcher.search(new Term(searchword),simpleSorter);
                    System.out.println("****查找词:"+searchword+"****");
                    searchResultOutput.println("****查找词:"+searchword+"****");
                    for(AbstractHit hit:hits){
                        System.out.println("---------------------------------");
                        System.out.println(hit);
                        searchResultOutput.println("---------------------------------");
                        searchResultOutput.println(hit);
                    }
                    System.out.println();
                    searchResultOutput.println();
                }

            }else{
                System.out.println("检索文件不存在");
            }
        }catch(Exception e){
            System.out.println("检索词文件读取异常");
        }finally {
            if(searchWordsInput != null) searchWordsInput.close();
            if(searchResultOutput!=null) searchResultOutput.close();
        }

    }
}
